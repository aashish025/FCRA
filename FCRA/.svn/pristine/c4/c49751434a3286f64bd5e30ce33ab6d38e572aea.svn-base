package web.masters;
import java.util.ArrayList;
import java.util.List;

import models.master.PcSection;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.PcSectionTypeService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.Home;
import web.TokenController;
@Controller
public class PcSectionController extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private String tokenKey="office-section-details";
	
	@RequestMapping(value={"/office-section-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");		
		String tokenGenerated = generateAndSaveToken(tokenKey);
		PcSectionTypeService pcSection=new PcSectionTypeService();
		pcSection.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("pcSectionTypeList", pcSection.getPcsectionList());
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/pc-section"); 
		return model;
	}
	
	@RequestMapping(value={"/get-office-section-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<PcSection> sectionTypeList(@RequestParam String pageNum, @RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception {
		logger.debug("execute() is executed ");		
		PcSectionTypeService pcSection=new PcSectionTypeService();
        pcSection.setPageNum(pageNum);
		pcSection.setRecordsPerPage(recordsPerPage);
		pcSection.setSortColumn(sortColumn);
		pcSection.setSortOrder(sortOrder);
		pcSection.initializePcSectionList();
		ListPager<PcSection> pcListPager = new ListPager<PcSection>();
		pcListPager.setList(pcSection.getPcsectionList()); 
		pcListPager.setTotalRecords(pcSection.getTotalRecords()); 
        return pcListPager;
	}
	@RequestMapping(value={"/create-office-section-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> createSection(@RequestParam String sectionName,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		PcSectionTypeService pcSection=new PcSectionTypeService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		pcSection.setSectionName(sectionName);	
		pcSection.initCreatePcSection();	
		token = getSessionToken(tokenKey);
		}
		else {
			pcSection.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> pcsection=new ArrayList<String>();				 
		pcsection.add(mapper.writeValueAsString(pcSection.getNotifyList()));
		pcsection.add(mapper.writeValueAsString(pcSection.getSectionId()));
		pcsection.add(mapper.writeValueAsString(new List1(token)));
	   return pcsection;
	}
	@RequestMapping(value={"/get-service-office-section-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getService(@RequestParam String sectionId) throws Exception{
		logger.debug("execute() is executed ");		
		PcSectionTypeService pcSection=new PcSectionTypeService();
		pcSection.setSectionId(sectionId);	
		pcSection.initService();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> pcsectionList=new ArrayList<String>();				 
		pcsectionList.add(mapper.writeValueAsString(pcSection.getAvailableseviceList()));
	    pcsectionList.add(mapper.writeValueAsString(pcSection.getAssignedserviceList()));
		return pcsectionList;
	}
	@RequestMapping(value={"/save-office-section-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> saveServices(@RequestParam String sectionId, @RequestParam String aspl,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		PcSectionTypeService pcSection=new PcSectionTypeService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		pcSection.setSectionId(sectionId);	
		pcSection.setAssignedService(aspl);
		pcSection.initSaveServices();	
		token = getSessionToken(tokenKey);
		}
		else {
			pcSection.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusList=new ArrayList<String>();				 
		statusList.add(mapper.writeValueAsString(pcSection.getNotifyList()));	
		statusList.add(mapper.writeValueAsString(new List1(token)));
		return statusList;
	}
	
	@RequestMapping(value={"/edit-office-section-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editNotificationList(@RequestParam String sectionName, @RequestParam String sectionId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");			
		PcSectionTypeService pcSection=new PcSectionTypeService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		pcSection.setSectionId(sectionId);	
		pcSection.setSectionName(sectionName);
		pcSection.editSection();
		token = getSessionToken(tokenKey);
		}
		else {
			pcSection.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
	    notificationList.add(mapper.writeValueAsString(pcSection.getNotifyList()));
	    notificationList.add(mapper.writeValueAsString(new List1(token)));
	    return notificationList;
	}
	
	
	@RequestMapping(value={"/delete-office-section-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> deleteUser(@RequestParam String sectionId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		PcSectionTypeService pcSection=new PcSectionTypeService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
		if(result){
		pcSection.setSectionId(sectionId);		
		pcSection.initDeleteSection();
		 token = getSessionToken(tokenKey);
		}
		else {
			pcSection.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> pcsection=new ArrayList<String>();				 
		pcsection.add(mapper.writeValueAsString(pcSection.getNotifyList()));		
		pcsection.add(mapper.writeValueAsString(new List1(token)));
		return pcsection;
	}

}
