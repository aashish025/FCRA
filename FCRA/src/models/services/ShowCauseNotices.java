package models.services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ShowCauseNotices {
 private String scnId;
 private String grievanceId;
 private String chatId;
 private String noticeBody;
 private String noticeSubject;
 private String generatedDate;
 private String generatedBy;
 
 
 public ShowCauseNotices() {
		
		// TODO Auto-generated constructor stub
	}

public String getScnId() {
	return scnId;
}
public void setScnId(String scnId) {
	this.scnId = scnId;
}
public String getGrievanceId() {
	return grievanceId;
}
public void setGrievanceId(String grievanceId) {
	this.grievanceId = grievanceId;
}
public String getChatId() {
	return chatId;
}
public void setChatId(String chatId) {
	this.chatId = chatId;
}
public String getNoticeBody() {
	return noticeBody;
}
public void setNoticeBody(String noticeBody) {
	this.noticeBody = noticeBody;
}
public String getGeneratedDate() {
	return generatedDate;
}
public void setGeneratedDate(String generatedDate) {
	this.generatedDate = generatedDate;
}
public String getGeneratedBy() {
	return generatedBy;
}
public void setGeneratedBy(String generatedBy) {
	this.generatedBy = generatedBy;
}
public List<ShowCauseNotices> getShowCauseNoticeList() {
	// TODO Auto-generated method stub
	return null;
}

public String getNoticeSubject() {
	return noticeSubject;
}

public void setNoticeSubject(String noticeSubject) {
	this.noticeSubject = noticeSubject;
}

	
	
}
