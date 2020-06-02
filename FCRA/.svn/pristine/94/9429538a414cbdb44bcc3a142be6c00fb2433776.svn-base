package service.masters;

import java.util.List;

import org.owasp.esapi.ESAPI;

import utilities.notifications.Notification;
import models.master.FileStatus;
import dao.master.FileStatusDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List3;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class FileStatusServices extends Commons {
	
	private static final String recordStatus = null;
	List<KVPair<String, String>> fileStatusList;
	List<FileStatus> fileStatus;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	//private String FileStatus;
	private String filestatusDesc;
	public Short filestatusId;
    private String rowId;
    private Integer CreatedOn;
	
	
	 public String execute() {
		begin();
		try {
				initFileStatusList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	


	public String initFileStatusList() {
		begin();
		try {
				populateFileList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateFileList() throws Exception{
		FileStatusDao adao=new FileStatusDao(connection);
		adao.setPageNum(pageNum);
		adao.setRecordsPerPage(recordsPerPage);
		adao.setSortColumn(sortColumn);
		adao.setSortOrder(sortOrder);	
		fileStatus=adao.getMasteracquisition();
		totalRecords=adao.getTotalRecords();
	}
	
	public String AddUser() {
		begin();
		try {
				addFileStatus();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public Boolean validateFileStatus() throws Exception{		
		if(ESAPI.validator().isValidInput("FileStatus", filestatusDesc, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "File Status Detail - Only Aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public void addFileStatus() throws Exception{
		FileStatusDao rdao=new FileStatusDao(connection);
		FileStatus userlavel=new FileStatus();
		if(validateFileStatus()==true){
				userlavel.setFilestatusDesc(filestatusDesc);
				userlavel.setCreatedIp(myIpAddress);
				userlavel.setCreatedBy(myUserId);
				userlavel.setLastModifiedBy(myUserId);
				userlavel.setLastModifiedIp(myIpAddress);
				int status=rdao.insertRecord(userlavel);
				if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "File Status Detail is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		
	}
	
	public String editFileStatus () {
		begin();
		try {
			editFileDetails();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editFileDetails() throws Exception {
		FileStatusDao rdao=new FileStatusDao(connection);
		FileStatus userlavel=new FileStatus();
		if(validateFileStatus()==true){
			userlavel.setFilestatusId(filestatusId);
			userlavel.setFilestatusDesc(filestatusDesc);
			userlavel.setCreatedIp(myIpAddress);
			userlavel.setCreatedBy(myUserId);
		int status=	rdao.editRecord(userlavel);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "File Status Detail is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}


public String initDeleteFileStatus() {
	begin();
	try {
			deletefilestatus();
	} catch(Exception e){
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletefilestatus() throws Exception {
	FileStatusDao rdao=new FileStatusDao(connection);
	FileStatus userlavel=new FileStatus();
	userlavel.setFilestatusId(filestatusId);
	userlavel.setFilestatusDesc(filestatusDesc);
	int status=rdao.removeRecord(userlavel);
	if(status>0)
		notifyList.add(new utilities.notifications.Notification("Success!!", "File Status Detail is Deleted successfully.", Status.SUCCESS, Type.BAR));
}


public Integer getCreatedOn() {
	return CreatedOn;
}

public void setCreatedOn(Integer createdOn) {
	CreatedOn = createdOn;
}

public String getRowId() {
	return rowId;
}

public void setRowId(String rowId) {
	this.rowId = rowId;
}



public String getFilestatusDesc() {
	return filestatusDesc;
}



public void setFilestatusDesc(String filestatusDesc) {
	this.filestatusDesc = filestatusDesc;
}



public Short getFilestatusId() {
	return filestatusId;
}



public void setFilestatusId(Short filestatusId) {
	this.filestatusId = filestatusId;
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

public List<KVPair<String, String>> getFileStatusList() {
	return fileStatusList;
}



public void setFileStatusList(List<KVPair<String, String>> fileStatusList) {
	this.fileStatusList = fileStatusList;
}



public List<FileStatus> getFileStatus() {
	return fileStatus;
}



public void setFileStatus(List<FileStatus> fileStatus) {
	this.fileStatus = fileStatus;
}



public List<List3> getRequestedDetails() {
	return list3;
}
}



