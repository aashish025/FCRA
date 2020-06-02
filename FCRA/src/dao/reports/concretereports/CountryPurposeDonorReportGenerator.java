package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.CountryPurposeDonor;
import models.reports.ReturnFiledReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.reports.MISReportGenerator;

public class CountryPurposeDonorReportGenerator extends MISReportGenerator{
	private List<CountryPurposeDonor> countryPurposeDonorReport;
    private MultiValueMap<String, String> parameterMap;
	private String pageNum;
	private String recordsPerPage;
	private String totalRecords;
	private List<String> blockYearList;
	private List<String> countryList;
	private List<String> donorList;
	private List<String> purposeList;	
	private String reportDisplyType;
	private String loginUserName;
	private String loginOfficeName;
	private int virtualizationMaxSize = 200;	
	public CountryPurposeDonorReportGenerator(Connection connection) {
		super(connection);		
	}	

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)	throws Exception {
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			blockYearList=parameterMap.get("blockYear6");
			countryList=parameterMap.get("country6");
			donorList=parameterMap.get("donor6");
			purposeList=parameterMap.get("purpose6");
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
		}
		
	}
	
	@Override
	protected void generateHTML() throws Exception {
		countryPurposeDonorReport=getCountryPurposeDonorReportRecord();	
		totalRecords=getTotalRecords();
		
	}
	
	public List<CountryPurposeDonor> getCountryPurposeDonorReportRecord() throws Exception {
		 if (connection == null) {
			throw new Exception("Invalid connection");
		 }		 
         String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
         String selectedcountryList = countryList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
         String selectedpurposeList = purposeList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
         String selecteddonorList = donorList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
      // String regDate="01-04-"+blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length()); 
         
         String subQueryBlockYearList="1=1";
         if(!selectedblockyearList.trim().equals("ALL"))
        	 subQueryBlockYearList=" ft.blkyear IN('"+selectedblockyearList+"')";
         
		 String subQueryCountryList1="1=1";		 
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList1=" fp3.ctr_code in('"+selectedcountryList+"')";
	     
	     String subQueryCountryList2="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList2=" fd.donor_country in('"+selectedcountryList+"')";
	     
	     String subQueryPurposeList1="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList1=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryPurposeList2="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList2=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryDonorList1="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList1=" (fp3.dcode||'O') in('"+selecteddonorList+"')";
	     
	     String subQueryDonorList2="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList2=" (fd.donor_code||'N') in('"+selecteddonorList+"')";
	
		StringBuffer countQuery = new StringBuffer(
				" SELECT COUNT(*) FROM (select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, fp3.pcode, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount"+        
                        " FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+     
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, fp3.pcode, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " and fp3.blkyear=fpi.blkyear "+
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+  
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3) "+
                        " union all "+
                        " SELECT DISTINCT ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, fp3.pcode, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  "+        
                        " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D') "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+   
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+                               
                        " and ft.final_submit='Y'"+        
                        " AND ft.rcn = fp3.rcn "+
                        " AND fi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear " +
                        " and fp3.donor_code=fd.donor_code "+
                        " GROUP BY ft.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " SELECT DISTINCT fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, fp3.pcode, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " AND fp3.blkyear=fpi.blkyear "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+     
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+                               
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " AND fpi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3))"); 
		
		     StringBuffer query = new StringBuffer(
				"WITH T2 AS (SELECT t.*,d.dname FROM (select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount"+        
                        " FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+     
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " and fp3.blkyear=fpi.blkyear "+
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+  
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3) "+
                        " union all "+
                        " SELECT DISTINCT ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  "+        
                        " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+   
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y'"+        
                        " AND ft.rcn = fp3.rcn "+
                        " AND fi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " SELECT DISTINCT fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " AND fp3.blkyear=fpi.blkyear "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+     
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " AND fpi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)"
                        + ")t,(SELECT DCODE||'O' as DCODE, DNAME FROM fc_inst_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname"
                        + "), T3 AS (SELECT T2.*, ROWNUM RN FROM T2 WHERE ROWNUM<=?) SELECT * FROM T3 WHERE RN>=?");
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

		}else {
			statement.setInt(1, (pageRequested - 1) * pageSize + pageSize);
			statement.setInt(2, (pageRequested - 1) * pageSize + 1);
		}
		System.out.println("Print Query qq+++ "+query.toString());
		 rs = statement.executeQuery();
		List<CountryPurposeDonor> reportTypeList = new ArrayList<CountryPurposeDonor>();
		while (rs.next()) {			
			reportTypeList.add(new CountryPurposeDonor(rs.getString(1), rs.getString(2), rs.getString(10), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
		}
		return reportTypeList;	
	}
	
	@Override
	protected void generatePDF() throws Exception {
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		//parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	 
	    String reportQuery="";
	    String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    parameters.put("selectedBlockYearList",selectedblockyearList);
	    String selectedcountryList = countryList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    CountryTypeDao sdao= new CountryTypeDao(connection);
		parameters.put("selectedCountryList",sdao.getCountry("'"+selectedcountryList+"'"));
	    String selectedpurposeList = purposeList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        parameters.put("selectedPurposeNameList", getPurposeName(selectedpurposeList));
	    String selecteddonorList = donorList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        parameters.put("selectedDonerList",getDonerName(selecteddonorList));
       // String regDate="01-04-"+blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length()); 
        
        String subQueryBlockYearList="1=1";
        if(!selectedblockyearList.trim().equals("ALL"))
       	 subQueryBlockYearList=" ft.blkyear IN('"+selectedblockyearList+"')";
        
	    String subQueryCountryList1="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList1=" fp3.ctr_code in('"+selectedcountryList+"')";
	     
	     String subQueryCountryList2="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList2=" fd.donor_country in('"+selectedcountryList+"')";
	     
	     String subQueryPurposeList1="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList1=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryPurposeList2="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList2=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryDonorList1="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList1=" (fp3.dcode||'O') in('"+selecteddonorList+"')";
	     
	     String subQueryDonorList2="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList2=" (fd.donor_code||'N') in('"+selecteddonorList+"')";
			
			reportQuery=
					"SELECT t.*,d.dname FROM (select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount"+        
                        " FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+     
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " and fp3.blkyear=fpi.blkyear "+
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+  
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3) "+
                        " union all "+
                        " SELECT DISTINCT ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  "+        
                        " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+   
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y'"+        
                        " AND ft.rcn = fp3.rcn "+
                        " AND fi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " SELECT DISTINCT fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " AND fp3.blkyear=fpi.blkyear "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+     
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " AND fpi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)"
                        + ")t,(SELECT DCODE||'O' as DCODE, DNAME FROM fc_inst_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_CountryPurposeDoner", ds);
		
	
		String tsPath = "/Reports/CountryPurposeDoner.jrxml";
		String fileName = "CountryPurposeDoner.pdf";
		//GenerateCSVVirtualizer.exportReportToCSV(tsPath, parameters, connection, fileName);
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	
		// TODO Auto-generated method stub
		
	
	}	
	
	protected void generateRCNReport() throws Exception {
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		//parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	 
	    String reportQuery="";
	    String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    parameters.put("selectedBlockYearList",selectedblockyearList);
	    String selectedcountryList = countryList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    CountryTypeDao sdao= new CountryTypeDao(connection);
		parameters.put("selectedCountryList",sdao.getCountry("'"+selectedcountryList+"'"));
	    String selectedpurposeList = purposeList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        parameters.put("selectedPurposeNameList", getPurposeName(selectedpurposeList));
	    String selecteddonorList = donorList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        parameters.put("selectedDonerList",getDonerName(selecteddonorList));
       // String regDate="01-04-"+blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length()); 
        
        String subQueryBlockYearList="1=1";
        if(!selectedblockyearList.trim().equals("ALL"))
       	 subQueryBlockYearList=" ft.blkyear IN('"+selectedblockyearList+"')";
        
	    String subQueryCountryList1="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList1=" fp3.ctr_code in('"+selectedcountryList+"')";
	     
	     String subQueryCountryList2="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList2=" fd.donor_country in('"+selectedcountryList+"')";
	     
	     String subQueryPurposeList1="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList1=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryPurposeList2="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList2=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryDonorList1="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList1=" (fp3.dcode||'O') in('"+selecteddonorList+"')";
	     
	     String subQueryDonorList2="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList2=" (fd.donor_code||'N') in('"+selecteddonorList+"')";
			
			reportQuery=
					"SELECT t.*,d.dname FROM (select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount"+        
                        " FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+     
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " and fp3.blkyear=fpi.blkyear "+
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+  
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3) "+
                        " union all "+
                        " SELECT DISTINCT ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  "+        
                        " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+   
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y'"+        
                        " AND ft.rcn = fp3.rcn "+
                        " AND fi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " SELECT DISTINCT fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " AND fp3.blkyear=fpi.blkyear "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+     
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " AND fpi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)"
                        + ")t,(SELECT DCODE||'O' as DCODE, DNAME FROM fc_inst_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_CountryPurposeDoner", ds);
		
	
		String tsPath = "/Reports/CountryPurposeDoner.jrxml";
		String fileName = "CountryPurposeDoner.pdf";
		//GenerateCSVVirtualizer.exportReportToCSV(tsPath, parameters, connection, fileName);
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
		// TODO Auto-generated method stub	
	}

	@Override
	protected void generateExcel() throws  Exception {
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		//parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	 
	    String reportQuery="";
	    String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    parameters.put("selectedBlockYearList",selectedblockyearList);
	    String selectedcountryList = countryList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    CountryTypeDao sdao= new CountryTypeDao(connection);
		parameters.put("selectedCountryList",sdao.getCountry("'"+selectedcountryList+"'"));
	    String selectedpurposeList = purposeList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    parameters.put("selectedPurposeNameList", getPurposeName(selectedpurposeList));
	    String selecteddonorList = donorList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        parameters.put("selectedDonerList",getDonerName(selecteddonorList));
       // String regDate="01-04-"+blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length()); 
        
        String subQueryBlockYearList="1=1";
        if(!selectedblockyearList.trim().equals("ALL"))
       	 subQueryBlockYearList=" ft.blkyear IN('"+selectedblockyearList+"')";
        
	    String subQueryCountryList1="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList1=" fp3.ctr_code in('"+selectedcountryList+"')";
	     
	     String subQueryCountryList2="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList2=" fd.donor_country in('"+selectedcountryList+"')";
	     
	     String subQueryPurposeList1="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList1=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryPurposeList2="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList2=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryDonorList1="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList1=" (fp3.dcode||'O') in('"+selecteddonorList+"')";
	     
	     String subQueryDonorList2="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList2=" (fd.donor_code||'N') in('"+selecteddonorList+"')";
			
			reportQuery=
					"SELECT t.*,d.dname FROM (select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount"+        
                        " FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+     
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " and fp3.blkyear=fpi.blkyear "+
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+  
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3) "+
                        " union all "+
                        " SELECT DISTINCT ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  "+        
                        " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+   
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y'"+        
                        " AND ft.rcn = fp3.rcn "+
                        " AND fi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " SELECT DISTINCT fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " AND fp3.blkyear=fpi.blkyear "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+     
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " AND fpi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)"
                        + ")t,(SELECT DCODE||'O' as DCODE, DNAME FROM fc_inst_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_CountryPurposeDoner", ds);	
		String tsPath = "/Reports/CountryPurposeDoner_Excel.jrxml";
		String fileName = "CountryPurposeDoner.csv";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	}	

	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}
	public List<CountryPurposeDonor> getCountryPurposeDonorReport() {
		return countryPurposeDonorReport;
	}

	public void setCountryPurposeDonorReport(
			List<CountryPurposeDonor> countryPurposeDonorReport) {
		this.countryPurposeDonorReport = countryPurposeDonorReport;
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

	

	public List<String> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<String> countryList) {
		this.countryList = countryList;
	}

	public List<String> getDonorList() {
		return donorList;
	}

	public void setDonorList(List<String> donorList) {
		this.donorList = donorList;
	}

	public List<String> getPurposeList() {
		return purposeList;
	}

	public void setPurposeList(List<String> purposeList) {
		this.purposeList = purposeList;
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
	/**
	 *  For MIS Report
	 * @param selectedDonerId
	 * @return Name Of Comma Seperated  Doner in String 
	 * @throws Exception
	 */
	
	public String getDonerName(String selectedDonerId) throws Exception {
		   if(selectedDonerId.contains("ALL")){
			   return "ALL";
		   }
		   else{
			  // selectedDonerId= selectedDonerId.replaceAll("O", "").replaceAll("N", "");
			   
			   
			   if(connection == null) {
					throw new Exception("Invalid connection");
				}
				StringBuffer donerLisr=new StringBuffer();		
				StringBuffer query = new StringBuffer(" SELECT DNAME FROM (SELECT DNAME AS DNAME FROM fc_inst_donor WHERE (DCODE||'O') IN ('"+selectedDonerId+"')  UNION ALL SELECT DONOR_NAME AS DNAME FROM  fc_fc3_donor WHERE  (DONOR_CODE||'N') IN ('"+selectedDonerId+"'))");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					donerLisr=donerLisr.append(rs.getString(1)+",");
				}
				return donerLisr.toString();
			
			  }
			 }
	public String getPurposeName(String selectedPurposeCode) throws Exception {
		   if(selectedPurposeCode.contains("ALL")){
			   return "ALL";
		   }
		   else{
			   if(connection == null) {
					throw new Exception("Invalid connection");
				}
				StringBuffer purposeName=new StringBuffer();		
				StringBuffer query = new StringBuffer("select PURPOSE_NAME from (SELECT PURPOSE_NAME FROM tm_amount_purpose where PURPOSE_CODE in ( '"+selectedPurposeCode+"') UNION ALL  SELECT pname FROM fc_purpose where pcode in('"+selectedPurposeCode+"')) order by PURPOSE_NAME");
				PreparedStatement statement = connection.prepareStatement(query.toString());
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					purposeName=purposeName.append(rs.getString(1)+",");
				}
				return purposeName.toString();
			
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
		//parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	 
	    String reportQuery="";
	    String selectedblockyearList = blockYearList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    parameters.put("selectedBlockYearList",selectedblockyearList);
	    String selectedcountryList = countryList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    CountryTypeDao sdao= new CountryTypeDao(connection);
		parameters.put("selectedCountryList",sdao.getCountry("'"+selectedcountryList+"'"));
	    String selectedpurposeList = purposeList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
	    parameters.put("selectedPurposeNameList", getPurposeName(selectedpurposeList));
	    String selecteddonorList = donorList.toString().replace("[", "").replace("]", "").replace(", ","','").trim();
        parameters.put("selectedDonerList",getDonerName(selecteddonorList));
       // String regDate="01-04-"+blockYearList.trim().substring(blockYearList.length()-4, blockYearList.length()); 
        
        String subQueryBlockYearList="1=1";
        if(!selectedblockyearList.trim().equals("ALL"))
       	 subQueryBlockYearList=" ft.blkyear IN('"+selectedblockyearList+"')";
        
	    String subQueryCountryList1="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList1=" fp3.ctr_code in('"+selectedcountryList+"')";
	     
	     String subQueryCountryList2="1=1";
	     if(!selectedcountryList.trim().equals("ALL"))
	    	 subQueryCountryList2=" fd.donor_country in('"+selectedcountryList+"')";
	     
	     String subQueryPurposeList1="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList1=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryPurposeList2="1=1";
	     if(!selectedpurposeList.trim().equals("ALL"))
	    	 subQueryPurposeList2=" fp3.pcode in('"+selectedpurposeList+"')";
	     
	     String subQueryDonorList1="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList1=" (fp3.dcode||'O') in('"+selecteddonorList+"')";
	     
	     String subQueryDonorList2="1=1";
	     if(!selecteddonorList.trim().equals("ALL"))
	    	 subQueryDonorList2=" (fd.donor_code||'N') in('"+selecteddonorList+"')";
			
			reportQuery=
					"SELECT t.*,d.dname FROM (select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount"+        
                        " FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+     
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O') donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " and fp3.blkyear=fpi.blkyear "+
                        " AND "+subQueryCountryList1+" "+        
                        " AND "+subQueryDonorList1+" "+  
                        " AND "+subQueryPurposeList1+" "+
                        " AND fp3.dtype='1' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3) "+
                        " union all "+
                        " SELECT DISTINCT ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  "+        
                        " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')  "+        
                        " AND fi.rcn=fp3.rcn "+        
                        " AND "+subQueryBlockYearList+" "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+   
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " AND substr(fp3.rcn,-1,1)='R' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y'"+        
                        " AND ft.rcn = fp3.rcn "+
                        " AND fi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3) "+       
                        " UNION ALL "+        
                        " SELECT DISTINCT fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount "+        
                        " FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd "+        
                        " where "+subQueryBlockYearList+" "+        
                        " and fpi.rcn=fp3.rcn "+        
                        " AND fp3.blkyear=fpi.blkyear "+        
                        " AND "+subQueryCountryList2+" "+        
                        " AND "+subQueryDonorList2+" "+     
                        " AND "+subQueryPurposeList2+" "+
                        " AND fd.donor_type='01' "+        
                        " and substr(fp3.rcn,-1,1)='P' "+        
                        " and ft.blkyear=fp3.blkyear "+        
                        " and ft.final_submit='Y' "+        
                        " AND ft.rcn = fp3.rcn "+        
                        " AND fpi.rcn=fd.rcn "+
                        " AND ft.blkyear=fp3.blkyear "+
                        " and fp3.donor_code=fd.donor_code "+
                        " GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)"
                        + ")t,(SELECT DCODE||'O' as DCODE, DNAME FROM fc_inst_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_CountryPurposeDoner", ds);	
		String tsPath = "/Reports/CountryPurposeDoner_CSV.jrxml";
		String fileName = "CountryPurposeDoner";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);
	
		
	}
	
	
	
}
