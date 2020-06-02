package service.masters;

import java.util.List;

import org.owasp.esapi.ESAPI;

import utilities.notifications.Notification;
import models.master.UserLevel;
import dao.master.UserLevelDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List3;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class UserLevelServices extends Commons {
	
	private static final String recordStatus = null;
	List<KVPair<String, String>> userlavelList;
	List<UserLevel> userList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	//private String UserLevel;
	private String UserLevelName;
	public Integer UserLevelId;
    private String rowId;
    private Integer CreatedOn;
	
	
	 public String execute() {
		begin();
		try {
				initUserLevelList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	


	public String initUserLevelList() {
		begin();
		try {
				populateUserList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateUserList() throws Exception{
		UserLevelDao adao=new UserLevelDao(connection);
		adao.setPageNum(pageNum);
		adao.setRecordsPerPage(recordsPerPage);
		adao.setSortColumn(sortColumn);
		adao.setSortOrder(sortOrder);	
		userList=adao.getMasteracquisition();
		totalRecords=adao.getTotalRecords();
	}
	
	public String AddUser() {
		begin();
		try {
				addUserLevel();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public Boolean validateUserLevel() throws Exception{		
		if(ESAPI.validator().isValidInput("UserLevel", UserLevelName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "User Type Detail - Only Aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public void addUserLevel() throws Exception{
		UserLevelDao rdao=new UserLevelDao(connection);
		UserLevel userlavel=new UserLevel();
		if(validateUserLevel()==true){
				userlavel.setUserlevelName(UserLevelName);
				userlavel.setCreatedIp(myIpAddress);
				userlavel.setCreatedBy(myUserId);
				userlavel.setLastModifiedBy(myUserId);
				userlavel.setLastModifiedIp(myIpAddress);
				int status=rdao.insertRecord(userlavel);
				if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "User Type Detail is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		
	}
	
	public String editUserLevel () {
		begin();
		try {
			editUserDetails();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editUserDetails() throws Exception {
		UserLevelDao rdao=new UserLevelDao(connection);
		UserLevel userlavel=new UserLevel();
		if(validateUserLevel()==true){
			userlavel.setUserlevelid(UserLevelId);
			userlavel.setUserlevelName(UserLevelName);
			userlavel.setCreatedIp(myIpAddress);
			userlavel.setCreatedBy(myUserId);
		int status=	rdao.editRecord(userlavel);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "User Type Detail is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}


public String initDeleteUserLevel() {
	begin();
	try {
			deleteuserlavel();
	} catch(Exception e){
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
}

private void deleteuserlavel() throws Exception {
	UserLevelDao rdao=new UserLevelDao(connection);
	UserLevel userlavel=new UserLevel();
	userlavel.setUserlevelid(UserLevelId);
	userlavel.setUserlevelName(UserLevelName);
	int status=rdao.removeRecord(userlavel);
	if(status>0)
		notifyList.add(new utilities.notifications.Notification("Success!!", "User Type Detail is Deleted successfully.", Status.SUCCESS, Type.BAR));
}


public Integer getCreatedOn() {
	return CreatedOn;
}

public void setCreatedOn(Integer createdOn) {
	CreatedOn = createdOn;
}

public String getRowId() {
	return rowId;
}

public void setRowId(String rowId) {
	this.rowId = rowId;
}



public String getUserLevelName() {
	return UserLevelName;
}


public void setUserLevelName(String acquisitionTypeName) {
	UserLevelName = acquisitionTypeName;
}





public Integer getUserLevelId() {
	return UserLevelId;
}



public void setUserLevelId(Integer userLevelId) {
	UserLevelId = userLevelId;
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


public List<KVPair<String, String>> getUserlavelList() {
	return userlavelList;
}


public void setUserlavelList(List<KVPair<String, String>> userlavelList) {
	this.userlavelList = userlavelList;
}


public List<UserLevel> getUserList() {
	return userList;
}


public void setUserList(List<UserLevel> userList) {
	this.userList = userList;
}


public List<List3> getRequestedDetails() {
	return list3;
}
}



