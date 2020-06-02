package models.services;

public class RedFlagDonors {
        private Integer donorId;
	    private String donorName;
	   private String clickdonorId;
	   private String rbiCircularIssueDate;
	    private String donorCountry;
	    private String donorCountryName;
	    private String originatorOffice;
	    private String originatorOrderNo ;
	    private String originatorOrderDate;
	    private String categoryCode;
	    private String categoryDesc;
	    private String remarks;
	    private String categoryType;
	    private String flagValue;
	    private String statusDate;
	    private String actionBy;
	    private String myofficeCode;
	    

	public RedFlagDonors(){
		}
	 public  RedFlagDonors(String donorName,String rbiCircularIssueDate,String donorCountry,String originatorOffice,String  originatorOrderDate,String originatorOrderNo,String categoryCode,
			 String remarks, String flagValue ,String myofficeCode){
		    this.donorName = donorName;
		    this.rbiCircularIssueDate=rbiCircularIssueDate;
			this.donorCountry = donorCountry;
			this.originatorOffice = originatorOffice;
			this.originatorOrderNo = originatorOrderNo;
			this.originatorOrderDate = originatorOrderDate;
		    this.categoryCode = categoryCode;
			this.remarks = remarks;
			this.flagValue=flagValue;
			this.myofficeCode=myofficeCode;
	 }
	 public  RedFlagDonors(String clickdonorId,String rbiCircularIssueDate,String originatorOffice,String  originatorOrderDate,String originatorOrderNo,String categoryCode,
			 String remarks, String flagValue ,String myOfficeCode){
		    this.clickdonorId=clickdonorId;
		    this.rbiCircularIssueDate=rbiCircularIssueDate;
			this.originatorOffice = originatorOffice;
			this.originatorOrderNo = originatorOrderNo;
			this.originatorOrderDate = originatorOrderDate;
		    this.categoryCode = categoryCode;
			this.remarks = remarks;
			this.flagValue=flagValue;
			this.myofficeCode=myOfficeCode;
	 }


	 public RedFlagDonors(int donorId,String donorName,
				String donorCountry,String donorCountryName,
				String originatorOffice,String originatorOrderDate, String originatorOrderNo,
				 String categoryCode,
				 String remarks,String rbiCircularIssueDate,String categoryDesc,String categoryType,String statusDate, String actionBy) {
			super();
			this.donorId=donorId;
			this.donorName = donorName;
			this.donorCountry = donorCountry;
			this.donorCountryName=donorCountryName;
			this.originatorOffice = originatorOffice;
			this.originatorOrderNo = originatorOrderNo;
			this.originatorOrderDate = originatorOrderDate;
			this.categoryCode = categoryCode;
			this.remarks = remarks;
			this.rbiCircularIssueDate=rbiCircularIssueDate;
			this.categoryDesc=categoryDesc;
			this.categoryType=categoryType;
			this.statusDate=statusDate;
			this.actionBy=actionBy;
		}
	 
	
	   public Integer getDonorId() {
		return donorId;
	}
	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}
	public String getDonorName() {
		return donorName;
	}
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	public String getDonorCountry() {
			return donorCountry;
		}
		public void setDonorCountry(String donorCountry) {
			this.donorCountry = donorCountry;
		}
		public String getDonorCountryName() {
			return donorCountryName;
		}
		public void setDonorCountryName(String donorCountryName) {
			this.donorCountryName = donorCountryName;
		}
		public String getOriginatorOffice() {
			return originatorOffice;
		}
		public void setOriginatorOffice(String originatorOffice) {
			this.originatorOffice = originatorOffice;
		}
		public String getOriginatorOrderNo() {
			return originatorOrderNo;
		}
		public void setOriginatorOrderNo(String originatorOrderNo) {
			this.originatorOrderNo = originatorOrderNo;
		}
		public String getOriginatorOrderDate() {
			return originatorOrderDate;
		}
		public void setOriginatorOrderDate(String originatorOrderDate) {
			this.originatorOrderDate = originatorOrderDate;
		}
		public String getCategoryCode() {
			return categoryCode;
		}
		public void setCategoryCode(String categoryCode) {
			this.categoryCode = categoryCode;
		}
		public String getCategoryDesc() {
			return categoryDesc;
		}
		public void setCategoryDesc(String categoryDesc) {
			this.categoryDesc = categoryDesc;
		}
		public String getRemarks() {
			return remarks;
		}
		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		public String getRbiCircularIssueDate() {
			return rbiCircularIssueDate;
		}
		public void setRbiCircularIssueDate(String rbiCircularIssueDate) {
			this.rbiCircularIssueDate = rbiCircularIssueDate;
		}

	    public String getCategoryType() {
				return categoryType;
			}
			public void setCategoryType(String categoryType) {
				this.categoryType = categoryType;
			}
			public String getFlagValue() {
				return flagValue;
			}
			public void setFlagValue(String flagValue) {
				this.flagValue = flagValue;
			}
			public String getClickdonorId() {
				return clickdonorId;
			}
			public void setClickdonorId(String clickdonorId) {
				this.clickdonorId = clickdonorId;
			}
			public String getStatusDate() {
				return statusDate;
			}
			public void setStatusDate(String statusDate) {
				this.statusDate = statusDate;
			}
			public String getActionBy() {
				return actionBy;
			}
			public void setActionBy(String actionBy) {
				this.actionBy = actionBy;
			}
			public String getMyofficeCode() {
				return myofficeCode;
			}
			public void setMyofficeCode(String myofficeCode) {
				this.myofficeCode = myofficeCode;
			} 
	    
}
