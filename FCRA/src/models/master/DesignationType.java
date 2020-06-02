package models.master;

public class DesignationType {
	private String designationId;
	private String designationName;
	private String shortDesignation;
	private String recordStatus;
	
	private Integer createddate;
	private String createdIp;
	private String createdOn;
	private String enteredOn;
	
	private String lastModifiedIp;
	private Integer lastModifiedDate;
	private String lastModifiedBy;	
	private String enteredBy;
	private String rowIdentifier;
	  private String assignedDesc;
	
	
   	public DesignationType(String designationId, String designationName, String shortDesignation, String enteredOn ) {
    	
             this.designationName=designationName;
			this.designationId=designationId;
			this.shortDesignation=shortDesignation;
			this.enteredOn=enteredOn;
		}
	public DesignationType() {

	}
	 public DesignationType(String designationId, String designationName,String shortDesignation,String recordStatus,String enteredon,String enteredBy,Integer createddate,String lastModifiedIp, String lastModifiedBy, Integer lastModifiedDate )
	 {
		 this.shortDesignation=shortDesignation;
		 this.designationId=designationId;
		 this.enteredBy=enteredBy;
		 this.enteredOn=enteredon;
		 this.recordStatus=recordStatus;
		 this.createddate=createddate;
		 this.designationName=designationName;
		 this.lastModifiedBy=lastModifiedBy;
		 this.lastModifiedDate=lastModifiedDate;
		 this.lastModifiedIp=lastModifiedIp;
		 
		 
		 }
	public Integer getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Integer createddate) {
		this.createddate = createddate;
	}
	public String getCreatedIp() {
		return createdIp;
	}
	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getEnteredOn() {
		return enteredOn;
	}
	public void setEnteredOn(String enteredOn) {
		this.enteredOn = enteredOn;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getLastModifiedIp() {
		return lastModifiedIp;
	}
	public void setLastModifiedIp(String lastModifiedIp) {
		this.lastModifiedIp = lastModifiedIp;
	}
	public Integer getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Integer lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getDesignationId() {
		return designationId;
	}
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getShortDesignation() {
		return shortDesignation;
	}
	public void setShortDesignation(String shortDesignation) {
		this.shortDesignation = shortDesignation;
	}
		public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	public String getAssignedDesc() {
		return assignedDesc;
	}
	public void setAssignedDesc(String assignedDesc) {
		this.assignedDesc = assignedDesc;
	}

}
