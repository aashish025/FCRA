package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.DonorType;
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

import service.masters.DonorTypeService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DonorTypeController {
	private final Logger logger = LoggerFactory
			.getLogger(DonorTypeController.class);
	//private DonorTypeService donorTypeService;

	@RequestMapping(value = "/donor-type-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/donor-type";
	}
	
	@RequestMapping(value = { "/get-donor-type-donor-type-details" }, method = RequestMethod.GET)	
	public @ResponseBody
	ListPager<DonorType> pullDonorTypeList(
			@RequestParam String pageNum, @RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("pullDonorTypeList() is executed ");
		DonorTypeService donorTypeService = new DonorTypeService();
		donorTypeService.setPageNum(pageNum);
		donorTypeService.setRecordsPerPage(recordsPerPage);
		donorTypeService.setSortColumn(sortColumn);
		donorTypeService.setSortOrder(sortOrder);
		donorTypeService.pullDonorTypeList();
		ListPager<DonorType> donorTypeListPager = new ListPager<DonorType>();
		donorTypeListPager.setList(donorTypeService
				.getDonorTypeList());
		donorTypeListPager.setTotalRecords(donorTypeService
				.getTotalRecords());
		return donorTypeListPager;
	}
	
	@RequestMapping(value = { "/donor-type-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("donorName") String donorName, @RequestParam("donorCode") String donorCode) {
		logger.debug("submit() is executed ");
		DonorTypeService donorTypeService = new DonorTypeService();
		donorTypeService.submit(donorCode, donorName);
		
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", donorTypeService.getNotifyList());
		model.setViewName("masters/donor-type");
		return model;
	}
	
	@RequestMapping(value = { "/get-donor-type-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getDonorTypeList(@RequestParam String donorCode)
			throws Exception {
		logger.debug("getDonorTypeList() is executed ");
		DonorTypeService donorTypeService = new DonorTypeService();
		donorTypeService
				.setDonorId(donorCode);
		donorTypeService.initGetDonorType(donorCode);
		ObjectMapper mapper = new ObjectMapper();
		List<String> donorTypeList = new ArrayList<String>();
		donorTypeList.add(mapper.writeValueAsString(donorTypeService
				.getRequestedDetails()));
		return donorTypeList;
	}
	
	@RequestMapping(value = { "/donor-edit-donor-type-details" }, method = RequestMethod.POST)
	public ModelAndView editDonorType(
			@RequestParam("donorCode") String donorCode,
			@RequestParam("donorName") String donorName)
			throws Exception {
		logger.debug("editDonorType() is executed ");
		DonorTypeService donorTypeService = new DonorTypeService();
		donorTypeService.editDonorType(donorCode, donorName);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", donorTypeService.getNotifyList());
		model.setViewName("masters/donor-type");
		return model;
	}
	
	@RequestMapping(value = { "/delete-donor-type-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteDonorType(@RequestParam String donorCodeForEdit)
			throws Exception {
		logger.debug("deleteDonorType() is executed ");
		DonorTypeService donorTypeService = new DonorTypeService();
		donorTypeService.deleteDonorType(donorCodeForEdit);
		
		ObjectMapper mapper = new ObjectMapper();		
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper.writeValueAsString(donorTypeService.getNotifyList()));		
		return notificationTypeList;
	}
}
