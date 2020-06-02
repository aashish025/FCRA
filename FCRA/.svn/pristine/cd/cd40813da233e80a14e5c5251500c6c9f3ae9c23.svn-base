package service.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.owasp.esapi.ESAPI;
import org.springframework.stereotype.Service;

import dao.master.GenderDetailDao;
import dao.master.ServicesDao;
import dao.master.StateDao;
import dao.master.UserDao;
import dao.services.AuthorityManagementDao;
import models.master.GenderType;
import models.services.AuthorityManagement;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
@Service
public class AuthorityManagementServices extends Commons {
	private List<AuthorityManagement> applicationList;
	List<KVPair<String, String>> userList;
	List<KVPair<String, String>> stateList;
	List<KVPair<String, String>> serviceTypeList;
	List<KVPair<String, String>>forwardUserList;
	
	 public String execute() {
			begin();
			try {
					initServiceTypeList();
					initUserList();
					initStateList();
					initForwardUserList();

			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			
			return "success";
		}
	 private void initUserList() throws Exception{
			UserDao udao=new UserDao(connection);
			userList=udao.getAllUserList(myOfficeCode);
		}
	 private void initForwardUserList() throws Exception{
			UserDao udao=new UserDao(connection);
			forwardUserList=udao.getForwardUserList(myOfficeCode);
		}
	private void initServiceTypeList() throws Exception{
		ServicesDao sdao=new ServicesDao(connection);
		serviceTypeList=sdao.getKVList();
	}
	 private void initStateList() throws Exception{
		 StateDao udao=new StateDao(connection);
			stateList=udao.getKVList();
		}
	public String initApplicationIdList(String userId,String serviceType,HttpServletRequest request,String state) {
		begin();
		try {
			System.out.println("Server Name=  "+request.getParameterMap().get(serviceType));
			populateApplicationIdList(userId,serviceType,state);
		} catch(Exception e){
			spl(e);
		}
		finally{
			
			finish();
		}	
		return "success";
	} 
	
	private void populateApplicationIdList(String userId,String serviceType,String state) throws Exception{
		AuthorityManagementDao amdao=new AuthorityManagementDao(connection);	
		applicationList=amdao.getApplicationIdList(userId,serviceType,state);
		
	}
	public String initiateForwardingAction(String forwardingUserId,String applicationIdString ,String remark,String fromUser ) {

		begin();
		try {
			forwardingAction(forwardingUserId,applicationIdString,remark,myOfficeCode,fromUser);
			notifyList.add(new Notification("Success !!", "Applications has been forwarded to "
					+ "<b>"+forwardingUserId+"</b> successfully.",Status.SUCCESS, Type.BAR));
		} 
		catch(ValidationException ve){
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"userModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}
		
		catch(Exception e){
			try {
				notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"userModal-error"));
			} catch (NotificationException ee) {				
				ee.printStackTrace();
			}
			return "error";
			
		}
		finally{
			finish();
		}	
		return "success";

		
	}
/**
 * 
 * @param state
 * @param serviceType
 * @return
 */
	public String initFreshApplicationIdList(String state,String serviceType) {
		begin();
		try {
			populateFreshApplicationIdList(state,serviceType);
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	/**
	 * fetch data for office to user(list of fresh application)
	 * @param state
	 * @param serviceType
	 * @throws Exception
	 */
	private void populateFreshApplicationIdList(String state,String serviceType) throws Exception{
		AuthorityManagementDao amdao=new AuthorityManagementDao(connection);	
		applicationList=amdao.getFreshApplicationIdList(state,serviceType);
		
	}
	/*
	 * 
	 * 
	 */
	public String initiateForwardingActionOfficeToUser(String forwardingUserId,String applicationIdString  ) {

		begin();
		try {
			forwardingActionOfficeToUser(forwardingUserId,applicationIdString,myOfficeCode);
			notifyList.add(new Notification("Success !!", "Applications has been forwarded to "
					+ "<b>"+forwardingUserId+"</b> successfully.",Status.SUCCESS, Type.BAR));
		} 
		catch(ValidationException ve){
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"userModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}
		
		catch(Exception e){
			try {
				notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"userModal-error"));
			} catch (NotificationException ee) {				
				ee.printStackTrace();
			}
			return "error";
			
		}
		finally{
			finish();
		}	
		return "success";

		
	}
	public void forwardingActionOfficeToUser(String forwardingUserId,String applicationIdString ,String officeCode) throws Exception{
		if(ESAPI.validator().isValidInput("forwardUser", forwardingUserId, "Word", 10, false) == false){
			throw new ValidationException("Invalid Forward To User.");
		} 
		AuthorityManagementDao amd=new AuthorityManagementDao(connection);
	    amd.forwardApplicationOfficeToUser(forwardingUserId,applicationIdString,officeCode,myOfficeId);
	}
	
	public void forwardingAction(String forwardingUserId,String applicationIdString ,String remark,String officeCode,String userId) throws Exception{
		if(ESAPI.validator().isValidInput("forwardUser", forwardingUserId, "Word", 10, false) == false){
			throw new ValidationException("Invalid Forward To User.");
		}
		if(ESAPI.validator().isValidInput("forwardUser", userId, "Word", 10, false) == false){
			throw new ValidationException("Invalid Forwar To User.");
		}
		if(ESAPI.validator().isValidInput("statusRemark", remark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid  Remark.Only 2000 characters are allowed.");
		} 
		AuthorityManagementDao amd=new AuthorityManagementDao(connection);
	    amd.forwardApplication(forwardingUserId,applicationIdString,remark,officeCode,userId,myUserId);
	}
	public List<AuthorityManagement> getApplicationList() {
		return applicationList;
	}
	public void setApplicationList(List<AuthorityManagement> applicationList) {
		this.applicationList = applicationList;
	}
	
	public List<KVPair<String, String>> getUserList() {
		return userList;
	}
	public void setUserList(List<KVPair<String, String>> userList) {
		this.userList = userList;
	}
	
	public List<KVPair<String, String>> getStateList() {
		return stateList;
	}
	public void setStateList(List<KVPair<String, String>> stateList) {
		this.stateList = stateList;
	}
	public List<KVPair<String, String>> getForwardUserList() {
		return forwardUserList;
	}
	public void setForwardUserList(List<KVPair<String, String>> forwardUserList) {
		this.forwardUserList = forwardUserList;
	}
	public List<KVPair<String, String>> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<KVPair<String, String>> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	
	
	
	
}
