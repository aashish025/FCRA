package service.masters;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;

import dao.master.PcSectionDao;
import models.master.PcSection;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class PcSectionTypeService extends Commons {
private static String recordStatus = null;
	

	List<KVPair<String, String>> pcSectionTypeList;
	List<KVPair<String, String>> availableseviceList;
	List<KVPair<String, String>> assignedserviceList;	
	List<PcSection> pcsectionList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
    private Integer CreatedOn;
    private Integer EnteredOn;
    private String assignedService;
    private String sectionName;
    private String sectionId;
    

   
	
	 public String execute() {
		begin();
		try {
				initPcsectionTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	

     public void initPcsectionTypeList() throws Exception{
		PcSectionDao bdao=new PcSectionDao(connection);
		 pcSectionTypeList=bdao.getKVList();
	}
	
	public String initializePcSectionList() {
		begin();
		try {
				populatePcSectionList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 

	private void populatePcSectionList() throws Exception{
		PcSectionDao bdao=new PcSectionDao(connection);
		bdao.setPageNum(pageNum);
		bdao.setRecordsPerPage(recordsPerPage);
		bdao.setSortColumn(sortColumn);
		bdao.setSortOrder(sortOrder);
		bdao.setMyOfficeCode(myOfficeCode);
	    pcsectionList=bdao.gettable();
		totalRecords=bdao.getTotalRecords();
	}
	public Boolean validateSection() throws Exception{		
		if(ESAPI.validator().isValidInput("Section Name", sectionName, "WordS", 5, false) == false){
			notifyList.add(new Notification("Error!!", "Section Name - Only Alphabet and Number allowed (5 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initCreatePcSection() {
		begin();
		try {
				populateCreateSection();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateCreateSection() throws Exception{
		PcSectionDao bdao=new PcSectionDao(connection);
		PcSection pcsection=new PcSection();
		if(validateSection()==true)
		{
		pcsection.setSectionName(sectionName);
		pcsection.setMyOfficeCode(myOfficeCode);
		pcsection.setCreatedBy(myUserId);
		pcsection.setCreatedIp(myIpAddress);
		pcsection.setLastModifiedBy(myUserId);
		pcsection.setLastModifiedIp(myIpAddress);
		   int status=	bdao.insertRecord(pcsection);
		   if(status>0)
			notifyList.add(new Notification("Success!!","New Section <b>"+sectionName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		  setSectionId(bdao.getSectionId());
		   }
	}
	
	public String initService() {
		begin();
		try {
				populateService();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateService() throws Exception{
		PcSectionDao bdao=new PcSectionDao(connection);
		bdao.setSectionId(sectionId);
		availableseviceList=bdao.getAvailableServices();
		assignedserviceList=bdao.getAssignedServices();
			
	}

	public String initSaveServices() {		
		try {
				begin();
				saveService();
				
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
	private void saveService() throws Exception{
		PcSectionDao bdao=new PcSectionDao(connection);
		PcSection pcsection=new PcSection();
	    pcsection.setMyOfficeCode(myOfficeCode);
		bdao.setSectionId(sectionId);;
		bdao.setAssignedService(assignedService);
		String status=bdao.saveService(pcsection);		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Services  are Assigned to Section .", Status.SUCCESS, Type.BAR));	
			
			}		
	}


	public String editSection () {
		begin();
		try {
				editsection();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editsection() throws Exception {
		PcSectionDao bdao=new PcSectionDao(connection);
		PcSection pcsection=new PcSection();
		if(validateSection()==true){
		pcsection.setSectionName(sectionName);
		pcsection.setSectionId(sectionId);
		pcsection.setLastModifiedBy(myUserId);
		pcsection.setLastModifiedIp(myIpAddress);
		int status=	bdao.editRecord(pcsection);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Section  is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}


	public String initDeleteSection() {
		begin();
		try {
				deleteSection();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void deleteSection() throws Exception{
		PcSectionDao bdao=new PcSectionDao(connection);
		PcSection pcsection=new PcSection();
		bdao.setSectionId(sectionId);;
	    String status=bdao.deleterole(pcsection);
		
		if(status.equals("success")){
			notifyList.add(new Notification("Success !!", "Section  is successfully deleted.", Status.SUCCESS, Type.BAR));	
		}	
	}



	public static String getRecordStatus() {
		return recordStatus;
	}


	public static void setRecordStatus(String recordStatus) {
		PcSectionTypeService.recordStatus = recordStatus;
	}


	public List<KVPair<String, String>> getPcSectionTypeList() {
		return pcSectionTypeList;
	}


	public void setPcSectionTypeList(List<KVPair<String, String>> pcSectionTypeList) {
		this.pcSectionTypeList = pcSectionTypeList;
	}


	public List<KVPair<String, String>> getAvailableseviceList() {
		return availableseviceList;
	}


	public void setAvailableseviceList(
			List<KVPair<String, String>> availableseviceList) {
		this.availableseviceList = availableseviceList;
	}


	public List<KVPair<String, String>> getAssignedserviceList() {
		return assignedserviceList;
	}


	public void setAssignedserviceList(
			List<KVPair<String, String>> assignedserviceList) {
		this.assignedserviceList = assignedserviceList;
	}


	public List<PcSection> getPcsectionList() {
		return pcsectionList;
	}


	public void setPcsectionList(List<PcSection> pcsectionList) {
		this.pcsectionList = pcsectionList;
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


	public Integer getCreatedOn() {
		return CreatedOn;
	}


	public void setCreatedOn(Integer createdOn) {
		CreatedOn = createdOn;
	}


	public Integer getEnteredOn() {
		return EnteredOn;
	}


	public void setEnteredOn(Integer enteredOn) {
		EnteredOn = enteredOn;
	}


	public String getAssignedService() {
		return assignedService;
	}


	public String getSectionName() {
		return sectionName;
	}


	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}


	public String getSectionId() {
		return sectionId;
	}


	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}


	public void setAssignedService(String assignedService) {
		this.assignedService = assignedService;
	}
	

	


	
	

}