package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.services.ListPager;
import models.master.FileStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.FileStatusServices;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.Home;
@Controller
public class FileStatusControllor {
	     private final Logger logger = LoggerFactory.getLogger(Home.class);
		/*private FileStatusServices filestatus;*/
		
		@RequestMapping(value={"/file-status-details"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");			
			FileStatusServices filestatus=new FileStatusServices();
			//filestatus.execute();
			ModelAndView model = new ModelAndView();		
			model.setViewName("masters/file-status"); 
			return model;
		}
		
		@RequestMapping(value={"/get-fileStatus-list-details-file-status-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<FileStatus> pullUserLavelList(@RequestParam String pageNum, 
				@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			FileStatusServices filestatus = new FileStatusServices();
			filestatus.setPageNum(pageNum);
			filestatus.setRecordsPerPage(recordsPerPage);
			filestatus.setSortColumn(sortColumn);
			filestatus.setSortOrder(sortOrder);
			filestatus.initFileStatusList();
			ListPager<FileStatus> userListPager = new ListPager<FileStatus>();
			userListPager.setList(filestatus.getFileStatus()); 
			userListPager.setTotalRecords(filestatus.getTotalRecords()); 
			return userListPager;
		}
		@RequestMapping(value={"/add-filestatus-details-file-status-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  AcquisitionList(@RequestParam String filestatusName) throws Exception{
			logger.debug("execute() is executed ");		
			FileStatusServices filestatus = new FileStatusServices();
			filestatus.setFilestatusDesc(filestatusName);
			filestatus.AddUser();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(filestatus.getNotifyList()));	
			return notificationList;
		}

		@RequestMapping(value={"/edit-filestatus-details-file-status-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editNotificationList(@RequestParam String filestatusName, Short filestatusId) throws Exception{
			logger.debug("execute() is executed ");			
			FileStatusServices filestatus = new FileStatusServices();
			filestatus.setFilestatusDesc(filestatusName);
			filestatus.setFilestatusId(filestatusId);
			filestatus.editFileStatus();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
				
		    notificationList.add(mapper.writeValueAsString(filestatus.getNotifyList()));
			
			return notificationList;
		}

	 
@RequestMapping(value={"/delete-filestatus-details-file-status-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteAcquisitionList(@RequestParam String filestatusName, Short filestatusId) throws Exception{
			logger.debug("execute() is executed ");			
			FileStatusServices filestatus = new FileStatusServices();
			filestatus.setFilestatusDesc(filestatusName);
			filestatus.setFilestatusId(filestatusId);		
			filestatus.initDeleteFileStatus();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(filestatus.getNotifyList()));	
			return notificationList;
	}
}

	
