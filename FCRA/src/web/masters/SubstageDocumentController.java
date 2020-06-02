package web.masters;
import java.util.ArrayList;
import java.util.List;

import models.master.SubstageDocument;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.SubstageDocumentServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

import com.fasterxml.jackson.databind.ObjectMapper;
@Controller

public class SubstageDocumentController extends TokenController {

	
			private final Logger logger = LoggerFactory.getLogger(Home.class);
			public String tokenKey="substage-document-details";
			
			@RequestMapping(value={"/substage-document-details"}, method=RequestMethod.GET)
			public ModelAndView submit(){
				logger.debug("execute() is executed ");	
				String tokenGenerated = generateAndSaveToken(tokenKey);
				SubstageDocumentServices sdsDetails=new SubstageDocumentServices();
				sdsDetails.execute();
				ModelAndView model = new ModelAndView();
				model.addObject("applicationsubTypeList", sdsDetails.getApplicationsubTypeList());
				model.addObject("proposalTypeList", sdsDetails.getProposalTypeList());
				model.addObject("documentTypeList", sdsDetails.getDocumentTypeList());
				model.addObject("requestToken", tokenGenerated);
				model.setViewName("masters/substage-document"); 
				return model;
			}
			
			
			@RequestMapping(value={"/get-substage-document-details"}, method=RequestMethod.GET)
			public @ResponseBody ListPager<SubstageDocument>substagedocTypeList(@RequestParam String pageNum, 
					@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
				logger.debug("execute() is executed ");		
				SubstageDocumentServices sdsDetails=new SubstageDocumentServices();
				sdsDetails.setPageNum(pageNum);
				sdsDetails.setRecordsPerPage(recordsPerPage);
				sdsDetails.setSortColumn(sortColumn);
				sdsDetails.setSortOrder(sortOrder);
				sdsDetails.initializeList();
				ListPager<SubstageDocument> appListPager = new ListPager<SubstageDocument>();
				appListPager.setList(sdsDetails.getSubstagedocumentList()); 
				appListPager.setTotalRecords(sdsDetails.getTotalRecords()); 
				return appListPager;
			}
			
			 @RequestMapping(value={"/add-substage-document-details"}, method=RequestMethod.GET)
			public @ResponseBody List<String>  adddescList(@RequestParam String substageDesc,@RequestParam String proposalDesc ,@RequestParam String documentDesc,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");		
				SubstageDocumentServices sdsDetails=new SubstageDocumentServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				sdsDetails.setSubstageDesc(substageDesc);
				sdsDetails.setProposalDesc(proposalDesc);
				sdsDetails.setDocumentDesc(documentDesc);
				sdsDetails.AddSubstageDocument();
				token = getSessionToken(tokenKey);
				}
				else {
					sdsDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();
				notificationList.add(mapper.writeValueAsString(sdsDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
			}
			
			 @RequestMapping(value={"/edit-substage-document-details"}, method=RequestMethod.GET)
			public @ResponseBody List<String> editList(@RequestParam String substageDesc,@RequestParam String proposalDesc ,@RequestParam String documentDesc,  @RequestParam String  rowId,@RequestParam String requestToken) throws Exception{
				logger.debug("execute() is executed ");	
				SubstageDocumentServices sdsDetails=new SubstageDocumentServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				sdsDetails.setSubstageDesc(substageDesc);
				sdsDetails.setProposalDesc(proposalDesc);
				sdsDetails.setDocumentDesc(documentDesc);
				sdsDetails.setRowId(rowId);
				sdsDetails.EditSubstageDocument();
				token = getSessionToken(tokenKey);
				}
				else {
					sdsDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();
				notificationList.add(mapper.writeValueAsString(sdsDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
			     return notificationList;
			}

		 
		@RequestMapping(value={"/delete-substage-document-details"}, method=RequestMethod.GET)
			public @ResponseBody List<String> deleteList(@RequestParam String  rowId,@RequestParam String requestToken ) throws Exception{
				logger.debug("execute() is executed ");			
				SubstageDocumentServices sdsDetails=new SubstageDocumentServices();
				Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
			    String token=null;
				if(result){
				sdsDetails.setRowId(rowId);
				sdsDetails.DeleteSubstageDocument();
				token = getSessionToken(tokenKey);
				}
				else {
					sdsDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
				}
				ObjectMapper mapper = new ObjectMapper();
				List<String> notificationList=new ArrayList<String>();				 
				notificationList.add(mapper.writeValueAsString(sdsDetails.getNotifyList()));
				notificationList.add(mapper.writeValueAsString(new List1(token)));
				return notificationList;
		}


	}


