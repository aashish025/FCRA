package web.reports;
import java.util.ArrayList;
import java.util.List;

import models.reports.DonorDetail;
import models.services.DonorDetails;
import models.services.ListPager;
import models.services.requests.AbstractRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.reports.DonorDetailService;
import service.reports.RegistrationTrackingService;
import utilities.lists.List2;
import web.Home;

	@Controller
	public class DonorDetailController {
		private final Logger logger = LoggerFactory.getLogger(Home.class);	
		private String actionStatus=null;
		@RequestMapping(value={"/donor-details"}, method=RequestMethod.GET)
		public ModelAndView submit() throws Exception{		
			DonorDetailService pds = new DonorDetailService();
			pds.initDonorAction();	
			ModelAndView model = new ModelAndView();
			model.addObject("stateList", pds.getStateList());
			model.addObject("countryList",pds.getCountryList());
		    model.setViewName("reports/donor-detail");		
			return model;
		}
		@RequestMapping(value={"/get-association-donor-details"}, method=RequestMethod.POST)
		public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
			logger.debug("execute() is executed ");		
			DonorDetailService pds = new DonorDetailService();		
			pds.setState(state);		
			pds.initAssociation();		 
			return pds.getAssocationList();
		}
		@RequestMapping(value={"/get-searchingdata-donor-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<DonorDetail> initSearchingList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
				,@RequestParam String country,@RequestParam String association, @RequestParam String donorName) throws Exception{
			logger.debug("execute() is executed ");		
			DonorDetailService pds = new DonorDetailService();
			pds.setPageNum(pageNum);
			pds.setRecordsPerPage(recordsPerPage);
			pds.setSortColumn(sortColumn);
			pds.setSortOrder(sortOrder);		
			pds.setState(state);
			pds.setDonorName(donorName);
			pds.setCountry(country);
		    pds.setAssociation(association);
			pds.initAdvanceApplicationListDetails();
			ListPager<DonorDetail> ar = new ListPager<DonorDetail>();
			ar.setList(pds.getDonorList());
			ar.setTotalRecords(pds.getTotalRecords()); 
			return ar;
		}
		

}
