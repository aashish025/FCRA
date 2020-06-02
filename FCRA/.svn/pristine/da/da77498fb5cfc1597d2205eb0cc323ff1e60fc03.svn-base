package web.masters;

import java.util.ArrayList;
import java.util.List;
import models.services.ListPager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import service.masters.BanksService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;
import web.TokenController;

@Controller
public class BanksController extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "bank-details";
		
	@RequestMapping(value={"/bank-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		BanksService banks=new BanksService();
		ModelAndView model = new ModelAndView();	
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/Banks"); 
		return model;
	}
	
	@RequestMapping(value={"/get-bank-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.Banks> pullBankList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		BanksService banks = new BanksService();
		banks.setPageNum(pageNum);
		banks.setRecordsPerPage(recordsPerPage);
		banks.setSortColumn(sortColumn);
		banks.setSortOrder(sortOrder);
		banks.initializeBankList();
		ListPager<models.master.Banks> bankListPager = new ListPager<models.master.Banks>();
		bankListPager.setList(banks.getBanksList());
		bankListPager.setTotalRecords(banks.getTotalRecords()); 
		return bankListPager;
	}
	
	@RequestMapping(value={"/add-bank-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addBankList(@RequestParam String bankName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		BanksService banks = new BanksService();
		if(result) {
		banks.setBankName(bankName);				
		banks.initAddBank();
		token = getSessionToken(tokenKey);
		}
		else {
			banks.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(banks.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-bank-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteBankList(@RequestParam Integer bankCode, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		BanksService banks = new BanksService();
		if(result) {
		banks.setBankCode(bankCode);				
		banks.initDeleteBank();
		token = getSessionToken(tokenKey);
		}
		else {
			banks.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(banks.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-bank-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editBankList(@RequestParam Integer bankCode,@RequestParam String bankName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		BanksService banks = new BanksService();
		if(result) {
		banks.setBankCode(bankCode);
		banks.setBankName(bankName);
		banks.initEditBank();
		token = getSessionToken(tokenKey);
		}
		else {
			banks.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(banks.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	
	}
}