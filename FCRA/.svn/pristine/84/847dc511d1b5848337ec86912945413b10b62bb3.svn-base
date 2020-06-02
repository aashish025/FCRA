package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.Currency;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.CurrencyTypeDetailServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

import com.fasterxml.jackson.databind.ObjectMapper;












import web.Home;
import web.TokenController;

@Controller
public class CurrencyDetails extends TokenController
{
			 private final Logger logger = LoggerFactory.getLogger(Home.class);
			 public String tokenKey="currency";
			@RequestMapping(value={"/currency"}, method=RequestMethod.GET)
			public ModelAndView submit(){
				logger.debug("execute() is executed ");	
				String tokenGenerated = generateAndSaveToken(tokenKey);
				CurrencyTypeDetailServices	currencyDetails=new CurrencyTypeDetailServices();
				currencyDetails.execute();
				ModelAndView model = new ModelAndView();
				model.addObject("currencyTypeList", currencyDetails.getCurrencyList());
				model.addObject("requestToken", tokenGenerated);
				model.setViewName("masters/currency"); 
				return model;
			}
			
			@RequestMapping(value={"/get-currency"}, method=RequestMethod.GET)
			public @ResponseBody ListPager<Currency> pullBoundaryTypeList(@RequestParam String pageNum, 
					@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
				logger.debug("execute() is executed ");		
				CurrencyTypeDetailServices currencyDetails = new CurrencyTypeDetailServices();
				currencyDetails.setPageNum(pageNum);
				currencyDetails.setRecordsPerPage(recordsPerPage);
				currencyDetails.setSortColumn(sortColumn);
				currencyDetails.setSortOrder(sortOrder);
				currencyDetails.initializeCurrencyList();
				ListPager<Currency> currencyListPager = new ListPager<Currency>();
				currencyListPager.setList(currencyDetails.getCurrencyList()); 
				currencyListPager.setTotalRecords(currencyDetails.getTotalRecords()); 
				return currencyListPager;
			}
			@RequestMapping(value={"/add-currency"}, method=RequestMethod.GET)
			public @ResponseBody List<String>  addcurrencyList(@RequestParam String currencyCode,@RequestParam String currencyName,@RequestParam String requestToken ) throws Exception{
				logger.debug("execute() is executed ");		
				CurrencyTypeDetailServices currencyDetails = new CurrencyTypeDetailServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				currencyDetails.setCurrencyCode(currencyCode);
				currencyDetails.setCurrencyName(currencyName);
				currencyDetails.AddCurrency();	
				token = getSessionToken(tokenKey);
				}
				else {
					currencyDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();
				notificationList.add(mapper.writeValueAsString(currencyDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
			}
		@RequestMapping(value={"/edit-currency"}, method=RequestMethod.GET)
			public @ResponseBody List<String> editList(@RequestParam String currencyCode,@RequestParam String currencyName,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");	
				CurrencyTypeDetailServices 	currencyDetails = new CurrencyTypeDetailServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				currencyDetails.setCurrencyCode(currencyCode);
				currencyDetails.setCurrencyName(currencyName);
				currencyDetails.editcurrencyName();
				token = getSessionToken(tokenKey);
				}
				else {
					currencyDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();
					
			    notificationList.add(mapper.writeValueAsString(currencyDetails.getNotifyList()));
			    notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
			}

		 
	@RequestMapping(value={"/delete-currency"}, method=RequestMethod.GET)
			public @ResponseBody List<String> deleteList(@RequestParam String currencyCode,@RequestParam String currencyName,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");			
				CurrencyTypeDetailServices currencyDetails = new CurrencyTypeDetailServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				currencyDetails.setCurrencyCode(currencyCode);
				currencyDetails.setCurrencyName(currencyName);
	     		currencyDetails.initDeleteCurrency();
	     		token = getSessionToken(tokenKey);
				}
				else {
					currencyDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();				 
				notificationList.add(mapper.writeValueAsString(currencyDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
		}
	}


