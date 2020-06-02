package models.services;

public class BankDetails {
	/** class variables */
	private String accountNumber;
	private String bankId;
	private String bankName;
	private String address;
	private String town;
	private String state;
	private String stateDesc;
	private String district;
	private String districtDesc;
	private String pincode;
	private String ifscCode;
	private String changedBankAcctDate;
	private String changedBankMailId;
	
	public String getChangedBankMailId() {
		return changedBankMailId;
	}


	public void setChangedBankMailId(String changedBankMailId) {
		this.changedBankMailId = changedBankMailId;
	}


	/** default constructor */
	public BankDetails() {		
	}
	

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankId() {
		return bankId;
	}


	public void setBankId(String bankId) {
		this.bankId = bankId;
	}


	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}


	public String getDistrictDesc() {
		return districtDesc;
	}


	public void setDistrictDesc(String districtDesc) {
		this.districtDesc = districtDesc;
	}


	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}


	public String getIfscCode() {
		return ifscCode;
	}


	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}


	public String getChangedBankAcctDate() {
		return changedBankAcctDate;
	}


	public void setChangedBankAcctDate(String changedBankAcctDate) {
		this.changedBankAcctDate = changedBankAcctDate;
	}

}