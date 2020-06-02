package web.services;
import java.util.ArrayList;
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
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;
@Controller
public class ChangeAssociationController extends TokenController {		
	private final Logger logger = LoggerFactory.getLogger(ChangeAssociationController.class);
	private final String tokenKey = "change-association-type";
	@RequestMapping(value={"/change-association-type"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{					
				ModelAndView model = new ModelAndView();
				String tokenGenerated = generateAndSaveToken(tokenKey);
				ChangeAssociationService associationService =new ChangeAssociationService();
				associationService.initRegistrationTrackingAction();
				model.addObject("stateList", associationService.getStateList());
				model.addObject("associationType", associationService.getAssoType());
				model.addObject("requestToken", tokenGenerated);
				model.setViewName("services/change-association-type");		
				return model;			
				
	}
	@RequestMapping(value={"/district-change-association-type"}, method=RequestMethod.POST)
	public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeAssociationService associationService =new ChangeAssociationService();	
		associationService.setState(state);		
		associationService.initDistrict();		 
		return associationService.getDistrictList();
	}
	@RequestMapping(value={"/advance-rcn-list-change-association-type"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
			,@RequestParam String district,@RequestParam String applicationName,@RequestParam String functionaryName) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeAssociationService associationService =new ChangeAssociationService();
		associationService.setPageNum(pageNum);
		associationService.setRecordsPerPage(recordsPerPage);
		associationService.setSortColumn(sortColumn);
		associationService.setSortOrder(sortOrder);		
		associationService.setAppName(applicationName.toUpperCase());
		associationService.setFunctionaryName(functionaryName.toUpperCase());
		associationService.setState(state);
		associationService.setDistrict(district);
		associationService.initAdvanceApplicationListDetails();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(associationService.getApplicationList());
		ar.setTotalRecords(associationService.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/rcn-details-change-association-type"}, method=RequestMethod.GET)
	public @ResponseBody List<String> getRCnDetails(@RequestParam String applicationId) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeAssociationService associationService =new ChangeAssociationService();
		associationService.setAppId(applicationId.toUpperCase());
		associationService.initGetApplicationChangeAsso();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(associationService.getApplicationList()));
		return details;
	}
	@RequestMapping(value={"/application-list-change-association-type"}, method=RequestMethod.GET)
	public @ResponseBody List<String> initApplicationList(@RequestParam String applicationId) throws Exception{
		logger.debug("execute() is executed ");		
		ChangeAssociationService associationService =new ChangeAssociationService();
		associationService.setAppId(applicationId.toUpperCase());
		associationService.initApplicationListDetails1();
		ObjectMapper mapper = new ObjectMapper();
		List<String> list=new ArrayList<String>();
		list.add(mapper.writeValueAsString(associationService.getRcnDetail()));	
	    list.add(mapper.writeValueAsString(associationService.getNotifyList()));
		return list;
	}

	@RequestMapping(value={"/add-change-association-type"}, method=RequestMethod.GET)
	public  @ResponseBody List<String> addExemptionCases(HttpServletRequest request, @RequestParam String applicationId,
			@RequestParam String assocType,@RequestParam  String requestToken) throws Exception{ 
		logger.debug("execute() is executed ");		
		ChangeAssociationService associationService =new ChangeAssociationService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){		
		associationService.setApplicationId(applicationId);
		associationService.setAssocType(assocType);
		associationService.submitAssociation();
		token = getSessionToken(tokenKey);
		}
		else {
			associationService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				
		notificationList.add(mapper.writeValueAsString(associationService.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		
		return notificationList;
	}


}
