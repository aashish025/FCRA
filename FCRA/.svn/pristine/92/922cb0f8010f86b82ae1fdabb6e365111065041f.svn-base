package web.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.reports.AnnualStatusDetailsReport;
import models.reports.AssociationsDetailsReport;
import models.reports.BlueFlaggedCategoryReport;
import models.reports.CountryPurposeDonor;
import models.reports.CountryStateReport;
import models.reports.CountryWiseReceiptReport;
import models.reports.Covid19EmergencyNotRespondReport;
import models.reports.Covid19EmergencyRespondReport;
import models.reports.Covid19EmergencyStateWiseReport;
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
import models.reports.AssociationsNotFiledAnnualReturnsReport;
import models.reports.ApplicationPendancyTimeRangewiseReport;
import models.reports.UserActivityReport;
import models.reports.DisposalDetailsReport;
import models.services.ListPager;
import models.services.requests.AbstractRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.reports.DonorDetailService;
import service.reports.MISReportService;
import service.reports.RegistrationTrackingService;
//import utilities.ActionStatus;
import utilities.KVPair;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

@Controller
public class MISReportController {
	private final Logger logger = LoggerFactory.getLogger(MISReportController.class);	
	
	@RequestMapping(value={"/mis-report"}, method=RequestMethod.GET)
	public ModelAndView execute() throws Exception{
		logger.debug("execute() is executed ");		
		MISReportService misReportService = new MISReportService();
		String result = misReportService.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("reportTypeList", misReportService.getReportTypeList());
		
		model.setViewName("reports/mis-reports");		
		return model;
	}
	
	
	@RequestMapping(value={"/get-lists-esp-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody Map<String, List<KVPair<String, String>>> pullLists(){
		logger.debug("execute() is executed ");		
		MISReportService misReportService = new MISReportService();
		String result = misReportService.execute();		
		Map<String, List<KVPair<String, String>>> map = new HashMap<String, List<KVPair<String,String>>>();
		map.put("countryList", misReportService.getCountryList());
		map.put("serviceTypeList", misReportService.getServiceTypeList());
		map.put("redFlagTypeList", misReportService.getRedFlagTypeList());
		map.put("YearListstatus",  misReportService.getYearListstatus());
		map.put("stateList",  misReportService.getStateList());
		map.put("blockYearList",  misReportService.getBlockYearList());
		map.put("religionList",  misReportService.getReligionWiseList());
		map.put("purposeList",  misReportService.getPurposeList()); 
		map.put("assoNatureList",  misReportService.getAssoNatureList()); 
		map.put("userList",  misReportService.getUserList()); 
		map.put("myLoginofficeId",  misReportService.getMyLoginofficeId());
		return map;
	}
	
	@RequestMapping(value={"/download-mis-report"}, method=RequestMethod.POST)
	public ModelAndView downloadReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed ");
		MISReportService misReportService = new MISReportService();		
		misReportService.setParameterMap(params);
		misReportService.generateReport();
		return null;
	}
	@RequestMapping(value={"/show-pendency-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<PendencyReport> initPendencyReport(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<PendencyReport> ReportList = new ListPager<PendencyReport>();
		ReportList.setList(misreportservices.getPendencyReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	@RequestMapping(value={"/show-StatusReport-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<StatusReport> initStatusReport(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<StatusReport> ReportList = new ListPager<StatusReport>();
		ReportList.setList(misreportservices.getStatusReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	//Html Report When Selected ** RETURN FIELD REPORT **
	@RequestMapping(value={"/show-returnField-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<ReturnFiledReport> initReturnFieldReport(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<ReturnFiledReport> ReportList = new ListPager<ReturnFiledReport>();
		ReportList.setList(misreportservices.getReturnFieldReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}

	// Purpose-Wise
	@RequestMapping(value = { "/show-purpose-mis-report" }, method = RequestMethod.GET)
	public @ResponseBody ListPager<PurposeWiseReport> initPurposeReport(@RequestParam MultiValueMap<String, String> params)
			throws Exception
	{
		logger.debug("execute() is executed ");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<PurposeWiseReport> ReportList = new ListPager<PurposeWiseReport>();
		ReportList.setList(misreportservices.getPurposeWiseReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}

	//HTML FOR CountryWise Receipt Report 
	@RequestMapping(value={"/show-CountryWiseReceipt-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<CountryWiseReceiptReport> initCountryWiseReceipt(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<CountryWiseReceiptReport> ReportList = new ListPager<CountryWiseReceiptReport>();
		ReportList.setList(misreportservices.getCountryWiseReceiptReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
//	show-User_Activity_Report-mis-report
	@RequestMapping(value={"/show-User_Activity-Report-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<UserActivityReport> initUserActivity(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<UserActivityReport> ReportList = new ListPager<UserActivityReport>();
		ReportList.setList(misreportservices.getUserActivityReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	
	// Country-Purpose-Donor
	@RequestMapping(value={"/get-donor-list-mis-report"}, method=RequestMethod.POST)
	public @ResponseBody List<KVPair<String, String>> getDistrict(@RequestParam String country6,@RequestParam String blockYear6,@RequestParam String searchText) throws Exception{
		logger.debug("execute() is executed ");		
		MISReportService mrs=new MISReportService();		
		mrs.setCountry(country6);
		mrs.setBlockYear(blockYear6);
		mrs.setSearchText(searchText);
		return mrs.getDonorList();
	}
	
	@RequestMapping(value={"/show-country-purpose-donor-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<CountryPurposeDonor> initCountryPurposrDonorReport(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<CountryPurposeDonor> ReportList = new ListPager<CountryPurposeDonor>();	
		ReportList.setList(misreportservices.getCountryPurposeDonorReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	// Country-Purpose-Donor	

	// Top District-Donor Receipt Wise Report
	@RequestMapping(value={"/show-district-donor-receipt-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<DistrictDonorReceipt> initDistrictDonorReceiptReport(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<DistrictDonorReceipt> ReportList = new ListPager<DistrictDonorReceipt>();	
		ReportList.setList(misreportservices.getDistrictDonorReceiptReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	// Top District-Donor Receipt Wise Report

	@RequestMapping(value = { "/show-country-state-mis-report" }, method = RequestMethod.GET)
	public @ResponseBody ListPager<CountryStateReport> initCountryStateReport(@RequestParam MultiValueMap<String, String> params)
			throws Exception
	{
		logger.debug("execute() is executed ");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<CountryStateReport> ReportList = new ListPager<CountryStateReport>();
		ReportList.setList(misreportservices.getCountryStateReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	

	@RequestMapping(value={"/show-ReligionWise-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<ReligionWiseReport> initReligionWise(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<ReligionWiseReport> ReportList = new ListPager<ReligionWiseReport>();
		ReportList.setList(misreportservices.getReligionWiseReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	//show-showDonorListReport-mis-report
	
	@RequestMapping(value={"/show-showDonorListReport-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<DonorListReport> showDonorListReport(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<DonorListReport> ReportList = new ListPager<DonorListReport>();
		ReportList.setList(misreportservices.getDonorListReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}

	@RequestMapping(value={"/show-sudden-rise-incom-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<SuddenRiseIncomeReport> initSuddenRiseInIncome(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<SuddenRiseIncomeReport> ReportList = new ListPager<SuddenRiseIncomeReport>();
		ReportList.setList(misreportservices.getSuddenRiseInIncomeList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	@RequestMapping(value={"/show-red_flagged-cat-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<RedFlaggedCategoryReport> initRedFlaggedCategory(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<RedFlaggedCategoryReport> ReportList = new ListPager<RedFlaggedCategoryReport>();
		ReportList.setList(misreportservices.getRedFlaggedCategoryList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	@RequestMapping(value={"/show-blue_flagged-cat-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<BlueFlaggedCategoryReport> initBlueFlaggedCategory(@RequestParam MultiValueMap<String, String> params ) throws Exception{
		logger.debug("execute() is executed ");	
		MISReportService misreportservices=new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<BlueFlaggedCategoryReport> ReportList = new ListPager<BlueFlaggedCategoryReport>();
		ReportList.setList(misreportservices.getBlueFlaggedCategoryList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	@RequestMapping(value={"/show-asso-not-filed-returns-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AssociationsNotFiledAnnualReturnsReport> initAssoNotFiledAnnualReturns(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<AssociationsNotFiledAnnualReturnsReport> ReportList = new ListPager<AssociationsNotFiledAnnualReturnsReport>();
		ReportList.setList(misreportservices.getAssociationsNotFiledAnnualReturnsList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	
	@RequestMapping(value={"/check-input-timerange-order-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody List<String> initApplicationPendancyTimeRangeInputOrderCheck(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		String status=misreportservices.invokeCheckInputOrder();

		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList = new ArrayList<String>();
		statusList.add(mapper.writeValueAsString(misreportservices.getNotifyList()));
		statusList.add(status);
		try{
			if(statusList.equals(null)){
				misreportservices.getNotifyList().add(new Notification("Info!!", "Enter time ranges in ascending order!", Status.WARNING, Type.STICKY));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return statusList;
	}
	
	@RequestMapping(value={"/show-application-pendancy-time-rangewise-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<ApplicationPendancyTimeRangewiseReport> initApplicationPendancyTimeRangewise(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<ApplicationPendancyTimeRangewiseReport> ReportList = new ListPager<ApplicationPendancyTimeRangewiseReport>();
		ReportList.setList(misreportservices.getApplicationPendancyTimeRangewiseList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	@RequestMapping(value={"/show-AssociationDetails-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AssociationsDetailsReport> initAssoDetails(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<AssociationsDetailsReport> ReportList = new ListPager<AssociationsDetailsReport>();
		ReportList.setList(misreportservices.getAssociationsDetailsList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	// COntroller for Annual Status

	@RequestMapping(value={"/show-annual-status-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AnnualStatusDetailsReport> initAnnnalDetails(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<AnnualStatusDetailsReport> ReportList = new ListPager<AnnualStatusDetailsReport>();
		ReportList.setList(misreportservices.getAnnualDetailsList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	@RequestMapping(value={"/get-year-Block-mis-report"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getYear(@RequestParam String noOfYear) throws Exception{
		logger.debug("execute() is executed ");		
		MISReportService misreportservices = new MISReportService();	
		misreportservices.setNoOfYear(noOfYear);		
		misreportservices.initYear();;		 
		return misreportservices.getYearList();
	}
	
	@RequestMapping(value={"/expiry-date-mis-report"}, method=RequestMethod.POST)
	public @ResponseBody List<String> DateList() throws Exception{
		logger.debug("execute() is executed ");		
		MISReportService misreportservices = new MISReportService();			
		misreportservices.ExpiryDate();
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList = new ArrayList<String>();
		statusList.add(mapper.writeValueAsString(misreportservices.getExpiryYear()));
		return statusList;
	}
	@RequestMapping(value={"/show-RegistrationEntryDetails-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<RegistrationExpiryReport> RegistrationExpiry(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<RegistrationExpiryReport> ReportList = new ListPager<RegistrationExpiryReport>();
		ReportList.setList(misreportservices.getRegistrationExpiryList());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	@RequestMapping(value={"/show-Covid19Emergency-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<Covid19EmergencyStateWiseReport> Covid19EmergencyReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		System.out.println("params"+params);
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<Covid19EmergencyStateWiseReport> ReportList = new ListPager<Covid19EmergencyStateWiseReport>();
		ReportList.setList(misreportservices.getCovid19Emgstwisereport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	@RequestMapping(value={"/show-DisposeDetails-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<DisposalDetailsReport> DisposalDetailsReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<DisposalDetailsReport> ReportList = new ListPager<DisposalDetailsReport>();
		ReportList.setList(misreportservices.getDisposalDetailsReport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	
	@RequestMapping(value={"/show-Covid19Emergency-not-requested-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<Covid19EmergencyNotRespondReport> Covid19EmergencyNotRequestReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		System.out.println("params"+params);
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<Covid19EmergencyNotRespondReport> ReportList = new ListPager<Covid19EmergencyNotRespondReport>();
		ReportList.setList(misreportservices.getCovid19Emgntresreport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
	@RequestMapping(value={"/show-Covid19Emergency-requested-mis-report"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<Covid19EmergencyRespondReport> Covid19EmergencyRequestReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed");
		System.out.println("params"+params);
		MISReportService misreportservices = new MISReportService();
		misreportservices.setParameterMap(params);
		misreportservices.generateReport();
		ListPager<Covid19EmergencyRespondReport> ReportList = new ListPager<Covid19EmergencyRespondReport>();
		ReportList.setList(misreportservices.getCovid19Emgresreport());
		ReportList.setTotalRecords(misreportservices.getTotalRecords());
		return ReportList;
	}
}

