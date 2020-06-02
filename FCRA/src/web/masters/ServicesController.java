package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.Service;
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

import service.masters.ServicesTypeDetailsServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

@Controller
public class ServicesController extends TokenController {
	 private final Logger logger = LoggerFactory.getLogger(Home.class);
		private String tokenKey="service-details";
		@RequestMapping(value={"/service-details"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");		
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ServicesTypeDetailsServices servicesDetails=new ServicesTypeDetailsServices();
			servicesDetails.execute();
			ModelAndView model = new ModelAndView();
			model.addObject("servicesTypeList", servicesDetails.getServicesTypeList());	
			model.addObject("requestToken", tokenGenerated);
			model.setViewName("masters/services-type"); 
			return model;
		}
		@RequestMapping(value={"/get-service-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<Service> Services(@RequestParam String pageNum,@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			ServicesTypeDetailsServices 	servicesDetails=new ServicesTypeDetailsServices();
			servicesDetails.setPageNum(pageNum);
			servicesDetails.setRecordsPerPage(recordsPerPage);
			servicesDetails.setSortColumn(sortColumn);
			servicesDetails.setSortOrder(sortOrder);
			servicesDetails.initializeServiceList();
			ListPager<Service> serviceListPager = new ListPager<Service>();
			serviceListPager.setList(servicesDetails.getServicesList()); 
			serviceListPager.setTotalRecords(servicesDetails.getTotalRecords()); 
			return serviceListPager;
		}
		
		@RequestMapping(value={"/add-service-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  addList(@RequestParam String servicesDesc,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");		
			ServicesTypeDetailsServices servicesDetails=new ServicesTypeDetailsServices();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
			if(result){
			servicesDetails.setServicesDesc(servicesDesc);
			servicesDetails.AddServices();	
			token = getSessionToken(tokenKey);
			}
			else {
				servicesDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(servicesDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
		}
		
		@RequestMapping(value={"/edit-service-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editRecreationalList(@RequestParam String servicesCode,@RequestParam String servicesDesc,@RequestParam String requestToken ) throws Exception{
			logger.debug("execute() is executed ");	
			ServicesTypeDetailsServices servicesDetails=new ServicesTypeDetailsServices();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
			if(result){
			servicesDetails.setServicesCode(servicesCode);
			servicesDetails.setServicesDesc(servicesDesc);
			servicesDetails.EditServices();
			token = getSessionToken(tokenKey);
			}
			else {
				servicesDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(servicesDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
		}

 @RequestMapping(value={"/delete-service-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteList(@RequestParam String servicesCode, @RequestParam String servicesDesc ,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");			
			ServicesTypeDetailsServices servicesDetails=new ServicesTypeDetailsServices();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
			if(result){
			servicesDetails.setServicesCode(servicesCode);
			servicesDetails.setServicesDesc(servicesDesc);
			servicesDetails.DeleteServices();
			token = getSessionToken(tokenKey);
			}
			else {
				servicesDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(servicesDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
}
}
