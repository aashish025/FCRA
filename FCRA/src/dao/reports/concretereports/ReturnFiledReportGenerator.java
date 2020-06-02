package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.ReturnFiledReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.ServicesDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class ReturnFiledReportGenerator extends MISReportGenerator  {
	private List<ReturnFiledReport> returnFiledReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private String blockYearList;
	private List<String> stateList;
	private String reportDisplyType;
	private String loginUserName;
	private String loginOfficeName;
	private String requireRow;
	private String fromAmt;
	private String toAmt;
	private int virtualizationMaxSize = 200;
	public ReturnFiledReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		     String selectedBlockYearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
			 String selectedStateList=stateList.toString().replace("[", "").replace("]", "").replace(", ","','");
		     String lastblockYearList=blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length());  
			String subQueryBlockYearList="1=1";	
			   if(!selectedBlockYearList.trim().equals("ALL")){
			    	 subQueryBlockYearList="P1.blkyear in ('"+selectedBlockYearList+ "') "; 
			     }
		     String subQuerystateList="1=1";
		     if(!selectedStateList.trim().equals("ALL")){
		    	 if(reportDisplyType.equalsIgnoreCase("s"))
		    	 subQuerystateList=" S.scode in('"+selectedStateList+"')"; 
		        else  
		        	subQuerystateList=" STATE in('"+selectedStateList+"')"; 
		     }
		     String amtCondnFrom="1=1";
		     String amtCondnTo="1=1";
		 	 if(!fromAmt.equals("")){
		 		amtCondnFrom= "FORAMT > "+fromAmt+" ";
		 	 }
		 	 if(!toAmt.equals("")){
		 		amtCondnTo = "FORAMT < "+toAmt+" ";
		 	 }
				parameters.put("selectedBlockYearList",selectedBlockYearList.replaceAll("'", ""));
				parameters.put("lastblockYearList",lastblockYearList);
				//parameters.put("selectedStateList",selectedStateList);
				StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState("'"+selectedStateList+"'"));
			     String recordReqQuery1="1=1";
			     if(!(requireRow==null || requireRow.equals(""))){
			    	 recordReqQuery1="rownum<='"+requireRow+"'";
			    	 parameters.put("recordSelected", requireRow);
			     }
			     else
			    	 parameters.put("recordSelected", "ALL");
			     
	     if(reportDisplyType.equalsIgnoreCase("s")){
	    	 reportQuery ="select blkyear, S.sname,sum(t1.total) as total_received,sum(t1.nilreport) as total_nil_received, sum(t1.foramt) as t1_foramt, "
							+ " sum(t1.totalamount) as t1_totalamount from ("
							+ " SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ " nvl(for_amt + bk_int + oth_int,0) as totalamount, substr(P1.stdist,1,2) state  "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi   "
							+ " where "+subQueryBlockYearList+" AND  "
							+ "  P1.blkyear = TL.blkyear and "
							+ "  substr(TL.rcn,-1,1)='R' and  "
							+ "  TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "
							+ " union all  "

							+ "   SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ "   P1.blkyear = TL.blkyear AND "
							+ "   substr(TL.rcn,-1,1)='P' and  "
							+ "  TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  union all "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1_new  P1,fc_fc3_tally TL, fc_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ " P1.blkyear = TL.blkyear and "
							+ " substr(TL.rcn,-1,1)='R' and  "
							+ " TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ " TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ " UNION ALL "
							+ " SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt,   "
							+ " nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state   "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi   "
							+ "  where "+subQueryBlockYearList+" AND   "
							+ "  P1.blkyear = TL.blkyear AND "
							+ "  substr(TL.rcn,-1,1)='P' and "
							+ " TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn "
							+ "  ) t1, TM_STATE S "
							+ "  where t1.state = S.scode and "+subQuerystateList+""
							+ "  GROUP BY blkyear, S.sname "
							+ "  ORDER BY blkyear, t1_foramt DESC ";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE", ds);	    	 
	     }
	     else{
	    
	    	 reportQuery = "select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

					+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt, "
					+ "    nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
					+ "     FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
					+ "     where "+subQueryBlockYearList+" AND  "
					+ "     substr(TL.rcn,-1,1)='R' "
					+ "     AND fi.rcn=TL.rcn "
					+ "     and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
					+ "     and P1.blkyear = TL.blkyear and  "
					+ "     TL.final_submit = 'Y' AND TL.rcn = P1.rcn "

					+ "     UNION ALL  "
					+ "     SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
					+ "     nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
					+ "      FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
					+ "    where "+subQueryBlockYearList+" AND  "
					+ "    substr(TL.rcn,-1,1)='P' "
					+ "    and fi.rcn=TL.rcn "
					+ "    and P1.blkyear = TL.blkyear   "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

					+ "    union all "

					+ "    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
					+ "   nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
					+ "    FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi  "
					+ "   WHERE "+subQueryBlockYearList+" AND  "
					+ "   substr(TL.rcn,-1,1)='R' "
					+ "   AND fi.rcn=TL.rcn "
					+ "    and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
					+ "    and P1.blkyear = TL.blkyear   "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "
					+ "    UNION ALL   "
					+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
					+ "    nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
					+ "   FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
					+ "   WHERE "+subQueryBlockYearList+" AND  "
					+ "   substr(TL.rcn,-1,1)='P' "
					+ "   AND fi.rcn=TL.rcn "
					+ "   and P1.blkyear = TL.blkyear  "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn "
					+ "   ORDER BY blkyear, foramt DESC   ) t1 "
							+ "  where "+subQuerystateList+"  and  "+recordReqQuery1+"  and "+amtCondnFrom+"  and "+amtCondnTo+"";  
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("REPORT_DATA_SOURCE_DETAILED", ds);	     
	     }
		String tsPath = "/Reports/returnFiledReport.jrxml";
		String fileName = "ReturnFiledReport";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);	
	}
	@Override
	protected void generateExcel() throws Exception {


		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		     String selectedBlockYearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
			 String selectedStateList=stateList.toString().replace("[", "").replace("]", "").replace(", ","','");
		     String lastblockYearList=blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length());  
			
		 	String subQueryBlockYearList="1=1";	
			   if(!selectedBlockYearList.trim().equals("ALL")){
			    	 subQueryBlockYearList="P1.blkyear in ('"+selectedBlockYearList+ "') "; 
			     }
		     
		     String subQuerystateList="1=1";
		     if(!selectedStateList.trim().equals("ALL")){
		    	 if(reportDisplyType.equalsIgnoreCase("s"))
		    	 subQuerystateList=" S.scode in('"+selectedStateList+"')"; 
		        else  
		        	subQuerystateList=" STATE in('"+selectedStateList+"')"; 
		     }
		     String amtCondnFrom="1=1";
		     String amtCondnTo="1=1";
		 	 if(!fromAmt.equals("")){
		 		amtCondnFrom= "FORAMT > "+fromAmt+" ";
		 	 }
		 	 if(!toAmt.equals("")){
		 		amtCondnTo = "FORAMT < "+toAmt+" ";
		 	 }
				parameters.put("selectedBlockYearList",selectedBlockYearList.replaceAll("'", ""));
				parameters.put("lastblockYearList",lastblockYearList);
				//parameters.put("selectedStateList",selectedStateList);
				StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState("'"+selectedStateList+"'"));
			     String recordReqQuery1="1=1";
			     if(!(requireRow==null || requireRow.equals(""))){
			    	 recordReqQuery1="rownum<='"+requireRow+"'";
			    	 parameters.put("recordSelected", requireRow);
			     }
			     else
			    	 parameters.put("recordSelected", "ALL");
			     
	     if(reportDisplyType.equalsIgnoreCase("s")){
	    	 reportQuery ="select blkyear, S.sname,sum(t1.total) as total_received,sum(t1.nilreport) as total_nil_received, sum(t1.foramt) as t1_foramt, "
							+ " sum(t1.totalamount) as t1_totalamount from ("
							+ " SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ " nvl(for_amt + bk_int + oth_int,0) as totalamount, substr(P1.stdist,1,2) state  "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi   "
							+ " where "+subQueryBlockYearList+" AND  "
							+ "  P1.blkyear = TL.blkyear and "
							+ "  substr(TL.rcn,-1,1)='R' and  "
							+ "  TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "
							+ " union all  "

							+ "   SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ "   P1.blkyear = TL.blkyear AND "
							+ "   substr(TL.rcn,-1,1)='P' and  "
							+ "  TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  union all "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1_new  P1,fc_fc3_tally TL, fc_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ " P1.blkyear = TL.blkyear and "
							+ " substr(TL.rcn,-1,1)='R' and  "
							+ " TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ " TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ " UNION ALL       "

							+ " SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt,   "
							+ " nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state   "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi   "
							+ "  where "+subQueryBlockYearList+" AND   "
							+ "  P1.blkyear = TL.blkyear AND "
							+ "  substr(TL.rcn,-1,1)='P' and "
							+ " TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn "
							+ "  ) t1, TM_STATE S "
							+ "  where t1.state = S.scode and "+subQuerystateList+"  "

							+ "  GROUP BY blkyear, S.sname "
							+ "  ORDER BY blkyear, t1_foramt DESC ";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE", ds);
	    	 
	     }
 else {

			reportQuery = "select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

					+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt, "
					+ "    nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "     (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
					+ "     FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
					+ "     where "+subQueryBlockYearList+" AND  "
					+ "     substr(TL.rcn,-1,1)='R' "
					+ "     AND fi.rcn=TL.rcn "
					+ "     and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
					+ "     and P1.blkyear = TL.blkyear and  "
					+ "     TL.final_submit = 'Y' AND TL.rcn = P1.rcn "

					+ "     UNION ALL  "
					+ "     SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
					+ "     nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "      (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website  "
					+ "      FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
					+ "    where "+subQueryBlockYearList+" AND  "
					+ "    substr(TL.rcn,-1,1)='P' "
					+ "    and fi.rcn=TL.rcn "
					+ "    and P1.blkyear = TL.blkyear   "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

					+ "    union all "

					+ "    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
					+ "   nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "    (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
					+ "    FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi  "
					+ "   WHERE "+subQueryBlockYearList+" AND  "
					+ "   substr(TL.rcn,-1,1)='R' "
					+ "   AND fi.rcn=TL.rcn "
					+ "    and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
					+ "    and P1.blkyear = TL.blkyear   "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

					+ "    UNION ALL   "

					+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
					+ "    nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "    (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website  "
					+ "   FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
					+ "   WHERE "+subQueryBlockYearList+" AND  "
					+ "   substr(TL.rcn,-1,1)='P' "
					+ "   AND fi.rcn=TL.rcn "
					+ "   and P1.blkyear = TL.blkyear  "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

					+ "  ORDER BY blkyear, foramt DESC   ) t1 "
							+ "  where "+subQuerystateList+"  and  "+recordReqQuery1+"  and "+amtCondnFrom+"  and "+amtCondnTo+"  "; 
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
			parameters.put("REPORT_DATA_SOURCE_DETAILED", ds);

		}
	 	String tsPath = "/Reports/returnFiledReport.jrxml";
		//String tsPath = "/Reports/returnFiledReport_Excel.jrxml";
		String fileName = "ReturnFiledReport";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	    
	
		
	}
	@Override
	protected void generateHTML() throws Exception {
		// TODO Auto-generated method stub
		returnFiledReport=getReturnFiledReportRecord();	
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
					blockYearList=parameterMap.get("blockYear-List").toString();
					stateList=parameterMap.get("state-List");
					reportDisplyType=parameterMap.get("reportDisplyType").get(0);
					loginOfficeName=parameterMap.get("loginOfficeName").get(0);
					loginUserName=parameterMap.get("loginUserName").get(0);
					requireRow=parameterMap.get("requireRowCount").get(0);
					fromAmt=parameterMap.get("from-amount").get(0);
					toAmt=parameterMap.get("to-amount").get(0);
				}
		
	}
	public List<ReturnFiledReport> getReturnFiledReportRecord() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
         
         String selectedBlockYearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
		 String selectedStateList=stateList.toString().replace("[", "").replace("]", "").replace(", ","','");
	     String lastblockYearList=blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length());  
			
	     String subQueryBlockYearList="1=1";	
		   if(!selectedBlockYearList.trim().equals("ALL")){
		    	 subQueryBlockYearList="P1.blkyear in ('"+selectedBlockYearList+ "') "; 
		     }
	     
	     
	     String subQuerystateList="1=1";
	     if(!selectedStateList.trim().equals("ALL")){
	    	 if(reportDisplyType.equalsIgnoreCase("s"))
	    	 subQuerystateList=" S.scode in('"+selectedStateList+"')"; 
	        else  
	        	subQuerystateList=" STATE in('"+selectedStateList+"')"; 
	     }
	     String amtCondnFrom="1=1";
	     String amtCondnTo="1=1";
	 	 if(!fromAmt.equals("")){
	 		amtCondnFrom= "FORAMT > "+fromAmt+" ";
	 	 }
	 	 if(!toAmt.equals("")){
	 		amtCondnTo = "FORAMT < "+toAmt+" ";
	 	 }
	    ///
	     String recordReqQuery="";
	     String recordReqQuery1="";
	     if(!(requireRow==null || requireRow.equals(""))){
	    	 recordReqQuery="T4 as (select * from TAB1 where rownum<='"+requireRow+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
	    	 recordReqQuery1="where rownum<='"+requireRow+"'";
	     }
	     else{
	    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM<=?";
	     }
	 
	     
	     ////
	     
	     
	     StringBuffer countQuery=null;
	     StringBuffer query=null;
	    if(reportDisplyType.equalsIgnoreCase("s")){
			countQuery = new StringBuffer(
					"select count(*) from (select blkyear, S.sname,sum(t1.total) as total_received,sum(t1.nilreport) as total_nil_received, sum(t1.foramt) as t1_foramt, "
							+ " sum(t1.totalamount) as t1_totalamount from ("
							+ " SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ " nvl(for_amt + bk_int + oth_int,0) as totalamount, substr(P1.stdist,1,2) state  "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi   "
							+ " where "+subQueryBlockYearList+" AND  "
							+ "  P1.blkyear = TL.blkyear and "
							+ "  substr(TL.rcn,-1,1)='R' and  "
							+ "  TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "
							+ " union all  "

							+ "   SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ "   P1.blkyear = TL.blkyear AND "
							+ "   substr(TL.rcn,-1,1)='P' and  "
							+ "  TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  union all "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1_new  P1,fc_fc3_tally TL, fc_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ " P1.blkyear = TL.blkyear and "
							+ " substr(TL.rcn,-1,1)='R' and  "
							+ " TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ " TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ " UNION ALL       "

							+ " SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt,   "
							+ " nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state   "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi   "
							+ "  where "+subQueryBlockYearList+" AND   "
							+ "  P1.blkyear = TL.blkyear AND "
							+ "  substr(TL.rcn,-1,1)='P' and "
							+ " TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn "
							+ "  ) t1, TM_STATE S "
							+ "  where t1.state = S.scode and "+subQuerystateList+"  "

							+ "  GROUP BY blkyear, S.sname) "
							+ "  ORDER BY blkyear, t1_foramt DESC");
			query = new StringBuffer(
					"WITH TAB1 AS (select blkyear, S.sname,sum(t1.total) as total_received,sum(t1.nilreport) as total_nil_received, sum(t1.foramt) as t1_foramt, "
							+ "sum(t1.totalamount) as t1_totalamount from ( "

							+ "SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ "  nvl(for_amt + bk_int + oth_int,0) as totalamount, substr(P1.stdist,1,2) state  "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ "  P1.blkyear = TL.blkyear and "
							+ "  substr(TL.rcn,-1,1)='R' and "
							+ "  TL.rcn = fi.rcn and "
							+ "  (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and  "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  union all  "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state "
							+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi "
							+ "  where "+subQueryBlockYearList+"  AND   "
							+ "  P1.blkyear = TL.blkyear AND "
							+ "  substr(TL.rcn,-1,1)='P' and  "
							+ "   TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "   union all "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "   nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1_new  P1,fc_fc3_tally TL, fc_india fi  "
							+ "   where "+subQueryBlockYearList+" AND  "
							+ "  P1.blkyear = TL.blkyear and "
							+ "  substr(TL.rcn,-1,1)='R' and  "
							+ "   TL.rcn = fi.rcn and "
							+ "   (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and  "
							+ "   TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "   UNION ALL  "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi "
							+ "  where "+subQueryBlockYearList+" AND     "
							+ "  P1.blkyear = TL.blkyear AND "
							+ "  substr(TL.rcn,-1,1)='P' and  "
							+ "  TL.rcn = fi.rcn and "
							+ " TL.final_submit = 'Y' AND TL.rcn = P1.rcn "
							+ "  ) t1, TM_STATE S "
							+ "  where t1.state = S.scode and "+subQuerystateList+"   "

							+ "  GROUP BY blkyear, S.sname    "
							+ "  ORDER BY blkyear, t1_foramt DESC), T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM<=?) SELECT * FROM T3 WHERE RN>=? ") ;
		
	    }
	    else{

			countQuery = new StringBuffer(
					" select count(*) from (select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

							+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
							+ " (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website"
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
							+ " where "+subQueryBlockYearList+" AND  "
							+ " substr(TL.rcn,-1,1)='R' "
							+ " AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear and  "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

							+ "  UNION ALL  "
							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district , "
							+ "  (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
							+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockYearList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  and fi.rcn=TL.rcn "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  union all "

							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district , "
							+ "  (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
							+ "   WHERE "+subQueryBlockYearList+" AND  "
							+ "   substr(TL.rcn,-1,1)='R' "
							+ "   AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  UNION ALL "

							+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
							+ "   (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website  "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  WHERE "+subQueryBlockYearList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  AND fi.rcn=TL.rcn "
							+ "   and P1.blkyear = TL.blkyear  "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  ) t1 "

							+ "  where " + subQuerystateList+ " and "+amtCondnFrom+"  and "+amtCondnTo+" )  "
							+ recordReqQuery1 + "   "/*--group by S.sname */
							+ "   ORDER BY foramt DESC");
			      query = new StringBuffer( "WITH TAB1 AS ("
			      		    + " select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

							+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
							+ " (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
							+ " where "+subQueryBlockYearList+" AND  "
							+ " substr(TL.rcn,-1,1)='R' "
							+ " AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear and  "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

							+ "  UNION ALL  "
							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district , "
							+ "  (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
							+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockYearList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  and fi.rcn=TL.rcn "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  union all "

							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
							+ "  (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website  "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
							+ "   WHERE "+subQueryBlockYearList+" AND  "
							+ "   substr(TL.rcn,-1,1)='R' "
							+ "   AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  UNION ALL "

							+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
							+ "   (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  WHERE "+subQueryBlockYearList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  AND fi.rcn=TL.rcn "
							+ "   and P1.blkyear = TL.blkyear  "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  ) t1 "
							+ "  where "+subQuerystateList+"  and "+amtCondnFrom+"  and "+amtCondnTo+" ORDER BY blkyear, foramt DESC"
							+ " ),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=? ") ;
			
		    
	    	
	    	
	    	
	    }
	  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
	  ResultSet rs = statement.executeQuery(); if(rs.next())
	 { 
		  totalRecords = rs.getString(1);
	 } 
	 rs.close();
	 statement.close();
		
	     Integer pageRequested = Integer.parseInt(pageNum);
		Integer pageSize = Integer.parseInt(recordsPerPage);
	      statement = connection.prepareStatement(query.toString());
		if (pageNum == null || recordsPerPage == null) {

		} else {

			statement.setInt(1, (pageRequested - 1) * pageSize + pageSize);
			statement.setInt(2, (pageRequested - 1) * pageSize + 1);
		}
		System.out.println("Print Queryyyyy qq+++ "+query.toString());
		 rs = statement.executeQuery();
		List<ReturnFiledReport> reportTypeList = new ArrayList<ReturnFiledReport>();
		while (rs.next()) {
			if(reportDisplyType.equalsIgnoreCase("s"))
			 reportTypeList.add(new ReturnFiledReport(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),rs.getString(6)));
			else
				reportTypeList.add(new ReturnFiledReport(rs.getString(1),rs.getString(10),rs.getString(11),rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(9)));
				
		}
		 rs.close();
		 statement.close();
		return reportTypeList;
	
	
		
	
	}
	public List<ReturnFiledReport> getReturnFiledReport() {
		return returnFiledReport;
	}
	public void setReturnFiledReport(List<ReturnFiledReport> returnFiledReport) {
		this.returnFiledReport = returnFiledReport;
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
	public String getBlockYearList() {
		return blockYearList;
	}
	public void setBlockYearList(String blockYearList) {
		this.blockYearList = blockYearList;
	}

	public List<String> getStateList() {
		return stateList;
	}
	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
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
	public String getRequireRow() {
		return requireRow;
	}
	public void setRequireRow(String requireRow) {
		this.requireRow = requireRow;
	}
	public String getFromAmt() {
		return fromAmt;
	}
	public void setFromAmt(String fromAmt) {
		this.fromAmt = fromAmt;
	}
	public String getToAmt() {
		return toAmt;
	}
	public void setToAmt(String toAmt) {
		this.toAmt = toAmt;
	}
	@Override
	protected void generateCSV() throws Exception {



		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		     String selectedBlockYearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
			 String selectedStateList=stateList.toString().replace("[", "").replace("]", "").replace(", ","','");
		     String lastblockYearList=blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length());  
			
		 	String subQueryBlockYearList="1=1";	
			   if(!selectedBlockYearList.trim().equals("ALL")){
			    	 subQueryBlockYearList="P1.blkyear in ('"+selectedBlockYearList+ "') "; 
			     }
		     
		     String subQuerystateList="1=1";
		     if(!selectedStateList.trim().equals("ALL")){
		    	 if(reportDisplyType.equalsIgnoreCase("s"))
		    	 subQuerystateList=" S.scode in('"+selectedStateList+"')"; 
		        else  
		        	subQuerystateList=" STATE in('"+selectedStateList+"')"; 
		     }
		     String amtCondnFrom="1=1";
		     String amtCondnTo="1=1";
		 	 if(!fromAmt.equals("")){
		 		amtCondnFrom= "FORAMT > "+fromAmt+" ";
		 	 }
		 	 if(!toAmt.equals("")){
		 		amtCondnTo = "FORAMT < "+toAmt+" ";
		 	 }
				parameters.put("selectedBlockYearList",selectedBlockYearList.replaceAll("'", ""));
				parameters.put("lastblockYearList",lastblockYearList);
				//parameters.put("selectedStateList",selectedStateList);
				StateDao sdao= new StateDao(connection);
				parameters.put("selectedStateList",sdao.getState("'"+selectedStateList+"'"));
			     String recordReqQuery1="1=1";
			     if(!(requireRow==null || requireRow.equals(""))){
			    	 recordReqQuery1="rownum<='"+requireRow+"'";
			    	 parameters.put("recordSelected", requireRow);
			     }
			     else
			    	 parameters.put("recordSelected", "ALL");
			     
	     if(reportDisplyType.equalsIgnoreCase("s")){
	    	 reportQuery ="select blkyear, S.sname,sum(t1.total) as total_received,sum(t1.nilreport) as total_nil_received, sum(t1.foramt) as t1_foramt, "
							+ " sum(t1.totalamount) as t1_totalamount from ("
							+ " SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ " nvl(for_amt + bk_int + oth_int,0) as totalamount, substr(P1.stdist,1,2) state  "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi   "
							+ " where "+subQueryBlockYearList+" AND  "
							+ "  P1.blkyear = TL.blkyear and "
							+ "  substr(TL.rcn,-1,1)='R' and  "
							+ "  TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "
							+ " union all  "

							+ "   SELECT 1 total, P1.blkyear, case when nvl(for_amt + bk_int + oth_int,0)=0 then 1 else 0 end nilreport, nvl(for_amt,0) AS foramt, "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ "   P1.blkyear = TL.blkyear AND "
							+ "   substr(TL.rcn,-1,1)='P' and  "
							+ "  TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  union all "

							+ "  SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state  "
							+ "   FROM fc_fc3_part1_new  P1,fc_fc3_tally TL, fc_india fi  "
							+ "  where "+subQueryBlockYearList+" AND  "
							+ " P1.blkyear = TL.blkyear and "
							+ " substr(TL.rcn,-1,1)='R' and  "
							+ " TL.rcn = fi.rcn and "
							+ " (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') and   "
							+ " TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ " UNION ALL       "

							+ " SELECT 1 total, P1.blkyear, case when nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0)=0 then 1 else 0 end nilreport, nvl(SOURCE_FOR_AMT,0) AS foramt,   "
							+ " nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state   "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi   "
							+ "  where "+subQueryBlockYearList+" AND   "
							+ "  P1.blkyear = TL.blkyear AND "
							+ "  substr(TL.rcn,-1,1)='P' and "
							+ " TL.rcn = fi.rcn and "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn "
							+ "  ) t1, TM_STATE S "
							+ "  where t1.state = S.scode and "+subQuerystateList+"  "

							+ "  GROUP BY blkyear, S.sname "
							+ "  ORDER BY blkyear, t1_foramt DESC ";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE", ds);
	    	 
	     }
 else {

			reportQuery = "select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

					+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt, "
					+ "    nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "      (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
					+ "     FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
					+ "     where "+subQueryBlockYearList+" AND  "
					+ "     substr(TL.rcn,-1,1)='R' "
					+ "     AND fi.rcn=TL.rcn "
					+ "     and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
					+ "     and P1.blkyear = TL.blkyear and  "
					+ "     TL.final_submit = 'Y' AND TL.rcn = P1.rcn "

					+ "     UNION ALL  "
					+ "     SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
					+ "     nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "       (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
					+ "      FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
					+ "    where "+subQueryBlockYearList+" AND  "
					+ "    substr(TL.rcn,-1,1)='P' "
					+ "    and fi.rcn=TL.rcn "
					+ "    and P1.blkyear = TL.blkyear   "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

					+ "    union all "

					+ "    SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
					+ "   nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district ,"
					+ "      (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
					+ "    FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi  "
					+ "   WHERE "+subQueryBlockYearList+" AND  "
					+ "   substr(TL.rcn,-1,1)='R' "
					+ "   AND fi.rcn=TL.rcn "
					+ "    and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
					+ "    and P1.blkyear = TL.blkyear   "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

					+ "    UNION ALL   "

					+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
					+ "    nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  ,"
					+ "     (select ASSO_OFFICIAL_WEBSITE from FC_ASSO_DETAILS where rcn = fi.rcn) website "
					+ "   FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
					+ "   WHERE "+subQueryBlockYearList+" AND  "
					+ "   substr(TL.rcn,-1,1)='P' "
					+ "   AND fi.rcn=TL.rcn "
					+ "   and P1.blkyear = TL.blkyear  "
					+ "    and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

					+ "  ORDER BY blkyear, foramt DESC   ) t1 "
							+ "  where "+subQuerystateList+"  and  "+recordReqQuery1+"  and "+amtCondnFrom+"  and "+amtCondnTo+"  "; 
			ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
			parameters.put("REPORT_DATA_SOURCE_DETAILED", ds);

		}
		String tsPath = "/Reports/returnFiledReport_CSV.jrxml";
		String fileName = "ReturnFiledReport";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	    
	
		
	
		
	}
	
}
