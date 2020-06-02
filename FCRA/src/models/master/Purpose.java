/*package models.master;

public class  {

}

*/
package models.master;

public class Purpose  {
	private Integer purposeCode;
	private String purposeName;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private String lastModifiedDate;
	private String rowIdentifier;
	
	/** default constructor */
	public Purpose() {
	}

	/** full constructor */
	public Purpose(Integer purposeCode, String purposeName, String createdBy, String createdIp, String createdDate,Boolean recordStatus) {
		this.purposeCode = purposeCode;
		this.purposeName = purposeName;
		this.recordStatus=recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	public Purpose(Integer purposeCode, String purposeName, String createdDate) {
			this.purposeCode=purposeCode;
			this.purposeName=purposeName;
			this.createdDate=createdDate;
	}
	
	
	public Integer getPurposeCode() {
		return purposeCode;
	}

	public void setPurposeCode(Integer purposeCode) {
		this.purposeCode = purposeCode;
	}

	public String getPurposeName() {
		return purposeName;
	}

	public void setPurposeName(String purposeName) {
		this.purposeName = purposeName;
	}

	public Boolean getRecordStatus() {
		return recordStatus;
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

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
