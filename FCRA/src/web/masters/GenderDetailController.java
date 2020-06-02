package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.GenderType;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.masters.GenderDetailServices;

@Controller
public class GenderDetailController {

    private final Logger logger = LoggerFactory.getLogger(GenderDetailController.class);
	//private GenderDetailServices genderDetails;
	
	@RequestMapping(value={"/gender-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");			
		GenderDetailServices genderDetails=new GenderDetailServices();
		genderDetails.execute();
		ModelAndView model = new ModelAndView();	
		model.setViewName("masters/gendertype"); 
		return model;
	}
	
	@RequestMapping(value={"/get-gender-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<GenderType> pullGenderTypeList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		GenderDetailServices genderDetails = new GenderDetailServices();
		genderDetails.setPageNum(pageNum);
		genderDetails.setRecordsPerPage(recordsPerPage);
		genderDetails.setSortColumn(sortColumn);
		genderDetails.setSortOrder(sortOrder);
		genderDetails.initializeGenderList();
		ListPager<GenderType> genderListPager = new ListPager<GenderType>();
		genderListPager.setList(genderDetails.getGenderList()); 
		genderListPager.setTotalRecords(genderDetails.getTotalRecords()); 
		return genderListPager;
	}

	@RequestMapping(value={"/add-gender-type-gender-details"}, method=RequestMethod.POST)
	public  @ResponseBody List<String>   GenderList(@RequestParam String genderType, @RequestParam String genderid) throws Exception{
		logger.debug("execute() is executed ");		
		GenderDetailServices genderDetails = new GenderDetailServices();
		genderDetails.setGenderType(genderType);
		genderDetails.setGenderId(genderid);  		
		genderDetails.AddGender();		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(genderDetails.getNotifyList()));	
		return notificationList;
	}
	@RequestMapping(value={"/edit-gender-type-gender-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editNotificationList(@RequestParam String genderType, String genderid) throws Exception{
		logger.debug("execute() is executed ");			
		GenderDetailServices genderDetails = new GenderDetailServices();
		genderDetails.setGenderType(genderType);
		genderDetails.setGenderId(genderid); 
		genderDetails.editGenderType();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
	    notificationList.add(mapper.writeValueAsString(genderDetails.getNotifyList()));
		return notificationList;
	}
	@RequestMapping(value={"/delete-gender-type-gender-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteAcquisitionList(@RequestParam String genderType, String genderid) throws Exception{
		logger.debug("execute() is executed ");			
		GenderDetailServices genderDetails = new GenderDetailServices();
		genderDetails.setGenderType(genderType);
		genderDetails.setGenderId(genderid); 		
		genderDetails.initDeleteGenderType();
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(genderDetails.getNotifyList()));	
		return notificationList;
      }
}
