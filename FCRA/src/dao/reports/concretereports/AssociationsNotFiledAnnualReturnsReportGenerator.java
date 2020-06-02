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
import models.reports.AssociationsNotFiledAnnualReturnsReport;
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

public class AssociationsNotFiledAnnualReturnsReportGenerator  extends MISReportGenerator{
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

	private int virtualizationMaxSize = 200;
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
	private List<AssociationsNotFiledAnnualReturnsReport> associationsNotFiledAnnualReturnsList;
	
	public AssociationsNotFiledAnnualReturnsReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		Map  parameters = new HashMap();
		
		String subQuery = cureentBlockYear;
		
		if(subQuery.equals("ALL")){
			subQuery= "1=1";
		}
		
		//parameters.put("reportDisplayType", reportDisplayType);
		//parameters.put("reportStatusDisplayType", reportStatusDisplayType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
/*	    String  subQueryServices="1=1";
	    
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
    */
	    String reportQuery="";


		// l means samll L for live red flagged cases 
		reportQuery =" Select FI.rcn as Regno,rcn,FI.asso_name,(case when fi.new_old='N' "
				+  " then nvl(fi.asso_address,' ')||','||nvl(fi.asso_town_city,' ')||nvl(fi.asso_pin,'') "
				+  " else nvl(fi.add1,' ')||', '||nvl(fi.add2,' ')||','||nvl(fi.add3,' ')||'-'|| "
				+  " nvl(asso_pin,'') end) as AsoAddress, (select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+  " (select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name "
				+ " from fc_india FI "
				+ "  where FI.status != 'D' and (FI.reg_date is null or FI.reg_date<trunc(to_date('01-04-'||substr('"+subQuery+"',-4,4),'dd-mm-yyyy'))) "
				+ " and FI.rcn not in (select rcn from fc_fc3_tally where final_submit='Y' and blkyear='"+subQuery+"' and substr(rcn,-1,1)='R') and FI.cancel_status='N' and FI.association_type is null order by upper(asso_name) ";
	
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
            
		parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);    		    		
	//
		String tsPath = "/Reports/AssociationsNotFiledAnnualReturns.jrxml";
		String fileName = "Associations-Not-Filed-Annual-Returns";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
				fileName);// TODO Auto-generated method stub	    		    	
    
	}

	@Override
	protected void generateCSV() throws Exception {
		Map  parameters = new HashMap();
		String subQuery = cureentBlockYear;

	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
		String reportQuery="";	    

	    reportQuery =" Select FI.rcn as Regno,rcn,FI.asso_name,(case when fi.new_old='N' "
				+  " then nvl(fi.asso_address,' ')||','||nvl(fi.asso_town_city,' ')||nvl(fi.asso_pin,'') "
				+  " else nvl(fi.add1,' ')||', '||nvl(fi.add2,' ')||','||nvl(fi.add3,' ')||'-'|| "
				+  " nvl(asso_pin,'') end) as AsoAddress, (select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+  " (select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name "
				+ " from fc_india FI "
				+ "  where FI.status != 'D' and (FI.reg_date is null or FI.reg_date<trunc(to_date('01-04-'||substr('"+subQuery+"',-4,4),'dd-mm-yyyy'))) "
				+ " and FI.rcn not in (select rcn from fc_fc3_tally where final_submit='Y' and blkyear='"+subQuery+"' and substr(rcn,-1,1)='R') and FI.cancel_status='N' and FI.association_type is null order by upper(asso_name) ";
	
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
            
		parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);  		
		
		String tsPath = "/Reports/AssociationsNotFiledAnnualReturnsCsv.jrxml";
		String fileName = "Associations-Not-Filed-Annual-Returns";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
				fileName);// TODO Auto-generated method stub	    	
	}
	
	@Override
	protected void generateExcel() throws Exception {
		Map  parameters = new HashMap();
		String subQuery = cureentBlockYear;

	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
		String reportQuery="";	    

	    reportQuery =" Select FI.rcn as Regno,rcn,FI.asso_name,(case when fi.new_old='N' "
				+  " then nvl(fi.asso_address,' ')||','||nvl(fi.asso_town_city,' ')||nvl(fi.asso_pin,'') "
				+  " else nvl(fi.add1,' ')||', '||nvl(fi.add2,' ')||','||nvl(fi.add3,' ')||'-'|| "
				+  " nvl(asso_pin,'') end) as AsoAddress, (select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+  " (select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name "
				+ " from fc_india FI "
				+ "  where FI.status != 'D' and (FI.reg_date is null or FI.reg_date<trunc(to_date('01-04-'||substr('"+subQuery+"',-4,4),'dd-mm-yyyy'))) "
				+ " and FI.rcn not in (select rcn from fc_fc3_tally where final_submit='Y' and blkyear='"+subQuery+"' and substr(rcn,-1,1)='R') and FI.cancel_status='N' and FI.association_type is null order by upper(asso_name) ";
	
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
            
		parameters.put("D_RED_FLAGGED_ASSOCIATIONS_CATEGORYWISE", ds);  		
		
		String tsPath = "/Reports/AssociationsNotFiledAnnualReturns.jrxml";
		String fileName = "Associations-Not-Filed-Annual-Returns";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
				fileName);// TODO Auto-generated method stub	    	
		
	}

	@Override
	protected void generateHTML() throws Exception {
		associationsNotFiledAnnualReturnsList = getAssociationsNotFiledAnnualReturns();
		totalRecords=getTotalRecords();
		

	}
	private List<AssociationsNotFiledAnnualReturnsReport> getAssociationsNotFiledAnnualReturns() throws Exception{
		
		List<AssociationsNotFiledAnnualReturnsReport> associationsNotFiledAnnualReturnsList = new ArrayList<AssociationsNotFiledAnnualReturnsReport>();
		String countQuery = " with temp as ( "
				+ " Select FI.rcn as Regno,rcn,FI.asso_name,(case when fi.new_old='N'  "
				+ " then nvl(fi.asso_address,' ')||','||nvl(fi.asso_town_city,' ')||nvl(fi.asso_pin,'') "
				+ " else nvl(fi.add1,' ')||', '||nvl(fi.add2,' ')||','||nvl(fi.add3,' ')||'-'|| "
				+ " nvl(asso_pin,'') end) as AsoAddress, (select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+ " (select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name "
				+ " from fc_india FI  "
				+ " where FI.status != 'D' and (FI.reg_date is null or FI.reg_date<trunc(to_date('01-04-'||substr( ? ,-4,4),'dd-mm-yyyy')))  "
				+ " and FI.rcn not in (select rcn from fc_fc3_tally where final_submit='Y' and blkyear= ?  and substr(rcn,-1,1)='R') and FI.cancel_status='N' and FI.association_type is null order by upper(asso_name)) "
				+ " select count(*) from temp ";

		String query = " with temp as ( "
				+ " Select FI.rcn as Regno,rcn,FI.asso_name,(case when fi.new_old='N'  "
				+ " then nvl(fi.asso_address,' ')||','||nvl(fi.asso_town_city,' ')||nvl(fi.asso_pin,'') "
				+ " else nvl(fi.add1,' ')||', '||nvl(fi.add2,' ')||','||nvl(fi.add3,' ')||'-'|| "
				+ " nvl(asso_pin,'') end) as AsoAddress, (select sname from tm_state where scode=substr(stdist,1,2)) state_name, "
				+ " (select DISTNAME from tm_district where scode=substr(stdist,1,2) and DISTCODE=substr(stdist,-3,3)) district_name "
				+ " from fc_india FI  "
				+ " where FI.status != 'D' and (FI.reg_date is null or FI.reg_date<trunc(to_date('01-04-'||substr( ? ,-4,4),'dd-mm-yyyy')))  "
				+ " and FI.rcn not in (select rcn from fc_fc3_tally where final_submit='Y' and blkyear= ?  and substr(rcn,-1,1)='R') and FI.cancel_status='N' and FI.association_type is null order by upper(asso_name)), "
				+ " P2 as (select temp.*, ROWNUM RN from temp) select * from P2 where RN BETWEEN ? and ? ";

			 PreparedStatement statement = connection.prepareStatement(countQuery.toString());
			 statement.setString(1, cureentBlockYear);
			 statement.setString(2, cureentBlockYear);
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
			 statement.setString(1, cureentBlockYear);
			 statement.setString(2, cureentBlockYear);
			 if(pageNum == null || recordsPerPage == null) {
	
			 }
			 else {
/*				 statement.setInt(1, (pageRequested-1) * pageSize + 1);	
				 statement.setInt(2, (pageRequested-1) * pageSize + pageSize);*/
				 statement.setInt(3, (pageRequested-1) * pageSize + 1);	
				 statement.setInt(4, (pageRequested-1) * pageSize + pageSize);				 
			 }
		
			rs = statement.executeQuery();
			while(rs.next()) {
				String rgno = rs.getString(1); 
				associationsNotFiledAnnualReturnsList.add(new AssociationsNotFiledAnnualReturnsReport(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
			}
			return associationsNotFiledAnnualReturnsList;
	}
	
	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			cureentBlockYear = parameterMap.get("blockYear-List").get(0);
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			
			//activityFormate = parameterMap.get("userActivity").get(0).toString();
			
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			selectServiceList=parameterMap.get("service-type");
			//reportDisplayType=parameterMap.get("reportDisplyType").get(0);
			//reportStatusDisplayType = parameterMap.get("reportStatusDisplyType").get(0);
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		    loginUserName=parameterMap.get("loginUserName").get(0);
		}
		
			myLoginofficecode=parameterMap.get("myLoginOfficCode").toString();
/*			cureentBlockYear=getCurrentBlockYear();
			blkyrList=getblockyearList(); */
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

	public List<AssociationsNotFiledAnnualReturnsReport> getAssociationsNotFiledAnnualReturnsList() {
		return associationsNotFiledAnnualReturnsList;
	}

	public void setAssociationsNotFiledAnnualReturnsList(
			List<AssociationsNotFiledAnnualReturnsReport> associationsNotFiledAnnualReturnsList) {
		this.associationsNotFiledAnnualReturnsList = associationsNotFiledAnnualReturnsList;
	}



}
