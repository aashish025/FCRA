package web.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import models.master.FileStatus;
import models.services.AuthorityManagement;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.masters.FileStatusServices;
import service.masters.GenderDetailServices;
import service.services.AuthorityManagementServices;
import web.Home;

@Controller
public class AuthorityManagementController {
	@Autowired AuthorityManagementServices ams;
	private final Logger logger = LoggerFactory.getLogger(AuthorityManagementController.class);	
	@RequestMapping(value={"/authority-management"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{
		//AuthorityManagementServices ams=new AuthorityManagementServices();
		ams.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("serviceTypeList", ams.getServiceTypeList());
		model.addObject("userList", ams.getUserList());
		model.addObject("userListForward", ams.getForwardUserList());
		model.addObject("stateList", ams.getStateList());
		model.setViewName("services/authority-management");		
		return model;
	}
	//get-application-detail-authority-management
	@RequestMapping(value={"/get-application-detail-authority-management"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AuthorityManagement>  ApplicationIdDetail(HttpServletRequest request,@RequestParam String userId,@RequestParam String serviceType,@RequestParam String state) throws Exception{
		logger.debug("execute() is executed ");		
		AuthorityManagementServices ams=new AuthorityManagementServices();
		ams.initApplicationIdList(userId,serviceType,request,state);
		ListPager<AuthorityManagement> applicationIdList = new ListPager<AuthorityManagement>();
		applicationIdList.setList(ams.getApplicationList()); 
		return applicationIdList;
	}
	//forward-application-detail-authority-management
	
	@RequestMapping(value={"/forward-application-detail-authority-management"}, method=RequestMethod.POST)
	public  @ResponseBody List<String>   ApplicationList(@RequestParam String forwardingUserId, @RequestParam String applicationIdString, @RequestParam String remark,@RequestParam String fromUser) throws Exception{
		logger.debug("execute() is executed ");		
	    AuthorityManagementServices ams=new AuthorityManagementServices();
	    ams.initiateForwardingAction(forwardingUserId,applicationIdString,remark,fromUser);
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(ams.getNotifyList()));	
		return notificationList;
	}
	//office to user  fresh application detail list
	@RequestMapping(value={"/get-fresh-application-detail-authority-management"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AuthorityManagement>  freshApplicationIdDetail(@RequestParam String state,@RequestParam String serviceType) throws Exception{
		logger.debug("execute() is executed ");		
		AuthorityManagementServices ams=new AuthorityManagementServices();
		ams.initFreshApplicationIdList(state,serviceType);
		ListPager<AuthorityManagement> freshapplicationIdList = new ListPager<AuthorityManagement>();
		freshapplicationIdList.setList(ams.getApplicationList()); 
		return freshapplicationIdList;
	}
	//fresh application forward from office to user
	@RequestMapping(value={"/forward-office-to-user-authority-management"}, method=RequestMethod.POST)
	public  @ResponseBody List<String>   freshApplicationList(@RequestParam String forwardToUserFresh, @RequestParam String applicationIdString) throws Exception{
		logger.debug("execute() is executed ");		
	    AuthorityManagementServices ams=new AuthorityManagementServices();
	    ams.initiateForwardingActionOfficeToUser(forwardToUserFresh,applicationIdString);
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(ams.getNotifyList()));	
		return notificationList;
	}
	
}
