package models.services.requests;


import javax.validation.constraints.Size;

public class ComplainRegisterBean {
private String complaintId;
@Size(min=1,message="required")  
private String complaintSubject;
@Size(min=1,message="required")  
private String complaintCategory;
private String complaintRaisedBy;
@Size(min=1,message="required")  
private String complaintSentDescription;
private String sentMacAddress;
private String sentBy;
private String sentOn;
private String sentDate;
private String complaintRepliedDescription;
private String repliedMacAddress;
private String attachedFile;
private String repliedBy;
private String attached;
private String repliedOn;
private String repliedDate;
private String complaintStatus;
private String complaintMenuName;
private String complaintSuggestions;
private String complaintAttachmentStatus;
private String recordStatus;
public ComplainRegisterBean(String complaintId, String complaintSubject, String complaintCategory,
		String complaintRaisedBy, String complaintSentDescription, String sentMacAddress, String sentBy, String sentOn,
		String sentDate, String complaintRepliedDescription, String repliedMacAddress, String attachedFile,
		String repliedBy, String attached, String repliedOn, String repliedDate, String complaintStatus,
		String complaintMenuName, String complaintSuggestions, String complaintAttachmentStatus, String recordStatus) {
	super();
	this.complaintId = complaintId;
	this.complaintSubject = complaintSubject;
	this.complaintCategory = complaintCategory;
	this.complaintRaisedBy = complaintRaisedBy;
	this.complaintSentDescription = complaintSentDescription;
	this.sentMacAddress = sentMacAddress;
	this.sentBy = sentBy;
	this.sentOn = sentOn;
	this.sentDate = sentDate;
	this.complaintRepliedDescription = complaintRepliedDescription;
	this.repliedMacAddress = repliedMacAddress;
	this.attachedFile = attachedFile;
	this.repliedBy = repliedBy;
	this.attached = attached;
	this.repliedOn = repliedOn;
	this.repliedDate = repliedDate;
	this.complaintStatus = complaintStatus;
	this.complaintMenuName = complaintMenuName;
	this.complaintSuggestions = complaintSuggestions;
	this.complaintAttachmentStatus = complaintAttachmentStatus;
	this.recordStatus = recordStatus;
}
public ComplainRegisterBean() {
	super();
	// TODO Auto-generated constructor stub
}
public String getComplaintId() {
	return complaintId;
}
public void setComplaintId(String complaintId) {
	this.complaintId = complaintId;
}
public String getComplaintSubject() {
	return complaintSubject;
}
public void setComplaintSubject(String complaintSubject) {
	this.complaintSubject = complaintSubject;
}
public String getComplaintCategory() {
	return complaintCategory;
}
public void setComplaintCategory(String complaintCategory) {
	this.complaintCategory = complaintCategory;
}
public String getComplaintRaisedBy() {
	return complaintRaisedBy;
}
public void setComplaintRaisedBy(String complaintRaisedBy) {
	this.complaintRaisedBy = complaintRaisedBy;
}
public String getComplaintSentDescription() {
	return complaintSentDescription;
}
public void setComplaintSentDescription(String complaintSentDescription) {
	this.complaintSentDescription = complaintSentDescription;
}
public String getSentMacAddress() {
	return sentMacAddress;
}
public void setSentMacAddress(String sentMacAddress) {
	this.sentMacAddress = sentMacAddress;
}
public String getSentBy() {
	return sentBy;
}
public void setSentBy(String sentBy) {
	this.sentBy = sentBy;
}
public String getSentOn() {
	return sentOn;
}
public void setSentOn(String sentOn) {
	this.sentOn = sentOn;
}
public String getSentDate() {
	return sentDate;
}
public void setSentDate(String sentDate) {
	this.sentDate = sentDate;
}
public String getComplaintRepliedDescription() {
	return complaintRepliedDescription;
}
public void setComplaintRepliedDescription(String complaintRepliedDescription) {
	this.complaintRepliedDescription = complaintRepliedDescription;
}
public String getRepliedMacAddress() {
	return repliedMacAddress;
}
public void setRepliedMacAddress(String repliedMacAddress) {
	this.repliedMacAddress = repliedMacAddress;
}
public String getAttachedFile() {
	return attachedFile;
}
public void setAttachedFile(String attachedFile) {
	this.attachedFile = attachedFile;
}
public String getRepliedBy() {
	return repliedBy;
}
public void setRepliedBy(String repliedBy) {
	this.repliedBy = repliedBy;
}
public String getAttached() {
	return attached;
}
public void setAttached(String attached) {
	this.attached = attached;
}
public String getRepliedOn() {
	return repliedOn;
}
public void setRepliedOn(String repliedOn) {
	this.repliedOn = repliedOn;
}
public String getRepliedDate() {
	return repliedDate;
}
public void setRepliedDate(String repliedDate) {
	this.repliedDate = repliedDate;
}
public String getComplaintStatus() {
	return complaintStatus;
}
public void setComplaintStatus(String complaintStatus) {
	this.complaintStatus = complaintStatus;
}
public String getComplaintMenuName() {
	return complaintMenuName;
}
public void setComplaintMenuName(String complaintMenuName) {
	this.complaintMenuName = complaintMenuName;
}
public String getComplaintSuggestions() {
	return complaintSuggestions;
}
public void setComplaintSuggestions(String complaintSuggestions) {
	this.complaintSuggestions = complaintSuggestions;
}
public String getComplaintAttachmentStatus() {
	return complaintAttachmentStatus;
}
public void setComplaintAttachmentStatus(String complaintAttachmentStatus) {
	this.complaintAttachmentStatus = complaintAttachmentStatus;
}
public String getRecordStatus() {
	return recordStatus;
}
public void setRecordStatus(String recordStatus) {
	this.recordStatus = recordStatus;
}

}