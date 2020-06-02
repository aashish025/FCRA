package web.reports;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import models.reports.ExemptionRenewalBlocking;
import models.services.ListPager;
import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
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



















import service.reports.ExemptionRenewalBlockingServices;
import service.reports.OldRegistrationEntryService;
import service.reports.RedFlagDonorsServices;
import service.reports.RegistrationTrackingService;
//import service.reports.RedFlagAssociationsServices;
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;


@Controller
public class ExemptionRenewalController extends TokenController {
	
	
private final Logger logger = LoggerFactory.getLogger(ExemptionRenewalController.class);
private final String tokenKey = "exemption-renewal-blocking";
DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


@RequestMapping(value={"/exemption-renewal-blocking"}, method=RequestMethod.GET)
public ModelAndView submit() throws Exception{					
			ModelAndView model = new ModelAndView();
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ExemptionRenewalBlockingServices exemptionRenewalBlockingServices =new ExemptionRenewalBlockingServices();
			exemptionRenewalBlockingServices.initRegistrationTrackingAction();
			exemptionRenewalBlockingServices.execute();
			model.addObject("stateList", exemptionRenewalBlockingServices.getStateList());	
			model.addObject("requestToken", tokenGenerated);
			model.setViewName("reports/exemption-renewal-blocking");		
			return model;			
			
}

@RequestMapping(value={"/get-district-exemption-renewal-blocking"}, method=RequestMethod.POST)
public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionRenewalBlockingServices exemptionRenewalBlockingServices =new ExemptionRenewalBlockingServices();		
	exemptionRenewalBlockingServices.setState(state);		
	exemptionRenewalBlockingServices.initDistrict();		 
	return exemptionRenewalBlockingServices.getDistrictList();
}


@RequestMapping(value={"/get-advance-rcn-list-exemption-renewal-blocking"}, method=RequestMethod.GET)
public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
		,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
	logger.debug("execute() is executed ");		

	ExemptionRenewalBlockingServices pds =new ExemptionRenewalBlockingServices();
	pds.setPageNum(pageNum);
	pds.setRecordsPerPage(recordsPerPage);
	pds.setSortColumn(sortColumn);
	pds.setSortOrder(sortOrder);		
	pds.setAppName(applicationName.toUpperCase());
	pds.setFunctionaryName(functionaryName.toUpperCase());
	pds.setState(state);
	pds.setDistrict(district);
	pds.initAdvanceApplicationListDetails();
	ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
	ar.setList(pds.getApplicationList());
	ar.setTotalRecords(pds.getTotalRecords()); 
	return ar;
}

@RequestMapping(value={"/get-list-exemption-renewal-blocking"}, method=RequestMethod.GET)
public @ResponseBody ListPager<ExemptionRenewalBlocking> initApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionRenewalBlockingServices exemptionRenewalBlockingServices =new ExemptionRenewalBlockingServices();
	exemptionRenewalBlockingServices.setPageNum(pageNum);
	exemptionRenewalBlockingServices.setRecordsPerPage(recordsPerPage);
	exemptionRenewalBlockingServices.setSortColumn(sortColumn);
	exemptionRenewalBlockingServices.setSortOrder(sortOrder);
	exemptionRenewalBlockingServices.initExemptionListCases();
	ListPager<ExemptionRenewalBlocking> erb = new ListPager<ExemptionRenewalBlocking>();
	erb.setList(exemptionRenewalBlockingServices.getRcnList());
	erb.setTotalRecords(exemptionRenewalBlockingServices.getTotalRecords()); 
	return erb;
}

@RequestMapping(value={"/get-application-list-exemption-renewal-blocking"}, method=RequestMethod.GET)
public @ResponseBody List<String> initApplicationList(@RequestParam String applicationId) throws Exception{
	logger.debug("execute() is executed ");		
	RegistrationTrackingService pds=new RegistrationTrackingService();	
	pds.setAppId(applicationId.toUpperCase());
	pds.initApplicationListDetails1();
	ObjectMapper mapper = new ObjectMapper();
	List<String> list=new ArrayList<String>();
	list.add(mapper.writeValueAsString(pds.getRcnDetail()));	
    list.add(mapper.writeValueAsString(pds.getNotifyList()));
	return list;
}


@RequestMapping(value={"/get-rcn-details-exemption-renewal-blocking"}, method=RequestMethod.GET)
public @ResponseBody List<String> getRCnDetails(@RequestParam String applicationId) throws Exception{
	logger.debug("execute() is executed ");		
	RegistrationTrackingService pds=new RegistrationTrackingService();
	pds.setAppId(applicationId.toUpperCase());
	pds.initGetApplicationDetailsExemption();
	ObjectMapper mapper = new ObjectMapper();
	List<String> details=new ArrayList<String>();	
	details.add(mapper.writeValueAsString(pds.getApplicationList()));
	details.add(mapper.writeValueAsString(pds.getNotifyList()));		
	return details;
}




@RequestMapping(value={"/add-rcn-details-exemption-renewal-blocking"}, method=RequestMethod.GET)
public  @ResponseBody List<String> addExemptionCases(HttpServletRequest request, @RequestParam String applicationId,
		@RequestParam String exemptionDays,@RequestParam String remarks,@RequestParam  String requestToken) throws Exception{ /**/
	logger.debug("execute() is executed ");	
	ExemptionRenewalBlockingServices exemptionRenewalBlockingServices =new ExemptionRenewalBlockingServices();
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
    String token=null;
	if(result){	
	exemptionRenewalBlockingServices.setApplicationId(applicationId);
	exemptionRenewalBlockingServices.setExemptionDays(exemptionDays);
	exemptionRenewalBlockingServices.setRemarks(remarks);
	exemptionRenewalBlockingServices.initAddExemptionRenewalCase();
	token = getSessionToken(tokenKey);
	}
	else {
		exemptionRenewalBlockingServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				
	notificationList.add(mapper.writeValueAsString(exemptionRenewalBlockingServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}

}
