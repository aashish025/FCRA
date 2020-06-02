package models.master;

import java.sql.Date;


public class DocumentType  {
	
	// Fields
	  
    private String  officeCode;
	private Short   documentTypeId;
	private String  documentType;
	private Short   displayOrder;
	private Boolean recordStatus;
    private String  createdBy;
	private String  createdIp;
	private Date    createdDate;
	private String  rowIdentifier;
	
	// Constructors

	/** default constructor */
	public DocumentType() {
	}
			
	/** minimal constructor */
	public DocumentType(String officeCode, Short documentTypeId, String documentType) {
		this.officeCode = officeCode;
		this.documentTypeId = documentTypeId;
		this.documentType = documentType;
	}

	/** full constructor */
	public DocumentType(String officeCode, Short documentTypeId, String documentType, String createdBy, String createdIp,
					Date createdDate, Short displayOrder,Boolean recordStatus) {
		this.officeCode = officeCode;
		this.documentTypeId = documentTypeId;
		this.documentType = documentType;
		this.displayOrder = displayOrder;
		this.recordStatus  = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	// Property accessors
	
	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public Short getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Short documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	public Short getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	
	@Override
	  public String toString()
	  {
	    return documentType;
	  }

}
