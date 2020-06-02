package service.masters;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dao.services.NotificationDao;
import models.services.Notification;
import utilities.Commons;
import utilities.KVPair;
import utilities.lists.List1;
import utilities.lists.List3;

public class NotificationDetailsService extends Commons{
	private List<Notification> notificationList;
	List<KVPair<String, String>> notificationTypeList;
	private List<List3> requestedDetails;
	private List<List1> requestedAttachmentList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String notificationType;
	private String notificationTitle;
	private String notificationDetails;
	private MultipartFile attachment;
	private String notificationId;
	private String uploadId;
	private String rowId;
	
	
	public String execute() {
		begin();
		try {
				initNotificationTypeList();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	public void initNotificationTypeList() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		notificationTypeList=ndao.getKVList();
	}
	
	public String initializeNotificationList() {
		begin();
		try {
				populateNotificationList();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateNotificationList() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		ndao.setPageNum(pageNum);
		ndao.setRecordsPerPage(recordsPerPage);
		ndao.setSortColumn(sortColumn);
		ndao.setSortOrder(sortOrder);				
		notificationList=ndao.getMasterNotification();
		totalRecords=ndao.getTotalRecords();
	}
	
	public String initAddNotification() {
		begin();
		try {
				addNotification();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addNotification() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		Notification notification=new Notification();
		notification.setNotificationTypeId(notificationType);
		notification.setMessageTitle(notificationTitle);
		notification.setMessageDetails(notificationDetails);
		notification.setEnteredBy(myUserId);
		notification.setSource_office(myOfficeCode);
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		ndao.setSessionId(sessId);
		ndao.insertRecord(notification);
	}
	
	public String initEditNotification() {
		begin();
		try {
				editNotification();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editNotification() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		Notification notification=new Notification();
		notification.setNotificationId(notificationId);
		notification.setNotificationTypeId(notificationType);
		notification.setMessageTitle(notificationTitle);
		notification.setMessageDetails(notificationDetails);
		notification.setEnteredBy(myUserId);
		notification.setSource_office(myOfficeCode);
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		ndao.setSessionId(sessId);
		ndao.editRecord(notification);
	}
	
	public String initDeleteNotification() {
		begin();
		try {
				deleteNotification();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteNotification() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		Notification notification=new Notification();
		notification.setNotificationId(notificationId);		
		ndao.removeRecord(notification);
	}
	
	public String initUploadNotificationAttachment() {
		begin();
		try {
				uploadNotificationAttachment();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	public void uploadNotificationAttachment() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		ndao.setAttachment(attachment);
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		ndao.setSessionId(sessId);
		ndao.setUploadId(uploadId);
		ndao.uploadNotificationDocument();
	}
	
	public String initDeleteNotificationAttachment() {
		begin();
		try {
				deleteNotificationAttachment();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	public void deleteNotificationAttachment() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);		
		int index=sessionId.indexOf(".");
		String sessId=sessionId.substring(0, index);
		ndao.setSessionId(sessId);
		ndao.setUploadId(uploadId);
		ndao.deleteNotificationDocument();
	}
	
	public String initDeleteAvailableNotificationAttachment() {
		begin();
		try {
				deleteAvailableNotificationAttachment();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	public void deleteAvailableNotificationAttachment() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);		
		Notification notification=new Notification();
		notification.setRowId(rowId);
		ndao.deleteAvailableNotificationDocument(notification);
	}
	
	public String initGetNotification() {
		begin();
		try {
				getNotification();
				getNotificationAttachment();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}	
	
	private void getNotification() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		Notification notification=new Notification();
		notification.setNotificationId(notificationId);
		requestedDetails=ndao.getNotification(notification);
	}
	
	private void getNotificationAttachment() throws Exception{
		NotificationDao ndao=new NotificationDao(connection);
		Notification notification=new Notification();
		notification.setNotificationId(notificationId);
		requestedAttachmentList=ndao.getNotificationAttachment(notification);
	}
		
	public List<Notification> getNotificationList() {
		return notificationList;
	}

	public void setNotificationList(List<Notification> notificationList) {
		this.notificationList = notificationList;
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

	public List<KVPair<String, String>> getNotificationTypeList() {
		return notificationTypeList;
	}

	public void setNotificationTypeList(
			List<KVPair<String, String>> notificationTypeList) {
		this.notificationTypeList = notificationTypeList;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public String getNotificationDetails() {
		return notificationDetails;
	}

	public void setNotificationDetails(String notificationDetails) {
		this.notificationDetails = notificationDetails;
	}

	public MultipartFile getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public List<List3> getRequestedDetails() {
		return requestedDetails;
	}

	public void setRequestedDetails(List<List3> requestedDetails) {
		this.requestedDetails = requestedDetails;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public List<List1> getRequestedAttachmentList() {
		return requestedAttachmentList;
	}

	public void setRequestedAttachmentList(List<List1> requestedAttachmentList) {
		this.requestedAttachmentList = requestedAttachmentList;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	
}
