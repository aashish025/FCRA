package service.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.owasp.esapi.ESAPI;

import models.services.ExemptionAnnualPenalty;
import models.services.requests.AbstractRequest;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.reports.RegistrationTrackingDao;
import dao.services.ExemptionAnnualPenaltyDao;

public class ExemptionAnnualPenaltyServices extends Commons{
	private String appId;
	private String rcn;
    private String applicationId;
    private String exemptionDays;
    private String remarks;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private List<ExemptionAnnualPenalty> rcnList;
	private List<List2> stateList;
	private List<List2> districtList;
	private String checkblkYear;;
	private String blockYear;
	private String state;
	private String appName;
	private String functionaryName;
	private String district;
	private List<AbstractRequest> applicationList;
	private List<AbstractRequest> applicationListadvsearch = new ArrayList<AbstractRequest>();
	private List<String> checkBlkYear;
	private List<List2> rcnDetail;
	private List<Notification> emailNotifyList = new ArrayList<Notification>();

	
	public void initExemptionListCases(){
		begin();
		try {
				populateRcnListDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
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
	
	public void populateRegistrationTrackingAction() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);		
		stateList=pdd.getStateList();
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
	private void getDistrict() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);
		districtList=pdd.getDistrictList(state);	
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
	private void getApplicationDetailsExemption() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);	
		ExemptionAnnualPenaltyDao eapd= new ExemptionAnnualPenaltyDao(connection);
		pdd.getApplicationListExemption(appId,myOfficeCode,myUserId);	
		applicationList=pdd.getApplicationList();	
		checkBlkYear=eapd.checkBlockYearList(appId);
		
	}
	
	private void populateAdvanceApplicationListDetails() throws Exception{
		ExemptionAnnualPenaltyDao pdd=new ExemptionAnnualPenaltyDao(connection);					
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setSearchString(appName);
		pdd.setFunctionaryName(functionaryName);
		pdd.setState(state);
		pdd.setDistrict(district);
		applicationList=pdd.getApplicationListDetailsExemptionAnnual();
		totalRecords=pdd.getTotalRecords();
	}
	
	
	private void populateRcnListDetails() throws Exception{
		ExemptionAnnualPenaltyDao erd=new ExemptionAnnualPenaltyDao(connection);						
		erd.setPageNum(pageNum);
		erd.setRecordsPerPage(recordsPerPage);
		erd.setSortColumn(sortColumn);
		erd.setSortOrder(sortOrder);	
		rcnList=erd.getRcnListDetails();
		totalRecords=erd.getTotalRecords();
	}
	
	public String initApplicationListDetails1(){
		String result="error";
		begin();
		try {
			    SearchingRcnDetails();		
				
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
	
	private void SearchingRcnDetails() throws Exception{
		ExemptionAnnualPenaltyDao erd=new ExemptionAnnualPenaltyDao(connection);							
		erd.setAppId(appId);				
		rcnDetail=erd.getApplicationListDetailsExemption();
	}
	//initAddExemptionRenewalCase
	public String initAddExemptionAnnualCase() throws NotificationException {
		begin();
		try {
			addExemptionDetails();
		} catch(ValidationException ve){
			notifyList.add(new Notification("Error!",ve.getMessage(), Status.ERROR, Type.BAR));
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}			
		}catch(Exception e){
			notifyList.add(new Notification("Error!","Some unexpected error occured.", Status.ERROR, Type.BAR));
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			spl(e);			
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void addExemptionDetails() throws Exception {
		ExemptionAnnualPenaltyDao erd=new ExemptionAnnualPenaltyDao(connection);	
		ExemptionAnnualPenalty erb=new ExemptionAnnualPenalty();
		erb.setApplicationId(applicationId.toUpperCase());
		erb.setRemarks(remarks);
		erb.setExemptionDays(exemptionDays);
		erb.setMyUserId(myUserId);
		erb.setBlockYear(blockYear);
	    int status=	erd.insertRecord(erb);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Exemption Details added successfully", Status.SUCCESS, Type.BAR));
			emailNotifyList=erd.getNotifyList();
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


	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getExemptionDays() {
		return exemptionDays;
	}

	public void setExemptionDays(String exemptionDays) {
		this.exemptionDays = exemptionDays;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<List2> getStateList() {
		return stateList;
	}

	public void setStateList(List<List2> stateList) {
		this.stateList = stateList;
	}

	public List<List2> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<List2> districtList) {
		this.districtList = districtList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}

	public void setDistrict(String district) {
		this.district = district;
	}


	public List<AbstractRequest> getApplicationListadvsearch() {
		return applicationListadvsearch;
	}

	public void setApplicationListadvsearch(
			List<AbstractRequest> applicationListadvsearch) {
		this.applicationListadvsearch = applicationListadvsearch;
	}
	public String getRcn() {
		return rcn;
	}
	public void setRcn(String rcn) {
		this.rcn = rcn;
	}
	public List<ExemptionAnnualPenalty> getRcnList() {
		return rcnList;
	}
	public void setRcnList(List<ExemptionAnnualPenalty> rcnList) {
		this.rcnList = rcnList;
	}
	public String getCheckblkYear() {
		return checkblkYear;
	}
	public void setCheckblkYear(String checkblkYear) {
		this.checkblkYear = checkblkYear;
	}
	public String getBlockYear() {
		return blockYear;
	}
	public void setBlockYear(String blockYear) {
		this.blockYear = blockYear;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public List<String> getCheckBlkYear() {
		return checkBlkYear;
	}
	public void setCheckBlkYear(List<String> checkBlkYear) {
		this.checkBlkYear = checkBlkYear;
	}
	public List<List2> getRcnDetail() {
		return rcnDetail;
	}
	public void setRcnDetail(List<List2> rcnDetail) {
		this.rcnDetail = rcnDetail;
	}
	public List<Notification> getEmailNotifyList() {
		return emailNotifyList;
	}
	public void setEmailNotifyList(List<Notification> emailNotifyList) {
		this.emailNotifyList = emailNotifyList;
	}
	
	
}
