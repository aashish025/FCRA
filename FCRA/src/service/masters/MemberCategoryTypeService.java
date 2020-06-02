package service.masters;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.owasp.esapi.ESAPI;

import models.master.MemberCategoryType;
import models.master.ReligionType;
import dao.master.MemberCategoryTypeDao;
import dao.master.ReligionTypeDao;
import utilities.Commons;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class MemberCategoryTypeService extends Commons {	
	private String categoryCode;
	private String categoryName;
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
	private List<MemberCategoryType> memberCategoryTypeList;
	
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	
	public List<MemberCategoryType> getMemberCategoryTypeList() {
		return memberCategoryTypeList;
	}
	public void setMemberCategoryTypeList(
			List<MemberCategoryType> memberCategoryTypeList) {
		this.memberCategoryTypeList = memberCategoryTypeList;
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
	
	public String pullMemberCategoryTypeList() {
		String ret = "success";
		begin();
		try {
			populateMemberCategoryTypeList();
		} catch(Exception e){
			ps(e);
			ret = "error";
		}
		finish();
		return ret;		
	}
	
	private void populateMemberCategoryTypeList() throws Exception {
		MemberCategoryTypeDao memberCategoryTypeDao = new MemberCategoryTypeDao(connection);
		memberCategoryTypeDao.setPageNum(pageNum);
		memberCategoryTypeDao.setRecordsPerPage(recordsPerPage);
		memberCategoryTypeDao.setSortColumn(sortColumn);
		memberCategoryTypeDao.setSortOrder(sortOrder);		
		memberCategoryTypeList = memberCategoryTypeDao.getAliveRecords(myOffice);
		totalRecords = memberCategoryTypeDao.getTotalRecords();
	}
	
	public String submit(String memberCategoryCode, String memberCategoryName) {
		begin();
		try {
			saveMemberCategoryType(memberCategoryCode, memberCategoryName);			
		} catch (Exception e) {
			try {
				notifyList.add(new Notification("Error!","Category Code is already in use", Status.ERROR, Type.BAR));
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
	
	private String saveMemberCategoryType(String memberCategoryCode, String memberCategoryName)
			throws Exception {
		String result = "error";
		if(ESAPI.validator().isValidInput("Member Category Name", memberCategoryName, "WordS", 200, false) == false){
			notifyList.add(new Notification("Error!!", "Member Category Name - Only alphabets and numbers and space allowed (15 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		MemberCategoryType memberCategoryType = new MemberCategoryType();
		memberCategoryType.setCategoryCode(memberCategoryCode);
		memberCategoryType.setCategoryName(memberCategoryName);
		memberCategoryType.setCreatedBy(myUserId);
		memberCategoryType.setCreatedIp(myIpAddress);
		memberCategoryType.setLastModifiedBy(myUserId);
		memberCategoryType.setLastModifiedIp(myIpAddress);
		MemberCategoryTypeDao memberCategoryTypeDao = new MemberCategoryTypeDao(connection);
		int status = memberCategoryTypeDao.insertRecord(memberCategoryType);
		if(status > 0) {
			notifyList.add(new utilities.notifications.Notification("Success!!", "Member Category type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		return "success";
	}
	
	public String initGetMemberCategoryType(String categoryCode) {
		begin();
		try {
			getMemberCategoryType(categoryCode);
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	private void getMemberCategoryType(String categoryCode) throws Exception {
		MemberCategoryTypeDao memberCategoryTypeDao = new MemberCategoryTypeDao(connection);
		requestedDetails = memberCategoryTypeDao.getMemberCategoryType(categoryCode);
	}
	
	public String editMemberCategoryType(String categoryCode,
			String categoryName) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
			String result = "error";
			if(ESAPI.validator().isValidInput("Religion Description", categoryName, "WordS", 200, false) == false){
				notifyList.add(new Notification("Error!!", "Member Category Name - Only alphabets and numbers and space allowed (200 characters max).", Status.ERROR, Type.BAR));	
				return result;
			}
			MemberCategoryType memberCategoryType = new MemberCategoryType();
			memberCategoryType.setCategoryCode(categoryCode);
			memberCategoryType.setCategoryName(categoryName);
			
			memberCategoryType.setLastModifiedBy(myUserId);
			memberCategoryType.setLastModifiedIp(myIpAddress);
			MemberCategoryTypeDao memberCategoryTypeDao = new MemberCategoryTypeDao(connection);
			int status = memberCategoryTypeDao.editRecord(memberCategoryType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Member Category type is edited successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
		return "success";
	}
	
	public void deleteMemberCategoryType(String categoryCode) {
		begin();
		try {
			if (connection == null) {
				throw new Exception("Invalid connection");
			}			
			MemberCategoryType memberCategoryType = new MemberCategoryType();
			memberCategoryType.setCategoryCode(categoryCode);
			MemberCategoryTypeDao memberCategoryTypeDao = new MemberCategoryTypeDao(connection);
			int status =  memberCategoryTypeDao.removeRecord(memberCategoryType);
			if(status > 0) {
				notifyList.add(new utilities.notifications.Notification("Success!!", "Member Category type is deleted successfully.", Status.SUCCESS, Type.BAR));
			}
		} catch (Exception e) {
			ps(e);
		} finally {
			finish();
		}
	}
}
