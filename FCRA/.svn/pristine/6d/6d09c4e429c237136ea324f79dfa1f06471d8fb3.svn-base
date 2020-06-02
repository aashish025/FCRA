package service.reports;

import java.util.ArrayList;
import java.util.List;

import models.services.CommitteeMember;
import models.services.requests.AbstractRequest;

import org.owasp.esapi.ESAPI;

import dao.reports.RegistrationTrackingDao;
import dao.services.dashboard.ApplicationStatusDao;
import dao.services.dashboard.ProjectDashboardDao;
import utilities.Commons;
import utilities.InformationException;
import utilities.ValidationException;
import utilities.lists.List1;
import utilities.lists.List12;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.lists.List5;
import utilities.lists.List6;
import utilities.lists.List7;
import utilities.lists.List8;
import utilities.lists.List9;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class RegistrationTrackingService extends Commons{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;	
	private List<AbstractRequest> applicationList;
	private List<List9> returnList;
	private List<CommitteeMember> committeeMembers;
	private List<List2> redFlagCategoryList=new ArrayList<List2>();
	private List<List4> regCancDetails=new ArrayList<List4>();
	private List<List7> redFlagDetailsList = new ArrayList<List7>();
	private List<List8> mailList=new ArrayList<List8>();
	private List<List5> smsList=new ArrayList<List5>();
	private List<List4> historyList;
	private List<List2> stateList;
	private List<List2> districtList;
	private List<List2> rcnDetail;
	private String appId;
	private String appName;
	private String functionaryName;
	private String state;
	private String district;
	private String statusRemark;
	private String cancellationType;
	private String cancellationReason;
	private String requestDate;
	private String actionFlag;
	private String redFlag;
	private String blueFlag;
	private String redFlagREDCategory;
	private String redFlagYELLOWCategory;
	private String redFlagAddRole;
	private String redFlagRemoveRole;
	private String yellowFlagRemoveRole;
	private String remark;
	private String redFlagCategory;
	private String originatorOffice;
	private String orderNumber;
	private String orderDate;
	private List<List2> rcnDetail1;
	
	
	public void initRegistrationTrackingAction(){
		begin();
		try {
				populateRegistrationTrackingAction();				
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	
	public void initDistrict(){
		begin();
		try {
				getDistrict();				
		} catch(Exception e){
			try {
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
		}
		finally{
			finish();
		}		
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
	
	public String initApplicationListDetails1(){
		String result="error";
		begin();
		try {
			    CheckExistingRcnDetails();
				//populateApplicationListDetails1();
				
				
		}
		catch(ValidationException ve ){
			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			
		
		}
		
		catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}
		return result;
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
	
	
	public String initGetApplicationDetailsExemption(){
		begin();
		try {
				getApplicationDetailsExemption();							
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
	public String initAddToRedFlagList(){
		begin();
		try {
				addToRedFlagList();	
				notifyList.add(new Notification("Success !!", "Association [ Reg. No. <b>"+appId+"</b> ] has been added to Red Flag list successfully.",Status.SUCCESS, Type.BAR));
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
	public String initRemoveFromRedFlagList(){
		begin();
		try {
				removeFromRedFlagList();	
				notifyList.add(new Notification("Success !!", "Association [ Reg. No. <b>"+appId+"</b> ] has been removed from Red Flag list successfully.",Status.SUCCESS, Type.BAR));
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
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);
		if(pdd.checkAddRedFlagRole(myUserId)==true)
			pdd.addToRedFlagList(appId,remark,myUserId,redFlagCategory,originatorOffice,orderNumber,orderDate);
		else
			throw new ValidationException("You are not authorized to modify red flag list.");
		
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
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);		
		if(pdd.checkRemoveRedFlagRole(myUserId,appId)==true)		
			pdd.removeFromRedFlagList(appId,remark,myUserId,originatorOffice,orderNumber,orderDate,myOfficeCode);
		else
			throw new ValidationException("You are not authorized to modify adverse flag list.");
		
	}
	public void populateRegistrationTrackingAction() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);		
		stateList=pdd.getStateList();
	}
	
	private void getDistrict() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);
		districtList=pdd.getDistrictList(state);	
	}	
	
	private void populateApplicationListDetails() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setAppId(appId);				
		applicationList=pdd.getApplicationListDetails();
		totalRecords=pdd.getTotalRecords();
	}
	
	private void getApplicationDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);		
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
		blueFlag=pdd.getBlueFlag();
		if(redFlag.equals("YES")){
			redFlagREDCategory=pdd.getRedFlagREDCategory();
			redFlagYELLOWCategory=pdd.getRedFlagYELLOWCategory();
		}
		redFlagAddRole=pdd.getRedFlagAddRole();
		redFlagRemoveRole=pdd.getRedFlagRemoveRole();
		yellowFlagRemoveRole=pdd.getYellowFlagRemoveRole();
		if(redFlag.equals("YES")/* && (redFlagAddRole.equals("YES") || redFlagRemoveRole.equals("YES") || yellowFlagRemoveRole.equals("YES"))*/){
			redFlagDetailsList=pdd.getRedFlagDetailsList();
		}
		getAdditionalResources();
	}
	
	
	private void getApplicationDetailsExemption() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);		
		pdd.getApplicationListExemption(appId,myOfficeCode,myUserId);	
		applicationList=pdd.getApplicationList();	
		
		//getAdditionalResources();
	}
	
	private void getAdditionalResources() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);
		redFlagCategoryList=pdd.getRedFlagCategory();
	}
	
	
	private void CheckExistingRcnDetails() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);						
		pdd.setAppId(appId);				
		String status=pdd.getExistingRcnCheck();
		if(status!=null && status.equals("Y")){
			rcnDetail=pdd.getApplicationListDetailsExemption();				
		}
		else
		{
			notifyList.add(new Notification("Error !!","Exemption for renewal has already been given to this association "+(status == null ? "" : "and is valid till "+status), Status.ERROR, Type.BAR));
		}
	}
	private void populateAdvanceApplicationListDetails() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);						
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
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getStatusRemark() {
		return statusRemark;
	}

	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}	

	public String getCancellationType() {
		return cancellationType;
	}

	public void setCancellationType(String cancellationType) {
		this.cancellationType = cancellationType;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	

	public List<List2> getStateList() {
		return stateList;
	}

	public void setStateList(List<List2> stateList) {
		this.stateList = stateList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<List2> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<List2> districtList) {
		this.districtList = districtList;
	}

	public void setDistrict(String district) {
		this.district = district;
	}	

	public List<CommitteeMember> getCommitteeMembers() {
		return committeeMembers;
	}

	public void setCommitteeMembers(List<CommitteeMember> committeeMembers) {
		this.committeeMembers = committeeMembers;
	}

	public String getRedFlag() {
		return redFlag;
	}

	public void setRedFlag(String redFlag) {
		this.redFlag = redFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFunctionaryName() {
		return functionaryName;
	}

	public void setFunctionaryName(String functionaryName) {
		this.functionaryName = functionaryName;
	}

	public List<List2> getRedFlagCategoryList() {
		return redFlagCategoryList;
	}

	public void setRedFlagCategoryList(List<List2> redFlagCategoryList) {
		this.redFlagCategoryList = redFlagCategoryList;
	}

	public String getRedFlagCategory() {
		return redFlagCategory;
	}

	public void setRedFlagCategory(String redFlagCategory) {
		this.redFlagCategory = redFlagCategory;
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

	public List<List7> getRedFlagDetailsList() {
		return redFlagDetailsList;
	}

	public void setRedFlagDetailsList(List<List7> redFlagDetailsList) {
		this.redFlagDetailsList = redFlagDetailsList;
	}

	public List<List4> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<List4> historyList) {
		this.historyList = historyList;
	}

	public String getYellowFlagRemoveRole() {
		return yellowFlagRemoveRole;
	}

	public void setYellowFlagRemoveRole(String yellowFlagRemoveRole) {
		this.yellowFlagRemoveRole = yellowFlagRemoveRole;
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

	public List<List9> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<List9> returnList) {
		this.returnList = returnList;
	}

	public List<List2> getRcnDetail() {
		return rcnDetail;
	}

	public void setRcnDetail(List<List2> rcnDetail) {
		this.rcnDetail = rcnDetail;
	}

	public List<List2> getRcnDetail1() {
		return rcnDetail1;
	}

	public void setRcnDetail1(List<List2> rcnDetail1) {
		this.rcnDetail1 = rcnDetail1;
	}

	public List<List4> getRegCancDetails() {
		return regCancDetails;
	}

	public void setRegCancDetails(List<List4> regCancDetails) {
		this.regCancDetails = regCancDetails;
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

	public String getBlueFlag() {
		return blueFlag;
	}

	public void setBlueFlag(String blueFlag) {
		this.blueFlag = blueFlag;
	}

	
}
