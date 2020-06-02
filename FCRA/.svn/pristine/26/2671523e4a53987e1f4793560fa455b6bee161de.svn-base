package service.masters;

import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.Banks;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.BanksDao;

public class BanksService extends Commons{
	private List<Banks> banksList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String bankName;
	private String createdBy;
	private Integer bankCode;
	
	public String initializeBankList() {
		begin();
		try {
				populateBankList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateBankList() throws Exception{
			BanksDao tdao=new BanksDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			banksList=tdao.getMainBanks();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validateBank() throws Exception{		
		if(ESAPI.validator().isValidInput("BankName", bankName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Bank Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddBank() {
		begin();
		try {
				addBank();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addBank() throws Exception{
		BanksDao ndao=new BanksDao(connection);
		Banks banks=new Banks();
		if(validateBank()==true){
			banks.setBankName(bankName);
			banks.setCreatedIp(myIpAddress);
			banks.setCreatedBy(myUserId);
			banks.setLastModifiedBy(myUserId);
			banks.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(banks);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New Bank <b>"+bankName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	
	public String initDeleteBank() {
		begin();
		try {
				deleteBank();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteBank() throws Exception{
		BanksDao ndao=new BanksDao(connection);
		Banks  banks=new Banks();
		banks.setBankCode(bankCode);	
		int status=ndao.removeRecord(banks);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Bank <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditBank() {
		begin();
		try {
				editBank();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editBank() throws Exception{
		BanksDao ndao=new BanksDao(connection);
		Banks banks=new Banks();
		if(validateBank()==true){
			banks.setBankCode(bankCode);
			banks.setBankName(bankName);
			banks.setCreatedIp(myIpAddress);
			banks.setCreatedBy(myUserId);
			banks.setLastModifiedBy(myUserId);
			banks.setLastModifiedIp(myIpAddress);
		
		int i=ndao.editRecord(banks);
		if(i>0)
			notifyList.add(new Notification("Success!!", " Bank <b>"+bankName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
		}
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
	
	public List<Banks> getBanksList() {
		return banksList;
	}
	public void setBanksList(List<Banks> banksList) {
		this.banksList = banksList;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Integer getBankCode() {
		return bankCode;
	}
	public void setBankCode(Integer bankCode) {
		this.bankCode = bankCode;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
