package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.Office;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import service.masters.OfficeServices;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class OfficeController {
	     private final Logger logger = LoggerFactory.getLogger(OfficeController.class);
	   //  private OfficeServices officeservices ;
		@RequestMapping(value={"/office-detail"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			OfficeServices officeservices=new OfficeServices();
			officeservices.execute();
			logger.debug("execute() is executed ");	
			ModelAndView model = new ModelAndView();
			model.addObject("officeTypeList", officeservices.getOfficeTypeList());	
			model.addObject("countryTypeList", officeservices.getCountryTypeList());
			model.addObject("currencyTypeList", officeservices.getCurrencyTypeList());
			model.addObject("timeZoneList", officeservices.getTimezoneTypeList());
			model.setViewName("masters/office"); 
			return model;
		}
		
		@RequestMapping(value={"/get-office-list-office-details-office-detail"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<Office> pullAcquisitionTypeList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			OfficeServices officeservices=new OfficeServices();
			officeservices.setPageNum(pageNum);
			officeservices.setRecordsPerPage(recordsPerPage);
			officeservices.setSortColumn(sortColumn);
			officeservices.setSortOrder(sortOrder);
			officeservices.initializeAcquisitionList();
			ListPager<Office> officeListPager = new ListPager<Office>();
			officeListPager.setList(officeservices.getOfficeList()); 
			officeListPager.setTotalRecords(officeservices.getTotalRecords()); 
			return officeListPager;
		}
		
		@RequestMapping(value={"/get-office-details-office-detail"}, method=RequestMethod.GET)
		public @ResponseBody List<String> getNotificationList(@RequestParam String officeCode) throws Exception{
			logger.debug("execute() is executed ");		
			OfficeServices officeservices=new OfficeServices();
			officeservices.setOfficeCode(officeCode);
			officeservices.initForEditOfficeDetail();
			ObjectMapper mapper = new ObjectMapper();
			List<String> officeListDetails=new ArrayList<String>();
			officeListDetails.add(mapper.writeValueAsString(officeservices.getRequestedDetails()));		 
			return officeListDetails;
		}
		
		@RequestMapping(value={"/add-office-details-office-detail"}, method=RequestMethod.GET)
		public  @ResponseBody List<String>   OfficeList(@RequestParam Integer officeId, @RequestParam String officeCode ,
				@RequestParam String officeName, @RequestParam String officeCity,@RequestParam String officeState, @RequestParam String officeCountry ,
				@RequestParam Integer officeZipCode, @RequestParam String officeContact,@RequestParam String officeEmail, @RequestParam String currencyType,
				@RequestParam String officeTimeZone, @RequestParam String officeSigntory,@RequestParam String officeAdd ) throws Exception{
			logger.debug("execute() is executed ");		
			OfficeServices officeservices=new OfficeServices();
			officeservices.setOfficeId(officeId);
			officeservices.setOfficeCode(officeCode);
			officeservices.setOfficeName(officeName);
			officeservices.setOfficeCity(officeCity);
			officeservices.setOfficeState(officeState);
			officeservices.setCountryName(officeCountry);
			officeservices.setOfficeZipCode(officeZipCode);
			officeservices.setContactNo(officeContact);
			officeservices.setEmailId(officeEmail);
			officeservices.setOfficeCurrencuy(" ");
			officeservices.setOfficeTimeZone(officeTimeZone);
			officeservices.setOfficeSigntory(officeSigntory);
			officeservices.setAddress(officeAdd);
		    officeservices.addOffice();		
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(officeservices.getNotifyList()));	
			return notificationList;
		}
		@RequestMapping(value={"/edit-office-details-office-detail"}, method=RequestMethod.POST)
		public @ResponseBody List<String> editNotificationList( @RequestParam String officeCode ,
				@RequestParam String officeName, @RequestParam String officeCity,@RequestParam String officeState, @RequestParam String officeCountry ,
				@RequestParam Integer officeZipCode, @RequestParam String officeContact,@RequestParam String officeEmail, @RequestParam String currencyType,
				@RequestParam String officeTimeZone, @RequestParam String officeSigntory,@RequestParam String officeAdd ) throws Exception{
			logger.debug("execute() is executed ");			
			OfficeServices officeservices=new OfficeServices();
			officeservices.setOfficeCode(officeCode);
			officeservices.setOfficeName(officeName);
			officeservices.setOfficeCity(officeCity);
			officeservices.setOfficeState(officeState);
			officeservices.setCountryName(officeCountry);
			officeservices.setOfficeZipCode(officeZipCode);
			officeservices.setContactNo(officeContact);
			officeservices.setEmailId(officeEmail);
			officeservices.setOfficeCurrencuy(" ");
			officeservices.setOfficeTimeZone(officeTimeZone);
			officeservices.setOfficeSigntory(officeSigntory);
			officeservices.setAddress(officeAdd);
			officeservices.editoffice();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
				
		    notificationList.add(mapper.writeValueAsString(officeservices.getNotifyList()));
			
			return notificationList;
		}
		
		@RequestMapping(value={"/delete-office-details-office-detail"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteAcquisitionList(@RequestParam String officeCode) throws Exception{
			logger.debug("execute() is executed ");			
			OfficeServices officeservices=new OfficeServices();
			officeservices.setOfficeCode(officeCode);		
			officeservices.initDeleteOffice();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(officeservices.getNotifyList()));	
			return notificationList;
	}

}

	
