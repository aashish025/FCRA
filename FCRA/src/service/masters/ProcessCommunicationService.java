package service.masters;

import java.util.List;

import models.master.ProcessCommunicationStage;

import org.owasp.esapi.ESAPI;

import dao.master.ProcessCommunicationStageDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;


public class ProcessCommunicationService extends Commons {
private static final String recordStatus = null;
	

	List<KVPair<String, String>> processcommunicationTypeList;
	List<ProcessCommunicationStage> processcommunicationList;
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
				initPcList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
     public void initPcList() throws Exception{
    	 ProcessCommunicationStageDao bdao=new ProcessCommunicationStageDao(connection);
    	 processcommunicationTypeList=bdao.getKVList();
	}
	
	public String initializePcList() {
		begin();
		try {
				populatepcList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populatepcList() throws Exception{
		ProcessCommunicationStageDao bdao=new ProcessCommunicationStageDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);				
		processcommunicationList=bdao.gettable();
		totalRecords=bdao.getTotalRecords();
	}
	public String AddPc() {
		begin();
		try {
				addpcdata();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	public Boolean validatepcstage() throws Exception{
			if(ESAPI.validator().isValidInput("Stage Desc", stageDesc, "AlphaS", 50, false) == false){
					notifyList.add(new Notification("Error!!", "Process Communication - Only alphabet allowed (50 characters max).", Status.ERROR, Type.BAR));
					return false;
				}
				return true;
	 }

	
	public void addpcdata() throws Exception{
		ProcessCommunicationStageDao bdao=new ProcessCommunicationStageDao(connection);
		ProcessCommunicationStage pc=new ProcessCommunicationStage();
		if(validatepcstage()==true)
		{
		pc.setStageDesc(stageDesc);
		pc.setStageId(stageId);
		pc.setCreatedIp(myIpAddress);
	    pc.setRecordStatus(recordStatus);
		pc.setCreatedBy(myUserId);
		pc.setLastModifiedBy(myUserId);
		pc.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(pc);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Process Communication Description is Inserted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
		
	
	public String EditPc () {
		begin();
		try {
				editpc();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editpc() throws Exception {
		ProcessCommunicationStageDao bdao=new ProcessCommunicationStageDao(connection);
		ProcessCommunicationStage pc=new ProcessCommunicationStage();
	  if(validatepcstage()==true)
		{
		pc.setStageDesc(stageDesc);
		pc.setStageId(stageId);
		pc.setLastModifiedBy(myUserId);
	    pc.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(pc);
		if(status>0)
			notifyList.add(new Notification("Success!!", " Process Communication is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}

	
public String DeletePc() {
	begin();
	try {
			deletedpc();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletedpc() throws Exception {
	ProcessCommunicationStageDao bdao=new ProcessCommunicationStageDao(connection);
	ProcessCommunicationStage pc=new ProcessCommunicationStage();
	pc.setStageDesc(stageDesc);
	pc.setStageId(stageId);
	int status=bdao.removeRecord(pc);
	if(status>0)
		notifyList.add(new Notification("Success!!", " Process Communication is Deleted successfully.", Status.SUCCESS, Type.BAR));
}

public List<KVPair<String, String>> getProcesscommunicationTypeList() {
	return processcommunicationTypeList;
}

public void setProcesscommunicationTypeList(
		List<KVPair<String, String>> processcommunicationTypeList) {
	this.processcommunicationTypeList = processcommunicationTypeList;
}

public List<ProcessCommunicationStage> getProcesscommunicationList() {
	return processcommunicationList;
}

public void setProcesscommunicationList(
		List<ProcessCommunicationStage> processcommunicationList) {
	this.processcommunicationList = processcommunicationList;
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
