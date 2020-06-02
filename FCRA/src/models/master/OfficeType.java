package models.master;

import java.util.Date;

public class OfficeType {

	// Fields
	private String officeId;
	private String officeType;
	private String officeName;
	private String pacFilenoSearch;
	private Boolean recordStatus;
	private Short displayOrder;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private String lastModifiedDate;
	private String rowIdentifier;
	private String enteredOn;
	

	// Constructors

	/** default constructor */
	public OfficeType() {
	}
	
	
	public OfficeType(String officeId, String officeType) {
		this.officeId = officeId;
		this.officeType = officeType;
		
	}
		
	public OfficeType(String officeId, String officeType ,String officeNmae) {
		this.officeId = officeId;
		this.officeType = officeType;
	   this.officeType = officeType;;
		
	}
		
	/** full constructor */
	public OfficeType(String officeId, String officeType, String officeName,
			Boolean recordStatus, Short displayOrder, String pacFilenoSearch,
			String createdBy, String createdIp, String createdDate,
			String rowIdentifier) {
		this.officeId = officeId;
		this.officeType = officeType;
		this.officeName = officeName;
		this.pacFilenoSearch = pacFilenoSearch;
		this.recordStatus = recordStatus;
		this.displayOrder = displayOrder;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.rowIdentifier = rowIdentifier;
	}

	// Property accessors
	
	public OfficeType(String officeId, String officeType, String officeName,String enteredOn) {
		 this.officeId=officeId;
		 this.officeType=officeType;
		 this.officeName=officeName;
		 this.enteredOn=enteredOn;
		
		
		// TODO Auto-generated constructor stub
	}


	public String getOfficeId() {
		return this.officeId;
	}

	

	public String getOfficeType() {
		return this.officeType;
	}

	public void setOfficeType(String officeType) {
		this.officeType = officeType;
	}

	public String getOfficeName() {   ///@@@@@@@@@
		return this.officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	
	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	public Short getDisplayOrder() {
		return displayOrder;
	}
    
	
	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getCreatedIp() {
		return createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}
	
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
    
	
	public String getPacFilenoSearch() {
		return pacFilenoSearch;
	}


	public void setPacFilenoSearch(String pacFilenoSearch) {
		this.pacFilenoSearch = pacFilenoSearch;
	}


	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}

	  public String getLastModifiedBy() {
		return lastModifiedBy;
	}


	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}


	public String getLastModifiedIp() {
		return lastModifiedIp;
	}


	public void setLastModifiedIp(String lastModifiedIp) {
		this.lastModifiedIp = lastModifiedIp;
	}


	public String getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public String getEnteredOn() {
		return enteredOn;
	}


	public void setEnteredOn(String enteredOn) {
		this.enteredOn = enteredOn;
	}


	@Override
	  public String toString()
	  {
	    return officeType;
	  }


	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
}