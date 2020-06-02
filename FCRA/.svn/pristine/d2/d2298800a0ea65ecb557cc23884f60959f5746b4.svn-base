package service.masters;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.owasp.esapi.ESAPI;

import dao.master.DonorTypeDao;
import dao.master.ReligionTypeDao;
import models.master.DonorType;
import models.master.ReligionType;
import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class DonorTypeService extends Commons {	
	private String donorId;
	private String donorName;
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
	private List<DonorType> donorTypeList;
	
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
	public List<DonorType> getDonorTypeList() {
		return donorTypeList;
	}
	public void setDonorTypeList(List<DonorType> donorTypeList) {
		this.donorTypeList = donorTypeList;
	}
	public String getDonorId() {
		return donorId;
	}
	public void setDonorId(String donorId) {
		this.donorId = donorId;
	}
	public String getDonorName() {
		return donorName;
	}
	public void setDonorName(String donorName) {
		this.donorName = donorName;
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
	public String pullDonorTypeList() {
		String ret = "success";
		begin();
		try {
			populateDonorTypeList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	private void populateDonorTypeList() throws Exception {
		DonorTypeDao donorTypeDao = new DonorTypeDao(connection);
		donorTypeDao.setPageNum(pageNum);
		donorTypeDao.setRecordsPerPage(recordsPerPage);
		donorTypeDao.setSortColumn(sortColumn);
		donorTypeDao.setSortOrder(sortOrder);		
		donorTypeList = donorTypeDao.getAliveRecords(myOffice);
		totalRecords = donorTypeDao.getTotalRecords();
	}
	
	public String submit(String donorCode, String donorDesc) {
		begin();
		try {
			saveDonorType(donorCode, donorDesc);
			
		} catch (Exception e) {
			try {
				notifyList.add(new Notification("Error!","Donor Code is already in use", Status.ERROR, Type.BAR));
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
	
	private String saveDonorType(String donorCode, String donorName)
			throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("religionDesc", donorName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Donor Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		DonorType donorType = new DonorType();
		donorType.setDonorId(donorCode);
		donorType.setDonorName(donorName);
		donorType.setCreatedBy(myUserId);
		donorType.setCreatedIp(myIpAddress);
		donorType.setLastModifiedBy(myUserId);
		donorType.setLastModifiedIp(myIpAddress);
		DonorTypeDao donorTypeDao = new DonorTypeDao(connection);
		int status = donorTypeDao.insertRecord(donorType);
		if(status > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Donor type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}
	
	public String initGetDonorType(String donorCode) {
		begin();
		try {
			getDonorType(donorCode);
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	private void getDonorType(String donorCode) throws Exception {
		DonorTypeDao donorTypeDao = new DonorTypeDao(connection);
		requestedDetails = donorTypeDao.getDonorType(donorCode);
	}
	
	public String editDonorType(String donorCode,
			String donorName) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("Religion Description", donorName, "WordS", 50, false) == false){
				notifyList.add(new Notification("Error!!", "Donor Name - Only alphabets and numbers and space allowed (50 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			DonorType donorType = new DonorType();
			donorType.setDonorId(donorCode);
			donorType.setDonorName(donorName);
			
			donorType.setLastModifiedBy(myUserId);
			donorType.setLastModifiedIp(myIpAddress);
			DonorTypeDao donorTypeDao = new DonorTypeDao(connection);
			int status = donorTypeDao.editRecord(donorType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Donor type is edited successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteDonorType(String donorCode) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}			
			DonorType donorType = new DonorType();
			donorType.setDonorId(donorCode);
			DonorTypeDao donorTypeDao = new DonorTypeDao(connection);
			int status =  donorTypeDao.removeRecord(donorType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Donor type is deleted successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}
