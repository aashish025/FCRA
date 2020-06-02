package web.masters;
import java.util.ArrayList;
import java.util.List;

import models.master.ContributionNature;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.ContributionNatureTypeService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
	@Controller
	public class ContributionNatureController extends TokenController {

	                 	private final Logger logger = LoggerFactory.getLogger(Home.class);
	                 	public String tokenKey="contribution-nature-details";
						
						@RequestMapping(value={"/contribution-nature-details"}, method=RequestMethod.GET)
						public ModelAndView submit(){
							logger.debug("execute() is executed ");		
							String tokenGenerated = generateAndSaveToken(tokenKey);
							ContributionNatureTypeService contDetails=new ContributionNatureTypeService();
							contDetails.execute();
							ModelAndView model = new ModelAndView();
							model.addObject("contributionTypeList", contDetails.getContributionList());		
							model.addObject("requestToken", tokenGenerated);
							model.setViewName("masters/contribution-nature"); 
							return model;
						}
						
						@RequestMapping(value={"/get-contribution-nature-details"}, method=RequestMethod.GET)
						public @ResponseBody ListPager<ContributionNature> NatureList(@RequestParam String pageNum, 
								@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
							logger.debug("execute() is executed ");		
							ContributionNatureTypeService contDetails=new ContributionNatureTypeService();
							contDetails.setPageNum(pageNum);
							contDetails.setRecordsPerPage(recordsPerPage);
							contDetails.setSortColumn(sortColumn);
							contDetails.setSortOrder(sortOrder);
							contDetails.initializeContributionList();
							ListPager<ContributionNature> contListPager = new ListPager<ContributionNature>();
							contListPager.setList(contDetails.getContributionList()); 
							contListPager.setTotalRecords(contDetails.getTotalRecords()); 
							return contListPager;
						}
						@RequestMapping(value={"/add-contribution-nature-details"}, method=RequestMethod.GET)
						public @ResponseBody List<String>  addList(@RequestParam String contributionType,@RequestParam String contributionName,@RequestParam String requestToken ) throws Exception{
							logger.debug("execute() is executed ");		
							ContributionNatureTypeService contDetails=new ContributionNatureTypeService();
							Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
						    String token=null;
							if(result){
						    contDetails.setContributionType(contributionType);
							contDetails.setContributionName(contributionName);
							contDetails.AddContribution();
							token = getSessionToken(tokenKey);
							}
							else {
								contDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
							}
							
							ObjectMapper mapper = new ObjectMapper();
							List<String> notificationList=new ArrayList<String>();
							notificationList.add(mapper.writeValueAsString(contDetails.getNotifyList()));
							notificationList.add(mapper.writeValueAsString(new List1(token)));
							return notificationList;
						}
				      @RequestMapping(value={"/edit-contribution-nature-details"}, method=RequestMethod.GET)
						public @ResponseBody List<String> editcountryList(@RequestParam String contributionType,@RequestParam String contributionName,@RequestParam String requestToken) throws Exception{
							logger.debug("execute() is executed ");	
							ContributionNatureTypeService contDetails=new ContributionNatureTypeService();
							Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
						    String token=null;
							if(result){
						    contDetails.setContributionType(contributionType);
							contDetails.setContributionName(contributionName);
							contDetails.EditContribution();
							token = getSessionToken(tokenKey);
							}
							else {
								contDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
							}
							

							ObjectMapper mapper = new ObjectMapper();
							List<String> notificationList=new ArrayList<String>();
							notificationList.add(mapper.writeValueAsString(contDetails.getNotifyList()));
							notificationList.add(mapper.writeValueAsString(new List1(token)));
							return notificationList;
						}
		
					 
				@RequestMapping(value={"/delete-contribution-nature-details"}, method=RequestMethod.GET)
						public @ResponseBody List<String> deleteList(@RequestParam String contributionType,@RequestParam String contributionName,@RequestParam String requestToken) throws Exception {
							logger.debug("execute() is executed ");			
							ContributionNatureTypeService contDetails=new ContributionNatureTypeService();
							Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
						    String token=null;
							if(result){
						    contDetails.setContributionType(contributionType);
							contDetails.setContributionName(contributionName);
							contDetails.DeleteContribution();
							token = getSessionToken(tokenKey);
							}
							else {
								contDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
							}
							

							ObjectMapper mapper = new ObjectMapper();
							List<String> notificationList=new ArrayList<String>();				 
							notificationList.add(mapper.writeValueAsString(contDetails.getNotifyList()));	
							notificationList.add(mapper.writeValueAsString(new List1(token)));
							return notificationList;
			

				}
	}		
		
		









