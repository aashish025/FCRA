package service.masters;

import java.util.List;

import models.master.AdminUser;
import models.master.User;
import dao.master.UserDao;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class AdminUserServices extends Commons{
	private List<AdminUser> AdminuserList;	
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String userid;
	
	public String initAdminUserList() {
		begin();
		try {
				populateUserList();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateUserList() throws Exception{
		 UserDao udao=new  UserDao(connection);
		udao.setPageNum(pageNum);
		udao.setRecordsPerPage(recordsPerPage);
		udao.setSortColumn(sortColumn);
		udao.setSortOrder(sortOrder);
		AdminuserList=udao.getAdminUser();
		totalRecords=udao.getTotalRecords();
	}
	
	public String initResetPassword() {
		begin();
		try {
				resetPassword();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void resetPassword() throws Exception{
		UserDao udao=new UserDao(connection);
		User user=new User();
		user.setActionBy(myUserId);
		user.setActionIp(myIpAddress);
		udao.setUserid(userid);
		String status=udao.resetPassword(user);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Password is successfully reset.New passsword is <b> "
					+ ""+udao.getUserpwd()+" </b>", Status.SUCCESS, Type.BAR));	
		}		
	}
	
	public String initUnlockAdmin() {
		begin();
		try {
				unlockAdmin();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void unlockAdmin() throws Exception{
		UserDao udao=new UserDao(connection);
		udao.setUserid(userid);
		User user=new User();
		user.setActionBy(myUserId);
		user.setActionIp(myIpAddress);
		String status=udao.unlockUser(user);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "User <b>"+userid.toUpperCase()+"</b> is successfully unlocked.", Status.SUCCESS, Type.BAR));	
		}	
	}
	public String getPageNum() {
		return pageNum;
	}
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public List<AdminUser> getAdminuserList() {
		return AdminuserList;
	}

	public void setAdminuserList(List<AdminUser> adminuserList) {
		AdminuserList = adminuserList;
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
}

