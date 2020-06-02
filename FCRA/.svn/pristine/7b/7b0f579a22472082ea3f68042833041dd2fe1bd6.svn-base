
package service.masters;

import java.util.List;

import models.master.Currency;

import org.owasp.esapi.ESAPI;

import dao.master.CurrencyDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;


public class CurrencyTypeDetailServices extends Commons {
private static final String recordStatus = null;
	

	List<KVPair<String, String>> currencyTypeList;
	List<Currency> currencyList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String rowId;
    private Integer CreatedOn;
    private String currencyCode;
   	private String currencyName;
   
	
	 public String execute() {
		begin();
		try {
				initCurrencyTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	 public Boolean validatecurrency() throws Exception{		
			if(ESAPI.validator().isValidInput("CurrencyName", currencyName, "AlphaS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Currency Name - Only aplphabets allowed (50 characters max).", Status.ERROR, Type.BAR));
				return false;
			}
			return true;
	 }
			 public Boolean validatecurrencycode() throws Exception{
		if(ESAPI.validator().isValidInput("Currency value", currencyCode, "Num", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Currency Code - Only Numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
				return false;
			}
			return true;
		}

     public void initCurrencyTypeList() throws Exception{
    	 CurrencyDao bdao=new CurrencyDao(connection);
    	 currencyTypeList=bdao.getKVList();
	}
	
	public String initializeCurrencyList() {
		begin();
		try {
				populateCurrencyList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populateCurrencyList() throws Exception{
		CurrencyDao bdao=new CurrencyDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);				
		currencyList=bdao.gettable();
		totalRecords=bdao.getTotalRecords();
	}
	public String AddCurrency() {
		begin();
		try {
				addcurrencydata();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	
	public void addcurrencydata() throws Exception{
		CurrencyDao bdao=new CurrencyDao(connection);
		Currency currency=new Currency();
		if(validatecurrency()==true){
			if(validatecurrencycode()==true)
			{
		currency.setCurrencyName(currencyName);
		currency.setCurrencyCode(currencyCode);
		currency.setCreatedIp(myIpAddress);
	    currency.setRecordStatus(recordStatus);
		currency.setCreatedBy(myUserId);
		currency.setLastModifiedBy(myUserId);
		currency.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(currency);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Currency Name is Inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		}
		}
	
	public String editcurrencyName () {
		begin();
		try {
				editcurrency();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editcurrency() throws Exception {
		CurrencyDao bdao=new CurrencyDao(connection);
		Currency currency=new Currency();
		if(validatecurrency()==true)
		{
		currency.setCurrencyName(currencyName);
		currency.setCurrencyCode(currencyCode);
		currency.setLastModifiedBy(myUserId);
	  currency.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(currency);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Currency Name is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
public String initDeleteCurrency() {
	begin();
	try {
			deletecurrencyName();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletecurrencyName() throws Exception {
	CurrencyDao bdao=new CurrencyDao(connection);
	Currency currency=new Currency();
    currency.setCurrencyName(currencyName);
	currency.setCurrencyCode(currencyCode);
	int status=bdao.removeRecord(currency);
	if(status>0)
		notifyList.add(new Notification("Success!!", "Currency Name is Deleted successfully.", Status.SUCCESS, Type.BAR));
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


	public String getCurrencyCode() {
		return currencyCode;
	}


	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}


	public String getCurrencyName() {
		return currencyName;
	}


	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}




	public void setCreatedOn(Integer createdOn) {
		CreatedOn = createdOn;
	}


	public static String getRecordstatus() {
		return recordStatus;
	}


	public List<KVPair<String, String>> getCurrencyTypeList() {
		return currencyTypeList;
	}


	public void setCurrencyTypeList(List<KVPair<String, String>> currencyTypeList) {
		this.currencyTypeList = currencyTypeList;
	}


	public List<Currency> getCurrencyList() {
		return currencyList;
	}


	public void setCurrencyList(List<Currency> currencyList) {
		this.currencyList = currencyList;
	}
	

}
