package models.services;

import java.math.BigDecimal;

public class HostDetails {
	/** class variables */
	private String natureOfHost;
	private String natureOfHostDesc;
	private String nameOfHost;
	private String addressOfHost;
	private String email;
	private String mobile;
	private String phoneNumber;
	private String relationshipWithHost;
	
	// Individual
	private String nationalityOfHost;
	private String nationalityOfHostDesc;
	private String nationalityOfHostOther;
	private String professionOfHost;
	private String professionOfHostDesc;
	private String professionOfHostOther;
	private String passportNumberOfHost;
	
	/** default constructor */
	public HostDetails() {		
	}

	public String getNatureOfHost() {
		return natureOfHost;
	}

	public void setNatureOfHost(String natureOfHost) {
		this.natureOfHost = natureOfHost;
	}

	public String getNatureOfHostDesc() {
		return natureOfHostDesc;
	}

	public void setNatureOfHostDesc(String natureOfHostDesc) {
		this.natureOfHostDesc = natureOfHostDesc;
	}

	public String getNameOfHost() {
		return nameOfHost;
	}

	public void setNameOfHost(String nameOfHost) {
		this.nameOfHost = nameOfHost;
	}

	public String getAddressOfHost() {
		return addressOfHost;
	}

	public void setAddressOfHost(String addressOfHost) {
		this.addressOfHost = addressOfHost;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRelationshipWithHost() {
		return relationshipWithHost;
	}

	public void setRelationshipWithHost(String relationshipWithHost) {
		this.relationshipWithHost = relationshipWithHost;
	}

	public String getNationalityOfHost() {
		return nationalityOfHost;
	}

	public void setNationalityOfHost(String nationalityOfHost) {
		this.nationalityOfHost = nationalityOfHost;
	}

	public String getNationalityOfHostDesc() {
		return nationalityOfHostDesc;
	}

	public void setNationalityOfHostDesc(String nationalityOfHostDesc) {
		this.nationalityOfHostDesc = nationalityOfHostDesc;
	}

	public String getNationalityOfHostOther() {
		return nationalityOfHostOther;
	}

	public void setNationalityOfHostOther(String nationalityOfHostOther) {
		this.nationalityOfHostOther = nationalityOfHostOther;
	}

	public String getProfessionOfHost() {
		return professionOfHost;
	}

	public void setProfessionOfHost(String professionOfHost) {
		this.professionOfHost = professionOfHost;
	}

	public String getProfessionOfHostDesc() {
		return professionOfHostDesc;
	}

	public void setProfessionOfHostDesc(String professionOfHostDesc) {
		this.professionOfHostDesc = professionOfHostDesc;
	}

	public String getProfessionOfHostOther() {
		return professionOfHostOther;
	}

	public void setProfessionOfHostOther(String professionOfHostOther) {
		this.professionOfHostOther = professionOfHostOther;
	}

	public String getPassportNumberOfHost() {
		return passportNumberOfHost;
	}

	public void setPassportNumberOfHost(String passportNumberOfHost) {
		this.passportNumberOfHost = passportNumberOfHost;
	}

}
