package web.services;

import java.util.ArrayList;
import java.util.List;

import models.services.ListPager;
import models.services.requests.AbstractRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.services.dashboard.InvestigationAgencyService;
import service.services.dashboard.ProjectDashboardService;
import utilities.lists.List1;
import web.Home;
import web.TokenController;


@Controller
public class InvestigationAgency extends TokenController{
		
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	private final String tokenKey = "investigation-agency-report";
	@RequestMapping(value={"/investigation-agency-report"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{
		InvestigationAgencyService ivs=new InvestigationAgencyService();	
		ivs.initInvestigation();
		ModelAndView model = new ModelAndView();
		model.addObject("officeNameList", ivs.getOfficeNameList());
		model.setViewName("services/dashboard/investigation-agency-report");		
		return model;	
}
	
@RequestMapping(value={"/get-application-list-investigation-agency-report"}, method=RequestMethod.GET)
public @ResponseBody ListPager<AbstractRequest> initApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
		,@RequestParam String applicationName) throws Exception{
	logger.debug("execute() is executed ");		
	InvestigationAgencyService pds = new InvestigationAgencyService();
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


@RequestMapping(value={"/get-application-details-investigation-agency-report"}, method=RequestMethod.POST)
public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
	logger.debug("execute() is executed ");		
	InvestigationAgencyService pds = new InvestigationAgencyService();
	pds.setAppId(appId.toUpperCase());
	pds.initGetApplicationDetails();
	ObjectMapper mapper = new ObjectMapper();
	List<String> details=new ArrayList<String>();	
	details.add(mapper.writeValueAsString(pds.getApplicationList()));
	details.add(mapper.writeValueAsString(pds.getOfficeStatusList()));
	details.add(pds.getFinalStatus());
	return details;
}


@RequestMapping(value={"/submit-investigation-agency-report"}, method=RequestMethod.GET)
public @ResponseBody List<String> submitInvestigationReport(@RequestParam String appId,String investAgencyList,String reportNo,String reportDate) throws Exception{
	logger.debug("execute() is executed ");		
	InvestigationAgencyService pds = new InvestigationAgencyService();
	pds.setAppId(appId.toUpperCase());
	pds.setReportFrom(investAgencyList);
	pds.setReportNumber(reportNo);
	pds.setReportDate(reportDate);	
	String token=null;
	pds.initSubmitInvestigationReport();
	token = getSessionToken(tokenKey);
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();	
	notificationList.add(mapper.writeValueAsString(pds.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}

}