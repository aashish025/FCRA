package models.reports;

public class SuddenRiseIncomeReport {
	private String rcn;
	private String	foreignAmt;
	private String averegeAmt;
	private String assoName;
	private String state;
	private String district;
	
	
	
	public SuddenRiseIncomeReport(String rcn, String foreignAmt,
			String averegeAmt, String assoName, String state, String district) {
		super();
		this.rcn = rcn;
		this.foreignAmt = foreignAmt;
		this.averegeAmt = averegeAmt;
		this.assoName = assoName;
		this.state = state;
		this.district = district;
	}

	public SuddenRiseIncomeReport(String rcn, String foreignAmt,
			String averegeAmt) {
		super();
		this.rcn = rcn;
		this.foreignAmt = foreignAmt;
		this.averegeAmt = averegeAmt;
	}
	
	public String getRcn() {
		return rcn;
	}
	public void setRcn(String rcn) {
		this.rcn = rcn;
	}
	public String getForeignAmt() {
		return foreignAmt;
	}
	public void setForeignAmt(String foreignAmt) {
		this.foreignAmt = foreignAmt;
	}
	public String getAveregeAmt() {
		return averegeAmt;
	}
	public void setAveregeAmt(String averegeAmt) {
		this.averegeAmt = averegeAmt;
	}

	public String getAssoName() {
		return assoName;
	}

	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	
	

}
