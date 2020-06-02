package dao.reports.concretereports;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;













import models.reports.PurposeWiseReport;
import models.reports.ReturnFiledReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class PurposeWiseReportGenerator extends MISReportGenerator  {
	
	private List<PurposeWiseReport> purposeWiseReport;
    private MultiValueMap<String, String> parameterMap;
    private String totalRecords;
      private String pageNum;
	private String recordsPerPage;
	private String sortColumn;
	private String sortOrder;
	private List<String> selectBlockYearList;
	private List<String> selectCountryList;
	private List<String> selectStateList;
	private List<String> selectPurposeList;
	 private String loginOfficeName;
		private String loginUserName;
	private String reportDisplayType;
	private int virtualizationMaxSize = 200;



	


	public PurposeWiseReportGenerator(Connection connection) {
		super(connection);
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
		String  PurposeList1="1=1";
		String BlockYearList1="1=1";
	     Map  parameters = new HashMap();
	    parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		 parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);	
	
		 String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		 String lastblockYearList=selectedBlockYearList1.trim().substring(selectedBlockYearList1.length()-4, selectedBlockYearList1.length());
	     String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	     String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");  
	    String selectedPurposeList=selectPurposeList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
	    if(!selectedBlockYearList1.trim().equals("'ALL'")){
	    	 BlockYearList1= " fp3.blkyear IN ("+selectedBlockYearList1+")";
	    	 }
	   
	    if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(fi.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(fpi.stdist,1,2) IN ("+selectedStateList+")";
	    	 }
	     
	      if(!selectedCountryList.trim().equals("'ALL'")){
 	 selectCountryList1= "fp3.ctr_code in ("+selectedCountryList+")";
 	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
	      }

			if(!selectedPurposeList.trim().equals("ALL")){
				PurposeList1= "pcode in("+selectedPurposeList+")";
			}
			
	      parameters.put("selectedBlockYearList",selectedBlockYearList1);
	      parameters.put("selectedState",sdao.getState(selectedStateList));
	     parameters.put("selectedCountry", cdao.getCountry(selectedCountryList));
	     parameters.put("selectedPurposeList", getPurposeName(selectedPurposeList));
	 	
		
          String  reportQuery = new String("SELECT blkyear, PNAME,ROUND(SUM(AMOUNT),0) AS AMOUNT, SUM(RCNCOUNT) AS RCNCOUNT  FROM ("
       			+ "  SELECT fp3.blkyear, fp.pname AS pname, nvl(sum(fp3.amount),0) AS Amount,  "
       			+ " count(distinct fp3.rcn) as rcncount, fp.pcode as pcode   "
       			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_india fi   "
       			+ "  WHERE fp3.rcn=fi.rcn       "
       			+ "   and "+BlockYearList1+"  "
       			+ " and "+selectCountryList1+"  "
       			+ " AND fp3.pcode=fp.pcode     "
       			+ " and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
       			+ " and fp3.rcn=ft.rcn     "
       			+ "  AND ft.blkyear=fp3.blkyear  "
       			+ "  AND substr(ft.rcn,-1,1)='R'     "
       			+ "  AND ft.final_submit='Y' "
       			+ " AND "+stateList1+"     "
       			+ " GROUP BY fp3.blkyear, fp.pcode, fp.pname  "
       			+ " UNION ALL       "
       			+ "select fp3.blkyear, fp.pname as pname, nvl(sum(fp3.amount),0) as Amount,     "
       			+ "count(distinct fp3.rcn) as rcncount, fp.pcode as pcode       "
       			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_pp_india fpi       "
       			+ " WHERE fp3.rcn=fpi.rcn       "
       			+ "  and "+BlockYearList1+"    "
       			+ "  and "+selectCountryList1+"      "
       			+ "  AND fp3.pcode=fp.pcode   "
       			//+ " AND fpi.blkyear=fp3.blkyear  "
       			+ "and fp3.rcn=ft.rcn       "
       			+ " AND ft.blkyear=fp3.blkyear "
       			+ "  and substr(ft.rcn,-1,1)='P'    "
       			+ " AND ft.final_submit='Y' "
       			+ " AND "+stateList2+"      "
       			+ " group by fp3.blkyear, fp.pcode, fp.pname   "
       			+ "union all   "
       			+ "SELECT fp3.blkyear, fp.PURPOSE_NAME AS pname, nvl(sum(fp3.amount_received),0) AS Amount,     "
       			+ "count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode   "
       			+ " from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_india fi       "
       			+ " WHERE fp3.rcn=fi.rcn      "
       			+ "  and "+BlockYearList1+"  "
       			+ " and "+selectCountryList2+"      "
       			+ "  AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
       			+ " and (reg_date is null or trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (fi.status <> 'D') "
       			+ "   and fp3.rcn=ft.rcn      "
       			+ "  AND substr(ft.rcn,-1,1)='R'      "
       			+ "  AND ft.final_submit='Y' "
       			+ "  AND "+stateList1+"  "
       			//+ "  AND fi.rcn=fd.rcn  "
       			+ "AND ft.blkyear=fp3.blkyear   "
       			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME   "
       			+ "  UNION ALL       "
       			+ " select fp3.blkyear, fp.PURPOSE_NAME as pname, nvl(sum(fp3.amount_received),0) as Amount, "
       			+ "  count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode       "
       			+ "from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_pp_india fpi      "
       			+ "  WHERE fp3.rcn=fpi.rcn  "
       			+ " and "+BlockYearList1+"     "
       			+ " and "+selectCountryList2+"   "
       			+ " AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
       			//+ " AND fpi.blkyear=fp3.blkyear  "
       			+ " and fp3.rcn=ft.rcn      "
       			+ " AND ft.blkyear=fp3.blkyear "
       			+ "  and substr(ft.rcn,-1,1)='P'     "
       			+ " AND ft.final_submit='Y' "
       			+ " AND "+stateList2+" "
       			//+ "  AND fpi.rcn=fd.rcn  "
       			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME)  "
       			+ "ABC where "+PurposeList1+"  GROUP BY blkyear,ABC.PCODE,ABC.PNAME ORDER BY blkyear, AMOUNT DESC");
		

	     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			 parameters.put("PRINTRECORD_PURPOSE_WISE", ds);
			 
			 
			 String tsPath = "/Reports/Purpose-WiseReport.jrxml";
				String fileName = "PurposeWiseReport.pdf";
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
		String  PurposeList1="1=1";
		String BlockYearList1="1=1";
		
	     Map  parameters = new HashMap();
	    parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		 parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);	
		 String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		 String lastblockYearList=selectedBlockYearList1.trim().substring(selectedBlockYearList1.length()-4, selectedBlockYearList1.length());
	     String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	     String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");  
	    String selectedPurposeList=selectPurposeList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		
	    if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(fi.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(fpi.stdist,1,2) IN ("+selectedStateList+")";
	    	 }
	    if(!selectedBlockYearList1.trim().equals("'ALL'")){
	    	 BlockYearList1= " fp3.blkyear IN ("+selectedBlockYearList1+")";
	    	 }
	   
	     
	      if(!selectedCountryList.trim().equals("'ALL'")){
 	 selectCountryList1= "fp3.ctr_code in ("+selectedCountryList+")";
 	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
	      }

			if(!selectedPurposeList.trim().equals("ALL")){
				PurposeList1= "pcode in("+selectedPurposeList+")";
			}
			
	      parameters.put("selectedBlockYearList",selectedBlockYearList1);
	      parameters.put("selectedState",sdao.getState(selectedStateList));
	     parameters.put("selectedCountry", cdao.getCountry(selectedCountryList));
	     parameters.put("selectedPurposeList", getPurposeName(selectedPurposeList));
	     
		
	     String  reportQuery = new String("SELECT blkyear, PNAME,ROUND(SUM(AMOUNT),0) AS AMOUNT, SUM(RCNCOUNT) AS RCNCOUNT  FROM ("
	     			+ "  SELECT fp3.blkyear, fp.pname AS pname, nvl(sum(fp3.amount),0) AS Amount,  "
	     			+ " count(distinct fp3.rcn) as rcncount, fp.pcode as pcode   "
	     			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_india fi   "
	     			+ "  WHERE fp3.rcn=fi.rcn       "
	     			+ "   and "+BlockYearList1+"  "
	     			+ " and "+selectCountryList1+"  "
	     			+ " AND fp3.pcode=fp.pcode     "
	     			+ " and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
	     			+ " and fp3.rcn=ft.rcn     "
	     			+ "  AND ft.blkyear=fp3.blkyear  "
	     			+ "  AND substr(ft.rcn,-1,1)='R'     "
	     			+ "  AND ft.final_submit='Y' "
	     			+ " AND "+stateList1+"     "
	     			+ " GROUP BY fp3.blkyear, fp.pcode, fp.pname  "
	     			+ " UNION ALL       "
	     			+ "select fp3.blkyear, fp.pname as pname, nvl(sum(fp3.amount),0) as Amount,     "
	     			+ "count(distinct fp3.rcn) as rcncount, fp.pcode as pcode       "
	     			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_pp_india fpi       "
	     			+ " WHERE fp3.rcn=fpi.rcn       "
	     			+ "  and "+BlockYearList1+"    "
	     			+ "  and "+selectCountryList1+"      "
	     			+ "  AND fp3.pcode=fp.pcode   "
	     			//+ " AND fpi.blkyear=fp3.blkyear  "
	     			+ "and fp3.rcn=ft.rcn       "
	     			+ " AND ft.blkyear=fp3.blkyear "
	     			+ "  and substr(ft.rcn,-1,1)='P'    "
	     			+ " AND ft.final_submit='Y' "
	     			+ " AND "+stateList2+"      "
	     			+ " group by fp3.blkyear, fp.pcode, fp.pname   "
	     			+ "union all   "
	     			+ "SELECT fp3.blkyear, fp.PURPOSE_NAME AS pname, nvl(sum(fp3.amount_received),0) AS Amount,     "
	     			+ "count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode   "
	     			+ " from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_india fi       "
	     			+ " WHERE fp3.rcn=fi.rcn      "
	     			+ "  and "+BlockYearList1+"  "
	     			+ " and "+selectCountryList2+"      "
	     			+ "  AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
	     			+ " and (reg_date is null or trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (fi.status <> 'D') "
	     			+ "   and fp3.rcn=ft.rcn      "
	     			+ "  AND substr(ft.rcn,-1,1)='R'      "
	     			+ "  AND ft.final_submit='Y' "
	     			+ "  AND "+stateList1+"  "
	     			//+ "  AND fi.rcn=fd.rcn  "
	     			+ "AND ft.blkyear=fp3.blkyear   "
	     			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME   "
	     			+ "  UNION ALL       "
	     			+ " select fp3.blkyear, fp.PURPOSE_NAME as pname, nvl(sum(fp3.amount_received),0) as Amount, "
	     			+ "  count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode       "
	     			+ "from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_pp_india fpi      "
	     			+ "  WHERE fp3.rcn=fpi.rcn  "
	     			+ " and "+BlockYearList1+"     "
	     			+ " and "+selectCountryList2+"   "
	     			+ " AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
	     			//+ " AND fpi.blkyear=fp3.blkyear  "
	     			+ " and fp3.rcn=ft.rcn      "
	     			+ " AND ft.blkyear=fp3.blkyear "
	     			+ "  and substr(ft.rcn,-1,1)='P'     "
	     			+ " AND ft.final_submit='Y' "
	     			+ " AND "+stateList2+" "
	     			//+ "  AND fpi.rcn=fd.rcn  "
	     			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME)  "
	     			+ "ABC where "+PurposeList1+"  GROUP BY blkyear,ABC.PCODE,ABC.PNAME ORDER BY blkyear, AMOUNT DESC");
		

	     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			 parameters.put("PRINTRECORD_PURPOSE_WISE", ds);
			 
			 String tsPath = "/Reports/Purpose-WiseReport.jrxml";
			// String tsPath = "/Reports/Purpose-Wise-Excel-Report.jrxml";
				String fileName = "PurposeWiseReport.xls";
				GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);


		
	}

	@Override
	protected void generateHTML() throws Exception {
		purposeWiseReport=getPurposewiseReportHtml();	
		totalRecords=getTotalRecords();
		
	}
	public List<PurposeWiseReport> getPurposewiseReportHtml() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		String BlockYearList1="1=1";
		String PurposeList1="1=1";
		String stateList1="1=1";
		String stateList2="1=1";
		String selectCountryList1="1=1";
	    String	selectCountryList2="1=1";
		String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	    String lastblockYearList=selectedBlockYearList1.trim().substring(selectedBlockYearList1.length()-4, selectedBlockYearList1.length()); 
	     if(!selectedBlockYearList1.trim().equals("'ALL'")){
	    	 BlockYearList1= " fp3.blkyear IN ("+selectedBlockYearList1+")";
	    	 }
	       if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(fi.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(fpi.stdist,1,2) IN ("+selectedStateList+")";
	    	 }
	    
	     String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
	      if(!selectedCountryList.trim().equals("'ALL'")){
    	 selectCountryList1= "fp3.ctr_code in ("+selectedCountryList+")";
    	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
	      }
	      String selectedPurposeList=selectPurposeList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
			if(!selectedPurposeList.trim().equals("ALL")){
				PurposeList1= "pcode in("+selectedPurposeList+")";
			}

     	StringBuffer query = new StringBuffer("SELECT blkyear, PNAME,ROUND(SUM(AMOUNT),0) AS AMOUNT, SUM(RCNCOUNT) AS RCNCOUNT  FROM ("
     			+ "  SELECT fp3.blkyear, fp.pname AS pname, nvl(sum(fp3.amount),0) AS Amount,  "
     			+ " count(distinct fp3.rcn) as rcncount, fp.pcode as pcode   "
     			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_india fi   "
     			+ "  WHERE fp3.rcn=fi.rcn       "
     			+ "   and "+BlockYearList1+"  "
     			+ " and "+selectCountryList1+"  "
     			+ " AND fp3.pcode=fp.pcode     "
     			+ " and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
     			+ " and fp3.rcn=ft.rcn     "
     			+ "  AND ft.blkyear=fp3.blkyear  "
     			+ "  AND substr(ft.rcn,-1,1)='R'     "
     			+ "  AND ft.final_submit='Y' "
     			+ " AND "+stateList1+"     "
     			+ " GROUP BY fp3.blkyear, fp.pcode, fp.pname  "
     			+ " UNION ALL       "
     			+ "select fp3.blkyear, fp.pname as pname, nvl(sum(fp3.amount),0) as Amount,     "
     			+ "count(distinct fp3.rcn) as rcncount, fp.pcode as pcode       "
     			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_pp_india fpi       "
     			+ " WHERE fp3.rcn=fpi.rcn       "
     			+ "  and "+BlockYearList1+"    "
     			+ "  and "+selectCountryList1+"      "
     			+ "  AND fp3.pcode=fp.pcode   "
     			//+ " AND fpi.blkyear=fp3.blkyear  "
     			+ "and fp3.rcn=ft.rcn       "
     			+ " AND ft.blkyear=fp3.blkyear "
     			+ "  and substr(ft.rcn,-1,1)='P'    "
     			+ " AND ft.final_submit='Y' "
     			+ " AND "+stateList2+"      "
     			+ " group by fp3.blkyear, fp.pcode, fp.pname   "
     			+ "union all   "
     			+ "SELECT fp3.blkyear, fp.PURPOSE_NAME AS pname, nvl(sum(fp3.amount_received),0) AS Amount,     "
     			+ "count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode   "
     			+ " from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_india fi       "
     			+ " WHERE fp3.rcn=fi.rcn      "
     			+ "  and "+BlockYearList1+"  "
     			+ " and "+selectCountryList2+"      "
     			+ "  AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
     			+ " and (reg_date is null or trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (fi.status <> 'D') "
     			+ "   and fp3.rcn=ft.rcn      "
     			+ "  AND substr(ft.rcn,-1,1)='R'      "
     			+ "  AND ft.final_submit='Y' "
     			+ "  AND "+stateList1+"  "
     			//+ "  AND fi.rcn=fd.rcn  "
     			+ "AND ft.blkyear=fp3.blkyear   "
     			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME   "
     			+ "  UNION ALL       "
     			+ " select fp3.blkyear, fp.PURPOSE_NAME as pname, nvl(sum(fp3.amount_received),0) as Amount, "
     			+ "  count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode       "
     			+ "from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_pp_india fpi      "
     			+ "  WHERE fp3.rcn=fpi.rcn  "
     			+ " and "+BlockYearList1+"     "
     			+ " and "+selectCountryList2+"   "
     			+ " AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
     			//+ " AND fpi.blkyear=fp3.blkyear  "
     			+ " and fp3.rcn=ft.rcn      "
     			+ " AND ft.blkyear=fp3.blkyear "
     			+ "  and substr(ft.rcn,-1,1)='P'     "
     			+ " AND ft.final_submit='Y' "
     			+ " AND "+stateList2+" "
     			//+ "  AND fpi.rcn=fd.rcn  "
     			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME)  "
     			+ "ABC where "+PurposeList1+"  GROUP BY blkyear,ABC.PCODE,ABC.PNAME ORDER BY blkyear, AMOUNT DESC");
     	
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
								 List<PurposeWiseReport> purposeReportList = new ArrayList<PurposeWiseReport>();
									while(rs.next()) {
										purposeReportList.add(new PurposeWiseReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5))); 
										
									
								
									
									}
								return purposeReportList;
	}
								
							
	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			
			if(sortColumn != null && sortColumn.equals("") == false) {
				if(sortColumn.equals("purpose")) {
					orderbyClause.append(" ORDER BY PNAME");
				}else if(sortColumn.equals("amount")) {
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
	
					

	 public String getPurposeName(String selectedPurposeList) throws Exception {
		   if(selectedPurposeList.contains("ALL")){
			   return "ALL";
		   }
		   else{
			   if(connection == null) {
					throw new Exception("Invalid connection");
				}
				StringBuffer purposeName=new StringBuffer();		
				StringBuffer query = new StringBuffer("select PURPOSE_NAME from (SELECT PURPOSE_NAME FROM tm_amount_purpose where PURPOSE_CODE in ("+selectedPurposeList+") UNION ALL  SELECT pname FROM fc_purpose where pcode in("+selectedPurposeList+")) order by PURPOSE_NAME");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					purposeName=purposeName.append(rs.getString(1)+",");
				}
				return purposeName.toString();
			
			  }
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
			
			//reportDisplayType=parameterMap.get("reportDisplyType").get(0);
			if(reportFormat.equals("3"))
			{
			//sortColumn=parameterMap.get("sortColumn").get(0);
			//sortOrder=parameterMap.get("sortOrder").get(0);
			pageNum=parameterMap.get("pageNum").get(0);
			recordsPerPage=parameterMap.get("recordsPerPage").get(0);
			}
			selectBlockYearList=parameterMap.get("blockYear-List");
			selectCountryList=parameterMap.get("country-type");
			selectStateList=parameterMap.get("state-List");
			selectPurposeList=parameterMap.get("purpose-type");
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
		
			
		}
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

	public List<PurposeWiseReport> getPurposeWiseReport() {
		return purposeWiseReport;
	}

	public void setPurposeWiseReport(List<PurposeWiseReport> purposeWiseReport) {
		this.purposeWiseReport = purposeWiseReport;
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




	public List<String> getSelectBlockYearList() {
		return selectBlockYearList;
	}




	public void setSelectBlockYearList(List<String> selectBlockYearList) {
		this.selectBlockYearList = selectBlockYearList;
	}




	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}




	public List<String> getSelectCountryList() {
		return selectCountryList;
	}




	public void setSelectCountryList(List<String> selectCountryList) {
		this.selectCountryList = selectCountryList;
	}




	public List<String> getSelectStateList() {
		return selectStateList;
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




	public void setSelectStateList(List<String> selectStateList) {
		this.selectStateList = selectStateList;
	}




	public List<String> getSelectPurposeList() {
		return selectPurposeList;
	}




	public void setSelectPurposeList(List<String> selectPurposeList) {
		this.selectPurposeList = selectPurposeList;
	}




	



	public String getReportDisplayType() {
		return reportDisplayType;
	}









	public void setReportDisplayType(String reportDisplayType) {
		this.reportDisplayType = reportDisplayType;
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
		String  PurposeList1="1=1";
		String BlockYearList1="1=1";
		
	     Map  parameters = new HashMap();
	    parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		 parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("loginUserName", loginUserName);	
		 String selectedBlockYearList1 = selectBlockYearList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		 String lastblockYearList=selectedBlockYearList1.trim().substring(selectedBlockYearList1.length()-4, selectedBlockYearList1.length());
	     String selectedStateList=selectStateList.toString().replace("[", "").replace("]", "").replace(", ",",");
	     String selectedCountryList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ","','");  
	    String selectedPurposeList=selectPurposeList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		
	    if(!selectedStateList.trim().equals("ALL")){
	    	   stateList1= "substr(fi.stdist,1,2) IN ("+selectedStateList+")";
	    	   stateList2= " substr(fpi.stdist,1,2) IN ("+selectedStateList+")";
	    	 }
	    if(!selectedBlockYearList1.trim().equals("'ALL'")){
	    	 BlockYearList1= " fp3.blkyear IN ("+selectedBlockYearList1+")";
	    	 }
	   
	     
	      if(!selectedCountryList.trim().equals("'ALL'")){
 	 selectCountryList1= "fp3.ctr_code in ("+selectedCountryList+")";
 	 selectCountryList2= "fd.donor_country in ("+selectedCountryList+")";
	      }

			if(!selectedPurposeList.trim().equals("ALL")){
				PurposeList1= "pcode in("+selectedPurposeList+")";
			}
			
	      parameters.put("selectedBlockYearList",selectedBlockYearList1);
	      parameters.put("selectedState",sdao.getState(selectedStateList));
	     parameters.put("selectedCountry", cdao.getCountry(selectedCountryList));
	     parameters.put("selectedPurposeList", getPurposeName(selectedPurposeList));
	     
		
	     String  reportQuery = new String("SELECT blkyear, PNAME,ROUND(SUM(AMOUNT),0) AS AMOUNT, SUM(RCNCOUNT) AS RCNCOUNT  FROM ("
	     			+ "  SELECT fp3.blkyear, fp.pname AS pname, nvl(sum(fp3.amount),0) AS Amount,  "
	     			+ " count(distinct fp3.rcn) as rcncount, fp.pcode as pcode   "
	     			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_india fi   "
	     			+ "  WHERE fp3.rcn=fi.rcn       "
	     			+ "   and "+BlockYearList1+"  "
	     			+ " and "+selectCountryList1+"  "
	     			+ " AND fp3.pcode=fp.pcode     "
	     			+ " and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
	     			+ " and fp3.rcn=ft.rcn     "
	     			+ "  AND ft.blkyear=fp3.blkyear  "
	     			+ "  AND substr(ft.rcn,-1,1)='R'     "
	     			+ "  AND ft.final_submit='Y' "
	     			+ " AND "+stateList1+"     "
	     			+ " GROUP BY fp3.blkyear, fp.pcode, fp.pname  "
	     			+ " UNION ALL       "
	     			+ "select fp3.blkyear, fp.pname as pname, nvl(sum(fp3.amount),0) as Amount,     "
	     			+ "count(distinct fp3.rcn) as rcncount, fp.pcode as pcode       "
	     			+ " from fc_fc3_part3 fp3, fc_purpose fp, fc_fc3_tally ft, fc_pp_india fpi       "
	     			+ " WHERE fp3.rcn=fpi.rcn       "
	     			+ "  and "+BlockYearList1+"    "
	     			+ "  and "+selectCountryList1+"      "
	     			+ "  AND fp3.pcode=fp.pcode   "
	     			//+ " AND fpi.blkyear=fp3.blkyear  "
	     			+ "and fp3.rcn=ft.rcn       "
	     			+ " AND ft.blkyear=fp3.blkyear "
	     			+ "  and substr(ft.rcn,-1,1)='P'    "
	     			+ " AND ft.final_submit='Y' "
	     			+ " AND "+stateList2+"      "
	     			+ " group by fp3.blkyear, fp.pcode, fp.pname   "
	     			+ "union all   "
	     			+ "SELECT fp3.blkyear, fp.PURPOSE_NAME AS pname, nvl(sum(fp3.amount_received),0) AS Amount,     "
	     			+ "count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode   "
	     			+ " from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_india fi       "
	     			+ " WHERE fp3.rcn=fi.rcn      "
	     			+ "  and "+BlockYearList1+"  "
	     			+ " and "+selectCountryList2+"      "
	     			+ "  AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
	     			+ " and (reg_date is null or trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(fp3.blkyear,-4,4),'dd-mm-yyyy'))) and (fi.status <> 'D') "
	     			+ "   and fp3.rcn=ft.rcn      "
	     			+ "  AND substr(ft.rcn,-1,1)='R'      "
	     			+ "  AND ft.final_submit='Y' "
	     			+ "  AND "+stateList1+"  "
	     			//+ "  AND fi.rcn=fd.rcn  "
	     			+ "AND ft.blkyear=fp3.blkyear   "
	     			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME   "
	     			+ "  UNION ALL       "
	     			+ " select fp3.blkyear, fp.PURPOSE_NAME as pname, nvl(sum(fp3.amount_received),0) as Amount, "
	     			+ "  count(DISTINCT fp3.rcn) AS rcncount, fp.PURPOSE_CODE AS pcode       "
	     			+ "from FC_FC3_PURPOSE_WISE fp3, tm_amount_purpose fp, fc_fc3_tally ft, fc_pp_india fpi      "
	     			+ "  WHERE fp3.rcn=fpi.rcn  "
	     			+ " and "+BlockYearList1+"     "
	     			+ " and "+selectCountryList2+"   "
	     			+ " AND fp3.PURPOSE_CODE=fp.PURPOSE_CODE  "
	     			//+ " AND fpi.blkyear=fp3.blkyear  "
	     			+ " and fp3.rcn=ft.rcn      "
	     			+ " AND ft.blkyear=fp3.blkyear "
	     			+ "  and substr(ft.rcn,-1,1)='P'     "
	     			+ " AND ft.final_submit='Y' "
	     			+ " AND "+stateList2+" "
	     			//+ "  AND fpi.rcn=fd.rcn  "
	     			+ "group by fp3.blkyear, fp.PURPOSE_CODE, fp.PURPOSE_NAME)  "
	     			+ "ABC where "+PurposeList1+"  GROUP BY blkyear,ABC.PCODE,ABC.PNAME ORDER BY blkyear, AMOUNT DESC");
		

	     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			 parameters.put("PRINTRECORD_PURPOSE_WISE", ds);
			 
			 
			 String tsPath = "/Reports/Purpose-Wise-CSV-Report.jrxml";
				String fileName = "PurposeWiseReport";
				GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);


		
	
		
	}




	
   
	
}
