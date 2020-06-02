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
import service.masters.CommitteeRelationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;

@Controller
public class CommitteeRelationController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private CommitteeRelationService  comrelation;
	
	@RequestMapping(value={"/committee-relation-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");			
		CommitteeRelationService comrelation=new CommitteeRelationService();
		ModelAndView model = new ModelAndView();				
		model.setViewName("masters/Committee-relation"); 
		return model;
	}
	
	@RequestMapping(value={"/get-committee-relation-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.CommitteeRelation> pullRelationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		CommitteeRelationService comrelation = new CommitteeRelationService();
		comrelation.setPageNum(pageNum);
		comrelation.setRecordsPerPage(recordsPerPage);
		comrelation.setSortColumn(sortColumn);
		comrelation.setSortOrder(sortOrder);
		comrelation.initializeComRelationList();
		ListPager<models.master.CommitteeRelation> relationListPager = new ListPager<models.master.CommitteeRelation>();
		relationListPager.setList(comrelation.getComRelationList());
		relationListPager.setTotalRecords(comrelation.getTotalRecords()); 
		return relationListPager;
	}
	
	@RequestMapping(value={"/add-committee-relation-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addRelationList(@RequestParam String relationName) throws Exception{
		logger.debug("execute() is executed ");		
		CommitteeRelationService comrelation = new CommitteeRelationService();
		comrelation.setRelationName(relationName);				
		comrelation.initAddComRelation();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(comrelation.getNotifyList()));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-committee-relation-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteRelationList(@RequestParam Integer relationCode) throws Exception{
		logger.debug("execute() is executed ");			
		CommitteeRelationService comrelation = new CommitteeRelationService();
		comrelation.setRelationCode(relationCode);				
		comrelation.initDeleteComRelation();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(comrelation.getNotifyList()));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-committee-relation-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editRelationList(@RequestParam Integer relationCode,@RequestParam String relationName) throws Exception{
		logger.debug("execute() is executed ");			
		CommitteeRelationService comrelation = new CommitteeRelationService();
		comrelation.setRelationCode(relationCode);
		comrelation.setRelationName(relationName);
		comrelation.initEditComRelation();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(comrelation.getNotifyList()));	
		return notificationList;
	
	}
}
