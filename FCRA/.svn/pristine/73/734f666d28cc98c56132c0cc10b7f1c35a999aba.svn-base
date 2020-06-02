package service.reports;

import java.util.ArrayList;
import java.util.List;

import org.owasp.esapi.ESAPI;

import dao.reports.BlueFlaggedRcnsDao;
import dao.reports.RedFlaggedRcnsDao;
import dao.reports.RegistrationTrackingDao;
import dao.services.RedFlagAssociationsDao;
import models.reports.BlueFlaggedRcns;
import models.reports.RedFlaggedRcns;
import models.services.CommitteeMember;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.lists.List4;
import utilities.lists.List5;
import utilities.lists.List7;
import utilities.lists.List8;
import utilities.lists.List9;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class BlueFlaggedRcnsService extends Commons {
	private List<BlueFlaggedRcns> blueflaggedList;
	private List<BlueFlaggedRcns> annualblueflaggedList;
	private List<BlueFlaggedRcns>annualStatusList;
	private String blueRcnexist;
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
	private List<List4> regCancDetails=new ArrayList<List4>();
	private List<List3> blueFlagDetailsList = new ArrayList<List3>();
	private List<List8> mailList=new ArrayList<List8>();
	private List<List5> smsList=new ArrayList<List5>();
	private List<List4> historyList;
	private String blueFlag;
	private int deleteblueId;
	private String flagdelete;
	private String annualrcn;
	private String removeblueFlag;
	
	public String initializeBlueFlaggedList() {
		begin();
		try {
				populateBlueflaggedList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateBlueflaggedList() throws Exception{
			BlueFlaggedRcnsDao rfcdao=new BlueFlaggedRcnsDao(connection);
			rfcdao.setPageNum(pageNum);
			rfcdao.setRecordsPerPage(recordsPerPage);
			rfcdao.setSortColumn(sortColumn);
			rfcdao.setSortOrder(sortOrder);				
			blueflaggedList=rfcdao.getList();
			totalRecords=rfcdao.getTotalRecords();
		}
	public void initBlueFlagRcnAction(){
		begin();
		try {
				populateBlueFlagRcnAction();				
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	public void populateBlueFlagRcnAction() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);	
		BlueFlaggedRcnsDao rcnrole=new BlueFlaggedRcnsDao(connection);
		stateList=pdd.getStateList();
		deleteblueId=rcnrole.blueremoveRole(myUserId);
		
		
	}
	
	
	
	//
	public String initializeAnnualBlueFlaggedList() {
		begin();
		try {
				populateAnnualblueflaggedList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateAnnualblueflaggedList() throws Exception{
			BlueFlaggedRcnsDao rfcdao=new BlueFlaggedRcnsDao(connection);
			rfcdao.setPageNum(pageNum);
			rfcdao.setRecordsPerPage(recordsPerPage);
			rfcdao.setSortColumn(sortColumn);
			rfcdao.setSortOrder(sortOrder);				
			annualblueflaggedList=rfcdao.getAnnualList();
			totalRecords=rfcdao.getTotalRecords();
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
		BlueFlaggedRcnsDao pdd=new BlueFlaggedRcnsDao(connection);						
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
		BlueFlaggedRcnsDao pdd=new BlueFlaggedRcnsDao(connection);						
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
	public String initRemoveFromBlueFlagList(){
		begin();
		try {
				removeFromBlueFlagList();	
				notifyList.add(new Notification("Success !!", "Association [ Reg. No. <b>"+appId+"</b> ] has been removed from Blue Flag list successfully.",Status.SUCCESS, Type.BAR));
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
	public void removeFromBlueFlagList() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}		
		if(ESAPI.validator().isValidInput("statusRemark", remark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}	
		
		BlueFlaggedRcnsDao pdd=new BlueFlaggedRcnsDao(connection);		
		if(pdd.checkRemoveBlueFlagRole(myUserId,appId)==true)	{	
			pdd.removeFromRedFlagList(appId,remark,myUserId,myOfficeCode);
		}
		else
			throw new ValidationException("You are not authorized to modify  flag list.");
		
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
		BlueFlaggedRcnsDao pdd=new BlueFlaggedRcnsDao(connection);		
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
		blueFlag=pdd.getBlueFlag();
		removeblueFlag=pdd.getBlueFlagRemoveRole();
		//if(blueFlag.equals("YES") && (removeblueFlag.equals("YES"))){
			blueFlagDetailsList=pdd.getBlueFlagDetailsList();
	//	}
		annualStatusList=pdd.getAnnualStatusList(appId);
		blueRcnexist=pdd.getExistRcn(appId);
		
		
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

	

	public List<List3> getBlueFlagDetailsList() {
		return blueFlagDetailsList;
	}

	public void setBlueFlagDetailsList(List<List3> blueFlagDetailsList) {
		this.blueFlagDetailsList = blueFlagDetailsList;
	}

	public String getBlueFlag() {
		return blueFlag;
	}

	public void setBlueFlag(String blueFlag) {
		this.blueFlag = blueFlag;
	}

	public String getRemoveblueFlag() {
		return removeblueFlag;
	}

	public void setRemoveblueFlag(String removeblueFlag) {
		this.removeblueFlag = removeblueFlag;
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

	public List<List4> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<List4> historyList) {
		this.historyList = historyList;
	}

	

	


	public String getFlagdelete() {
		return flagdelete;
	}

	public void setFlagdelete(String flagdelete) {
		this.flagdelete = flagdelete;
	}


	public String getAnnualrcn() {
		return annualrcn;
	}

	public void setAnnualrcn(String annualrcn) {
		this.annualrcn = annualrcn;
	}

	

	public List<BlueFlaggedRcns> getBlueflaggedList() {
		return blueflaggedList;
	}

	public void setBlueflaggedList(List<BlueFlaggedRcns> blueflaggedList) {
		this.blueflaggedList = blueflaggedList;
	}

	public int getDeleteblueId() {
		return deleteblueId;
	}

	public void setDeleteblueId(int deleteblueId) {
		this.deleteblueId = deleteblueId;
	}

	public List<BlueFlaggedRcns> getAnnualblueflaggedList() {
		return annualblueflaggedList;
	}

	public void setAnnualblueflaggedList(List<BlueFlaggedRcns> annualblueflaggedList) {
		this.annualblueflaggedList = annualblueflaggedList;
	}

	public List<BlueFlaggedRcns> getAnnualStatusList() {
		return annualStatusList;
	}

	public void setAnnualStatusList(List<BlueFlaggedRcns> annualStatusList) {
		this.annualStatusList = annualStatusList;
	}

	public String getBlueRcnexist() {
		return blueRcnexist;
	}

	public void setBlueRcnexist(String blueRcnexist) {
		this.blueRcnexist = blueRcnexist;
	}




		

}
