package web.reports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import models.services.ListPager;
import models.services.requests.AbstractRequest;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.reports.MISReportService;
import service.reports.RegistrationTrackingService;
import service.services.dashboard.ApplicationStatusService;
import service.services.dashboard.ProjectDashboardService;
import utilities.ValidationException;
import utilities.lists.List2;
import web.Home;

@Controller
public class RegistrationTracking {
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	private String actionStatus=null;
	@RequestMapping(value={"/registration-tracking"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();          
		ServletContext context = attr.getRequest().getSession().getServletContext();
		String version = context.getInitParameter("version");
		if(ESAPI.validator().isValidInput("version", version, "version", 6, false) == false){
			throw new ValidationException("version- Invalid entry. Only  numbers and . allowed (6 characters max)");
		}
		RegistrationTrackingService pds = new RegistrationTrackingService();
		pds.initRegistrationTrackingAction();	
		ModelAndView model = new ModelAndView();		
		model.addObject("stateList", pds.getStateList());	
		model.addObject("version", version);
		model.setViewName("reports/registration-tracking");		
		return model;
	}
	
	@RequestMapping(value={"/get-district-registration-tracking","/get-district-red-flagged-rcn","/get-district-blue-flagged-rcn"}, method=RequestMethod.POST)
	public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationTrackingService pds = new RegistrationTrackingService();		
		pds.setState(state);		
		pds.initDistrict();		 
		return pds.getDistrictList();
	}
	
	@RequestMapping(value={"/get-application-list-registration-tracking"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
			,@RequestParam String applicationName) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationTrackingService pds=new RegistrationTrackingService();
		pds.setPageNum(pageNum);
		pds.setRecordsPerPage(recordsPerPage);
		pds.setSortColumn(sortColumn);
		pds.setSortOrder(sortOrder);	
		pds.setAppId(applicationId.toUpperCase());
		pds.setAppName(applicationName.toUpperCase());
		pds.initApplicationListDetails();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(pds.getApplicationList());
		ar.setTotalRecords(pds.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/get-advance-application-list-registration-tracking"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
			,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationTrackingService pds=new RegistrationTrackingService();
		pds.setPageNum(pageNum);
		pds.setRecordsPerPage(recordsPerPage);
		pds.setSortColumn(sortColumn);
		pds.setSortOrder(sortOrder);		
		pds.setAppName(applicationName.toUpperCase());
		pds.setFunctionaryName(functionaryName.toUpperCase());
		pds.setState(state);
		pds.setDistrict(district);
		pds.initAdvanceApplicationListDetails();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(pds.getApplicationList());
		ar.setTotalRecords(pds.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/get-application-details-registration-tracking"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationTrackingService pds=new RegistrationTrackingService();
		pds.setAppId(appId.toUpperCase());
		pds.initGetApplicationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(pds.getApplicationList()));
		details.add(mapper.writeValueAsString(pds.getNotifyList()));		
		details.add(mapper.writeValueAsString(pds.getReturnList()));		
		details.add(mapper.writeValueAsString(pds.getCommitteeMembers()));
		details.add(pds.getRedFlag());
		details.add(pds.getRedFlagAddRole());
		details.add(pds.getRedFlagRemoveRole());
		details.add(mapper.writeValueAsString(pds.getRedFlagCategoryList()));
		details.add(mapper.writeValueAsString(pds.getRedFlagDetailsList()));
		details.add(mapper.writeValueAsString(pds.getHistoryList()));		
		details.add(pds.getYellowFlagRemoveRole());
		details.add(pds.getRedFlagREDCategory());
		details.add(pds.getRedFlagYELLOWCategory());
		details.add(mapper.writeValueAsString(pds.getRegCancDetails()));
		details.add(mapper.writeValueAsString(pds.getSmsList()));
		details.add(mapper.writeValueAsString(pds.getMailList()));
		details.add(pds.getBlueFlag());
		return details;
	}
	@RequestMapping(value={"/add-red-flag-registration-tracking"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addRedFlag(@RequestParam String appId,@RequestParam String remark,@RequestParam String category
			,@RequestParam String office,@RequestParam String order,@RequestParam String date) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationTrackingService pds=new RegistrationTrackingService();
		pds.setAppId(appId.toUpperCase());
		pds.setRemark(remark);
		pds.setRedFlagCategory(category);
		pds.setOriginatorOffice(office);
		pds.setOrderNumber(order);
		pds.setOrderDate(date);
		String actionStatus=pds.initAddToRedFlagList();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();		
		details.add(mapper.writeValueAsString(pds.getNotifyList()));	
		details.add(actionStatus);
		return details;
	}
	@RequestMapping(value={"/remove-red-flag-registration-tracking"}, method=RequestMethod.POST)
	public @ResponseBody List<String> removeRedFlag(@RequestParam String appId,@RequestParam String remark
			,@RequestParam String office,@RequestParam String order,@RequestParam String date) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationTrackingService pds=new RegistrationTrackingService();
		pds.setAppId(appId.toUpperCase());
		pds.setRemark(remark);
		pds.setOriginatorOffice(office);
		pds.setOrderNumber(order);
		pds.setOrderDate(date);
		String actionStatus=pds.initRemoveFromRedFlagList();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();		
		details.add(mapper.writeValueAsString(pds.getNotifyList()));		
		details.add(actionStatus);
		return details;
	}
	@RequestMapping(value = {"/popup-registration-tracking"}, method = RequestMethod.GET)
	public ModelAndView submitPopup(@RequestParam String appId) throws Exception {
		/*if(ESAPI.validator().isValidInput("PropertyId", appId, "Word", 12, false) == false) {
			throw new ValidationException("Property ID - Invalid entry. Only alphabets and numbers allowed (12 characters max)");
		}*/			
		logger.debug("execute() is executed ");	
		ModelAndView model=new ModelAndView();
	    model.addObject("registrationId",appId);
        model.setViewName("reports/registration-tracking"); 
		return model;
	}	
@RequestMapping(value={"/download-registration-tracking","/download-red-flagged-rcn"}, method=RequestMethod.POST)
	public ModelAndView downloadReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
		logger.debug("execute() is executed ");		
		MISReportService misReportService = new MISReportService();		
		misReportService.setParameterMap(params);
		misReportService.generateReport();
		return null;
	}	
}
