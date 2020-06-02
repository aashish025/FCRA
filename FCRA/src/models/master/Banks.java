package models.master;

public class Banks  {

	// Fields

	private Integer bankCode;
	private String bankName;
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
	public Banks() {
	}

	/** full constructor */
	public Banks(Integer bankCode, String bankName,String createdBy, String createdIp, String createdDate,Boolean recordStatus) {
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.recordStatus=recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}

	public Banks(Integer bankCode, String bankName, String createdDate) {
			this.bankCode=bankCode;
			this.bankName=bankName;
			this.createdDate=createdDate;
	}
	
	
	public Integer getBankCode() {
		return bankCode;
	}

	public void setBankCode(Integer bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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