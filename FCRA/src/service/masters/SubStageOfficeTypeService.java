package service.masters;

import java.util.List;
import dao.master.ApplicationSubStageDao;
import dao.master.OfficeTypeDao;

import dao.master.SubStageOfficeTypeDao;
import models.master.SubStage;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class SubStageOfficeTypeService extends Commons{
	private List<SubStage> subList;
	private List<SubStage> subStageList;
	List<KVPair<String, String>> proposalDescList;
	List<KVPair<String, String>> substageDescList;
	List<KVPair<String, String>> officeTypeList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String officeId;
	private String subStageId;
	private String proposalTypeId;
	private String createdBy;
	private String rowId;
	
	public String execute() {
			begin();
			try {
				initProposalDescList();
				initSubStageDescList();
				initOfficeTypeList();
				} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}
	 public void initProposalDescList() throws Exception{
		
		}
	 public void initSubStageDescList() throws Exception{
		 ApplicationSubStageDao ddao=new ApplicationSubStageDao(connection);
	         substageDescList=ddao.getKVList();
		}
	 public void initOfficeTypeList() throws Exception{
		 OfficeTypeDao ddao=new OfficeTypeDao(connection);
	         officeTypeList=ddao.getKVList();
		}
	 
 	public String initializeSubStageOfcList() {
		begin();
		try {
				populateSubStageOfcList();
				
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateSubStageOfcList() throws Exception{
		SubStageOfficeTypeDao tdao=new SubStageOfficeTypeDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			subStageList=tdao.getMainSubStage();
			totalRecords=tdao.getTotalRecords();
		}
	
	public String initAddSubStage() {
		begin();
		try {
				addSubStage();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	private void addSubStage() throws Exception{
		SubStageOfficeTypeDao ndao=new SubStageOfficeTypeDao(connection);
		SubStage subStage=new SubStage();
			subStage.setOfficeId(officeId);
			subStage.setSubStageId(subStageId);
			subStage.setProposalTypeId(proposalTypeId);
			subStage.setCreatedIp(myIpAddress);
			subStage.setCreatedBy(myUserId);
			subStage.setLastModifiedBy(myUserId);
			subStage.setLastModifiedIp(myIpAddress);		
			Integer x=ndao.insertRecord(subStage);
		if(x!=null){
			notifyList.add(new Notification("Success!!", " Sub Stage Office Type<b></b> is Inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		else {
			notifyList.add(new Notification("Info!!", " Check Your Input It May be duplicate ", Status.INFORMATION, Type.BAR));
				
				}}
	
	
	public String initDeleteSubStage() {
		begin();
		try {
				deleteSubStage();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void deleteSubStage() throws Exception{
		SubStageOfficeTypeDao ndao=new SubStageOfficeTypeDao(connection);
		SubStage subStage=new SubStage();
		subStage.setRowId(rowId);
		int status=ndao.removeRecord(subStage);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Sub Stage Office Type<b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditSubStage() {
		begin();
		try {
				editSubStage();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void editSubStage() throws Exception{
		SubStageOfficeTypeDao ndao=new SubStageOfficeTypeDao(connection);
		SubStage subStage=new SubStage();
		subStage.setRowId(rowId);
		subStage.setOfficeId(officeId);
		subStage.setSubStageId(subStageId);
		subStage.setProposalTypeId(proposalTypeId);
		subStage.setCreatedIp(myIpAddress);
		subStage.setCreatedBy(myUserId);
		subStage.setLastModifiedBy(myUserId);
		subStage.setLastModifiedIp(myIpAddress);
		Integer x=ndao.editRecord(subStage);
		if(x!=null){
			notifyList.add(new Notification("Success!!", " Sub Stage Office Type<b></b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
		else {
			notifyList.add(new Notification("Info!!", " Check Your Input It is Already Exist in Database", Status.INFORMATION, Type.BAR));
				
				}}
	
	public String getPageNum() {
		return pageNum;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getSubStageId() {
		return subStageId;
	}
	public void setSubStageId(String subStageId) {
		this.subStageId = subStageId;
	}
	public String getProposalTypeId() {
		return proposalTypeId;
	}
	public void setProposalTypeId(String proposalTypeId) {
		this.proposalTypeId = proposalTypeId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public List<KVPair<String, String>> getOfficeTypeList() {
		return officeTypeList;
	}
	public void setOfficeTypeList(List<KVPair<String, String>> officeTypeList) {
		this.officeTypeList = officeTypeList;
	}
	public List<KVPair<String, String>> getProposalDescList() {
		return proposalDescList;
	}
	public void setProposalDescList(List<KVPair<String, String>> proposalDescList) {
		this.proposalDescList = proposalDescList;
	}
	public List<KVPair<String, String>> getSubstageDescList() {
		return substageDescList;
	}
	public void setSubstageDescList(List<KVPair<String, String>> substageDescList) {
		this.substageDescList = substageDescList;
	}
	
	public List<SubStage> getSubList() {
		return subList;
	}

	public void setSubList(List<SubStage> subList) {
		this.subList = subList;
	}

	public List<SubStage> getSubStageList() {
		return subStageList;
	}

	public void setSubStageList(List<SubStage> subStageList) {
		this.subStageList = subStageList;
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

}

