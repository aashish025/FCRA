package service.masters;
import java.util.List;
import models.master.ContributionNature;
import org.owasp.esapi.ESAPI;
import dao.master.ContributionNatureDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;


public class ContributionNatureTypeService extends Commons{
	


	
			private static final String recordStatus = null;
			List<KVPair<String, String>> contributionTypeList;
			List<ContributionNature> contributionList;
		    private String pageNum;
			private String recordsPerPage;
			private String sortColumn;
			private String sortOrder;
			private String totalRecords;
		    private Integer CreatedOn;
		    private String contributionType;
		   	private String contributionName;
		  
				   
					
				 public String execute() {
					begin();
					try {
							initContributionTypeList();
					} catch(Exception e){
						spl(e);
					}
					finally{
						finish();
					}	
					return "success";
				}

				
						 
			     public void initContributionTypeList() throws Exception{
			    	 ContributionNatureDao bdao=new ContributionNatureDao(connection);
			    	 contributionTypeList=bdao.getKVList();
				}
				
				public String initializeContributionList() {
					begin();
					try {
							populatecontributionList();
					} catch(Exception e){
						spl(e);
					}
					finally{
						finish();
					}	
					return "success";
				} 

				private void populatecontributionList() throws Exception{
					ContributionNatureDao bdao=new ContributionNatureDao(connection);
					bdao.setPageNum(pageNum);
					bdao.setRecordsPerPage(recordsPerPage);
					bdao.setSortColumn(sortColumn);
					bdao.setSortOrder(sortOrder);				
					contributionList=bdao.gettable();
					totalRecords=bdao.getTotalRecords();
				}



				
				public String AddContribution() {
					begin();
					try {
							addcontributiondata();
					} 
					catch(Exception e){
						try {
							notifyList.add(new Notification("Error!","Contribution Type is already in use", Status.ERROR, Type.BAR));
						} catch(Exception ex) {}
					}
					finally{
						finish();
					}	
					return "success";
				}
				
			
				public void addcontributiondata() throws Exception{
					 ContributionNatureDao bdao=new ContributionNatureDao(connection);
					 ContributionNature contribution=new ContributionNature();
					 if(validateContributionCode()==true)
						{
						if(validateContributionName()==true)
						{
				    contribution.setContributionType(contributionType);
					contribution.setContributionName(contributionName);
					contribution.setCreatedIp(myIpAddress);
				    contribution.setRecordStatus(recordStatus);
					contribution.setCreatedBy(myUserId);
					contribution.setLastModifiedBy(myUserId);
					contribution.setLastModifiedIp(myIpAddress);
			      int status=	bdao.insertRecord(contribution);
					if(status>0)
						notifyList.add(new Notification("Success!!", "Contribution Name is Inserted successfully.", Status.SUCCESS, Type.BAR));
					}
						}
				}
			
				
				public String EditContribution () {
					begin();
					try {
							editcontribution();
					} catch(Exception e){
						ps(e);
					}finally{
						finish();
					}		
					return "success";
				}

				public void editcontribution() throws Exception {
					 ContributionNatureDao bdao=new ContributionNatureDao(connection);
					 ContributionNature contribution=new ContributionNature();
					if(validateContributionCode()==true)
					{
					if(validateContributionName()==true)
					{
					 contribution.setContributionType(contributionType);
						contribution.setContributionName(contributionName);
						contribution.setLastModifiedBy(myUserId);
				        contribution.setLastModifiedIp(myIpAddress);
					    int status=	bdao.editRecord(contribution);
					if(status>0)
						notifyList.add(new Notification("Success!!", "Contribution Name is Edited successfully.", Status.SUCCESS, Type.BAR));
					}
					}
				}
				
			public String DeleteContribution() {
				begin();
				try {
						deletecontribution();
				} catch(Exception e){
					e.printStackTrace();
				}
				finally{
					finish();
				}	
				return "success";
			}

			private void deletecontribution() throws Exception {
				 ContributionNatureDao bdao=new ContributionNatureDao(connection);
				 ContributionNature contribution=new ContributionNature();
				contribution.setContributionType(contributionType);
				contribution.setContributionName(contributionName);
				int status=bdao.removeRecord(contribution);
				if(status>0)
					notifyList.add(new Notification("Success!!", "Contribution Name is Deleted successfully.", Status.SUCCESS, Type.BAR));
			}
			
			
		public Boolean validateContributionName() throws Exception{
				if(ESAPI.validator().isValidInput("Contribution Name ", contributionName, "AlphaS", 100, false) == false){
						notifyList.add(new Notification("Error!!", "Contribution Name - Only alphabet allowed (100 characters max).", Status.ERROR, Type.BAR));
						return false;
					}
					return true;
				}
			public Boolean validateContributionCode() throws Exception{
				if(ESAPI.validator().isValidInput("Contribution Type", contributionType, "Num", 2, false) == false){
						notifyList.add(new Notification("Error!!", "Contribution Type - Only Number allowed (2 number max).", Status.ERROR, Type.BAR));
						return false;
					}
					return true;
				}




				public List<KVPair<String, String>> getContributionTypeList() {
					return contributionTypeList;
				}



				public void setContributionTypeList(
						List<KVPair<String, String>> contributionTypeList) {
					this.contributionTypeList = contributionTypeList;
				}



				public List<ContributionNature> getContributionList() {
					return contributionList;
				}



				public void setContributionList(List<ContributionNature> contributionList) {
					this.contributionList = contributionList;
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



				public String getContributionType() {
					return contributionType;
				}



				public void setContributionType(String contributionType) {
					this.contributionType = contributionType;
				}



				public String getContributionName() {
					return contributionName;
				}



				public void setContributionName(String contributionName) {
					this.contributionName = contributionName;
				}



				public static String getRecordstatus() {
					return recordStatus;
				}







			
				
		}





