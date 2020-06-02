package web.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.services.dashboard.RegistrartionSuspensionService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

@Controller
public class RegistrationSuspensionController extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	private String tokenKey="registration-suspension";
	private String actionStatus=null;
	@RequestMapping(value={"/registration-suspension"}, method=RequestMethod.GET)
	
	public ModelAndView submit() throws Exception{		
		ModelAndView model = new ModelAndView();		
		String tokenGenerated = generateAndSaveToken(tokenKey);
		model.setViewName("services/dashboard/registration-suspension");	
		model.addObject("requestToken", tokenGenerated);
		return model;
	}
	
	@RequestMapping(value={"/get-application-details-registration-suspension"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrartionSuspensionService pds = new RegistrartionSuspensionService();
		pds.setAppId(appId.toUpperCase());
		pds.initGetApplicationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(pds.getApplicationList()));
		details.add(mapper.writeValueAsString(pds.getNotifyList()));		
	//	details.add(mapper.writeValueAsString(pds.getCancellationReasonList()));
		return details;
	}
	@RequestMapping(value={"/cancel-registration-registration-suspension"}, method=RequestMethod.POST)
	public @ResponseBody List<String> cancelRegistrationDetails(@RequestParam String appId,@RequestParam String remark,@RequestParam String requestToken)
			throws Exception{
		logger.debug("execute() is executed ");		
		RegistrartionSuspensionService pds = new RegistrartionSuspensionService();
		pds.setAppId(appId.toUpperCase());
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
	    String status=null;
		
		if(result){
		pds.setStatusRemark(remark);
		status=pds.initCancelRegistrationDetails();
		token = getSessionToken(tokenKey);
		}
		else {
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();		
		details.add(status);
		details.add(mapper.writeValueAsString(pds.getNotifyList()));	
		details.add(mapper.writeValueAsString(new List1(token)));
		return details;
	}
	@RequestMapping(value={"/revoke-registration-registration-suspension"}, method=RequestMethod.POST)
	public @ResponseBody List<String> revokeRegistrationDetails(@RequestParam String appId,@RequestParam String remark,@RequestParam String requestToken) 
			throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		RegistrartionSuspensionService pds = new RegistrartionSuspensionService();
		  String token=null;
		   String status=null;
		if(result){
		pds.setAppId(appId.toUpperCase());
		pds.setStatusRemark(remark);		
		status=pds.initRevokeRegistrationDetails();
		token = getSessionToken(tokenKey);
		}
		else {
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();
		details.add(status);
		details.add(mapper.writeValueAsString(pds.getNotifyList()));	
		details.add(mapper.writeValueAsString(new List1(token)));
		return details;
	}
	@RequestMapping(value={"/download-cancel-report-registration-suspension"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView  initGenerateSignedPdf(@RequestParam String appId,HttpServletResponse response) throws Exception{		
		logger.debug("execute() is executed ");		
		RegistrartionSuspensionService pds = new RegistrartionSuspensionService();
		pds.setAppId(appId);
		pds.GetCancelReport(response);		
		return null; 
		}
	
	/*@RequestMapping(value={"/cancel-report-registration-suspension"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView  initGenerateSignedPdf(@RequestParam String appId,HttpServletResponse response,@RequestParam String remark) throws Exception{		
		logger.debug("execute() is executed ");		
		RegistrartionSuspensionService pds = new RegistrartionSuspensionService();
		pds.setAppId(appId);
		pds.setStatusRemark(remark);	
		pds.initCancelRegistrationDetails();
		pds.GetCancelReport(response);		
		return null; 
	}
*/
}
