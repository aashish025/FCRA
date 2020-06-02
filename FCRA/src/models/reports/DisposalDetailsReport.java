package models.reports;

public class DisposalDetailsReport {
	private String year;
	private String serviceCode;
	private String serviceDesc;
	private String granted;
	private String denied;
	private String closed;
	private String month;
	private String mon;
	
	
	public DisposalDetailsReport(){
		
	}

	
	public DisposalDetailsReport(String year, String month,String mon,String serviceDesc,String serviceCode,
			String granted, String denied, String closed) {
		super();
		this.year = year;
		this.month = month;
		this.mon=mon;		
		this.serviceDesc=serviceDesc;
		this.serviceCode = serviceCode;
		this.granted = granted;
		this.denied = denied;
		this.closed = closed;		
		
	}
	
	
	public DisposalDetailsReport(String year, String serviceDesc,String serviceCode,
			String granted, String denied, String closed) {
		super();
		this.year = year;
		this.serviceDesc = serviceDesc;
		this.serviceCode = serviceCode;
		this.granted = granted;
		this.denied = denied;
		this.closed = closed;
	}


	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getGranted() {
		return granted;
	}
	public void setGranted(String granted) {
		this.granted = granted;
	}
	public String getDenied() {
		return denied;
	}
	public void setDenied(String denied) {
		this.denied = denied;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}


	public String getMon() {
		return mon;
	}


	public void setMon(String mon) {
		this.mon = mon;
	}
	
	
	
	
	

}
