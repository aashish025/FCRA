package models.reports;

public class UserActivityReport {
	private String  sino;
	private String	userName;
	private String	userId;
	private String	serviceName;
	private String	pendProcessing;
	private String	pendMail;
	private String	serviceCode;
	private String	processedCount;
	private String	officeCode;
	private String	receivedCount;
	private String	applicationId;
	private String granted;
	private String denied;
	private String closed;
	private String disposed;
	private String pendDays;
	private String statusName;
	private String state;
	private String applicantName;
	private String activityOn;
	public UserActivityReport(String userName,String serviceName,String pendProcessing,String pendMail,
			 String sino,String state) {
			super();
			
			this.userName = userName;
			this.serviceName = serviceName;
			this.pendProcessing = pendProcessing;
			this.pendMail = pendMail;
			this.sino = sino;
		    this.state=state;
		}
	
	public UserActivityReport(String applicationId,String userName,String serviceName,String pendDays , String statusName, String sino,String state,String applicantName,int a)
	{
		this.applicationId=applicationId;
		this.userName=userName;
		this.serviceName=serviceName;
		this.statusName=statusName;
		this.pendDays=pendDays;
		this.sino=sino;
		this.state=state;
		this.applicantName=applicantName;
	}
	public UserActivityReport(String applicationId,String serviceName,String serviceCode,String officeCode,String userName,String userId,String receivedCount,String processedCount,
			 String sino,String state,String applicantName,int a) {
			super();
			
			this.applicationId = applicationId;
			this.serviceName = serviceName;
			this.serviceCode = serviceCode;
			this.officeCode = officeCode;
			this.userName = userName;
			this.userId = userId;
			this.receivedCount = receivedCount;
			this.processedCount = processedCount;
			this.sino = sino;
			this.state=state;
			this.applicantName=applicantName;
		
		}
	public UserActivityReport(String userName,String serviceName,String userId,String serviceCode,String processedCount,String receivedCount,String officeCode,
			 String state) {
			super();
			
			this.userName = userName;
			this.serviceName = serviceName;
			this.userId = userId;
			this.serviceCode = serviceCode;
			this.processedCount = processedCount;
			this.receivedCount = receivedCount;
			this.officeCode = officeCode;
			this.state = state;
		
		}
	
	
	public UserActivityReport(String userName,String serviceName,String officeCode,String userId,String serviceCode,String granted,String denied,String closed,String disposed,String state) {
			super();
			
			this.userName = userName;
			this.serviceName = serviceName;
			this.officeCode = officeCode;
			this.userId = userId;
			this.serviceCode = serviceCode;
			this.granted = granted;
			this.denied = denied;
			this.closed = closed;
			this.disposed = disposed;
			this.state = state;
		
		}
	
	
	public UserActivityReport(String applicationId,String serviceName,String serviceCode,String officeCode,String userName,String userId,String granted,String denied,String closed,String disposed,
			 String sino,String state,String applicantName,String activityOn ) {
			super();
			
			this.applicationId = applicationId;
			this.serviceName = serviceName;
			this.serviceCode = serviceCode;
			this.officeCode = officeCode;
			this.userName = userName;
			this.userId = userId;
			this.granted = granted;
			this.denied = denied;
			this.closed = closed;
			this.disposed = disposed;
			this.sino = sino;
		this.state=state;
		this.applicantName=applicantName;
		this.activityOn=activityOn;
		}
	

	
	
	
	public String getGranted() {
		return granted;
	}


	public void setGranted(String granted) {
		this.granted = granted;
	}


	public String getDenied() {
		return denied;
	}


	public void setDenied(String denied) {
		this.denied = denied;
	}


	public String getClosed() {
		return closed;
	}


	public void setClosed(String closed) {
		this.closed = closed;
	}


	public String getDisposed() {
		return disposed;
	}


	public void setDisposed(String disposed) {
		this.disposed = disposed;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getServiceCode() {
		return serviceCode;
	}


	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}


	public String getProcessedCount() {
		return processedCount;
	}


	public void setProcessedCount(String processedCount) {
		this.processedCount = processedCount;
	}


	public String getOfficeCode() {
		return officeCode;
	}


	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}


	public String getReceivedCount() {
		return receivedCount;
	}


	public void setReceivedCount(String receivedCount) {
		this.receivedCount = receivedCount;
	}


	public String getSino() {
		return sino;
	}

	public void setSino(String sino) {
		this.sino = sino;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getPendProcessing() {
		return pendProcessing;
	}

	public void setPendProcessing(String pendProcessing) {
		this.pendProcessing = pendProcessing;
	}

	public String getPendMail() {
		return pendMail;
	}

	public void setPendMail(String pendMail) {
		this.pendMail = pendMail;
	}


	public String getApplicationId() {
		return applicationId;
	}


	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getPendDays() {
		return pendDays;
	}

	public void setPendDays(String pendDays) {
		this.pendDays = pendDays;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getActivityOn() {
		return activityOn;
	}

	public void setActivityOn(String activityOn) {
		this.activityOn = activityOn;
	}
	
	

}
