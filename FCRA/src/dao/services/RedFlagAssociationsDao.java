package dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import utilities.KVPair;
import utilities.ValidationException;
import utilities.communication.mail.MailScheduler;
import models.master.State;
import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
import dao.BaseDao;

public class RedFlagAssociationsDao extends BaseDao<RedFlagAssociations, String, String>{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String myUserId; 
	private String myOfficeCode;
	private int roleId;
    private String assoName;
    private String assoAddress;
    private String assoState;
    private String assoStateName;
    private String originatorOffice;
    private String originatorOrderNo ;
    private String originatorOrderDate;
    private String categoryCode;
    private String categoryDesc;
    private String remarks;
	public RedFlagAssociationsDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Integer insertRecord(RedFlagAssociations object) throws Exception {
		int rows=0;int rows1=0; int assoId=0;
		int status=0;
		roleId=16;//Temp Assume
		if(roleId==16){
			PreparedStatement statement1=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}	
			int count=0;
			String queryCheckAssoExist="";
			PreparedStatement stmt=null;
			if(object.getAssoState().equals("") || object.getAssoState()==null ){
				queryCheckAssoExist="SELECT count(1) FROM T_RED_FLAG_NGO_NAME where upper(ASSO_NAME)=UPPER(?) AND STATE is null ";
				 stmt = connection.prepareStatement(queryCheckAssoExist);
				 stmt.setString(1, object.getAssoName().trim());
			}
				
			else{
				 queryCheckAssoExist="SELECT count(1) FROM T_RED_FLAG_NGO_NAME where upper(ASSO_NAME)=UPPER(?) AND STATE=? ";
				 stmt = connection.prepareStatement(queryCheckAssoExist);
					stmt.setString(1, object.getAssoName().trim());
					stmt.setString(2, object.getAssoState());
			}
			ResultSet rsCheck=stmt.executeQuery();
			if(rsCheck.next()){
				count=rsCheck.getInt(1);
			}
			if(count>0){
				status=-2; //Association already add in adverse list 
			}
			stmt.close();
			
			if(status!=-2){
			String queryAssoId="SELECT NVL(MAX(ASSO_ID), 0)+1 FROM T_RED_FLAG_NGO_NAME";
			statement1 = connection.prepareStatement(queryAssoId);
			ResultSet rs=statement1.executeQuery();
			if(rs.next()){
				assoId=rs.getInt(1);	
			}
			
			StringBuffer query = new StringBuffer("INSERT INTO T_RED_FLAG_NGO_NAME(ASSO_ID,ASSO_NAME,STATE,ADDRESS,RECORD_STATUS) "
					+ " VALUES(?,?,?,?,0)");
			statement1 = connection.prepareStatement(query.toString());		
			statement1.setInt(1, assoId);			
			statement1.setString(2, object.getAssoName());
			statement1.setString(3, object.getAssoState());
			statement1.setString(4,object.getAssoAddress());
      	    rows=statement1.executeUpdate();
      	    statement1.close();
      	    if(rows>0){
				StringBuffer query1 = new StringBuffer("INSERT INTO T_RED_FLAG_NGO_STATUS_HISTORY"
						+ "(ASSO_ID,STATUS,REMARKS,STATUS_DATE,ACTION_BY,ORIGINATOR_OFFICE,ORIGINATOR_ORDER_NO,ORIGINATOR_ORDER_DATE,RED_FLAG_CATEGORY,FLAG_TYPE,ACTION_BY_OFFICE) "
						+ " VALUES(?,0,?,systimestamp,?,?,?,to_date(?, 'dd-mm-yyyy'),?,?,?)");
				statement1 = connection.prepareStatement(query1.toString());					
				statement1.setInt(1, assoId);
				statement1.setString(2, object.getRemarks());
				statement1.setString(3, myUserId);
				statement1.setString(4,object.getOriginatorOffice());
    			statement1.setString(5, object.getOriginatorOrderNo());            
				statement1.setString(6, object.getOriginatorOrderDate());//				
				statement1.setString(7, object.getCategoryCode());//
				statement1.setString(8, object.getFlagValue());
				statement1.setString(9, object.getMyOfficeCode());
				status=statement1.executeUpdate();
          	    statement1.close();
      	    }
			
			}
		}
		else{
			status=-1;
		}
		return status;
	
	}

	@Override
	public Integer removeRecord(RedFlagAssociations object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RedFlagAssociations> getAll() throws Exception {
		

		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }
		
		
		// StringBuffer query = new StringBuffer("select A.ASSO_ID,A.ASSO_NAME,A.ADDRESS,A.STATE,(SELECT C.SNAME FROM TM_STATE C WHERE C.SCODE=A.STATE),B.ORIGINATOR_OFFICE,to_char(B.ORIGINATOR_ORDER_DATE,'dd-mm-yyyy'),B.ORIGINATOR_ORDER_NO,B.RED_FLAG_CATEGORY,B.REMARKS,(Select D.CATEGORY_DESC from TM_RED_FLAG_CATEGORY D where D.CATEGORY_CODE=B.RED_FLAG_CATEGORY) from T_RED_FLAG_NGO_NAME A,T_RED_FLAG_NGO_STATUS_HISTORY B Where A.ASSO_ID=B.ASSO_ID and A.RECORD_STATUS=0 and B.STATUS=0");
		 StringBuffer query = new StringBuffer(" select A.ASSO_ID,A.ASSO_NAME,A.ADDRESS,A.STATE, "
			 		+ "(SELECT C.SNAME FROM TM_STATE C WHERE C.SCODE=A.STATE) as stateCode,B.ORIGINATOR_OFFICE,to_char(B.ORIGINATOR_ORDER_DATE,'dd-mm-yyyy'), "
			 		+ "B.ORIGINATOR_ORDER_NO,B.RED_FLAG_CATEGORY,B.REMARKS,(Select D.CATEGORY_DESC from TM_RED_FLAG_CATEGORY D  "
			 		+ " where D.CATEGORY_CODE=B.RED_FLAG_CATEGORY),(case when B.FLAG_TYPE ='2' then 'Yellow' ELSE case when B.FLAG_TYPE='1' then 'Red' END END )  as catogery_type ,to_char(B.Status_Date,'dd-mm-yyyy HH24:MI:SS'),b.ACTION_BY ||'('|| e.user_name||')' "
			 		+ " from T_RED_FLAG_NGO_NAME A,T_RED_FLAG_NGO_STATUS_HISTORY B,Tm_User e Where A.ASSO_ID=B.ASSO_ID  "
			 		+ "and A.RECORD_STATUS=0 and B.STATUS=0 AND b.action_by=E.USER_id  and B.STATUS_DATE=(SELECT MAX(STATUS_DATE) FROM T_RED_FLAG_NGO_STATUS_HISTORY WHERE STATUS=0 AND ASSO_ID=A.ASSO_ID) ");
		
		/* StringBuffer query = new StringBuffer(" with t1 as ( "
		 		+ "select  A.ASSO_ID,A.ASSO_NAME,A.ADDRESS,A.STATE,B.STATUS_DATE,  "
		 		+ "(SELECT C.SNAME FROM TM_STATE C WHERE C.SCODE=A.STATE) as SNAME,B.ORIGINATOR_OFFICE,to_char(B.ORIGINATOR_ORDER_DATE,'dd-mm-yyyy') as DATE1,  "
		 		+ "B.ORIGINATOR_ORDER_NO,B.RED_FLAG_CATEGORY,B.REMARKS,(Select D.CATEGORY_DESC from TM_RED_FLAG_CATEGORY D   "
		 		+ "where D.CATEGORY_CODE=B.RED_FLAG_CATEGORY) as CAT_DESC,case when B.RED_FLAG_CATEGORY is null then 'Yellow' else (select case when D.CATEGORY_TYPE=1 then 'Red' else case when  D.CATEGORY_TYPE=2 then 'Yellow' end end  from TM_RED_FLAG_CATEGORY D where D.CATEGORY_CODE=B.RED_FLAG_CATEGORY ) end as catogery_type  "
		 		+ "from T_RED_FLAG_NGO_NAME A,T_RED_FLAG_NGO_STATUS_HISTORY B Where A.ASSO_ID=B.ASSO_ID    "
		 		+ "and A.RECORD_STATUS=0 and B.STATUS=0),t2 as (select ASSO_ID,MAX(STATUS_DATE) as STATUS_DATE1 from T_RED_FLAG_NGO_STATUS_HISTORY where STATUS = 0 GROUP by ASSO_ID) "
		 		+ "select  F.ASSO_ID,F.ASSO_NAME,F.ADDRESS,F.STATE,F.SNAME,F.ORIGINATOR_OFFICE,F.DATE1, "
		 		+ " F.ORIGINATOR_ORDER_NO,F.RED_FLAG_CATEGORY,F.REMARKS,F.CAT_DESC,F.catogery_type  from t1 F,t2 P where F.ASSO_ID=P.ASSO_ID and F.STATUS_DATE = P.STATUS_DATE1") ;
				   */      
		 
		 
		 StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");		 
		 PreparedStatement statement = connection.prepareStatement(countQuery.toString());	
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
		 List<RedFlagAssociations> redFlagAssociationsTableList=new ArrayList<RedFlagAssociations>();
		 while(rs.next()) {	
			 redFlagAssociationsTableList.add(new RedFlagAssociations(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14)));	
		 }
		 return redFlagAssociationsTableList;

	}

	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			if(sortColumn != null && sortColumn.equals("") == false) {
				if(sortColumn.equals("statusDate")) {
					orderbyClause.append(" ORDER BY b.status_date ");
				}else if(sortColumn.equals("assoName")) {
					orderbyClause.append(" ORDER BY A.ASSO_NAME ");
				}else if(sortColumn.equals("assoStateName")) {
					orderbyClause.append(" ORDER BY stateCode ");
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
	
		public List<KVPair<String, String>> getKVList() throws Exception{
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			StringBuffer query = new StringBuffer("select category_code, category_desc from TM_RED_FLAG_CATEGORY");
			PreparedStatement statement = connection.prepareStatement(query.toString());				
			ResultSet rs = statement.executeQuery();
			List<KVPair<String, String>>  redFlagTypeList = new ArrayList<KVPair<String, String>>();
			while(rs.next()) {			
				KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
				redFlagTypeList.add(temp);
			}
			KVPair<String, String> temp1 = new KVPair<String, String>("NOCATEGORY","No Category");
			redFlagTypeList.add(temp1);							
			return redFlagTypeList;
		}	 

		public List<String> getRedFlagtypeList(String redFlagTypeList) throws Exception{
			List<String>  redFlagList = new ArrayList<String>();
			if (redFlagTypeList.contains("ALL")){
				redFlagList.add("ALL");
				 return redFlagList;
			}
			if (redFlagTypeList.contains("NOCATEGORY")){
				redFlagList.add("NOCATEGORY");
				 return redFlagList;
			}			
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			String query = "SELECT category_desc FROM TM_RED_FLAG_CATEGORY WHERE category_code IN ("+redFlagTypeList+")";
			PreparedStatement statement = connection.prepareStatement(query);				
			ResultSet rs = statement.executeQuery();
			
			while(rs.next()) {			
				redFlagList.add(rs.getString(1));			
			}
			return redFlagList;
		}		
	
	@Override
	public List<KVPair<String, String>> getKVList(List<RedFlagAssociations> list) {
		// TODO Auto-generated method stub
		return null;
	}
	public Integer removeRecord(String assoId,String deloriginatorOffice,String deloriginatorOrderNo,String deloriginatorOrderDate,String delremarkOriginatorOffice,String myOfficeCode) throws Exception {

		PreparedStatement statement1=null; int rows=0; int rows1=0; int rows2=0;
		if(connection == null) {
			throw new Exception("Invalid connection");
			
		}	
		connection.setAutoCommit(false);
			// Updating  RECORD_STATUS in TM_ROOM_TYPE			
			StringBuffer query = new StringBuffer("UPDATE T_RED_FLAG_NGO_NAME SET RECORD_STATUS=1 WHERE ASSO_ID=?");
				statement1 = connection.prepareStatement(query.toString());					
				statement1.setString(1, assoId);
				 rows1=statement1.executeUpdate();				
				statement1.close();
				/*if(rows1>0){
					StringBuffer query1 = new StringBuffer("UPDATE T_RED_FLAG_NGO_STATUS_HISTORY SET STATUS=1 WHERE ASSO_ID=?");
					statement1 = connection.prepareStatement(query1.toString());					
					statement1.setString(1, assoId);
					rows2=statement1.executeUpdate();				
					statement1.close();	
				}*/
				if (rows1 > 0) {
					StringBuffer query1 = new StringBuffer("INSERT INTO T_RED_FLAG_NGO_STATUS_HISTORY"
							+ "(ASSO_ID,STATUS,REMARKS,STATUS_DATE,ACTION_BY,ORIGINATOR_OFFICE,ORIGINATOR_ORDER_NO,ORIGINATOR_ORDER_DATE,RED_FLAG_CATEGORY,FLAG_TYPE,ACTION_BY_OFFICE) "
							+ " VALUES(?,1,?,systimestamp,?,?,?,to_date(?, 'dd-mm-yyyy'),?,?,?)");
					statement1 = connection.prepareStatement(query1.toString());					
					statement1.setString(1, assoId);
					statement1.setString(2, delremarkOriginatorOffice);
					statement1.setString(3, myUserId);
					statement1.setString(4,deloriginatorOffice);
	    			statement1.setString(5, deloriginatorOrderNo);            
					statement1.setString(6, deloriginatorOrderDate);//				
					statement1.setString(7, null);//
					statement1.setString(8, null);
					statement1.setString(9, myOfficeCode);
	          	    rows=statement1.executeUpdate();
	          	    statement1.close();
	      	    }
				
				 connection.commit();
				//Email Id 	
			       String  query2 = "SELECT EMAIL_ID,to_char(sysdate,'dd-mm-yyyy hh24:mi:ss') FROM TM_OFFICE WHERE OFFICE_CODE=?";
			  		PreparedStatement statement2 = connection.prepareStatement(query2.toString());		
			  		statement2.setString(1, myOfficeCode);
			  		ResultSet rs2=statement2.executeQuery();
			  		String email=null;
			  		String dateAndTime=null;
			  		if(rs2.next()){
			  			email=rs2.getString(1);
			  			dateAndTime=rs2.getString(2);
			  		}
			  		rs2.close();
			  		statement2.close();   
			      	
			  //	User information	
			  		String query3 = "SELECT USER_ID||'(' ||USER_NAME || ')[' ||(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=TM_USER.DESIGNATION_ID) "
							+ "|| ']' AS USERNAME  FROM TM_USER WHERE USER_ID=?";
					PreparedStatement statement3 = connection.prepareStatement(query3.toString());		
					statement3.setString(1, myUserId);
					ResultSet rs3=statement3.executeQuery();
					String userInfo=null;
					if(rs3.next()){
						userInfo=rs3.getString(1);
					}
					rs3.close();
					statement3.close();
					//Doner Detail
					  String  query4 = "select A.ASSO_NAME,B.SNAME,A.ADDRESS from T_RED_FLAG_NGO_NAME A ,TM_STATE B WHERE A.ASSO_ID=? and A.STATE=B.SCODE";
				  		PreparedStatement statement4 = connection.prepareStatement(query4.toString());		
				  		statement4.setString(1, assoId);
				  		ResultSet rs4=statement4.executeQuery();
				  		String assoName1=null;
				  		String assoState1=null;
				  		String assoAdd=null;
				  		if(rs4.next()){
				  			assoName1=rs4.getString(1);
				  			assoState1=rs4.getString(2);
				  			assoAdd=rs4.getString(3);
				  		}
				  		rs4.close();
				  		statement4.close();   
				      	
					
					
			      	// Sending Mail to JSF	
			    		String mailContent="This is to inform you that the following Association has been removed from Adverse list by <b>"+userInfo+"</b>  on <b>"+dateAndTime+"</b>.<br/><br/>"
			    				+ "Association Name: <b>"+assoName1+"</b><br/> State: <b>"+assoState1+"</b><br/>Address: <b>"+assoAdd+"</b><br/> "
			    						+ " Remark :<b>"+delremarkOriginatorOffice+"</b> <br/><br/> Note:This Email is system generated. Please do not reply to this email ID";			
			    		//List<Notification> notifyList=notifier.pushAutoNotifications(appId, Integer.valueOf(6), "2", email, connection,myOfficeCode,mailContent);    
			    		MailScheduler.schedule("no-reply-MHAFCRA@nic.in", email, "Red Flag Entry Removal - Intimation under FCRA",mailContent, null, null, null, connection);
			    	
				
				return rows;
	
	}
	public int findRoleId(String userId) throws SQLException{
		StringBuffer query = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE where USER_ID=? AND (ROLE_ID=16 OR ROLE_ID=17) and RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, userId);
		ResultSet rs=statement.executeQuery();
		int addRoleId=0;
		while(rs.next()){
			addRoleId=addRoleId+rs.getInt(1);
		}
	
	  return addRoleId;	
	}
	public int yellowFlagRole(String userid) throws SQLException{
		StringBuffer query = new StringBuffer("SELECT  ROLE_ID FROM TM_USER_ROLE where USER_ID=? AND  (ROLE_ID=18 OR ROLE_ID=15) and RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, userid);
		ResultSet rs=statement.executeQuery();
		int yellowFlag=0;
		while(rs.next()){
			yellowFlag=yellowFlag+rs.getInt(1);
		}
	
	  return yellowFlag;	
	}
	
	public String insertRecordAddingYellow(RedFlagAssociations object) throws Exception {
		PreparedStatement statement=null;
		int checkflag=0,rows=0;
		PreparedStatement statement1=null;
		String remark1=null, user1=null, office1=null,order1=null,date1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		StringBuffer query2 = new StringBuffer("SELECT FLAG_TYPE,REMARKS,ACTION_BY,ORIGINATOR_OFFICE ,ORIGINATOR_ORDER_NO,to_char(ORIGINATOR_ORDER_DATE,'dd-mm-yyyy') FROM T_RED_FLAG_NGO_STATUS_HISTORY  WHERE ASSO_ID=?");
		statement = connection.prepareStatement(query2.toString());		
		statement.setString(1, object.getAssoId());
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			checkflag=rs.getInt(1);
			remark1=rs.getString(2);
			user1=rs.getString(3);
			office1=rs.getString(4);
			order1=rs.getString(5);
			date1=rs.getString(6);
		}
		if(checkflag==2){
			removeRecord(object.getAssoId(),object.getOriginatorOffice(),object.getOriginatorOrderNo(),object.getOriginatorOrderDate(),"Removing from Yellow flag and Upgrading to Red Flag",object.getMyOfficeCode());
			
		}
		StringBuffer query = new StringBuffer("UPDATE  T_RED_FLAG_NGO_NAME SET RECORD_STATUS=0 WHERE ASSO_ID=?");
		statement1 = connection.prepareStatement(query.toString());		
		statement1.setString(1,  object.getAssoId());			
  	    rows=statement1.executeUpdate();
  	    statement1.close();
  	    if(rows>0){
			StringBuffer query1 = new StringBuffer("INSERT INTO T_RED_FLAG_NGO_STATUS_HISTORY"
					+ "(ASSO_ID,STATUS,REMARKS,STATUS_DATE,ACTION_BY,ORIGINATOR_OFFICE,ORIGINATOR_ORDER_NO,ORIGINATOR_ORDER_DATE,RED_FLAG_CATEGORY,FLAG_TYPE,ACTION_BY_OFFICE) "
					+ " VALUES(?,0,?,systimestamp,?,?,?,to_date(?, 'dd-mm-yyyy'),?,?,?)");
			statement1 = connection.prepareStatement(query1.toString());					
			statement1.setString(1,  object.getAssoId());
			statement1.setString(2, object.getRemarks());
			statement1.setString(3, myUserId);
			statement1.setString(4,object.getOriginatorOffice());
			statement1.setString(5, object.getOriginatorOrderNo());            
			statement1.setString(6, object.getOriginatorOrderDate());//	
			statement1.setString(7, object.getCategoryCode());//	
			statement1.setString(8, object.getFlagValue());
			statement1.setString(9, object.getMyOfficeCode());
			statement1.executeUpdate();
      	    statement1.close();
  	    }
		return "success";
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
	public String getAssoName() {
		return assoName;
	}

	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}

	public String getAssoAddress() {
		return assoAddress;
	}

	public void setAssoAddress(String assoAddress) {
		this.assoAddress = assoAddress;
	}

	public String getAssoState() {
		return assoState;
	}

	public void setAssoState(String assoState) {
		this.assoState = assoState;
	}

	public String getAssoStateName() {
		return assoStateName;
	}

	public void setAssoStateName(String assoStateName) {
		this.assoStateName = assoStateName;
	}

	public String getOriginatorOffice() {
		return originatorOffice;
	}

	public void setOriginatorOffice(String originatorOffice) {
		this.originatorOffice = originatorOffice;
	}

	public String getOriginatorOrderNo() {
		return originatorOrderNo;
	}

	public void setOriginatorOrderNo(String originatorOrderNo) {
		this.originatorOrderNo = originatorOrderNo;
	}

	public String getOriginatorOrderDate() {
		return originatorOrderDate;
	}

	public void setOriginatorOrderDate(String originatorOrderDate) {
		this.originatorOrderDate = originatorOrderDate;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getMyUserId() {
		return myUserId;
	}

	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}

	public String getMyOfficeCode() {
		return myOfficeCode;
	}

	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}

	
}
