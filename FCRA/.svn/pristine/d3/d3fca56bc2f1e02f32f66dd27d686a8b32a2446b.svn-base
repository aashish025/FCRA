package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.CommitteeDesignationType;
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

import service.masters.CommitteeDesignationTypeService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CommitteeDesignationController {
	private final Logger logger = LoggerFactory
			.getLogger(CommitteeDesignationController.class);
	//private CommitteeDesignationTypeService committeeDesignationTypeService;

	@RequestMapping(value = "/committee-designation-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/committee-designation-type";
	}
	
	@RequestMapping(value = { "/get-committee-designation-type-committee-designation-details" }, method = RequestMethod.GET)	
	public @ResponseBody
	ListPager<CommitteeDesignationType> pullCommitteeDesignationTypeList(
			@RequestParam String pageNum, @RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("pullCommitteeDesignationTypeList() is executed ");
		CommitteeDesignationTypeService committeeDesignationTypeService = new CommitteeDesignationTypeService();
		committeeDesignationTypeService.setPageNum(pageNum);
		committeeDesignationTypeService.setRecordsPerPage(recordsPerPage);
		committeeDesignationTypeService.setSortColumn(sortColumn);
		committeeDesignationTypeService.setSortOrder(sortOrder);
		committeeDesignationTypeService.pullCommitteeDesignationTypeList();
		ListPager<CommitteeDesignationType> committeeDesignationTypeListPager = new ListPager<CommitteeDesignationType>();
		committeeDesignationTypeListPager.setList(committeeDesignationTypeService
				.getCommitteeDesignationTypeList());
		committeeDesignationTypeListPager.setTotalRecords(committeeDesignationTypeService
				.getTotalRecords());
		return committeeDesignationTypeListPager;
	}
	
	@RequestMapping(value = { "/committee-designation-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("designationName") String designationName) {
		logger.debug("submit() is executed ");
		CommitteeDesignationTypeService committeeDesignationTypeService = new CommitteeDesignationTypeService();
		committeeDesignationTypeService.submit(designationName);
		
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", committeeDesignationTypeService.getNotifyList());
		model.setViewName("masters/committee-designation-type");
		return model;
	}
	
	@RequestMapping(value = { "/get-committee-designation-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getCommitteeDesignationTypeList(@RequestParam String designationCode)
			throws Exception {
		logger.debug("getAgreementTypeList() is executed ");
		CommitteeDesignationTypeService committeeDesignationTypeService = new CommitteeDesignationTypeService();
		committeeDesignationTypeService
				.setDesignationCode(Integer.parseInt(designationCode));
		committeeDesignationTypeService.initGetCommitteeDesignationType(designationCode);
		ObjectMapper mapper = new ObjectMapper();
		List<String> committeeDesignationTypeList = new ArrayList<String>();
		committeeDesignationTypeList.add(mapper.writeValueAsString(committeeDesignationTypeService
				.getRequestedDetails()));
		return committeeDesignationTypeList;
	}
	
	@RequestMapping(value = { "/committee-designation-edit-committee-designation-details" }, method = RequestMethod.POST)
	public ModelAndView editCommitteeDesignationtType(
			@RequestParam("committeeDesignationTypeId") String committeeDesignationTypeId,
			@RequestParam("committeeDeignationName") String committeeDeignationName)
			throws Exception {
		logger.debug("editCommitteeDesignationtType() is executed ");
		CommitteeDesignationTypeService committeeDesignationTypeService = new CommitteeDesignationTypeService();
		committeeDesignationTypeService.editCommitteeDesignationtType(committeeDesignationTypeId, committeeDeignationName);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", committeeDesignationTypeService.getNotifyList());
		model.setViewName("masters/committee-designation-type");
		return model;
	}
	
	@RequestMapping(value = { "/delete-committee-designation-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteCommitteeDesignationType(@RequestParam String committeeDesignationCode)
			throws Exception {
		logger.debug("deleteCommitteeDesignationType() is executed ");
		CommitteeDesignationTypeService committeeDesignationTypeService = new CommitteeDesignationTypeService();
		committeeDesignationTypeService.deleteCommitteeDesignationType(committeeDesignationCode);
		
		ObjectMapper mapper = new ObjectMapper();		
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper.writeValueAsString(committeeDesignationTypeService.getNotifyList()));		
		return notificationTypeList;
	}
}
