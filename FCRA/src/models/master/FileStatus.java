package models.master;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "T_FILESTATUS_MASTER", schema = "FRRO")

public class FileStatus {

	private Short filestatusId;
	private String filestatusDesc;
	private String filestatusRole;
	private Boolean recordStatus;
	private Short displayOrder;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private String createdDate1;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private String lastModifiedDate;
	private String rowIdentifier;
	
	public FileStatus()
	{}
	public FileStatus(Short filestatusId, String filestatusDesc) {
	
		this.filestatusId = filestatusId;
		this.filestatusDesc = filestatusDesc;
	}
	public FileStatus(Short filestatusId, String filestatusDesc, String filestatusRole,
			Boolean recordStatus, Short displayOrder, String createdBy,
			String createdIp, Date createdDate) {
	
		this.filestatusId = filestatusId;
		this.filestatusDesc = filestatusDesc;
		this.filestatusRole = filestatusRole;
		this.recordStatus = recordStatus;
		this.displayOrder = displayOrder;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		
	}
	
	public FileStatus(short int1, String string, String string2) {
		this.filestatusId =  int1;
		this.filestatusDesc = string;
		this.createdDate1 = string2;
	}
	@Id
	@Column(name = "FILESTATUS_ID", unique = true, nullable = false, precision = 3, scale = 0)
	public Short getFilestatusId() {
		return filestatusId;
	}

	public void setFilestatusId(Short filestatusId) {
		this.filestatusId = filestatusId;
	}
	
	@Column(name = "FILESTATUS_DESC", nullable = false, length = 50)
	public String getFilestatusDesc() {
		return filestatusDesc;
	}

	public void setFilestatusDesc(String filestatusDesc) {
		this.filestatusDesc = filestatusDesc;
	}

	@Column(name = "FILESTATUS_ROLE", nullable = false, length = 100)
	public String getFilestatusRole() {
		return filestatusRole;
	}

	public void setFilestatusRole(String filestatusRole) {
		this.filestatusRole = filestatusRole;
	}

	@Column(name = "RECORD_STATUS", precision = 3, scale = 0)
	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}
	@Column(name = "DISPLAY_ORDER", precision = 3, scale = 0)
	public Short getDisplayOrder() {
		return displayOrder;
	}
    
	
	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}
	@Column(name = "CREATED_BY", nullable = false, length = 10)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "CREATED_IP", nullable = false, length = 30)
	public String getCreatedIp() {
		return createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", nullable = false, length = 7)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
    
	@Column(name = "ROWID", insertable = false, updatable = false)	
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
	public String getCreatedDate1() {
		return createdDate1;
	}
	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}
	
	
	
}

	
	

