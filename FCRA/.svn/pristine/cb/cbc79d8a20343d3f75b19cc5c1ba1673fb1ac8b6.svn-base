package service.masters;

import java.util.List;
import models.master.OfficeType;
import org.owasp.esapi.ESAPI;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.OfficeTypeDao;

public class OfficeTypeServices  extends Commons{


	
	private static final String recordStatus = null;
	List<KVPair<String, String>> genderTypeList;
	List<OfficeType> officeList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String officeType;  
	private String officeName; 
	public String  officeId;   
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
    	 OfficeTypeDao rdao=new OfficeTypeDao(connection);
     genderTypeList=rdao.getKVList();
	}
	

	
	private void populateOfficeList() throws Exception{
		OfficeTypeDao adao=new OfficeTypeDao(connection);
		adao.setPageNum(pageNum);
		adao.setRecordsPerPage(recordsPerPage);
		adao.setSortColumn(sortColumn);
		adao.setSortOrder(sortOrder);	
		officeList=adao.getMasteroffice();
		totalRecords=adao.getTotalRecords();
	}

public Boolean validateOfficeType() throws Exception{		
		if(ESAPI.validator().isValidInput("OfficeType", officeType, "Word", 5, false) == false){
			notifyList.add(new Notification("Error!!", "Office Type - Only Aplphabets and numbers allowed (5 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		if(ESAPI.validator().isValidInput("OfficeType", officeName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Office Name - Only Aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	public void addOfficeType() throws Exception{
		OfficeTypeDao rdao=new OfficeTypeDao(connection);
		OfficeType office=new OfficeType();
		if(validateOfficeType()==true){
			office.setOfficeType(officeType);
			office.setOfficeName(officeName);	
			office.setCreatedIp(myIpAddress);
			office.setCreatedBy(myUserId);
			office.setLastModifiedBy(myUserId);
			office.setLastModifiedIp(myIpAddress);
				int status=rdao.insertRecord(office);
				if(status>0)
				notifyList.add(new utilities.notifications.Notification("Success!!", "Office type is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		
	}
	public String initializeOfficeList() {
		
   begin();
		try {
			populateOfficeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";

		// TODO Auto-generated method stub
		
	}


	public String AddOffice() {

		begin();
		try {
				addOfficeType();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";

		
	}
	public String editOffice () {
		begin();
		try {
				editOfficeType();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}

	public void editOfficeType() throws Exception {
	  OfficeTypeDao rdao=new OfficeTypeDao(connection);
		OfficeType office=new OfficeType();
		if(validateOfficeType()==true){
			office.setOfficeId(officeId);
			office.setOfficeType(officeType);
			System.out.println("Serviceeeeeeeeeeeeee  "+officeName);
			office.setOfficeName(officeName);
			office.setLastModifiedBy(myUserId);
			office.setLastModifiedIp(myIpAddress);
		int status=	rdao.editRecord(office);
		if(status>0)
			notifyList.add(new utilities.notifications.Notification("Success!!", "Office type is Edited successfully.", Status.SUCCESS, Type.BAR));
		}
	}


public String initDeleteOfficeType() {
	begin();
	try {
			deleteofficetype();
	} catch(Exception e){
		ps(e);
	}
	finally{
		finish();
	}	
	return "success";
}

private void deleteofficetype() throws Exception {
	OfficeTypeDao rdao=new OfficeTypeDao(connection);
	OfficeType officetype=new OfficeType();
    officetype.setOfficeId(officeId);
    System.out.println("printttttttttttttt   "+officeId);
	//officetype.setOfficeType(officeType);
	int status=rdao.removeRecord(officetype);
	if(status>0)
		notifyList.add(new utilities.notifications.Notification("Success!!", "Office type is Deleted successfully.", Status.SUCCESS, Type.BAR));
}



public String getOfficeType() {
	return officeType;
}


public void setOfficeType(String officeType) {
	this.officeType = officeType;
}


public String getOfficeName() {
	return officeName;
}


public void setOfficeName(String officeName) {
	this.officeName = officeName;
}


public String getOfficeId() {
	return officeId;
}


public void setOfficeId(String officeId) {
	this.officeId = officeId;
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


public List<OfficeType> getOfficeList() {
	return officeList;
}


public void setOfficeList(List<OfficeType> officeList) {
	this.officeList = officeList;
}



}
