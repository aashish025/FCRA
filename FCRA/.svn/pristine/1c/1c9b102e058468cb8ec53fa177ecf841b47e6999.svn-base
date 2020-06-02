package service.masters;

import java.sql.SQLException;
import java.util.List;
import org.owasp.esapi.ESAPI;
import models.master.State;
import utilities.Commons;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import dao.master.StateDao;

public class StateService extends Commons{
	private List<State> stateList;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String sname;
	private String createdBy;
	private String scode;
	private Integer lcode;
	
	public String initializeStateList() {
		begin();
		try {
				populateStateList();
		} catch(Exception e){
			e.getMessage();
			ps(e);
		}
		finally{
			finish();
		}	
		return "success";
	} 
	
	private void populateStateList() throws Exception{
		StateDao tdao=new StateDao(connection);
			tdao.setPageNum(pageNum);
			tdao.setRecordsPerPage(recordsPerPage);
			tdao.setSortColumn(sortColumn);
			tdao.setSortOrder(sortOrder);				
			stateList=tdao.getMainState();
			totalRecords=tdao.getTotalRecords();
		}
		
	public Boolean validateState() throws Exception{		
		if(ESAPI.validator().isValidInput("Sname", sname, "WordS", 50, false) == false){
			notifyList.add(new Notification("Error!!", "State Name - Only aplphabets and numbers allowed (50 characters max).", Status.ERROR, Type.BAR));
			return false;
		}
		return true;
	}
	
	public String initAddState() {
		begin();
		try {
				addState();
		} catch(Exception e){
			try{	notifyList.add(new Notification("Error!","State Code is already in use", Status.ERROR, Type.BAR));
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
	
	private void addState() throws Exception{
		StateDao ndao=new StateDao(connection);
		State state=new State();
		if(validateState()==true){
			state.setScode(scode);
			state.setSname(sname);
			state.setLcode(lcode);
			state.setCreatedIp(myIpAddress);
			state.setCreatedBy(myUserId);
			state.setLastModifiedBy(myUserId);
			state.setLastModifiedIp(myIpAddress);		
		int i=ndao.insertRecord(state);
		if(i>0)
			notifyList.add(new Notification("Success!!", "New State <b>"+sname.toUpperCase()+"</b> is inserted successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}
	
	public String initDeleteState() {
		begin();
		try {
				deleteState();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void deleteState() throws Exception{
		StateDao ndao=new StateDao(connection);
		State  state=new State();
		state.setScode(scode);	
		int status=ndao.removeRecord(state);
		if(status>0){
			notifyList.add(new Notification("Success!!", "State <b></b> is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
	}
	
	public String initEditState() {
		begin();
		try {
				editState();
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}		
		return "success";
	}  
	
	private void editState() throws Exception{
		StateDao ndao=new StateDao(connection);
		State state=new State();
		if(validateState()==true){
			state.setScode(scode);
			state.setSname(sname);
			state.setLcode(lcode);
			state.setCreatedIp(myIpAddress);
			state.setCreatedBy(myUserId);
			state.setLastModifiedBy(myUserId);
			state.setLastModifiedIp(myIpAddress);
				int i=ndao.editRecord(state);
		if(i>0)
			notifyList.add(new Notification("Success!!", " State <b>"+sname.toUpperCase()+"</b> is Edited successfully.", Status.SUCCESS, Type.BAR));
		
		}
	}


	public List<State> getStateList() {
		return stateList;
	}

	public void setStateList(List<State> stateList) {
		this.stateList = stateList;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public Integer getLcode() {
		return lcode;
	}

	public void setLcode(Integer lcode) {
		this.lcode = lcode;
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

