package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.OfficeFacilityType;
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

import service.masters.OfficeFacilityTypeService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class OfficeFacilityController {
	private final Logger logger = LoggerFactory
			.getLogger(OfficeFacilityController.class);
	//private OfficeFacilityTypeService officeFacilityTypeService;

	@RequestMapping(value = "/office-facility-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/office-facility-type";
	}

	@RequestMapping(value = { "/get-office-facility-list-type-office-facility-details" }, method = RequestMethod.GET)
	public @ResponseBody
	ListPager<OfficeFacilityType> pullOfficeFaciltyTypeList(
			@RequestParam String pageNum, @RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("pullOfficeFaciltyTypeList() is executed ");
		OfficeFacilityTypeService officeFacilityTypeService = new OfficeFacilityTypeService();
		officeFacilityTypeService.setPageNum(pageNum);
		officeFacilityTypeService.setRecordsPerPage(recordsPerPage);
		officeFacilityTypeService.setSortColumn(sortColumn);
		officeFacilityTypeService.setSortOrder(sortOrder);
		officeFacilityTypeService.pullOfficeFacilityTypeList();
		ListPager<OfficeFacilityType> officeFacilityTypeListPager = new ListPager<OfficeFacilityType>();
		officeFacilityTypeListPager.setList(officeFacilityTypeService
				.getOfficeFacilityTypeList());
		officeFacilityTypeListPager.setTotalRecords(officeFacilityTypeService
				.getTotalRecords());
		return officeFacilityTypeListPager;
	}

	@RequestMapping(value = { "/office-facility-type-details-office-facility-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("officeFacilityDesc") String officeFacilityDesc) {
		logger.debug("submit() is executed ");
		OfficeFacilityTypeService officeFacilityTypeService = new OfficeFacilityTypeService();
		officeFacilityTypeService.submit(officeFacilityDesc);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", officeFacilityTypeService.getNotifyList());
		model.setViewName("masters/office-facility-type");
		return model;
	}

	@RequestMapping(value = { "/get-office-facility-type-office-facility-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getOfficeFacilityTypeList(
			@RequestParam String officeFacilityType) throws Exception {
		logger.debug("getOfficeFacilityTypeList() is executed ");
		OfficeFacilityTypeService officeFacilityTypeService = new OfficeFacilityTypeService();
		officeFacilityTypeService.setFacilityId(Integer
				.parseInt(officeFacilityType));
		officeFacilityTypeService.initGetOfficeFacilityType(officeFacilityType);
		ObjectMapper mapper = new ObjectMapper();
		List<String> officeFacilityTypeList = new ArrayList<String>();
		officeFacilityTypeList.add(mapper
				.writeValueAsString(officeFacilityTypeService
						.getRequestedDetails()));
		return officeFacilityTypeList;
	}

	@RequestMapping(value = { "/office-facility-type-edit-office-facility-details" }, method = RequestMethod.POST)
	public ModelAndView editOfficeFacilityType(
			@RequestParam("officeFacilityTypeId") String officeFacilityTypeId,
			@RequestParam("officeFacilityDesc") String officeFacilityDesc)
			throws Exception {
		logger.debug("editOfficeFacilityType() is executed ");
		OfficeFacilityTypeService officeFacilityTypeService = new OfficeFacilityTypeService();
		officeFacilityTypeService.editOfficeFacilityType(officeFacilityTypeId,
				officeFacilityDesc);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", officeFacilityTypeService.getNotifyList());
		model.setViewName("masters/office-facility-type");
		return model;
	}

	@RequestMapping(value = { "/delete-office-facility-Type-office-facility-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteOfficeFacilityType(
			@RequestParam String officeFacilityTypeId) throws Exception {
		logger.debug("deleteOfficeFacilityType() is executed ");
		OfficeFacilityTypeService officeFacilityTypeService = new OfficeFacilityTypeService();
		officeFacilityTypeService
				.deleteOfficeFacilityType(officeFacilityTypeId);		
		ObjectMapper mapper = new ObjectMapper();		
		List<String> officeFacilityTypeList = new ArrayList<String>();
		officeFacilityTypeList.add(mapper.writeValueAsString(officeFacilityTypeService.getNotifyList()));		
		return officeFacilityTypeList;
	}
}