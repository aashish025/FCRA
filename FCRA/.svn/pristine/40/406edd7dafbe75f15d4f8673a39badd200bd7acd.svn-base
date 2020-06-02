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
import service.masters.UserStatusService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;
import web.TokenController;

@Controller
public class UserStatusDetails extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "user-status-details";
		
	@RequestMapping(value={"/user-status-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		UserStatusService userStatus=new UserStatusService();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/user-status"); 
		return model;
	}
	
	@RequestMapping(value={"/get-UserSt-type-user-status-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.UserStatus> pullUserList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		UserStatusService userStatus = new UserStatusService();
		userStatus.setPageNum(pageNum);
		userStatus.setRecordsPerPage(recordsPerPage);
		userStatus.setSortColumn(sortColumn);
		userStatus.setSortOrder(sortOrder);
		userStatus.initializeUserStatusList();
		ListPager<models.master.UserStatus> userListPager = new ListPager<models.master.UserStatus>();
		userListPager.setList(userStatus.getUserStatusList());
		userListPager.setTotalRecords(userStatus.getTotalRecords()); 
		return userListPager;
	}
	
	@RequestMapping(value={"/add-UserSt-details-user-status-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addUserList(@RequestParam String actionName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		UserStatusService userStatus = new UserStatusService();
		if(result) {
		userStatus.setActionName(actionName);				
		userStatus.initAddUserSt();
		token = getSessionToken(tokenKey);
		}
		else {
			userStatus.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(userStatus.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-UserSt-details-user-status-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteuserList(@RequestParam Integer actionId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		UserStatusService userStatus = new UserStatusService();
		if(result) {
		userStatus.setActionId(actionId);				
		userStatus.initDeleteUserSt();
		token = getSessionToken(tokenKey);
		}
		else {
			userStatus.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(userStatus.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-UserSt-details-user-status-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editUserList(@RequestParam Integer actionId,@RequestParam String actionName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		UserStatusService userStatus = new UserStatusService();
		if(result) {
		userStatus.setActionId(actionId);
		userStatus.setActionName(actionName);
		userStatus.initEditUserSt();
		token = getSessionToken(tokenKey);
		}
		else {
			userStatus.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(userStatus.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	
	}
}