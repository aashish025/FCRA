package models.reports;

public class DonorListReport {
	private String  sino;
	private String	address;
	private String	donorName;
	
	public DonorListReport(String donorName,String address,
			 String sino) {
			super();
			
			this.donorName = donorName;
			this.address = address;
			this.sino = sino;
		}

	public String getSino() {
		return sino;
	}

	public void setSino(String sino) {
		this.sino = sino;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	
}
