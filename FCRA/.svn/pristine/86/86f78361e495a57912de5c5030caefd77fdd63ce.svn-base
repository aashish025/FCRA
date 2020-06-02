package service.masters;

import java.util.List;
import org.owasp.esapi.ESAPI;
import dao.master.ParentMenuDao;
import dao.master.SubMenuDao;
import models.master.SubMenu;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class SubMenuService extends Commons{
	private List<SubMenu> subList;
	private List<SubMenu> subMenuList;
	List<KVPair<String, String>> pmenuNameList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String smenuName;
	private String actionPath;
	private String pMenuId;
	private String createdBy;
	private Short smenuId;
	
	public String execute() {
			begin();
			try {
				initParentMenuList();
				} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}
	 public void initParentMenuList() throws Exception{
		 ParentMenuDao ddao=new ParentMenuDao(connection);
	         pmenuNameList=ddao.getKVList();
		}
	 
 	public String initializeSubMenuList() {
		begin();
		try {
				populateSubMenuList();
				
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateSubMenuList() throws Exception{
			SubMenuDao tdao=new SubMenuDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			subMenuList=tdao.getMainSubMenu();
			totalRecords=tdao.getTotalRecords();
		}
	
	public Boolean validateSubMenu() throws Exception{		
		if(ESAPI.validator().isValidInput("SmenuName", smenuName, "RoomUtil", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Sub Menu Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddSubMenu() {
		begin();
		try {
				addSubMenu();
		} catch(Exception e){
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	private void addSubMenu() throws Exception{
		SubMenuDao ndao=new SubMenuDao(connection);
		SubMenu subMenu=new SubMenu();
		if(validateSubMenu()==true){
			subMenu.setSmenuName(smenuName);
			subMenu.setActionPath(actionPath);
			subMenu.setpMenuId(pMenuId);
			subMenu.setCreatedIp(myIpAddress);
			subMenu.setCreatedBy(myUserId);
			subMenu.setLastModifiedBy(myUserId);
			subMenu.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(subMenu);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New Sub Menu <b>"+smenuName.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initDeleteSubMenu() {
		begin();
		try {
				deleteSubMenu();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void deleteSubMenu() throws Exception{
		SubMenuDao ndao=new SubMenuDao(connection);
		SubMenu subMenu=new SubMenu();
		subMenu.setSmenuId(smenuId);
		int status=ndao.removeRecord(subMenu);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Sub Menu <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditSubMenu() {
		begin();
		try {
				editSubMenu();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void editSubMenu() throws Exception{
		SubMenuDao ndao=new SubMenuDao(connection);
		SubMenu subMenu=new SubMenu();
		if(validateSubMenu()==true){
			subMenu.setSmenuId(smenuId);
			subMenu.setSmenuName(smenuName);
			subMenu.setActionPath(actionPath);
			subMenu.setpMenuId(pMenuId);
			subMenu.setCreatedIp(myIpAddress);
			subMenu.setCreatedBy(myUserId);
			subMenu.setLastModifiedBy(myUserId);
			subMenu.setLastModifiedIp(myIpAddress);
			int i=ndao.editRecord(subMenu);
		if(i>0)
			notifyList.add(new Notification("Success!!", "Sub Menu <b>"+smenuName.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
				}
	}
	
	public Short getSmenuId() {
		return smenuId;
	}
	public void setSmenuId(Short smenuId) {
		this.smenuId = smenuId;
	}
	public String getPageNum() {
		return pageNum;
	}
	public String getSmenuName() {
		return smenuName;
	}
	public void setSmenuName(String smenuName) {
		this.smenuName = smenuName;
	}
	public String getActionPath() {
		return actionPath;
	}
	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}
	public String getpMenuId() {
		return pMenuId;
	}
	public void setpMenuId(String pMenuId) {
		this.pMenuId = pMenuId;
	}
	public List<KVPair<String, String>> getPmenuNameList() {
		return pmenuNameList;
	}
	public void setPmenuNameList(List<KVPair<String, String>> pmenuNameList) {
		this.pmenuNameList = pmenuNameList;
	}
	public List<SubMenu> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<SubMenu> subMenuList) {
		this.subMenuList = subMenuList;
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
	public List<SubMenu> getSubList() {
		return subList;
	}
	public void setSubList(List<SubMenu> subList) {
		this.subList = subList;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}

