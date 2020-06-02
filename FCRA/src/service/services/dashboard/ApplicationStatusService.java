package service.services.dashboard;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import models.services.requests.AbstractRequest;

import org.owasp.esapi.ESAPI;

import dao.services.dashboard.ApplicationStatusDao;
import dao.services.dashboard.ProjectDashboardDao;
import utilities.Commons;
import utilities.GeneratePdfVirtualizer;
import utilities.InformationException;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class ApplicationStatusService extends Commons{
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;	
	private List<AbstractRequest> applicationList;
	private List<List2> cancellationReasonList=new ArrayList<List2>();;
	private String appId;
	private String appName;
	private String statusRemark;
	private String cancellationType;
	private String cancellationReason;
	private String requestDate;
	private String actionFlag;
	private Map  parameters = new HashMap();
	private String tsPath=null;
	private String fileName=null;
	
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
	
	public String initCancelRegistrationDetails(){
		begin();
		try {
				cancelRegistrationDetails();
				notifyList.add(new Notification("Success !!", "Registration of Association with Registration Number <b>"+appId+"</b> is <b>CANCELLED</b> successfully.", 
							Status.SUCCESS, Type.BAR));
							
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"cancelModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"cancelModal-error"));
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
	
	public String initRevokeRegistrationDetails(){
		begin();
		try {
				revokeRegistrationDetails();
				notifyList.add(new Notification("Success !!", "Registration Cancellation of Association with Registration Number <b>"+appId+"</b> is <b>REVOKED</b> successfully.", 
							Status.SUCCESS, Type.BAR));
							
			}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR,"remarkModal-error"));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR,"revokeModal-error"));
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
	
	private void cancelRegistrationDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}
		if(ESAPI.validator().isValidInput("cancellationType", cancellationType, "Alpha", 1, false) == false){
			throw new ValidationException("Invalid Cancellation Type.Only one character allowed.");
		}
		if(ESAPI.validator().isValidInput("cancellationReason", cancellationReason, "PaymentModes", 50, false) == false){
			throw new ValidationException("Invalid Reason.Please provide a valid value.");
		}
		if(ESAPI.validator().isValidInput("RequestDate", requestDate, "Date", 15, true) == false){
			throw new ValidationException("Invalid Date.");
		}		
        cancellationReason = cancellationReason.replaceAll(",", "");
		ApplicationStatusDao pdd=new ApplicationStatusDao(connection);		
		pdd.cancelApplicationDetails(appId,statusRemark,cancellationReason,myUserId,requestDate,cancellationType,myOfficeCode);				
	}
	
	private void revokeRegistrationDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}
		if(ESAPI.validator().isValidInput("statusRemark", statusRemark, "WordSS", 2000, false) == false){
			throw new ValidationException("Invalid Status Remark.Only 2000 characters are allowed.");
		}	
		ApplicationStatusDao pdd=new ApplicationStatusDao(connection);		
		pdd.revokeApplicationDetails(appId,statusRemark,myUserId,myOfficeCode);				
	}
	
	private void getApplicationDetails() throws Exception{
		if(ESAPI.validator().isValidInput("applicationId", appId, "Word", 15, false) == false){
			throw new ValidationException("Invalid Application ID");
		}	
		ApplicationStatusDao pdd=new ApplicationStatusDao(connection);		
		pdd.getApplicationDetails(appId,myOfficeCode,myUserId);	
		applicationList=pdd.getApplicationList();	
		getAdditionalResources();
	}
	
	private void getAdditionalResources() throws Exception{
		ApplicationStatusDao pdd=new ApplicationStatusDao(connection);
		cancellationReasonList=pdd.getCancellationReasonList();
	}
	
	private void populateApplicationListDetails() throws Exception{
		ApplicationStatusDao pdd=new ApplicationStatusDao(connection);						
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
	public void GetCancelReport(HttpServletResponse response) {
		// TODO Auto-generated method stub
		begin();
		try {					
			String query="SELECT USER_NAME,(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER WHERE "
					+ "USER_ID=?)) AS DESG,(SELECT CONTACT_NO FROM TM_OFFICE WHERE OFFICE_CODE=?) AS CONTACT ,(SELECT ADDRESS FROM TM_OFFICE WHERE OFFICE_CODE=?) AS address,"
					+ " (SELECT CITY_NAME FROM TM_OFFICE WHERE OFFICE_CODE=?) AS city ,( select ZIPCODE FROM TM_OFFICE WHERE OFFICE_CODE=?) AS pinCode "
					+ "FROM TM_USER WHERE USER_ID=?";
			statement=connection.prepareStatement(query);
			statement.setString(1, myUserId);
			statement.setString(2, myOfficeCode);
			statement.setString(3, myOfficeCode);
			statement.setString(4, myOfficeCode);
			statement.setString(5, myOfficeCode);
			statement.setString(6, myUserId);
			ResultSet rs=statement.executeQuery();
			while(rs.next()){
				parameters.put("officerName", rs.getString(1));
				parameters.put("officerDesg", rs.getString(2));
				parameters.put("officerContact", rs.getString(3));
				parameters.put("officeAddress", rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6));
			}	
			statement.close();
			rs.close();
			parameters.put("regNumber", appId);							
			String reportQuery="SELECT ASSO_NAME,ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE= substr(STDIST,1,2))||', ' || "
					+ "(SELECT DISTNAME FROM TM_DISTRICT  WHERE DISTCODE=substr(stdist,-3,3) and scode =substr(stdist,1,2))||', ' || ASSO_PIN,"
					+ "to_char(REG_DATE,'dd-mm-yyyy') as REG_DATE, CANCEL_TYPE FROM FC_INDIA WHERE RCN=?";
			statement = connection.prepareStatement(reportQuery); 	
			statement.setString(1, appId);
			ResultSet rsReg=statement.executeQuery();	 
			if(rsReg.next()){			
				parameters.put("assoName", rsReg.getString(1));			
				parameters.put("assoAddress", rsReg.getString(2));
				parameters.put("regDate", rsReg.getString(3));
			}
			if(rsReg.getString(4).equals("V"))
				tsPath="/Reports/Cancellation_On_Violation.jrxml";
			else if (rsReg.getString(4).equals("R"))
				tsPath = "/Reports/RegCancellationOnRequest.jrxml";
			fileName = "Cancellation Report";
			GeneratePdfVirtualizer.asInlineWithDB(tsPath, parameters, connection, fileName);
		}catch (Exception e) {					
		}
		finally{	
			//finish();
		}	
		
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

	public List<List2> getCancellationReasonList() {
		return cancellationReasonList;
	}

	public void setCancellationReasonList(List<List2> cancellationReasonList) {
		this.cancellationReasonList = cancellationReasonList;
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

	
}
