package models.reports;

public class DonorDetail {

	String dcode;
	String donorName;
	String country;
	String email;
	String donorAddress;
	String phone;
	String website;
	
	
	
	public DonorDetail(String dcode,String donorName, String donorAddress, String email, String phone,String website,  String country) {
		this.dcode=dcode;
		this.donorName=donorName;
		this.donorAddress=donorAddress;
		this.email=email;
		this.phone=phone;
		this.website=website;
		this.country=country;
		
	}

	
	public String getDonorName() {
		return donorName;
	}
	
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}


	

	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getEmail() {
		return email;
	}


	public String getDonorAddress() {
		return donorAddress;
	}


	public String getPhone() {
		return phone;
	}


	public String getWebsite() {
		return website;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setDonorAddress(String donorAddress) {
		this.donorAddress = donorAddress;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public void setWebsite(String website) {
		this.website = website;
	}
	
	

}
