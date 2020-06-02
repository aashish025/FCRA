package service.masters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.master.OfficeFacilityType;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.OfficeFacilityTypeDao;

public class OfficeFacilityTypeService extends Commons {
	private Integer facilityId;
	private String facilityDesc;
	private String createdBy;
	private String createdIp;
	private Date createdDate;
	private Boolean recordStatus;
	private String lastModifiedBy;
	private String lastModifiedIp;
	private Timestamp lastModifiedDate;
	
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	
	private List<List2> requestedDetails;
	private Map<String,String> parameterMap;
	private List<OfficeFacilityType> officeFacilityTypeList;
	
	public Integer getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(Integer facilityId) {
		this.facilityId = facilityId;
	}
	public String getFacilityDesc() {
		return facilityDesc;
	}
	public void setFacilityDesc(String facilityDesc) {
		this.facilityDesc = facilityDesc;
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
	public Boolean getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Boolean recordStatus) {
		this.recordStatus = recordStatus;
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
	public List<OfficeFacilityType> getOfficeFacilityTypeList() {
		return officeFacilityTypeList;
	}
	public void setOfficeFacilityTypeList(
			List<OfficeFacilityType> officeFacilityTypeList) {
		this.officeFacilityTypeList = officeFacilityTypeList;
	}
	public String pullOfficeFacilityTypeList() {
		String ret = "success";
		begin();
		try {
			populateOfficeFacilityTypeList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;
		
	}
	
	private void populateOfficeFacilityTypeList() throws Exception {
		OfficeFacilityTypeDao officeFacilityTypeDao = new OfficeFacilityTypeDao(connection);
		officeFacilityTypeDao.setPageNum(pageNum);
		officeFacilityTypeDao.setRecordsPerPage(recordsPerPage);
		officeFacilityTypeDao.setSortColumn(sortColumn);
		officeFacilityTypeDao.setSortOrder(sortOrder);		
		officeFacilityTypeList = officeFacilityTypeDao.getAliveRecords(myOffice);
		totalRecords = officeFacilityTypeDao.getTotalRecords();
	}
	
	public void populateVisibleOfficeFacilityTypeList() throws Exception {
		begin();
		try {
			OfficeFacilityTypeDao officeFacilityTypeDao = new OfficeFacilityTypeDao(connection);
			officeFacilityTypeList = officeFacilityTypeDao.getVisibleAliveRecords(myOffice);
		}
		catch(Exception e){
			try {
				if(connection != null)
					connection.rollback();
			}catch(SQLException e2){
				ps(e2);
			}
			ps(e);
		}
		finish();
	}
	
	public String submit(String officeFacilityDesc) {
		begin();
		try {
			assignParameters();
			validateParameters();
			if(facilityId == null || facilityId.equals("")) {
				insertOfficeFacilityType(officeFacilityDesc);
			}
			else {
				updateOfficeFacilityType();
			}			
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
	
	private void assignParameters() throws Exception{
		if(parameterMap != null) {			
		}
	}
	
	private void validateParameters() throws Exception {
		
	}
	
	private void insertOfficeFacilityType(String officeFacilityDesc) throws Exception {
		int id = generateOfficeFacilityTypeId();
		saveOfficeFacilityType(id, officeFacilityDesc);
	}
	
	private int generateOfficeFacilityTypeId() throws Exception {
		int officeFacilityTypeId = 0;
		StringBuffer query = new StringBuffer("SELECT max(facility_id) from tm_office_facilities");
		PreparedStatement pStmt = connection.prepareStatement(query.toString());
		ResultSet rs = pStmt.executeQuery();
		while(rs.next()) {
			officeFacilityTypeId = rs.getInt(1);
		}
		return ++officeFacilityTypeId;
	}
	
	private String saveOfficeFacilityType(int officeFacilityTypeId, String officeFacilityDesc) throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("OfficeFacilityType", officeFacilityDesc, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Office Facility Type Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		OfficeFacilityType officeFacilityType = new OfficeFacilityType();		
		officeFacilityType.setFacilityId(officeFacilityTypeId);
		officeFacilityType.setFacilityDesc(officeFacilityDesc);		
		officeFacilityType.setCreatedBy(myUserId);
		officeFacilityType.setCreatedIp(myIpAddress);
		officeFacilityType.setLastModifiedBy(myUserId);
		officeFacilityType.setLastModifiedIp(myIpAddress);		
		OfficeFacilityTypeDao officeFacilityTypeDao = new OfficeFacilityTypeDao(connection);
		int record = officeFacilityTypeDao.insertRecord(officeFacilityType);	
		if(record > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Office Facility type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}
	
	private void updateOfficeFacilityType() throws Exception {
		if(facilityId == null || facilityId.equals(""))
			throw new Exception("Invalid Notification Type Id");		
	}
	
	public String initGetOfficeFacilityType(String officeFacilityType) {
		begin();
		try {
				getOfficeFacilityType(officeFacilityType);
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void getOfficeFacilityType(String officeFacilityType) throws Exception{		
		OfficeFacilityTypeDao officeFacilityTypeDao = new OfficeFacilityTypeDao(connection);
		requestedDetails = officeFacilityTypeDao.getOfficeFacilityType(officeFacilityType);
	}
	
	public String editOfficeFacilityType(String officeFacilityTypeId, String officeFacilityDesc) throws Exception {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("OfficeFacilityType", officeFacilityDesc, "WordS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Office Facility Type Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			OfficeFacilityType officeFacilityType = new OfficeFacilityType();
			
				officeFacilityType.setFacilityId(Integer
					.parseInt(officeFacilityTypeId));
				officeFacilityType.setFacilityDesc(officeFacilityDesc);
				OfficeFacilityTypeDao officeFacilityTypeDao = new OfficeFacilityTypeDao(
					connection);
				int record = officeFacilityTypeDao.editRecord(officeFacilityType);
				if (record > 0) {
					notifyList.add(new utilities.notifications.Notification(
						"Success!!",
						"Office Facility type is Edited successfully.",
						Status.SUCCESS, Type.BAR));
				}
			
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteOfficeFacilityType(String officeFacilityTypeId) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			OfficeFacilityType officeFacilityType = new OfficeFacilityType();
			officeFacilityType.setFacilityId(Integer
					.parseInt(officeFacilityTypeId));
			OfficeFacilityTypeDao officeFacilityTypeDao = new OfficeFacilityTypeDao(
					connection);
			int result = officeFacilityTypeDao.removeRecord(officeFacilityType);
			if (result > 0) {
				notifyList.add(new utilities.notifications.Notification(
						"Success!!",
						"Office Facility type is deleted successfully.",
						Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}