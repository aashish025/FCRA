package dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import models.services.ChangeAssociation;
import models.services.ChangeRegistrationValidity;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List2;
import dao.BaseDao;

public class ChangeRegistrationValidityDao extends BaseDao<ChangeRegistrationValidity, String, String>{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String searchString;
	private String district;
	private String functionaryName;
	private String state;
	private String appId;
	private List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
	
	
	public ChangeRegistrationValidityDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	
	public List<AbstractRequest> getApplicationListDetailsChangeRegistration() throws Exception{

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
		 query=new StringBuffer("SELECT a.rcn, " +
								"  a.asso_name,  " +
								"  a.valid_from, "+ 
								" a.CURRENT_STATUS,"+ 
								" case when trunc(a.VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired,"+ 
								"  a.stdist, " +
								"  a.valid_to , " +
								"  add_months(a.valid_to,4) " +
								"  FROM fc_india a " +
								"  WHERE current_status!=1 and ASSOCIATION_TYPE in (1,2) and "+queryField+ "" );		
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
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
			statusDesc="CANCELLED";
		} else if(rs.getString(5) != null && rs.getString(5).equals("Y")) {
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
	public List<List2> getApplicationListDetailsRegChange(String appId) throws Exception{
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT rcn, " +
									" asso_name," +
									" valid_from, CURRENT_STATUS,"+ 
									" case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired,  "+
									" valid_to, ASSOCIATION_TYPE " +
									"FROM fc_india " +
									"WHERE rcn =? and current_status!=1");
								
		PreparedStatement stmt=connection.prepareStatement(query.toString());
		stmt.setString(1, appId);
        ResultSet rsQ=stmt.executeQuery();
        List<List2> list=new ArrayList<List2>();
        int recorFound=0;
        String assoType = null;
		if(rsQ.next()) {
			assoType = rsQ.getString(7);
			recorFound++;
			list.add(new List2(rsQ.getString(1), rsQ.getString(2)));		
		}
		if(assoType==null)
			throw new ValidationException("Registration Validity can be changed only for Central or State Goverenment Associations.");
		
		if(recorFound==0)
			throw new ValidationException("Registration Number <b>"+appId+"</b> either cancelled or not found ");		
		return list;
	}
	public void getApplicationListChangeReg(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	
		StringBuffer query =null;ResultSet rs=null;
		
		query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
				+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
				+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'),(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION),"
				+ "to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),to_char(VALID_TO,'dd-mm-yyyy'),case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired ,A.ASSOCIATION_TYPE "
				+ "FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		rs=statement.executeQuery();	
		String statusDesc=null;
		String assoNewOldFlag=null;
		String bankNewOldFlag=null;
		String assoAddress=null;
		String bankAdress=null;
		String assoNature=null;
		String assoReligion=null;
		String assoNatureDesc="";
		String assocType= null;
		if(rs.next()) {
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} else if(rs.getString(24) != null && rs.getString(24).equals("Y")) {
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
					bankAdress=rs.getString(14)+", "+rs.getString(15);
				}else if(bankNewOldFlag.equals("O")){
					bankAdress=rs.getString(16)+", "+rs.getString(17)+", "+rs.getString(18);
				}
			}
			assoNature=rs.getString(11);
			assoReligion=rs.getString(21);			
			if(assoNature != null) {	 			
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<assoNature.length();j++){
		 			Boolean religious=false;
		 			String natureCode = assoNature.substring(j, j+1);
		 			if(natureCode.equals("1")){
		 				religious=true;
		 			}
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){		 				
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 				if(religious==true){
			 				   assoNatureDesc=assoNatureDesc+" ("+assoReligion+")";
			 			}
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();
			}	
			
		  applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),statusDesc,assoAddress,
				   assoNatureDesc,rs.getString(13),bankAdress,rs.getString(20),rs.getString(19),rs.getString(22),rs.getString(23),rs.getString(25),0));			
		}else{			
			throw new ValidationException("Registration Number <b>"+appId+"</b> doesn't exist.Please search with valid registration number.");
		}	
		rs.close();
		statement.close();
	
	}


	@Override
	public Integer insertRecord(ChangeRegistrationValidity object) throws Exception {
		//getApplicationListDetailsRegChange(object.getApplicationId());
         int rows=0,rows1=0;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		String oldValidTo = null;		
		StringBuffer query = new StringBuffer("Select to_char(VALID_TO,'dd-mm-yyyy') from Fc_India where rcn=? ");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, object.getApplicationId());
		ResultSet rs = statement.executeQuery();
		if(rs.next()){
			oldValidTo=rs.getString(1);
		}
		statement.close();
		
			
		StringBuffer query1= new StringBuffer("Insert INTO T_REGN_EXPIRY_CHANGE_LOG( RCN,OLD_EXPIRY,NEW_EXPIRY,ACTION_DATE,ACTION_BY)VALUES(?,to_date(?,'dd-mm-yyyy'),to_date(?,'dd-mm-yyyy'),systimestamp,?)");
		PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		statement1.setString(1, object.getApplicationId());
		statement1.setString(2, oldValidTo);
		statement1.setString(3, object.getValidityUpTo());
		statement1.setString(4, object.getActionBy());
		rows=statement1.executeUpdate();
		statement1.close();
		
		if(rows>0){
			StringBuffer query2= new StringBuffer("UPDATE Fc_India SET VALID_FROM = to_date(?,'dd-mm-yyyy'), VALID_TO = to_date(?,'dd-mm-yyyy'), LAST_RENEWED_ON = systimestamp WHERE rcn=?");
			PreparedStatement statement2 = connection.prepareStatement(query2.toString());
			statement2.setString(1, object.getValidityFrom());
			statement2.setString(2, object.getValidityUpTo());
			statement2.setString(3, object.getApplicationId());
			rows1=statement2.executeUpdate();
			statement2.close();
			
		}
		
		return rows1;
	
		
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


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}


	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}


	@Override
	public Integer removeRecord(ChangeRegistrationValidity object)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ChangeRegistrationValidity> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<KVPair<String, String>> getKVList(
			List<ChangeRegistrationValidity> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
