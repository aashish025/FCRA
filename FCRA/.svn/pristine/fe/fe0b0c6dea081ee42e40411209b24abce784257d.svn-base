package dao.services.dashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utilities.InformationException;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List2;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;
import dao.reports.RedFlaggedRcnsDao;

public class RegistrationSuspensionDao extends BaseDao<ProjectRequest, String, String>{
	private String appId;
	private String searchString;
	private String searchFlag;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
	
	public RegistrationSuspensionDao(Connection connection) {
		super(connection);
	}	

	public List<AbstractRequest> getApplicationListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		String queryField=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		if(searchFlag.equals("name")){
			queryField="UPPER(ASSO_NAME) LIKE ('%' || ? || '%')";
		}else if(searchFlag.equals("id")){
			queryField="(RCN=?)";
		}	
		query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS  ,CASE  WHEN TRUNC(VALID_TO) >= TRUNC(sysdate) THEN 'N' ELSE 'Y' END AS expired FROM FC_INDIA A  WHERE "+queryField+"");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(searchFlag.equals("name")){
			statement.setString(1, searchString);
		}else if(searchFlag.equals("id")){
			statement.setString(1, searchString);			
		}
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}		
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(searchFlag.equals("name")){
				statement.setString(1, searchString);										
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
			}else if(searchFlag.equals("id")){
				statement.setString(1, searchString);														
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
			}
			
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		String statusDesc=null;
		while(rs.next()) {	
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} 
			else if(rs.getString(4) != null && rs.getString(4).equals("3")) {
				statusDesc="SUSPENDED";
			}else if(rs.getString(5) != null && rs.getString(5).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),statusDesc,"","","","","","","",""));			
		}
		return applicationList;
	}
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("applicationId")) {
				orderbyClause.append(" ORDER BY APPLICATION_ID");
			}else if(sortColumn.equals("tempFileNo")) {
				orderbyClause.append(" ORDER BY TEMP_FILENO");
			}else if(sortColumn.equals("sectionFileNo")) {
				orderbyClause.append(" ORDER BY SECTION_FILENO");
			}else if(sortColumn.equals("submissionDate")) {
				orderbyClause.append(" ORDER BY SUBMISSION_DATE");
			}else if(sortColumn.equals("serviceName")) {
				orderbyClause.append(" ORDER BY SERVICE_CODE");
			}
			if(orderbyClause.equals("") == false) {
				if(sortOrder != null && sortOrder.equals("") == false) {
					if(sortOrder.equalsIgnoreCase("ASC")) {
						order.append("ASC");
					}
					else if(sortOrder.equalsIgnoreCase("DESC")) {
						order.append("DESC");
					}
				}
				if(order.toString().equals("") == false) {
					orderbyClause.append(" "+order);
				}
				else {
					orderbyClause = null;
				}
			}
		}
		if(orderbyClause != null && orderbyClause.equals("") == false)
			query.append(orderbyClause);
		
		StringBuffer queryForPaging = null;
		if(pageNum == null || recordsPerPage == null)
			queryForPaging = query;
		else
			queryForPaging = new StringBuffer("WITH T1 AS ("+query+"), "
					+ "T2 AS (SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	public void getApplicationDetails(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	StringBuffer query, query1 =null;ResultSet rs=null;
		query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
				+ "(SELECT NATURE_DESC FROM TM_NATURE WHERE NATURE_CODE=A.ASSO_NATURE),B.NEW_OLD,B.BANK_NAME,B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
				+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,CASE  WHEN TRUNC(VALID_TO) >= TRUNC(sysdate) THEN 'N' ELSE 'Y' END AS expired FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");		
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		rs=statement.executeQuery();	
		String statusDesc=null;
		String assoNewOldFlag=null;
		String bankNewOldFlag=null;
		String assoAddress=null;
		String bankAdress=null;
		if(rs.next()) {		
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} 
			else if(rs.getString(4) != null && rs.getString(4).equals("3")) {
				statusDesc="SUSPENDED";
			}else if(rs.getString(20) != null && rs.getString(20).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			assoNewOldFlag=rs.getString(10);
			if(assoNewOldFlag.equals("N")){
				assoAddress=rs.getString(5)+", "+rs.getString(6);
			}else if(assoNewOldFlag.equals("O")){
				assoAddress=rs.getString(7)+", "+rs.getString(8)+", "+rs.getString(9);
			}
			bankNewOldFlag=rs.getString(12);
			if(bankNewOldFlag != null) {
				if(bankNewOldFlag.equals("N")){
					bankAdress=rs.getString(13)+"<br/>"+rs.getString(14)+", "+rs.getString(15)+"<br/>Account No.: "+rs.getString(19);
				}else if(bankNewOldFlag.equals("O")){
					bankAdress=rs.getString(13)+"<br/>"+rs.getString(16)+", "+rs.getString(17)+", "+rs.getString(18)+"<br/>Account No.: "+rs.getString(19);
				}
			}
			query1 = new StringBuffer("SELECT STATUS FROM T_REGISTRATION_STATUS_HISTORY WHERE RCN = ? AND STATUS_DATE = (SELECT MAX(STATUS_DATE) FROM "
					+ "T_REGISTRATION_STATUS_HISTORY WHERE RCN = ?)");
			PreparedStatement ps = connection.prepareStatement(query1.toString());
			ps.setString(1, appId);
			ps.setString(2, appId);
			ResultSet rs1 = ps.executeQuery();
			String history_status = null;
			if(rs1.next()) {		
				 history_status = rs1.getString(1);
			}
		   applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),statusDesc,assoAddress,
				   rs.getString(11),"",bankAdress,"","","","",history_status));			
		}else{			
			throw new ValidationException("Registration Number <b>"+appId+"</b> doesn't exist.Please search with valid registration number.");
		}		
	}
	/*public List<List2> getCancellationReasonList() throws Exception{
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
		query = new StringBuffer("SELECT REASON_ID,REASON_DESC FROM TM_REGN_CANCELLATION_REASONS WHERE RECORD_STATUS=0");		
		statement = connection.prepareStatement(query.toString());				
		rs=statement.executeQuery();	
		List<List2> list=new ArrayList<List2>();
		while(rs.next()){
			list.add(new List2(rs.getString(1),rs.getString(2)));
		}
		return list;
	}*/
	public void cancelApplicationDetails(String appId,String remark,String user,String officeCode) throws Exception{
		PreparedStatement statement=null;	StringBuffer query =null;
		// Updating FC_INDIA for Registration Cancellation
		query = new StringBuffer("UPDATE FC_INDIA SET CURRENT_STATUS=? WHERE RCN=?");		
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, "3");		
		statement.setString(2, appId);
		statement.executeUpdate();
		statement.close();
		query=new StringBuffer("INSERT INTO T_REGISTRATION_STATUS_HISTORY(RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY) "
				+ "VALUES(?,?,?,sysdate,?)");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);		
		statement.setString(2, "3");
		statement.setString(3, remark);		
		statement.setString(4, user);	
		statement.executeUpdate();
		statement.close();
		StringBuffer querychecking=new StringBuffer("SELECT * FROM T_RED_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(querychecking.toString());	
		statement.setString(1, appId);	
		ResultSet	rs1=statement.executeQuery();
		String redFlag;
		if(rs1.next()){
			redFlag="YES";
		}else{
			redFlag="NO";
		}
		
		if(redFlag.equalsIgnoreCase("NO")){
		RedFlaggedRcnsDao redrcn= new RedFlaggedRcnsDao(connection);
		redrcn.addToRedFlagList(appId, remark, user, "2", null, null, null, "1", officeCode);
		}	
		
		
		connection.commit();
		
	}
	public void revokeApplicationDetails(String appId,String remark,String user,String officeCode) throws Exception{
		PreparedStatement statement=null;	StringBuffer query =null;
		query = new StringBuffer("UPDATE FC_INDIA SET CURRENT_STATUS=? WHERE RCN=?");		
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, "0");	
		statement.setString(2, appId);
		statement.executeUpdate();
		statement.close();		
		
		// Inserting Registration Cancellation for our reference
		query=new StringBuffer("INSERT INTO T_REGISTRATION_STATUS_HISTORY(RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY,REFERENCE_FOR_DETAILS) "
				+ "VALUES(?,?,?,sysdate,?,?)");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);		
		statement.setString(2, "0");
		statement.setString(3, remark);		
		statement.setString(4, user);
		statement.setString(5, null);		
		statement.executeUpdate();
		statement.close();
		connection.commit();
		
	}
/*	public String generateChatId(String officeCode) throws Exception{
		String chatId=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_CHATID(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,officeCode);
		ResultSet rs=statement.executeQuery();
		if(rs.next())
			chatId=rs.getString(1);
		return chatId;
	}*/
	@Override
	public Integer insertRecord(ProjectRequest object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(ProjectRequest object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProjectRequest> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<ProjectRequest> list) {
		// TODO Auto-generated method stub
		return null;
	}



	public String getAppId() {
		return appId;
	}



	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getRecordsPerPage() {
		return recordsPerPage;
	}

	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}	
}
