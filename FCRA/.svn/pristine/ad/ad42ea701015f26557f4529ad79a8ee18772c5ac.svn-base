package web.masters;

import java.util.ArrayList;
import models.master.AdminUser;
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
import service.masters.AdminUserServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

@Controller
public class AdminUserController extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "admin-user-management";
	
	@RequestMapping(value={"/admin-user-management"}, method=RequestMethod.GET)
	public ModelAndView submit(){	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		AdminUserServices uds = new AdminUserServices();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/Admin-user"); 
		return model;
	}
	
	@RequestMapping(value={"/get-admin-user-management"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AdminUser> pullNotificationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		AdminUserServices uds = new AdminUserServices();
		uds.setPageNum(pageNum);
		uds.setRecordsPerPage(recordsPerPage);
		uds.setSortColumn(sortColumn);
		uds.setSortOrder(sortOrder);
		uds.initAdminUserList();
		ListPager<AdminUser> ulp = new ListPager<AdminUser>();
		ulp.setList(uds.getAdminuserList());
		ulp.setTotalRecords(uds.getTotalRecords()); 
		return ulp;
	}
	
	@RequestMapping(value={"/reset-password-admin-user-management"}, method=RequestMethod.POST)
	public @ResponseBody java.util.List<String> resetPassword(@RequestParam String userid, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		AdminUserServices uds = new AdminUserServices();
		if(result) {
		uds.setUserid(userid);		
		uds.initResetPassword();	
		token = getSessionToken(tokenKey);
		}
		else {
			uds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper = new ObjectMapper();
		java.util.List<String> userDetails=new ArrayList<String>();				 
		userDetails.add(mapper.writeValueAsString(uds.getNotifyList()));
		userDetails.add(mapper.writeValueAsString(new List1(token)));
		return userDetails;
	}

	@RequestMapping(value={"/unlock-admin-user-management"}, method=RequestMethod.POST)
	public @ResponseBody java.util.List<String> unlockUser(@RequestParam String userid, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		AdminUserServices uds = new AdminUserServices();
		if(result) {
		uds.setUserid(userid);		
		uds.initUnlockAdmin();	
		token = getSessionToken(tokenKey);
		}
		else {
			uds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper = new ObjectMapper();
		java.util.List<String> adminDetails=new ArrayList<String>();				 
		adminDetails.add(mapper.writeValueAsString(uds.getNotifyList()));	
		adminDetails.add(mapper.writeValueAsString(new List1(token)));
		return adminDetails;
	}
}