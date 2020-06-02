package service.masters;

import java.sql.SQLException;
import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.Purpose;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.PurposeDao;

public class PurposeService extends Commons{
	private List<Purpose> purposeList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String purposeName;
	private String createdBy;
	private Integer purposeCode;
	
	
	public String initializePurposeList() {
		begin();
		try {
				populatePurposeList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populatePurposeList() throws Exception{
		PurposeDao tdao=new PurposeDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			purposeList=tdao.getMainPurpose();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validatePurpose() throws Exception{		
		if(ESAPI.validator().isValidInput("PurposeName", purposeName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Purpose Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddPurpose() {
		begin();
		try {
				addPurpose();
		} catch(Exception e){
			try{	notifyList.add(new Notification("Error!","Purpose Code is already in use", Status.ERROR, Type.BAR));
			}catch(Exception ex) {}
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException e2) {
				ps(e2);
			}
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addPurpose() throws Exception{
		PurposeDao ndao=new PurposeDao(connection);
		Purpose purpose=new Purpose();
		if(validatePurpose()==true){
			purpose.setPurposeCode(purposeCode);
			purpose.setPurposeName(purposeName);
			purpose.setCreatedIp(myIpAddress);
			purpose.setCreatedBy(myUserId);
			purpose.setLastModifiedBy(myUserId);
			purpose.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(purpose);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New Purpose <b>"+purposeName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	
	public String initDeletePurpose() {
		begin();
		try {
				deletePurpose();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deletePurpose() throws Exception{
		PurposeDao ndao=new PurposeDao(connection);
		Purpose  purpose=new Purpose();
		purpose.setPurposeCode(purposeCode);	
		int status=ndao.removeRecord(purpose);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Purpose <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditPurpose() {
		begin();
		try {
				editPurpose();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editPurpose() throws Exception{
		PurposeDao ndao=new PurposeDao(connection);
		Purpose purpose=new Purpose();
		if(validatePurpose()==true){
			purpose.setPurposeCode(purposeCode);
			purpose.setPurposeName(purposeName);
			purpose.setCreatedIp(myIpAddress);
			purpose.setCreatedBy(myUserId);
			purpose.setLastModifiedBy(myUserId);
			purpose.setLastModifiedIp(myIpAddress);
				int i=ndao.editRecord(purpose);
		if(i>0)
			notifyList.add(new Notification("Success!!", " Purpose <b>"+purposeName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}


	
	public List<Purpose> getPurposeList() {
		return purposeList;
	}

	public void setPurposeList(List<Purpose> purposeList) {
		this.purposeList = purposeList;
	}

	public String getPurposeName() {
		return purposeName;
	}

	public void setPurposeName(String purposeName) {
		this.purposeName = purposeName;
	}

	public Integer getPurposeCode() {
		return purposeCode;
	}

	public void setPurposeCode(Integer purposeCode) {
		this.purposeCode = purposeCode;
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
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}

