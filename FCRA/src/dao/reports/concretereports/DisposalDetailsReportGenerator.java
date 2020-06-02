package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.DisposalDetailsReport;







import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.KVPair;
import utilities.ReportDataSource;
import dao.master.ServicesDao;
import dao.reports.MISReportGenerator;

public class DisposalDetailsReportGenerator extends MISReportGenerator {
	private List<DisposalDetailsReport> disposalDetailsReport;
	private List<String> selectServiceList;
	private int virtualizationMaxSize = 200;
	private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private String loginOfficeName;
	private String loginUserName;
	private String fromDate;
	private String toDate;
	private String reportTypewise;
	private String loginOfficeCode;
	private String selectedInformation;

	public DisposalDetailsReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	public List<String> serviceList() throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("SELECT SERVICE_CODE FROM TM_SERVICE WHERE RECORD_STATUS=0");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<String>  servicesTypeList= new ArrayList<String>();
		while(rs.next()) {			
			servicesTypeList.add(rs.getString(1));				
		}
		return servicesTypeList;
	}
	
	
	
	
	@Override
	protected void generatePDF() throws Exception {
		// TODO Auto-generated method stub
		String  qp1="", qp2="";
		String reportTypewise1=reportTypewise;  
		String reportQuery1="",reportQuery = "";
		String  subQueryServices="";
		Map  parameters = new HashMap();
		parameters.put("loginUserName", loginUserName);
		parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("fromDate", fromDate);
		parameters.put("toDate",toDate);
		parameters.put("selectedInformation", selectedInformation);
		fromDate=fromDate.toString().replace("[", "").replace("]", "");
		toDate=toDate.toString().replace("[", "").replace("]", "");
		reportTypewise1=reportTypewise.toString().replace("[","").replace("]","");
		String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();		

		if(selectedServices.equals("ALL")){			
			selectServiceList=serviceList();
			selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();	
			subQueryServices = "service_code in ("+selectedServices+")";	
		}
		if(!selectedServices.trim().equals("ALL"))
			subQueryServices = "service_code in ("+selectedServices+")";	
		if(reportTypewise1.equals("M")){	
			parameters.put("titleKey","Disposal Details from " + fromDate + " to " + toDate);	
			selectedInformation = "Selected Office:" + loginOfficeName;
			parameters.put("selectedInformation", selectedInformation);

			reportQuery=" with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id " 
					+ " FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+ " BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code "
					+ " AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code" 
					+ " order by  c.service_desc, d.user_id)"
					+ " SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, to_char(activity_on, 'MON') month, to_char(activity_on, 'mm') mon, t.application_id, s.service_desc, v.service_code, status " 
					+ " FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+ " pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+ " where "+subQueryServices+" " 
					+ " order by year,mon,service_code ";
			//parameters.put("disposalDetails","Disposal Details");
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
			parameters.put("DisposeDetails", ds);  		
			String tsPath = "/Reports/DisposeDetails.jrxml";
			String fileName = "DisposalDetails-Report";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,fileName);   		
		}else if(reportTypewise1.equals("Y")){
			parameters.put("titleKey","Disposal Details from " + fromDate + " to " + toDate);	
			selectedInformation = "Selected Office:" + loginOfficeName;
			parameters.put("selectedInformation", selectedInformation);

			reportQuery1=" with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id "
					+" FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+" BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code " 
					+" AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code " 
					+" order by  c.service_desc, d.user_id)"
					+" SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, t.application_id, s.service_desc, v.service_code, status " 
					+" FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+" pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+" where "+subQueryServices+" " 
					+" order by year,service_code ";
			//parameters.put("disposalDetails","Disposal Details");
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery1,connection, virtualizationMaxSize)  ;
			parameters.put("DisposeDetailsY", ds);  		
			String tsPath = "/Reports/DisposeDetails.jrxml";
			String fileName = "DisposalDetails-Report";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,fileName);   	

		}

	}

	@Override
	protected void generateExcel() throws Exception {
		String reportTypewise1=reportTypewise;  
		String reportQuery1="",reportQuery = "";;
		Map  parameters = new HashMap();
		fromDate=fromDate.toString().replace("[", "").replace("]", "");
		toDate=toDate.toString().replace("[", "").replace("]", "");
		reportTypewise1=reportTypewise.toString().replace("[","").replace("]","");
		String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();		
		if(selectedServices.equals("ALL")){			
			selectServiceList=serviceList();
			selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();	
			String subQueryServices = "service_code in ("+selectedServices+")";	
		}
		String  subQueryServices="service_code in 1=1";
		if(!selectedServices.trim().equals("ALL"))
			subQueryServices = "service_code in ("+selectedServices+")";	
		if(reportTypewise1.equals("M")){	
	
			reportQuery=" with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id " 
					+ " FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+ " BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code "
					+ " AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code" 
					+ " order by  c.service_desc, d.user_id)"
					+ " SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, to_char(activity_on, 'MON') month, to_char(activity_on, 'mm') mon, t.application_id, s.service_desc, v.service_code, status " 
					+ " FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+ " pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+ " where "+subQueryServices+" " 
					+ " order by year,mon,service_code ";
		
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
			parameters.put("DisposeDetails", ds);  		
			String tsPath = "/Reports/DisposeDetailsExcel.jrxml";
			String fileName = "DisposalDetails-Report";
			GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName); 	
		}else if(reportTypewise1.equals("Y")){

			reportQuery1=" with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id "
					+" FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+" BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code " 
					+" AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code " 
					+" order by  c.service_desc, d.user_id)"
					+" SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, t.application_id, s.service_desc, v.service_code, status " 
					+" FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+" pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+" where "+subQueryServices+" " 
					+" order by year,service_code ";
			
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery1,connection, virtualizationMaxSize)  ;
			parameters.put("DisposeDetailsY", ds);  		
			String tsPath = "/Reports/DisposeDetailsExcel.jrxml";
			String fileName = "DisposalDetails-Report";
			GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName); 	

		}
	}

	@Override
	protected void generateHTML() throws Exception {
		// TODO Auto-generated method stub		
		String reportTypewise1=reportTypewise; 	
		reportTypewise1=reportTypewise.toString().replace("[","").replace("]","");
		if(reportTypewise1.equals("M")){
			disposalDetailsReport=fetchDisposaldetailsMonthlyHtml();
		    totalRecords=getTotalRecords();
		}
		else if(reportTypewise1.equals("Y")){
			disposalDetailsReport=fetchDisposaldetailsYearlyHtml();
		    totalRecords=getTotalRecords();			
		}
		

	}

	private List<DisposalDetailsReport> fetchDisposaldetailsMonthlyHtml()throws Exception {
		// TODO Auto-generated method stub
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		fromDate=fromDate.toString().replace("[", "").replace("]", "");
		toDate=toDate.toString().replace("[", "").replace("]", "");
		String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();		
		String  subQueryServices="";
		if(selectedServices.equals("ALL")){			
			selectServiceList=serviceList();
			selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();	
			subQueryServices = "service_code in ("+selectedServices+")";	
		}
		if(!selectedServices.trim().equals("ALL"))
			subQueryServices = "service_code in ("+selectedServices+")";	
		String reportQuery="";
		reportQuery="with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id " 
				+ " FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
				+ " BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code "
				+ " AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code" 
				+ " order by  c.service_desc, d.user_id)"
				+ " SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, to_char(activity_on, 'MON') month, to_char(activity_on, 'mm') mon, t.application_id, s.service_desc, v.service_code, status " 
				+ " FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
				+ " pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
				+ " where "+subQueryServices+" " 
				+ " order by year,mon,service_code";
	    StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+reportQuery+")");			
		PreparedStatement stmt = connection.prepareStatement(countQuery.toString());
		ResultSet rs = stmt.executeQuery(); 
		if(rs.next()){ 
			totalRecords = rs.getString(1);
		} 
		rs.close();
		stmt.close();	
		 Integer pageRequested = Integer.parseInt(pageNum);
		 Integer pageSize = Integer.parseInt(recordsPerPage);
		 StringBuffer query1 = new StringBuffer("with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id " 
					+ " FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+ " BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code "
					+ " AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code" 
					+ " order by  c.service_desc, d.user_id),t2 as"
					+ " (SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, to_char(activity_on, 'MON') month, to_char(activity_on, 'mm') mon, t.application_id, s.service_desc, v.service_code, status " 
					+ " FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+ " pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+ " where "+subQueryServices+" " 
					+ " order by year,mon,service_code),t3 as (select t2.*,rownum rn from t2 where  ROWNUM<=?) SELECT * FROM t3 WHERE RN>=? ");
		 PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 if(pageNum == null || recordsPerPage == null) {
				
		 }
		 else {
			 statement1.setInt(1, (pageRequested - 1) * pageSize + pageSize);
			 statement1.setInt(2, (pageRequested - 1) * pageSize + 1);
		 }	
		ResultSet rs1 = statement1.executeQuery();
		List<DisposalDetailsReport>  disposalDetailsReportList = new ArrayList<DisposalDetailsReport>();
		while(rs1.next()){
			disposalDetailsReportList.add(new DisposalDetailsReport(rs1.getString(1),rs1.getString(2),"",rs1.getString(4),"",rs1.getString(6),rs1.getString(7),rs1.getString(8)));
		}
		rs1.close();
		statement1.close();
		return disposalDetailsReportList;			
	}

	
	private List<DisposalDetailsReport> fetchDisposaldetailsYearlyHtml() throws Exception {
		// TODO Auto-generated method stub
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		fromDate=fromDate.toString().replace("[", "").replace("]", "");
		toDate=toDate.toString().replace("[", "").replace("]", "");
		String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();		
		String  subQueryServices="";
		if(selectedServices.equals("ALL")){			
			selectServiceList=serviceList();
			selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();	
			subQueryServices = "service_code in ("+selectedServices+")";	
		}
		if(!selectedServices.trim().equals("ALL"))
			subQueryServices = "service_code in ("+selectedServices+")";	
		String reportQuery="";
		reportQuery="with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id "
				+" FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
				+" BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code " 
				+" AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code " 
				+" order by  c.service_desc, d.user_id)"
				+" SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, t.application_id, s.service_desc, v.service_code, status " 
				+" FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
				+" pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
				+" where "+subQueryServices+" " 
				+" order by year,service_code";
		StringBuffer countQuery=new StringBuffer("SELECT COUNT(1) FROM ("+reportQuery+")");
		PreparedStatement stmt = connection.prepareStatement(countQuery.toString());
		ResultSet rs = stmt.executeQuery(); 
		if(rs.next()){ 
			totalRecords = rs.getString(1);
		} 
		rs.close();
		stmt.close();	
		Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
		
		StringBuffer query1 = new StringBuffer("with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id "
				+" FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
				+" BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code " 
				+" AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code " 
				+" order by  c.service_desc, d.user_id),t2 as"
				+" (SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, t.application_id, s.service_desc, v.service_code, status " 
				+" FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
				+" pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
				+" where "+subQueryServices+" " 
				+" order by year,service_code ),t3 as (select t2.*,rownum rn from t2 where  ROWNUM<=?) SELECT * FROM t3 WHERE RN>=?");
		 PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 if(pageNum == null || recordsPerPage == null) {
				
		 }
		 else {
			 statement1.setInt(1, (pageRequested - 1) * pageSize + pageSize);
			 statement1.setInt(2, (pageRequested - 1) * pageSize + 1);
		 }	
		ResultSet rs1 = statement1.executeQuery();
		List<DisposalDetailsReport>  disposalDetailsList = new ArrayList<DisposalDetailsReport>();
		while(rs1.next()){
			disposalDetailsList.add(new DisposalDetailsReport(rs1.getString(1),rs1.getString(2),"",rs1.getString(4),rs1.getString(5),rs1.getString(6)));
		}
		rs1.close();
		statement1.close();
		return disposalDetailsList;		

	}

	
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void generateCSV() throws Exception {
		// TODO Auto-generated method stub
		String reportTypewise1=reportTypewise;  
		String reportQuery1="",reportQuery = "";
		String subQueryServices="";
		Map  parameters = new HashMap();
		fromDate=fromDate.toString().replace("[", "").replace("]", "");
		toDate=toDate.toString().replace("[", "").replace("]", "");
		reportTypewise1=reportTypewise.toString().replace("[","").replace("]","");
		String selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();		
		if(selectedServices.equals("ALL")){			
			selectServiceList=serviceList();
			selectedServices = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();	
			subQueryServices = "service_code in ("+selectedServices+")";	
		}
		 
		if(!selectedServices.trim().equals("ALL"))
			subQueryServices = "service_code in ("+selectedServices+")";	
		if(reportTypewise1.equals("M")){	
	
			reportQuery=" with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id " 
					+ " FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+ " BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code "
					+ " AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code" 
					+ " order by  c.service_desc, d.user_id)"
					+ " SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, to_char(activity_on, 'MON') month, to_char(activity_on, 'mm') mon, t.application_id, s.service_desc, v.service_code, status " 
					+ " FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+ " pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+ " where "+subQueryServices+" " 
					+ " order by year,mon,service_code ";
		
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
			parameters.put("DisposeDetails", ds);  		
			String tsPath = "/Reports/DisposeDetailsCSV.jrxml";
			String fileName = "DisposalDetails-Report";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);	
		}else if(reportTypewise1.equals("Y")){

			reportQuery1=" with t as (SELECT A.activity_on, c.service_desc, d.user_id, A.status, A.application_id "
					+" FROM t_pc_office_level_final_status A,v_application_details b, tm_service c,  t_pc_office_user_details d WHERE A.application_id=b.application_id AND trunc(A.activity_on) " 
					+" BETWEEN  trunc(to_date('"+fromDate+"','dd-mm-yyyy')) AND trunc(to_date('"+toDate+"','dd-mm-yyyy')) AND A.status IN(9,10,12) AND b.service_code=c.service_code " 
					+" AND  A.office_code in('"+loginOfficeCode+"') AND A.application_id=d.application_id AND d.office_code=A.office_code " 
					+" order by  c.service_desc, d.user_id)"
					+" SELECT * FROM (SELECT distinct to_char(activity_on, 'yyyy') YEAR, t.application_id, s.service_desc, v.service_code, status " 
					+" FROM t, v_application_details v, tm_service s WHERE  t.application_id=v.application_id and  v.service_code=s.service_code)" 
					+" pivot (count(application_id) FOR(status) IN ('9' AS Granted, '10' AS Denied, '12' AS Closed))" 
					+" where "+subQueryServices+" " 
					+" order by year,service_code ";
			
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery1,connection, virtualizationMaxSize)  ;
			parameters.put("DisposeDetailsY", ds);  		
			String tsPath = "/Reports/DisposeDetailsCSV.jrxml";
			String fileName = "DisposalDetails-Report";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);

		}
		
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		// TODO Auto-generated method stub
		reportType = parameterMap.get("reportType").get(0);
		reportFormat = parameterMap.get("reportFormat").get(0);
		loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		loginOfficeCode = parameterMap.get("myLoginOfficCode").get(0);
	    loginUserName=parameterMap.get("loginUserName").get(0);
	    fromDate=parameterMap.get("from-date").toString();
		toDate=parameterMap.get("to-date").toString();
		reportTypewise=parameterMap.get("reportTypewise").toString();
		selectServiceList=parameterMap.get("service-type");
		if(reportFormat.equals("3")){
			pageNum=parameterMap.get("pageNum").get(0);
			recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
		}
		
		
		
	}

	public List<DisposalDetailsReport> getDisposalDetailsReport() {
		return disposalDetailsReport;
	}

	public void setDisposalDetailsReport(
			List<DisposalDetailsReport> disposalDetailsReport) {
		this.disposalDetailsReport = disposalDetailsReport;
	}

	public int getVirtualizationMaxSize() {
		return virtualizationMaxSize;
	}

	public void setVirtualizationMaxSize(int virtualizationMaxSize) {
		this.virtualizationMaxSize = virtualizationMaxSize;
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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getReportTypewise() {
		return reportTypewise;
	}

	public void setReportTypewise(String reportTypewise) {
		this.reportTypewise = reportTypewise;
	}

	public List<String> getSelectServiceList() {
		return selectServiceList;
	}

	public void setSelectServiceList(List<String> selectServiceList) {
		this.selectServiceList = selectServiceList;
	}

	public String getLoginOfficeCode() {
		return loginOfficeCode;
	}

	public void setLoginOfficeCode(String loginOfficeCode) {
		this.loginOfficeCode = loginOfficeCode;
	}

	public String getSelectedInformation() {
		return selectedInformation;
	}

	public void setSelectedInformation(String selectedInformation) {
		this.selectedInformation = selectedInformation;
	}

    

	
	
	
	
	
	
	
	
	
}
