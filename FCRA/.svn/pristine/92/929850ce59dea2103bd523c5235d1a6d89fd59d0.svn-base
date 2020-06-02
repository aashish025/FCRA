package dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.owasp.esapi.ESAPI;

import utilities.ValidationException;
import utilities.communication.AutoNotifier;
import utilities.lists.List2;
import utilities.notifications.Notification;
import models.services.ExemptionAnnualPenalty;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class ExemptionAnnualPenaltyDao extends BaseDao {
	
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
	public List<Notification> notifyList = new ArrayList<Notification>();
	
	public ExemptionAnnualPenaltyDao(Connection connection){
		super(connection);
	}
	  public List<String> checkBlockYearList(String rcn) throws Exception{
			List<String>  blkTypeList = new ArrayList<String>();
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			String query = "with t1 as (select 2010 as start_year from dual),t2 as "
					+ "(select  (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date"
					+ "('01-APR-'||to_char(sysdate, 'YYYY')) then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 "
					+ "end ) - ((select start_year from t1)) + 1), "
					+ "t3 as (select yr||'-'||(yr+1) blkyr, yr from t2),t4 as"
					+ " (select rcn, trunc(reg_date) regdate, case when reg_date is null then 2006 else case when trunc(reg_date) >= to_date('01-APR-'||to_char(reg_date, 'YYYY')) then"
					+ " to_number(to_char(reg_date, 'YYYY')) else to_number(to_char(reg_date, 'YYYY'))-1 end end blk from fc_india ) select blkyr from t3,t4 "
					+ "where yr>=blk and rcn=? minus select distinct blkyear from fc_fc3_tally where RCN=?";
			PreparedStatement statement = connection.prepareStatement(query);		
			statement.setString(1, rcn);
			statement.setString(2, rcn);
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {			
				blkTypeList.add(rs.getString(1));			
			}
			return blkTypeList;
		}
	public List<ExemptionAnnualPenalty> getRcnListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT A.rcn,fc.ASSO_NAME,A.BLKYEAR,A.REMARKS,TO_CHAR(A.action_date,'dd-mm-yyyy')AS actiondate ,"
				+ "A.EXEMPTION_DAYS FROM  T_EXEMPTION_FOR_ANNUAL_RETURNS A LEFT JOIN fc_india fc ON fc.rcn=A.rcn");
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
		 if(pageNum == null || recordsPerPage == null) {

		 }
		 else
		 {
		 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
		 statement.setInt(2, (pageRequested-1) * pageSize + 1);
		 }
		 rs = statement.executeQuery();
		 List<ExemptionAnnualPenalty> applicationList = new ArrayList<ExemptionAnnualPenalty>( );
		 while(rs.next()) {	
			 applicationList.add(new ExemptionAnnualPenalty(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));	
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
	
	
	public Integer insertRecord(ExemptionAnnualPenalty object)throws Exception{
		int row=1;
		if(ESAPI.validator().isValidInput("Remarks", object.getRemarks(), "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid remarks (2000 Max.)");
		}
		if(ESAPI.validator().isValidInput("Days", object.getExemptionDays(), "Num", 3, true) == false){
			throw new ValidationException("Error in Exemption allowed for (in days) (3 digits max)");
		}
		//List<String>  blkTypeList = new ArrayList<String>();
		PreparedStatement stmt=null, stmt1=null;String validupto = "", validexemptionDays="";
		StringBuffer query1= new StringBuffer ("with t1 as (select rcn, BLKYEAR, to_char(action_date,'dd-mm-yyyy'), EXEMPTION_DAYS, "
				+ "to_char(action_date+EXEMPTION_DAYS,'dd-mm-yyyy'), case when trunc(action_date+EXEMPTION_DAYS) < trunc(sysdate) then 'Y' else 'N' end as"
				+ " exemption_expired, row_number() over (partition by rcn, BLKYEAR order by action_date desc ) rn "
				+ " from T_EXEMPTION_FOR_ANNUAL_RETURNS)select * from t1 where rn=1 and rcn = ? and blkyear=?");
		stmt1=connection.prepareStatement(query1.toString());
		StringBuffer query=new StringBuffer("INSERT INTO T_EXEMPTION_FOR_ANNUAL_RETURNS (RCN,BLKYEAR,EXEMPTION_DAYS,REMARKS,ACTION_DATE,ACTION_BY,RECORD_STATUS,INTIMATION_DATE)"
				+ "    VALUES(?,?,?,?,SYSDATE,?,0,NULL)");
		stmt = connection.prepareStatement(query.toString());
		String[]  ids = object.getBlockYear().split(",");
		for(int i=0;i<ids.length;i++){
			
			int count=0;	
			stmt1.setString(1, object.getApplicationId());
			stmt1.setString(2,  ids[i].toString());
			ResultSet rs=stmt1.executeQuery();
			 if(rs.next()){
				if(rs.getString(6).equals("N")){
					count++;					
				}			
			 }
			 if(count==0){					
					stmt.setString(1, object.getApplicationId());
					stmt.setString(2, ids[i].toString());
					stmt.setString(3, object.getExemptionDays());
					stmt.setString(4, object.getRemarks());
					stmt.setString(5, object.getMyUserId());
				    row=stmt.executeUpdate();
			 }			 
			 else{				
				 	row=0;
				 	throw new ValidationException("Exemption for <b>"+rs.getString(2)+"</b> has already been given to this association and is valid from <b>"+rs.getString(3)+"</b> to <b>"+rs.getString(5)+"</b>.");		 
			 } 
			 stmt.close();
			 stmt1.close();
		}
		// Sending Mail
	    if(row>0){
	    	 String selectedblockyearList = object.getBlockYear().toString().replace("[", "").replace("]", "").trim().replace(",","','");
	    	StringBuffer query5 = new StringBuffer("SELECT to_char(action_date+EXEMPTION_DAYS,'dd-mm-yyyy'),EXEMPTION_DAYS FROM T_EXEMPTION_FOR_ANNUAL_RETURNS WHERE RCN=? AND blkyear in ('"+selectedblockyearList+"')");
			PreparedStatement statement5 = connection.prepareStatement(query5.toString());		
			statement5.setString(1,object.getApplicationId());	
			//statement5.setString(2,ids[i].toString());			
			ResultSet rs2=statement5.executeQuery();
			if(rs2.next()){
				validupto=rs2.getString(1);
				validexemptionDays=rs2.getString(2);
				
			}
	    	StringBuffer query4 = new StringBuffer("SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE FROM FC_ASSO_DETAILS WHERE RCN=?");
			PreparedStatement statement4 = connection.prepareStatement(query4.toString());		
			statement4.setString(1,object.getApplicationId());				
			ResultSet rs1=statement4.executeQuery();
			String emailId=null,mobile=null;
			if(rs1.next()){
				 emailId=rs1.getString(1);
				 mobile= rs1.getString(2);	    					
			} 
			AutoNotifier notifier=new AutoNotifier();
			String mailContent="The FCRA portal for uploading Annual Returns of RCN "+object.getApplicationId()+" for year(s) ( "+object.getBlockYear()+" ) has been made available for "+validexemptionDays+" days i.e till "+validupto+" only.Please do the needful."; 		
	 		if(emailId!=null){
	 			notifyList=notifier.pushAutoNotifications(object.getApplicationId(), Integer.valueOf(7), "2", emailId, connection,myUserId,mailContent); 			
	 		}
	 		if(mobile!=null){
	 			notifier.setPhoneNumber(mobile);
	 			notifyList=notifier.pushAutoNotifications(object.getApplicationId(), Integer.valueOf(7), "1", "", connection,myUserId,mailContent);
	 		}  
		}	
		
		return row;	
	}
	
	public List<List2> getApplicationListDetailsExemption() throws Exception{
		StringBuffer query=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		query = new StringBuffer("SELECT rcn, " +
									" asso_name," +
									" valid_from, CURRENT_STATUS,"+ 
									" case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired,  "+
									" valid_to " +
									"FROM fc_india " +
									"WHERE rcn =? ");
								
		PreparedStatement stmt=connection.prepareStatement(query.toString());
		stmt.setString(1, appId);
        ResultSet rsQ=stmt.executeQuery();
        List<List2> list=new ArrayList<List2>();
        int recorFound=0;
		if(rsQ.next()) {
			recorFound++;
			list.add(new List2(rsQ.getString(1), rsQ.getString(2)));		
		}
		if(recorFound==0)
			throw new ValidationException("Exemption can be given only to those associations whose registration validity has already expired.");		
		return list;
	}
	//rcn search for advanced 
	
	public List<AbstractRequest> getApplicationListDetailsExemptionAnnual() throws Exception{

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
								"  WHERE "+queryField+ "" );		
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
	public List<Notification> getNotifyList() {
		return notifyList;
	}
	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
	}
	

	
	
	
}
