package service.masters;

import java.util.List;

import org.owasp.esapi.ESAPI;

import models.master.RegistrationCancellationReasons;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.RegistrationCancellationReasonsDao;

public class RegistrationCancellationReasonsService extends Commons{
	//private List<UserStatus> userList;
	private List<RegistrationCancellationReasons> cancelReasonList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String reasonDesc;
	private String createdBy;
	private String reasonId;
	public String initializeRegistrationCancellationReasonsList() {
		begin();
		try {
				populateRegistrationCancellationReasonsList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateRegistrationCancellationReasonsList() throws Exception{
	
		RegistrationCancellationReasonsDao tdao=new RegistrationCancellationReasonsDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			cancelReasonList=tdao.getMainReason();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validateRegistrationCancellationReasons() throws Exception{		
		if(ESAPI.validator().isValidInput("ReasonDesc", reasonDesc, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Registration Cancellation Reasons - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddRegistrationCancellationReasons() {
		begin();
		try {
				addRegistrationCancellationReason();
		} catch(Exception e){
			try {
				notifyList.add(new Notification("Error!","Cancellation Reasons Id is already in use", Status.ERROR, Type.BAR));
			}catch(Exception ex) {}
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addRegistrationCancellationReason() throws Exception{
		
		RegistrationCancellationReasonsDao ndao=new RegistrationCancellationReasonsDao(connection);
		RegistrationCancellationReasons userStatus=new RegistrationCancellationReasons();
		if(validateRegistrationCancellationReasons()==true){
			userStatus.setReasonDesc(reasonDesc);
			userStatus.setReasonId(reasonId);
			userStatus.setCreatedIp(myIpAddress);
			userStatus.setCreatedBy(myUserId);
			userStatus.setLastModifiedBy(myUserId);
			userStatus.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(userStatus);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New Registration Cancellation Reasons <b>"+reasonDesc.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	public String initDeleteRegistrationCancellationReasons() {
		begin();
		try {
				deleteRegistrationCancellationReasons();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteRegistrationCancellationReasons() throws Exception{
		RegistrationCancellationReasonsDao ndao=new RegistrationCancellationReasonsDao(connection);
		RegistrationCancellationReasons  userStatus=new RegistrationCancellationReasons();
		userStatus.setReasonId(reasonId);	
		int status=ndao.removeRecord(userStatus);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Registration Cancellation Reasons <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	public String initEditRegistrationCancellationReasons() {
		begin();
		try {
				editRegistrationCancellationReasons();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editRegistrationCancellationReasons() throws Exception{
		RegistrationCancellationReasonsDao ndao=new RegistrationCancellationReasonsDao(connection);
		RegistrationCancellationReasons userStatus=new RegistrationCancellationReasons();
		if(validateRegistrationCancellationReasons()==true){
			userStatus.setReasonId(reasonId);
			userStatus.setReasonDesc(reasonDesc);
			userStatus.setCreatedIp(myIpAddress);
			userStatus.setCreatedBy(myUserId);
			userStatus.setLastModifiedBy(myUserId);
			userStatus.setLastModifiedIp(myIpAddress);
		
		int i=ndao.editRecord(userStatus);
		if(i>0)
			notifyList.add(new Notification("Success!!", " Registration Cancellation Reasons <b>"+reasonDesc.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
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

	public List<RegistrationCancellationReasons> getCancelReasonList() {
		return cancelReasonList;
	}
	public void setCancelReasonList(
			List<RegistrationCancellationReasons> cancelReasonList) {
		this.cancelReasonList = cancelReasonList;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	
	public String getReasonId() {
		return reasonId;
	}
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}

