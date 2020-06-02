package dao.reports.concretereports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.reports.AssociationsDetailsReport;

import org.springframework.util.MultiValueMap;

import utilities.GenerateExcelVirtualizer;
import utilities.GeneratePdfVirtualizer;
import utilities.ReportDataSource;
import dao.reports.MISReportGenerator;

public class AssociationsDetailsReportGenerator extends MISReportGenerator{

	private List<AssociationsDetailsReport> associationsDetailsList;
	private int virtualizationMaxSize = 200;
	 private MultiValueMap<String, String> parameterMap;
		private String pageNum;
		private String recordsPerPage;
		private String totalRecords;
		private String blockyear;
		private String associationStatus;
		private String loginOfficeName;
		private String loginUserName;
		private List<String> selectAssoNatureList;
		private List<String> selectReligionList;
		private List<String> selectStateList;
		
		public String getTotalRecords() {
			return totalRecords;
		}

		public void setTotalRecords(String totalRecords) {
			this.totalRecords = totalRecords;
		}

	public AssociationsDetailsReportGenerator(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void generatePDF() throws Exception {
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("loginUserName", loginUserName);
		parameters.put("loginOfficeName",loginOfficeName);
		String selectedReligion= "1=1 ";
		String selectedNature="1=1";
		String selectedState= "1=1";
		String selectedStateList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedReligionList=selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedNatureList=selectAssoNatureList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		
		if(!selectedStateList.trim().equals("'ALL'")){
			selectedState="(substr(fi.stdist,1,2))in("+selectedStateList+")";
		
		}
		if(!selectedReligionList.trim().equals("'ALL'")){
			
			selectedReligion="asso_religion in("+selectedReligionList +")";
		
		}
		if(!selectedNatureList.trim().equals("'ALL'")){
			List<String> myList = new ArrayList<String>();
			myList=selectAssoNatureList;
			StringBuffer query1 = new StringBuffer("");
			for(int i=0;i<myList.size();i++){
				query1.append("asso_nature like '%"+ myList.get(i) +"%' or ");
			}
			query1.setLength(query1.length()-3);
			
			selectedNature=query1.toString();
		}
		String reportQuery = null;
		if(associationStatus.equals("0")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and valid_to>=sysdate";
			parameters.put("associationStatus","Association Details List (Active)");
		}
		else if(associationStatus.equals("1")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress , state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=1";
			parameters.put("associationStatus","Association Details List (Cancelled)");
		}	
		else if(associationStatus.equals("2")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress , state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and (valid_to<sysdate or valid_to is null)";
			 parameters.put("associationStatus","Association Details List ( Deemed to have Ceased)");
		}else if(associationStatus.equals("3")){
			reportQuery = "with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress ,state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is not null ";
			parameters.put("associationStatus","Association Details List ( Exempted )");
				
			}else if(associationStatus.equals("4")){
				reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
						+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
						+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
						+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,"
						+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
						+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
						+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
						+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress , state from t i left join"
						+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=3";
				parameters.put("associationStatus","Association Details List (Suspended)");
			}	
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;

		parameters.put("ASSOCIATION_DETAILS", ds);  		
        String tsPath = "/Reports/AssociationsDetails.jrxml";
		String fileName = "Associations-Details-Report";
		GeneratePdfVirtualizer.asAttachment(tsPath, parameters, connection,
				fileName);    	
	}
	

	@Override
	protected void generateExcel() throws Exception {
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		//String subQuery = cureentBlockYear;

		parameters.put("loginUserName", loginUserName);
		parameters.put("loginOfficeName",loginOfficeName);
		String reportQuery = null;
		String selectedReligion= "1=1 ";
		String selectedNature="1=1";
		String selectedState= "1=1";
		String selectedStateList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedReligionList=selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedNatureList=selectAssoNatureList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		
		if(!selectedStateList.trim().equals("'ALL'")){
			selectedState="(substr(fi.stdist,1,2))in("+selectedStateList+")";
		
		}
		if(!selectedReligionList.trim().equals("'ALL'")){
			selectedReligion="asso_religion in("+selectedReligionList +")";
		
		}
		if(!selectedNatureList.trim().equals("'ALL'")){
			List<String> myList = new ArrayList<String>();
			myList=selectAssoNatureList;
			StringBuffer query1 = new StringBuffer("");
			for(int i=0;i<myList.size();i++){
				query1.append("asso_nature like '%"+ myList.get(i) +"%' or ");
			}
			query1.setLength(query1.length()-3);
			
			selectedNature=query1.toString();
		}
		if(associationStatus.equals("0")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress, state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and valid_to>=sysdate";
			parameters.put("associationStatus","Association Details List (Active)");
		}
		else if(associationStatus.equals("1")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+" ) nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=1";
			parameters.put("associationStatus","Association Details List (Cancelled)");
		}	
		else if(associationStatus.equals("2")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,''''||b.account_no||'''' account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress ,state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and (valid_to<sysdate or valid_to is null)";
			parameters.put("associationStatus","Association Details List ( Deemed to have Ceased)");
		}else if(associationStatus.equals("3")){
			reportQuery = "with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress , state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is not null ";	
		parameters.put("associationStatus","Association Details List ( Exempted )");			
		}
		else if(associationStatus.equals("4")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+" ) nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress, state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=3";
			parameters.put("associationStatus","Association Details List (Suspended)");
		}	
		
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;

		parameters.put("ASSOCIATION_DETAILS", ds);	
		String tsPath = "/Reports/AssociationsDetailsExcel.jrxml";
		String fileName = "Associations-Details-Report";
		GenerateExcelVirtualizer.exportReportToExcel(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void generateHTML() throws Exception {
		// TODO Auto-generated method stub
		associationsDetailsList = getAssociationsDetails();
		totalRecords=getTotalRecords();
	}

	private List<AssociationsDetailsReport> getAssociationsDetails() throws Exception {
		// TODO Auto-generated method stub
		List<AssociationsDetailsReport> associationsDetailsList = new ArrayList<AssociationsDetailsReport>();
		StringBuffer str = null;
		String selectedReligion= "1=1 ";
		String selectedNature="1=1";
		String selectedState= "1=1";
		String selectedStateList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedReligionList=selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedNatureList=selectAssoNatureList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		
		if(!selectedStateList.trim().equals("'ALL'")){
			selectedState="(substr(fi.stdist,1,2))in("+selectedStateList+")";
		
		}
		if(!selectedReligionList.trim().equals("'ALL'")){
			selectedReligion="asso_religion in("+selectedReligionList +")";
		
		}
		if(!selectedNatureList.trim().equals("'ALL'")){
			List<String> myList = new ArrayList<String>();
			myList=selectAssoNatureList;
			StringBuffer query1 = new StringBuffer("");
			for(int i=0;i<myList.size();i++){
				query1.append("asso_nature like '%"+ myList.get(i) +"%' or ");
			}
			query1.setLength(query1.length()-3);
			
			
			
			selectedNature=query1.toString();
		}
		if(associationStatus.equals("0")){
		 str = new StringBuffer("with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
		 		+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
		 		+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' "
		 		+ "then fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,"
		 		+ "current_status,association_type,valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn), "
		 		+ "T1 as (select i.rcn,i.asso_name,b.account_no,"
		 		+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
		 		+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
		 		+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,state from t i left join "
		 		+ "(select * from fc_bank where status = 'Y') b on i.rcn = b.rcn where current_status=0 and association_type is null and valid_to>=sysdate)");
		}
		else if(associationStatus.equals("1")){
			str = new StringBuffer("with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
		 		+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
		 		+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' "
		 		+ "then fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,"
		 		+ "current_status,valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn), T1 as ( select i.rcn,i.asso_name,b.account_no,"
		 		+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
		 		+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
		 		+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress, state from t i left join "
		 		+ "(select * from fc_bank where status = 'Y') b on i.rcn = b.rcn where current_status=1)");
		}
		else if(associationStatus.equals("2")){
			str=new StringBuffer("with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn), T1 as ( select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress , state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and (valid_to<sysdate or valid_to is null))");
		}else if(associationStatus.equals("3")){
			 str = new StringBuffer("with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
			 		+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
			 		+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' "
			 		+ "then fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,"
			 		+ "current_status,association_type,valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn), "
			 		+ "T1 as (select i.rcn,i.asso_name,b.account_no,"
			 		+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
			 		+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
			 		+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress, state from t i left join "
			 		+ "(select * from fc_bank where status = 'Y') b on i.rcn = b.rcn where current_status=0 and association_type is not null)");
			
		}else if(associationStatus.equals("4")){
			str = new StringBuffer("with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
			 		+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
			 		+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,(case when fi.new_old='N' "
			 		+ "then fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,"
			 		+ "current_status,valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn), T1 as ( select i.rcn,i.asso_name,b.account_no,"
			 		+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
			 		+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
			 		+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress, state  from t i left join "
			 		+ "(select * from fc_bank where status = 'Y') b on i.rcn = b.rcn where current_status=3)");
			}
		
		StringBuffer countquery = new StringBuffer(str+" select count(1) from T1");
		PreparedStatement statement = connection.prepareStatement(countquery.toString());
		//  statement.setString(1, associationStatus);
		 ResultSet rs = statement.executeQuery();
		  if(rs.next())
		     { 
			  totalRecords = rs.getString(1);
		      } 
		  rs.close();
		  statement.close();
		  Integer pageRequested = Integer.parseInt(pageNum);
			 Integer pageSize = Integer.parseInt(recordsPerPage);
			 StringBuffer query = new StringBuffer(str+", T2 AS (SELECT T1.*, ROWNUM RN FROM T1 WHERE ROWNUM<=?) SELECT * FROM T2 WHERE RN>=?");
			 statement = connection.prepareStatement(query.toString());
			 if(pageNum == null || recordsPerPage == null) {
					
			 }
			 else {
				
				 statement.setInt(1, (pageRequested - 1) * pageSize + pageSize);
				 statement.setInt(2, (pageRequested - 1) * pageSize + 1);
			 }
			 rs = statement.executeQuery();
			 while(rs.next()) {
				 associationsDetailsList.add(new AssociationsDetailsReport(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), "", "", rs.getString(10),rs.getString(11)));
			 }
			 rs.close();
			  statement.close();
		return associationsDetailsList;
	}

	@Override
	protected void generateChart() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void generateCSV() throws Exception {
		// TODO Auto-generated method stub
		Map  parameters = new HashMap();
		parameters.put("loginUserName", loginUserName);
	    parameters.put("loginOfficeName",loginOfficeName);
	    String selectedReligion= "1=1 ";
		String selectedNature="1=1";
		String selectedState= "1=1";
		String selectedStateList=selectStateList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedReligionList=selectReligionList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		String selectedNatureList=selectAssoNatureList.toString().replace("[", "'").replace("]", "'").replace(", ","','");
		
		if(!selectedStateList.trim().equals("'ALL'")){
			selectedState="(substr(fi.stdist,1,2))in("+selectedStateList+")";
		
		}
		if(!selectedReligionList.trim().equals("'ALL'")){
			selectedReligion="asso_religion in("+selectedReligionList +")";
		
		}
		if(!selectedNatureList.trim().equals("'ALL'")){
			List<String> myList = new ArrayList<String>();
			myList=selectAssoNatureList;
			StringBuffer query1 = new StringBuffer("");
			for(int i=0;i<myList.size();i++){
				query1.append("asso_nature like '%"+ myList.get(i) +"%' or ");
			}
			query1.setLength(query1.length()-3);
			
			selectedNature=query1.toString();
		}
	    String reportQuery = null;
	    if(associationStatus.equals("0")){
	    	reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
	    			+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
	    			+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,"
	    		    + "(SELECT listagg(NATURE_DESC,', ') within GROUP(ORDER BY NATURE_CODE)FROM TM_NATURE WHERE NATURE_CODE IN (SELECT regexp_substr(ASSO_NATURE, '(.{1,1})', 1, level)"
	    		    + " FROM dual CONNECT BY level <= ceil(LENGTH(ASSO_NATURE)))) nature,TO_CHAR(fi.reg_date,'dd/mm/yyyy') regndate,"
	    			+ "(case when fi.new_old='N' then"
	    			+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
	    			+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+" ) nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,''''||b.account_no||'''' account_no,"
	    			+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
	    			+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
	    			+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,i.nature,i.regndate,state from t i left join"
	    			+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and valid_to>=sysdate";
	    }
	    else if(associationStatus.equals("1")){
	    	reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress "
					+ "from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,(select distname from tm_district  "
					+ "where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,"
					+ "(SELECT listagg(NATURE_DESC,', ') within GROUP(ORDER BY NATURE_CODE)FROM TM_NATURE WHERE NATURE_CODE IN (SELECT regexp_substr(ASSO_NATURE, '(.{1,1})', 1, level)"
		    	    + " FROM dual CONNECT BY level <= ceil(LENGTH(ASSO_NATURE)))) nature,TO_CHAR(fi.reg_date,'dd/mm/yyyy') regndate,"
					+ "(case when fi.new_old='N' "
					+ "then fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,valid_to "
					+ "from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn),t2 as ( select i.rcn,"
					+ "i.asso_name,b.account_no,(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
					+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end)"
					+ " as bankAddress,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,i.nature,i.regndate, state "
					+ " from t i left join (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=1),t3 as (select rcn, status,status_date, remarks, REFERENCE_FOR_DETAILS from (select a.*, row_number() over (partition by rcn order by status_date desc ) rn from T_REGISTRATION_STATUS_HISTORY a) where rn=1)"
					+ " ,t4 as (select a.rcn,status,status_date,remarks,case when CANCELLATION_TYPE='V' then 'Violation' else case when CANCELLATION_TYPE='R' then 'On Request' else null end end as cancel_type, (select listagg(REASON_DESC,'; ') within group(order by REASON_ID) from TM_REGN_CANCELLATION_REASONS where REASON_ID in (select regexp_substr(b.CANCELLATION_REASON, '(.{1,2})', 1, level) from dual connect by level <= ceil(length(b.CANCELLATION_REASON)/2))) reason from t3 A left join T_REGN_CANCELLATION_DETAILS b "
					+ "on a.REFERENCE_FOR_DETAILS = b.REFERENCE_FOR_DETAILS) "
					+ "select t2.*,to_char(t4.status_date,'dd-mm-yyyy') as cancelled_date,  t4.cancel_type, t4.remarks as cancelled_remarks,t4.reason from t2, t4 where t2.rcn=t4.rcn";
	    }
	    else if(associationStatus.equals("2")){
			reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,"
					+ "(SELECT listagg(NATURE_DESC,', ') within GROUP(ORDER BY NATURE_CODE)FROM TM_NATURE WHERE NATURE_CODE IN (SELECT regexp_substr(ASSO_NATURE, '(.{1,1})', 1, level)"
		    	    + " FROM dual CONNECT BY level <= ceil(LENGTH(ASSO_NATURE)))) nature,TO_CHAR(fi.reg_date,'dd/mm/yyyy') regndate,"
					+ "(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,''''||b.account_no||'''' account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,i.nature,i.regndate , state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is null and (valid_to<sysdate or valid_to is null)";
		}else if(associationStatus.equals("3")){
			reportQuery = "with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', "
					+ "'||nw.state) assoAddress from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,"
					+ "(select distname from tm_district where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,"
					+ "(SELECT listagg(NATURE_DESC,', ') within GROUP(ORDER BY NATURE_CODE)FROM TM_NATURE WHERE NATURE_CODE IN (SELECT regexp_substr(ASSO_NATURE, '(.{1,1})', 1, level)"
		    	    + " FROM dual CONNECT BY level <= ceil(LENGTH(ASSO_NATURE)))) nature,TO_CHAR(fi.reg_date,'dd/mm/yyyy') regndate,"
					+ "(case when fi.new_old='N' then"
					+ " fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,association_type,"
					+ "valid_to from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn) select i.rcn,i.asso_name,b.account_no,"
					+ "(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city "
					+ "||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end) as bankAddress,i.cheif_functionary_name,"
					+ "i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,i.nature,i.regndate, state from t i left join"
					+ " (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=0 and association_type is not null ";
		}
		  else if(associationStatus.equals("4")){
		    	reportQuery="with t as (select nw.*,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(nw.AsoAddress || ', '||nw.district||', '||nw.state) assoAddress "
						+ "from  (select fi.rcn,asso_name,(select sname from tm_state where scode = substr(fi.stdist,1,2) )as state,(select distname from tm_district  "
						+ "where  distcode = substr(fi.stdist,-3,3)  and scode =substr(fi.stdist,1,2)  )as district,"
						+ "(SELECT listagg(NATURE_DESC,', ') within GROUP(ORDER BY NATURE_CODE)FROM TM_NATURE WHERE NATURE_CODE IN (SELECT regexp_substr(ASSO_NATURE, '(.{1,1})', 1, level)"
			    	    + " FROM dual CONNECT BY level <= ceil(LENGTH(ASSO_NATURE)))) nature,TO_CHAR(fi.reg_date,'dd/mm/yyyy') regndate,"
						+ "(case when fi.new_old='N' "
						+ "then fi.asso_address || ','|| fi.asso_town_city || fi.asso_pin else fi.add1||', '|| fi.add2 || ','||fi.add3 || '-'|| fi.asso_pin end) as AsoAddress,current_status,valid_to "
						+ "from fc_india fi where status!='D' and  "+selectedState+" and ("+selectedNature+") and "+selectedReligion+") nw left join fc_asso_details i on nw.rcn=i.rcn),t2 as ( select i.rcn,"
						+ "i.asso_name,b.account_no,(select ba.bank_name from tm_banks ba where to_char(ba.bank_code) = b.bank_name) as bankname,"
						+ "(case when b.new_old='N' then b.bank_address ||',' || b.bank_town_city ||', '|| b.bank_pin  else b.bank_add1 || ', ' || b.bank_add2 || ',' || b.bank_add3 ||',' || b.bank_pin end)"
						+ " as bankAddress,i.cheif_functionary_name,i.asso_cheif_mobile, i.asso_official_email,i.PAN_no,(AsoAddress || ', '||district||', '||state) assoAddress,i.nature,i.regndate, state "
						+ " from t i left join (select * from fc_bank where status = 'Y') b on i.rcn = b.rcn   where current_status=3),t3 as (select rcn, status,status_date, remarks, REFERENCE_FOR_DETAILS from (select a.*, row_number() over (partition by rcn order by status_date desc ) rn from T_REGISTRATION_STATUS_HISTORY a) where rn=1)"
						+ " ,t4 as (select a.rcn,status,status_date,remarks from t3 A ) "
						+ "select t2.*,to_char(t4.status_date,'dd-mm-yyyy') as cancelled_date, t4.remarks as cancelled_remarks from t2, t4 where t2.rcn=t4.rcn";
		    }
		ReportDataSource ds = new ReportDataSource(parameters, reportQuery,connection, virtualizationMaxSize)  ;
		if(associationStatus.equals("1")){   
		parameters.put("ASSOCIATION_Cancel_DETAILS", ds);  	
		}
		else if(associationStatus.equals("4")){   
			parameters.put("ASSOCIATION_Suspended_DETAILS", ds);  	
			}
		else
			parameters.put("ASSOCIATION_DETAILS", ds);  	
		
		String tsPath = "/Reports/AssociationsDetailsCsv.jrxml";
		String fileName = "Associations-Details-Report";
		GenerateExcelVirtualizer.exportReportToCsv(tsPath, parameters, connection, fileName);
	}

	@Override
	protected void assignParameters(MultiValueMap<String, String> parameterMap)
			throws Exception {
		// TODO Auto-generated method stub
		if(parameterMap != null) {
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			associationStatus = parameterMap.get("associationStatus").get(0);
			loginOfficeName=parameterMap.get("loginOfficeName").get(0);
		    loginUserName=parameterMap.get("loginUserName").get(0);
		    selectAssoNatureList=parameterMap.get("nature-List");
			selectReligionList=parameterMap.get("religion-List");
			selectStateList=parameterMap.get("state-List");
		
			if(reportFormat.equals("3")){
				pageNum=parameterMap.get("pageNum").get(0);
				recordsPerPage=parameterMap.get("recordsPerPage").get(0);	
			}
			
		}

	}

	public List<AssociationsDetailsReport> getAssociationsDetailsList() {
		return associationsDetailsList;
	}

	public void setAssociationsDetailsList(
			List<AssociationsDetailsReport> associationsDetailsList) {
		this.associationsDetailsList = associationsDetailsList;
	}

	public int getVirtualizationMaxSize() {
		return virtualizationMaxSize;
	}

	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public String getPageNum() {
		return pageNum;
	}

	public String getRecordsPerPage() {
		return recordsPerPage;
	}

	public String getBlockyear() {
		return blockyear;
	}

	public String getAssociationStatus() {
		return associationStatus;
	}

	public void setVirtualizationMaxSize(int virtualizationMaxSize) {
		this.virtualizationMaxSize = virtualizationMaxSize;
	}

	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public void setRecordsPerPage(String recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public void setBlockyear(String blockyear) {
		this.blockyear = blockyear;
	}

	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
	}

	public String getLoginOfficeName() {
		return loginOfficeName;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginOfficeName(String loginOfficeName) {
		this.loginOfficeName = loginOfficeName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public List<String> getSelectAssoNatureList() {
		return selectAssoNatureList;
	}

	public void setSelectAssoNatureList(List<String> selectAssoNatureList) {
		this.selectAssoNatureList = selectAssoNatureList;
	}

	public List<String> getSelectReligionList() {
		return selectReligionList;
	}

	public void setSelectReligionList(List<String> selectReligionList) {
		this.selectReligionList = selectReligionList;
	}

	public List<String> getSelectStateList() {
		return selectStateList;
	}

	public void setSelectStateList(List<String> selectStateList) {
		this.selectStateList = selectStateList;
	}



}
