package models.services.requests;

import java.sql.Connection;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class AbstractRequest {
	protected String applicationId = null;
	protected String tempFileNo = null;
	protected String sectionFileNo = null;
	protected String applicantName=null;
	protected String tempApplicationId = null;
	protected String sectionFileNumber = null;
	protected String documentId = null;
	protected String officeCode = null;
	protected String officeId = null;
	protected String serviceId = null;
	protected String serviceName=null;	
	protected String currentStage = null;
	protected String currentStatus = null;
	protected String currentStatusDesc = null;
	protected String currentStageDesc = null;
	protected String currentSubStage = null;
	protected String currentSubStageDesc =null;
	protected String currentSubStageStatus = null;
	protected String pendingWithOffice = null;
	protected String pendingWithUser = null;
	protected String selectedStatus = null;
	protected String toOffice = null;
	protected String toOfficeInfo=null;
	protected String toUser = null;
	protected String userId=null;
	protected String userDesignationId=null;
	protected String remark = null;
	protected String submissionDate=null;
	protected String creationDate=null;
	protected String state=null;
	protected String section=null;
	protected String service=null;
	protected String officeUser=null;
	protected String docRoot=null;
	protected String assoRegnNumber=null;
	protected String assoRegnDate=null;
	protected String assoRegnStatus=null;
	protected String assoRegnStatusDesc=null;
	protected String assoAccNumber=null;
	protected String emailId=null;
	protected String mobile=null;	
	protected String district=null;
	protected String lastAccessedBy=null;
	protected String lastAccessedOn=null;
	protected String deletedDocRoot=null;
	protected String docUploadFolder=null;
	protected String deletedDocFolder=null;
	
	
	
	protected Connection connection = null;
	
	public AbstractRequest(Connection connection) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		docRoot="/FCRA/docs";
		deletedDocRoot="/FCRA/previousdocs";
		docUploadFolder = "/image/FCRAdocs/";
		deletedDocFolder = "/Deleted";
		this.connection = connection;
	}
	
	
	
	public abstract void getDetails() throws Exception;
	public abstract void saveDetails() throws Exception;
	public abstract void forward() throws Exception;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getTempApplicationId() {
		return tempApplicationId;
	}



	public void setTempApplicationId(String tempApplicationId) {
		this.tempApplicationId = tempApplicationId;
	}



	public String getSectionFileNumber() {
		return sectionFileNumber;
	}



	public void setSectionFileNumber(String sectionFileNumber) {
		this.sectionFileNumber = sectionFileNumber;
	}



	public String getDocumentId() {
		return documentId;
	}



	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}



	public String getOfficeCode() {
		return officeCode;
	}



	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}



	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCurrentStatusDesc() {
		return currentStatusDesc;
	}



	public void setCurrentStatusDesc(String currentStatusDesc) {
		this.currentStatusDesc = currentStatusDesc;
	}



	public String getPendingWithOffice() {
		return pendingWithOffice;
	}

	public void setPendingWithOffice(String pendingWithOffice) {
		this.pendingWithOffice = pendingWithOffice;
	}

	public String getPendingWithUser() {
		return pendingWithUser;
	}

	public void setPendingWithUser(String pendingWithUser) {
		this.pendingWithUser = pendingWithUser;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getToOffice() {
		return toOffice;
	}

	public void setToOffice(String toOffice) {
		this.toOffice = toOffice;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getDocRoot() {
		return docRoot;
	}



	public void setDocRoot(String docRoot) {
		this.docRoot = docRoot;
	}



	public String getSubmissionDate() {
		return submissionDate;
	}



	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}



	public String getCurrentSubStage() {
		return currentSubStage;
	}



	public void setCurrentSubStage(String currentSubStage) {
		this.currentSubStage = currentSubStage;
	}



	public String getCurrentSubStageStatus() {
		return currentSubStageStatus;
	}



	public void setCurrentSubStageStatus(String currentSubStageStatus) {
		this.currentSubStageStatus = currentSubStageStatus;
	}



	public String getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}



	public String getServiceName() {
		return serviceName;
	}



	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}



	public String getToOfficeInfo() {
		return toOfficeInfo;
	}



	public void setToOfficeInfo(String toOfficeInfo) {
		this.toOfficeInfo = toOfficeInfo;
	}



	public String getCurrentSubStageDesc() {
		return currentSubStageDesc;
	}



	public void setCurrentSubStageDesc(String currentSubStageDesc) {
		this.currentSubStageDesc = currentSubStageDesc;
	}




	public String getCurrentStageDesc() {
		return currentStageDesc;
	}



	public void setCurrentStageDesc(String currentStageDesc) {
		this.currentStageDesc = currentStageDesc;
	}



	public String getOfficeId() {
		return officeId;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getSection() {
		return section;
	}



	public void setSection(String section) {
		this.section = section;
	}



	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}



	public String getService() {
		return service;
	}



	public void setService(String service) {
		this.service = service;
	}



	public String getApplicantName() {
		return applicantName;
	}



	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}



	public String getTempFileNo() {
		return tempFileNo;
	}



	public void setTempFileNo(String tempFileNo) {
		this.tempFileNo = tempFileNo;
	}



	public String getSectionFileNo() {
		return sectionFileNo;
	}



	public void setSectionFileNo(String sectionFileNo) {
		this.sectionFileNo = sectionFileNo;
	}



	public String getAssoRegnStatus() {
		return assoRegnStatus;
	}



	public void setAssoRegnStatus(String assoRegnStatus) {
		this.assoRegnStatus = assoRegnStatus;
	}



	public String getAssoRegnNumber() {
		return assoRegnNumber;
	}



	public void setAssoRegnNumber(String assoRegnNumber) {
		this.assoRegnNumber = assoRegnNumber;
	}



	public String getAssoRegnStatusDesc() {
		return assoRegnStatusDesc;
	}



	public void setAssoRegnStatusDesc(String assoRegnStatusDesc) {
		this.assoRegnStatusDesc = assoRegnStatusDesc;
	}



	public String getAssoRegnDate() {
		return assoRegnDate;
	}



	public void setAssoRegnDate(String assoRegnDate) {
		this.assoRegnDate = assoRegnDate;
	}



	public String getAssoAccNumber() {
		return assoAccNumber;
	}



	public void setAssoAccNumber(String assoAccNumber) {
		this.assoAccNumber = assoAccNumber;
	}



	public String getEmailId() {
		return emailId;
	}



	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}



	public String getMobile() {
		return mobile;
	}



	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public String getDistrict() {
		return district;
	}



	public void setDistrict(String district) {
		this.district = district;
	}



	public String getLastAccessedBy() {
		return lastAccessedBy;
	}



	public void setLastAccessedBy(String lastAccessedBy) {
		this.lastAccessedBy = lastAccessedBy;
	}



	public String getDeletedDocRoot() {
		return deletedDocRoot;
	}



	public void setDeletedDocRoot(String deletedDocRoot) {
		this.deletedDocRoot = deletedDocRoot;
	}



	public String getDocUploadFolder() {
		return docUploadFolder;
	}



	public void setDocUploadFolder(String docUploadFolder) {
		this.docUploadFolder = docUploadFolder;
	}



	public String getDeletedDocFolder() {
		return deletedDocFolder;
	}



	public void setDeletedDocFolder(String deletedDocFolder) {
		this.deletedDocFolder = deletedDocFolder;
	}



	public String getUserDesignationId() {
		return userDesignationId;
	}



	public void setUserDesignationId(String userDesignationId) {
		this.userDesignationId = userDesignationId;
	}



	public String getLastAccessedOn() {
		return lastAccessedOn;
	}



	public void setLastAccessedOn(String lastAccessedOn) {
		this.lastAccessedOn = lastAccessedOn;
	}



	public String getOfficeUser() {
		return officeUser;
	}



	public void setOfficeUser(String officeUser) {
		this.officeUser = officeUser;
	}
}
