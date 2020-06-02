package service.masters;

import java.sql.SQLException;
import java.util.List;

import models.master.Country;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.CountryTypeDao;

public class CountryTypeDetailsServices extends Commons {
private static final String recordStatus = null;
	

	List<KVPair<String, String>> countryTypeList;
	List<Country> countryList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String rowId;
    private Integer CreatedOn;
    private String countryCode;
   	private String countryName;
   	private String isoCountryCode;
    
   
	
	 public String execute() {
		begin();
		try {
				initCountryTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	public Boolean validatecountrycode() throws Exception{		
			if(ESAPI.validator().isValidInput("Country Code", countryCode, "Num", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Country Code - Only number allowed (50 characters max).", Status.ERROR, Type.BAR));
				return false;
			}
			return true;
	 }
			 public Boolean validatecountrynam() throws Exception{
		if(ESAPI.validator().isValidInput("Country Name", countryName, "AlphaS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Country Name - Only alphabet allowed (50 characters max).", Status.ERROR, Type.BAR));
				return false;
			}
			return true;
		}
			 
     public void initCountryTypeList() throws Exception{
    	 CountryTypeDao bdao=new CountryTypeDao(connection);
    	 countryTypeList=bdao.getKVList();
	}
	
	public String initializeCountryList() {
		begin();
		try {
				populateCountryList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populateCountryList() throws Exception{
		CountryTypeDao bdao=new CountryTypeDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);				
		countryList=bdao.gettable();
		totalRecords=bdao.getTotalRecords();
	}
	public String AddCountry() {
		begin();
		try {
				addcountrydata();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	
	public void addcountrydata() throws Exception{
		CountryTypeDao bdao=new CountryTypeDao(connection);
		Country country=new Country();
		if(validatecountrycode()==true){
			if(validatecountrynam()==true)
			{
		country.setCountryCode(countryCode);
		country.setCountryName(countryName);
		country.setCreatedIp(myIpAddress);
	    country.setRecordStatus(recordStatus);
		country.setCreatedBy(myUserId);
		country.setLastModifiedBy(myUserId);
		country.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(country);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Country Name is Inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		}
	}
	
	public String editcountryName () {
		begin();
		try {
				editcountry();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editcountry() throws Exception {
		CountryTypeDao bdao=new CountryTypeDao(connection);
		Country country=new Country();
		if(validatecountrynam()==true)
		{
		country.setCountryCode(countryCode);
		country.setCountryName(countryName);
		country.setLastModifiedBy(myUserId);
	    country.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(country);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Country Name is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
public String initDeleteCountry() {
	begin();
	try {
			deletecountryName();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletecountryName() throws Exception {
	CountryTypeDao bdao=new CountryTypeDao(connection);
	Country country=new Country();
	country.setCountryCode(countryCode);
	country.setCountryName(countryName);
	int status=bdao.removeRecord(country);
	if(status>0)
		notifyList.add(new Notification("Success!!", "Country Name is Deleted successfully.", Status.SUCCESS, Type.BAR));
}

	public List<KVPair<String, String>> getCountryTypeList() {
		return countryTypeList;
	}
	public void setCountryTypeList(List<KVPair<String, String>> countryTypeList) {
		this.countryTypeList = countryTypeList;
	
	}
	public List<Country> getCountryList() {
		return countryList;
	}
	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}
	public String getPageNum() {
		return pageNum;
	}
	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}
	public String getRecordsPerPage() {
		return recordsPerPage;
	}
	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public Integer getCreatedOn() {
		return CreatedOn;
	}
	public void setCreatedOn(Integer createdOn) {
		CreatedOn = createdOn;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getIsoCountryCode() {
		return isoCountryCode;
	}
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}
	public static String getRecordstatus() {
		return recordStatus;
	}
	
	public void populateVisibleCountryTypeList() throws Exception { 
		begin();
		try {			
			CountryTypeDao countryTypeDao = new CountryTypeDao(connection);
			countryList = countryTypeDao
					.getVisibleAliveRecords(myOffice);
		} catch (Exception e) {
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException e2) {
				ps(e2);
			}
			ps(e);
		}
		finish();
	}

}
