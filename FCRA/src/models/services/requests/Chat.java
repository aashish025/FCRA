package models.services.requests;

import org.springframework.web.multipart.MultipartFile;

public class Chat {
	private String applicationId=null;
	private String fromOfficeCode=null;
	private String fromOfficeId=null;
	private String fromOffice=null;
	private String fromUserId=null;
	private String fromUserName=null;
	private String fromUserDesignation=null;
	private String toOfficeCode=null;
	private String toOfficeId=null;
	private String toOffice=null;
	private String toUserId=null;
	private String toUserName=null;
	private String toUserDesignation=null;
	private String statusId=null;
	private String statusRemark=null;
	private String statusDate=null;
	private String recordStatus=null;
	private String subStageId=null;
	private String subStageDesc=null;
	private String nextStageId=null;
	private MultipartFile[] stageFiles=null;
	private String[] stageFilesIds=null;
	private String sessionId=null;
	private String chatId=null;
	private String attachmentName=null;
	private String rowId=null;
	private String statusOffice=null;
	private String statusName=null;
	private String sectionId=null;
	private String noticeSubject;
	private String noticeBody;
	private String otherRemark=null;
	public Chat(){}
	
	public Chat(String applicationId,String subStageId,String subStageDesc){
		this.applicationId=applicationId;
		this.subStageId=subStageId;
		this.subStageDesc=subStageDesc;		
	}
	
	public Chat(String statusOffice,String statusName){
		this.statusOffice=statusOffice;
		this.statusName=statusName;				
	}
	
	
	public Chat(String applicationId,String chatId,String attachmentName,String rowId){
		this.applicationId=applicationId;
		this.chatId=chatId;
		this.attachmentName=attachmentName;
		this.rowId=rowId;		
	}
	
	public Chat(String fromOfficeCode,
			String fromOfficeId, String fromOffice, String fromUserId,
			String fromUserName, String fromUserDesignation,
			String toOfficeCode, String toOfficeId, String toOffice,
			String toUserId, String toUserName, String toUserDesignation,
			String statusRemark, String statusDate,String chatId,String statusId) {	
		this.fromOfficeCode = fromOfficeCode;
		this.fromOfficeId = fromOfficeId;
		this.fromOffice = fromOffice;
		this.fromUserId = fromUserId;
		this.fromUserName = fromUserName;
		this.fromUserDesignation = fromUserDesignation;
		this.toOfficeCode = toOfficeCode;
		this.toOfficeId = toOfficeId;
		this.toOffice = toOffice;
		this.toUserId = toUserId;
		this.toUserName = toUserName;
		this.toUserDesignation = toUserDesignation;
		this.statusRemark = statusRemark;
		this.statusDate = statusDate;		
		this.chatId=chatId;		
		this.statusId=statusId;
	}
	
	
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getFromOfficeCode() {
		return fromOfficeCode;
	}
	public void setFromOfficeCode(String fromOfficeCode) {
		this.fromOfficeCode = fromOfficeCode;
	}
	public String getFromOfficeId() {
		return fromOfficeId;
	}
	public void setFromOfficeId(String fromOfficeId) {
		this.fromOfficeId = fromOfficeId;
	}
	public String getFromOffice() {
		return fromOffice;
	}
	public void setFromOffice(String fromOffice) {
		this.fromOffice = fromOffice;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getFromUserDesignation() {
		return fromUserDesignation;
	}
	public void setFromUserDesignation(String fromUserDesignation) {
		this.fromUserDesignation = fromUserDesignation;
	}
	public String getToOfficeCode() {
		return toOfficeCode;
	}
	public void setToOfficeCode(String toOfficeCode) {
		this.toOfficeCode = toOfficeCode;
	}
	public String getToOfficeId() {
		return toOfficeId;
	}
	public void setToOfficeId(String toOfficeId) {
		this.toOfficeId = toOfficeId;
	}
	public String getToOffice() {
		return toOffice;
	}
	public void setToOffice(String toOffice) {
		this.toOffice = toOffice;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getToUserDesignation() {
		return toUserDesignation;
	}
	public void setToUserDesignation(String toUserDesignation) {
		this.toUserDesignation = toUserDesignation;
	}
	public String getStatusRemark() {
		return statusRemark;
	}
	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}


	public String getStatusId() {
		return statusId;
	}


	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}


	public String getRecordStatus() {
		return recordStatus;
	}


	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}


	public MultipartFile[] getStageFiles() {
		return stageFiles;
	}


	public void setStageFiles(MultipartFile[] stageFiles) {
		this.stageFiles = stageFiles;
	}


	public String[] getStageFilesIds() {
		return stageFilesIds;
	}


	public void setStageFilesIds(String[] stageFilesIds) {
		this.stageFilesIds = stageFilesIds;
	}


	public String getSubStageDesc() {
		return subStageDesc;
	}


	public void setSubStageDesc(String subStageDesc) {
		this.subStageDesc = subStageDesc;
	}


	public String getSubStageId() {
		return subStageId;
	}


	public void setSubStageId(String subStageId) {
		this.subStageId = subStageId;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public String getChatId() {
		return chatId;
	}


	public void setChatId(String chatId) {
		this.chatId = chatId;
	}


	public String getAttachmentName() {
		return attachmentName;
	}


	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}


	public String getRowId() {
		return rowId;
	}


	public void setRowId(String rowId) {
		this.rowId = rowId;
	}


	public String getStatusOffice() {
		return statusOffice;
	}


	public void setStatusOffice(String statusOffice) {
		this.statusOffice = statusOffice;
	}


	public String getStatusName() {
		return statusName;
	}


	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	public String getNextStageId() {
		return nextStageId;
	}


	public void setNextStageId(String nextStageId) {
		this.nextStageId = nextStageId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getNoticeSubject() {
		return noticeSubject;
	}

	public void setNoticeSubject(String noticeSubject) {
		this.noticeSubject = noticeSubject;
	}

	public String getNoticeBody() {
		return noticeBody;
	}

	public void setNoticeBody(String noticeBody) {
		this.noticeBody = noticeBody;
	}

	public String getOtherRemark() {
		return otherRemark;
	}

	public void setOtherRemark(String otherRemark) {
		this.otherRemark = otherRemark;
	}
	
}

