package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Size;

import models.reports.ApplicationPendancyTimeRangewiseReport;
import models.reports.AssociationsNotFiledAnnualReturnsReport;

import org.apache.poi.ss.formula.udf.UDFFinder;
import org.springframework.util.MultiValueMap;

import com.sun.org.apache.bcel.internal.generic.NEW;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import utilities.ValidationException;
import dao.reports.MISReportGenerator;

public class ApplicationPendancyTimeRangewiseReportGenerator extends MISReportGenerator{
	   private MultiValueMap<String, String> parameterMap;
	   private String pageNum;
	   private String recordsPerPage;
	   private String totalRecords;
	   private String loginUserName;
	   private String loginOfficeName;	
	   private String myLoginofficecode;
	   private int virtualizationMaxSize = 200;
	   private String reportDisplayPendancyType;
	   private List<String> selectServiceList;
	   private String range1;
	   private String range2;
	   private String range3;
	   private String range4;
	   private String allPendancy;
	   private String allOther;
	   private String heading0;
	   private String heading1;
	   private String heading2;
	   private String heading3;
	   private String heading4; 
	   private String heading5;
	   
	   private List<ApplicationPendancyTimeRangewiseReport> applicationPendancyTimeRangewiseList;
	   private ArrayList<Integer> range = new ArrayList<Integer>();
	   private ArrayList<Integer> rangeArray = new ArrayList<Integer>();
	   
	   
	   public ApplicationPendancyTimeRangewiseReportGenerator(Connection connection){
		super(connection);   
	   }
	   
	   @Override
	   protected void generateHTML() throws Exception{
		   if(reportDisplayPendancyType.equals("d")){
			   applicationPendancyTimeRangewiseList = getApplicationPendancytimeRangewiseDetailedList();
			   totalRecords = getTotalRecords();			   
		   }
		   else if(reportDisplayPendancyType.equals("s")){
			   applicationPendancyTimeRangewiseList = getApplicationPendancytimeRangewiseStatisticsList();
			   totalRecords = getTotalRecords();
		   }
	   }
	   private List<ApplicationPendancyTimeRangewiseReport> getApplicationPendancytimeRangewiseStatisticsList() throws Exception{
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
		   
		   String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		   String  subQueryServices="1=1";
		   if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
		    	 subQueryServices=" service in ("+selectedServices+")";
		   List<ApplicationPendancyTimeRangewiseReport> applicationPendancyTimeRangewiseList = new ArrayList<ApplicationPendancyTimeRangewiseReport>();
		   int length = rangeArray.size();
		   if(length!=0){
			   StringBuffer countQuery = new StringBuffer(" with t1 as ( ");
			   countQuery.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
			   countQuery.append(" t2 as ( ");
			   countQuery.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
			   countQuery.append(" t3 as ( ");
			   countQuery.append(" select t2.*, ");
			   //if(length==1){
			   		countQuery.append(" case when days between 0 and ? then 1 "); //1 range
			   		countQuery.append(" when days between ? and 5000 then 2 "); // 1 range +1 and all
			   //}

			   countQuery.append(" else 6 end as grp from t2) ");
			   countQuery.append(" SELECT count(*) FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4)) order by service_desc ");
			   PreparedStatement statement = connection.prepareStatement(countQuery.toString());
			   int rangeOne = rangeArray.get(0);
			   int rangeOne1 = rangeOne+1;
			   statement.setInt(1, rangeOne);
			   statement.setInt(2, rangeOne1);
			   
			   ResultSet rs = statement.executeQuery();
				  if(rs.next())
				     { 
					  totalRecords = rs.getString(1);
				      } 
				rs.close();
				statement.close();
			   
			   StringBuffer query = new StringBuffer(" with t1 as ( ");
			   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
			   		query.append(" t2 as ( ");
			   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
			   		query.append(" t3 as ( ");
			   		query.append(" select t2.*, ");
					   if(length==1){
						   query.append(" case when days between 0 and ? then 1 "); //1 range -- 1
						   //query.append(" when days between ? and 5000 then 2 "); // 1 range +1 and all --2
						   query.append(" else 2 end as grp from t2), ");
					   }
					   if(length==2){
						   query.append(" case when days between 0 and ? then 1 "); //1 range --1
						   query.append(" when days between ? and ? then 2 "); //1 range+1 and 2 range --2,3
						   //query.append(" when days between ? and 5000 then 3 "); //2 range+1 and all --4
						   query.append(" else 3 end as grp from t2), ");
					   }
					   if(length==3){
						   query.append(" case when days between 0 and ? then 1 "); // 1 range --1
						   query.append(" when days between ? and ? then 2 "); // 1 range +1 and 2 range --2,3
						   query.append(" when days between ? and ? then 3 "); // 2 range+1 and 3 range --4,5
						   //query.append(" when days between ? and 5000 then 4 "); //3 range+1 and all --6
						   query.append(" else 4 end as grp from t2), ");
					   }
					   if(length==4){
						   query.append(" case when days between 0 and ? then 1 "); // 1 range --1
						   query.append(" when days between ? and ? then 2 "); // 1 range +1 and 2 range --2,3
						   query.append(" when days between ? and ? then 3 "); // 2 range+1 and 3 range --4,5
						   query.append(" when days between ? and ? then 4 "); //3 range+1 and 4 range ---6,7
						   //query.append(" when days between ? and 5000 then 5 "); //4 range+1 and all --8
						   query.append(" else 5 end as grp from t2), ");
					   }			   		
					   
					   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
					   query.append(" t5 as (select t4.*, ROWNUM RN from t4) select * from t5 where  RN between ? and ? "); // --9,10
					   Integer pageRequested = Integer.parseInt(pageNum);
					   Integer pageSize = Integer.parseInt(recordsPerPage);					   
					   statement = connection.prepareStatement(query.toString());
					   
					 if(length==1){
						   statement.setInt(1, rangeOne);
						   //statement.setInt(2, rangeOne1);
							 if(pageNum == null || recordsPerPage == null) {
							 }
							 else {
		/*						 statement.setInt(1, (pageRequested-1) * pageSize + 1);	
								 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);*/
								 statement.setInt(2, (pageRequested-1) * pageSize + 1);	
								 statement.setInt(3, (pageRequested-1) * pageSize + pageSize);				 
							 }						   
					 }  
					 if(length==2){
						 int rangeTwo = rangeArray.get(1); 
						 int rangeTwo1 = rangeTwo+1;
						 statement.setInt(1, rangeOne);
						 statement.setInt(2, rangeOne1);
						 statement.setInt(3, rangeTwo);
						 //statement.setInt(4, rangeTwo1);
						 if(pageNum == null || recordsPerPage == null) {
						 }
						 else {
	/*						 statement.setInt(1, (pageRequested-1) * pageSize + 1);	
							 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);*/
							 statement.setInt(4, (pageRequested-1) * pageSize + 1);	
							 statement.setInt(5, (pageRequested-1) * pageSize + pageSize);				 
						 }						 
					 }  	
					 if(length==3){
						 int rangeTwo = rangeArray.get(1); 
						 int rangeTwo1 = rangeTwo+1;
						 int rangeThree = rangeArray.get(2);
						 int rangeThree1 = rangeThree+1;
						 statement.setInt(1, rangeOne);
						 statement.setInt(2, rangeOne1);
						 statement.setInt(3, rangeTwo);
						 statement.setInt(4, rangeTwo1);
						 statement.setInt(5, rangeThree);
						 //statement.setInt(6, rangeThree1);
						 if(pageNum == null || recordsPerPage == null) {
						 }
						 else {
	/*						 statement.setInt(1, (pageRequested-1) * pageSize + 1);	
							 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);*/
							 statement.setInt(6, (pageRequested-1) * pageSize + 1);	
							 statement.setInt(7, (pageRequested-1) * pageSize + pageSize);				 
						 }						 
					 }	
					 if(length==4){
						 int rangeTwo = rangeArray.get(1); 
						 int rangeTwo1 = rangeTwo+1;
						 int rangeThree = rangeArray.get(2);
						 int rangeThree1 = rangeThree+1;
						 int rangeFour = rangeArray.get(3);
						 int rangeFour1 = rangeFour+1;
						 statement.setInt(1, rangeOne);
						 statement.setInt(2, rangeOne1);
						 statement.setInt(3, rangeTwo);
						 statement.setInt(4, rangeTwo1);
						 statement.setInt(5, rangeThree);
						 statement.setInt(6, rangeThree1);
						 statement.setInt(7, rangeFour);
						 //statement.setInt(8, rangeFour1);
						 if(pageNum == null || recordsPerPage == null) {
						 }
						 else {
	/*						 statement.setInt(1, (pageRequested-1) * pageSize + 1);	
							 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);*/
							 statement.setInt(8, (pageRequested-1) * pageSize + 1);	
							 statement.setInt(9, (pageRequested-1) * pageSize + pageSize);				 
						 }						 
					 }					 
					   
					 rs = statement.executeQuery();

					 while(rs.next()) {
							applicationPendancyTimeRangewiseList.add(new ApplicationPendancyTimeRangewiseReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
					}
		   }
		   // when array length is zero means no input from user
		   if(length==0) {
			   StringBuffer countQuery = new StringBuffer(" with t1 as ( ");
			   countQuery.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
			   countQuery.append(" t2 as ( ");
			   countQuery.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
			   countQuery.append(" t3 as ( ");
			   countQuery.append(" select t2.*, ");
			   		countQuery.append(" case when days between 0 and 5000 then 1 "); //1 range
			   countQuery.append(" else 6 end as grp from t2) ");
			   countQuery.append(" SELECT count(*) FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4)) order by service_desc ");
			   PreparedStatement statement = connection.prepareStatement(countQuery.toString());
			   ResultSet rs = statement.executeQuery();
				  if(rs.next())
				     { 
					  totalRecords = rs.getString(1);
				      } 
				rs.close();
				statement.close();
			   
			   StringBuffer query = new StringBuffer(" with t1 as ( ");
			   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
			   		query.append(" t2 as ( ");
			   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
			   		query.append(" t3 as ( ");
			   		query.append(" select t2.*, ");
						   query.append(" case when days between 0 and 5000 then 1 "); //1 range -- 1
						   query.append(" else 2 end as grp from t2), ");
					   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
					   query.append(" t5 as (select t4.*, ROWNUM RN from t4) select * from t5 where  RN between ? and ? "); // --9,10
					   Integer pageRequested = Integer.parseInt(pageNum);
					   Integer pageSize = Integer.parseInt(recordsPerPage);					   
					   statement = connection.prepareStatement(query.toString());

							 if(pageNum == null || recordsPerPage == null) {
							 }
							 else {
								 statement.setInt(1, (pageRequested-1) * pageSize + 1);	
								 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
/*								 statement.setInt(2, (pageRequested-1) * pageSize + 1);	
								 statement.setInt(3, (pageRequested-1) * pageSize + pageSize);				 */
							 }						   
					 rs = statement.executeQuery();
					 while(rs.next()) {
							applicationPendancyTimeRangewiseList.add(new ApplicationPendancyTimeRangewiseReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
					}
		   }
		   return applicationPendancyTimeRangewiseList;		   
	   }
	   // Detailed Pendancy //
	   private List<ApplicationPendancyTimeRangewiseReport> getApplicationPendancytimeRangewiseDetailedList() throws Exception{
			if (connection == null) {
				throw new Exception("Invalid connection");
			}
		   
		   String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		   String  subQueryServices="1=1";
		   if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
		    	 subQueryServices=" service in ("+selectedServices+")";
		   
		   List<ApplicationPendancyTimeRangewiseReport> applicationPendancyTimeRangewiseList = new ArrayList<ApplicationPendancyTimeRangewiseReport>();
		   		   
		   String countQuery = " with t1 as ( "
				   + " SELECT application_id, service_code service , applicant_name , state, district, case when submission_date is null then created_on else submission_date end AS s_date,SECTION_FILENO,submission_date "
				   + " FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), "
				   + " t2 as ( select t1.*, to_char(s_date, 'yyyy') YEAR , trunc(sysdate)- trunc(s_date) days from t1) "
				   + " select count(*) from t2 where "+subQueryServices+" ";
		   		   
		   String query = " with t1 as ( "
				   + " SELECT application_id, service_code service , applicant_name , state, district, case when submission_date is null then created_on else submission_date end AS s_date, SECTION_FILENO,submission_date "
				   + " FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), "
				   + " t2 as ( "
				   + " select t1.*, ROWNUM RN, to_char(s_date, 'yyyy') YEAR , trunc(sysdate)- trunc(s_date) days from t1) "
				   + " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, application_id, applicant_name, (select SNAME from tm_state where scode=state) statename,(select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname,SECTION_FILENO, "
				   + " submission_date,days from t2 WHERE "+subQueryServices+" and RN BETWEEN ? and  ? order by days desc ";


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
/*				 statement.setInt(3, (pageRequested-1) * pageSize + 1);	
				 statement.setInt(4, (pageRequested-1) * pageSize + pageSize);				 */
			 }
		
			rs = statement.executeQuery();
			while(rs.next()) {
				//String rgno = rs.getString(1); 
				applicationPendancyTimeRangewiseList.add(new ApplicationPendancyTimeRangewiseReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
			}		   
	   
		   return applicationPendancyTimeRangewiseList;
	   }
	   @Override
	   protected void generatePDF() throws Exception{
			Map  parameters = new HashMap();
		   String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		   String  subQueryServices="1=1";
		   if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
		    	 subQueryServices=" service in ("+selectedServices+")";			
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		    int length = rangeArray.size();
		    parameters.put("length", length);
		    parameters.put("rangeArray",rangeArray);
		    parameters.put("allOther", allOther);
		    parameters.put("allPendancy", allPendancy);
		    parameters.put("heading0", heading0);
		    parameters.put("heading1", heading1);
		    parameters.put("heading2", heading2);
		    parameters.put("heading3", heading3);
		    parameters.put("heading4", heading4);
		    
		    
		if(reportDisplayPendancyType.equalsIgnoreCase("d")){
		    String reportQuery="";
	
		    reportQuery =" with t1 as ( "
					+  " SELECT application_id, service_code service , applicant_name , state, district, case when submission_date is null then created_on else submission_date end AS s_date, SECTION_FILENO,submission_date "
					+  " FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), "
					+  " t2 as ( "
					+  " select t1.*, to_char(s_date, 'yyyy') YEAR , trunc(sysdate)- trunc(s_date) days from t1) "
					+ " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, application_id, applicant_name, (select SNAME from tm_state where scode=state) statename, (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname,SECTION_FILENO, to_char(submission_date, 'dd-mm-yyyy') as submission_date,days from t2 where "+subQueryServices+" order by days desc ";
			
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
	            
			parameters.put("D_APPLICATION_PENDANCY_TIME_RANGEWISE_REPORT", ds);    		    		
		//
			String tsPath = "/Reports/ApplicationPendancyTimerangeWise.jrxml";
			String fileName = "Detailed-Application-Pendancy-TimeRangewiseReport";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    				
		}
		else if(reportDisplayPendancyType.equalsIgnoreCase("s")){
			   //int length = rangeArray.size();
			//int rangeOne,rangeOne1,rangeTwo,rangeTwo1,rangeThree,rangeThree1,rangeFour,rangeFour1;
			 if(length!=0){
				   StringBuffer query = new StringBuffer(" with t1 as ( ");
				   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
				   		query.append(" t2 as ( ");
				   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
				   		query.append(" t3 as ( ");
				   		query.append(" select t2.*, ");
						   if(length==1){
							int rangeOne = rangeArray.get(0);
							int rangeOne1 = rangeOne+1;	
							 query.append(" case when days between 0 and "+rangeOne+" then 1 "); //1 range -- 1
							 //query.append(" when days between ? and 5000 then 2 "); // 1 range +1 and all --2
							 query.append(" else 2 end as grp from t2), ");
						   }
						   if(length==2){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;			
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); //1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); //1 range+1 and 2 range --2,3
							   //query.append(" when days between ? and 5000 then 3 "); //2 range+1 and all --4
							   query.append(" else 3 end as grp from t2), ");
						   }
						   if(length==3){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;	
								 int rangeThree = rangeArray.get(2);
								 int rangeThree1 = rangeThree+1;								
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); // 1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); // 1 range +1 and 2 range --2,3
							   query.append(" when days between "+rangeTwo1+" and "+rangeThree+" then 3 "); // 2 range+1 and 3 range --4,5
							   //query.append(" when days between ? and 5000 then 4 "); //3 range+1 and all --6
							   query.append(" else 4 end as grp from t2), ");
						   }
						   if(length==4){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;		
								 int rangeThree = rangeArray.get(2);
								 int rangeThree1 = rangeThree+1;
								 int rangeFour = rangeArray.get(3);
								 int rangeFour1 = rangeFour+1;										 
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); // 1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); // 1 range +1 and 2 range --2,3
							   query.append(" when days between "+rangeTwo1+" and "+rangeThree+" then 3 "); // 2 range+1 and 3 range --4,5
							   query.append(" when days between "+rangeThree1+" and "+rangeFour+" then 4 "); //3 range+1 and 4 range ---6,7
							   //query.append(" when days between ? and 5000 then 5 "); //4 range+1 and all --8
							   query.append(" else 5 end as grp from t2), ");
						   }			   		
						   
						   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
						   query.append(" t5 as (select t4.* from t4) select * from t5 "); // --9,10
						   
						   ReportDataSource ds = new ReportDataSource(parameters, query.toString(),connection, virtualizationMaxSize)  ;
						   parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
						   String tsPath = "/Reports/ApplicationPendancyTimerangeWiseStatistics.jrxml";
						   String fileName = "Statistics-Application-Pendancy-TimeRangewiseReport";
						   GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
								   fileName);// TODO Auto-generated method stub		
			   }
			 if(length==0){
				   StringBuffer query = new StringBuffer(" with t1 as ( ");
				   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
				   		query.append(" t2 as ( ");
				   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
				   		query.append(" t3 as ( ");
				   		query.append(" select t2.*, ");

				   		query.append(" case when days between 0 and 5000 then 1 "); //1 range -- 1
							 query.append(" else 2 end as grp from t2), ");
			   
						   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
						   query.append(" t5 as (select t4.* from t4) select * from t5 "); // --9,10
						   
						   ReportDataSource ds = new ReportDataSource(parameters, query.toString(),connection, virtualizationMaxSize)  ;
						   parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
						   String tsPath = "/Reports/ApplicationPendancyTimerangeWiseStatistics.jrxml";
						   String fileName = "Statistics-Application-Pendancy-TimeRangewiseReport";
						   GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
								   fileName);// TODO Auto-generated method stub		
			 }
		}
	   
	   }
	   @Override
	   protected void generateCSV() throws Exception{
			Map  parameters = new HashMap();
		   String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		   String  subQueryServices="1=1";
		   if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
		    	 subQueryServices=" service in ("+selectedServices+")";					
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		    int length = rangeArray.size();
		    parameters.put("length", length);
		    parameters.put("rangeArray",rangeArray);
		    parameters.put("heading0", heading0);
		    parameters.put("heading1", heading1);
		    parameters.put("heading2", heading2);
		    parameters.put("heading3", heading3);
		    parameters.put("heading4", heading4);		    
		    
		if(reportDisplayPendancyType.equalsIgnoreCase("d")){
		    String reportQuery="";
	
		    reportQuery =" with t1 as ( "
					+  " SELECT application_id, service_code service , applicant_name , state, district, case when submission_date is null then created_on else submission_date end AS s_date, SECTION_FILENO,submission_date "
					+  " FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), "
					+  " t2 as ( "
					+  " select t1.*, to_char(s_date, 'yyyy') YEAR , trunc(sysdate)- trunc(s_date) days from t1) "
					+ " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, application_id, applicant_name, (select SNAME from tm_state where scode=state) statename, (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname,SECTION_FILENO, to_char(submission_date, 'dd-mm-yyyy') as submission_date,days from t2 where "+subQueryServices+" order by days desc ";
			
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
	            
			parameters.put("D_APPLICATION_PENDANCY_TIME_RANGEWISE_REPORT", ds);    		    		
		//
			String tsPath = "/Reports/ApplicationPendancyTimerangeWiseCsv.jrxml";
			String fileName = "Detailed-Application-Pendancy-TimeRangewiseReport";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    				
		}
		else if(reportDisplayPendancyType.equalsIgnoreCase("s")){
			   //int length = rangeArray.size();
			//int rangeOne,rangeOne1,rangeTwo,rangeTwo1,rangeThree,rangeThree1,rangeFour,rangeFour1;
			 if(length!=0){
				   StringBuffer query = new StringBuffer(" with t1 as ( ");
				   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
				   		query.append(" t2 as ( ");
				   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
				   		query.append(" t3 as ( ");
				   		query.append(" select t2.*, ");
						   if(length==1){
							int rangeOne = rangeArray.get(0);
							int rangeOne1 = rangeOne+1;	
							 query.append(" case when days between 0 and "+rangeOne+" then 1 "); //1 range -- 1
							 //query.append(" when days between ? and 5000 then 2 "); // 1 range +1 and all --2
							 query.append(" else 2 end as grp from t2), ");
						   }
						   if(length==2){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;			
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); //1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); //1 range+1 and 2 range --2,3
							   //query.append(" when days between ? and 5000 then 3 "); //2 range+1 and all --4
							   query.append(" else 3 end as grp from t2), ");
						   }
						   if(length==3){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;	
								 int rangeThree = rangeArray.get(2);
								 int rangeThree1 = rangeThree+1;								
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); // 1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); // 1 range +1 and 2 range --2,3
							   query.append(" when days between "+rangeTwo1+" and "+rangeThree+" then 3 "); // 2 range+1 and 3 range --4,5
							   //query.append(" when days between ? and 5000 then 4 "); //3 range+1 and all --6
							   query.append(" else 4 end as grp from t2), ");
						   }
						   if(length==4){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;		
								 int rangeThree = rangeArray.get(2);
								 int rangeThree1 = rangeThree+1;
								 int rangeFour = rangeArray.get(3);
								 int rangeFour1 = rangeFour+1;										 
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); // 1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); // 1 range +1 and 2 range --2,3
							   query.append(" when days between "+rangeTwo1+" and "+rangeThree+" then 3 "); // 2 range+1 and 3 range --4,5
							   query.append(" when days between "+rangeThree1+" and "+rangeFour+" then 4 "); //3 range+1 and 4 range ---6,7
							   //query.append(" when days between ? and 5000 then 5 "); //4 range+1 and all --8
							   query.append(" else 5 end as grp from t2), ");
						   }			   		
						   
						   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
						   query.append(" t5 as (select t4.* from t4) select * from t5 "); // --9,10
						   
						   ReportDataSource ds = new ReportDataSource(parameters, query.toString(),connection, virtualizationMaxSize)  ;
						   parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
						   String tsPath = "/Reports/ApplicationPendancyTimerangeWiseStatisticsCsv.jrxml";
						   String fileName = "Statistics-Application-Pendancy-TimeRangewiseReport";
						   GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
								   fileName);// TODO Auto-generated method stub		
			   }
			 if(length==0){

				   StringBuffer query = new StringBuffer(" with t1 as ( ");
				   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
				   		query.append(" t2 as ( ");
				   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
				   		query.append(" t3 as ( ");
				   		query.append(" select t2.*, ");

				   		query.append(" case when days between 0 and 5000 then 1 "); //1 range -- 1
							 query.append(" else 2 end as grp from t2), ");
			   
						   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
						   query.append(" t5 as (select t4.* from t4) select * from t5 "); // --9,10
						   
						   ReportDataSource ds = new ReportDataSource(parameters, query.toString(),connection, virtualizationMaxSize)  ;
						   parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
						   String tsPath = "/Reports/ApplicationPendancyTimerangeWiseStatisticsCsv.jrxml";
						   String fileName = "Statistics-Application-Pendancy-TimeRangewiseReport";
						   GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
								   fileName);// TODO Auto-generated method stub		
			 				 
			 }
		}

/*			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);		 */  
	   }
	   @Override
	   protected void generateExcel() throws Exception{
			Map  parameters = new HashMap();
		   String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		   String  subQueryServices="1=1";
		   if((!selectedServices.trim().equals("ALL")) && (!selectedServices.contains("ALL")))
		    	 subQueryServices=" service in ("+selectedServices+")";
			
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		    int length = rangeArray.size();
		    parameters.put("length", length);
		    parameters.put("rangeArray",rangeArray);
		    parameters.put("heading0", heading0);
		    parameters.put("heading1", heading1);
		    parameters.put("heading2", heading2);
		    parameters.put("heading3", heading3);
		    parameters.put("heading4", heading4);
		    
		if(reportDisplayPendancyType.equalsIgnoreCase("d")){
		    String reportQuery="";
	
		    reportQuery =" with t1 as ( "
					+  " SELECT application_id, service_code service , applicant_name , state, district, case when submission_date is null then created_on else submission_date end AS s_date, SECTION_FILENO,submission_date "
					+  " FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), "
					+  " t2 as ( "
					+  " select t1.*, to_char(s_date, 'yyyy') YEAR , trunc(sysdate)- trunc(s_date) days from t1) "
					+ " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, application_id, applicant_name, (select SNAME from tm_state where scode=state) statename, (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname,SECTION_FILENO, to_char(submission_date, 'dd-mm-yyyy') as submission_date,days from t2 where "+subQueryServices+" order by days desc ";
			
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
	            
			parameters.put("D_APPLICATION_PENDANCY_TIME_RANGEWISE_REPORT", ds);    		    		
		//
			String tsPath = "/Reports/ApplicationPendancyTimerangeWise.jrxml";
			String fileName = "Detailed-Application-Pendancy-TimeRangewiseReport";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    				
		}
		else if(reportDisplayPendancyType.equalsIgnoreCase("s")){
			   //int length = rangeArray.size();
			//int rangeOne,rangeOne1,rangeTwo,rangeTwo1,rangeThree,rangeThree1,rangeFour,rangeFour1;
			 if(length!=0){
				   StringBuffer query = new StringBuffer(" with t1 as ( ");
				   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
				   		query.append(" t2 as ( ");
				   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
				   		query.append(" t3 as ( ");
				   		query.append(" select t2.*, ");
						   if(length==1){
							int rangeOne = rangeArray.get(0);
							int rangeOne1 = rangeOne+1;	
							 query.append(" case when days between 0 and "+rangeOne+" then 1 "); //1 range -- 1
							 //query.append(" when days between ? and 5000 then 2 "); // 1 range +1 and all --2
							 query.append(" else 2 end as grp from t2), ");
						   }
						   if(length==2){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;			
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); //1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); //1 range+1 and 2 range --2,3
							   //query.append(" when days between ? and 5000 then 3 "); //2 range+1 and all --4
							   query.append(" else 3 end as grp from t2), ");
						   }
						   if(length==3){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;	
								 int rangeThree = rangeArray.get(2);
								 int rangeThree1 = rangeThree+1;								
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); // 1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); // 1 range +1 and 2 range --2,3
							   query.append(" when days between "+rangeTwo1+" and "+rangeThree+" then 3 "); // 2 range+1 and 3 range --4,5
							   //query.append(" when days between ? and 5000 then 4 "); //3 range+1 and all --6
							   query.append(" else 4 end as grp from t2), ");
						   }
						   if(length==4){
								int rangeOne = rangeArray.get(0);
								int rangeOne1 = rangeOne+1;	
								int rangeTwo = rangeArray.get(1); 
								int rangeTwo1 = rangeTwo+1;		
								 int rangeThree = rangeArray.get(2);
								 int rangeThree1 = rangeThree+1;
								 int rangeFour = rangeArray.get(3);
								 int rangeFour1 = rangeFour+1;										 
							   query.append(" case when days between 0 and "+rangeOne+" then 1 "); // 1 range --1
							   query.append(" when days between "+rangeOne1+" and "+rangeTwo+" then 2 "); // 1 range +1 and 2 range --2,3
							   query.append(" when days between "+rangeTwo1+" and "+rangeThree+" then 3 "); // 2 range+1 and 3 range --4,5
							   query.append(" when days between "+rangeThree1+" and "+rangeFour+" then 4 "); //3 range+1 and 4 range ---6,7
							   //query.append(" when days between ? and 5000 then 5 "); //4 range+1 and all --8
							   query.append(" else 5 end as grp from t2), ");
						   }			   		
						   
						   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
						   query.append(" t5 as (select t4.* from t4) select * from t5 "); // --9,10
						   
						   ReportDataSource ds = new ReportDataSource(parameters, query.toString(),connection, virtualizationMaxSize)  ;
						   parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
						   String tsPath = "/Reports/ApplicationPendancyTimerangeWiseStatistics.jrxml";
						   String fileName = "Statistics-Application-Pendancy-TimeRangewiseReport";
						   GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
								   fileName);// TODO Auto-generated method stub		
			   }
			 if(length==0){


				   StringBuffer query = new StringBuffer(" with t1 as ( ");
				   		query.append(" SELECT application_id, applicant_name name, state, case when submission_date is null then created_on else submission_date end AS s_date,service_code service FROM v_application_details where current_stage=1 or (current_stage=2 and current_status=4)), ");
				   		query.append(" t2 as ( ");
				   		query.append(" select t1.*, trunc(sysdate)- trunc(s_date) days from t1 ), ");
				   		query.append(" t3 as ( ");
				   		query.append(" select t2.*, ");

				   		query.append(" case when days between 0 and 5000 then 1 "); //1 range -- 1
							 query.append(" else 2 end as grp from t2), ");
			   
						   query.append(" t4 as ( SELECT * FROM (SELECT service_desc, grp FROM t3, tm_service s WHERE t3.service=s.service_code and "+subQueryServices+") pivot (count(grp) FOR(grp) IN (1,2,3,4,5)) order by service_desc), ");
						   query.append(" t5 as (select t4.* from t4) select * from t5 "); // --9,10
						   
						   ReportDataSource ds = new ReportDataSource(parameters, query.toString(),connection, virtualizationMaxSize)  ;
						   parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
						   String tsPath = "/Reports/ApplicationPendancyTimerangeWiseStatistics.jrxml";
						   String fileName = "Statistics-Application-Pendancy-TimeRangewiseReport";
						   GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
								   fileName);// TODO Auto-generated method stub		
			 }
		}
	   }
	   @Override
	   protected void generateChart() throws Exception{}
	   
	   private void checkRangeOrder() throws Exception{
		   int rangeSize = rangeArray.size();
		   if(rangeSize>=2){
			   int temp = rangeArray.get(0);
			   int count=1;
			   while(count<rangeSize){
					if(temp<rangeArray.get(count)){
						temp=rangeArray.get(count);
						count++;
					}else{
						throw new ValidationException("Kindly enter time ranges in ascending order!");
					}
				}
			}
	   }
	   
	   @Override
	   protected void assignParameters(MultiValueMap<String, String> parameterMap) throws Exception{
		   //ArrayList<String> range = new ArrayList<String>();	
		   if(parameterMap != null) {
				//cureentBlockYear = parameterMap.get("blockYear-List").get(0);
				reportType = parameterMap.get("reportType").get(0);
				reportFormat = parameterMap.get("reportFormat").get(0);
				reportDisplayPendancyType = parameterMap.get("reportPendancyDisplayType").get(0);
				
				range1 = parameterMap.get("days-of-pendancy1").get(0);
				range2 = parameterMap.get("days-of-pendancy2").get(0);
				range3 = parameterMap.get("days-of-pendancy3").get(0);
				range4 = parameterMap.get("days-of-pendancy4").get(0);
				//allPendancy = "All Pendancy";
				allPendancy = "All Pendancy";
				allOther = "All Other Pendancy";
				if(range1!=""){range.add(0,Integer.parseInt(range1));heading1=range1;}else{range.add(0, -1);}
				if(range2!=""){range.add(1,Integer.parseInt(range2));heading2=range2;}else{range.add(1, -1);}
				if(range3!=""){range.add(2,Integer.parseInt(range3));heading3=range3;}else{range.add(2, -1);}
				if(range4!=""){range.add(3,Integer.parseInt(range4));heading4=range4;}else{range.add(3, -1);}
				
				int i=0, k=0;
				while(i<=3){
					Integer temp = range.get(i);
					if(temp!=-1){rangeArray.add(k, temp);k++;}
					i++;
				}
				//checkRangeOrder();
	
				if(rangeArray.size()==0){
					heading0="No. of Days Pending";
				}
				if(rangeArray.size()==1){
					heading0 = "0 to ".concat(rangeArray.get(0).toString().concat(" days"));
					heading1 = "> than ".concat(rangeArray.get(0).toString().concat(" days"));
				}
				if(rangeArray.size()==2){
					heading0 = "0 to ".concat(rangeArray.get(0).toString().concat(" days"));
					int num = rangeArray.get(0); num = num+1;
					heading1 = ""+num+"".concat(" to ").concat(rangeArray.get(1).toString().concat(" days"));
					heading2 = "> than ".concat(rangeArray.get(1).toString().concat(" days"));
				}
				if(rangeArray.size()==3){
					heading0="0 to ".concat(rangeArray.get(0).toString().concat(" days"));
					int num = rangeArray.get(0); num = num+1;
					heading1=""+num+"".concat(" to ").concat(rangeArray.get(1).toString().concat(" days"));
					int num1 = rangeArray.get(1); num1 = num1+1;
					heading2=""+num1+"".concat(" to ").concat(rangeArray.get(2).toString().concat(" days"));
					//int num1 = range.get(2); num1 = num1+1;
					//heading3="From range "+num1+"".concat(" to ").concat(rangeArray.get(2).toString());					
					heading3 = "> than ".concat(rangeArray.get(2).toString().concat(" days"));
				}
				if(rangeArray.size()==4){
					heading0="0 to ".concat(rangeArray.get(0).toString().concat(" days"));
					int num = rangeArray.get(0); num = num+1;
					heading1=""+num+"".concat(" to ").concat(rangeArray.get(1).toString().concat(" days"));
					int num1 = rangeArray.get(1); num1 = num1+1;
					heading2=""+num1+"".concat(" to ").concat(rangeArray.get(2).toString().concat(" days"));
					int num2 = rangeArray.get(2); num2 = num2+1;
					heading3=""+num2+"".concat(" to ").concat(rangeArray.get(3).toString().concat(" days"));
					heading4= "> than ".concat(rangeArray.get(3).toString().concat(" days"));	
					//heading5 = "All Other";
				}				
					//activityFormate = parameterMap.get("userActivity").get(0).toString();
				if(reportFormat.equals("3")){
					pageNum=parameterMap.get("pageNum").get(0);
					recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
				}
				selectServiceList=parameterMap.get("service-type");
				//reportDisplayType=parameterMap.get("reportDisplyType").get(0);
				//reportStatusDisplayType = parameterMap.get("reportStatusDisplyType").get(0);
				loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			    loginUserName=parameterMap.get("loginUserName").get(0);
			}
			
				myLoginofficecode=parameterMap.get("myLoginOfficCode").toString();
	/*			cureentBlockYear=getCurrentBlockYear();
				blkyrList=getblockyearList(); */
	   }
	   
	public List<ApplicationPendancyTimeRangewiseReport> getApplicationPendancyTimeRangewiseList() {
		return applicationPendancyTimeRangewiseList;
	}

	public void setApplicationPendancyTimeRangewiseList(
			List<ApplicationPendancyTimeRangewiseReport> applicationPendancyTimeRangewiseList) {
		this.applicationPendancyTimeRangewiseList = applicationPendancyTimeRangewiseList;
	}

	public List<String> getSelectServiceList() {
		return selectServiceList;
	}

	public void setSelectServiceList(List<String> selectServiceList) {
		this.selectServiceList = selectServiceList;
	}

	public int getVirtualizationMaxSize() {
		return virtualizationMaxSize;
	}
	public void setVirtualizationMaxSize(int virtualizationMaxSize) {
		this.virtualizationMaxSize = virtualizationMaxSize;
	}
	public String getReportDisplayPendancyType() {
		return reportDisplayPendancyType;
	}
	public void setReportDisplayPendancyType(String reportDisplayPendancyType) {
		this.reportDisplayPendancyType = reportDisplayPendancyType;
	}


	public String getAllOther() {
		return allOther;
	}

	public void setAllOther(String allOther) {
		this.allOther = allOther;
	}

	public String getallPendancy() {
		return allPendancy;
	}

	public void setallPendancy(String allPendancy) {
		this.allPendancy = allPendancy;
	}

	public String getHeading0() {
		return heading0;
	}

	public void setHeading0(String heading0) {
		this.heading0 = heading0;
	}

	public String getHeading1() {
		return heading1;
	}

	public void setHeading1(String heading1) {
		this.heading1 = heading1;
	}

	public String getHeading2() {
		return heading2;
	}

	public void setHeading2(String heading2) {
		this.heading2 = heading2;
	}

	public String getHeading3() {
		return heading3;
	}

	public void setHeading3(String heading3) {
		this.heading3 = heading3;
	}

	public String getHeading4() {
		return heading4;
	}

	public void setHeading4(String heading4) {
		this.heading4 = heading4;
	}

	public String getAllPendancy() {
		return allPendancy;
	}

	public void setAllPendancy(String allPendancy) {
		this.allPendancy = allPendancy;
	}

	public String getHeading5() {
		return heading5;
	}

	public void setHeading5(String heading5) {
		this.heading5 = heading5;
	}

	public String getRange1() {
		return range1;
	}

	public void setRange1(String range1) {
		this.range1 = range1;
	}

	public String getRange2() {
		return range2;
	}

	public void setRange2(String range2) {
		this.range2 = range2;
	}

	public String getRange3() {
		return range3;
	}

	public void setRange3(String range3) {
		this.range3 = range3;
	}

	public String getRange4() {
		return range4;
	}

	public void setRange4(String range4) {
		this.range4 = range4;
	}

	public ArrayList<Integer> getRange() {
		return range;
	}

	public void setRange(ArrayList<Integer> range) {
		this.range = range;
	}

	public String getMyLoginofficecode() {
		return myLoginofficecode;
	}
	public void setMyLoginofficecode(String myLoginofficecode) {
		this.myLoginofficecode = myLoginofficecode;
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
	   
}
