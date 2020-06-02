package service.masters;

import java.util.List;

import models.master.ApplicationStage;

import org.owasp.esapi.ESAPI;

import dao.master.ApplicationStageDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;


public class ApplicationStageServices extends Commons {
private static final String recordStatus = null;
	

	List<KVPair<String, String>> applicationTypeList;
	List<ApplicationStage> applicationList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String rowId;
    private Integer CreatedOn;
    private String stageId;
   	private String stageDesc;
  
   
	
	 public String execute() {
		begin();
		try {
				initappList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
     public void initappList() throws Exception{
    	 ApplicationStageDao bdao=new ApplicationStageDao(connection);
    	 applicationTypeList=bdao.getKVList();
	}
	
	public String initializeAppList() {
		begin();
		try {
				populateappList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populateappList() throws Exception{
		 ApplicationStageDao bdao=new ApplicationStageDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);				
		applicationList=bdao.gettable();
		totalRecords=bdao.getTotalRecords();
	}
	public String AddDesc() {
		begin();
		try {
				adddescdata();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	 public Boolean validatestage() throws Exception{
			if(ESAPI.validator().isValidInput("Stage Desc", stageDesc, "AlphaS", 50, false) == false){
					notifyList.add(new Notification("Error!!", "Stage Description - Only alphabet allowed (50 characters max).", Status.ERROR, Type.BAR));
					return false;
				}
				return true;
	 }

	
	public void adddescdata() throws Exception{
		ApplicationStageDao bdao=new ApplicationStageDao(connection);
		ApplicationStage app=new ApplicationStage();
		if(validatestage()==true)
		{
		app.setStageDesc(stageDesc);
		app.setStageId(stageId);
		app.setCreatedIp(myIpAddress);
	    app.setRecordStatus(recordStatus);
		app.setCreatedBy(myUserId);
		app.setLastModifiedBy(myUserId);
		app.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(app);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Stage Description is Inserted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
		
	
	public String EditDesc () {
		begin();
		try {
				editdesc();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editdesc() throws Exception {
		ApplicationStageDao bdao=new ApplicationStageDao(connection);
		ApplicationStage app=new ApplicationStage();
		if(validatestage()==true)
		{
		app.setStageDesc(stageDesc);
		app.setStageId(stageId);
		app.setLastModifiedBy(myUserId);
	    app.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(app);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Stage Description is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}

	
public String DeleteDesc() {
	begin();
	try {
			deletedesc();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletedesc() throws Exception {
	ApplicationStageDao bdao=new ApplicationStageDao(connection);
	ApplicationStage app=new ApplicationStage();
	app.setStageDesc(stageDesc);
	app.setStageId(stageId);
	int status=bdao.removeRecord(app);
	if(status>0)
		notifyList.add(new Notification("Success!!", " Stage Description is Deleted successfully.", Status.SUCCESS, Type.BAR));
}
	public List<KVPair<String, String>> getApplicationTypeList() {
		return applicationTypeList;
	}



	public void setApplicationTypeList(
			List<KVPair<String, String>> applicationTypeList) {
		this.applicationTypeList = applicationTypeList;
	}



	public List<ApplicationStage> getApplicationList() {
		return applicationList;
	}



	public void setApplicationList(List<ApplicationStage> applicationList) {
		this.applicationList = applicationList;
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



	public String getStageId() {
		return stageId;
	}



	public void setStageId(String stageId) {
		this.stageId = stageId;
	}



	public String getStageDesc() {
		return stageDesc;
	}



	public void setStageDesc(String stageDesc) {
		this.stageDesc = stageDesc;
	}



	public static String getRecordstatus() {
		return recordStatus;
	}
}
