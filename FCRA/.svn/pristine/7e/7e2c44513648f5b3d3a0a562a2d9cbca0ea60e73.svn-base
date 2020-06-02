package service.reports;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.reports.ExemptionRenewalBlocking;
import models.reports.OldRegistrationEntryDtl;
import models.services.requests.AbstractRequest;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.reports.ExemptionRenewalDao;
import dao.reports.OldRegistrationEntryDao;
import dao.reports.RegistrationTrackingDao;

public class ExemptionRenewalBlockingServices extends Commons{
	private String rcn;
    private String applicationId;
    private String exemptionDays;
    private String remarks;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private List<ExemptionRenewalBlocking> rcnList;
	private List<List2> stateList;
	private List<List2> districtList;
	private String state;
	private String appName;
	private String functionaryName;
	private String district;
	private List<AbstractRequest> applicationList;
	private List<AbstractRequest> applicationListadvsearch = new ArrayList<AbstractRequest>();

	
	
	public String execute() {
		String result = "error";
		begin();
		try {
			
	        result = "success";
		}  catch (Exception e) {
			ps(e);
			try {
				notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}
		finish();
		return result;
	}
	
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
	
	private void populateAdvanceApplicationListDetails() throws Exception{
		ExemptionRenewalDao pdd=new ExemptionRenewalDao(connection);					
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setSearchString(appName);
		pdd.setFunctionaryName(functionaryName);
		pdd.setState(state);
		pdd.setDistrict(district);
		applicationList=pdd.getApplicationListDetailsExemptionRenewal();
		totalRecords=pdd.getTotalRecords();
	}
	
	
	private void populateRcnListDetails() throws Exception{
		ExemptionRenewalDao erd=new ExemptionRenewalDao(connection);						
		erd.setPageNum(pageNum);
		erd.setRecordsPerPage(recordsPerPage);
		erd.setSortColumn(sortColumn);
		erd.setSortOrder(sortOrder);	
		
		rcnList=erd.getRcnListDetails();
		totalRecords=erd.getTotalRecords();
	}
	
	//initAddExemptionRenewalCase
	public String initAddExemptionRenewalCase() throws NotificationException {
		String result = "error";	
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
		// TODO Auto-generated method stub
		//if(validateParameters()==true){
		ExemptionRenewalDao erd=new ExemptionRenewalDao(connection);	
		ExemptionRenewalBlocking erb=new ExemptionRenewalBlocking();
		erb.setApplicationId(applicationId.toUpperCase());
		erb.setRemarks(remarks);
		erb.setExemptionDays(exemptionDays);
		erb.setMyUserId(myUserId);
	    int status=	erd.insertRecord(erb);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Exemption Details added successfully", Status.SUCCESS, Type.BAR));		
		
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

	public List<ExemptionRenewalBlocking> getRcnList() {
		return rcnList;
	}

	public void setRcnList(List<ExemptionRenewalBlocking> rcnList) {
		this.rcnList = rcnList;
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

	

	


	
	
}
