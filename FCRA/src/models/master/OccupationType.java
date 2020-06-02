package models.master;

import java.sql.Timestamp;

public class OccupationType {
	private Integer occupationCode;
	private String occupationName;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private Boolean recordStatus;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private Timestamp lastModifiedDate;
	private String rowIdentifier;
	
	public Integer getOccupationCode() {
		return occupationCode;
	}
	public void setOccupationCode(Integer occupationCode) {
		this.occupationCode = occupationCode;
	}
	public String getOccupationName() {
		return occupationName;
	}
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
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
	public Boolean getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
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
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}

}