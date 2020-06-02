package web.masters;
import java.util.ArrayList;
import java.util.List;
import models.master.OfficeType;
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
import service.masters.OfficeTypeServices;
@Controller
public class OfficeTypeControllor {
	  private final Logger logger = LoggerFactory.getLogger(OfficeTypeControllor.class);
	 // private OfficeTypeServices officeDetail;
		@RequestMapping(value={"/office-type-details"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");			
			ModelAndView model = new ModelAndView();	
			model.setViewName("masters/officeType"); 
			return model;
		}
		
		@RequestMapping(value={"/get-office-list-details-office-type-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<OfficeType> pullGenderTypeList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			OfficeTypeServices officeDetail = new OfficeTypeServices() ;
			officeDetail.setPageNum(pageNum);
			officeDetail.setRecordsPerPage(recordsPerPage);
			officeDetail.setSortColumn(sortColumn);
			officeDetail.setSortOrder(sortOrder);
			officeDetail.initializeOfficeList();
			ListPager<OfficeType> genderListPager = new ListPager<OfficeType>();
			genderListPager.setList(officeDetail.getOfficeList()); 
			genderListPager.setTotalRecords(officeDetail.getTotalRecords()); 
			return genderListPager;
		}
		
		@RequestMapping(value={"/add-office-type-details-office-type-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  addOfficeType(@RequestParam String officeType ,@RequestParam String officeName) throws Exception{
			logger.debug("execute() is executed ");		
			OfficeTypeServices officeDetail = new  OfficeTypeServices();
			officeDetail.setOfficeType(officeType);		
			officeDetail.setOfficeName(officeName);	
			officeDetail.AddOffice();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(officeDetail.getNotifyList()));	
			return notificationList;
		}
		
		@RequestMapping(value={"/edit-officetype-details-office-type-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editOffice(@RequestParam String officeId, @RequestParam String officeType ,@RequestParam String officeName ) throws Exception{
			logger.debug("execute() is executed ");			
			OfficeTypeServices officeDetail = new OfficeTypeServices();
			officeDetail.setOfficeId(officeId);
			officeDetail.setOfficeType(officeType);
			officeDetail.setOfficeName(officeName);
			officeDetail.editOffice();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
				
		    notificationList.add(mapper.writeValueAsString(officeDetail.getNotifyList()));
			
			return notificationList;
		}
		
		@RequestMapping(value={"/delete-officetype-details-office-type-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteOfficeType(@RequestParam String officeId, @RequestParam String officeType ) throws Exception{
			logger.debug("execute() is executed ");			
			OfficeTypeServices officeDetail=new OfficeTypeServices();
			officeDetail.setOfficeId(officeId);	
			officeDetail.setOfficeType(officeType);
			officeDetail.initDeleteOfficeType();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(officeDetail.getNotifyList()));	
			return notificationList;
	}

	}
