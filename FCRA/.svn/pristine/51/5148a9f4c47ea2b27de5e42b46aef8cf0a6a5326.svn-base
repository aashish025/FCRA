package dao.master;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utilities.KVPair;
import models.master.Role;
import dao.BaseDao;

public class RoleDao extends BaseDao<Role, String, String> {
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private Short roleId;
	private String roleName;
	private String assignedRoles;
	private String smenuId;
	private String assignedUser;
	

	public RoleDao(Connection connection) {
		super(connection);
	}
	
	public List<KVPair<String, String>> getKVList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT ROLE_ID,ROLE_NAME ,CREATED_DATE FROM TM_ROLE");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  roleTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			roleTypeList.add(temp);			
		}
		return roleTypeList;
	}
	
	public List<Role> gettable() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT ROLE_ID,ROLE_NAME,to_char(CREATED_DATE,'dd-mm-yyyy') FROM TM_ROLE WHERE RECORD_STATUS=0");
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
		
		List<Role> roletypeList = new ArrayList<Role>();
		while(rs.next()) {			
			roletypeList.add(new Role(rs.getShort(1),rs.getString(2),rs.getString(3)));			
		}
		return roletypeList;
	}
	private String preparePagingQuery(StringBuffer query) {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("roleId")) {
				orderbyClause.append(" ORDER BY ROLE_ID");
			}else if(sortColumn.equals("roleName")) {
				orderbyClause.append(" ORDER BY ROLE_NAME");
			}else if(sortColumn.equals("createdDate")) {
				orderbyClause.append(" ORDER BY CREATED_DATE");
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
					+ "T2 AS (SELECT * FROM (SELECT T1.*, ROWNUM RN FROM T1) WHERE RN<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	private Short generateroleId() throws Exception {
		Short val=null;
		StringBuffer query = new StringBuffer("SELECT NVL(MAX(ROLE_ID), 0)+1 FROM TM_ROLE");
		PreparedStatement statement = connection.prepareStatement(query.toString());		
		ResultSet rs = statement.executeQuery();
		if(rs.next())
			 val=rs.getShort(1);
		return val;
	}

	@Override
	public Integer insertRecord(Role object) throws Exception {
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		roleId=generateroleId();	
	    setRoleId(roleId);
		StringBuffer query = new StringBuffer("INSERT INTO TM_ROLE(ROLE_ID,ROLE_NAME,RECORD_STATUS,CREATED_BY,CREATED_IP,CREATED_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_IP,LAST_MODIFIED_DATE)VALUES(?,?,0,?,?,sysdate,?,?,sysdate)");
			statement1 = connection.prepareStatement(query.toString());	
			statement1.setShort(1, roleId);
			statement1.setString(2, object.getRoleName());
			statement1.setString(3, object.getCreatedBy());
			statement1.setString(4, object.getCreatedIp());	
			statement1.setString(5,object.getLastModifiedBy());
			statement1.setString(6, object.getLastModifiedIp());
			int rows=statement1.executeUpdate();
			statement1.close();
			return rows;
}
	public List<KVPair<String, String>> getAvailableRole() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SMENU_ID, SMENU_NAME FROM TM_SUBMENU WHERE SMENU_ID NOT IN (SELECT SMENU_ID FROM TM_ROLE_MENU WHERE ROLE_ID=?)");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setShort(1, roleId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  availableRoleList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			availableRoleList.add(temp);			
		}
		return availableRoleList;
	}
      public List<KVPair<String, String>> getAssignedRole() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SMENU_ID,(SELECT SMENU_NAME FROM TM_SUBMENU WHERE SMENU_ID=TM_ROLE_MENU.SMENU_ID) FROM TM_ROLE_MENU WHERE ROLE_ID=?");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setShort(1, roleId);
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  assignedRoleList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			assignedRoleList.add(temp);			
		}
		return assignedRoleList;
	}
      public List<KVPair<String, String>> getAvailableUser() throws Exception{
  		if(connection == null) {
  			throw new Exception("Invalid connection");
  		}				
  		StringBuffer query = new StringBuffer("SELECT USER_LEVEL_ID, USER_LEVEL_NAME FROM TM_USER_LEVEL WHERE USER_LEVEL_ID NOT IN (SELECT USER_LEVEL_ID FROM TM_USER_LEVEL_ROLE WHERE ROLE_ID=?)");
  		PreparedStatement statement = connection.prepareStatement(query.toString());
  		statement.setShort(1, roleId);
  		ResultSet rs = statement.executeQuery();
  		List<KVPair<String, String>>  availableUserList = new ArrayList<KVPair<String, String>>();
  		while(rs.next()) {			
  			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
  			availableUserList.add(temp);			
  		}
  		return availableUserList;
  	}
        public List<KVPair<String, String>> getAssignedUser() throws Exception{
  		if(connection == null) {
  			throw new Exception("Invalid connection");
  		}				
  		StringBuffer query = new StringBuffer("SELECT USER_LEVEL_ID,(SELECT USER_LEVEL_NAME FROM TM_USER_LEVEL WHERE USER_LEVEL_ID=TM_USER_LEVEL_ROLE.USER_LEVEL_ID) FROM TM_USER_LEVEL_ROLE WHERE ROLE_ID=?");
  		PreparedStatement statement = connection.prepareStatement(query.toString());
  		statement.setShort(1, roleId);
  		ResultSet rs = statement.executeQuery();
  		List<KVPair<String, String>>  assignedUserList = new ArrayList<KVPair<String, String>>();
  		while(rs.next()) {			
  			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
  			assignedUserList.add(temp);			
  		}
  		return assignedUserList;
  	}
      
      public Integer editRecord(Role object) throws Exception {
  		PreparedStatement statement1=null;
  		if(connection == null) {
  			throw new Exception("Invalid connection");
  		}			
  			StringBuffer query = new StringBuffer("UPDATE TM_ROLE SET ROLE_NAME=?, LAST_MODIFIED_BY=?"
  					+ ", LAST_MODIFIED_IP=?,LAST_MODIFIED_DATE=sysdate WHERE ROLE_ID=?");
  		
  				statement1 = connection.prepareStatement(query.toString());				
  				statement1.setString(1,object.getRoleName());					
  				statement1.setString(2,object.getLastModifiedBy());
  				statement1.setString(3, object.getLastModifiedIp());
  				statement1.setShort(4, object.getRoleId());
  				int rows=statement1.executeUpdate();				
  				statement1.close();
  				return rows;			
  	}	

	public String saveRoles(Role object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		if((assignedRoles.equals("null"))){
			StringBuffer query=new StringBuffer("DELETE FROM TM_ROLE_MENU WHERE ROLE_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setShort(1, roleId);
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
			StringBuffer query=new StringBuffer("DELETE FROM TM_ROLE_MENU WHERE ROLE_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setShort(1, roleId);
			statement.executeUpdate();
			statement.close();
			StringBuffer query1 = new StringBuffer("INSERT INTO TM_ROLE_MENU(ROLE_ID,SMENU_ID,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,"
					+ "CREATED_IP,CREATED_DATE) "
					+ " VALUES(?,?,0,0,?,?,sysdate)");
			int count=updatedAssigendList.size();
			for(i=0;i<count;i++){
				 statement = connection.prepareStatement(query1.toString());		 
				 statement.setShort(1, roleId);	
				 statement.setShort(2,updatedAssigendList.get(i));
				 statement.setString(3, object.getCreatedBy());
				 statement.setString(4, object.getCreatedIp());
				 statement.executeUpdate();
			}	
			connection.commit();
		}		 
		return "success";			
		
	}
	public String saveUser(Role object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		connection.setAutoCommit(false);
		if((assignedUser.equals("null"))){
			StringBuffer query=new StringBuffer("DELETE FROM TM_USER_LEVEL_ROLE WHERE ROLE_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setShort(1, roleId);
			statement.executeUpdate();
			statement.close();
		}else{

			StringTokenizer ids = new StringTokenizer(assignedUser, ",");
			List<Short> updatedAssigendList = new ArrayList<Short>();
			int i;
			while (ids.hasMoreTokens()) {
				Short menuId = Short.parseShort(ids.nextToken().trim());
				updatedAssigendList.add(menuId);
			}	
			StringBuffer query=new StringBuffer("DELETE FROM TM_USER_LEVEL_ROLE WHERE ROLE_ID=?");
			statement = connection.prepareStatement(query.toString());
			statement.setShort(1, roleId);
			statement.executeUpdate();
			statement.close();
			StringBuffer query1 = new StringBuffer("INSERT INTO TM_USER_LEVEL_ROLE(ROLE_ID,USER_LEVEL_ID,DISPLAY_ORDER,RECORD_STATUS,CREATED_BY,"
					+ "CREATED_IP,CREATED_DATE) "
					+ " VALUES(?,?,0,0,?,?,sysdate)");
			int count=updatedAssigendList.size();
			for(i=0;i<count;i++){
				 statement = connection.prepareStatement(query1.toString());		 
				 statement.setShort(1, roleId);	
				 statement.setShort(2,updatedAssigendList.get(i));
				 statement.setString(3, object.getCreatedBy());
				 statement.setString(4, object.getCreatedIp());
				 statement.executeUpdate();
			}	
			connection.commit();
		}
			return "success";	
		}		 
				
	

	public String deleterole(Role object) throws Exception {
		PreparedStatement statement=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query=new StringBuffer("UPDATE TM_USER_LEVEL_ROLE SET RECORD_STATUS=1 WHERE ROLE_ID=?");
	    statement = connection.prepareStatement(query.toString());
	    statement.setShort(1, roleId);
		statement.executeUpdate();
		statement.close();	
		PreparedStatement statement1=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query1=new StringBuffer("UPDATE TM_ROLE_MENU  SET RECORD_STATUS=1 WHERE ROLE_ID=?");
		statement1 = connection.prepareStatement(query1.toString());
        statement1.setShort(1, roleId);
		statement1.executeUpdate();
		statement1.close();	
		PreparedStatement statement2=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		StringBuffer query2=new StringBuffer("UPDATE TM_ROLE  SET RECORD_STATUS=1 WHERE ROLE_ID=?");
		statement2 = connection.prepareStatement(query2.toString());
        statement2.setShort(1, roleId);
		statement2.executeUpdate();
		statement2.close();	
	return "success";			
}	
	
	@Override
	public Integer removeRecord(Role object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<Role> list) {
		// TODO Auto-generated method stub
		return null;
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

	public void setRoleId(Short roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getAssignedRoles() {
		return assignedRoles;
	}

	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}

	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}

	public Short getRoleId() {
		return roleId;
	}
	public String getSmenuId() {
		return smenuId;
	}

	public void setSmenuId(String smenuId) {
		this.smenuId = smenuId;
	}

	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
