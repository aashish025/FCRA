package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utilities.KVPair;
import utilities.RandomString;
import utilities.ValidationException;
import utilities.lists.List2;
import models.master.AdminUser;
import models.master.Designation;
import models.master.Gender;
import models.master.Office;
import models.master.User;
import models.master.UserStatus;
import models.services.Notification;
import dao.BaseDao;

public class UserDao extends BaseDao<User, String, String>{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String userid;
	private String userpwd;
	private String officeCode;
	private String assignedRoles;
	private String myOfficeCode;
	private String assignedSection;
	
	public UserDao(Connection connection) {
		super(connection);
	}
	@Override
	public List<User> getAll() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT A.USER_ID,A.USER_NAME,A.EMAIL_ID,C.GENDER_CODE,C.GENDER_DESC, "
							+ "E.DESIGNATION_ID,E.DESIGNATION_NAME,D.STATUS_NAME FROM TM_USER A,TM_GENDER C,TM_USERSTATUS D,TM_DESIGNATION E "+
							" WHERE A.OFFICE_CODE=? AND A.USER_ID!=? AND A.GENDER_CODE=C.GENDER_CODE AND A.STATUS_ID=D.STATUS_ID"
							+ " AND A.DESIGNATION_ID=E.DESIGNATION_ID AND A.STATUS_ID NOT IN(1) AND A.USER_ID NOT IN('nic')");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		PreparedStatement statement = connection.prepareStatement(countQuery.toString());	
		statement.setString(1, officeCode);
		statement.setString(2, officeCode);
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
			statement.setString(1, officeCode);
			statement.setString(2, officeCode);
			statement.setInt(3, (pageRequested-1) * pageSize + pageSize);
			statement.setInt(4, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		List<User> userList = new ArrayList<User>();
		while(rs.next()) {			
			userList.add(new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8)));			
		}
		return userList;
	}
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("userid")) {
				orderbyClause.append(" ORDER BY USER_ID");
			}else if(sortColumn.equals("userName")) {
				orderbyClause.append(" ORDER BY USER_NAME");
			}
			else if(sortColumn.equals("officeName")) {
				orderbyClause.append(" ORDER BY a.OFFICE_NAME");
			}else if(sortColumn.equals("officeType")) {
				orderbyClause.append(" ORDER BY a.COUNTRY_CODE");
			}else if(sortColumn.equals("countryCode")) {
				orderbyClause.append(" ORDER BY c.OFFICE_TYPE");
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
	public Integer insertRecord(User object) throws Exception {
			PreparedStatement statement=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}
			if(checkEmail(object.getEmailId())==true){
				throw new ValidationException("User is already exist with Email Id <b>[ "+object.getEmailId()+" ]</b>.Please create user with "
					+ "some other Email Id");
			}
			// Inserting  in TM_USER
			String userId=generateUserId(object.getOfficeCode());			
			RandomString rs=new RandomString();
			String pwd=rs.getRandomString(8);
			String md5pwd=generateMD5Pwd(pwd);
			setUserid(userId);
			setUserpwd(pwd);			
			StringBuffer query = new StringBuffer("INSERT INTO TM_USER(USER_ID,USERPWD,EMAIL_ID,OFFICE_CODE,USER_LEVEL,USER_NAME,"
					+ "DESIGNATION_ID,GENDER_CODE,PWD_DATE,STATUS_ID,STATUS_DATE,STATUS_BY,STATUS_IP,CREATED_BY,CREATED_IP,"
					+ "CREATED_DATE) VALUES(?,?,?,?,?,?,?,?,(sysdate-31),?,sysdate,?,?,?,?,sysdate)");
				statement = connection.prepareStatement(query.toString());
				statement.setString(1, userId);
				statement.setString(2, md5pwd);
				statement.setString(3, object.getEmailId());
				statement.setString(4, object.getOfficeCode());
				statement.setInt(5, object.getUserLevel());			
				statement.setString(6, object.getUserName());
				statement.setString(7, object.getDesignationId());
				statement.setString(8, object.getGenderCode());
				statement.setString(9, object.getStatusId());
				statement.setString(10, object.getActionBy());
				statement.setString(11, object.getActionIp());
				statement.setString(12, object.getCreatedBy());
				statement.setString(13, object.getCreatedIp());
				int rows=statement.executeUpdate();
				statement.close();				
		return rows;			
	}
	public Boolean checkCode(User Object) throws Exception{
		PreparedStatement statement=null;
		Boolean flag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM TM_PC_SECTION WHERE OFFICE_CODE=? AND RECORD_STATUS=0");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, myOfficeCode);
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			flag=true;
		}				
		return flag;		
	}
	private Boolean checkEmail(String email,String userId) throws Exception{
		PreparedStatement statement=null;
		Boolean flag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM TM_USER WHERE EMAIL_ID=? AND USER_ID!=? And STATUS_ID=0");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, email);
		statement.setString(2, userId);
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			flag=true;
		}				
		return flag;		
	}
	private Boolean checkEmail(String email) throws Exception{
		PreparedStatement statement=null;
		Boolean flag=false;
		StringBuffer query = new StringBuffer("SELECT * FROM TM_USER WHERE EMAIL_ID=? and STATUS_ID=0");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, email);		
		ResultSet rs=statement.executeQuery();
		if(rs.next()){
			flag=true;
		}				
		return flag;		
	}
	public String editUser(User object) throws Exception {
			PreparedStatement statement=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}
			if(checkEmail(object.getEmailId(),object.getUserid())==true){
				throw new ValidationException("User is already exist with Email Id <b>[ "+object.getEmailId()+" ]</b>.Please provide some other Email Id");
			}
			connection.setAutoCommit(false);		
			StringBuffer query=new StringBuffer("UPDATE TM_USER SET USER_NAME=?,EMAIL_ID=?,GENDER_CODE=?,DESIGNATION_ID=?"
					+ ",STATUS_DATE=sysdate,STATUS_BY=?,STATUS_IP=? WHERE USER_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, object.getUserName());
			statement.setString(2, object.getEmailId());
			statement.setString(3, object.getGenderCode());
			statement.setString(4, object.getDesignationId());
			statement.setString(5, object.getActionBy());
			statement.setString(6, object.getActionIp());
			statement.setString(7, object.getUserid());
			statement.executeUpdate();
			statement.close();	
			connection.commit();
		return "success";			
	}	
	public String resetPassword(User object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}		
		RandomString rs=new RandomString();
		String pwd=rs.getRandomString(8);
		String md5pwd=generateMD5Pwd(pwd);		
		setUserpwd(pwd);
		StringBuffer query=new StringBuffer("UPDATE TM_USER SET USERPWD=?,PWD_DATE=(sysdate-31),STATUS_DATE=sysdate,STATUS_BY=?,STATUS_IP=? WHERE USER_ID=?");
		statement = connection.prepareStatement(query.toString());
		statement.setString(1, md5pwd);	
		statement.setString(2, object.getActionBy());
		statement.setString(3, object.getActionIp());
		statement.setString(4, userid);
		statement.executeUpdate();
		statement.close();		
		return "success";			
	}	
	public String unlockUser(User object) throws Exception {
			PreparedStatement statement=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}			
			StringBuffer query=new StringBuffer("UPDATE TM_USER SET STATUS_ID=0, WRONG_PWD_COUNT=0,STATUS_DATE=sysdate,STATUS_BY=?,STATUS_IP=? WHERE USER_ID=?");
			statement = connection.prepareStatement(query.toString());						
			statement.setString(1, object.getActionBy());
			statement.setString(2, object.getActionIp());
			statement.setString(3, userid);
			statement.executeUpdate();
			statement.close();			
		return "success";			
	}	
	public String deleteUser(User object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query=new StringBuffer("UPDATE TM_USER SET STATUS_ID=1,STATUS_DATE=sysdate,STATUS_BY=?,STATUS_IP=? WHERE USER_ID=?");
		statement = connection.prepareStatement(query.toString());						
		statement.setString(1, object.getActionBy());
		statement.setString(2, object.getActionIp());
		statement.setString(3, userid);
		statement.executeUpdate();
		statement.close();			
	return "success";			
}	
	private String generateUserId(String officeCode) throws Exception {
		String val=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_USERID(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officeCode);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			 val = rs.getString(1);
		}	
		return val;
	}
	private String generateMD5Pwd(String pwd) throws Exception {
		String val=null;
		StringBuffer query = new StringBuffer("SELECT FN_GEN_MD5(?) FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, pwd);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			 val = rs.getString(1);
		}	
		return val;
	}
	public List<KVPair<String, String>> getAvailableUserRole() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT distinct A.ROLE_ID,A.ROLE_NAME FROM TM_ROLE A,TM_USER_LEVEL_ROLE B,TM_USER C "+ 
							"WHERE A.ROLE_ID=B.ROLE_ID AND B.USER_LEVEL_ID=C.USER_LEVEL and c.user_id=? "
							+ "AND A.ROLE_ID NOT IN(SELECT ROLE_ID FROM TM_USER_ROLE WHERE USER_ID=?) ORDER BY ROLE_NAME ASC");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, userid);
		statement.setString(2, userid);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  availableRoleList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			availableRoleList.add(temp);			
		}
		return availableRoleList;
	}
	public List<KVPair<String, String>> getAssignedUserRole() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT distinct A.ROLE_ID,A.ROLE_NAME FROM TM_ROLE A,TM_USER_ROLE B "+
							"WHERE A.ROLE_ID=B.ROLE_ID AND B.USER_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());	
		statement.setString(1, userid);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  assignedRoleList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assignedRoleList.add(temp);			
		}
		return assignedRoleList;
	}
	public List<KVPair<String, String>> getAvailableSection() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SECTION_ID, SECTION_NAME FROM TM_PC_SECTION  WHERE OFFICE_CODE=? AND RECORD_STATUS=0 AND SECTION_ID NOT IN (SELECT SECTION_ID FROM TM_USER_SECTION WHERE USER_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, myOfficeCode);
		statement.setString(2, userid);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  availableSectionList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			availableSectionList.add(temp);			
		}
		return availableSectionList;
	}
	public List<KVPair<String, String>> getAssignedSection() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SECTION_ID,(SELECT SECTION_NAME FROM TM_PC_SECTION WHERE OFFICE_CODE=? AND SECTION_ID=TM_USER_SECTION.SECTION_ID) FROM TM_USER_SECTION WHERE USER_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, myOfficeCode);
		statement.setString(2, userid);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  assignedSectionList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assignedSectionList.add(temp);			
		}
		return assignedSectionList;
	}
	public String saveUserRoles(User object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		if((assignedRoles.equals("null"))){
			StringBuffer query=new StringBuffer("DELETE FROM TM_USER_ROLE WHERE USER_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, userid);
			statement.executeUpdate();
			statement.close();
		}else{
			StringTokenizer ids = new StringTokenizer(assignedRoles, ",");
			List<Short> updatedAssigendList = new ArrayList<Short>();
			int i;
			while (ids.hasMoreTokens()) {
				Short menuId = Short.parseShort(ids.nextToken().trim());
				updatedAssigendList.add(menuId);
			}	
			StringBuffer query=new StringBuffer("DELETE FROM TM_USER_ROLE WHERE USER_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, userid);
			statement.executeUpdate();
			statement.close();
			StringBuffer query1 = new StringBuffer("INSERT INTO TM_USER_ROLE(USER_ID,ROLE_ID,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,"
					+ "CREATED_IP,CREATED_DATE) "
					+ " VALUES(?,?,0,0,?,?,sysdate)");
			int count=updatedAssigendList.size();		
			for(i=0;i<count;i++){
				 statement = connection.prepareStatement(query1.toString());		 
				 statement.setString(1, userid);	
				 statement.setShort(2, updatedAssigendList.get(i));
				 statement.setString(3, object.getCreatedBy());
				 statement.setString(4, object.getCreatedIp());
				 statement.executeUpdate();
			}	
			connection.commit();
		}		 
		return "success";			
	}	
	
	public String saveSection(User object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		if((assignedSection.equals("null"))){
			StringBuffer query=new StringBuffer("DELETE FROM TM_USER_SECTION WHERE USER_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, userid);
			statement.executeUpdate();
			statement.close();
		}else{
			StringTokenizer ids = new StringTokenizer(assignedSection, ",");
			List<Short> updatedAssigendList = new ArrayList<Short>();
			int i;
			while (ids.hasMoreTokens()) {
				Short menuId = Short.parseShort(ids.nextToken().trim());
				updatedAssigendList.add(menuId);
			}	
			StringBuffer query=new StringBuffer("DELETE FROM TM_USER_SECTION WHERE USER_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setString(1, userid);
			statement.executeUpdate();
			statement.close();
			StringBuffer query1 = new StringBuffer("INSERT INTO TM_USER_SECTION(USER_ID,SECTION_ID,OFFICE_CODE)"
					+ " VALUES(?,?,?)");
			int count=updatedAssigendList.size();		
			for(i=0;i<count;i++){
				 statement = connection.prepareStatement(query1.toString());		 
				 statement.setString(1, userid);	
				 statement.setShort(2, updatedAssigendList.get(i));
				 statement.setString(3,myOfficeCode);
				 statement.executeUpdate();
			}	
			connection.commit();
		}		 
		return "success";			
	}	
	@Override
	public Integer removeRecord(User object) throws Exception {
		/*PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
			// Updating  RECORD_STATUS in T_NOTIFICATION_DETAILS			
			StringBuffer query = new StringBuffer("UPDATE T_NOTIFICATION_DETAILS SET RECORD_STATUS=1 WHERE NOTIFICATION_ID=?");
				statement = connection.prepareStatement(query.toString());					
				statement.setString(1, object.getNotificationId());
				int rows=statement.executeUpdate();				
				statement.close();
				
			// Updating  RECORD_STATUS in T_NOTIFICATION_DOCUMENT			
			StringBuffer query1 = new StringBuffer("UPDATE T_NOTIFICATION_DOCUMENT SET RECORD_STATUS=1 WHERE NOTIFICATION_ID=?");
				statement = connection.prepareStatement(query1.toString());					
				statement.setString(1, object.getNotificationId());	
				statement.executeUpdate();
				statement.close();		*/
		return null;
	}
	// for Admin User
	public List<AdminUser> getAdminUser() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select a.OFFICE_NAME, c.OFFICE_NAME AS ONAME, a.COUNTRY_CODE, b.USER_ID, b.STATUS_ID, d.CTR_NAME FROM TM_OFFICE a , TM_USER b,TM_OFFICETYPE c,TM_COUNTRY d  WHERE a.OFFICE_CODE=b.OFFICE_CODE AND a.OFFICE_ID=c.OFFICE_ID AND b.USER_LEVEL in(1,3,5) AND a.COUNTRY_CODE=d.CTR_CODE");
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
		else {
			statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
			statement.setInt(2, (pageRequested-1) * pageSize + 1);
		}
		rs = statement.executeQuery();
		List<AdminUser> adminList = new ArrayList<AdminUser>();
		while(rs.next()) {			
			adminList.add(new AdminUser(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));			
		}
		return adminList;
	}
		@Override
	public List<KVPair<String, String>> getKVList(List<User> list) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<KVPair<String, String>> getGenderList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT GENDER_CODE,GENDER_DESC FROM TM_GENDER WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  genderList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			genderList.add(temp);			
		}
		return genderList;
	}
	public List<KVPair<String, String>> getDesginationList(String officeId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT A.DESIGNATION_ID,A.DESIGNATION_NAME FROM TM_DESIGNATION A,TM_DESIGNATION_OFFICETYPE B "
				+ " WHERE A.RECORD_STATUS=0 AND B.OFFICE_ID=?  AND A.DESIGNATION_ID=B.DESIGNATION_ID");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officeId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  designationList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			designationList.add(temp);			
		}
		return designationList;
	}
	public List<KVPair<String, String>> getAllUserList(String officecode) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select USER_ID,USER_NAME from TM_USER where OFFICE_CODE=?  and USER_LEVEL NOT IN(1,3,5) ORDER BY USER_NAME");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officecode);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  designationList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(1)+"[ "+rs.getString(2)+"]");
			designationList.add(temp);			
		}
		return designationList;
	}
	
	/**
	 * Use Only Application Authority Management
	 * Not get Deleted User
	 * @param officecode
	 * @return
	 * @throws Exception
	 */
	public List<KVPair<String, String>> getForwardUserList(String officecode) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select USER_ID,USER_NAME from TM_USER where OFFICE_CODE=? and STATUS_ID NOT IN(1) and USER_LEVEL NOT IN(1,3,5) ORDER BY USER_NAME");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officecode);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  forwardinguserlist = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(1)+"[ "+rs.getString(2)+"]");
			forwardinguserlist.add(temp);			
		}
		return forwardinguserlist;
	}
	
	//// for report
	public List<KVPair<String, String>> getKVList(String officecode,String userId) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
	
		StringBuffer query = new StringBuffer("select USER_ID,USER_NAME ||'[' || USER_ID ||']' from tm_user WHERE OFFICE_CODE=? AND user_id!=? ORDER BY user_name");
	
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, officecode);
		statement.setString(2,userId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  userList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			userList.add(temp);			
		}
		return userList;
	}
	
	// FOR USER AUDIT
	 public List<KVPair<String, String>> getUserAuditList() throws Exception{
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			StringBuffer query = new StringBuffer("select user_id, user_id ||'[' || user_name ||']'||(CASE WHEN status_id=1 THEN 'DELETED' ELSE '' END)  from tm_user order by user_id");
			PreparedStatement statement = connection.prepareStatement(query.toString());				
			ResultSet rs = statement.executeQuery();
			List<KVPair<String, String>>  userAuditList = new ArrayList<KVPair<String, String>>();
			while(rs.next()) {			
				KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
				userAuditList.add(temp);			
			}
			return userAuditList;
		}
	
	//PDF SELECTED LIST
	public String getUser(String usrList) throws Exception {
		   if(usrList.contains("'ALL'")){
			   return "ALL";
		   }
		   else{	if(connection == null) {
					throw new Exception("Invalid connection");
				}
				String userList=null;		

				StringBuffer query = new StringBuffer("select LISTAGG(USER_NAME ||'[' || USER_ID ||']',', ') WITHIN GROUP (ORDER BY USER_NAME) from tm_user WHERE user_id IN ("+usrList+")");
				PreparedStatement statement = connection.prepareStatement(query.toString());

            
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					userList = rs.getString(1);
				}
				return userList.toString();
			
			  }
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
	public void setAssignedSection(String assignedSection) {
		this.assignedSection = assignedSection;
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
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	public String getAssignedRoles() {
		return assignedRoles;
	}
	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getMyOfficeCode() {
		return myOfficeCode;
	}
	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}
}
