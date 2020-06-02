package service.masters;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import models.master.ReligionType;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.ReligionTypeDao;

public class ReligionTypeService extends Commons {
	private String religionCode;
	private String religionDesc;
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
	private List<ReligionType> religionTypeList;
	
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
	public List<ReligionType> getReligionTypeList() {
		return religionTypeList;
	}
	public void setReligionTypeList(List<ReligionType> religionTypeList) {
		this.religionTypeList = religionTypeList;
	}
	public String getReligionCode() {
		return religionCode;
	}
	public void setReligionCode(String religionCode) {
		this.religionCode = religionCode;
	}
	public String getReligionDesc() {
		return religionDesc;
	}
	public void setReligionDesc(String religionDesc) {
		this.religionDesc = religionDesc;
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
	
	public String pullReligionTypeList() {
		String ret = "success";
		begin();
		try {
			populateReligionTypeList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	private void populateReligionTypeList() throws Exception {
		ReligionTypeDao religionTypeDao = new ReligionTypeDao(connection);
		religionTypeDao.setPageNum(pageNum);
		religionTypeDao.setRecordsPerPage(recordsPerPage);
		religionTypeDao.setSortColumn(sortColumn);
		religionTypeDao.setSortOrder(sortOrder);		
		religionTypeList = religionTypeDao.getAliveRecords(myOffice);
		totalRecords = religionTypeDao.getTotalRecords();
	}
	
	public String submit(String religionCode, String religionDesc) {
		begin();
		try {
			saveReligionType(religionCode, religionDesc);
			/*if (religionCode == null || religionCode.equals("")) {
				//insertReligionType(religionCode, religionDesc);
				saveReligionType(religionCode, religionDesc);
			} else {
				updateReligionType();
			}*/
		} catch (Exception e) {
			try {
				notifyList.add(new Notification("Error!","Religion Code is already in use", Status.ERROR, Type.BAR));
			}catch(Exception ex) {}
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

	/*private void insertReligionType(String religionDesc) throws Exception {
		String id = generateReligionTypeId().toString();
		saveReligionType(id, religionDesc);
	}*/
	
	private void updateReligionType() throws Exception {
		if (religionCode == null || religionCode.equals(""))
			throw new Exception("Invalid Designation Type Code");
	}
	
	/*private Integer generateReligionTypeId() throws Exception {
		Integer religionTypeId = 0;
		StringBuffer query = new StringBuffer(
				"SELECT max(religion_code) from tm_religion where religion_code != 99");		
		PreparedStatement pStmt = connection.prepareStatement(query.toString());
		ResultSet rs = pStmt.executeQuery();
		while (rs.next()) {
			religionTypeId = rs.getInt(1);
		}
		return ++religionTypeId;
	}*/
	
	private String saveReligionType(String religionTypeId, String religionDesc)
			throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("religionDesc", religionDesc, "WordS", 15, false) == false){
			notifyList.add(new Notification("Error!!", "Religion Description - Only alphabets and numbers and space allowed (15 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		ReligionType religionType = new ReligionType();
		religionType.setReligionCode(religionTypeId);
		religionType.setReligionDesc(religionDesc);
		religionType.setCreatedBy(myUserId);
		religionType.setCreatedIp(myIpAddress);
		religionType.setLastModifiedBy(myUserId);
		religionType.setLastModifiedIp(myIpAddress);
		ReligionTypeDao religionTypeDao = new ReligionTypeDao(connection);
		int status = religionTypeDao.insertRecord(religionType);
		if(status > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Religion type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}
	
	public String initGetReligionType(String religionCode) {
		begin();
		try {
			getReligionType(religionCode);
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	private void getReligionType(String religionCode) throws Exception {
		ReligionTypeDao religionTypeDao = new ReligionTypeDao(connection);
		requestedDetails = religionTypeDao.getReligionType(religionCode);
	}
	
	public String editReligionType(String religionTypeId,
			String religionDesc) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("Religion Description", religionDesc, "WordS", 15, false) == false){
				notifyList.add(new Notification("Error!!", "Religion Description - Only alphabets and numbers and space allowed (15 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			ReligionType religionType = new ReligionType();
			religionType.setReligionCode(religionTypeId);
			religionType.setReligionDesc(religionDesc);
			
			religionType.setLastModifiedBy(myUserId);
			religionType.setLastModifiedIp(myIpAddress);
			ReligionTypeDao religionTypeDao = new ReligionTypeDao(connection);
			int status = religionTypeDao.editRecord(religionType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Religion type is edited successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteReligionType(String religionCode) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}			
			ReligionType religionType = new ReligionType();
			religionType.setReligionCode(religionCode);
			ReligionTypeDao religionTypeDao = new ReligionTypeDao(connection);
			int status =  religionTypeDao.removeRecord(religionType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Religion type is deleted successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
	
}
