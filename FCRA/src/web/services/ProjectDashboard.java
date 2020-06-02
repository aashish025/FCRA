package web.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import models.services.ListPager;
import models.services.requests.AbstractRequest;
import models.services.requests.AnnualReturns;
import models.services.requests.Chat;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.services.ApplicationTrackingService;
import service.services.dashboard.ProjectDashboardService;
import utilities.ValidationException;
import utilities.lists.List2;
import utilities.lists.List3;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;

@Controller
public class ProjectDashboard extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private ProjectDashboardService pds=null;
	//private String actionStatus=null;
	private final String tokenKey = "workspace";	
	@RequestMapping(value={"/workspace"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();          
		ServletContext context = attr.getRequest().getSession().getServletContext();
		String version = context.getInitParameter("version");
		if(ESAPI.validator().isValidInput("version", version, "version", 6, false) == false){
			throw new ValidationException("version- Invalid entry. Only  numbers and . allowed (6 characters max)");
		}
		ModelAndView model = new ModelAndView();
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.initDashboardAction();
		ObjectMapper mapper = new ObjectMapper();		
		model.addObject("sectionList", pds.getOfficeSectionList());
		model.addObject("serviceList", pds.getOfficeServiceList());
		model.addObject("stateList", pds.getStateList());
		model.addObject("officeUserList", pds.getOfficeUserList());
		model.addObject("myDetails", mapper.writeValueAsString(pds.getMyDetails()));
		model.addObject("recordsPendingForMail", mapper.writeValueAsString(pds.getTotalRecords()));
		model.addObject("requestToken", tokenGenerated);
		model.addObject("version", version);
		model.setViewName("services/dashboard/project-dashboard");		
		return model;
	}
	@RequestMapping(value={"/init-workspace"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initDashboard(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String stage
			,@RequestParam String state,@RequestParam String section,@RequestParam String service,@RequestParam String user) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setPageNum(pageNum);
		pds.setRecordsPerPage(recordsPerPage);
		pds.setSortColumn(sortColumn);
		pds.setSortOrder(sortOrder);
		pds.setStage(stage);
		pds.setState(state);
		pds.setSectionId(section);
		pds.setService(service);
		pds.setOfficeUser(user);
		pds.initDashboard();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(pds.getApplicationList());
		ar.setTotalRecords(pds.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/init-office-workspace"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initOfficeDashboard(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String stage
			,@RequestParam String state,@RequestParam String section,@RequestParam String service,@RequestParam String user) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setPageNum(pageNum);
		pds.setRecordsPerPage(recordsPerPage);
		pds.setSortColumn(sortColumn);
		pds.setSortOrder(sortOrder);
		pds.setStage(stage);
		pds.setState(state);
		pds.setSectionId(section);
		pds.setService(service);
		pds.setOfficeUser(user);
		pds.initOfficeDashboard();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(pds.getApplicationList());
		ar.setTotalRecords(pds.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/get-section-filtered-service-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<List2> getSectionFilteredServices(@RequestParam String section) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();		
		pds.setSectionId(section);		
		pds.initSectionFilteredService();		 
		return pds.getOfficeServiceList();
	}
	@RequestMapping(value={"/get-application-list-workspace"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
			,@RequestParam String applicationName) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setPageNum(pageNum);
		pds.setRecordsPerPage(recordsPerPage);
		pds.setSortColumn(sortColumn);
		pds.setSortOrder(sortOrder);	
		pds.setAppId(applicationId.toUpperCase());
		pds.setAppName(applicationName.toUpperCase());
		pds.initApplicationListDetails();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(pds.getApplicationList());
		ar.setTotalRecords(pds.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/get-application-details-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getApplicationDetails(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId.toUpperCase());
		pds.initGetApplicationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> details=new ArrayList<String>();	
		details.add(mapper.writeValueAsString(pds.getApplicationList()));
		details.add(mapper.writeValueAsString(pds.getNotifyList()));
		details.add(mapper.writeValueAsString(pds.getDoExist()));
		details.add(mapper.writeValueAsString(pds.getDoAccept()));
		details.add(mapper.writeValueAsString(pds.getWithMe()));
		details.add(mapper.writeValueAsString(pds.getMySection()));
		details.add(mapper.writeValueAsString(pds.getWithMeAnswered()));
		details.add(mapper.writeValueAsString(pds.getWithMeToAnswer()));
		details.add(mapper.writeValueAsString(pds.getWithMeToMail()));
		details.add(mapper.writeValueAsString(pds.getNotWithMe()));
		details.add(mapper.writeValueAsString(pds.getNotWithMeToFetch()));
		details.add(pds.getFinalStatus());
		details.add(mapper.writeValueAsString(pds.getMarkOffice()));
		return details;
	}
	
	@RequestMapping(value={"/get-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getChat(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId);
		pds.initChat();	
		// Getting documents data
		ApplicationTrackingService tracker = new ApplicationTrackingService();
		tracker.setApplicationId(appId.toUpperCase());
		tracker.pullApplicationDetails();
		String documentDetails = tracker.getApplicationDetails();
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getChatList()));		
		chatDetails.add(mapper.writeValueAsString(pds.getForwardOfficeList()));
		chatDetails.add(mapper.writeValueAsString(pds.getForwardUserList()));
		chatDetails.add(mapper.writeValueAsString(pds.getForwardOtherUserList()));
		chatDetails.add(mapper.writeValueAsString(pds.getMyDetails()));
		chatDetails.add(mapper.writeValueAsString(pds.getChatAttachmentList()));
		chatDetails.add(mapper.writeValueAsString(pds.getOfficeStatusList()));				
		chatDetails.add(mapper.writeValueAsString(pds.getOfficeSectionList()));
		chatDetails.add(mapper.writeValueAsString(pds.getDaysCount()));
		chatDetails.add(mapper.writeValueAsString(pds.getRunningStatus()));
		chatDetails.add(mapper.writeValueAsString(pds.getApplicationFinalStatus()));		
		chatDetails.add(documentDetails);
		chatDetails.add(pds.getValidityFrom());
		chatDetails.add(pds.getValidityUpTo());		
		chatDetails.add(pds.getCurrentUser());
		chatDetails.add(pds.getRedFlag());
		chatDetails.add(mapper.writeValueAsString(pds.getMatchingRedFlagAssociationList()));
		chatDetails.add(mapper.writeValueAsString(pds.getMatchingRedFlagDonorList()));
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(pds.getGrantingOfficialFlag());
		chatDetails.add(pds.getProcessingOfficialFlag());
		chatDetails.add(pds.getClarificationReminderFlag());
		chatDetails.add(pds.getCurrentDate());
		chatDetails.add(pds.getPpAmount());
		chatDetails.add(pds.getPpAmountDesc());
		chatDetails.add(pds.getPpAmountCurrency());
		chatDetails.add(mapper.writeValueAsString(pds.getOfficeMarkList()));
		chatDetails.add(pds.getClarificationOfficialFlag());
		System.out.println("@@@@@@ "+chatDetails);
		return chatDetails;
	}
	@RequestMapping(value={"/get-section-users-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getSectionUsers(@RequestParam String sectionId) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setSectionId(sectionId);
		pds.initSectionUsers();		 
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();		
		chatDetails.add(mapper.writeValueAsString(pds.getForwardUserList()));
		chatDetails.add(mapper.writeValueAsString(pds.getForwardOtherUserList()));		
		return chatDetails;
	}
	@RequestMapping(value={"/get-office-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getOfficeResources(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId);
		
		// Getting documents data
		ApplicationTrackingService tracker = new ApplicationTrackingService();
		tracker.setApplicationId(appId.toUpperCase());
		tracker.pullApplicationDetails();
		String documentDetails = tracker.getApplicationDetails();
		
		pds.initOfficeResources();		 
		ObjectMapper mapper = new ObjectMapper();
		List<String> resourcesDetails=new ArrayList<String>();	
		resourcesDetails.add(mapper.writeValueAsString(pds.getForwardUserList()));	
		resourcesDetails.add(documentDetails);
		resourcesDetails.add(mapper.writeValueAsString(pds.getChatList()));
		resourcesDetails.add(mapper.writeValueAsString(pds.getChatAttachmentList()));
		return resourcesDetails;
	}
	@RequestMapping(value={"/accept-app-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> acceptApplication(@RequestParam String appId,@RequestParam String user) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId);
		pds.setForwardUser(user);		
		String actionStatus=pds.initAcceptApplication();
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		return chatDetails;
	}
	@RequestMapping(value={"/pull-app-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> pullApplication(@RequestParam String appId,@RequestParam String user,@RequestParam String remark) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId);
		pds.setForwardUser(user);	
		pds.setStatusRemark(remark);
		String actionStatus=pds.initPullApplication();
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-user-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitForwardUser(@RequestParam String appId,@RequestParam String user,@RequestParam String remark,
			@RequestParam String sectionId, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");
		//COMPARE TOKEN 
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);
			pds.setForwardUser(user);
			pds.setStatusRemark(remark);
			pds.setSectionId(sectionId);
			actionStatus=pds.initSubmitForwardUserChatDetails();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"userModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-user-note-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitUserNote(@RequestParam String appId,@RequestParam String remark,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);
			actionStatus=pds.initSubmitUserNoteChatDetails();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"noteModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-user-clarification-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitUserClarification(@RequestParam String appId,@RequestParam String remark, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);
			actionStatus=pds.initSubmitUserClarificationChatDetails();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent  is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"clarificationModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-showcause-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitShowCause(@RequestParam String appId,@RequestParam String noticeSubject,@RequestParam String noticeBody
			, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setNoticeSubject(noticeSubject);
			pds.setNoticeBody(noticeBody);
			actionStatus=pds.initSubmitShowCause();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent  is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"clarificationModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-user-onhold-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitUserOnHoldDetails(@RequestParam String appId,@RequestParam String remark, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);
			actionStatus=pds.initSubmitUserOnHoldChatDetails();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!","Request you have sent  is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.",Status.WARNING,Type.BAR,"on-hold-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-resume-process-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitResumeProcessDetails(@RequestParam String appId,@RequestParam String remark, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);
			actionStatus=pds.initSubmitResumeProcessDetails();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!","Request you have sent  is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.",Status.WARNING,Type.BAR,"resume-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-office-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitForwardOffice(@RequestParam String appId,@RequestParam String office,@RequestParam String remark,
			@RequestParam(required=false) MultipartFile[] files, @RequestParam(required=false) String[] ids, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);
			pds.setForwardOffice(office);
			pds.setStatusRemark(remark);
			pds.setStageFiles(files);
			pds.setStageFilesIds(ids);
			actionStatus=pds.initSubmitForwardOfficeChatDetails();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"officeModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-reject-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitReject(@RequestParam String appId,@RequestParam String remark, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");		
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);		
			actionStatus=pds.initSubmitReject();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"rejectModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(pds.getDeniedStatus());
		chatDetails.add(mapper.writeValueAsString(pds.getEmailNotifyList()));
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-close-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitClose(@RequestParam String appId,@RequestParam String remark,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);		
			actionStatus=pds.initSubmitClose();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"closeModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);		
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-reminder-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitReminder(@RequestParam String appId,@RequestParam String remark,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);		
			actionStatus=pds.initSubmitReminder();
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"closeModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);		
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/submit-next-stage-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitNextStage(@RequestParam String appId,@RequestParam String remark,@RequestParam String pdfFormat,
			@RequestParam String validityFrom, @RequestParam(required=false) String redFlagClearingRemarks, @RequestParam String requestToken,@RequestParam String installments,
			@RequestParam String installmentNumbers,@RequestParam String ppInsFlag,@RequestParam(required=false) String otherRemarks) throws Exception{
		logger.debug("execute() is executed ");	
		//COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus=null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);
			pds.setHosPdfFormat(pdfFormat);
			pds.setValidityFrom(validityFrom);		
			pds.setRedFlagClearingRemarks(redFlagClearingRemarks);
			pds.setInstallments(installments);
			pds.setInstallmentNumbers(installmentNumbers);
			pds.setPpInsFlag(ppInsFlag);
			pds.setOtherRemark(otherRemarks);
			actionStatus=pds.initSubmitApprove();	
			token = getSessionToken(tokenKey);
		}else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"nextStageModal-error"));
		}		
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);
		chatDetails.add(pds.getGrantStatus());
		chatDetails.add(pds.getRegistrationNumber());
		/*if(pds.getRegSecStatus()==true || pds.getPdfStatus()==true)
			chatDetails.add(Base64.encodeBytes(pds.getPdfBytes()));*/
		chatDetails.add(mapper.writeValueAsString(pds.getEmailNotifyList()));	
		chatDetails.add(pds.getRedFlag());
		chatDetails.add(token);
		return chatDetails;
	}
	@RequestMapping(value={"/get-substage-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> getSubStageChat(@RequestParam String appId,@RequestParam String subStageId) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId);	
		pds.setSubStageId(subStageId);
		pds.initSubStageChat();				
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getChatList()));
		chatDetails.add(mapper.writeValueAsString(pds.getChatAttachmentList()));
		return chatDetails;
	}
	@RequestMapping(value={"/get-project-documents-chat-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<List3> getProjectDocuments(@RequestParam String appId) throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();
		pds.setAppId(appId);		
		pds.initProjectDocuments();		
		return pds.getProjDocList();
	}
	@RequestMapping(value={"/delete-upload-cache-workspace"}, method=RequestMethod.POST)
	public @ResponseBody String initDeleteUploadCache() throws Exception{
		logger.debug("execute() is executed ");		
		ProjectDashboardService pds = new ProjectDashboardService();				
		String status=pds.initDeleteUploadCache();		
		return status;
	}
	
	@RequestMapping(value={"/mark-office-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitMarkOffice(@RequestParam String appId,@RequestParam String remark,@RequestParam String markOfficeId, @RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		// COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String actionStatus = null;
		ProjectDashboardService pds = new ProjectDashboardService();
		if(result) {
			pds.setAppId(appId);		
			pds.setStatusRemark(remark);		
			actionStatus=pds.initSubmitMarkOffice(markOfficeId);
			token = getSessionToken(tokenKey);
		} else{
			pds.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.BAR,"closeModal-error"));
		}
		ObjectMapper mapper = new ObjectMapper();
		List<String> chatDetails=new ArrayList<String>();				 
		chatDetails.add(mapper.writeValueAsString(pds.getNotifyList()));
		chatDetails.add(actionStatus);		
		chatDetails.add(token);
		return chatDetails;
	}
	
}
