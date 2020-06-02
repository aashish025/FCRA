package web.services;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import service.services.ChangeAssociationService;
import service.services.ChangeRegistrationValidityService;
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;
@Controller
public class ChangeRegistrationValidityController extends TokenController {		
	private final Logger logger = LoggerFactory.getLogger(ChangeRegistrationValidityController.class);
	private final String tokenKey = "change-registration-validity";
	@RequestMapping(value={"/change-registration-validity"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{					
				ModelAndView model = new ModelAndView();
				String tokenGenerated = generateAndSaveToken(tokenKey);
				ChangeRegistrationValidityService registrationValidity =new ChangeRegistrationValidityService();
				registrationValidity.initRegistrationTrackingAction();
				model.addObject("stateList", registrationValidity.getStateList());
				model.addObject("associationType", registrationValidity.getAssoType());
				model.addObject("requestToken", tokenGenerated);
				model.setViewName("services/change-registration-validity");		
				return model;			
				
	}
	@RequestMapping(value={"/district-change-registration-validity"}, method=RequestMethod.POST)
	public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidity =new ChangeRegistrationValidityService();	
		registrationValidity.setState(state);		
		registrationValidity.initDistrict();		 
		return registrationValidity.getDistrictList();
	}
	@RequestMapping(value={"/advance-rcn-list-change-registration-validity"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
			,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidity =new ChangeRegistrationValidityService();
		registrationValidity.setPageNum(pageNum);
		registrationValidity.setRecordsPerPage(recordsPerPage);
		registrationValidity.setSortColumn(sortColumn);
		registrationValidity.setSortOrder(sortOrder);		
		registrationValidity.setAppName(applicationName.toUpperCase());
		registrationValidity.setFunctionaryName(functionaryName.toUpperCase());
		registrationValidity.setState(state);
		registrationValidity.setDistrict(district);
		registrationValidity.initAdvanceApplicationListDetails();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(registrationValidity.getApplicationList());
		ar.setTotalRecords(registrationValidity.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/rcn-details-change-registration-validity"}, method=RequestMethod.GET)
	public @ResponseBody List<String> getRCnDetails(@RequestParam String applicationId) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidity =new ChangeRegistrationValidityService();
		registrationValidity.setAppId(applicationId.toUpperCase());
		registrationValidity.initGetApplicationChangeRegistration();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(registrationValidity.getApplicationList()));
		return details;
	}
	@RequestMapping(value={"/application-list-change-registration-validity"}, method=RequestMethod.GET)
	public @ResponseBody List<String> initApplicationList(@RequestParam String applicationId) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidity =new ChangeRegistrationValidityService();
		registrationValidity.setAppId(applicationId.toUpperCase());
		registrationValidity.initApplicationListDetails1();
		ObjectMapper mapper = new ObjectMapper();
		List<String> list=new ArrayList<String>();
		list.add(mapper.writeValueAsString(registrationValidity.getRcnDetail()));	
	    list.add(mapper.writeValueAsString(registrationValidity.getNotifyList()));
		return list;
	}

	/*@RequestMapping(value={"/add-change-registration-validity"}, method=RequestMethod.GET)
	public  @ResponseBody List<String> addExemptionCases(HttpServletRequest request, @RequestParam String applicationId,
			@RequestParam String assocType,@RequestParam  String requestToken) throws Exception{ 
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidity =new ChangeRegistrationValidityService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){		
		registrationValidity.setApplicationId(applicationId);
		registrationValidity.setAssocType(assocType);
		registrationValidity.submitRegistrationValidity();
		token = getSessionToken(tokenKey);
		}
		else {
			registrationValidity.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				
		notificationList.add(mapper.writeValueAsString(registrationValidity.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		
		return notificationList;
	}*/
	
	@RequestMapping(value={"/submit-change-validity-change-registration-validity"}, method=RequestMethod.POST)
	public  @ResponseBody List<String> submitChangeValidity(HttpServletRequest request, @RequestParam String rcn,
			@RequestParam String validityFrom, @RequestParam String validityUpTo, @RequestParam String noOfYrs, @RequestParam  String requestToken) throws Exception{ 
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidityService =new ChangeRegistrationValidityService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){		
			registrationValidityService.setApplicationId(rcn);
			registrationValidityService.submitRegistrationValidity(validityFrom, validityUpTo, noOfYrs);
		token = getSessionToken(tokenKey);
		}
		else {
			registrationValidityService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				
		notificationList.add(mapper.writeValueAsString(registrationValidityService.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		
		return notificationList;
	}

	@RequestMapping(value={"/date-validity-change-registration-validity"}, method=RequestMethod.POST)
	public  @ResponseBody List<String> fromDateValidity(HttpServletRequest request,	@RequestParam String validityFrom, @RequestParam String noOfYrs) throws Exception{ 
		logger.debug("execute() is executed ");		
		ChangeRegistrationValidityService registrationValidityService =new ChangeRegistrationValidityService();
		//Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    //String token=null;
		//if(result){		
			registrationValidityService.currentDateString(validityFrom, noOfYrs);
		//token = getSessionToken(tokenKey);
		//}
		//else {
		//	registrationValidityService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		//}
				
		//notificationList.add(mapper.writeValueAsString(registrationValidityService.getNotifyList()));
		//notificationList.add(mapper.writeValueAsString(new List1(token)));
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				
			notificationList.add(mapper.writeValueAsString(registrationValidityService.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(registrationValidityService.getPresentDateString()));
		 return notificationList;
	}
}
