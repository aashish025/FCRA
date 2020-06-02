package models.master;

public class ApplicationSubStage {
	private String substageId;
	private String substageDesc;
	private Short displayOrder;
	private String recordStatus;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String lastModifiedIp;
	private Integer lastModifiedDate;
	private String lastModifiedBy;
    private String parentstageId;
    private String stageId;
	private String stageDesc;
	
	
	

	public ApplicationSubStage()
	{
		
	}
	
	public ApplicationSubStage(String substageId, String substageDesc, String createdDate ) {

		this.substageId=substageId;
		this.substageDesc = substageDesc;
		this.createdDate=createdDate;
	}
	public ApplicationSubStage(String substageId, String substageDesc,String parentstageId, String createdDate) {

		this.substageId=substageId;
		this.substageDesc = substageDesc;
		this.parentstageId=parentstageId;
		this.createdDate=createdDate;
		
	}

	public ApplicationSubStage(String substageId, String substageDesc,String recordStatus, String createdBy, String createdIp, String createdDate) {
		this.substageId = substageId;
		this.substageDesc = substageDesc;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	public String getSubstageId() {
		return substageId;
	}

	public void setSubstageId(String substageId) {
		this.substageId = substageId;
	}

	public String getSubstageDesc() {
		return substageDesc;
	}

	public void setSubstageDesc(String substageDesc) {
		this.substageDesc = substageDesc;
	}

	public Short getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
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

	public String getStageId() {
		return stageId;
	}

	public void setStageId(String stageId) {
		this.stageId = stageId;
	}

	public String getStageDesc() {
		return stageDesc;
	}

	public void setStageDesc(String stageDesc) {
		this.stageDesc = stageDesc;
	}

	public String getParentstageId() {
		return parentstageId;
	}

	public void setParentstageId(String parentstageId) {
		this.parentstageId = parentstageId;
	}

}
