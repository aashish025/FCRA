package models.reports;

public class Covid19EmergencyStateWiseReport {
	private String  sino;
	private String	association;
	private String	rName;
	private String	amount;
	private String blkYear;
	private String stateName;
	//private String distName;
	//private String rCN;
	
	public Covid19EmergencyStateWiseReport(String blkYear, String rName,String association,String stateName,String amount,
		 String sino) {
		super();
		this.blkYear=blkYear;
		this.rName = rName;
		this.association = association;
		this.stateName=stateName;
		this.amount = amount;
		this.sino = sino;
	}
	


	public String getSino() {
		return sino;
	}
	public void setSino(String sino) {
		this.sino = sino;
	}
	public String getAssociation() {
		return association;
	}
	public void setAssociation(String association) {
		this.association = association;
	}
	public String getrName() {
		return rName;
	}
	public void setrName(String rName) {
		this.rName = rName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBlkYear() {
		return blkYear;
	}
	public void setBlkYear(String blkYear) {
		this.blkYear = blkYear;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	

}
