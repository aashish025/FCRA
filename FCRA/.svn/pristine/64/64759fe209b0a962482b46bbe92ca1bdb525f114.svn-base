package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.ReligionType;
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

import service.masters.ReligionTypeService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ReligionController {
	private final Logger logger = LoggerFactory
			.getLogger(ReligionController.class);
	//private ReligionTypeService religionTypeService;

	@RequestMapping(value = "/religion-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/religion-type";
	}

	@RequestMapping(value = { "/get-religion-type-religion-details" }, method = RequestMethod.GET)	
	public @ResponseBody
	ListPager<ReligionType> pullReligionTypeList(
			@RequestParam String pageNum, @RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("pullReligionTypeList() is executed ");
		ReligionTypeService religionTypeService = new ReligionTypeService();
		religionTypeService.setPageNum(pageNum);
		religionTypeService.setRecordsPerPage(recordsPerPage);
		religionTypeService.setSortColumn(sortColumn);
		religionTypeService.setSortOrder(sortOrder);
		religionTypeService.pullReligionTypeList();
		ListPager<ReligionType> religionTypeListPager = new ListPager<ReligionType>();
		religionTypeListPager.setList(religionTypeService
				.getReligionTypeList());
		religionTypeListPager.setTotalRecords(religionTypeService
				.getTotalRecords());
		return religionTypeListPager;
	}
	
	@RequestMapping(value = { "/religion-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("religionDesc") String religionDesc, @RequestParam("religionCode") String religionCode) {
		logger.debug("submit() is executed ");
		ReligionTypeService religionTypeService = new ReligionTypeService();
		religionTypeService.submit(religionCode, religionDesc);
		
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", religionTypeService.getNotifyList());
		model.setViewName("masters/religion-type");
		return model;
	}
	
	@RequestMapping(value = { "/get-religion-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getReligionTypeList(@RequestParam String religionCode)
			throws Exception {
		logger.debug("getReligionTypeList() is executed ");
		ReligionTypeService religionTypeService = new ReligionTypeService();
		religionTypeService
				.setReligionCode(religionCode);
		religionTypeService.initGetReligionType(religionCode);
		ObjectMapper mapper = new ObjectMapper();
		List<String> religionTypeList = new ArrayList<String>();
		religionTypeList.add(mapper.writeValueAsString(religionTypeService
				.getRequestedDetails()));
		return religionTypeList;
	}
	
	@RequestMapping(value = { "/religion-edit-religion-details" }, method = RequestMethod.POST)
	public ModelAndView editReligionType(
			@RequestParam("religionCode") String religionCode,
			@RequestParam("religionDesc") String religionDesc)
			throws Exception {
		logger.debug("editReligionType() is executed ");
		ReligionTypeService religionTypeService = new ReligionTypeService();
		religionTypeService.editReligionType(religionCode, religionDesc);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", religionTypeService.getNotifyList());
		model.setViewName("masters/religion-type");
		return model;
	}
	
	@RequestMapping(value = { "/delete-religion-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteReligionType(@RequestParam String religionCodeForEdit)
			throws Exception {
		logger.debug("deleteReligionType() is executed ");
		ReligionTypeService religionTypeService = new ReligionTypeService();
		religionTypeService.deleteReligionType(religionCodeForEdit);
		
		ObjectMapper mapper = new ObjectMapper();		
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper.writeValueAsString(religionTypeService.getNotifyList()));		
		return notificationTypeList;
	}
}
