package service.masters;

import java.sql.SQLException;
import java.util.List;

import models.master.Office;

import org.owasp.esapi.ESAPI;

import utilities.notifications.Notification;
import dao.master.CountryTypeDao;
import dao.master.CurrencyDao;
import dao.master.OfficeDao;
import dao.master.OfficeTypeDao;
import dao.master.TimeZoneTypeDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List13;
import utilities.lists.List3;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class OfficeServices extends Commons {
	
	private static final String recordStatus = null;
	List<KVPair<String, String>> officeDetailList;
	List<KVPair<String, String>> officeTypeList;
	List<KVPair<String, String>> countryTypeList;
	List<KVPair<String, String>> currencyTypeList;
	List<KVPair<String, String>> timezoneTypeList;
	List<Office> officeList;
	private List<List13> requestedDetails;
  


	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
    private Integer officeId;
	public String officeCode;
    private String officeName;
    public String officeCity;
    private String officeState;
	public String officeCurrencuy;
    private String countryName;
    private Integer officeZipCode;
    public String address;
    private String contactNo;
    private String officeTimeZone;
 	public String officeSigntory;
    public String emailId;
    private String rowId;
    private Integer CreatedOn;
	
	
	 public String execute() {
		begin();
		try {
				initOfficeTypeList();
				initCountryTypeList();
				initCurrencyTypeList();
				//initTmeZoneTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		
		return "success";
	}
	    /**
	     * Method For Pull Currency Record From Database In Key Value Format.
	     * 
	     */	 
	private void initCurrencyTypeList() throws Exception {
		CurrencyDao cdao=new CurrencyDao(connection);
		currencyTypeList=cdao.getKVList();
		
	}
	  /**
     * Method For Pull Country Record From Database In Key Value Format.
     * 
     */	
	private void initCountryTypeList() throws Exception {
		CountryTypeDao ctdao=new CountryTypeDao(connection);
		countryTypeList=ctdao.getKVList();
		
	}
	  /**
     * Method For Pull Office Type Record From Database In Key Value Format.
     * 
     */	
	private void initOfficeTypeList() throws Exception {
		OfficeTypeDao otdao=new OfficeTypeDao(connection);
		officeTypeList=otdao.getKVList();
	}
//	private void initTmeZoneTypeList() throws Exception {
//		TimeZoneTypeDao otdao=new TimeZoneTypeDao(connection);
//		timezoneTypeList=otdao.getKVList();
//	}
	public String initializeAcquisitionList() {
		begin();
		try {
				populateAcquisitionList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateAcquisitionList() throws Exception{
		OfficeDao adao=new OfficeDao(connection);
		adao.setPageNum(pageNum);
		adao.setRecordsPerPage(recordsPerPage);
		adao.setSortColumn(sortColumn);
		adao.setSortOrder(sortOrder);	
		officeList=adao.getMasterOffice();
		totalRecords=adao.getTotalRecords();
	}
	
	public String addOffice() {
		begin();
		try {
			addOfficeDetail();
		} catch(Exception e){
			try {
				connection.rollback();
		} catch (SQLException e1) {				
			e1.printStackTrace();
		}
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public Boolean validateOffice() throws Exception{		
		if(ESAPI.validator().isValidInput("Office", officeCode, "Word", 5, false) == false){
			notifyList.add(new Notification("Error!!", "Office Code - Only Aplphabets and numbers allowed (5 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public void addOfficeDetail() throws Exception{
		OfficeDao rdao=new OfficeDao(connection);
		Office office=new Office();
		if(validateOffice()==true){
			office.setOfficeId(officeId);
			office.setOfficeCode(officeCode.toUpperCase());
			office.setOfficeName(officeName);
			office.setCityName(officeCity);
			office.setStateName(officeState);
			office.setOfficeCurrency(" ");
			office.setEmailId(emailId);
			office.setContactNo(contactNo);
			office.setZipcode(officeZipCode);
			office.setCountryName(countryName);
			office.setTimeZoneId(officeTimeZone);
			office.setAddress(address);
			office.setSignatory(officeSigntory);
			office.setCreatedIp(myIpAddress);
			office.setCreatedBy(myUserId);
			office.setLastModifiedBy(myUserId);
			office.setLastModifiedIp(myIpAddress);
				int status=rdao.insertRecord(office);
				if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "Office Details is inserted successfully.", Status.SUCCESS, Type.BAR));
				else if(status==-1)
					notifyList.add(new utilities.notifications.Notification("Info!!", "Office Code Exist!!!", Status.WARNING, Type.BAR));
				else if(status==-2)
					notifyList.add(new utilities.notifications.Notification("Info!!", "Invalid Office Type!!!", Status.WARNING, Type.BAR));
		}
		
	}
	
	public String editoffice () {
		begin();
		try {
				editofficeDetail();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editofficeDetail() throws Exception {
		OfficeDao rdao=new OfficeDao(connection);
		Office office=new Office();
		if(validateOffice()==true){
			office.setOfficeId(officeId);
			office.setOfficeCode(officeCode);
			office.setOfficeName(officeName);
			office.setCityName(officeCity);
			office.setStateName(officeState);
			office.setOfficeCurrency(officeCurrencuy);
			office.setEmailId(emailId);
			office.setContactNo(contactNo);
			office.setZipcode(officeZipCode);
			office.setCountryName(countryName);
			office.setTimeZoneId(officeTimeZone);
			office.setAddress(address);
			office.setSignatory(officeSigntory);
			office.setCreatedIp(myIpAddress);
			office.setCreatedBy(myUserId);
			office.setLastModifiedBy(myUserId);
			office.setLastModifiedIp(myIpAddress);
		int status=	rdao.editRecord(office);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Office Details is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}


public String initDeleteOffice() {
	begin();
	try {
			deleteoffice();
	} catch(Exception e){
		try {
			connection.rollback();
	} catch (SQLException e1) {				
		e1.printStackTrace();
	}
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
}

private void deleteoffice() throws Exception {
	OfficeDao rdao=new OfficeDao(connection);
	Office office=new Office();
	office.setOfficeCode(officeCode);
	int status=rdao.removeRecord(office);
	if(status>0)
		notifyList.add(new utilities.notifications.Notification("Success!!", "Office Details is Deleted successfully.", Status.SUCCESS, Type.BAR));
}

public List<KVPair<String, String>> getOfficeDetailList() {
	return officeDetailList;
}

public void setOfficeDetailList(List<KVPair<String, String>> officeDetailList) {
	this.officeDetailList = officeDetailList;
}

public List<KVPair<String, String>> getOfficeTypeList() {
	return officeTypeList;
}

public void setOfficeTypeList(List<KVPair<String, String>> officeTypeList) {
	this.officeTypeList = officeTypeList;
}

public List<KVPair<String, String>> getCountryTypeList() {
	return countryTypeList;
}

public void setCountryTypeList(List<KVPair<String, String>> countryTypeList) {
	this.countryTypeList = countryTypeList;
}

public List<KVPair<String, String>> getCurrencyTypeList() {
	return currencyTypeList;
}

public void setCurrencyTypeList(List<KVPair<String, String>> currencyTypeList) {
	this.currencyTypeList = currencyTypeList;
}

public List<KVPair<String, String>> getTimezoneTypeList() {
	return timezoneTypeList;
}

public void setTimezoneTypeList(List<KVPair<String, String>> timezoneTypeList) {
	this.timezoneTypeList = timezoneTypeList;
}

public List<Office> getOfficeList() {
	return officeList;
}

public void setOfficeList(List<Office> officeList) {
	this.officeList = officeList;
}

public List<List13> getRequestedDetails() {
	return requestedDetails;
}

public void setRequestedDetails(List<List13> requestedDetails) {
	this.requestedDetails = requestedDetails;
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

public Integer getOfficeId() {
	return officeId;
}

public void setOfficeId(Integer officeId) {
	this.officeId = officeId;
}

public String getOfficeCode() {
	return officeCode;
}

public void setOfficeCode(String officeCode) {
	this.officeCode = officeCode;
}

public String getOfficeName() {
	return officeName;
}

public void setOfficeName(String officeName) {
	this.officeName = officeName;
}

public String getOfficeCity() {
	return officeCity;
}

public void setOfficeCity(String officeCity) {
	this.officeCity = officeCity;
}

public String getOfficeState() {
	return officeState;
}

public void setOfficeState(String officeState) {
	this.officeState = officeState;
}

public String getOfficeCurrencuy() {
	return officeCurrencuy;
}

public void setOfficeCurrencuy(String officeCurrencuy) {
	this.officeCurrencuy = officeCurrencuy;
}


public String getCountryName() {
	return countryName;
}

public void setCountryName(String countryName) {
	this.countryName = countryName;
}

public Integer getOfficeZipCode() {
	return officeZipCode;
}

public void setOfficeZipCode(Integer officeZipCode) {
	this.officeZipCode = officeZipCode;
}

public String getAddress() {
	return address;
}

public void setAddress(String address) {
	this.address = address;
}

public String getContactNo() {
	return contactNo;
}

public void setContactNo(String contactNo) {
	this.contactNo = contactNo;
}

public String getOfficeTimeZone() {
	return officeTimeZone;
}

public void setOfficeTimeZone(String officeTimeZone) {
	this.officeTimeZone = officeTimeZone;
}

public String getOfficeSigntory() {
	return officeSigntory;
}

public void setOfficeSigntory(String officeSigntory) {
	this.officeSigntory = officeSigntory;
}

public String getEmailId() {
	return emailId;
}

public void setEmailId(String emailId) {
	this.emailId = emailId;
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

public String initForEditOfficeDetail() {

	begin();
	try {
			officeDetailList();
	} catch(Exception e){
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
	// TODO Auto-generated method stub
	
}

private void officeDetailList() throws Exception {
   OfficeDao odao=new OfficeDao(connection);
   Office office=new Office();
   office.setOfficeCode(officeCode);
   requestedDetails=odao.getEditOfficeDetail(office);
   
	
}
}



