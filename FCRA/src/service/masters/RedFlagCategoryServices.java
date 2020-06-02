package service.masters;

import java.util.List;

import models.master.RedFlagCategory;

import org.owasp.esapi.ESAPI;

import dao.master.RedFlagCategoryDao;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class RedFlagCategoryServices extends Commons {
	private List<RedFlagCategory> redflagList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String categoryName;
	private String createdBy;
	private Integer categoryCode;
	
	public String initializeRedFlagList() {
		begin();
		try {
				populateRedflagList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateRedflagList() throws Exception{
			RedFlagCategoryDao rfcdao=new RedFlagCategoryDao(connection);
			rfcdao.setPageNum(pageNum);
			rfcdao.setRecordsPerPage(recordsPerPage);
			rfcdao.setSortColumn(sortColumn);
			rfcdao.setSortOrder(sortOrder);				
			redflagList=rfcdao.getList();
			totalRecords=rfcdao.getTotalRecords();
		}
		
	public Boolean validateCategoryName() throws Exception{		
		if(ESAPI.validator().isValidInput("CategoryName", categoryName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Category Description - Only alphabet and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddCategory() {
		begin();
		try {
			addCategory();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addCategory() throws Exception{
		RedFlagCategoryDao rfcdao=new RedFlagCategoryDao(connection);
		RedFlagCategory rfc=new RedFlagCategory();
		if(validateCategoryName()==true){
			rfc.setCategoryName(categoryName);
			rfc.setCreatedIp(myIpAddress);
			rfc.setCreatedBy(myUserId);
			rfc.setLastModifiedBy(myUserId);
			rfc.setLastModifiedIp(myIpAddress);		
			int i=rfcdao.insertRecord(rfc);
			if(i>0)
			notifyList.add(new Notification("Success!!", "New Category <b>"+categoryName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	public String initDeleteCategory() {
		begin();
		try {
				deleteCategory();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteCategory() throws Exception{
		RedFlagCategoryDao rfcdao=new RedFlagCategoryDao(connection);
		RedFlagCategory rfc=new RedFlagCategory();
		rfc.setCategoryCode(categoryCode);	
		int status=rfcdao.removeRecord(rfc);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Category   <b></b> is deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditCategory() {
		begin();
		try {
				editCategory();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editCategory() throws Exception{
		RedFlagCategoryDao rfcdao=new RedFlagCategoryDao(connection);
		RedFlagCategory rfc=new RedFlagCategory();
		if(validateCategoryName()==true){
			rfc.setCategoryCode(categoryCode);
		rfc.setCategoryName(categoryName);
			rfc.setCreatedIp(myIpAddress);
			rfc.setCreatedBy(myUserId);
			rfc.setLastModifiedBy(myUserId);
			rfc.setLastModifiedIp(myIpAddress);
		
		int i=rfcdao.editRecord(rfc);
		if(i>0)
			notifyList.add(new Notification("Success!!", " Category <b>"+categoryName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}



	public List<RedFlagCategory> getRedflagList() {
		return redflagList;
	}

	public String getPageNum() {
		return pageNum;
	}

	public String getRecordsPerPage() {
		return recordsPerPage;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setRedflagList(List<RedFlagCategory> redflagList) {
		this.redflagList = redflagList;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}


}
