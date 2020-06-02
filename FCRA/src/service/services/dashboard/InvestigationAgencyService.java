package service.services.dashboard;

import java.sql.SQLException;
import java.util.List;

import models.services.requests.AbstractRequest;
import models.services.requests.InvestigationAgency;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.InformationException;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.services.dashboard.InvestigationAgencyReportDao;


public class InvestigationAgencyService extends Commons{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String stage;
	private List<AbstractRequest> applicationList;
	private String appId;
	private String appName;
	private Object obj=null;
	private Boolean doExist;
	private Boolean doAccept;	
	private String finalStatus;
	private List<List2> officeStatusList;
	List<KVPair<String, String>> officeNameList;
	private String reportFrom;
	private String reportDate;
	private String reportNumber;


	public String initInvestigation(){
		String result="error";
		begin();
		try {
			populateOfficeNameList();
			result="success";
		} catch(Exception e){ps(e);
		try {
			notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
					Type.BAR));
		} catch (Exception ex) {
	      }
		}
		finally{
			finish();
		}	
		
		return result;
		//return "success";
	}
	
	public void populateOfficeNameList() throws Exception{
		InvestigationAgencyReportDao pdd=new InvestigationAgencyReportDao(connection);
		officeNameList=pdd.getKVList();
	}
	
	public String initApplicationListDetails() throws Exception{
		begin();
		try {
				populateApplicationListDetails();
		} catch(ValidationException ve){
			try{
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			}catch(NotificationException e) {				
				e.printStackTrace();			
		 }
		return "error";	
		}
			/*try {
				notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
				connection.rollback();
			} catch (Exception e) {				
			e.printStackTrace();
		}*/
			finally{
				finish();
			}		
			return "success";
		}
	
	private void populateApplicationListDetails() throws Exception{
		InvestigationAgencyReportDao pdd=new InvestigationAgencyReportDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		if(nob(appId)){
			pdd.setSearchFlag("name");
			pdd.setSearchString(appName);
		}			
		else{
			pdd.setSearchFlag("id");
			pdd.setSearchString(appId);
		}					
		applicationList=pdd.getApplicationListDetails();
		totalRecords=pdd.getTotalRecords();
	}
	
	public String initSubmitInvestigationReport() throws Exception{
	String result = "error";	
		begin();
		try {
			submitInvestigationReport();
		} catch(ValidationException ve){
			try{
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
				connection.rollback();
			}catch(Exception e) {				
				e.printStackTrace();			
			}
			return "error";	
		}
		 catch(Exception ve){
				try{
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				}catch(Exception e) {				
					e.printStackTrace();			
				}
				return "error";	
			}
		
		finally{
			finish();
		}		
		return "success";
	}
	
	 private void submitInvestigationReport() throws Exception {
		if(validateParameters()==true){
			InvestigationAgencyReportDao iad=new InvestigationAgencyReportDao(connection);
			InvestigationAgency ia=new InvestigationAgency();
			ia.setApplicationId(appId);
			ia.setReportFrom(reportFrom);
			ia.setReportNumber(reportNumber);
			ia.setReportDate(reportDate);
			ia.setMyUserId(myUserId);
			ia.setMyOfficeCode(myOfficeCode);
			iad.insertRecord(ia);
			notifyList.add(new Notification("Success!!","Investigation Report for Application ID <b>"+appId+"</b> has been updated." , Status.SUCCESS, Type.BAR));									
		}			
	}
						
	public Boolean validateParameters() throws Exception{									
			if(ESAPI.validator().isValidInput("InvestigationAgency","reportFrom", "WordS",100, false) == false){
				notifyList.add(new Notification("Error!!", "Invalid Office", Status.ERROR, Type.BAR));
				return false;
			}
			if(ESAPI.validator().isValidInput("InvestigationAgency","reportNumber" , "WordSS",150, false) == false){
				notifyList.add(new Notification("Error!!", "Invalid report Number", Status.ERROR, Type.BAR));
				return false;
			}
			if(ESAPI.validator().isValidInput("InvestigationAgency","reportDate" , "WordSS",150, false) == false){
				notifyList.add(new Notification("Error!!", "Invalid report Date", Status.ERROR, Type.BAR));
				return false;
			}
			return true;				
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
		}catch(InformationException ve){/*			
		
		*/}catch(Exception e){
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
		InvestigationAgencyReportDao pdd=new InvestigationAgencyReportDao(connection);	
		officeStatusList=pdd.officeStatusList(appId);
		applicationList=pdd.getApplicationDetails(appId,myOfficeCode,myUserId);			
		finalStatus=pdd.getFinalStatus();
		
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

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		if(appId!=null)
			this.appId = appId.trim();
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

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Boolean getDoExist() {
		return doExist;
	}

	public void setDoExist(Boolean doExist) {
		this.doExist = doExist;
	}

	public Boolean getDoAccept() {
		return doAccept;
	}

	public void setDoAccept(Boolean doAccept) {
		this.doAccept = doAccept;
	}	

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public List<List2> getOfficeStatusList() {
		return officeStatusList;
	}

	public void setOfficeStatusList(List<List2> officeStatusList) {
		this.officeStatusList = officeStatusList;
	}

	public List<KVPair<String, String>> getOfficeNameList() {
		return officeNameList;
	}

	public void setOfficeNameList(List<KVPair<String, String>> officeNameList) {
		this.officeNameList = officeNameList;
	}

	public String getReportFrom() {
		return reportFrom;
	}

	public void setReportFrom(String reportFrom) {
		this.reportFrom = reportFrom;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	


	

}
