package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.RedFlagCategory;
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

import service.masters.RedFlagCategoryServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;
@Controller
public class RedFlagCategoryController extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "red-flag-category";
		
	@RequestMapping(value={"/red-flag-category"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		RedFlagCategoryServices redflag =new RedFlagCategoryServices();
		ModelAndView model = new ModelAndView();	
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/red-flag-category"); 
		return model;
	}
	@RequestMapping(value={"/get-red-flag-category"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<RedFlagCategory> pullCategoryList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		RedFlagCategoryServices redflag = new RedFlagCategoryServices();
		redflag.setPageNum(pageNum);
		redflag.setRecordsPerPage(recordsPerPage);
		redflag.setSortColumn(sortColumn);
		redflag.setSortOrder(sortOrder);
		redflag.initializeRedFlagList();
		ListPager<RedFlagCategory> redflagListPager = new ListPager<RedFlagCategory>();
		redflagListPager.setList(redflag.getRedflagList());
		redflagListPager.setTotalRecords(redflag.getTotalRecords()); 
		return redflagListPager;
	}
	
	@RequestMapping(value={"/add-red-flag-category"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addCategoryList(@RequestParam String categoryName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		RedFlagCategoryServices redflag = new RedFlagCategoryServices();
		if(result) {
			
			redflag.setCategoryName(categoryName);;				
			redflag.initAddCategory();
			token = getSessionToken(tokenKey);
		}
		else {
			redflag.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(redflag.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	@RequestMapping(value={"/delete-red-flag-category"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteCategoryList(@RequestParam Integer categoryCode, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		RedFlagCategoryServices redflag = new RedFlagCategoryServices();
		if(result) {
		redflag.setCategoryCode(categoryCode);		
		redflag.initDeleteCategory();
		token = getSessionToken(tokenKey);
		}
		else {
			redflag.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(redflag.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-red-flag-category"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editCategoryList(@RequestParam Integer categoryCode,@RequestParam String categoryName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		RedFlagCategoryServices redflag = new RedFlagCategoryServices();
		if(result) {
			redflag.setCategoryCode(categoryCode);
			redflag.setCategoryName(categoryName);
			redflag.initEditCategory();
		token = getSessionToken(tokenKey);
		}
		else {
			redflag.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(redflag.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	
	}


}
