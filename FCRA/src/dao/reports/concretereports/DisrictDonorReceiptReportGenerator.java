package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.CountryPurposeDonor;
import models.reports.CountryWiseReceiptReport;
import models.reports.DistrictDonorReceipt;
import models.reports.ReturnFiledReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class DisrictDonorReceiptReportGenerator extends MISReportGenerator{
	private List<DistrictDonorReceipt> districtDonorReceiptReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private List<String> blockYearList;
	private List<String> stateList;
	private List<String> selectCountryList;
	private String recordRequired;	
	private String districtDonorFlag;
	private String loginUserName;
	private String loginOfficeName;
	private String sortColumn;
	private String sortOrder;
	private int virtualizationMaxSize = 200;	

	public DisrictDonorReceiptReportGenerator(Connection connection) {
		super(connection);		
	}	

	@Override
	protected void generateHTML() throws Exception {
		if(districtDonorFlag.equals("0"))			
			districtDonorReceiptReport=getDonorWiseReceiptReport();
		else if(districtDonorFlag.equals("1"))
			districtDonorReceiptReport=getDistrictWiseReceiptReport();
		else if(districtDonorFlag.equals("2"))
			districtDonorReceiptReport=getStateWiseReceiptReport();
		else if(districtDonorFlag.equals("3"))
			districtDonorReceiptReport=getCountryWiseReceiptReport1();
		else if(districtDonorFlag.equals("4"))
			districtDonorReceiptReport=getAssociationWiseReceiptReport();
		totalRecords=getTotalRecords();		
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
			blockYearList=parameterMap.get("blockYear8");
			stateList=parameterMap.get("state8");	
			selectCountryList=parameterMap.get("country8");
			recordRequired=parameterMap.get("record8").toString().replace("[", "").replace("]", "").trim();
			districtDonorFlag=parameterMap.get("districtDonor8").toString().replace("[", "").replace("]", "").trim();
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
		}
		
	}

	public List<DistrictDonorReceipt> getDonorWiseReceiptReport() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}
		 
         String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
         String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
         
         String subQueryBlockList="1=1";
	     if(!selectedblockyearList.trim().equals("ALL"))
	    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
         
         String subQueryStateList1="1=1";
	     if(!selectedstateList.trim().equals("ALL"))
	    	 subQueryStateList1=" substr(fp3.stdist,1,2) in('"+selectedstateList+"')";
	     
	     String subQueryStateList2="1=1";
	     if(!selectedstateList.trim().equals("ALL"))
	    	 subQueryStateList2=" substr(ft.stdist,1,2) in('"+selectedstateList+"')";
	     
	     String recordReqQuery="";
	     String recordReqQuery1="";
	     if(!(recordRequired==null || recordRequired.equals(""))){
	    	 recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
	    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
	     }
	     else{
	    	 recordReqQuery="T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?";
	     }	
		StringBuffer countQuery = new StringBuffer(
				" SELECT COUNT(*) FROM (SELECT * FROM ("+
					" select    ft.blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "+  
                    " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "+  
                    " WHERE fd.dcode = fp3.dcode "+                      
                    " and fc.ctr_code = fp3.ctr_code "+
                    " AND fp3.blkyear =ft.blkyear"+
                    " AND "+subQueryBlockList+" "+  
                    " and "+subQueryStateList1+" "+  
                    " and fp3.dtype ='1' "+                      
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.dname,fc.ctr_name "+                   
                    " UNION ALL "+                    
					" select    ft.blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "+  
                    " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "+  
                    " WHERE fd.donor_code = fp3.donor_code "+                      
                    " and fc.ctr_code = fd.donor_country "+  
                    " AND fp3.blkyear = ft.blkyear  "+  
                    " AND "+subQueryStateList2+" "+  
                    " and fd.donor_type ='01' "+  
                    " and "+subQueryBlockList+" "+  
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.donor_name,fc.ctr_name ) "+recordReqQuery1+" order by amount desc)"); 
		
		     StringBuffer query = new StringBuffer(
				"WITH T2 AS (SELECT * FROM ("+
					" select    ft.blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "+  
                    " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "+  
                    " WHERE fd.dcode = fp3.dcode "+                      
                    " and fc.ctr_code = fp3.ctr_code "+
                    " AND fp3.blkyear =ft.blkyear"+
                    " AND "+subQueryBlockList+" "+  
                    " and "+subQueryStateList1+" "+  
                    " and fp3.dtype ='1' "+                      
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.dname,fc.ctr_name "+                   
                    " UNION ALL "+                    
					" select    ft.blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "+  
                    " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "+  
                    " WHERE fd.donor_code = fp3.donor_code "+                      
                    " and fc.ctr_code = fd.donor_country "+  
                    " AND fp3.blkyear = ft.blkyear  "+  
                    " AND "+subQueryStateList2+" "+  
                    " and fd.donor_type ='01' "+  
                    " and "+subQueryBlockList+" "+  
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.donor_name,fc.ctr_name) order by amount desc"
                    + "), "+recordReqQuery+") SELECT * FROM T3 WHERE RN>=?");		
		
		  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
		  ResultSet rs = statement.executeQuery(); 
		  if(rs.next()) { 
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
		System.out.println("Print Query qq+++ "+query.toString());
		 rs = statement.executeQuery();
		List<DistrictDonorReceipt> reportTypeList = new ArrayList<DistrictDonorReceipt>();
		while (rs.next()) {			
			reportTypeList.add(new DistrictDonorReceipt(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4)));
		}
		return reportTypeList;	
	}
	
	public List<DistrictDonorReceipt> getDistrictWiseReceiptReport() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}		 
		String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        
        String subQueryBlockList="1=1";
	     if(!selectedblockyearList.trim().equals("ALL"))
	    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
        
        String subQueryStateList="1=1";
	     if(!selectedstateList.trim().equals("ALL"))
	    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";
	     
	     String recordReqQuery="";
	     String recordReqQuery1="";
	     if(!(recordRequired==null || recordRequired.equals(""))){
	    	 recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
	    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
	     }
	     else
	    	 recordReqQuery="T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?";
	
		StringBuffer countQuery = new StringBuffer(
				" SELECT COUNT(*) FROM (select distinct blkyear,distname, sum(associations) as sassociations,sname,   sum(amount) AS aa from ( "+
						" select distinct ft.blkyear,fd.distname, count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname "+                          
                        " union all "+                         
                        " select distinct ft.blkyear,fd.distname, "+  
                        " count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname ) "+recordReqQuery1+" GROUP BY blkyear,distname,sname ORDER BY aa DESC)"); 
		
		     StringBuffer query = new StringBuffer(
				"WITH T2 AS (select  distinct blkyear,distname, sum(associations) as sassociations,sname,   sum(amount) AS aa from ( "+
						" select distinct ft.blkyear,fd.distname, count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname "+                          
                        " union all "+                         
                        " select distinct ft.blkyear,fd.distname, "+  
                        " count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname) GROUP BY blkyear,distname,sname ORDER BY aa DESC"
                        + "), "+recordReqQuery+") SELECT * FROM T3 WHERE RN>=?");
		
		
		  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
		  ResultSet rs = statement.executeQuery(); 
		  if(rs.next()) { 
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
		System.out.println("Print Query qq+++ "+query.toString());
		rs = statement.executeQuery();
		List<DistrictDonorReceipt> reportTypeList = new ArrayList<DistrictDonorReceipt>();
		while (rs.next()) {			
			reportTypeList.add(new DistrictDonorReceipt(rs.getString(1), rs.getString(4), rs.getString(2), rs.getString(3), rs.getString(5)));
		}
		return reportTypeList;	
	}
	
	public List<DistrictDonorReceipt> getStateWiseReceiptReport() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}		 
		String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        
        String subQueryBlockList="1=1";
	     if(!selectedblockyearList.trim().equals("ALL"))
	    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
        
        String subQueryStateList="1=1";
	     if(!selectedstateList.trim().equals("ALL"))
	    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";
	     
	     String recordReqQuery="";
	     String recordReqQuery1="";
	     if(!(recordRequired==null || recordRequired.equals(""))){
	    	 recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
	    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
	     }
	     else
	    	 recordReqQuery="T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?";
	
		StringBuffer countQuery = new StringBuffer(
				" SELECT COUNT(*) FROM (select  blkyear,sum(associations) as sassociations,sname,sum(amount) AS samount from ( "+
						" select distinct ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname "+                          
                        " union all "+                         
                        " select distinct "+  
                        " ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname ) "+recordReqQuery1+" group by blkyear,sname ORDER BY samount DESC)"); 
		
		     StringBuffer query = new StringBuffer(
				"WITH T2 AS (select  blkyear,sum(associations) as sassociations,sname,sum(amount) AS samount from ( "+
						" select distinct ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname "+                          
                        " union all "+                         
                        " select distinct "+  
                        " ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname ) group by blkyear,sname ORDER BY samount DESC"
                        + "),"+recordReqQuery+") SELECT * FROM T3 WHERE RN>=?");		
		  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
		  ResultSet rs = statement.executeQuery(); 
		  if(rs.next()) { 
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
		System.out.println("Print Query qq+++ "+query.toString());
		rs = statement.executeQuery();
		List<DistrictDonorReceipt> reportTypeList = new ArrayList<DistrictDonorReceipt>();
		while (rs.next()) {			
			reportTypeList.add(new DistrictDonorReceipt(rs.getString(1), rs.getString(3),"",rs.getString(2), rs.getString(4)));
		}
		return reportTypeList;	
	}
	
	public List<DistrictDonorReceipt> getAssociationWiseReceiptReport() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}		 
		String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();          
        String subQueryBlockList="1=1";
	     if(!selectedblockyearList.trim().equals("ALL"))
	    	 subQueryBlockList=" p1.blkyear in('"+selectedblockyearList+"')";    
	     
	     
	     String recordReqQuery="";
	     String recordReqQuery1="";
	     if(!(recordRequired==null || recordRequired.equals(""))){
	    	 recordReqQuery="T4 as (select * from TAB1 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
	    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
	     }
	     else{
	    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM<=?";
	     }
	
		StringBuffer countQuery = new StringBuffer(
				" select count(*) from (select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

							+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
							+ " where "+subQueryBlockList+" AND  "
							+ " substr(TL.rcn,-1,1)='R' "
							+ " AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear and  "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

							+ "  UNION ALL  "
							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
							+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  and fi.rcn=TL.rcn "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  union all "

							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district   "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
							+ "   WHERE "+subQueryBlockList+" AND  "
							+ "   substr(TL.rcn,-1,1)='R' "
							+ "   AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  UNION ALL "

							+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  WHERE "+subQueryBlockList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  AND fi.rcn=TL.rcn "
							+ "   and P1.blkyear = TL.blkyear  "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "
							+ "  ) t1 )"
							+ 	recordReqQuery1 + "   "/*--group by S.sname */
							+ "   ORDER BY foramt DESC"); 
		
		     StringBuffer query = new StringBuffer(
				"WITH TAB1 AS ("
			      		    + " select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

							+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
							+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
							+ " where "+subQueryBlockList+" AND  "
							+ " substr(TL.rcn,-1,1)='R' "
							+ " AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear and  "
							+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

							+ "  UNION ALL  "
							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
							+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
							+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  where "+subQueryBlockList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  and fi.rcn=TL.rcn "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  union all "

							+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district   "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
							+ "   WHERE "+subQueryBlockList+" AND  "
							+ "   substr(TL.rcn,-1,1)='R' "
							+ "   AND fi.rcn=TL.rcn "
							+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
							+ "  and P1.blkyear = TL.blkyear   "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

							+ "  UNION ALL "

							+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
							+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
							+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
							+ "  WHERE "+subQueryBlockList+" AND   "
							+ "  substr(TL.rcn,-1,1)='P' "
							+ "  AND fi.rcn=TL.rcn "
							+ "   and P1.blkyear = TL.blkyear  "
							+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

							+ "  ) t1 "
							+ "  ORDER BY blkyear, foramt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=? ");		
		  PreparedStatement statement = connection.prepareStatement(countQuery.toString());
		  ResultSet rs = statement.executeQuery(); 
		  if(rs.next()) { 
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
		System.out.println("Print Query qq+++ "+query.toString());
		rs = statement.executeQuery();
		List<DistrictDonorReceipt> reportTypeList = new ArrayList<DistrictDonorReceipt>();
		while (rs.next()) {			
			reportTypeList.add(new DistrictDonorReceipt(rs.getString(1),rs.getString(9),rs.getString(10),rs.getString(2), rs.getString(3),  rs.getString(5)));
		}
		return reportTypeList;	
	}
	
	public List<DistrictDonorReceipt> getCountryWiseReceiptReport1() throws Exception {
		if(connection == null) {
			throw new Exception("Invalid connection");
		}
			
								
							String selectBlockyearList = blockYearList.toString().replace("[", "'").replace("]", "'").replace(", ","','").trim();
							//String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
								String subStringStateList="1=1";
								String CountryList="1=1";
								 String subQueryBlockList="1=1";
							     if(!selectBlockyearList.trim().equals("'ALL'"))
							    	 subQueryBlockList=" A.blkyear in("+selectBlockyearList+")";    
							     
								String stList=stateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
								if(!stList.trim().equals("'ALL'")){
							//	if(!stList.trim().equals("'ALL'")){
									subStringStateList= "substr(d.stdist,1,2) IN ("+stList+")";
								}
								
								String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
								if(!contList.trim().equals("'ALL'")){
									CountryList= "ctr_code in ("+contList+")";
								}
								 String recordReqQuery="";
							     String recordReqQuery1="";
							     if(!(recordRequired==null || recordRequired.equals(""))){
							    	 recordReqQuery="T4 as (select * from TAB1 where rownum<="+recordRequired+"),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
							    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
							     }
							     else{
							    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM<=?";
							     }
								StringBuffer countQuery = new StringBuffer(
										"select count(*) from (select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from   (    SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount "    
												+"FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d   where   "+ subQueryBlockList
												+"    AND b.blkyear =  A.blkyear  "  
												+"AND A.final_submit='Y' AND b.ctr_code = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "   
												+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  "  
												+"AND " +subStringStateList+" "   
												+"GROUP BY A.blkyear, ctr_name,b.ctr_code"

												+" UNION ALL  "   

												+"SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount     FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where "
														+ subQueryBlockList
														+ " and "  
												+"b.blkyear =  a.blkyear and      a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P'    AND " +subStringStateList+" "  
												+"group by a.blkyear, ctr_name, b.ctr_code  "

												+" union all  " 

												+"SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount      FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd "
												+"WHERE "
														+ subQueryBlockList
														+ "     AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn  "  
												+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')   "
												+"AND substr(A.rcn,-1,1)='R'   AND " +subStringStateList+" "
												+"AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code    GROUP BY A.blkyear, ctr_name, donor_country"

												+" UNION ALL   "   
												+"SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount       FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   "
												+"where "
														+ subQueryBlockList
														+ " and      b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   "
												+"A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND " +subStringStateList+" " 
												+"AND fd.donor_type='01'     AND d.rcn=fd.rcn     and b.donor_code=fd.donor_code " 
												+"GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name"
												+")     tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name) "+ 	recordReqQuery1 + " ORDER BY blkyear, amt DESC");
							
		/*StringBuffer countQuery = new StringBuffer(
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
						+ "   ORDER BY blkyear, amt DESC");*/
								StringBuffer query;
							query = new StringBuffer(
									"WITH TAB1 AS ( select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from   (    SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount "    
											+"FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d      WHERE "
											+ subQueryBlockList
													+ "    AND b.blkyear =  A.blkyear  "  
											+"AND A.final_submit='Y' AND b.ctr_code = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "   
											+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  "  
											+"AND " +subStringStateList+" "   
											+"GROUP BY A.blkyear, ctr_name,b.ctr_code"

											+" UNION ALL  "   

											+"SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount     FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where "
											+ subQueryBlockList
													+ " and "  
											+"b.blkyear =  a.blkyear and      a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P'    AND " +subStringStateList+" "  
											+"group by a.blkyear, ctr_name, b.ctr_code  "

											+" union all  " 

											+"SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount      FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd "
											+"WHERE "
											+ subQueryBlockList
													+ "     AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn  "  
											+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')   "
											+"AND substr(A.rcn,-1,1)='R'   AND " +subStringStateList+" "
											+"AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code    GROUP BY A.blkyear, ctr_name, donor_country"

											+" UNION ALL   "   
											+"SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount       FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   "
											+"where "
											+ subQueryBlockList
													+ " and      b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   "
											+"A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND " +subStringStateList+" " 
											+"AND fd.donor_type='01'     AND d.rcn=fd.rcn     and b.donor_code=fd.donor_code " 
											+"GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name"
											+")     tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name  ORDER BY blkyear, amt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=? ");
							/*	 query = new StringBuffer(
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
							*/
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
						     //     String queryForPaging = preparePagingQuery(query);	
								// statement = connection.prepareStatement(queryForPaging);
								 statement = connection.prepareStatement(query.toString());
								 if(pageNum == null || recordsPerPage == null) {
						
								 }
								 else {
								 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
								 statement.setInt(2, (pageRequested-1) * pageSize + 1);
								 }
							
								 rs = statement.executeQuery();
								List<DistrictDonorReceipt> countryWiseReceiptReportList = new ArrayList<DistrictDonorReceipt>();
								while(rs.next()) {
									countryWiseReceiptReportList.add(new DistrictDonorReceipt(rs.getString(1), rs.getString(2),rs.getString(3))); 
									
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
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    parameters.put("districtDonorFlag1", districtDonorFlag);
	    String reportQuery="";
	    // Donor Wise Report 
		if(districtDonorFlag.equals("0")){
			  parameters.put("districtDonorFlag","Doner Wise Receipt Report");
	         String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	         String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	         parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
	         parameters.put("selectedStateList",getState(selectedstateList));
	         String subQueryBlockList="1=1";
		     if(!selectedblockyearList.trim().equals("ALL"))
		    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
	         
	         String subQueryStateList1="1=1";
		     if(!selectedstateList.trim().equals("ALL"))
		    	 subQueryStateList1=" substr(fp3.stdist,1,2) in('"+selectedstateList+"')";
		     
		     String subQueryStateList2="1=1";
		     if(!selectedstateList.trim().equals("ALL"))
		    	 subQueryStateList2=" substr(ft.stdist,1,2) in('"+selectedstateList+"')";
		     
		     String recordReqQuery="";
		     String recordReqQuery1="";
		     if(!(recordRequired==null || recordRequired.equals(""))){
		    	// recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
		    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
		    	 parameters.put("recordSelected", recordRequired);
		     }
		     else{
		    	// recordReqQuery="T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?";
		    	 parameters.put("recordSelected", "ALL");
		     }	 
		     
		     
			reportQuery="SELECT * FROM ("+
					" select    ft.blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "+  
                    " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "+  
                    " WHERE fd.dcode = fp3.dcode "+                      
                    " and fc.ctr_code = fp3.ctr_code "+
                    " AND fp3.blkyear =ft.blkyear"+
                    " AND "+subQueryBlockList+" "+  
                    " and "+subQueryStateList1+" "+  
                    " and fp3.dtype ='1' "+                      
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.dname,fc.ctr_name "+                   
                    " UNION ALL "+                    
					" select    ft.blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "+  
                    " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "+  
                    " WHERE fd.donor_code = fp3.donor_code "+                      
                    " and fc.ctr_code = fd.donor_country "+  
                    " AND fp3.blkyear = ft.blkyear  "+  
                    " AND "+subQueryStateList2+" "+  
                    " and fd.donor_type ='01' "+  
                    " and "+subQueryBlockList+" "+  
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.donor_name,fc.ctr_name  order by  amount desc ) "+recordReqQuery1+"";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_DonerWise", ds);
		
		}
			
		else  if(districtDonorFlag.equals("1")){
			  parameters.put("districtDonorFlag","District Wise Receipt Report");
				String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
		        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
		        parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
		        parameters.put("selectedStateList",getState(selectedstateList));
		        String subQueryBlockList="1=1";
			     if(!selectedblockyearList.trim().equals("ALL"))
			    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
		        
		        String subQueryStateList="1=1";
			     if(!selectedstateList.trim().equals("ALL"))
			    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";

			     String recordReqQuery1="";
			     if(!(recordRequired==null || recordRequired.equals(""))){
			    	// recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
			    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
			    	 parameters.put("recordSelected", recordRequired);
			     }
			     else
			    	 parameters.put("recordSelected", "ALL");					

			     reportQuery="select * from(select distinct blkyear,distname, sum(associations) as associations,sname,sum(amount) AS amount from ( "+
						" select distinct ft.blkyear,fd.distname, count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname "+                          
                        " union all "+                         
                        " select distinct ft.blkyear,fd.distname, "+  
                        " count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname ) GROUP BY blkyear,distname,sname ORDER BY amount DESC) "+recordReqQuery1 ;
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("PRINTRECORD_DATA_SOURCE_DistrictWise", ds);
		}
		else if(districtDonorFlag.equals("2")){ //State Wise
					parameters.put("districtDonorFlag","State Wise Receipt Report");
					String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
			        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
			        parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
			        parameters.put("selectedStateList",getState(selectedstateList));
			        String subQueryBlockList="1=1";
				     if(!selectedblockyearList.trim().equals("ALL"))
				    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
			        
			        String subQueryStateList="1=1";
				     if(!selectedstateList.trim().equals("ALL"))
				    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";
				     
				
				     String recordReqQuery1="";
				     if(!(recordRequired==null || recordRequired.equals(""))){
				    	 parameters.put("recordSelected", recordRequired);
				    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
				     }
				     else
				    	 parameters.put("recordSelected", "ALL");
				    
				     reportQuery="select * from(select  blkyear,sum(associations) as associations,sname,sum(amount) AS amount from ( "+
						" select distinct ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname "+                          
                        " union all "+                         
                        " select distinct "+  
                        " ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname) group by blkyear,sname  ORDER BY amount DESC ) "+recordReqQuery1;
				     
				 	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
					parameters.put("PRINTRECORD_DATA_SOURCE_StateWise", ds);
		
		}
		else if(districtDonorFlag.equals("3")){ // Country Wise
			String selectBlockyearList = blockYearList.toString().replace("[", "'").replace("]", "'").replace(", ","','").trim();
			parameters.put("selectedBlockYearList",selectBlockyearList.replaceAll("'", ""));
			  parameters.put("districtDonorFlag","Country Wise Receipt Report");
			  
			  parameters.put("selectedCountryList","N/A");
			  String subQueryBlockList="1=1";
			     if(!selectBlockyearList.trim().equals("'ALL'"))
			    	 subQueryBlockList=" a.blkyear in("+selectBlockyearList+")";   
			     
		//	  String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
				String subStringStateList="1=1";
				String CountryList="1=1";
		
				String stList=stateList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
				parameters.put("selectedStateList",getState(stList));
				if(!stList.trim().equals("ALL")){
			//	if(!stList.trim().equals("'ALL'")){
					subStringStateList= "substr(d.stdist,1,2) IN ('"+stList+"')";
				}
				
				String contList=selectCountryList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
				parameters.put("selectedCountryList", getCountry(contList));
				if(!contList.trim().equals("ALL")){
					CountryList= "ctr_code in ('"+contList+"')";
				}
				 String recordReqQuery="";
			     String recordReqQuery1="";
			     if(!(recordRequired==null || recordRequired.equals(""))){
			    	 parameters.put("recordSelected", recordRequired);
			    	 recordReqQuery="T4 as (select * from TAB1),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<="+recordRequired+"";
			    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
			     }
			     else{
			    	 parameters.put("recordSelected", "ALL");
			    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1";
			     }
				
				//StringBuffer query;
				reportQuery = "WITH TAB1 AS ( select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from   (    SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount "    
							+"FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d      WHERE "
							+ subQueryBlockList
									+ "    AND b.blkyear =  A.blkyear  "  
							+"AND A.final_submit='Y' AND b.ctr_code = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "   
							+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  "  
							+"AND " +subStringStateList+" "   
							+"GROUP BY A.blkyear, ctr_name,b.ctr_code"

							+" UNION ALL  "   

							+"SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount     FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where "
							+ subQueryBlockList
									+ " and "  
							+"b.blkyear =  a.blkyear and      a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P'    AND " +subStringStateList+" "  
							+"group by a.blkyear, ctr_name, b.ctr_code  "

							+" union all  " 

							+"SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount      FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd "
							+"WHERE "
							+ subQueryBlockList
									+ "     AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn  "  
							+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')   "
							+"AND substr(A.rcn,-1,1)='R'   AND " +subStringStateList+" "
							+"AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code    GROUP BY A.blkyear, ctr_name, donor_country"

							+" UNION ALL   "   
							+"SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount       FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   "
							+"where "
							+ subQueryBlockList
									+ " and      b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   "
							+"A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND " +subStringStateList+" " 
							+"AND fd.donor_type='01'     AND d.rcn=fd.rcn     and b.donor_code=fd.donor_code " 
							+"GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name"
							+")     tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name  ORDER BY blkyear, amt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=1 ";
			/*	 query = new StringBuffer(
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
			*/
		     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("PRINTRECORD_DATA_SOURCE_CountryWise", ds);
	
		
		}
		else if(districtDonorFlag.equals("4")){ // Association Wise
			String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
				  parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
				  parameters.put("districtDonorFlag","Association Wise Receipt Report");
				  parameters.put("selectedStateList","N/A");
				  String subQueryBlockList="1=1";
				     if(!selectedblockyearList.trim().equals("ALL"))
				    	 subQueryBlockList=" p1.blkyear in('"+selectedblockyearList+"')";   
				     
				   
				     String recordReqQuery="";
				     String recordReqQuery1="";
				     if(!(recordRequired==null || recordRequired.equals(""))){
				    	 parameters.put("recordSelected", recordRequired);
				    	 recordReqQuery="T4 as (select * from TAB1 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<="+recordRequired+"";
				    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
				     }
				     else{
				    	 parameters.put("recordSelected", "ALL");
				    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM>=1";
				     }
				
				
				     
				     

				     reportQuery="WITH TAB1 AS ("
					      		    + " select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

									+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
									+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
									+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
									+ " where "+subQueryBlockList+" AND  "
									+ " substr(TL.rcn,-1,1)='R' "
									+ " AND fi.rcn=TL.rcn "
									+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
									+ "  and P1.blkyear = TL.blkyear and  "
									+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

									+ "  UNION ALL  "
									+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
									+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
									+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
									+ "  where "+subQueryBlockList+" AND   "
									+ "  substr(TL.rcn,-1,1)='P' "
									+ "  and fi.rcn=TL.rcn "
									+ "  and P1.blkyear = TL.blkyear   "
									+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

									+ "  union all "

									+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
									+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district   "
									+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
									+ "   WHERE "+subQueryBlockList+" AND  "
									+ "   substr(TL.rcn,-1,1)='R' "
									+ "   AND fi.rcn=TL.rcn "
									+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
									+ "  and P1.blkyear = TL.blkyear   "
									+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

									+ "  UNION ALL "

									+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
									+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
									+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
									+ "  WHERE "+subQueryBlockList+" AND   "
									+ "  substr(TL.rcn,-1,1)='P' "
									+ "  AND fi.rcn=TL.rcn "
									+ "   and P1.blkyear = TL.blkyear  "
									+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

									+ "  ) t1 "
									+ "  ORDER BY blkyear, foramt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=1 ";
				    		 
				     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
						parameters.put("PRINTRECORD_DATA_SOURCE_AssociationWise", ds);
			
			
			
			
		}
			
		String tsPath = "/Reports/Top_Doner_District_Report.jrxml";
		String fileName = "TopDonerDistrictReport.pdf";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	
	}

	@Override
	protected void generateExcel() throws Exception {

		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    parameters.put("districtDonorFlag1", districtDonorFlag);
	    String reportQuery="";
	    // Donor Wise Report 
		if(districtDonorFlag.equals("0")){
			  parameters.put("districtDonorFlag","Donor Wise Receipt Report");
	         String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	         String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	         parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
	         parameters.put("selectedStateList",getState(selectedstateList));
	         String subQueryBlockList="1=1";
		     if(!selectedblockyearList.trim().equals("ALL"))
		    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
	         
	         String subQueryStateList1="1=1";
		     if(!selectedstateList.trim().equals("ALL"))
		    	 subQueryStateList1=" substr(fp3.stdist,1,2) in('"+selectedstateList+"')";
		     
		     String subQueryStateList2="1=1";
		     if(!selectedstateList.trim().equals("ALL"))
		    	 subQueryStateList2=" substr(ft.stdist,1,2) in('"+selectedstateList+"')";
		     
		     String recordReqQuery="";
		     String recordReqQuery1="";
		     if(!(recordRequired==null || recordRequired.equals(""))){
		    	// recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
		    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
		    	 parameters.put("recordSelected", recordRequired);
		     }
		     else{
		    	// recordReqQuery="T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?";
		    	 parameters.put("recordSelected", "ALL");
		     }	 
		     
		     
			reportQuery="SELECT * FROM ("+
					" select    ft.blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "+  
                    " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "+  
                    " WHERE fd.dcode = fp3.dcode "+                      
                    " and fc.ctr_code = fp3.ctr_code "+
                    " AND fp3.blkyear =ft.blkyear"+
                    " AND "+subQueryBlockList+" "+  
                    " and "+subQueryStateList1+" "+  
                    " and fp3.dtype ='1' "+                      
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.dname,fc.ctr_name "+                   
                    " UNION ALL "+                    
					" select    ft.blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "+  
                    " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "+  
                    " WHERE fd.donor_code = fp3.donor_code "+                      
                    " and fc.ctr_code = fd.donor_country "+  
                    " AND fp3.blkyear = ft.blkyear  "+  
                    " AND "+subQueryStateList2+" "+  
                    " and fd.donor_type ='01' "+  
                    " and "+subQueryBlockList+" "+  
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.donor_name,fc.ctr_name  order by  amount desc ) "+recordReqQuery1+" ";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_DonerWise", ds);
		
		}
			
		else  if(districtDonorFlag.equals("1")){
			  parameters.put("districtDonorFlag","District Wise Receipt Report");
				String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
		        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
		        parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
		        parameters.put("selectedStateList",getState(selectedstateList));
		        String subQueryBlockList="1=1";
			     if(!selectedblockyearList.trim().equals("ALL"))
			    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
		        
		        String subQueryStateList="1=1";
			     if(!selectedstateList.trim().equals("ALL"))
			    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";

			     String recordReqQuery1="";
			     if(!(recordRequired==null || recordRequired.equals(""))){
			    	// recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
			    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
			    	 parameters.put("recordSelected", recordRequired);
			     }
			     else
			    	 parameters.put("recordSelected", "ALL");
				
				
				
				reportQuery="select * from(select distinct blkyear,distname, sum(associations) as associations,sname,sum(amount) AS amount from ( "+
						" select distinct ft.blkyear,fd.distname, count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname "+                          
                        " union all "+                         
                        " select distinct ft.blkyear,fd.distname, "+  
                        " count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname  ) GROUP BY blkyear,distname,sname ORDER BY amount DESC) "+recordReqQuery1 ;
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("PRINTRECORD_DATA_SOURCE_DistrictWise", ds);
		}
			else
				//State Wise
				if(districtDonorFlag.equals("2")){
					  parameters.put("districtDonorFlag","State Wise Receipt Report");
					String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
			        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
			        parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
			        parameters.put("selectedStateList",getState(selectedstateList));
			        String subQueryBlockList="1=1";
				     if(!selectedblockyearList.trim().equals("ALL"))
				    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
			        
			        String subQueryStateList="1=1";
				     if(!selectedstateList.trim().equals("ALL"))
				    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";
				     
				
				     String recordReqQuery1="";
				     if(!(recordRequired==null || recordRequired.equals(""))){
				    	 parameters.put("recordSelected", recordRequired);
				    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
				     }
				     else
				    	 parameters.put("recordSelected", "ALL");
				    
				     reportQuery="select * from(select  blkyear,sum(associations) as associations,sname,sum(amount) AS amount from ( "+
						" select distinct ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname "+                          
                        " union all "+                         
                        " select distinct "+  
                        " ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname ) group by blkyear,sname  ORDER BY amount DESC ) "+recordReqQuery1;
				     
				 	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
					parameters.put("PRINTRECORD_DATA_SOURCE_StateWise", ds);
			}
				else if(districtDonorFlag.equals("3")){ // Country Wise
					String selectBlockyearList = blockYearList.toString().replace("[", "'").replace("]", "'").replace(", ","','").trim();
					parameters.put("selectedBlockYearList",selectBlockyearList.replaceAll("'", ""));
					  parameters.put("districtDonorFlag","Country Wise Receipt Report");
					  
					  parameters.put("selectedCountryList","N/A");
					  String subQueryBlockList="1=1";
					     if(!selectBlockyearList.trim().equals("'ALL'"))
					    	 subQueryBlockList=" a.blkyear in("+selectBlockyearList+")";   
					     
				//	  String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
						String subStringStateList="1=1";
						String CountryList="1=1";
				
						String stList=stateList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
						parameters.put("selectedStateList",getState(stList));
						if(!stList.trim().equals("ALL")){
					//	if(!stList.trim().equals("'ALL'")){
							subStringStateList= "substr(d.stdist,1,2) IN ('"+stList+"')";
						}
						
						String contList=selectCountryList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
						parameters.put("selectedCountryList", getCountry(contList));
						if(!contList.trim().equals("ALL")){
							CountryList= "ctr_code in ('"+contList+"')";
						}
						 String recordReqQuery="";
					     String recordReqQuery1="";
					     if(!(recordRequired==null || recordRequired.equals(""))){
					    	 parameters.put("recordSelected", recordRequired);
					    	 recordReqQuery="T4 as (select * from TAB1),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<="+recordRequired+"";
					    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
					     }
					     else{
					    	 parameters.put("recordSelected", "ALL");
					    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1";
					     }
						//StringBuffer query;
						reportQuery = "WITH TAB1 AS ( select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from   (    SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount "    
									+"FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d      WHERE "
									+ subQueryBlockList
											+ "    AND b.blkyear =  A.blkyear  "  
									+"AND A.final_submit='Y' AND b.ctr_code = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "   
									+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  "  
									+"AND " +subStringStateList+" "   
									+"GROUP BY A.blkyear, ctr_name,b.ctr_code"

									+" UNION ALL  "   

									+"SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount     FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where "
									+ subQueryBlockList
											+ " and "  
									+"b.blkyear =  a.blkyear and      a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P'    AND " +subStringStateList+" "  
									+"group by a.blkyear, ctr_name, b.ctr_code  "

									+" union all  " 

									+"SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount      FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd "
									+"WHERE "
									+ subQueryBlockList
											+ "     AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn  "  
									+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')   "
									+"AND substr(A.rcn,-1,1)='R'   AND " +subStringStateList+" "
									+"AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code    GROUP BY A.blkyear, ctr_name, donor_country"

									+" UNION ALL   "   
									+"SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount       FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   "
									+"where "
									+ subQueryBlockList
											+ " and      b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   "
									+"A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND " +subStringStateList+" " 
									+"AND fd.donor_type='01'     AND d.rcn=fd.rcn     and b.donor_code=fd.donor_code " 
									+"GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name"
									+")     tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name  ORDER BY blkyear, amt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=1 ";
					/*	 query = new StringBuffer(
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
					*/
				     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
						parameters.put("PRINTRECORD_DATA_SOURCE_CountryWise", ds);
			
				
				}
				else if(districtDonorFlag.equals("4")){ // Association Wise
					
					String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
						  parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
						  parameters.put("districtDonorFlag","Association Wise Receipt Report");
						  parameters.put("selectedStateList","N/A");
						  String subQueryBlockList="1=1";
						     if(!selectedblockyearList.trim().equals("ALL"))
						    	 subQueryBlockList=" p1.blkyear in('"+selectedblockyearList+"')";   
						     
						   
						     String recordReqQuery="";
						     String recordReqQuery1="";
						     if(!(recordRequired==null || recordRequired.equals(""))){
						    	 parameters.put("recordSelected", recordRequired);
						    	 recordReqQuery="T4 as (select * from TAB1 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<="+recordRequired+"";
						    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
						     }
						     else{
						    	 parameters.put("recordSelected", "All");
						    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM>=1";
						     }
						
						
						     
						     

						     reportQuery="WITH TAB1 AS ("
							      		    + " select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

											+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
											+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
											+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
											+ " where "+subQueryBlockList+" AND  "
											+ " substr(TL.rcn,-1,1)='R' "
											+ " AND fi.rcn=TL.rcn "
											+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
											+ "  and P1.blkyear = TL.blkyear and  "
											+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

											+ "  UNION ALL  "
											+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
											+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
											+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
											+ "  where "+subQueryBlockList+" AND   "
											+ "  substr(TL.rcn,-1,1)='P' "
											+ "  and fi.rcn=TL.rcn "
											+ "  and P1.blkyear = TL.blkyear   "
											+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

											+ "  union all "

											+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
											+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district   "
											+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
											+ "   WHERE "+subQueryBlockList+" AND  "
											+ "   substr(TL.rcn,-1,1)='R' "
											+ "   AND fi.rcn=TL.rcn "
											+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
											+ "  and P1.blkyear = TL.blkyear   "
											+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

											+ "  UNION ALL "

											+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
											+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
											+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
											+ "  WHERE "+subQueryBlockList+" AND   "
											+ "  substr(TL.rcn,-1,1)='P' "
											+ "  AND fi.rcn=TL.rcn "
											+ "   and P1.blkyear = TL.blkyear  "
											+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

											+ "  ) t1 "
											+ "  ORDER BY blkyear, foramt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=1 ";
						    		 
						     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
								parameters.put("PRINTRECORD_DATA_SOURCE_AssociationWise", ds);
					
					
					
					
				}
		/*String tsPath=""; String fileName="";
		if(districtDonorFlag.equals("4")){
			 tsPath = "/Reports/Top_Doner_District_Report.jrxml";
			 fileName = "TopDonerDistrictReport.xls";	
		}
		else{*/
		String	tsPath = "/Reports/Top_Doner_District_Excel_Report.jrxml";
		String	 fileName = "TopDonerDistrictReport.xls";	
		

		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public List<DistrictDonorReceipt> getDistrictDonorReceiptReport() {
		return districtDonorReceiptReport;
	}

	public void setDistrictDonorReceiptReport(
			List<DistrictDonorReceipt> districtDonorReceiptReport) {
		this.districtDonorReceiptReport = districtDonorReceiptReport;
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

	
	public List<String> getStateList() {
		return stateList;
	}

	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}

	public String getRecordRequired() {
		return recordRequired;
	}

	public void setRecordRequired(String recordRequired) {
		this.recordRequired = recordRequired;
	}

	public String getDistrictDonorFlag() {
		return districtDonorFlag;
	}

	public void setDistrictDonorFlag(String districtDonorFlag) {
		this.districtDonorFlag = districtDonorFlag;
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
	public List<String> getSelectCountryList() {
		return selectCountryList;
	}
	public void setSelectCountryList(List<String> selectCountryList) {
		this.selectCountryList = selectCountryList;
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
	public String getState(String selecteStateList) throws Exception {
		   if(selecteStateList.contains("ALL")){
			   return "ALL";
		   }
		   else{	if(connection == null) {
					throw new Exception("Invalid connection");
				}
				StringBuffer stateList=new StringBuffer();		
				StringBuffer query = new StringBuffer("SELECT SNAME FROM TM_STATE WHERE SCODE IN('"+selecteStateList+"')");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					stateList=stateList.append(rs.getString(1)+",");
				}
				return stateList.toString();
			
			  }
			 }
	public String getCountry(String selecteCountryList) throws Exception {
		   if(selecteCountryList.contains("ALL")){
			   return "ALL";
		   }
		   else{	if(connection == null) {
					throw new Exception("Invalid connection");
				}
				StringBuffer countryList=new StringBuffer();		
				StringBuffer query = new StringBuffer("SELECT CTR_NAME FROM TM_COUNTRY WHERE CTR_CODE IN('"+selecteCountryList+"')");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					countryList=countryList.append(rs.getString(1)+",");
				}
				return countryList.toString();
			
			  }
			 }
	public void setBlockYearList(List<String> blockYearList) {
		this.blockYearList = blockYearList;
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
	    // Donor Wise Report 
		if(districtDonorFlag.equals("0")){
			  parameters.put("districtDonorFlag","Donor Wise Receipt Report");
	         String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	         String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	         parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
	         parameters.put("selectedStateList",getState(selectedstateList));
	         String subQueryBlockList="1=1";
		     if(!selectedblockyearList.trim().equals("ALL"))
		    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
	         
	         String subQueryStateList1="1=1";
		     if(!selectedstateList.trim().equals("ALL"))
		    	 subQueryStateList1=" substr(fp3.stdist,1,2) in('"+selectedstateList+"')";
		     
		     String subQueryStateList2="1=1";
		     if(!selectedstateList.trim().equals("ALL"))
		    	 subQueryStateList2=" substr(ft.stdist,1,2) in('"+selectedstateList+"')";
		     
		     String recordReqQuery="";
		     String recordReqQuery1="";
		     if(!(recordRequired==null || recordRequired.equals(""))){
		    	// recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
		    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
		    	 parameters.put("recordSelected", recordRequired);
		     }
		     else{
		    	// recordReqQuery="T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?";
		    	 parameters.put("recordSelected", "ALL");
		     }	 
		     
		     
			reportQuery="SELECT * FROM ("+
					" select    ft.blkyear,fd.dname,fc.ctr_name,nvl(SUM(fp3.amount),0) AS amount "+  
                    " from fc_inst_donor fd,tm_country fc,fc_fc3_part3 fp3,fc_fc3_tally ft "+  
                    " WHERE fd.dcode = fp3.dcode "+                      
                    " and fc.ctr_code = fp3.ctr_code "+
                    " AND fp3.blkyear =ft.blkyear"+
                    " AND "+subQueryBlockList+" "+  
                    " and "+subQueryStateList1+" "+  
                    " and fp3.dtype ='1' "+                      
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.dname,fc.ctr_name "+                   
                    " UNION ALL "+                    
					" select    ft.blkyear,fd.donor_name,fc.ctr_name, nvl(SUM(fp3.amount),0) AS amount "+  
                    " FROM fc_fc3_donor fd,tm_country fc,fc_fc3_donor_wise fp3,fc_fc3_tally ft "+  
                    " WHERE fd.donor_code = fp3.donor_code "+                      
                    " and fc.ctr_code = fd.donor_country "+  
                    " AND fp3.blkyear = ft.blkyear  "+  
                    " AND "+subQueryStateList2+" "+  
                    " and fd.donor_type ='01' "+  
                    " and "+subQueryBlockList+" "+  
                    " and ft.final_submit ='Y' "+  
                    " AND ft.rcn = fp3.rcn "+
                    " GROUP BY ft.blkyear,fd.donor_name,fc.ctr_name  order by  amount desc ) "+recordReqQuery1+" ";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_DonerWise", ds);
		
		}
			
		else  if(districtDonorFlag.equals("1")){
			  parameters.put("districtDonorFlag","District Wise Receipt Report");
				String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
		        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
		        parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
		        parameters.put("selectedStateList",getState(selectedstateList));
		        String subQueryBlockList="1=1";
			     if(!selectedblockyearList.trim().equals("ALL"))
			    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
		        
		        String subQueryStateList="1=1";
			     if(!selectedstateList.trim().equals("ALL"))
			    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";

			     String recordReqQuery1="";
			     if(!(recordRequired==null || recordRequired.equals(""))){
			    	// recordReqQuery="T4 as (select * from t2 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<=?";
			    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
			    	 parameters.put("recordSelected", recordRequired);
			     }
			     else
			    	 parameters.put("recordSelected", "ALL");
				
				
				
				reportQuery="select * from(select distinct blkyear,distname, sum(associations) as associations,sname,sum(amount) AS amount from ( "+
						" select distinct ft.blkyear,fd.distname, count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname "+                          
                        " union all "+                         
                        " select distinct ft.blkyear,fd.distname, "+  
                        " count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM TM_district fd,tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE fd.distcode = substr(fp1.stdist, -3,3) "+  
                        " AND "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fd.distname,fs.sname ) GROUP BY blkyear,distname,sname ORDER BY amount DESC) "+recordReqQuery1 ;
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("PRINTRECORD_DATA_SOURCE_DistrictWise", ds);
		}
			else
				//State Wise
				if(districtDonorFlag.equals("2")){
					  parameters.put("districtDonorFlag","State Wise Receipt Report");
					String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
			        String selectedstateList = stateList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
			        parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
			        parameters.put("selectedStateList",getState(selectedstateList));
			        String subQueryBlockList="1=1";
				     if(!selectedblockyearList.trim().equals("ALL"))
				    	 subQueryBlockList=" ft.blkyear in('"+selectedblockyearList+"')";
			        
			        String subQueryStateList="1=1";
				     if(!selectedstateList.trim().equals("ALL"))
				    	 subQueryStateList=" substr(fp1.stdist,1,2) in('"+selectedstateList+"')";
				     
				
				     String recordReqQuery1="";
				     if(!(recordRequired==null || recordRequired.equals(""))){
				    	 parameters.put("recordSelected", recordRequired);
				    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
				     }
				     else
				    	 parameters.put("recordSelected", "ALL");
				    
				     reportQuery="select * from(select  blkyear,sum(associations) as associations,sname,sum(amount) AS amount from ( "+
						" select distinct ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.for_amt+fp1.bk_int +fp1.oth_int) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1 fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear   "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname "+                          
                        " union all "+                         
                        " select distinct "+  
                        " ft.blkyear,count(distinct fp1.rcn) as associations,fs.sname, "+  
                        " sum(fp1.SOURCE_FOR_AMT+fp1.bk_int +fp1.SOURCE_LOCAL_AMT) AS amount "+  
                        " FROM tm_state fs,fc_fc3_part1_new fp1,fc_fc3_tally ft "+  
                        " WHERE "+  
                        " "+subQueryStateList+" "+  
                        " AND fp1.blkyear=ft.blkyear "+  
                        " and substr(fp1.stdist,1,2)=fs.scode "+  
                        " and "+subQueryBlockList+" "+  
                        " and ft.final_submit='Y' "+  
                        " and ft.rcn=fp1.rcn "+  
                        " GROUP BY ft.blkyear,fs.sname ) group by blkyear,sname  ORDER BY amount DESC ) "+recordReqQuery1;
				     
				 	ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
					parameters.put("PRINTRECORD_DATA_SOURCE_StateWise", ds);
			}
				else if(districtDonorFlag.equals("3")){ // Country Wise
					String selectBlockyearList = blockYearList.toString().replace("[", "'").replace("]", "'").replace(", ","','").trim();
					parameters.put("selectedBlockYearList",selectBlockyearList.replaceAll("'", ""));
					  parameters.put("districtDonorFlag","Country Wise Receipt Report");
					  
					  parameters.put("selectedCountryList","N/A");
					  String subQueryBlockList="1=1";
					     if(!selectBlockyearList.trim().equals("'ALL'"))
					    	 subQueryBlockList=" a.blkyear in("+selectBlockyearList+")";   
					     
				//	  String lastblockYearList=selectBlockyearList.trim().substring(selectBlockyearList.length()-4, selectBlockyearList.length()); 
						String subStringStateList="1=1";
						String CountryList="1=1";
				
						String stList=stateList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
						parameters.put("selectedStateList",getState(stList));
						if(!stList.trim().equals("ALL")){
					//	if(!stList.trim().equals("'ALL'")){
							subStringStateList= "substr(d.stdist,1,2) IN ('"+stList+"')";
						}
						
						String contList=selectCountryList.toString().replace("[", "").replace("]", "").replace(", ", "','").trim();
						parameters.put("selectedCountryList", getCountry(contList));
						if(!contList.trim().equals("ALL")){
							CountryList= "ctr_code in ('"+contList+"')";
						}
						 String recordReqQuery="";
					     String recordReqQuery1="";
					     if(!(recordRequired==null || recordRequired.equals(""))){
					    	 parameters.put("recordSelected", recordRequired);
					    	 recordReqQuery="T4 as (select * from TAB1),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<="+recordRequired+"";
					    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
					     }
					     else{
					    	 parameters.put("recordSelected", "ALL");
					    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1";
					     }
						//StringBuffer query;
						reportQuery = "WITH TAB1 AS ( select tab.blkyear, tab.ctr_name,sum(tab.amount) amt from   (    SELECT A.blkyear, ctr_name, b.ctr_code, sum(amount) amount "    
									+"FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_india d      WHERE "
									+ subQueryBlockList
											+ "    AND b.blkyear =  A.blkyear  "  
									+"AND A.final_submit='Y' AND b.ctr_code = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn "   
									+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')    AND substr(A.rcn,-1,1)='R'  "  
									+"AND " +subStringStateList+" "   
									+"GROUP BY A.blkyear, ctr_name,b.ctr_code"

									+" UNION ALL  "   

									+"SELECT a.blkyear, ctr_name, b.ctr_code, sum(amount) amount     FROM fc_fc3_tally A, fc_fc3_part3 b, tm_country c, fc_pp_india d      where "
									+ subQueryBlockList
											+ " and "  
									+"b.blkyear =  a.blkyear and      a.final_submit='Y' and b.ctr_code = c.ctr_code and     A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear   AND substr(A.rcn,-1,1)='P'    AND " +subStringStateList+" "  
									+"group by a.blkyear, ctr_name, b.ctr_code  "

									+" union all  " 

									+"SELECT A.blkyear, ctr_name, donor_country, sum(amount) amount      FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_india d, fc_fc3_donor fd "
									+"WHERE "
									+ subQueryBlockList
											+ "     AND b.blkyear =  A.blkyear    AND A.final_submit='Y' AND fd.donor_country = c.ctr_code    AND A.rcn=b.rcn AND A.rcn=d.rcn  "  
									+"and (d.reg_date is null or trunc(d.reg_date) < trunc(to_date('01-04-'||substr(a.blkyear,-4,4),'dd-mm-yyyy'))) and (d.status <> 'D')   "
									+"AND substr(A.rcn,-1,1)='R'   AND " +subStringStateList+" "
									+"AND fd.donor_type='01'    AND d.rcn=fd.rcn 	and b.donor_code=fd.donor_code    GROUP BY A.blkyear, ctr_name, donor_country"

									+" UNION ALL   "   
									+"SELECT a.blkyear, ctr_name, donor_country,  sum(amount) amount       FROM fc_fc3_tally A, fc_fc3_donor_wise b, tm_country c, fc_pp_india d, fc_fc3_donor fd   "
									+"where "
									+ subQueryBlockList
											+ " and      b.blkyear =  a.blkyear and      a.final_submit='Y' and fd.donor_country = c.ctr_code and   "
									+"A.rcn=b.rcn    AND A.rcn=d.rcn    AND d.blkyear=a.blkyear     AND substr(A.rcn,-1,1)='P'     AND " +subStringStateList+" " 
									+"AND fd.donor_type='01'     AND d.rcn=fd.rcn     and b.donor_code=fd.donor_code " 
									+"GROUP BY a.blkyear, ctr_name, donor_country, d.asso_name"
									+")     tab where "+CountryList+" GROUP BY tab.blkyear, tab.ctr_name  ORDER BY blkyear, amt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=1 ";
					
				     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
						parameters.put("PRINTRECORD_DATA_SOURCE_CountryWise", ds);
			
				
				}
				else if(districtDonorFlag.equals("4")){ // Association Wise
				
				String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
					  parameters.put("selectedBlockYearList",selectedblockyearList.replaceAll("'", ""));
					  parameters.put("districtDonorFlag","Association Wise Receipt Report");
					  parameters.put("selectedStateList","N/A");
					  String subQueryBlockList="1=1";
					     if(!selectedblockyearList.trim().equals("ALL"))
					    	 subQueryBlockList=" p1.blkyear in('"+selectedblockyearList+"')";   
					     
					   
					     String recordReqQuery="";
					     String recordReqQuery1="";
					     if(!(recordRequired==null || recordRequired.equals(""))){
					    	 parameters.put("recordSelected", recordRequired);
					    	 recordReqQuery="T4 as (select * from TAB1 where rownum<='"+recordRequired+"'),T3 AS (SELECT T4.*, ROWNUM RN FROM T4 WHERE ROWNUM<="+recordRequired+"";
					    	 recordReqQuery1="where rownum<='"+recordRequired+"'";
					     }
					     else{
					    	 parameters.put("recordSelected", "All");
					    	 recordReqQuery="T3 AS (SELECT TAB1.*, ROWNUM RN FROM TAB1 WHERE ROWNUM>=1";
					     }
					
					
					     
					     

					     reportQuery="WITH TAB1 AS ("
						      		    + " select t1.*, (select sname from tm_state where t1.state = scode) state_name, (select DISTNAME from tm_district where t1.state = scode and t1.district=DISTCODE) district_name from ( "

										+ " SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
										+ " nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district "
										+ " FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_india fi  "
										+ " where "+subQueryBlockList+" AND  "
										+ " substr(TL.rcn,-1,1)='R' "
										+ " AND fi.rcn=TL.rcn "
										+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
										+ "  and P1.blkyear = TL.blkyear and  "
										+ "  TL.final_submit = 'Y' AND TL.rcn = P1.rcn  "

										+ "  UNION ALL  "
										+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.dt_mdf, 'dd-mm-yyyy') submission_date, nvl(for_amt,0) AS foramt,  "
										+ "  nvl(for_amt + bk_int + oth_int,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
										+ "  FROM fc_fc3_part1 P1,fc_fc3_tally TL, fc_pp_india fi  "
										+ "  where "+subQueryBlockList+" AND   "
										+ "  substr(TL.rcn,-1,1)='P' "
										+ "  and fi.rcn=TL.rcn "
										+ "  and P1.blkyear = TL.blkyear   "
										+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

										+ "  union all "

										+ "  SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt,  "
										+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district   "
										+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_india fi   "
										+ "   WHERE "+subQueryBlockList+" AND  "
										+ "   substr(TL.rcn,-1,1)='R' "
										+ "   AND fi.rcn=TL.rcn "
										+ "  and (reg_date is null or trunc(reg_date) < trunc(to_date('01-04-'||substr(P1.blkyear,-4,4),'dd-mm-yyyy'))) and (status <> 'D') "
										+ "  and P1.blkyear = TL.blkyear   "
										+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn  "

										+ "  UNION ALL "

										+ "   SELECT P1.blkyear, tl.rcn, fi.asso_name, to_char(TL.final_submission_date, 'dd-mm-yyyy') submission_date, nvl(SOURCE_FOR_AMT,0) AS foramt, "
										+ "  nvl(SOURCE_FOR_AMT + bk_int + SOURCE_LOCAL_AMT,0) AS totalamount, substr(P1.stdist,1,2) state, substr(P1.stdist,-3,3) district  "
										+ "  FROM fc_fc3_part1_new P1,fc_fc3_tally TL, fc_pp_india fi  "
										+ "  WHERE "+subQueryBlockList+" AND   "
										+ "  substr(TL.rcn,-1,1)='P' "
										+ "  AND fi.rcn=TL.rcn "
										+ "   and P1.blkyear = TL.blkyear  "
										+ "  and TL.final_submit = 'Y' and TL.rcn = P1.rcn "

										+ "  ) t1 "
										+ "  ORDER BY blkyear, foramt DESC),"+recordReqQuery+ " ) SELECT * FROM T3 WHERE RN>=1 ";
					    		 
					     ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
							parameters.put("PRINTRECORD_DATA_SOURCE_AssociationWise", ds);
				
				
				
				
			}
			
		String tsPath = "/Reports/Top_Doner_District_CSV_Report.jrxml";
		String fileName = "TopDonerDistrictReport";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
		// TODO Auto-generated method stub
		
	
		
	}
}
