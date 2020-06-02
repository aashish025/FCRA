package models.services.requests;

public class InvestigationAgency {
	private String applicationId;
	private String reportFrom;
	private String reportNumber;
	private String reportDate;
	private String reportDocSno;
	private String enteredBy;
	private String enteredOn;
	private String myUserId;
	private String myOfficeCode;
	
	
	public InvestigationAgency(){
		
	}
		
	
	public InvestigationAgency(String applicationId, String reportFrom,
			String reportNumber, String reportDate, String reportDocSno,
			String enteredBy, String enteredOn) {
		super();
		this.applicationId = applicationId;
		this.reportFrom = reportFrom;
		this.reportNumber = reportNumber;
		this.reportDate = reportDate;
		this.reportDocSno = reportDocSno;
		this.enteredBy = enteredBy;
		this.enteredOn = enteredOn;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getReportFrom() {
		return reportFrom;
	}
	public void setReportFrom(String reportFrom) {
		this.reportFrom = reportFrom;
	}
	public String getReportNumber() {
		return reportNumber;
	}
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportDocSno() {
		return reportDocSno;
	}
	public void setReportDocSno(String reportDocSno) {
		this.reportDocSno = reportDocSno;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getEnteredOn() {
		return enteredOn;
	}
	public void setEnteredOn(String enteredOn) {
		this.enteredOn = enteredOn;
	}
	public String getMyUserId() {
		return myUserId;
	}
	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}


	public String getMyOfficeCode() {
		return myOfficeCode;
	}


	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}
	
	
	

}
