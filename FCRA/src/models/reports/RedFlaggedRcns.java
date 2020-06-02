package models.reports;

public class RedFlaggedRcns {

// Fields
		private String rcn;
		private String associationName;
		private String state;
		private String district;
		private String redFlagCategory;
		private String redflaggedOn;
		private String redflaggedBy;
		private String cateogryType;
		private String remarks;
		private String status;
	    private String annualyear;
	    private String registered_on;
		private String lastrenewedOn;
		private String expiryOn;
		private String startYear;
		private String endYear;
		private String tobeUploaded;
		public RedFlaggedRcns(String rcn, String associationName, String state,
				String district, String registered_on, String lastrenewedOn,
				String expiryOn, String startYear, String endYear,
				String tobeUploaded, String uploaded) {
			
			this.rcn = rcn;
			this.associationName = associationName;
			this.state = state;
			this.district = district;
			this.registered_on = registered_on;
			this.lastrenewedOn = lastrenewedOn;
			this.expiryOn = expiryOn;
			this.startYear = startYear;
			this.endYear = endYear;
			this.tobeUploaded = tobeUploaded;
			this.uploaded = uploaded;
		}

		private String  uploaded;
	

		// Constructors

		/** default constructor */
		public RedFlaggedRcns() {
		}

		/** full constructor */
		public RedFlaggedRcns(String rcn,String associationName, String state, String district,String redflaggedOn, String redflaggedBy,String redFlagCategory,String cateogryType, String remarks) {
			this.rcn = rcn;
			this.associationName=associationName;
			this.state = state;
			this.district=district;
			this.redFlagCategory = redFlagCategory;
			this.redflaggedOn = redflaggedOn;
			this.redflaggedBy = redflaggedBy;
			this.cateogryType=cateogryType;
			this.remarks=remarks;
		}
		/** full constructor */
		public RedFlaggedRcns(String rcn,String associationName, String redFlagCategory, String redflaggedOn,String redflaggedBy) {
			this.rcn = rcn;
			this.associationName=associationName;
			this.redFlagCategory = redFlagCategory;
			this.redflaggedOn = redflaggedOn;
			this.redflaggedBy = redflaggedBy;
		}
		public RedFlaggedRcns(String annualyear, String status){
			this.annualyear=annualyear;
			this.status=status;
		}
		public String getRcn() {
			return rcn;
		}

		

		public String getAssociationName() {
			return associationName;
		}

		public void setAssociationName(String associationName) {
			this.associationName = associationName;
		}

		public String getState() {
			return state;
		}

		public String getDistrict() {
			return district;
		}

		public String getRedFlagCategory() {
			return redFlagCategory;
		}

		public String getRedflaggedOn() {
			return redflaggedOn;
		}

		public String getRedflaggedBy() {
			return redflaggedBy;
		}

		public void setRcn(String rcn) {
			this.rcn = rcn;
		}

	

		public void setState(String state) {
			this.state = state;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public void setRedFlagCategory(String redFlagCategory) {
			this.redFlagCategory = redFlagCategory;
		}

		public void setRedflaggedOn(String redflaggedOn) {
			this.redflaggedOn = redflaggedOn;
		}

		public void setRedflaggedBy(String redflaggedBy) {
			this.redflaggedBy = redflaggedBy;
		}

		public String getCateogryType() {
			return cateogryType;
		}

		public void setCateogryType(String cateogryType) {
			this.cateogryType = cateogryType;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getAnnualyear() {
			return annualyear;
		}

		public void setAnnualyear(String annualyear) {
			this.annualyear = annualyear;
		}

		public String getRegistered_on() {
			return registered_on;
		}

		public void setRegistered_on(String registered_on) {
			this.registered_on = registered_on;
		}

		public String getLastrenewedOn() {
			return lastrenewedOn;
		}

		public void setLastrenewedOn(String lastrenewedOn) {
			this.lastrenewedOn = lastrenewedOn;
		}

		public String getExpiryOn() {
			return expiryOn;
		}

		public void setExpiryOn(String expiryOn) {
			this.expiryOn = expiryOn;
		}

		public String getStartYear() {
			return startYear;
		}

		public void setStartYear(String startYear) {
			this.startYear = startYear;
		}

		public String getEndYear() {
			return endYear;
		}

		public void setEndYear(String endYear) {
			this.endYear = endYear;
		}

		public String getTobeUploaded() {
			return tobeUploaded;
		}

		public void setTobeUploaded(String tobeUploaded) {
			this.tobeUploaded = tobeUploaded;
		}

		public String getUploaded() {
			return uploaded;
		}

		public void setUploaded(String uploaded) {
			this.uploaded = uploaded;
		}

		

		
		


}
