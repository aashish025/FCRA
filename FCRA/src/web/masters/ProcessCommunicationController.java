package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.ProcessCommunicationStage;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.ProcessCommunicationService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
@Controller


public class ProcessCommunicationController extends TokenController {

		private final Logger logger = LoggerFactory.getLogger(Home.class);
		public String tokenKey="process-communication-stage-details";
		@RequestMapping(value={"/process-communication-stage-details"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");	
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ProcessCommunicationService	pcDetails=new ProcessCommunicationService();
			pcDetails.execute();
			ModelAndView model = new ModelAndView();
			model.addObject("processcommunicationTypeList", pcDetails.getProcesscommunicationList());	
			model.addObject("requestToken", tokenGenerated);
			model.setViewName("masters/process-communication"); 
			return model;
		}
		
	@RequestMapping(value={"/get-process-communication-stage-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<ProcessCommunicationStage> pullPcTypeList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			ProcessCommunicationService pcDetails=new ProcessCommunicationService();
			pcDetails.setPageNum(pageNum);
			pcDetails.setRecordsPerPage(recordsPerPage);
			pcDetails.setSortColumn(sortColumn);
			pcDetails.setSortOrder(sortOrder);
			pcDetails.initializePcList();
			ListPager<ProcessCommunicationStage> appListPager = new ListPager<ProcessCommunicationStage>();
			appListPager.setList(pcDetails.getProcesscommunicationList()); 
			appListPager.setTotalRecords(pcDetails.getTotalRecords()); 
			return appListPager;
		}
		@RequestMapping(value={"/add-process-communication-stage-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  adddescList(@RequestParam String stageDesc,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");		
			ProcessCommunicationService pcDetails=new ProcessCommunicationService();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
			if(result){
			pcDetails.setStageDesc(stageDesc);
			pcDetails.AddPc();	
			token = getSessionToken(tokenKey);
			}
			else {
				pcDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(pcDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
		}
		
		@RequestMapping(value={"/edit-process-communication-stage-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editList(@RequestParam String stageDesc, String stageId,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");	
			ProcessCommunicationService	pcDetails=new ProcessCommunicationService();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
			if(result){
			pcDetails.setStageDesc(stageDesc);
			pcDetails.setStageId(stageId);
			pcDetails.EditPc();	
			token = getSessionToken(tokenKey);
			}
			else {
				pcDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(pcDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
		     return notificationList;
		}

	 
	@RequestMapping(value={"/delete-process-communication-stage-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteList(@RequestParam String stageDesc, String stageId,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");			
			ProcessCommunicationService	pcDetails=new ProcessCommunicationService();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
			if(result){
			pcDetails.setStageDesc(stageDesc);
			pcDetails.setStageId(stageId);
			pcDetails.DeletePc();
			token = getSessionToken(tokenKey);
			}
			else {
				pcDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(pcDetails.getNotifyList()));	
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
	}

	}


