package dao.reports.concretereports;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.RegistrationExpiryReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.KVPair;
import utilities.ReportDataSource;
import dao.reports.MISReportGenerator;

public class RegistrationExpiryReportGenerator  extends MISReportGenerator{
	private List<RegistrationExpiryReport> registrationExpiryDetailsList;
	private int virtualizationMaxSize = 200;
	 private MultiValueMap<String, String> parameterMap;
		private String pageNum;
		private String recordsPerPage;
		private String totalRecords;
		private String blockyear;
		private String associationStatus;
		private String loginOfficeName;
		private String loginUserName;
		private String fromDate;
		private String toDate;

	public RegistrationExpiryReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	public List<KVPair<String, String>> dateList() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}				
		StringBuffer query = new StringBuffer("select to_char(sysdate, 'dd-mm-yyyy'), to_char(add_months(sysdate, 6), 'dd-mm-yyyy') from dual");
		PreparedStatement statement = connection.prepareStatement(query.toString());				
		ResultSet rs = statement.executeQuery();
		List<KVPair<String, String>>  countryTypeList = new ArrayList<KVPair<String, String>>();
		while(rs.next()) {			
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(1), rs.getString(2));
			countryTypeList.add(temp);			
		}
		return countryTypeList;
	}
	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("loginUserName", loginUserName);
		parameters.put("loginOfficeName",loginOfficeName);
		String	expiryfromDate=fromDate.toString().replace("[", "").replace("]", "");
		String	expirytoDate=toDate.toString().replace("[", "").replace("]", "");
		String reportQuery = null;
		reportQuery=("with t1 as ("
				+ " select a.rcn,asso_name,(select sname from tm_state where scode = substr(stdist,1,2) )as state,"
				+ "(select distname from tm_district where  distcode = substr(stdist,-3,3)  and scode =substr(stdist,1,2)  )as district,"
				+ "(case when new_old='N' then asso_address || ','|| asso_town_city || asso_pin else add1||', '|| add2 || ','||add3 || '-'|| asso_pin end) as AsoAddress,"
				+ "current_status,valid_to from FC_INDIA a where trunc(a.VALID_TO) between trunc(to_date('"+expiryfromDate+"', 'dd-mm-yyyy'))"
				+ "and trunc(to_date('"+expirytoDate+"', 'dd-mm-yyyy')) and a.CURRENT_STATUS=0 and a.STATUS!='D'), t2 as"
				+ "(select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,"
				+ "(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress from t1 nw left join fc_asso_details i on nw.rcn=i.rcn )"
				+ "select i.rcn,i.asso_name, to_char(i.VALID_TO,'dd-mm-yyyy') expiring_on, b.account_no,"
				+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
				+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin "
				+ "else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,"
				+ "(AsoAddress || ', '||district||', '||state) assoAddress "
				+ "from t2 i left join (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn order by VALID_TO ");
		parameters.put("registrationExpiry","Registration Expiry Detail");
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
		parameters.put("RegistrationExpiry", ds);  		
        String tsPath = "/Reports/RegistrationExpiryDetail.jrxml";
		String fileName = "RegistrationExpiry-Report";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,fileName);    	
	}
	
	@Override
	protected void generateExcel() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("loginUserName", loginUserName);
		parameters.put("loginOfficeName",loginOfficeName);
		String	expiryfromDate=fromDate.toString().replace("[", "").replace("]", "");
		String	expirytoDate=toDate.toString().replace("[", "").replace("]", "");
		String reportQuery = null;
		reportQuery=("with t1 as ("
				+ " select a.rcn,asso_name,(select sname from tm_state where scode = substr(stdist,1,2) )as state,"
				+ "(select distname from tm_district where  distcode = substr(stdist,-3,3)  and scode =substr(stdist,1,2)  )as district,"
				+ "(case when new_old='N' then asso_address || ','|| asso_town_city || asso_pin else add1||', '|| add2 || ','||add3 || '-'|| asso_pin end) as AsoAddress,"
				+ "current_status,valid_to from FC_INDIA a where trunc(a.VALID_TO) between trunc(to_date('"+expiryfromDate+"', 'dd-mm-yyyy'))"
				+ "and trunc(to_date('"+expirytoDate+"', 'dd-mm-yyyy')) and a.CURRENT_STATUS=0 and a.STATUS!='D'), t2 as"
				+ "(select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,"
				+ "(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress from t1 nw left join fc_asso_details i on nw.rcn=i.rcn )"
				+ "select i.rcn,i.asso_name, to_char(i.VALID_TO,'dd-mm-yyyy') expiring_on, b.account_no,"
				+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
				+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin "
				+ "else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,"
				+ "(AsoAddress || ', '||district||', '||state) assoAddress "
				+ "from t2 i left join (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn order by VALID_TO ");
		parameters.put("registrationExpiry","Registration Expiry Detail");
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
		parameters.put("RegistrationExpiry", ds);  		
        String tsPath = "/Reports/RegistrationExpiryDetailExcel.jrxml";
		String fileName = "RegistrationExpiry-Report";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName); 	
	}

	@Override
	protected void generateHTML() throws Exception {
		registrationExpiryDetailsList = RegistrationExpiryDetails();
		totalRecords=getTotalRecords();
	}

	private List<RegistrationExpiryReport>RegistrationExpiryDetails() throws Exception {
		List<RegistrationExpiryReport> registrationDetailsList = new ArrayList<RegistrationExpiryReport>();
		StringBuffer str = null;
		String	expiryfromDate=fromDate.toString().replace("[", "").replace("]", "");
		String	expirytoDate=toDate.toString().replace("[", "").replace("]", "");
		str = new StringBuffer("with t1 as ("
				+ " select a.rcn,asso_name,(select sname from tm_state where scode = substr(stdist,1,2) )as state,"
				+ "(select distname from tm_district where  distcode = substr(stdist,-3,3)  and scode =substr(stdist,1,2)  )as district,"
				+ "(case when new_old='N' then asso_address || ','|| asso_town_city || asso_pin else add1||', '|| add2 || ','||add3 || '-'|| asso_pin end) as AsoAddress,"
				+ "current_status,valid_to from FC_INDIA a where trunc(a.VALID_TO) between trunc(to_date(?, 'dd-mm-yyyy'))"
				+ "and trunc(to_date(?, 'dd-mm-yyyy')) and a.CURRENT_STATUS=0 and a.STATUS!='D'), t2 as"
				+ "(select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,"
				+ "(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress from t1 nw left join fc_asso_details i on nw.rcn=i.rcn )"
				+ "select i.rcn,i.asso_name, to_char(i.VALID_TO,'dd-mm-yyyy') expiring_on, b.account_no,"
				+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
				+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin "
				+ "else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,"
				+ "(AsoAddress || ', '||district||', '||state) assoAddress "
				+ "from t2 i left join (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn order by VALID_TO ");
		StringBuffer countquery = new StringBuffer("SELECT COUNT(1) FROM ("+str+")");
		PreparedStatement statement = connection.prepareStatement(countquery.toString());
		statement.setString(1, expiryfromDate);
		statement.setString(2, expirytoDate);
		 ResultSet rs = statement.executeQuery();
		  if(rs.next())
		     { 
			  totalRecords = rs.getString(1);
		      } 
		  rs.close();
		  statement.close();
		  Integer pageRequested = Integer.parseInt(pageNum);
			 Integer pageSize = Integer.parseInt(recordsPerPage);
			 StringBuffer query = new StringBuffer("with t1 as"
			 		+ "( select a.rcn,asso_name,(select sname from tm_state where scode = substr(stdist,1,2) )as state,"
			 		+ "(select distname from tm_district where  distcode = substr(stdist,-3,3)  and scode =substr(stdist,1,2)  )as district,"
			 		+ "(case when new_old='N' then asso_address || ','|| asso_town_city || asso_pin else add1||', '|| add2 || ','||add3 || '-'|| asso_pin end) as AsoAddress"
			 		+ ",current_status,valid_to from FC_INDIA a "
			 		+ "where trunc(a.VALID_TO) between trunc(to_date(?, 'dd-mm-yyyy'))and trunc(to_date(?, 'dd-mm-yyyy'))"
			 		+ "and a.CURRENT_STATUS=0 and a.STATUS!='D'),"
			 		+ "t2 as(select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress "
			 		+ "from t1 nw left join fc_asso_details i on nw.rcn=i.rcn ),"
			 		+ "t3 as(select i.rcn,i.asso_name, to_char(i.VALID_TO,'dd-mm-yyyy') expiring_on,"
			 		+ " b.account_no,(select ba.bank_name "
			 		+ "from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
			 		+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,"
			 		+ "(AsoAddress || ', '||district||', '||state) assoAddress "
			 		+ "from t2 i left join (select * from fc_bank "
			 		+ "where status = 'Y') b on i.rcn = b.rcn order by VALID_TO ),"
			 		+ "T4 AS (SELECT T3.*, ROWNUM RN FROM T3 WHERE ROWNUM<=?) SELECT * FROM T4 WHERE RN>=?");
			 statement = connection.prepareStatement(query.toString());
			 if(pageNum == null || recordsPerPage == null) {
					
			 }
			 else {
				 statement.setString(1, expiryfromDate);
				 statement.setString(2, expirytoDate);
				 statement.setInt(3, (pageRequested - 1) * pageSize + pageSize);
				 statement.setInt(4, (pageRequested - 1) * pageSize + 1);
			 }
			 rs = statement.executeQuery();
			 while(rs.next()) {
				 registrationDetailsList.add(new RegistrationExpiryReport(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
			 }
			 rs.close();
			  statement.close();
		return registrationDetailsList;
	
		
	}
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateCSV() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("loginUserName", loginUserName);
		parameters.put("loginOfficeName",loginOfficeName);
		String	expiryfromDate=fromDate.toString().replace("[", "").replace("]", "");
		String	expirytoDate=toDate.toString().replace("[", "").replace("]", "");
		String reportQuery = null;
		reportQuery=("with t1 as ("
				+ " select a.rcn,asso_name,(select sname from tm_state where scode = substr(stdist,1,2) )as state,"
				+ "(select distname from tm_district where  distcode = substr(stdist,-3,3)  and scode =substr(stdist,1,2)  )as district,"
				+ "(case when new_old='N' then asso_address || ','|| asso_town_city || asso_pin else add1||', '|| add2 || ','||add3 || '-'|| asso_pin end) as AsoAddress,"
				+ "current_status,valid_to from FC_INDIA a where trunc(a.VALID_TO) between trunc(to_date('"+expiryfromDate+"', 'dd-mm-yyyy'))"
				+ "and trunc(to_date('"+expirytoDate+"', 'dd-mm-yyyy')) and a.CURRENT_STATUS=0 and a.STATUS!='D'), t2 as"
				+ "(select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,"
				+ "(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress from t1 nw left join fc_asso_details i on nw.rcn=i.rcn )"
				+ "select i.rcn,i.asso_name, to_char(i.VALID_TO,'dd-mm-yyyy') expiring_on, b.account_no,"
				+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
				+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin "
				+ "else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,"
				+ "(AsoAddress || ', '||district||', '||state) assoAddress "
				+ "from t2 i left join (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn order by VALID_TO ");
		parameters.put("registrationExpiry","Registration Expiry Detail");
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
		parameters.put("RegistrationExpiry", ds);  		
        String tsPath = "/Reports/RegistrationExpiryDetailCsv.jrxml";
		String fileName = "RegistrationExpiry-Report";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {if(parameterMap != null) {
				reportType = parameterMap.get("reportType").get(0);
				reportFormat = parameterMap.get("reportFormat").get(0);
				loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			    loginUserName=parameterMap.get("loginUserName").get(0);
			    fromDate=parameterMap.get("from-date").toString();
				toDate=parameterMap.get("to-date").toString();
				if(reportFormat.equals("3")){
					pageNum=parameterMap.get("pageNum").get(0);
					recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
				}
				
			}
	}
	public List<RegistrationExpiryReport> getRegistrationExpiryDetailsList() {
		return registrationExpiryDetailsList;
	}
	public void setRegistrationExpiryDetailsList(
			List<RegistrationExpiryReport> registrationExpiryDetailsList) {
		this.registrationExpiryDetailsList = registrationExpiryDetailsList;
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
	public String getBlockyear() {
		return blockyear;
	}
	public void setBlockyear(String blockyear) {
		this.blockyear = blockyear;
	}
	public String getAssociationStatus() {
		return associationStatus;
	}
	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
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



}
