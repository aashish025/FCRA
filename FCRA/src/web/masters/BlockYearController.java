package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.BlockYearService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.Home;
import web.TokenController;

@Controller
public class BlockYearController extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "block-year-details";
	
	@RequestMapping(value={"/block-year-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");		
		String tokenGenerated = generateAndSaveToken(tokenKey);
		BlockYearService blockyear=new BlockYearService();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/Block-Year"); 
		return model;
	}
	
	@RequestMapping(value={"/get-block-year-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.BlockYear> pullBlockYearList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		BlockYearService blockyear= new BlockYearService();
		blockyear.setPageNum(pageNum);
		blockyear.setRecordsPerPage(recordsPerPage);
		blockyear.setSortColumn(sortColumn);
		blockyear.setSortOrder(sortOrder);
		blockyear.initializeBlockYearList();
		ListPager<models.master.BlockYear> subListPager = new ListPager<models.master.BlockYear>();
		subListPager.setList(blockyear.getBlockYearList());
		subListPager.setTotalRecords(blockyear.getTotalRecords()); 
		return subListPager;
	}
	
	@RequestMapping(value={"/add-block-year-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addBlockYearList(@RequestParam String blkyr, @RequestParam String status,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		BlockYearService blockyear = new BlockYearService();
		if(result) {
		blockyear.setBlkyr(blkyr);
		blockyear.setStatus(status);
		blockyear.initAddBlockYear();
		token = getSessionToken(tokenKey);
		}
		else {
			blockyear.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(blockyear.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
@RequestMapping(value={"/delete-block-year-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteBlockYearList(@RequestParam String blkyr,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		BlockYearService blockyear = new BlockYearService();
		if(result) {
		blockyear.setBlkyr(blkyr);				
		blockyear.initDeleteBlockYear();
		token = getSessionToken(tokenKey);
		}
		else {
			blockyear.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(blockyear.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-block-year-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editBlockYearList(@RequestParam String blkyr, @RequestParam String status,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		BlockYearService blockyear = new BlockYearService();
		if(result) {
		blockyear.setBlkyr(blkyr);
		blockyear.setStatus(status);
		blockyear.initEditBlockYear();
		token = getSessionToken(tokenKey);
		}
		else {
			blockyear.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(blockyear.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
		}
}