package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.ApplicationSubStage;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.masters.ApplicationSubstageServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;
@Controller
public class ApplicationSubStageDetails extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	  public String tokenKey="application-substage-details";
	
	@RequestMapping(value={"/application-substage-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");		
		String tokenGenerated = generateAndSaveToken(tokenKey);
		ApplicationSubstageServices appsubDetails=new ApplicationSubstageServices();
		appsubDetails.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("applicationTypeList", appsubDetails.getApplicationTypeList());
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/application-substage"); 
		return model;
	}
	@RequestMapping(value={"/get-application-substage-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<ApplicationSubStage> pullAppTypeList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationSubstageServices appsubDetails=new ApplicationSubstageServices();
		appsubDetails.setPageNum(pageNum);
		appsubDetails.setRecordsPerPage(recordsPerPage);
		appsubDetails.setSortColumn(sortColumn);
		appsubDetails.setSortOrder(sortOrder);
		appsubDetails.initializeSubAppList();
		ListPager<ApplicationSubStage> appListPager = new ListPager<ApplicationSubStage>();
		appListPager.setList(appsubDetails.getApplicationsubList()); 
		appListPager.setTotalRecords(appsubDetails.getTotalRecords()); 
		return appListPager;
	}
	@RequestMapping(value={"/add-application-substage-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String>  adddescList(@RequestParam String substageDesc,@RequestParam String parentstageId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		ApplicationSubstageServices appsubDetails=new ApplicationSubstageServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
	    appsubDetails.setSubstageDesc(substageDesc);
		appsubDetails.setParentstageId(parentstageId);
		appsubDetails.AddSubsDesc();
		token = getSessionToken(tokenKey);
		}
		else {
			appsubDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(appsubDetails.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;
	}
	@RequestMapping(value={"/edit-application-substage-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editList(@RequestParam String substageDesc, String substageId, String parentstageId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		ApplicationSubstageServices appsubDetails=new ApplicationSubstageServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
	    appsubDetails.setSubstageDesc(substageDesc);
		appsubDetails.setSubstageId(substageId);
		appsubDetails.setParentstageId(parentstageId);
		appsubDetails.EditSubDesc();
		token = getSessionToken(tokenKey);
		}
		else {
			appsubDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(appsubDetails.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
	     return notificationList;
	}

 
@RequestMapping(value={"/delete-application-substage-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteList(@RequestParam String substageDesc, String substageId, String parentstageId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");				
		ApplicationSubstageServices appsubDetails=new ApplicationSubstageServices();	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		appsubDetails.setSubstageDesc(substageDesc);
		appsubDetails.setSubstageId(substageId);
		appsubDetails.setParentstageId(parentstageId);
		appsubDetails.DeleteSubDesc();
		token = getSessionToken(tokenKey);
		}
		else {
			appsubDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(appsubDetails.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;
}

}
