package models.master;

import java.sql.Timestamp;
import java.util.Date;

public class NoticeBoard implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	private Short notificationType;
	private String notificationName;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;	
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private Timestamp lastModifiedDate;
	private String rowIdentifier;

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

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}

	// Constructors

	/** default constructor */
	public NoticeBoard() {
	}

	public NoticeBoard(Short notificationType, String notificationName) {
		this.notificationType = notificationType;
		this.notificationName = notificationName;
	}

	/** full constructor */
	public NoticeBoard(Short notificationType, Boolean recordStatus,
			String notificationName, String createdBy, String createdIp,
			String createdDate, String rowIdentifier) {
		this.notificationType = notificationType;
		this.notificationName = notificationName;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
		this.rowIdentifier = rowIdentifier;
	}

	public Short getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Short notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationName() {
		return notificationName;
	}

	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedIp() {
		return this.createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}

	public String getRowIdentifier() {
		return rowIdentifier;
	}

	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}

	public String toString() {
		return notificationName;
	}
}