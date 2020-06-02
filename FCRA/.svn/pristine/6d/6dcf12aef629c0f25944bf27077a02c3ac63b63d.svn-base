package web.masters;
import java.util.ArrayList;
import java.util.List;












import models.master.AssociationNature;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.AssociationNatureTypeService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
	@Controller
	class AssociationNatureController extends TokenController {
                 	private final Logger logger = LoggerFactory.getLogger(Home.class);
					 public String tokenKey="association-nature-details";
					
					@RequestMapping(value={"/association-nature-details"}, method=RequestMethod.GET)
					public ModelAndView submit(){
						logger.debug("execute() is executed ");	
						String tokenGenerated = generateAndSaveToken(tokenKey);
						AssociationNatureTypeService natureDetails=new AssociationNatureTypeService();
						natureDetails.execute();
						ModelAndView model = new ModelAndView();
						model.addObject("natureTypeList", natureDetails.getNatureList());
						model.addObject("requestToken", tokenGenerated);
						model.setViewName("masters/association-nature"); 
						return model;
					}
					
					@RequestMapping(value={"/get-association-nature-details"}, method=RequestMethod.GET)
					public @ResponseBody ListPager<AssociationNature> NatureList(@RequestParam String pageNum, 
							@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
						logger.debug("execute() is executed ");		
						AssociationNatureTypeService natureDetails=new AssociationNatureTypeService();
						natureDetails.setPageNum(pageNum);
						natureDetails.setRecordsPerPage(recordsPerPage);
						natureDetails.setSortColumn(sortColumn);
						natureDetails.setSortOrder(sortOrder);
						natureDetails.initializeNatureList();
						ListPager<AssociationNature> natureListPager = new ListPager<AssociationNature>();
						natureListPager.setList(natureDetails.getNatureList()); 
						natureListPager.setTotalRecords(natureDetails.getTotalRecords()); 
						return natureListPager;
					}
					@RequestMapping(value={"/add-association-nature-details"}, method=RequestMethod.GET)
					public @ResponseBody List<String>  addcountryList(@RequestParam String natureName,@RequestParam String natureCode,@RequestParam String requestToken ) throws Exception{
						logger.debug("execute() is executed ");		
						AssociationNatureTypeService natureDetails=new AssociationNatureTypeService();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
					    natureDetails.setNatureName(natureName);
						natureDetails.setNatureCode(natureCode);
						natureDetails.AddNature();	
						token = getSessionToken(tokenKey);
						}
						else {
							natureDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();
						notificationList.add(mapper.writeValueAsString(natureDetails.getNotifyList()));
						notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
					}
			      @RequestMapping(value={"/edit-association-nature-details"}, method=RequestMethod.GET)
					public @ResponseBody List<String> editcountryList(@RequestParam String natureCode,@RequestParam String natureName,@RequestParam String requestToken) throws Exception{
						logger.debug("execute() is executed ");	
						AssociationNatureTypeService natureDetails=new AssociationNatureTypeService();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
						natureDetails.setNatureCode(natureCode);
						natureDetails.setNatureName(natureName);
						natureDetails.EditNature();
						token = getSessionToken(tokenKey);
						}
						else {
							natureDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						
						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();
						notificationList.add(mapper.writeValueAsString(natureDetails.getNotifyList()));
						notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
					}
	
				 
			@RequestMapping(value={"/delete-association-nature-details"}, method=RequestMethod.GET)
					public @ResponseBody List<String> deleteList(@RequestParam String natureCode,@RequestParam String natureName,@RequestParam String requestToken) throws Exception{
						logger.debug("execute() is executed ");			
						AssociationNatureTypeService natureDetails=new AssociationNatureTypeService();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
						natureDetails.setNatureCode(natureCode);
						natureDetails.setNatureName(natureName);
						natureDetails.DeleteNature();
						token = getSessionToken(tokenKey);
						}
						else {
							natureDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						
						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();				 
						notificationList.add(mapper.writeValueAsString(natureDetails.getNotifyList()));	
						notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
				}
			}
	
	
	
	



