package web.masters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.services.ListPager;
import models.services.Notification;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.masters.NotificationDetailsService;
import web.Home;

@Controller
public class NotificationDetails {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private NotificationDetailsService notificationDetails;
	
	@RequestMapping(value={"/notification-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");			
		NotificationDetailsService notificationDetails=new NotificationDetailsService();
		notificationDetails.execute();
		ModelAndView model = new ModelAndView();
		model.addObject("notificationTypeList", notificationDetails.getNotificationTypeList());		
		model.setViewName("masters/notification-details"); 
		return model;
	}
	
	@RequestMapping(value={"/get-notification-list-notification-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<Notification> pullNotificationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		NotificationDetailsService notificationDetails = new NotificationDetailsService();
		notificationDetails.setPageNum(pageNum);
		notificationDetails.setRecordsPerPage(recordsPerPage);
		notificationDetails.setSortColumn(sortColumn);
		notificationDetails.setSortOrder(sortOrder);
		notificationDetails.initializeNotificationList();
		ListPager<Notification> notificationListPager = new ListPager<Notification>();
		notificationListPager.setList(notificationDetails.getNotificationList());
		notificationListPager.setTotalRecords(notificationDetails.getTotalRecords()); 
		return notificationListPager;
	}
	
	@RequestMapping(value={"/add-notification-details"}, method=RequestMethod.POST)
	public ModelAndView  addNotificationList(@RequestParam String notificationType, 
			@RequestParam String notificationTitle, @RequestParam String notificationDetail) throws Exception{
		logger.debug("execute() is executed ");		
		NotificationDetailsService notificationDetails = new NotificationDetailsService();
		notificationDetails.setNotificationType(notificationType);
		notificationDetails.setNotificationTitle(notificationTitle);
		notificationDetails.setNotificationDetails(notificationDetail);			
		notificationDetails.initAddNotification();
		notificationDetails.execute();		
		ModelAndView model = new ModelAndView();
		model.addObject("notificationTypeList", notificationDetails.getNotificationTypeList());		
		model.setViewName("masters/notification-details"); 
		return model;
	}
	
	@RequestMapping(value={"/upload-attachment-notification-details"}, method=RequestMethod.POST)
	public @ResponseBody String uploadNotificationAttachment(MultipartHttpServletRequest request,@RequestParam String uploadId) throws Exception{
		logger.debug("execute() is executed ");		
		NotificationDetailsService notificationDetails = new NotificationDetailsService();
		Iterator<String> itr =  request.getFileNames();		
	    MultipartFile mpf = request.getFile(itr.next());
		notificationDetails.setAttachment(mpf);
		notificationDetails.setUploadId(uploadId);
		notificationDetails.initUploadNotificationAttachment();				 
		return "success";
	}
	
	@RequestMapping(value={"/delete-attachment-notification-details"}, method=RequestMethod.POST)
	public @ResponseBody String deleteNotificationAttachment(@RequestParam String uploadId) throws Exception{
		logger.debug("execute() is executed ");		
		NotificationDetailsService notificationDetails = new NotificationDetailsService();		
		notificationDetails.setUploadId(uploadId);
		notificationDetails.initDeleteNotificationAttachment();				 
		return "success";
	}
	
	@RequestMapping(value={"/delete-available-attachment-notification-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> deleteAvailableNotificationAttachment(@RequestParam String rowId,@RequestParam String notificationId) throws Exception{
		logger.debug("execute() is executed ");		
		NotificationDetailsService notificationDetails = new NotificationDetailsService();		
		notificationDetails.setRowId(rowId);
		notificationDetails.setNotificationId(notificationId);
		notificationDetails.initDeleteAvailableNotificationAttachment();
		notificationDetails.initGetNotification();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationListDetails=new ArrayList<String>();				 
		notificationListDetails.add(mapper.writeValueAsString(notificationDetails.getRequestedAttachmentList()));
		return notificationListDetails;		
	}
	
	@RequestMapping(value={"/get-notification-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> getNotificationList(@RequestParam String notificationId) throws Exception{
		logger.debug("execute() is executed ");		
		NotificationDetailsService notificationDetails = new NotificationDetailsService();
		notificationDetails.setNotificationId(notificationId);
		notificationDetails.initGetNotification();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationListDetails=new ArrayList<String>();
		notificationListDetails.add(mapper.writeValueAsString(notificationDetails.getRequestedDetails()));		 
		notificationListDetails.add(mapper.writeValueAsString(notificationDetails.getRequestedAttachmentList()));
		return notificationListDetails;
	}
	
	@RequestMapping(value={"/edit-notification-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editNotificationList(@RequestParam String notificationId,@RequestParam String notificationType,
			@RequestParam String notificationTitle,@RequestParam String notificationDetail) throws Exception{
		logger.debug("execute() is executed ");			
		NotificationDetailsService notificationDetails = new NotificationDetailsService();
		notificationDetails.setNotificationId(notificationId);
		notificationDetails.setNotificationType(notificationType);
		notificationDetails.setNotificationTitle(notificationTitle);
		notificationDetails.setNotificationDetails(notificationDetail);		
		notificationDetails.initEditNotification();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationListDetails=new ArrayList<String>();
		notificationListDetails.add(mapper.writeValueAsString(notificationDetails.getRequestedDetails()));		 
		notificationListDetails.add(mapper.writeValueAsString(notificationDetails.getRequestedAttachmentList()));
		return notificationListDetails;
	}
	@RequestMapping(value={"/delete-notification-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteNotificationList(@RequestParam String notificationId) throws Exception{
		logger.debug("execute() is executed ");			
		NotificationDetailsService notificationDetails = new NotificationDetailsService();
		notificationDetails.setNotificationId(notificationId);				
		notificationDetails.initDeleteNotification();
		List<String> notificationListDetails=new ArrayList<String>();
		return notificationListDetails;
	}
}
