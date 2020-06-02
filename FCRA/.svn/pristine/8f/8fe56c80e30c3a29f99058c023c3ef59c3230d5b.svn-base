package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;









import java.util.Map;

import models.reports.DonorListReport;
import models.reports.UserActivityReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.ServicesDao;
import dao.master.StateDao;
import dao.master.UserDao;
import dao.reports.MISReportGenerator;

public class UserActivityReportGenerator extends MISReportGenerator {
	private List<UserActivityReport> userActivityReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private List<String> selectServiceList;
	private List<String> selectUserActivityList;
	private String sortColumn;
	private String sortOrder;
	private int virtualizationMaxSize = 200;
	private String loginUserName;
	private String loginOfficeName;
	private String myLoginofficecode;
	private String activityFormate;
	private String fromDate;
	private String toDate;
	private String reportDisplyType;
	private String officeId;
	public UserActivityReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			activityFormate = parameterMap.get("userActivity").get(0).toString();
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			
			reportDisplyType=parameterMap.get("reportDisplyType").get(0);
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
			selectServiceList=parameterMap.get("service-type");
			if(activityFormate.equals("A")){
			fromDate=parameterMap.get("from-date").toString();
			toDate=parameterMap.get("to-date").toString();
			}
			else if(activityFormate.equals("D")){
				fromDate=parameterMap.get("from-date").toString();
				toDate=parameterMap.get("to-date").toString();
				officeId=parameterMap.get("myLoginOfficId").get(0);
			}
			selectUserActivityList=parameterMap.get("user-List");
			myLoginofficecode=parameterMap.get("myLoginOfficCode").toString();
			
		
		}
}
	
	
	
	@Override
	protected void generateHTML() throws Exception {
		
		if(activityFormate.equals("P"))
		 {
			userActivityReport=getDetailsUserActivityPendencyHtml();	
			totalRecords=getTotalRecords();	
		 }
	 else if(activityFormate.equals("A")){
		 userActivityReport=getDetailsUserActivityHtml();	
			totalRecords=getTotalRecords();	
			 }
	 else if(activityFormate.equals("D")){
		 userActivityReport=getDisposedUserActivityHtml();	
			totalRecords=getTotalRecords();	
			 }
		
			
		}
	
	
	
	
	
	
	
	
	
	
	////Pendency Details
	public List<UserActivityReport> getDetailsUserActivityPendencyHtml() throws Exception {
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}
 							String ServiceList="1=1";
 							String UserList="1=1";
 							String	fcode=myLoginofficecode.toString().replace("[", "'").replace("]", "'").replace(", ", ",").trim();
 							String srvList=selectServiceList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 							if(!srvList.trim().equals("'ALL'")){
 								ServiceList= "B.SERVICE_CODE in ("+srvList+")";
 							}
					
							String usrList=selectUserActivityList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
							if(!usrList.trim().equals("'ALL'")){
								UserList= "A.USER_ID in ("+usrList+")";
							} 
							  String countQuery=null;
						     String query=null;
							if(reportDisplyType.equalsIgnoreCase("s")){
							 countQuery =" SELECT COUNT(1) FROM ( WITH t1 AS (SELECT A.APPLICATION_ID, "
									+ " B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.STATE "
									+ " FROM T_PC_USER_LEVEL_STATUS A,V_APPLICATION_DETAILS B WHERE "
									+ " A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
									+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND "
									+ " "+UserList+" ) SELECT * FROM (SELECT application_id, user_name,t1.user_id, "
									+ " service_desc,stage,t1.STATE FROM t1,tm_user b, tm_service s "
									+ " WHERE t1.service_code=s.service_code and t1.user_id=b.user_id) pivot "
									+ " (count(application_id) FOR(stage) IN ('1' AS PendingForProcessing, '2' AS PendingForMail)) "
									+ "ORDER BY user_name,state,service_desc)";
						
							 query =" WITH t1 AS (SELECT A.APPLICATION_ID, "
									+ " B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.STATE "
									+ " FROM T_PC_USER_LEVEL_STATUS A,V_APPLICATION_DETAILS B WHERE "
									+ " A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
									+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND "
									+ " "+UserList+") ,P1 as  ( SELECT * FROM (SELECT application_id, user_name,t1.user_id, "
									+ " service_desc,stage,(select sname from TM_STATE where  scode=t1.STATE) as state FROM t1,tm_user b, tm_service s "
									+ " WHERE t1.service_code=s.service_code and t1.user_id=b.user_id) pivot "
									+ " (count(application_id) FOR(stage) IN ('1' AS PendingForProcessing, '2' AS PendingForMail)) "
									+ " ORDER BY user_name,state,service_desc),T2 AS (SELECT P1.*, ROWNUM RN FROM P1 ) SELECT * FROM T2 WHERE RN between ? and ?";
	     }
							else{
								countQuery=" select count(*) from( WITH t1 AS (SELECT A.APPLICATION_ID,  B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID,"
										+ " ACTIVITY_ON, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage  FROM T_PC_USER_LEVEL_STATUS A,"
										+ "V_APPLICATION_DETAILS B WHERE  A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
										+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND  "+UserList+" ) SELECT application_id, user_name,t1.user_id, "
										+ " service_desc,ACTIVITY_ON received_on, trunc(sysdate)-trunc(ACTIVITY_ON) pending_for_days,"
										+ " stage, DECODE (stage, 1, 'Pending For Processing',  "
										+ " 2, 'Pending for Mailing') statusname FROM t1,tm_user b, tm_service s "
										+ " WHERE t1.service_code=s.service_code and t1.user_id=b.user_id ORDER BY pending_for_days)";
								
								query=" WITH t1 AS (SELECT A.APPLICATION_ID,  B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, "
										+ "ACTIVITY_ON, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.state  FROM T_PC_USER_LEVEL_STATUS A, "
										+ "V_APPLICATION_DETAILS B WHERE  A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
										+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND  "+UserList+" ),P1 as (SELECT application_id, user_name,t1.user_id, "
										+ " service_desc,ACTIVITY_ON received_on, trunc(sysdate)-trunc(ACTIVITY_ON) pending_for_days,"
										+ " stage, DECODE (stage, 1, 'Pending For Processing',  "
										+ " 2, 'Pending for Mailing') statusname,(select sname from TM_STATE where scode=state) as state,APPLICANT_NAME FROM t1,tm_user b, tm_service s "
										+ "	 WHERE t1.service_code=s.service_code and t1.user_id=b.user_id ORDER BY user_name,state,service_desc ),T2"
										+ " AS (SELECT P1.*, ROWNUM RN FROM P1 ) SELECT * FROM T2 WHERE  RN between ? and ? ";
              
							}
								 PreparedStatement statement = connection.prepareStatement(countQuery.toString());
								 ResultSet rs = statement.executeQuery();
								  if(rs.next())
								     { 
									  totalRecords = rs.getString(1);
								      } 
								  rs.close();
								 statement.close();
								 Integer pageRequested = Integer.parseInt(pageNum);
								 Integer pageSize = Integer.parseInt(recordsPerPage);
								 statement = connection.prepareStatement(query.toString());
								 if(pageNum == null || recordsPerPage == null) {
						
								 }
								 else {
									 statement.setInt(1, (pageRequested-1) * pageSize + 1);
									 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
								 
								 }
								 rs = statement.executeQuery();
								List<UserActivityReport> userActivityReportList = new ArrayList<UserActivityReport>();
								while(rs.next()) {
									if(reportDisplyType.equalsIgnoreCase("s"))
									userActivityReportList.add(new UserActivityReport(rs.getString(1)+" ["+rs.getString(2)+" ]", rs.getString(3),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString("STATE"))); 
									else{
										userActivityReportList.add(new UserActivityReport(rs.getString(1),rs.getString(2)+" ["+rs.getString(3)+" ]",rs.getString(4),rs.getString(6),rs.getString(8),rs.getString(9),rs.getString("state"),rs.getString("APPLICANT_NAME"),0))	;
									}
								}
							return userActivityReportList;
										}
	
	
	
	
	
	
	
	
	
	///START Activity Details
	//Activity
	public List<UserActivityReport> getDetailsUserActivityHtml() throws Exception {
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}
 							String ServiceList="1=1";
 							String UserList="1=1";
 							String	fcode=myLoginofficecode.toString().replace("[", "'").replace("]", "'").replace(", ", ",").trim();
 							String srvList=selectServiceList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 							if(!srvList.trim().equals("'ALL'")){
 								ServiceList= "service_code in ("+srvList+")";
 							}
					
 							
							String usrList=selectUserActivityList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
							if(!usrList.trim().equals("'ALL'")){
								UserList= "user_id in ("+usrList+")";
							}
							
							String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
							
							String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();

						     StringBuffer countQuery=null;
						     StringBuffer query=null;
 							//change it gggggggggggggggggg
		if(reportDisplyType.equalsIgnoreCase("s")){
			countQuery = new StringBuffer( " WITH t AS( SELECT DISTINCT application_id, to_USERID user_id, 1 status, status_date FROM t_pc_communication WHERE trunc(status_date) "
					+ "  BETWEEN trunc(to_date('"
					+ FROM
					+ "','dd-mm-yyyy'))  AND trunc(to_date('"
					+ TO
					+ "','dd-mm-yyyy')) AND to_USERID IS NOT NULL UNION ALL SELECT DISTINCT application_id, by_USERID user_id,  2 status, status_date  "
					+ " FROM t_pc_communication  WHERE trunc(status_date) BETWEEN trunc(to_date('"
					+ FROM
					+ "','dd-mm-yyyy')) AND  trunc(to_date('"
					+ TO
					+ "','dd-mm-yyyy')) AND by_USERID IS NOT NULL) "
					+ " SELECt count(*) from( SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,  b.office_code, user_name, t.user_id, status,v.state FROM t, tm_user b, v_application_details v,  tm_service s  "
					+ " WHERE  t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code) pivot (count(application_id) FOR(status) IN ('1' AS Received, '2' AS Processed)) "
					+ " where "
					+ ServiceList
					+ " and "
					+ UserList
					+ " "
					+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc)");
							
			query = new StringBuffer(" WITH t AS("
										+ " SELECT DISTINCT application_id, to_USERID user_id, 1 status, status_date"
										+ " FROM t_pc_communication WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
										+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND to_USERID IS NOT NULL"
										+ " UNION ALL SELECT DISTINCT application_id, by_USERID user_id, "
										+ " 2 status, status_date FROM t_pc_communication "
										+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
										+ " trunc(to_date('"+TO+"','dd-mm-yyyy')) AND by_USERID IS NOT NULL),P1 as("
										+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code, "
										+ " b.office_code, user_name, t.user_id, status,(select sname from tm_state where scode=v.state) as state FROM t, tm_user b, v_application_details v, "
										+ " tm_service s WHERE "
										+ " t.user_id=b.user_id and t.application_id=v.application_id and "
										+ " v.service_code=s.service_code)"
										+ " pivot (count(application_id) FOR(status) IN ('1' AS Received, '2' AS Processed))"
										+ " where "+ServiceList+" and "+UserList+" and "
										+ " office_code in("+fcode+")"
										+ " ORDER BY user_name,state,service_desc) , T3 AS (SELECT P1.*, ROWNUM RN FROM P1 ) "
										+ " SELECT * FROM T3 WHERE RN between ? and ? ");
			
		}
		else
		{
			countQuery = new StringBuffer("  select count(*) from ( WITH t AS( SELECT DISTINCT application_id, to_USERID user_id, 1 status, "
					+ " status_date FROM t_pc_communication "
					+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
					+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) "
					+ " AND to_USERID IS NOT NULL UNION ALL SELECT DISTINCT application_id, "
					+ " by_USERID user_id,  2 status, status_date FROM t_pc_communication "
					+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
					+ "  trunc(to_date('"+TO+"','dd-mm-yyyy')) "
					+ " AND by_USERID IS NOT NULL) "
					+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, "
					+ " v.service_code,  b.office_code,  user_name ||'[' || t.user_id ||']' as user_name, "
					+ " t.user_id, status FROM t, tm_user b, v_application_details v, "
					+ " tm_service s  WHERE  t.user_id=b.user_id and t.application_id=v.application_id "
					+ " and  v.service_code=s.service_code) "
					+ " pivot (count(status) FOR(status) IN ('1' AS Received, '2' AS Processed)) "
					+ " where "+ServiceList+" "
					+ " and "+UserList+" "
					+ " and  office_code in("+fcode+") ORDER BY user_name)");
						
		query = new StringBuffer("  WITH t AS( SELECT DISTINCT application_id, to_USERID user_id, 1 status, "
					+ " status_date FROM t_pc_communication "
					+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
					+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) "
					+ " AND to_USERID IS NOT NULL UNION ALL SELECT DISTINCT application_id, "
					+ " by_USERID user_id,  2 status, status_date FROM t_pc_communication "
					+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
					+ "  trunc(to_date('"+TO+"','dd-mm-yyyy')) "
					+ " AND by_USERID IS NOT NULL),P1 as("
					+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, "
					+ " v.service_code,  b.office_code,  user_name ||'[' || t.user_id ||']' as user_name, "
					+ " t.user_id, status,(select sname from Tm_state where scode=v.state) as state,v.APPLICANT_NAME FROM t, tm_user b, v_application_details v, "
					+ " tm_service s  WHERE  t.user_id=b.user_id and t.application_id=v.application_id "
					+ " and  v.service_code=s.service_code) "
					+ " pivot (count(status) FOR(status) IN ('1' AS Received, '2' AS Processed)) "
					+ " where "+ServiceList+" "
					+ " and "+UserList+" "
					+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc) , T3 AS (SELECT P1.*, ROWNUM RN FROM P1 ) "
									+ " SELECT * FROM T3 WHERE RN between ? and ? ");
		}
							
								 PreparedStatement statement = connection.prepareStatement(countQuery.toString());
								 ResultSet rs = statement.executeQuery();
								  if(rs.next())
								     { 
									  totalRecords = rs.getString(1);
								      } 
								  rs.close();
								 statement.close();
								 Integer pageRequested = Integer.parseInt(pageNum);
								 Integer pageSize = Integer.parseInt(recordsPerPage);
								 statement = connection.prepareStatement(query.toString());
								 if(pageNum == null || recordsPerPage == null) {
						
								 }
								 else {
									 statement.setInt(1, (pageRequested-1) * pageSize + 1);
								 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
								 
								 }
							
								 rs = statement.executeQuery();
								List<UserActivityReport> userActivityReportList = new ArrayList<UserActivityReport>();
								while(rs.next()) {
									if(reportDisplyType.equalsIgnoreCase("s"))
										
									userActivityReportList.add(new UserActivityReport(rs.getString(4)+" ["+rs.getString(5)+" ]", rs.getString(1),rs.getString(5),rs.getString(2),rs.getString(8),rs.getString(7),rs.getString(3),rs.getString("state")));
									else
										userActivityReportList.add(new UserActivityReport(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5),rs.getString(6),rs.getString("Received"),rs.getString("Processed"),rs.getString(9),rs.getString("state"),rs.getString("APPLICANT_NAME"),0));
								}
							return userActivityReportList;
								
							}///END
	
	///	START DISPOSED DETAILS 
	
	public List<UserActivityReport> getDisposedUserActivityHtml() throws Exception {
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}
 							String ServiceList="1=1";
 							String UserList="1=1";
 							String	fcode=myLoginofficecode.toString().replace("[", "'").replace("]", "'").replace(", ", ",").trim();
 							String srvList=selectServiceList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 							if(!srvList.trim().equals("'ALL'")){
 								ServiceList= "service_code in ("+srvList+")";
 							}
					
 							
							String usrList=selectUserActivityList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
							if(!usrList.trim().equals("'ALL'")){
								UserList= "user_id in ("+usrList+")";
							}
							
							String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
							
							String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();

						     StringBuffer countQuery=null;
						     StringBuffer query=null;
 							//change it gggggggggggggggggg
		if(reportDisplyType.equalsIgnoreCase("s")){
			countQuery = new StringBuffer( "  select count(*) from (with t as (SELECT c.service_desc, d.user_id, A.status,"
					+ " A.application_id,b.STATE FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
					+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
					+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
					+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
					+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
					+ " b.office_code, user_name, t.user_id, status,t.state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
					+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
					+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
					+ " where "
					+ ServiceList
					+ " and "
					+ UserList
					+ " and  office_code in(" + fcode + ") ORDER BY user_name)  ");
							
			query = new StringBuffer("with t as (SELECT c.service_desc, d.user_id, A.status,"
					+ " A.application_id,b.state FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
					+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
					+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
					+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
					+ " c.service_desc, d.user_id),P1 as(SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
					+ " b.office_code, user_name, t.user_id, status,(select sname from TM_STATE where scode=t.STATE) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
					+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
					+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
					+ " where "
					+ ServiceList
					+ " and "
					+ UserList
					+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc) , T3 AS (SELECT P1.*, ROWNUM RN FROM P1 ) "
					+ " SELECT * FROM T3 WHERE RN between ? and ? ");
			
		}
		else
		{
			countQuery = new StringBuffer("  select count(*) from ( with t as (SELECT c.service_desc, "
					+ " d.user_id, A.status, A.application_id FROM t_pc_office_level_final_status A, "
					+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
					+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
					+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
					+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
					+ " s.service_desc, v.service_code, b.office_code, user_name, t.user_id, status,v.state FROM t, tm_user b, v_application_details v,"
					+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
					+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
					+ " where "+ServiceList+" "
					+ " and "+UserList+" "
					+ " and  office_code in("+fcode+") ORDER BY user_name)");
						
		query = new StringBuffer("  with t as ("
				+ "SELECT c.service_desc, d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A,"
				+ " v_application_details b, tm_service c, t_pc_office_user_details d "
				+ " WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND "
				+ " A.status IN(9,10,12,5) AND b.service_code=c.service_code AND A.office_code in("+fcode+") "
				+ " AND A.application_id=d.application_id AND d.office_code=A.office_code "
				+ " order by c.service_desc, d.user_id), P1 as(SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
				+ " b.office_code, user_name, t.user_id, status,(select sname from TM_STATE where scode=v.state) as state,v.applicant_name,to_char(t.activity_on,'dd-mm-yyyy') as activity_on FROM t, tm_user b, v_application_details v, "
				+ " tm_service s WHERE "
				+ " t.user_id=b.user_id and t.application_id=v.application_id and "
				+ " v.service_code=s.service_code )"
				+ " pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
					+ " where "+ServiceList+" "
					+ " and "+UserList+" "
					+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc) , T3 AS (SELECT P1.*, ROWNUM RN FROM P1 ) "
									+ " SELECT * FROM T3 WHERE RN between ? and ? ");
		}
							
								 PreparedStatement statement = connection.prepareStatement(countQuery.toString());
								 ResultSet rs = statement.executeQuery();
								  if(rs.next())
								     { 
									  totalRecords = rs.getString(1);
								      } 
								  rs.close();
								 statement.close();
								 Integer pageRequested = Integer.parseInt(pageNum);
								 Integer pageSize = Integer.parseInt(recordsPerPage);
								 statement = connection.prepareStatement(query.toString());
								 System.out.println("QUERY"+query);
								 if(pageNum == null || recordsPerPage == null) {
						
								 }
								 else {
									 statement.setInt(1, (pageRequested-1) * pageSize + 1);
									 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
								
								 }
							
								 rs = statement.executeQuery();
								List<UserActivityReport> userActivityReportList = new ArrayList<UserActivityReport>();
								while(rs.next()) {
									if(reportDisplyType.equalsIgnoreCase("s"))
										userActivityReportList.add(new UserActivityReport(rs.getString("USER_NAME")+" ["+rs.getString("USER_ID")+" ]", rs.getString("SERVICE_DESC"),rs.getString("OFFICE_CODE"),rs.getString("USER_ID"),rs.getString("SERVICE_CODE"),rs.getString("GRANTED"), rs.getString("DENIED"),rs.getString("CLOSED"),rs.getString("DISPOSED"),rs.getString("STATE")));
									else
										userActivityReportList.add(new UserActivityReport(rs.getString("application_id"), rs.getString("service_desc"), rs.getString("service_code"),rs.getString("office_code"), rs.getString("user_name")+"["+rs.getString("user_id")+"]",rs.getString("user_id"),rs.getString("Granted"),rs.getString("Denied"),rs.getString("Closed"),rs.getString("Disposed"),rs.getString(11),rs.getString("state"),rs.getString("applicant_name"),rs.getString("activity_on")));
								}
							return userActivityReportList;
								
							}
	///END DISPOSED
	
	 @Override
		protected void generatePDF() throws Exception {
			Map  parameters = new HashMap();
			parameters.put("reportType", reportType);
			parameters.put("reportFormat", reportFormat);
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		   
		   
				String reportQuery="";
				String StateList="1=1";
				String CountryList="1=1";
				String ServiceList="1=1";
				String UserList="1=1";
				String	fcode=myLoginofficecode.toString().replace("[", "'").replace("]", "'").replace(", ", ",").trim();
				String srvList=selectServiceList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				
				String usrList=selectUserActivityList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				
				
				if(srvList.equals("'ALL'")){
					   parameters.put("selectedServiceList","ALL");
					    }
					    else{
					    	ServicesDao sdao=new ServicesDao(connection);
					    	 parameters.put("selectedServiceList",sdao.getServiceList(srvList).toString().replace("[", "").replace("]", ""));
					    }
				UserDao sdao= new UserDao(connection);
				parameters.put("selectedUserList",sdao.getUser(usrList));
				
				 if(activityFormate.equals("P"))
				 {
					 if(reportDisplyType.equalsIgnoreCase("s")){
					parameters.put("reportName","User Activity [Pendency Statistics]");
					 }
					 else{
						 parameters.put("reportName","User Activity [Pendency Details]") ;
					 }
				 }
				else if(activityFormate.equals("A"))
				{
					String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					if(reportDisplyType.equalsIgnoreCase("s"))
					{
											parameters.put("reportName","User Activity [Statistics]");
					}
					else
					{
							parameters.put("reportName","User Activity [Details]");
					}
					parameters.put("from",FROM);
					 parameters.put("to",TO);
					parameters.put("selectedDate","Date Range:");
					parameters.put("btw","TO");
					parameters.put("st","FROM");
				}
				else if(activityFormate.equals("D"))
				{
					String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					if(reportDisplyType.equalsIgnoreCase("s"))
					{
											parameters.put("reportName","Disposal [Statistics]");
					}
					else
					{
							parameters.put("reportName","Disposal [Details]");
					}
					parameters.put("from",FROM);
					 parameters.put("to",TO);
					parameters.put("selectedDate","Date Range:");
					parameters.put("btw","TO");
					parameters.put("st","FROM");
				}

				if(activityFormate.equals("P"))
				 {     
					if(!srvList.trim().equals("'ALL'")){
						ServiceList= "B.SERVICE_CODE in ("+srvList+")";
					}
					if(!usrList.trim().equals("'ALL'")){
						UserList= "A.USER_ID in ("+usrList+")";
						UserList= "user_id in ("+usrList+")";
					}
					if(reportDisplyType.equalsIgnoreCase("s")){
			    	 reportQuery ="WITH t1 AS (SELECT A.APPLICATION_ID, "
										+ " B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.STATE "
										+ " FROM T_PC_USER_LEVEL_STATUS A,V_APPLICATION_DETAILS B WHERE "
										+ " A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
										+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND "
										+ " "+UserList+" ) SELECT * FROM (SELECT application_id, user_name ||'[' || t1.user_id ||']' as user_name,"
										+ " service_desc,stage,(select sname from TM_STATE where  scode=t1.STATE) as state FROM t1,tm_user b, tm_service s "
										+ " WHERE t1.service_code=s.service_code and t1.user_id=b.user_id) pivot "
										+ " (count(application_id) FOR(stage) IN ('1' AS PendingForProcessing, '2' AS PendingForMail)) "
										+ "ORDER BY user_name,state,service_desc"; 
			 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
					parameters.put("User_Activity_Report", ds);
					}
					else{
						reportQuery="WITH t1 AS (SELECT A.APPLICATION_ID,  B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, "
										+ "ACTIVITY_ON, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.state  FROM T_PC_USER_LEVEL_STATUS A, "
										+ "V_APPLICATION_DETAILS B WHERE  A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
										+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND  "+UserList+" ) SELECT application_id, user_name ||'[' || t1.user_id ||']' as username , "
										+ " service_desc,ACTIVITY_ON received_on, trunc(sysdate)-trunc(ACTIVITY_ON) pending_for_days,"
										+ " stage, DECODE (stage, 1, 'Pending For Processing',  "
										+ " 2, 'Pending for Mailing') statusname,(select sname from TM_STATE where scode=state) as state,APPLICANT_NAME FROM t1,tm_user b, tm_service s "
										+ "	 WHERE t1.service_code=s.service_code and t1.user_id=b.user_id ORDER BY username,state,service_desc ";
						ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
						parameters.put("User_Pendency_DetailReport", ds);		
						
					}
				 }
				else if(activityFormate.equals("A"))
						{
							String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
							String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
							if(!srvList.trim().equals("'ALL'")){
									ServiceList= "service_code in ("+srvList+")";
								}
								if(!usrList.trim().equals("'ALL'")){
								UserList= "user_id in ("+usrList+")";
							}
								if(reportDisplyType.equalsIgnoreCase("s"))
								{
									
									 reportQuery =" WITH t AS("
													+ " SELECT DISTINCT application_id, to_USERID user_id, 1 status, status_date"
													+ " FROM t_pc_communication WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
													+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND to_USERID IS NOT NULL"
													+ " UNION ALL SELECT DISTINCT application_id, by_USERID user_id, "
													+ " 2 status, status_date FROM t_pc_communication "
													+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
													+ " trunc(to_date('"+TO+"','dd-mm-yyyy')) AND by_USERID IS NOT NULL)"
													+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code, "
													+ " b.office_code,  user_name ||'[' || t.user_id ||']' as user_name,  t.user_id, status,(select sname from tm_state where scode=v.state) as state FROM t, tm_user b, v_application_details v, "
													+ " tm_service s WHERE "
													+ " t.user_id=b.user_id and t.application_id=v.application_id and "
													+ " v.service_code=s.service_code)"
													+ " pivot (count(application_id) FOR(status) IN ('1' AS Received, '2' AS Processed))"
													+ " where "+ServiceList+" and "+UserList+" and "
													+ " office_code in("+fcode+")"
													+ " ORDER BY user_name,state,service_desc"; 
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_ActivityDetails_Report", ds);
								}
								else
								{
									 reportQuery ="WITH t AS( SELECT DISTINCT application_id, to_USERID user_id, 1 status, "
											+ " status_date FROM t_pc_communication "
											+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
											+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) "
											+ " AND to_USERID IS NOT NULL UNION ALL SELECT DISTINCT application_id, "
											+ " by_USERID user_id,  2 status, status_date FROM t_pc_communication "
											+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
											+ "  trunc(to_date('"+TO+"','dd-mm-yyyy')) "
											+ " AND by_USERID IS NOT NULL) "
											+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, "
											+ " v.service_code,  b.office_code,  user_name ||'[' || t.user_id ||']' as user_name, "
											+ " t.user_id, status,(select sname from Tm_state where scode=v.state) as state,v.APPLICANT_NAME FROM t, tm_user b, v_application_details v, "
											+ " tm_service s  WHERE  t.user_id=b.user_id and t.application_id=v.application_id "
											+ " and  v.service_code=s.service_code) "
											+ " pivot (count(status) FOR(status) IN ('1' AS Received, '2' AS Processed)) "
											+ " where "+ServiceList+" "
											+ " and "+UserList+" "
											+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_Details_Report", ds);
								}
						}
				
				else if(activityFormate.equals("D"))
				{
					String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					if(!srvList.trim().equals("'ALL'")){
							ServiceList= "service_code in ("+srvList+")";
						}
						if(!usrList.trim().equals("'ALL'")){
						UserList= "user_id in ("+usrList+")";
					}
						if(reportDisplyType.equalsIgnoreCase("s"))
						{
							 if(officeId.equals("1")) 
							 {
								 reportQuery =" with t as (SELECT c.service_desc, d.user_id, A.status,"
												+ " A.application_id,b.STATE FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
												+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
												+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
												+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
												+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
												+ " b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status,(select sname from TM_state where scode=t.state) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
												+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
												+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
												+ " where "
												+ ServiceList
												+ " and "
												+ UserList
												+ " and  office_code in(" + fcode + ") ORDER BY user_name,STATE,service_desc"; 
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_ActivityDisposalStatisticM_Report", ds);
							}
							 else if (officeId.equals("2")){
								 reportQuery =" with t as (SELECT c.service_desc, d.user_id, A.status,"
											+ " A.application_id,b.STATE FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
											+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
											+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
											+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
											+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
											+ " b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status,(select sname from TM_state where scode=t.state) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
											+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
											+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
											+ " where "
											+ ServiceList
											+ " and "
											+ UserList
											+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc"; 
							 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
							 parameters.put("User_ActivityDisposalStatisticI_Report", ds);
								 
							 }
						}
						else
						{
							if(officeId.equals("1")) 
								 {
								 reportQuery ="with t as (SELECT c.service_desc, "
									+ " d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A, "
									+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
									+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
									+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
									+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
									+ " s.service_desc, v.service_code, b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status, (SELECT sname FROM TM_STATE WHERE scode=v.state) AS state,v.applicant_name,TO_CHAR(t.activity_on,'dd-mm-yyyy') AS activity_on FROM t, tm_user b, v_application_details v,"
									+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
									+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
									+ " where "+ServiceList+" "
									+ " and "+UserList+" "
									+ " and  office_code in("+fcode+") ORDER BY user_name,STATE,service_desc";
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_ActivityDisposalDetailsM_Report", ds);
								 }
							else if(officeId.equals("2")) 
								 {
									reportQuery ="with t as (SELECT c.service_desc, "
										+ " d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A, "
										+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
										+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
										+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
										+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
										+ " s.service_desc, v.service_code, b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status, (SELECT sname FROM TM_STATE WHERE scode=v.state) AS state,v.applicant_name,TO_CHAR(t.activity_on,'dd-mm-yyyy') AS activity_on FROM t, tm_user b, v_application_details v,"
										+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
										+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
										+ " where "+ServiceList+" "
										+ " and "+UserList+" "
										+ " and  office_code in("+fcode+") ORDER BY user_name,STATE,service_desc";
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_ActivityDisposalDetailsI_Report", ds);
								 }
							}
				}
		     
			String tsPath = "/Reports/UserActivityReport.jrxml";
			String fileName = "UserActivityReport.pdf";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		
}
	 
	 @Override
		protected void generateChart() throws Exception {
			// TODO Auto-generated method stub
			
		}
	 
		@Override
		protected void generateCSV() throws Exception {
			Map  parameters = new HashMap();
			parameters.put("reportType", reportType);
			parameters.put("reportFormat", reportFormat);
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		   
				String reportQuery="";
				String StateList="1=1";
				String CountryList="1=1";
				String ServiceList="1=1";
				String UserList="1=1";
				String	fcode=myLoginofficecode.toString().replace("[", "'").replace("]", "'").replace(", ", ",").trim();
				String srvList=selectServiceList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				
				String usrList=selectUserActivityList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				
				if(srvList.equals("'ALL'")){
					   parameters.put("selectedServiceList","ALL");
					    }
					    else{
					    	ServicesDao sdao=new ServicesDao(connection);
					    	 parameters.put("selectedServiceList",sdao.getServiceList(srvList).toString().replace("[", "").replace("]", ""));
					    }
				UserDao sdao= new UserDao(connection);
				parameters.put("selectedUserList",sdao.getUser(usrList));
				
				if(activityFormate.equals("P"))
				 {    
					   
					if(!srvList.trim().equals("'ALL'")){
						ServiceList= "B.SERVICE_CODE in ("+srvList+")";
					}
					if(!usrList.trim().equals("'ALL'")){
						UserList= "A.USER_ID in ("+usrList+")";
						UserList= "user_id in ("+usrList+")";
					}
					if(reportDisplyType.equalsIgnoreCase("s"))
					{
				
			    	 reportQuery ="WITH t1 AS (SELECT A.APPLICATION_ID, "
										+ " B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.STATE "
										+ " FROM T_PC_USER_LEVEL_STATUS A,V_APPLICATION_DETAILS B WHERE "
										+ " A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
										+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND "
										+ " "+UserList+" ) SELECT * FROM (SELECT application_id, user_name ||'[' || t1.user_id ||']' as user_name, "
										+ " service_desc,stage,(select sname from TM_STATE where  scode=t1.STATE) as state FROM t1,tm_user b, tm_service s "
										+ " WHERE t1.service_code=s.service_code and t1.user_id=b.user_id) pivot "
										+ " (count(application_id) FOR(stage) IN ('1' AS PendingForProcessing, '2' AS PendingForMail)) "
										+ "ORDER BY user_name,state,service_desc"; 
			 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
					parameters.put("User_Activity_Report", ds);
					}
					else{

						reportQuery="WITH t1 AS (SELECT A.APPLICATION_ID,  B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, "
										+ "ACTIVITY_ON, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.state  FROM T_PC_USER_LEVEL_STATUS A, "
										+ "V_APPLICATION_DETAILS B WHERE  A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
										+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND  "+UserList+" ) SELECT application_id, user_name ||'[' || t1.user_id ||']' as username , "
										+ " service_desc,ACTIVITY_ON received_on, trunc(sysdate)-trunc(ACTIVITY_ON) pending_for_days,"
										+ " stage, DECODE (stage, 1, 'Pending For Processing',  "
										+ " 2, 'Pending for Mailing') statusname,(select sname from TM_STATE where scode=state) as state,APPLICANT_NAME FROM t1,tm_user b, tm_service s "
										+ "	 WHERE t1.service_code=s.service_code and t1.user_id=b.user_id ORDER BY username,state,service_desc ";
						ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
						parameters.put("User_Pendency_DetailReport", ds);		
						
					
					}
				 }
				else if(activityFormate.equals("A"))
						{
							String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
							String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
							if(!srvList.trim().equals("'ALL'")){
									ServiceList= "service_code in ("+srvList+")";
								}
								if(!usrList.trim().equals("'ALL'")){
								UserList= "user_id in ("+usrList+")";
							}
								if(reportDisplyType.equalsIgnoreCase("s"))
								{
							
							 reportQuery =" WITH t AS("
											+ " SELECT DISTINCT application_id, to_USERID user_id, 1 status, status_date"
											+ " FROM t_pc_communication WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
											+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND to_USERID IS NOT NULL"
											+ " UNION ALL SELECT DISTINCT application_id, by_USERID user_id, "
											+ " 2 status, status_date FROM t_pc_communication "
											+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
											+ " trunc(to_date('"+TO+"','dd-mm-yyyy')) AND by_USERID IS NOT NULL)"
											+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code, "
											+ " b.office_code,  user_name ||'[' || t.user_id ||']' as user_name,  t.user_id, status,(select sname from tm_state where scode=v.state) as state FROM t, tm_user b, v_application_details v, "
											+ " tm_service s WHERE "
											+ " t.user_id=b.user_id and t.application_id=v.application_id and "
											+ " v.service_code=s.service_code)"
											+ " pivot (count(application_id) FOR(status) IN ('1' AS Received, '2' AS Processed))"
											+ " where "+ServiceList+" and "+UserList+" and "
											+ " office_code in("+fcode+")"
											+ " ORDER BY user_name,state,service_desc"; 
							 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
							 parameters.put("User_ActivityDetails_Report", ds);
								}
								else
								{
									 reportQuery ="WITH t AS( SELECT DISTINCT application_id, to_USERID user_id, 1 status, "
											+ " status_date FROM t_pc_communication "
											+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
											+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) "
											+ " AND to_USERID IS NOT NULL UNION ALL SELECT DISTINCT application_id, "
											+ " by_USERID user_id,  2 status, status_date FROM t_pc_communication "
											+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
											+ "  trunc(to_date('"+TO+"','dd-mm-yyyy')) "
											+ " AND by_USERID IS NOT NULL) "
											+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, "
											+ " v.service_code,  b.office_code,  user_name ||'[' || t.user_id ||']' as user_name, "
											+ " t.user_id, status,(select sname from Tm_state where scode=v.state) as state,v.APPLICANT_NAME FROM t, tm_user b, v_application_details v, "
											+ " tm_service s  WHERE  t.user_id=b.user_id and t.application_id=v.application_id "
											+ " and  v.service_code=s.service_code) "
											+ " pivot (count(status) FOR(status) IN ('1' AS Received, '2' AS Processed)) "
											+ " where "+ServiceList+" "
											+ " and "+UserList+" "
											+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_Details_Report", ds);
								}
						}
				
				///DISPOSAL
					else if(activityFormate.equals("D"))
					{
						String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
						String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
						if(!srvList.trim().equals("'ALL'")){
								ServiceList= "service_code in ("+srvList+")";
							}
							if(!usrList.trim().equals("'ALL'")){
							UserList= "user_id in ("+usrList+")";
						}
							if(reportDisplyType.equalsIgnoreCase("s"))
							{
								 if(officeId.equals("1")) 
								 {
									 reportQuery =" with t as (SELECT c.service_desc, d.user_id, A.status,"
													+ " A.application_id,b.state FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
													+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
													+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
													+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
													+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
													+ " b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status,(select sname from TM_state where scode=t.state) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
													+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
													+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
													+ " where "
													+ ServiceList
													+ " and "
													+ UserList
													+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc"; 
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_ActivityDisposalStatisticM_Report", ds);
								}
								 else if (officeId.equals("2")){
									 reportQuery =" with t as (SELECT c.service_desc, d.user_id, A.status,"
												+ " A.application_id,b.state FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
												+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
												+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
												+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
												+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
												+ " b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status,(select sname from TM_state where scode=t.state) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
												+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
												+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
												+ " where "
												+ ServiceList
												+ " and "
												+ UserList
												+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc"; 
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_ActivityDisposalStatisticI_Report", ds);
									 
								 }
							}
							else
							{
								if(officeId.equals("1")) 
									 {
									 reportQuery ="with t as (SELECT c.service_desc, "
										+ " d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A, "
										+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
										+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
										+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
										+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
										+ " s.service_desc, v.service_code, b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status, (SELECT sname FROM TM_STATE WHERE scode=v.state) AS state,v.applicant_name,TO_CHAR(t.activity_on,'dd-mm-yyyy') AS activity_on FROM t, tm_user b, v_application_details v,"
										+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
										+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
										+ " where "+ServiceList+" "
										+ " and "+UserList+" "
										+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_ActivityDisposalDetailsM_Report", ds);
									 }
								else if(officeId.equals("2")) 
									 {
										reportQuery ="with t as (SELECT c.service_desc, "
											+ " d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A, "
											+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
											+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
											+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
											+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
											+ " s.service_desc, v.service_code, b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status, (SELECT sname FROM TM_STATE WHERE scode=v.state) AS state,v.applicant_name,TO_CHAR(t.activity_on,'dd-mm-yyyy') AS activity_on FROM t, tm_user b, v_application_details v,"
											+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
											+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
											+ " where "+ServiceList+" "
											+ " and "+UserList+" "
											+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
										 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
										 parameters.put("User_ActivityDisposalDetailsI_Report", ds);
									 }
								}
					}
			     
				
				
		     
				String tsPath = "/Reports/UserActivityReportCSV.jrxml";
				String fileName = "UserActivityReport";
				GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
		}
	 
	 @Override
	 protected void generateExcel() throws Exception {
			Map  parameters = new HashMap();
			parameters.put("reportType", reportType);
			parameters.put("reportFormat", reportFormat);
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		   
				String reportQuery="";
				String StateList="1=1";
				String CountryList="1=1";
				String ServiceList="1=1";
				String UserList="1=1";
				String	fcode=myLoginofficecode.toString().replace("[", "'").replace("]", "'").replace(", ", ",").trim();
				String srvList=selectServiceList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				String usrList=selectUserActivityList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				
				if(srvList.equals("'ALL'")){
					   parameters.put("selectedServiceList","ALL");
					    }
					    else{
					    	ServicesDao sdao=new ServicesDao(connection);
					    	 parameters.put("selectedServiceList",sdao.getServiceList(srvList).toString().replace("[", "").replace("]", ""));
					    }
				UserDao sdao= new UserDao(connection);
				parameters.put("selectedUserList",sdao.getUser(usrList));

				 if(activityFormate.equals("P")){
					 if(reportDisplyType.equalsIgnoreCase("s")){
						 parameters.put("reportName","User Activity [Pendency Statistics]");
						 }
					 else{
						 parameters.put("reportName","User Activity [Pendency Details]");
						 }
					 }
				else if(activityFormate.equals("A"))
				{
					String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					if(reportDisplyType.equalsIgnoreCase("s"))
					{
											parameters.put("reportName","User Activity  [Statistics]");
					}
					else
					{
							parameters.put("reportName","User Activity [Details]");
					}
					parameters.put("from",FROM);
					parameters.put("to",TO);
					parameters.put("selectedDate","Date Range:");
					parameters.put("btw","TO");
					parameters.put("st","FROM");
				}
				else if(activityFormate.equals("D"))
				{
					String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					if(reportDisplyType.equalsIgnoreCase("s"))
					{
											parameters.put("reportName","User Activity Disposal [Statistics]");
					}
					else
					{
							parameters.put("reportName","User Activity Disposal [Details]");
					}
					parameters.put("from",FROM);
					parameters.put("to",TO);
					parameters.put("selectedDate","Date Range:");
					parameters.put("btw","TO");
					parameters.put("st","FROM");
				}
				if(activityFormate.equals("P"))
					 {    
						if(!srvList.trim().equals("'ALL'")){
							ServiceList= "B.SERVICE_CODE in ("+srvList+")";
						}
						if(!usrList.trim().equals("'ALL'")){
							UserList= "A.USER_ID in ("+usrList+")";
							UserList= "user_id in ("+usrList+")";
						}
						if(reportDisplyType.equalsIgnoreCase("s")){
				    	 reportQuery ="WITH t1 AS (SELECT A.APPLICATION_ID, "
											+ " B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.STATE "
											+ " FROM T_PC_USER_LEVEL_STATUS A,V_APPLICATION_DETAILS B WHERE "
											+ " A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
											+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND "
											+ " "+UserList+" ) SELECT * FROM (SELECT application_id, user_name ||'[' || t1.user_id ||']' as user_name,"
											+ " service_desc,stage,(select sname from TM_STATE where  scode=t1.STATE) as state FROM t1,tm_user b, tm_service s "
											+ " WHERE t1.service_code=s.service_code and t1.user_id=b.user_id) pivot "
											+ " (count(application_id) FOR(stage) IN ('1' AS PendingForProcessing, '2' AS PendingForMail)) "
											+ "ORDER BY user_name,state,service_desc"; 
				 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
						parameters.put("User_Activity_Report", ds);
						}
						else{
							reportQuery="WITH t1 AS (SELECT A.APPLICATION_ID,  B.SERVICE_CODE, B.APPLICANT_NAME,A.USER_ID, "
									+ "ACTIVITY_ON, CASE WHEN STAGE_ID=7 THEN 2 ELSE 1 END stage,B.state  FROM T_PC_USER_LEVEL_STATUS A, "
									+ "V_APPLICATION_DETAILS B WHERE  A.APPLICATION_ID=B.APPLICATION_ID  AND STAGE_ID IN (1,2,7) "
									+ " AND A.office_code in("+fcode+") and "+ServiceList+" AND  "+UserList+" ) SELECT application_id, user_name ||'[' || t1.user_id ||']' as username , "
									+ " service_desc,ACTIVITY_ON received_on, trunc(sysdate)-trunc(ACTIVITY_ON) pending_for_days,"
									+ " stage, DECODE (stage, 1, 'Pending For Processing',  "
									+ " 2, 'Pending for Mailing') statusname,(select sname from TM_STATE where scode=state) as state,APPLICANT_NAME FROM t1,tm_user b, tm_service s "
									+ "	 WHERE t1.service_code=s.service_code and t1.user_id=b.user_id ORDER BY username,state,service_desc ";
					ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
					parameters.put("User_Pendency_DetailReport", ds);	
							
						}
					 }
				else if(activityFormate.equals("A"))
					{
						String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
						String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
								if(!srvList.trim().equals("'ALL'")){
										ServiceList= "service_code in ("+srvList+")";
									}
									if(!usrList.trim().equals("'ALL'")){
									UserList= "user_id in ("+usrList+")";
								}
							if(reportDisplyType.equalsIgnoreCase("s"))
									{
								 reportQuery =" WITH t AS("
												+ " SELECT DISTINCT application_id, to_USERID user_id, 1 status, status_date"
												+ " FROM t_pc_communication WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
												+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND to_USERID IS NOT NULL"
												+ " UNION ALL SELECT DISTINCT application_id, by_USERID user_id, "
												+ " 2 status, status_date FROM t_pc_communication "
												+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
												+ " trunc(to_date('"+TO+"','dd-mm-yyyy')) AND by_USERID IS NOT NULL)"
												+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code, "
												+ " b.office_code,  user_name ||'[' || t.user_id ||']' as user_name,  t.user_id, status,(select sname from tm_state where scode=v.state) as state FROM t, tm_user b, v_application_details v, "
												+ " tm_service s WHERE "
												+ " t.user_id=b.user_id and t.application_id=v.application_id and "
												+ " v.service_code=s.service_code)"
												+ " pivot (count(application_id) FOR(status) IN ('1' AS Received, '2' AS Processed))"
												+ " where "+ServiceList+" and "+UserList+" and "
												+ " office_code in("+fcode+")"
												+ " ORDER BY user_name,state,service_desc"; 
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_ActivityDetails_Report", ds);
							}
						else
							{
								 reportQuery ="WITH t AS( SELECT DISTINCT application_id, to_USERID user_id, 1 status, "
										+ " status_date FROM t_pc_communication "
										+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) "
										+ " AND trunc(to_date('"+TO+"','dd-mm-yyyy')) "
										+ " AND to_USERID IS NOT NULL UNION ALL SELECT DISTINCT application_id, "
										+ " by_USERID user_id,  2 status, status_date FROM t_pc_communication "
										+ " WHERE trunc(status_date) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND "
										+ "  trunc(to_date('"+TO+"','dd-mm-yyyy')) "
										+ " AND by_USERID IS NOT NULL) "
										+ " SELECT * FROM (SELECT distinct t.application_id, s.service_desc, "
										+ " v.service_code,  b.office_code,  user_name ||'[' || t.user_id ||']' as user_name, "
										+ " t.user_id, status,(select sname from Tm_state where scode=v.state) as state,v.APPLICANT_NAME FROM t, tm_user b, v_application_details v, "
										+ " tm_service s  WHERE  t.user_id=b.user_id and t.application_id=v.application_id "
										+ " and  v.service_code=s.service_code) "
										+ " pivot (count(status) FOR(status) IN ('1' AS Received, '2' AS Processed)) "
										+ " where "+ServiceList+" "
										+ " and "+UserList+" "
										+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_Details_Report", ds);
							}
					}
				else if(activityFormate.equals("D"))
				{
					String FROM=fromDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					String TO=toDate.toString().replace("[", "").replace("]", "").replace(", ", " ").trim();
					if(!srvList.trim().equals("'ALL'")){
							ServiceList= "service_code in ("+srvList+")";
						}
						if(!usrList.trim().equals("'ALL'")){
						UserList= "user_id in ("+usrList+")";
					}
						if(reportDisplyType.equalsIgnoreCase("s"))
						{
							 if(officeId.equals("1")) 
							 {
								 reportQuery =" with t as (SELECT c.service_desc, d.user_id, A.status,"
												+ " A.application_id,b.state FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
												+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
												+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
												+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
												+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
												+ " b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status,(select sname from TM_state where scode=t.state) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
												+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
												+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
												+ " where "
												+ ServiceList
												+ " and "
												+ UserList
												+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc"; 
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_ActivityDisposalStatisticM_Report", ds);
							}
							 else if (officeId.equals("2")){
								 reportQuery =" with t as (SELECT c.service_desc, d.user_id, A.status,"
											+ " A.application_id,b.state FROM t_pc_office_level_final_status A,v_application_details b, tm_service c, "
											+ " t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) BETWEEN "
											+ " trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND b.service_code=c.service_code AND "
											+ " A.office_code in("+fcode+") AND A.application_id=d.application_id AND d.office_code=A.office_code order by "
											+ " c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, s.service_desc, v.service_code,"
											+ " b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status,(select sname from TM_state where scode=t.state) as state FROM t, tm_user b, v_application_details v, tm_service s WHERE "
											+ " t.user_id=b.user_id and t.application_id=v.application_id and  v.service_code=s.service_code)pivot"
											+ " (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed))"
											+ " where "
											+ ServiceList
											+ " and "
											+ UserList
											+ " and  office_code in(" + fcode + ") ORDER BY user_name,state,service_desc"; 
							 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
							 parameters.put("User_ActivityDisposalStatisticI_Report", ds);
								 
							 }
						}
						else
						{
							if(officeId.equals("1")) 
								 {
								 reportQuery ="with t as (SELECT c.service_desc, "
									+ " d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A, "
									+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
									+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
									+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
									+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
									+ " s.service_desc, v.service_code, b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status, (SELECT sname FROM TM_STATE WHERE scode=v.state) AS state,v.applicant_name,TO_CHAR(t.activity_on,'dd-mm-yyyy') AS activity_on FROM t, tm_user b, v_application_details v,"
									+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
									+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
									+ " where "+ServiceList+" "
									+ " and "+UserList+" "
									+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
								 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								 parameters.put("User_ActivityDisposalDetailsM_Report", ds);
								 }
							else if(officeId.equals("2")) 
								 {
									reportQuery ="with t as (SELECT c.service_desc, "
										+ " d.user_id, A.status, A.application_id,A.activity_on FROM t_pc_office_level_final_status A, "
										+ " v_application_details b, tm_service c, t_pc_office_user_details d WHERE A.application_id=b.application_id "
										+ " AND trunc(A.activity_on) BETWEEN trunc(to_date('"+FROM+"','dd-mm-yyyy')) AND trunc(to_date('"+TO+"','dd-mm-yyyy')) AND A.status IN(9,10,12,5) AND "
										+ " b.service_code=c.service_code AND A.office_code in("+fcode+") AND A.application_id=d.application_id"
										+ "  AND d.office_code=A.office_code order by c.service_desc, d.user_id)SELECT * FROM (SELECT distinct t.application_id, "
										+ " s.service_desc, v.service_code, b.office_code, user_name ||'[' || t.user_id ||']' as user_name, t.user_id, status, (SELECT sname FROM TM_STATE WHERE scode=v.state) AS state,v.applicant_name,TO_CHAR(t.activity_on,'dd-mm-yyyy') AS activity_on FROM t, tm_user b, v_application_details v,"
										+ " tm_service s WHERE  t.user_id=b.user_id and t.application_id=v.application_id and "
										+ " v.service_code=s.service_code ) pivot (count(status) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed, '5' AS Disposed)) "
										+ " where "+ServiceList+" "
										+ " and "+UserList+" "
										+ " and  office_code in("+fcode+") ORDER BY user_name,state,service_desc";
									 ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
									 parameters.put("User_ActivityDisposalDetailsI_Report", ds);
								 }
							}
				}
		     
				String tsPath = "/Reports/UserActivityReportExcel.jrxml";
				String fileName = "UserActivityReport";
				GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
		}
	
	
	public String getMyLoginofficecode() {
		return myLoginofficecode;
	}
	public void setMyLoginofficecode(String myLoginofficecode) {
		this.myLoginofficecode = myLoginofficecode;
	}
	public List<UserActivityReport> getUserActivityReport() {
		return userActivityReport;
	}
	public void setUserActivityReport(List<UserActivityReport> userActivityReport) {
		this.userActivityReport = userActivityReport;
	}
	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}
	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
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
	public String getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
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
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public String getLoginOfficeName() {
		return loginOfficeName;
	}
	public void setLoginOfficeName(String loginOfficeName) {
		this.loginOfficeName = loginOfficeName;
	}
	public List<String> getSelectServiceList() {
		return selectServiceList;
	}
	public void setSelectServiceList(List<String> selectServiceList) {
		this.selectServiceList = selectServiceList;
	}
	public List<String> getSelectUserActivityList() {
		return selectUserActivityList;
	}
	public void setSelectUserActivityList(List<String> selectUserActivityList) {
		this.selectUserActivityList = selectUserActivityList;
	}
	public String getReportDisplyType() {
		return reportDisplyType;
	}
	public void setReportDisplyType(String reportDisplyType) {
		this.reportDisplyType = reportDisplyType;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

}
