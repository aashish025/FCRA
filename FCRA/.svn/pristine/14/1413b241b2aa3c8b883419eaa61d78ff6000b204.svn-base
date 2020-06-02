package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.services.ListPager;
import models.master.UserLevel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.masters.UserLevelServices;

@Controller
public class UserLevelConrollor {
	     private final Logger logger = LoggerFactory.getLogger(UserLevelConrollor.class);
		//private UserLevelServices userlavel;
		
		@RequestMapping(value={"/user-level-details"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");			
			UserLevelServices userlavel=new UserLevelServices();
			userlavel.execute();
			ModelAndView model = new ModelAndView();		
			model.setViewName("masters/user-level"); 
			return model;
		}
		
		@RequestMapping(value={"/get-user-list-details-user-level-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<UserLevel> pullUserLavelList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			UserLevelServices userlavel = new UserLevelServices();
			userlavel.setPageNum(pageNum);
			userlavel.setRecordsPerPage(recordsPerPage);
			userlavel.setSortColumn(sortColumn);
			userlavel.setSortOrder(sortOrder);
			userlavel.initUserLevelList();
			ListPager<UserLevel> userListPager = new ListPager<UserLevel>();
			userListPager.setList(userlavel.getUserList()); 
			userListPager.setTotalRecords(userlavel.getTotalRecords()); 
			return userListPager;
		}
		@RequestMapping(value={"/add-user-type-details-user-level-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  AcquisitionList(@RequestParam String userlavelName) throws Exception{
			logger.debug("execute() is executed ");		
			UserLevelServices userlavel = new UserLevelServices();
			userlavel.setUserLevelName(userlavelName);
			userlavel.AddUser();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(userlavel.getNotifyList()));	
			return notificationList;
		}

		@RequestMapping(value={"/edit-userlavel-details-user-level-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editNotificationList(@RequestParam String userlavelName, Integer userlavelId) throws Exception{
			logger.debug("execute() is executed ");			
			UserLevelServices userlavel = new UserLevelServices();
			userlavel.setUserLevelName(userlavelName);
			userlavel.setUserLevelId(userlavelId);
			userlavel.editUserLevel();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
				
		    notificationList.add(mapper.writeValueAsString(userlavel.getNotifyList()));
			
			return notificationList;
		}

	 
@RequestMapping(value={"/delete-userlavel-details-user-level-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteAcquisitionList(@RequestParam String userlavelName, Integer userlavelId) throws Exception{
			logger.debug("execute() is executed ");			
			UserLevelServices userlavel = new UserLevelServices();
			userlavel.setUserLevelName(userlavelName);
			userlavel.setUserLevelId(userlavelId);			
			userlavel.initDeleteUserLevel();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(userlavel.getNotifyList()));	
			return notificationList;
	}
}

	
