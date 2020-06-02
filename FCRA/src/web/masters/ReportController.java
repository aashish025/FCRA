package web.masters;

import java.util.ArrayList;
import java.util.List;
import models.reports.ReportType;
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
import service.masters.ReportTypeDetailsServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.Home;
import web.TokenController;
@Controller
public class ReportController extends TokenController {
	 private final Logger logger = LoggerFactory.getLogger(Home.class);
	 public String tokenKey="report-type";
		@RequestMapping(value={"/mis-report-detail"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");	
			String tokenGenerated = generateAndSaveToken(tokenKey);
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			reportDetails.execute();
			ModelAndView model = new ModelAndView();
			model.addObject("reportTypeList", reportDetails.getReportList());	
			model.addObject("requestToken", tokenGenerated);
			model.setViewName("masters/report"); 
			return model;
		}
		
		@RequestMapping(value={"/get-mis-report-detail"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<ReportType> ReportTypeList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			reportDetails.setPageNum(pageNum);
			reportDetails.setRecordsPerPage(recordsPerPage);
			reportDetails.setSortColumn(sortColumn);
			reportDetails.setSortOrder(sortOrder);
			reportDetails.initializeReportList();
			ListPager<ReportType> reportListPager = new ListPager<ReportType>();
			reportListPager.setList(reportDetails.getReportList()); 
			reportListPager.setTotalRecords(reportDetails.getTotalRecords()); 
			return reportListPager;
		}
		@RequestMapping(value={"/add-mis-report-detail"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  addreportList(@RequestParam String reportName,@RequestParam String  requestToken) throws Exception{
			logger.debug("execute() is executed ");		
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
		    if(result){
		    reportDetails.setReportName(reportName);
			reportDetails.AddName();
			 token = getSessionToken(tokenKey);
			}
			else {
				reportDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(reportDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(reportDetails.getReportId()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));

			return notificationList;
		}
		@RequestMapping(value={"/get-officeassign-mis-report-detail"}, method=RequestMethod.POST)
		public @ResponseBody List<String> getOfficeList(@RequestParam Integer reportId,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			reportDetails.setReportId(reportId);
			reportDetails.initReport();
            ObjectMapper mapper = new ObjectMapper();
			List<String> officeList=new ArrayList<String>();				 
			officeList.add(mapper.writeValueAsString(reportDetails.getAvailableOfficeList()));
		    officeList.add(mapper.writeValueAsString(reportDetails.getAssignedOfficeList()));
		
			return officeList;
		}
		@RequestMapping(value={"/save-mis-report-detail"}, method=RequestMethod.POST)
		public @ResponseBody List<String> saveDetails(@RequestParam Integer reportId,@RequestParam String aspl,@RequestParam String requestToken) throws Exception{
			logger.debug("execute() is executed ");
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=requestToken;
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			if(result){
			reportDetails.setReportId(reportId);
			reportDetails.setAssignedOffice(aspl);
		    reportDetails.saveOffice();
			token = getSessionToken(tokenKey);
		}
		else {
			reportDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}

			ObjectMapper mapper = new ObjectMapper();
			List<String> statusList=new ArrayList<String>();				 
			statusList.add(mapper.writeValueAsString(reportDetails.getNotifyList()));
			statusList.add(mapper.writeValueAsString(new List1(token)));
			return statusList;
		}
		
		@RequestMapping(value={"/edit-mis-report-detail"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editReportList(@RequestParam String reportName, @RequestParam Integer reportId,@RequestParam String  requestToken) throws Exception{
			logger.debug("execute() is executed ");	
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
		    if(result){
		    reportDetails.setReportId(reportId);
		    reportDetails.setReportName(reportName);
		    reportDetails.EditReport();
			token = getSessionToken(tokenKey);
			}
			else {
			reportDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
				
		    notificationList.add(mapper.writeValueAsString(reportDetails.getNotifyList()));
		    notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
		}

	 
            @RequestMapping(value={"/delete-mis-report-detail"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteList(@RequestParam String reportName, @RequestParam Integer reportId,@RequestParam String  requestToken) throws Exception{
			logger.debug("execute() is executed ");			
			ReportTypeDetailsServices	reportDetails=new ReportTypeDetailsServices();
			Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		    String token=null;
		    if(result){
			reportDetails.setReportId(reportId);
		    reportDetails.DeleteReport();
			token = getSessionToken(tokenKey);
		}
		else {
			reportDetails.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(reportDetails.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			return notificationList;
	}
}


