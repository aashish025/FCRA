package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.ApplicationStage;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.ApplicationStageServices;import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class ApplicationStageDetails extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);

	  public String tokenKey="application-stage-details";
	
	@RequestMapping(value={"/application-stage-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		ApplicationStageServices appDetails=new ApplicationStageServices();
		appDetails.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("applicationTypeList", appDetails.getApplicationList());	
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/application-stage"); 
		return model;
	}
	
	@RequestMapping(value={"/get-application-stage-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<ApplicationStage> pullAppTypeList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStageServices appDetails=new ApplicationStageServices();
		appDetails.setPageNum(pageNum);
		appDetails.setRecordsPerPage(recordsPerPage);
		appDetails.setSortColumn(sortColumn);
		appDetails.setSortOrder(sortOrder);
		appDetails.initializeAppList();
		ListPager<ApplicationStage> appListPager = new ListPager<ApplicationStage>();
		appListPager.setList(appDetails.getApplicationList()); 
		appListPager.setTotalRecords(appDetails.getTotalRecords()); 
		return appListPager;
	}
	@RequestMapping(value={"/add-application-stage-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String>  adddescList(@RequestParam String stageDesc,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationStageServices 	appDetails=new ApplicationStageServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		appDetails.setStageDesc(stageDesc);
		appDetails.AddDesc();
		token = getSessionToken(tokenKey);
		}
		else {
			appDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(appDetails.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;
	}
	
	@RequestMapping(value={"/edit-application-stage-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editList(@RequestParam String stageDesc, String stageId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		ApplicationStageServices appDetails=new ApplicationStageServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		appDetails.setStageDesc(stageDesc);
		appDetails.setStageId(stageId);
		appDetails.EditDesc();	
		token = getSessionToken(tokenKey);
		}
		else {
			appDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(appDetails.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
	     return notificationList;
	}

 
@RequestMapping(value={"/delete-application-stage-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteList(@RequestParam String stageDesc,@RequestParam String stageId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");			
		ApplicationStageServices appDetails=new ApplicationStageServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		appDetails.setStageDesc(stageDesc);
		appDetails.setStageId(stageId);
		appDetails.DeleteDesc();
		token = getSessionToken(tokenKey);
		}
		else {
			appDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(appDetails.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;
}

}
