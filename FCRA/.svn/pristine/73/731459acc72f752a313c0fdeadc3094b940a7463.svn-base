package web.reports;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;




import com.fasterxml.jackson.databind.ObjectMapper;

import service.reports.AdminDashBoardService;

import web.Home;
@Controller
	public class AdminDashBoardController {
		private final Logger logger = LoggerFactory.getLogger(Home.class);	
		private String actionStatus=null;
		@RequestMapping(value={"/administration-dashboard"}, method=RequestMethod.GET)
		public ModelAndView submit() throws Exception{		
		//	AdminDashBoardService pds = new AdminDashBoardService();
			//pds.initDonorAction();	
			ModelAndView model = new ModelAndView();
			//model.addObject("stateList", pds.getStateList());
			//model.addObject("countryList",pds.getCountryList());
			model.setViewName("reports/admin-dashboard");		
			return model;
		}
		@RequestMapping(value={"/pending-bar-chart-administration-dashboard"}, method=RequestMethod.POST)
		public @ResponseBody List<String> getApplicationDetails() throws Exception{
			logger.debug("execute() is executed ");		
			AdminDashBoardService pds=new AdminDashBoardService();
			//pds.setAppId(appId.toUpperCase());
			pds.initGetPendingDetails();
			ObjectMapper mapper = new ObjectMapper();
			List<String> details=new ArrayList<String>();	
			//details.add(mapper.writeValueAsString(pds.getNotifyList()));		
			details.add(mapper.writeValueAsString(pds.getReturnList()));		
			
			return details;
		}
}

