package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.ReligionWiseReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.ReligionTypeDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class ReligionWiseReportGenerator extends MISReportGenerator{
	
	private List<ReligionWiseReport> religionWiseReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private int virtualizationMaxSize = 200;
	private String loginUserName;
	private String loginOfficeName;
	private List<String> selectStateList;
	private List<String> selectBlockYear;
	private List<String> selectReligionList;
	private String sortColumn;
	private String sortOrder;

	public ReligionWiseReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void generateExcel() throws Exception {
		// TODO Auto-generated method stub
		
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);

	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		
		
		/* String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		     String BlockYearList1="1=1";
			 String StateList="1=1";
			  if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 }
              if(!stList.trim().equals("'ALL'")){
           	   StateList= "S.scode in ("+stList+")";
								}
		
				parameters.put("selectedBlockYearList",selectBlockyearList);
				
				StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState(stList));
			     
	    
				reportQuery = (" SELECT t1.blkyear, sname, t1.RELIGION_DESC AS rname, sum(associations) associations, sum(amount) "
				 		+ "amount FROM ( "
				 		+ "select ft.blkyear, substr(fi.stdist,1,2) state, fr.RELIGION_DESC,   "
				 		+ "  count(DISTINCT fi.rcn) associations,sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount  "
				 		+ "  from fc_india fi, fc_fc3_part1 fp1, tm_religion fr, fc_fc3_tally ft   "
				 		+ " where fi.rcn=fp1.rcn   "
				 		+ "  AND fi.asso_religion=fr.RELIGION_CODE   "
				 		+ " and fp1.blkyear=ft.blkyear  "
				 		+ " and fi.rcn = ft.rcn   "
				 		+ "and ft.final_submit='Y'   "
				 		+ " AND ft.rcn = fp1.rcn   "
				 		+ " AND "+BlockYearList1+"   "
				 		+ " GROUP BY ft.blkyear, substr(fi.stdist,1,2), RELIGION_DESC   "
				 		+ " UNION ALL   "
				 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fr.RELIGION_DESC as rname, count(distinct fpi.rcn) associations,  "
				 		+ " sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount  "
				 		+ " from fc_pp_india fpi, fc_fc3_part1 fp1, tm_religion fr, fc_fc3_tally ft   "
				 		+ "  where fpi.rcn=fp1.rcn   "
				 		+ " and fpi.asso_religion=fr.RELIGION_CODE    "
				 		+ "  AND fpi.blkyear=ft.blkyear"
				 		+ " and fp1.blkyear=ft.blkyear"
				 		+ "   and fpi.rcn = ft.rcn   "
				 		+ " and ft.final_submit='Y' "
				 		+ "AND ft.rcn = fp1.rcn   "
				 		+ " AND "+BlockYearList1+"  "
				 		+ " GROUP BY ft.blkyear,substr(fpi.stdist,1,2) ,RELIGION_DESC  "
				 		+ "  union all "
				 		+ "select ft.blkyear, substr(fi.stdist,1,2) state, fr.RELIGION_DESC,   "
				 		+ "   count(DISTINCT fi.rcn) associations,sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) amount   "
				 		+ "  from fc_india fi, fc_fc3_part1_new fp1, tm_religion fr, fc_fc3_tally ft   "
				 		+ "  where fi.rcn=fp1.rcn   "
				 		+ " AND fi.asso_religion=fr.RELIGION_CODE   "
				 		+ "  and fp1.blkyear=ft.blkyear  "
				 		+ " and fi.rcn = ft.rcn   "
				 		+ " and ft.final_submit='Y'   "
				 		+ "   AND ft.rcn = fp1.rcn "
				 		+ "  AND "+BlockYearList1+"   "
				 		+ " group by ft.blkyear, substr(fi.stdist,1,2) , RELIGION_DESC "
				 		+ "   UNION ALL   "
				 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fr.RELIGION_DESC as rname, count(distinct fpi.rcn) associations, "
				 		+ "  sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) amount    "
				 		+ "  from fc_pp_india fpi, fc_fc3_part1_new fp1, tm_religion fr, fc_fc3_tally ft  "
				 		+ "  where fpi.rcn=fp1.rcn   "
				 		+ " and fpi.asso_religion=fr.RELIGION_CODE  "
				 		+ " and fpi.blkyear=ft.blkyear  "
				 		+ " and fp1.blkyear=ft.blkyear "
				 		+ " and fpi.rcn = ft.rcn "
				 		+ " and ft.final_submit='Y'"
				 		+ " and ft.rcn = fp1.rcn   "
				 		+ "AND "+BlockYearList1+" "
				 		+ " GROUP BY ft.blkyear, substr(fpi.stdist,1,2) , RELIGION_DESC  "
				 		+ "   ) t1, TM_STATE S"
				 		+ "  WHERE t1.state = S.scode and "+StateList+" GROUP BY t1.blkyear,sname,t1.RELIGION_DESC"
				 		+ " ORDER BY t1.blkyear,sname,t1.RELIGION_DESC ");*/
		 String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		   String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    
		   
		     String BlockYearList1="1=1";
			 String StateList="1=1";
			 String ReligionList="1=1";
			  if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 
			    	 }
			  String selectBlockList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		 		
		    	 parameters.put("selectedBlockYearList",selectBlockList);
			
            if(!stList.trim().equals("'ALL'")){
	            	   StateList= "state in ("+stList+")";
	            	   
					}
           
            
            if(!selectedReligionList.trim().equals("'ALL'")){
         	   ReligionList= "rname in  ("+selectedReligionList+")";
         	  			}
            
            ReligionTypeDao rdo=new ReligionTypeDao(connection);
				parameters.put("selectedReligionList",rdo.getReligion(selectedReligionList));
				
            StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState(stList));
			     
          
				
	    
				reportQuery = ("SELECT blkyear,r.RELIGION_DESC religion, rcn, asso_name, sname, distname, amount FROM ( "
							 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion rname, "
							 		+ " fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
							 		+ " from fc_india fi, fc_fc3_part1 fp1, fc_fc3_tally ft "
							 		+ "  where fi.rcn=fp1.rcn "
							 		+ " and fp1.blkyear=ft.blkyear "
							 		+ " and fi.rcn = ft.rcn "
							 		+ " and ft.final_submit='Y' "
							 		+ " AND ft.rcn = fp1.rcn "
							 		+ " AND "+BlockYearList1+" "
							 		+ "  GROUP BY ft.blkyear, substr(fi.stdist,1,2), fi.asso_religion,fi.rcn,"
							 		+ " fi.asso_name, substr(fi.stdist,-3,3) "
							 		+ "  UNION ALL "
							 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
							 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
							 		+ " sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
							 		+ " from fc_pp_india fpi, fc_fc3_part1 fp1, fc_fc3_tally ft "
							 		+ "  where fpi.rcn=fp1.rcn  "
							 		+ " AND fpi.blkyear=ft.blkyear "
							 		+ " and fp1.blkyear=ft.blkyear "
							 		+ " and fpi.rcn = ft.rcn  "
							 		+ " and ft.final_submit='Y' "
							 		+ " AND ft.rcn = fp1.rcn "
							 		+ "  AND "+BlockYearList1+" "
							 		+ " GROUP BY ft.blkyear,substr(fpi.stdist,1,2) ,fpi.asso_religion,fpi.rcn, fpi.asso_name, "
							 		+ " substr(fpi.stdist,-3,3) "
							 		+ "  union all "
							 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion, "
							 		+ "   fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) "
							 		+ "  amount  from fc_india fi, fc_fc3_part1_new fp1, fc_fc3_tally ft "
							 		+ "where fi.rcn=fp1.rcn  and fp1.blkyear=ft.blkyear   and fi.rcn = ft.rcn   and ft.final_submit='Y' "
							 		+ " AND ft.rcn = fp1.rcn   AND "+BlockYearList1+"  "
							 		+ " group by ft.blkyear, substr(fi.stdist,1,2) , fi.asso_religion,fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) "
							 		+ "  UNION ALL "
							 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
							 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
							 		+ " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) amount "
							 		+ "  from fc_pp_india fpi, fc_fc3_part1_new fp1, fc_fc3_tally ft "
							 		+ " where fpi.rcn=fp1.rcn   and fpi.blkyear=ft.blkyear  and fp1.blkyear=ft.blkyear   and fpi.rcn = ft.rcn "
							 		+ " and ft.final_submit='Y' "
							 		+ " and ft.rcn = fp1.rcn   AND "+BlockYearList1+" "
							 		+ " GROUP BY ft.blkyear, substr(fpi.stdist,1,2) , fpi.asso_religion,fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) "
							 		+ "   ) t1, TM_STATE S, TM_RELIGION r, tm_district d "
							 		+ " WHERE t1.state = S.scode and rname=r.RELIGION_CODE and district=d.distcode "
							 		+ " and "+StateList+" and "+ReligionList+" "
							 		+ "  ORDER BY t1.blkyear,amount desc ");
		
         	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Religion_Wise_Report", ds);
	     
	     
		String tsPath ="/Reports/ReligionWiseReportExcel.jrxml";
		String fileName ="ReligionWiseReport";
 		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	    
	
	


		
		
	}
	@Override
	protected void generateHTML() throws Exception {
		
		religionWiseReport=getDetailsReligionWiseHtml();	
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
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
			selectBlockYear=parameterMap.get("blockYear-List");
			selectStateList=parameterMap.get("state-List");
			selectReligionList=parameterMap.get("religion-List");
		
		}
}
	
	public List<ReligionWiseReport> getDetailsReligionWiseHtml() throws Exception {
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}
 		     String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 		    String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 		   String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    
 		     String BlockYearList1="1=1";
			 String StateList="1=1";
			 String ReligionList="1=1";
			  if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 }
               if(!stList.trim().equals("'ALL'")){
            	   StateList= "state in ("+stList+")";
								}
               
               if(!selectedReligionList.trim().equals("'ALL'")){
            	   ReligionList= "rname in  ("+selectedReligionList+")";
								}
								
								StringBuffer query; 
								
								
								 query = new StringBuffer("SELECT blkyear,r.RELIGION_DESC religion, rcn, asso_name, sname, distname, amount FROM ( "
								 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion rname, "
								 		+ " fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
								 		+ " from fc_india fi, fc_fc3_part1 fp1, fc_fc3_tally ft "
								 		+ "  where fi.rcn=fp1.rcn "
								 		+ " and fp1.blkyear=ft.blkyear "
								 		+ " and fi.rcn = ft.rcn "
								 		+ " and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn "
								 		+ " AND "+BlockYearList1+" "
								 		+ "  GROUP BY ft.blkyear, substr(fi.stdist,1,2), fi.asso_religion,fi.rcn,"
								 		+ " fi.asso_name, substr(fi.stdist,-3,3) "
								 		+ "  UNION ALL "
								 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
								 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
								 		+ " sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
								 		+ " from fc_pp_india fpi, fc_fc3_part1 fp1,  fc_fc3_tally ft "
								 		+ "  where fpi.rcn=fp1.rcn  "
								 		+ " AND fpi.blkyear=ft.blkyear "
								 		+ " and fp1.blkyear=ft.blkyear "
								 		+ " and fpi.rcn = ft.rcn  "
								 		+ " and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn "
								 		+ "  AND "+BlockYearList1+" "
								 		+ " GROUP BY ft.blkyear,substr(fpi.stdist,1,2) ,fpi.asso_religion,fpi.rcn, fpi.asso_name, "
								 		+ " substr(fpi.stdist,-3,3) "
								 		+ "  union all "
								 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion, "
								 		+ "   fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) "
								 		+ "  amount  from fc_india fi, fc_fc3_part1_new fp1,  fc_fc3_tally ft "
								 		+ "where fi.rcn=fp1.rcn  and fp1.blkyear=ft.blkyear   and fi.rcn = ft.rcn   and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn   AND "+BlockYearList1+"  "
								 		+ " group by ft.blkyear, substr(fi.stdist,1,2) , fi.asso_religion,fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) "
								 		+ "  UNION ALL "
								 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
								 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
								 		+ " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) amount "
								 		+ "  from fc_pp_india fpi, fc_fc3_part1_new fp1,  fc_fc3_tally ft "
								 		+ " where fpi.rcn=fp1.rcn   and fpi.blkyear=ft.blkyear  and fp1.blkyear=ft.blkyear   and fpi.rcn = ft.rcn "
								 		+ " and ft.final_submit='Y' "
								 		+ " and ft.rcn = fp1.rcn   AND "+BlockYearList1+" "
								 		+ " GROUP BY ft.blkyear, substr(fpi.stdist,1,2) , fpi.asso_religion,fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) "
								 		+ "   ) t1, TM_STATE S, TM_RELIGION r, tm_district d "
								 		+ " WHERE t1.state = S.scode and rname=r.RELIGION_CODE and district=d.distcode "
								 		+ " and "+StateList+" and "+ReligionList+" "
								 		+ "  ORDER BY t1.blkyear,amount desc");
						  StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
							
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
								List<ReligionWiseReport> religionWiseReportReportList = new ArrayList<ReligionWiseReport>();
								while(rs.next()) {
									religionWiseReportReportList.add(new ReligionWiseReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8))); 
									
								}
							return religionWiseReportReportList;
								
							}
	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			
			if(sortColumn != null && sortColumn.equals("") == false) {
				if(sortColumn.equals("rname")) {
					orderbyClause.append(" ORDER BY RNAME");
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
	
		    parameters.put("loginUserName", loginUserName);
		    parameters.put("loginOfficeName",loginOfficeName);
		   
			String reportQuery="";
			 
	 		  String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
	 		    String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
	 		   String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			    
	 		   
	 		     String BlockYearList1="1=1";
				 String StateList="1=1";
				 String ReligionList="1=1";
				  if(!selectBlockyearList.trim().equals("'ALL'")){
				    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
				    	 
				    	 }
				  String selectBlockList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
			 		
			    	 parameters.put("selectedBlockYearList",selectBlockList);
				
	               if(!stList.trim().equals("'ALL'")){
		            	   StateList= "state in ("+stList+")";
		            	   
						}
	              
	               
	               if(!selectedReligionList.trim().equals("'ALL'")){
	            	   ReligionList= "rname in  ("+selectedReligionList+")";
	            	  			}
	               
	               ReligionTypeDao rdo=new ReligionTypeDao(connection);
					parameters.put("selectedReligionList",rdo.getReligion(selectedReligionList));
					
	               StateDao sdao= new StateDao(connection);
					parameters.put("selectedStateList",sdao.getState(stList));
				     
	             
					
		    
					reportQuery = ("SELECT blkyear,r.RELIGION_DESC religion, rcn, asso_name, sname, distname, amount FROM ( "
								 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion rname, "
								 		+ " fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
								 		+ " from fc_india fi, fc_fc3_part1 fp1, fc_fc3_tally ft "
								 		+ "  where fi.rcn=fp1.rcn "
								 		+ " and fp1.blkyear=ft.blkyear "
								 		+ " and fi.rcn = ft.rcn "
								 		+ " and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn "
								 		+ " AND "+BlockYearList1+" "
								 		+ "  GROUP BY ft.blkyear, substr(fi.stdist,1,2), fi.asso_religion,fi.rcn,"
								 		+ " fi.asso_name, substr(fi.stdist,-3,3) "
								 		+ "  UNION ALL "
								 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
								 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
								 		+ " sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
								 		+ " from fc_pp_india fpi, fc_fc3_part1 fp1,  fc_fc3_tally ft "
								 		+ "  where fpi.rcn=fp1.rcn  "
								 		+ " AND fpi.blkyear=ft.blkyear "
								 		+ " and fp1.blkyear=ft.blkyear "
								 		+ " and fpi.rcn = ft.rcn  "
								 		+ " and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn "
								 		+ "  AND "+BlockYearList1+" "
								 		+ " GROUP BY ft.blkyear,substr(fpi.stdist,1,2) ,fpi.asso_religion,fpi.rcn, fpi.asso_name, "
								 		+ " substr(fpi.stdist,-3,3) "
								 		+ "  union all "
								 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion, "
								 		+ "   fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) "
								 		+ "  amount  from fc_india fi, fc_fc3_part1_new fp1,  fc_fc3_tally ft "
								 		+ "where fi.rcn=fp1.rcn  and fp1.blkyear=ft.blkyear   and fi.rcn = ft.rcn   and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn   AND "+BlockYearList1+"  "
								 		+ " group by ft.blkyear, substr(fi.stdist,1,2) , fi.asso_religion,fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) "
								 		+ "  UNION ALL "
								 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
								 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
								 		+ " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) amount "
								 		+ "  from fc_pp_india fpi, fc_fc3_part1_new fp1,  fc_fc3_tally ft "
								 		+ " where fpi.rcn=fp1.rcn   and fpi.blkyear=ft.blkyear  and fp1.blkyear=ft.blkyear   and fpi.rcn = ft.rcn "
								 		+ " and ft.final_submit='Y' "
								 		+ " and ft.rcn = fp1.rcn   AND "+BlockYearList1+" "
								 		+ " GROUP BY ft.blkyear, substr(fpi.stdist,1,2) , fpi.asso_religion,fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) "
								 		+ "   ) t1, TM_STATE S, TM_RELIGION r, tm_district d "
								 		+ " WHERE t1.state = S.scode and rname=r.RELIGION_CODE and district=d.distcode "
								 		+ " and "+StateList+" and "+ReligionList+" "
								 		+ "  ORDER BY t1.blkyear,amount desc ");
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("Religion_Wise_Report", ds);
		     
		     
			String tsPath = "/Reports/ReligionWiseReport.jrxml";
			String fileName = "ReligionWiseReport.pdf";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		
		


}
			
	 


	
	 
	public List<ReligionWiseReport> getReligionWiseReport() {
		return religionWiseReport;
	}
	public void setReligionWiseReport(List<ReligionWiseReport> religionWiseReport) {
		this.religionWiseReport = religionWiseReport;
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
	
	

	public List<String> getSelectReligionList() {
		return selectReligionList;
	}

	public void setSelectReligionList(List<String> selectReligionList) {
		this.selectReligionList = selectReligionList;
	}

	@Override
	protected void generateCSV() throws Exception {

		// TODO Auto-generated method stub
		
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);

	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		 String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		   String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    
		   
		     String BlockYearList1="1=1";
			 String StateList="1=1";
			 String ReligionList="1=1";
			  if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 
			    	 }
			  String selectBlockList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		 		
		    	 parameters.put("selectedBlockYearList",selectBlockList);
			
            if(!stList.trim().equals("'ALL'")){
	            	   StateList= "state in ("+stList+")";
	            	   
					}
           
            
            if(!selectedReligionList.trim().equals("'ALL'")){
         	   ReligionList= "rname in  ("+selectedReligionList+")";
         	  			}
            
            ReligionTypeDao rdo=new ReligionTypeDao(connection);
				parameters.put("selectedReligionList",rdo.getReligion(selectedReligionList));
				
            StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState(stList));
			     
          
	    
				reportQuery = (" SELECT blkyear,r.RELIGION_DESC religion, rcn, asso_name, sname, distname, amount FROM ( "
								 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion rname, "
								 		+ " fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
								 		+ " from fc_india fi, fc_fc3_part1 fp1, fc_fc3_tally ft "
								 		+ "  where fi.rcn=fp1.rcn "
								 		+ " and fp1.blkyear=ft.blkyear "
								 		+ " and fi.rcn = ft.rcn "
								 		+ " and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn "
								 		+ " AND "+BlockYearList1+" "
								 		+ "  GROUP BY ft.blkyear, substr(fi.stdist,1,2), fi.asso_religion,fi.rcn,"
								 		+ " fi.asso_name, substr(fi.stdist,-3,3) "
								 		+ "  UNION ALL "
								 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
								 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
								 		+ " sum(fp1.for_amt+fp1.bk_int+fp1.oth_int) amount "
								 		+ " from fc_pp_india fpi, fc_fc3_part1 fp1,  fc_fc3_tally ft "
								 		+ "  where fpi.rcn=fp1.rcn  "
								 		+ " AND fpi.blkyear=ft.blkyear "
								 		+ " and fp1.blkyear=ft.blkyear "
								 		+ " and fpi.rcn = ft.rcn  "
								 		+ " and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn "
								 		+ "  AND "+BlockYearList1+" "
								 		+ " GROUP BY ft.blkyear,substr(fpi.stdist,1,2) ,fpi.asso_religion,fpi.rcn, fpi.asso_name, "
								 		+ " substr(fpi.stdist,-3,3) "
								 		+ "  union all "
								 		+ " select ft.blkyear, substr(fi.stdist,1,2) state, fi.asso_religion, "
								 		+ "   fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) district, sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) "
								 		+ "  amount  from fc_india fi, fc_fc3_part1_new fp1,  fc_fc3_tally ft "
								 		+ "where fi.rcn=fp1.rcn  and fp1.blkyear=ft.blkyear   and fi.rcn = ft.rcn   and ft.final_submit='Y' "
								 		+ " AND ft.rcn = fp1.rcn   AND "+BlockYearList1+"  "
								 		+ " group by ft.blkyear, substr(fi.stdist,1,2) , fi.asso_religion,fi.rcn, fi.asso_name, substr(fi.stdist,-3,3) "
								 		+ "  UNION ALL "
								 		+ " select ft.blkyear, substr(fpi.stdist,1,2) state, fpi.asso_religion as rname,"
								 		+ " fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) district, "
								 		+ " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) amount "
								 		+ "  from fc_pp_india fpi, fc_fc3_part1_new fp1,  fc_fc3_tally ft "
								 		+ " where fpi.rcn=fp1.rcn   and fpi.blkyear=ft.blkyear  and fp1.blkyear=ft.blkyear   and fpi.rcn = ft.rcn "
								 		+ " and ft.final_submit='Y' "
								 		+ " and ft.rcn = fp1.rcn   AND "+BlockYearList1+" "
								 		+ " GROUP BY ft.blkyear, substr(fpi.stdist,1,2) , fpi.asso_religion,fpi.rcn, fpi.asso_name, substr(fpi.stdist,-3,3) "
								 		+ "   ) t1, TM_STATE S, TM_RELIGION r, tm_district d "
								 		+ " WHERE t1.state = S.scode and rname=r.RELIGION_CODE and district=d.distcode "
								 		+ " and "+StateList+" and "+ReligionList+" "
								 		+ "  ORDER BY t1.blkyear,amount desc ");
         	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Religion_Wise_Report", ds);
	     
	     
		String tsPath ="/Reports/ReligionWiseReportCSV.jrxml";
		String fileName ="ReligionWiseReport";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	    
		
	}
	
	
	
}
