package web.masters;
import java.util.ArrayList;
import java.util.List;

import models.master.Country;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.CountryTypeDetailsServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
	@Controller
	class CountryDetails extends TokenController{
                 	private final Logger logger = LoggerFactory.getLogger(Home.class);
                 	public String tokenKey="country-details";
					@RequestMapping(value={"/country-details"}, method=RequestMethod.GET)
					public ModelAndView submit(){
						logger.debug("execute() is executed ");	
						String tokenGenerated = generateAndSaveToken(tokenKey);
						CountryTypeDetailsServices countryDetails=new CountryTypeDetailsServices();
						countryDetails.execute();
						ModelAndView model = new ModelAndView();
						model.addObject("countryTypeList", countryDetails.getCountryList());
						model.addObject("requestToken", tokenGenerated);
						model.setViewName("masters/country"); 
						return model;
					}
					
					@RequestMapping(value={"/get-country-details"}, method=RequestMethod.GET)
					public @ResponseBody ListPager<Country> pullcountryTypeList(@RequestParam String pageNum, 
							@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
						logger.debug("execute() is executed ");		
						CountryTypeDetailsServices	countryDetails=new CountryTypeDetailsServices();
						countryDetails.setPageNum(pageNum);
						countryDetails.setRecordsPerPage(recordsPerPage);
						countryDetails.setSortColumn(sortColumn);
						countryDetails.setSortOrder(sortOrder);
						countryDetails.initializeCountryList();
						ListPager<Country> countryListPager = new ListPager<Country>();
						countryListPager.setList(countryDetails.getCountryList()); 
						countryListPager.setTotalRecords(countryDetails.getTotalRecords()); 
						return countryListPager;
					}
					@RequestMapping(value={"/add-country-details"}, method=RequestMethod.GET)
					public @ResponseBody List<String>  addcountryList(@RequestParam String countryCode,@RequestParam String countryName,@RequestParam String requestToken ) throws Exception{
						logger.debug("execute() is executed ");
						CountryTypeDetailsServices countryDetails = new CountryTypeDetailsServices();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
							
						countryDetails.setCountryCode(countryCode);
						countryDetails.setCountryName(countryName);
						countryDetails.AddCountry();	
						token = getSessionToken(tokenKey);
						}
						else {
							countryDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						

						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();
						notificationList.add(mapper.writeValueAsString(countryDetails.getNotifyList()));
						notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
					}
			      @RequestMapping(value={"/edit-country-details"}, method=RequestMethod.GET)
					public @ResponseBody List<String> editcountryList(@RequestParam String countryCode,@RequestParam String countryName,@RequestParam String requestToken) throws Exception{
						logger.debug("execute() is executed ");
						CountryTypeDetailsServices 	countryDetails = new CountryTypeDetailsServices();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
							
						countryDetails.setCountryCode(countryCode);
						countryDetails.setCountryName(countryName);
						countryDetails.editcountryName();
						countryDetails.AddCountry();	
						token = getSessionToken(tokenKey);
						}
						else {
							countryDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();
							
					    notificationList.add(mapper.writeValueAsString(countryDetails.getNotifyList()));
					    notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
					}
	
				 
			@RequestMapping(value={"/delete-country-details"}, method=RequestMethod.GET)
					public @ResponseBody List<String> deleteList(@RequestParam String countryCode,@RequestParam String countryName,@RequestParam String requestToken) throws Exception{
						logger.debug("execute() is executed ");			
						CountryTypeDetailsServices countryDetails = new CountryTypeDetailsServices();
						Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
					    String token=null;
						if(result){
					    countryDetails.setCountryCode(countryCode);
						countryDetails.setCountryName(countryName);
						countryDetails.initDeleteCountry();
						token = getSessionToken(tokenKey);
						}
						else {
							countryDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
						}
						ObjectMapper mapper = new ObjectMapper();
						List<String> notificationList=new ArrayList<String>();				 
						notificationList.add(mapper.writeValueAsString(countryDetails.getNotifyList()));	
						notificationList.add(mapper.writeValueAsString(new List1(token)));
						return notificationList;
				}
			}
	
	
	
	
