package models.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


public class UserStatus implements java.io.Serializable {

	// Fields

	private Integer actionId;
	private String actionName;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private String lastModifiedDate;
	private String rowIdentifier;

	// Constructors

	/** default constructor */
	public UserStatus() {
	}

	/** full constructor */
	public UserStatus(Integer actionId, String actionName,
			String createdBy, String createdIp, String createdDate,Boolean recordStatus) {
		this.actionId = actionId;
		this.actionName = actionName;
		this.recordStatus=recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	

	public UserStatus(Integer actionId, String actionName, String createdDate) {
		
			
			
			this.actionId=actionId;
			this.actionName=actionName;
			this.createdDate=createdDate;
		
		
	
		// TODO Auto-generated constructor stub
	}


	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
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
	
	@Column(name = "RECORD_STATUS", precision = 1, scale = 0)
	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	 

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
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
	
}