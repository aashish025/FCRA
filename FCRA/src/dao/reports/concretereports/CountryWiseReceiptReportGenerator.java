package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.CountryWiseReceiptReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class CountryWiseReceiptReportGenerator extends MISReportGenerator{
	
	
	private List<CountryWiseReceiptReport> countryWiseReceiptReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private String reportDisplyType;
	private String loginUserName;
	private String loginOfficeName;
	private List<String> selectCountryList;
	private List<String> selectStateList;
	private List<String> selectBlockYear;
	private String sortColumn;
	private String sortOrder;
	private int virtualizationMaxSize = 200;
	
	public CountryWiseReceiptReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void generateExcel() throws Exception {
		// TODO Auto-generated method stub
		
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
	//	parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
		String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
			String subStringStateList="1=1";
			String CountryList="1=1";
	
			String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			if(!stList.trim().equals("'ALL'")){
				subStringStateList= "substr(d.stdist,1,2) IN ("+stList+")";
			}
			
			String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			if(!contList.trim().equals("'ALL'")){
				CountryList= "ctr_code in ("+contList+")";
			}
				parameters.put("selectedBlockYearList",selectBlockyearList);
			
				CountryTypeDao coa= new CountryTypeDao(connection);
				parameters.put("selectedCountryList",coa.getCountry(contList));
				StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState(stList));
			     
	    
	    	 reportQuery ="select tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn,sum(tab.amount) amt from   (   "
						+ " SELECT A.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
						+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d   "
						+ "   WHERE A.blkyear in('"
						+ 	selectBlockyearList
						+ "') "
						+ "   AND b.blkyear =  A.blkyear "
						+ "   AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
						+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
						+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')  "
						+ "  AND substr(A.rcn,-1,1)='R' "
						+ "   AND  "+subStringStateList+" "
						+ "   GROUP BY A.blkyear, ctr_name,b.ctr_code, d.asso_name, d.rcn  "

						+ "   UNION ALL   "
						+ "   SELECT a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
						+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d  "
						+ "    where a.blkyear in('"
						+ selectBlockyearList
						+ "') and   "
						+ "   b.blkyear =  a.blkyear and   "
						+ "   a.final_submit='Y' and b.ctr_code = c.ctr_code and  "
						+ "   A.rcn=b.rcn "
						+ "   AND A.rcn=d.rcn "
						+ "   AND d.blkyear=a.blkyear "
						+ "  AND substr(A.rcn,-1,1)='P' "
						+ "   AND "+subStringStateList+" "
						+ "   group by a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn  "

						+ "   union all "

						+ "   SELECT A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount   "
						+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd  "
						+ "   WHERE A.blkyear in('"
						+ selectBlockyearList
						+ "') "
						+ "    AND b.blkyear =  A.blkyear "
						+ "   AND A.final_submit='Y' AND fd.donor_country = c.ctr_code "
						+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
						+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
						+ "   AND substr(A.rcn,-1,1)='R' "
						+ "  AND "+subStringStateList+" "
						+ "   AND fd.donor_type='01' "
						+ "   AND d.rcn=fd.rcn "
						+ "	   and b.donor_code=fd.donor_code "
						+ "    GROUP BY A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn  "

						+ "   UNION ALL  "
						+ "    SELECT a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount    "
						+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd  "
						+ "   where a.blkyear in ('"
						+ selectBlockyearList
						+ "') and   "
						+ "   b.blkyear =  a.blkyear and  "
						+ "    a.final_submit='Y' and fd.donor_country = c.ctr_code and  "
						+ "   A.rcn=b.rcn "
						+ "   AND A.rcn=d.rcn"
						+ "    AND d.blkyear=a.blkyear "
						+ "    AND substr(A.rcn,-1,1)='P' "
						+ "    AND "+subStringStateList+" "
						+ "    AND fd.donor_type='01' "
						+ "    AND d.rcn=fd.rcn "
						+ "		and b.donor_code=fd.donor_code"
						
						+ "   GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn   "

						+ "   )  "
						+ "   tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn "
						+ "   ORDER BY blkyear, amt DESC"; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Country_Wise_Receipt_Report", ds);
	     
	     
		String tsPath ="/Reports/CountryWiseReceipReportExcel.jrxml";
		String fileName ="CountryWiseReceiptReport";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	    
	
	


		
		
	}
	@Override
	protected void generateHTML() throws Exception {
		
		countryWiseReceiptReport=getDetailsCountryWiseReceiptHtml();	
					totalRecords=getTotalRecords();	
			
		}
		

	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
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
			selectBlockYear=parameterMap.get("blockYear-List");
			selectStateList=parameterMap.get("state-List");
			selectCountryList=parameterMap.get("country-List");
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
		}
		// TODO Auto-generated method stub
		
	}
	public List<CountryWiseReceiptReport> getDetailsCountryWiseReceiptHtml() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
			
								
							String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
							String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
								String subStringStateList="1=1";
								String CountryList="1=1";
						
								String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
								if(!stList.trim().equals("'ALL'")){
									subStringStateList= "substr(d.stdist,1,2) IN ("+stList+")";
								}
								
								String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
								if(!contList.trim().equals("'ALL'")){
									CountryList= "ctr_code in ("+contList+")";
								}
		StringBuffer countQuery = new StringBuffer(
				"select count(*) from (select tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn,sum(tab.amount) amt from   (   "
						+ " SELECT A.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
						+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d   "
						+ "   WHERE A.blkyear in('"
						+ selectBlockyearList
						+ "') "
						+ "   AND b.blkyear =  A.blkyear "
						+ "   AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
						+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
						+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')  "
						+ "  AND substr(A.rcn,-1,1)='R' "
						+ "   AND "+subStringStateList+" "
						+ "   GROUP BY A.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn  "

						+ "   UNION ALL   "
						+ "   SELECT a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
						+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d  "
						+ "    where a.blkyear in('"
						+ selectBlockyearList
						+ "') and   "
						+ "   b.blkyear =  a.blkyear and   "
						+ "   a.final_submit='Y' and b.ctr_code = c.ctr_code and  "
						+ "   A.rcn=b.rcn "
						+ "   AND A.rcn=d.rcn "
						+ "   AND d.blkyear=a.blkyear "
						+ "  AND substr(A.rcn,-1,1)='P' "
						+ "   AND "+subStringStateList+" "
						+ "   group by a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn  "

						+ "   union all "

						+ "   SELECT A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount   "
						+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd  "
						+ "   WHERE A.blkyear in('"
						+ selectBlockyearList
						+ "') "
						+ "    AND b.blkyear =  A.blkyear "
						+ "   AND A.final_submit='Y' AND fd.donor_country = c.ctr_code "
						+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
						+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
						+ "   AND substr(A.rcn,-1,1)='R' "
						+ "  AND "+subStringStateList+" "
						+ "   AND fd.donor_type='01' "
						+ "   AND d.rcn=fd.rcn "
						+ "		and b.donor_code=fd.donor_code"
						+ "    GROUP BY A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn  "

						+ "   UNION ALL  "
						+ "    SELECT a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount    "
						+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd  "
						+ "   where a.blkyear in ('"
						+ selectBlockyearList
						+ "') and   "
						+ "   b.blkyear =  a.blkyear and  "
						+ "    a.final_submit='Y' and fd.donor_country = c.ctr_code and  "
						+ "   A.rcn=b.rcn "
						+ "   AND A.rcn=d.rcn"
						+ "    AND d.blkyear=a.blkyear "
						+ "    AND substr(A.rcn,-1,1)='P' "
						+ "    AND "+subStringStateList+" "
						+ "    AND fd.donor_type='01' "
						+ "    AND d.rcn=fd.rcn "
						+ "    and b.donor_code=fd.donor_code"
						+ "   GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn   "

						+ "   )  "
						+ "   tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn )"
						+ "   ORDER BY blkyear, amt DESC");
								StringBuffer query;
							
								 query = new StringBuffer(
											"select tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn,sum(tab.amount) amt from   (   "
													+ " SELECT A.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
													+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d   "
													+ "   WHERE A.blkyear in('"
													+ selectBlockyearList
													+ "') "
													+ "   AND b.blkyear =  A.blkyear "
													+ "   AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
													+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
													+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')  "
													+ "  AND substr(A.rcn,-1,1)='R' "
													+ "   AND "+subStringStateList+" "
													+ "   GROUP BY A.blkyear, ctr_name,b.ctr_code, d.asso_name, d.rcn  "

													+ "   UNION ALL   "
													+ "   SELECT a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
													+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d  "
													+ "    where a.blkyear in('"
													+ selectBlockyearList
													+ "') and   "
													+ "   b.blkyear =  a.blkyear and   "
													+ "   a.final_submit='Y' and b.ctr_code = c.ctr_code and  "
													+ "   A.rcn=b.rcn "
													+ "   AND A.rcn=d.rcn "
													+ "   AND d.blkyear=a.blkyear "
													+ "  AND substr(A.rcn,-1,1)='P' "
													+ "   AND "+subStringStateList+" "
													+ "   group by a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn  "

													+ "   union all "

													+ "   SELECT A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount   "
													+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd  "
													+ "   WHERE A.blkyear in('"
													+ selectBlockyearList
													+ "') "
													+ "    AND b.blkyear =  A.blkyear "
													+ "   AND A.final_submit='Y' AND fd.donor_country = c.ctr_code "
													+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
													+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
													+ "   AND substr(A.rcn,-1,1)='R' "
													+ "  AND "+subStringStateList+" "
													+ "   AND fd.donor_type='01' "
													+ "   AND d.rcn=fd.rcn "
													+ "	and b.donor_code=fd.donor_code"
													+ "    GROUP BY A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn  "

													+ "   UNION ALL  "
													+ "    SELECT a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount    "
													+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd  "
													+ "   where a.blkyear in ('"
													+ selectBlockyearList
													+ "') and   "
													+ "   b.blkyear =  a.blkyear and  "
													+ "    a.final_submit='Y' and fd.donor_country = c.ctr_code and  "
													+ "   A.rcn=b.rcn "
													+ "   AND A.rcn=d.rcn"
													+ "    AND d.blkyear=a.blkyear "
													+ "    AND substr(A.rcn,-1,1)='P' "
													+ "    AND "+subStringStateList+" "
													+ "    AND fd.donor_type='01' "
													+ "    AND d.rcn=fd.rcn "
													+ "		and b.donor_code=fd.donor_code"
													+ "   GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn   "

													+ "   )  "
													+ "   tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn "
													+ "   ORDER BY tab.blkyear, amt DESC");
							
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
						          String queryForPaging = preparePagingQuery(query);	
								 statement = connection.prepareStatement(queryForPaging);
								 if(pageNum == null || recordsPerPage == null) {
						
								 }
								 else {
								 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
								 statement.setInt(2, (pageRequested-1) * pageSize + 1);
								 }
							
								 rs = statement.executeQuery();
								List<CountryWiseReceiptReport> countryWiseReceiptReportList = new ArrayList<CountryWiseReceiptReport>();
								while(rs.next()) {
									countryWiseReceiptReportList.add(new CountryWiseReceiptReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6))); 
									
								}
							return countryWiseReceiptReportList;
								
							}
	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			
			if(sortColumn != null && sortColumn.equals("") == false) {
				if(sortColumn.equals("country")) {
					orderbyClause.append(" ORDER BY CTR_NAME");
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
	 
	 
	 @Override
		protected void generatePDF() throws Exception {
			Map  parameters = new HashMap();
			parameters.put("reportType", reportType);
			parameters.put("reportFormat", reportFormat);
		//	parameters.put("reportDisplayType", reportDisplyType);
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		   
			String reportQuery="";
			String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
			String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
				String subStringStateList="1=1";
				String CountryList="1=1";
		
				String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				if(!stList.trim().equals("'ALL'")){
					subStringStateList= "substr(d.stdist,1,2) IN ("+stList+")";
				}
				
				String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				if(!contList.trim().equals("'ALL'")){
					CountryList= "ctr_code in ("+contList+")";
				}
					parameters.put("selectedBlockYearList",selectBlockyearList);
				
					CountryTypeDao coa= new CountryTypeDao(connection);
					parameters.put("selectedCountryList",coa.getCountry(contList));
					StateDao sdao= new StateDao(connection);
					parameters.put("selectedStateList",sdao.getState(stList));
				     
		    
					 reportQuery ="select tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn,sum(tab.amount) amt from   (   "
								+ " SELECT A.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
								+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d   "
								+ "   WHERE A.blkyear in('"
								+ selectBlockyearList
								+ "') "
								+ "   AND b.blkyear =  A.blkyear "
								+ "   AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
								+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
								+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')  "
								+ "  AND substr(A.rcn,-1,1)='R' "
								+ "   AND "+subStringStateList+" "
								+ "   GROUP BY A.blkyear, ctr_name,b.ctr_code, d.asso_name, d.rcn  "

								+ "   UNION ALL   "
								+ "   SELECT a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
								+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d  "
								+ "    where a.blkyear in('"
								+ selectBlockyearList
								+ "') and   "
								+ "   b.blkyear =  a.blkyear and   "
								+ "   a.final_submit='Y' and b.ctr_code = c.ctr_code and  "
								+ "   A.rcn=b.rcn "
								+ "   AND A.rcn=d.rcn "
								+ "   AND d.blkyear=a.blkyear "
								+ "  AND substr(A.rcn,-1,1)='P' "
								+ "   AND "+subStringStateList+" "
								+ "   group by a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn  "

								+ "   union all "

								+ "   SELECT A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount   "
								+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd  "
								+ "   WHERE A.blkyear in('"
								+ selectBlockyearList
								+ "') "
								+ "    AND b.blkyear =  A.blkyear "
								+ "   AND A.final_submit='Y' AND fd.donor_country = c.ctr_code "
								+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
								+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
								+ "   AND substr(A.rcn,-1,1)='R' "
								+ "  AND "+subStringStateList+" "
								+ "   AND fd.donor_type='01' "
								+ "   AND d.rcn=fd.rcn "
								+ "	and b.donor_code=fd.donor_code"
								+ "    GROUP BY A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn  "

								+ "   UNION ALL  "
								+ "    SELECT a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount    "
								+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd  "
								+ "   where a.blkyear in ('"
								+ selectBlockyearList
								+ "') and   "
								+ "   b.blkyear =  a.blkyear and  "
								+ "    a.final_submit='Y' and fd.donor_country = c.ctr_code and  "
								+ "   A.rcn=b.rcn "
								+ "   AND A.rcn=d.rcn"
								+ "    AND d.blkyear=a.blkyear "
								+ "    AND substr(A.rcn,-1,1)='P' "
								+ "    AND "+subStringStateList+" "
								+ "    AND fd.donor_type='01' "
								+ "    AND d.rcn=fd.rcn "
								+ "    and b.donor_code=fd.donor_code"
								+ "   GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn   "

								+ "   )  "
								+ "   tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn "
								+ "   ORDER BY blkyear, amt DESC";
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("Country_Wise_Receipt_Report", ds);
		     
		     
			String tsPath = "/Reports/CountryWiseReceipReport.jrxml";
			String fileName = "CountryWiseReceiptReport.pdf";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		
		


}
			
	 

	
	public String getReportDisplyType() {
		return reportDisplyType;
	}

	public void setReportDisplyType(String reportDisplyType) {
		this.reportDisplyType = reportDisplyType;
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

	public List<String> getSelectCountryList() {
		return selectCountryList;
	}
	public void setSelectCountryList(List<String> selectCountryList) {
		this.selectCountryList = selectCountryList;
	}
	public List<String> getSelectStateList() {
		return selectStateList;
	}
	public void setSelectStateList(List<String> selectStateList) {
		this.selectStateList = selectStateList;
	}
	public List<String> getSelectBlockYear() {
		return selectBlockYear;
	}
	public void setSelectBlockYear(List<String> selectBlockYear) {
		this.selectBlockYear = selectBlockYear;
	}
	public List<CountryWiseReceiptReport> getCountryWiseReceiptReport() {
		return countryWiseReceiptReport;
	}
	public void setCountryWiseReceiptReport(
			List<CountryWiseReceiptReport> countryWiseReceiptReport) {
		this.countryWiseReceiptReport = countryWiseReceiptReport;
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

	@Override
	protected void generateCSV() throws Exception {

		// TODO Auto-generated method stub
		
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
	//	parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
		String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
			String subStringStateList="1=1";
			String CountryList="1=1";
	
			String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			if(!stList.trim().equals("'ALL'")){
				subStringStateList= "substr(d.stdist,1,2) IN ("+stList+")";
			}
			
			String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			if(!contList.trim().equals("'ALL'")){
				CountryList= "ctr_code in ("+contList+")";
			}
				parameters.put("selectedBlockYearList",selectBlockyearList);
			
				CountryTypeDao coa= new CountryTypeDao(connection);
				parameters.put("selectedCountryList",coa.getCountry(contList));
				StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState(stList));
			     
	    
	    	 reportQuery ="select tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn,sum(tab.amount) amt from   (   "
						+ " SELECT A.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
						+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d   "
						+ "   WHERE A.blkyear in('"
						+ selectBlockyearList
						+ "') "
						+ "   AND b.blkyear =  A.blkyear "
						+ "   AND A.final_submit='Y' AND b.ctr_code = c.ctr_code "
						+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
						+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')  "
						+ "  AND substr(A.rcn,-1,1)='R' "
						+ "   AND  "+subStringStateList+" "
						+ "   GROUP BY A.blkyear, ctr_name,b.ctr_code, d.asso_name, d.rcn  "

						+ "   UNION ALL   "
						+ "   SELECT a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn, sum(amount) amount  "
						+ "   FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d  "
						+ "    where a.blkyear in('"
						+ selectBlockyearList
						+ "') and   "
						+ "   b.blkyear =  a.blkyear and   "
						+ "   a.final_submit='Y' and b.ctr_code = c.ctr_code and  "
						+ "   A.rcn=b.rcn "
						+ "   AND A.rcn=d.rcn "
						+ "   AND d.blkyear=a.blkyear "
						+ "  AND substr(A.rcn,-1,1)='P' "
						+ "   AND "+subStringStateList+" "
						+ "   group by a.blkyear, ctr_name, b.ctr_code, d.asso_name, d.rcn  "

						+ "   union all "

						+ "   SELECT A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount   "
						+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd  "
						+ "   WHERE A.blkyear in('"
						+ selectBlockyearList
						+ "') "
						+ "    AND b.blkyear =  A.blkyear "
						+ "   AND A.final_submit='Y' AND fd.donor_country = c.ctr_code "
						+ "   AND A.rcn=b.rcn AND A.rcn=d.rcn "
						+ "   and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D') "
						+ "   AND substr(A.rcn,-1,1)='R' "
						+ "  AND "+subStringStateList+" "
						+ "   AND fd.donor_type='01' "
						+ "   AND d.rcn=fd.rcn "
						+ "   and b.donor_code=fd.donor_code"
						+ "    GROUP BY A.blkyear, ctr_name, donor_country, d.asso_name, d.rcn  "

						+ "   UNION ALL  "
						+ "    SELECT a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn, sum(amount) amount    "
						+ "   FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd  "
						+ "   where a.blkyear in ('"
						+ selectBlockyearList
						+ "') and   "
						+ "   b.blkyear =  a.blkyear and  "
						+ "    a.final_submit='Y' and fd.donor_country = c.ctr_code and  "
						+ "   A.rcn=b.rcn "
						+ "   AND A.rcn=d.rcn"
						+ "    AND d.blkyear=a.blkyear "
						+ "    AND substr(A.rcn,-1,1)='P' "
						+ "    AND "+subStringStateList+" "
						+ "    AND fd.donor_type='01' "
						+ "    AND d.rcn=fd.rcn "
						+ "    and b.donor_code=fd.donor_code"
						+ "   GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name, d.rcn   "

						+ "   )  "
						+ "   tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name,tab.asso_name, tab.rcn "
						+ "   ORDER BY blkyear, amt DESC"; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Country_Wise_Receipt_Report", ds);
	     
	     
		String tsPath ="/Reports/CountryWiseReceipReportCSV.jrxml";
		String fileName ="CountryWiseReceiptReport";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	    
	
		// TODO Auto-generated method stub
		
	}
	
	

}
