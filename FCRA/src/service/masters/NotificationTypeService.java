package service.masters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.owasp.esapi.ESAPI;

import models.master.NoticeBoard;
import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.NotificationTypeDao;

public class NotificationTypeService extends Commons {
	private Short notificationTypeId;
	private String notificationName;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private Date lastModifiedDate;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private List<List2> requestedDetails;
	private Map<String, String> parameterMap;

	public List<List2> getRequestedDetails() {
		return requestedDetails;
	}

	public void setRequestedDetails(List<List2> requestedDetails) {
		this.requestedDetails = requestedDetails;
	}

	public Map<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	private List<NoticeBoard> notificationTypeList;

	public List<NoticeBoard> getNotificationTypeList() {
		return notificationTypeList;
	}

	public void setNotificationTypeList(List<NoticeBoard> notificationTypeList) {
		this.notificationTypeList = notificationTypeList;
	}

	public Short getNotificationTypeId() {
		return notificationTypeId;
	}

	public void setNotificationTypeId(Short notificationTypeId) {
		this.notificationTypeId = notificationTypeId;
	}

	public String getNotificationName() {
		return notificationName;
	}

	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

	public Boolean getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedIp() {
		return createdIp;
	}

	public void setCreatedIp(String createdIp) {
		this.createdIp = createdIp;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedIp() {
		return lastModifiedIp;
	}

	public void setLastModifiedIp(String lastModifiedIp) {
		this.lastModifiedIp = lastModifiedIp;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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

	public String pullNotificationTypeList() {
		String ret = "success";
		begin();
		try {
			populateNotificationTypeList();
		} catch (Exception e) {
			ps(e);
			ret = "error";
		}
		finish();
		return ret;
	}

	private void populateNotificationTypeList() throws Exception {
		NotificationTypeDao notificationTypeDao = new NotificationTypeDao(
				connection);
		notificationTypeDao.setPageNum(pageNum);
		notificationTypeDao.setRecordsPerPage(recordsPerPage);
		notificationTypeDao.setSortColumn(sortColumn);
		notificationTypeDao.setSortOrder(sortOrder);
		notificationTypeList = notificationTypeDao.getAliveRecords(myOffice);
		totalRecords = notificationTypeDao.getTotalRecords();
	}

	public String submit(String notificationTypeName) {
		begin();
		try {
			assignParameters();
			validateParameters();
			if (notificationTypeId == null || notificationTypeId.equals("")) {
				insertNotificationType(notificationTypeName);
			} else {
				updateNotificationType();
			}
		} catch (Exception e) {
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException e2) {
				ps(e2);
			}
			ps(e);
		}
		finish();
		return "success";
	}

	private void assignParameters() throws Exception {
		if (parameterMap != null) {
		}
	}

	private void validateParameters() throws Exception {

	}

	private void updateNotificationType() throws Exception {
		if (notificationTypeId == null || notificationTypeId.equals(""))
			throw new Exception("Invalid Notification Type Id");
	}

	private void insertNotificationType(String notificationTypeName)
			throws Exception {
		Short id = generateNotificationTypeId();
		saveNotificationType(id, notificationTypeName);
	}

	private Short generateNotificationTypeId() throws Exception {
		Short notificationTypeId = 0;
		StringBuffer query = new StringBuffer(
				"SELECT max(notification_type_id) from tm_notification_type");
		PreparedStatement pStmt = connection.prepareStatement(query.toString());
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			notificationTypeId = rs.getShort(1);
		}
		return ++notificationTypeId;
	}

	private String saveNotificationType(Short notificationTypeId,
			String notificationTypeName) throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("NoticeBoard", notificationTypeName, "WordS", 25, false) == false){
			notifyList.add(new Notification("Error!!", "Notification Type Name - Only alphabets and numbers and space allowed (25 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		NoticeBoard noticeBoard = new NoticeBoard();
		noticeBoard.setNotificationType(notificationTypeId);
		noticeBoard.setNotificationName(notificationTypeName);
		noticeBoard.setCreatedBy(myUserId);
		noticeBoard.setCreatedIp(myIpAddress);
		noticeBoard.setLastModifiedBy(myUserId);
		noticeBoard.setLastModifiedIp(myIpAddress);
		NotificationTypeDao notificationTypeDao = new NotificationTypeDao(
				connection);
		int status = notificationTypeDao.insertRecord(noticeBoard);
		if(status > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Notification type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}

	public String initGetNotificationType(String notificationType) {
		begin();
		try {
			getNotificationType(notificationType);
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}

	private void getNotificationType(String notificationType) throws Exception {
		NotificationTypeDao notificationTypeDao = new NotificationTypeDao(
				connection);
		requestedDetails = notificationTypeDao
				.getNotificationType(notificationType);
	}

	public String editNotificationType(String notificationTypeId,
			String notificationTypeName) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("NoticeBoard", notificationTypeName, "WordS", 25, false) == false){
				notifyList.add(new Notification("Error!!", "Notification Type Name - Only alphabets and numbers and space allowed (25 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			NoticeBoard noticeBoard = new NoticeBoard();
			noticeBoard.setNotificationType(Short
					.parseShort(notificationTypeId));
			noticeBoard.setNotificationName(notificationTypeName);
			NotificationTypeDao notificationTypeDao = new NotificationTypeDao(
					connection);
			int status = notificationTypeDao.editRecord(noticeBoard);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Notification type is edited successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}

	public void deleteNotificationType(String notificationTypeId) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			Short notificationType = Short.parseShort(notificationTypeId);
			NoticeBoard noticeBoard = new NoticeBoard();
			noticeBoard.setNotificationType(notificationType);
			NotificationTypeDao notificationTypeDao = new NotificationTypeDao(
					connection);
			int status = notificationTypeDao.removeRecord(noticeBoard);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Notification type is deleted successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}