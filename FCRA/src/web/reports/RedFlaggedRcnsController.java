package web.reports;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import models.master.RedFlagCategory;
import models.reports.RedFlaggedRcns;
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
import service.reports.MISReportService;
import service.reports.RedFlaggedRcnsService;
import service.reports.RegistrationTrackingService;
import utilities.ValidationException;
import web.Home;
import web.TokenController;


	@Controller
	public class RedFlaggedRcnsController extends TokenController {
		private final Logger logger = LoggerFactory.getLogger(Home.class);
		private final String tokenKey = "red-flagged-rcn";
			
		@RequestMapping(value={"/red-flagged-rcn"}, method=RequestMethod.GET)
		public ModelAndView submit() throws Exception{
			logger.debug("execute() is executed ");	
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();          
			ServletContext context = attr.getRequest().getSession().getServletContext();
			String version = context.getInitParameter("version");
			if(ESAPI.validator().isValidInput("version", version, "version", 6, false) == false){
				throw new ValidationException("version- Invalid entry. Only  numbers and . allowed (6 characters max)");
			}
			RedFlaggedRcnsService redflag =new RedFlaggedRcnsService();
			redflag.initRedFlagRcnAction();	
			ModelAndView model = new ModelAndView();	
			int roleIdAddGrantYellow=0;int roleIdAddGrantRed=0;
			int tempred=redflag.getRedroleId();
			int tempyellow=redflag.getYellowroleId();
			if(tempred==16){
				roleIdAddGrantRed=16;
			} 
			if(tempyellow==15){
				roleIdAddGrantYellow=15;
				}
			int roleIddeleteGrantYellow=0;int roleIddeleteGrantRed=0;
			int deletered=redflag.getDeleteredroleId();
			int deleteyellow=redflag.getDeleteyellowId();
			if(deletered==17){
				roleIddeleteGrantRed=17;
				
			}
			if(deleteyellow==18){
				roleIddeleteGrantYellow=18;
				
			}
			
			model.addObject("roleIdAddGrantYellow",roleIdAddGrantYellow);
			model.addObject("roleIdAddGrantRed",roleIdAddGrantRed);
			model.addObject("roleIddeleteGrantRed",roleIddeleteGrantRed);
			model.addObject("roleIddeleteGrantYellow",roleIddeleteGrantYellow);
			model.addObject("requestToken", tokenGenerated);
			model.addObject("stateList", redflag.getStateList());
			model.addObject("version", version);
			model.setViewName("reports/redflaggedrcns"); 
			return model;
		}
		@RequestMapping(value={"/get-red-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<RedFlaggedRcns> pullList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService redflag =new RedFlaggedRcnsService();
			redflag.setPageNum(pageNum);
			redflag.setRecordsPerPage(recordsPerPage);
			redflag.setSortColumn(sortColumn);
			redflag.setSortOrder(sortOrder);
			redflag.initializeRedFlaggedList();
			ListPager<RedFlaggedRcns> redflagListPager = new ListPager<RedFlaggedRcns>();
			redflagListPager.setList(redflag.getRedflaggedList());
			redflagListPager.setTotalRecords(redflag.getTotalRecords()); 
			return redflagListPager;
		}
		@RequestMapping(value={"/get-annual-red-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<RedFlaggedRcns> pullList1(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService redflag =new RedFlaggedRcnsService();
			redflag.setPageNum(pageNum);
			redflag.setRecordsPerPage(recordsPerPage);
			redflag.setSortColumn(sortColumn);
			redflag.setSortOrder(sortOrder);
			redflag.initializeAnnualRedFlaggedList();
			ListPager<RedFlaggedRcns> redflagListPager = new ListPager<RedFlaggedRcns>();
			redflagListPager.setList(redflag.getAnnualredflaggedList());
			redflagListPager.setTotalRecords(redflag.getTotalRecords()); 
			return redflagListPager;
		}
		//controller
		@RequestMapping(value={"/print-red-flagged-rcn"}, method=RequestMethod.POST)
			public ModelAndView downloadReport(@RequestParam MultiValueMap<String, String> params) throws Exception{
				logger.debug("execute() is executed ");		
				MISReportService misReportService = new MISReportService();		
				misReportService.setParameterMap(params);
				misReportService.generateReport();
				return null;
			}	
		
		@RequestMapping(value={"/get-application-list-red-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<AbstractRequest> initApplicationList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
				,@RequestParam String applicationName) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService redflag =new RedFlaggedRcnsService();
			redflag.setPageNum(pageNum);
			redflag.setRecordsPerPage(recordsPerPage);
			redflag.setSortColumn(sortColumn);
			redflag.setSortOrder(sortOrder);	
			redflag.setAppId(applicationId.toUpperCase());
			redflag.setAppName(applicationName.toUpperCase());
			redflag.initApplicationListDetails();
			ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
			ar.setList(redflag.getApplicationList());
			ar.setTotalRecords(redflag.getTotalRecords()); 
			return ar;
		}
		@RequestMapping(value={"/get-advance-application-list-red-flagged-rcn"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
				,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService redflag =new RedFlaggedRcnsService();
			redflag.setPageNum(pageNum);
			redflag.setRecordsPerPage(recordsPerPage);
			redflag.setSortColumn(sortColumn);
			redflag.setSortOrder(sortOrder);		
			redflag.setAppName(applicationName.toUpperCase());
			redflag.setFunctionaryName(functionaryName.toUpperCase());
			redflag.setState(state);
			redflag.setDistrict(district);
			redflag.initAdvanceApplicationListDetails();
			ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
			ar.setList(redflag.getApplicationList());
			ar.setTotalRecords(redflag.getTotalRecords()); 
			return ar;
		}
		@RequestMapping(value={"/remove-red-yellow-red-flagged-rcn"}, method=RequestMethod.POST)
		public @ResponseBody List<String> removeRedFlag(@RequestParam String appId,@RequestParam String remark
				,@RequestParam String office,@RequestParam String order,@RequestParam String date,@RequestParam String flagdelete) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService pds=new RedFlaggedRcnsService();
			pds.setAppId(appId.toUpperCase());
			pds.setRemark(remark);
			pds.setOriginatorOffice(office);
			pds.setOrderNumber(order);
			pds.setOrderDate(date);
			pds.setFlagdelete(flagdelete);
			String actionStatus=pds.initRemoveFromRedFlagList();
			ObjectMapper mapper = new ObjectMapper();
			List<String> details=new ArrayList<String>();		
			details.add(mapper.writeValueAsString(pds.getNotifyList()));		
			details.add(actionStatus);
			return details;
		}
		@RequestMapping(value={"/add-red-flagged-rcn"}, method=RequestMethod.POST)
		public @ResponseBody List<String> addRedFlag(@RequestParam String appId,@RequestParam String remark,@RequestParam String category
				,@RequestParam String office,@RequestParam String order,@RequestParam String date, @RequestParam String flagvalue) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService pds=new RedFlaggedRcnsService();
			pds.setAppId(appId.toUpperCase());
			pds.setRemark(remark);
			pds.setRedFlagCategory(category);
			pds.setOriginatorOffice(office);
			pds.setOrderNumber(order);
			pds.setOrderDate(date);
			pds.setFlagvalue(flagvalue);
			String actionStatus=pds.initAddToRedFlagList();
			ObjectMapper mapper = new ObjectMapper();
			List<String> details=new ArrayList<String>();		
			details.add(mapper.writeValueAsString(pds.getNotifyList()));	
			details.add(actionStatus);
			return details;
		}
		@RequestMapping(value={"/get-application-details-red-flagged-rcn"}, method=RequestMethod.POST)
		public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
			logger.debug("execute() is executed ");		
			RedFlaggedRcnsService pds=new RedFlaggedRcnsService();
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
			details.add(pds.getYellowFlagAddRole());
			details.add(mapper.writeValueAsString(pds.getAnnualStatusList()));
			
			return details;
		}


}
