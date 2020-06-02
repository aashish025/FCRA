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
import com.fasterxml.jackson.databind.ObjectMapper;
import service.masters.ParentMenuService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

@Controller
public class ParentMenuDetails extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "parent-menu";
	
	@RequestMapping(value={"/parent-menu"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		ParentMenuService parentMenu=new ParentMenuService();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/parent-menu"); 
		return model;
	}
	
	@RequestMapping(value={"/get-PMenu-type-parent-menu"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.ParentMenu> pullParentList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		ParentMenuService parentMenu = new ParentMenuService();
		parentMenu.setPageNum(pageNum);
		parentMenu.setRecordsPerPage(recordsPerPage);
		parentMenu.setSortColumn(sortColumn);
		parentMenu.setSortOrder(sortOrder);
		parentMenu.initializeParentMenuList();
		ListPager<models.master.ParentMenu> parentListPager = new ListPager<models.master.ParentMenu>();
		parentListPager.setList(parentMenu.getParentMenuList());
		parentListPager.setTotalRecords(parentMenu.getTotalRecords()); 
		return parentListPager;
	}
	
	@RequestMapping(value={"/add-PMenu-details-parent-menu"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addParentList(@RequestParam String pmenuName, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		ParentMenuService parentMenu = new ParentMenuService();
		if(result) {
		parentMenu.setPmenuName(pmenuName);				
		parentMenu.initAddParent();
		token = getSessionToken(tokenKey);
		}
		else {
			parentMenu.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(parentMenu.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-PMenu-details-parent-menu"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteparentList(@RequestParam Short pmenuId, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		ParentMenuService parentMenu = new ParentMenuService();
		if(result) {
		parentMenu.setPmenuId(pmenuId);				
		parentMenu.initDeleteParent();
		token = getSessionToken(tokenKey);
		}
		else {
			parentMenu.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(parentMenu.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
	@RequestMapping(value={"/edit-PMenu-details-parent-menu"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editParentList(@RequestParam Short pmenuId,@RequestParam String pmenuName, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		ParentMenuService parentMenu = new ParentMenuService();
		if(result) {
		parentMenu.setPmenuId(pmenuId);
		parentMenu.setPmenuName(pmenuName);
			
		parentMenu.initEditParent();
		token = getSessionToken(tokenKey);
		}
		else {
			parentMenu.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(parentMenu.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
}