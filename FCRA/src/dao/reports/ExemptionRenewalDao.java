package dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;
import models.reports.ExemptionRenewalBlocking;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class ExemptionRenewalDao extends BaseDao {
	
	private String myOfficeCode;
	private String myUserId; 
	private String rcn;
    private String remarks;	
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String appId;
	private String state;
	private String searchString;
	private String district;
	private String functionaryName;
	
	public ExemptionRenewalDao(Connection connection){
		super(connection);
	}

	public List<ExemptionRenewalBlocking> getRcnListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT te.rcn, " +
								"  fc.asso_name, " +
								"  te.remarks, " +
								"  TO_CHAR(te.action_date,'dd-mm-yyyy')AS actiondate, " +
								"  te.exemption_days " +
								"  FROM t_exemption_for_renewal te " +
								"  LEFT JOIN fc_india fc " +
								"  ON fc.rcn=te.rcn ");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");		
		statement = connection.prepareStatement(countQuery.toString());	
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
		//statement = connection.prepareStatement(query.toString());
		 if(pageNum == null || recordsPerPage == null) {

		 }
		 else
		 {
		 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
		 statement.setInt(2, (pageRequested-1) * pageSize + 1);
		 }
		 rs = statement.executeQuery();
		 List<ExemptionRenewalBlocking> applicationList = new ArrayList<ExemptionRenewalBlocking>( );
		 while(rs.next()) {	
		
			 applicationList.add(new ExemptionRenewalBlocking(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)));	
		 }
		 return applicationList;
		 }

	
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("rcn")) {
				orderbyClause.append(" ORDER BY RCN");
			}else if(sortColumn.equals("assoName")) {
				orderbyClause.append(" ORDER BY ASSO_NAME");
			}else if(sortColumn.equals("actionDate")) {
				orderbyClause.append(" ORDER BY ACTION_DATE");
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
	
	
	public Integer insertRecord(ExemptionRenewalBlocking object)throws Exception{
		int row=1;
		if(ESAPI.validator().isValidInput("Remarks", object.getRemarks(), "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid remarks (2000 Max.)");
		}
		if(ESAPI.validator().isValidInput("Days", object.getExemptionDays(), "Num", 3, true) == false){
			throw new ValidationException("Error in Exemption allowed for (in days) (3 digits max)");
		}
		PreparedStatement stmt=null;
		StringBuffer query=new StringBuffer("INSERT " +
							"INTO T_EXEMPTION_FOR_RENEWAL " +
							"  ( " +
							"    RCN, " +
							"    REMARKS, " +
							"    ACTION_DATE, " +
							"    ACTION_BY, " +
							"    RECORD_STATUS, " +
							"    INTIMATION_DATE, " +
							"    EXEMPTION_DAYS " +
							"  ) " +
							"  VALUES " +
							"  ( " +
							"    ?, " +
							"    ?, " +
							"    sysdate, " +
							"    ?, " +
							"    0, " +
							"   null, " +
							"    ? " +
							"  )");
		stmt = connection.prepareStatement(query.toString());
		stmt.setString(1, object.getApplicationId());
		stmt.setString(2, object.getRemarks());
		stmt.setString(3, object.getMyUserId());
		stmt.setString(4, object.getExemptionDays());
		row=stmt.executeUpdate();
		
		stmt.close();
		
		return row;
		
	}
	
	
	//rcn search for advanced 
	
	public List<AbstractRequest> getApplicationListDetailsExemptionRenewal() throws Exception{

		PreparedStatement statement=null;
		StringBuffer query=null;
		StringBuffer queryField=null;
		Boolean searchStringFlag=false,stateFlag=false,stateDistrictFlag=false,functionary=false;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(!(searchString==null || searchString.equals(""))){			
			queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%')");
			searchStringFlag=true;
		}	
		if(!(functionaryName==null || functionaryName.equals(""))){	
			if(searchStringFlag==true)
				queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%'))");
			else
				queryField=new StringBuffer("A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%'))");
			functionary=true;
		}	
		if(!(state==null || state.equals(""))){	
			if(searchStringFlag==true){
				if(functionary==true)
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND SUBSTR(A.STDIST,0,2)=?");
				else
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND SUBSTR(A.STDIST,0,2)=?");
			}
			else{
				if(functionary==true)
					queryField=new StringBuffer(" A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND SUBSTR(A.STDIST,0,2)=? ");
				else
					queryField=new StringBuffer(" SUBSTR(A.STDIST,0,2)=?");
			}				
			stateFlag=true;			
		}	
		if(!(state==null || state.equals("")) && !(district==null || district.equals(""))){	
			if(searchStringFlag==true){
				if(functionary==true)
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND A.STDIST=?");
				else
					queryField=new StringBuffer("UPPER(ASSO_NAME) LIKE ('%' || ? || '%') AND A.STDIST=?");
			}				
			else{
				if(functionary==true)
					queryField=new StringBuffer(" A.RCN IN(SELECT RCN FROM FC_COMMITTEE WHERE UPPER(NAME) LIKE ('%' || ? || '%')) AND  A.STDIST=?");
				else				
					queryField=new StringBuffer(" A.STDIST=?");
			}			
			stateDistrictFlag=true;		
			stateFlag=false;		
		}		
		/*query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM "
				+ "(SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A union SELECT A.RCN,A.ASSO_NAME,null,A.STDIST FROM FC_PP_INDIA A ) A  "
				+ "WHERE "+queryField+"");*/
		
		query=new StringBuffer("SELECT a.rcn, " +
								"  a.asso_name, "+
								"  a.valid_from, " +
								"  a.stdist, " +
								"  a.valid_to , " +
								"  add_months(a.valid_to,4), " +
								"  a.CURRENT_STATUS,case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired "+
								"  FROM fc_india a " +
								"  WHERE a.current_status =0 " +
								"  AND (add_months(a.valid_to,4)<TRUNC(sysdate) " +
								"  OR a.valid_to IS NULL) and "+queryField+ "" );
				
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(searchStringFlag==false){
			if(functionary==false){
				if(stateFlag==false){
					if(stateDistrictFlag==false){					
					}else{
						statement.setString(1, state+district);
					}
				}else{
					statement.setString(1, state);
				}
			}else{
				if(stateFlag==false){
					if(stateDistrictFlag==false){	
						statement.setString(1, functionaryName);
					}else{
						statement.setString(1, functionaryName);
						statement.setString(2, state+district);
					}
				}else{
					statement.setString(1, functionaryName);
					statement.setString(2, state);
				}
			}			
		}else{
			if(functionary==false){
				if(stateFlag==false){
					if(stateDistrictFlag==false){
						statement.setString(1, searchString);
					}else{
						statement.setString(1, searchString);
						statement.setString(2, state+district);
					}
				}else{
					statement.setString(1, searchString);
					statement.setString(2, state);
				}
			}else{
				if(stateFlag==false){
					if(stateDistrictFlag==false){
						statement.setString(1, searchString);
						statement.setString(2, functionaryName);
					}else{
						statement.setString(1, searchString);
						statement.setString(2, functionaryName);
						statement.setString(3, state+district);
					}
				}else{
					statement.setString(1, searchString);
					statement.setString(2, functionaryName);
					statement.setString(3, state);
				}
			}			
		}
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}		
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery2(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(searchStringFlag==false){
				if(functionary==false){
					if(stateFlag==false){
						if(stateDistrictFlag==false){	
							statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(2, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, state+district);
							statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(3, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, state);
						statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(3, (pageRequested-1) * pageSize + 1);
					}
				}else{
					if(stateFlag==false){
						if(stateDistrictFlag==false){	
							statement.setString(1, functionaryName);
							statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(3, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, functionaryName);
							statement.setString(2, state+district);
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, functionaryName);
						statement.setString(2, state);
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
				}			
			}else{
				if(functionary==false){
					if(stateFlag==false){
						if(stateDistrictFlag==false){
							statement.setString(1, searchString);
							statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(3, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, searchString);
							statement.setString(2, state+district);
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, searchString);
						statement.setString(2, state);
						statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(4, (pageRequested-1) * pageSize + 1);
					}
				}else{
					if(stateFlag==false){
						if(stateDistrictFlag==false){
							statement.setString(1, searchString);
							statement.setString(2, functionaryName);
							statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(4, (pageRequested-1) * pageSize + 1);
						}else{
							statement.setString(1, searchString);
							statement.setString(2, functionaryName);
							statement.setString(3, state+district);
							statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
							statement.setInt(5, (pageRequested-1) * pageSize + 1);
						}
					}else{
						statement.setString(1, searchString);
						statement.setString(2, functionaryName);
						statement.setString(3, state);
						statement.setInt(4, (pageRequested-1) * pageSize + pageSize);
						statement.setInt(5, (pageRequested-1) * pageSize + 1);
					}
				}			
			}			
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		String statusDesc=null;
		while(rs.next()) {	
			/*if(rs.getString(3)==null || rs.getString(3).equals("") || rs.getString(3).equals("0"))
				statusDesc="ALIVE";
			else if(rs.getString(3).equals("1"));
				statusDesc="CANCELLED";*/
			
			if(rs.getString(7) != null && rs.getString(7).equals("1")){
				statusDesc="CANCELLED";
			} else if(rs.getString(8) != null && rs.getString(8).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),"",rs.getString(3),statusDesc,"","","","","","","","s"));			
		}
		return applicationList;
	}
	
	
	private String preparePagingQuery2(StringBuffer query) throws Exception {
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
	
	
	
	
	
	
	
	@Override
	public Integer insertRecord(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(Object object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getKVList(List list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public String getMyOfficeCode() {
		return myOfficeCode;
	}

	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}

	public String getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}

	public String getRcn() {
		return rcn;
	}

	public void setRcn(String rcn) {
		this.rcn = rcn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getFunctionaryName() {
		return functionaryName;
	}

	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}
	

	
	
	
}
