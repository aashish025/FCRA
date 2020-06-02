package dao.reports.concretereports;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.reports.MISReportGenerator;

public class BlueFlaggedRcnsReportGenerator extends MISReportGenerator{
	 private MultiValueMap<String, String> parameterMap;
		private String totalRecords;	
		private String reportDisplyType;
		private String loginUserName;
		private String loginOfficeName;
		private String rcnNumber;
		private int virtualizationMaxSize = 200;	

	public BlueFlaggedRcnsReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {

		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
			     
		reportQuery = new String("  with temp as( SELECT  "
		 		+ "(row_number() over (partition by a.rcn order by  C.STATUS_DATE desc)) as rn ,A.RCN,B.ASSO_NAME,"
		 		+ " (select sname from tm_state where scode=substr(b.stdist,1,2)) as state, "
		 		+ "(select distname from tm_district where distcode=substr(b.stdist,-3,3))  as district,"
		 		+ " to_char(C.STATUS_DATE,'dd-mm-yyyy')as Status_date,C.ACTION_BY||'('|| e.user_name||')' as user_name ,"
		 		+ "  c.REMARKS "
		 		+ " FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, T_BLUE_FLAG_STATUS_HISTORY C  "
		 		+ " ,TM_User e WHERE c.status=0 AND  "
		 		+ " A.RCN=B.RCN AND A.RCN=C.RCN AND  "
		 		+ "  c.action_by=E.USER_id ) select * from temp where rn=1  ") ;
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("BlueFlaggedRcns", ds);
	     
	     
		String tsPath = "/Reports/BlueFlaggedRcnsReport.jrxml";
		String fileName = "BlueFlaggedRcns";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	
	



		
	}

	@Override
	protected void generateExcel() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
			     
	    
		reportQuery = new String("  with temp as( SELECT  "
		 		+ "(row_number() over (partition by a.rcn order by  C.STATUS_DATE desc)) as rn ,A.RCN,B.ASSO_NAME,"
		 		+ " (select sname from tm_state where scode=substr(b.stdist,1,2)) as state, "
		 		+ "(select distname from tm_district where distcode=substr(b.stdist,-3,3))  as district,"
		 		+ " to_char(C.STATUS_DATE,'dd-mm-yyyy')as Status_date,C.ACTION_BY||'('|| e.user_name||')' as user_name ,"
		 		+ "  c.REMARKS "
		 		+ " FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, T_BLUE_FLAG_STATUS_HISTORY C  "
		 		+ " ,TM_User e WHERE c.status=0 AND  "
		 		+ " A.RCN=B.RCN AND A.RCN=C.RCN AND  "
		 		+ "  c.action_by=E.USER_id ) select * from temp where rn=1  ") ;
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("BlueFlaggedRcns", ds);
	     
	     
		String tsPath = "/Reports/BlueFlaggedRcnsExcelReport.jrxml";
		String fileName = "BlueFlaggedRcns";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	    


		
	}

	@Override
	protected void generateHTML() throws Exception {
		// TODO Auto-generated method stub
		
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
			     

		reportQuery = new String("  with temp as( SELECT  "
		 		+ "(row_number() over (partition by a.rcn order by  C.STATUS_DATE desc)) as rn ,A.RCN,B.ASSO_NAME,"
		 		+ " (select sname from tm_state where scode=substr(b.stdist,1,2)) as state, "
		 		+ "(select distname from tm_district where distcode=substr(b.stdist,-3,3))  as district,"
		 		+ " to_char(C.STATUS_DATE,'dd-mm-yyyy')as Status_date,C.ACTION_BY||'('|| e.user_name||')' as user_name ,"
		 		+ "  c.REMARKS "
		 		+ " FROM T_BLUE_FLAGGED_ASSOCIATIONS A, fc_india B, T_BLUE_FLAG_STATUS_HISTORY C  "
		 		+ " ,TM_User e WHERE c.status=0 AND  "
		 		+ " A.RCN=B.RCN AND A.RCN=C.RCN AND  "
		 		+ "  c.action_by=E.USER_id ) select * from temp where rn=1  ") ;
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("BlueFlaggedRcns", ds);
	     
	     
		String tsPath = "/Reports/BlueFlaggedRcnsCsvReport.jrxml";
		String fileName = "BlueFlaggedRcns";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	
	


		
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			//rcnNumber=parameterMap.get("rcnNumber").get(0);
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);			
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
			
		}
		
	}

	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public String getReportDisplyType() {
		return reportDisplyType;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public String getLoginOfficeName() {
		return loginOfficeName;
	}

	public String getRcnNumber() {
		return rcnNumber;
	}

	public int getVirtualizationMaxSize() {
		return virtualizationMaxSize;
	}

	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public void setReportDisplyType(String reportDisplyType) {
		this.reportDisplyType = reportDisplyType;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public void setLoginOfficeName(String loginOfficeName) {
		this.loginOfficeName = loginOfficeName;
	}

	public void setRcnNumber(String rcnNumber) {
		this.rcnNumber = rcnNumber;
	}

	public void setVirtualizationMaxSize(int virtualizationMaxSize) {
		this.virtualizationMaxSize = virtualizationMaxSize;
	}

}
