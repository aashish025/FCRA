package service.masters;

import java.util.List;

import models.master.Service;

import org.owasp.esapi.ESAPI;

import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.ServicesDao;

public class ServicesTypeDetailsServices extends Commons{
private static final Boolean recordStatus = null;
	
	List<KVPair<String, String>> servicesTypeList;
	List<Service> servicesList;
    private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String servicesDesc;
	private String servicesCode;
	
	public String execute() {
		begin();
		try {
				initServicesTypeList();
		} catch(Exception e){
			spl(e);
		}
		finally{
			finish();
		}	
		return "success";
	}
	public Boolean validateServices() throws Exception{		
		if(ESAPI.validator().isValidInput("Services Description ",servicesDesc, "AlphaS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Services Description - Only aplphabets allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	 public void initServicesTypeList() throws Exception{
			ServicesDao rdao=new ServicesDao(connection);
			servicesTypeList=rdao. getKVList();
		}
	 public String initializeServiceList() {
			begin();
			try {
					populateserviceList();
			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		} 

		private void populateserviceList() throws Exception{
			ServicesDao rdao=new ServicesDao(connection);
			rdao.setPageNum(pageNum);
			rdao.setRecordsPerPage(recordsPerPage);
			rdao.setSortColumn(sortColumn);
			rdao.setSortOrder(sortOrder);				
			servicesList=rdao.gettable();
			totalRecords=rdao.getTotalRecords();
		}
		public String AddServices() {
			begin();
			try {
					addserviceDesc();
			} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}
	
		public void addserviceDesc() throws Exception{
			ServicesDao rdao=new ServicesDao(connection);
			Service services=new Service();
		if(validateServices()==true){
          	services.setServicesCode(servicesCode); 
          	services.setServicesDesc(servicesDesc);
          	services.setCreatedBy(myUserId);
          	services.setCreatedIp(myIpAddress);
          	services.setRecordStatus(recordStatus);
          	services.setLastModifiedBy(myUserId);
          	services.setLastModifiedIp(myIpAddress);
	      int status=	rdao.insertRecord(services);
			if(status>0)
				notifyList.add(new Notification("Success!!", "Services Description is Inserted successfully.", Status.SUCCESS, Type.BAR));
			}
		}
		public String EditServices() {
			begin();
			try {
					editservices();
			} catch(Exception e){
				ps(e);
			}finally{
				finish();
			}		
			return "success";
		}

		public void editservices() throws Exception {
			ServicesDao rdao=new ServicesDao(connection);
			Service services=new Service();
			if(validateServices()==true){
			services.setServicesCode(servicesCode); 
          	services.setServicesDesc(servicesDesc);
			services.setLastModifiedBy(myUserId);
		    services.setLastModifiedIp(myIpAddress);
			int status=	rdao.editRecord(services);
			if(status>0)
				notifyList.add(new Notification("Success!!", "Services Description is Edited successfully.", Status.SUCCESS, Type.BAR));
			

		}
		}
		
	public String DeleteServices() {
		begin();
		try {
				deleteservices();
		} catch(Exception e){
			e.printStackTrace();
		}
		finally{
			finish();
		}	
		return "success";
	}

	private void deleteservices() throws Exception {
		ServicesDao rdao=new ServicesDao(connection);
		Service services=new Service();
		services.setServicesCode(servicesCode); 
		int status=rdao.removeRecord(services);
		if(status>0)
			notifyList.add(new Notification("Success!!", "Services Description is Deleted successfully.", Status.SUCCESS, Type.BAR));
	}
	
		public List<KVPair<String, String>> getServicesTypeList() {
		return servicesTypeList;
	}
	public List<Service> getServicesList() {
			return servicesList;
		}
	public void setServicesTypeList(List<KVPair<String, String>> servicesTypeList) {
		this.servicesTypeList = servicesTypeList;
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
	public String getServicesDesc() {
		return servicesDesc;
	}
	public void setServicesDesc(String servicesDesc) {
		this.servicesDesc = servicesDesc;
	}
	public static Boolean getRecordstatus() {
		return recordStatus;
	}
	public String getServicesCode() {
		return servicesCode;
	}
	public void setServicesCode(String servicesCode) {
		this.servicesCode = servicesCode;
	}
	public void setServicesList(List<Service> servicesList) {
		this.servicesList = servicesList;
	}
}
