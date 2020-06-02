package service.masters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.Country;
import models.master.TimeZone;
import utilities.Commons;
import utilities.lists.List3;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.TimeZoneTypeDao;
import utilities.notifications.Notification;

public class TimeZoneTypeService extends Commons {
	private Short zoneId;
	private Country country;
	private String countryName;
	private String zoneName;
	private Short displayOrder;
	private Boolean recordStatus;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private Timestamp lastModifiedDate;	
	private List<List3> requestedDetails;
	
	public List<List3> getRequestedDetails() {
		return requestedDetails;
	}
	public void setRequestedDetails(List<List3> requestedDetails) {
		this.requestedDetails = requestedDetails;
	}
	public Short getZoneId() {
		return zoneId;
	}
	public void setZoneId(Short zoneId) {
		this.zoneId = zoneId;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public Short getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
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
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private List<TimeZone> timeZoneTypeList;
	
	public List<TimeZone> getTimeZoneTypeList() {
		return timeZoneTypeList;
	}
	public void setTimeZoneTypeList(List<TimeZone> timeZoneTypeList) {
		this.timeZoneTypeList = timeZoneTypeList;
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
	
	public String pullTimeZoneTypeList() {
		String ret = "success";
		begin();
		try {
			populateTimeZoneTypeList();
		} catch(Exception e){
			ps(e);
			e.getMessage();
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	private void populateTimeZoneTypeList() throws Exception {
		TimeZoneTypeDao timeZoneTypeDao = new TimeZoneTypeDao(connection);
		timeZoneTypeDao.setPageNum(pageNum);
		timeZoneTypeDao.setRecordsPerPage(recordsPerPage);
		timeZoneTypeDao.setSortColumn(sortColumn);
		timeZoneTypeDao.setSortOrder(sortOrder);		
		timeZoneTypeList = timeZoneTypeDao.getAliveRecords(myOffice);
		totalRecords = timeZoneTypeDao.getTotalRecords();
	}
	
	public String submit(String country, String zoneName) {
		begin();
		try {
			insertTimeZoneType(country, zoneName);
		} catch(Exception e){
			try {
				if(connection != null)
					connection.rollback();
			}catch(SQLException e2){
				ps(e2);
			}
			ps(e);
		}
		finish();
		return "success";		
	}
	
	private void insertTimeZoneType(String country, String zoneName) throws Exception {
		Short zoneId = generateTimeZoneTypeId();
		saveTimeZoneType(zoneId, country, zoneName);
	}
	
	private Short generateTimeZoneTypeId() throws Exception {
		Short timeZoneTypeId = 0;
		StringBuffer query = new StringBuffer("SELECT max(zone_id) from tm_timezone");
		PreparedStatement pStmt = connection.prepareStatement(query.toString());
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()) {
			timeZoneTypeId = rs.getShort(1);
		}
		return ++timeZoneTypeId;
	}
	
	private String saveTimeZoneType(Short zoneId, String country, String zoneName) throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("TimeZone", zoneName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "TimeZone Type Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		TimeZone timeZone = new TimeZone();
		timeZone.setZoneId(zoneId);
		timeZone.setCountryName(country);
		timeZone.setZoneName(zoneName);
			
		timeZone.setCreatedBy(myUserId);
		timeZone.setCreatedIp(myIpAddress);
		timeZone.setLastModifiedBy(myUserId);
		timeZone.setLastModifiedIp(myIpAddress);
		TimeZoneTypeDao timeZoneTypeDao = new TimeZoneTypeDao(
				connection);
		int record = timeZoneTypeDao.insertRecord(timeZone);
		if(record > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Time Zone Type is inserted successfully", Status.SUCCESS, Type.BAR));
		}
		return "success";
			
	}
	
	public String initGetTimeZoneType(String timeZoneType) {
		begin();
		try {
				getTimeZoneType(timeZoneType);
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void getTimeZoneType(String timeZoneType) throws Exception{		
		TimeZoneTypeDao timeZoneTypeDao = new TimeZoneTypeDao(connection);
		requestedDetails = timeZoneTypeDao.getTimeZoneType(timeZoneType);
	}
	
	public String editTimeZoneType(String country, String zoneName, String timeZoneTypeId) throws Exception {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("TimeZone", zoneName, "WordS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "TimeZone Type Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			TimeZone timeZone = new TimeZone();
			
			timeZone.setZoneId(Short.parseShort(timeZoneTypeId));
			timeZone.setCountryName(country);
			timeZone.setZoneName(zoneName);
			TimeZoneTypeDao timeZoneTypeDao = new TimeZoneTypeDao(
					connection);
			int record = timeZoneTypeDao.editRecord(timeZone);
			if (record > 0) {
					notifyList.add(new utilities.notifications.Notification(
						"Success!!",
						"Time Zone type is Edited successfully.",
						Status.SUCCESS, Type.BAR));
				}
			
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteTimeZoneType(String timeZoneTypeId) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			TimeZone timeZone = new TimeZone();
			timeZone.setZoneId(Short.parseShort(timeZoneTypeId));
			TimeZoneTypeDao timeZoneTypeDao = new TimeZoneTypeDao(
					connection);
			int result = timeZoneTypeDao.removeRecord(timeZone);
			if (result > 0) {
				notifyList.add(new utilities.notifications.Notification(
						"Success!!",
						"Time Zone type is deleted successfully.",
						Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}
