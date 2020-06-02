package models.reports;

public class ReportType {
	// Fields			
    private Integer reportId;
	private String reportName;
	private Integer displayOrder;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private String createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private String lastModifiedDate;
	private String rowIdentifier;
			
	/** default constructor */
	public ReportType() {
	
	}
	
	public ReportType(Integer reportId, String reportName) {
		
		this.reportId = reportId;
		this.reportName = reportName;
		
	}
	
	public ReportType(Integer reportId, String reportName, String createdDate) {
		
		this.reportId = reportId;
		this.reportName = reportName;
		this.createdDate = createdDate;
	}
	
	/** minimal constructor */
	public ReportType(Integer reportId, String reportName,
			Boolean recordStatus, String createdBy, String createdIp,
			String createdDate) {
		this.reportId = reportId;
		this.reportName = reportName;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
	}
	
	/** full constructor */
	public ReportType(Integer reportId, String reportName,
			Integer displayOrder, Boolean recordStatus, String createdBy, String createdIp,
			String createdDate) {
		this.reportId = reportId;
		this.reportName = reportName;
		this.displayOrder = displayOrder;
		this.recordStatus = recordStatus;
		this.createdBy = createdBy;
		this.createdIp = createdIp;
		this.createdDate = createdDate;
			
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Boolean getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Boolean recordStatus) {
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


	public Integer getReportId() {
		return reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
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

	public String getRowIdentifier() {
		return rowIdentifier;
	}
	public void setRowIdentifier(String rowIdentifier) {
		this.rowIdentifier = rowIdentifier;
	}
	
	@Override
	  public String toString()
	  {
	    return reportName;
	  }
}
