package service.masters;

import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.CommitteeRelation;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.CommitteeRelationDao;

public class CommitteeRelationService extends Commons{
	
	private List<CommitteeRelation> comRelationList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String relationName;
	private String createdBy;
	private Integer relationCode;
	
	public String initializeComRelationList() {
		begin();
		try {
				populateComRelationList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateComRelationList() throws Exception{
		CommitteeRelationDao tdao=new CommitteeRelationDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			comRelationList=tdao.getMainComRelation();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validateComRelation() throws Exception{		
		if(ESAPI.validator().isValidInput("RelationName", relationName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Committee Relation Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddComRelation() {
		begin();
		try {
				addComRelation();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addComRelation() throws Exception{
		CommitteeRelationDao ndao=new CommitteeRelationDao(connection);
		CommitteeRelation comrelation=new CommitteeRelation();
		if(validateComRelation()==true){
			comrelation.setRelationName(relationName);
			comrelation.setCreatedIp(myIpAddress);
			comrelation.setCreatedBy(myUserId);
			comrelation.setLastModifiedBy(myUserId);
			comrelation.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(comrelation);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New Committee Relation <b>"+relationName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
				}
	}
	
	public String initDeleteComRelation() {
		begin();
		try {
				deleteComRelation();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteComRelation() throws Exception{
		CommitteeRelationDao ndao=new CommitteeRelationDao(connection);
		CommitteeRelation  comrelation=new CommitteeRelation();
		comrelation.setRelationCode(relationCode);	
		int status=ndao.removeRecord(comrelation);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Committee Relation<b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditComRelation() {
		begin();
		try {
				editComRelation();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editComRelation() throws Exception{
		CommitteeRelationDao ndao=new CommitteeRelationDao(connection);
		CommitteeRelation comrelation=new CommitteeRelation();
		if(validateComRelation()==true){
			comrelation.setRelationCode(relationCode);
			comrelation.setRelationName(relationName);
			comrelation.setCreatedIp(myIpAddress);
			comrelation.setCreatedBy(myUserId);
			comrelation.setLastModifiedBy(myUserId);
			comrelation.setLastModifiedIp(myIpAddress);
			int i=ndao.editRecord(comrelation);
		if(i>0)
			notifyList.add(new Notification("Success!!", " Committee Relation <b>"+relationName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
				}
	}


	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public Integer getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(Integer relationCode) {
		this.relationCode = relationCode;
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
		
	public List<CommitteeRelation> getComRelationList() {
		return comRelationList;
	}

	public void setComRelationList(List<CommitteeRelation> comRelationList) {
		this.comRelationList = comRelationList;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}

