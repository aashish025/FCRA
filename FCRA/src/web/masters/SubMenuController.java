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
import service.masters.SubMenuService;
import utilities.KVPair;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;
import web.TokenController;

@Controller
public class SubMenuController extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "sub-menu-details";
	@RequestMapping(value={"/sub-menu-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		SubMenuService subMenu=new SubMenuService();
		subMenu.execute();
		List<KVPair<String, String>> pmenuNameList = subMenu.getPmenuNameList();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.addObject("pmenuNameList", pmenuNameList);
		model.setViewName("masters/sub-menu"); 
		return model;
	}
	
	@RequestMapping(value={"/get-SubMenu-type-sub-menu-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.SubMenu> pullUserList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		SubMenuService subMenu= new SubMenuService();
		subMenu.setPageNum(pageNum);
		subMenu.setRecordsPerPage(recordsPerPage);
		subMenu.setSortColumn(sortColumn);
		subMenu.setSortOrder(sortOrder);
		subMenu.initializeSubMenuList();
		ListPager<models.master.SubMenu> subListPager = new ListPager<models.master.SubMenu>();
		subListPager.setList(subMenu.getSubMenuList());
		subListPager.setTotalRecords(subMenu.getTotalRecords()); 
		return subListPager;
	}
	
	@RequestMapping(value={"/add-SubMenu-details-sub-menu-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addsubmenuList(@RequestParam String smenuName, @RequestParam String actionPath, @RequestParam String pMenuId, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		SubMenuService subMenu = new SubMenuService();
		if(result) {
		subMenu.setSmenuName(smenuName);
		subMenu.setActionPath(actionPath);
		subMenu.setpMenuId(pMenuId);
		subMenu.initAddSubMenu();
		token = getSessionToken(tokenKey);
	}
	else {
		subMenu.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(subMenu.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
@RequestMapping(value={"/delete-SubMenu-details-sub-menu-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deletesubmenuList(@RequestParam Short smenuId, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		SubMenuService subMenu = new SubMenuService();
		if(result) {
		subMenu.setSmenuId(smenuId);				
		subMenu.initDeleteSubMenu();
		token = getSessionToken(tokenKey);
	}
	else {
		subMenu.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(subMenu.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-SubMenu-details-sub-menu-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editsubmenuList(@RequestParam Short smenuId,@RequestParam String smenuName, @RequestParam String actionPath, @RequestParam String pMenuId, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		SubMenuService subMenu = new SubMenuService();
		if(result) {
		subMenu.setSmenuId(smenuId);
		subMenu.setSmenuName(smenuName);
		subMenu.setActionPath(actionPath);
		subMenu.setpMenuId(pMenuId);
		subMenu.initEditSubMenu();
		token = getSessionToken(tokenKey);
	}
	else {
		subMenu.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(subMenu.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
		}
}