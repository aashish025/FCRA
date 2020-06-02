package models.reports;

public class ExemptionRenewalBlocking {
	
	private String applicationId;
	private String rcn;
	private String assoName;
	private String validFrom;
	private String validTo;
	private String remarks;
	private String actionDate;
	private String actionBy;
	private String recordStatus;
	private String intimationDate;
	private String exemptionDays;
	private String myUserId;
	
	
	public ExemptionRenewalBlocking(){
		
	}
	
	
	public ExemptionRenewalBlocking(String assoName, String remarks,
			String actionDate, String exemptionDays) {
		super();
		this.assoName = assoName;
		this.remarks = remarks;
		this.actionDate = actionDate;
		this.exemptionDays = exemptionDays;
	}


	public ExemptionRenewalBlocking(String rcn, String assoName,
			String remarks, String actionDate, String exemptionDays) {
		super();
		this.rcn = rcn;
		this.assoName = assoName;
		this.remarks = remarks;
		this.actionDate = actionDate;
		this.exemptionDays = exemptionDays;
	}



	public String getRcn() {
		return rcn;
	}
	public void setRcn(String rcn) {
		this.rcn = rcn;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getActionDate() {
		return actionDate;
	}
	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getIntimationDate() {
		return intimationDate;
	}
	public void setIntimationDate(String intimationDate) {
		this.intimationDate = intimationDate;
	}

	
	public String getAssoName() {
		return assoName;
	}

	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}



	public String getExemptionDays() {
		return exemptionDays;
	}



	public void setExemptionDays(String exemptionDays) {
		this.exemptionDays = exemptionDays;
	}


	public String getMyUserId() {
		return myUserId;
	}


	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}


	public String getApplicationId() {
		return applicationId;
	}


	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	
	
	

}
