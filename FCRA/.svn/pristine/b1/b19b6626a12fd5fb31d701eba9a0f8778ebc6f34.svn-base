package service.masters;

import java.util.Date;
import java.util.List;

import models.master.UserAudit;
import utilities.Commons;
import utilities.KVPair;
import dao.master.UserAuditDao;
import dao.master.UserDao;

public class UserAuditServices extends Commons{
	List<KVPair<String, String>> userAuditList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String userAudit;
	private String fromDate;
	private String toDate;
	private String totalRecords;
	
	private List<UserAudit> auditList;
	

	public String execute() {
			begin();
			try {
				initUserAuditList();
				} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}
	 public void initUserAuditList() throws Exception{
		 UserDao ddao=new UserDao(connection);
	         userAuditList=ddao.getUserAuditList();
		}
	 
	 public String submitUserAuditList() {
			begin();
			try {
					populateSubmitUserAuditList();
					
			} catch(Exception e){
				e.getMessage();
				ps(e);
			}
			finally{
				finish();
			}	
			return "success";
		} 
		private void populateSubmitUserAuditList() throws Exception{
			UserAuditDao tdao=new UserAuditDao(connection);
				tdao.setUserAudit(userAudit);
				tdao.setFromDate(fromDate);
				tdao.setToDate(toDate);
				tdao.setPageNum(pageNum);
				tdao.setRecordsPerPage(recordsPerPage);
				tdao.setSortColumn(sortColumn);
				tdao.setSortOrder(sortOrder);				
				auditList=tdao.getMainUserAudit();
				totalRecords=tdao.getTotalRecords();
			}
	 
	 
	 
	public List<KVPair<String, String>> getUserAuditList() {
		return userAuditList;
	}
	public void setUserAuditList(List<KVPair<String, String>> userAuditList) {
		this.userAuditList = userAuditList;
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
	public List<UserAudit> getAuditList() {
		return auditList;
	}
	public void setAuditList(List<UserAudit> auditList) {
		this.auditList = auditList;
	}
	public String getUserAudit() {
		return userAudit;
	}
	public void setUserAudit(String userAudit) {
		this.userAudit = userAudit;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	
	
	
	 
	 
	 

}
