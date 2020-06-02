package models.reports;

public class ReturnFiledReport {
    private String blockYear;
	private String  stateName;
	private String	totalReported;
	private String	totalNilReported;
	private String	foreignAmt;
	private String	totalAmt;	
	//Extra field for deatailed part
	private String districtName;
	private String fcraRcn;
	private String associationName; 
	private String submission_Date;
	private String website;
	/**
	 * 
	 * For Statatics report
	 * @param stateName
	 * @param totalReported
	 * @param totalNilReported
	 * @param foreignAmt
	 * @param totalAmt
	 */
public ReturnFiledReport(String blockYear,String stateName, String totalReported,
			String totalNilReported, String foreignAmt, String totalAmt) {
		super();
		this.blockYear=blockYear;
		this.stateName = stateName;
		this.totalReported = totalReported;
		this.totalNilReported = totalNilReported;
		this.foreignAmt = foreignAmt;
		this.totalAmt = totalAmt;
	}
	/**
	 * For Detailed Constructor
	 * @param stateName
	 * @param districtName
	 * @param fcraRcn
	 * @param associationName
	 * @param submission_Date
	 * @param foreignAmt
	 * @param totalAmt
	 */
	public ReturnFiledReport(String blockYear,String stateName,String districtName, String fcraRcn,
			String associationName,String submission_Date, String foreignAmt, String totalAmt, String website) {
		super();
		this.blockYear=blockYear;
		this.stateName = stateName;
		this.districtName = districtName;
		this.fcraRcn = fcraRcn;
		this.associationName = associationName;
		this.submission_Date = submission_Date;
		this.foreignAmt = foreignAmt;
		this.totalAmt = totalAmt;
		this.website=website;
	}
	
	public String getBlockYear() {
		return blockYear;
	}
	public void setBlockYear(String blockYear) {
		this.blockYear = blockYear;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getTotalReported() {
		return totalReported;
	}
	public void setTotalReported(String totalReported) {
		this.totalReported = totalReported;
	}
	public String getTotalNilReported() {
		return totalNilReported;
	}
	public void setTotalNilReported(String totalNilReported) {
		this.totalNilReported = totalNilReported;
	}
	public String getForeignAmt() {
		return foreignAmt;
	}
	public void setForeignAmt(String foreignAmt) {
		this.foreignAmt = foreignAmt;
	}
	public String getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getFcraRcn() {
		return fcraRcn;
	}
	public void setFcraRcn(String fcraRcn) {
		this.fcraRcn = fcraRcn;
	}
	public String getAssociationName() {
		return associationName;
	}
	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}
	public String getSubmission_Date() {
		return submission_Date;
	}
	public void setSubmission_Date(String submission_Date) {
		this.submission_Date = submission_Date;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	
}
