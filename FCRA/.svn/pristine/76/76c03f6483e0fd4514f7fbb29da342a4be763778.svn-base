package service.reports;

import java.util.ArrayList;
import java.util.List;
























import org.owasp.esapi.ESAPI;

import dao.reports.RedFlaggedRcnsDao;
import dao.reports.RegistrationTrackingDao;
import dao.services.RedFlagAssociationsDao;
import models.reports.RedFlaggedRcns;
import models.services.CommitteeMember;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.lists.List4;
import utilities.lists.List5;
import utilities.lists.List7;
import utilities.lists.List8;
import utilities.lists.List9;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class RedFlaggedRcnsService extends Commons {
	private List<RedFlaggedRcns> redflaggedList;
	private List<RedFlaggedRcns> annualredflaggedList;
	private List<RedFlaggedRcns>annualStatusList;
	private List<AbstractRequest> applicationList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String createdBy;
	private List<List2> stateList;
	private String appId;
	private String appName;
	private String functionaryName;
	private String state;
	private String district;
	private String remark;
	private String redFlagCategory;
	private String originatorOffice;
	private String orderNumber;
	private String orderDate;
	private String flagvalue;
	private List<List9> returnList;
	private List<CommitteeMember> committeeMembers;
	private List<List2> redFlagCategoryList=new ArrayList<List2>();
	private List<List4> regCancDetails=new ArrayList<List4>();
	private List<List7> redFlagDetailsList = new ArrayList<List7>();
	private List<List8> mailList=new ArrayList<List8>();
	private List<List5> smsList=new ArrayList<List5>();
	private List<List4> historyList;
	private String redFlag;
	private String redFlagREDCategory;
	private String redFlagYELLOWCategory;
	private String redFlagAddRole;
	private String redFlagRemoveRole;
	private String yellowFlagRemoveRole;
	private String yellowFlagAddRole;
	private int redroleId;
	private int yellowroleId;
	private int deleteredroleId;
	private int deleteyellowId;
	private String flagdelete;
	private String annualrcn;
	
	public String initializeRedFlaggedList() {
		begin();
		try {
				populateRedflaggedList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateRedflaggedList() throws Exception{
			RedFlaggedRcnsDao rfcdao=new RedFlaggedRcnsDao(connection);
			rfcdao.setPageNum(pageNum);
			rfcdao.setRecordsPerPage(recordsPerPage);
			rfcdao.setSortColumn(sortColumn);
			rfcdao.setSortOrder(sortOrder);				
			redflaggedList=rfcdao.getList();
			totalRecords=rfcdao.getTotalRecords();
		}
	
	public String initializeAnnualRedFlaggedList() {
		begin();
		try {
				populateAnnualRedflaggedList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateAnnualRedflaggedList() throws Exception{
			RedFlaggedRcnsDao rfcdao=new RedFlaggedRcnsDao(connection);
			rfcdao.setPageNum(pageNum);
			rfcdao.setRecordsPerPage(recordsPerPage);
			rfcdao.setSortColumn(sortColumn);
			rfcdao.setSortOrder(sortOrder);				
			annualredflaggedList=rfcdao.getAnnualList();
			totalRecords=rfcdao.getTotalRecords();
		}
	
/*	//Annual Status
	public String initializeAnnualStatusList() {
		begin();
		try {
				populateAnnualStatusList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateAnnualStatusList() throws Exception{
			RedFlaggedRcnsDao rfcdao=new RedFlaggedRcnsDao(connection);
			annualStatusList=rfcdao.getAnnualStatusList(annualrcn);
			totalRecords=rfcdao.getTotalRecords();
		}
	*/
	
	
	public void initRedFlagRcnAction(){
		begin();
		try {
				populateRedFlagRcnAction();				
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	public void populateRedFlagRcnAction() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);	
		RedFlaggedRcnsDao rcnrole=new RedFlaggedRcnsDao(connection);
		stateList=pdd.getStateList();
		redroleId=rcnrole.findRoleId(myUserId);
		yellowroleId=rcnrole.yellowFlagRole(myUserId);
		deleteredroleId=rcnrole.redremoveFlagRole(myUserId);
		deleteyellowId=rcnrole.yellowremoveRole(myUserId);
	}
	public void initApplicationListDetails(){
		begin();
		try {
				populateApplicationListDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	private void populateApplicationListDetails() throws Exception{
		RedFlaggedRcnsDao pdd=new RedFlaggedRcnsDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setAppId(appId);				
		applicationList=pdd.getApplicationListDetails();
		totalRecords=pdd.getTotalRecords();
	}

	public void initAdvanceApplicationListDetails(){
		begin();
		try {
				populateAdvanceApplicationListDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	
	private void populateAdvanceApplicationListDetails() throws Exception{
		RedFlaggedRcnsDao pdd=new RedFlaggedRcnsDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setSearchString(appName);
		pdd.setFunctionaryName(functionaryName);
		pdd.setState(state);
		pdd.setDistrict(district);
		applicationList=pdd.getAdvanceApplicationListDetails("2");
		totalRecords=pdd.getTotalRecords();
	}
	public String initRemoveFromRedFlagList(){
		begin();
		try {
				removeFromRedFlagList();	
				notifyList.add(new Notification("Success !!", "Association [ Reg. No. <b>"+appId+"</b> ] has been removed from  Flag list successfully.",Status.SUCCESS, Type.BAR));
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"removeModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"removeModal-error"));
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
	public void removeFromRedFlagList() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", remark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}	
		if(ESAPI.validator().isValidInput("OriginatorOffice", originatorOffice, "WordSS", 50, false) == false){
			throw new ValidationException("Invalid Originator office.Only 50 characters are allowed.");
		}	
		if(ESAPI.validator().isValidInput("orderNumber", orderNumber, "WordSS", 50, false) == false){
			throw new ValidationException("Invalid order number.Only 50 characters are allowed.");
		}	
		if(ESAPI.validator().isValidInput("orderDate", orderDate, "Date", 20, false) == false){
			throw new ValidationException("Invalid order date. Should be in dd-mm-yyyy format.");
		}	
		RedFlaggedRcnsDao pdd=new RedFlaggedRcnsDao(connection);		
		if(pdd.checkRemoveRedFlagRole(myUserId,appId)==true)		
			pdd.removeFromRedFlagList(appId,remark,myUserId,originatorOffice,orderNumber,orderDate,myOfficeCode);
		else
			throw new ValidationException("You are not authorized to modify  flag list.");
		
	}
	public String initAddToRedFlagList(){
		begin();
		try {
				addToRedFlagList();	
				//notifyList.add(new Notification("Success !!", "Association [ Reg. No. <b>"+appId+"</b> ] has been added to Red Flag list successfully.",Status.SUCCESS, Type.BAR));
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"addModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"addModal-error"));
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
	public void addToRedFlagList() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", remark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}	
		if(ESAPI.validator().isValidInput("redFlagCategory", redFlagCategory, "Num", 2, false) == false){
			throw new ValidationException("Invalid Red Flag Category.Invalid value.");
		}	
		if(ESAPI.validator().isValidInput("OriginatorOffice", originatorOffice, "WordSS", 50, false) == false){
			throw new ValidationException("Invalid Originator Office.Only 50 characters are allowed.");
		}	
		if(ESAPI.validator().isValidInput("orderNumber", orderNumber, "WordSS", 50, false) == false){
			throw new ValidationException("Invalid order number.Only 50 characters are allowed.");
		}	
		if(ESAPI.validator().isValidInput("orderDate", orderDate, "Date", 20, false) == false){
			throw new ValidationException("Invalid order date. Should be in dd-mm-yyyy format");
		}	
		RedFlaggedRcnsDao pdd=new RedFlaggedRcnsDao(connection);
		redroleId=pdd.findRoleId(myUserId);
		yellowroleId=pdd.yellowFlagRole(myUserId);
		if(redroleId==16|| yellowroleId==15){
			pdd.addToRedFlagList(appId,remark,myUserId,redFlagCategory,originatorOffice,orderNumber,orderDate,flagvalue,myOfficeCode);
			if(flagvalue.equalsIgnoreCase("1")){
				notifyList.add(new utilities.notifications.Notification("Success!!", " Rcn sucessfully inserted into red flag list.", Status.SUCCESS, Type.BAR));
			}
			else{
				notifyList.add(new utilities.notifications.Notification("Success!!", " Rcn sucessfully inserted into Yellow flag list.", Status.SUCCESS, Type.BAR));
				
			}
			
			
		}
	
		else{
			throw new ValidationException("You are not authorized to modify  flag list.");
		
	}
	}
	public String initGetApplicationDetails(){
		begin();
		try {
				getApplicationDetails();							
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
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
	private void getApplicationDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		RedFlaggedRcnsDao pdd=new RedFlaggedRcnsDao(connection);		
		pdd.getApplicationDetails(appId,myOfficeCode,myUserId);		
		applicationList=pdd.getApplicationList();	
		if(applicationList.get(0).getAssoRegnStatus()!=null && applicationList.get(0).getAssoRegnStatus().equals("1")){
			regCancDetails=pdd.getRegCancDetails();
		}
		returnList=pdd.getReturnList();
		committeeMembers=pdd.getCommitteeMembers();
		historyList=pdd.getHistoryList();
		mailList=pdd.getMailList();
		smsList=pdd.getSmsList();
		
		redFlag=pdd.getRedFlag();
		if(redFlag.equals("YES")){
			redFlagREDCategory=pdd.getRedFlagREDCategory();
			redFlagYELLOWCategory=pdd.getRedFlagYELLOWCategory();
		}
		redFlagAddRole=pdd.getRedFlagAddRole();
		redFlagRemoveRole=pdd.getRedFlagRemoveRole();
		yellowFlagRemoveRole=pdd.getYellowFlagRemoveRole();
		yellowFlagAddRole=pdd.getYellowFlagAddRole();
		if(redFlag.equals("YES") && (redFlagAddRole.equals("YES") || redFlagRemoveRole.equals("YES") || yellowFlagRemoveRole.equals("YES"))){
			redFlagDetailsList=pdd.getRedFlagDetailsList();
		}
		annualStatusList=pdd.getAnnualStatusList(appId);
		getAdditionalResources();
	}
	private void getAdditionalResources() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);
		redFlagCategoryList=pdd.getRedFlagCategory();
	}
	
	public List<RedFlaggedRcns> getRedflaggedList() {
		return redflaggedList;
	}

	public String getPageNum() {
		return pageNum;
	}

	public String getRecordsPerPage() {
		return recordsPerPage;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setRedflaggedList(List<RedFlaggedRcns> redflaggedList) {
		this.redflaggedList = redflaggedList;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<List2> getStateList() {
		return stateList;
	}

	public void setStateList(List<List2> stateList) {
		this.stateList = stateList;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFunctionaryName() {
		return functionaryName;
	}

	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOriginatorOffice() {
		return originatorOffice;
	}

	public void setOriginatorOffice(String originatorOffice) {
		this.originatorOffice = originatorOffice;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getRedFlagCategory() {
		return redFlagCategory;
	}

	public void setRedFlagCategory(String redFlagCategory) {
		this.redFlagCategory = redFlagCategory;
	}

	public String getFlagvalue() {
		return flagvalue;
	}

	public void setFlagvalue(String flagvalue) {
		this.flagvalue = flagvalue;
	}

	public List<List9> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<List9> returnList) {
		this.returnList = returnList;
	}

	public List<CommitteeMember> getCommitteeMembers() {
		return committeeMembers;
	}

	public void setCommitteeMembers(List<CommitteeMember> committeeMembers) {
		this.committeeMembers = committeeMembers;
	}

	public List<List2> getRedFlagCategoryList() {
		return redFlagCategoryList;
	}

	public void setRedFlagCategoryList(List<List2> redFlagCategoryList) {
		this.redFlagCategoryList = redFlagCategoryList;
	}

	public List<List4> getRegCancDetails() {
		return regCancDetails;
	}

	public void setRegCancDetails(List<List4> regCancDetails) {
		this.regCancDetails = regCancDetails;
	}

	public List<List7> getRedFlagDetailsList() {
		return redFlagDetailsList;
	}

	public void setRedFlagDetailsList(List<List7> redFlagDetailsList) {
		this.redFlagDetailsList = redFlagDetailsList;
	}

	public List<List8> getMailList() {
		return mailList;
	}

	public void setMailList(List<List8> mailList) {
		this.mailList = mailList;
	}

	public List<List5> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<List5> smsList) {
		this.smsList = smsList;
	}

	public List<List4> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<List4> historyList) {
		this.historyList = historyList;
	}

	public String getRedFlag() {
		return redFlag;
	}

	public void setRedFlag(String redFlag) {
		this.redFlag = redFlag;
	}

	public String getRedFlagREDCategory() {
		return redFlagREDCategory;
	}

	public void setRedFlagREDCategory(String redFlagREDCategory) {
		this.redFlagREDCategory = redFlagREDCategory;
	}

	public String getRedFlagYELLOWCategory() {
		return redFlagYELLOWCategory;
	}

	public void setRedFlagYELLOWCategory(String redFlagYELLOWCategory) {
		this.redFlagYELLOWCategory = redFlagYELLOWCategory;
	}

	public String getRedFlagAddRole() {
		return redFlagAddRole;
	}

	public void setRedFlagAddRole(String redFlagAddRole) {
		this.redFlagAddRole = redFlagAddRole;
	}

	public String getRedFlagRemoveRole() {
		return redFlagRemoveRole;
	}

	public void setRedFlagRemoveRole(String redFlagRemoveRole) {
		this.redFlagRemoveRole = redFlagRemoveRole;
	}

	public String getYellowFlagRemoveRole() {
		return yellowFlagRemoveRole;
	}

	public void setYellowFlagRemoveRole(String yellowFlagRemoveRole) {
		this.yellowFlagRemoveRole = yellowFlagRemoveRole;
	}

	public String getYellowFlagAddRole() {
		return yellowFlagAddRole;
	}

	public void setYellowFlagAddRole(String yellowFlagAddRole) {
		this.yellowFlagAddRole = yellowFlagAddRole;
	}

	public int getRedroleId() {
		return redroleId;
	}

	public void setRedroleId(int redroleId) {
		this.redroleId = redroleId;
	}

	public int getYellowroleId() {
		return yellowroleId;
	}

	public void setYellowroleId(int yellowroleId) {
		this.yellowroleId = yellowroleId;
	}

	public int getDeleteredroleId() {
		return deleteredroleId;
	}

	public void setDeleteredroleId(int deleteredroleId) {
		this.deleteredroleId = deleteredroleId;
	}

	public int getDeleteyellowId() {
		return deleteyellowId;
	}

	public void setDeleteyellowId(int deleteyellowId) {
		this.deleteyellowId = deleteyellowId;
	}

	public String getFlagdelete() {
		return flagdelete;
	}

	public void setFlagdelete(String flagdelete) {
		this.flagdelete = flagdelete;
	}

	public List<RedFlaggedRcns> getAnnualStatusList() {
		return annualStatusList;
	}

	public void setAnnualStatusList(List<RedFlaggedRcns> annualStatusList) {
		this.annualStatusList = annualStatusList;
	}

	public String getAnnualrcn() {
		return annualrcn;
	}

	public void setAnnualrcn(String annualrcn) {
		this.annualrcn = annualrcn;
	}

	public List<RedFlaggedRcns> getAnnualredflaggedList() {
		return annualredflaggedList;
	}

	public void setAnnualredflaggedList(List<RedFlaggedRcns> annualredflaggedList) {
		this.annualredflaggedList = annualredflaggedList;
	}


		

}
