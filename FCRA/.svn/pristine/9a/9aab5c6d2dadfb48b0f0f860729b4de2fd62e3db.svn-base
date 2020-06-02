package service.masters;

import java.util.List;

import models.master.GenderType;
import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.KVPair;
//import utilities.lists.List3;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.GenderDetailDao;
//import dao.services.GateDao;

public class GenderDetailServices extends Commons {

	
	private static final String recordStatus = null;
	List<KVPair<String, String>> genderTypeList;
	List<GenderType> genderList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String    GenderType;  
	//private String    GenderName; 
	public String     GenderId;   
    private String rowId;
    private Integer CreatedOn;
	
	
	 public String execute() {
		begin();
		try {
				initGenderTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	

     public void initGenderTypeList() throws Exception{
    	 GenderDetailDao rdao=new GenderDetailDao(connection);
     genderTypeList=rdao.getKVList();
	}
	

	
	private void populateGenderList() throws Exception{
		GenderDetailDao adao=new GenderDetailDao(connection);
		adao.setPageNum(pageNum);
		adao.setRecordsPerPage(recordsPerPage);
		adao.setSortColumn(sortColumn);
		adao.setSortOrder(sortOrder);	
		genderList=adao.getMastergender();
		totalRecords=adao.getTotalRecords();
	}
	
public Boolean validateGenderType() throws Exception{		
		if(ESAPI.validator().isValidInput("GenderType", GenderType, "Word", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Gender Type - Only Aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("GenderType", GenderId, "Word", 1, false) == false){
			notifyList.add(new Notification("Error!!", "Gender Code - Only Aplphabets and numbers allowed (1 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public void addGenderType() throws Exception{
		GenderDetailDao rdao=new GenderDetailDao(connection);
		GenderType gendertype=new GenderType();
		if(validateGenderType()==true){
				gendertype.setgenderType(GenderType);
				gendertype.setgenderId(GenderId);
				gendertype.setCreatedIp(myIpAddress);
				gendertype.setEnteredBy(myUserId);
				gendertype.setLastModifiedBy(myUserId);
				gendertype.setLastModifiedIp(myIpAddress);
				int status=rdao.insertRecord(gendertype);
				if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "Gender type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		
	}
	public String initializeGenderList() {
		

		begin();
		try {
				populateGenderList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";

		// TODO Auto-generated method stub
		
	}


	public String AddGender() {

		begin();
		try {
				addGenderType();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";

		
	}
	public String editGenderType () {
		begin();
		try {
				editGender();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editGender() throws Exception {
	  GenderDetailDao rdao=new GenderDetailDao(connection);
		GenderType gendertype=new GenderType();
		if(validateGenderType()==true){
			gendertype.setgenderType(GenderType);
			gendertype.setgenderId(GenderId);
			gendertype.setLastModifiedBy(myUserId);
			gendertype.setLastModifiedIp(myIpAddress);
		int status=	rdao.editRecord(gendertype);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Gender type is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}


public String initDeleteGenderType() {
	begin();
	try {
			deletegendertype();
	} catch(Exception e){
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
}

private void deletegendertype() throws Exception {
	GenderDetailDao rdao=new GenderDetailDao(connection);
	GenderType gendertype=new GenderType();

	gendertype.setgenderId(GenderId);	
	gendertype.setgenderType(GenderType);
	int status=rdao.removeRecord(gendertype);
	if(status>0)
		notifyList.add(new utilities.notifications.Notification("Success!!", "Gender type is Deleted successfully.", Status.SUCCESS, Type.BAR));
}






public String getGenderType() {
	return GenderType;
}


public void setGenderType(String genderType) {
	GenderType = genderType;
}

public Integer getCreatedOn() {
	return CreatedOn;
}

public void setCreatedOn(Integer createdOn) {
	CreatedOn = createdOn;
}

public String getRowId() {
	return rowId;
}

public void setRowId(String rowId) {
	this.rowId = rowId;
}





public String getGenderId() {
	return GenderId;
}


public void setGenderId(String genderId) {
	GenderId = genderId;
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


public List<KVPair<String, String>> getGenderTypeList() {
	return genderTypeList;
}


public void setGenderTypeList(List<KVPair<String, String>> genderTypeList) {
	this.genderTypeList = genderTypeList;
}


public List<GenderType> getGenderList() {
	return genderList;
}


public void setGenderList(List<GenderType> genderList) {
	this.genderList = genderList;
}






}
