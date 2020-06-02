package web.reports;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.services.HomeNotificationDetailsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DashBoardController {

	private final Logger logger = LoggerFactory.getLogger(DashBoardController.class);	
	
	@RequestMapping(value={"/dash-board"}, method=RequestMethod.GET)
	public ModelAndView execute() throws Exception{
		logger.debug("execute() is executed ");		
		ModelAndView model = new ModelAndView();
		
		model.setViewName("reports/dash-board");		
		return model;
	}
	
	
	@RequestMapping(value={"/reg-graph-dash-board"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullRegGraphDetails() throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();	
		nds.pullRegistrationList();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getRcnBarGraphYearWise()));
		return notificationList;
	}
	
	@RequestMapping(value={"/donor-graph-dash-board"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullDonorGraphDetails() throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();	
		nds.pullDonorList();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getDonorWiseBarGraph()));
		notificationList.add(nds.getCurrentBlockYear());
		notificationList.add(mapper.writeValueAsString(nds.getDonorFinYearWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getBlockYearList()));
		return notificationList;
	}
	@RequestMapping(value={"/country-graph-dash-board"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullCountryGraphDetails() throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();	
		nds.pullCountryList();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getCountryWiseBarGraph()));
		notificationList.add(nds.getCurrentBlockYear());
		notificationList.add(mapper.writeValueAsString(nds.getCountryFinYearWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getBlockYearList()));
		return notificationList;
	}
	@RequestMapping(value={"/association-graph-dash-board"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullAssociationGraphDetails() throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();	
		nds.pullAssociationList();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getAssoWiseBarGraph()));
		notificationList.add(nds.getCurrentBlockYear());
		notificationList.add(mapper.writeValueAsString(nds.getAssoFinYearWiseBarGraph()));
		notificationList.add(mapper.writeValueAsString(nds.getBlockYearList()));
		return notificationList;
	}
	@RequestMapping(value={"/application-graph-dash-board"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullApplicationGraphDetails() throws JsonProcessingException{
		logger.debug("graph executed ");		
		HomeNotificationDetailsService nds = new HomeNotificationDetailsService();	
		nds.pullApplicationList();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(nds.getApplicationStatistics()));
		notificationList.add(mapper.writeValueAsString(nds.getApplicationServiceStatistics()));
		return notificationList;
	}
}
