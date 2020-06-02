package service.masters;

import java.sql.SQLException;
import java.util.List;
import org.owasp.esapi.ESAPI;
import dao.master.DistrictDao;
import dao.master.StateDao;
import models.master.District;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class DistrictService extends Commons{
	private List<District> districtList;
	List<KVPair<String, String>> snameList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String dname;
	private Integer dcode;
	private Integer scode;
	private String createdBy;
	
	
	public String execute() {
			begin();
			try {
				initDistrictList();
				} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}
	 public void initDistrictList() throws Exception{
		 StateDao ddao=new StateDao(connection);
	         snameList=ddao.getKVList();
		}
	 
 	public String initializeDistrictList() {
		begin();
		try {
				populateDistrictList();
				
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateDistrictList() throws Exception{
		DistrictDao tdao=new DistrictDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			districtList=tdao.getMainDistrict();
			totalRecords=tdao.getTotalRecords();
		}
	
	public Boolean validateDistrict() throws Exception{		
		if(ESAPI.validator().isValidInput("Dname", dname, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "District Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddDistrict() {
		begin();
		try {
				addDistrict();
		} catch(Exception e){
			try{	notifyList.add(new Notification("Error!","District Code is already in use", Status.ERROR, Type.BAR));
			}catch(Exception ex) {}
			try {
				if (connection != null)
					connection.rollback();
			} catch (SQLException e2) {
				ps(e2);
			}
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	}  
	private void addDistrict() throws Exception{
		DistrictDao ndao=new DistrictDao(connection);
		District district=new District();
		if(validateDistrict()==true){
			district.setDcode(dcode);
			district.setDname(dname);
			district.setScode(scode);
			district.setCreatedIp(myIpAddress);
			district.setCreatedBy(myUserId);
			district.setLastModifiedBy(myUserId);
			district.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(district);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New District <b>"+dname.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initDeleteDistrict() {
		begin();
		try {
				deleteDistrict();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void deleteDistrict() throws Exception{
		DistrictDao ndao=new DistrictDao(connection);
		District district=new District();
		district.setDcode(dcode);
		int status=ndao.removeRecord(district);
		if(status>0){
			notifyList.add(new Notification("Success!!", "District <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditDistrict() {
		begin();
		try {
				editDistrict();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void editDistrict() throws Exception{
		DistrictDao ndao=new DistrictDao(connection);
		District district=new District();
		if(validateDistrict()==true){
			district.setDcode(dcode);
			district.setDname(dname);
			district.setScode(scode);
			district.setCreatedIp(myIpAddress);
			district.setCreatedBy(myUserId);
			district.setLastModifiedBy(myUserId);
			district.setLastModifiedIp(myIpAddress);
			int i=ndao.editRecord(district);
		if(i>0)
			notifyList.add(new Notification("Success!!", " District <b>"+dname.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
				}
	}
	
	
	public List<District> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<District> districtList) {
		this.districtList = districtList;
	}
	public List<KVPair<String, String>> getSnameList() {
		return snameList;
	}
	public void setSnameList(List<KVPair<String, String>> snameList) {
		this.snameList = snameList;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public Integer getDcode() {
		return dcode;
	}
	public void setDcode(Integer dcode) {
		this.dcode = dcode;
	}
	public Integer getScode() {
		return scode;
	}
	public void setScode(Integer scode) {
		this.scode = scode;
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}

