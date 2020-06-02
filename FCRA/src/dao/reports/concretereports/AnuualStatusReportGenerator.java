package dao.reports.concretereports;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.master.GenderType;
import models.reports.AnnualStatusDetailsReport;
import models.reports.CountryStateReport;
import models.reports.ReturnFiledReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.KVPair;
import utilities.ReportDataSource;
import utilities.lists.List2;
import dao.reports.MISReportGenerator;

public class AnuualStatusReportGenerator extends MISReportGenerator {
	private List<AnnualStatusDetailsReport> annualStatusDetailsList;
	 private MultiValueMap<String, String> parameterMap;
		private String pageNum;
		private String recordsPerPage;
		private String totalRecords;
		private String loginOfficeName;
		private String loginUserName;
		private String noOfYear;
		private List<String> yearList;
		private int virtualizationMaxSize = 200;

	public AnuualStatusReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		 parameters.put("loginOfficeName",loginOfficeName);
		 parameters.put("noOfYear", noOfYear);	
			parameters.put("loginUserName", loginUserName);	
		StringBuffer query = new StringBuffer("with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ "to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-?+1 start_year from dual),t2 as (select"
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ "then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1)"
				+ "select yr||'-'||(yr+1) blkyear from t2");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, noOfYear );			
		ResultSet rs = statement.executeQuery();
		StringBuffer query1 = new StringBuffer("select  rcn,asso_name,(select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+ "(select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name,"
				+ "nvl(to_char(reg_date,'dd-mm-yyyy'),'---') as REGISTRERED_ON,nvl(to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') as"
				+ " LAST_RENEWED_ON,nvl(to_char(valid_to,'dd-mm-yyyy'),'---') as EXPIRY_ON,");
		   char alphabet = 'A';
			while(rs.next()){
				parameters.put(alphabet+"1", rs.getString(1));
				query1.append("(Case when trunc(reg_date) >= to_date('01-APR-'||substr('"+rs.getString(1)+"',-4,4)) then '-' else (select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = i.rcn )end ) as "+alphabet+" ,");
				//query1.append("(select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = rcn ) as "+alphabet+" ,"); 
				alphabet++;
			}
			int count=0;
			count=rs.getRow();
			    if(count<6){
			    	for(int i = count; i<6;i++){
			    		query1.append("null as "+alphabet+" ,");
			    		parameters.put(alphabet+"1", "");
			    		alphabet++;
			    	}
			    }
    	    	rs.close();
			   query1.setLength(query1.length()-1);
			    query1.append("  from fc_india i where status <> 'D' and CURRENT_STATUS=0  and ASSOCIATION_TYPE is null and (valid_to is not null) and (trunc(valid_to) >= trunc(sysdate)) ");
			    ReportDataSource ds=new ReportDataSource(parameters, query1.toString(), connection, virtualizationMaxSize);
				 parameters.put("PRINTRECORD_AnnualReport", ds);
				  String tsPath = "/Reports/Annual-StatusReport.jrxml";
					String fileName = "AnnualStatusReport";
					GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);

				
	}

	@Override
	protected void generateExcel() throws Exception {
		Map  parameters = new HashMap();
		 parameters.put("loginOfficeName",loginOfficeName);
		 parameters.put("noOfYear", noOfYear);	
			parameters.put("loginUserName", loginUserName);	
		StringBuffer query = new StringBuffer("with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ "to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-?+1 start_year from dual),t2 as (select"
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ "then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1)"
				+ "select yr||'-'||(yr+1) blkyear from t2");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, noOfYear );			
		ResultSet rs = statement.executeQuery();
		StringBuffer query1 = new StringBuffer("select  rcn,asso_name,(select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+ "(select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name,"
				+ "nvl(to_char(reg_date,'dd-mm-yyyy'),'---') as REGISTRERED_ON,nvl(to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') as"
				+ " LAST_RENEWED_ON,nvl(to_char(valid_to,'dd-mm-yyyy'),'---') as EXPIRY_ON,");
		   char alphabet = 'A';
			while(rs.next()){
				parameters.put(alphabet+"1", rs.getString(1));
				query1.append("(Case when trunc(reg_date) >= to_date('01-APR-'||substr('"+rs.getString(1)+"',-4,4)) then '-' else (select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = i.rcn )end ) as "+alphabet+" ,");
				//query1.append("(select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = rcn ) as "+alphabet+" ,"); 
				alphabet++;
			}
			int count=0;
			count=rs.getRow();
			    if(count<6){
			    	for(int i = count; i<6;i++){
			    		query1.append("null as "+alphabet+" ,");
			    		parameters.put(alphabet+"1", "");
			    		alphabet++;
			    	}
			    }
   	    	rs.close();
			   query1.setLength(query1.length()-1);
			    query1.append("  from fc_india i where status <> 'D' and CURRENT_STATUS=0 and ASSOCIATION_TYPE is null and (valid_to is not null) and (trunc(valid_to) >= trunc(sysdate)) ");
			    ReportDataSource ds=new ReportDataSource(parameters, query1.toString(), connection, virtualizationMaxSize);
				 parameters.put("PRINTRECORD_AnnualReport", ds);
				  String tsPath = "/Reports/Annual-StatusReport.jrxml";
					String fileName = "AnnualStatusReport";
					GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void generateHTML() throws Exception {
		annualStatusDetailsList=getAnnualStatusReportRecord();	
		totalRecords=getTotalRecords();	
		
	}
	public List<AnnualStatusDetailsReport> getAnnualStatusReportRecord() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		StringBuffer query = new StringBuffer("with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ "to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-?+1 start_year from dual),t2 as (select"
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ "then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1)"
				+ "select yr||'-'||(yr+1) blkyear from t2");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, noOfYear );			
		ResultSet rs = statement.executeQuery();
		StringBuffer query1 = new StringBuffer("select  rcn,asso_name,(select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+ "(select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name,"
				+ "nvl(to_char(reg_date,'dd-mm-yyyy'),'---') as REGISTRERED_ON,nvl(to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') as"
				+ " LAST_RENEWED_ON,nvl(to_char(valid_to,'dd-mm-yyyy'),'---') as EXPIRY_ON,");
		char alphabet = 'A';
       	while(rs.next()){
       		query1.append("(Case when trunc(reg_date) >= to_date('01-APR-'||substr('"+rs.getString(1)+"',-4,4)) then '-' else (select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = i.rcn )end ) as "+alphabet+" ,");
		// query1.append("(select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = rcn ) as "+alphabet+",");
		alphabet++;
	}
	int count=0;
	count=rs.getRow();
	    if(count<6){
	    	for(int i = count; i<6;i++){
	    		query1.append("null as "+alphabet+",");
	    		alphabet++;
	    	}
	    }
		
	rs.close();
	    query1.setLength(query1.length()-1);
	    query1.append("  from fc_india i where status <> 'D' and CURRENT_STATUS=0  and ASSOCIATION_TYPE is null and (valid_to is not null) and (trunc(valid_to) >= trunc(sysdate)) ");
	   StringBuffer countQuery = new StringBuffer("select count(*) from("+query1+")");
	   StringBuffer query3 = new StringBuffer("WITH T1 AS("+query1+"),T2 AS(SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
	   PreparedStatement statement1 = connection.prepareStatement(countQuery.toString());		
		ResultSet rs1 = statement1.executeQuery();
		if(rs1.next()) {
			totalRecords = rs1.getString(1);
		}
		rs1.close();
		statement1.close();
		   Integer pageRequested = Integer.parseInt(pageNum);
			Integer pageSize = Integer.parseInt(recordsPerPage);
		      statement = connection.prepareStatement(query3.toString());
			if (pageNum == null || recordsPerPage == null) {

			}else {
				statement.setInt(1, (pageRequested - 1) * pageSize + pageSize);
				statement.setInt(2, (pageRequested - 1) * pageSize + 1);
			}
			ResultSet rs3 = statement.executeQuery();
					List<AnnualStatusDetailsReport> annualStatusDetails = new ArrayList<AnnualStatusDetailsReport>();
						while(rs3.next()) {
							annualStatusDetails.add(new AnnualStatusDetailsReport(rs3.getString(1), rs3.getString(2),rs3.getString(3),rs3.getString(4),rs3.getString(5),rs3.getString(6),rs3.getString(7),
									rs3.getString(8),rs3.getString(9),rs3.getString(10),rs3.getString(11),rs3.getString(12),rs3.getString(13))); 
							
						
						}
						rs3.close();
						statement.close();
						
			
		return annualStatusDetails;
	}
	 

	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateCSV() throws Exception {
         Map  parameters = new HashMap();
		 parameters.put("loginOfficeName",loginOfficeName);
		 parameters.put("noOfYear", noOfYear);	
			parameters.put("loginUserName", loginUserName);	
		StringBuffer query = new StringBuffer("with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ "to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-?+1 start_year from dual),t2 as (select"
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ "then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1)"
				+ "select yr||'-'||(yr+1) blkyear from t2");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, noOfYear );			
		ResultSet rs = statement.executeQuery();
		StringBuffer query1 = new StringBuffer("select  rcn,asso_name,(select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+ "(select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name,"
				+ "nvl(to_char(reg_date,'dd-mm-yyyy'),'---') as REGISTRERED_ON,nvl(to_char(LAST_RENEWED_ON,'dd-mm-yyyy'),'---') as"
				+ " LAST_RENEWED_ON,nvl(to_char(valid_to,'dd-mm-yyyy'),'---') as EXPIRY_ON,");
		   char alphabet = 'A';
			while(rs.next()){
				parameters.put(alphabet+"1", rs.getString(1));
				query1.append("(Case when trunc(reg_date) >= to_date('01-APR-'||substr('"+rs.getString(1)+"',-4,4)) then '-' else (select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = i.rcn )end ) as "+alphabet+" ,");
			 //query1.append("(Case when trunc(reg_date) >= to_date('01-APR-'||substr('"+rs.getString(1)+"',-4,4)) then '-' else (select (Case count(1) when 0 then 'No' else 'Yes' end) from fc_fc3_tally t where t.final_submit = 'Y' and t.blkyear = '"+rs.getString(1)+"' and t.rcn = rcn )end ) as "+alphabet+" ,"); 
				alphabet++;
			}
			int count=0;
			count=rs.getRow();
			    if(count<6){
			    	for(int i = count; i<6;i++){
			    		query1.append("null as "+alphabet+" ,");
			    		parameters.put(alphabet+"1", "");
			    		alphabet++;
			    	}
			    }
    	    	rs.close();
			   query1.setLength(query1.length()-1);
			    query1.append("  from fc_india i where status <> 'D' and CURRENT_STATUS=0 and ASSOCIATION_TYPE is null and (valid_to is not null) and (trunc(valid_to) >= trunc(sysdate)) ");
			    ReportDataSource ds=new ReportDataSource(parameters, query1.toString(), connection, virtualizationMaxSize);
				 parameters.put("PRINTRECORD_AnnualReport_CSV", ds);
				  String tsPath = "/Reports/Annual-StatusReport-Csv.jrxml";
					String fileName = "AnnualStatusReport.pdf";
					GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);

				
	
		
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			noOfYear=parameterMap.get("noOfYear").get(0);
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
		}
		
	}
	
	
	public List<String> getYaerList(String noOfYear) throws Exception{
		if(connection == null) {
			throw new Exception("Invalid connection");
		}	
		
		StringBuffer query = new StringBuffer("with t1 as (select (case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) then "
				+ "to_number(to_char(sysdate, 'YYYY'))-1 else to_number(to_char(sysdate, 'YYYY'))-2 end )-?+1 start_year from dual),t2 as (select"
				+ " (select start_year from t1) + LEVEL - 1 yr from dual connect by LEVEL<=(case when trunc(sysdate) >= to_date('01-APR-'||to_char(sysdate, 'YYYY')) "
				+ "then to_number(to_char(sysdate, 'YYYY'))-1  else to_number(to_char(sysdate, 'YYYY'))-2 end ) - ((select start_year from t1)) + 1)"
				+ "select yr||'-'||(yr+1) blkyear from t2");
		PreparedStatement statement = connection.prepareStatement(query.toString());
		statement.setString(1, noOfYear );	
		ResultSet rs = statement.executeQuery();
		List<String>  yearList = new ArrayList<String>();
		while(rs.next()) {			
			yearList.add(rs.getString(1));
		}
		return yearList;
	} 

	public List<AnnualStatusDetailsReport> getAnnualStatusDetailsList() {
		return annualStatusDetailsList;
	}

	public void setAnnualStatusDetailsList(
			List<AnnualStatusDetailsReport> annualStatusDetailsList) {
		this.annualStatusDetailsList = annualStatusDetailsList;
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

	public String getNoOfYear() {
		return noOfYear;
	}

	public void setNoOfYear(String noOfYear) {
		this.noOfYear = noOfYear;
	}

}
