package web.services;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import models.services.ExemptionAnnualPenalty;
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

import service.services.ExemptionAnnualPenaltyServices;
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;

@Controller
public class ExemptionAnnualPenaltyController extends TokenController {		
private final Logger logger = LoggerFactory.getLogger(ExemptionAnnualPenaltyController.class);
private final String tokenKey = "exemption-annual-return-penalty";
DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
@RequestMapping(value={"/exemption-annual-return-penalty"}, method=RequestMethod.GET)
public ModelAndView submit() throws Exception{					
			ModelAndView model = new ModelAndView();
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();
			exemptionAnnualServices.initRegistrationTrackingAction();
			model.addObject("stateList", exemptionAnnualServices.getStateList());
			model.addObject("requestToken", tokenGenerated);
			model.setViewName("services/exemption-annual-return-penalty");		
			return model;			
			
}
@RequestMapping(value={"/district-exemption-annual-return-penalty"}, method=RequestMethod.POST)
public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();	
	exemptionAnnualServices.setState(state);		
	exemptionAnnualServices.initDistrict();		 
	return exemptionAnnualServices.getDistrictList();
}


@RequestMapping(value={"/advance-rcn-list-exemption-annual-return-penalty"}, method=RequestMethod.GET)
public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
		,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();
	exemptionAnnualServices.setPageNum(pageNum);
	exemptionAnnualServices.setRecordsPerPage(recordsPerPage);
	exemptionAnnualServices.setSortColumn(sortColumn);
	exemptionAnnualServices.setSortOrder(sortOrder);		
	exemptionAnnualServices.setAppName(applicationName.toUpperCase());
	exemptionAnnualServices.setFunctionaryName(functionaryName.toUpperCase());
	exemptionAnnualServices.setState(state);
	exemptionAnnualServices.setDistrict(district);
	exemptionAnnualServices.initAdvanceApplicationListDetails();
	ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
	ar.setList(exemptionAnnualServices.getApplicationList());
	ar.setTotalRecords(exemptionAnnualServices.getTotalRecords()); 
	return ar;
}

@RequestMapping(value={"/table-exemption-annual-return-penalty"}, method=RequestMethod.GET)
public @ResponseBody ListPager<ExemptionAnnualPenalty> initApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();
	exemptionAnnualServices.setPageNum(pageNum);
	exemptionAnnualServices.setRecordsPerPage(recordsPerPage);
	exemptionAnnualServices.setSortColumn(sortColumn);
	exemptionAnnualServices.setSortOrder(sortOrder);
	exemptionAnnualServices.initExemptionListCases();
	ListPager<ExemptionAnnualPenalty> erb = new ListPager<ExemptionAnnualPenalty>();
	erb.setList(exemptionAnnualServices.getRcnList());
	erb.setTotalRecords(exemptionAnnualServices.getTotalRecords()); 
	return erb;
}

@RequestMapping(value={"/application-list-exemption-annual-return-penalty"}, method=RequestMethod.GET)
public @ResponseBody List<String> initApplicationList(@RequestParam String applicationId) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();
	exemptionAnnualServices.setAppId(applicationId.toUpperCase());
	exemptionAnnualServices.initApplicationListDetails1();
	ObjectMapper mapper = new ObjectMapper();
	List<String> list=new ArrayList<String>();
	list.add(mapper.writeValueAsString(exemptionAnnualServices.getRcnDetail()));	
    list.add(mapper.writeValueAsString(exemptionAnnualServices.getNotifyList()));
	return list;
}


@RequestMapping(value={"/rcn-details-exemption-annual-return-penalty"}, method=RequestMethod.GET)
public @ResponseBody List<String> getRCnDetails(@RequestParam String applicationId) throws Exception{
	logger.debug("execute() is executed ");		
	ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();
	exemptionAnnualServices.setAppId(applicationId.toUpperCase());
	exemptionAnnualServices.initGetApplicationDetailsExemption();
	ObjectMapper mapper = new ObjectMapper();
	List<String> details=new ArrayList<String>();	
	details.add(mapper.writeValueAsString(exemptionAnnualServices.getApplicationList()));
	details.add(mapper.writeValueAsString(exemptionAnnualServices.getCheckBlkYear()));
	details.add(mapper.writeValueAsString(exemptionAnnualServices.getNotifyList()));		
	return details;
}

@RequestMapping(value={"/add-rcn-details-exemption-annual-return-penalty"}, method=RequestMethod.GET)
public  @ResponseBody List<String> addExemptionCases(HttpServletRequest request, @RequestParam String applicationId,
		@RequestParam String exemptionDays,@RequestParam String remarks,@RequestParam  String requestToken,@RequestParam String blockYear) throws Exception{ 
	logger.debug("execute() is executed ");		
	ExemptionAnnualPenaltyServices exemptionAnnualServices =new ExemptionAnnualPenaltyServices();
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
    String token=null;
	if(result){		
	exemptionAnnualServices.setApplicationId(applicationId);
	exemptionAnnualServices.setExemptionDays(exemptionDays);
	exemptionAnnualServices.setRemarks(remarks);
	exemptionAnnualServices.setBlockYear(blockYear);
	exemptionAnnualServices.initAddExemptionAnnualCase();
	token = getSessionToken(tokenKey);
	}
	else {
		exemptionAnnualServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				
	notificationList.add(mapper.writeValueAsString(exemptionAnnualServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	notificationList.add(mapper.writeValueAsString(exemptionAnnualServices.getEmailNotifyList()));
	return notificationList;
}

}
