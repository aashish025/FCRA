package web.reports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import models.reports.BlueFlaggedRcns;
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

import service.masters.RedFlagCategoryServices;
import service.reports.BlueFlaggedRcnsService;
import service.reports.MISReportService;
import service.reports.RedFlaggedRcnsService;
import service.reports.RegistrationTrackingService;
import utilities.ValidationException;
import web.Home;
import web.TokenController;


	@Controller
	public class BlueFlaggedRcnsController extends TokenController {
		private final Logger logger = LoggerFactory.getLogger(Home.class);
		private final String tokenKey = "blue-flagged-rcn";
			
		@RequestMapping(value={"/blue-flagged-rcn"}, method=RequestMethod.GET)
		public ModelAndView submit() throws Exception{
			logger.debug("execute() is executed ");	
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();          
			ServletContext context = attr.getRequest().getSession().getServletContext();
			String version = context.getInitParameter("version");
			if(ESAPI.validator().isValidInput("version", version, "version", 6, false) == false){
				throw new ValidationException("version- Invalid entry. Only  numbers and . allowed (6 characters max)");
			}
			BlueFlaggedRcnsService blueflag =new BlueFlaggedRcnsService();
			blueflag.initBlueFlagRcnAction();;	
			ModelAndView model = new ModelAndView();	
			int roleIdAddGrantBlue=0;
			int tempblue=blueflag.getDeleteblueId();
			if(tempblue==14){
				roleIdAddGrantBlue=14;
			} 
			model.addObject("roleIddeleteGrantBlue",roleIdAddGrantBlue);
			model.addObject("requestToken", tokenGenerated);
			model.addObject("stateList", blueflag.getStateList());
			model.addObject("version", version);
			model.setViewName("reports/blue-flagged-rcn"); 
			return model;
		}
		@RequestMapping(value={"/get-blue-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<BlueFlaggedRcns> pullList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			BlueFlaggedRcnsService blueflag =new BlueFlaggedRcnsService();
			blueflag.setPageNum(pageNum);
			blueflag.setRecordsPerPage(recordsPerPage);
			blueflag.setSortColumn(sortColumn);
			blueflag.setSortOrder(sortOrder);
			blueflag.initializeBlueFlaggedList();
			ListPager<BlueFlaggedRcns> blueflagListPager = new ListPager<BlueFlaggedRcns>();
			blueflagListPager.setList(blueflag.getBlueflaggedList());
			blueflagListPager.setTotalRecords(blueflag.getTotalRecords()); 
			return blueflagListPager;
		}
		@RequestMapping(value={"/get-annual-blue-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<BlueFlaggedRcns> pullList1(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			BlueFlaggedRcnsService blueflag =new BlueFlaggedRcnsService();
			blueflag.setPageNum(pageNum);
			blueflag.setRecordsPerPage(recordsPerPage);
			blueflag.setSortColumn(sortColumn);
			blueflag.setSortOrder(sortOrder);
			blueflag.initializeAnnualBlueFlaggedList();
			ListPager<BlueFlaggedRcns> blueflagListPager = new ListPager<BlueFlaggedRcns>();
			blueflagListPager.setList(blueflag.getAnnualblueflaggedList());
			blueflagListPager.setTotalRecords(blueflag.getTotalRecords()); 
			return blueflagListPager;
		}
		//controller
		@RequestMapping(value={"/print-blue-flagged-rcn"}, method=RequestMethod.POST)
			public ModelAndView downloadReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
				logger.debug("execute() is executed ");		
				MISReportService misReportService = new MISReportService();		
				misReportService.setParameterMap(params);
				misReportService.generateReport();
				return null;
			}	
		
		@RequestMapping(value={"/get-application-list-blue-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<AbstractRequest> initApplicationList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
				,@RequestParam String applicationName) throws Exception{
			logger.debug("execute() is executed ");		
			BlueFlaggedRcnsService blueflag =new BlueFlaggedRcnsService();
			blueflag.setPageNum(pageNum);
			blueflag.setRecordsPerPage(recordsPerPage);
			blueflag.setSortColumn(sortColumn);
			blueflag.setSortOrder(sortOrder);	
			blueflag.setAppId(applicationId.toUpperCase());
			blueflag.setAppName(applicationName.toUpperCase());
			blueflag.initApplicationListDetails();
			ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
			ar.setList(blueflag.getApplicationList());
			ar.setTotalRecords(blueflag.getTotalRecords()); 
			return ar;
		}
		@RequestMapping(value={"/get-advance-application-list-blue-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
				,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
			logger.debug("execute() is executed ");		
			BlueFlaggedRcnsService blueflag =new BlueFlaggedRcnsService();
			blueflag.setPageNum(pageNum);
			blueflag.setRecordsPerPage(recordsPerPage);
			blueflag.setSortColumn(sortColumn);
			blueflag.setSortOrder(sortOrder);		
			blueflag.setAppName(applicationName.toUpperCase());
			blueflag.setFunctionaryName(functionaryName.toUpperCase());
			blueflag.setState(state);
			blueflag.setDistrict(district);
			blueflag.initAdvanceApplicationListDetails();
			ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
			ar.setList(blueflag.getApplicationList());
			ar.setTotalRecords(blueflag.getTotalRecords()); 
			return ar;
		}
		@RequestMapping(value={"/remove-blue-flagged-rcn"}, method=RequestMethod.POST)
		public @ResponseBody List<String> removeRedFlag(@RequestParam String appId,@RequestParam String remark) throws Exception{
			logger.debug("execute() is executed ");		
			BlueFlaggedRcnsService pds =new BlueFlaggedRcnsService();
			pds.setAppId(appId.toUpperCase());
			pds.setRemark(remark);
			String actionStatus=pds.initRemoveFromBlueFlagList();
			ObjectMapper mapper = new ObjectMapper();
			List<String> details=new ArrayList<String>();		
			details.add(mapper.writeValueAsString(pds.getNotifyList()));		
			details.add(actionStatus);
			return details;
		}
		
		@RequestMapping(value={"/get-application-details-blue-flagged-rcn"}, method=RequestMethod.POST)
		public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
			logger.debug("execute() is executed ");		
			BlueFlaggedRcnsService pds =new BlueFlaggedRcnsService();
			pds.setAppId(appId.toUpperCase());
			pds.initGetApplicationDetails();
			ObjectMapper mapper = new ObjectMapper();
			List<String> details=new ArrayList<String>();	
			details.add(mapper.writeValueAsString(pds.getApplicationList()));
			details.add(mapper.writeValueAsString(pds.getNotifyList()));		
			details.add(mapper.writeValueAsString(pds.getReturnList()));		
			details.add(mapper.writeValueAsString(pds.getCommitteeMembers()));
			details.add(mapper.writeValueAsString(pds.getHistoryList()));		
			details.add(mapper.writeValueAsString(pds.getRegCancDetails()));
			details.add(mapper.writeValueAsString(pds.getSmsList()));
			details.add(mapper.writeValueAsString(pds.getMailList()));
			details.add(mapper.writeValueAsString(pds.getAnnualStatusList()));
			details.add(mapper.writeValueAsString(pds.getBlueFlagDetailsList()));
			details.add(mapper.writeValueAsString(pds.getBlueFlag()));
			details.add(mapper.writeValueAsString(pds.getBlueRcnexist()));
			
			return details;
		}


}
