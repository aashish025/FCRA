package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.User;
import models.services.ListPager;
import utilities.notifications.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.masters.UserDetailsService;
import web.Home;

@Controller
public class UserDetails {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private UserDetailsService uds=null;
	@RequestMapping(value={"/user-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){	
		UserDetailsService uds = new UserDetailsService();
		uds.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("genderList", uds.getGenderList());
		model.addObject("designationList", uds.getDesignationList());
		model.setViewName("masters/user-console"); 
		return model;
	}
	@RequestMapping(value={"/get-user-list-user-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<User> pullNotificationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setPageNum(pageNum);
		uds.setRecordsPerPage(recordsPerPage);
		uds.setSortColumn(sortColumn);
		uds.setSortOrder(sortOrder);
		uds.initUserList();
		ListPager<User> ulp = new ListPager<User>();
		ulp.setList(uds.getUserList());
		ulp.setTotalRecords(uds.getTotalRecords()); 
		return ulp;
	}
	@RequestMapping(value={"/office-user-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> checkuser() throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.checkOffice();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> userDetails=new ArrayList<String>();
		userDetails.add(mapper.writeValueAsString(uds.isSectionStatus()));
		return userDetails;
	}
	@RequestMapping(value={"/create-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> createUser(@RequestParam String name, 
			@RequestParam String gender, @RequestParam String email, @RequestParam String designation) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setName(name);
		uds.setEmail(email);
		uds.setGender(gender);
		uds.setDesignation(designation);
		uds.initCreateUser();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> userDetails=new ArrayList<String>();				 
		userDetails.add(mapper.writeValueAsString(uds.getNotifyList()));		
		userDetails.add(mapper.writeValueAsString(uds.getUserid()));
		return userDetails;
	}
	@RequestMapping(value={"/edit-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> editUser(@RequestParam String userid,@RequestParam String name, 
			@RequestParam String gender, @RequestParam String email, @RequestParam String designation) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);
		uds.setName(name);
		uds.setEmail(email);
		uds.setGender(gender);
		uds.setDesignation(designation);
		uds.initEditUser();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> userDetails=new ArrayList<String>();				 
		userDetails.add(mapper.writeValueAsString(uds.getNotifyList()));		
		return userDetails;
	}
	
	
	@RequestMapping(value={"/reset-password-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> resetPassword(@RequestParam String userid) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);		
		uds.initResetPassword();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> userDetails=new ArrayList<String>();				 
		userDetails.add(mapper.writeValueAsString(uds.getNotifyList()));		
		return userDetails;
	}
	@RequestMapping(value={"/unlock-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> unlockUser(@RequestParam String userid) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);		
		uds.initUnlockUser();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> userDetails=new ArrayList<String>();				 
		userDetails.add(mapper.writeValueAsString(uds.getNotifyList()));		
		return userDetails;
	}
	@RequestMapping(value={"/delete-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> deleteUser(@RequestParam String userid) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);		
		uds.initDeleteUser();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> userDetails=new ArrayList<String>();				 
		userDetails.add(mapper.writeValueAsString(uds.getNotifyList()));		
		return userDetails;
	}
	@RequestMapping(value={"/get-role-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getUserRole(@RequestParam String userid) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);
		uds.initUserRole();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> roleList=new ArrayList<String>();				 
		roleList.add(mapper.writeValueAsString(uds.getAvailableRoleList()));
		roleList.add(mapper.writeValueAsString(uds.getAssignedRoleList()));
		return roleList;
	}
	@RequestMapping(value={"/get-section-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getSection(@RequestParam String userid) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService	uds = new UserDetailsService();
		uds.setUserid(userid);
		uds.initSection();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> sectionList=new ArrayList<String>();				 
		sectionList.add(mapper.writeValueAsString(uds.getAvailableSectionList()));
		sectionList.add(mapper.writeValueAsString(uds.getAssignedSectionList()));
		return sectionList;
	}
	@RequestMapping(value={"/save-role-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> saveUserRole(@RequestParam String userid,@RequestParam String aspl) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);
		uds.setAssignedRoles(aspl);
		uds.initSaveUserRole();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList=new ArrayList<String>();				 
		statusList.add(mapper.writeValueAsString(uds.getNotifyList()));		
		return statusList;
	}
	@RequestMapping(value={"/save-section-user-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> saveUserSection(@RequestParam String userid,@RequestParam String aspl1) throws Exception{
		logger.debug("execute() is executed ");		
		UserDetailsService uds = new UserDetailsService();
		uds.setUserid(userid);
		uds.setAssignedSection(aspl1);
		uds.initSaveSection();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList=new ArrayList<String>();				 
		statusList.add(mapper.writeValueAsString(uds.getNotifyList()));		
		return statusList;
	}
}
