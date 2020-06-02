package service.masters;

import java.sql.SQLException;
import java.util.List;
import org.owasp.esapi.ESAPI;
import dao.master.RoleDao;
import models.master.Role;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class RoleTypeDetailServices extends Commons {
private static String recordStatus = null;
	

	List<KVPair<String, String>> roleTypeList;
	List<KVPair<String, String>> availableRoleList;
	List<KVPair<String, String>> assignedRoleList;	
	List<KVPair<String, String>> availableUserList;
	List<KVPair<String, String>> assignedUserList;	
	List<Role> roleList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String roleName;
	public Short roleId;
    private String rowId;
    private Integer CreatedOn;
    private Integer EnteredOn;
    private String assignedRoles;
    private String assignedUser;
   
	
	 public String execute() {
		begin();
		try {
				initRoleTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	

     public void initRoleTypeList() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		roleTypeList=bdao.getKVList();
	}
	
	public String initializeRoleList() {
		begin();
		try {
				populateRoleList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populateRoleList() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);				
	    roleList=bdao.gettable();
		totalRecords=bdao.getTotalRecords();
	}
	public Boolean validateRole() throws Exception{		
		if(ESAPI.validator().isValidInput("Role Name", roleName, "AlphaS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Role Name - Only Alphabet allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public String initCreateRole() {
		begin();
		try {
				populateCreateRole();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateCreateRole() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		Role role=new Role();
		if(validateRole()==true){
		role.setRoleName(roleName);
		role.setCreatedBy(myUserId);
		role.setCreatedIp(myIpAddress);
		role.setLastModifiedBy(myUserId);
		role.setLastModifiedIp(myIpAddress);
		   int status=	bdao.insertRecord(role);
		   if(status>0)
			notifyList.add(new Notification("Success!!","New Role <b>"+roleName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		   setRoleId(bdao.getRoleId());
		   }
	}
	public String initUserRole() {
		begin();
		try {
				populateUserRole();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateUserRole() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		bdao.setRoleId(roleId);
		availableRoleList=bdao.getAvailableRole();
		assignedRoleList=bdao.getAssignedRole();
			
	}

public String initUserLevel() {
	begin();
	try {
			populateUserLevel();
	} catch(Exception e){
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
} 
private void populateUserLevel() throws Exception{
	RoleDao bdao=new RoleDao(connection);
	bdao.setRoleId(roleId);
	availableUserList=bdao.getAvailableUser();
	assignedUserList=bdao.getAssignedUser();
		
}
	public String editRolename () {
		begin();
		try {
				editRole();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editRole() throws Exception {
		RoleDao bdao=new RoleDao(connection);
		Role role=new Role();
		if(validateRole()==true){
		role.setRoleName(roleName);
		role.setRoleId(roleId);
		role.setLastModifiedBy(myUserId);
		role.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(role);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Role Name is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}

	public String initSaveRole() {		
		try {
				begin();
				saveRole();
				
		} catch(Exception e){
			try {
					connection.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void saveRole() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		Role role=new Role();
		role.setCreatedBy(myUserId);
		role.setCreatedIp(myIpAddress);
		bdao.setRoleId(roleId);
		bdao.setAssignedRoles(assignedRoles);
		String status=bdao.saveRoles(role);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Menu are Sucessfully Assigned to Role.", Status.SUCCESS, Type.BAR));	
			
			}		
	}
	public String initSaveUser() {		
		try {
				begin();
				saveuser();
				
		} catch(Exception e){
			try {
					connection.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void saveuser() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		Role role=new Role();
		role.setCreatedBy(myUserId);
		role.setCreatedIp(myIpAddress);
		bdao.setRoleId(roleId);
		bdao.setAssignedUser(assignedUser);
		String status=bdao.saveUser(role);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Roles for user  are successfully saved.", Status.SUCCESS, Type.BAR));	
			
			}		
	}
	public String initDeleteRole() {
		begin();
		try {
				deleteRole();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void deleteRole() throws Exception{
		RoleDao bdao=new RoleDao(connection);
		Role role=new Role();
		bdao.setRoleId(roleId);
	    String status=bdao.deleterole(role);
		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Role  is successfully deleted.", Status.SUCCESS, Type.BAR));	
		}	
	}
	

	public static String getRecordStatus() {
		return recordStatus;
	}


	


	public Short getRoleId() {
		return roleId;
	}


	public void setRoleId(Short roleId) {
		this.roleId = roleId;
	}


	public List<KVPair<String, String>> getRoleTypeList() {
		return roleTypeList;
	}


	public void setRoleTypeList(List<KVPair<String, String>> roleTypeList) {
		this.roleTypeList = roleTypeList;
	}


	public List<Role> getRoleList() {
		return roleList;
	}


	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
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


	public String getAssignedUser() {
		return assignedUser;
	}


	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}


	public String getSortOrder() {
		return sortOrder;
	}


	public List<KVPair<String, String>> getAvailableRoleList() {
		return availableRoleList;
	}


	public void setAvailableRoleList(List<KVPair<String, String>> availableRoleList) {
		this.availableRoleList = availableRoleList;
	}


	public List<KVPair<String, String>> getAssignedRoleList() {
		return assignedRoleList;
	}


	public void setAssignedRoleList(List<KVPair<String, String>> assignedRoleList) {
		this.assignedRoleList = assignedRoleList;
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


	public String getAssignedRoles() {
		return assignedRoles;
	}


	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}


	public static void setRecordstatus(String recordstatus) {
		recordStatus = recordstatus;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}




	public String getRowId() {
		return rowId;
	}


	public void setRowId(String rowId) {
		this.rowId = rowId;
	}


	public Integer getCreatedOn() {
		return CreatedOn;
	}


	public void setCreatedOn(Integer createdOn) {
		CreatedOn = createdOn;
	}


	public Integer getEnteredOn() {
		return EnteredOn;
	}


	public void setEnteredOn(Integer enteredOn) {
		EnteredOn = enteredOn;
	}


	public static String getRecordstatus() {
		return recordStatus;
	}


	public List<KVPair<String, String>> getAvailableUserList() {
		return availableUserList;
	}


	public void setAvailableUserList(List<KVPair<String, String>> availableUserList) {
		this.availableUserList = availableUserList;
	}


	public List<KVPair<String, String>> getAssignedUserList() {
		return assignedUserList;
	}


	public void setAssignedUserList(List<KVPair<String, String>> assignedUserList) {
		this.assignedUserList = assignedUserList;
	}


	public static void setRecordStatus(String recordStatus) {
		RoleTypeDetailServices.recordStatus = recordStatus;
	}

}
