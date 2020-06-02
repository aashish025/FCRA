package service.masters;

import java.util.List;
import models.master.ApplicationSubStage;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.ApplicationStageDao;
import dao.master.ApplicationSubStageDao;


public class ApplicationSubstageServices extends Commons{
private static final String recordStatus = null;
	

	List<KVPair<String, String>> applicationsubTypeList;
	List<KVPair<String, String>> applicationTypeList;
	List<ApplicationSubStage> applicationsubList;

    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String rowId;
    private Integer CreatedOn;
    private String substageId;
   	private String substageDesc;
   	private String parentstageId;
  
   
	
	 public String execute() {
		begin();
		try {
			initApplicationList();
				initappList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	 public void initApplicationList() throws Exception{
    	 ApplicationStageDao ddao=new ApplicationStageDao(connection);
    	 applicationTypeList=ddao.getKVList();
	 }
	 
	
     public void initappList() throws Exception{
    	 ApplicationSubStageDao bdao=new ApplicationSubStageDao(connection);
    	 applicationsubTypeList=bdao.getKVList();
	}

     public String initializeSubAppList() {
 		begin();
 		try {
 				populatesubappList();
 		} catch(Exception e){
 			spl(e);
 		}
 		finally{
 			finish();
 		}	
 		return "success";
 	} 

 	private void populatesubappList() throws Exception{
 		 ApplicationSubStageDao bdao=new ApplicationSubStageDao(connection);
 		bdao.setPageNum(pageNum);
 		bdao.setRecordsPerPage(recordsPerPage);
 		bdao.setSortColumn(sortColumn);
 		bdao.setSortOrder(sortOrder);				
 		applicationsubList=bdao.getsubtable();
 		totalRecords=bdao.getTotalRecords();
 	}
 	
	public String AddSubsDesc() {
		begin();
		try {
				addsubdescdata();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	 public Boolean validatesubstage() throws Exception{
			if(ESAPI.validator().isValidInput(" Sub Stage Desc", substageDesc, "AlphaS", 50, false) == false){
					notifyList.add(new Notification("Error!!", "Sub Stage Description - Only alphabet allowed (50 characters max).", Status.ERROR, Type.BAR));
					return false;
				}
				return true;
	 }

	
	public void addsubdescdata() throws Exception{
		 ApplicationSubStageDao bdao=new ApplicationSubStageDao(connection);
		ApplicationSubStage appsub=new ApplicationSubStage();
		if(validatesubstage()==true)
		{
		appsub.setSubstageDesc(substageDesc);
		appsub.setSubstageId(substageId);
		appsub.setParentstageId(parentstageId);
		appsub.setCreatedIp(myIpAddress);
	    appsub.setRecordStatus(recordStatus);
		appsub.setCreatedBy(myUserId);
		appsub.setLastModifiedBy(myUserId);
		appsub.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(appsub);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Sub Stage Description is Inserted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
		
	
	public String EditSubDesc () {
		begin();
		try {
				editsubdesc();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editsubdesc() throws Exception {
		ApplicationSubStageDao bdao=new ApplicationSubStageDao(connection);
		ApplicationSubStage appsub=new ApplicationSubStage();
		
		if(validatesubstage()==true)
		{
		appsub.setSubstageDesc(substageDesc);
		appsub.setSubstageId(substageId);
		appsub.setParentstageId(parentstageId);
		appsub.setLastModifiedBy(myUserId);
	    appsub.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(appsub);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Sub Stage Description is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
}

	
public String DeleteSubDesc() {
	begin();
	try {
			deletesubdesc();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletesubdesc() throws Exception {
	ApplicationSubStageDao bdao=new ApplicationSubStageDao(connection);
	ApplicationSubStage appsub=new ApplicationSubStage();
	appsub.setSubstageDesc(substageDesc);
	appsub.setSubstageId(substageId);
	appsub.setParentstageId(parentstageId);
	int status=bdao.removeRecord(appsub);
	if(status>0)
		notifyList.add(new Notification("Success!!", "Sub Stage Description is Deleted successfully.", Status.SUCCESS, Type.BAR));
}

	public List<KVPair<String, String>> getApplicationsubTypeList() {
		return applicationsubTypeList;
	}

	public void setApplicationsubTypeList(
			List<KVPair<String, String>> applicationsubTypeList) {
		this.applicationsubTypeList = applicationsubTypeList;
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

	public List<KVPair<String, String>> getApplicationTypeList() {
		return applicationTypeList;
	}


	public void setApplicationTypeList(
			List<KVPair<String, String>> applicationTypeList) {
		this.applicationTypeList = applicationTypeList;
	}


	public void setCreatedOn(Integer createdOn) {
		CreatedOn = createdOn;
	}

	public String getSubstageId() {
		return substageId;
	}

	public void setSubstageId(String substageId) {
		this.substageId = substageId;
	}

	public String getSubstageDesc() {
		return substageDesc;
	}

	public void setSubstageDesc(String substageDesc) {
		this.substageDesc = substageDesc;
	}


	public static String getRecordstatus() {
		return recordStatus;
	}


	public List<ApplicationSubStage> getApplicationsubList() {
		return applicationsubList;
	}


	public void setApplicationsubList(List<ApplicationSubStage> applicationsubList) {
		this.applicationsubList = applicationsubList;
	}
	public String getParentstageId() {
		return parentstageId;
	}
	public void setParentstageId(String parentstageId) {
		this.parentstageId = parentstageId;
	}
	
	
	
}
