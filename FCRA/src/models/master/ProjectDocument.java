package models.master;

public class ProjectDocument {
	private String docId;
    private String doctypeDesc;
    private Boolean recordStatus;
	private Short displayOrder;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String rowIdentifier;
	private String lastModifiedIp;
	private Integer lastModifiedDate;
	private String lastModifiedBy;
	
	
	public ProjectDocument() {
	}

	public ProjectDocument(String docId, String doctypeDesc,String createdDate)
	{
		this.docId=docId;
		this.doctypeDesc=doctypeDesc;
		this.createdDate=createdDate;
	}
	
	public ProjectDocument(String docId, String doctypeDescs) {
		this.docId = docId;
		this.doctypeDesc = doctypeDesc;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDoctypeDesc() {
		return doctypeDesc;
	}

	public void setDoctypeDesc(String doctypeDesc) {
		this.doctypeDesc = doctypeDesc;
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

	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
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
	

}
