package service.masters;

import java.sql.SQLException;
import java.util.List;

import models.master.DesignationType;
import models.master.Role;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.DesignationDao;
import dao.master.RoleDao;


public class DesignationTypeDetailsServices  extends Commons {

	
	List<KVPair<String, String>> designationTypeList;
	List<DesignationType> designationList;
	List<KVPair<String, String>> availableDesList;
	List<KVPair<String, String>> assignedDesList;
	private static final String recordStatus = null;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String designationName;
    private String designationId;
    private String shortDesignation;
    private String assignedDesc;

 
	

	 public String execute() {
		begin();
		try {
				initDesignationList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	

    public void initDesignationList() throws Exception{
		DesignationDao ddao=new DesignationDao(connection);
		designationTypeList=ddao.getKVList();
	}
	
	public String initializeDesignationList() {
		begin();
		try {
				populateDesignationList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populateDesignationList() throws Exception{
		DesignationDao ddao=new DesignationDao(connection);
		ddao.setPageNum(pageNum);
		ddao.setRecordsPerPage(recordsPerPage);
		ddao.setSortColumn(sortColumn);
		ddao.setSortOrder(sortOrder);				
	    designationList=ddao.getMastertype();
		totalRecords=ddao.getTotalRecords();
	}
	public String AddDesignation() {
		begin();
		try {
				adddesignationName();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	public Boolean validateDes() throws Exception{		
		if(ESAPI.validator().isValidInput("DesignationName", designationName, "AlphaS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Designation Name - Only aplphabets allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public void adddesignationName() throws Exception{
		DesignationDao bdao=new DesignationDao(connection);
		DesignationType des=new DesignationType();
		//if(validateDes()==true){
		des.setDesignationId(designationId);
		des.setDesignationName(designationName);
		des.setShortDesignation(shortDesignation);
        des.setCreatedIp(myIpAddress);
	    des.setRecordStatus(recordStatus);
		des.setEnteredBy(myUserId);
		des.setLastModifiedBy(myUserId);
		des.setLastModifiedIp(myIpAddress);
      int status=	bdao.insertRecord(des);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Designation Name is Inserted successfully.", Status.SUCCESS, Type.BAR));
		      setDesignationId(bdao.getDesignationId());
		}
	//}
	public String initDesignation() {
		begin();
		try {
				populateDes();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateDes() throws Exception{
		DesignationDao bdao=new DesignationDao(connection);
		bdao.setDesignationid(designationId);
		availableDesList=bdao.getAvailableDes();
		assignedDesList=bdao.getAssignedDes();
			
	}
	public String initSaveDesc() {		
		try {
				begin();
				saveDesc();
				
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
	private void saveDesc() throws Exception{
		DesignationDao bdao=new DesignationDao(connection);
		DesignationType des=new DesignationType();
		des.setEnteredBy(myUserId);
		des.setCreatedIp(myIpAddress);
	    des.setRecordStatus(recordStatus);
		des.setLastModifiedBy(myUserId);
	    des.setLastModifiedIp(myIpAddress);
		bdao.setDesignationId(designationId);
		bdao.setAssignedDesc(assignedDesc);
		String status=bdao.saveDes(des);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Office Type for Designation  are successfully saved.", Status.SUCCESS, Type.BAR));	
			
			}		
	}
	public String editDesignationName () {
		begin();
		try {
				editDesignation();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editDesignation() throws Exception {
		DesignationDao bdao=new DesignationDao(connection);
		DesignationType des=new DesignationType();
		if(validateDes()==true){
			des.setDesignationId(designationId);
			des.setDesignationName(designationName);
			des.setShortDesignation(shortDesignation);	    
	     	des.setLastModifiedBy(myUserId);
	        des.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(des);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Designation Name is Edited successfully.", Status.SUCCESS, Type.BAR));
		}

	}
public String initDeleteDes() {
	begin();
	try {
			deleteDesName();
	} catch(Exception e){
		e.printStackTrace();
	}
	finally{
		finish();
	}	
	return "success";
}

private void deleteDesName() throws Exception {
	DesignationDao bdao=new DesignationDao(connection);
	DesignationType des=new DesignationType();
	bdao.setDesignationId(designationId);
	String status=bdao.deletetable(des);
	if(status.equals("success")){
		notifyList.add(new Notification("Success!!", "Designation Type is Deleted successfully.", Status.SUCCESS, Type.BAR));
}
}

	public List<KVPair<String, String>> getDesignationTypeList() {
		return designationTypeList;
	}


	public void setDesignationTypeList(
			List<KVPair<String, String>> designationTypeList) {
		this.designationTypeList = designationTypeList;
	}


	public List<DesignationType> getDesignationList() {
		return designationList;
	}


	public void setDesignationList(List<DesignationType> designationList) {
		this.designationList = designationList;
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


	public String getDesignationName() {
		return designationName;
	}


	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}


	public String getAssignedDesc() {
		return assignedDesc;
	}


	public void setAssignedDesc(String assignedDesc) {
		this.assignedDesc = assignedDesc;
	}


	public String getDesignationId() {
		return designationId;
	}


	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}


	public String getShortDesignation() {
		return shortDesignation;
	}


	public void setShortDesignation(String shortDesignation) {
		this.shortDesignation = shortDesignation;
	}


	public static String getRecordstatus() {
		return recordStatus;
	}
	
	public List<KVPair<String, String>> getAvailableDesList() {
		return availableDesList;
	}


	public void setAvailableDesList(List<KVPair<String, String>> availableDesList) {
		this.availableDesList = availableDesList;
	}


	public List<KVPair<String, String>> getAssignedDesList() {
		return assignedDesList;
	}


	public void setAssignedDesList(List<KVPair<String, String>> assignedDesList) {
		this.assignedDesList = assignedDesList;
	}


	public void populateVisibleDesignationTypeList() throws Exception {
		begin();
		try {			
			DesignationDao designationDao = new DesignationDao(connection);
			designationList = designationDao
					.getVisibleAliveRecords(myOffice);
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
	}
}
