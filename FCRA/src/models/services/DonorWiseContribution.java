package models.services;

import java.math.BigDecimal;

public class DonorWiseContribution {
	private DonorDetails donor;
	private String purpose;
	private String purposeDesc;
	private BigDecimal amount;
	
	public DonorDetails getDonor() {
		return donor;
	}
	public void setDonor(DonorDetails donor) {
		this.donor = donor;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getPurposeDesc() {
		return purposeDesc;
	}
	public void setPurposeDesc(String purposeDesc) {
		this.purposeDesc = purposeDesc;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
