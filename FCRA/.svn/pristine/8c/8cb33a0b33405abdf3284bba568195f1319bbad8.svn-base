package service.masters;
import java.sql.SQLException;
import java.util.List;
import org.owasp.esapi.ESAPI;
import models.reports.ReportType;
import dao.reports.ReportTypeDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class ReportTypeDetailsServices extends Commons {

	private static final Boolean recordStatus = null;
	

	List<KVPair<String, String>> reportTypeList;
	List<ReportType> reportList;
	List<KVPair<String, String>> availableOfficeList;
	List<KVPair<String, String>> assignedOfficeList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String reportName;
	public Integer reportId;
	private String assignedOffice;
    private Integer CreatedOn;
    private Integer EnteredOn;
   
	
	 public String execute() {
		begin();
		try {
				initReportTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	

     public void initReportTypeList() throws Exception{
		ReportTypeDao bdao=new ReportTypeDao(connection);
		reportTypeList=bdao.getKVList();
	}
	
	public String initializeReportList() {
		begin();
		try {
				populateReportList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populateReportList() throws Exception{
		ReportTypeDao bdao=new ReportTypeDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);				
		reportList=bdao.getMasterreport();
		totalRecords=bdao.getTotalRecords();
	}
	
	public String initReport() {
		begin();
		try {
				populateOffice();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateOffice() throws Exception{
		ReportTypeDao bdao=new ReportTypeDao(connection);
		if(validateReportId()==true){
		bdao.setReportId(reportId);
		availableOfficeList=bdao.getAvailableOffice();
		assignedOfficeList=bdao.getAssignedOffice();
			
	}
	}
	
	
	
	
	
	
	
	
	
	public String AddName() {
		begin();
		try {
				addreportName();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	public Boolean validatereportName() throws Exception{		
		if(ESAPI.validator().isValidInput("Report Name", reportName, "ActionPath", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Report Name - No Special Character allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public Boolean validateReportId() throws Exception{		
		if(ESAPI.validator().isValidInput("Report Id", reportId.toString(), "Num", 2, false) == false){
			notifyList.add(new Notification("Error!!", "Report Id - Only Number allowed (2 numbers max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public void addreportName() throws Exception{
		ReportTypeDao bdao=new ReportTypeDao(connection);
		ReportType report=new ReportType();
		if(validatereportName()==true){
		report.setReportId(reportId);
		report.setReportName(reportName);
	    report.setCreatedIp(myIpAddress);
	    report.setRecordStatus(recordStatus);
		report.setCreatedBy(myUserId);
		report.setLastModifiedBy(myUserId);
		report.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(report);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Report Name is Inserted successfully.", Status.SUCCESS, Type.BAR));
		 
		 setReportId(bdao.getReportId());
		}
	}
	public String saveOffice() {		
		try {
				begin();
				saveOfficeType();
				
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
	private void saveOfficeType() throws Exception{
		ReportTypeDao bdao=new ReportTypeDao(connection);
		ReportType report=new ReportType();
		if(validateReportId()==true){
		report.setCreatedBy(myUserId);
		report.setCreatedIp(myIpAddress);
		report.setRecordStatus(recordStatus);
		report.setLastModifiedBy(myUserId);
		report.setLastModifiedIp(myIpAddress);
		bdao.setReportId(reportId);
		bdao.setAssignedOffice(assignedOffice);
		String status=bdao.saveOffice(report);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Office Type for Report  are successfully saved.", Status.SUCCESS, Type.BAR));	
			
			}		
	}
	}
	public String EditReport () {
		begin();
		try {
				editreport();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editreport() throws Exception {
		ReportTypeDao bdao=new ReportTypeDao(connection);
		ReportType report=new ReportType();
		if(validatereportName()==true){
		if(validateReportId()==true){
		report.setReportId(reportId);
		report.setReportName(reportName);
		report.setLastModifiedBy(myUserId);
	  report.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(report);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Report Name is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
		}

}
public String DeleteReport() {
	begin();
	try {
			deletereportName();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletereportName() throws Exception {
	ReportTypeDao bdao=new ReportTypeDao(connection);
	ReportType report=new ReportType();
     if(validateReportId()==true){
     bdao.setReportId(reportId);
	String status=bdao.deletetable(report);		
	if(status.equals("success")){
		notifyList.add(new Notification("Success !!", "Report  are Deleted successfully .", Status.SUCCESS, Type.BAR));
}
}
}

public List<KVPair<String, String>> getReportTypeList() {
	return reportTypeList;
}


public void setReportTypeList(List<KVPair<String, String>> reportTypeList) {
	this.reportTypeList = reportTypeList;
}


public List<ReportType> getReportList() {
	return reportList;
}


public void setReportList(List<ReportType> reportList) {
	this.reportList = reportList;
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


public String getAssignedOffice() {
	return assignedOffice;
}


public void setAssignedOffice(String assignedOffice) {
	this.assignedOffice = assignedOffice;
}


public void setTotalRecords(String totalRecords) {
	this.totalRecords = totalRecords;
}





public String getReportName() {
	return reportName;
}


public Integer getReportId() {
	return reportId;
}


public void setReportName(String reportName) {
	this.reportName = reportName;
}


public void setReportId(Integer reportId) {
	this.reportId = reportId;
}


public static Boolean getRecordstatus() {
	return recordStatus;
}


public List<KVPair<String, String>> getAvailableOfficeList() {
	return availableOfficeList;
}


public void setAvailableOfficeList(
		List<KVPair<String, String>> availableOfficeList) {
	this.availableOfficeList = availableOfficeList;
}


public List<KVPair<String, String>> getAssignedOfficeList() {
	return assignedOfficeList;
}


public void setAssignedOfficeList(
		List<KVPair<String, String>> assignedOfficeList) {
	this.assignedOfficeList = assignedOfficeList;
}


public Integer getCreatedOn() {
	return CreatedOn;
}


public void setCreatedOn(Integer createdOn) {
	CreatedOn = createdOn;
}


public Integer getEnteredOn() {
	return EnteredOn;
}


public void setEnteredOn(Integer enteredOn) {
	EnteredOn = enteredOn;
}


	

	
}


