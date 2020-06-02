package models.reports;


public class OldRegistrationEntryDtl {

	private String rcn;
	private String assoName;
	private String assoAddress;
	private String assoTownCity;
	private String assoState;
	private String assoDistrict;
	private String assoPin;
	private String assoNature;
	private String assoReligion;
	private String regDate;
	private String rcatgry;
	private String status;
	private String assoNewold;
	private String userId;
	private String cancelStatus;
	private String validFrom;
	private String validTo;
	private String assoAims;
	private String currentStatus;
	private String newOld;
	private String bankName;
	private String bankAddress;
	private String bankState;
	private String bankDistrict;
	private String bankTownCity;
	private String bankZipCode;	
	private String accountNumber;
	private String bankStatus;
	private String oldregRemark;
	private String myUserId;

	
	
	public OldRegistrationEntryDtl() {
		// TODO Auto-generated constructor stub
	}


	/* constructor for registration details
	 * 
	 */
	

	public OldRegistrationEntryDtl(String assoName, String assoAddress,
			String assoTownCity, String assoState, String assoDistrict,
			String assoPin, String assoNature, String assoReligion,
			String regDate, String rcatgry, String status, String assoNewold,
			String userId, String cancelStatus, String validFrom, String validTo,
			String assoAims, String currentStatus) {
		super();
		this.assoName = assoName;
		this.assoAddress = assoAddress;
		this.assoTownCity = assoTownCity;
		this.assoState = assoState;
		this.assoDistrict = assoDistrict;
		this.assoPin = assoPin;
		this.assoNature = assoNature;
		this.assoReligion = assoReligion;
		this.regDate = regDate;
		this.rcatgry = rcatgry;
		this.status = status;
		this.assoNewold = assoNewold;
		this.userId = userId;
		this.cancelStatus = cancelStatus;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.assoAims = assoAims;
		this.currentStatus = currentStatus;
	}

	
	
	
	/*constructor for bank details	 
	 */
	public OldRegistrationEntryDtl(String rcn, String newOld, String bankName,
			String bankAddress, String bankTownCity, String bankZipCode,
			String bankState, String bankDistrict,
			String accountNumber, String bankStatus) {
		super();
		this.rcn = rcn;
		this.newOld = newOld;
		this.bankName = bankName;
		this.bankAddress = bankAddress;
		this.bankTownCity = bankTownCity;
		this.bankZipCode = bankZipCode;
		this.bankState = bankState;
		this.bankDistrict = bankDistrict;
		this.accountNumber = accountNumber;
		this.bankStatus = bankStatus;
	}

	

	/** constructor for registration status history ***/
	

	public OldRegistrationEntryDtl(String oldregRemark, String myUserId) {
		super();
		this.oldregRemark = oldregRemark;
		this.myUserId = myUserId;
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



	public String getAssoAddress() {
		return assoAddress;
	}



	public void setAssoAddress(String assoAddress) {
		this.assoAddress = assoAddress;
	}



	public String getAssoTownCity() {
		return assoTownCity;
	}



	public void setAssoTownCity(String assoTownCity) {
		this.assoTownCity = assoTownCity;
	}



	public String getAssoState() {
		return assoState;
	}



	public void setAssoState(String assoState) {
		this.assoState = assoState;
	}



	public String getAssoDistrict() {
		return assoDistrict;
	}



	public void setAssoDistrict(String assoDistrict) {
		this.assoDistrict = assoDistrict;
	}



	public String getAssoPin() {
		return assoPin;
	}



	public void setAssoPin(String assoPin) {
		this.assoPin = assoPin;
	}



	public String getAssoNature() {
		return assoNature;
	}



	public void setAssoNature(String assoNature) {
		this.assoNature = assoNature;
	}



	public String getAssoReligion() {
		return assoReligion;
	}



	public void setAssoReligion(String assoReligion) {
		this.assoReligion = assoReligion;
	}



	public String getRegDate() {
		return regDate;
	}



	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}



	public String getRcatgry() {
		return rcatgry;
	}



	public void setRcatgry(String rcatgry) {
		this.rcatgry = rcatgry;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getAssoNewold() {
		return assoNewold;
	}



	public void setAssoNewold(String assoNewold) {
		this.assoNewold = assoNewold;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getCancelStatus() {
		return cancelStatus;
	}



	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}



	public String getValidFrom() {
		return validFrom;
	}



	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}



	public String getValidTo() {
		return validTo;
	}



	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}



	public String getAssoAims() {
		return assoAims;
	}



	public void setAssoAims(String assoAims) {
		this.assoAims = assoAims;
	}



	public String getCurrentStatus() {
		return currentStatus;
	}



	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}



	public String getNewOld() {
		return newOld;
	}



	public void setNewOld(String newOld) {
		this.newOld = newOld;
	}



	public String getBankName() {
		return bankName;
	}



	public void setBankName(String bankName) {
		this.bankName = bankName;
	}



	public String getBankAddress() {
		return bankAddress;
	}



	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}



	public String getBankTownCity() {
		return bankTownCity;
	}



	public void setBankTownCity(String bankTownCity) {
		this.bankTownCity = bankTownCity;
	}



	public String getBankZipCode() {
		return bankZipCode;
	}


	public void setBankZipCode(String bankZipCode) {
		this.bankZipCode = bankZipCode;
	}


	public String getBankState() {
		return bankState;
	}



	public void setBankState(String bankState) {
		this.bankState = bankState;
	}



	public String getBankDistrict() {
		return bankDistrict;
	}



	public void setBankDistrict(String bankDistrict) {
		this.bankDistrict = bankDistrict;
	}




	public String getAccountNumber() {
		return accountNumber;
	}



	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}



	public String getBankStatus() {
		return bankStatus;
	}



	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}


	public String getOldregRemark() {
		return oldregRemark;
	}


	public void setOldregRemark(String oldregRemark) {
		this.oldregRemark = oldregRemark;
	}



	public String getMyUserId() {
		return myUserId;
	}


	public void setMyUserId(String myUserId) {
		this.myUserId = myUserId;
	}

	
	
	
}
