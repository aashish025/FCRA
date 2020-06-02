package service.masters;
import java.util.List;

import models.master.SocietyActs;

import org.owasp.esapi.ESAPI;

import dao.master.SocietyActsDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;



public class SocietyActsTypeServices extends Commons{
		private static final String recordStatus = null;
		List<KVPair<String, String>> actTypeList;
		List<SocietyActs> actList;
	    private String pageNum;
		private String recordsPerPage;
		private String sortColumn;
		private String sortOrder;
		private String totalRecords;
	    private Integer CreatedOn;
	    private String actCode;
	   	private String actName;
	  
			   
				
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
		    	 SocietyActsDao bdao=new SocietyActsDao(connection);
		    	 actTypeList=bdao.getKVList();
			}
			
			public String initializeActList() {
				begin();
				try {
						populateActList();
				} catch(Exception e){
					spl(e);
				}
				finally{
					finish();
				}	
				return "success";
			} 

			private void populateActList() throws Exception{
				SocietyActsDao bdao=new SocietyActsDao(connection);
				bdao.setPageNum(pageNum);
				bdao.setRecordsPerPage(recordsPerPage);
				bdao.setSortColumn(sortColumn);
				bdao.setSortOrder(sortOrder);				
				actList=bdao.gettable();
				totalRecords=bdao.getTotalRecords();
			}
			
			public String AddAct() {
				begin();
				try {
						addActdata();
				} 
				catch(Exception e){
					try {
						notifyList.add(new Notification("Error!","Act Code is already in use", Status.ERROR, Type.BAR));
					} catch(Exception ex) {}
				}
				finally{
					finish();
				}	
				return "success";
			}
			
			
			
			public void addActdata() throws Exception{
				SocietyActsDao bdao=new SocietyActsDao(connection);
				SocietyActs act=new SocietyActs();
				if(validateactCode()==true)
				{
				if(validateactName()==true)
				{
				act.setActCode(actCode);
				act.setActName(actName);
				act.setCreatedIp(myIpAddress);
			    act.setRecordStatus(recordStatus);
				act.setCreatedBy(myUserId);
				act.setLastModifiedBy(myUserId);
				act.setLastModifiedIp(myIpAddress);
		      int status=	bdao.insertRecord(act);
				if(status>0)
					notifyList.add(new Notification("Success!!", "Society Act is Inserted successfully.", Status.SUCCESS, Type.BAR));
				}
			}
			}
		
			
			public String EditAct () {
				begin();
				try {
						editact();
				} catch(Exception e){
					ps(e);
				}finally{
					finish();
				}		
				return "success";
			}

			public void editact() throws Exception {
				SocietyActsDao bdao=new SocietyActsDao(connection);
				SocietyActs act=new SocietyActs();
				if(validateactCode()==true)
				{
				if(validateactName()==true)
				{
					act.setActCode(actCode);
					act.setActName(actName);
					act.setLastModifiedBy(myUserId);
			        act.setLastModifiedIp(myIpAddress);
				    int status=	bdao.editRecord(act);
				if(status>0)
					notifyList.add(new Notification("Success!!", "Society Act is Edited successfully.", Status.SUCCESS, Type.BAR));
				}
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
			SocietyActsDao bdao=new SocietyActsDao(connection);
			SocietyActs act=new SocietyActs();
			act.setActCode(actCode);
			act.setActName(actName);
			int status=bdao.removeRecord(act);
			if(status>0)
				notifyList.add(new Notification("Success!!", "Society Act is Deleted successfully.", Status.SUCCESS, Type.BAR));
		}
		
		
		public Boolean validateactName() throws Exception{
			if(ESAPI.validator().isValidInput("Society Name", actName, "AlphaS", 100, false) == false){
					notifyList.add(new Notification("Error!!", "Society Act - Only alphabet allowed (100 characters max).", Status.ERROR, Type.BAR));
					return false;
				}
				return true;
			}
		public Boolean validateactCode() throws Exception{
			if(ESAPI.validator().isValidInput("Society Name", actCode, "Num", 2, false) == false){
					notifyList.add(new Notification("Error!!", "Society Act - Only Number allowed (2 number max).", Status.ERROR, Type.BAR));
					return false;
				}
				return true;
			}




		public List<KVPair<String, String>> getActTypeList() {
			return actTypeList;
		}



		public void setActTypeList(List<KVPair<String, String>> actTypeList) {
			this.actTypeList = actTypeList;
		}



		public List<SocietyActs> getActList() {
			return actList;
		}



		public void setActList(List<SocietyActs> actList) {
			this.actList = actList;
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



		public String getActCode() {
			return actCode;
		}



		public void setActCode(String actCode) {
			this.actCode = actCode;
		}



		public String getActName() {
			return actName;
		}



		public void setActName(String actName) {
			this.actName = actName;
		}



		public static String getRecordstatus() {
			return recordStatus;
		}
		
			
	}


