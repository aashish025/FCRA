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
import models.services.requests.ProjectRequest;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.master.CountryTypeDao;
import dao.reports.MISReportGenerator;

public class RegistrationTrackingReportGenerator extends MISReportGenerator{
	//private List<Regi> countryPurposeDonorReport;
    private MultiValueMap<String, String> parameterMap;
	private String totalRecords;	
	private String reportDisplyType;
	private String loginUserName;
	private String loginOfficeName;
	private String rcnNumber;
	private int virtualizationMaxSize = 200;	
	public RegistrationTrackingReportGenerator(Connection connection) {
		super(connection);		
	}	
	//{rcnNumber, reportType, reportFormat, loginOfficeName, loginUserName=[Nishi]}
	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)	throws Exception {
		if(parameterMap != null) {
			rcnNumber=parameterMap.get("rcnNumber").get(0);
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);			
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
			loginUserName=parameterMap.get("loginUserName").get(0);
			
		}
		
	}
	
	@Override
	protected void generateHTML() throws Exception {
		
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
	    
	   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    
	    PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
	    if(rcnNumber.substring(rcnNumber.length()-1,rcnNumber.length()).equals("R")){
	    	parameters.put("assoType","REGISTERED");
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
					+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'), case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired "
					+ "FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");
		}else if(rcnNumber.substring(rcnNumber.length()-1,rcnNumber.length()).equals("P")){
			parameters.put("assoType","PRIOR PERMISSION");
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,null,null,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,null,null,null,null,null,null,null,null,to_char(A.PP_DATE,'dd-mm-yyyy'),null FROM FC_PP_INDIA A  WHERE A.RCN=?");
		}					
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, rcnNumber);		
		rs=statement.executeQuery();	
		String statusDesc=null;
		String assoNewOldFlag=null;
		String bankNewOldFlag=null;
		String assoAddress=null;
		String bankAdress=null;
		String assoNature=null;
		String assoNatureDesc="";
		if(rs.next()) {
			/* *
			if(rs.getString(4)==null || rs.getString(4).equals("") || rs.getString(4).equals("0")){
				statusDesc="ALIVE";
			}
			else if(rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			}
			* */
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} else if(rs.getString(21) != null && rs.getString(21).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			assoNewOldFlag=rs.getString(10);
			if(assoNewOldFlag.equals("N")){
				assoAddress=rs.getString(5)+", "+rs.getString(6);
			}else if(assoNewOldFlag.equals("O")){
				assoAddress=rs.getString(7)+", "+rs.getString(8)+", "+rs.getString(9);
			}
			bankNewOldFlag=rs.getString(12);			
			if(bankNewOldFlag != null) {
				if(bankNewOldFlag.equals("N")){
					bankAdress=rs.getString(14)+", "+rs.getString(15);
				}else if(bankNewOldFlag.equals("O")){
					bankAdress=rs.getString(16)+", "+rs.getString(17)+", "+rs.getString(18);
				}
			}
			assoNature=rs.getString(11);	
			if(assoNature != null) {	 			
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<assoNature.length();j++){
		 			String natureCode = assoNature.substring(j, j+1);
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();	 		 
			}
			parameters.put("rcnNumber",checkNull(rs.getString(1)));
			parameters.put("regDate",checkNull(rs.getString(20)));
			parameters.put("statusDesc",checkNull(statusDesc));
			parameters.put("assoName",checkNull(rs.getString(2)));
			parameters.put("assoAddress",checkNull(assoAddress));
			parameters.put("bankName",checkNull(rs.getString(13)));
			parameters.put("bankAddress",checkNull(bankAdress));
			parameters.put("accountNumber",checkNull(rs.getString(19)));
			parameters.put("assoNature",checkNull(assoNatureDesc));
			
		}	
		   
		   
	    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    
	    
	 
		String reportQuery = "SELECT t.*,d.dname FROM (select distinct ft.blkyear, "
				+ "   (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, "
				+ "   (fp3.dcode||'O'||fp3.dtype) donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,"
				+ "   (select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, "
				+ "   nvl(sum(fp3.amount),0) as amount FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "
				+ "   where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')   AND fi.rcn=fp3.rcn "
				+ "   AND 1=1 AND substr(fp3.rcn,-1,1)='R'  "
				+ "   and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ 	  rcnNumber
				+ "' "
				+ "   group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'||fp3.dtype), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3)  "
				+ "   UNION ALL  "
				+ "   select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O'||fp3.dtype) donor_code, "
				+ "   (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount  FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft  "
				+ "   where fpi.rcn=fp3.rcn  and fp3.blkyear=fpi.blkyear AND 1=1"
				+ "   and substr(fp3.rcn,-1,1)='P'"
				+ "   and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+     rcnNumber
				+ "'  group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'||fp3.dtype), "
				+ "   fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)  "
				+ "   union all  "
				+ "   SELECT distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, "
				+ "   (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount   "
				+ "   FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd  where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND "
				+ "   (fi.status <> 'D')   AND fi.rcn=fp3.rcn    AND 1=1   "
				+ "   AND substr(fp3.rcn,-1,1)='R'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y' AND ft.rcn = fp3.rcn AND ft.rcn='"
				+     rcnNumber
				+ "'  "
				+ "   AND fi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear and fp3.donor_code=fd.donor_code  group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), "
				+ "   fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3)  "
				+ "   UNION ALL  "
				+ "   SELECT distinct fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, "
				+ "   (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, "
				+ "   fc_fc3_donor fd  where  fpi.rcn=fp3.rcn  AND fp3.blkyear=fpi.blkyear  AND 1=1  "
				+ "   and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+     rcnNumber
				+ "'  AND fpi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear  and fp3.donor_code=fd.donor_code "
				+ "   GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3))t, "
				+ "   (SELECT DCODE||'O1' as DCODE, DNAME FROM fc_inst_donor UNION ALL  SELECT DCODE||'O2' as DCODE, DNAME FROM fc_indv_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d  "
				+ "   WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";

     		
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_RegistrationTracking", ds);
		
			
			String reportQuery2="with t1 as ( "
					+ "select distinct ft.blkyear,  "
					+ "nvl(sum(fp3.amount),0) as amount FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft  "
					+ " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')   AND fi.rcn=fp3.rcn   "
					+ " AND 1=1 AND substr(fp3.rcn,-1,1)='R'   "
					+ "and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'  group by ft.blkyear "
					+ " UNION ALL   "
					+ " select distinct ft.blkyear,  "
					+ " nvl(sum(fp3.amount),0) as amount  FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft   "
					+ " where fpi.rcn=fp3.rcn  and fp3.blkyear=fpi.blkyear AND 1=1  "
					+ " and substr(fp3.rcn,-1,1)='P' "
					+ "  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'   "
					+ "  group by ft.blkyear "
					+ "  union all   "
					+ " SELECT distinct ft.blkyear,  "
					+ "  nvl(sum(fp3.amount),0) AS amount    "
					+ " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd  where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND  "
					+ " (fi.status <> 'D')   AND fi.rcn=fp3.rcn    AND 1=1    "
					+ " AND substr(fp3.rcn,-1,1)='R'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y' AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'   "
					+ " AND fi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear and fp3.donor_code=fd.donor_code   "
					+ " group by ft.blkyear   "
					+ " UNION ALL   SELECT distinct fp3.blkyear, "
					+ " nvl(sum(fp3.amount),0) AS amount  FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft,  "
					+ " fc_fc3_donor fd  where  fpi.rcn=fp3.rcn  AND fp3.blkyear=fpi.blkyear  AND 1=1   "
					+ " and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'  AND fpi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear  and fp3.donor_code=fd.donor_code  "
					+ " GROUP BY fp3.blkyear ), t2 as ( select fp.blkyear,for_amt+bk_int+oth_int as totamount,for_amt,(bk_int+oth_int) interest,0 as local_amt,"
					+ "  TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') as SUBMISSION_DT,ft.unique_fileno     "
					+ " from fc_fc3_part1 fp,fc_fc3_tally ft    "
					+ "  where fp.rcn='"+rcnNumber+"' and    "
					+ " ft.rcn=fp.rcn and    "
					+ " fp.blkyear=ft.blkyear and    "
					+ " fp.blkyear not in ('2005-2006') and     "
					+ " ft.final_submit='Y'                  "
					+ " union  "
					+ " select fp.blkyear,BK_INT+SOURCE_FOR_AMT+SOURCE_LOCAL_AMT as amount,SOURCE_FOR_AMT,BK_INT,SOURCE_LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') as SUBMISSION_DT,ft.unique_fileno      "
					+ " from fc_fc3_part1_new fp,fc_fc3_tally ft   "
					+ "  where fp.rcn='"+rcnNumber+"' and    "
					+ "  ft.rcn=fp.rcn and    "
					+ " fp.blkyear=ft.blkyear and    "
					+ " fp.blkyear not in ('2005-2006') and    "
					+ " ft.final_submit='Y' ) "
					+ " select t2.*,nvl(t1.amount,0) donorwiseamt, (t2.totamount-nvl(t1.amount,0)) diff from t2 left join t1 on t2.blkyear=t1.blkyear order by t2.blkyear desc";
			
			    ReportDataSource ds1=new ReportDataSource(parameters, reportQuery2, connection, virtualizationMaxSize);
			    parameters.put("PRINTRECORD_DATA_SOURCE_RegistrationTracking_2", ds1);
	
		String tsPath = "/Reports/RegistraionTrackingDetail.jrxml";
		String fileName = "RegistraionTrackingDetail";
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
	    
	   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    
	    PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
	    if(rcnNumber.substring(rcnNumber.length()-1,rcnNumber.length()).equals("R")){
	    	parameters.put("assoType","REGISTERED");
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
					+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'), case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired "
					+ "FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");
		}else if(rcnNumber.substring(rcnNumber.length()-1,rcnNumber.length()).equals("P")){
			parameters.put("assoType","PRIOR PERMISSION");
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,null,null,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,null,null,null,null,null,null,null,null,to_char(A.PP_DATE,'dd-mm-yyyy'),null FROM FC_PP_INDIA A  WHERE A.RCN=?");
		}			
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, rcnNumber);		
		rs=statement.executeQuery();	
		String statusDesc=null;
		String assoNewOldFlag=null;
		String bankNewOldFlag=null;
		String assoAddress=null;
		String bankAdress=null;
		String assoNature=null;
		String assoNatureDesc="";
		if(rs.next()) {
			/* *
			if(rs.getString(4)==null || rs.getString(4).equals("") || rs.getString(4).equals("0")){
				statusDesc="ALIVE";
			}
			else if(rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			}
			* */
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} else if(rs.getString(21) != null && rs.getString(21).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			assoNewOldFlag=rs.getString(10);
			if(assoNewOldFlag.equals("N")){
				assoAddress=rs.getString(5)+", "+rs.getString(6);
			}else if(assoNewOldFlag.equals("O")){
				assoAddress=rs.getString(7)+", "+rs.getString(8)+", "+rs.getString(9);
			}
			bankNewOldFlag=rs.getString(12);			
			if(bankNewOldFlag != null) {
				if(bankNewOldFlag.equals("N")){
					bankAdress=rs.getString(14)+", "+rs.getString(15);
				}else if(bankNewOldFlag.equals("O")){
					bankAdress=rs.getString(16)+", "+rs.getString(17)+", "+rs.getString(18);
				}
			}
			assoNature=rs.getString(11);	
			if(assoNature != null) {	 			
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<assoNature.length();j++){
		 			String natureCode = assoNature.substring(j, j+1);
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();	 		 
			}
			parameters.put("rcnNumber",checkNull(rs.getString(1)));
			parameters.put("regDate",checkNull(rs.getString(20)));
			parameters.put("statusDesc",checkNull(statusDesc));
			parameters.put("assoName",checkNull(rs.getString(2)));
			parameters.put("assoAddress",checkNull(assoAddress));
			parameters.put("bankName",checkNull(rs.getString(13)));
			parameters.put("bankAddress",checkNull(bankAdress));
			parameters.put("accountNumber",checkNull(rs.getString(19)));
			parameters.put("assoNature",checkNull(assoNatureDesc));
			
		}	
		   
		   
	    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    
	    
	 
		String reportQuery = "SELECT t.*,d.dname FROM (select distinct ft.blkyear, "
				+ "  (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, "
				+ "  (fp3.dcode||'O'||fp3.dtype) donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,"
				+ "  (select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, "
				+ "   nvl(sum(fp3.amount),0) as amount FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "
				+ "   where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')   AND fi.rcn=fp3.rcn "
				+ "  AND 1=1  AND substr(fp3.rcn,-1,1)='R'  "
				+ "   and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "' "
				+ "   group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'||fp3.dtype), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3)  "
				+ "   UNION ALL  "
				+ "   select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O'||fp3.dtype) donor_code, "
				+ "   (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount  FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft  "
				+ "  where   fpi.rcn=fp3.rcn  and fp3.blkyear=fpi.blkyear   AND 1=1 "
				+ "  and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "'  group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'||fp3.dtype), "
				+ "   fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)  "
				+ "   union all  "
				+ "   SELECT distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, "
				+ "   (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state, "
				+ "  (select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount   "
				+ "  FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd  where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND "
				+ "  (fi.status <> 'D')   AND fi.rcn=fp3.rcn    AND 1=1   "
				+ "   AND substr(fp3.rcn,-1,1)='R'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y' AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "'  "
				+ "  AND fi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear  and fp3.donor_code=fd.donor_code group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), "
				+ "   fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3)  "
				+ "  UNION ALL  "
				+ "  SELECT distinct fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, "
				+ "  (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, "
				+ "  fc_fc3_donor fd  where  fpi.rcn=fp3.rcn  AND fp3.blkyear=fpi.blkyear  AND 1=1    "
				+ "  and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "'  AND fpi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear   and fp3.donor_code=fd.donor_code "
				+ "   GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3))t, "
				+ "   (SELECT DCODE||'O1' as DCODE, DNAME FROM fc_inst_donor UNION ALL  SELECT DCODE||'O2' as DCODE, DNAME FROM fc_indv_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d  "
				+ "   WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";

     		
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_RegistrationTracking", ds);
		
			String reportQuery2="with t1 as ( "
					+ "select distinct ft.blkyear,  "
					+ "nvl(sum(fp3.amount),0) as amount FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft  "
					+ " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')   AND fi.rcn=fp3.rcn   "
					+ " AND 1=1 AND substr(fp3.rcn,-1,1)='R'   "
					+ "and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'  group by ft.blkyear "
					+ " UNION ALL   "
					+ " select distinct ft.blkyear,  "
					+ " nvl(sum(fp3.amount),0) as amount  FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft   "
					+ " where fpi.rcn=fp3.rcn  and fp3.blkyear=fpi.blkyear AND 1=1  "
					+ " and substr(fp3.rcn,-1,1)='P' "
					+ "  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'   "
					+ "  group by ft.blkyear "
					+ "  union all   "
					+ " SELECT distinct ft.blkyear,  "
					+ "  nvl(sum(fp3.amount),0) AS amount    "
					+ " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd  where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND  "
					+ " (fi.status <> 'D')   AND fi.rcn=fp3.rcn    AND 1=1    "
					+ " AND substr(fp3.rcn,-1,1)='R'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y' AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'   "
					+ " AND fi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear and fp3.donor_code=fd.donor_code   "
					+ " group by ft.blkyear   "
					+ " UNION ALL   SELECT distinct fp3.blkyear, "
					+ " nvl(sum(fp3.amount),0) AS amount  FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft,  "
					+ " fc_fc3_donor fd  where  fpi.rcn=fp3.rcn  AND fp3.blkyear=fpi.blkyear  AND 1=1   "
					+ " and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'  AND fpi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear  and fp3.donor_code=fd.donor_code  "
					+ " GROUP BY fp3.blkyear ), t2 as ( select fp.blkyear,for_amt+bk_int+oth_int as totamount,for_amt,(bk_int+oth_int) interest,0 as local_amt,"
					+ "  TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') as SUBMISSION_DT,ft.unique_fileno     "
					+ " from fc_fc3_part1 fp,fc_fc3_tally ft    "
					+ "  where fp.rcn='"+rcnNumber+"' and    "
					+ " ft.rcn=fp.rcn and    "
					+ " fp.blkyear=ft.blkyear and    "
					+ " fp.blkyear not in ('2005-2006') and     "
					+ " ft.final_submit='Y'                  "
					+ " union  "
					+ " select fp.blkyear,BK_INT+SOURCE_FOR_AMT+SOURCE_LOCAL_AMT as amount,SOURCE_FOR_AMT,BK_INT,SOURCE_LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') as SUBMISSION_DT,ft.unique_fileno      "
					+ " from fc_fc3_part1_new fp,fc_fc3_tally ft   "
					+ "  where fp.rcn='"+rcnNumber+"' and    "
					+ "  ft.rcn=fp.rcn and    "
					+ " fp.blkyear=ft.blkyear and    "
					+ " fp.blkyear not in ('2005-2006') and    "
					+ " ft.final_submit='Y' ) "
					+ " select t2.*,nvl(t1.amount,0) donorwiseamt, (t2.totamount-nvl(t1.amount,0)) diff from t2 left join t1 on t2.blkyear=t1.blkyear order by t2.blkyear desc";
			
			    ReportDataSource ds1=new ReportDataSource(parameters, reportQuery2, connection, virtualizationMaxSize);
			    parameters.put("PRINTRECORD_DATA_SOURCE_RegistrationTracking_2", ds1);
			
			
	
		String tsPath = "/Reports/RegistraionTrackingDetail.jrxml";
		String fileName = "RegistraionTrackingDetail";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
		//GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);	
		// TODO Auto-generated method stub	
	
		
	}	

	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}
	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}



	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
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

	
	public String getRcnNumber() {
		return rcnNumber;
	}
	public void setRcnNumber(String rcnNumber) {
		this.rcnNumber = rcnNumber;
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
	@Override
	protected void generateCSV() throws Exception {

		// TODO Auto-generated method stub
		
		Map  parameters = new HashMap();
		parameters.put("reportType", reportType);
		parameters.put("reportFormat", reportFormat);
		//parameters.put("reportDisplayType", reportDisplyType);
	    parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    
	   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    
	    PreparedStatement statement=null;	StringBuffer query =null;ResultSet rs=null;
	    if(rcnNumber.substring(rcnNumber.length()-1,rcnNumber.length()).equals("R")){
	    	parameters.put("assoType","REGISTERED");
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,A.SECTION_FILENO,A.CURRENT_STATUS,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,B.NEW_OLD,(SELECT BANK_NAME FROM TM_BANKS WHERE to_char(BANK_CODE)=B.BANK_NAME),B.BANK_ADDRESS,B.BANK_TOWN_CITY,B.BANK_ADD1,"
					+ "B.BANK_ADD2,B.BANK_ADD3,B.ACCOUNT_NO,to_char(A.REG_DATE,'dd-mm-yyyy'), case when trunc(VALID_TO) >= trunc(sysdate) then 'N' else 'Y' end as expired "
					+ "FROM FC_INDIA A LEFT JOIN FC_BANK B ON A.RCN=B.RCN AND B.STATUS='Y' WHERE A.RCN=?");
		}else if(rcnNumber.substring(rcnNumber.length()-1,rcnNumber.length()).equals("P")){
			parameters.put("assoType","PRIOR PERMISSION");
			query = new StringBuffer("SELECT A.RCN,A.ASSO_NAME,null,null,A.ASSO_ADDRESS,A.ASSO_TOWN_CITY,A.ADD1,A.ADD2,A.ADD3,A.NEW_OLD,"
					+ "A.ASSO_NATURE,null,null,null,null,null,null,null,null,to_char(A.PP_DATE,'dd-mm-yyyy'),null FROM FC_PP_INDIA A  WHERE A.RCN=?");
		}					
		statement = connection.prepareStatement(query.toString());	
		statement.setString(1, rcnNumber);		
		rs=statement.executeQuery();	
		String statusDesc=null;
		String assoNewOldFlag=null;
		String bankNewOldFlag=null;
		String assoAddress=null;
		String bankAdress=null;
		String assoNature=null;
		String assoNatureDesc="";
		if(rs.next()) {
			/* *
			if(rs.getString(4)==null || rs.getString(4).equals("") || rs.getString(4).equals("0")){
				statusDesc="ALIVE";
			}
			else if(rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			}
			* */
			if(rs.getString(4) != null && rs.getString(4).equals("1")){
				statusDesc="CANCELLED";
			} else if(rs.getString(21) != null && rs.getString(21).equals("Y")) {
				statusDesc="DEEMED TO HAVE CEASED";
			} else {
				statusDesc="ALIVE";
			}
			assoNewOldFlag=rs.getString(10);
			if(assoNewOldFlag.equals("N")){
				assoAddress=rs.getString(5)+", "+rs.getString(6);
			}else if(assoNewOldFlag.equals("O")){
				assoAddress=rs.getString(7)+", "+rs.getString(8)+", "+rs.getString(9);
			}
			bankNewOldFlag=rs.getString(12);			
			if(bankNewOldFlag != null) {
				if(bankNewOldFlag.equals("N")){
					bankAdress=rs.getString(14)+", "+rs.getString(15);
				}else if(bankNewOldFlag.equals("O")){
					bankAdress=rs.getString(16)+", "+rs.getString(17)+", "+rs.getString(18);
				}
			}
			assoNature=rs.getString(11);	
			if(assoNature != null) {	 			
	 			StringBuffer query1 = new StringBuffer("SELECT NATURE_DESC FROM TM_NATURE  WHERE NATURE_CODE=?");
	 			PreparedStatement statement1 = connection.prepareStatement(query1.toString());
		 		for(int j=0;j<assoNature.length();j++){
		 			String natureCode = assoNature.substring(j, j+1);
		 			statement1.setString(1, natureCode);
		 			String delim=(j==0 ? "" : ",");
		 			ResultSet rs5 = statement1.executeQuery();
		 			if(rs5.next()){
		 				assoNatureDesc=assoNatureDesc + delim + rs5.getString(1);
		 			}
		 			rs5.close();
		 		}
		 		statement1.close();	 		 
			}
			parameters.put("rcnNumber",checkNull(rs.getString(1)));
			parameters.put("regDate",checkNull(rs.getString(20)));
			parameters.put("statusDesc",checkNull(statusDesc));
			parameters.put("assoName",checkNull(rs.getString(2)));
			parameters.put("assoAddress",checkNull(assoAddress));
			parameters.put("bankName",checkNull(rs.getString(13)));
			parameters.put("bankAddress",checkNull(bankAdress));
			parameters.put("accountNumber",checkNull(rs.getString(19)));
			parameters.put("assoNature",checkNull(assoNatureDesc));
			
		}	
		   
		   
	    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	    
	    
	 
		String reportQuery = "SELECT t.*,d.dname FROM (select distinct ft.blkyear, "
				+ "  (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, "
				+ "  (fp3.dcode||'O'||fp3.dtype) donor_code, (select pname from fc_purpose where pcode=fp3.pcode) pname, fi.rcn, fi.asso_name,"
				+ "  (select sname from tm_state where scode=substr(fi.stdist,1,2)) state,(select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, "
				+ "   nvl(sum(fp3.amount),0) as amount FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft "
				+ "   where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')   AND fi.rcn=fp3.rcn "
				+ "  AND 1=1   AND substr(fp3.rcn,-1,1)='R'  "
				+ "   and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "' "
				+ "   group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'||fp3.dtype), fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3)  "
				+ "   UNION ALL  "
				+ "   select distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fp3.ctr_code) country, (fp3.dcode||'O'||fp3.dtype) donor_code, "
				+ "   (select pname from fc_purpose where pcode=fp3.pcode) pname, fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) as amount  FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft  "
				+ "  where   fpi.rcn=fp3.rcn  and fp3.blkyear=fpi.blkyear   AND 1=1 "
				+ "  and substr(fp3.rcn,-1,1)='P'"
				+ "  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "'  group by ft.blkyear, fp3.ctr_code, (fp3.dcode||'O'||fp3.dtype), "
				+ "   fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3)  "
				+ "   union all  "
				+ "   SELECT distinct ft.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, "
				+ "   (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname, fi.rcn, fi.asso_name,(select sname from tm_state where scode=substr(fi.stdist,1,2)) state, "
				+ "  (select distname from tm_district where distcode=substr(fi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount   "
				+ "  FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd  where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND "
				+ "  (fi.status <> 'D')   AND fi.rcn=fp3.rcn    AND 1=1   "
				+ "   AND substr(fp3.rcn,-1,1)='R'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y' AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "'  "
				+ "  AND fi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear and fp3.donor_code=fd.donor_code  group by ft.blkyear, fd.donor_country, (fd.donor_code||'N'), "
				+ "   fp3.pcode, fi.rcn, fi.asso_name,substr(fi.stdist,1,2),substr(fi.stdist,-3,3)  "
				+ "  UNION ALL  "
				+ "  SELECT distinct fp3.blkyear, (select ctr_name from tm_country where ctr_code=fd.donor_country) country, (fd.donor_code||'N') donor_code, "
				+ "  (select purpose_name from TM_AMOUNT_PURPOSE where purpose_code=fp3.pcode) pname , fpi.rcn, fpi.asso_name,(select sname from tm_state where scode=substr(fpi.stdist,1,2)) state, "
				+ "   (select distname from tm_district where distcode=substr(fpi.stdist,-3,3)) district, nvl(sum(fp3.amount),0) AS amount  FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, "
				+ "  fc_fc3_donor fd  where  fpi.rcn=fp3.rcn  AND fp3.blkyear=fpi.blkyear  AND 1=1   "
				+ "  and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"
				+ rcnNumber
				+ "'  AND fpi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear  and fp3.donor_code=fd.donor_code "
				+ "   GROUP BY fp3.blkyear, fd.donor_country, (fd.donor_code||'N'), fp3.pcode, fpi.rcn, fpi.asso_name,substr(fpi.stdist,1,2),substr(fpi.stdist,-3,3))t, "
				+ "   (SELECT DCODE||'O1' as DCODE, DNAME FROM fc_inst_donor UNION ALL  SELECT DCODE||'O2' as DCODE, DNAME FROM fc_indv_donor UNION ALL SELECT DONOR_CODE||'N' as DCODE, DONOR_NAME AS DNAME FROM  fc_fc3_donor ) d  "
				+ "   WHERE d.DCODE=t.donor_code order by blkyear, country, donor_code, pname";

     		
	 		ReportDataSource ds=new ReportDataSource(parameters, reportQuery, connection, virtualizationMaxSize);
			parameters.put("PRINTRECORD_DATA_SOURCE_RegistrationTrackingCSV", ds);
			
			String reportQuery2="with t1 as ( "
					+ "select distinct ft.blkyear,  "
					+ "nvl(sum(fp3.amount),0) as amount FROM fc_india fi,fc_fc3_part3 fp3,fc_fc3_tally ft  "
					+ " where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND (fi.status <> 'D')   AND fi.rcn=fp3.rcn   "
					+ " AND 1=1 AND substr(fp3.rcn,-1,1)='R'   "
					+ "and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'  group by ft.blkyear "
					+ " UNION ALL   "
					+ " select distinct ft.blkyear,  "
					+ " nvl(sum(fp3.amount),0) as amount  FROM fc_pp_india fpi,fc_fc3_part3 fp3,fc_fc3_tally ft   "
					+ " where fpi.rcn=fp3.rcn  and fp3.blkyear=fpi.blkyear AND 1=1  "
					+ " and substr(fp3.rcn,-1,1)='P' "
					+ "  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'   "
					+ "  group by ft.blkyear "
					+ "  union all   "
					+ " SELECT distinct ft.blkyear,  "
					+ "  nvl(sum(fp3.amount),0) AS amount    "
					+ " FROM fc_india fi,fc_fc3_donor_wise fp3,fc_fc3_tally ft, fc_fc3_donor fd  where (fi.reg_date IS NULL OR trunc(fi.reg_date) < trunc(to_date('01-04-'||substr(ft.blkyear,-4,4),'dd-mm-yyyy'))) AND  "
					+ " (fi.status <> 'D')   AND fi.rcn=fp3.rcn    AND 1=1    "
					+ " AND substr(fp3.rcn,-1,1)='R'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y' AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'   "
					+ " AND fi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear and fp3.donor_code=fd.donor_code   "
					+ " group by ft.blkyear   "
					+ " UNION ALL   SELECT distinct fp3.blkyear, "
					+ " nvl(sum(fp3.amount),0) AS amount  FROM fc_pp_india fpi,fc_fc3_donor_wise fp3,fc_fc3_tally ft,  "
					+ " fc_fc3_donor fd  where  fpi.rcn=fp3.rcn  AND fp3.blkyear=fpi.blkyear  AND 1=1   "
					+ " and substr(fp3.rcn,-1,1)='P'  and ft.blkyear=fp3.blkyear  and ft.final_submit='Y'  AND ft.rcn = fp3.rcn AND ft.rcn='"+rcnNumber+"'  AND fpi.rcn=fd.rcn  AND ft.blkyear=fp3.blkyear  and fp3.donor_code=fd.donor_code  "
					+ " GROUP BY fp3.blkyear ), t2 as ( select fp.blkyear,for_amt+bk_int+oth_int as totamount,for_amt,(bk_int+oth_int) interest,0 as local_amt,"
					+ "  TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') as SUBMISSION_DT,ft.unique_fileno     "
					+ " from fc_fc3_part1 fp,fc_fc3_tally ft    "
					+ "  where fp.rcn='"+rcnNumber+"' and    "
					+ " ft.rcn=fp.rcn and    "
					+ " fp.blkyear=ft.blkyear and    "
					+ " fp.blkyear not in ('2005-2006') and     "
					+ " ft.final_submit='Y'                  "
					+ " union  "
					+ " select fp.blkyear,BK_INT+SOURCE_FOR_AMT+SOURCE_LOCAL_AMT as amount,SOURCE_FOR_AMT,BK_INT,SOURCE_LOCAL_AMT, TO_CHAR(FINAL_SUBMISSION_DATE, 'dd-mm-yyyy') as SUBMISSION_DT,ft.unique_fileno      "
					+ " from fc_fc3_part1_new fp,fc_fc3_tally ft   "
					+ "  where fp.rcn='"+rcnNumber+"' and    "
					+ "  ft.rcn=fp.rcn and    "
					+ " fp.blkyear=ft.blkyear and    "
					+ " fp.blkyear not in ('2005-2006') and    "
					+ " ft.final_submit='Y' ) "
					+ " select t2.*,nvl(t1.amount,0) donorwiseamt, (t2.totamount-nvl(t1.amount,0)) diff from t2 left join t1 on t2.blkyear=t1.blkyear order by t2.blkyear desc";
			
			    ReportDataSource ds1=new ReportDataSource(parameters, reportQuery2, connection, virtualizationMaxSize);
			    parameters.put("PRINTRECORD_DATA_SOURCE_RegistrationTrackingCSV_2", ds1);
			
			    
			   // String tsPath = "/Reports/RegistraionTrackingDetailCSV.jrxml";
			    String tsPath = "/Reports/RegistrationTrackingDetails_CSV.jrxml";
            String fileName = "RegistraionTrackingDetail";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection, fileName);	
		// TODO Auto-generated method stub	
	
		
	}

private String checkNull(String value){
	if(value==null)
		return "";
	else
	return value;
}
	
}
