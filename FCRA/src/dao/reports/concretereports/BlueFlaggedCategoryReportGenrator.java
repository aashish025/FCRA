package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.BlueFlaggedCategoryReport;
import models.reports.SuddenRiseIncomeReport;
import models.reports.RedFlaggedCategoryReport;
import models.reports.UserActivityReport;

import org.apache.poi.ss.formula.udf.UDFFinder;
import org.springframework.util.MultiValueMap;

import com.sun.org.apache.bcel.internal.generic.NEW;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.ServicesDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;
import dao.services.RedFlagAssociationsDao;

public class BlueFlaggedCategoryReportGenrator  extends MISReportGenerator{
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	
	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	private int virtualizationMaxSize =200;
	private String loginUserName;
	private String loginOfficeName;
	private String activityFormate;
	private String myLoginofficecode;
	private int fromLastyears;
	private String cureentBlockYear;
	private String blkyrList;
	private int ratioTamt_avgAmt;
	private List<String> selectStateList;
	private List<String> selectServiceList;
	private String reportDisplayType;
	private String reportStatusDisplayType;
	private List<SuddenRiseIncomeReport> suddenRiseInIncomeList;
	private List<BlueFlaggedCategoryReport> blueFlaggedCategoryList;
	public BlueFlaggedCategoryReportGenrator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    String reportQuery="";
	    if(reportDisplayType.equalsIgnoreCase("s")){
	        	   if(reportStatusDisplayType.equalsIgnoreCase("l")){
	        		   // for live red flagged
		        	   reportQuery = " with temp as (  select tbl1.rcn as rcn,tbl4.STDIST AS SS,"
		        	   		+ " ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date  "
		        	   		+ "	  from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
							+ "  where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
							+ "  temp2 as (select rcn, substr(SS,1,2) AS STATE from temp where last_update=1 ) "
							+ "	  SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=TEMP2.STATE) AS STATE,COUNT(TEMP2.RCN) AS COUNT1 FROM TEMP2 GROUP BY STATE  ORDER BY STATE "; 	        		   
	        	   }
	          	   else{
	        		   // for removed red flagged	
		        	   reportQuery = "WITH temp AS (SELECT tbl1.rcn AS rcn,fc.STDIST AS AB, ROW_NUMBER() over(PARTITION BY tbl1.rcn order by tbl1.STATUS_DATE DESC) AS last_update "
		        	   		+ " FROM T_BLUE_FLAG_STATUS_HISTORY tbl1,FC_INDIA fc  WHERE tbl1.RCN NOT IN (SELECT rcn FROM T_BLUE_FLAGGED_ASSOCIATIONS  ) "
		        	   		+ " AND tbl1.status=0 and tbl1.rcn=fc.rcn ),T2 AS (SELECT RCN,substr(AB,1,2) AS STATE FROM temp WHERE last_update=1 ) "
		        	   		+ "SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=T2.STATE) AS STATE,COUNT(T2.RCN) AS COUNT1 FROM T2 GROUP BY STATE  ORDER BY STATE ";
								 	        		   	        		   
	        	   }
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);
		    }
		    else {
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){ 
		    	// l means small L for Live red flagged cases
		    		 reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks,"
								+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by,"
								+ "ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date "
								+ " from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
								+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
								+ " temp2 as (select rcn, asso_name,  status_date , remarks, action_by from temp where last_update=1 ) "
								+ " select rcn, asso_name, to_char(status_date,'dd-mm-yyyy') as status_date,remarks, action_by from temp where last_update=1  ";
				    	}
				    	
				    	//Removed rcn wise
				    	else {
							 reportQuery = " with temp as (  "
										+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name,"
										+ " to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS, "
										+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by "
										+ " , ROW_NUMBER() over(PARTITION by tbl1.rcn order by tbl1.STATUS_DATE desc) as last_update  from T_BLUE_FLAG_STATUS_HISTORY tbl1"
										+ " where RCN NOT IN (select rcn from T_BLUE_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
										+ " select rcn, asso_name, status_date, remarks, action_by  from temp where last_update=1 order by rcn ";
				    	}
		    	ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    	
		    }
					
			String tsPath = "/Reports/BlueFlaggedAssociationsCategoryWise.jrxml";
			String fileName = "BlueFlaggedAssociations";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,fileName);// TODO Auto-generated method stub	    	
	    
	  
			
			  		    	
	    
	}
	
	@Override
	protected void generateExcel() throws Exception {
		Map  parameters = new HashMap();
		//parameters.put("reportType", reportType);
		//parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    String  subQueryServices="1=1";
	    String reportQuery="";
	
		    if(reportDisplayType.equalsIgnoreCase("s")){
	        	   if(reportStatusDisplayType.equalsIgnoreCase("l")){
	        		   // for live red flagged
	        		   reportQuery = " with temp as (  select tbl1.rcn as rcn,tbl4.STDIST AS SS,"
			        	   		+ " ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date  "
			        	   		+ "	  from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
								+ "  where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
								+ "  temp2 as (select rcn, substr(SS,1,2) AS STATE from temp where last_update=1 ) "
								+ "	  SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=TEMP2.STATE) AS STATE,COUNT(TEMP2.RCN) AS COUNT1 FROM TEMP2 GROUP BY STATE  ORDER BY STATE "; 	        		   
	        	   }
	          	   else{
	        		   // for removed red flagged	
	          		  reportQuery = "WITH temp AS (SELECT tbl1.rcn AS rcn,fc.STDIST AS AB, ROW_NUMBER() over(PARTITION BY tbl1.rcn order by tbl1.STATUS_DATE DESC) AS last_update "
			        	   		+ " FROM T_BLUE_FLAG_STATUS_HISTORY tbl1,FC_INDIA fc  WHERE tbl1.RCN NOT IN (SELECT rcn FROM T_BLUE_FLAGGED_ASSOCIATIONS  ) "
			        	   		+ " AND tbl1.status=0 and tbl1.rcn=fc.rcn ),T2 AS (SELECT RCN,substr(AB,1,2) AS STATE FROM temp WHERE last_update=1 ) "
			        	   		+ "SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=T2.STATE) AS STATE,COUNT(T2.RCN) AS COUNT1 FROM T2 GROUP BY STATE   ORDER BY STATE ";
								 	        		   	        		   
	        	   }

				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);
		    }
		    else {
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){ 
		    	// l means small L for Live red flagged cases
				 reportQuery = " with temp as (  "
						+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks,"
						+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by,"
						+ "ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date "
						+ " from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
						+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
						+ " temp2 as (select rcn, asso_name,  status_date , remarks, action_by from temp where last_update=1 ) "
						+ " select rcn, asso_name, to_char(status_date,'dd-mm-yyyy') as status_date,remarks, action_by from temp where last_update=1  ";
		    	}
		    	
		    	//Removed rcn wise
		    	else {
					 reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name,"
								+ " to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS, "
								+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by "
								+ " , ROW_NUMBER() over(PARTITION by tbl1.rcn order by tbl1.STATUS_DATE desc) as last_update  from T_BLUE_FLAG_STATUS_HISTORY tbl1"
								+ " where RCN NOT IN (select rcn from T_BLUE_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select rcn, asso_name, status_date, remarks, action_by  from temp where last_update=1 order by rcn ";
		    	}
			
		    	ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    	
		    }
					
			String tsPath = "/Reports/BlueFlaggedAssociationsCategoryWise.jrxml";
			String fileName = "BlueFlaggedAssociations";
			GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection,fileName);// TODO Auto-generated method stub	    	
	    
	    	
	}

	@Override
	protected void generateHTML() throws Exception {
		
			if(reportDisplayType.equalsIgnoreCase("s"))
	        {
			blueFlaggedCategoryList=getBlueFlaggedCategoryStatistics();	
			totalRecords=getTotalRecords();	
			
		     }
			else {
				blueFlaggedCategoryList=getBlueFlaggedCategoryDetailed();
				totalRecords=getTotalRecords();	
			}

	}
private List<BlueFlaggedCategoryReport> getBlueFlaggedCategoryDetailed() throws Exception{
 		List<BlueFlaggedCategoryReport> blueFlaggedCategoryList = new ArrayList<BlueFlaggedCategoryReport>();
		   	if(connection == null) {
			throw new Exception("Invalid connection");
		}

		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			// for Live Cases, here  l means small L
			String countQuery = " with temp as ( select tbl1.rcn as rcn , tbl4.asso_name as asso_name, tbl1.remarks, tbl1.action_by,"
					+ "     ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date "
					+ "  from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_Blue_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
					+ "  where tbl1.status='0' and tbl1.rcn=tbl3.rcn and tbl4.rcn=tbl3.rcn) "
					+ "  select count(*) from temp where last_update=1 ";

				
			String query =" with temp as ( select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks,"
					+ "    (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by,"
					+ "   ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date "
					+ "  from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
					+ "  where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
					+ "  temp2 as (select rcn, asso_name,  to_char(status_date,'dd-mm-yyyy'), remarks, action_by from temp where last_update=1 ), "
					+ "  P2 as (select temp2.*, ROWNUM RN from temp2 ) select * from P2 where RN BETWEEN ? and ? ";

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
				 statement = connection.prepareStatement(query.toString());
				 if(pageNum == null || recordsPerPage == null) {
		
				 }
				 else {
				  statement.setInt(1, (pageRequested-1) * pageSize + 1);
				  statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				 }
			
				 rs = statement.executeQuery();
			
				while(rs.next()) {
					blueFlaggedCategoryList.add(new BlueFlaggedCategoryReport(rs.getString(1),rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5)));
					
				}
				
		}
		else{
			// for removed cases
			String countQuery = " with temp as (  select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name,"
					+ "  to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS, " 
					+ "  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by "
					+ " ,(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rownumber "
					+ "    from T_Blue_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_Blue_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
					 + "   select count(*)  from temp where  rownumber=1 order by rcn ";
					
  			String query =" with temp as ( select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, "
  					+ "   to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  "
  					+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by "
					+ "  ,(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rownumber "
					+ "  from T_BLUE_FLAG_STATUS_HISTORY tbl1 "
					+ "  where RCN NOT IN (select rcn from T_BLUE_FLAGGED_ASSOCIATIONS) and  tbl1.status=0), "
					 + " temp2 as (select rcn, asso_name, status_date, remarks, action_by from temp where  rownumber=1 order by rcn), "
					 + " P2 as (select temp2.*, ROWNUM RN from temp2 ) select * from P2 where RN BETWEEN ? and ? ";

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
				 statement = connection.prepareStatement(query.toString());
				 if(pageNum == null || recordsPerPage == null) {
		
				 }
				 else {
				  statement.setInt(1, (pageRequested-1) * pageSize + 1);
				  statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				 }
			
				 rs = statement.executeQuery();
				
				while(rs.next()) {
					blueFlaggedCategoryList.add(new BlueFlaggedCategoryReport(rs.getString(1),rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5)));
					
				}			
		} 
		return blueFlaggedCategoryList;
	}
	
	private List<BlueFlaggedCategoryReport> getBlueFlaggedCategoryStatistics() throws Exception{
		Map  parameters = new HashMap();
		List<BlueFlaggedCategoryReport> blueFlaggedCategoryList = new ArrayList<BlueFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	  
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
	    if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    	// for Live red flag cases
				String countQuery = " SELECT COUNT(*) FROM (with temp as (  select tbl1.rcn as rcn,tbl4.STDIST AS SS,"
						+ " ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date "
						+ " from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4  "
						+ "	 where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
						+ "  temp2 as (select rcn, substr(SS,1,2) AS STATE from temp where last_update=1 ) "
						+ "	 SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=TEMP2.STATE) AS STATE,COUNT(TEMP2.RCN) AS COUNT1 FROM TEMP2 GROUP BY STATE  ORDER BY STATE) ";
				 
				
				 String query ="WITH temp AS  (SELECT tbl1.rcn AS rcn, tbl4.STDIST AS SS, ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date DESC) AS last_update,"
				 		+ "    tbl1.status_date  FROM T_BLUE_FLAG_STATUS_HISTORY tbl1,  T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 WHERE tbl1.status='0' "
				 		+ "  AND tbl4.rcn     =tbl3.rcn  AND tbl1.rcn     =tbl3.rcn ), temp2 AS (SELECT rcn, SUBSTR(SS,1,2) AS STATE FROM temp WHERE last_update=1 ), "
				 		+ " t3 as(SELECT(SELECT SNAME FROM TM_STATE WHERE SCODE=TEMP2.STATE)  AS STATE, COUNT(TEMP2.RCN) AS COUNT1 FROM TEMP2 GROUP BY STATE  ORDER BY STATE) " 
				 		+ "  ,t4 as(select t3.* ,ROWNUM RN FROM T3) SELECT * FROM T4 WHERE RN BETWEEN ? AND  ? ";

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
					 statement = connection.prepareStatement(query.toString());
					 if(pageNum == null || recordsPerPage == null) {
			
					 }
					 else {
					 statement.setInt(1, (pageRequested-1) * pageSize + 1);
					 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
					 }
				
					 rs = statement.executeQuery();
					while(rs.next()) {
						blueFlaggedCategoryList.add(new BlueFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
						
					}	    	
	    }
	    else {
	    	// for removed red flagged cases
			String countQuery = "SELECT COUNT(*) FROM (WITH temp AS (SELECT tbl1.rcn AS rcn,fc.STDIST AS AB, ROW_NUMBER() over(PARTITION BY tbl1.rcn order by tbl1.STATUS_DATE DESC) AS last_update "
					+ " FROM T_BLUE_FLAG_STATUS_HISTORY tbl1,FC_INDIA fc  WHERE tbl1.RCN NOT IN (SELECT rcn FROM T_BLUE_FLAGGED_ASSOCIATIONS  ) "
					+ "AND tbl1.status=0 and tbl1.rcn=fc.rcn ),T2 AS (SELECT RCN,substr(AB,1,2) AS STATE FROM temp WHERE last_update=1 ) "
					+ " SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=T2.STATE) AS STATE,COUNT(T2.RCN) AS COUNT FROM T2 GROUP BY STATE  ORDER BY STATE) ";

			String query ="     WITH temp AS (SELECT tbl1.rcn AS rcn,fc.STDIST AS AB, ROW_NUMBER() over(PARTITION BY tbl1.rcn order by tbl1.STATUS_DATE DESC) AS last_update "
					+ "	FROM T_BLUE_FLAG_STATUS_HISTORY tbl1,FC_INDIA fc  WHERE tbl1.RCN NOT IN (SELECT rcn FROM T_BLUE_FLAGGED_ASSOCIATIONS  ) "
					+ "	AND tbl1.status=0 and tbl1.rcn=fc.rcn ),T2 AS (SELECT RCN,substr(AB,1,2) AS STATE FROM temp WHERE last_update=1 ), "
					+ "	t3 as(SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=T2.STATE) AS STATE,COUNT(T2.RCN) AS COUNT FROM T2 GROUP BY STATE  ORDER BY STATE ) ,"
					+ "	t4 as(select t3.* ,ROWNUM RN FROM T3) SELECT * FROM T4 WHERE RN BETWEEN ? AND  ? ";

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
				 statement = connection.prepareStatement(query.toString());
				 if(pageNum == null || recordsPerPage == null) {
		
				 }
				 else {
				 statement.setInt(1, (pageRequested-1) * pageSize + 1);
				 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);
				 }
			
				 rs = statement.executeQuery();
				while(rs.next()) {
					blueFlaggedCategoryList.add(new BlueFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
					
				}	    		    	
	    }
				
 		return blueFlaggedCategoryList;
								
  }
	
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateCSV() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    String reportQuery="";
	   if(reportDisplayType.equalsIgnoreCase("s")){
	        	   if(reportStatusDisplayType.equalsIgnoreCase("l")){
	        		   // for live red flagged
	        		   reportQuery = " with temp as (  select tbl1.rcn as rcn,tbl4.STDIST AS SS,"
			        	   		+ " ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date  "
			        	   		+ "	  from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
								+ "  where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
								+ "  temp2 as (select rcn, substr(SS,1,2) AS STATE from temp where last_update=1 ) "
								+ "	  SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=TEMP2.STATE) AS STATE,COUNT(TEMP2.RCN) AS COUNT1 FROM TEMP2 GROUP BY STATE "; 	        		          		   
	        	   }
	          	   else{
	        		   // for removed red flagged	
	          		  reportQuery = "WITH temp AS (SELECT tbl1.rcn AS rcn,fc.STDIST AS AB, ROW_NUMBER() over(PARTITION BY tbl1.rcn order by tbl1.STATUS_DATE DESC) AS last_update "
			        	   		+ " FROM T_BLUE_FLAG_STATUS_HISTORY tbl1,FC_INDIA fc  WHERE tbl1.RCN NOT IN (SELECT rcn FROM T_BLUE_FLAGGED_ASSOCIATIONS  ) "
			        	   		+ " AND tbl1.status=0 and tbl1.rcn=fc.rcn ),T2 AS (SELECT RCN,substr(AB,1,2) AS STATE FROM temp WHERE last_update=1 ) "
			        	   		+ "SELECT (SELECT SNAME FROM TM_STATE WHERE SCODE=T2.STATE) AS STATE,COUNT(T2.RCN) AS COUNT1 FROM T2 GROUP BY STATE ";
								 	        		   	        		   
	        	   }

				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);
		    }
		    else {
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){ 
		    	// l means small L for Live red flagged cases
				 reportQuery = " with temp as (  "
						+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks,"
						+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by,"
						+ "ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date "
						+ " from  T_BLUE_FLAG_STATUS_HISTORY tbl1, T_BLUE_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
						+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
						+ " temp2 as (select rcn, asso_name,  status_date , remarks, action_by from temp where last_update=1 ) "
						+ " select rcn, asso_name, to_char(status_date,'dd-mm-yyyy') as status_date,remarks, action_by from temp where last_update=1  ";
		    	}
		    	
		    	//Removed rcn wise
		    	else {
					 reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name,"
								+ " to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS, "
								+ " (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by "
								+ " , ROW_NUMBER() over(PARTITION by tbl1.rcn order by tbl1.STATUS_DATE desc) as last_update  from T_BLUE_FLAG_STATUS_HISTORY tbl1"
								+ " where RCN NOT IN (select rcn from T_BLUE_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select rcn, asso_name, status_date, remarks, action_by  from temp where last_update=1 order by rcn ";
		    	}
		    	ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    	
		    	
		    }
			String tsPath = "/Reports/BlueFlaggedAssociationsCategoryWiseCsv.jrxml";
			String fileName = "BlueFlaggedAssociationss";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,fileName);// TODO Auto-generated method stub	    	
	    
		    
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
			reportDisplayType=parameterMap.get("reportDisplyType").get(0);
			reportStatusDisplayType = parameterMap.get("reportStatusDisplyType").get(0);
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		    loginUserName=parameterMap.get("loginUserName").get(0);
		}
		
			myLoginofficecode=parameterMap.get("myLoginOfficCode").toString();

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

	public String getMyLoginofficecode() {
		return myLoginofficecode;
	}

	public void setMyLoginofficecode(String myLoginofficecode) {
		this.myLoginofficecode = myLoginofficecode;
	}

	public List<SuddenRiseIncomeReport> getSuddenRiseInIncomeList() {
		return suddenRiseInIncomeList;
	}

	public void setSuddenRiseInIncomeList(
			List<SuddenRiseIncomeReport> suddenRiseInIncomeList) {
		this.suddenRiseInIncomeList = suddenRiseInIncomeList;
	}

	public List<BlueFlaggedCategoryReport> getBlueFlaggedCategoryList() {
		return blueFlaggedCategoryList;
	}

	public void setBlueFlaggedCategoryList(
			List<BlueFlaggedCategoryReport> blueFlaggedCategoryList) {
		this.blueFlaggedCategoryList = blueFlaggedCategoryList;
	}

	


}
