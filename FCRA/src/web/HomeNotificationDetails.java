package web;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.services.HomeNotificationDetailsService;


@Controller
public class HomeNotificationDetails {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private HomeNotificationDetailsService  nds=null;
	
	
	@RequestMapping(value={"/default-home"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullNotificationList() throws JsonProcessingException{
		logger.debug("notification executed ");		
		System.out.println("Come in");
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();		
		nds.pullNotificationList();
		nds.pullNewNotificationCountList();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getNotificationList()));
		notificationList.add(mapper.writeValueAsString(nds.getNewNotificationList()));
		notificationList.add(mapper.writeValueAsString(nds.getRcnBarGraphYearWise()));
		notificationList.add(mapper.writeValueAsString(nds.getApplicationStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getApplicationServiceStatistics()));
/*		notificationList.add(mapper.writeValueAsString(nds.getDonorWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getAssoWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getCountryWiseBarGraph()));
		notificationList.add(nds.getCurrentBlockYear());*/
		return notificationList;
	}
	
	@RequestMapping(value={"/graph-home"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullGraphList() throws JsonProcessingException{
		logger.debug("notification executed ");		
		System.out.println("Come in");
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();		
		nds.pullBarGraphCountList();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getDonorWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getAssoWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getCountryWiseBarGraph()));
		notificationList.add(nds.getCurrentBlockYear());
		notificationList.add(mapper.writeValueAsString(nds.getDonorFinYearWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getAssoFinYearWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getCountryFinYearWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getBlockYearList()));
		return notificationList;
	}
	
	
	@RequestMapping(value={"/selected-home"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullSelectedNotificationList(@RequestParam String notificationType) throws JsonProcessingException{
		logger.debug("notification executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();
		nds.setNotificationType(notificationType);
		nds.pullSelectedNotificationList();
		nds.pullNotificationAttachmentList();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getNotificationList()));
		notificationList.add(mapper.writeValueAsString(nds.getNotificationAttachmentList()));				 
		return notificationList;
	}
}
