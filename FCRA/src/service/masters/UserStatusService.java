package service.masters;

import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.UserStatus;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.UserStatusDao;

public class UserStatusService extends Commons{
	private List<UserStatus> userList;
	private List<UserStatus> userStatusList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String actionName;
	private String createdBy;
	private Integer actionId;
	public String initializeUserStatusList() {
		begin();
		try {
				populateUserStatusList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateUserStatusList() throws Exception{
	
		UserStatusDao tdao=new UserStatusDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			userStatusList=tdao.getMainUserSt();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validateUserStatus() throws Exception{		
		if(ESAPI.validator().isValidInput("ActionName", actionName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "User Status Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddUserSt() {
		begin();
		try {
				addUserSt();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addUserSt() throws Exception{
		
		UserStatusDao ndao=new UserStatusDao(connection);
		UserStatus userStatus=new UserStatus();
		if(validateUserStatus()==true){
			userStatus.setActionName(actionName);
			userStatus.setCreatedIp(myIpAddress);
			userStatus.setCreatedBy(myUserId);
			userStatus.setLastModifiedBy(myUserId);
			userStatus.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(userStatus);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New User Status <b>"+actionName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	public String initDeleteUserSt() {
		begin();
		try {
				deleteUserSt();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteUserSt() throws Exception{
		UserStatusDao ndao=new UserStatusDao(connection);
		UserStatus  userStatus=new UserStatus();
		userStatus.setActionId(actionId);	
		int status=ndao.removeRecord(userStatus);
		if(status>0){
			notifyList.add(new Notification("Success!!", "User Status <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	public String initEditUserSt() {
		begin();
		try {
				editUserSt();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editUserSt() throws Exception{
		UserStatusDao ndao=new UserStatusDao(connection);
		UserStatus userStatus=new UserStatus();
		if(validateUserStatus()==true){
			userStatus.setActionId(actionId);
			userStatus.setActionName(actionName);
			userStatus.setCreatedIp(myIpAddress);
			userStatus.setCreatedBy(myUserId);
			userStatus.setLastModifiedBy(myUserId);
			userStatus.setLastModifiedIp(myIpAddress);
		
		int i=ndao.editRecord(userStatus);
		if(i>0)
			notifyList.add(new Notification("Success!!", " User Status <b>"+actionName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
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

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	
	public List<UserStatus> getUserList() {
		return userList;
	}
	public void setUserList(List<UserStatus> userList) {
		this.userList = userList;
	}
	public List<UserStatus> getUserStatusList() {
		return userStatusList;
	}
	public void setUserStatusList(List<UserStatus> userStatusList) {
		this.userStatusList = userStatusList;
	}
	

	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public Integer getActionId() {
		return actionId;
	}
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
