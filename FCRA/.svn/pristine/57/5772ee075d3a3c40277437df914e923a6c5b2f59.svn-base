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

import service.masters.SubStageOfficeTypeService;
import utilities.KVPair;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.Home;

@Controller
public class SubStageOfficeTypeController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private SubStageOfficeTypeService  subStage;
	@RequestMapping(value={"/substage-officetype-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");			
		SubStageOfficeTypeService subStage=new SubStageOfficeTypeService();
		subStage.execute();
		List<KVPair<String, String>> proposalDescList = subStage.getProposalDescList();
		List<KVPair<String, String>> substageDescList = subStage.getSubstageDescList();
		List<KVPair<String, String>> officeTypeList = subStage.getOfficeTypeList();
		ModelAndView model = new ModelAndView();	
		model.addObject("proposalDescList", proposalDescList);
		model.addObject("substageDescList", substageDescList);
		model.addObject("officeTypeList", officeTypeList);
		model.setViewName("masters/substage-officetype"); 
		return model;
	}
	
	@RequestMapping(value={"/get-substage-officetype-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.SubStage> pullUserList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		SubStageOfficeTypeService subStage= new SubStageOfficeTypeService();
		subStage.setPageNum(pageNum);
		subStage.setRecordsPerPage(recordsPerPage);
		subStage.setSortColumn(sortColumn);
		subStage.setSortOrder(sortOrder);
		subStage.initializeSubStageOfcList();
		ListPager<models.master.SubStage> subListPager = new ListPager<models.master.SubStage>();
		subListPager.setList(subStage.getSubStageList());
		subListPager.setTotalRecords(subStage.getTotalRecords()); 
		return subListPager;
	}
	
	@RequestMapping(value={"/add-substage-officetype-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addsubmenuList(@RequestParam String officeId, @RequestParam String subStageId, @RequestParam String proposalTypeId) throws Exception{
		logger.debug("execute() is executed ");		
		SubStageOfficeTypeService subStage = new SubStageOfficeTypeService();
		subStage.setOfficeId(officeId);
		subStage.setSubStageId(subStageId);
		subStage.setProposalTypeId(proposalTypeId);
		subStage.initAddSubStage();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(subStage.getNotifyList()));	
		return notificationList;
	}
	
@RequestMapping(value={"/delete-substage-officetype-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deletesubstageList(@RequestParam String rowId) throws Exception{
		logger.debug("execute() is executed ");			
		SubStageOfficeTypeService subStage = new SubStageOfficeTypeService();
		subStage.setRowId(rowId);				
		subStage.initDeleteSubStage();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(subStage.getNotifyList()));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-substage-officetype-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editsubstageList(@RequestParam String rowId,@RequestParam String officeId, @RequestParam String subStageId, @RequestParam String proposalTypeId) throws Exception{
		logger.debug("execute() is executed ");			
		SubStageOfficeTypeService subStage = new SubStageOfficeTypeService();
		subStage.setRowId(rowId);
		subStage.setOfficeId(officeId);
		subStage.setSubStageId(subStageId);
		subStage.setProposalTypeId(proposalTypeId);
		subStage.initEditSubStage();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(subStage.getNotifyList()));	
		return notificationList;
		}
}