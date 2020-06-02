package web.masters;
import java.util.ArrayList;
import java.util.List;

import models.master.Role;
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

import service.masters.RoleTypeDetailServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;
@Controller
public class RoleDetails extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private String tokenKey="role-details";
	
	@RequestMapping(value={"/role-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		RoleTypeDetailServices roleDetails=new RoleTypeDetailServices();
		roleDetails.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("roleTypeList", roleDetails.getRoleList());		
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/role"); 
		return model;
	}
	
	@RequestMapping(value={"/get-role-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<Role> pullroleTypeList(@RequestParam String pageNum, @RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception {
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices	roleDetails=new RoleTypeDetailServices();
        roleDetails.setPageNum(pageNum);
		roleDetails.setRecordsPerPage(recordsPerPage);
		roleDetails.setSortColumn(sortColumn);
		roleDetails.setSortOrder(sortOrder);
		roleDetails.initializeRoleList();
		ListPager<Role> roleListPager = new ListPager<Role>();
		roleListPager.setList(roleDetails.getRoleList()); 
		roleListPager.setTotalRecords(roleDetails.getTotalRecords()); 
          return roleListPager;
	}
	@RequestMapping(value={"/create-role-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> createUser(@RequestParam String roleName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices roleDetails=new RoleTypeDetailServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		roleDetails.setRoleName(roleName);		
		roleDetails.initCreateRole();
		token = getSessionToken(tokenKey);
		}
		else {
			roleDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> role=new ArrayList<String>();				 
		role.add(mapper.writeValueAsString(roleDetails.getNotifyList()));
		role.add(mapper.writeValueAsString(roleDetails.getRoleId()));
		role.add(mapper.writeValueAsString(new List1(token)));
	   return role;
	}
	@RequestMapping(value={"/get-assign-role-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getUserRole(@RequestParam Short roleId) throws Exception{
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices roleDetails=new RoleTypeDetailServices();
		roleDetails.setRoleId(roleId);
		roleDetails.initUserRole();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> roleList=new ArrayList<String>();				 
		roleList.add(mapper.writeValueAsString(roleDetails.getAvailableRoleList()));
	    roleList.add(mapper.writeValueAsString(roleDetails.getAssignedRoleList()));
		return roleList;
	}
	@RequestMapping(value={"/get-assignlevel-role-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getUserLevel(@RequestParam Short roleId) throws Exception{
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices roleDetails=new RoleTypeDetailServices();
		roleDetails.setRoleId(roleId);
		roleDetails.initUserLevel();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> roleList=new ArrayList<String>();				 
		roleList.add(mapper.writeValueAsString(roleDetails.getAvailableUserList()));
	    roleList.add(mapper.writeValueAsString(roleDetails.getAssignedUserList()));
		return roleList;
	}
	@RequestMapping(value={"/edit-role-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editNotificationList(@RequestParam String roleName, Short roleId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");			
		RoleTypeDetailServices roleDetails=new RoleTypeDetailServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		roleDetails.setRoleName(roleName);
		roleDetails.setRoleId(roleId);
		roleDetails.editRolename();
		token = getSessionToken(tokenKey);
		}
		else {
			roleDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
	    notificationList.add(mapper.writeValueAsString(roleDetails.getNotifyList()));
	    notificationList.add(mapper.writeValueAsString(new List1(token)));
	    return notificationList;
	}
	@RequestMapping(value={"/save-role-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> saveRole(@RequestParam Short roleId,@RequestParam String aspl,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices 	roleDetails=new RoleTypeDetailServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		roleDetails.setRoleId(roleId);
		roleDetails.setAssignedRoles(aspl);
		roleDetails.initSaveRole();	
		token = getSessionToken(tokenKey);
		}
		else {
			roleDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList=new ArrayList<String>();				 
		statusList.add(mapper.writeValueAsString(roleDetails.getNotifyList()));	
		statusList.add(mapper.writeValueAsString(new List1(token)));
		return statusList;
	}
	
	@RequestMapping(value={"/save-userlevel-role-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> saveUserRole(@RequestParam Short roleId,@RequestParam String aspl1,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices roleDetails=new RoleTypeDetailServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		roleDetails.setRoleId(roleId);
		roleDetails.setAssignedUser(aspl1);
		roleDetails.initSaveUser();		
		token = getSessionToken(tokenKey);
		}
		else {
			roleDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList=new ArrayList<String>();				 
		statusList.add(mapper.writeValueAsString(roleDetails.getNotifyList()));	
		statusList.add(mapper.writeValueAsString(new List1(token)));
		return statusList;
	}
	@RequestMapping(value={"/delete-role-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> deleteUser(@RequestParam Short roleId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		RoleTypeDetailServices 	roleDetails=new RoleTypeDetailServices();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		roleDetails.setRoleId(roleId);		
		roleDetails.initDeleteRole();	
		token = getSessionToken(tokenKey);
		}
		else {
			roleDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> role=new ArrayList<String>();				 
		role.add(mapper.writeValueAsString(roleDetails.getNotifyList()));
		role.add(mapper.writeValueAsString(new List1(token)));
		return role;
	}

}
