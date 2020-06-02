package models.reports;

public class DistrictDonorReceipt {
		private String donorName;
		private String countryName;
		private String amount;
		private String district;
		private String state;
		private String assoNumber;	
		private String blockYear;
		private String assoName;
		private int  sino;
		
		
		
		public DistrictDonorReceipt(String blockYear,String donorName, String countryName,
				String amount) {			
			this.donorName = donorName;
			this.countryName = countryName;
			this.amount = amount;
			this.blockYear=blockYear;
		}
		
		
		public DistrictDonorReceipt(String blockYear,String state,String district,
				 String assoNumber,String amount) {			
			this.amount = amount;
			this.district = district;
			this.state = state;
			this.assoNumber = assoNumber;
			this.blockYear=blockYear;
		}

		

		public DistrictDonorReceipt(String blockYear, String state,String district, String assoNumber, String assoName,String amount) {			
			this.amount = amount;
			this.district = district;
			this.state = state;
			this.assoNumber = assoNumber;
			this.blockYear = blockYear;
			this.assoName = assoName;
		}
		public DistrictDonorReceipt(String blockYear, String countryName,String amount) {			
			this.blockYear = blockYear;
			this.countryName = countryName;
			this.amount = amount;
			
		}
		public int getSino() {
			return sino;
		}
		public void setSino(int sino) {
			this.sino = sino;
		}
		public String getDonorName() {
			return donorName;
		}
		public void setDonorName(String donorName) {
			this.donorName = donorName;
		}
		public String getCountryName() {
			return countryName;
		}
		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getAssoNumber() {
			return assoNumber;
		}
		public void setAssoNumber(String assoNumber) {
			this.assoNumber = assoNumber;
		}


		public String getBlockYear() {
			return blockYear;
		}


		public void setBlockYear(String blockYear) {
			this.blockYear = blockYear;
		}


		public String getAssoName() {
			return assoName;
		}


		public void setAssoName(String assoName) {
			this.assoName = assoName;
		}
		
}
