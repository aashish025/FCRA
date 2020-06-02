package dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utilities.KVPair;
import utilities.ValidationException;
import dao.BaseDao;
import dao.services.dashboard.ProjectDashboardDao;
import models.master.DesignationType;
import models.master.User;
import models.services.ApplicationStatusManagement;
import models.services.requests.ProjectRequest;

public class ApplicationStatusManagementDao extends BaseDao<ProjectRequest, String, String>{	
	private String totalRecords;
	private String currentStatus;
	
	int recordStatus;
	public int getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}

	public ApplicationStatusManagementDao(Connection connection) {
		super(connection);
	}	
	
	 public List<ApplicationStatusManagement> getApplicationManagementListDetails(String searchId, String myOfficeCode ) throws Exception{
		 if(connection == null) {
		 throw new Exception("Invalid connection");
		 }	
		 StringBuffer query = new StringBuffer("SELECT APPLICATION_ID FROM V_APPLICATION_DETAILS WHERE SECTION_FILENO= ? OR APPLICATION_ID=? OR TEMP_FILENO=?");
		 PreparedStatement statement = connection.prepareStatement(query.toString());
		 statement.setString(1, searchId);
		 statement.setString(2, searchId);
		 statement.setString(3, searchId);
		 ResultSet rs = statement.executeQuery();
		 List<ApplicationStatusManagement> applicationStatusManagementList = new ArrayList<ApplicationStatusManagement>();
			 if(rs.next())
		 {
			 String appId = rs.getString(1);
			 StringBuffer query1 = new StringBuffer("SELECT  A.TEMP_FILENO, A.SECTION_FILENO, A.APPLICATION_ID, A.APPLICANT_NAME, (SELECT B.SERVICE_DESC FROM TM_SERVICE B"
			 		+ " where A.SERVICE_CODE=B.SERVICE_CODE ) as SERVICE_NAME,(SELECT B.SERVICE_CODE FROM TM_SERVICE B where A.SERVICE_CODE=B.SERVICE_CODE ) as SERVICE_CODE,"
			 		+ "to_char(A.SUBMISSION_DATE,'dd-mm-yyyy'),CASE WHEN A.CURRENT_STATUS = 10 OR A.CURRENT_STATUS = 12 THEN A.CURRENT_STATUS  WHEN D.STATUS = 10 OR D.STATUS = 12"
			 		+ " THEN D.STATUS else 04 END AS STATUS, (SELECT FILESTATUS_DESC FROM TM_FILESTATUS WHERE FILESTATUS_ID = (CASE WHEN A.CURRENT_STATUS = 10 OR "
			 		+ "A.CURRENT_STATUS = 12 THEN A.CURRENT_STATUS  WHEN D.STATUS = 10 OR D.STATUS = 12 THEN D.STATUS else 04 END)) AS FILESTATUS_DESC from "
			 		+ "V_APPLICATION_DETAILS A, T_PC_OFFICE_LEVEL_FINAL_STATUS D WHERE   A.APPLICATION_ID = D.APPLICATION_ID AND A.APPLICATION_ID=?  AND D.OFFICE_CODE = ?");
			 PreparedStatement statement1 = connection.prepareStatement(query1.toString());
			 statement1.setString(1, appId);
			 statement1.setString(2, myOfficeCode);
			 ResultSet rs1 = statement1.executeQuery();
			 if(rs1.next()) {		      
				 applicationStatusManagementList.add(new ApplicationStatusManagement(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),rs1.getString(5),rs1.getString(6),rs1.getString(7),rs1.getString(8),rs1.getString(9)));	
				 currentStatus=rs1.getString(8);
				// serviceCode=rs.getString(6);
				recordStatus=1;
			 }
			 else{
				 recordStatus=0;
			 }
			 rs.close();
			 statement.close();
		 }
		/* StringBuffer query = new StringBuffer("SELECT  A.TEMP_FILENO, A.SECTION_FILENO, A.APPLICATION_ID, A.APPLICANT_NAME, (SELECT B.SERVICE_DESC FROM TM_SERVICE B where A.SERVICE_CODE=B.SERVICE_CODE ) as SERVICE_NAME,(SELECT B.SERVICE_CODE FROM TM_SERVICE B where A.SERVICE_CODE=B.SERVICE_CODE ) as SERVICE_CODE,to_char(A.SUBMISSION_DATE,'dd-mm-yyyy'), A.CURRENT_STATUS,C.FILESTATUS_DESC  from V_APPLICATION_DETAILS A,TM_FILESTATUS C WHERE   A.CURRENT_STATUS=C.FILESTATUS_ID AND (SECTION_FILENO= '"+searchId+"' OR APPLICATION_ID='"+searchId+"' OR TEMP_FILENO='"+searchId+"')");

 		 PreparedStatement statement = connection.prepareStatement(query.toString());	
		 ResultSet rs = statement.executeQuery();
		 List<ApplicationStatusManagement> applicationStatusManagementList = new ArrayList<ApplicationStatusManagement>();
		 if(rs.next()) {		      
			 applicationStatusManagementList.add(new ApplicationStatusManagement(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));	
			 currentStatus=rs.getString(8);
			// serviceCode=rs.getString(6);
			recordStatus=1;
		 }
		 else{
			 recordStatus=0;
		 }*/
		 
		 
		 //for user list
		 
		 
		 return applicationStatusManagementList;
	}
	 
	//user list 
	
		
		public List<KVPair<String, String>> getUserList(String searchId,String myOfficeCode ) throws Exception {
			PreparedStatement statement=null;
			List<KVPair<String, String>>  userList = new ArrayList<KVPair<String, String>>();
			StringBuffer query=new StringBuffer("SELECT  A.USER_ID, A.USER_NAME||'('||A.USER_ID||')','['||(SELECT DESIGNATION_NAME FROM TM_DESIGNATION B WHERE B.DESIGNATION_ID=A.DESIGNATION_ID)||']'AS DESIGNATION FROM TM_USER A"
	  						+ " WHERE user_id in (SELECT USER_ID FROM TM_USER_SECTION WHERE OFFICE_CODE='"+myOfficeCode+"'"
	  						+ " AND SECTION_ID IN(SELECT SECTION_ID FROM T_PC_SECTION_DETAILS WHERE OFFICE_CODE='"+myOfficeCode+"' AND APPLICATION_ID='"+searchId+"'))");
			  	  			statement = connection.prepareStatement(query.toString());
			  	  			ResultSet rs = statement.executeQuery();
 			  	  			List<String> ulist=new  ArrayList<String>();
				  			while(rs.next()) {	
				  					ulist.add(rs.getString(1));
					  				userList.add(new KVPair<String, String>(rs.getString(1),rs.getString(2)+rs.getString(3)));
						  			}
			  	  			statement.close();
		  		
			  	  			if(ulist.size()==0)
						  			{
			  	  			StringBuffer query3=new StringBuffer("SELECT  A.USER_ID, A.USER_NAME||'('||A.USER_ID||')','['||(SELECT DESIGNATION_NAME FROM TM_DESIGNATION B WHERE B.DESIGNATION_ID=A.DESIGNATION_ID )||']'AS DESIGNATION FROM TM_USER A WHERE A.STATUS_ID='0'  AND A.OFFICE_CODE='"+myOfficeCode+"' AND A.USER_ID!='"+myOfficeCode+"'  order by A.USER_ID");
						  	  			statement = connection.prepareStatement(query3.toString());
						  	  			ResultSet rs3 = statement.executeQuery();
								  			while(rs3.next()) {	
								  				
								  				userList.add(new KVPair<String, String>(rs3.getString(1),rs3.getString(2)+rs3.getString(3)));;			
								  			}
						  	  			statement.close();
						  			}
					return userList;
					
				}
			
	public String ReOpenApplicationStatusManagement(String appId,String serviceCode, String remark,String userId, String myUserId, String myOfficeCode) throws Exception {
	  		PreparedStatement statement=null;
	  		String ofccode="";
	  		String fromUserId="";
	  		
	  		if(connection == null) {
	  			throw new Exception("Invalid connection");
	  		}	
  			connection.setAutoCommit(false);
  			String chatId=null;
  			StringBuffer query7 = new StringBuffer("SELECT FN_GEN_CHATID(?) FROM DUAL");
  			 statement = connection.prepareStatement(query7.toString());	
  			statement.setString(1,myOfficeCode);
  			ResultSet rs2=statement.executeQuery();
  			if(rs2.next())
  				chatId=rs2.getString(1);
  			StringBuffer query=new StringBuffer("UPDATE T_PC_OFFICE_LEVEL_FINAL_STATUS SET STATUS=NULL, CHAT_ID = '"+chatId+"' WHERE APPLICATION_ID ='"+appId+"' AND OFFICE_CODE = '"+myOfficeCode+"'");
  			statement = connection.prepareStatement(query.toString());
  			statement.executeUpdate();
  			statement.close();
  			
  			StringBuffer query1=new StringBuffer("SELECT USER_ID,OFFICE_CODE FROM T_PC_OFFICE_USER_DETAILS WHERE APPLICATION_ID ='"+appId+"' AND OFFICE_CODE = '"+myOfficeCode+"'");
  			statement = connection.prepareStatement(query1.toString());
  			ResultSet rs = statement.executeQuery();
  			if(rs.next()) {
  				fromUserId=rs.getString(1);
  				//ofccode=rs.getString(2);
  			}
  			statement.close();
  			
  			
  			StringBuffer query2 = new StringBuffer("UPDATE T_PC_USER_LEVEL_STATUS SET STAGE_ID=5, CHAT_ID = '"+chatId+"'  WHERE APPLICATION_ID ='"+appId+"' AND OFFICE_CODE='"+myOfficeCode+"' AND USER_ID='"+fromUserId+"' ");
  			statement = connection.prepareStatement(query2.toString());
  			statement.executeUpdate();
  			statement.close();
  			
  			ProjectDashboardDao pdd = new ProjectDashboardDao(connection);
  			Boolean appStatus = pdd.flagCheckPCUserLevelStatus(appId,myOfficeCode,userId);
  			if(appStatus==true)
  				pdd.updatePCUserLevelStatus(appId,myOfficeCode,userId,chatId,"2",appStatus);
  			else 
  				pdd.updatePCUserLevelStatus(appId, myOfficeCode, userId, chatId, "1", appStatus);
  			
  			StringBuffer query3 = new StringBuffer("UPDATE T_PC_OFFICE_LEVEL_STATUS SET STAGE_ID=6, CHAT_ID = '"+chatId+"'  WHERE APPLICATION_ID ='"+appId+"' AND OFFICE_CODE='"+myOfficeCode+"'");
  			statement = connection.prepareStatement(query3.toString());
  			statement.executeUpdate();
  			statement.close();
  			
  			StringBuffer query4 = new StringBuffer("INSERT INTO T_PC_COMMUNICATION(APPLICATION_ID, CHAT_ID, BY_OFFICE_CODE, BY_USERID, TO_OFFICE_CODE, TO_USERID, STATUS_ID, STATUS_DATE, STATUS_REMARK, RECORD_STATUS) VALUES ('"+appId+"', '"+chatId+"', '"+myOfficeCode+"', '"+myUserId+"', '"+myOfficeCode+"', '"+userId+"', 7, sysdate, '"+remark+"', 0)");
  			statement = connection.prepareStatement(query4.toString());
  			statement.executeUpdate();
  			statement.close();
  			
  			StringBuffer query5=new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STATUS=4 WHERE APPLICATION_ID ='"+appId+"'");
  			statement = connection.prepareStatement(query5.toString());
  			statement.executeUpdate();
  			statement.close();
  			
  			String statusRemark = "Your application has been reopened and is under process.";
  			StringBuffer query6=new StringBuffer("INSERT INTO T_APPLICATION_STATUS_NOTIFN(APPLICATION_ID,SERVICE_CODE, APPLICATION_STATUS, STATUS_REMARK,NOTIFIED_ON, RECORD_STATUS) VALUES ('"+appId+"','"+serviceCode+"', 4, '"+statusRemark+"', sysdate, 0)");
  			statement = connection.prepareStatement(query6.toString());
  			statement.executeUpdate();
  			statement.close();	
  			
  			StringBuffer toUserQuery = new StringBuffer("UPDATE T_PC_OFFICE_USER_DETAILS SET USER_ID = ?, CHAT_ID = ?  WHERE APPLICATION_ID = ? AND OFFICE_CODE = ?"); 
  			statement = connection.prepareStatement(toUserQuery.toString());
  			statement.setString(1, userId);
  			statement.setString(2, chatId);
  			statement.setString(3, appId);
  			statement.setString(4, myOfficeCode);
  			statement.executeUpdate();
  			connection.commit();	  	
	  		return "success";	
	  	}		
	@Override
	public Integer insertRecord(ProjectRequest object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer removeRecord(ProjectRequest object) throws Exception {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List<ProjectRequest> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<ProjectRequest> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	
	
}
