package service.services.dashboard.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import dao.services.dashboard.ProjectDashboardDao;
import utilities.Commons;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import utilities.ValidationException;
import utilities.communication.AutoNotifier;
import utilities.communication.mail.MailException;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;

public class DashboardReportService extends Commons {
	private String appId;
	private String regNumber;
	private String tsPath=null;
	private String fileName=null;
	private byte[] attachment=null;
	private String svcCode=null;
	private String mailContent="";	
	private String hosPdfFormat;
	private	String	email=null;
	private String mobile;
	public String  reportQuery     = "";
	private int virtualizationMaxSize = 200;
	private Map  parameters = new HashMap();
	private String digitalSignStatus=null;
	private String assoMobile;	

	public byte[] initGenerateReport(Connection connection,String svcCode){		
		try {	
				try{
					//begin();
					String query="SELECT USER_NAME,(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER WHERE "
							+ "USER_ID=?)) AS DESG,(SELECT CONTACT_NO FROM TM_OFFICE WHERE OFFICE_CODE=?) AS CONTACT,(SELECT ADDRESS FROM TM_OFFICE WHERE OFFICE_CODE=?) AS address,"
							+ " (SELECT CITY_NAME FROM TM_OFFICE WHERE OFFICE_CODE=?) AS city ,( select ZIPCODE FROM TM_OFFICE WHERE OFFICE_CODE=?) AS pinCode "
							+ "FROM TM_USER WHERE USER_ID=?";
					statement=connection.prepareStatement(query);
					statement.setString(1, myUserId);
					statement.setString(2, myOfficeCode);
					statement.setString(3, myOfficeCode);
					statement.setString(4, myOfficeCode);
					statement.setString(5, myOfficeCode);
					statement.setString(6, myUserId);
					ResultSet rs=statement.executeQuery();
					while(rs.next()){
						parameters.put("officerName", rs.getString(1));
						parameters.put("officerDesg", rs.getString(2));
						parameters.put("officerContact", rs.getString(3));
						parameters.put("officeAddress", rs.getString(4));
						parameters.put("officeCity", rs.getString(5));
						parameters.put("officePinCode", rs.getString(6));
					}
					statement.close();
					rs.close();
				 }catch (Exception e) {					
				}
				finally{	
					//finish();
				}	
				String status=getApplicationFinalStatus();
				if(status.equals("9")){
					String othRmark="";
			 		
			 		StringBuffer query9 = new StringBuffer("SELECT OTHER_REMARK  FROM T_PC_COMMUNICATION  WHERE APPLICATION_ID=? ORDER BY STATUS_DATE DESC");
			 		statement = connection.prepareStatement(query9.toString());
					statement.setString(1, appId);
					ResultSet rs = statement.executeQuery();
					if(rs.next()){
						othRmark=rs.getString(1);
					}
					rs.close();
			 		statement.close();
			 		
			 		parameters.put("otherRemark", othRmark);
			 		parameters.put("servoceCode", svcCode);
					
					
					
					if(svcCode.equals("01")){
						prepareRegistrationQuery(connection);												
						String query1="SELECT RCN FROM FC_INDIA WHERE SECTION_FILENO IN(SELECT SECTION_FILENO FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?)";
						statement=connection.prepareStatement(query1);
						statement.setString(1, appId);						
						ResultSet rs10=statement.executeQuery();
						if(rs10.next()){
							parameters.put("regNumber", rs10.getString(1).substring(0, rs10.getString(1).length()-1));							
						}	
						parameters.put("appId", appId);
						statement.close();
						rs10.close();												
						tsPath="/Reports/Registration-Dashboard.jrxml";
				 		fileName = "Registration-Certificate.pdf";	
					}else if(svcCode.equals("02")){						
						StringBuffer insQuery = new StringBuffer("SELECT count(1) FROM T_PP_INSTALLMENT_DETAILS WHERE APPLICATION_ID=?");
				 		statement = connection.prepareStatement(insQuery.toString()); 			
				 		statement.setString(1, appId);
				 		ResultSet insRS=statement.executeQuery(); 
				 		Boolean existFlag=false;
				 		if(insRS.next()){
				 			if(insRS.getInt(1) > 0)
				 				existFlag=true; 			
				 		} 
				 		insRS.close();
				 		statement.close();
				 		
						preparePriorPermissionQuery(connection,existFlag);				
						parameters.put("appId", appId);
						parameters.put("regNumber", regNumber);
						if(existFlag==true)
							tsPath="/Reports/PriorPermission_INS.jrxml";
						else
							tsPath="/Reports/PriorPermission.jrxml";
						
				 		fileName = "PriorPermission-Certificate.pdf";
					}else if(svcCode.equals("03")){
						prepareRenewalQuery(connection);				
						parameters.put("appId", appId);						
						tsPath="/Reports/Renewal.jrxml";
				 		fileName = "Renewal-Certificate.pdf";
					}
					/* Chagedetails */
					else if(svcCode.equals("06")){
						prepareChangeofDetailQuery(connection);
						parameters.put("appId", appId);
						tsPath="/Reports/ChangeDetailTextOld.jrxml";
				 		fileName = "ChangeDetail-Certificate.pdf";	
						
					}
					else if(svcCode.equals("07")){ 
						prepareHospitalityQuery(connection);
						parameters.put("appId", appId);	
						if(hosPdfFormat.equals("1"))
							tsPath="/Reports/Hospitality.jrxml";
						else if(hosPdfFormat.equals("2"))
							tsPath="/Reports/Hospitality_Memo.jrxml";
				 		fileName = "Hospitality-Certificate.pdf";
					}
					else if(svcCode.equals("13")){  //Change of NAME and ADDRESS FC_6A
						prepareChangeofNameAndAddressQuery(connection);
						parameters.put("appId", appId);	
						parameters.put("FC6","FC-6A");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}							
					else if(svcCode.equals("15")){  //CHANGE OF  FC RECEIPT-CUM-UTILISATION BANK FC_6C
						prepareChangeOfFcRecieptCumUtilisationQuery(connection);
						parameters.put("appId", appId);	
						parameters.put("FC6","FC-6C (Designate FC receipt-cum-utilization bank account)");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}
					else if(svcCode.equals("16")){  //Change of OPENING OF UTILIZATION BANK ACCOUNT FC_6D
						prepareChangeofOpeningOfUtilisationBankAccountQuery(connection);
						parameters.put("appId", appId);	
						parameters.put("FC6","FC-6D (Additional FC - utilization account)");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}	
					else if(svcCode.equals("17")){  //Change of CHANGE OF COMMITTEE MEMBERS FC_6E
						prepareChangeofCommitteeMembersQuery(connection);
						parameters.put("appId", appId);
						parameters.put("FC6","FC-6E");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}					
				}
				else if(status.equals("10")){
					if(svcCode.equals("01")){
						prepareRegistrationRejectQuery(connection);
						parameters.put("appId", appId);
						tsPath="/Reports/RejectionLetter.jrxml";
				 		fileName = "Refusal-Letter.pdf";		
					}
					else if(svcCode.equals("02")){
						preparePriorPermissionRejectQuery(connection);
						parameters.put("appId", appId);
						tsPath="/Reports/PriorPermissionRefusalLetter.jrxml";
				 		fileName = "Refusal-Letter.pdf";		
					}
				}				
				attachment=GeneratePdfVirtualizer.asAttachmentBytes(tsPath, parameters, connection, fileName);
						 		
		} catch(Exception e){
			ps(e);
		}
		return attachment;
	}
	
	public String initGenerateReport(){	
		begin();
		try {					
				String query="SELECT USER_NAME,(SELECT DESIGNATION_NAME FROM TM_DESIGNATION WHERE DESIGNATION_ID=(SELECT DESIGNATION_ID FROM TM_USER WHERE "
						+ "USER_ID=?)) AS DESG,(SELECT CONTACT_NO FROM TM_OFFICE WHERE OFFICE_CODE=?) AS CONTACT ,(SELECT ADDRESS FROM TM_OFFICE WHERE OFFICE_CODE=?) AS address,"
						+ " (SELECT CITY_NAME FROM TM_OFFICE WHERE OFFICE_CODE=?) AS city ,( select ZIPCODE FROM TM_OFFICE WHERE OFFICE_CODE=?) AS pinCode "
						+ "FROM TM_USER WHERE USER_ID=?";
				statement=connection.prepareStatement(query);
				statement.setString(1, myUserId);
				statement.setString(2, myOfficeCode);
				statement.setString(2, myOfficeCode);
				statement.setString(3, myOfficeCode);
				statement.setString(4, myOfficeCode);
				statement.setString(5, myOfficeCode);
				statement.setString(6, myUserId);
		
				ResultSet rs=statement.executeQuery();
				while(rs.next()){
					parameters.put("officerName", rs.getString(1));
					parameters.put("officerDesg", rs.getString(2));
					parameters.put("officerContact", rs.getString(3));
					/*parameters.put("officeAddress", rs.getString(4));
					parameters.put("officeCity", rs.getString(5));
					parameters.put("officePinCode", rs.getString(6));*/
					parameters.put("officeAddress", rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6));
					
				}	
				String status=getApplicationFinalStatus();

				if(status.equals("9")){
					String othRmark="";
			 		
			 		StringBuffer query9 = new StringBuffer("SELECT OTHER_REMARK  FROM T_PC_COMMUNICATION  WHERE APPLICATION_ID=? ORDER BY STATUS_DATE DESC");
			 		statement = connection.prepareStatement(query9.toString());
					statement.setString(1, appId);
					ResultSet rs0 = statement.executeQuery();
					if(rs0.next()){
						othRmark=rs0.getString(1);
					}
					rs0.close();
			 		statement.close();
			 		
			 		parameters.put("otherRemark", othRmark);
			 		parameters.put("servoceCode", svcCode);
					
					
					if(svcCode.equals("01")){
						String query1="SELECT RCN FROM FC_INDIA WHERE SECTION_FILENO IN(SELECT SECTION_FILENO FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?)";
						statement=connection.prepareStatement(query1);
						statement.setString(1, appId);						
						ResultSet rs1=statement.executeQuery();
						if(rs1.next()){
							parameters.put("regNumber", rs1.getString(1).substring(0, rs1.getString(1).length()-1));							
						}
						statement.close();
						rs1.close();
						prepareRegistrationQuery(connection);				
						parameters.put("appId", appId);						
						tsPath="/Reports/Registration-Dashboard.jrxml";
				 		fileName = "Registration-Certificate";	
					}else if(svcCode.equals("02")){
						StringBuffer insQuery = new StringBuffer("SELECT count(1) FROM T_PP_INSTALLMENT_DETAILS WHERE APPLICATION_ID=?");
				 		statement = connection.prepareStatement(insQuery.toString()); 			
				 		statement.setString(1, appId);
				 		ResultSet insRS=statement.executeQuery(); 
				 		Boolean existFlag=false;
				 		if(insRS.next()){
				 			if(insRS.getInt(1) > 0)
				 				existFlag=true; 			
				 		} 
				 		insRS.close();
				 		statement.close();
				 		
						preparePriorPermissionQuery(connection,existFlag);				
						parameters.put("appId", appId);
						parameters.put("regNumber", regNumber);
						
						if(existFlag==true)
							tsPath="/Reports/PriorPermission_INS.jrxml";
						else
							tsPath="/Reports/PriorPermission.jrxml";
				 		fileName = "PriorPermission-Certificate";
					}else if(svcCode.equals("03")){
						prepareRenewalQuery(connection);				
						parameters.put("appId", appId);						
						tsPath="/Reports/Renewal.jrxml";
				 		fileName = "Renewal-Certificate";
					}
					//for Change of detail//
					else if(svcCode.equals("06")){
						prepareChangeofDetailQuery(connection);
						parameters.put("appId", appId);						
						tsPath="/Reports/ChangeDetailTextOld.jrxml";
				 		fileName = "ChangeDetail-Certificate";						
					}
					
					else if(svcCode.equals("07")){ 
						prepareHospitalityQuery(connection);						
						parameters.put("appId", appId);	
						if(hosPdfFormat.equals("1"))
							tsPath="/Reports/Hospitality.jrxml";
						else if(hosPdfFormat.equals("2"))
							tsPath="/Reports/Hospitality_Memo.jrxml";
				 		fileName = "Hospitality-Certificate";
					}
					else if(svcCode.equals("13")){  //Change of NAME and ADDRESS FC_6A
						prepareChangeofNameAndAddressQuery(connection);
						parameters.put("appId", appId);	
						parameters.put("FC6","FC-6A");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}							
					else if(svcCode.equals("15")){  //CHANGE OF  FC RECEIPT-CUM-UTILISATION BANK FC_6C
						prepareChangeOfFcRecieptCumUtilisationQuery(connection);
						parameters.put("appId", appId);	
						parameters.put("FC6","FC-6C (Designate FC receipt-cum-utilization bank account)");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}
					else if(svcCode.equals("16")){  //Change of OPENING OF UTILIZATION BANK ACCOUNT FC_6D
						prepareChangeofOpeningOfUtilisationBankAccountQuery(connection);
						parameters.put("appId", appId);	
						parameters.put("FC6","FC-6D (Additional FC - utilization account)");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}	
					else if(svcCode.equals("17")){  //Change of CHANGE OF COMMITTEE MEMBERS FC_6E
						prepareChangeofCommitteeMembersQuery(connection);
						parameters.put("appId", appId);
						parameters.put("FC6","FC-6E");
						tsPath="/Reports/ChangeDetailText.jrxml";
				 		fileName = "ChangeDetail-Certificate";											
					}					
				}else if(status.equals("10")){
					if(svcCode.equals("01")){
						prepareRegistrationRejectQuery(connection);
						parameters.put("appId", appId);
						tsPath="/Reports/RejectionLetter.jrxml";
				 		fileName = "Refusal-Letter.pdf";		
					}else if(svcCode.equals("02")){
						preparePriorPermissionRejectQuery(connection);
						parameters.put("appId", appId);
						tsPath="/Reports/PriorPermissionRefusalLetter.jrxml";
				 		fileName = "Refusal-Letter.pdf";		
					}
				}		
				GeneratePdfVirtualizer.asInline(tsPath, parameters, connection, fileName);
		} catch(Exception e){
			ps(e);
		}finally{
			finish();
		}
		return null;
	}
	
	public String GetSignedCertificate(HttpServletResponse response){		
		try 
		{
		  begin();		      
		/*ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		  HttpServletResponse response = attr.getResponse();		  
		  	String req  = "SELECT DOCUMENT_CONTET From T_CERTIFICATE_DETAILS WHERE APPLICATION_ID = ? AND SERVICE_CODE=?" ;			
			statement = connection.prepareStatement (req);
			statement.setString(1, appId);
			statement.setString(2, svcCode);
			resultSet  = statement.executeQuery (); 
			while (resultSet.next ()){    
			  pdf = resultSet.getBlob(1);
			  pdfDATA = pdf.getBytes(1,(int)pdf.length());
			} 	
			response.reset();
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=Certificate.pdf");
			out = response.getOutputStream();
			out.write(pdfDATA); */
		  	String query  = "SELECT to_char(CERTIFICATE_DATE,'dd-mm-yyyy'),DOC_SERIAL_NO From T_CERTIFICATE_DETAILS WHERE APPLICATION_ID = ? AND SERVICE_CODE=? "
		  					+ "AND DOC_SERIAL_NO=(SELECT MAX(DOC_SERIAL_NO) FROM T_CERTIFICATE_DETAILS WHERE APPLICATION_ID =? AND SERVICE_CODE=?)" ;			
			statement = connection.prepareStatement (query);
			statement.setString(1, appId);
			statement.setString(2, svcCode);
			statement.setString(3, appId);
			statement.setString(4, svcCode);
			resultSet  = statement.executeQuery();
			String creationDate=null,date=null,month=null,year=null,docSerialNo=null;
			if(resultSet.next()){
				creationDate=resultSet.getString(1);
				docSerialNo=resultSet.getString(2);
			}	
			String[] parts=creationDate.split("-");
			date=parts[0];
			month=parts[1];
			year=parts[2];
			
			String settings = "attachment;filename="+appId+"_"+docSerialNo+".pdf";
			response.setHeader("Content-Disposition",settings);
			response.setContentType("application/pdf");
			ServletOutputStream servletOutputStream=null;   
			servletOutputStream=response.getOutputStream();
			String filePath = "/image/FCRAcertificates/"+year+"/"+month+"/"+date+"/"+appId+"_"+docSerialNo+".pdf";			
		    byte[] buf = new byte[20];
		    File file=new File(filePath);
		    InputStream inputStream = new FileInputStream(file);
			for(int br = inputStream.read(buf);br > -1;br=inputStream.read(buf)){
				servletOutputStream.write(buf, 0 , br);
			}		
	        servletOutputStream.flush();
	        servletOutputStream.close();
	        inputStream.close();
			
			return null;
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
		finally	{
			try{	
				finish();  
			}
			catch (Exception sqle){
				sqle.printStackTrace();
			}			
		}		
		return null;
	}
	
	public void insertFinalStatus(String status) throws Exception{	
		String officeChatId=null,userChatId=null;
		StringBuffer query = new StringBuffer("SELECT CHAT_ID FROM T_PC_OFFICE_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.setString(2, myOfficeCode);
		ResultSet rs=statement.executeQuery();
		if(rs.next())
			officeChatId=rs.getString(1);
		
		StringBuffer query1 = new StringBuffer("SELECT CHAT_ID FROM T_PC_USER_LEVEL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=? AND USER_ID=?");
		statement = connection.prepareStatement(query1.toString());		
		statement.setString(1, appId);
		statement.setString(2, myOfficeCode);
		statement.setString(3, myUserId);
		ResultSet rs1=statement.executeQuery();
		if(rs1.next())
			userChatId=rs1.getString(1);
	
		ProjectDashboardDao pdd=new ProjectDashboardDao(connection);			
		pdd.updatePCUserLevelStatus(appId,myOfficeCode,myUserId,userChatId,"4",true);			
		pdd.updatePCOfficeLevelStatus(appId,myOfficeCode,"4",officeChatId,true);			
		
		// Insert Signed Certificate row with NULL BLOB in t_certificate_details
		int count=0;
		String folderDate=null,date=null,month=null,year=null;
		File certDir=null,file=null;
		StringBuffer query5=new StringBuffer("SELECT DOC_SERIAL_NO FROM T_CERTIFICATE_DETAILS WHERE APPLICATION_ID=? ORDER BY CERTIFICATE_DATE DESC");
		statement = connection.prepareStatement(query5.toString());
		statement.setString(1, appId);
		ResultSet rs2=statement.executeQuery();
		if(rs2.next()){
			count=rs2.getInt(1);
		}
		rs2.close();
		count=count+1;
		
		StringBuffer query2 = new StringBuffer("INSERT INTO T_CERTIFICATE_DETAILS(APPLICATION_ID,SERVICE_CODE,CERTIFICATE_DATE,DOCUMENT_CONTENT,RECORD_STATUS,DOC_SERIAL_NO) "
				+ "VALUES(?,(SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?),sysdate,null,0,?)");
		statement = connection.prepareStatement(query2.toString());
		statement.setString(1, appId);
		statement.setString(2, appId);	
		statement.setInt(3, count);	
		statement.execute(); 
		
		// Write file to OS level	
		
		StringBuffer query6=new StringBuffer("SELECT to_char(sysdate,'dd-mm-yyyy') from dual");
		statement = connection.prepareStatement(query6.toString());
		ResultSet rs3=statement.executeQuery();
		if(rs3.next()){
			folderDate=rs3.getString(1);
		}
		 String[] parts=folderDate.split("-");
		 date=parts[0];
		 month=parts[1];
		 year=parts[2];
		 
		 //Creating file
		 certDir = new File("/image/FCRAcertificates/"+year+"/"+month+"/"+date);
	     if (!certDir.exists()) {
	    	 certDir.mkdirs();
		 }
	     file = new File("/image/FCRAcertificates/"+year+"/"+month+"/"+date+"/"+appId+"_"+count+".pdf");
	     if (!file.exists()) {
	    	 file.createNewFile();
		 }
	     FileOutputStream fop = new FileOutputStream(file);
	     fop.write(attachment);
	     fop.flush();
	     fop.close();
	     
	    // updating Blob in t_certificate_details 
		/*StringBuffer query4 = new StringBuffer("UPDATE T_CERTIFICATE_DETAILS SET DOCUMENT_CONTENT=? WHERE APPLICATION_ID=? AND DOCUMENT_CONTENT IS NULL");
		statement = connection.prepareStatement(query4.toString());
		statement.setBytes(1, attachment);
		statement.setString(2, appId);		
		statement.execute();*/

		// UPDATE CURRENT_STATUS IN T_APPLICATION_DETAILS			
		StringBuffer query3 = new StringBuffer("UPDATE T_APPLICATION_STAGE_DETAILS SET CURRENT_STATUS=? WHERE APPLICATION_ID=?");
		statement = connection.prepareStatement(query3.toString());
		statement.setString(1, status);
		statement.setString(2, appId);
		statement.executeUpdate();				
	}
	
	private String getApplicationFinalStatus() throws Exception{
		String status=null;
		StringBuffer query = new StringBuffer("SELECT STATUS FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE APPLICATION_ID=? AND OFFICE_CODE=?");
		statement = connection.prepareStatement(query.toString());		
		statement.setString(1, appId);
		statement.setString(2, myOfficeCode);		
		ResultSet rs=statement.executeQuery();
		if(rs.next())
			status=rs.getString(1);		
		return status;
	}
	
	public String checkDigitalSignFlagStatus() throws Exception{		
		begin();
		try {				
				StringBuffer query = new StringBuffer("SELECT DSC_FLAG FROM TM_DEFAULT_PARAMS");
				statement = connection.prepareStatement(query.toString());			
				ResultSet rs=statement.executeQuery();
				if(rs.next())
					digitalSignStatus=rs.getString(1);	
		}catch(Exception e){
			try {					
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{			
			finish();
		}		
		return "success";
	}
	
	public String generateBytes(){
		begin();
		try {
			attachment=initGenerateReport(connection,svcCode);
		}catch(Exception e){
			try {					
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{			
			finish();
		}		
		return "success";
	}
	
	public String initMail() throws Exception{
		begin();
		try {
				int appStatus=0;
				connection.setAutoCommit(false);
				String status=getApplicationFinalStatus();				
				insertFinalStatus(status);	
				/* Without Sign*/
					//attachment=initGenerateReport(connection,svcCode);
				/* Without Sign*/				
				prepareMail(status);
		 		AutoNotifier notifier=new AutoNotifier();
		 		notifier.setAttachment(attachment);
		 		notifier.setAttachmentName(fileName);
		 		if(status.equals("9"))
		 			appStatus=1;
		 		else if(status.equals("10"))
		 			appStatus=3;
		 		
		 		if(email!=null){
		 			notifyList=notifier.pushAutoNotifications(appId, appStatus, "2", email, connection,myOfficeCode,mailContent);
		 		}		 		
		 		if(mobile!=null){
		 			notifier.setPhoneNumber(mobile);
		 			notifyList=notifier.pushAutoNotifications(appId, appStatus, "1", "", connection,myOfficeCode,"");
		 		}
		 		if(email!=null || mobile!=null){
					if(notifyList.get(0).getS().equals("e")){
						connection.rollback();
					}else{
						connection.commit();
					}
		 		}else{
		 			connection.commit();
		 	}				
		}catch(ValidationException ve){			
			try {	
					connection.rollback();
					notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					connection.rollback();
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{			
			finish();
		}		
		return "success";
	}
	
	public String initMailWithOutSign() throws Exception{
		begin();
		try {
				int appStatus=0;
				connection.setAutoCommit(false);
				String status=getApplicationFinalStatus();
				attachment=initGenerateReport(connection,svcCode);
				insertFinalStatus(status);											
				prepareMail(status);
		 		AutoNotifier notifier=new AutoNotifier();
		 		notifier.setAttachment(attachment);
		 		notifier.setAttachmentName(fileName);
		 		
		 		if(status.equals("9"))
		 			appStatus=1;
		 		else if(status.equals("10"))
		 			appStatus=3;
		 		
		 		if(email!=null){
		 			notifyList=notifier.pushAutoNotifications(appId, appStatus, "2", email, connection,myOfficeCode,mailContent);
		 		}		 		
		 		if(mobile!=null){
		 			notifier.setPhoneNumber(mobile);
		 			notifyList=notifier.pushAutoNotifications(appId, appStatus, "1", "", connection,myOfficeCode,"");
		 		} 
		 		if(email!=null || mobile!=null){
					if(notifyList.get(0).getS().equals("e")){
						connection.rollback();
					}else{
						connection.commit();
					}
		 		}else{
		 			connection.commit();
		 		}
		}catch(ValidationException ve){			
			try {	
					connection.rollback();
					notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {
					connection.rollback();
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
			return "error";
		}
		finally{			
			finish();
		}		
		return "success";
	}
	
	private void prepareRegistrationQuery(Connection connection) throws Exception{	
 		reportQuery="SELECT ASSO_NAME,ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=(SELECT ASSO_STATE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"'))||', ' || (SELECT DISTNAME FROM TM_DISTRICT "
				+ " WHERE DISTCODE=(SELECT ASO_DISTRICT FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"'))||', ' || ASSO_PIN,to_char(REG_DATE,'dd-mm-yyyy') as REG_DATE,(SELECT ACCOUNT_NO FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"') AS ACC_NO,"
				+ "(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=(SELECT BANK_NAME FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT BANK_ADDRESS FROM T_FC8_ENTRY "
				+ "WHERE UNIQUE_FILENO='"+appId+"') || ', ' || (SELECT BANK_TOWNCITY FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"') || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=(SELECT BANK_STATE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(SELECT BANK_DISTRICT FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT BANK_PIN FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"') AS BANK,(SELECT USERNAME FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"') AS USER_NAME, "
				+ "(SELECT to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy') FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"')  FROM FC_INDIA WHERE RCN="
				+ "(SELECT RCN FROM FC_INDIA WHERE SECTION_FILENO IN(SELECT SECTION_FILENO FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"') and rownum=1 )";
		statement = connection.prepareStatement(reportQuery); 		
		ResultSet rsReg=statement.executeQuery();	 
		if(rsReg.next()){			
			parameters.put("assoName", rsReg.getString(1));			
			parameters.put("assoAddress", rsReg.getString(2));
			parameters.put("regDate", rsReg.getString(3));			
			parameters.put("accNumber", rsReg.getString(4));
			parameters.put("bankDetails", rsReg.getString(5));
			parameters.put("userName", rsReg.getString(6));
			parameters.put("submissionDate", rsReg.getString(7));
		}
		StringBuffer query7 = new StringBuffer("SELECT ASSO_NATURE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?");
 		statement = connection.prepareStatement(query7.toString()); 			
 		statement.setString(1, appId);
 		ResultSet rs4=statement.executeQuery();
 		String assoNature=null;
 		if(rs4.next()){
 			assoNature=rs4.getString(1);
 		} 
 		String assoNatureDesc="";
 		ResultSet rs5=null;
 		StringBuffer query8 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
 		statement = connection.prepareStatement(query8.toString());
 		for(int i=0;i<assoNature.length();i++){
 			String natureCode = assoNature.substring(i, i+1);
		statement.setString(1, natureCode);
		String delim=(i==0?"":",");
		rs5 = statement.executeQuery();
		if(rs5.next()){
			assoNatureDesc=assoNatureDesc+delim+rs5.getString(1);
		}
		rs5.close();
 		}
 		statement.close();
 		parameters.put("assoNature", assoNatureDesc);
 		
 		
 		
	}
	
	private void prepareRenewalQuery(Connection connection) throws Exception{
		reportQuery="SELECT ASSO_NAME,ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT "
				+ " WHERE DISTCODE=ASSO_DISTRICT)||', ' || ASSO_PIN_CODE,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,(SELECT ACCOUNT_NO FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"') AS ACC_NO,"
				+ "(SELECT BANK_NAME FROM TM_BANKS  WHERE to_char(BANK_CODE)=(SELECT BANK_NAME FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"')),(SELECT BANK_ADDRESS FROM FC_FC5_RECEIPT_BANK "
				+ "WHERE UNIQUE_FILENO='"+appId+"') || ', ' || (SELECT BANK_TOWNCITY FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"') || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=(SELECT BANK_STATE FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(SELECT BANK_DISTRICT FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT BANK_PIN FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"') AS BANK,(SELECT USERNAME FROM FC_FC5_RECEIPT_BANK WHERE UNIQUE_FILENO='"+appId+"') AS USER_NAME, "
				+ "(SELECT to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy') FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO='"+appId+"'),ASSO_FCRA_RCN,"
				+ "(SELECT to_char(VALID_FROM,'dd-mm-yyyy') FROM FC_INDIA WHERE RCN=(select ASSO_FCRA_RCN from fc_fc5_entry_new1 where unique_fileno='"+appId+"') ) "
				+ " FROM FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO='"+appId+"'";
		statement = connection.prepareStatement(reportQuery); 		
		ResultSet rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("assoName", rs.getString(1));			
			parameters.put("assoAddress", rs.getString(2));
			parameters.put("regDate", rs.getString(3));			
			parameters.put("accNumber", rs.getString(4));
			parameters.put("bankDetails", rs.getString(5));
			parameters.put("bankAddress", rs.getString(6));
			parameters.put("userName", rs.getString(7));
			parameters.put("submissionDate", rs.getString(8));
			parameters.put("regNumber", rs.getString(9).substring(0, rs.getString(9).length()-1));
			parameters.put("validFrom", rs.getString(10));
		}	
		
		StringBuffer query7 = new StringBuffer("SELECT ASSO_NATURE FROM FC_INDIA WHERE  RCN=(select ASSO_FCRA_RCN from fc_fc5_entry_new1 where unique_fileno=?)");
 		statement = connection.prepareStatement(query7.toString()); 			
 		statement.setString(1, appId);
 		ResultSet rs4=statement.executeQuery();
 		String assoNature=null;
 		if(rs4.next()){
 			assoNature=rs4.getString(1);
 		} 
 		String assoNatureDesc="";
 		ResultSet rs5=null;
 		StringBuffer query8 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
 		statement = connection.prepareStatement(query8.toString());
 		for(int i=0;i<assoNature.length();i++){
 			String natureCode = assoNature.substring(i, i+1);
		statement.setString(1, natureCode);
		String delim=(i==0?"":",");
		rs5 = statement.executeQuery();
		if(rs5.next()){
			assoNatureDesc=assoNatureDesc+delim+rs5.getString(1);
		}
		rs5.close();
 		}
 		statement.close();
 		parameters.put("assoNature", assoNatureDesc);
	}
	
	// For Change of Name and Address FC_6A//
	private void prepareChangeofNameAndAddressQuery(Connection connection) throws Exception {
		   String assonamechng="";
		   String assoaddChng="";
		   String assobankChng="";
		   String assoNatureChng="",assoUtilbnkChng="";		   
		   String assoMemChng="";
		  
			//StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
	 		//			+ ",ASSO_CHANGE_UTILISATION_STATUS,ASSO_CHANGE_MEMBER FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");
			StringBuffer query5 = new StringBuffer("SELECT is_name_change, is_address_change FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?");			
	 	 		statement = connection.prepareStatement(query5.toString()); 
	 	 		statement.setString(1,appId);
	 	 		ResultSet rs4=statement.executeQuery();
	 	 		if(rs4.next()){
	 	 			// Name Change
	 	 			if(rs4.getString(1)==null){			
	 	 			}
	 	 			else if(rs4.getString(1).equals("Y")){
	 	 				assonamechng="Y";	 	 				
	 	 			} 
	 	 			// Address Change
	 	 			if(rs4.getString(2)==null){			
	 	 			}
	 	 			else if(rs4.getString(2).equals("Y")){
	 	 				assoaddChng="Y";
	 	 			}
	 	 		}
	 	 		parameters.put("assonamechng",assonamechng);
	 	 		parameters.put("assoaddChng", assoaddChng);
	 	 		//parameters.put("assobankChng",assobankChng);
	 	 		//parameters.put("assoNatureChng", assoNatureChng);
	 	 		//parameters.put("assoUtilbnkChng", assoUtilbnkChng);
	 	 		//parameters.put("assoMemChng", assoMemChng);
	 	 		
	 	 	    if(assonamechng=="Y"){
	 	 			//reportQuery = "SELECT ASSO_NAME,MAIN_AIM FROM FC_FC6_FORM WHERE UNIQUE_FILENO='"+appId+"'";
	 	 	    	reportQuery = "SELECT CHANGED_TO_ASSONAME, CURRENT_ASSO_NAME FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO='"+appId+"'";
	 	 			statement=connection.prepareStatement(reportQuery);
	 	 			ResultSet rs=statement.executeQuery();	
	 	 			while(rs.next()){ 	
	 	 				parameters.put("assoName", rs.getString(1));
	 	 				parameters.put("oldAssoName", rs.getString(2));
	 	 				//parameters.put("assoAim", rs.getString(2));
	 	 			}
	 	 		}
	 	 	   if(assoaddChng=="Y"){
	 	 		    //reportQuery = "SELECT ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT "
					 //             +" WHERE DISTCODE=ASO_DISTRICT)||', ' || ASSO_PIN AS ASSOADDRESS FROM FC_FC6_FORM WHERE UNIQUE_FILENO='"+appId+"'";
		              reportQuery = "SELECT CHANGED_TO_ASSOADDRESS ||', ' || CHANGED_TO_ASSOTOWNCITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=CHANGED_TO_ASSOSTATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=CHANGED_TO_ASSODISTRICT)||', ' || CHANGED_TO_ASSOPIN AS CHANGED_TO_ASSOADDRESS, "
		            		       + "CURRENT_ASSO_ADDRESS ||', ' || CURRENT_ASSO_DISTRICT ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=CURRENT_ASSO_STATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=CURRENT_ASSO_DISTRICT)||', ' || CURRENT_ASSO_PIN AS OLD_TO_ASSOADDRESS " 
		              		       + "FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO='"+appId+"'";	
		              
	 	 			statement=connection.prepareStatement(reportQuery);
	 	 			ResultSet rs=statement.executeQuery();	
	 	 			while(rs.next()){ 	
	 	 				parameters.put("assoAddress", rs.getString(1));
	 	 				parameters.put("oldassoAddress", rs.getString(2));
	 	 			}
	 	 	   }
	 			   
	 	 	 //reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_FORM.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		     //           +"substr(FC_FC6_FORM.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_FORM,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_FORM.RCN=FC_INDIA.RCN";

	 	 	 reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_NAMEADDRESS_CHANGE.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		                +"substr(FC_FC6_NAMEADDRESS_CHANGE.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_NAMEADDRESS_CHANGE,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_NAMEADDRESS_CHANGE.RCN=FC_INDIA.RCN";	 	 	 
	 	 	 
				statement=connection.prepareStatement(reportQuery);
				ResultSet resultSet=statement.executeQuery();	
				while(resultSet.next()){
					parameters.put("fcAssoName", resultSet.getString(1));	
					parameters.put("fcAssoAddress", resultSet.getString(2));	
					parameters.put("fileNo", resultSet.getString(3));
					parameters.put("regDate", resultSet.getString(4));
					parameters.put("userName", resultSet.getString(5));
					parameters.put("submissionDate", resultSet.getString(6));
					parameters.put("regNumber", resultSet.getString(7));
				}
				
	}
	
	// Change of Details of Fc Reciept Cum Utilisation Accounts or Bank Change
	
	private void prepareChangeOfFcRecieptCumUtilisationQuery(Connection connection) throws Exception{
		   String assonamechng="";
		   String assoaddChng="";
		   String assobankChng="";
		   String assoNatureChng="",assoUtilbnkChng="";		   
		   String assoMemChng="";
		  
/*			StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
	 					+ ",ASSO_CHANGE_UTILISATION_STATUS,ASSO_CHANGE_MEMBER FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");*/
			StringBuffer query5 = new StringBuffer("SELECT * FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?");		   
	 	 		statement = connection.prepareStatement(query5.toString()); 
	 	 		statement.setString(1,appId);
	 	 		ResultSet rs4=statement.executeQuery();
	 	 		if(rs4.next()){
	 	 			assobankChng="Y";
	 	 		}
	 	 		parameters.put("assobankChng",assobankChng);

	 	 	   if(assobankChng=="Y"){
/*	 	 		 reportQuery ="SELECT ('Account No:'||ACCOUNT_NO||','||(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=FC_FC6_FORM.BANK_NAME)|| ',' ||FC_FC6_FORM.BANK_BRANCH_NAME||','||FC_FC6_FORM.BANK_ADDRESS || ', ' ||"
	 	 		 		+ "( BANK_TOWNCITY ) || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=( BANK_STATE )) || ', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE ="
	 	 		 		+ "( BANK_DISTRICT )) || ', ' || ( BANK_PIN ) )FROM FC_FC6_FORM WHERE UNIQUE_FILENO='"+appId+"'";*/
	 	 		 reportQuery = "SELECT ('Account No:'||CHANGED_ACCOUNT_NO||','||(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=FC_FC6_BANK_CHANGE.CHANGED_BANK_NAME)|| ','"
	 	 		 		+ "||FC_FC6_BANK_CHANGE.CHANGED_BANK_ADDRESS || ', ' ||( CHANGED_BANK_TOWN_CITY ) || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=( CHANGED_BANK_STATE )) || ', '"
	 	 		 		+ "|| (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE =( CHANGED_BANK_DISTRICT )) || ', ' || ( CHANGED_BANK_PIN ) )|| ', '||'E-mail:'||CHANGED_BANK_EMAIL||', '"
	 	 		 		+ "||'Changed Bank Acct. Date:'||to_char(CHANGED_BANK_ACCOUNT_DATE,'dd-mm-yyyy') as bankDetails, "
	 	 		 		+ "('Account No:'||CURRENT_ACCOUNT_NO||','||(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=FC_FC6_BANK_CHANGE.CURRENT_BANK_NAME)|| ','"
	 	 		 		+ "||FC_FC6_BANK_CHANGE.CURRENT_BANK_ADDRESS || ', ' ||( CURRENT_BANK_TOWN_CITY ) || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=( CURRENT_BANK_STATE )) || ', '"
	 	 		 		+ "|| (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE =( CURRENT_BANK_DISTRICT )) || ', ' || ( CURRENT_BANK_PIN ) )|| ', '||'E-mail:'||CURRENT_BANK_EMAIL||', '"
	 	 		 		+ "||'Changed Bank Acct. Date:'||to_char(CURRENT_BANK_ACCOUNT_DATE,'dd-mm-yyyy') as oldBankDetails"
	 	 		 		+ " FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO='"+appId+"'";
		 			
	 	 		 statement=connection.prepareStatement(reportQuery);
		 			ResultSet rs=statement.executeQuery();	
		 			while(rs.next()){ 			 				
		 				parameters.put("bankDetails", rs.getString(1));
		 				parameters.put("oldBankDetails", rs.getString(2));		 				
			        }
	 	 	   }		
		 	 	 
		   
	 	 	 /*reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_FORM.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		                +"substr(FC_FC6_FORM.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_FORM,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_FORM.RCN=FC_INDIA.RCN";*/
	 	 	 
	 	 	 reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_BANK_CHANGE.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
	 	 	 		+ "substr(FC_FC6_BANK_CHANGE.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_BANK_CHANGE,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_BANK_CHANGE.RCN=FC_INDIA.RCN";	 	 	 
		
				statement=connection.prepareStatement(reportQuery);
				ResultSet resultSet=statement.executeQuery();	
				while(resultSet.next()){
					parameters.put("fcAssoName", resultSet.getString(1));	
					parameters.put("fcAssoAddress", resultSet.getString(2));	
					parameters.put("fileNo", resultSet.getString(3));
					parameters.put("regDate", resultSet.getString(4));
					parameters.put("userName", resultSet.getString(5));
					parameters.put("submissionDate", resultSet.getString(6));
					parameters.put("regNumber", resultSet.getString(7));										
				}
	}
	
	// For Change of Utilisaction Bank accounts FC6_D
	private void prepareChangeofOpeningOfUtilisationBankAccountQuery(Connection connection) throws Exception{
		   String assonamechng="";
		   String assoaddChng="";
		   String assobankChng="";
		   String assoNatureChng="",assoUtilbnkChng="";		   
		   String assoMemChng="";
		  
/*		   StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
	 					+ ",ASSO_CHANGE_UTILISATION_STATUS,ASSO_CHANGE_MEMBER FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");*/
			StringBuffer query5 = new StringBuffer("SELECT IS_NEW_ACCOUNT from FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?");			
	 	 		statement = connection.prepareStatement(query5.toString()); 
	 	 		statement.setString(1,appId);
	 	 		ResultSet rs4=statement.executeQuery();
	 	 		if(rs4.next()){
	 	 			//Util bank Change
	 	 			if(rs4.getString(1)==null){			
	 	 			}
	 	 			else if(rs4.getString(1).equals("Y")){
	 	 				assoUtilbnkChng="Y";
	 	 			}	 	 			
	
	 	 		}
	 	 		/*parameters.put("assonamechng",assonamechng);
	 	 		parameters.put("assoaddChng", assoaddChng);
	 	 		parameters.put("assobankChng",assobankChng);
	 	 		parameters.put("assoNatureChng", assoNatureChng);
	 	 		parameters.put("assoUtilbnkChng", assoUtilbnkChng);
	 	 		parameters.put("assoMemChng", assoMemChng);
	 	 		*/
	 	 		parameters.put("assoUtilbnkChng", assoUtilbnkChng);
	 	 		
	 	 		if(assoUtilbnkChng=="Y"){
/*	 	 		 reportQuery="SELECT ACCOUNT_NO,(((SELECT BANK_NAME FROM TM_BANKS WHERE TO_CHAR(BANK_CODE)=A.BANK_NAME)||','||BANK_ADDRESS) ||','|| "
	 	 				     +" BANK_TOWNCITY ||','|| (SELECT SNAME FROM TM_STATE WHERE SCODE=(BANK_STATE))||','||(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(BANK_DISTRICT))|| ', '|| "
	 	 				     +" (BANK_PIN )) AS UTILIZATIONBANK,BRANCH_NAME FROM FC_FC6_UTILIZATION_BANK A WHERE UNIQUE_FILENO='"+appId+"'";*/
	 	 		 
	 	 		 reportQuery="SELECT NEW_ACCOUNT_NO as ACCOUNT_NO,(((SELECT BANK_NAME FROM TM_BANKS WHERE TO_CHAR(BANK_CODE)=A.NEW_BANK_NAME)||','||NEW_BANK_ADDRESS ) ||','||  NEW_BANK_TOWN_CITY ||','|| "
	 	 		 		+ " (SELECT SNAME FROM TM_STATE WHERE SCODE=(NEW_BANK_STATE))||','||(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(NEW_BANK_DISTRICT))|| ', '||  (NEW_BANK_PIN )) AS UTILIZATIONBANK, "
	 	 		 		+ " 'BRANCH_NAME' as BRANCH_NAME FROM FC_FC6_NEW_UTILIZATION_ACCTS A WHERE UNIQUE_FILENO='"+appId+"'";
	 	 		 
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
		 				   parameters.put("UtilizationBankDetails", ds);

		 		// For fetching old bank details		   
     	 		 String reportQueryOld="SELECT ACCOUNT_NO as OLD_ACCOUNT_NO,(((SELECT BANK_NAME FROM TM_BANKS WHERE TO_CHAR(BANK_CODE)=A.BANK_NAME)||','||BANK_ADDRESS ) ||','||  BANK_TOWNCITY ||','|| "
     	 		 		+ " (SELECT SNAME FROM TM_STATE WHERE SCODE=(BANK_STATE))||','||(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(BANK_DISTRICT))|| ', '||  (BANK_PIN )) AS OLD_UTILIZATIONBANK "
     	 		 		+ " FROM T_FC_UTILIZATION_BANK_HISTORY A WHERE RCN=(SELECT RCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO='"+appId+"')";
		 		 	 		 
 	     		ReportDataSource dsOld=new ReportDataSource(parameters, reportQueryOld, connection, virtualizationMaxSize);
		 			 				   parameters.put("OldUtilizationBankDetails", dsOld);
		 				   
	 	 		   
	 	 	   }
	 			   
	 	 	 /*reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_FORM.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		                +"substr(FC_FC6_FORM.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_FORM,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_FORM.RCN=FC_INDIA.RCN";*/

	 	 	 reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_UTILIZATION_ACCTS_ENTRY.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		                +"substr(FC_FC6_UTILIZATION_ACCTS_ENTRY.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_UTILIZATION_ACCTS_ENTRY,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_UTILIZATION_ACCTS_ENTRY.RCN=FC_INDIA.RCN";	 	 	 
		
				statement=connection.prepareStatement(reportQuery);
				ResultSet resultSet=statement.executeQuery();	
				while(resultSet.next()){
					parameters.put("fcAssoName", resultSet.getString(1));	
					parameters.put("fcAssoAddress", resultSet.getString(2));	
					parameters.put("fileNo", resultSet.getString(3));
					parameters.put("regDate", resultSet.getString(4));
					parameters.put("userName", resultSet.getString(5));
					parameters.put("submissionDate", resultSet.getString(6));
					parameters.put("regNumber", resultSet.getString(7));										
				}
			
					
	}
	
	// For change in committee Members
	private void prepareChangeofCommitteeMembersQuery(Connection connection)throws Exception{
		   String assonamechng="";
		   String assoaddChng="";
		   String assobankChng="";
		   String assoNatureChng="",assoUtilbnkChng="";		   
		   String assoMemChng="";
		  
/*			StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
	 					+ ",ASSO_CHANGE_UTILISATION_STATUS,ASSO_CHANGE_MEMBER FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");*/
			StringBuffer query5 = new StringBuffer("SELECT IS_CHANGE_MEMBERS FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?");		   
	 	 		statement = connection.prepareStatement(query5.toString()); 
	 	 		statement.setString(1,appId);
	 	 		ResultSet rs4=statement.executeQuery();
	 	 		if(rs4.next()){
	 	 			// Member Change
	 	 			if(rs4.getString(1)==null){			
	 	 			}
	 	 			else if(rs4.getString(1).equals("Y")){
	 	 				assoMemChng="Y";
	 	 			}
	 	 			
	 	 		}
/*	 	 		parameters.put("assonamechng",assonamechng);
	 	 		parameters.put("assoaddChng", assoaddChng);
	 	 		parameters.put("assobankChng",assobankChng);
	 	 		parameters.put("assoNatureChng", assoNatureChng);
	 	 		parameters.put("assoUtilbnkChng", assoUtilbnkChng);*/
	 	 		parameters.put("assoMemChng", assoMemChng);
	 	 		
	 	 	   if(assoMemChng=="Y"){
/*	 	 		reportQuery="SELECT NAME,(SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE DESIG_CODE=OFFICE_OF_ASSO)AS COMMITTEEMEM FROM FC_FC6_COMMITTEE "
	 	 				    + "WHERE UNIQUE_FILENO='"+appId+"'";*/
	 	 		
	 	 		reportQuery="SELECT NAME,(SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE DESIG_CODE=OFFICE_OF_ASSO)AS COMMITTEEMEM "
	 	 				+ " FROM FC_FC6_MEMBERS WHERE UNIQUE_FILENO='"+appId+"'";
		 	 		
	 	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
	 				   parameters.put("MemberDetails", ds);	

	 		 	//Old Members Details
	 		    String reportQueryOld="SELECT NAME as NAME_OLD,(SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE DESIG_CODE=OFFICE_OF_ASSO)AS COMMITTEEMEM_OLD "
	 		 	 				+ " FROM T_FC_COMMITTEE_HISTORY WHERE RCN=(SELECT RCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO='"+appId+"')";
	 			 	 		
	 		 	ReportDataSource dsOld=new ReportDataSource(parameters, reportQueryOld, connection, virtualizationMaxSize);
	 		 				   parameters.put("OldMemberDetails", dsOld);	 				   
	 				   
	 		}	 	
	 			   
	 	 	 reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_MEMBERS_CHANGE_ENTRY.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		                +"substr(FC_FC6_MEMBERS_CHANGE_ENTRY.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_MEMBERS_CHANGE_ENTRY,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_MEMBERS_CHANGE_ENTRY.RCN=FC_INDIA.RCN";
		
				statement=connection.prepareStatement(reportQuery);
				ResultSet resultSet=statement.executeQuery();	
				while(resultSet.next()){
					parameters.put("fcAssoName", resultSet.getString(1));	
					parameters.put("fcAssoAddress", resultSet.getString(2));	
					parameters.put("fileNo", resultSet.getString(3));
					parameters.put("regDate", resultSet.getString(4));
					parameters.put("userName", resultSet.getString(5));
					parameters.put("submissionDate", resultSet.getString(6));
					parameters.put("regNumber", resultSet.getString(7));										
				}
	}
	
	
	//For Change of Detail//
	
	private void prepareChangeofDetailQuery(Connection connection)throws Exception{
		   String assonamechng="";
		   String assoaddChng="";
		   String assobankChng="";
		   String assoNatureChng="",assoUtilbnkChng="";		   
		   String assoMemChng="";
		  
			StringBuffer query5 = new StringBuffer("SELECT ASSO_NAME_CHANGE_ST,ASSO_CHANGE_STATE_ST,ASSO_CHANGE_DESIGBANK_STATUS,ASSO_CHANGE_NATURE"
	 					+ ",ASSO_CHANGE_UTILISATION_STATUS,ASSO_CHANGE_MEMBER FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");
	 	 		statement = connection.prepareStatement(query5.toString()); 
	 	 		statement.setString(1,appId);
	 	 		ResultSet rs4=statement.executeQuery();
	 	 		if(rs4.next()){
	 	 			// Name Change
	 	 			if(rs4.getString(1)==null){			
	 	 			}
	 	 			else if(rs4.getString(1).equals("Y")){
	 	 				assonamechng="Y";	 	 				
	 	 			} 
	 	 			// Address Change
	 	 			if(rs4.getString(2)==null){			
	 	 			}
	 	 			else if(rs4.getString(2).equals("Y")){
	 	 				assoaddChng="Y";
	 	 			}
	 	 			// Bank Change
	 	 			if(rs4.getString(3)==null){			
	 	 			}
	 	 			else if(rs4.getString(3).equals("Y")){
	 	 				assobankChng="Y"; 	 	 
	 	 			}
	 	 			// ASSOnature Change
	 	 			if(rs4.getString(4)==null){			
	 	 			}
	 	 			else if(rs4.getString(4).equals("Y")){
	 	 				assoNatureChng="Y";
	 	 			}	 	 			
	 	 			//Util bank Change
	 	 			if(rs4.getString(5)==null){			
	 	 			}
	 	 			else if(rs4.getString(5).equals("Y")){
	 	 				assoUtilbnkChng="Y";
	 	 			}	 	 			
	 	 			// Member Change
	 	 			if(rs4.getString(6)==null){			
	 	 			}
	 	 			else if(rs4.getString(6).equals("Y")){
	 	 				assoMemChng="Y";
	 	 			}
	 	 			
	 	 		}
	 	 		parameters.put("assonamechng",assonamechng);
	 	 		parameters.put("assoaddChng", assoaddChng);
	 	 		parameters.put("assobankChng",assobankChng);
	 	 		parameters.put("assoNatureChng", assoNatureChng);
	 	 		parameters.put("assoUtilbnkChng", assoUtilbnkChng);
	 	 		parameters.put("assoMemChng", assoMemChng);
	 	 		
	 	 	    if(assonamechng=="Y"){
	 	 			reportQuery = "SELECT ASSO_NAME,MAIN_AIM FROM FC_FC6_FORM WHERE UNIQUE_FILENO='"+appId+"'";
	 	 			statement=connection.prepareStatement(reportQuery);
	 	 			ResultSet rs=statement.executeQuery();	
	 	 			while(rs.next()){ 	
	 	 				parameters.put("assoName", rs.getString(1));
	 	 				parameters.put("assoAim", rs.getString(2));
	 	 			}
 	 	 		}
	 	 	   if(assoaddChng=="Y"){
	 	 		    reportQuery = "SELECT ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT "
					              +" WHERE DISTCODE=ASO_DISTRICT)||', ' || ASSO_PIN AS ASSOADDRESS FROM FC_FC6_FORM WHERE UNIQUE_FILENO='"+appId+"'";
	 	 			statement=connection.prepareStatement(reportQuery);
	 	 			ResultSet rs=statement.executeQuery();	
	 	 			while(rs.next()){ 	
	 	 				parameters.put("assoAddress", rs.getString(1));
	 	 			}
	 	 	   }
	 	 	   if(assobankChng=="Y"){
	 	 		 reportQuery ="SELECT ('Account No:'||ACCOUNT_NO||','||(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=FC_FC6_FORM.BANK_NAME)|| ',' ||FC_FC6_FORM.BANK_BRANCH_NAME||','||FC_FC6_FORM.BANK_ADDRESS || ', ' ||"
	 	 		 		+ "( BANK_TOWNCITY ) || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=( BANK_STATE )) || ', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE ="
	 	 		 		+ "( BANK_DISTRICT )) || ', ' || ( BANK_PIN ) )FROM FC_FC6_FORM WHERE UNIQUE_FILENO='"+appId+"'";
		 			statement=connection.prepareStatement(reportQuery);
		 			ResultSet rs=statement.executeQuery();	
		 			while(rs.next()){ 			 				
		 				parameters.put("bankDetails", rs.getString(1));
 			        }
	 	 	   }		
	 	 	   if(assoNatureChng=="Y") {
		 	 		StringBuffer query7 = new StringBuffer("SELECT ASSO_NATURE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?");
		 	 		statement = connection.prepareStatement(query7.toString()); 			
		 	 		statement.setString(1, appId);
		 	 		ResultSet rslt=statement.executeQuery();
		 	 		String assoNature=null;
		 	 		if(rslt.next()){
		 	 			assoNature=rslt.getString(1);
		 	 		} 
		 	 		String assoNatureDesc="";
		 	 		ResultSet rs5=null;
		 	 		StringBuffer query8 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
		 	 		statement = connection.prepareStatement(query8.toString());
		 	 		for(int i=0;i<assoNature.length();i++){
		 	 			String natureCode = assoNature.substring(i, i+1);
		 			statement.setString(1, natureCode);
		 			String delim=(i==0?"":",");
		 			rs5 = statement.executeQuery();
		 			if(rs5.next()){
		 				assoNatureDesc=assoNatureDesc+delim+rs5.getString(1);
		 			}
		 			rs5.close();
		 	 		}
		 	 		statement.close();
		 	 		parameters.put("assoNature", assoNatureDesc);
	 	 	   }		 	 	 
	 	 	   if(assoUtilbnkChng=="Y"){
	 	 		 reportQuery="SELECT ACCOUNT_NO,(((SELECT BANK_NAME FROM TM_BANKS WHERE TO_CHAR(BANK_CODE)=A.BANK_NAME)||','||BANK_ADDRESS) ||','|| "
	 	 				     +" BANK_TOWNCITY ||','|| (SELECT SNAME FROM TM_STATE WHERE SCODE=(BANK_STATE))||','||(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(BANK_DISTRICT))|| ', '|| "
	 	 				     +" (BANK_PIN )) AS UTILIZATIONBANK,BRANCH_NAME FROM FC_FC6_UTILIZATION_BANK A WHERE UNIQUE_FILENO='"+appId+"'";
		 			ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
		 				   parameters.put("UtilizationBankDetails", ds);

	 	 		   
	 	 	   }
	 	 	   if(assoMemChng=="Y"){
	 	 		reportQuery="SELECT NAME,(SELECT DESIG_NAME FROM TM_COMMITTEE_DESIGNATION WHERE DESIG_CODE=OFFICE_OF_ASSO)AS COMMITTEEMEM FROM FC_FC6_COMMITTEE "
	 	 				    + "WHERE UNIQUE_FILENO='"+appId+"'";
		 	 		
	 	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
	 				   parameters.put("MemberDetails", ds);	
	 				   
	 		}	 	
	 			   
	 	 	 reportQuery="SELECT FC_INDIA.ASSO_NAME,FC_INDIA.ASSO_ADDRESS,FC_FC6_FORM.FILE_NO,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy')as subdate,"
		                +"substr(FC_FC6_FORM.RCN,1,9) AS CHANGEDRCN FROM FC_FC6_FORM,FC_INDIA WHERE UNIQUE_FILENO='"+appId+"' AND FC_FC6_FORM.RCN=FC_INDIA.RCN";
		
				statement=connection.prepareStatement(reportQuery);
				ResultSet resultSet=statement.executeQuery();	
				while(resultSet.next()){
					parameters.put("fcAssoName", resultSet.getString(1));	
					parameters.put("fcAssoAddress", resultSet.getString(2));	
					parameters.put("fileNo", resultSet.getString(3));
					parameters.put("regDate", resultSet.getString(4));
					parameters.put("userName", resultSet.getString(5));
					parameters.put("submissionDate", resultSet.getString(6));
					parameters.put("regNumber", resultSet.getString(7));										
				}
			
					
	}
	
	
	private void preparePriorPermissionQuery(Connection connection,Boolean flag) throws Exception{
		reportQuery="SELECT ASSO_NAME,ASSO_ADDRESS ||', ' || ASSO_TOWN_CITY ||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=ASSO_STATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT "
				+ " WHERE DISTCODE=ASSO_DISTRICT)||', ' || ASSO_PINCODE,to_char(sysdate,'dd-mm-yyyy') as REG_DATE,ACCOUNT_NO,"
				+ "((SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=FC_FC1A_ENTRY.BANK_NAME) || ', ' || BANK_ADDRESS) || ', ' || (SELECT BANK_TOWNCITY FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO='"+appId+"') || ', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=(SELECT BANK_STATE FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=(SELECT BANK_DISTRICT FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO='"+appId+"')) || ', ' || (SELECT BANK_PIN FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO='"+appId+"') AS BANK,USERNAME AS USER_NAME,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy') "
				+ " FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO=?";
		statement = connection.prepareStatement(reportQuery); 	
		statement.setString(1, appId);
		ResultSet rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("assoName", rs.getString(1));			
			parameters.put("assoAddress", rs.getString(2));
			parameters.put("regDate", rs.getString(3));			
			parameters.put("accNumber", rs.getString(4));
			
			parameters.put("bankDetails", rs.getString(5));
			parameters.put("userName", rs.getString(6));
			parameters.put("submissionDate", rs.getString(7));
		}		
		StringBuffer query7 = new StringBuffer("SELECT ASSO_NATURE FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO=?");
 		statement = connection.prepareStatement(query7.toString()); 			
 		statement.setString(1, appId);
 		ResultSet rs4=statement.executeQuery();
 		String assoNature=null;
 		if(rs4.next()){
 			assoNature=rs4.getString(1);
 		} 
 		String assoNatureDesc="";
 		ResultSet rs5=null;
 		StringBuffer query8 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
 		statement = connection.prepareStatement(query8.toString());
 		for(int i=0;i<assoNature.length();i++){
 			String natureCode = assoNature.substring(i, i+1);
		statement.setString(1, natureCode);
		String delim=(i==0?"":",");
		rs5 = statement.executeQuery();
		if(rs5.next()){
			assoNatureDesc=assoNatureDesc+delim+rs5.getString(1);
		}
		rs5.close();
 		}
 		statement.close();
 		parameters.put("assoNature", assoNatureDesc);
 		
 		ReportDataSource ds=null;
 		reportQuery = ("SELECT NATUREANDVALUE_VALUE || ' ' || (SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=NATUREANDVALUE_CURRENCY) AS CURRENCY,'Type: ' || (SELECT DONOR_TNAME FROM TM_DONOR_TYPE WHERE DONOR_ID=IND_DONOR_YES_NO) || ' \n Details: ' || UPPER(TRIM(IND_DONOR_NAME)) || ', ' || UPPER(TRIM(IND_DONOR_PERMANENTADDRESS)) || ', ' ||(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=IND_DONOR_COUNTRY) AS DONOR,(SELECT PURPOSE_NAME FROM TM_AMOUNT_PURPOSE WHERE PURPOSE_CODE=PURPOSE_FOREIGNCONT) AS PURPOSE,OTHER_ACTIVITY AS ACTIVITY FROM FC_FC1A_DONOR WHERE UNIQUE_FILENO='"+appId+"'");
		ds = new ReportDataSource(parameters, reportQuery, connection,virtualizationMaxSize);
		parameters.put("PRINTRECORD_DATA_SOURCE", ds);
		
		if(flag==true){		
			StringBuffer queryDON = new StringBuffer("SELECT  UPPER(TRIM(IND_DONOR_NAME)) || ', ' || UPPER(TRIM(IND_DONOR_PERMANENTADDRESS)) || ', ' ||(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=IND_DONOR_COUNTRY) AS DONOR FROM FC_FC1A_DONOR WHERE UNIQUE_FILENO=?");
	 		statement = connection.prepareStatement(queryDON.toString()); 			
	 		statement.setString(1, appId);
	 		ResultSet rsDON=statement.executeQuery();
	 		String donorDesc=null;
	 		if(rsDON.next()){
	 			donorDesc=rsDON.getString(1);
	 		} 
			rsDON.close();
			statement.close();
			parameters.put("donorDesc", donorDesc);
			reportQuery = ("SELECT B.INSTALLMENT_SNUMBER,B.INSTALLMENT_AMOUNT || ' ' || (SELECT CURR_NAME FROM TM_CURRENCY WHERE CURR_CODE=A.NATUREANDVALUE_CURRENCY) AS INSTALLMENT_AMOUNT"
					+ " from Fc_Fc1a_Donor A,T_PP_INSTALLMENT_DETAILS B WHERE a.UNIQUE_FILENO=B.APPLICATION_ID AND A.ID=B.DONOR_ID AND APPLICATION_ID='"+appId+"'");
			ds = new ReportDataSource(parameters, reportQuery, connection,virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_INS", ds);
		}
	}
	
	private void prepareHospitalityQuery(Connection connection) throws Exception{
		reportQuery="SELECT FWD_OFF_NAME,FWD_OFF_ADD ||', ' || FWD_OFF_TOWN_CITY||', ' || (SELECT SNAME FROM TM_STATE WHERE SCODE=FWD_OFF_STATE)||', ' || (SELECT DISTNAME FROM TM_DISTRICT "
				+ " WHERE DISTCODE=FWD_OFF_DISTRICT)||', ' || FWD_OFF_PINCODE,FWD_LETTER_NO,to_char(FWD_LETTER_DATE,'dd-mm-yyyy'),APPT_NAME ||', ' || EMP_DESIG,to_char(sysdate,'dd-mm-yyyy'),FWD_OFF_DES,EMP_ORG FROM FC_H_ENTRY WHERE UNIQUE_FILENO='"+appId+"'";
		statement = connection.prepareStatement(reportQuery); 		
		ResultSet rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("assoName", rs.getString(1));			
			parameters.put("assoAddress", rs.getString(2));
			parameters.put("letterNo", rs.getString(3));
			parameters.put("letterDate", rs.getString(4));
			parameters.put("appDetails", rs.getString(5));			
			parameters.put("regDate", rs.getString(6));
			parameters.put("fwdOffDesg", rs.getString(7));
			parameters.put("officeName", rs.getString(8));
		}			
		
		reportQuery = ("SELECT A.IND_ORG_NAME,(CASE WHEN A.IND_ORG='01' THEN  A.IND_PRESENT_ADD ELSE A.ORG_PRESENT_ADD END) AS ADDRESS,(SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE=B.VISITING_COUNTRY) AS VISITING_COUNTRY,B.VISITING_PLACE,to_char(DURATION_HOSPI_FROM,'dd-mm-yyyy'),to_char(DURATION_HOSPI_TO,'dd-mm-yyyy') FROM FC_H_PARTICULARS_HOST A,FC_H_HOSPITALITY_DETAILS B WHERE A.UNIQUE_FILENO=B.UNIQUE_FILENO AND A.UNIQUE_FILENO='"+appId+"'");
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery, connection,virtualizationMaxSize);
		parameters.put("PRINTRECORD_DATA_SOURCE", ds);
	}
	
	
	private void prepareRegistrationRejectQuery(Connection connection) throws Exception{
		ResultSet rs=null;
		reportQuery="SELECT ASSO_NAME,ASSO_ADDRESS,to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy'),to_char(sysdate,'dd-mm-yyyy')"
				+ "  FROM T_FC8_ENTRY WHERE UNIQUE_FILENO='"+appId+"'";
		statement = connection.prepareStatement(reportQuery); 		
		rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("assoName", rs.getString(1));			
			parameters.put("assoAddress", rs.getString(2));			
			
			parameters.put("submissionDate", rs.getString(3));
			parameters.put("sysDate", rs.getString(4));
		}
		rs.close();	
		statement.close();		
		reportQuery="SELECT STATUS_REMARK FROM T_PC_COMMUNICATION WHERE APPLICATION_ID=? AND CHAT_ID=(SELECT CHAT_ID FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE"
				+ " APPLICATION_ID=? AND OFFICE_CODE=? AND STATUS=10)";
		statement = connection.prepareStatement(reportQuery); 	
		statement.setString(1, appId);
		statement.setString(2, appId);
		statement.setString(3, myOfficeCode);
		rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("remark", rs.getString(1));			
		}
	}
	
	private void preparePriorPermissionRejectQuery(Connection connection) throws Exception{
		ResultSet rs=null;
		reportQuery="SELECT ASSO_NAME,ASSO_ADDRESS,to_char(sysdate,'dd-mm-yyyy'),to_char(FORM_SUBMISSION_DATE,'dd-mm-yyyy') "
				+ " FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO=?";
		statement = connection.prepareStatement(reportQuery); 	
		statement.setString(1, appId);
		rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("assoName", rs.getString(1));			
			parameters.put("assoAddress", rs.getString(2));
			parameters.put("sysDate", rs.getString(3));			
			parameters.put("submissionDate", rs.getString(4));
		}	
		rs.close();	
		statement.close();		
		reportQuery="SELECT STATUS_REMARK FROM T_PC_COMMUNICATION WHERE APPLICATION_ID=? AND CHAT_ID=(SELECT CHAT_ID FROM T_PC_OFFICE_LEVEL_FINAL_STATUS WHERE"
				+ " APPLICATION_ID=? AND OFFICE_CODE=? AND STATUS=10)";
		statement = connection.prepareStatement(reportQuery); 	
		statement.setString(1, appId);
		statement.setString(2, appId);
		statement.setString(3, myOfficeCode);
		rs=statement.executeQuery();	 
		while(rs.next()){			
			parameters.put("remark", rs.getString(1));			
		}
	}
	
	private void prepareMail(String status) throws Exception{
		String appName="",assoName="",assoAdd="",appDate="",assoMobile="";		
		// Getting service_code 
		String query="SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?";
		statement = connection.prepareStatement(query); 
		statement.setString(1, appId);
		ResultSet rs=statement.executeQuery();	 
		if(rs.next()){			
			svcCode=rs.getString(1);
		}	
		statement.close();
		rs.close();
		// Getting emailIds
		if(status.equals("9")){
			if(svcCode.equals("01")){
				fileName = "Registration-Certificate.pdf";	
				String query1="SELECT ASSO_EMAIL,APPLICANT_NAME,ASSO_NAME,ASSO_ADDRESS,to_char(sysdate,'dd-mm-yyyy'),ASSO_MOBILE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();	 
				if(rs1.next()){			
					email=rs1.getString(1);
					appName=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);				
					appDate=rs1.getString(5);
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent(appName,assoName,assoAdd,appDate,"01","9");
			}else if(svcCode.equals("02")){
				fileName = "PriorPermission-Letter.pdf";	
				String query1="SELECT ASSO_EMAIL,APPLICANT_NAME,ASSO_NAME,ASSO_ADDRESS,to_char(sysdate,'dd-mm-yyyy'),ASSO_MOBILE FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO =?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();	 
				if(rs1.next()){			
					email=rs1.getString(1);
					appName=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);				
					appDate=rs1.getString(5);
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent(appName,assoName,assoAdd,appDate,"02","9");
			}else if(svcCode.equals("03")){
				fileName = "Renewal-Certificate.pdf";	
				String query1="SELECT ASSO_OFFICIAL_EMAIL,CHEIF_FUNCTIONARY_NAME,ASSO_NAME,ASSO_ADDRESS,to_char(sysdate,'dd-mm-yyyy'),ASSO_CHEIF_MOBILE FROM "
						+ "FC_FC5_ENTRY_NEW1 WHERE UNIQUE_FILENO=?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();	 
				if(rs1.next()){			
					email=rs1.getString(1);
					appName=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);		
					appDate=rs1.getString(5);
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent(appName,assoName,assoAdd,appDate,"03","9");
			}else if(svcCode.equals("06")){
				fileName="ChangeDetail-Certificate.pdf";
				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE,ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_FORM.RCN)AS ASSOADD,"
						+ "to_char(sysdate,'dd-mm-yyyy'),ASSO_CHEIF_MOBILE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();
				if(rs1.next()){
					email=rs1.getString(1);
					assoMobile=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);
					appDate=rs1.getString(5);		
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent("",assoName,assoAdd,appDate,"06","9");
			}else if (svcCode.equals("13")){                           // for change name and address
				fileName="ChangeDetail-Certificate.pdf";
/*				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE,ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_FORM.RCN)AS ASSOADD,"
						+ "to_char(sysdate,'dd-mm-yyyy'),ASSO_CHEIF_MOBILE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?";*/
				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE,CURRENT_ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_NAMEADDRESS_CHANGE.RCN)AS ASSOADD, to_char(sysdate,'dd-mm-yyyy'),ASSO_CHIEF_MOBILE FROM FC_FC6_NAMEADDRESS_CHANGE WHERE UNIQUE_FILENO=?";				
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();
				if(rs1.next()){
					email=rs1.getString(1);
					assoMobile=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);
					appDate=rs1.getString(5);		
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent("",assoName,assoAdd,appDate,"06","9");
			}else if (svcCode.equals("15")){                 //CHANGE OF  FC RECEIPT-CUM-UTILISATION BANK 
				fileName="ChangeDetail-Certificate.pdf";
/*				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE,ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_FORM.RCN)AS ASSOADD,"
						+ "to_char(sysdate,'dd-mm-yyyy'),ASSO_CHEIF_MOBILE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?";*/
				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE,CURRENT_ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_BANK_CHANGE.RCN)AS ASSOADD, to_char(sysdate,'dd-mm-yyyy'),ASSO_CHIEF_MOBILE FROM FC_FC6_BANK_CHANGE WHERE UNIQUE_FILENO=?";				
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();
				if(rs1.next()){
					email=rs1.getString(1);
					assoMobile=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);
					appDate=rs1.getString(5);		
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent("",assoName,assoAdd,appDate,"06","9");				
			}else if (svcCode.equals("16")){                                //OPENING OF UTILIZATION BANK ACCOUNT             
				fileName="ChangeDetail-Certificate.pdf";
/*				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE,ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_FORM.RCN)AS ASSOADD,"
						+ "to_char(sysdate,'dd-mm-yyyy'),ASSO_CHEIF_MOBILE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?";*/
				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE,CURRENT_ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_UTILIZATION_ACCTS_ENTRY.RCN)AS ASSOADD, to_char(sysdate,'dd-mm-yyyy'),ASSO_CHIEF_MOBILE FROM FC_FC6_UTILIZATION_ACCTS_ENTRY WHERE UNIQUE_FILENO=?";				
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();
				if(rs1.next()){
					email=rs1.getString(1);
					assoMobile=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);
					appDate=rs1.getString(5);		
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent("",assoName,assoAdd,appDate,"06","9");							
			} else if (svcCode.equals("17")){   //Change of committee members........
				fileName="ChangeDetail-Certificate.pdf";
/*				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE,ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_FORM.RCN)AS ASSOADD,"
						+ "to_char(sysdate,'dd-mm-yyyy'),ASSO_CHEIF_MOBILE FROM FC_FC6_FORM WHERE UNIQUE_FILENO=?";*/
				String query1="SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHIEF_MOBILE,CURRENT_ASSO_NAME,(SELECT ASSO_ADDRESS FROM FC_INDIA WHERE RCN=FC_FC6_MEMBERS_CHANGE_ENTRY.RCN)AS ASSOADD, to_char(sysdate,'dd-mm-yyyy'),ASSO_CHIEF_MOBILE FROM FC_FC6_MEMBERS_CHANGE_ENTRY WHERE UNIQUE_FILENO=?";				
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();
				if(rs1.next()){
					email=rs1.getString(1);
					assoMobile=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);
					appDate=rs1.getString(5);		
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent("",assoName,assoAdd,appDate,"06","9");							
			}
			else if(svcCode.equals("07")){           
				fileName = "Hospitality-Certificate.pdf";	
				String query1="SELECT APPT_EMAIL,APPT_MOBILE FROM FC_H_ENTRY WHERE UNIQUE_FILENO=?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();	 
				if(rs1.next()){			
					email=rs1.getString(1);
					mobile=rs1.getString(2);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent("","","","","07","9");
			}
		}else if(status.equals("10")){
			if(svcCode.equals("01")){
				fileName = "Refusal-Letter.pdf";	
				String query1="SELECT ASSO_EMAIL,APPLICANT_NAME,ASSO_NAME,ASSO_ADDRESS,to_char(sysdate,'dd-mm-yyyy'),ASSO_MOBILE FROM T_FC8_ENTRY WHERE UNIQUE_FILENO=?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();	 
				if(rs1.next()){			
					email=rs1.getString(1);
					appName=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);				
					appDate=rs1.getString(5);
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent(appName,assoName,assoAdd,appDate,"01","10");
			}
			else if(svcCode.equals("02")){
				fileName = "Refusal-Letter.pdf";	
				String query1="SELECT ASSO_EMAIL,APPLICANT_NAME,ASSO_NAME,ASSO_ADDRESS,to_char(sysdate,'dd-mm-yyyy'),ASSO_MOBILE FROM FC_FC1A_ENTRY WHERE UNIQUE_FILENO=?";
				statement = connection.prepareStatement(query1); 
				statement.setString(1, appId);
				ResultSet rs1=statement.executeQuery();	 
				if(rs1.next()){			
					email=rs1.getString(1);
					appName=rs1.getString(2);
					assoName=rs1.getString(3);
					assoAdd=rs1.getString(4);				
					appDate=rs1.getString(5);
					mobile=rs1.getString(6);
				}
				statement.close();
				rs1.close();
				mailContent=prepareMailContent(appName,assoName,assoAdd,appDate,"02","10");
			}
		}
			
	}
	
	private String prepareMailContent(String appName,String assoName,String assoAdd,String appDate,String svcCode,String svcStatus) throws Exception{
		String mailContent="",certHeader="",certName="";
		
		if(svcCode.equals("01")){
			if(svcStatus.equals("9")){
				certHeader="Intimations of Registration Under FCRA";
				certName="registration certificate";
			}
			else if(svcStatus.equals("10")){
				certHeader="Intimation of refusal to grant registration under FCRA, 2010";
				certName="refusal certificate";
			}
		}else if(svcCode.equals("02")){
			if(svcStatus.equals("9")){
				certHeader="Intimations of grant of Prior Permission under FCRA, 2010";
				certName="prior permission certificate";
			}
			else if(svcStatus.equals("10")){
				certHeader="Intimation of refusal to grant Prior Permission under FCRA, 2010";
				certName="refusal certificate";
			}			
		}
		else if(svcCode.equals("03")){
			certHeader="Intimation of Renewal of Registration under FCRA, 2010";
			certName="renewed registration certificate";			
		}	
		//for changedetails
		else if(svcCode.equals("06")){
			certHeader="Intimation of Change of Details Under FCRA, 2010";
			certName="Change of Details Certificate";
		}
		//for changedetails
		else if(svcCode.equals("13")){
			certHeader="Intimation of Change of Details Under FCRA, 2010";
			certName="Change of Details Certificate";
		}	
		else if(svcCode.equals("15")){
			certHeader="Intimation of Change of Details Under FCRA, 2010";
			certName="Change of Details Certificate";
		}	
		else if(svcCode.equals("16")){
			certHeader="Intimation of Change of Details Under FCRA, 2010";
			certName="Change of Details Certificate";
		}	
		else if(svcCode.equals("17")){
			certHeader="Intimation of Change of Details Under FCRA, 2010";
			certName="Change of Details Certificate";
		}			
		else if(svcCode.equals("07")){
			certHeader="Intimation of Hospitality under FCRA, 2010";
			certName="Hospitality certificate";	
			mailContent="<center><b>"+certHeader+"</b></center><br/>"
					+ "Please find attached the "+certName+" granted under FCRA, 2010."
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Adobe Acrobat Reader version (5.0 or above) can be used to open this attachment. Please note that you will not be able to open this file or "
					+ "view the file properly with any version lower than Adobe Acrobat Reader 5.0. The attached digital communication is authenticated by a digital "
					+ "signature obtained from a certifying authority under the Information Technology Act, 2000.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The digital intimation is authenticated by a digital signature obtained from a certifying authority "
					+ "under the Information Technology Act, 2000.<br><br><b>("+myUserName+")</b><br><b>"+myUserDesignation+"</b><br><br><br>"
					+ "This Email is system generated. Please do not reply to this email ID.<br><br>For any query, please send email at the e-mail IDs mentioned in "
					+ "FCRA website <b>https://fcraonline.nic.in </b></p>";
			return mailContent;
		}
		if(svcStatus.equals("9")){
			mailContent="<center><b>"+certHeader+"</b></center><br/>To,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"+appName+"</b><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"+appDate+"</b><br><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Please find attached the "+certName+" granted under FCRA, 2010 to your association <b>"+assoName+", "+assoAdd+"</b>."
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This e-mail may immediately be sent to your bank in which your"
					+ " designated FCRA bank account is being maintained.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Adobe Acrobat Reader version (5.0 or above) can be used to open this attachment. Please note that you will not be able to open this file or "
					+ "view the file properly with any version lower than Adobe Acrobat Reader 5.0. The attached digital communication is authenticated by a digital "
					+ "signature obtained from a certifying authority under the Information Technology Act, 2000.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The digital intimation is authenticated by a digital signature obtained from a certifying authority "
					+ "under the Information Technology Act, 2000.<br><br><b>("+myUserName+")</b><br><b>"+myUserDesignation+"</b><br><br><br>"
					+ "This Email is system generated. Please do not reply to this email ID.<br><br>For any query, please send email at the e-mail IDs mentioned in "
					+ "FCRA website <b>https://fcraonline.nic.in </b></p>";
		}else if(svcStatus.equals("10")){
			mailContent="<center><b>"+certHeader+"</b></center><br/>To,<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"+appName+"</b><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>"+appDate+"</b><br><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Please find attached the letter of refusal of registration under FCRA, 2010 to your association <b>"+assoName+", "+assoAdd+"</b>."
					+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "Adobe Acrobat Reader version (5.0 or above) can be used to open this attachment. Please note that you will not be able to open this file or "
					+ "view the file properly with any version lower than Adobe Acrobat Reader 5.0. The attached digital communication is authenticated by a digital "
					+ "signature obtained from a certifying authority under the Information Technology Act, 2000.<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The digital intimation is authenticated by a digital signature obtained from a certifying authority "
					+ "under the Information Technology Act, 2000.<br><br><b>("+myUserName+")</b><br><b>"+myUserDesignation+"</b><br><br><br>"
					+ "This Email is system generated. Please do not reply to this email ID.<br><br>For any query, please send email at the e-mail IDs mentioned in "
					+ "FCRA website <b>https://fcraonline.nic.in </b></p>";
		}
		return mailContent;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRegNumber() {
		return regNumber;
	}

	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getHosPdfFormat() {
		return hosPdfFormat;
	}

	public void setHosPdfFormat(String hosPdfFormat) {
		this.hosPdfFormat = hosPdfFormat;
	}
	
	public String getSvcCode() {
		return svcCode;
	}

	public void setSvcCode(String svcCode) {
		this.svcCode = svcCode;
	}

	public String getDigitalSignStatus() {
		return digitalSignStatus;
	}

	public void setDigitalSignStatus(String digitalSignStatus) {
		this.digitalSignStatus = digitalSignStatus;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
