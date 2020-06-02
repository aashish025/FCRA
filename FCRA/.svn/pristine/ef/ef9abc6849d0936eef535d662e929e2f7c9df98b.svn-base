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
import service.masters.StateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;

@Controller
public class StateController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private StateService  state;
	
	@RequestMapping(value={"/state-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");			
		StateService state=new StateService();
		ModelAndView model = new ModelAndView();				
		model.setViewName("masters/State"); 
		return model;
	}
	
	@RequestMapping(value={"/get-state-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.State> pullStateList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		StateService state = new StateService();
		state.setPageNum(pageNum);
		state.setRecordsPerPage(recordsPerPage);
		state.setSortColumn(sortColumn);
		state.setSortOrder(sortOrder);
		state.initializeStateList();
		ListPager<models.master.State> stateListPager = new ListPager<models.master.State>();
		stateListPager.setList(state.getStateList());
		stateListPager.setTotalRecords(state.getTotalRecords()); 
		return stateListPager;
	}
	
	@RequestMapping(value={"/add-state-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addStateList(@RequestParam String scode,@RequestParam String sname,@RequestParam Integer lcode) throws Exception{
		logger.debug("execute() is executed ");		
		StateService state = new StateService();
		state.setScode(scode);
		state.setSname(sname);
		state.setLcode(lcode);				
		state.initAddState();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(state.getNotifyList()));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-state-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteStateList(@RequestParam String scode) throws Exception{
		logger.debug("execute() is executed ");			
		StateService state = new StateService();
		state.setScode(scode);				
		state.initDeleteState();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(state.getNotifyList()));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-state-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editStateList(@RequestParam String scode,@RequestParam String sname,@RequestParam Integer lcode) throws Exception{
		logger.debug("execute() is executed ");			
		StateService state = new StateService();
		state.setScode(scode);
		state.setSname(sname);
		state.setLcode(lcode);
		state.initEditState();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(state.getNotifyList()));	
		return notificationList;	
	
	}
}
