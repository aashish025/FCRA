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
import models.reports.UserActivityReport;

import org.apache.poi.ss.formula.udf.UDFFinder;
import org.springframework.util.MultiValueMap;

import com.sun.org.apache.bcel.internal.generic.NEW;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class SuddenRiseInIncomeReportGenrator  extends MISReportGenerator{
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
	private String myLoginofficecode;
	private int fromLastyears;
	private String cureentBlockYear;
	private String blkyrList;
	private int ratioTamt_avgAmt;
	private List<String> selectStateList;
	private List<SuddenRiseIncomeReport> suddenRiseInIncomeList;
	public SuddenRiseInIncomeReportGenrator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		Map parameters = new HashMap();
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("BLOCK_YEAR_LIST", blkyrList.replace("'", ""));
		parameters.put("ratio", String.valueOf(ratioTamt_avgAmt));
		parameters.put("calYear", String.valueOf(fromLastyears));
		String subStringStateList="1=1";
		String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		if(!stList.trim().equals("'ALL'")){
			subStringStateList= "t.scode in ("+stList+")";
		}
		StateDao sdao= new StateDao(connection);
		parameters.put("selectedStateList",sdao.getState(stList));
		String reportQuery = "with t as (select a.rcn, a.blkyear, (b.for_amt+b.bk_int+b.oth_int) for_amt"
				+ ", fi.asso_name  as asso_name,(select sname from tm_state "
				+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district"
				+ ",substr(fi.stdist,1,2) as scode from fc_fc3_tally a, fc_fc3_part1 b,fc_india fi"
				+ "  where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn  "
				+ " union "
				+ " select a.rcn, a.blkyear, (b.bk_int+b.source_for_amt+b.source_local_amt) source_for_amt, fi.asso_name  as asso_name,"
				+ "(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,"
				+ "(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode"
				+ "  from fc_fc3_tally a, fc_fc3_part1_new b,fc_india fi where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn ), "
				+ " t2 as ( "
				+ " select rcn, avg(for_amt) average from t where blkyear in ("+blkyrList+") and blkyear!='"+cureentBlockYear+"' group by rcn "
				+ " ) "
				+ " select t.rcn, ROUND(t.for_amt,2) as for_amt,ROUND(average,2) as average ,t.asso_name,t.state,t.district from t, t2 where t.rcn=t2.rcn and t.for_amt/(replace(average,0,1)) > "+ratioTamt_avgAmt+" and blkyear='"+cureentBlockYear+"' and "+subStringStateList+" order by for_amt desc ";

		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
		parameters.put("SUDDEN_RISE_IN_INCOME", ds);

		String tsPath = "/Reports/SuddenRiseInIncome.jrxml";
		String fileName = "SudeenRiseInIncome";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
				fileName);// TODO Auto-generated method stub

	}

	@Override
	protected void generateExcel() throws Exception {

		Map parameters = new HashMap();
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("BLOCK_YEAR_LIST", blkyrList.replace("'", ""));
		parameters.put("ratio", String.valueOf(ratioTamt_avgAmt));
		parameters.put("calYear", String.valueOf(fromLastyears));
		String subStringStateList="1=1";
		String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		if(!stList.trim().equals("'ALL'")){
			subStringStateList= "t.scode in ("+stList+")";
		}
		StateDao sdao= new StateDao(connection);
		parameters.put("selectedStateList",sdao.getState(stList));
		
		
		String reportQuery = "with t as (select a.rcn, a.blkyear, (b.for_amt+b.bk_int+b.oth_int) for_amt"
				+ ", fi.asso_name  as asso_name,(select sname from tm_state "
				+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district"
				+ ",substr(fi.stdist,1,2) as scode from fc_fc3_tally a, fc_fc3_part1 b,fc_india fi"
				+ "  where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn  "
				+ " union "
				+ " select a.rcn, a.blkyear, (b.bk_int+b.source_for_amt+b.source_local_amt) source_for_amt, fi.asso_name  as asso_name,"
				+ "(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,"
				+ "(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode"
				+ "  from fc_fc3_tally a, fc_fc3_part1_new b,fc_india fi where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn ), "
				+ " t2 as ( "
				+ " select rcn, avg(for_amt) average from t where blkyear in ("+blkyrList+") and blkyear!='"+cureentBlockYear+"' group by rcn "
				+ " ) "
				+ " select t.rcn, ROUND(t.for_amt,2) as for_amt,ROUND(average,2) as average ,t.asso_name,t.state,t.district from t, t2 where t.rcn=t2.rcn and t.for_amt/(replace(average,0,1)) > "+ratioTamt_avgAmt+" and blkyear='"+cureentBlockYear+"' and "+subStringStateList+" order by for_amt desc ";

		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
		parameters.put("SUDDEN_RISE_IN_INCOME", ds);

		String tsPath = "/Reports/SuddenRiseInIncome.jrxml";
		String fileName = "SudeenRiseInIncome";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection,
				fileName);// TODO Auto-generated method stub

	
		
	}

	@Override
	protected void generateHTML() throws Exception {
		suddenRiseInIncomeList=getSuddenRiseIncomeList();
		totalRecords=getTotalRecords();
	}

	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateCSV() throws Exception {


		Map parameters = new HashMap();
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
		parameters.put("BLOCK_YEAR_LIST", blkyrList.replace("'", ""));
		
		String subStringStateList="1=1";
		String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		if(!stList.trim().equals("'ALL'")){
			subStringStateList= "t.scode in ("+stList+")";
		}
		//StateDao sdao= new StateDao(connection);
		//parameters.put("selectedStateList",sdao.getState(stList));
		
		
		String reportQuery = "with t as (select a.rcn, a.blkyear, (b.for_amt+b.bk_int+b.oth_int) for_amt"
				+ ", fi.asso_name  as asso_name,(select sname from tm_state "
				+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district"
				+ ",substr(fi.stdist,1,2) as scode from fc_fc3_tally a, fc_fc3_part1 b,fc_india fi"
				+ "  where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn  "
				+ " union "
				+ " select a.rcn, a.blkyear, (b.bk_int+b.source_for_amt+b.source_local_amt) source_for_amt, fi.asso_name  as asso_name,"
				+ "(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,"
				+ "(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode"
				+ "  from fc_fc3_tally a, fc_fc3_part1_new b,fc_india fi where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn ), "
				+ " t2 as ( "
				+ " select rcn, avg(for_amt) average from t where blkyear in ("+blkyrList+") and blkyear!='"+cureentBlockYear+"' group by rcn "
				+ " ) "
				+ " select t.rcn, ROUND(t.for_amt,2) as for_amt,ROUND(average,2) as average ,t.asso_name,t.state,t.district from t, t2 where t.rcn=t2.rcn and t.for_amt/(replace(average,0,1)) > "+ratioTamt_avgAmt+" and blkyear='"+cureentBlockYear+"' and "+subStringStateList+" order by for_amt desc ";

		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize);
		parameters.put("SUDDEN_RISE_IN_INCOME", ds);

		String tsPath = "/Reports/SuddenRiseInIncomeCSV.jrxml";
		String fileName = "SudeenRiseInIncome";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection,
				fileName);// TODO Auto-generated method stub

	
		
	
		
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			fromLastyears=Integer.parseInt(parameterMap.get("report-required-for-year").get(0));
			ratioTamt_avgAmt=Integer.parseInt(parameterMap.get("ratio-tamt-avgamt").get(0));
			selectStateList=parameterMap.get("state-List");
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		    loginUserName=parameterMap.get("loginUserName").get(0);

			}
		
			myLoginofficecode=parameterMap.get("myLoginOfficCode").toString();
			cureentBlockYear=getCurrentBlockYear();
			blkyrList=getblockyearList();
		}

	private String getCurrentBlockYear() throws SQLException{
		String query="SELECT BLkYR FROM TM_BLOCK_YEAR WHERE STATUS='O' ";
		PreparedStatement ps=connection.prepareStatement(query);
		 ResultSet rs = ps.executeQuery();
		 if(rs.next()){
			 return rs.getString(1); 
		 }
		 else return null;
	}
	
	private String getblockyearList(){
	  StringBuffer aa=new StringBuffer();
		String startFrom=cureentBlockYear.trim().substring(0, 4); //2016
		for (int i = 1; i <= fromLastyears; i++) {
		String.valueOf(Integer.parseInt(startFrom)-1) ;
			aa.append("'"+String.valueOf(Integer.parseInt(startFrom)-i)+"-"+String.valueOf((Integer.parseInt(startFrom)-i)+1)+"',");	
		}
		String a=aa.toString();
		a=a.substring(0, a.length()-1);
		return a;
	}
	
	private List<SuddenRiseIncomeReport> getSuddenRiseIncomeList() throws Exception{
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}

 		String subStringStateList="1=1";
		String stList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
		if(!stList.trim().equals("'ALL'")){
			subStringStateList= "t.scode in ("+stList+")";
		}
 		
 		
							String countQuery ="SELECT COUNT(1) FROM ( with t as (select a.rcn, a.blkyear, (b.for_amt+b.bk_int+b.oth_int) for_amt"
									+ ", fi.asso_name  as asso_name,(select sname from tm_state "
									+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode  "
									+ " from fc_fc3_tally a, fc_fc3_part1 b ,fc_india fi"
									+ "  where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn "
									+ " union "
									+ " select a.rcn, a.blkyear, (b.bk_int+b.source_for_amt+b.source_local_amt) source_for_amt "
									+ ", fi.asso_name  as asso_name,(select sname from tm_state "
									+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode  "
									+ " from fc_fc3_tally a, fc_fc3_part1_new b,fc_india fi where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn), "
									+ " t2 as ( "
									+ " select rcn, avg(for_amt) average from t where blkyear in ("+blkyrList+") and blkyear!='"+cureentBlockYear+"' group by rcn "
									+ " ) "
									+ " select t.rcn, t.for_amt,average ,t.asso_name,t.state,t.district from t, t2 where t.rcn=t2.rcn and t.for_amt/(replace(average,0,1)) > "+ratioTamt_avgAmt+" and blkyear='"+cureentBlockYear+"' and "+subStringStateList+" order by for_amt desc ) " ;
						
							String query ="with t as (select a.rcn, a.blkyear, (b.for_amt+b.bk_int+b.oth_int) for_amt"
									+ ", fi.asso_name  as asso_name,(select sname from tm_state "
									+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode  "
									+ " from fc_fc3_tally a, fc_fc3_part1 b ,fc_india fi "
									+ "  where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn "
									+ " union "
									+ " select a.rcn, a.blkyear, (b.bk_int+b.source_for_amt+b.source_local_amt) source_for_amt "
									+ ", fi.asso_name  as asso_name,(select sname from tm_state "
									+ "where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district,substr(fi.stdist,1,2) as scode  "
									+ " from fc_fc3_tally a, fc_fc3_part1_new b,fc_india fi  where a.unique_fileno=b.unique_fileno and a.blkyear=b.blkyear and a.rcn=b.rcn and a.rcn=fi.rcn), "
									+ " t2 as ( "
									+ " select rcn, avg(for_amt) average from t where blkyear in ("+blkyrList+") and blkyear!='"+cureentBlockYear+"' group by rcn "
									+ " ) ,"
									+ " t3 as (select t.rcn,ROUND(t.for_amt,2) as for_amt,ROUND(average,2) as average ,t.asso_name,t.state,t.district from t, t2 where t.rcn=t2.rcn and t.for_amt/(replace(average,0,1)) > "+ratioTamt_avgAmt+" and blkyear='"+cureentBlockYear+"' and "+subStringStateList+"  order by for_amt desc) , t4 as"
									+ " (SELECT t3.*, ROWNUM RN FROM t3 WHERE ROWNUM<=?) SELECT * FROM T4 WHERE RN>=? ";
							
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
								 statement.setInt(1, (pageRequested-1) * pageSize + pageSize);
								 statement.setInt(2, (pageRequested-1) * pageSize + 1);
								 }
							
								 rs = statement.executeQuery();
								List<SuddenRiseIncomeReport> suddenRiseInIncomeList = new ArrayList<SuddenRiseIncomeReport>();
								while(rs.next()) {
									suddenRiseInIncomeList.add(new SuddenRiseIncomeReport(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
									
								}
							return suddenRiseInIncomeList;
								
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



}
