
package web.masters;
import java.util.ArrayList;
import java.util.List;

import models.master.SocietyActs;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.SocietyActsTypeServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
	@Controller
	class SocietyActsController extends TokenController {
	                 	private final Logger logger = LoggerFactory.getLogger(Home.class);
	                 	public String tokenKey="society-acts-details";
						
						@RequestMapping(value={"/society-acts-details"}, method=RequestMethod.GET)
						public ModelAndView submit(){
							logger.debug("execute() is executed ");	
							String tokenGenerated = generateAndSaveToken(tokenKey);
							SocietyActsTypeServices actDetails=new SocietyActsTypeServices();
							actDetails.execute();
							ModelAndView model = new ModelAndView();
							model.addObject("actTypeList", actDetails.getActList());	
							model.addObject("requestToken", tokenGenerated);
							model.setViewName("masters/society-acts"); 
							return model;
						}
						
						@RequestMapping(value={"/get-society-acts-details"}, method=RequestMethod.GET)
						public @ResponseBody ListPager<SocietyActs> NatureList(@RequestParam String pageNum, 
								@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
							logger.debug("execute() is executed ");		
							SocietyActsTypeServices actDetails=new SocietyActsTypeServices();
							actDetails.setPageNum(pageNum);
							actDetails.setRecordsPerPage(recordsPerPage);
							actDetails.setSortColumn(sortColumn);
							actDetails.setSortOrder(sortOrder);
							actDetails.initializeActList();
							ListPager<SocietyActs> natureListPager = new ListPager<SocietyActs>();
							natureListPager.setList(actDetails.getActList()); 
							natureListPager.setTotalRecords(actDetails.getTotalRecords()); 
							return natureListPager;
						}
						@RequestMapping(value={"/add-society-acts-details"}, method=RequestMethod.GET)
						public @ResponseBody List<String>  addList(@RequestParam String actCode,@RequestParam String actName,@RequestParam String requestToken ) throws Exception{
							logger.debug("execute() is executed ");		
							SocietyActsTypeServices actDetails=new SocietyActsTypeServices();
							Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
						    String token=null;
							if(result){
							actDetails.setActCode(actCode);
							actDetails.setActName(actName);
							actDetails.AddAct();
							token = getSessionToken(tokenKey);
							}
							else {
								actDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
							}
							ObjectMapper mapper = new ObjectMapper();
							List<String> notificationList=new ArrayList<String>();
							notificationList.add(mapper.writeValueAsString(actDetails.getNotifyList()));
							notificationList.add(mapper.writeValueAsString(new List1(token)));
							return notificationList;
						}
				      @RequestMapping(value={"/edit-society-acts-details"}, method=RequestMethod.GET)
						public @ResponseBody List<String> editcountryList(@RequestParam String actCode,@RequestParam String actName,@RequestParam String requestToken) throws Exception{
							logger.debug("execute() is executed ");	
							SocietyActsTypeServices actDetails=new SocietyActsTypeServices();
							Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
						    String token=null;
							if(result){
							actDetails.setActCode(actCode);
							actDetails.setActName(actName);
							actDetails.EditAct();
							token = getSessionToken(tokenKey);
							}
							else {
								actDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
							}
							ObjectMapper mapper = new ObjectMapper();
							List<String> notificationList=new ArrayList<String>();
							notificationList.add(mapper.writeValueAsString(actDetails.getNotifyList()));
							notificationList.add(mapper.writeValueAsString(new List1(token)));
							return notificationList;
						}
		
					 
				@RequestMapping(value={"/delete-society-acts-details"}, method=RequestMethod.GET)
						public @ResponseBody List<String> deleteList(@RequestParam String actCode,@RequestParam String actName,@RequestParam String requestToken) throws Exception {
							logger.debug("execute() is executed ");			
							SocietyActsTypeServices 	actDetails=new SocietyActsTypeServices();
							Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
						    String token=null;
							if(result){
							actDetails.setActCode(actCode);
							actDetails.setActName(actName);
							actDetails.DeleteNature();
							token = getSessionToken(tokenKey);
							}
							else {
								actDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
							}
							ObjectMapper mapper = new ObjectMapper();
							List<String> notificationList=new ArrayList<String>();				 
							notificationList.add(mapper.writeValueAsString(actDetails.getNotifyList()));
							notificationList.add(mapper.writeValueAsString(new List1(token)));
							return notificationList;
			

				}
	}		
		
		






