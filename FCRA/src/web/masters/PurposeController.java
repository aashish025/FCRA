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
import service.masters.PurposeService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;
import web.TokenController;

@Controller
public class PurposeController extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "purpose-details";
		
	@RequestMapping(value={"/purpose-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		PurposeService purpose=new PurposeService();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/Purpose"); 
		return model;
	}
	
	@RequestMapping(value={"/get-purpose-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.Purpose> pullPurposeList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		PurposeService purpose = new PurposeService();
		purpose.setPageNum(pageNum);
		purpose.setRecordsPerPage(recordsPerPage);
		purpose.setSortColumn(sortColumn);
		purpose.setSortOrder(sortOrder);
		purpose.initializePurposeList();
		ListPager<models.master.Purpose> purposeListPager = new ListPager<models.master.Purpose>();
		purposeListPager.setList(purpose.getPurposeList());
		purposeListPager.setTotalRecords(purpose.getTotalRecords()); 
		return purposeListPager;
	}
	
	@RequestMapping(value={"/add-purpose-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addPurposeList(@RequestParam Integer purposeCode,@RequestParam String purposeName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		PurposeService purpose = new PurposeService();
		if(result) {
		purpose.setPurposeCode(purposeCode);
		purpose.setPurposeName(purposeName);
		purpose.initAddPurpose();
		token = getSessionToken(tokenKey);
		}
		else {
			purpose.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(purpose.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-purpose-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deletePurposeList(@RequestParam Integer purposeCode,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		PurposeService purpose = new PurposeService();
		if(result) {
		purpose.setPurposeCode(purposeCode);				
		purpose.initDeletePurpose();
		token = getSessionToken(tokenKey);
		}
		else {
			purpose.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(purpose.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;
	}

	@RequestMapping(value={"/edit-purpose-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editPurposeList(@RequestParam Integer purposeCode,@RequestParam String purposeName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		PurposeService purpose = new PurposeService();
		if(result) {
		purpose.setPurposeCode(purposeCode);
		purpose.setPurposeName(purposeName);
		purpose.initEditPurpose();
		token = getSessionToken(tokenKey);
		}
		else {
			purpose.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(purpose.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;	
	
	}
}