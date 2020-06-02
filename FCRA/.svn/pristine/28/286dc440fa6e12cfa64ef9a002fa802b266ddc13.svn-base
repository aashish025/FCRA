package service.masters;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;

import models.master.User;
import dao.master.UserDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class UserDetailsService extends Commons{
	private List<User> userList;	
	List<KVPair<String, String>> genderList;
	List<KVPair<String, String>> designationList;	
	List<KVPair<String, String>> availableRoleList;
	List<KVPair<String, String>> assignedRoleList;	
	List<KVPair<String, String>> availableSectionList;
	List<KVPair<String, String>> assignedSectionList;	
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String name;
	private String email;
	private String gender;
	private String designation;
	private String userid;
	private String assignedRoles;
	private String assignedSection;
	private boolean sectionStatus;
	
	public String execute() {
		begin();
		try {
				initGenderList();
				initDesignationList();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public void initGenderList() throws Exception{
		UserDao udao=new UserDao(connection);
		genderList=udao.getGenderList();
	}
	
	public void initDesignationList() throws Exception{
		UserDao udao=new UserDao(connection);
		designationList=udao.getDesginationList(myOfficeId);
	}
	
	public String initUserList() {
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
		UserDao udao=new UserDao(connection);
		udao.setPageNum(pageNum);
		udao.setRecordsPerPage(recordsPerPage);
		udao.setSortColumn(sortColumn);
		udao.setSortOrder(sortOrder);
		udao.setOfficeCode(myOfficeCode);
		userList=udao.getAll();
		totalRecords=udao.getTotalRecords();
	}
	
	public String initCreateUser() {
		begin();
		try {
				populateCreateUser();
		} catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"user-error"));
			} catch (NotificationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"user-error"));
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
	
	private void populateCreateUser() throws Exception{
		if(ESAPI.validator().isValidInput("name", name, "WordS", 50, false) == false){
			throw new ValidationException("Invalid Name.Only characters and spaces are allowed.Max 50 characters.");
		}
		if(ESAPI.validator().isValidInput("email", email, "Email", 50, false) == false){
			throw new ValidationException("Invalid EmailId.Max 50 characters.");
		}
		if(ESAPI.validator().isValidInput("gender", gender, "Word", 1, false) == false){
			throw new ValidationException("Invalid Gender.");
		}	
		if(ESAPI.validator().isValidInput("designation", designation, "Word", 3, false) == false){
			throw new ValidationException("Invalid Designation.");
		}	
		UserDao udao=new UserDao(connection);
		User user=new User();
		Integer userLevel = null;
		if(myUser.getLevel().equals("0")) {
			userLevel = 0;
		} else if(myUser.getLevel().equals("1")) {
			userLevel = 2;
		} else if(myUser.getLevel().equals("3")) {
			userLevel = 4;
		} else if(myUser.getLevel().equals("5")) {
			userLevel = 6;
		} else {
			notifyList.add(new Notification("Error !!", "You are not authorized to create a user.", Status.ERROR, Type.BAR,"user-error"));		
			return;
		}		
		user.setUserName(name);
		user.setEmailId(email);
		user.setDesignationId(designation);
		user.setGenderCode(gender);
		user.setOfficeCode(myOfficeCode);
		user.setActionBy(myOfficeCode);
		user.setUserLevel(userLevel);
		user.setStatusId("0");		
		user.setActionBy(myUserId);
		user.setActionIp(myIpAddress);
		user.setCreatedBy(myUserId);
		user.setCreatedIp(myIpAddress);
		int i=udao.insertRecord(user);		
		if(i>0){
			notifyList.add(new Notification("Success !!", "Created user is <b>[ "+udao.getUserid().toUpperCase()+" ]</b> and default password is <b> "
					+ ""+udao.getUserpwd()+" </b>", Status.SUCCESS, Type.BAR));
			setUserid(udao.getUserid().toUpperCase());
		}
		
	}
	public String initEditUser() {
		begin();
		try {				
				editUser();					
			}
			catch(ValidationException ve){			
				try {
					notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"user-error"));
				} catch (NotificationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "error";
			}catch(Exception e){
				try {
						notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"user-error"));
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
	private void editUser() throws Exception{	
		if(ESAPI.validator().isValidInput("name", name, "WordS", 50, false) == false){
			throw new ValidationException("Invalid Name.Only characters and spaces are allowed.Max 50 characters.");
		}
		if(ESAPI.validator().isValidInput("email", email, "Email", 50, false) == false){
			throw new ValidationException("Invalid EmailId.Max 50 characters.");
		}
		if(ESAPI.validator().isValidInput("gender", gender, "Word", 1, false) == false){
			throw new ValidationException("Invalid Gender.");
		}	
		if(ESAPI.validator().isValidInput("designation", designation, "Word", 2, false) == false){
			throw new ValidationException("Invalid Designation.");
		}	
		UserDao udao=new UserDao(connection);
		User user=new User();
		user.setUserid(userid);
		user.setUserName(name);
		user.setEmailId(email);
		user.setDesignationId(designation);
		user.setGenderCode(gender);	
		user.setActionBy(myUserId);
		user.setActionIp(myIpAddress);
		String status=udao.editUser(user);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Details for user <b>"+userid.toUpperCase()+"</b> are successfully updated.", Status.SUCCESS, Type.BAR));	
		}		
	}
	public String checkOffice() {
		begin();
		try {
				office();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void office() throws Exception{
		UserDao udao=new UserDao(connection);
		User user=new User();
		udao.setMyOfficeCode(myOfficeCode);
		Boolean status=udao.checkCode(user);
		
		if(status){
			boolean flag=true;
			setSectionStatus(flag);
			
		}	else{
			boolean flag=false;
			setSectionStatus(flag);
		}
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
	public String initUnlockUser() {
		begin();
		try {
				unlockUser();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void unlockUser() throws Exception{
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
	public String initDeleteUser() {
		begin();
		try {
				deleteUser();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void deleteUser() throws Exception{
		UserDao udao=new UserDao(connection);
		udao.setUserid(userid);
		User user=new User();
		user.setActionBy(myUserId);
		user.setActionIp(myIpAddress);
		String status=udao.deleteUser(user);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "User <b>"+userid.toUpperCase()+"</b> is successfully deleted.", Status.SUCCESS, Type.BAR));	
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
		UserDao udao=new UserDao(connection);
		udao.setUserid(userid);
		udao.setMyOfficeCode(myOfficeCode);
		availableRoleList=udao.getAvailableUserRole();
		assignedRoleList=udao.getAssignedUserRole();		
	}
	public String initSection() {
		begin();
		try {
				populateSection();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateSection() throws Exception{
		UserDao udao=new UserDao(connection);
		udao.setUserid(userid);
		udao.setMyOfficeCode(myOfficeCode);
		availableSectionList=udao.getAvailableSection();
		assignedSectionList=udao.getAssignedSection();		
	}
	public String initSaveUserRole() {		
		try {
				begin();
				saveUserRole();
				
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
	private void saveUserRole() throws Exception{
		UserDao udao=new UserDao(connection);
		User user=new User();
		user.setCreatedBy(myUserId);
		user.setCreatedIp(myIpAddress);
		udao.setUserid(userid);
		udao.setAssignedRoles(assignedRoles);
		String status=udao.saveUserRoles(user);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Roles for user <b>"+userid.toUpperCase()+"</b> are successfully saved.", Status.SUCCESS, Type.BAR));	
		}		
	}
	public String initSaveSection() {		
		try {
				begin();
				saveSection();
				
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
	private void saveSection() throws Exception{
		UserDao udao=new UserDao(connection);
		User user=new User();
		user.setCreatedBy(myUserId);
		user.setCreatedIp(myIpAddress);
		udao.setUserid(userid);
		udao.setAssignedSection(assignedSection);
		udao.setMyOfficeCode(myOfficeCode);
		String status=udao.saveSection(user);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Section for user <b>"+userid.toUpperCase()+"</b> are successfully saved.", Status.SUCCESS, Type.BAR));	
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

	public List<KVPair<String, String>> getAvailableSectionList() {
		return availableSectionList;
	}

	public void setAvailableSectionList(
			List<KVPair<String, String>> availableSectionList) {
		this.availableSectionList = availableSectionList;
	}

	public List<KVPair<String, String>> getAssignedSectionList() {
		return assignedSectionList;
	}

	public String getAssignedSection() {
		return assignedSection;
	}

	public void setAssignedSection(String assignedSection) {
		this.assignedSection = assignedSection;
	}

	public void setAssignedSectionList(
			List<KVPair<String, String>> assignedSectionList) {
		this.assignedSectionList = assignedSectionList;
	}

	
	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<KVPair<String, String>> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<KVPair<String, String>> genderList) {
		this.genderList = genderList;
	}

	public List<KVPair<String, String>> getDesignationList() {
		return designationList;
	}

	public void setDesignationList(List<KVPair<String, String>> designationList) {
		this.designationList = designationList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getUserid() {
		return userid;
	}

	

	public boolean isSectionStatus() {
		return sectionStatus;
	}

	public void setSectionStatus(boolean sectionStatus) {
		this.sectionStatus = sectionStatus;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public String getAssignedRoles() {
		return assignedRoles;
	}

	public void setAssignedRoles(String assignedRoles) {
		this.assignedRoles = assignedRoles;
	}

	
}
