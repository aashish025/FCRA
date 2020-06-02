package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.MemberCategoryType;
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

import service.masters.MemberCategoryTypeService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MemberCategoryController {
	private final Logger logger = LoggerFactory
			.getLogger(MemberCategoryController.class);
	//private MemberCategoryTypeService memberCategoryTypeService;

	@RequestMapping(value = "/member-category-details", method = RequestMethod.GET)
	public String testMethod(Model model) throws Exception {
		return "masters/member-category-type";
	}
	@RequestMapping(value = { "/get-member-category-details" }, method = RequestMethod.GET)	
	public @ResponseBody
	ListPager<MemberCategoryType> pullReligionTypeList(
			@RequestParam String pageNum, @RequestParam String recordsPerPage,
			@RequestParam String sortColumn, @RequestParam String sortOrder) {
		logger.debug("pullReligionTypeList() is executed ");
		MemberCategoryTypeService memberCategoryTypeService = new MemberCategoryTypeService();
		memberCategoryTypeService.setPageNum(pageNum);
		memberCategoryTypeService.setRecordsPerPage(recordsPerPage);
		memberCategoryTypeService.setSortColumn(sortColumn);
		memberCategoryTypeService.setSortOrder(sortOrder);
		memberCategoryTypeService.pullMemberCategoryTypeList();
		ListPager<MemberCategoryType> memberCategoryTypeListPager = new ListPager<MemberCategoryType>();
		memberCategoryTypeListPager.setList(memberCategoryTypeService
				.getMemberCategoryTypeList());
		memberCategoryTypeListPager.setTotalRecords(memberCategoryTypeService
				.getTotalRecords());
		return memberCategoryTypeListPager;
	}
	
	@RequestMapping(value = { "/member-category-details" }, method = RequestMethod.POST)
	public ModelAndView submit(
			@RequestParam("memberCategoryCode") String memberCategoryCode, @RequestParam("memberCategoryName") String memberCategoryName) {
		logger.debug("submit() is executed ");
		MemberCategoryTypeService memberCategoryTypeService = new MemberCategoryTypeService();
		memberCategoryTypeService.submit(memberCategoryCode, memberCategoryName);
		
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", memberCategoryTypeService.getNotifyList());
		model.setViewName("masters/member-category-type");
		return model;
	}
	
	@RequestMapping(value = { "/get-member-category-type-member-category-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> getMemberCategoryTypeList(@RequestParam String categoryCode)
			throws Exception {
		logger.debug("getMemberCategoryTypeList() is executed ");
		MemberCategoryTypeService memberCategoryTypeService = new MemberCategoryTypeService();
		memberCategoryTypeService
				.setCategoryCode(categoryCode);
		memberCategoryTypeService.initGetMemberCategoryType(categoryCode);
		ObjectMapper mapper = new ObjectMapper();
		List<String> memberCategoryTypeList = new ArrayList<String>();
		memberCategoryTypeList.add(mapper.writeValueAsString(memberCategoryTypeService
				.getRequestedDetails()));
		return memberCategoryTypeList;
	}
	
	@RequestMapping(value = { "/member-category-edit-member-category-details" }, method = RequestMethod.POST)
	public ModelAndView editMemberCategoryType(
			@RequestParam("categoryCode") String categoryCode,
			@RequestParam("categoryName") String categoryName)
			throws Exception {
		logger.debug("editMemberCategoryType() is executed ");
		MemberCategoryTypeService memberCategoryTypeService = new MemberCategoryTypeService();
		memberCategoryTypeService.editMemberCategoryType(categoryCode, categoryName);
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", memberCategoryTypeService.getNotifyList());
		model.setViewName("masters/member-category-type");
		return model;
	}
	
	@RequestMapping(value = { "/delete-member-category-details" }, method = RequestMethod.GET)
	public @ResponseBody
	List<String> deleteMemberCategoryType(@RequestParam String categoryCode)
			throws Exception {
		logger.debug("deleteMemberCategoryType() is executed ");
		MemberCategoryTypeService memberCategoryTypeService = new MemberCategoryTypeService();
		memberCategoryTypeService.deleteMemberCategoryType(categoryCode);
		
		ObjectMapper mapper = new ObjectMapper();		
		List<String> notificationTypeList = new ArrayList<String>();
		notificationTypeList.add(mapper.writeValueAsString(memberCategoryTypeService.getNotifyList()));		
		return notificationTypeList;
	}
}
