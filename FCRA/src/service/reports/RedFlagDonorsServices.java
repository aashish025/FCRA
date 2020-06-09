package service.reports;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;

import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;
import dao.master.CountryTypeDao;
import dao.master.RedFlagCategoryDao;
import dao.services.RedFlagAssociationsDao;
import dao.services.RedFlagDonorsDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class RedFlagDonorsServices extends Commons{
	private List<KVPair<String, String>> categoryList;
	private List<KVPair<String, String>> countryList;
	private int roleId;
	private String donorId;
    private String donorName;
    private String rbiCircularIssueDate;
    private String donorCountry;
    private String originatorOffice;
    private String originatorOrderNo ;
    private String originatorOrderDate;
    private String categoryCode;
    private String categoryDesc;
    private String remarks;
	private String flagValue;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private int yelloFlagRemove;
	private String flagdelete;
	private String appName;
	 private List<RedFlagDonors> applicationList;
	List<RedFlagDonors> redFlagDonorList;
	public String execute() {
		String result = "error";
		begin();
		try {
			initializeList();
			
			//roleId=setRole();
			
	        result = "success";
		} /*catch (ValidationException ve) {
			try {
				notifyList.add(new Notification("Error!", ve.getMessage(),Status.ERROR, Type.BAR));
			} catch (Exception ex) {
			}
		}*/ catch (Exception e) {
			ps(e);
			try {
				notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}
		finish();
		return result;
	}
	
	
	public String initRedFlagDonors() {
		
		begin();
		try {
				populateRedFlagDonors();
		} catch(Exception e){
			try {
				notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,Type.BAR));
			} catch (Exception ex) {

			}
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	public String initAddRedFlagAssociation() throws NotificationException {
		
		String result = "error";
		if(ESAPI.validator().isValidInput("donorName", donorName, "WordSS", 150, false) == false){
			notifyList.add(new Notification("Error!!", "Donor Name - Only alphabets and numbers and space allowed (150 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("Remark", remarks, "WordSS", 2000, false) == false){
			notifyList.add(new Notification("Error!!", "Remark - 2000 characters max.", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("categoryCode", categoryDesc, "Num", 2, false) == false){
			notifyList.add(new Notification("Error!!", "Category - Select a valid category.", Status.ERROR, Type.BAR));	
			return result;
		}
		begin();
		try {
			addFlagDonors();
			
		} catch(Exception e){
			try {
				connection.rollback();
				
		} catch (Exception e1) {				
			e1.printStackTrace();
		}
			
			spl(e);
			try {
				notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void addFlagDonors() throws Exception{
		
		
	RedFlagDonorsDao redFlagAssociationsDao=new RedFlagDonorsDao(connection);
	redFlagAssociationsDao.setMyUserId(myUserId);
	int roleId=redFlagAssociationsDao.findRoleId(myUserId);
	int yellowroleId=redFlagAssociationsDao.yellowFlagRole(myUserId);
	if(flagValue.equalsIgnoreCase("1")){
	if(roleId-16==17 || roleId-16==0){
	int status=redFlagAssociationsDao.insertRecord(new RedFlagDonors(donorName,rbiCircularIssueDate,donorCountry,originatorOffice,originatorOrderDate,originatorOrderNo,categoryDesc,remarks,flagValue,myOfficeCode));
	if(status==-2){
		notifyList.add(new utilities.notifications.Notification("Info!!", "Doner is already exist in red flag list", Status.INFORMATION, Type.BAR));
		}
	else if(status==-1){
		notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify red flag list.", Status.WARNING, Type.BAR));
		}
	else if(status>0)
		notifyList.add(new utilities.notifications.Notification("Success!!", "Donor Details sucessfully inserted into red flag list.", Status.SUCCESS, Type.BAR));
		}	
	}
	else{
		if(yellowroleId-18==15 || yellowroleId-15==0 ||roleId-17==16 || roleId-16==0 ){
		int status=redFlagAssociationsDao.insertRecord(new RedFlagDonors(donorName,rbiCircularIssueDate,donorCountry,originatorOffice,originatorOrderDate,originatorOrderNo,categoryDesc,remarks,flagValue,myOfficeCode));
		if(status==-2){
			notifyList.add(new utilities.notifications.Notification("Info!!", "Doner is already exist in Yellow flag list.", Status.INFORMATION, Type.BAR));
		}
		else if(status==-1){
			notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
		}
		else if(status>0){
			notifyList.add(new utilities.notifications.Notification("Success!!", "Donor Details sucessfully inserted into yellow flag list.", Status.SUCCESS, Type.BAR));
				}
			
			}	}
	}
	
public String initAddRedRemoveYellowFlagAssociation() throws NotificationException {
		
		String result = "error";
		if(ESAPI.validator().isValidInput("Remark", remarks, "WordSS", 2000, false) == false){
			notifyList.add(new Notification("Error!!", "Remark - 2000 characters max.", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("categoryCode", categoryDesc, "Num", 2, false) == false){
			notifyList.add(new Notification("Error!!", "Category - Select a valid category.", Status.ERROR, Type.BAR));	
			return result;
		}
		begin();
		try {
			addRedFlagDonors();
			
		} catch(Exception e){
			try {
				connection.rollback();
				
		} catch (Exception e1) {				
			e1.printStackTrace();
		}
			
			spl(e);
			try {
				notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void addRedFlagDonors() throws Exception{
		
		
	RedFlagDonorsDao redFlagAssociationsDao=new RedFlagDonorsDao(connection);
	redFlagAssociationsDao.setMyUserId(myUserId);
	redFlagAssociationsDao.setMyOfficeCode(myOfficeCode);
	if(flagValue.equalsIgnoreCase("1")){
	int roleId=redFlagAssociationsDao.findRoleId(myUserId);
	if(roleId-16==17 || roleId-16==0){
	redFlagAssociationsDao.insertRecordAddingYellow(new RedFlagDonors(donorId,rbiCircularIssueDate,originatorOffice,originatorOrderDate,originatorOrderNo,categoryDesc,remarks,flagValue,myOfficeCode));
	notifyList.add(new utilities.notifications.Notification("Success!!", "Donor Details sucessfully inserted into red flag list.", Status.SUCCESS, Type.BAR));
	}
	}
	else {
		notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify red flag list.", Status.WARNING, Type.BAR));
		}
	
	}
		
	private void populateRedFlagDonors() throws Exception{
		RedFlagDonorsDao redFlagAssociationsDao=new RedFlagDonorsDao(connection);
		redFlagAssociationsDao.setPageNum(pageNum);
		redFlagAssociationsDao.setRecordsPerPage(recordsPerPage);
		redFlagAssociationsDao.setSortColumn(sortColumn);
		redFlagAssociationsDao.setSortOrder(sortOrder);	
		redFlagDonorList=redFlagAssociationsDao.getAll();
		totalRecords=redFlagAssociationsDao.getTotalRecords();
	}	
	
	private void initializeList() throws Exception {
		
		RedFlagCategoryDao redFlagCategoryDao=new RedFlagCategoryDao(connection);
		categoryList=redFlagCategoryDao.getKVList();
	    CountryTypeDao countryTypeDao=new CountryTypeDao(connection);
	    countryList=countryTypeDao.getKVList();
		RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
		roleId=redFlagAssociationsDao.findRoleId(myUserId);
		yelloFlagRemove=redFlagAssociationsDao.yellowFlagRole(myUserId);
	}
		public String initDeleteRedFlagDonors(String donorId,String deloriginatorOffice,String deloriginatorOrderNo,String deloriginatorOrderDate,String delremarkOriginatorOffice) throws NotificationException {
		String result= "error";
		if(ESAPI.validator().isValidInput("delremarkOriginatorOffice", delremarkOriginatorOffice, "WordSS", 2000, false) == false){
			notifyList.add(new Notification("Error!!", "Remark - 2000 characters max.", Status.ERROR, Type.BAR));	
			return result;
		}
		
		begin();
		try {
				deleteRedFlagDonor(donorId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice);
		} catch(Exception e){
			try {
				connection.rollback();
		} catch (SQLException e1) {				
			e1.printStackTrace();
		}
			try {
				notifyList.add(new Notification("Error!","Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
			ps(e);
			
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void deleteRedFlagDonor(String donorId,String deloriginatorOffice,String deloriginatorOrderNo,String deloriginatorOrderDate,String delremarkOriginatorOffice) throws Exception {
		RedFlagDonorsDao redFlagDonorDao=new RedFlagDonorsDao(connection);
		redFlagDonorDao.setMyUserId(myUserId);
		redFlagDonorDao.setMyOfficeCode(myOfficeCode);
		if(flagdelete.equalsIgnoreCase("1")){
		int roleId=redFlagDonorDao.findRoleId(myUserId);
		if((roleId-16==17 || roleId-16==1)){
		int status=redFlagDonorDao.removeRecord(donorId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice,myOfficeCode);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Donor Details sucessfully deleted from Adverse list.", Status.SUCCESS, Type.BAR));
		}
		else
		{
			notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
		}
		}
		else{

			int yellowroleId=redFlagDonorDao.yellowFlagRole(myUserId);;
			if((yellowroleId-15==18 || yellowroleId-18==0)){
				int status=redFlagDonorDao.removeRecord(donorId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice,myOfficeCode);
			if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "Association Details sucessfully deleted from Adverse list.", Status.SUCCESS, Type.BAR));
			}
			else{
				notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
			}
		
		}
		}
	
	public void initApplicationListDetails(){
		begin();
		try {
				populateApplicationListDetails();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}		
	}
	private void populateApplicationListDetails() throws Exception{
		RedFlagDonorsDao pdd=new RedFlagDonorsDao(connection);						
		pdd.setPageNum(pageNum);
		pdd.setRecordsPerPage(recordsPerPage);
		pdd.setSortColumn(sortColumn);
		pdd.setSortOrder(sortOrder);
		pdd.setAssoName(appName);				
		applicationList=pdd.getApplicationListDetails();
		totalRecords=pdd.getTotalRecords();
	}
		
	
	
	public List<KVPair<String, String>> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<KVPair<String, String>> categoryList) {
		this.categoryList = categoryList;
	}


	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
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


	public String getOriginatorOffice() {
		return originatorOffice;
	}


	public void setOriginatorOffice(String originatorOffice) {
		this.originatorOffice = originatorOffice;
	}


	public String getOriginatorOrderNo() {
		return originatorOrderNo;
	}


	public void setOriginatorOrderNo(String originatorOrderNo) {
		this.originatorOrderNo = originatorOrderNo;
	}


	public String getOriginatorOrderDate() {
		return originatorOrderDate;
	}


	public void setOriginatorOrderDate(String originatorOrderDate) {
		this.originatorOrderDate = originatorOrderDate;
	}


	public String getCategoryDesc() {
		return categoryDesc;
	}


	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}




	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
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




	public String getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}


	

	public String getDonorCountry() {
		return donorCountry;
	}


	public void setDonorCountry(String donorCountry) {
		this.donorCountry = donorCountry;
	}


	public List<KVPair<String, String>> getCountryList() {
		return countryList;
	}


	public void setCountryList(List<KVPair<String, String>> countryList) {
		this.countryList = countryList;
	}


	public String getRbiCircularIssueDate() {
		return rbiCircularIssueDate;
	}


	public void setRbiCircularIssueDate(String rbiCircularIssueDate) {
		this.rbiCircularIssueDate = rbiCircularIssueDate;
	}


	public List<RedFlagDonors> getRedFlagDonorList() {
		return redFlagDonorList;
	}


	public void setRedFlagDonorList(List<RedFlagDonors> redFlagDonorList) {
		this.redFlagDonorList = redFlagDonorList;
	}
	
   

	public String getFlagValue() {
		return flagValue;
	}


	public void setFlagValue(String flagValue) {
		this.flagValue = flagValue;
	}


	public int getYelloFlagRemove() {
		return yelloFlagRemove;
	}


	public void setYelloFlagRemove(int yelloFlagRemove) {
		this.yelloFlagRemove = yelloFlagRemove;
	}


	public String getFlagdelete() {
		return flagdelete;
	}


	public void setFlagdelete(String flagdelete) {
		this.flagdelete = flagdelete;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public List<RedFlagDonors> getApplicationList() {
		return applicationList;
	}


	public void setApplicationList(List<RedFlagDonors> applicationList) {
		this.applicationList = applicationList;
	}


}
