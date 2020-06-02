package service.masters;

import java.sql.SQLException;
import java.util.List;

import org.owasp.esapi.ESAPI;

import dao.master.BlockYearDao;
import models.master.BlockYear;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class BlockYearService extends Commons{
	private List<BlockYear> blockYearList;
	/*List<KVPair<String, String>> snameList;*/
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String status;
	private String blkyr;
	private String createdBy;
		
	/*public String execute() {
			begin();
			try {
				//initBlockYearList();
				} catch(Exception e){
				spl(e);
			}
			finally{
				finish();
			}	
			return "success";
		}*/
	/* public void initBlockYearList() throws Exception{
		 StateDao ddao=new StateDao(connection);
	         snameList=ddao.getKVList();
		}*/
	 
 	public String initializeBlockYearList() {
		begin();
		try {
				populateBlockYearList();
				
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	private void populateBlockYearList() throws Exception{
		BlockYearDao tdao=new BlockYearDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			blockYearList=tdao.getMainBlockYear();
			totalRecords=tdao.getTotalRecords();
		}
	
	public Boolean validateBlockYear() throws Exception{		
		if(ESAPI.validator().isValidInput("Blkyr", blkyr, "NumSS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "Block Year - Only numbers(Year) allowed.", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
		public Boolean validateInput() throws Exception{
		String year1=blkyr.substring(0,4);
		String year2=blkyr.substring(5,9);
		int byear= Integer.parseInt(year2)-Integer.parseInt(year1);
		if(byear!=1){
			notifyList.add(new Notification("Error!!", "Block Year- Please Enter a Valid Year.", Status.ERROR, Type.BAR));
			return false;  
		}
		
		return true;
	}
	
	public String initAddBlockYear() {
		begin();
		try {
				addBlockYear();
		} catch(Exception e){
			try{	notifyList.add(new Notification("Error!","Block Year is already in use", Status.ERROR, Type.BAR));
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
	
	private void addBlockYear() throws Exception{
		BlockYearDao ndao=new BlockYearDao(connection);
		BlockYear blockyr=new BlockYear();
		if(validateBlockYear()==true && validateInput()==true){
			blockyr.setBlkyr(blkyr);
			blockyr.setStatus(status);
			blockyr.setCreatedIp(myIpAddress);
			blockyr.setCreatedBy(myUserId);
			blockyr.setLastModifiedBy(myUserId);
			blockyr.setLastModifiedIp(myIpAddress);		
			Integer x=ndao.insertRecord(blockyr);
		if(x!=null){
			notifyList.add(new Notification("Success!!", "New Block Year <b>"+blkyr+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		}
		else {
			notifyList.add(new Notification("Info!!", " Check Staus Value it should be 'Selection' ", Status.INFORMATION, Type.BAR));
				
				}}
		/*if(i>0)
			notifyList.add(new Notification("Success!!", "New Block Year <b>"+blkyr+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		}*/
	}
	
	public String initDeleteBlockYear() {
		begin();
		try {
				deleteBlockYear();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void deleteBlockYear() throws Exception{
		BlockYearDao ndao=new BlockYearDao(connection);
		BlockYear blockyr=new BlockYear();
		blockyr.setBlkyr(blkyr);
		int status=ndao.removeRecord(blockyr);
		if(status>0){
			notifyList.add(new Notification("Success!!", "Block Year <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditBlockYear() {
		begin();
		try {
				editBlockYear();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	private void editBlockYear() throws Exception{
		BlockYearDao ndao=new BlockYearDao(connection);
		BlockYear blockyr=new BlockYear();
		if(validateBlockYear()==true){
			blockyr.setBlkyr(blkyr);
			blockyr.setStatus(status);
			blockyr.setCreatedIp(myIpAddress);
			blockyr.setCreatedBy(myUserId);
			blockyr.setLastModifiedBy(myUserId);
			blockyr.setLastModifiedIp(myIpAddress);
			Integer x=ndao.editRecord(blockyr);
		/*if(i>0)
			notifyList.add(new Notification("Success!!", " Block Year <b>"+blkyr+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
				}
	}*/
	if(x!=null){
		notifyList.add(new Notification("Success!!", " Block Year <b>"+blkyr+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
	}
	else {
		notifyList.add(new Notification("Info!!", " Check Staus Value it should be 'Selection' ", Status.INFORMATION, Type.BAR));
			
			}}
	}
	
	public String getBlkyr() {
		return blkyr;
	}
	public void setBlkyr(String blkyr) {
		this.blkyr = blkyr;
	}
	public List<BlockYear> getBlockYearList() {
		return blockYearList;
	}
	public void setBlockYearList(List<BlockYear> blockYearList) {
		this.blockYearList = blockYearList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

