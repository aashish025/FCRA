package service.services;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;
import models.services.ChangeAssociation;
import models.services.requests.AbstractRequest;
import utilities.Commons;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.AssociationTypeDao;
import dao.reports.RegistrationTrackingDao;
import dao.services.ChangeAssociationDao;

public class ChangeAssociationService  extends Commons{
		private List<List2> stateList;
		private List<List2> assoType;
		private List<List2> districtList;
		private List<List2> rcnDetail;
		private List<AbstractRequest> applicationList;
		private String state;
		private String pageNum;
		private String recordsPerPage;
		private String sortColumn;
		private String sortOrder;
		private String totalRecords;
		private String appName;
		private String functionaryName;
		private String district;
		private String appId;
		private String applicationId;
		private String assocType;
		
		
	public void initRegistrationTrackingAction(){
		begin();
		try {
				populateRegistrationTrackingAction();				
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
	
	public void populateRegistrationTrackingAction() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);	
		AssociationTypeDao at= new AssociationTypeDao(connection);
		stateList=pdd.getStateList();
		assoType=at.getAssociationTypeList();
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
		ChangeAssociationDao pdd=new ChangeAssociationDao(connection);					
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setSearchString(appName);
		pdd.setFunctionaryName(functionaryName);
		pdd.setState(state);
		pdd.setDistrict(district);
		applicationList=pdd.getApplicationListDetailsChangeAss();
		totalRecords=pdd.getTotalRecords();
	}
	
	
	
	private void getDistrict() throws Exception{
		RegistrationTrackingDao pdd=new RegistrationTrackingDao(connection);
		districtList=pdd.getDistrictList(state);	
	}	
	public String initGetApplicationChangeAsso(){
		begin();
		try {
				getApplicationDetailsAssociationChange();							
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
	private void getApplicationDetailsAssociationChange() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		ChangeAssociationDao pdd=new ChangeAssociationDao(connection);	
		pdd.getApplicationListChange(appId,myOfficeCode,myUserId);	
		applicationList=pdd.getApplicationList();	
		
		
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
		ChangeAssociationDao erd=new ChangeAssociationDao(connection);							
		//erd.setAppId(appId);				
		rcnDetail=erd.getApplicationListDetailsAsso(appId);
	}
	
	public String submitAssociation() throws NotificationException {
		begin();
		try {
			addDetails();
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
	
	private void addDetails() throws Exception {
		ChangeAssociationDao erd=new ChangeAssociationDao(connection);		
		ChangeAssociation erb=new ChangeAssociation();
		erb.setApplicationId(applicationId);
	    erb.setAssocType(assocType);
	    erb.setActionBy(myUserId);
	    int status=	erd.insertRecord(erb);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Association Type changed successfully .", Status.SUCCESS, Type.BAR));
			
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

	public void setDistrict(String district) {
		this.district = district;
	}

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public List<List2> getAssoType() {
		return assoType;
	}

	public void setAssoType(List<List2> assoType) {
		this.assoType = assoType;
	}

	public List<List2> getRcnDetail() {
		return rcnDetail;
	}

	public void setRcnDetail(List<List2> rcnDetail) {
		this.rcnDetail = rcnDetail;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getAssocType() {
		return assocType;
	}

	public void setAssocType(String assocType) {
		this.assocType = assocType;
	}

}
