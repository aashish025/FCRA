package service.services;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;

import models.services.ApplicationStatusManagement;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.services.ApplicationStatusManagementDao;

public class ApplicationStatusManagementServices extends Commons{
	
	
	private String totalRecords;
	private String appId;
	private String userId;
	private String serviceCode;
	private String remark;
	private List<ApplicationStatusManagement> applicationManagementList;
	private List<KVPair<String, String>> userList;
	
	
	public void initApplicationListDetails(String searchId){
		begin();
		try {
				populateApplicationListDetails(searchId);
				//UserList(searchId,myOfficeCode);
		} 
		 catch(Exception e){
				e.getMessage();
				ps(e);
				
					try {
						
						notifyList.add(new Notification("Error!","Invalid Application Id..", Status.ERROR, Type.BAR));
					}catch(Exception ex) {}
				}
		
		finally{
			finish();
		}		
	}
	///user list
	/*private void UserList(String serviceCode, String officeCode) throws Exception{
		ApplicationStatusManagementDao asm=new ApplicationStatusManagementDao(connection);
		asm.UserList(serviceCode,officeCode);		
	
	}*/
	
	
	
	
	private void populateApplicationListDetails(String searchId) throws Exception{
		ApplicationStatusManagementDao pdd=new ApplicationStatusManagementDao(connection);	
		if(searchId == null || searchId.equals("")) {
				notifyList.add(new Notification("Error!","Invalid Application Id.", Status.ERROR, Type.BAR));	
			
		}
		else{
			applicationManagementList=pdd.getApplicationManagementListDetails(searchId, myOfficeCode);
			userList=pdd.getUserList(searchId,myOfficeCode);
			
			totalRecords=pdd.getTotalRecords();
			int recordFound=pdd.getRecordStatus();
				if(recordFound==0){
					notifyList.add(new Notification("Info!!", "Entered Application Id Is Not Valid", Status.WARNING, Type.BAR));	
				}
				else{
					String currntStatus=pdd.getCurrentStatus();
					if(!currntStatus.equals("12")||!currntStatus.equals("10")){
						notifyList.add(new Notification("Info!!", "Entered Application Id Is Nither Closed Nor Denied.", Status.WARNING, Type.BAR));	
					}
				}
			}
	}
	
	public String initReOpen(String appId, String serviceCode, String remark,String userId) {
		begin();
		try {
				ReOpenApp(appId,serviceCode,remark,userId);				
			}catch(ValidationException ve){			
				try {
						notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"reopenModal-error"));
				} catch (NotificationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "error";
			}catch(Exception e){
				try {
						notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"reopenModal-error"));
						connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void ReOpenApp(String appId, String serviceCode, String remark, String userId ) throws Exception{
		this.remark=remark;
		this.userId=userId;
		if(ESAPI.validator().isValidInput("Remark", remark, "WordSS", 200, false) == false){
			throw new ValidationException("Remark - (200 characters max).");			
		}
		if(ESAPI.validator().isValidInput("UserId", userId, "WordS", 10, false) == false){
			throw new ValidationException("UserId - Only Alphabets and numbers allowed (20 characters max)");			
		}
		ApplicationStatusManagementDao asm=new ApplicationStatusManagementDao(connection);		
		asm.ReOpenApplicationStatusManagement(appId,serviceCode,remark,userId,myUserId,myOfficeCode);
		notifyList.add(new Notification("Success !!", "Application Id <b>"+appId.toUpperCase()+"</b> is successfully Opened.", Status.SUCCESS, Type.BAR));		
	}
		

	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	

	


	public List<KVPair<String, String>> getUserList() {
		return userList;
	}


	public void setUserList(List<KVPair<String, String>> userList) {
		this.userList = userList;
	}


	public String getServiceCode() {
		return serviceCode;
	}


	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}


	public String getTotalRecords() {
		return totalRecords;
	}


	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}


	public List<ApplicationStatusManagement> getApplicationManagementList() {
		return applicationManagementList;
	}


	public void setApplicationManagementList(
			List<ApplicationStatusManagement> applicationManagementList) {
		this.applicationManagementList = applicationManagementList;
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
