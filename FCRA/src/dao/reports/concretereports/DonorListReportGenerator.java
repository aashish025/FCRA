package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.DonorListReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.master.StateDao;
import dao.reports.MISReportGenerator;

public class DonorListReportGenerator extends MISReportGenerator {
	
	private List<DonorListReport> donorListReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private List<String> selectCountryList;
	private List<String> selectBlockYear;
	private String sortColumn;
	private String sortOrder;
	private int virtualizationMaxSize = 200;
	private String loginUserName;
	private String loginOfficeName;
	
	public DonorListReportGenerator(Connection connection) {
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
		
		
		String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").trim();
		
			String CountryListfcctr="1=1";
			String CountryListfd="1=1";
			
			String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			if(!contList.trim().equals("'ALL'")){
				CountryListfcctr= "fc_fc3_part3.ctr_code in ("+contList+")";
				CountryListfd= "fd.donor_country in ("+contList+")";
			}
				parameters.put("selectedBlockYearList",selectBlockyearList);
				CountryTypeDao coa= new CountryTypeDao(connection);
				parameters.put("selectedCountryList",coa.getCountry(contList));
				
			     
	    
	    	 reportQuery ="SELECT * FROM ( "
							 		+ " SELECT DISTINCT dname donor_name, "
							 		+ " trim(add1)||', '||trim(add2)||', '||trim(add3)||', Pin :- '||pin as donor_address "
							 		+ " FROM fc_fc3_part3,fc_inst_donor "
							 		+ " WHERE "+CountryListfcctr+" "
							 		+ " AND fc_fc3_part3.dcode = fc_inst_donor.dcode "
							 		+ " AND dtype = '1' AND fc_fc3_part3.blkyear = '"+selectBlockyearList+"' "
							 		+ " UNION ALL  "
							 		+ " SELECT fd.donor_name, "
							 		+ " trim(donor_address) donor_address "
							 		+ " FROM fc_fc3_donor fd, fc_fc3_donor_wise fdw "
							 		+ " WHERE fdw.blkyear = '"+selectBlockyearList+"' "
							 		+ " AND fd.rcn=fdw.rcn "
							 		+ " AND fd.donor_code=fdw.donor_code "
							 		+ " AND fd.donor_type='01'  "
							 		+ " and "+CountryListfd+")"
							 		+ " ORDER BY donor_name ASC"; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Donor_List_Report", ds);
	     
	     
		String tsPath = "/Reports/DonorListReportExcel.jrxml";
		String fileName = "DonorListReport";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	    
		
	}
	@Override
	protected void generateHTML() throws Exception {
		
		donorListReport=getDetailsDonorListHtml();	
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
			selectCountryList=parameterMap.get("country-List");
		
		}
}
	
	public List<DonorListReport> getDetailsDonorListHtml() throws Exception {
 		if(connection == null) {
			throw new Exception("Invalid connection");
		}
			
								
							String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").trim();
							
								String CountryListfcctr="1=1";
								String CountryListfd="1=1";
								
								String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
								if(!contList.trim().equals("'ALL'")){
									CountryListfcctr= "fc_fc3_part3.ctr_code in ("+contList+")";
									CountryListfd= "fd.donor_country in ("+contList+")";
								}
								
								StringBuffer query;
							
								 query = new StringBuffer("SELECT * FROM ( "
								 		+ " SELECT DISTINCT dname donor_name, "
								 		+ " trim(add1)||', '||trim(add2)||', '||trim(add3)||', Pin :- '||pin as donor_address "
								 		+ " FROM fc_fc3_part3,fc_inst_donor "
								 		+ " WHERE "+CountryListfcctr+" "
								 		+ " AND fc_fc3_part3.dcode = fc_inst_donor.dcode "
								 		+ " AND dtype = '1' AND fc_fc3_part3.blkyear = '"+selectBlockyearList+"' "
								 		+ " UNION ALL  "
								 		+ " SELECT fd.donor_name, "
								 		+ " trim(donor_address) donor_address "
								 		+ " FROM fc_fc3_donor fd, fc_fc3_donor_wise fdw "
								 		+ " WHERE fdw.blkyear = '"+selectBlockyearList+"' "
								 		+ " AND fd.rcn=fdw.rcn "
								 		+ " AND fd.donor_code=fdw.donor_code "
								 		+ " AND fd.donor_type='01'  "
								 		+ " and "+CountryListfd+")"
								 		+ " ORDER BY donor_name ASC");
								 
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
								List<DonorListReport> religionWiseReportReportList = new ArrayList<DonorListReport>();
								while(rs.next()) {
									religionWiseReportReportList.add(new DonorListReport(rs.getString(1), rs.getString(2),rs.getString(3))); 
									
								}
							return religionWiseReportReportList;
								
							}
	 private String preparePagingQuery(StringBuffer query) throws Exception {
			StringBuffer orderbyClause = new StringBuffer("");
			StringBuffer order = new StringBuffer("");
			
			if(sortColumn != null && sortColumn.equals("") == false) {
				if(sortColumn.equals("donorName")) {
					orderbyClause.append(" ORDER BY DONOR_NAME");
				}else if(sortColumn.equals("address")) {
					orderbyClause.append(" ORDER BY DONOR_ADDRESS");
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
			
			
			String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").trim();
			
				String CountryListfcctr="1=1";
				String CountryListfd="1=1";
				
				String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
				if(!contList.trim().equals("'ALL'")){
					CountryListfcctr= "fc_fc3_part3.ctr_code in ("+contList+")";
					CountryListfd= "fd.donor_country in ("+contList+")";
				}
					parameters.put("selectedBlockYearList",selectBlockyearList);
					CountryTypeDao coa= new CountryTypeDao(connection);
					parameters.put("selectedCountryList",coa.getCountry(contList));
					
				     
		    
		    	 reportQuery ="SELECT * FROM ( "
								 		+ " SELECT DISTINCT dname donor_name, "
								 		+ " trim(add1)||', '||trim(add2)||', '||trim(add3)||', Pin :- '||pin as donor_address "
								 		+ " FROM fc_fc3_part3,fc_inst_donor "
								 		+ " WHERE "+CountryListfcctr+" "
								 		+ " AND fc_fc3_part3.dcode = fc_inst_donor.dcode "
								 		+ " AND dtype = '1' AND fc_fc3_part3.blkyear = '"+selectBlockyearList+"' "
								 		+ " UNION ALL  "
								 		+ " SELECT fd.donor_name, "
								 		+ " trim(donor_address) donor_address "
								 		+ " FROM fc_fc3_donor fd, fc_fc3_donor_wise fdw "
								 		+ " WHERE fdw.blkyear = '"+selectBlockyearList+"' "
								 		+ " AND fd.rcn=fdw.rcn "
								 		+ " AND fd.donor_code=fdw.donor_code "
								 		+ " AND fd.donor_type='01'  "
								 		+ " and "+CountryListfd+")"
								 		+ " ORDER BY donor_name ASC"; 
		 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
				parameters.put("Donor_List_Report", ds);
		     
		     
			String tsPath = "/Reports/DonorListReport.jrxml";
			String fileName ="DonorListReport.pdf";
			GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);

}
		
	 
	public List<DonorListReport> getDonorListReport() {
		return donorListReport;
	}
	public void setDonorListReport(List<DonorListReport> donorListReport) {
		this.donorListReport = donorListReport;
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

	@Override
	protected void generateCSV() throws Exception {

		// TODO Auto-generated method stub
		
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);

	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	   
		String reportQuery="";
		
		
		String selectBlockyearList = selectBlockYear.toString().replace("[", "").replace("]", "").trim();
		
			String CountryListfcctr="1=1";
			String CountryListfd="1=1";
			
			String contList=selectCountryList.toString().replace("[", "'").replace("]", "'").replace(", ", "','").trim();
			if(!contList.trim().equals("'ALL'")){
				CountryListfcctr= "fc_fc3_part3.ctr_code in ("+contList+")";
				CountryListfd= "fd.donor_country in ("+contList+")";
			}
				parameters.put("selectedBlockYearList",selectBlockyearList);
				CountryTypeDao coa= new CountryTypeDao(connection);
				parameters.put("selectedCountryList",coa.getCountry(contList));
				
			     
	    
	    	 reportQuery ="SELECT * FROM ( "
							 		+ " SELECT DISTINCT dname donor_name, "
							 		+ " trim(add1)||', '||trim(add2)||', '||trim(add3)||', Pin :- '||pin as donor_address "
							 		+ " FROM fc_fc3_part3,fc_inst_donor "
							 		+ " WHERE "+CountryListfcctr+" "
							 		+ " AND fc_fc3_part3.dcode = fc_inst_donor.dcode "
							 		+ " AND dtype = '1' AND fc_fc3_part3.blkyear = '"+selectBlockyearList+"' "
							 		+ " UNION ALL  "
							 		+ " SELECT fd.donor_name, "
							 		+ " trim(donor_address) donor_address "
							 		+ " FROM fc_fc3_donor fd, fc_fc3_donor_wise fdw "
							 		+ " WHERE fdw.blkyear = '"+selectBlockyearList+"' "
							 		+ " AND fd.rcn=fdw.rcn "
							 		+ " AND fd.donor_code=fdw.donor_code "
							 		+ " AND fd.donor_type='01'  "
							 		+ " and "+CountryListfd+")"
							 		+ " ORDER BY donor_name ASC"; 
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("Donor_List_Report", ds);
	     
	     
		String tsPath = "/Reports/DonorListReportCSV.jrxml";
		String fileName = "DonorListReport";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	    
		
	
		
	}
	 
	 
	 
	


}
