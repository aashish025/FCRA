package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class RedFlaggedCategoryReportGenrator  extends MISReportGenerator{
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
	private List<RedFlaggedCategoryReport> redFlaggedCategoryList;
	public RedFlaggedCategoryReportGenrator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		//parameters.put("reportType", reportType);
		//parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    //String redFlagStatusSubQuery="";
	    String  subQueryServices="1=1";
	    
	    if(reportDisplayType.equalsIgnoreCase("d")){
		    String redFlagTypeList = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
			if(redFlagTypeList.equals("ALL")){
			   parameters.put("redFlagTypeList",redFlagTypeList);
			}
			else{
			   	RedFlagAssociationsDao rfdao = new RedFlagAssociationsDao(connection);
				//ServicesDao sdao=new ServicesDao(connection);
			    parameters.put("redFlagTypeList",rfdao.getRedFlagtypeList(redFlagTypeList).toString());
		    }
		    subQueryServices="1=1";
		    if(!redFlagTypeList.trim().equals("ALL"))
		    	subQueryServices=" cat_code in ("+redFlagTypeList+")";
		    if(redFlagTypeList.trim().equals("NOCATEGORY"))
		    	subQueryServices=" cat_code is null";	
  
	    }
	    else{  }
    
	    String reportQuery="";
	    
	    if(activityFormate.equals("R")){
	           if(reportDisplayType.equalsIgnoreCase("s")){
	        	   if(reportStatusDisplayType.equalsIgnoreCase("l")){
	        		   // for live red flagged
		        	   reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
								+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3, fc_india tbl4 "
								+ " where tbl1.status='0' and tbl1.rcn=tbl3.rcn and tbl3.rcn=tbl4.rcn)  "
								+ " select cat_desc, count(cat_desc) as count from temp where last_update=1 group by cat_desc order by cat_desc "; 	        		   
	        	   }
	          	   else{
	        		   // for removed red flagged	
		        	   reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
								+ ", (row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select cat_desc, count (cat_desc) as count from temp where rn=1 group by cat_desc  ";
								 	        		   	        		   
	        	   }
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);
		    }
		    else {
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){ 
		    	// l means small L for Live red flagged cases
				 reportQuery = " with temp as (  "
						+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks, (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc ,ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
						+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
						+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
						+ " temp2 as (select rcn, asso_name, cat_desc, cat_code, status_date , remarks, action_by from temp where last_update=1 order by cat_desc) "
						+ " select rcn, asso_name, cat_desc, to_char(status_date,'dd-mm-yyyy') as status_date,remarks, action_by from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";
		    	}
		    	
		    	//Removed rcn wise
		    	else {
					 reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
								+ " , ROW_NUMBER() over(PARTITION by tbl1.rcn order by tbl1.STATUS_DATE desc) as last_update  from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select rcn, asso_name, cat_desc, status_date, remarks, action_by  from temp where "+subQueryServices+"  and last_update=1 order by rcn ";
		    	}
		    	ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    	
		    }
					
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryWise.jrxml";
			String fileName = "RedFlaggedAssociations-List-RCN-Wise";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    	
	    }
	    else if(activityFormate.equals("A")){
	    	if(reportDisplayType.equalsIgnoreCase("s")){
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			// l means samll L for live red flagged cases
		    		reportQuery =" with temp as ( "
							+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,tbl1.STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
							+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
							+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0) "
							+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";
    			
	    		}
	    		else {
	    			// for removed redflag associations
		    		reportQuery =" with temp as ( "
							+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
							+  " , (row_number() over (partition by tbl1.ASSO_ID order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
							+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
							+  " select cat_desc, count(cat_desc) as count from temp  where rn=1 group  by cat_desc ";
	    		}
	    		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
	    	}
	    	else {
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			// l means samll L for live red flagged cases 
	    		reportQuery =" with temp as ( "
						+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,(select sname from tm_state where tm_state.scode=tbl1.STATE) as STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
						+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
						+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0 ) "
						+  " select asso_name,cat_desc, (case when state is null then '  ' else state end) as state, remarks, status_date,action_by  from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";
	    		}
	    		// for removed one for associationwise
	    		else {
	    			reportQuery = " with temp as ( "
	    					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,to_char(tbl1.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
							+  "  , ROW_NUMBER() over(PARTITION by tbl1.ASSO_ID order by tbl1.STATUS_DATE desc) as rn from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
	    					+ " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "  
	    					+  " select asso_name, cat_desc, (case when state is null then '  ' else state end) as state,remarks, status_date, action_by  from temp where "+subQueryServices+" and rn=1 order by asso_name ";
	    		}
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
	    	}
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryAssonameWise.jrxml";
			String fileName = "RedFlagged-List-Assoname-Wise";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    	
	    }
	    else {
	    	if(reportDisplayType.equalsIgnoreCase("s")){
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			//for live red flagged
		    		reportQuery =" with temp as ( "
							+  " select tbl1.DONOR_NAME,tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, tbl1.COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS,tbl2.red_flag_category as cat_code "
							+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
							+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.record_status=0) "
							+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";	    			
	    		}
	    		else{
	    			// for removed redflagged    		
	    			reportQuery =" with temp as ( "
							+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by,tbl1.red_flag_category as cat_code "
							+  "  ,(row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
							+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
							+  " select cat_desc, Count(cat_desc) as count from temp where rn=1 group by cat_desc ";	    				    			
	    		}
			
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
	    	}
	    	else {
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    		reportQuery =" with temp as ( "
	    				+  " select tbl1.DONOR_NAME, tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, (select ctr_name from tm_country where tm_country.CTR_CODE=tbl1.COUNTRY) as COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS, tbl2.red_flag_category as cat_code "
	    				+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
	    				+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.RECORD_STATUS=0) "
	    				+  " select donor_name, cat_desc, (case when country is null then '   ' else country end) as country,remarks, to_char(status_date,'dd-mm-yyyy') as status_date, action_by from temp where last_update=1 and "+subQueryServices+"  order by cat_desc ";
	    		}
	    		// for removed one for donornamewise 
	    		else {
		    		reportQuery =" with temp as( "
		    				+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
		    				+  " , ROW_NUMBER() over(PARTITION by tbl1.DONOR_ID order by tbl1.STATUS_DATE desc) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
		    				+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
		    				+  " select donor_name, cat_desc,(case when country is null then '   ' else country end) as country, remarks, to_char(STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,action_by from temp where "+subQueryServices+" and rn=1  order by donor_name ";
	    		}
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
	    	}
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryDonornameWise.jrxml";
			String fileName = "RedFlagged-List-Donorname-Wise";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    	
	    }
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
	    if(reportDisplayType.equalsIgnoreCase("d")){
		    String redFlagTypeList = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
			if(redFlagTypeList.equals("ALL")){
			   parameters.put("redFlagTypeList",redFlagTypeList);
			}
			else{
			   	RedFlagAssociationsDao rfdao = new RedFlagAssociationsDao(connection);
				//ServicesDao sdao=new ServicesDao(connection);
			    parameters.put("redFlagTypeList",rfdao.getRedFlagtypeList(redFlagTypeList).toString());
		    }
		    subQueryServices="1=1";
		    if(!redFlagTypeList.trim().equals("ALL"))
		    	subQueryServices=" cat_code in ("+redFlagTypeList+")";
		    if(redFlagTypeList.trim().equals("NOCATEGORY"))
		    	subQueryServices=" cat_code is null";	    	    	
	    }
	    else{}	    	    
	    
	    String reportQuery="";
	    if(activityFormate.equals("R")){
		    if(reportDisplayType.equalsIgnoreCase("s")){
	        	   if(reportStatusDisplayType.equalsIgnoreCase("l")){
	        		   // for live red flagged
		        	   reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
								+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3, fc_india tbl4 "
								+ " where tbl1.status='0' and tbl1.rcn=tbl3.rcn and tbl3.rcn=tbl4.rcn)  "
								+ " select cat_desc, count(cat_desc) as count from temp where last_update=1 group by cat_desc order by cat_desc "; 	        		   
	        	   }
	          	   else{
	        		   // for removed red flagged	
		        	   reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
								+ " ,(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select cat_desc, count (cat_desc) as count from temp  where rn=1 group by cat_desc  ";
								 	        		   	        		   
	        	   }

				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);
		    }
		    else {
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){ 
		    	// l means small L for Live red flagged cases
				 reportQuery = " with temp as (  "
						+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks, (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc ,ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
						+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
						+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
						+ " temp2 as (select rcn, asso_name, cat_desc, cat_code, status_date , remarks, action_by from temp where last_update=1 order by cat_desc) "
						+ " select rcn, asso_name, cat_desc, to_char(status_date,'dd-mm-yyyy') as status_date,remarks, action_by from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";
		    	}
		    	
		    	//Removed rcn wise
		    	else {
					 reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
								+ " , ROW_NUMBER() over(PARTITION by tbl1.rcn order by tbl1.STATUS_DATE desc) as last_update  from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select rcn, asso_name, cat_desc, status_date, remarks, action_by  from temp where "+subQueryServices+"  and last_update=1 order by rcn ";
		    	}
			
		    	ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    	
		    }
					
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryWise.jrxml";
			String fileName = "RedFlaggedAssociations-List-RCN-Wise";
			GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    	
	    }
	    else if(activityFormate.equals("A")){
	    	if(reportDisplayType.equalsIgnoreCase("s")){
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			// l means samll L for live red flagged cases
		    		reportQuery =" with temp as ( "
							+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,tbl1.STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
							+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
							+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0 ) "
							+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";
    			
	    		}
	    		else {
	    			// for removed redflag associations
		    		reportQuery =" with temp as ( "
							+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
							+  " ,(row_number() over (partition by tbl1.ASSO_ID order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
							+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
							+  " select cat_desc, count(cat_desc) as count from temp where rn=1 group by cat_desc ";
	    		}
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
	    	}
	    	else {
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			// l means samll L for live red flagged cases 
	    		reportQuery =" with temp as ( "
						+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,(select sname from tm_state where tm_state.scode=tbl1.STATE) as STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
						+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
						+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0 ) "
						+  " select asso_name,cat_desc, (case when state is null then '  ' else state end) as state, remarks, status_date,action_by  from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";
	    		}
	    		// for removed one for associationwise
	    		else {
	    			reportQuery = " with temp as ( "
	    					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,to_char(tbl1.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
							+  " , ROW_NUMBER() over(PARTITION by tbl1.ASSO_ID order by tbl1.STATUS_DATE desc) as rn  from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
	    					+ " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "  
	    					+  " select asso_name, cat_desc, (case when state is null then '  ' else state end) as state,remarks, status_date, action_by  from temp where "+subQueryServices+"  and rn=1 order by asso_name ";
	    		}
	    		
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
	    	}
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryAssonameWise.jrxml";
			String fileName = "RedFlagged-List-Assoname-Wise";
			GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    	
	    }
	    else {
	    	if(reportDisplayType.equalsIgnoreCase("s")){
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){
		    			//for live red flagged
			    		reportQuery =" with temp as ( "
								+  " select tbl1.DONOR_NAME,tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, tbl1.COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS,tbl2.red_flag_category as cat_code "
								+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
								+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.record_status=0) "
								+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";	    			
		    		}
		    		else{
		    			// for removed redflagged    		
		    			reportQuery =" with temp as ( "
								+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by,tbl1.red_flag_category as cat_code "
								+  "  ,(row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
								+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
								+  " select cat_desc, Count(cat_desc) as count from temp  where rn=1 group by cat_desc ";	    				    			
		    		}
		
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
	    	}
	    	else {
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    		reportQuery =" with temp as ( "
	    				+  " select tbl1.DONOR_NAME, tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, (select ctr_name from tm_country where tm_country.CTR_CODE=tbl1.COUNTRY) as COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS, tbl2.red_flag_category as cat_code "
	    				+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
	    				+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.RECORD_STATUS=0) "
	    				+  " select donor_name, cat_desc, (case when country is null then '   ' else country end) as country,remarks, to_char(status_date,'dd-mm-yyyy') as status_date, action_by from temp where last_update=1 and "+subQueryServices+"  order by cat_desc ";
	    		}
	    		// for removed one for donornamewise 
	    		else {
		    		reportQuery =" with temp as( "
		    				+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
		    				+  " , ROW_NUMBER() over(PARTITION by tbl1.DONOR_ID order by tbl1.STATUS_DATE desc) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
		    				+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
		    				+  " select donor_name, cat_desc,(case when country is null then '   ' else country end) as country, remarks, to_char(STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,action_by from temp where "+subQueryServices+"  and rn=1 order by donor_name ";
	    		}

				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
	    	}
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryDonornameWise.jrxml";
			String fileName = "RedFlagged-List-Donorname-Wise";
			GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    	
	    }
	}

	@Override
	protected void generateHTML() throws Exception {
/*		redFlaggedCategoryList=getRedFlaggedCategoryAssociationList();
		totalRecords=getTotalRecords();   */
		if(activityFormate.equals("R")){
			if(reportDisplayType.equalsIgnoreCase("s"))
	        {
				redFlaggedCategoryList=getRedFlaggedCategoryAssociationListStatistics();	
				totalRecords=getTotalRecords();	
			
		     }
			else {
				redFlaggedCategoryList=getRedFlaggedCategoryAssociationListDetailed();
				totalRecords=getTotalRecords();	
			}
			
		}
		else if(activityFormate.equals("A")){
			if(reportDisplayType.equalsIgnoreCase("s"))
	        {
				redFlaggedCategoryList=getAssonamewiseRedFlaggedCategoryAssociationListStatistics();	
				totalRecords=getTotalRecords();	
			
		     }
			else {
				redFlaggedCategoryList=getAssonamewiseRedFlaggedCategoryAssociationListDetailed();
				totalRecords=getTotalRecords();	
			}
		}
		else{
			if(reportDisplayType.equalsIgnoreCase("s"))
	        {
				redFlaggedCategoryList=getDonornamewiseRedFlaggedCategoryAssociationListStatistics();	
				totalRecords=getTotalRecords();	
			
		     }
			else {
				redFlaggedCategoryList=getDonornamewiseRedFlaggedCategoryAssociationListDetailed();
				totalRecords=getTotalRecords();	
			}
			
		}

	}
	
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateCSV() throws Exception {
		Map  parameters = new HashMap();
		//parameters.put("reportType", reportType);
		//parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplayType);
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    
	    String  subQueryServices="1=1";
	    if(reportDisplayType.equalsIgnoreCase("d")){
		    String redFlagTypeList = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
			if(redFlagTypeList.equals("ALL")){
			   parameters.put("redFlagTypeList",redFlagTypeList);
			}
			else{
			   	RedFlagAssociationsDao rfdao = new RedFlagAssociationsDao(connection);
				//ServicesDao sdao=new ServicesDao(connection);
			    parameters.put("redFlagTypeList",rfdao.getRedFlagtypeList(redFlagTypeList).toString());
		    }
		    subQueryServices="1=1";
		    if(!redFlagTypeList.trim().equals("ALL"))
		    	subQueryServices=" cat_code in ("+redFlagTypeList+")";
		    if(redFlagTypeList.trim().equals("NOCATEGORY"))
		    	subQueryServices=" cat_code is null";	    	    	
	    }
	    else{} 
	    
	    String reportQuery="";
	    if(activityFormate.equals("R")){
		    if(reportDisplayType.equalsIgnoreCase("s")){
	        	   if(reportStatusDisplayType.equalsIgnoreCase("l")){
	        		   // for live red flagged
		        	   reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
								+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3, fc_india tbl4 "
								+ " where tbl1.status='0' and tbl1.rcn=tbl3.rcn and tbl3.rcn=tbl4.rcn)  "
								+ " select cat_desc, count(cat_desc) as count from temp where last_update=1 group by cat_desc order by cat_desc "; 	        		   
	        	   }
	          	   else{
	        		   // for removed red flagged	
		        	   reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
								+ " ,(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select cat_desc, count (cat_desc) as count from temp  where rn=1 group by cat_desc  ";
								 	        		   	        		   
	        	   }

				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);
		    }
		    else {
		    	if(reportStatusDisplayType.equalsIgnoreCase("l")){ 
		    	// l means small L for Live red flagged cases
				 reportQuery = " with temp as (  "
						+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks, (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc ,ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
						+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
						+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
						+ " temp2 as (select rcn, asso_name, cat_desc, cat_code, status_date , remarks, action_by from temp where last_update=1 order by cat_desc) "
						+ " select rcn, asso_name, cat_desc, to_char(status_date,'dd-mm-yyyy') as status_date,remarks, action_by from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";
		    	}
		    	
		    	//Removed rcn wise
		    	else {
					 reportQuery = " with temp as (  "
								+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
								+ " , ROW_NUMBER() over(PARTITION by tbl1.rcn order by tbl1.STATUS_DATE desc) as last_update  from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
								+ " select rcn, asso_name, cat_desc, status_date, remarks, action_by  from temp where "+subQueryServices+" and last_update=1 order by rcn ";
		    	}
			
		    	ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    	
		    }
					
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryWiseCsv.jrxml";
			String fileName = "RedFlaggedAssociations-List-RCN-Wise";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    	
	    }
	    else if(activityFormate.equals("A")){
	    	if(reportDisplayType.equalsIgnoreCase("s")){
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			// l means samll L for live red flagged cases
		    		reportQuery =" with temp as ( "
							+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,tbl1.STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
							+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
							+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.RECORD_STATUS=0 ) "
							+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";
    			
	    		}
	    		else {
	    			// for removed redflag associations
		    		reportQuery =" with temp as ( "
							+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
							+  ", (row_number() over (partition by tbl1.ASSO_ID order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
							+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and   tbl1.status=0) "
							+  " select cat_desc, count(cat_desc) as count from temp where rn=1 group by cat_desc ";
	    		}
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
	    	}
	    	else {
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			// l means samll L for live red flagged cases 
	    		reportQuery =" with temp as ( "
						+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,(select sname from tm_state where tm_state.scode=tbl1.STATE) as STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
						+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
						+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0 ) "
						+  " select asso_name,cat_desc, (case when state is null then '  ' else state end) as state, remarks, status_date,action_by  from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";
	    		}
	    		// for removed one for associationwise
	    		else {
	    			reportQuery = " with temp as ( "
	    					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,to_char(tbl1.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
							+  " , ROW_NUMBER() over(PARTITION by tbl1.ASSO_ID order by tbl1.STATUS_DATE desc) as rn  from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
	    					+ " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "  
	    					+  " select asso_name, cat_desc, (case when state is null then '  ' else state end) as state,remarks, status_date, action_by  from temp where "+subQueryServices+" and rn=1 order by asso_name ";
	    		}
	    		
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
	    	}
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryAssonameWiseCsv.jrxml";
			String fileName = "RedFlagged-List-Assoname-Wise";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    	
	    }
	    else {
	    	if(reportDisplayType.equalsIgnoreCase("s")){
	    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    			//for live red flagged
		    		reportQuery =" with temp as ( "
							+  " select tbl1.DONOR_NAME,tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, tbl1.COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS,tbl2.red_flag_category as cat_code "
							+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
							+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.record_status=0) "
							+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";	    			
	    		}
	    		else{
	    			// for removed redflagged    		
	    			reportQuery =" with temp as ( "
							+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by,tbl1.red_flag_category as cat_code "
							+  "  ,(row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
							+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
							+  " select cat_desc, Count(cat_desc) as count from temp  where rn=1 group by cat_desc ";	    				    			
	    		}

				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
	    	}
	    	else {
		    	if(reportDisplayType.equalsIgnoreCase("s")){
		    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
		    			//for live red flagged
			    		reportQuery =" with temp as ( "
								+  " select tbl1.DONOR_NAME,tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, tbl1.COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS,tbl2.red_flag_category as cat_code "
								+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
								+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0') "
								+  " select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc ";	    			
		    		}
		    		else{
		    			// for removed redflagged    		
		    			reportQuery =" with temp as ( "
								+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by,tbl1.red_flag_category as cat_code "
								+  " from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
								+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1) "
								+  " select cat_desc, Count(cat_desc) as count from temp group by cat_desc ";	    				    			
		    		}    		

					ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
					parameters.put("RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);	
		    	}
		    	else {
		    		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			    		reportQuery =" with temp as ( "
			    				+  " select tbl1.DONOR_NAME, tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, (select ctr_name from tm_country where tm_country.CTR_CODE=tbl1.COUNTRY) as COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS, tbl2.red_flag_category as cat_code "
			    				+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
			    				+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.RECORD_STATUS=0) "
			    				+  " select donor_name, cat_desc, (case when country is null then '   ' else country end) as country,remarks, to_char(status_date,'dd-mm-yyyy') as status_date, action_by from temp where last_update=1 and "+subQueryServices+"  order by cat_desc ";
			    		}
			    		// for removed one for donornamewise 
			    		else {
				    		reportQuery =" with temp as( "
				    				+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
				    				+  "  , ROW_NUMBER() over(PARTITION by tbl1.DONOR_ID order by tbl1.STATUS_DATE desc) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
				    				+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0 ) "
				    				+  " select donor_name, cat_desc,(case when country is null then '   ' else country end) as country, remarks, to_char(STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,action_by from temp where "+subQueryServices+"  and rn=1 order by donor_name ";
			    		}
				ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
				parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
		    	}}
			String tsPath = "/Reports/RedFlaggedAssociationsCategoryDonornameWiseCsv.jrxml";
			String fileName = "RedFlagged-List-Donorname-Wise";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	    		    	
	    }
	    	/*String tsPath = "/Reports/RedFlaggedAssociationsCategoryDonornameWiseCsv.jrxml";
			String fileName = "RedFlagged-List-Donorname-Wise";
			GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
					fileName);// TODO Auto-generated method stub	
*/			
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			activityFormate = parameterMap.get("userActivity").get(0).toString();
			/*fromLastyears=Integer.parseInt(parameterMap.get("report-required-for-year").get(0));
			ratioTamt_avgAmt=Integer.parseInt(parameterMap.get("ratio-tamt-avgamt").get(0));
			selectStateList=parameterMap.get("state-List");  */   
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			selectServiceList=parameterMap.get("service-type");
			reportDisplayType=parameterMap.get("reportDisplyType").get(0);
			reportStatusDisplayType = parameterMap.get("reportStatusDisplyType").get(0);
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		    loginUserName=parameterMap.get("loginUserName").get(0);
		}
		
			myLoginofficecode=parameterMap.get("myLoginOfficCode").toString();
/*			cureentBlockYear=getCurrentBlockYear();
			blkyrList=getblockyearList(); */
		}


	//start of Association name wise red flagged category list 
	private List<RedFlaggedCategoryReport> getAssonamewiseRedFlaggedCategoryAssociationListStatistics() throws Exception{
		
		Map  parameters = new HashMap();
		List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);	
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			// for Live Cases, here  l means small L
			String countQuery = " with temp as ( "
					+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,tbl1.STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
					+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0 ) "
					+  " select count(*) from (select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc) ";
			
			String query =" with temp as ( "
					+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,tbl1.STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
					+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0' and tbl1.record_status=0 ), "
					+  " temp2 as (select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc), "
					+  " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
					
				}			
		}
		else{
			//removed redflag carses
			String countQuery = " with temp as ( "
					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+  " ,(row_number() over (partition by tbl1.ASSO_ID order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1  and  tbl1.status=0) "
					+  " select count(*) from (select cat_desc, count(cat_desc) as count from temp where rn=1 group by cat_desc) ";
			String query =" with temp as ( "
					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+  " ,(row_number() over (partition by tbl1.ASSO_ID order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0), "
					+  " temp2 as (select cat_desc, count(cat_desc) as count from temp where rn=1 group by cat_desc), "
					+  " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
					
				}						
		}
							return redFlaggedCategoryList;
	}
	
	private List<RedFlaggedCategoryReport> getAssonamewiseRedFlaggedCategoryAssociationListDetailed() throws Exception{
		Map  parameters = new HashMap();
		List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
		String redFlagTypeList = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		if(redFlagTypeList.equals("ALL")){
		   parameters.put("redFlagTypeList",redFlagTypeList);
		}
		else{
		   	RedFlagAssociationsDao rfdao = new RedFlagAssociationsDao(connection);
			//ServicesDao sdao=new ServicesDao(connection);
		    parameters.put("redFlagTypeList",rfdao.getRedFlagtypeList(redFlagTypeList).toString());
	    }
	    String  subQueryServices="1=1";
	    if(!redFlagTypeList.trim().equals("ALL"))
	    	subQueryServices=" cat_code in ("+redFlagTypeList+")";
	    if(redFlagTypeList.trim().equals("NOCATEGORY"))
	    	subQueryServices=" cat_code is null";	    	    
	    
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			// for Live Cases, here  l means small L
			String countQuery = " with temp as ( "
					+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,tbl1.STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
					+  "  from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0'  and tbl1.RECORD_STATUS=0 ) "
					+ " select count(*) from (select asso_name,cat_desc, state, remarks, status_date,action_by  from temp where last_update=1 and "+subQueryServices+" order by cat_desc) ";

			String query =" with temp as ( "
								+  " select tbl1.ASSO_ID, ROW_NUMBER() over(PARTITION by tbl2.ASSO_ID order by tbl2.STATUS_DATE desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc, tbl1.ASSO_NAME,(select sname from tm_state where tm_state.scode=tbl1.STATE) as STATE, tbl1.RECORD_STATUS,tbl2.REMARKS, to_char(tbl2.STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.red_flag_category as cat_code "
								+  " from T_RED_FLAG_NGO_NAME tbl1, T_RED_FLAG_NGO_STATUS_HISTORY tbl2 "
								+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.status='0'  and tbl1.RECORD_STATUS=0 ), "
								+ " temp2 as (select asso_name,cat_desc, (case when state is null then '  ' else state end) as state, remarks, status_date,action_by  from temp where last_update=1 and "+subQueryServices+" order by cat_desc), "
								+ " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
							//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
							while(rs.next()) {
								
									RedFlaggedCategoryReport temp = new RedFlaggedCategoryReport();
									temp.setAsso_name(rs.getString(1));
									temp.setCat_desc(rs.getString(2));
									temp.setState(rs.getString(3));
									temp.setRemarks(rs.getString(4));
									temp.setStatus_date(rs.getString(5));
									temp.setAction_by(rs.getString(6));
									redFlaggedCategoryList.add(temp);

							}			
		}
		else{
			// for removed redflag cases
			String countQuery = " with temp as ( "
					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+  " , (row_number() over (partition by tbl2.asso_id order by  tbl1.STATUS_DATE desc)) as rownumber from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and tbl1.status='0') "
					+  " select count(*)  from temp where "+subQueryServices+" and rownumber=1 order by asso_name ";

			String query =" with temp as ( "
					+  " select tbl2.ASSO_NAME as asso_name,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc,(select sname from tm_state where tm_state.scode=tbl2.STATE) as STATE, tbl1.REMARKS,tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+  "  , (row_number() over (partition by tbl2.asso_id order by  tbl1.STATUS_DATE desc)) as rownumber from T_RED_FLAG_NGO_STATUS_HISTORY tbl1, T_RED_FLAG_NGO_NAME tbl2 "
					+  " where tbl1.ASSO_ID=tbl2.ASSO_ID and tbl2.RECORD_STATUS=1 and tbl1.status='0'), "
					+  " temp2 as (select asso_name, cat_desc, (case when state is null then '  ' else state end) as state,remarks, to_char(status_date,'dd-mm-yyyy') as status_date, action_by  from temp where "+subQueryServices+"  and rownumber=1 order by asso_name), "
					+  " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
							//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
							while(rs.next()) {
								
									RedFlaggedCategoryReport temp = new RedFlaggedCategoryReport();
									temp.setAsso_name(rs.getString(1));
									temp.setCat_desc(rs.getString(2));
									temp.setState(rs.getString(3));
									temp.setRemarks(rs.getString(4));
									temp.setStatus_date(rs.getString(5));
									temp.setAction_by(rs.getString(6));
									redFlaggedCategoryList.add(temp);

							}			
			
		}
			return redFlaggedCategoryList;		
	}
	// Association name wise red flagged category list   ---------------End
	
	// start of red flagged donor wise ---
	private List<RedFlaggedCategoryReport> getDonornamewiseRedFlaggedCategoryAssociationListStatistics() throws Exception{
		Map  parameters = new HashMap();
		List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    String  subQueryServices="1=1";
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			//Live redflagged records
			String countQuery = " with temp as ( "
					+  " select tbl1.DONOR_NAME, tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, (select ctr_name from tm_country where tm_country.CTR_CODE=tbl1.COUNTRY) as COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS,tbl2.red_flag_category as cat_code "
					+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.record_status=0) "
					+  " select count(*) from (select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc) ";

			String query =" with temp as ( "
					+  " select tbl1.DONOR_NAME,tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, tbl1.COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS,tbl2.red_flag_category as cat_code "
					+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.record_status=0), "
					+  " temp2 as (select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc), "
					+  " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
				}			
		}
		else{
			// removed redflagged records
			String countQuery = " with temp as ( "
					+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by,tbl1.red_flag_category as cat_code "
					+  "  ,(row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
					+  " select count(*) from (select cat_desc, count(cat_desc) as count  from temp where rn=1  group by cat_desc) ";

			String query =" with temp as ( "
					+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by,tbl1.red_flag_category as cat_code "
					+  " ,(row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rn  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0), "
					+  " temp2 as (select cat_desc, count(cat_desc) as  count   from temp where rn=1  group by cat_desc), "
					+  " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
				}			
		}
 	return redFlaggedCategoryList;
 }
	
	private List<RedFlaggedCategoryReport> getDonornamewiseRedFlaggedCategoryAssociationListDetailed() throws Exception{
		
		Map  parameters = new HashMap();
		List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
		String redFlagTypeList = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		if(redFlagTypeList.equals("ALL")){
		   parameters.put("redFlagTypeList",redFlagTypeList);
		}
		else{
		   	RedFlagAssociationsDao rfdao = new RedFlagAssociationsDao(connection);
			//ServicesDao sdao=new ServicesDao(connection);
		    parameters.put("redFlagTypeList",rfdao.getRedFlagtypeList(redFlagTypeList).toString());
	    }
	    String  subQueryServices="1=1";
	    if(!redFlagTypeList.trim().equals("ALL"))
	    	subQueryServices=" cat_code in ("+redFlagTypeList+")";
	    if(redFlagTypeList.trim().equals("NOCATEGORY"))
	    	subQueryServices=" cat_code is null";	    	    
		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			// for Live Cases, here  l means small L	
			String countQuery = " with temp as ( "
					+  " select tbl1.DONOR_NAME, tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, (select ctr_name from tm_country where tm_country.CTR_CODE=tbl1.COUNTRY) as COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS, tbl2.red_flag_category as cat_code "
					+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and  tbl1.RECORD_STATUS=0) "
					+ " select count(*) from (select donor_name, cat_desc, country,remarks, to_char(status_date,'dd-mm-yyyy') as status_date, action_by  from temp where last_update=1 and "+subQueryServices+" order by cat_desc) ";
			
			String query =" with temp as ( "
					+  " select tbl1.DONOR_NAME, tbl1.donor_id,ROW_NUMBER() OVER(PARTITION by tbl2.donor_id order by tbl2.status_date desc) as last_update, (case when tbl2.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end) as cat_desc, (select ctr_name from tm_country where tm_country.CTR_CODE=tbl1.COUNTRY) as COUNTRY, (select USER_NAME from TM_USER Where USER_ID=tbl2.action_by)||'['||tbl2.action_by||']' as action_by, tbl2.REMARKS,tbl2.STATUS_DATE, tbl2.STATUS, tbl2.red_flag_category as cat_code "
					+  " from T_RED_FLAG_DONOR_NAME tbl1, T_RED_FLAG_DONR_STATUS_HISTORY tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.status='0' and tbl1.RECORD_STATUS=0 ), "
					+ " temp2 as (select donor_name, cat_desc, (case when country is null then '   ' else country end) as country,remarks, to_char(status_date,'dd-mm-yyyy') as status_date, action_by from temp where last_update=1 and "+subQueryServices+" order by cat_desc), "
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
									//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
									while(rs.next()) {
										
										RedFlaggedCategoryReport temp = new RedFlaggedCategoryReport();
										temp.setDonor_name(rs.getString(1));
										temp.setCat_desc(rs.getString(2));
										temp.setCountry(rs.getString(3));
										temp.setRemarks(rs.getString(4));
										temp.setStatus_date(rs.getString(5));
										temp.setAction_by(rs.getString(6));
										redFlaggedCategoryList.add(temp);
									}			
		}
		else {
			//for redflag removed cases
			String countQuery = " with temp as ( "
					+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.red_flag_category as cat_code "
					+  " , (row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rownumber  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0) "
					+ " select Count(*) from temp where "+subQueryServices+" and rownumber=1  order by donor_name ";

			String query =" with temp as ( "
					+  " select tbl2.DONOR_NAME as donor_name,(select ctr_name from tm_country where tm_country.CTR_CODE=tbl2.COUNTRY) as COUNTRY,(case when tbl1.red_flag_category is null then 'No Category' else (select category_desc from tm_red_flag_category where category_code=red_flag_category) end ) as cat_desc ,tbl1.REMARKS, tbl1.STATUS_DATE as STATUS_DATE,(select USER_NAME from TM_USER Where USER_ID=tbl1.action_by)||'['||tbl1.action_by||']' as action_by, tbl1.red_flag_category as cat_code "
					+  " , (row_number() over (partition by tbl1.DONOR_ID order by  tbl1.STATUS_DATE desc)) as rownumber  from T_RED_FLAG_DONR_STATUS_HISTORY tbl1, T_RED_FLAG_DONOR_NAME tbl2 "
					+  " where tbl1.DONOR_ID=tbl2.DONOR_ID and tbl2.RECORD_STATUS=1 and  tbl1.status=0), "
					+ " temp2 as (select donor_name, cat_desc,(case when country is null then '   ' else country end) as country, remarks, to_char(STATUS_DATE,'dd-mm-yyyy') as STATUS_DATE,action_by from temp where "+subQueryServices+" and rownumber=1  order by donor_name), "
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
									//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
									while(rs.next()) {
										
										RedFlaggedCategoryReport temp = new RedFlaggedCategoryReport();
										temp.setDonor_name(rs.getString(1));
										temp.setCat_desc(rs.getString(2));
										temp.setCountry(rs.getString(3));
										temp.setRemarks(rs.getString(4));
										temp.setStatus_date(rs.getString(5));
										temp.setAction_by(rs.getString(6));
										redFlaggedCategoryList.add(temp);
									}			
		}

		return redFlaggedCategoryList;		
 		
	}
	// Ends here 

	private List<RedFlaggedCategoryReport> getRedFlaggedCategoryAssociationListDetailed() throws Exception{
 		
		Map  parameters = new HashMap();
		List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    String redFlagStatusSubQuery="";
		String redFlagTypeList = selectServiceList.toString().replace("[", "").replace("]", "").replace(", ", ",").trim();
		if(redFlagTypeList.equals("ALL")){
		   parameters.put("redFlagTypeList",redFlagTypeList);
		}
		else{
		   	RedFlagAssociationsDao rfdao = new RedFlagAssociationsDao(connection);
			//ServicesDao sdao=new ServicesDao(connection);
		    parameters.put("redFlagTypeList",rfdao.getRedFlagtypeList(redFlagTypeList).toString());
	    }
	    String  subQueryServices="1=1";
	    if(!redFlagTypeList.trim().equals("ALL"))
	    	subQueryServices=" cat_code in ("+redFlagTypeList+")";
	    if(redFlagTypeList.trim().equals("NOCATEGORY"))
	    	subQueryServices=" cat_code is null";	    	    
		
		if(connection == null) {
			throw new Exception("Invalid connection");
		}

		if(reportStatusDisplayType.equalsIgnoreCase("l")){
			// for Live Cases, here  l means small L
			String countQuery = " with temp as ( "
					+  " select tbl1.rcn as rcn , tbl4.asso_name as asso_name, tbl1.remarks, tbl1.action_by, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc , ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
					+  " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
					+  " where tbl1.status='0' and tbl1.rcn=tbl3.rcn and tbl4.rcn=tbl3.rcn) "
					+  " select count(*) from temp where last_update=1 and "+subQueryServices+" order by cat_desc ";

				
			String query =" with temp as ( "
					+ " select tbl1.rcn as rcn, (select asso_name from fc_india where rcn=tbl3.rcn) as asso_name, tbl1.remarks, (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc ,ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
					+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 , fc_india tbl4 "
					+ " where tbl1.status='0' and tbl4.rcn=tbl3.rcn and tbl1.rcn=tbl3.rcn ), "
					+ " temp2 as (select rcn, asso_name, cat_desc, to_char(status_date,'dd-mm-yyyy'), remarks, action_by from temp where last_update=1 and "+subQueryServices+" order by cat_desc), "
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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1),rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
					
				}
			//return redFlaggedCategoryList;			
		}
		else{
			// for removed cases
			String countQuery = " with temp as ( "
					+  " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+ ",(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rownumber "
					+  " from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
					+  " select count(*)  from temp where "+subQueryServices+" and rownumber=1 order by rcn ";
					
  			String query =" with temp as ( "
					+ " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+ " ,(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rownumber "
					+ " from T_RED_FLAG_STATUS_HISTORY tbl1 "
					+ " where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0), "
					+ " temp2 as (select rcn, asso_name, cat_desc, status_date, remarks, action_by from temp where "+subQueryServices+" and rownumber=1 order by rcn), "
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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1),rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
					
				}			
		} 
		return redFlaggedCategoryList;
	}
	
	private List<RedFlaggedCategoryReport> getRedFlaggedCategoryAssociationListStatistics() throws Exception{
 	
		Map  parameters = new HashMap();
		List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
		parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    String redFlagStatusSubQuery="";
	    String  subQueryServices="1=1";
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
	    if(reportStatusDisplayType.equalsIgnoreCase("l")){
	    	// for Live red flag cases
				String countQuery = " with temp as ( "
						+  " select tbl1.rcn as rcn,(case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc , ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
						+  " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3 "
						+  " where tbl1.status= '0' and tbl1.rcn=tbl3.rcn ) "
						+  " select count(*) from (select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc) ";
				 
				
				 String query =" with temp as ( "
						+ " select tbl1.rcn as rcn,(case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, ROW_NUMBER() OVER(partition BY tbl1.rcn order by tbl1.status_date desc) as last_update, tbl1.status_date, tbl1.red_flag_category as cat_code "
						+ " from  T_RED_FLAG_STATUS_HISTORY tbl1, T_RED_FLAGGED_ASSOCIATIONS tbl3, fc_india tbl4 "
						+ " where tbl1.status= '0' and tbl1.rcn=tbl3.rcn and tbl3.rcn=tbl4.rcn), "
						+ " temp2 as (select cat_desc, count(cat_desc) as count from temp where last_update=1  group by cat_desc order by cat_desc), "
						+ " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
					//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
					while(rs.next()) {
						redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
						
					}	    	
	    }
	    else {
	    	// for removed red flagged cases
			String countQuery = " with temp as ( "
					+  " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+  " ,(row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0) "
					+  " select count(*) from (select cat_desc, count (cat_desc) as count from temp  where rn=1 group by cat_desc) ";

			String query =" with temp as ( "
					+  " select tbl1.rcn as rcn,(select asso_name from fc_india where rcn=tbl1.rcn) as asso_name, (case WHEN tbl1.red_flag_category is null then 'No Category' else (select category_desc from TM_RED_FLAG_CATEGORY where CATEGORY_CODE=red_flag_category) end) as cat_desc, to_char (tbl1.STATUS_DATE, 'dd-mm-yyyy') as status_date,tbl1.REMARKS,  (select USER_NAME from TM_USER Where USER_ID=tbl1.action_by) ||'['||tbl1.action_by||']' as action_by, tbl1.RED_FLAG_CATEGORY as cat_code "
					+  "  , (row_number() over (partition by tbl1.rcn order by  tbl1.STATUS_DATE desc)) as rn from T_RED_FLAG_STATUS_HISTORY tbl1 where RCN NOT IN (select rcn from T_RED_FLAGGED_ASSOCIATIONS) and  tbl1.status=0), "
					+  " temp2 as (select cat_desc, count(cat_desc) as count from temp  where rn=1 group by cat_desc), "
					+  " P2 as (select temp2.*, ROWNUM RN from temp2) select * from P2 where RN BETWEEN ? and ? ";

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
				//List<RedFlaggedCategoryReport> redFlaggedCategoryList = new ArrayList<RedFlaggedCategoryReport>();
				while(rs.next()) {
					redFlaggedCategoryList.add(new RedFlaggedCategoryReport(rs.getString(1), rs.getString(2)));
					
				}	    		    	
	    }
				
 		return redFlaggedCategoryList;
								
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

	public List<RedFlaggedCategoryReport> getRedFlaggedCategoryList() {
		return redFlaggedCategoryList;
	}

	public void setRedFlaggedCategoryList(
			List<RedFlaggedCategoryReport> redFlaggedCategoryList) {
		this.redFlaggedCategoryList = redFlaggedCategoryList;
	}



}
