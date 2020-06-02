package models.services.requests;

public enum ServiceType {
	REGISTRATION("1"),
	PRIOR_PERMISSION("2"),
	RENEWAL("3"),
	ANNUAL_RETURNS("4"),
	TRANSFER_OF_FUNDS("5"),
	CHANGE_OF_DETAILS("6"),
	HOSPITALITY("7"),
	GIFT_CONTRIBUTION("8"),
	ARTICLE_CONTRIBUTION("9"),
	SECURITY_CONTRIBUTION("10"),
	ELECTION_CONTRIBUTION("11"),
	GRIEVANCES("12"),
	CHANGE_NAME_ADDRESS("13"),
	CHANGE_FC_RECEIPT_CUM_UTILISATION_BANK("15"),
	CHANGE_OPENING_UTILIZATION_BANK_ACCOUNT("16"),
	CHANGE_CHANGE_COMMITTEE_MEMBERS("17");
	
	private String type;
	
	private ServiceType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
