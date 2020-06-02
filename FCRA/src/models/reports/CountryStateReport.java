package models.reports;

public class CountryStateReport {
	private String sino;
	private String rcn;
	private String associationName;
	private String bankDetail;
	private String amount;

	public CountryStateReport( String rcn, String amount,String associationName,String bankDetail,String sino) {
		super();
		
		
		this.rcn = rcn;
		this.amount = amount;
	    this.associationName = associationName;
		this.bankDetail=bankDetail;
		this.sino=sino;
	}

	public String getSino() {
		return sino;
	}

	public void setSino(String sino) {
		this.sino = sino;
	}

	public String getRcn() {
		return rcn;
	}

	public void setRcn(String rcn) {
		this.rcn = rcn;
	}

	public String getAssociationName() {
		return associationName;
	}

	public void setAssociationName(String associationName) {
		this.associationName = associationName;
	}

	public String getBankDetail() {
		return bankDetail;
	}

	public void setBankDetail(String bankDetail) {
		this.bankDetail = bankDetail;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	}
