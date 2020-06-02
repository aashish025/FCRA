package dao.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utilities.InformationException;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.communication.AutoNotifier;
import utilities.communication.mail.MailScheduler;
import utilities.lists.List1;
import utilities.lists.List12;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.lists.List5;
import utilities.lists.List6;
import utilities.lists.List7;
import utilities.lists.List8;
import utilities.lists.List9;
import utilities.notifications.Notification;
import models.services.CommitteeMember;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class RegistrationTrackingDao extends BaseDao<ProjectRequest, String, String>{
	private String appId;
	private String searchString;
	private String functionaryName;
	private String searchFlag;
	private String state;
	private String district;
	private String pageNum;
	private String recordsPerPage;
	private String redFlag;
	private String blueFlag;
	private String redFlagREDCategory;
	private String redFlagYELLOWCategory;
	private String redFlagAddRole;
	private String redFlagRemoveRole;
	private String yellowFlagRemoveRole;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
	private List<List9> returnList = new ArrayList<List9>();
	private List<List4> historyList=new ArrayList<List4>();
	private List<List8> mailList=new ArrayList<List8>();
	private List<List5> smsList=new ArrayList<List5>();
	private List<List7> redFlagDetailsList = new ArrayList<List7>();
	private List<CommitteeMember> committeeMembers = new ArrayList<CommitteeMember>();
	private List<Notification> notifyList = new ArrayList<Notification>();
	private List<List4> regCancDetails=new ArrayList<List4>();
	
	public RegistrationTrackingDao(Connection connection) {
		super(connection);
	}	

	public List<AbstractRequest> getApplicationListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		String queryField=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM (SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A union SELECT A.RCN,A.ASSO_NAME,null,A.STDIST FROM FC_PP_INDIA A )  A  WHERE RCN=?");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		statement.setString(1, appId);		
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
				statement.setString(1, appId);
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		String statusDesc=null;
		while(rs.next()) {	
			if(rs.getString(3)==null || rs.getString(3).equals("") || rs.getString(3).equals("0"))
				statusDesc="ALIVE";
			else if(rs.getString(3).equals("1"))
				statusDesc="CANCELLED";
			else if(rs.getString(3).equals("3"))
				statusDesc="SUSPENDED";
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),"",rs.getString(3),statusDesc,"","","","","","","",""));			
		}
		return applicationList;
	}
	
	
	
	
	//for basic search rcn search in exemption renewal//
	public List<List2> getApplicationListDetailsExemption() throws Exception{
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT rcn, " +
									" asso_name, " +
									" valid_from, " +
									" valid_to, " +
									" CURRENT_STATUS,case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired"+
									" FROM fc_india " +
									" WHERE rcn =? " +
									" AND current_status=0 " +
									" AND (valid_to is null OR ADD_MONTHS(valid_to,4)< TRUNC(sysdate))" );
								
		PreparedStatement stmt=connection.prepareStatement(query.toString());
		stmt.setString(1, appId);
        ResultSet rsQ=stmt.executeQuery();
        List<List2> list=new ArrayList<List2>();
        int recorFound=0;
		if(rsQ.next()) {
			recorFound++;
			list.add(new List2(rsQ.getString(1), rsQ.getString(2)));		
		}
		if(recorFound==0){
			//if(checkExemptionExpired(appId)){
				throw new ValidationException("Exemption can be given only to those associations whose registration validity has already expired.");
			//}					
		}			
		return list;
	}

	
	//for exemption renewal//
	public String getExistingRcnCheck() throws Exception{
		StringBuffer query=null;
		String status="Y";
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("with t1 as (select rcn, action_date, EXEMPTION_DAYS, to_char(action_date+EXEMPTION_DAYS,'dd-mm-yyyy'), case when trunc(action_date+EXEMPTION_DAYS) < trunc(sysdate) then 'Y' else 'N' end as exemption_expired," 
				+" row_number() over (partition by rcn order by action_date desc ) rn from T_EXEMPTION_FOR_RENEWAL)"
                +" select * from t1 where rn=1 and rcn = ?");
		PreparedStatement stmt=connection.prepareStatement(query.toString());
		stmt.setString(1, appId);
        ResultSet rsQ=stmt.executeQuery();
        List<List2> list=new ArrayList<List2>();
		if (rsQ.next()) {
			if (rsQ.getString(5).equals("Y")) {
			} else {
				status = rsQ.getString(4);
			}
		}
		return status;	
	
	}
	
	
	
	//for registration tracking
	
	public List<AbstractRequest> getAdvanceApplicationListDetails(String flag) throws Exception{
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
		if(flag.equals("0")){			// Registered
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM (SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A) A WHERE "+queryField+"");
		}else if(flag.equals("1")){		// PP
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM (SELECT A.RCN,A.ASSO_NAME,null,A.STDIST FROM FC_PP_INDIA A ) A  WHERE "+queryField+"");
		}else{							// ALL
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS FROM "
					+ "(SELECT A.RCN,A.ASSO_NAME,A.CURRENT_STATUS,A.STDIST FROM FC_INDIA A union SELECT A.RCN,A.ASSO_NAME,null,A.STDIST FROM FC_PP_INDIA A ) A  "
					+ "WHERE "+queryField+"");
		}		
				
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
		String queryForPaging = preparePagingQuery(query);	
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
			if(rs.getString(3)==null || rs.getString(3).equals("") || rs.getString(3).equals("0"))
				statusDesc="ALIVE";
			else if(rs.getString(3).equals("1"))
				statusDesc="CANCELLED";
			else if(rs.getString(3).equals("3"))
							statusDesc="SUSPENDED";
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),"",rs.getString(3),statusDesc,"","","","","","","","s"));			
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
	
	// For Exemption Renewal blocking cases basic search for populating rcn details //
	
	public void getApplicationListExemption(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	
		StringBuffer query =null;ResultSet rs=null;
		
		query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
				+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
				+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'),(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION),"
				+ "to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),to_char(VALID_TO,'dd-mm-yyyy'),case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired "
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
		if(rs.next()) {
			/* *
			if(rs.getString(4)==null || rs.getString(4).equals("") || rs.getString(4).equals("0")){
				statusDesc="ALIVE";
			}
			else if(rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			}
			* */
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} 
			else if(rs.getString(4) != null && rs.getString(4).equals("3")) {
				statusDesc="SUSPENDED";
			}else if(rs.getString(24) != null && rs.getString(24).equals("Y")) {
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
				   assoNatureDesc,rs.getString(13),bankAdress,rs.getString(20),rs.getString(19),rs.getString(22),rs.getString(23)));			
		}else{			
			throw new ValidationException("Registration Number <b>"+appId+"</b> doesn't exist.Please search with valid registration number.");
		}	
		rs.close();
		statement.close();
	
	}
	
	//application tracking//
	public void getApplicationDetails(String appId,String myOfficeCode,String myUserId) throws Exception{		
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
		if(appId.substring(appId.length()-1,appId.length()).equals("R")){
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
					+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'),(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION),"
					+ "to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),to_char(VALID_TO,'dd-mm-yyyy'), case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired,A.ASSOCIATION_TYPE "
					+ " FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");
		}else if(appId.substring(appId.length()-1,appId.length()).equals("P")){
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,null,null,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,null,null,null,null,null,null,null,null,to_char(A.PP_DATE,'dd-mm-yyyy'),(SELECT RELIGION_DESC FROM TM_RELIGION WHERE RELIGION_CODE=A.ASSO_RELIGION),null,null,null,null FROM FC_PP_INDIA A  WHERE A.RCN=?");
		}				
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
		String assoStatus=null;
		String assoType=null;
		if(rs.next()) {
			/* *
			if(rs.getString(4)==null || rs.getString(4).equals("") || rs.getString(4).equals("0")){
				statusDesc="ALIVE";
			}
			else if(rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			}
			* */
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
				assoStatus=rs.getString(4);
			}
			else if(rs.getString(4) != null && rs.getString(4).equals("3")) {
				statusDesc="SUSPENDED";
			}else if(rs.getString(24) != null && rs.getString(24).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else  {
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
			assoType=rs.getString(25);
			if(rs.getString(25) !=null && rs.getString(25).equals("1")){
				assoType="EXEMPTED(STATE GOVT)";				
			}else if (rs.getString(25) !=null && rs.getString(25).equals("2")){
				assoType="EXEMPTED(CENTRAL GOVT)";
			}else{
				assoType="NOT EXEMPTED";
			}
			
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
				   assoNatureDesc,rs.getString(13),bankAdress,rs.getString(20),rs.getString(19),rs.getString(22),rs.getString(23),assoType,"1"));
		  
		  // Getting Cancellation Details
		  if(assoStatus!=null && assoStatus.equals("1")){
			  query=new StringBuffer("with t3 as (select rcn, status,status_date, remarks, REFERENCE_FOR_DETAILS from (select a.*, row_number() over (partition by rcn order by status_date desc ) rn "
			  		+ "from T_REGISTRATION_STATUS_HISTORY a) where rn=1 ),t4 as (select a.rcn,status,status_date,remarks,case when CANCELLATION_TYPE='V' then 'Violation' else case when CANCELLATION_TYPE='R' "
			  		+ "then 'On Request' else null end end as cancel_type,(select listagg(REASON_DESC,'; ') within group(order by REASON_ID) from TM_REGN_CANCELLATION_REASONS where REASON_ID in "
			  		+ "(select regexp_substr(b.CANCELLATION_REASON, '(.{1,2})', 1, level) from dual connect by level <= ceil(length(b.CANCELLATION_REASON)/2))) reason from t3 A left join "
			  		+ "T_REGN_CANCELLATION_DETAILS b on a.REFERENCE_FOR_DETAILS = b.REFERENCE_FOR_DETAILS) select to_char(t4.status_date,'dd-mm-yyyy') as cancelled_date,  t4.cancel_type, "
			  		+ "t4.remarks as cancelled_remarks,t4.reason from  t4 where rcn=?");
			  statement = connection.prepareStatement(query.toString());	
			  statement.setString(1, appId);		  	
			  rs=statement.executeQuery();		   
			  if(rs.next()){
				  regCancDetails.add(new List4(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			  }
		  }
		}else{			
			throw new ValidationException("Registration Number <b>"+appId+"</b> doesn't exist.Please search with valid registration number.");
		}	
		rs.close();
		statement.close();
		
		// Checking whether in adverse list		
		query=new StringBuffer("SELECT * FROM T_RED_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			redFlag="YES";
		}else{
			redFlag="NO";
		}
		
		// Checking RED FLAG CATEGORY_TYPE	
		if(redFlag.equals("YES")){
			query=new StringBuffer("SELECT FLAG_TYPE FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=? AND STATUS=0 AND STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_RED_FLAG_STATUS_HISTORY WHERE  RCN=? AND STATUS=0)");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, appId);	
			statement.setString(2, appId);	
			rs=statement.executeQuery();
			if(rs.next()){
				if(rs.getString(1)== null || rs.getString(1).equals("2")){
					redFlagYELLOWCategory="YES";
				}
				else if(rs.getString(1).equals("1")){
					redFlagREDCategory="YES";
				}			
			}else{
				redFlagYELLOWCategory="YES";
			}
		}
		StringBuffer query1=new StringBuffer("SELECT * FROM T_BLUE_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query1.toString());	
		statement.setString(1, appId);	
		rs=statement.executeQuery();
		if(rs.next()){
			blueFlag="YES";
		}else{
			blueFlag="NO";
		}
		// Checking Logged in User having Red Flag Add Role
		query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=16 AND RECORD_STATUS = 0");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, myUserId);		
		rs=statement.executeQuery();
		if(rs.next()){
			redFlagAddRole="YES";
		}else{
			redFlagAddRole="NO";
		}
		
		// Checking Logged in User having Red Flag Remove Role
		query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=17 AND RECORD_STATUS = 0");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, myUserId);		
		rs=statement.executeQuery();
		if(rs.next()){
			redFlagRemoveRole="YES";
		}else{
			redFlagRemoveRole="NO";
		}	
		
		// Checking Logged in User having YELLOW Flag Remove Role
		query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=18 AND RECORD_STATUS = 0");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, myUserId);		
		rs=statement.executeQuery();
		if(rs.next()){
			yellowFlagRemoveRole="YES";
		}else{
			yellowFlagRemoveRole="NO";
		}	
		
		if(redFlag.equals("YES")/* && (redFlagAddRole.equals("YES") || redFlagRemoveRole.equals("YES") || yellowFlagRemoveRole.equals("YES"))*/){
			query=new StringBuffer("SELECT REMARKS,to_char(STATUS_DATE,'dd-mm-yyyy'),ACTION_BY || ' [' || (SELECT USER_NAME FROM TM_USER WHERE USER_ID=ACTION_BY) || ' ]',"
					+ "ORIGINATOR_OFFICE,ORIGINATOR_ORDER_NO,to_char(ORIGINATOR_ORDER_DATE,'dd-mm-yyyy'),"
					+ "(SELECT CATEGORY_DESC FROM TM_RED_FLAG_CATEGORY WHERE CATEGORY_CODE=RED_FLAG_CATEGORY) FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=? "
					+ "AND STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=?) ");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, appId);	
			statement.setString(2, appId);
			rs=statement.executeQuery();
			if(rs.next()){
				redFlagDetailsList.add(new List7(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
			}
		}
		
		// Return Details
		query=new StringBuffer("WITH T1 AS ("
				+ "SELECT DISTINCT FT.BLKYEAR, "
				+ "NVL(SUM(FP3.AMOUNT),0) AS AMOUNT FROM FC_INDIA FI,FC_FC3_PART3 FP3,FC_FC3_TALLY FT "
				+ "WHERE (FI.REG_DATE IS NULL OR TRUNC(FI.REG_DATE) < TRUNC(TO_DATE('01-04-'||SUBSTR(FT.BLKYEAR,-4,4),'DD-MM-YYYY'))) AND (FI.STATUS <> 'D')   AND FI.RCN=FP3.RCN "
				+ "AND 1=1 AND SUBSTR(FP3.RCN,-1,1)='R' AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y'  AND FT.RCN = FP3.RCN AND FT.RCN=? "
				+ "GROUP BY FT.BLKYEAR "
				+ "UNION ALL "
				+ "SELECT DISTINCT FT.BLKYEAR, "
				+ "NVL(SUM(FP3.AMOUNT),0) AS AMOUNT  FROM FC_PP_INDIA FPI,FC_FC3_PART3 FP3,FC_FC3_TALLY FT "
				+ "WHERE FPI.RCN=FP3.RCN  AND FP3.BLKYEAR=FPI.BLKYEAR AND 1=1 "
				+ "AND SUBSTR(FP3.RCN,-1,1)='P' AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y'  AND FT.RCN = FP3.RCN AND FT.RCN=? "
				+ "GROUP BY FT.BLKYEAR "
				+ "UNION ALL "
				+ "SELECT DISTINCT FT.BLKYEAR, NVL(SUM(FP3.AMOUNT),0) AS AMOUNT FROM FC_INDIA FI,FC_FC3_DONOR_WISE FP3,FC_FC3_TALLY FT, FC_FC3_DONOR FD  "
				+ "WHERE (FI.REG_DATE IS NULL OR TRUNC(FI.REG_DATE) < TRUNC(TO_DATE('01-04-'||SUBSTR(FT.BLKYEAR,-4,4),'DD-MM-YYYY'))) AND (FI.STATUS <> 'D')   "
				+ "AND FI.RCN=FP3.RCN    AND 1=1 AND SUBSTR(FP3.RCN,-1,1)='R'  AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y' AND FT.RCN = FP3.RCN AND FT.RCN=?  "
				+ "AND FI.RCN=FD.RCN  AND FT.BLKYEAR=FP3.BLKYEAR AND FP3.DONOR_CODE=FD.DONOR_CODE GROUP BY FT.BLKYEAR "
				+ "UNION ALL "
				+ "SELECT DISTINCT FP3.BLKYEAR, NVL(SUM(FP3.AMOUNT),0) AS AMOUNT  FROM FC_PP_INDIA FPI,FC_FC3_DONOR_WISE FP3,FC_FC3_TALLY FT,FC_FC3_DONOR FD  WHERE  "
				+ "FPI.RCN=FP3.RCN  AND FP3.BLKYEAR=FPI.BLKYEAR  AND 1=1 AND SUBSTR(FP3.RCN,-1,1)='P'  AND FT.BLKYEAR=FP3.BLKYEAR  AND FT.FINAL_SUBMIT='Y'  "
				+ "AND FT.RCN = FP3.RCN AND FT.RCN=?  AND FPI.RCN=FD.RCN  AND FT.BLKYEAR=FP3.BLKYEAR  AND FP3.DONOR_CODE=FD.DONOR_CODE GROUP BY FP3.BLKYEAR )"
				+ ", T2 AS ( "
				+ "SELECT FP.BLKYEAR,FOR_AMT+BK_INT+OTH_INT AS TOTAMOUNT,FOR_AMT,(BK_INT+OTH_INT) INTEREST,0 AS LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'DD-MM-YYYY'),"
				+ "FT.UNIQUE_FILENO FROM FC_FC3_PART1 FP,FC_FC3_TALLY FT WHERE FP.RCN=? AND FT.RCN=FP.RCN AND FP.BLKYEAR=FT.BLKYEAR AND FP.BLKYEAR NOT IN ('2005-2006') "
				+ "AND FT.FINAL_SUBMIT='Y' "
				+ "UNION SELECT FP.BLKYEAR,BK_INT+SOURCE_FOR_AMT+SOURCE_LOCAL_AMT AS AMOUNT,SOURCE_FOR_AMT,BK_INT,SOURCE_LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'DD-MM-YYYY'),"
				+ "FT.UNIQUE_FILENO FROM FC_FC3_PART1_NEW FP,FC_FC3_TALLY FT WHERE FP.RCN=? AND FT.RCN=FP.RCN AND FP.BLKYEAR=FT.BLKYEAR AND FP.BLKYEAR NOT IN ('2005-2006') "
				+ "AND FT.FINAL_SUBMIT='Y')"
				+ " SELECT T2.*,NVL(T1.AMOUNT,0) DONORWISEAMT, (T2.TOTAMOUNT-NVL(T1.AMOUNT,0)) DIFF FROM T2 LEFT JOIN T1 ON T2.BLKYEAR=T1.BLKYEAR ORDER BY T2.BLKYEAR DESC");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		statement.setString(2, appId);
		statement.setString(3, appId);		
		statement.setString(4, appId);
		statement.setString(5, appId);		
		statement.setString(6, appId);
		rs=statement.executeQuery();
		while(rs.next()){
			String date=null;
			if(rs.getString(6)==null)
				date="";
			else
				date=rs.getString(6);
			String amount=null;
			if(rs.getString(2)==null)
				amount="";
			else
				amount=rs.getString(2);
			returnList.add(new List9(rs.getString(1),amount,rs.getString(3),rs.getString(4),rs.getString(5),date,rs.getString(7),rs.getString(8),
					rs.getString(9)));
		}			
		// Committee Members Details		
		 query = new StringBuffer("SELECT NAME, FATHER_HUSBAND_NAME, NATIONALITY, "
				+ "(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=NATIONALITY), "
				+ "AADHAAR, OCCUPATION, CASE WHEN OCCUPATION=99 THEN  (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=OCCUPATION) || ' (' ||OCCUPATION_OTHER || ')' ELSE  (SELECT OCC_NAME FROM TM_OCCUPATION WHERE OCC_CODE=OCCUPATION) END OCC, OCCUPATION_OTHER, "
				+ "CASE WHEN OLD_NEW='O' THEN OFFICE_OF_ASSO ELSE CASE WHEN OFFICE_OF_ASSO=99 THEN  (SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE to_char(DESIG_CODE)=OFFICE_OF_ASSO) || ' (' ||OFFICE_OF_ASSO_OTHER || ')' ELSE  (SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE to_char(DESIG_CODE)=OFFICE_OF_ASSO) END END DESIG, "
				+ "OFFICE_OF_ASSO_OTHER, CASE WHEN OLD_NEW='O' THEN BEARERS_RELATIONSHIP ELSE CASE WHEN BEARERS_RELATIONSHIP=99 THEN  (SELECT RELATION_NAME FROM TM_COMMITTEE_RELATION WHERE to_char(RELATION_CODE)=BEARERS_RELATIONSHIP) || ' (' ||BEARERS_RELATIONSHIP_OTHER || ')' ELSE  (SELECT RELATION_NAME FROM TM_COMMITTEE_RELATION WHERE to_char(RELATION_CODE)=BEARERS_RELATIONSHIP) END END RELN, "
				+ "BEARERS_RELATIONSHIP_OTHER, ADDRESS_OF_ASSO, "
				+ "ADDRESS_OF_RESID, EMAIL_ID, LANDLINE, MOBILE, MOBILE_CTR_CODE, TO_CHAR(DOB, 'dd-mm-yyyy'), PLACE_BIRTH, PASSPORT_NO, PER_ADD_FC, "
				+ "CASE WHEN WHETHER_IN_YN IS NULL THEN '' ELSE CASE WHEN WHETHER_IN_YN='Y' THEN 'Yes' ELSE 'No' END END, CASE WHEN WHETHER_IN_YN IS NULL THEN '' ELSE CASE WHEN WHETHER_IN_YN='Y' THEN PIO_NO ELSE '' END END, CASE WHEN RES_INDIA_STATUS IS NULL THEN '' ELSE CASE WHEN RES_INDIA_STATUS='Y' THEN 'Yes' ELSE 'No' END END, TO_CHAR(RES_INDIA_DATE, 'dd-mm-yyyy') "
				+ "FROM fc_committee WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, appId);
		rs = statement.executeQuery();
		while(rs.next()) {
			int i=1;
			CommitteeMember member = new CommitteeMember();			
			member.setName(rs.getString(i++));
			member.setNameOfFatherSpouse(rs.getString(i++));
			member.setNationality(rs.getString(i++));
			member.setNationalityDesc(rs.getString(i++));
			//nationalityOther;
			member.setAadhaarNumber(rs.getString(i++));
			member.setOccupation(rs.getString(i++));
			member.setOccupationDesc(rs.getString(i++));
			member.setOccupationOther(rs.getString(i++));
			member.setDesignationInAssociation(rs.getString(i++));
			//member.setDesignationInAssociationDesc(rs.getString(i++));
			member.setDesignationInAssociationOther(rs.getString(i++));
			member.setRelationWithOfficeBearers(rs.getString(i++));
			//member.setRelationWithOfficeBearersDesc(rs.getString(i++));
			member.setRelationWithOfficeBearersOther(rs.getString(i++));
			member.setOfficeAddress(rs.getString(i++));
			member.setResidenceAddress(rs.getString(i++));
			member.setEmail(rs.getString(i++));
			member.setPhoneNumber(rs.getString(i++));
			String mobile = rs.getString(i++);
			String mobileCode = rs.getString(i++);
			member.setMobile(mobileCode+"-"+mobile);
			
			member.setDateOfBirth(rs.getString(i++));
			member.setPlaceOfBirth(rs.getString(i++));
			member.setPassportNumber(rs.getString(i++));
			member.setAddressInForeignCountry(rs.getString(i++));
			member.setPersonOfIndianOrigin(rs.getString(i++));
			member.setPioOciCardNumber(rs.getString(i++));
			member.setResidentInIndia(rs.getString(i++));
			member.setDateFromWhichResidingInIndia(rs.getString(i++));
			committeeMembers.add(member);
		}		
		// History
		/*query=new StringBuffer("SELECT APPLICATION_ID,TO_CHAR(SUBMISSION_DATE,'DD-MM-YYYY'),"
								+ " (SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=V.SERVICE_CODE) SERVICE,"
								+ " (SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=V.CURRENT_STATUS) STATUS"
								+ " FROM V_APPLICATION_DETAILS V WHERE APPLICATION_ID IN (SELECT UNIQUE_FILENO FROM ("
								+ " SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN "
								+ " FROM FC_FC5_ENTRY_NEW1 UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM UNION"
								+ " SELECT UNIQUE_FILENO, RCN FROM T_FC8_ENTRY A, FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO ) "
								+ " WHERE UPPER(RCN)=UPPER('"+appId+"')) ORDER BY SUBMISSION_DATE");*/
		
	//BELOW CODE COMMENTED FOR TESTING AND LATER THIS QUERY WAS COMMENTED AND REPLACED WITH QUERY PREPARED USING WITH CLAUSE commented for testing	,,, and later
		
/*		query =new StringBuffer(" SELECT APPLICATION_ID,TO_CHAR(SUBMISSION_DATE,'DD-MM-YYYY'),(SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=V.SERVICE_CODE) SERVICE, (SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID=V.CURRENT_STATUS) STATUS FROM V_APPLICATION_DETAILS V "
                + " WHERE APPLICATION_ID IN (SELECT UNIQUE_FILENO FROM ( SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN FROM FC_FC5_ENTRY_NEW1 "
                + " UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM "
                + " UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY  "
                + " UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY  "
                + " UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_BANK_CHANGE "
                + " UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_NAMEADDRESS_CHANGE "
                + " UNION SELECT UNIQUE_FILENO, RCN "
				+ " FROM T_FC8_ENTRY A, FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO ) WHERE UPPER(RCN)=UPPER('"+appId+"')) ORDER BY SUBMISSION_DATE");*/
		
		query =new StringBuffer(" with t1 as ( SELECT APPLICATION_ID,   TO_CHAR(SUBMISSION_DATE,'DD-MM-YYYY') SUBMISSION_DATE,   (SELECT SERVICE_DESC FROM TM_SERVICE WHERE SERVICE_CODE=V.SERVICE_CODE   ) SERVICE,   (SELECT FILESTATUS_DESC   FROM TM_FILESTATUS   WHERE FILESTATUS_ID=V.CURRENT_STATUS   ) STATUS FROM V_APPLICATION_DETAILS V ), t2 as (SELECT UNIQUE_FILENO   FROM     ( SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN FROM FC_FC5_ENTRY_NEW1     UNION     SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM     UNION     SELECT UNIQUE_FILENO, RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY     UNION     SELECT UNIQUE_FILENO, RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY     UNION     SELECT UNIQUE_FILENO, RCN FROM FC_FC6_BANK_CHANGE     UNION     SELECT UNIQUE_FILENO, RCN FROM FC_FC6_NAMEADDRESS_CHANGE     UNION     SELECT UNIQUE_FILENO,       RCN     FROM T_FC8_ENTRY A,       FC_INDIA B     WHERE A.SECTION_FILENO=B.SECTION_FILENO     )   WHERE UPPER(RCN)=UPPER('"+appId+"') )select * from t1 where APPLICATION_ID in (select UNIQUE_FILENO from t2) order by SUBMISSION_DATE ");		
		statement = connection.prepareStatement(query.toString());		
		rs = statement.executeQuery();		
		while(rs.next()){
			historyList.add(new List4(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
		}
		// History	
		

		getCommunicationDetailsWithApplicant(appId);
	}
	
	private void getCommunicationDetailsWithApplicant(String rcn) throws Exception{
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
		// MAIL
		query=new StringBuffer("with t1 as (SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN  FROM FC_FC5_ENTRY_NEW1 UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM UNION SELECT UNIQUE_FILENO, RCN FROM T_FC8_ENTRY A,"
				+ " FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO), t2 as (select a.application_id, mail_id,sent_on from t_application_automail_details a WHERE APPLICATION_ID IN "
				+ "(SELECT UNIQUE_FILENO FROM t1 WHERE UPPER(RCN)=UPPER(?)) union select a.application_id, mail_id,sent_on from t_application_automail_details a WHERE "
				+ "APPLICATION_ID =UPPER(?) ) SELECT A.mail_id,A.TO_ADDRESS,A.MAIL_SUBJECT,A.MAIL_BODY,to_char(B.SENT_ON,'dd-mm-yyyy hh24:mi:ss') as sent_on, CASE WHEN A.STATUS_ID=0 THEN 'PENDING' WHEN "
				+ "A.STATUS_ID=1 THEN 'SENT' ELSE 'ERROR' END STATUS ,to_char(A.STATUS_DATE,'dd-mm-yyyy hh24:mi:ss') as statusdate,(SELECT  listagg(DOCUMENT_NAME,',') within group (order by document_name)"
				+ " FROM T_DOCUMENT_DETAILS WHERE DOCUMENT_ID in ( select DOCUMENT_ID FROM T_MAIL_ATTACHMENT_DETAILS WHERE MAIL_ID=A.MAIL_ID)) ATTACHMENT from T_MAIL_DETAILS A, t2 B WHERE B.MAIL_ID=A.MAIL_ID order by status_date desc,SENT_ON desc");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, rcn);
			statement.setString(2, rcn);
			rs = statement.executeQuery();		
			while(rs.next()){
				mailList.add(new List8(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));
			}
			rs.close();
			statement.close();
			
		// SMS
		query=new StringBuffer("with t1 as (SELECT UNIQUE_FILENO, ASSO_FCRA_RCN RCN FROM FC_FC5_ENTRY_NEW1 UNION SELECT UNIQUE_FILENO, RCN FROM FC_FC6_FORM UNION SELECT UNIQUE_FILENO, RCN FROM T_FC8_ENTRY A, "
				+ "FC_INDIA B WHERE A.SECTION_FILENO=B.SECTION_FILENO ), t2 as (select a.application_id, sms_id,sent_on from t_application_autosms_details a WHERE APPLICATION_ID IN "
				+ "(SELECT UNIQUE_FILENO FROM t1 WHERE UPPER(RCN)=UPPER(?))   union  select a.application_id, sms_id,sent_on from t_application_autosms_details a "
				+ "WHERE APPLICATION_ID =UPPER(?) ) SELECT A.TO_ADDRESS,A.MESSAGE,to_char(B.SENT_ON,'dd-mm-yyyy hh24:mi:ss'),CASE WHEN A.STATUS_ID=0 THEN 'PENDING' WHEN A.STATUS_ID=1 THEN 'SENT' ELSE 'ERROR' END STATUS ,"
				+ "to_char(A.STATUS_DATE,'dd-mm-yyyy hh24:mi:ss') as statusdate FROM T_SMS_DETAILS A, t2 B WHERE A.sms_id= b.sms_id and A.SMS_ID IN(SELECT SMS_ID FROM T2) order by A.STATUS_DATE desc,sent_on desc");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, rcn);
			statement.setString(2, rcn);
			rs = statement.executeQuery();		
			while(rs.next()){
				smsList.add(new List5(new StringBuffer(rs.getString(1)),new StringBuffer(rs.getString(2)),new StringBuffer(rs.getString(3)),new StringBuffer(rs.getString(4)),new StringBuffer(rs.getString(5))));
			}
			rs.close();
			statement.close();
	}
	
	public Boolean checkAddRedFlagRole(String userId) throws Exception{
		Boolean status=false;
		// Checking Logged in User having Red Flag Add Role
		StringBuffer query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=16 AND RECORD_STATUS = 0");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, userId);		
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			status=true;
		}
		return status;
	}
	
	public Boolean checkRemoveRedFlagRole(String userId,String appId) throws Exception{
		Boolean status=false;StringBuffer query=null;PreparedStatement statement=null;ResultSet rs=null;		
		// Checking RED FLAG CATEGORY_TYPE		
		query=new StringBuffer("SELECT FLAG_TYPE FROM T_RED_FLAG_STATUS_HISTORY WHERE RCN=? AND STATUS=0 AND STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_RED_FLAG_STATUS_HISTORY WHERE  RCN=? AND STATUS=0)");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);	
		statement.setString(2, appId);	
		rs=statement.executeQuery();
		if(rs.next()){			
			if(rs.getString(1)== null || rs.getString(1).equals("2")){
				redFlagYELLOWCategory="YES";
			}
			else if(rs.getString(1).equals("1")){
				redFlagREDCategory="YES";
			}			
		}else{
			redFlagYELLOWCategory="YES";
		}
		rs.close();
		statement.close();
		if(redFlagYELLOWCategory!=null && redFlagYELLOWCategory.equals("YES")){
			// Checking Logged in User having Red Flag Yellow Remove Role
			query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=18 AND RECORD_STATUS = 0");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, userId);		
			rs=statement.executeQuery();
			if(rs.next()){
				status=true;
			}
			rs.close();
			statement.close();
		}else if(redFlagREDCategory!=null && redFlagREDCategory.equals("YES")){
			// Checking Logged in User having Red Flag Red Remove Role
			query=new StringBuffer("SELECT * FROM TM_USER_ROLE WHERE USER_ID=? AND ROLE_ID=17 AND RECORD_STATUS = 0");
			statement = connection.prepareStatement(query.toString());	
			statement.setString(1, userId);		
			rs=statement.executeQuery();
			if(rs.next()){
				status=true;
			}
			rs.close();
			statement.close();
		}		
		return status;	
	}
	
	public void addToRedFlagList(String appId,String remark,String user,String category,String office,String order,String date) throws Exception{
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		StringBuffer query = new StringBuffer("INSERT INTO T_RED_FLAGGED_ASSOCIATIONS VALUES(?)");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.executeUpdate();	
		
		StringBuffer query1 = new StringBuffer("INSERT INTO T_RED_FLAG_STATUS_HISTORY(RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY,ORIGINATOR_OFFICE,ORIGINATOR_ORDER_NO,ORIGINATOR_ORDER_DATE,RED_FLAG_CATEGORY)"
				+ " VALUES(?,?,?,sysdate,?,?,?,to_date(?,'dd-mm-yyyy'),?)");
		statement = connection.prepareStatement(query1.toString());		
		statement.setString(1, appId);
		statement.setString(2, "0");
		statement.setString(3, remark);
		statement.setString(4, user);
		statement.setString(5, office);
		statement.setString(6, order);
		statement.setString(7, date);
		statement.setString(8, category);
		statement.executeUpdate();	
		connection.commit();			
	} 
	
	public void removeFromRedFlagList(String appId,String remark,String user,String office,String order,String date,String myOfficeCode) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		query = new StringBuffer("DELETE FROM T_RED_FLAGGED_ASSOCIATIONS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.executeUpdate();	
		statement.close();
		
		
		query = new StringBuffer("INSERT INTO T_RED_FLAG_STATUS_HISTORY (RCN,STATUS,REMARKS,STATUS_DATE,ACTION_BY,ORIGINATOR_OFFICE,ORIGINATOR_ORDER_NO,ORIGINATOR_ORDER_DATE,RED_FLAG_CATEGORY) VALUES(?,?,?,sysdate,?,?,?,to_date(?,'dd-mm-yyyy'),null)");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.setString(2, "1");
		statement.setString(3, remark);
		statement.setString(4, user);
		statement.setString(5, office);
		statement.setString(6, order);
		statement.setString(7, date);
		statement.executeUpdate();		
		connection.commit();
		statement.close();
		
		ResultSet rs=null;
		query = new StringBuffer("SELECT USER_ID||'(' ||USER_NAME || ')[' ||(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=TM_USER.DESIGNATION_ID) "
				+ "|| ']' AS USERNAME  FROM TM_USER WHERE USER_ID=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, user);
		rs=statement.executeQuery();
		String userInfo=null;
		if(rs.next()){
			userInfo=rs.getString(1);
		}
		rs.close();
		statement.close();
		
		query = new StringBuffer("SELECT EMAIL_ID,to_char(sysdate,'dd-mm-yyyy hh24:mi:ss') FROM TM_OFFICE WHERE OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, myOfficeCode);
		rs=statement.executeQuery();
		String email=null,time=null;
		if(rs.next()){
			email=rs.getString(1);
			time=rs.getString(2);
		}
		rs.close();
		statement.close();
		
		query = new StringBuffer("SELECT ASSO_NAME,CASE WHEN NEW_OLD='N' THEN ASSO_ADDRESS ELSE ADD1||ADD2||ADD3||' - '||ASSO_PIN END FROM FC_INDIA WHERE RCN=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		rs=statement.executeQuery();
		String assoName=null,assoAdd=null;
		if(rs.next()){
			assoName=rs.getString(1);
			assoAdd=rs.getString(2);
		}
		rs.close();
		statement.close();
		
		// Sending Mail to JSF	 	
		String mailContent="This is to inform you that the following association has been removed from adverse list by <b>"+userInfo+"</b> on <b>"+time+".</b><br/><br/>"
				+ "Asoociation Name: <b>"+assoName+"</b><br/> RCN: <b>"+appId+"</b> <br/> Address: <b>"+assoAdd+"</b> <br/> Remark: <b>"+remark+"</b> <br/><br/> "
				+ "Note:This Email is system generated. Please do not reply to this email ID";		
		MailScheduler.schedule("no-reply-MHAFCRA@nic.in", email, "Red Flag Entry Removal - Intimation under FCRA", mailContent, null, null, null, connection);
	} 
	
	public List<List2> getStateList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SCODE,SNAME FROM TM_STATE WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<List2>  stateList = new ArrayList<List2>();
		while(rs.next()) {			
			stateList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return stateList;
	} 
	
	public List<List2> getDistrictList(String state) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT DISTCODE,DISTNAME FROM TM_DISTRICT WHERE SCODE=? AND RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, state);
		ResultSet rs = statement.executeQuery();
		List<List2>  districtList = new ArrayList<List2>();
		while(rs.next()) {			
			districtList.add(new List2(rs.getString(1), rs.getString(2)));			
		}
		return districtList;
	} 
	
	public List<List2> getRedFlagCategory() throws Exception{
		PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
		query = new StringBuffer("SELECT CATEGORY_CODE,CATEGORY_DESC FROM TM_RED_FLAG_CATEGORY WHERE RECORD_STATUS=0");		
		statement = connection.prepareStatement(query.toString());				
		rs=statement.executeQuery();	
		List<List2> list=new ArrayList<List2>();
		while(rs.next()){
			list.add(new List2(rs.getString(1),rs.getString(2)));
		}
		return list;
	}

	
	public String generateChatId(String officeCode) throws Exception{
		String chatId=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_CHATID(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1,officeCode);
		ResultSet rs=statement.executeQuery();
		if(rs.next())
			chatId=rs.getString(1);
		return chatId;
	}
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

	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	


	public List<CommitteeMember> getCommitteeMembers() {
		return committeeMembers;
	}

	public void setCommitteeMembers(List<CommitteeMember> committeeMembers) {
		this.committeeMembers = committeeMembers;
	}

	public String getRedFlag() {
		return redFlag;
	}

	

	public void setRedFlag(String redFlag) {
		this.redFlag = redFlag;
	}

	public String getFunctionaryName() {
		return functionaryName;
	}

	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}

	public String getRedFlagAddRole() {
		return redFlagAddRole;
	}

	public void setRedFlagAddRole(String redFlagAddRole) {
		this.redFlagAddRole = redFlagAddRole;
	}

	public String getRedFlagRemoveRole() {
		return redFlagRemoveRole;
	}

	public void setRedFlagRemoveRole(String redFlagRemoveRole) {
		this.redFlagRemoveRole = redFlagRemoveRole;
	}

	

	public void setRedFlagDetailsList(List<List7> redFlagDetailsList) {
		this.redFlagDetailsList = redFlagDetailsList;
	}

	public List<List7> getRedFlagDetailsList() {
		return redFlagDetailsList;
	}

	public List<List4> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<List4> historyList) {
		this.historyList = historyList;
	}

	public List<Notification> getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
	}

	public String getYellowFlagRemoveRole() {
		return yellowFlagRemoveRole;
	}

	public void setYellowFlagRemoveRole(String yellowFlagRemoveRole) {
		this.yellowFlagRemoveRole = yellowFlagRemoveRole;
	}

	public String getRedFlagREDCategory() {
		return redFlagREDCategory;
	}

	public void setRedFlagREDCategory(String redFlagREDCategory) {
		this.redFlagREDCategory = redFlagREDCategory;
	}

	public String getRedFlagYELLOWCategory() {
		return redFlagYELLOWCategory;
	}

	public void setRedFlagYELLOWCategory(String redFlagYELLOWCategory) {
		this.redFlagYELLOWCategory = redFlagYELLOWCategory;
	}

	public void setReturnList(List<List9> returnList) {
		this.returnList = returnList;
	}

	public List<List9> getReturnList() {
		return returnList;
	}

	public List<List4> getRegCancDetails() {
		return regCancDetails;
	}

	public void setRegCancDetails(List<List4> regCancDetails) {
		this.regCancDetails = regCancDetails;
	}

	public List<List8> getMailList() {
		return mailList;
	}

	public void setMailList(List<List8> mailList) {
		this.mailList = mailList;
	}

	public List<List5> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<List5> smsList) {
		this.smsList = smsList;
	}

	public String getBlueFlag() {
		return blueFlag;
	}

	public void setBlueFlag(String blueFlag) {
		this.blueFlag = blueFlag;
	}

}
