package web.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import service.services.dashboard.ApplicationStatusService;
import service.services.dashboard.ProjectDashboardService;
import service.services.dashboard.reports.DashboardReportService;
import web.Home;

@Controller
public class ApplicationStatus {
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	private String actionStatus=null;
	@RequestMapping(value={"/application-status"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{		
		ModelAndView model = new ModelAndView();		
		model.setViewName("services/dashboard/application-status");		
		return model;
	}
	
	@RequestMapping(value={"/get-application-list-application-status","/get-application-list-registration-suspension"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
			,@RequestParam String applicationName) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStatusService pds = new ApplicationStatusService();
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
	@RequestMapping(value={"/get-application-details-application-status"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStatusService pds = new ApplicationStatusService();
		pds.setAppId(appId.toUpperCase());
		pds.initGetApplicationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(pds.getApplicationList()));
		details.add(mapper.writeValueAsString(pds.getNotifyList()));		
		details.add(mapper.writeValueAsString(pds.getCancellationReasonList()));
		return details;
	}
	@RequestMapping(value={"/cancel-registration-application-status"}, method=RequestMethod.POST)
	public @ResponseBody List<String> cancelRegistrationDetails(@RequestParam String appId,@RequestParam String remark,@RequestParam String requestDate
			,@RequestParam String cancellationType,@RequestParam String cancellationReason) 
			throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStatusService pds = new ApplicationStatusService();
		pds.setAppId(appId.toUpperCase());
		pds.setStatusRemark(remark);	
		pds.setRequestDate(requestDate);
		pds.setCancellationReason(cancellationReason);
		pds.setCancellationType(cancellationType);
		String status=pds.initCancelRegistrationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();		
		details.add(status);
		details.add(mapper.writeValueAsString(pds.getNotifyList()));		
		return details;
	}
	@RequestMapping(value={"/revoke-registration-application-status"}, method=RequestMethod.POST)
	public @ResponseBody List<String> revokeRegistrationDetails(@RequestParam String appId,@RequestParam String remark) 
			throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStatusService pds = new ApplicationStatusService();
		pds.setAppId(appId.toUpperCase());
		pds.setStatusRemark(remark);		
		String status=pds.initRevokeRegistrationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();
		details.add(status);
		details.add(mapper.writeValueAsString(pds.getNotifyList()));		
		return details;
	}
	@RequestMapping(value={"/cancel-report-application-status"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView  initGenerateSignedPdf(@RequestParam String appId,HttpServletResponse response,@RequestParam String remark,@RequestParam String requestDate
			,@RequestParam String cancellationType,@RequestParam String cancellationReason) throws Exception{		
		logger.debug("execute() is executed ");		
		ApplicationStatusService pds=new ApplicationStatusService();
		pds.setAppId(appId);
		pds.setStatusRemark(remark);	
		pds.setRequestDate(requestDate);
		pds.setCancellationReason(cancellationReason);
		pds.setCancellationType(cancellationType);
		pds.initCancelRegistrationDetails();
		pds.GetCancelReport(response);		
		return null; 
	}
	@RequestMapping(value={"/download-cancel-report-application-status"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView  initGenerateSignedPdf(@RequestParam String appId,HttpServletResponse response) throws Exception{		
		logger.debug("execute() is executed ");		
		ApplicationStatusService pds=new ApplicationStatusService();
		pds.setAppId(appId);
		pds.GetCancelReport(response);		
		return null; 
	}
}
