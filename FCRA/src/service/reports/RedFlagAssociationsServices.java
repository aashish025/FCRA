package service.reports;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;

import models.services.RedFlagAssociations;
import models.services.requests.AbstractRequest;
import dao.master.RedFlagCategoryDao;
import dao.master.StateDao;
import dao.reports.RedFlaggedRcnsDao;
import dao.services.RedFlagAssociationsDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class RedFlagAssociationsServices extends Commons{
	private List<KVPair<String, String>> categoryList;
	private List<KVPair<String, String>> stateList;
	private int roleId;
	private String assoId;
    private String assoName;
    private String assoAddress;
    private String assoState;
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
	private String deleteflagValue;
	private String appName;
    List<RedFlagAssociations> redFlagAssociationsList;
    private List<RedFlagAssociations> applicationList;
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
	
	
	public String initRedFlagAssociations() {
		begin();
		try {
				populateRedFlagAssociation();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	public String initAddYellowFlagAssociation() throws NotificationException {
		String result = "error";
		if(ESAPI.validator().isValidInput("AssoName", assoName, "WordSS", 150, false) == false){
			notifyList.add(new Notification("Error!!", "Association Name - Only alphabets and numbers and space allowed (150 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("Remark", remarks, "WordSS", 2000, false) == false){
			notifyList.add(new Notification("Error!!", "Remark - 2000 characters max.", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("Category", categoryDesc, "Num", 2, false) == false){
			notifyList.add(new Notification("Error!!", "Category - Select a valid category.", Status.ERROR, Type.BAR));	
			return result;
		}
		begin();
		try {
			AddyellowFlagAssociation();
		} catch(Exception e){
			try {
				connection.rollback();
		} catch (SQLException e1) {				
			e1.printStackTrace();
		}
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void AddyellowFlagAssociation() throws Exception{
		
		
	RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
	redFlagAssociationsDao.setMyUserId(myUserId);
	int roleId=redFlagAssociationsDao.yellowFlagRole(myUserId);
	int redroleId=redFlagAssociationsDao.findRoleId(myUserId);
	if(roleId-18==15 || roleId-15==0 || redroleId-17==16 || redroleId-16==0    ){
	int status=redFlagAssociationsDao.insertRecord(new RedFlagAssociations(assoName,assoAddress,assoState,originatorOffice,originatorOrderDate,originatorOrderNo,categoryDesc,remarks,flagValue,myOfficeCode));
	
	if(status==-2){
		notifyList.add(new utilities.notifications.Notification("Info!!", "Association is already added in Adverse list.", Status.INFORMATION, Type.BAR));
	}
	else if(status==-1){
		notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
	}
	else if(status>0){
		notifyList.add(new utilities.notifications.Notification("Success!!", "Association Details sucessfully inserted into Yellow list.", Status.SUCCESS, Type.BAR));
			}
		
		}	
	}
	
	
	
	
	
	public String initAddRedFlagAssociation() throws NotificationException {
		String result = "error";
		if(ESAPI.validator().isValidInput("AssoName", assoName, "WordSS", 150, false) == false){
			notifyList.add(new Notification("Error!!", "Association Name - Only alphabets and numbers and space allowed (150 characters max).", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("Remark", remarks, "WordSS", 2000, false) == false){
			notifyList.add(new Notification("Error!!", "Remark - 2000 characters max.", Status.ERROR, Type.BAR));	
			return result;
		}
		if(ESAPI.validator().isValidInput("Category", categoryDesc, "Num", 2, false) == false){
			notifyList.add(new Notification("Error!!", "Category - Select a valid category.", Status.ERROR, Type.BAR));	
			return result;
		}
		begin();
		try {
			addFlagAssociation();
		} catch(Exception e){
			try {
				connection.rollback();
		} catch (SQLException e1) {				
			e1.printStackTrace();
		}
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void addFlagAssociation() throws Exception{
		
		
	RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
	redFlagAssociationsDao.setMyUserId(myUserId);
	int roleId=redFlagAssociationsDao.findRoleId(myUserId);
	if(roleId-16==17 || roleId-16==0){
	int status=redFlagAssociationsDao.insertRecord(new RedFlagAssociations(assoName,assoAddress,assoState,originatorOffice,originatorOrderDate,originatorOrderNo,categoryDesc,remarks,flagValue,myOfficeCode));
	
	if(status==-2){
		notifyList.add(new utilities.notifications.Notification("Info!!", "Association is already added in Adverse list.", Status.INFORMATION, Type.BAR));
	}
	else if(status==-1){
		notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
	}
	else if(status>0){
		notifyList.add(new utilities.notifications.Notification("Success!!", "Association Details sucessfully inserted into Red list.", Status.SUCCESS, Type.BAR));
			}
		
		}	
	}
	private void populateRedFlagAssociation() throws Exception{
		RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
		redFlagAssociationsDao.setPageNum(pageNum);
		redFlagAssociationsDao.setRecordsPerPage(recordsPerPage);
		redFlagAssociationsDao.setSortColumn(sortColumn);
		redFlagAssociationsDao.setSortOrder(sortOrder);	
	    redFlagAssociationsList=redFlagAssociationsDao.getAll();
		totalRecords=redFlagAssociationsDao.getTotalRecords();
	}	
	
	private void initializeList() throws Exception {
		
		RedFlagCategoryDao redFlagCategoryDao=new RedFlagCategoryDao(connection);
		categoryList=redFlagCategoryDao.getKVList();
	    StateDao stateDao=new StateDao(connection);
	    stateList=stateDao.getKVList();
		RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
		roleId=redFlagAssociationsDao.findRoleId(myUserId);
		yelloFlagRemove=redFlagAssociationsDao.yellowFlagRole(myUserId);
	}
	
	public String initDeleteRedFlagAssociations(String assoId,String deloriginatorOffice,String deloriginatorOrderNo,String deloriginatorOrderDate,String delremarkOriginatorOffice) throws NotificationException {
		String result = "error";
		if(ESAPI.validator().isValidInput("Remark", delremarkOriginatorOffice, "WordSS", 2000, false) == false){
			notifyList.add(new Notification("Error!!", "Remark - 2000 characters max.", Status.ERROR, Type.BAR));	
			return result;
		}
		begin();
		try {
				deleteRedFlagAssociations(assoId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice,myOfficeCode);
		} catch(Exception e){
			try {
				connection.rollback();
		} catch (SQLException e1) {				
			e1.printStackTrace();
		}
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	
	private void deleteRedFlagAssociations(String assoId,String deloriginatorOffice,String deloriginatorOrderNo,String deloriginatorOrderDate,String delremarkOriginatorOffice,String myOfficeCode) throws Exception {
		RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
		redFlagAssociationsDao.setMyUserId(myUserId);
		redFlagAssociationsDao.setMyOfficeCode(myOfficeCode);
	
		if(deleteflagValue.equalsIgnoreCase("1")){
		int roleId=redFlagAssociationsDao.findRoleId(myUserId);
		if((roleId-16==17 || roleId-16==1)){
		int status=redFlagAssociationsDao.removeRecord(assoId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice,myOfficeCode);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Association Details sucessfully deleted from Red list.", Status.SUCCESS, Type.BAR));
		}
		else{
			notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
		}
	}
		else{
			int yellowroleId=redFlagAssociationsDao.yellowFlagRole(myUserId);;
			if((yellowroleId-15==18 || yellowroleId-18==0)){
			int status=redFlagAssociationsDao.removeRecord(assoId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice,myOfficeCode);
			if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "Association Details sucessfully deleted from Yellow list.", Status.SUCCESS, Type.BAR));
			}
			else{
				notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify Adverse list.", Status.WARNING, Type.BAR));
			}
		}
			
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
			addRedFlagAssociation();
			
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
	
	private void addRedFlagAssociation() throws Exception{
	RedFlagAssociationsDao redFlagAssociationsDao=new RedFlagAssociationsDao(connection);
	redFlagAssociationsDao.setMyUserId(myUserId);
	redFlagAssociationsDao.setMyOfficeCode(myOfficeCode);
	if(flagValue.equalsIgnoreCase("1")){
	int roleId=redFlagAssociationsDao.findRoleId(myUserId);
	if(roleId-16==17 || roleId-16==0){
	redFlagAssociationsDao.insertRecordAddingYellow(new RedFlagAssociations(assoId,originatorOffice,originatorOrderDate,originatorOrderNo,categoryDesc,remarks,flagValue,myOfficeCode));
	notifyList.add(new utilities.notifications.Notification("Success!!", "Association Details sucessfully inserted into red flag list.", Status.SUCCESS, Type.BAR));
	}
	}
	else {
		notifyList.add(new utilities.notifications.Notification("Warning!!", "You are not authorized to modify red flag list.", Status.WARNING, Type.BAR));
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
		RedFlagAssociationsDao pdd=new RedFlagAssociationsDao(connection);						
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


	public String getAssoName() {
		return assoName;
	}


	public void setAssoName(String assoName) {
		this.assoName = assoName;
	}


	public String getAssoAddress() {
		return assoAddress;
	}


	public void setAssoAddress(String assoAddress) {
		this.assoAddress = assoAddress;
	}


	public String getAssoState() {
		return assoState;
	}


	public void setAssoState(String assoState) {
		this.assoState = assoState;
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


	public List<RedFlagAssociations> getRedFlagAssociationsList() {
		return redFlagAssociationsList;
	}


	public void setRedFlagAssociationsList(
			List<RedFlagAssociations> redFlagAssociationsList) {
		this.redFlagAssociationsList = redFlagAssociationsList;
	}


	public List<KVPair<String, String>> getStateList() {
		return stateList;
	}


	public void setStateList(List<KVPair<String, String>> stateList) {
		this.stateList = stateList;
	}


	public String getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}


	public String getAssoId() {
		return assoId;
	}


	public void setAssoId(String assoId) {
		this.assoId = assoId;
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


	public String getDeleteflagValue() {
		return deleteflagValue;
	}


	public void setDeleteflagValue(String deleteflagValue) {
		this.deleteflagValue = deleteflagValue;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public List<RedFlagAssociations> getApplicationList() {
		return applicationList;
	}


	public void setApplicationList(List<RedFlagAssociations> applicationList) {
		this.applicationList = applicationList;
	}


	
	
	
	
	
}
