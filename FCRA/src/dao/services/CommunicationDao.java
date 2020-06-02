package dao.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.owasp.esapi.ESAPI;

import sun.print.resources.serviceui;
import sun.util.locale.StringTokenIterator;
import utilities.InformationException;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.communication.AutoNotifier;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.lists.List5;
import utilities.notifications.Notification;
import models.services.requests.AbstractRequest;
import models.services.requests.ProjectRequest;
import dao.BaseDao;

public class CommunicationDao extends BaseDao<ProjectRequest, String, String>{
	private String appId;
	private String searchString;
	private String searchFlag;
	private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private String totalRecords;
	private String emailId;
	private String mobile;
	private String documentType;
	List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
	private List<Notification> notifyList = new ArrayList<Notification>();
	
	public CommunicationDao(Connection connection) {
		super(connection);
	}	

	public List<AbstractRequest> getApplicationListDetails() throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=null;
		String queryField=null;
		if(connection == null) {
			throw new Exception("Invalid connection");
		}			
		if(searchFlag.equals("reg")){
			query=new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS FROM FC_INDIA A  WHERE RCN=?");
		}else if(searchFlag.equals("id")){
			query=new StringBuffer("SELECT A.APPLICATION_ID,A.APPLICANT_NAME,A.SECTION_FILENO,A.CURRENT_STATUS FROM V_APPLICATION_DETAILS A  WHERE APPLICATION_ID=?");
		}		
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
		statement = connection.prepareStatement(countQuery.toString());	
		if(searchFlag.equals("reg")){
			statement.setString(1, searchString);
		}else if(searchFlag.equals("id")){
			statement.setString(1, searchString);			
		}
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			totalRecords = rs.getString(1);
		}		
		rs.close();
		statement.close();
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);		
		String queryForPaging = preparePagingQuery(query);	
		statement = connection.prepareStatement(queryForPaging);
		if(pageNum == null || recordsPerPage == null) {			
		}
		else {
			if(searchFlag.equals("reg")){
				statement.setString(1, searchString);										
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
			}else if(searchFlag.equals("id")){
				statement.setString(1, searchString);														
				statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				statement.setInt(3, (pageRequested-1) * pageSize + 1);
			}
			
		}
		rs = statement.executeQuery();
		List<AbstractRequest> applicationList = new ArrayList<AbstractRequest>();
		String statusDesc=null;
		while(rs.next()) {	
			if(rs.getString(4)==null || rs.getString(4).equals("") || rs.getString(4).equals("0"))
				statusDesc="ALIVE";
			else if(rs.getString(4).equals("1"));
				statusDesc="CANCELLED";
			applicationList.add(new ProjectRequest(connection,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),statusDesc,"","","","","","","",""));			
		}
		return applicationList;
	}
	private String preparePagingQuery(StringBuffer query) throws Exception {
		StringBuffer orderbyClause = new StringBuffer("");
		StringBuffer order = new StringBuffer("");
		if(sortColumn != null && sortColumn.equals("") == false) {
			if(sortColumn.equals("applicationId")) {
				orderbyClause.append(" ORDER BY APPLICATION_ID");
			}else if(sortColumn.equals("tempFileNo")) {
				orderbyClause.append(" ORDER BY TEMP_FILENO");
			}else if(sortColumn.equals("sectionFileNo")) {
				orderbyClause.append(" ORDER BY SECTION_FILENO");
			}else if(sortColumn.equals("submissionDate")) {
				orderbyClause.append(" ORDER BY SUBMISSION_DATE");
			}else if(sortColumn.equals("serviceName")) {
				orderbyClause.append(" ORDER BY SERVICE_CODE");
			}
			if(orderbyClause.equals("") == false) {
				if(sortOrder != null && sortOrder.equals("") == false) {
					if(sortOrder.equalsIgnoreCase("ASC")) {
						order.append("ASC");
					}
					else if(sortOrder.equalsIgnoreCase("DESC")) {
						order.append("DESC");
					}
				}
				if(order.toString().equals("") == false) {
					orderbyClause.append(" "+order);
				}
				else {
					orderbyClause = null;
				}
			}
		}
		if(orderbyClause != null && orderbyClause.equals("") == false)
			query.append(orderbyClause);
		
		StringBuffer queryForPaging = null;
		if(pageNum == null || recordsPerPage == null)
			queryForPaging = query;
		else
			queryForPaging = new StringBuffer("WITH T1 AS ("+query+"), "
					+ "T2 AS (SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
		return queryForPaging.toString();
	}
	public void getApplicationDetails(String appId,String queryFileds,String tableName,String whereClause,String searchType) throws Exception{		
		PreparedStatement statement=null;StringBuffer query =null;ResultSet rs=null;String resultString=null;		
		query = new StringBuffer("SELECT "+queryFileds+" FROM "+tableName+"  WHERE "+whereClause+"=?");		
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, appId);		
		rs=statement.executeQuery();
		String applicationId=null,secFileNo=null,appName=null,stateDistrict=null,state=null,district=null,rcn=null;
		if(rs.next()) {	
			applicationId=appId;
			secFileNo=rs.getString(2);
			appName=rs.getString(3);
			stateDistrict=rs.getString(4);		   			
		}else{			
			throw new ValidationException("Registration Number <b>"+appId+"</b> doesn't exist.Please search with valid registration number.");
		}
		rs.close();
		statement.close();
		
		// Getting Mail Ids and Mobile Numbers
		StringTokenizer token=null;
		if(searchType.equals("R")){	
			rcn=appId;
			getApplicantMailByRCN(appId);			
		}else if(searchType.equals("A")){						
			getApplicantMail(appId);		
			if(emailId.equals("NA") || mobile.equals("NA")){
				rcn=getApplicantRCN(appId);		// Getting RCN
				getApplicantMailByRCN(rcn);		// Getting email and mobile using RCN				
			}			
		}		
		// Getting Chief Functionary Name
		String cheifFunationary=null;
		if(rcn!=null){
			cheifFunationary=getChiefFunctionaryName(rcn);
		}		
		// Getting State and District
		resultString=getStateAndDistrict(stateDistrict);
		token=new StringTokenizer(resultString,":");
		state=token.nextToken();
		district=token.nextToken();
		
		// Adding data to list
		applicationList.add(new ProjectRequest(connection,applicationId,secFileNo,appName,state,district,emailId,mobile,cheifFunationary,0));
	}
	private String getChiefFunctionaryName(String RCN) throws Exception{
		PreparedStatement statement=null;String resultString=null;
		StringBuffer query=new StringBuffer("SELECT CHEIF_FUNCTIONARY_NAME FROM FC_ASSO_DETAILS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString()); 
		statement.setString(1, RCN);
		ResultSet rs=statement.executeQuery();	 
		if(rs.next()){			
			resultString=rs.getString(1);
		}
		rs.close();
		statement.close();
		return resultString;
	}
	private String getStateAndDistrict(String stateDistrict) throws Exception{
		PreparedStatement statement=null;String resultString=null;
		StringBuffer query=new StringBuffer("SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=SUBSTR(?,1,2)),(SELECT DISTNAME FROM TM_DISTRICT WHERE DISTCODE=SUBSTR(?,-3,3) "
				+ "AND SCODE=SUBSTR(?,1,2)) FROM DUAL");
		statement = connection.prepareStatement(query.toString()); 
		statement.setString(1, stateDistrict);
		statement.setString(2, stateDistrict);
		statement.setString(3, stateDistrict);
		ResultSet rs=statement.executeQuery();	
		if(rs.next()){
			resultString=rs.getString(1)+":"+rs.getString(2);
		}
		rs.close();
		statement.close();
		return resultString;
	}
	private void getApplicantMailByRCN(String rcn) throws Exception{
		PreparedStatement statement=null;
		StringBuffer query=new StringBuffer("SELECT ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE FROM FC_ASSO_DETAILS WHERE RCN=?");
		statement = connection.prepareStatement(query.toString()); 
		statement.setString(1, rcn);
		ResultSet rs=statement.executeQuery();	 
		if(rs.next()){			
			if(rs.getString(1)==null){
				emailId="NA";
			}else{
				emailId=rs.getString(1);
			}
			if(rs.getString(2)==null){
				mobile="NA";
			}else{
				mobile=rs.getString(2);
			}			
		}
		rs.close();
		statement.close();
	}
	private String getApplicantRCN(String uniqueFileno) throws Exception{
		PreparedStatement statement=null;String resultField=null,tableName=null,rcn=null,query=null,svcCode=null;ResultSet rs=null;
		// Getting service_code 
		query="SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?";
		statement = connection.prepareStatement(query); 
		statement.setString(1, uniqueFileno);
		rs=statement.executeQuery();	 
		if(rs.next()){			
			svcCode=rs.getString(1);
		}	
		rs.close();
		statement.close();
		
		if(svcCode.equals("03")){ 	// Renewal
			tableName="FC_FC5_ENTRY_NEW1";
			resultField="ASSO_REGISTRATION_NO";
		}else if(svcCode.equals("06")){		// Change of Details
			tableName="FC_FC6_FORM";
			resultField="RCN";			
		}else if(svcCode.equals("06")){		// Change of Details
			tableName="FC_FC5_FUNDTRANSFERENTRY";
			resultField="RCN_OR_PP_NUMBER";			
		}
		
		query="SELECT "+resultField+" FROM "+tableName+" WHERE UNIQUE_FILENO=?";
		statement = connection.prepareStatement(query); 
		statement.setString(1, uniqueFileno);
		rs=statement.executeQuery();	 
		if(rs.next()){			
			rcn=rs.getString(1);
		}
		rs.close();
		statement.close();
		return rcn;
	}
	private void getApplicantMail(String appId) throws Exception{
		PreparedStatement statement=null;String svcCode=null;String resultField=null,tableName=null,resultString=null;
		// Getting service_code 
		String query="SELECT SERVICE_CODE FROM V_APPLICATION_DETAILS WHERE APPLICATION_ID=?";
		statement = connection.prepareStatement(query); 
		statement.setString(1, appId);
		ResultSet rs=statement.executeQuery();	 
		if(rs.next()){			
			svcCode=rs.getString(1);
		}
		rs.close();
		statement.close();
		// Getting emailIds
		if(svcCode.equals("01")){			// Registration			
			tableName="T_FC8_ENTRY";
			resultField="ASSO_EMAIL,ASSO_MOBILE";
		}else if(svcCode.equals("02")){ 	// Prior Permission
			tableName="FC_FC1A_ENTRY";
			resultField="ASSO_EMAIL,ASSO_MOBILE";
		}else if(svcCode.equals("03")){ 	// Renewal
			tableName="FC_FC5_ENTRY_NEW1";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE";
		}else if(svcCode.equals("04")){		// Annual Returns
			tableName="";
			resultField="";
			emailId="NA";
			mobile="NA";
			return;
		}else if(svcCode.equals("05")){		// Transfer of Fund
			tableName="FC_FC5_FUNDTRANSFERENTRY";
			resultField="RECIPIENT_EMAIL,RECIPIENT_CHEIF_MOBILE";
		}else if(svcCode.equals("06")){		// Change of Details
			tableName="FC_FC6_FORM";
			resultField="ASSO_OFFICIAL_EMAIL,ASSO_CHEIF_MOBILE";
		}else if(svcCode.equals("07")){		// Hospitality
			tableName="FC_H_ENTRY";
			resultField="APPT_EMAIL,APPT_MOBILE";
		}else if(svcCode.equals("08")){		// Foreign contribution to individual as gift
			tableName="FC_FC1_GIFT_ENTRY";
			resultField="RECIPIENT_EMAIL,RECIPIENT_MOBNO";
		}else if(svcCode.equals("09")){		// Foreign contribution to individual as articles
			tableName="FC_FC1_ARTCL_SECURITIES_ENTRY";
			resultField="APPLICANT_EMAIL,null";
		}else if(svcCode.equals("10")){		// Foreign contribution to individual as security
			tableName="FC_FC1_SECURITIES_ENTRY1";
			resultField="APPLICANT_EMAIL,null";
		}else if(svcCode.equals("11")){		// Foreign contribution to a candidate for election
			tableName="FC_FC1_ELECTION_ENTRY";
			resultField="APPLICANT_EMAIL,APPLICANT_MOBILE_NO";
		}
		String query1="SELECT "+resultField+" FROM "+tableName+" WHERE UNIQUE_FILENO=?";
		statement = connection.prepareStatement(query1); 
		statement.setString(1, appId);
		ResultSet rs1=statement.executeQuery();	 
		if(rs1.next()){			
			if(rs1.getString(1)==null){
				emailId="NA";
			}else{
				emailId=rs1.getString(1);
			}
			if(rs1.getString(2)==null){
				mobile="NA";
			}else{
				mobile=rs1.getString(2);
			}			
		}
		rs1.close();
		statement.close();
	}	
	
	public void sendEmail(String appId,String communicationType,String searchType,String mailSubject,String mailBody,String myOfficeCode,String sessionId, String myUserId) throws Exception{		
		if(communicationType.equals("0")){			// Bulk
			sendMailToAll(sessionId,mailSubject,mailBody,myOfficeCode, appId, myUserId);			
		}else if(communicationType.equals("1")){	// Individual
			getRecepientMailId(appId,searchType);
			
			// Sending Mails to Applicants/Associations
		    AutoNotifier notifier=new AutoNotifier(); 		
	 		// Getting attachment		
	 		List<KVPair<String, byte[]>> attachment=getAttachment(sessionId);		
	 		if(emailId!=null){
	 			notifier.setAttachmentList(attachment);
	 			/*notifier.setAttachment(attachment);
	 			notifier.setAttachmentName("Document."+documentType);
	 			*/notifier.setEmailSubject(mailSubject);
	 			notifier.setMyUserId(myUserId);
	 			notifyList=notifier.pushAutoNotificationsMultiple(appId, Integer.valueOf(99), "2", emailId, connection,myOfficeCode,mailBody);
	 		}else{
	 			throw new ValidationException("Unbale to send the mail as recipient's Mail Id(s) are not available.");
	 		}
		}		
	}
	public void sendSMS(String appId,String communicationType,String searchType,String smsBody,String myOfficeCode, String myUserId) throws Exception{		
		if(communicationType.equals("0")){			// Bulk
			sendSMSToAll(smsBody,myOfficeCode, appId, myUserId);			
		}else if(communicationType.equals("1")){	// Individual
			getRecepientMailId(appId,searchType);	// Same in case of getting Mobile Numbers
			
			// Sending SMS(s) to Applicants/Associations
		    AutoNotifier notifier=new AutoNotifier();	 				
	 		if(mobile!=null){	 			
	 			notifier.setPhoneNumber(mobile);
	 			notifier.setMyUserId(myUserId);
	 			notifyList=notifier.pushAutoNotifications(appId, Integer.valueOf(99), "1", "", connection,myOfficeCode,smsBody);
	 		}else{
	 			throw new ValidationException("Unbale to send the SMS as recipient's Mobile Number(s) are not available.");
	 		}
		}		
	}
	private List<KVPair<String, byte[]>> getAttachment(String sessionId) throws Exception{
		PreparedStatement statement=null;byte[] attachment=null; 
		List<KVPair<String,byte[]>> attachmentList=new ArrayList<KVPair<String,byte[]>>();
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query=new StringBuffer("SELECT DOCUMENT,TYPE,NAME FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, sessionId);		
		ResultSet rs=statement.executeQuery();
		while(rs.next()){
			String name = rs.getString(3);
			String NameReplace = name.replace("..", "");
			 name = NameReplace.replaceAll("\\s", "");
			attachmentList.add(new KVPair<String,byte[]>(name, rs.getBytes(1)));
			/*attachment=rs.getBytes(1);
			documentType=rs.getString(2);*/
		}
		rs.close();
		statement.close();
		return attachmentList;
	}
	public void getRecepientMailId(String appId,String searchType) throws Exception{		
		if(searchType.equals("R")){				// By RCN
			getApplicantMailByRCN(appId);			
		}else if(searchType.equals("A")){		// BY Application Id
			String rcn=null;
			getApplicantMail(appId);		
			if(emailId.equals("NA") || mobile.equals("NA")){
				rcn=getApplicantRCN(appId);		// Getting RCN
				getApplicantMailByRCN(rcn);		// Getting email and mobile using RCN				
			}	
		}		
	}
	public void sendMailToAll(String sessionId,String mailSubject,String mailBody,String myOfficeCode, String appId, String myUserId) throws Exception{
		PreparedStatement statement=null;ResultSet rs=null;String rcn=null;  StringBuffer query = null;
		StringTokenizer tt = new StringTokenizer(appId, ",");
		Map<String,String> map = new HashMap<String,String>();
		String tt1 = "";
		while(tt.hasMoreElements()){
			tt1 = tt.nextToken();
			map.put(tt1, tt1);
		}
		String arr[] = new String[map.size()];     
		int i=0;       
		List<KVPair<String, byte[]>> attachment=getAttachment(sessionId);		
		AutoNotifier notifier=new AutoNotifier();
		notifier.setAttachmentList(attachment);
		/*notifier.setAttachment(attachment);
		notifier.setAttachmentName("Document."+documentType);
		*/notifier.setEmailSubject(mailSubject);
		notifier.setMyUserId(myUserId);
		for(Map.Entry k:map.entrySet())
		{
			if(ESAPI.validator().isValidInput("applicationId", k.getKey().toString(), "Word", 15, false) == false){
				throw new ValidationException("Invalid Application ID");
			}
			arr[i] = (String) k.getKey();      
			i++;     
		}
		String ww = "";
		String notificationType = "2";
		Integer status = Integer.valueOf(99);
		query = new StringBuffer("SELECT RCN,ASSO_EMAIL , ASSO_NAME FROM COVID_TABLE WHERE RCN IN(SELECT regexp_substr(?, '[^,]+', 1, LEVEL) FROM dual CONNECT BY LEVEL <= length(?) - length(REPLACE(?, ',', '')) + 1)");
		statement = connection.prepareStatement(query.toString());			
		for(int j=0;j<arr.length;j += 100){
			ww="";
			for(int k = j;(k<j+100)&&(k<arr.length);k++){
				ww = ww+arr[k]+",";
			}
			String qryInput = ww.substring(0, ww.length()-1);
			statement.setString(1, qryInput);
			statement.setString(2, qryInput);
			statement.setString(3, qryInput);
			rs=statement.executeQuery();
			List<List3> mailList=new ArrayList<List3>();
			while(rs.next()){
				rcn=rs.getString(1); 
				emailId=rs.getString(2);
				String asso_name = rs.getString(3);
				mailList.add(new List3(rcn, emailId,asso_name));			
			}
			rs.close();
			//long lStartTime = System.currentTimeMillis();
			//System.out.println(lStartTime);
			if(mailList.size()>0){
				int m=0;
				while(m<mailList.size()){			     		
					// Getting attachment	
					String mailId = mailList.get(m).getP2();
					String rcn1 = mailList.get(m).getP1();
					String assname = mailList.get(m).getP3();
					String mailbd = "To<br>FCRA Registration No. : "+ mailList.get(m).getP1()+ "<br>FCRA Association Name : "+mailList.get(m).getP3()+"<br><br>"+mailBody;
				//	System.out.println("@@@@@@@@@@@@@@@@@@@@@@ "+rcn1+" "+ mailId+" "+assname);
					if(mailId!=null){
						notifier.pushAutoNotificationsMultiple(mailList.get(m).getP1(), status, notificationType, mailId, connection,myOfficeCode,mailbd);
						
					}
					m++;
				}
			}	
			//long lEndTime = System.currentTimeMillis();
			//long output = lEndTime - lStartTime;
			//System.out.println("j-value" +j + "Elapsed time in milliseconds: " + output);
		}
		statement.close();
}
	
	public void sendSMSToAll(String smsBody,String myOfficeCode, String appId, String myUserId) throws Exception{
		PreparedStatement statement=null;ResultSet rs=null;String rcn=null;  StringBuffer query = null;
		StringTokenizer tt = new StringTokenizer(appId, ",");
		Map<String,String> map = new HashMap<String,String>();
		String tt1 = "";
		while(tt.hasMoreElements()){
			tt1 = tt.nextToken();
			map.put(tt1, tt1);
		}
		String arr[] = new String[map.size()];     
		int i=0;       
		for(Map.Entry k:map.entrySet())
		{
			if(ESAPI.validator().isValidInput("applicationId", k.getKey().toString(), "Word", 15, false) == false){
				throw new ValidationException("Invalid Application ID");
			}	
			arr[i] = (String) k.getKey();      
			i++;     
		}
		String ww = "";
		for(int j=0;j<arr.length;j += 100){
			ww="";
			for(int k = j;(k<j+100)&&(k<arr.length);k++){
				ww = ww+arr[k]+",";
			}
			query=new StringBuffer("SELECT RCN,ASSO_MOBILE FROM COVID_TABLE WHERE RCN IN(SELECT regexp_substr(?, '[^,]+', 1, LEVEL) FROM dual CONNECT BY LEVEL <= length(?) - length(REPLACE(?, ',', '')) + 1)");
			statement = connection.prepareStatement(query.toString());		
			statement.setString(1, ww.substring(0, ww.length()-1));
			statement.setString(2, ww.substring(0, ww.length()-1));
			statement.setString(3, ww.substring(0, ww.length()-1));
			rs=statement.executeQuery();
			List<List2> mobileList=new ArrayList<List2>();
			while(rs.next()){			
				rcn=rs.getString(1);
				mobile=rs.getString(2);
				mobileList.add(new List2(rcn, mobile));			
			}
			rs.close();
			statement.close();
			if(mobileList.size()>0){
				int m=0;
				AutoNotifier notifier=new AutoNotifier();
				while(m<mobileList.size()){		 		
			 		if(mobileList.get(m).getLd()!=null){
			 			notifier.setPhoneNumber(mobileList.get(m).getLd());
			 			notifier.setMyUserId(myUserId);
			 			notifier.pushAutoNotifications(mobileList.get(m).getLi(), Integer.valueOf(99), "1", "", connection,myOfficeCode,smsBody);
			 		}
			 		m++;
				}
			}				
		}
}
	
	public String deleteUploadCache(String sessionId) throws Exception{
		PreparedStatement statement=null;
		// DELETE FROM T_UPLOAD CACHE
		StringBuffer query2=new StringBuffer("DELETE FROM T_UPLOAD_CACHE WHERE SESSION_ID=?");
		statement = connection.prepareStatement(query2.toString());
		statement.setString(1, sessionId);	
		statement.executeUpdate();
		statement.close();
		return "success";
	}
	
	public List<List5> getBulkAssociationList(String associationType, String state,
			String assoDistrict, String assoBlockYear) throws Exception {
		// TODO Auto-generated method stub
		if(associationType.equals("2")){
			if(ESAPI.validator().isValidInput("blockYear", assoBlockYear, "WordSS", 9, false) == false){
				throw new ValidationException("Please select Block Year.");
			}
		}
		PreparedStatement statement = null;
		StringBuffer query = null; String stateqry = ""; String districtqry = "";
		if(!state.equals("")){
			stateqry = " and SUBSTR(stdist,1,2)=? ";
			if(!assoDistrict.equals(""))
				districtqry = " and substr(stdist,-3,3) = ? ";
		}
		if(associationType.equals("1")){
			query = new StringBuffer("SELECT RCN, ASSO_NAME,  CASE WHEN NEW_OLD='N' THEN ASSO_ADDRESS ELSE ADD1||' '||ADD2||' '||ADD3||' - '||ASSO_PIN END  as ASSO_ADDRESS,nvl((select DISTNAME from TM_DISTRICT where distcode = substr(stdist,-3,3) and SCODE = SUBSTR(stdist,1,2)),' ') , "
					+ "nvl((select SNAME from TM_STATE where SCODE =SUBSTR(stdist,1,2)), ' ') FROM FC_INDIA WHERE CURRENT_STATUS=0 AND STATUS != 'D'  and "
					+ "valid_to>=sysdate and ASSOCIATION_TYPE is null "+stateqry+districtqry+"   order by upper(asso_name)");
		} 
		else if(associationType.equals("2")){
			query = new StringBuffer(" Select rcn,asso_name,  CASE WHEN NEW_OLD='N' THEN ASSO_ADDRESS ELSE ADD1||' '||ADD2||' '||ADD3||' - '||ASSO_PIN END  as ASSO_ADDRESS,nvl((select DISTNAME from TM_DISTRICT where distcode = substr(stdist,-3,3) and SCODE = SUBSTR(stdist,1,2)),' ') , "
					+ "(select SNAME from TM_STATE where SCODE =SUBSTR(stdist,1,2)) from fc_india  where status != 'D' and (reg_date is null or"
					+ " reg_date<trunc(to_date('01-04-'||substr( ? ,-4,4),'dd-mm-yyyy'))) and rcn not in (select rcn from fc_fc3_tally where final_submit='Y' and"
					+ " blkyear= ?  and substr(rcn,-1,1)='R') and cancel_status='N' and ASSOCIATION_TYPE is null "+stateqry+districtqry+" order by upper(asso_name)");
		}
		else if(associationType.equals("3")){
			query = new StringBuffer(" with temp as (  select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name,  CASE WHEN NEW_OLD='N' THEN ASSO_ADDRESS ELSE ADD1||' '||ADD2||' '||ADD3||' - '||ASSO_PIN END  as ASSO_ADDRESS,"
					+ " nvl((select DISTNAME from TM_DISTRICT where distcode = substr(stdist,-3,3) and SCODE = SUBSTR(stdist,1,2)),' ') as dist,"
					+ " (select SNAME from TM_STATE where SCODE =SUBSTR(stdist,1,2)) as state, ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, "
					+ "tbl1.red_flag_category as cat_code from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4  where tbl1.status='0' and tbl4.rcn=tbl3.rcn and "
					+ "tbl1.rcn=tbl3.rcn  "+stateqry+districtqry+" ) select rcn, asso_name, ASSO_ADDRESS, dist, state from temp where last_update=1 and  cat_code in (2) order by asso_name");
		}
		else if(associationType.equals("4")){
			query = new StringBuffer("select * from COVID_TABLE");
		}
		statement = connection.prepareStatement(query.toString());
		if(associationType.equals("1")||associationType.equals("3")){
			if(!state.equals("")){
				statement.setString(1, state);
				if(!assoDistrict.equals(""))
					statement.setString(2, assoDistrict);
			}
		}else if(associationType.equals("2")){
			statement.setString(1, assoBlockYear);
			statement.setString(2, assoBlockYear);
			if(!state.equals("")){
				statement.setString(3, state);
				if(!assoDistrict.equals(""))
					statement.setString(4, assoDistrict);
			}
		}

		ResultSet rs = statement.executeQuery();
		List<List5> applicationList = new ArrayList<List5>();
		String assoAddress = ""; String assDistrict = ""; String assoState = "";
		while(rs.next()) {
			if(rs.getString(3)==null){
				assoAddress = "";
			}
			else
				assoAddress = rs.getString(3);
			if(rs.getString(4)==null){
				assDistrict = "";
			}
			else
				assDistrict = rs.getString(4);
			/*if(rs.getString(5)==null){
				assoState = "";
			}
			else
				assoState = rs.getString(5);*/
			applicationList.add(new List5(rs.getString(1), rs.getString(2), assoAddress, assDistrict, "null"));
		}return applicationList;
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



	public String getAppId() {
		return appId;
	}



	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
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

	public List<AbstractRequest> getApplicationList() {
		return applicationList;
	}

	public void setApplicationList(List<AbstractRequest> applicationList) {
		this.applicationList = applicationList;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public List<Notification> getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(List<Notification> notifyList) {
		this.notifyList = notifyList;
	}
}
