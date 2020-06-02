package web.services;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.services.ApplicationStatusManagementServices;
import web.Home;

@Controller
public class ApplicationStatusManagementController {
	
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	
	@RequestMapping(value={"/status-management"}, method=RequestMethod.GET)
	
	public ModelAndView submit1() throws Exception{		
		ModelAndView model = new ModelAndView();		
		model.setViewName("services/application-status-management");		
		return model;
	}
	
	
	@RequestMapping(value={"/get-application-status-management"}, method=RequestMethod.GET)
	public @ResponseBody List<String> initApplicationList(@RequestParam String applicationId ) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStatusManagementServices pds = new ApplicationStatusManagementServices();
		pds.initApplicationListDetails(applicationId);
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(pds.getApplicationManagementList()));
		details.add(mapper.writeValueAsString(pds.getNotifyList()));
		details.add(mapper.writeValueAsString(pds.getUserList()));
		return details;
	}
	
	@RequestMapping(value={"/reopen-application-status-management"}, method=RequestMethod.POST)
	public @ResponseBody java.util.List<String> reopen(@RequestParam String applicationId,@RequestParam String serviceCode, @RequestParam String remark, @RequestParam String userId) throws Exception{
		logger.debug("execute() is executed ");	
		ApplicationStatusManagementServices psm = new ApplicationStatusManagementServices();
		String status=psm.initReOpen(applicationId,serviceCode,remark,userId);	
		ObjectMapper mapper = new ObjectMapper();
		java.util.List<String> appDetails=new ArrayList<String>();				 
		appDetails.add(mapper.writeValueAsString(psm.getNotifyList()));	
		appDetails.add(status);
		return appDetails;
	}
	
}
