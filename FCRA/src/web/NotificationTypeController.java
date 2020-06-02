package web;

import java.util.ArrayList;
import java.util.List;

import models.master.NoticeBoard;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.NotificationTypeService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class NotificationTypeController extends TokenController {
	private final Logger logger = LoggerFactory
			.getLogger(NotificationTypeController.class);
	//private NotificationTypeService notificationTypeService;
	private final String tokenKey = "notification-type-details";

	/*@RequestMapping(value = "/notification-type-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/notification-type";
	}*/
	
	@RequestMapping(value = "/notification-type-details", method = RequestMethod.GET)	
	public ModelAndView submit() {
		String tokenGenerated = generateAndSaveToken(tokenKey);		
		NotificationTypeService notificationTypeService = new NotificationTypeService();
		ModelAndView model = new ModelAndView();
		model.addObject("notificationTypeList", notificationTypeService.getNotificationTypeList());	
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/notification-type"); 
		return model;
	}

	@RequestMapping(value = { "/get-notifications-list-notifications-type-notification-type-details" }, method = RequestMethod.GET)
	public @ResponseBody
	ListPager<NoticeBoard> pullNotificationsList(@RequestParam String pageNum,
			@RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("execute() is executed ");
		NotificationTypeService notificationTypeService = new NotificationTypeService();
		notificationTypeService.setPageNum(pageNum);
		notificationTypeService.setRecordsPerPage(recordsPerPage);
		notificationTypeService.setSortColumn(sortColumn);
		notificationTypeService.setSortOrder(sortOrder);
		notificationTypeService.pullNotificationTypeList();
		ListPager<NoticeBoard> notificationTypeListPager = new ListPager<NoticeBoard>();
		notificationTypeListPager.setList(notificationTypeService
				.getNotificationTypeList());
		notificationTypeListPager.setTotalRecords(notificationTypeService
				.getTotalRecords());
		return notificationTypeListPager;
	}

	@RequestMapping(value = { "/notification-type-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("notificationTypeName") String notificationTypeName, @RequestParam ("requestToken") String requestToken) throws Exception {
		logger.debug("submit() is executed ");
		//COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		NotificationTypeService notificationTypeService = new NotificationTypeService();
		if(result) {
			notificationTypeService.submit(notificationTypeName);
			token = getSessionToken(tokenKey);
		} else {
			notificationTypeService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", notificationTypeService.getNotifyList());
		model.addObject("requestToken", token);
		model.setViewName("masters/notification-type");
		return model;
	}

	@RequestMapping(value = { "/get-notification-type-notification-type-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getNotificationTypeList(@RequestParam String notificationType)
			throws Exception {
		logger.debug("getNotificationTypeList() is executed ");
		NotificationTypeService notificationTypeService = new NotificationTypeService();
		notificationTypeService.setNotificationTypeId(Short
				.parseShort(notificationType));
		notificationTypeService.initGetNotificationType(notificationType);
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper
				.writeValueAsString(notificationTypeService
						.getRequestedDetails()));
		return notificationTypeList;
	}

	@RequestMapping(value = { "/notification-type-edit-notification-type-details" }, method = RequestMethod.POST)
	public ModelAndView editNotificationType(
			@RequestParam("notificationTypeId") String notificationTypeId,
			@RequestParam("notificationTitle") String notificationTitle, @RequestParam ("requestToken") String requestToken)
					throws Exception {
		logger.debug("editNotificationType() is executed ");
		NotificationTypeService notificationTypeService = new NotificationTypeService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		if(result) {
			notificationTypeService.editNotificationType(notificationTypeId,
				notificationTitle);
			token = getSessionToken(tokenKey);
		} else {
			notificationTypeService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", notificationTypeService.getNotifyList());
		model.addObject("requestToken", token);
		model.setViewName("masters/notification-type");
		return model;
	}

	@RequestMapping(value = { "/delete-notification-Type-notification-type-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteNotificationType(@RequestParam String notificationTypeId, @RequestParam("requestToken") String requestToken)
			throws Exception {
		logger.debug("deleteNotificationType() is executed ");
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		NotificationTypeService notificationTypeService = new NotificationTypeService();
		if(result) {
			notificationTypeService.deleteNotificationType(notificationTypeId);	
			token = getSessionToken(tokenKey);
		} else {
			notificationTypeService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}	
		ObjectMapper mapper = new ObjectMapper();		
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper.writeValueAsString(notificationTypeService.getNotifyList()));		
		notificationTypeList.add(mapper.writeValueAsString(new List1(token)));
		return notificationTypeList;
	}
}