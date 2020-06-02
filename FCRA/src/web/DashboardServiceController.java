package web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.reports.PendencyReport;
import models.services.ListPager;
import service.reports.MISReportService;
import service.services.HomeNotificationDetailsService;

@Controller
public class DashboardServiceController {
private final Logger logger = LoggerFactory.getLogger(DashboardServiceController.class);	
	
	@RequestMapping(value={"/dash-board-service"}, method=RequestMethod.GET)
	public ModelAndView execute() throws Exception{
		logger.debug("execute() is executed ");		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("reports/dash-board-service");		
		return model;
	}

	@RequestMapping(value={"/status-dash-board-service"}, method=RequestMethod.GET)
	public @ResponseBody List<String> dashboardServiceStatus(HttpServletRequest req) throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();		
		nds.pullDashboard(req);		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getDashboardServiceStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getApplicationOrganisationStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getRegistrationDesignationStatistics()));
		//System.out.println("reg"+notificationList);
		return notificationList;
		
	}
	@RequestMapping(value={"/PriorPermission-dash-board-service"}, method=RequestMethod.GET)
	public @ResponseBody List<String> dashboardPriorPermissionStatus(HttpServletRequest req) throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();		
		nds.pullPriorpermission(req);		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getPeriorPermissionOrganisationStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getPriorPermissionDesignationStatistics()));
		return notificationList;
		
	}
	@RequestMapping(value={"/Renewal-dash-board-service"}, method=RequestMethod.GET)
	public @ResponseBody List<String> dashboardRenewalStatus(HttpServletRequest req) throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();		
		nds.pullRenewal(req);		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getRenewalOrganisationStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getRenewalDesignationStatistics()));
		return notificationList;
		
	}
	@RequestMapping(value={"/Hospitality-dash-board-service"}, method=RequestMethod.GET)
	public @ResponseBody List<String> dashboardHospitalityStatus(HttpServletRequest req) throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();		
		nds.pullHospitality(req);		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getHospitalityOrganisationStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getHospitalityDesignationStatistics()));
	//	System.out.println(notificationList);
		return notificationList;
		
	}
	@RequestMapping(value={"/show-pendency-reg-dash-board-service"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<PendencyReport> initPendencyReport(@RequestParam MultiValueMap<String, String> params,HttpServletRequest req ) throws Exception{
		logger.debug("execute() is executed ");	
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();	
		nds.setParameterMap(params);
		nds.pendencyreportdegwise(req);
		ListPager<PendencyReport> ReportList = new ListPager<PendencyReport>();
		ReportList.setList(nds.getPendencyReportDegwise());
		ReportList.setTotalRecords(nds.getPendencyCountReportDegwise());
		//System.out.println("ReportList============"+ReportList);
		return ReportList;
	}
}
