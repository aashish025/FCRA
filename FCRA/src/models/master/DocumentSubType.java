package models.master;

import java.sql.Date;

public class DocumentSubType {

	// Fields
	
    private String       officeCode;
	private DocumentType documentType;
	private Short        documentSubTypeId;
	private String       documentSubType;
	private String 		 documentCode;
    private Short        displayOrder;
	private Boolean      recordStatus;
    private String       createdBy;
	private String       createdIp;
	private Date         createdDate;
	private String       rowIdentifier;
	
	// Constructors

		/** default constructor */
		public DocumentSubType() {
		}
				
		/** minimal constructor */
		public DocumentSubType(String officeCode, DocumentType documentType, Short documentSubTypeId,
				String documentSubType, String documentCode) {
			
			this.officeCode        = officeCode;
			this.documentSubTypeId = documentSubTypeId;
			this.documentType      = documentType;
			this.documentSubType   = documentSubType;
			this.documentCode      = documentCode;
		}

		/** full constructor */
		public DocumentSubType(String officeCode, DocumentType documentType, Short documentSubTypeId,
				String documentSubType, String documentCode, String createdBy, String createdIp,
						Date createdDate, Short displayOrder,Boolean recordStatus) {
			
			this.officeCode = officeCode;
			this.documentSubTypeId = documentSubTypeId;
			this.documentType = documentType;
			this.documentSubType = documentSubType;
			this.documentCode = documentCode;
			this.displayOrder = displayOrder;
			this.recordStatus  = recordStatus;
			this.createdBy = createdBy;
			this.createdIp = createdIp;
			this.createdDate = createdDate;
		}

		public void setOfficeCode(String officeCode) {
			this.officeCode = officeCode;
		}

		public void setDocumentType(DocumentType documentType) {
			this.documentType = documentType;
		}

		public void setDocumentSubTypeId(Short documentSubTypeId) {
			this.documentSubTypeId = documentSubTypeId;
		}

		public void setDocumentSubType(String documentSubType) {
			this.documentSubType = documentSubType;
		}

		public void setDisplayOrder(Short displayOrder) {
			this.displayOrder = displayOrder;
		}

		public void setRecordStatus(Boolean recordStatus) {
			this.recordStatus = recordStatus;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public void setCreatedIp(String createdIp) {
			this.createdIp = createdIp;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public String getRowIdentifier() {
			return rowIdentifier;
		}

		public String getOfficeCode() {
			return officeCode;
		}

		public DocumentType getDocumentType() {
			return documentType;
		}

		public Short getDocumentSubTypeId() {
			return documentSubTypeId;
		}

		public String getDocumentSubType() {
			return documentSubType;
		}

		public Short getDisplayOrder() {
			return displayOrder;
		}

		public Boolean getRecordStatus() {
			return recordStatus;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public String getCreatedIp() {
			return createdIp;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setRowIdentifier(String rowIdentifier) {
			this.rowIdentifier = rowIdentifier;
		}

		public String getDocumentCode() {
			return documentCode;
		}

		public void setDocumentCode(String documentCode) {
			this.documentCode = documentCode;
		}
		
		
    
}
