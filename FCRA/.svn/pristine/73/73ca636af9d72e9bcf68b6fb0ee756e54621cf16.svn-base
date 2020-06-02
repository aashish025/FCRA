package dao.services;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import utilities.KVPair;
import models.services.Grievance;
import dao.BaseDao;

public class GrievanceDao extends BaseDao<Grievance, String, String>{
	private String myOfficeCode;
	private String applicationId;
	private byte[] attachment=null;

	public GrievanceDao(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	private String generateApplicationId(String offCode) throws Exception {
		String applicationId = "";
		StringBuffer query = new StringBuffer("SELECT FN_GEN_APPLICATION_ID(?,'12') FROM DUAL");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, offCode);
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			applicationId = rs.getString(1);
		}
		return applicationId;		
	}
	@Override
	public Integer insertRecord(Grievance object) throws Exception {
	   int rows1=0,rows2=2,rows = 0;
		String appId="";
		 appId=generateApplicationId(object.getMyOfficeCode());
		 setApplicationId(appId);
         PreparedStatement statement1=null;
         PreparedStatement statement2=null;
         PreparedStatement statement3=null;
         PreparedStatement statement4=null;
			if(connection == null) {
				throw new Exception("Invalid connection");
			}	
	       StringBuffer query = new StringBuffer("INSERT INTO T_GRIEVANCES(APPLICATION_ID,ASSO_REGISTERED_FLAG,ASSO_RCN,ASSO_NAME,ASSO_STATE,ASSO_DISTRICT,ASSO_ADDRESS,ASSO_TOWN_CITY, "
						+ "ASSO_PIN,GRIEVANCE_DESCRIPTION,COMPLAINANT_NAME,COMPLAINANT_ADDRESS,COMPLAINANT_EMAIL,COMPLAINANT_MOBILE,FILE_CREATED_DATE,FINAL_SUBMISSION_DATE,CREATED_BY)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?)");
				statement1 = connection.prepareStatement(query.toString());	
				statement1.setString(1,appId);
				statement1.setString(2,object.getCheckedvalue());
		      	statement1.setString(3, object.getRegistrationId());
				statement1.setString(4, object.getAssoName());	
		    	statement1.setString(5,object.getState());
				statement1.setString(6, object.getDistrict());
				statement1.setString(7, object.getAssoAddress());
				statement1.setString(8, object.getTownCity());
				statement1.setString(9, object.getAssoPincode());
				statement1.setString(10, object.getComplaint());
				statement1.setString(11, object.getComName());
				statement1.setString(12, object.getComAddress());
				statement1.setString(13, object.getComEmail());
				statement1.setString(14, object.getComMobile());
				statement1.setString(15, object.getMyUserId());
				 rows1=statement1.executeUpdate();
				statement1.close();
				 if(rows1>0){
	    				StringBuffer query1 = new StringBuffer("INSERT INTO T_APPLICATION_STAGE_DETAILS(APPLICATION_ID,CURRENT_STAGE,CURRENT_STATUS)VALUES(?,'2','4')");
	    				statement2 = connection.prepareStatement(query1.toString());		
	    				statement2.setString(1,appId);				
	    				 rows2=statement2.executeUpdate();
	    				statement2.close();
				 }
	    				if(rows2>0){
	    					
	    					String sectionId="";
	    					StringBuffer querySection = new StringBuffer("SELECT  SECTION_ID FROM TM_PC_SERVICE_SECTION WHERE OFFICE_CODE=? AND SERVICE_CODE='12'");
	    					PreparedStatement stmt = connection.prepareStatement(querySection.toString());
	    					stmt.setString(1, object.getMyOfficeCode());
	    					ResultSet rs = stmt.executeQuery();
	    					while(rs.next()) {
	    						sectionId=rs.getString(1);		
	    							}
	    							stmt.close();
	    					StringBuffer query2= new StringBuffer("INSERT INTO T_PC_SECTION_DETAILS(APPLICATION_ID,OFFICE_CODE,SECTION_ID) VALUES(?,?,?)");
	    					statement3= connection.prepareStatement(query2.toString());		
		    				statement3.setString(1,appId);		
		    				statement3.setString(2,object.getMyOfficeCode());
		    				statement3.setString(3,sectionId);
		    				 rows=statement3.executeUpdate();
		    				statement3.close();
	    					
	    				}
	    				if(rows>0){
	    					String folderDate=null,date=null,month=null,year=null;
	    					File certDir=null,file=null;
	    					int count=0;
	    				    	StringBuffer query6=new StringBuffer("SELECT to_char(CREATED_ON, 'dd-mm-yyyy') from V_APPLICATION_DETAILS2 "
	    				    			+ "where application_id=?");
	    				    	statement4 = connection.prepareStatement(query6.toString());
	    				    	statement4.setString(1, appId);
		    	                ResultSet rs3=statement4.executeQuery();
		    	                if(rs3.next()){
		    	                        folderDate=rs3.getString(1);
		    	                }
		    	                 String[] parts=folderDate.split("-");
		    	                 date=parts[0];
		    	                 month=parts[1];
		    	                 year=parts[2];
		    	                 //Creating file
		    	                certDir = new File("/image/FCRAdocs/"+year+"/"+month+"/"+date+"/12/"+appId);
		    	              //   certDir = new File("E:\\image\\FCRADocs\\"+year+"\\"+month+"\\"+date);
		    	             if (!certDir.exists()) {
		    	                     certDir.mkdirs();
		    	                 }
		    	             file = new File("/image/FCRAdocs/"+year+"/"+month+"/"+date+"/12/"+appId+"/"+appId+"_01.pdf");
		    	            // file = new File("E:\\image\\FCRADocs\\"+year+"\\"+month+"\\"+date+"\\"+appId+"_"+count+".pdf");
		    	             if (!file.exists()) {
		    	                     file.createNewFile();
		    	                 }
		    	             FileOutputStream fop = new FileOutputStream(file);
		    	              MultipartFile document;
		    	             document=object.getDocumentFile();
		    	             attachment=document.getBytes();
		    	             fop.write(attachment);
		    	             fop.flush();
		    	             fop.close();
	    				}
				 
		
		return rows;
	}
	 public List<KVPair<String, String>> IbListfromOffice() throws Exception{
			if(connection == null) {
				throw new Exception("Invalid connection");
			}				
			StringBuffer query = new StringBuffer("SELECT  OFFICE_CODE,OFFICE_NAME  FROM TM_OFFICE WHERE OFFICE_ID in(2,3) AND RECORD_STATUS='0'");
			PreparedStatement statement = connection.prepareStatement(query.toString());				
			ResultSet rs = statement.executeQuery();
			List<KVPair<String, String>>  IbList = new ArrayList<KVPair<String, String>>();
			while(rs.next()) {			
				KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
				IbList.add(temp);			
			}
			return IbList;
		}


	@Override
	public Integer removeRecord(Grievance object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Grievance> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KVPair<String, String>> getKVList(List<Grievance> list) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getMyOfficeCode() {
		return myOfficeCode;
	}
	public void setMyOfficeCode(String myOfficeCode) {
		this.myOfficeCode = myOfficeCode;
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
    
	

}
