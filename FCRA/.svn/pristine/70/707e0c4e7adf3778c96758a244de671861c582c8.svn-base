package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.StatusReport;

import org.springframework.util.MultiValueMap;

import dao.master.ServicesDao;
import dao.reports.MISReportGenerator;
import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;

public class StatusReportGenerator extends MISReportGenerator{
    
	private List<StatusReport> statusReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private List<String> selectServiceList;
	private List<String> selectYearList;

	private String loginOfficeName;
	private String loginUserName;
	private String reportDisplayType;
	private int virtualizationMaxSize = 200;
	public StatusReportGenerator(Connection connection) {
		super(connection);
	}
  
	
	
	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);
		parameters.put("reportDisplayType", reportDisplayType);
	    String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    parameters.put("selectYearList", selectedYear);
		  if(selectedServices.equals("ALL")){
			   parameters.put("selectServiceList",selectedServices);
			    }
			    else{
			    	ServicesDao sdao=new ServicesDao(connection);
			    	 parameters.put("selectServiceList",sdao.getServiceList(selectedServices).toString());
			    }
	      	  
		  String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if(!selectedServices.trim().equals("ALL"))
	    	 subQueryServices=" service in ("+selectedServices+")";
	     if(!selectedYear.trim().equals("ALL"))
	         subQueryYear=" YEAR in("+selectedYear+")";
	     
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		
		if(reportDisplayType.equals("s")){
		String reportQuery="WITH t1 AS ("
		+ "SELECT application_id, case when submission_date is null then created_on else submission_date end AS s_date, service_code service, "
		+ " CASE WHEN current_stage=1 THEN 0 "
		+ " ELSE  "
		+ "CASE WHEN current_stage=2 then "
		+ " CASE WHEN current_status=4 THEN 1  "
		+ "WHEN current_status=9 THEN 2  "
		+ " WHEN current_status=10 THEN 3  "
		+ " WHEN current_status=12 THEN 4"
		+ "  WHEN current_status=8 THEN 5  "
		+ " END"
		+ " END"
		+ " END "
		+ "status "
		+ " FROM v_application_details "
		+ " ), t2 AS ( "
		+ "SELECT to_char(s_date, 'yyyy') YEAR, service, status FROM t1 where "+subQueryServices+" "
		+ "), "
		+ " T4 as (SELECT * FROM (SELECT YEAR, service_desc, status FROM t2, tm_service s WHERE t2.service=s.service_code)"
		+ " pivot (count(status) FOR(status) IN (0 AS FRESH, 1 AS Pending, 2 AS Approved, 3 AS Denied, 4 AS Closed, 5 as ClarificationRequested)) where "
		+ ""+subQueryYear+""
		+ "order by year, service_desc ), T5 AS (SELECT T4.* FROM T4) SELECT * FROM T5";
		ReportDataSource ds1=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
 		//ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
		parameters.put("NEW_DATASOURCE", ds1);
		}
		else{

			String reportQuery="WITH t1 AS (SELECT application_id,SECTION_FILENO, applicant_name,state,district, case when submission_date is null "
					+ " then created_on else submission_date end AS s_date, service_code service,  "
					+ "CASE WHEN current_stage=1 THEN 0  ELSE  CASE "
					+ " WHEN current_stage=2 then  CASE WHEN current_status=4 THEN 1 "
					+ " WHEN current_status=9 THEN 2   "
					+ " WHEN current_status=10 THEN 3   WHEN current_status=12 THEN 4  WHEN current_status=8 THEN 5  "
					+ " END END END status,submission_date  FROM v_application_details  ),  "
					+ " t2 AS ( SELECT application_id,SECTION_FILENO, applicant_name, state,district, to_char(s_date, 'yyyy') YEAR, service,"
					+ "  status,submission_date FROM t1 where  "+subQueryServices+" ) "
					+ " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, "
					+ " application_id,SECTION_FILENO, applicant_name, (select SNAME from tm_state where scode=state) statename,"
					+ " (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname, "
					+ "DECODE (status, 0, 'Fresh',   1, 'Pending',   2, 'Approved',  3, 'Denied',  4, 'Closed', 5, 'ClarificationRequested') statusname,to_char(submission_date,'dd-mm-yyyy') as submission_date  from t2"
					+ "  where "+subQueryYear+" order by year, service, status, state, district, applicant_name";
			ReportDataSource ds2=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
	 		//ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("NEW_DATASOURCE_DETAILS", ds2);
			
		}
		String tsPath = "/Reports/ApplicationStatusReport.jrxml";
		String fileName = "ApplicationStatusReport.pdf";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void generateExcel() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);
	    String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    parameters.put("selectYearList", selectedYear);
		  if(selectedServices.equals("ALL")){
			   parameters.put("selectServiceList",selectedServices);
			    }
			    else{
			    	ServicesDao sdao=new ServicesDao(connection);
			    	 parameters.put("selectServiceList",sdao.getServiceList(selectedServices).toString());
			    }
	      	  
		  String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if(!selectedServices.trim().equals("ALL"))
	    	 subQueryServices=" service in ("+selectedServices+")";
	     if(!selectedYear.trim().equals("ALL"))
	         subQueryYear=" YEAR in("+selectedYear+")";
	     
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if(reportDisplayType.equals("s")){
		String reportQuery="WITH t1 AS ("
		+ "SELECT application_id, case when submission_date is null then created_on else submission_date end AS s_date, service_code service, "
		+ " CASE WHEN current_stage=1 THEN 0 "
		+ " ELSE  "
		+ "CASE WHEN current_stage=2 then "
		+ " CASE WHEN current_status=4 THEN 1  "
		+ "WHEN current_status=9 THEN 2  "
		+ " WHEN current_status=10 THEN 3  "
		+ " WHEN current_status=12 THEN 4"
		+ "  WHEN current_status=8 THEN 5  "
		+ " END"
		+ " END"
		+ " END "
		+ "status "
		+ " FROM v_application_details "
		+ " ), t2 AS ( "
		+ "SELECT to_char(s_date, 'yyyy') YEAR, service, status FROM t1 where "+subQueryServices+" "
		+ "), "
		+ " T4 as (SELECT * FROM (SELECT YEAR, service_desc, status FROM t2, tm_service s WHERE t2.service=s.service_code)"
		+ " pivot (count(status) FOR(status) IN (0 AS FRESH, 1 AS Pending, 2 AS Approved, 3 AS Denied, 4 AS Closed, 5 as ClarificationRequested)) where "
		+ ""+subQueryYear+""
		+ "order by year, service_desc ), T5 AS (SELECT T4.* FROM T4) SELECT * FROM T5";
		ReportDataSource ds1=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
 		//ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
		parameters.put("NEW_DATASOURCE", ds1);
		}
		else{


			String reportQuery="WITH t1 AS (SELECT application_id,SECTION_FILENO, applicant_name,state,district, case when submission_date is null "
					+ " then created_on else submission_date end AS s_date, service_code service,  "
					+ "CASE WHEN current_stage=1 THEN 0  ELSE  CASE "
					+ " WHEN current_stage=2 then  CASE WHEN current_status=4 THEN 1 "
					+ " WHEN current_status=9 THEN 2   "
					+ " WHEN current_status=10 THEN 3   WHEN current_status=12 THEN 4  WHEN current_status=8 THEN 5  "
					+ " END END END status,submission_date  FROM v_application_details  ),  "
					+ " t2 AS ( SELECT application_id,SECTION_FILENO, applicant_name, state,district, to_char(s_date, 'yyyy') YEAR, service,"
					+ "  status,submission_date FROM t1 where  "+subQueryServices+" ) "
					+ " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, "
					+ " application_id,SECTION_FILENO, applicant_name, (select SNAME from tm_state where scode=state) statename,"
					+ " (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname, "
					+ "DECODE (status, 0, 'Fresh',   1, 'Pending',   2, 'Approved',  3, 'Denied',  4, 'Closed', 5, 'ClarificationRequested') statusname, to_char(submission_date,'dd-mm-yyyy') as submission_date  from t2"
					+ "  where "+subQueryYear+" order by year, service, status, state, district, applicant_name";
			ReportDataSource ds2=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
	 		//ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("NEW_DATASOURCE_DETAILS", ds2);
			
		
		}
		String tsPath = "/Reports/ApplicationStatusReport.jrxml";
		//String tsPath = "/Reports/ApplicationStatus_Excel_Report.jrxml";
		String fileName = "ApplicationStatusReport";
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void generateHTML() throws Exception {
       if(reportDisplayType.equals("s")){
   		statusReport=getMasterStatusReport();	
   		totalRecords=getTotalRecords();	 
       }
       else {
    	   statusReport=getMasterStatusReportDetails();
    	  totalRecords=getTotalRecords();
       }
		

		
	}

	@Override
	protected void generateChart() throws Exception {
	}

	
	public List<StatusReport> getMasterStatusReport() throws Exception {
        String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		  String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	       
		  String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if(!selectedServices.trim().equals("ALL"))
	    	 subQueryServices=" service in ("+selectedServices+")";
	     if(!selectedYear.trim().equals("ALL"))
	         subQueryYear=" YEAR in("+selectedYear+")";
	     
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer countQuery = new StringBuffer(
				"WITH t1 AS ("
				+ "SELECT application_id, case when submission_date is null then created_on else submission_date end AS s_date, service_code service, "
				+ " CASE WHEN current_stage=1 THEN 0 "
				+ " ELSE  "
				+ "CASE WHEN current_stage=2 then "
				+ " CASE WHEN current_status=4 THEN 1  "
				+ "WHEN current_status=9 THEN 2  "
				+ " WHEN current_status=10 THEN 3  "
				+ " WHEN current_status=12 THEN 4"
				+ "  WHEN current_status=8 THEN 5  "
				+ " END"
				+ " END"
				+ " END "
				+ "status "
				+ " FROM v_application_details "
				+ " ), t2 AS ( "
				+ "SELECT to_char(s_date, 'yyyy') YEAR, service, status FROM t1 where "+subQueryServices+" "
				+ ") "
				+ "Select count(*) from (SELECT * FROM (SELECT YEAR, service_desc, status FROM t2, tm_service s WHERE t2.service=s.service_code)"
				+ " pivot (count(status) FOR(status) IN (0 AS FRESH, 1 AS Pending, 2 AS Approved, 3 AS Denied, 4 AS Closed, 5 as ClarificationRequested)) where "
				+ ""+subQueryYear+""
				+ "order by year, service_desc) ");
		StringBuffer query = new StringBuffer(
				"WITH t1 AS ("
				+ "SELECT application_id, case when submission_date is null then created_on else submission_date end AS s_date, service_code service, "
				+ " CASE WHEN current_stage=1 THEN 0 "
				+ " ELSE  "
				+ "CASE WHEN current_stage=2 then "
				+ " CASE WHEN current_status=4 THEN 1  "
				+ "WHEN current_status=9 THEN 2  "
				+ " WHEN current_status=10 THEN 3  "
				+ " WHEN current_status=12 THEN 4"
				+ "  WHEN current_status=8 THEN 5  "
				+ " END"
				+ " END"
				+ " END "
				+ "status "
				+ " FROM v_application_details "
				+ " ), t2 AS ( "
				+ "SELECT to_char(s_date, 'yyyy') YEAR, service, status FROM t1 where "+subQueryServices+" "
				+ "), "
				+ " T4 as (SELECT * FROM (SELECT YEAR, service_desc, status FROM t2, tm_service s WHERE t2.service=s.service_code)"
				+ " pivot (count(status) FOR(status) IN (0 AS FRESH, 1 AS Pending, 2 AS Approved, 3 AS Denied, 4 AS Closed, 5 as ClarificationRequested)) where "
				+ ""+subQueryYear+""
				+ "order by year, service_desc ), T5 AS (SELECT T4.*, ROWNUM RN FROM T4) SELECT * FROM T5 WHERE RN between ? and ? ") ;
		
		
	  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
	  ResultSet rs = statement.executeQuery(); if(rs.next())
	 { 
		  totalRecords = rs.getString(1);
	 } 
	 rs.close();
	 statement.close();
		
	 Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
	      statement = connection.prepareStatement(query.toString());
		if (pageNum == null || recordsPerPage == null) {

		} else {
			statement.setInt(1, (pageRequested - 1) * pageSize + 1);
			statement.setInt(2, (pageRequested - 1) * pageSize + pageSize);
			
		}
		System.out.println("Print Query qq+++ "+query.toString());
		 rs = statement.executeQuery();
		List<StatusReport> reportTypeList = new ArrayList<StatusReport>();
		while (rs.next()) {
			
			reportTypeList.add(new StatusReport(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
		}
		return reportTypeList;
	
	
		
	}
	
	public List<StatusReport> getMasterStatusReportDetails() throws Exception {

        String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		  String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	       
		  String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if(!selectedServices.trim().equals("ALL"))
	    	 subQueryServices=" service in ("+selectedServices+")";
	     if(!selectedYear.trim().equals("ALL"))
	         subQueryYear=" YEAR in("+selectedYear+")";
	     
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer countQuery = new StringBuffer(
				"WITH t1 AS (SELECT application_id, applicant_name,state,district, case when submission_date is null then created_on "
				+ "else submission_date end AS s_date, service_code service,   "
				+ " CASE WHEN current_stage=1 THEN 0  ELSE  CASE WHEN current_stage=2 then  CASE WHEN current_status=4 THEN 1"
				+ "  WHEN current_status=9 THEN 2   "
				+ "WHEN current_status=10 THEN 3   WHEN current_status=12 THEN 4  WHEN current_status=8 THEN 5   END END END status,SECTION_FILENO,submission_date  FROM v_application_details  ),  "
				+ " t2 AS ( SELECT application_id, applicant_name, state,district, to_char(s_date, 'yyyy') YEAR, service, status,SECTION_FILENO,submission_date FROM t1 where  "+subQueryServices+" ), "
				+ " t3 as ( select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, application_id, applicant_name, (select SNAME from tm_state where scode=state) statename,"
				+ "(select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname,  "
				+ "DECODE (status, 0, 'Fresh',  "
				+ "1, 'Pending',  "
				+ " 2, 'Approved',  3, 'Denied', 4, 'Closed', 5, 'ClarificationRequested') statusname,SECTION_FILENO,submission_date from t2 where "+subQueryYear+" order by year, "
				+ "service, status, state, district, applicant_name ) select count(1) from t3");
		StringBuffer query = new StringBuffer(
				" WITH t1 AS (SELECT application_id, applicant_name,state,district, case when submission_date is null then created_on else submission_date end AS s_date, service_code service,   "
				+ " CASE WHEN current_stage=1 THEN 0  ELSE  CASE WHEN current_stage=2 then  CASE WHEN current_status=4 THEN 1  WHEN current_status=9 THEN 2    "
				+ " WHEN current_status=10 THEN 3   WHEN current_status=12 THEN 4  WHEN current_status=8 THEN 5   END END END status,SECTION_FILENO,submission_date  FROM v_application_details  ),  "
				+ " t2 AS ( SELECT application_id, applicant_name, state,district, to_char(s_date, 'yyyy') YEAR, service, status,SECTION_FILENO,to_char(submission_date,'dd-mm-yyyy') as submission_date  FROM t1 where  "+subQueryServices+" ) ,"
				+ "  t3 as (select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, application_id, applicant_name,   "
				+ " (select SNAME from tm_state where scode=state) statename, "
				+ " (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname,  "
				+ " DECODE (status, 0, 'Fresh',  1, 'Pending',  2, 'Approved',  3, 'Denied',  4, 'Closed', 5, 'ClarificationRequested') statusname,SECTION_FILENO,submission_date"
				+ "  from t2 where "+subQueryYear+" order by year, service, status, state, district, applicant_name),  "
				+ "  t4  as (SELECT t3.* , ROWNUM RN FROM t3) SELECT * FROM T4 WHERE RN between ? AND ? ") ;
		
		
	  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
	  ResultSet rs = statement.executeQuery(); if(rs.next())
	 { 
		  totalRecords = rs.getString(1);
	 } 
	 rs.close();
	 statement.close();
		
	 Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
	      statement = connection.prepareStatement(query.toString());
		if (pageNum == null || recordsPerPage == null) {

		} else {
			statement.setInt(1, (pageRequested - 1) * pageSize + 1);
			statement.setInt(2, (pageRequested - 1) * pageSize + pageSize);
		
		}
		System.out.println("Print Query qq+++ "+query.toString());
		 rs = statement.executeQuery();
		List<StatusReport> reportTypeList = new ArrayList<StatusReport>();
		while (rs.next()) {
			
			reportTypeList.add(new StatusReport(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8),rs.getString(9)));
		}
		return reportTypeList;
	
	
		
	
	}
	
	
	
public String checkNull(String val){
		if(val==null||val=="")
			val="-";
		return val;
	}
	



	public List<StatusReport> getStatusReport() {
		return statusReport;
	}



	public void setStatusReport(List<StatusReport> statusReport) {
		this.statusReport = statusReport;
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



	public List<String> getSelectServiceList() {
		return selectServiceList;
	}



	public void setSelectServiceList(List<String> selectServiceList) {
		this.selectServiceList = selectServiceList;
	}



	public List<String> getSelectYearList() {
		return selectYearList;
	}



	public void setSelectYearList(List<String> selectYearList) {
		this.selectYearList = selectYearList;
	}
	public String getLoginOfficeName() {
		return loginOfficeName;
	}



	public void setLoginOfficeName(String loginOfficeName) {
		this.loginOfficeName = loginOfficeName;
	}



	public String getLoginUserName() {
		return loginUserName;
	}



	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap) throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);//("reportType");
			reportFormat = parameterMap.get("reportFormat").get(0);//("reportFormat");
			if(reportFormat.equals("3")){
			pageNum=parameterMap.get("pageNum").get(0);
			recordsPerPage=parameterMap.get("recordsPerPage").get(0);
			}
			selectServiceList=parameterMap.get("service-type");
			selectYearList=parameterMap.get("statusYear-List");
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
			reportDisplayType=parameterMap.get("app-status-reportDisplyType").get(0);
		}		
	}



	@Override
	protected void generateCSV() throws Exception {

		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);
	    String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		String selectedYear=selectYearList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    parameters.put("selectYearList", selectedYear);
		  if(selectedServices.equals("ALL")){
			   parameters.put("selectServiceList",selectedServices);
			    }
			    else{
			    	ServicesDao sdao=new ServicesDao(connection);
			    	 parameters.put("selectServiceList",sdao.getServiceList(selectedServices).toString());
			    }
	      	  
		  String  subQueryServices="1=1";
			String subQueryYear="1=1";
	     if(!selectedServices.trim().equals("ALL"))
	    	 subQueryServices=" service in ("+selectedServices+")";
	     if(!selectedYear.trim().equals("ALL"))
	         subQueryYear=" YEAR in("+selectedYear+")";
	     
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		if(reportDisplayType.equals("s")){
		String reportQuery="WITH t1 AS ("
		+ "SELECT application_id, case when submission_date is null then created_on else submission_date end AS s_date, service_code service, "
		+ " CASE WHEN current_stage=1 THEN 0 "
		+ " ELSE  "
		+ "CASE WHEN current_stage=2 then "
		+ " CASE WHEN current_status=4 THEN 1  "
		+ "WHEN current_status=9 THEN 2  "
		+ " WHEN current_status=10 THEN 3  "
		+ " WHEN current_status=12 THEN 4"
		+ "  WHEN current_status=8 THEN 5  "
		+ " END"
		+ " END"
		+ " END "
		+ "status "
		+ " FROM v_application_details "
		+ " ), t2 AS ( "
		+ "SELECT to_char(s_date, 'yyyy') YEAR, service, status FROM t1 where "+subQueryServices+" "
		+ "), "
		+ " T4 as (SELECT * FROM (SELECT YEAR, service_desc, status FROM t2, tm_service s WHERE t2.service=s.service_code)"
		+ " pivot (count(status) FOR(status) IN (0 AS FRESH, 1 AS Pending, 2 AS Approved, 3 AS Denied, 4 AS Closed, 5 as ClarificationRequested)) where "
		+ ""+subQueryYear+""
		+ "order by year, service_desc ), T5 AS (SELECT T4.* FROM T4) SELECT * FROM T5";
		ReportDataSource ds1=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
 		//ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
		parameters.put("NEW_DATASOURCE", ds1);
		}
		else {


			String reportQuery="WITH t1 AS (SELECT application_id,SECTION_FILENO, applicant_name,state,district, case when submission_date is null "
					+ " then created_on else submission_date end AS s_date, service_code service,  "
					+ "CASE WHEN current_stage=1 THEN 0  ELSE  CASE "
					+ " WHEN current_stage=2 then  CASE WHEN current_status=4 THEN 1 "
					+ " WHEN current_status=9 THEN 2   "
					+ " WHEN current_status=10 THEN 3   WHEN current_status=12 THEN 4  WHEN current_status=8 THEN 5  "
					+ " END END END status,submission_date  FROM v_application_details  ),  "
					+ " t2 AS ( SELECT application_id,SECTION_FILENO, applicant_name, state,district, to_char(s_date, 'yyyy') YEAR, service,"
					+ "  status,submission_date FROM t1 where  "+subQueryServices+" ) "
					+ " select year, (select SERVICE_DESC from TM_SERVICE where SERVICE_CODE=service) servicename, "
					+ " ''''|| application_id ||'''' as application_id ,SECTION_FILENO, applicant_name, (select SNAME from tm_state where scode=state) statename,"
					+ " (select distNAME from TM_DISTRICT where scode=state and distcode=district) districtname, "
					+ "DECODE (status, 0, 'Fresh',   1, 'Pending',   2, 'Approved',  3, 'Denied',  4, 'Closed', 5, 'ClarificationRequested') statusname,to_char(submission_date,'dd-mm-yyyy') as submission_date from t2"
					+ "  where "+subQueryYear+" order by year, service, status, state, district, applicant_name";
			ReportDataSource ds2=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
	 		//ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("NEW_DATASOURCE_DETAILS", ds2);
			
		
		}
		String tsPath = "/Reports/ApplicationStatus_CSV_Report.jrxml";
		String fileName = "ApplicationStatusReport";
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	
		
	}

}
