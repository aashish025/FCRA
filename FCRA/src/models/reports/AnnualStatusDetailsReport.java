package models.reports;

public class AnnualStatusDetailsReport {
	private String valueYear;
	private String rcn;
	private String assoName;
	private String stateName;
	private String districtName;
	private String regOn;
	private String lastRenewed;
	private String expiryOn;
	private String year1;
	private String year2;
	private String year3;
	private String year4;
	private String year5;
	private String year6;

	
	public AnnualStatusDetailsReport(String valueYear)
	{
		this.valueYear=valueYear;
	}
	public AnnualStatusDetailsReport(String rcn ,String assoName, String stateName, String districtName, String  regOn, String lastRenewed, String expiryOn,
			String year1,String year2,String year3, String year4, String year5, String year6){
		this.rcn=rcn;
		this.assoName=assoName;
		this.stateName=stateName;
		this.districtName=districtName;
		this.regOn=regOn;
		this.lastRenewed=lastRenewed;
		this.expiryOn=expiryOn;
		this.year1=year1;
		this.year2=year2;
		this.year3=year3;
		this.year4=year4;
		this.year5=year5;
		this.year6=year6;
		
	}

	public String getValueYear() {
		return valueYear;
	}

	public void setValueYear(String valueYear) {
		this.valueYear = valueYear;
	}
	public String getRcn() {
		return rcn;
	}
	public void setRcn(String rcn) {
		this.rcn = rcn;
	}
	public String getAssoName() {
		return assoName;
	}
	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getRegOn() {
		return regOn;
	}
	public void setRegOn(String regOn) {
		this.regOn = regOn;
	}
	public String getLastRenewed() {
		return lastRenewed;
	}
	public void setLastRenewed(String lastRenewed) {
		this.lastRenewed = lastRenewed;
	}
	public String getExpiryOn() {
		return expiryOn;
	}
	public void setExpiryOn(String expiryOn) {
		this.expiryOn = expiryOn;
	}
	public String getYear1() {
		return year1;
	}
	public void setYear1(String year1) {
		this.year1 = year1;
	}
	public String getYear2() {
		return year2;
	}
	public void setYear2(String year2) {
		this.year2 = year2;
	}
	public String getYear3() {
		return year3;
	}
	public void setYear3(String year3) {
		this.year3 = year3;
	}
	public String getYear4() {
		return year4;
	}
	public void setYear4(String year4) {
		this.year4 = year4;
	}
	public String getYear5() {
		return year5;
	}
	public void setYear5(String year5) {
		this.year5 = year5;
	}
	public String getYear6() {
		return year6;
	}
	public void setYear6(String year6) {
		this.year6 = year6;
	}
	


}
