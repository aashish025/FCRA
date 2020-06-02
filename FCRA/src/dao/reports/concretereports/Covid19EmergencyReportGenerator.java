package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.Covid19EmergencyStateWiseReport;
import models.reports.ReligionWiseReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.ReligionTypeDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class Covid19EmergencyReportGenerator extends MISReportGenerator{
	
	private List<Covid19EmergencyStateWiseReport> covid19Emgstwisereport;
    

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

	public Covid19EmergencyReportGenerator(Connection connection) {
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
		// String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		  //  String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		  // String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    
		   
		    // String BlockYearList1="1=1";
			// String StateList="1=1";
			// String ReligionList="1=1";
			/*  if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 
			    	 }*/
			 // String selectBlockList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		 		
		    	// parameters.put("selectedBlockYearList",selectBlockList);
			
          /*  if(!stList.trim().equals("'ALL'")){
	            	   StateList= "state in ("+stList+")";
	            	   
					}
           */
            
          /*  if(!selectedReligionList.trim().equals("'ALL'")){
         	   ReligionList= "rname in  ("+selectedReligionList+")";
         	  			}*/
            
            /*ReligionTypeDao rdo=new ReligionTypeDao(connection);
				parameters.put("selectedReligionList",rdo.getReligion(selectedReligionList));*/
				
           // StateDao sdao= new StateDao(connection);
			//	parameters.put("selectedStateList",sdao.getState(stList));
			     
          
				
	    
				reportQuery = ("select (SELECT sname FROM TM_STATE WHERE scode = substr(re.stdist,1,2)   )stateName,   (select count(e.rcn) from FC_COVID_RESPONSE_EXEMPTION e "
						+ "where substr(e.stdist,1,2)=substr(re.stdist,1,2) group by substr(re.stdist,1,2)) assorequested,   COUNT(re.rcn) assoresponded,     SUM(re.ESTIMATED_AMOUNT_SPENT) sumspent, "
						+ "  SUM(re.DURATION) duration from FC_COVID_RESPONSE_ENTRY "
						+ "re   GROUP BY (substr(re.stdist,1,2)) ORDER BY stateName");
		
         	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Religion_Wise_Report", ds);
	     
	     
		String tsPath ="/Reports/Covid19EmergencyStateWiseExcel.jrxml";
		String fileName ="Covid19EmergencyStateWise";
 		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	    
	
	


		
		
	}
	@Override
	protected void generateHTML() throws Exception {
		
		covid19Emgstwisereport=getCovid19EmergencyStateWiseReportHtml();	
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
	
	public List<Covid19EmergencyStateWiseReport> getCovid19EmergencyStateWiseReportHtml() throws Exception {
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}
 		     //String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 		  //  String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
 		   //String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    
 		   //  String BlockYearList1="1=1";
			 String StateList="1=1";
			// String ReligionList="1=1";
			 /* if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 }*/
            /*   if(!stList.trim().equals("'ALL'")){
            	   StateList= "state in ("+stList+")";
								}
               */
            /*   if(!selectedReligionList.trim().equals("'ALL'")){
            	   ReligionList= "rname in  ("+selectedReligionList+")";
								}*/
								
								StringBuffer query; 
								
								
								 query = new StringBuffer("select (SELECT sname FROM TM_STATE WHERE scode = substr(re.stdist,1,2)   )stateName,   (select count(e.rcn) from FC_COVID_RESPONSE_EXEMPTION e where substr(e.stdist,1,2)=substr(re.stdist,1,2) group by substr(re.stdist,1,2)) assorequested,   COUNT(re.rcn) assoresponded,     SUM(re.ESTIMATED_AMOUNT_SPENT) sumspent,   SUM(re.DURATION) duration from FC_COVID_RESPONSE_ENTRY re   GROUP BY (substr(re.stdist,1,2)) ORDER BY stateName "
								 		);
						  StringBuffer countQuery = new StringBuffer("SELECT COUNT(1) FROM ("+query+")");
						  System.out.println("query---"+query);
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
								 System.out.println("Ddddddddddd---"+rs.getFetchSize());
								List<Covid19EmergencyStateWiseReport> covid19EmergencyStateWiseReportList = new ArrayList<Covid19EmergencyStateWiseReport>();
								while(rs.next()) {
									covid19EmergencyStateWiseReportList.add(new Covid19EmergencyStateWiseReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6))); 
									
								}
								System.out.println("covid19Emgstwisereport----- "+covid19EmergencyStateWiseReportList);
							return covid19EmergencyStateWiseReportList;
								
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
			 
	 		//  String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
	 		  //  String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
	 		 //  String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			    
	 		   
	 		   //  String BlockYearList1="1=1";
				 String StateList="1=1";
				// String ReligionList="1=1";
				 /* if(!selectBlockyearList.trim().equals("'ALL'")){
				    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
				    	 
				    	 }
				  String selectBlockList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
			 		
			    	 parameters.put("selectedBlockYearList",selectBlockList);*/
				
	               /*if(!stList.trim().equals("'ALL'")){
		            	   StateList= "state in ("+stList+")";
		            	   
						}*/
	              
	               
	              /* if(!selectedReligionList.trim().equals("'ALL'")){
	            	   ReligionList= "rname in  ("+selectedReligionList+")";
	            	  			}*/
	               
	              /* ReligionTypeDao rdo=new ReligionTypeDao(connection);
					parameters.put("selectedReligionList",rdo.getReligion(selectedReligionList));*/
					
	               /*StateDao sdao= new StateDao(connection);
					parameters.put("selectedStateList",sdao.getState(stList));*/
				     
	             
					
		    
					reportQuery = ("select (SELECT sname FROM TM_STATE WHERE scode = substr(re.stdist,1,2)   )stateName, "
							+ "  (select count(e.rcn) from FC_COVID_RESPONSE_EXEMPTION e where substr(e.stdist,1,2)=substr(re.stdist,1,2) group by substr(re.stdist,1,2)) assorequested,   "
							+ " COUNT(re.rcn) assoresponded,     "
							+ " SUM(re.ESTIMATED_AMOUNT_SPENT) sumspent,   "
							+ " SUM(re.DURATION) duration from FC_COVID_RESPONSE_ENTRY re   GROUP BY (substr(re.stdist,1,2)) ORDER BY stateName ");
		 		
					ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				System.out.println("Query"+reportQuery);
					parameters.put("Religion_Wise_Report", ds);
		     
		     
			String tsPath = "/Reports/Covid19EmergencyStateWise.jrxml";
			String fileName = "Covid19EmergencyStateWise.pdf";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		
		


}
			
	 

	 public List<Covid19EmergencyStateWiseReport> getCovid19Emgstwisereport() {
			return covid19Emgstwisereport;
		}

		public void setCovid19Emgstwisereport(List<Covid19EmergencyStateWiseReport> covid19Emgstwisereport) {
			this.covid19Emgstwisereport = covid19Emgstwisereport;
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
		// String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		//    String selectBlockyearList = selectBlockYear.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		  // String selectedReligionList = selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		    
		   
		//     String BlockYearList1="1=1";
			 String StateList="1=1";
			// String ReligionList="1=1";
			 /* if(!selectBlockyearList.trim().equals("'ALL'")){
			    	 BlockYearList1= " ft.blkyear IN ("+selectBlockyearList+")";
			    	 
			    	 }*/
			//  String selectBlockList = selectBlockYear.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		 		
		    	// parameters.put("selectedBlockYearList",selectBlockList);
			
           /* if(!stList.trim().equals("'ALL'")){
	            	   StateList= "state in ("+stList+")";
	            	   
					}*/
           
            
        /*    if(!selectedReligionList.trim().equals("'ALL'")){
         	   ReligionList= "rname in  ("+selectedReligionList+")";
         	  			}*/
            
           /* ReligionTypeDao rdo=new ReligionTypeDao(connection);
				parameters.put("selectedReligionList",rdo.getReligion(selectedReligionList));*/
				
          //  StateDao sdao= new StateDao(connection);
			//	parameters.put("selectedStateList",sdao.getState(stList));
			     
          
	    
				reportQuery = ("select (SELECT sname FROM TM_STATE WHERE scode = substr(re.stdist,1,2)   )stateName,  "
						+ " (select count(e.rcn) from FC_COVID_RESPONSE_EXEMPTION e where substr(e.stdist,1,2)=substr(re.stdist,1,2) group by "
						+ "substr(re.stdist,1,2)) assorequested,   COUNT(re.rcn) assoresponded,     SUM(re.ESTIMATED_AMOUNT_SPENT) sumspent,   "
						+ "SUM(re.DURATION) duration from FC_COVID_RESPONSE_ENTRY re   GROUP BY (substr(re.stdist,1,2)) ORDER BY stateName");
         	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Religion_Wise_Report", ds);
	     
	     
		String tsPath ="/Reports/Covid19EmergencyStateWiseCSV.jrxml";
		String fileName ="Covid19EmergencyStateWise";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	    
		
	}
	
	
	
}
