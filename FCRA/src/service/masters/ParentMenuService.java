package service.masters;
import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.ParentMenu;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.ParentMenuDao;
public class ParentMenuService extends Commons{
	private List<ParentMenu> parentList;
	private List<ParentMenu> parentMenuList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String pmenuName;
	private String createdBy;
	private Short pmenuId;
	public String initializeParentMenuList() {
		begin();
		try {
				populateParentMenuList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateParentMenuList() throws Exception{
	
		ParentMenuDao tdao=new ParentMenuDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			parentMenuList=tdao.getMainPMenu();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validateParentMenu() throws Exception{		
		if(ESAPI.validator().isValidInput("PmenuName", pmenuName, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Parent Menu Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddParent() {
		begin();
		try {
				addParent();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	
	private void addParent() throws Exception{
		
		ParentMenuDao ndao=new ParentMenuDao(connection);
		ParentMenu parentMenu=new ParentMenu();
		if(validateParentMenu()==true){
			parentMenu.setPmenuName(pmenuName);
		
			parentMenu.setCreatedIp(myIpAddress);
			parentMenu.setCreatedBy(myUserId);
			parentMenu.setLastModifiedBy(myUserId);
			parentMenu.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(parentMenu);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New Parent Menu <b>"+pmenuName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	public String initDeleteParent() {
		begin();
		try {
				deleteParent();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteParent() throws Exception{
		ParentMenuDao ndao=new ParentMenuDao(connection);
		ParentMenu  parentMenu=new ParentMenu();
		parentMenu.setPmenuId(pmenuId);	
		int status=ndao.removeRecord(parentMenu);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Parent Menu <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	public String initEditParent() {
		begin();
		try {
				editParent();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editParent() throws Exception{
		
		ParentMenuDao ndao=new ParentMenuDao(connection);
		ParentMenu parentMenu=new ParentMenu();
		if(validateParentMenu()==true){
			parentMenu.setPmenuId(pmenuId);
			parentMenu.setPmenuName(pmenuName);
			parentMenu.setCreatedIp(myIpAddress);
			parentMenu.setCreatedBy(myUserId);
			parentMenu.setLastModifiedBy(myUserId);
			parentMenu.setLastModifiedIp(myIpAddress);
		
		int i=ndao.editRecord(parentMenu);
		if(i>0)
			notifyList.add(new Notification("Success!!", " Parent Menu <b>"+pmenuName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}

	public Short getPmenuId() {
		return pmenuId;
	}

	public void setPmenuId(Short pmenuId) {
		this.pmenuId = pmenuId;
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

	public List<ParentMenu> getParentList() {
		return parentList;
	}

	public void setParentList(List<ParentMenu> parentList) {
		this.parentList = parentList;
	}

	public List<ParentMenu> getParentMenuList() {
		return parentMenuList;
	}

	public void setParentMenuList(List<ParentMenu> parentMenuList) {
		this.parentMenuList = parentMenuList;
	}

	public String getPmenuName() {
		return pmenuName;
	}

	public void setPmenuName(String pmenuName) {
		this.pmenuName = pmenuName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
