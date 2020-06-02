package service.masters;
import java.util.List;

import models.master.AssociationNature;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.AssociationNatureDao;


public class AssociationNatureTypeService extends Commons{
	private static final String recordStatus = null;
		List<KVPair<String, String>> natureTypeList;
		List<AssociationNature> natureList;
	    private String pageNum;
		private String recordsPerPage;
		private String sortColumn;
		private String sortOrder;
		private String totalRecords;
	    private Integer CreatedOn;
	    private String natureCode;
	   	private String natureName;
	  
	   
		
		 public String execute() {
			begin();
			try {
					initNatureTypeList();
			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}

		
				 
	     public void initNatureTypeList() throws Exception{
	    	 AssociationNatureDao bdao=new AssociationNatureDao(connection);
	    	 natureTypeList=bdao.getKVList();
		}
		
		public String initializeNatureList() {
			begin();
			try {
					populateNatureList();
			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		} 

		private void populateNatureList() throws Exception{
			AssociationNatureDao bdao=new AssociationNatureDao(connection);
			bdao.setPageNum(pageNum);
			bdao.setRecordsPerPage(recordsPerPage);
			bdao.setSortColumn(sortColumn);
			bdao.setSortOrder(sortOrder);				
			natureList=bdao.gettable();
			totalRecords=bdao.getTotalRecords();
		}
		
		public String AddNature() {
			begin();
			try {
					addnaturedata();
			} catch(Exception e){
				try {
					notifyList.add(new Notification("Error!","Religion Code is already in use", Status.ERROR, Type.BAR));
				}catch(Exception ex) {}
			}
			finally{
				finish();
			}	
			return "success";
		}
		
		
		
		public void addnaturedata() throws Exception{
			AssociationNatureDao bdao=new AssociationNatureDao(connection);
			AssociationNature nature=new AssociationNature();
			if(validatenature()==true)
				{
			nature.setNatureName(natureName);
			nature.setNatureCode(natureCode);
			nature.setCreatedIp(myIpAddress);
		    nature.setRecordStatus(recordStatus);
			nature.setCreatedBy(myUserId);
			nature.setLastModifiedBy(myUserId);
			nature.setLastModifiedIp(myIpAddress);
	      int status=	bdao.insertRecord(nature);
			if(status>0)
				notifyList.add(new Notification("Success!!", "Nature  Description is Inserted successfully.", Status.SUCCESS, Type.BAR));
			}
		}
	
		
		public String EditNature () {
			begin();
			try {
					editnature();
			} catch(Exception e){
				ps(e);
			}finally{
				finish();
			}		
			return "success";
		}

		public void editnature() throws Exception {
			AssociationNatureDao bdao=new AssociationNatureDao(connection);
			AssociationNature nature=new AssociationNature();
			if(validatenature()==true)
			{
			nature.setNatureCode(natureCode);
			nature.setNatureName(natureName);
			nature.setLastModifiedBy(myUserId);
		    nature.setLastModifiedIp(myIpAddress);
			int status=	bdao.editRecord(nature);
			if(status>0)
				notifyList.add(new Notification("Success!!", " Nature  Description is Edited successfully.", Status.SUCCESS, Type.BAR));
			}
		}
		
	public String DeleteNature() {
		begin();
		try {
				deletenature();
		} catch(Exception e){
			e.printStackTrace();
		}
		finally{
			finish();
		}	
		return "success";
	}

	private void deletenature() throws Exception {
		AssociationNatureDao bdao=new AssociationNatureDao(connection);
		AssociationNature nature=new AssociationNature();
		nature.setNatureCode(natureCode);
		nature.setNatureName(natureName);
		int status=bdao.removeRecord(nature);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Nature Description is Deleted successfully.", Status.SUCCESS, Type.BAR));
	}
	
	
	public Boolean validatenature() throws Exception{
		if(ESAPI.validator().isValidInput("Nature Name", natureName, "AlphaS", 15, false) == false){
				notifyList.add(new Notification("Error!!", "Nature  Description - Only alphabet allowed (15 characters max).", Status.ERROR, Type.BAR));
				return false;
			}
			return true;
		}
	public List<KVPair<String, String>> getNatureTypeList() {
		return natureTypeList;
	}
	public void setNatureTypeList(List<KVPair<String, String>> natureTypeList) {
		this.natureTypeList = natureTypeList;
	}
	public List<AssociationNature> getNatureList() {
		return natureList;
	}
	public void setNatureList(List<AssociationNature> natureList) {
		this.natureList = natureList;
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
	public String getNatureCode() {
		return natureCode;
	}
	public void setNatureCode(String natureCode) {
		this.natureCode = natureCode;
	}
	public String getNatureName() {
		return natureName;
	}
	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	public static String getRecordstatus() {
		return recordStatus;
	}

		
}
