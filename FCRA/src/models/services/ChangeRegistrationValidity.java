package models.services;

public class ChangeRegistrationValidity {
	private String applicationId;
	private String actionBy;
	private String validityFrom; 
	private String validityUpTo;
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		if(applicationId != null){
		this.applicationId = applicationId.toUpperCase();
		}
	}
	
	
	public String getActionBy() {
		return actionBy;
	}
	public void setActionBy(String actionBy) {
		this.actionBy = actionBy;
	}
	public String getValidityFrom() {
		return validityFrom;
	}
	public String getValidityUpTo() {
		return validityUpTo;
	}
	public void setValidityFrom(String validityFrom) {
		this.validityFrom = validityFrom;
	}
	public void setValidityUpTo(String validityUpTo) {
		this.validityUpTo = validityUpTo;
	}

}
