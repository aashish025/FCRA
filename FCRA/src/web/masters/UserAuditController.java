package web.masters;

import java.util.Date;
import java.util.List;

import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.UserAuditServices;
import utilities.KVPair;
import web.Home;

	
	@Controller
	public class UserAuditController {
		private final Logger logger = LoggerFactory.getLogger(Home.class);
		
		@RequestMapping(value={"/user-audit"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");			
			UserAuditServices audit=new UserAuditServices();
			audit.execute();
			List<KVPair<String, String>> auditList = audit.getUserAuditList();
			ModelAndView model = new ModelAndView();	
			model.addObject("userAuditList", auditList);
			model.setViewName("masters/UserAudit"); 
			return model;
		}
		
		@RequestMapping(value={"/details-user-audit"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<models.master.UserAudit> pullDistrictList(@RequestParam String userAudit, @RequestParam String fromDate, @RequestParam String toDate, @RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			UserAuditServices usraudit= new UserAuditServices();
			usraudit.setUserAudit(userAudit);
			usraudit.setFromDate(fromDate);
			usraudit.setToDate(toDate);
			usraudit.setPageNum(pageNum);
			usraudit.setRecordsPerPage(recordsPerPage);
			usraudit.setSortColumn(sortColumn);
			usraudit.setSortOrder(sortOrder);
			usraudit.submitUserAuditList();
			ListPager<models.master.UserAudit> subListPager = new ListPager<models.master.UserAudit>();
			subListPager.setList(usraudit.getAuditList());
			subListPager.setTotalRecords(usraudit.getTotalRecords()); 
			return subListPager;
		}
		

}
