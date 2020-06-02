package service.masters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import models.master.CommitteeDesignationType;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.CommitteeDesignationTypeDao;

public class CommitteeDesignationTypeService extends Commons {
	private Integer designationCode;
	private String designationName;
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
	private List<CommitteeDesignationType> committeeDesignationTypeList;
	public Integer getDesignationCode() {
		return designationCode;
	}
	public void setDesignationCode(Integer designationCode) {
		this.designationCode = designationCode;
	}
	public String getDesignationName() {
		return designationName;
	}
	public void setDesignationName(String designationName) {
		this.designationName = designationName;
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
	public List<CommitteeDesignationType> getCommitteeDesignationTypeList() {
		return committeeDesignationTypeList;
	}
	public void setCommitteeDesignationTypeList(
			List<CommitteeDesignationType> committeeDesignationTypeList) {
		this.committeeDesignationTypeList = committeeDesignationTypeList;
	}
	
	public String pullCommitteeDesignationTypeList() {
		String ret = "success";
		begin();
		try {
			populateCommitteeDesignationTypeList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	private void populateCommitteeDesignationTypeList() throws Exception {
		CommitteeDesignationTypeDao committeeDesignationTypeDao = new CommitteeDesignationTypeDao(connection);
		committeeDesignationTypeDao.setPageNum(pageNum);
		committeeDesignationTypeDao.setRecordsPerPage(recordsPerPage);
		committeeDesignationTypeDao.setSortColumn(sortColumn);
		committeeDesignationTypeDao.setSortOrder(sortOrder);		
		committeeDesignationTypeList = committeeDesignationTypeDao.getAliveRecords(myOffice);
		totalRecords = committeeDesignationTypeDao.getTotalRecords();
	}
	
	public String submit(String designationName) {
		begin();
		try {
			if (designationCode == null || designationCode.equals("")) {
				insertCommitteeDesignationType(designationName);
			} else {
				updateCommitteeDesignationType();
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
	
	private void updateCommitteeDesignationType() throws Exception {
		if (designationCode == null || designationCode.equals(""))
			throw new Exception("Invalid Designation Type Code");
	}
	
	private void insertCommitteeDesignationType(String designationName) throws Exception {
		int id = generateCommitteeDesignationTypeId();
		saveCommitteeDesignationType(id, designationName);
	}
	
	private int generateCommitteeDesignationTypeId() throws Exception {
		int committeeDesignationTypeId = 0;
		StringBuffer query = new StringBuffer(
				"SELECT max(desig_code) from tm_committee_designation where desig_code != 99");		
		PreparedStatement pStmt = connection.prepareStatement(query.toString());
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			committeeDesignationTypeId = rs.getInt(1);
		}
		return ++committeeDesignationTypeId;
	}
	
	private String saveCommitteeDesignationType(int committeeDesignationTypeId, String designationName)
			throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("DesignationName", designationName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Committee Designation Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		CommitteeDesignationType committeeDesignationType = new CommitteeDesignationType();
		committeeDesignationType.setDesignationCode(committeeDesignationTypeId);
		committeeDesignationType.setDesignationName(designationName);
		committeeDesignationType.setCreatedBy(myUserId);
		committeeDesignationType.setCreatedIp(myIpAddress);
		committeeDesignationType.setLastModifiedBy(myUserId);
		committeeDesignationType.setLastModifiedIp(myIpAddress);
		CommitteeDesignationTypeDao committeeDesignationTypeDao = new CommitteeDesignationTypeDao(connection);
		int status = committeeDesignationTypeDao.insertRecord(committeeDesignationType);
		if(status > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Committee Designation type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}
	
	public String initGetCommitteeDesignationType(String designationCode) {
		begin();
		try {
			getCommitteeDesignationType(designationCode);
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	private void getCommitteeDesignationType(String designationCode) throws Exception {
		CommitteeDesignationTypeDao committeeDesignationTypeDao = new CommitteeDesignationTypeDao(connection);
		requestedDetails = committeeDesignationTypeDao.getCommitteeDesignationType(designationCode);
	}
	
	public String editCommitteeDesignationtType(String committeeDesignationTypeId,
			String committeeDeignationName) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("Committee Designation", committeeDeignationName, "WordS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Committee Designation Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			CommitteeDesignationType committeeDesignationType = new CommitteeDesignationType();
			committeeDesignationType.setDesignationCode(Integer.parseInt(committeeDesignationTypeId));
			committeeDesignationType.setDesignationName(committeeDeignationName);
			
			committeeDesignationType.setLastModifiedBy(myUserId);
			committeeDesignationType.setLastModifiedIp(myIpAddress);
			CommitteeDesignationTypeDao committeeDesignationTypeDao = new CommitteeDesignationTypeDao(connection);
			int status = committeeDesignationTypeDao.editRecord(committeeDesignationType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Committee Designation type is edited successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteCommitteeDesignationType(String committeeDesignationCode) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}			
			CommitteeDesignationType committeeDesignationType = new CommitteeDesignationType();
			committeeDesignationType.setDesignationCode(Integer.parseInt(committeeDesignationCode));
			CommitteeDesignationTypeDao committeeDesignationTypeDao = new CommitteeDesignationTypeDao(connection);
			int status =  committeeDesignationTypeDao.removeRecord(committeeDesignationType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Committee Designation type is deleted successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}
