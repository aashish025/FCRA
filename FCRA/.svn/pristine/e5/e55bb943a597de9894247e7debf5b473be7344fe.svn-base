package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.CountryStateReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.ServicesDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class CountryStateReportGenerator extends MISReportGenerator{
	private List<CountryStateReport> countryStateReport;
    private MultiValueMap<String, String> parameterMap;
    private String totalRecords;
      private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private List<String> selectBlockYearList;
	private List<String> selectCountryList;
	private List<String> selectStateList;
    private String reportDisplayType;
    private String loginOfficeName;
	private String loginUserName;
	private int virtualizationMaxSize = 200;

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

	public CountryStateReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		StateDao sdao= new StateDao(connection);
		CountryTypeDao cdao= new CountryTypeDao(connection);
		String stateList1="1=1";
		String stateList2="1=1";
		String selectCountryList1="1=1";
		String selectCountryList3="1=1";
	    String	selectCountryList2="1=1";
		
	     Map  parameters = new HashMap();
	    parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		 parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);	
		String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");  
	     if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(P3.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(stdist,1,2) IN ("+selectedStateList+")";
	    	 } 
	      if(!selectedCountryList.trim().equals("'ALL'")){
    	 selectCountryList1= "ctr_code in ("+selectedCountryList+")";
    	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
    	 selectCountryList3="P3.ctr_code in ("+selectedCountryList+")";
	      }
	     
	      parameters.put("selectedBlockYearList",selectedBlockYearList1);
	      parameters.put("selectedState",sdao.getState(selectedStateList));
	     parameters.put("selectedCountry", cdao.getCountry(selectedCountryList));
	     
		
          String  reportQuery = new String(" "
          		+ " SELECT P3.rcn as Registration_no, sum(P3.amount) as Amount, "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||    "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,  "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', ' "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) As  Bank_name_Address  "
					+ " FROM fc_fc3_part3 P3,fc_india FI,fc_bank B  "
					+ " WHERE "+stateList1+" AND blkyear='"+selectedBlockYearList1+"' "
					+ "AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList1+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn  "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin, "
					+ "FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ "B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION  "
					+ "Select P3.rcn,sum(P3.amount), "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', ' "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE    "
					+ " FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) "
					+ " As Asso_name_Address,('  ') As Bank_name_Address   "
					+ "FROM fc_fc3_part3 P3,fc_pp_india FI    "
					+ "WHERE "+ stateList1+" "
					+ " and P3.blkyear='"+selectedBlockYearList1+"' "
					+ "AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally   "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y') "
					+ "and P3.rcn=FI.rcn and "+selectCountryList3+"     "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city, "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin"
					+ " union "
					+ " SELECT P3.rcn, sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||  "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,    "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', '   "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) AS  Bank_name_Address  "
					+ " FROM fc_fc3_donor_wise P3,fc_india FI,fc_bank B, fc_fc3_donor fd   "
					+ " WHERE blkyear='"+selectedBlockYearList1+"'  "
					+ " AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND   "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList2+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn and p3.rcn=fd.rcn    "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin,  "
					+ " FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ " B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION   "
					+ "Select P3.rcn,sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '  "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE      "
					+ "FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) AS Asso_name_Address,('  ') AS Bank_name_Address  "
					+ "FROM fc_fc3_donor_wise P3,fc_pp_india FI, fc_fc3_donor fd  "
					+ " WHERE P3.blkyear='"+selectedBlockYearList1+"' "
					+ " AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally     "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')    "
					+ " and P3.rcn=FI.rcn and p3.rcn=fd.rcn and "+selectCountryList2+"    "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city,  "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin    ");
		

	     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			 parameters.put("PRINTRECORD_CountryState", ds);
			 
			 
			 String tsPath = "/Reports/Country-StateReport.jrxml";
				String fileName = "CountryStateReport.pdf";
				GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);

			
			}
			

	@Override
	protected void generateExcel() throws Exception {
		StateDao sdao= new StateDao(connection);
		CountryTypeDao cdao= new CountryTypeDao(connection);
		String stateList1="1=1";
		String stateList2="1=1";
		String selectCountryList1="1=1";
		String selectCountryList3="1=1";
	    String	selectCountryList2="1=1";
		
	     Map  parameters = new HashMap();
	    parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		 parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);	
		String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");  
	     if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(P3.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(stdist,1,2) IN ("+selectedStateList+")";
	    	 } 
	      if(!selectedCountryList.trim().equals("'ALL'")){
    	 selectCountryList1= "ctr_code in ("+selectedCountryList+")";
    	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
    	 selectCountryList3="P3.ctr_code in ("+selectedCountryList+")";
	      }
	     
	      parameters.put("selectedBlockYearList",selectedBlockYearList1);
	      parameters.put("selectedState",sdao.getState(selectedStateList));
	     parameters.put("selectedCountry", cdao.getCountry(selectedCountryList));
	     
		
          String  reportQuery = new String(" "
          		+ " SELECT P3.rcn as Registration_no, sum(P3.amount) as Amount, "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||    "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,  "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', ' "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) As  Bank_name_Address  "
					+ " FROM fc_fc3_part3 P3,fc_india FI,fc_bank B  "
					+ " WHERE "+stateList1+" AND blkyear='"+selectedBlockYearList1+"' "
					+ "AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList1+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn  "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin, "
					+ "FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ "B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION  "
					+ "Select P3.rcn,sum(P3.amount), "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', ' "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE    "
					+ " FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) "
					+ " As Asso_name_Address,('  ') As Bank_name_Address   "
					+ "FROM fc_fc3_part3 P3,fc_pp_india FI    "
					+ "WHERE "+ stateList1+" "
					+ " and P3.blkyear='"+selectedBlockYearList1+"' "
					+ "AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally   "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y') "
					+ "and P3.rcn=FI.rcn and "+selectCountryList3+"     "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city, "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin"
					+ " union "
					+ " SELECT P3.rcn, sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||  "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,    "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', '   "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) AS  Bank_name_Address  "
					+ " FROM fc_fc3_donor_wise P3,fc_india FI,fc_bank B, fc_fc3_donor fd   "
					+ " WHERE blkyear='"+selectedBlockYearList1+"'  "
					+ " AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND   "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList2+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn and p3.rcn=fd.rcn    "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin,  "
					+ " FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ " B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION   "
					+ "Select P3.rcn,sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '  "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE      "
					+ "FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) AS Asso_name_Address,('  ') AS Bank_name_Address  "
					+ "FROM fc_fc3_donor_wise P3,fc_pp_india FI, fc_fc3_donor fd  "
					+ " WHERE P3.blkyear='"+selectedBlockYearList1+"' "
					+ " AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally     "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')    "
					+ " and P3.rcn=FI.rcn and p3.rcn=fd.rcn and "+selectCountryList2+"    "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city,  "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin    ");
		

	     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			 parameters.put("PRINTRECORD_CountryState", ds);
			 
			 
			 String tsPath = "/Reports/Country-Statexls.jrxml";
				String fileName = "CountryStateReport.xls";
				GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);


	}

	@Override
	protected void generateHTML() throws Exception {
        countryStateReport=getCountryStateReportHtml();
	    totalRecords=getTotalRecords();
	}

	public List<CountryStateReport> getCountryStateReportHtml() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		
		
		String stateList1="1=1";
		String stateList2="1=1";
		String selectCountryList1="1=1";
		String selectCountryList3="1=1";
	    String	selectCountryList2="1=1";
		 String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		 String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	     String lastblockYearList=selectedBlockYearList1.trim().substring(selectedBlockYearList1.length()-4, selectedBlockYearList1.length()); 
	       if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(P3.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(stdist,1,2) IN ("+selectedStateList+")";
	    	 }
	   // String selectedCountryList=selectCountryList.toString().replace("[", "").replace("]", "").replace(", ",",");
	       String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','"); 
	      if(!selectedCountryList.trim().equals("'ALL'")){
    	 selectCountryList1= "ctr_code in ("+selectedCountryList+")";
    	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
    	 selectCountryList3="P3.ctr_code in ("+selectedCountryList+")";
	      }
	     
		
		
		
		
		
		
			StringBuffer query = new StringBuffer("  SELECT P3.rcn, sum(P3.amount), "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||    "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,  "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', ' "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) As  Bank_name_Address  "
					+ " FROM fc_fc3_part3 P3,fc_india FI,fc_bank B  "
					+ " WHERE "+stateList1+" AND blkyear='"+selectedBlockYearList1+"' "
					+ "AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList1+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn  "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin, "
					+ "FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ "B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION  "
					+ "Select P3.rcn,sum(P3.amount), "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', ' "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE    "
					+ " FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) "
					+ " As Asso_name_Address,('  ') As Bank_name_Address   "
					+ "FROM fc_fc3_part3 P3,fc_pp_india FI    "
					+ "WHERE "+ stateList1+" "
					+ " and P3.blkyear='"+selectedBlockYearList1+"' "
					+ "AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally   "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y') "
					+ "and P3.rcn=FI.rcn and "+selectCountryList3+"     "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city, "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin"
					+ " union "
					+ " SELECT P3.rcn, sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||  "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,    "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', '   "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) AS  Bank_name_Address  "
					+ " FROM fc_fc3_donor_wise P3,fc_india FI,fc_bank B, fc_fc3_donor fd   "
					+ " WHERE blkyear='"+selectedBlockYearList1+"'  "
					+ " AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND   "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList2+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn and p3.rcn=fd.rcn    "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin,  "
					+ " FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ " B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION   "
					+ "Select P3.rcn,sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '  "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE      "
					+ "FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) AS Asso_name_Address,('  ') AS Bank_name_Address  "
					+ "FROM fc_fc3_donor_wise P3,fc_pp_india FI, fc_fc3_donor fd  "
					+ " WHERE P3.blkyear='"+selectedBlockYearList1+"' "
					+ " AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally     "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')    "
					+ " and P3.rcn=FI.rcn and p3.rcn=fd.rcn and "+selectCountryList2+"    "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city,  "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin  ");
                    StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
					PreparedStatement statement = connection.prepareStatement(countQuery.toString());		
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
								 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
								 statement.setInt(2, (pageRequested-1) * pageSize + 1);
								 }
							
								 rs = statement.executeQuery();
								 List<CountryStateReport> countrystateReportList = new ArrayList<CountryStateReport>();
									while(rs.next()) {
										countrystateReportList.add(new CountryStateReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5))); 
										
									
								
									
									}
							return countrystateReportList;
	}
	
	
	
	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			
			if(sortColumn != null && sortColumn.equals("") == false) {
				
			 if(sortColumn.equals("amount")) {
					orderbyClause.append(" ORDER BY AMOUNT");
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

	
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);//("reportType");
			reportFormat = parameterMap.get("reportFormat").get(0);//("reportFormat");
			if(reportFormat.equals("3")){
		    pageNum=parameterMap.get("pageNum").get(0);
			recordsPerPage=parameterMap.get("recordsPerPage").get(0);
			}
			selectBlockYearList=parameterMap.get("blockYear-List");
			selectCountryList=parameterMap.get("country-type");
			selectStateList=parameterMap.get("state-List");
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
		
			
			
		}
	}

	public List<CountryStateReport> getCountryStateReport() {
		return countryStateReport;
	}

	public void setCountryStateReport(List<CountryStateReport> countryStateReport) {
		this.countryStateReport = countryStateReport;
	}

	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
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

	public List<String> getSelectBlockYearList() {
		return selectBlockYearList;
	}

	public void setSelectBlockYearList(List<String> selectBlockYearList) {
		this.selectBlockYearList = selectBlockYearList;
	}

	public List<String> getSelectCountryList() {
		return selectCountryList;
	}

	public void setSelectCountryList(List<String> selectCountryList) {
		this.selectCountryList = selectCountryList;
	}

	public String getReportDisplayType() {
		return reportDisplayType;
	}

	public void setReportDisplayType(String reportDisplayType) {
		this.reportDisplayType = reportDisplayType;
	}

	public List<String> getSelectStateList() {
		return selectStateList;
	}

	public void setSelectStateList(List<String> selectStateList) {
		this.selectStateList = selectStateList;
	}

	@Override
	protected void generateCSV() throws Exception {

		StateDao sdao= new StateDao(connection);
		CountryTypeDao cdao= new CountryTypeDao(connection);
		String stateList1="1=1";
		String stateList2="1=1";
		String selectCountryList1="1=1";
		String selectCountryList3="1=1";
	    String	selectCountryList2="1=1";
		
	     Map  parameters = new HashMap();
	    parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		 parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);	
		String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	     String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");  
	     if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(P3.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(stdist,1,2) IN ("+selectedStateList+")";
	    	 } 
	      if(!selectedCountryList.trim().equals("'ALL'")){
    	 selectCountryList1= "ctr_code in ("+selectedCountryList+")";
    	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
    	 selectCountryList3="P3.ctr_code in ("+selectedCountryList+")";
	      }
	     
	      parameters.put("selectedBlockYearList",selectedBlockYearList1);
	      parameters.put("selectedState",sdao.getState(selectedStateList));
	     parameters.put("selectedCountry", cdao.getCountry(selectedCountryList));
	     
		
          String  reportQuery = new String(" "
          		+ " SELECT P3.rcn as Registration_no, sum(P3.amount) as Amount, "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||    "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,  "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', ' "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) As  Bank_name_Address  "
					+ " FROM fc_fc3_part3 P3,fc_india FI,fc_bank B  "
					+ " WHERE "+stateList1+" AND blkyear='"+selectedBlockYearList1+"' "
					+ "AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList1+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn  "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin, "
					+ "FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ "B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION  "
					+ "Select P3.rcn,sum(P3.amount), "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', ' "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE    "
					+ " FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) "
					+ " As Asso_name_Address,('  ') As Bank_name_Address   "
					+ "FROM fc_fc3_part3 P3,fc_pp_india FI    "
					+ "WHERE "+ stateList1+" "
					+ " and P3.blkyear='"+selectedBlockYearList1+"' "
					+ "AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally   "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y') "
					+ "and P3.rcn=FI.rcn and "+selectCountryList3+"     "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city, "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin"
					+ " union "
					+ " SELECT P3.rcn, sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '||  "
					+ " FI.asso_town_city ||' - '|| FI.asso_pin  ELSE FI.add1||', '||FI.add2||', '   "
					+ " ||FI.add3||' - '||FI.asso_pin END)) AS  Asso_name_Address,    "
					+ " ((select BANK_NAME from tm_banks where BANK_CODE = B.bank_name)||', "
					+ " '||(CASE WHEN B.new_old ='N' THEN  B.bank_address ||', '   "
					+ " || B.bank_town_city ELSE B.bank_add1||', ' ||B.bank_add2||', '||  "
					+ " B.bank_add3 END) ||' A/c no: '||B.account_no) AS  Bank_name_Address  "
					+ " FROM fc_fc3_donor_wise P3,fc_india FI,fc_bank B, fc_fc3_donor fd   "
					+ " WHERE blkyear='"+selectedBlockYearList1+"'  "
					+ " AND  P3.rcn IN(SELECT RCN FROM fc_fc3_tally WHERE substr(rcn,-1,1)='R' "
					+ " AND   "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')  "
					+ " and "+selectCountryList2+" AND  P3.rcn=FI.rcn and P3.rcn=B.rcn and p3.rcn=fd.rcn    "
					+ " group by P3.rcn,FI.asso_name,FI.asso_address,FI.asso_town_city,FI.asso_pin,  "
					+ " FI.add1,FI.add2,FI.add3,FI.asso_pin,B.bank_name,B.bank_address,B.bank_town_city, "
					+ " B.bank_add1,B.bank_add2,B.bank_add3,B.account_no,FI.new_old,B.new_old  "
					+ "UNION   "
					+ "Select P3.rcn,sum(P3.amount),  "
					+ "(FI.asso_name||', '||(CASE WHEN FI.new_old ='N' THEN FI.asso_address ||', '  "
					+ "|| FI.asso_town_city ||' - '|| FI.asso_pin ELSE      "
					+ "FI.add1||', '||FI.add2||', '||FI.add3||' - '||FI.asso_pin END)) AS Asso_name_Address,('  ') AS Bank_name_Address  "
					+ "FROM fc_fc3_donor_wise P3,fc_pp_india FI, fc_fc3_donor fd  "
					+ " WHERE P3.blkyear='"+selectedBlockYearList1+"' "
					+ " AND P3.rcn IN(SELECT RCN FROM fc_fc3_tally     "
					+ "WHERE substr(rcn,-1,1)='P' AND  "
					+ "  "+stateList2+" AND blkyear='"+selectedBlockYearList1+"' AND final_submit='Y')    "
					+ " and P3.rcn=FI.rcn and p3.rcn=fd.rcn and "+selectCountryList2+"    "
					+ " group by P3.rcn,FI.asso_name,FI.new_old,FI.asso_address,FI.asso_town_city,  "
					+ " FI.asso_pin, FI.add1,FI.add2,FI.add3,FI.asso_pin    ");
		

	     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			 parameters.put("PRINTRECORD_CountryState", ds);
			 
			 
			 String tsPath = "/Reports/Country-State-Csv.jrxml";
				String fileName = "CountryStateReport";
				GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);


	
		
	}

}
