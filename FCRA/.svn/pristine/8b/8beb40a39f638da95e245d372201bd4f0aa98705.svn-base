package service.reports;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import models.reports.AnnualStatusDetailsReport;
import models.reports.AssociationsDetailsReport;
import models.reports.AssociationsNotFiledAnnualReturnsReport;
import models.reports.ApplicationPendancyTimeRangewiseReport;
import models.reports.BlueFlaggedCategoryReport;
import models.reports.CountryPurposeDonor;
import models.reports.CountryStateReport;
import models.reports.CountryWiseReceiptReport;
import models.reports.Covid19EmergencyNotRespondReport;
import models.reports.Covid19EmergencyRespondReport;
import models.reports.Covid19EmergencyStateWiseReport;
import models.reports.DisposalDetailsReport;
import models.reports.DistrictDonorReceipt;
import models.reports.DonorListReport;
import models.reports.PendencyReport;
import models.reports.PurposeWiseReport;
import models.reports.RegistrationExpiryReport;
import models.reports.ReligionWiseReport;
import models.reports.ReturnFiledReport;
import models.reports.StatusReport;
import models.reports.SuddenRiseIncomeReport;
import models.reports.RedFlaggedCategoryReport;
import models.reports.UserActivityReport;

import org.apache.log4j.pattern.IntegerPatternConverter;
import org.owasp.esapi.ESAPI;
import org.springframework.util.MultiValueMap;

import dao.master.AssociationNatureDao;
import dao.master.BlockYearDao;
import dao.master.CountryTypeDao;
import dao.master.OfficeDao;
import dao.master.OfficeTypeDao;
import dao.master.ReligionTypeDao;
import dao.master.ServicesDao;
import dao.master.StateDao;
import dao.master.UserDao;
import dao.reports.concretereports.AnuualStatusReportGenerator;
import dao.reports.DonorDetailDao;
import dao.reports.ReportTypeDao;
import dao.reports.MISReportGenerator;
import dao.reports.MISReportGeneratorFactory;
import dao.reports.concretereports.ApplicationPendancyTimeRangewiseReportGenerator;
import dao.reports.concretereports.AssociationsDetailsReportGenerator;
import dao.reports.concretereports.AssociationsNotFiledAnnualReturnsReportGenerator;
import dao.reports.concretereports.BlueFlaggedCategoryReportGenrator;
import dao.reports.concretereports.CountryPurposeDonorReportGenerator;
import dao.reports.concretereports.CountryStateReportGenerator;
import dao.reports.concretereports.CountryWiseReceiptReportGenerator;
import dao.reports.concretereports.Covid19EmergencyNotRespondReportGenerator;
import dao.reports.concretereports.Covid19EmergencyReportGenerator;
import dao.reports.concretereports.Covid19EmergencyRespondReportGenerator;
import dao.reports.concretereports.DisposalDetailsReportGenerator;
import dao.reports.concretereports.DisrictDonorReceiptReportGenerator;
import dao.reports.concretereports.DonorListReportGenerator;
import dao.reports.concretereports.PendencyReportGenerator;
import dao.reports.concretereports.PurposeWiseReportGenerator;
import dao.reports.concretereports.RegistrationExpiryReportGenerator;
import dao.reports.concretereports.ReligionWiseReportGenerator;
import dao.reports.concretereports.ReturnFiledReportGenerator;
import dao.reports.concretereports.StatusReportGenerator;
import dao.reports.concretereports.SuddenRiseInIncomeReportGenrator;
import dao.reports.concretereports.RedFlaggedCategoryReportGenrator;
import dao.reports.concretereports.UserActivityReportGenerator;
import dao.services.RedFlagAssociationsDao;
import utilities.Commons;
import utilities.KVPair;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.NotificationException;
import utilities.notifications.Status;
import utilities.notifications.Type;
public class MISReportService extends Commons {
	private HttpServletResponse response;
	private List<KVPair<String, String>> reportTypeList;
	private List<KVPair<String, String>> countryList;
	private List<KVPair<String, String>> serviceTypeList;
	private List<KVPair<String, String>> redFlagTypeList;
	private List<KVPair<String, String>> YearListstatus;
	private List<KVPair<String, String>> blockYearList;
	private List<KVPair<String, String>> religionWiseList;
	private List<KVPair<String, String>> stateList;
	private List<KVPair<String, String>> purposeList;
	private List<KVPair<String, String>> assoNatureList;
	private List<KVPair<String, String>> userList;
	private List<String> selectServiceList;
	private List<String> selectReligionList;
	private List<String> selectAssoNatureList;
	private List<String> selectYearList;
	private List<String> selectBlockYear;
	private List<String> selectDonor;
	private List<String> selectUserActivity;
	private List<String> selectStateList;
	private List<String> selectCountryList;	
	private String recordRequired;	
	private String districtDonorFlag;
	private List<PendencyReport> pendencyReport;
	private List<StatusReport> statusReport;
	private List<StatusReport> statusReportDetail;
	private List<PurposeWiseReport>purposeWiseReport;
    private List<ReturnFiledReport> returnFieldReport;   
    private List<CountryPurposeDonor> countryPurposeDonorReport;
    private List<DistrictDonorReceipt> districtDonorReceiptReport;
	private List<CountryWiseReceiptReport> countryWiseReceiptReport;
	private List<UserActivityReport> userActivityReport;
    private List<CountryStateReport> countryStateReport;
	private List<ReligionWiseReport> religionWiseReport;
	private List<DonorListReport>  donorListReport;
	private List<SuddenRiseIncomeReport> suddenRiseInIncomeList;
	private List<RedFlaggedCategoryReport> redFlaggedCategoryList;
	private List<BlueFlaggedCategoryReport>blueFlaggedCategoryList;
	private List<AssociationsNotFiledAnnualReturnsReport> associationsNotFiledAnnualReturnsList; 
	private List<ApplicationPendancyTimeRangewiseReport> applicationPendancyTimeRangewiseList;
	private List<AssociationsDetailsReport> associationsDetailsList;
	private List<AnnualStatusDetailsReport> annualDetailsList;
	private List<RegistrationExpiryReport>registrationExpiryList;
	private List<DisposalDetailsReport> disposalDetailsReport;
	private List<Covid19EmergencyStateWiseReport> covid19Emgstwisereport;
	private List<Covid19EmergencyNotRespondReport> covid19Emgntresreport;
	private List<Covid19EmergencyRespondReport> covid19Emgresreport;
	private MultiValueMap<String, String> parameterMap;
    private String totalRecords;
	private String reportType;
	private String reportFormat;
	private String reportDisplayType;
	private String noOfYear;
	private String reportStatusDisplayType;
	private String serviceType;
	private List<String> selectPurposeList;
	private List<String> loginUserName=null;
	private String country;
	private String blockYear;
	private String searchText;
    private List<KVPair<String, String>> expiryYear;
	private String range1;
	private String range2;
	private String range3;
	private String range4;

	private List<String> yearList;

	
	private List<KVPair<String, String>> myLoginofficeId;

	MISReportGenerator report = null;
	private List<String> associationStatus;
	private List<String> reportTypewise;

	private void initializeLists() throws Exception {
		ReportTypeDao reportTypeDao = new ReportTypeDao(connection);
	    reportTypeList = reportTypeDao.getKVList(reportTypeDao.getAliveRecords(myOfficeId));
		CountryTypeDao countryDao = new CountryTypeDao(connection);
		countryList = countryDao.getKVList();
		ServicesDao servicestypedao = new ServicesDao(connection);
		serviceTypeList = servicestypedao.getKVList();
		RedFlagAssociationsDao redFlagAssociationsDao = new RedFlagAssociationsDao(connection);
		redFlagTypeList = redFlagAssociationsDao.getKVList();
		UserDao user= new UserDao(connection);
		if(myOfficeId.equals("0"))
			userList=user.getKVList(myOfficeCode,myOfficeCode.toLowerCase());
		else
			userList=user.getKVList(myOfficeCode,myOfficeCode);
        YearListstatus=getStatusYearList();
        BlockYearDao blockYearDao = new BlockYearDao(connection);
        blockYearList = blockYearDao.getKVList(blockYearDao.getAliveRecords());
        
        
        ReligionTypeDao religion = new ReligionTypeDao(connection);
        religionWiseList = religion.getReligionRecords();
        AssociationNatureDao assoNature= new AssociationNatureDao(connection);
        assoNatureList=assoNature.getKVList();
        
        StateDao stateDao = new StateDao(connection);
        stateList = stateDao.getKVList(stateDao.getAliveRecords());
        purposeList = populatePurposeList();
        OfficeTypeDao obj = new OfficeTypeDao(connection);
        myLoginofficeId = obj.getKVListOfficeId(myOfficeId);
       
        
              
	}

	
	public String execute() {
		String result = "error";
		begin();
		try {
			initializeLists();
			
	        result = "success";
		} catch (ValidationException ve) {
			try {
				notifyList.add(new Notification("Error!", ve.getMessage(),
						Status.ERROR, Type.BAR));
			} catch (Exception ex) {
			}
		} catch (Exception e) {
			ps(e);
			try {
				notifyList.add(new Notification("Error!",
						"Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}
		finish();
		return result;
	}

/*	public List<KVPair<String, String>> getDonorList() throws Exception {
		List<KVPair<String, String>> donorList = new ArrayList<KVPair<String,String>>();
		begin();
		try{
			 country=country.replaceAll(",", "','");
			 String countryQuery1="1=1";
		     if(!country.trim().equals("ALL"))
		    	 countryQuery1=" fid.ctr_code in('"+country+"')";
		     String countryQuery2="1=1";
		     if(!country.trim().equals("ALL"))
		    	 countryQuery2=" fd.DONOR_COUNTRY in('"+country+"')";
			
			StringBuffer query = new StringBuffer("SELECT DCODE,DNAME FROM (select distinct substr(fid.dname,1,60) as dname,fid.dcode||'O'  dcode "
					+ "from fc_inst_donor fid, fc_fc3_part3 fp3 WHERE "+countryQuery1+" and fp3.blkyear  ='"+blockYear+"' AND fid.dcode =fp3.dcode AND "
					+ "fp3.dtype ='1' UNION ALL SELECT DISTINCT substr(fd.DONOR_NAME,1,60) AS dname,fd.DONOR_CODE||'N' dcode from fc_fc3_donor fd, "
					+ "fc_fc3_donor_wise fdw WHERE "+countryQuery2+" AND fdw.blkyear  ='"+blockYear+"' AND fd.DONOR_CODE =fdw.DONOR_CODE AND "
					+ "fd.DONOR_TYPE ='01') order by dname");
			PreparedStatement statement = connection.prepareStatement(query.toString());			
			ResultSet rs = statement.executeQuery();
 			while (rs.next()) {
				int i=1;
				KVPair<String, String> temp = new KVPair<String, String>(rs.getString(i++), 
						rs.getString(i++));
				donorList.add(temp);			
			}		
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			ps(e);
			try {
				notifyList.add(new Notification("Error!",
						"Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}finally{
			finish();
		}
		return donorList;
	} */
	
	public List<KVPair<String, String>> getDonorList() throws Exception {
		List<KVPair<String, String>> donorList = new ArrayList<KVPair<String,String>>();
		begin();
		try{			
			 blockYear=blockYear.replaceAll(",", "','");
			 String blockYear1="1=1";
		     if(!blockYear.trim().equals("ALL"))
		    	 blockYear1=" fp3.blkyear in('"+blockYear+"')";
		     
		     String blockYear2="1=1";
		     if(!blockYear.trim().equals("ALL"))
		    	 blockYear2=" fdw.blkyear in('"+blockYear+"')";
		     
			 country=country.replaceAll(",", "','");
			 String countryQuery1="1=1";
		     if(!country.trim().equals("ALL"))
		    	 countryQuery1=" fid.ctr_code in('"+country+"')";
		     String countryQuery2="1=1";
		     if(!country.trim().equals("ALL"))
		    	 countryQuery2=" fd.DONOR_COUNTRY in('"+country+"')";
			String searchText="1=1";
			if(this.searchText != null && this.searchText.equals("") == false)
				searchText="upper(dname) like upper('%"+this.searchText+"%')";
			StringBuffer query = new StringBuffer("SELECT DCODE,DNAME FROM (select distinct substr(fid.dname,1,60) as dname,fid.dcode||'O'  dcode "
					+ "from fc_inst_donor fid, fc_fc3_part3 fp3 WHERE "+countryQuery1+" and "+blockYear1+" AND fid.dcode =fp3.dcode AND "
					+ "fp3.dtype ='1' UNION ALL SELECT DISTINCT substr(fd.DONOR_NAME,1,60) AS dname,fd.DONOR_CODE||'N' dcode from fc_fc3_donor fd, "
					+ "fc_fc3_donor_wise fdw WHERE "+countryQuery2+" AND "+blockYear2+" AND fd.DONOR_CODE =fdw.DONOR_CODE AND "
					+ "fd.DONOR_TYPE ='01') where "+ searchText+" order by dname");
			PreparedStatement statement = connection.prepareStatement(query.toString());			
			ResultSet rs = statement.executeQuery();
 			while (rs.next()) {
				int i=1;
				KVPair<String, String> temp = new KVPair<String, String>(rs.getString(i++), 
						rs.getString(i++));
				donorList.add(temp);			
			}		
			rs.close();
			statement.close();
		}
		catch (Exception e) {
			ps(e);
			try {
				notifyList.add(new Notification("Error!",
						"Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}finally{
			finish();
		}
		return donorList;
	}
	
	private List<KVPair<String, String>> populatePurposeList() throws Exception {
		if (connection == null) {
			throw new Exception("Invalid connection");
		}

		List<KVPair<String, String>> purposeList = new ArrayList<KVPair<String,String>>();
		StringBuffer query = new StringBuffer(
				"select * from (SELECT PURPOSE_CODE, PURPOSE_NAME FROM "
				+ "tm_amount_purpose where record_status=0 "
				+ "UNION ALL "
				+ "SELECT pcode,pname FROM fc_purpose) order by PURPOSE_NAME");
		PreparedStatement statement = connection.prepareStatement(query
				.toString());
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			int i=1;
			KVPair<String, String> temp = new KVPair<String, String>(rs.getString(i++), 
					rs.getString(i++));
			purposeList.add(temp);			
		}		
		rs.close();
		statement.close();
		return purposeList;
	}
	
	private void assignParameters() throws Exception {
		if (parameterMap != null) {
		///	blockYear = parameterMap.get("blockYear-List").get(0);
			
			reportType = parameterMap.get("reportType").get(0);
			reportFormat = parameterMap.get("reportFormat").get(0);
			OfficeDao odao=new OfficeDao(connection);
			parameterMap.put("loginOfficeName",odao.getOfiiceName(myOfficeCode));
			List<String> loginUserName = new ArrayList<String>();
			loginUserName.add(myUserName);
			parameterMap.put("loginUserName",loginUserName);
			parameterMap.add("myLoginOfficCode", myOfficeCode);
			parameterMap.add("myLoginOfficId", myOfficeId);
		}
	}

	private void validateParameters() throws Exception {
		if (ESAPI.validator().isValidInput("reportType", reportType, "Num", 2,
				false) == false) {
			throw new ValidationException("Invalid Report Type");
		}
		if (ESAPI.validator().isValidInput("reportFormat", reportFormat, "Num",
				1, false) == false) {
			throw new ValidationException("Invalid Report Format");
		}
	}
	private void validatePendencyParameters() throws Exception {
		reportDisplayType=parameterMap.get("reportDisplyType").get(0);
		if (ESAPI.validator().isValidInput("reportDisplayType", reportDisplayType, "Word", 1,true) == false) {
			throw new ValidationException("Invalid Report Display Type");
		}
		selectServiceList=parameterMap.get("service-type");
		if((!selectServiceList.toString().equals("[ALL]")) && (!selectServiceList.toString().contains("ALL"))   )
		if (ESAPI.validator().isValidInput("reportDisplayType", selectServiceList.toString(), "validateMultiSelect", 500,false) == false) {
			throw new ValidationException("Select Service Type !!!");
		}
		selectYearList=parameterMap.get("statusYear-List");
		if((!selectYearList.toString().equals("[ALL]")) && (!selectYearList.toString().contains("ALL")))
		if (ESAPI.validator().isValidInput("reportFormat", selectYearList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Year selection!!!");
		}
	}
	private void validateStatusReportParameters() throws Exception{
		selectServiceList=parameterMap.get("service-type");
		if(!selectServiceList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportDisplayType", selectServiceList.toString(), "validateMultiSelect", 500,false) == false) {
			throw new ValidationException("Select Service Type !!!");
		}
		selectYearList=parameterMap.get("statusYear-List");
		if(!selectYearList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectYearList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Report Format");
		}	
	}
	private void validateReturnFiledParameters() throws Exception{
		/*selectBlockYear=parameterMap.get("blockYear-List");
		if(!selectServiceList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectBlockYear", selectBlockYear.toString(), "validateMultiSelect", 500,false) == false) {
			throw new ValidationException("Select Block Year List!!");
		}*/
		selectStateList=parameterMap.get("state-List");
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectStateList", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid State Selected!!!");
		}	
	}
	private void validatePurposeWiseParameters() throws Exception{
		
		selectStateList=parameterMap.get("state-List");
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectStateList", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid State Selected!!!");
		}	
		selectCountryList=parameterMap.get("country-type");
		if(!selectCountryList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectCountryList", selectCountryList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Country Selected!!!");
		}
		selectPurposeList=parameterMap.get("purpose-type");
		if(!selectPurposeList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectPurposeList", selectPurposeList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Purpose Selected!!!");
		}
	}
	
	private void validateCountryWiseReceiptParameters() throws Exception{
		/*selectBlockYear=parameterMap.get("blockYear-List");
		if(!selectBlockYear.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectBlockYear", selectBlockYear.toString(), "validateMultiSelect", 500,false) == false) {
			throw new ValidationException("Select Block Year List!!");
		}*/
		selectStateList=parameterMap.get("state-List");
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid State!!!");
		}	
		selectCountryList=parameterMap.get("country-List");
		if(!selectCountryList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectCountryList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Country!!!");
		}	
	}
	
	private void validateReligionWiseParameters() throws Exception{
		
		selectStateList=parameterMap.get("state-List");
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid State!!!");
		}	
			
	}
	
private void validateDonorListReportParameters() throws Exception{
		
		selectCountryList=parameterMap.get("country-List");
		if(!selectCountryList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectCountryList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Country!!!");
		}	
			
	}

private void validateSuddenRiseInIncomeReportParameters() throws Exception{
	
 	String forYear=parameterMap.get("report-required-for-year").get(0).toString();
    String avgRatio=parameterMap.get("ratio-tamt-avgamt").get(0).toString();
	if ((ESAPI.validator().isValidInput("reportRequired", forYear, "Num", 1,false) == false) && (Integer.parseInt(forYear) >1) ) {
		throw new ValidationException("No. of years to be Considered is Invalid!!!(Integer Max 1 digit and greater than 1 ");
	}
	
	if ((ESAPI.validator().isValidInput("ratio", avgRatio, "Num", 3,false) == false) && (Integer.parseInt(avgRatio)>1)) {
		
		throw new ValidationException("Ration between total amt and avg amount  is Invalid!!!(Integer Max 1 digit and greater than 1 ");
	}
	
	selectStateList=parameterMap.get("state-List");
	if(!selectStateList.toString().equals("[ALL]"))
	if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
		throw new ValidationException("Invalid State Selected!!!");
	}	
	

}

private void validateAssociationsNotFiledAnnualReturnsReportParameters() throws Exception{
	
	
}
private void validateRegistrationExpiryReportParameters(){
	
}

private void validateRedFlaggedCategoryReportParameters() throws Exception{

	reportDisplayType=parameterMap.get("reportDisplyType").get(0);
	if (ESAPI.validator().isValidInput("reportDisplayType", reportDisplayType, "Word", 1,true) == false) {
		throw new ValidationException("Invalid Report Display Type");
	}
	reportStatusDisplayType = parameterMap.get("reportStatusDisplyType").get(0);
	if (ESAPI.validator().isValidInput("reportStatusDisplayType", reportStatusDisplayType, "Word", 1,true) == false) {
		throw new ValidationException("Invalid Report Status Display Type");
	}	
/*	
 	String forYear=parameterMap.get("report-required-for-year").get(0).toString();
    String avgRatio=parameterMap.get("ratio-tamt-avgamt").get(0).toString();
	if ((ESAPI.validator().isValidInput("reportRequired", forYear, "Num", 1,false) == false) && (Integer.parseInt(forYear) >1) ) {
		throw new ValidationException("No. of years to be Considered is Invalid!!!(Integer Max 1 digit and greater than 1 ");
	}
	
	if ((ESAPI.validator().isValidInput("ratio", avgRatio, "Num", 3,false) == false) && (Integer.parseInt(avgRatio)>1)) {
		
		throw new ValidationException("Ration between total amt and avg amount  is Invalid!!!(Integer Max 1 digit and greater than 1 ");
	}
	
	selectStateList=parameterMap.get("state-List");
	if(!selectStateList.toString().equals("[ALL]"))
	if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
		throw new ValidationException("Invalid State Selected!!!");
	}	
*/	
}

private void validateBlueFlaggedCategoryReportParameters() throws Exception{

	reportDisplayType=parameterMap.get("reportDisplyType").get(0);
	if (ESAPI.validator().isValidInput("reportDisplayType", reportDisplayType, "Word", 1,true) == false) {
		throw new ValidationException("Invalid Report Display Type");
	}
	reportStatusDisplayType = parameterMap.get("reportStatusDisplyType").get(0);
	if (ESAPI.validator().isValidInput("reportStatusDisplayType", reportStatusDisplayType, "Word", 1,true) == false) {
		throw new ValidationException("Invalid Report Status Display Type");
	}	
	
}

private void validateUserActivityReportParameters() throws Exception{
	
 	selectServiceList=parameterMap.get("service-type");
	if(!selectServiceList.toString().equals("[ALL]"))
	if (ESAPI.validator().isValidInput("reportDisplayType", selectServiceList.toString(), "validateMultiSelect", 500,false) == false) {
		throw new ValidationException("Select Service Type !!!");
	}
	/*selectUserActivity=parameterMap.get("user-List");
	if(!selectUserActivity.toString().equals("[ALL]"))
	if (ESAPI.validator().isValidInput("reportDisplayType", selectUserActivity.toString(), "validateMultiSelect", 500,false) == false) {
		throw new ValidationException("Select User Activity !!!");
	}
	*/	
}

	private void validateCountryPurposrDonorParameters() throws Exception{
		selectBlockYear=parameterMap.get("blockYear6");
		/*if(!selectBlockYear.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectBlockYear", selectBlockYear.toString(), "BlockYear", 500,false) == false) {
			throw new ValidationException("Select Block Year List!!");
		}*/
		selectPurposeList=parameterMap.get("purpose6");
		if(!selectPurposeList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectPurposeList", selectPurposeList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Purpose Selected!!!");
		}
		/*selectDonor=parameterMap.get("donor6");
		if(!selectDonor.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectDonorList", selectDonor.toString(), "validateMultiAlpha",500, false) == false) {
			throw new ValidationException("Invalid Donor Selected!!!");
		}*/
		selectCountryList=parameterMap.get("country6");
		if(!selectCountryList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectCountryList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Report Format");
		}	
	}
	private void validateCountryStateParameters() throws Exception{
		selectStateList=parameterMap.get("state-List");
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Report Format");
		}	
		selectCountryList=parameterMap.get("country-List");
		if(!selectCountryList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectCountryList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid Report Format");
		}	
	}
	
	private void validateDistrictDonorReceiptParameters() throws Exception{
		selectBlockYear=parameterMap.get("blockYear8");
		/*if(!selectBlockYear.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("selectBlockYear", selectBlockYear.toString(), "BlockYear", 500,false) == false) {
			throw new ValidationException("Select Block Year List!!");
		}*/
		districtDonorFlag=parameterMap.get("districtDonor8").toString();
		if(!(districtDonorFlag.equals("[4]"))){
			selectStateList=parameterMap.get("state8");
			if(!selectStateList.toString().equals("[ALL]"))
			if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
				throw new ValidationException("Invalid Report Format");
			}
		}
		if(districtDonorFlag.equals("[3]")){
			selectCountryList=parameterMap.get("country8");
			if(!selectCountryList.toString().equals("[ALL]"))
			if (ESAPI.validator().isValidInput("reportFormat", selectCountryList.toString(), "validateMultiSelect",500, false) == false) {
				throw new ValidationException("Invalid Report Format");
			}
		}
	/*	recordRequired=parameterMap.get("record8").toString();
		if (ESAPI.validator().isValidInput("reportFormat", recordRequired, "Num",3, true) == false) {
			throw new ValidationException("Invalid Record Required input");
		}	
		districtDonorFlag=parameterMap.get("districtDonor8").toString();
		if (ESAPI.validator().isValidInput("reportFormat", districtDonorFlag, "validateMultiSelect",1, false) == false) {
			throw new ValidationException("Invalid input");
		}	*/
	}
	
	private void validateAssociationsDetailsReportParameters() throws Exception{
		selectStateList=parameterMap.get("state-List");
		selectAssoNatureList=parameterMap.get("nature-List");
		selectReligionList=parameterMap.get("religion-List");
		
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid State!!!");
		}
		if(!selectAssoNatureList.toString().equals("[ALL]"))
			if (ESAPI.validator().isValidInput("reportFormat", selectAssoNatureList.toString(), "validateMultiSelect",500, false) == false) {
				throw new ValidationException("Invalid Nature!!!");
			}	
		if(!selectReligionList.toString().equals("[ALL]"))
			if (ESAPI.validator().isValidInput("reportFormat", selectReligionList.toString(), "validateMultiSelect",500, false) == false) {
				throw new ValidationException("Invalid Religion!!!");
			}	
			
	}
	
	//validate Anuual Ststus
	public void validateAnnualStatusReportParameters() throws Exception{
		
		noOfYear = parameterMap.get("noOfYear").get(0);
		if (ESAPI.validator().isValidInput("noOfYear", noOfYear, "Word", 1,true) == false) {
			throw new ValidationException("Invalid No. Of Year");
		}	
		
	}
	private void validateApplicationPendancyTimeRangewiseReportParameters() throws Exception{

	/*	String range1 = parameterMap.get("days-of-pendancy1").get(0).toString();
		if(ESAPI.validator().isValidInput("days-of-pendancy1", range1, "Num", 1, false) && (Integer.parseInt(range1)>0)){
			throw new ValidationException("Time Range should be greater than 0 and number only !");
		}*/
		/*		
	 	String forYear=parameterMap.get("report-required-for-year").get(0).toString();
	    String avgRatio=parameterMap.get("ratio-tamt-avgamt").get(0).toString();
		if ((ESAPI.validator().isValidInput("reportRequired", forYear, "Num", 1,false) == false) && (Integer.parseInt(forYear) >1) ) {
			throw new ValidationException("No. of years to be Considered is Invalid!!!(Integer Max 1 digit and greater than 1 ");
		}
		
		if ((ESAPI.validator().isValidInput("ratio", avgRatio, "Num", 3,false) == false) && (Integer.parseInt(avgRatio)>1)) {
			
			throw new ValidationException("Ration between total amt and avg amount  is Invalid!!!(Integer Max 1 digit and greater than 1 ");
		}
		
		selectStateList=parameterMap.get("state-List");
		if(!selectStateList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportFormat", selectStateList.toString(), "validateMultiSelect",500, false) == false) {
			throw new ValidationException("Invalid State Selected!!!");
		}			*/
	}
	
	public String generateReport() {
		String result = "error";
		begin();
		try {
			assignParameters();
			if((!(parameterMap.get("reportType").get(0).equals("tracking"))) && (!(parameterMap.get("reportType").get(0).equals("redflagged")))&&  (!(parameterMap.get("reportType").get(0).equals("blueflagged"))))
				validateParameters();
			if(reportFormat.equals("3")){
				if(reportType.equals("1") ){
					validatePendencyParameters();	
				}
				else if(reportType.equals("2")){
					validateStatusReportParameters();
				}
				else	if(reportType.equals("3")){
					validateReturnFiledParameters();
				}
				else if(reportType.equals("4")){
					validatePurposeWiseParameters();
				}
				else if(reportType.equals("5")){
					validateCountryWiseReceiptParameters();
				}
				else if(reportType.equals("6")){
					validateCountryPurposrDonorParameters();
				}
				else if(reportType.equals("8")){
					validateDistrictDonorReceiptParameters();
				}
				else if(reportType.equals("9")){
					//validateCountryStateParameters();
				}
				else if(reportType.equals("7")){
					validateReligionWiseParameters();
				}
				else if(reportType.equals("10")){
					validateDonorListReportParameters();
				}
				else if(reportType.equals("11")){
					validateUserActivityReportParameters();
				}
				else if(reportType.equals("12")){
					validateSuddenRiseInIncomeReportParameters();
				}
				else if(reportType.equals("13")){
					validateRedFlaggedCategoryReportParameters();
				}
				else if(reportType.equals("27")){
					validateBlueFlaggedCategoryReportParameters();
				}
				else if(reportType.equals("14")){
					validateAssociationsNotFiledAnnualReturnsReportParameters();
				}
				else if(reportType.equals("15")){
					validateApplicationPendancyTimeRangewiseReportParameters();
				}
				else if(reportType.equals("26")){
					validateAssociationsDetailsReportParameters();
				}
				else if(reportType.equals("16")){
					validateAnnualStatusReportParameters();
				}
				else if(reportType.equals("17")){
					validateRegistrationExpiryReportParameters();
				}
				else if(reportType.equals("18")){
					validateDisposalDetailsReportParameters();
				}
			}
			 String covidreportTypewise = null ;
			// System.out.println("88888888888 "+ parameterMap.get("reportTypewise").size());
			if(parameterMap.get("reportTypewise") !=null)       
			{
				covidreportTypewise=parameterMap.get("reportTypewise").get(0);
				System.out.println("ee1111111111111111");
			}
			report = MISReportGeneratorFactory.getReportGenerator(reportType,connection,covidreportTypewise);
			if (report != null) {
				report.generateReport(parameterMap);
			}
			if(report != null && reportFormat.equals("3")){
				if(reportType.equals("1")){					
					pendencyReport=((PendencyReportGenerator)report).getPendencyReport();
				    totalRecords=((PendencyReportGenerator)report).getTotalRecords();				    
				}
				 if(reportType.equals("2")){
					 
						statusReport=((StatusReportGenerator)report).getStatusReport();
					    totalRecords=((StatusReportGenerator)report).getTotalRecords();		 
				 }
				 if(reportType.equals("3")){
					 returnFieldReport=((ReturnFiledReportGenerator)report).getReturnFiledReport();
					    totalRecords=((ReturnFiledReportGenerator)report).getTotalRecords();		 
				 } 
				 if(reportType.equals("4")){
					   purposeWiseReport=((PurposeWiseReportGenerator)report).getPurposeWiseReport();
					    totalRecords=((PurposeWiseReportGenerator)report).getTotalRecords();		 
				 } 
				 if(reportType.equals("5")){
					 countryWiseReceiptReport=((CountryWiseReceiptReportGenerator)report).getCountryWiseReceiptReport();
					 totalRecords=((CountryWiseReceiptReportGenerator)report).getTotalRecords();		 
				 } 
				 if(reportType.equals("6")){					 
					 countryPurposeDonorReport=((CountryPurposeDonorReportGenerator)report).getCountryPurposeDonorReport();					 
					 totalRecords=((CountryPurposeDonorReportGenerator)report).getTotalRecords();	
					 
				 } 
				 if(reportType.equals("8")){
					 districtDonorReceiptReport=((DisrictDonorReceiptReportGenerator)report).getDistrictDonorReceiptReport();					 
					 totalRecords=((DisrictDonorReceiptReportGenerator)report).getTotalRecords();		 
				 } 
				 if(reportType.equals("9")){
					 countryStateReport=((CountryStateReportGenerator)report).getCountryStateReport();					 
					 totalRecords=((CountryStateReportGenerator)report).getTotalRecords();		 
				 } 

				 if(reportType.equals("7")){
					 religionWiseReport=((ReligionWiseReportGenerator)report).getReligionWiseReport();
					 totalRecords=((ReligionWiseReportGenerator)report).getTotalRecords();		 
				 }
				 if(reportType.equals("10")){
					 donorListReport=((DonorListReportGenerator)report).getDonorListReport();
					 totalRecords=((DonorListReportGenerator)report).getTotalRecords();		 
				 }
				 
				 if(reportType.equals("11")){
					 userActivityReport=((UserActivityReportGenerator)report).getUserActivityReport();
					 totalRecords=((UserActivityReportGenerator)report).getTotalRecords();		 
				 }
				 if(reportType.equals("12")){
					 suddenRiseInIncomeList=((SuddenRiseInIncomeReportGenrator)report).getSuddenRiseInIncomeList();
					 totalRecords=((SuddenRiseInIncomeReportGenrator)report).getTotalRecords();		 
				 } 
				 if(reportType.equals("13")){
					 redFlaggedCategoryList=((RedFlaggedCategoryReportGenrator)report).getRedFlaggedCategoryList();
					 totalRecords=((RedFlaggedCategoryReportGenrator)report).getTotalRecords();		 
				 }
				 if(reportType.equals("27")){
					 blueFlaggedCategoryList=((BlueFlaggedCategoryReportGenrator)report).getBlueFlaggedCategoryList();
					 totalRecords=((BlueFlaggedCategoryReportGenrator)report).getTotalRecords();		 
				 }
				 if(reportType.equals("14")){
					 associationsNotFiledAnnualReturnsList=((AssociationsNotFiledAnnualReturnsReportGenerator)report).getAssociationsNotFiledAnnualReturnsList();
					 totalRecords = ((AssociationsNotFiledAnnualReturnsReportGenerator)report).getTotalRecords();
				 }
				 if(reportType.equals("15")){
					 applicationPendancyTimeRangewiseList=((ApplicationPendancyTimeRangewiseReportGenerator)report).getApplicationPendancyTimeRangewiseList();
					 totalRecords=((ApplicationPendancyTimeRangewiseReportGenerator)report).getTotalRecords();
				 } 
				 if(reportType.equals("26")){
					 associationsDetailsList=((AssociationsDetailsReportGenerator)report).getAssociationsDetailsList();
					 totalRecords = ((AssociationsDetailsReportGenerator)report).getTotalRecords();
				 }
				 if(reportType.equals("16")){
					annualDetailsList=((AnuualStatusReportGenerator)report).getAnnualStatusDetailsList();
					 totalRecords = ((AnuualStatusReportGenerator)report).getTotalRecords();
				 }
				 if(reportType.equals("17")){
					 registrationExpiryList=((RegistrationExpiryReportGenerator)report).getRegistrationExpiryDetailsList();
					 totalRecords=((RegistrationExpiryReportGenerator)report).getTotalRecords();
				 } 
				 
				 if(reportType.equals("18")){
					 disposalDetailsReport=((DisposalDetailsReportGenerator)report).getDisposalDetailsReport();
					 totalRecords=((DisposalDetailsReportGenerator)report).getTotalRecords();
				 } 
				 if(reportType.equals("28")){
					 covid19Emgstwisereport=((Covid19EmergencyReportGenerator)report).getCovid19Emgstwisereport();
					 totalRecords=((Covid19EmergencyReportGenerator)report).getTotalRecords();
				 } 
				 if(reportType.equals("29")){
					 String covidreportTypewise11 = parameterMap.get("reportTypewise").get(0);
						if(covidreportTypewise11.equals("Requested")) {
							System.out.println("444444444");
							covid19Emgntresreport=((Covid19EmergencyNotRespondReportGenerator)report).getCovid19Emgntresreport();
							 totalRecords=((Covid19EmergencyNotRespondReportGenerator)report).getTotalRecords();
						}
						else {
							System.out.println("44444444411111");
							covid19Emgresreport=((Covid19EmergencyRespondReportGenerator)report).getCovid19Emgresreport();
							 totalRecords=((Covid19EmergencyRespondReportGenerator)report).getTotalRecords();
						}
					 
				 } 
			}			
		} catch (ValidationException ve) {
			try {
				notifyList.add(new Notification("Error!", ve.getMessage(),
						Status.ERROR, Type.BAR));
			} catch (Exception ex) {
			}
		} catch (Exception e) {
			ps(e);
			try {
				notifyList.add(new Notification("Error!",
						"Some unexpected error occured!.", Status.ERROR,
						Type.BAR));
			} catch (Exception ex) {

			}
		}
		finish();
		return result;
	}
	

	private void validateDisposalDetailsReportParameters() throws ValidationException {
		// TODO Auto-generated method stub	
		selectServiceList=parameterMap.get("service-type");
		reportTypewise=parameterMap.get("reportTypewise");
		if(!selectServiceList.toString().equals("[ALL]"))
		if (ESAPI.validator().isValidInput("reportDisplayType", selectServiceList.toString(), "validateMultiSelect", 500,false) == false) {
			throw new ValidationException("Select Service Type !!!");
		}	
		
	}


	public String invokeCheckInputOrder(){
		begin();
		try {					
				checkInputOrder();				
		}catch(ValidationException ve){			
			try {
				notifyList.add(new Notification("Error !!", ve.getMessage(), Status.ERROR, Type.BAR));
			} catch (NotificationException e) {				
				e.printStackTrace();
			}
			return "error";
		}catch(Exception e){
			try {	
					notifyList.add(new Notification("Error !!", "Some unexpected error.Please try again later.", Status.ERROR, Type.BAR));
					connection.rollback();
				} catch (Exception e1) {				
					e1.printStackTrace();
			}			
			ps(e);			
			return "error";
		}
		finally{
			finish();
		}	
		return "success";
	}
public void checkInputOrder() throws Exception{
	range1 = parameterMap.get("days-of-pendancy1").get(0);
	range2 = parameterMap.get("days-of-pendancy2").get(0);
	range3 = parameterMap.get("days-of-pendancy3").get(0);
	range4 = parameterMap.get("days-of-pendancy4").get(0);
	List<Integer> range = new ArrayList<Integer>();
	List<Integer> rangeArray = new ArrayList<Integer>();
	

	if(range1!=""){range.add(0,Integer.parseInt(range1));}else{range.add(0, -1);}
	if(range2!=""){range.add(1,Integer.parseInt(range2));}else{range.add(1, -1);}
	if(range3!=""){range.add(2,Integer.parseInt(range3));}else{range.add(2, -1);}
	if(range4!=""){range.add(3,Integer.parseInt(range4));}else{range.add(3, -1);}
	
	int i=0, k=0;
	while(i<=3){
		Integer temp = range.get(i);
		if(temp!=-1){rangeArray.add(k, temp);k++;}
		i++;
	}
	
		int rangeSize = rangeArray.size();
		if(rangeSize>=2){
		int temp = rangeArray.get(0);
		int count=1;
		while(count<rangeSize){
			if(temp<rangeArray.get(count)){
				temp=rangeArray.get(count);
				count++;
			}else{
				throw new ValidationException("Kindly enter time ranges in ascending order!");
			}
		}
		}
	
}	

	public void initYear(){
		begin();
		try {
			getYear();				
		} catch(Exception e){
			try {
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
		}
		finally{
			finish();
		}		
	}
	private void getYear() throws Exception{
		AnuualStatusReportGenerator pdd=new AnuualStatusReportGenerator(connection);	
		yearList=pdd.getYaerList(noOfYear);	
	}
	public void ExpiryDate(){
		begin();
		try {
			ExpiryDateDetails();				
		} catch(Exception e){
			try {
					connection.rollback();
				} catch (Exception e1) {				
				e1.printStackTrace();
			}
			ps(e);
		}
		finally{
			finish();
		}		
	}
	private void ExpiryDateDetails() throws Exception{
		RegistrationExpiryReportGenerator pdd=new RegistrationExpiryReportGenerator(connection);	
		expiryYear=pdd.dateList();	
	}
	

public List<AssociationsNotFiledAnnualReturnsReport> getAssociationsNotFiledAnnualReturnsList() {
		return associationsNotFiledAnnualReturnsList;
	}


	public void setAssociationsNotFiledAnnualReturnsList(
			List<AssociationsNotFiledAnnualReturnsReport> associationsNotFiledAnnualReturnsList) {
		this.associationsNotFiledAnnualReturnsList = associationsNotFiledAnnualReturnsList;
	}


public List<UserActivityReport> getUserActivityReport() {
		return userActivityReport;
	}

	public void setUserActivityReport(List<UserActivityReport> userActivityReport) {
		this.userActivityReport = userActivityReport;
	}

public List<DonorListReport> getDonorListReport() {
		return donorListReport;
	}

	public void setDonorListReport(List<DonorListReport> donorListReport) {
		this.donorListReport = donorListReport;
	}

public List<ReligionWiseReport> getReligionWiseReport() {
		return religionWiseReport;
	}

	public void setReligionWiseReport(List<ReligionWiseReport> religionWiseReport) {
		this.religionWiseReport = religionWiseReport;
	}

public List<CountryWiseReceiptReport> getCountryWiseReceiptReport() {
		return countryWiseReceiptReport;
	}

public void setCountryWiseReceiptReport(
			List<CountryWiseReceiptReport> countryWiseReceiptReport) {
		this.countryWiseReceiptReport = countryWiseReceiptReport;
	}

public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public List<KVPair<String, String>> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<KVPair<String, String>> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public List<KVPair<String, String>> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<KVPair<String, String>> countryList) {
		this.countryList = countryList;
	}


	public List<String> getSelectCountryList() {
		return selectCountryList;
	}

	public void setSelectCountryList(List<String> selectCountryList) {
		this.selectCountryList = selectCountryList;
	}

	public List<PendencyReport> getPendencyReport() {
		return pendencyReport;
	}

	
	public void setPendencyReport(List<PendencyReport> pendencyReport) {
		this.pendencyReport = pendencyReport;
	}

	public List<KVPair<String, String>> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<KVPair<String, String>> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public MultiValueMap<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(MultiValueMap<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportDisplayType() {
		return reportDisplayType;
	}

	public void setReportDisplayType(String reportDisplayType) {
		this.reportDisplayType = reportDisplayType;
	}

	public String getReportFormat() {
		return reportFormat;
	}

	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}
	
	public String getRange1() {
		return range1;
	}


	public void setRange1(String range1) {
		this.range1 = range1;
	}


	public String getRange2() {
		return range2;
	}


	public void setRange2(String range2) {
		this.range2 = range2;
	}


	public String getRange3() {
		return range3;
	}


	public void setRange3(String range3) {
		this.range3 = range3;
	}


	public String getRange4() {
		return range4;
	}


	public void setRange4(String range4) {
		this.range4 = range4;
	}


	public List<PurposeWiseReport> getPurposeWiseReport() {
		return purposeWiseReport;
	}

	public void setPurposeWiseReport(List<PurposeWiseReport> purposeWiseReport) {
		this.purposeWiseReport = purposeWiseReport;
	}

	public List<String> getSelectPurposeList() {
		return selectPurposeList;
	}

	public void setSelectPurposeList(List<String> selectPurposeList) {
		this.selectPurposeList = selectPurposeList;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	
	public List<KVPair<String, String>> getYearListstatus() {
		return YearListstatus;
	}

	public void setYearListstatus(List<KVPair<String, String>> yearListstatus) {
		YearListstatus = yearListstatus;
	}

	public List<StatusReport> getStatusReport() {
		return statusReport;
	}

	public void setStatusReport(List<StatusReport> statusReport) {
		this.statusReport = statusReport;
	}

	public List<String> getSelectServiceList() {
		return selectServiceList;
	}

	public void setSelectServiceList(List<String> selectServiceList) {
		this.selectServiceList = selectServiceList;
	}

	public List<String> getSelectYearList() {
		return selectYearList;
	}

	public void setSelectYearList(List<String> selectYearList) {
		this.selectYearList = selectYearList;
	}
   public List<KVPair<String, String>> getStatusYearList() {
		int strtYear=2006;
		int endYear=Calendar.getInstance().get(Calendar.YEAR);
		int ctr=endYear-strtYear;
		List<KVPair<String, String>>  YearListstatus = new ArrayList<KVPair<String, String>>();
		for (int i = 0; i < ctr+1; i++) {
		String aa=String.valueOf(strtYear+i);
		KVPair<String, String> temp = new KVPair<String, String>(aa, aa);
			YearListstatus.add(temp);
		}
		return YearListstatus;
	}
   
   

public List<ApplicationPendancyTimeRangewiseReport> getApplicationPendancyTimeRangewiseList() {
	return applicationPendancyTimeRangewiseList;
}


public void setApplicationPendancyTimeRangewiseList(
		List<ApplicationPendancyTimeRangewiseReport> applicationPendancyTimeRangewiseList) {
	this.applicationPendancyTimeRangewiseList = applicationPendancyTimeRangewiseList;
}


public String getReportStatusDisplayType() {
	return reportStatusDisplayType;
}

public void setReportStatusDisplayType(String reportStatusDisplayType) {
	this.reportStatusDisplayType = reportStatusDisplayType;
}

public List<KVPair<String, String>> getRedFlagTypeList() {
	return redFlagTypeList;
}

public void setRedFlagTypeList(List<KVPair<String, String>> redFlagTypeList) {
	this.redFlagTypeList = redFlagTypeList;
}

public List<String> getSelectUserActivity() {
	return selectUserActivity;
}

public void setSelectUserActivity(List<String> selectUserActivity) {
	this.selectUserActivity = selectUserActivity;
}

public List<KVPair<String, String>> getBlockYearList() {
	return blockYearList;
}

public List<CountryStateReport> getCountryStateReport() {
	return countryStateReport;
}

public void setCountryStateReport(List<CountryStateReport> countryStateReport) {
	this.countryStateReport = countryStateReport;
}

public void setBlockYearList(List<KVPair<String, String>> blockYearList) {
	this.blockYearList = blockYearList;
}

public List<KVPair<String, String>> getStateList() {
	return stateList;
}

public void setStateList(List<KVPair<String, String>> stateList) {
	this.stateList = stateList;
}


public List<ReturnFiledReport> getReturnFieldReport() {
	return returnFieldReport;
}

public void setReturnFieldReport(List<ReturnFiledReport> returnFieldReport) {
	this.returnFieldReport = returnFieldReport;
}


public List<KVPair<String, String>> getPurposeList() {
	return purposeList;
}

public void setPurposeList(List<KVPair<String, String>> purposeList) {
	this.purposeList = purposeList;
}

public List<String> getSelectStateList() {
	return selectStateList;
}

public void setSelectStateList(List<String> selectStateList) {
	this.selectStateList = selectStateList;
}

public List<String> getSelectBlockYear() {
	return selectBlockYear;
}

public void setSelectBlockYear(List<String> selectBlockYear) {
	this.selectBlockYear = selectBlockYear;
}

public String getCountry() {
	return country;
}

public void setCountry(String country) {
	this.country = country;
}

public String getBlockYear() {
	return blockYear;
}

public void setBlockYear(String blockYear) {
	this.blockYear = blockYear;
}

public List<String> getSelectDonor() {
	return selectDonor;
}

public void setSelectDonor(List<String> selectDonor) {
	this.selectDonor = selectDonor;
}

public List<CountryPurposeDonor> getCountryPurposeDonorReport() {
	return countryPurposeDonorReport;
}

public void setCountryPurposeDonorReport(
		List<CountryPurposeDonor> countryPurposeDonorReport) {
	this.countryPurposeDonorReport = countryPurposeDonorReport;
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

public List<DistrictDonorReceipt> getDistrictDonorReceiptReport() {
	return districtDonorReceiptReport;
}

public void setDistrictDonorReceiptReport(
		List<DistrictDonorReceipt> districtDonorReceiptReport) {
	this.districtDonorReceiptReport = districtDonorReceiptReport;
}


public List<KVPair<String, String>> getUserList() {
	return userList;
}

public void setUserList(List<KVPair<String, String>> userList) {
	this.userList = userList;
}


public String getSearchText() {
	return searchText;
}

public void setSearchText(String searchText) {
	this.searchText = searchText;
}

public List<KVPair<String, String>> getReligionWiseList() {
	return religionWiseList;
}

public void setReligionWiseList(List<KVPair<String, String>> religionWiseList) {
	this.religionWiseList = religionWiseList;
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


public List<KVPair<String, String>> getMyLoginofficeId() {
	return myLoginofficeId;
}


public void setMyLoginofficeId(List<KVPair<String, String>> myLoginofficeId) {
	this.myLoginofficeId = myLoginofficeId;
}


public List<AssociationsDetailsReport> getAssociationsDetailsList() {
	return associationsDetailsList;
}


public void setAssociationsDetailsList(
		List<AssociationsDetailsReport> associationsDetailsList) {
	this.associationsDetailsList = associationsDetailsList;
}


public String getNoOfYear() {
	return noOfYear;
}


public void setNoOfYear(String noOfYear) {
	this.noOfYear = noOfYear;
}


public List<AnnualStatusDetailsReport> getAnnualDetailsList() {
	return annualDetailsList;
}


public void setAnnualDetailsList(
		List<AnnualStatusDetailsReport> annualDetailsList) {
	this.annualDetailsList = annualDetailsList;
}


public List<String> getYearList() {
	return yearList;
}


public void setYearList(List<String> yearList) {
	this.yearList = yearList;
}


public List<KVPair<String, String>> getExpiryYear() {
	return expiryYear;
}


public void setExpiryYear(List<KVPair<String, String>> expiryYear) {
	this.expiryYear = expiryYear;
}


public List<RegistrationExpiryReport> getRegistrationExpiryList() {
	return registrationExpiryList;
}


public void setRegistrationExpiryList(
		List<RegistrationExpiryReport> registrationExpiryList) {
	this.registrationExpiryList = registrationExpiryList;
}


public List<KVPair<String, String>> getAssoNatureList() {
	return assoNatureList;
}


public void setAssoNatureList(List<KVPair<String, String>> assoNatureList) {
	this.assoNatureList = assoNatureList;
}


public List<DisposalDetailsReport> getDisposalDetailsReport() {
	return disposalDetailsReport;
}


public void setDisposalDetailsReport(
		List<DisposalDetailsReport> disposalDetailsReport) {
	this.disposalDetailsReport = disposalDetailsReport;
}


public List<BlueFlaggedCategoryReport> getBlueFlaggedCategoryList() {
	return blueFlaggedCategoryList;
}


public void setBlueFlaggedCategoryList(
		List<BlueFlaggedCategoryReport> blueFlaggedCategoryList) {
	this.blueFlaggedCategoryList = blueFlaggedCategoryList;
}

public List<Covid19EmergencyStateWiseReport> getCovid19Emgstwisereport() {
	return covid19Emgstwisereport;
}


public void setCovid19Emgstwisereport(List<Covid19EmergencyStateWiseReport> covid19Emgstwisereport) {
	this.covid19Emgstwisereport = covid19Emgstwisereport;
}


public List<Covid19EmergencyNotRespondReport> getCovid19Emgntresreport() {
	return covid19Emgntresreport;
}


public void setCovid19Emgntresreport(List<Covid19EmergencyNotRespondReport> covid19Emgntresreport) {
	this.covid19Emgntresreport = covid19Emgntresreport;
}


public List<Covid19EmergencyRespondReport> getCovid19Emgresreport() {
	return covid19Emgresreport;
}


public void setCovid19Emgresreport(List<Covid19EmergencyRespondReport> covid19Emgresreport) {
	this.covid19Emgresreport = covid19Emgresreport;
}






}
