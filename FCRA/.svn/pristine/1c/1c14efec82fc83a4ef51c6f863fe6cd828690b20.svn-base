package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.OccupationType;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.OccupationTypeService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class OccupationController {
	private final Logger logger = LoggerFactory
			.getLogger(OccupationController.class);
	//private OccupationTypeService occupationTypeService;

	@RequestMapping(value = "/occupation-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/occupation-type";
	}
	
	@RequestMapping(value = { "/get-occupation-type-occupation-details" }, method = RequestMethod.GET)	
	public @ResponseBody
	ListPager<OccupationType> pullOccupationTypeList(
			@RequestParam String pageNum, @RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("pullOccupationTypeList() is executed ");
		OccupationTypeService occupationTypeService = new OccupationTypeService();
		occupationTypeService.setPageNum(pageNum);
		occupationTypeService.setRecordsPerPage(recordsPerPage);
		occupationTypeService.setSortColumn(sortColumn);
		occupationTypeService.setSortOrder(sortOrder);
		occupationTypeService.pullOccupationTypeList();
		ListPager<OccupationType> occupationTypeListPager = new ListPager<OccupationType>();
		occupationTypeListPager.setList(occupationTypeService
				.getOccupationTypeList());
		occupationTypeListPager.setTotalRecords(occupationTypeService
				.getTotalRecords());
		return occupationTypeListPager;
	}
	
	@RequestMapping(value = { "/occupation-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("occupationName") String occupationName) {
		logger.debug("submit() is executed ");
		OccupationTypeService occupationTypeService = new OccupationTypeService();
		occupationTypeService.submit(occupationName);
		
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", occupationTypeService.getNotifyList());
		model.setViewName("masters/occupation-type");
		return model;
	}
	

	@RequestMapping(value = { "/get-occupation-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getOccupationTypeList(@RequestParam String occupationCode)
			throws Exception {
		logger.debug("getOccupationTypeList() is executed ");
		OccupationTypeService occupationTypeService = new OccupationTypeService();
		occupationTypeService
				.setOccupationCode(Integer.parseInt(occupationCode));
		occupationTypeService.initGetOccupationType(occupationCode);
		ObjectMapper mapper = new ObjectMapper();
		List<String> occupationTypeList = new ArrayList<String>();
		occupationTypeList.add(mapper.writeValueAsString(occupationTypeService
				.getRequestedDetails()));
		return occupationTypeList;
	}
	
	@RequestMapping(value = { "/occupation-edit-occupation-details" }, method = RequestMethod.POST)
	public ModelAndView editOccupationType(
			@RequestParam("occupationCode") String occupationCode,
			@RequestParam("occupationName") String occupationName)
			throws Exception {
		logger.debug("editOccupationType() is executed ");
		OccupationTypeService occupationTypeService = new OccupationTypeService();
		occupationTypeService.editOccupationType(occupationCode, occupationName);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", occupationTypeService.getNotifyList());
		model.setViewName("masters/occupation-type");
		return model;
	}
	
	@RequestMapping(value = { "/delete-occupation-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteOccupationType(@RequestParam String occupationCode)
			throws Exception {
		logger.debug("deleteOccupationType() is executed ");
		OccupationTypeService occupationTypeService = new OccupationTypeService();
		occupationTypeService.deleteOccupationType(occupationCode);
		
		ObjectMapper mapper = new ObjectMapper();		
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper.writeValueAsString(occupationTypeService.getNotifyList()));		
		return notificationTypeList;
	}
}
