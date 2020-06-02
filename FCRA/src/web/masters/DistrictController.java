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
import service.masters.DistrictService;
import utilities.KVPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;

@Controller
public class DistrictController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private DistrictService  district;
	@RequestMapping(value={"/district-details"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");			
		DistrictService district=new DistrictService();
		district.execute();
		List<KVPair<String, String>> snameList = district.getSnameList();
		ModelAndView model = new ModelAndView();	
		model.addObject("snameList", snameList);
		model.setViewName("masters/District"); 
		return model;
	}
	
	@RequestMapping(value={"/get-district-details"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.District> pullDistrictList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		DistrictService district= new DistrictService();
		district.setPageNum(pageNum);
		district.setRecordsPerPage(recordsPerPage);
		district.setSortColumn(sortColumn);
		district.setSortOrder(sortOrder);
		district.initializeDistrictList();
		ListPager<models.master.District> subListPager = new ListPager<models.master.District>();
		subListPager.setList(district.getDistrictList());
		subListPager.setTotalRecords(district.getTotalRecords()); 
		return subListPager;
	}
	
	@RequestMapping(value={"/add-district-details"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addDistrictList(@RequestParam Integer dcode, @RequestParam String dname, @RequestParam Integer scode) throws Exception{
		logger.debug("execute() is executed ");		
		DistrictService district = new DistrictService();
		district.setDcode(dcode);
		district.setDname(dname);
		district.setScode(scode);
		district.initAddDistrict();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(district.getNotifyList()));	
		return notificationList;
	}
	
@RequestMapping(value={"/delete-district-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteDistrictList(@RequestParam Integer dcode) throws Exception{
		logger.debug("execute() is executed ");			
		DistrictService district = new DistrictService();
		district.setDcode(dcode);				
		district.initDeleteDistrict();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(district.getNotifyList()));	
		return notificationList;
	}

	@RequestMapping(value={"/edit-district-details"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editDistrictList(@RequestParam Integer dcode, @RequestParam String dname, @RequestParam Integer scode) throws Exception{
		logger.debug("execute() is executed ");			
		DistrictService district = new DistrictService();
		district.setDcode(dcode);
		district.setDname(dname);
		district.setScode(scode);
		district.initEditDistrict();
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(district.getNotifyList()));	
		return notificationList;
		}
}