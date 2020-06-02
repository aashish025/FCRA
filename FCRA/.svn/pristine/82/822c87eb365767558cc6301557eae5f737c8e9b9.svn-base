package web.masters;


import java.util.ArrayList;
import java.util.List;

import models.master.DesignationType;
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

import service.masters.DesignationTypeDetailsServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;
@Controller
public class DesignationDetails extends TokenController{
	
		 private final Logger logger = LoggerFactory.getLogger(Home.class);
			public String tokenKey="designation";
			@RequestMapping(value={"/designation"}, method=RequestMethod.GET)
			public ModelAndView submit(){
				logger.debug("execute() is executed ");	
				String tokenGenerated = generateAndSaveToken(tokenKey);
				DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
				designationDetails.execute();
				ModelAndView model = new ModelAndView();
				model.addObject("designationTypeList", designationDetails.getDesignationList());	
				model.addObject("requestToken", tokenGenerated);
				model.setViewName("masters/Designation"); 
				return model;
			}
			@RequestMapping(value={"/get-designation"}, method=RequestMethod.GET)
			public @ResponseBody ListPager<DesignationType> pullDesignationTypeList(@RequestParam String pageNum, 
					@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
				logger.debug("execute() is executed ");		
				DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
				designationDetails.setPageNum(pageNum);
				designationDetails.setRecordsPerPage(recordsPerPage);
				designationDetails.setSortColumn(sortColumn);
				designationDetails.setSortOrder(sortOrder);
				designationDetails.initializeDesignationList();
				ListPager<DesignationType> designationListPager = new ListPager<DesignationType>();
				designationListPager.setList(designationDetails.getDesignationList()); 
			    designationListPager.setTotalRecords(designationDetails.getTotalRecords()); 
				return designationListPager;
			}
			@RequestMapping(value={"/add-designation"}, method=RequestMethod.POST)
			public @ResponseBody List<String>  adddesignationList(@RequestParam String shortDesignation, @RequestParam String designationName,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");		
				DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				designationDetails.setShortDesignation(shortDesignation);
				designationDetails.setDesignationName(designationName);
			    designationDetails.AddDesignation();
			    token = getSessionToken(tokenKey);
				}
				else {
					designationDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();
				notificationList.add(mapper.writeValueAsString(designationDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(designationDetails.getDesignationId()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
			}
			@RequestMapping(value={"/get-desassign-designation"}, method=RequestMethod.POST)
			public @ResponseBody List<String> getUserRole(@RequestParam String designationId) throws Exception{
				logger.debug("execute() is executed ");		
				DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
				designationDetails.setDesignationId(designationId);
                designationDetails.initDesignation();		
				ObjectMapper mapper = new ObjectMapper();
				List<String> roleList=new ArrayList<String>();				 
				roleList.add(mapper.writeValueAsString(designationDetails.getAvailableDesList()));
			    roleList.add(mapper.writeValueAsString(designationDetails.getAssignedDesList()));
				return roleList;
			}
			@RequestMapping(value={"/save-designation"}, method=RequestMethod.POST)
			public @ResponseBody List<String> saveDetails(@RequestParam String designationId,@RequestParam String aspl,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");		
				DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				designationDetails.setDesignationId(designationId);
				designationDetails.setAssignedDesc(aspl);;
				designationDetails.initSaveDesc();
				token = getSessionToken(tokenKey);
				}
				else {
					designationDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> statusList=new ArrayList<String>();				 
				statusList.add(mapper.writeValueAsString(designationDetails.getNotifyList()));	
				statusList.add(mapper.writeValueAsString(new List1(token)));
				return statusList;
			}
			@RequestMapping(value={"/edit-designation"}, method=RequestMethod.GET)
			public @ResponseBody List<String> editDesignationList(@RequestParam String designationName,@RequestParam String designationId,@RequestParam String shortDesignation,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");
				DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				designationDetails.setShortDesignation(shortDesignation);
				designationDetails.setDesignationName(designationName);
				designationDetails.setDesignationId(designationId);
				designationDetails.editDesignationName();
				token = getSessionToken(tokenKey);
				}
				else {
					designationDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();
				notificationList.add(mapper.writeValueAsString(designationDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
			}
			 
			@RequestMapping(value={"/delete-designation"}, method=RequestMethod.GET)
					public @ResponseBody List<String> deletedesignation(@RequestParam String designationId,@RequestParam String requestToken) throws Exception{
						logger.debug("execute() is executed ");			
						DesignationTypeDetailsServices designationDetails=new DesignationTypeDetailsServices();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
						designationDetails.setDesignationId(designationId);
						designationDetails.initDeleteDes();
						token = getSessionToken(tokenKey);
						}
						else {
							designationDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();				 
						notificationList.add(mapper.writeValueAsString(designationDetails.getNotifyList()));	
						notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
				}


}
