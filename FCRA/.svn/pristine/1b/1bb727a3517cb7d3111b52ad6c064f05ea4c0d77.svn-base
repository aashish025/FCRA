package service.masters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import models.master.CommitteeDesignationType;
import models.master.OccupationType;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.CommitteeDesignationTypeDao;
import dao.master.OccupationTypeDao;

public class OccupationTypeService extends Commons {
	private Integer occupationCode;
	private String occupationName;
	private String createdBy;
	private String createdIp;
	private String createdDate;
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
	private List<OccupationType> occupationTypeList;
	
	public Integer getOccupationCode() {
		return occupationCode;
	}
	public void setOccupationCode(Integer occupationCode) {
		this.occupationCode = occupationCode;
	}
	public String getOccupationName() {
		return occupationName;
	}
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
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
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
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
	public List<OccupationType> getOccupationTypeList() {
		return occupationTypeList;
	}
	public void setOccupationTypeList(List<OccupationType> occupationTypeList) {
		this.occupationTypeList = occupationTypeList;
	}
	
	public String pullOccupationTypeList() {
		String ret = "success";
		begin();
		try {
			populateOccupationTypeList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	private void populateOccupationTypeList() throws Exception {
		OccupationTypeDao occupationTypeDao = new OccupationTypeDao(connection);
		occupationTypeDao.setPageNum(pageNum);
		occupationTypeDao.setRecordsPerPage(recordsPerPage);
		occupationTypeDao.setSortColumn(sortColumn);
		occupationTypeDao.setSortOrder(sortOrder);		
		occupationTypeList = occupationTypeDao.getAliveRecords(myOffice);
		totalRecords = occupationTypeDao.getTotalRecords();
	}
	
	public String submit(String occupationName) {
		begin();
		try {
			if (occupationCode == null || occupationCode.equals("")) {
				insertOccupationType(occupationName);
			} else {
				//updateCommitteeDesignationType();
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
	
	private void insertOccupationType(String occupationName) throws Exception {
		int id = generateOccupationTypeId();
		saveOccupationType(id, occupationName);
	}
	
	private int generateOccupationTypeId() throws Exception {
		int occupationTypeId = 0;
		StringBuffer query = new StringBuffer(
				"SELECT max(occ_code) from tm_occupation where occ_code != 99");		
		PreparedStatement pStmt = connection.prepareStatement(query.toString());
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			occupationTypeId = rs.getInt(1);
		}
		return ++occupationTypeId;
	}
	
	private String saveOccupationType(int occupationTypeId, String occupationName)
			throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("Occupation Name", occupationName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Occupation Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		OccupationType occupationType = new OccupationType();
		occupationType.setOccupationCode(occupationTypeId);
		occupationType.setOccupationName(occupationName);
		occupationType.setCreatedBy(myUserId);
		occupationType.setCreatedIp(myIpAddress);
		occupationType.setLastModifiedBy(myUserId);
		occupationType.setLastModifiedIp(myIpAddress);
		OccupationTypeDao occupationTypeDao = new OccupationTypeDao(connection);
		int status = occupationTypeDao.insertRecord(occupationType);
		if(status > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Occupation type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}
	
	public String initGetOccupationType(String occupationCode) {
		begin();
		try {
			getOccupationType(occupationCode);
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	private void getOccupationType(String occupationCode) throws Exception {
		OccupationTypeDao occupationTypeDao = new OccupationTypeDao(connection);
		requestedDetails = occupationTypeDao.getOccupationType(occupationCode);
	}
	
	public String editOccupationType(String occupationCode,
			String occupationName) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("Occupation Name", occupationName, "WordS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Occupation Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			OccupationType occupationType = new OccupationType();
			occupationType.setOccupationCode(Integer.parseInt(occupationCode));
			occupationType.setOccupationName(occupationName);
			
			occupationType.setLastModifiedBy(myUserId);
			occupationType.setLastModifiedIp(myIpAddress);
			OccupationTypeDao occupationTypeDao = new OccupationTypeDao(connection);
			int status = occupationTypeDao.editRecord(occupationType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Occupation type is edited successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteOccupationType(String occupationCode) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}			
			OccupationType occupationType = new OccupationType();
			occupationType.setOccupationCode(Integer.parseInt(occupationCode));
			OccupationTypeDao occupationTypeDao = new OccupationTypeDao(connection);
			int status =  occupationTypeDao.removeRecord(occupationType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Occupation type is deleted successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}
