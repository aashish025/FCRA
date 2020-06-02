package web.masters;

import java.util.ArrayList;
import java.util.List;

import models.master.ProjectDocument;
import models.master.Service;
import models.services.ListPager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.masters.ProjectDocumentTypeService;
import web.Home;

import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class ProjectDocumentDetails {
	 private final Logger logger = LoggerFactory.getLogger(Home.class);
		//private ProjectDocumentTypeService docDetails;
		@RequestMapping(value={"/project-document-type-details"}, method=RequestMethod.GET)
		public ModelAndView submit(){
			logger.debug("execute() is executed ");			
			ProjectDocumentTypeService docDetails=new ProjectDocumentTypeService();
			docDetails.execute();
			ModelAndView model = new ModelAndView();
			model.addObject("servicesTypeList", docDetails.getDocumentTypeList());		
			model.setViewName("masters/project-document-type"); 
			return model;
		}
		@RequestMapping(value={"/get-project-document-type-details"}, method=RequestMethod.GET)
		public @ResponseBody ListPager<ProjectDocument> Document(@RequestParam String pageNum,@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
			logger.debug("execute() is executed ");		
			ProjectDocumentTypeService docDetails=new ProjectDocumentTypeService();
			docDetails.setPageNum(pageNum);
			docDetails.setRecordsPerPage(recordsPerPage);
			docDetails.setSortColumn(sortColumn);
			docDetails.setSortOrder(sortOrder);
			docDetails.initializeDocumentList();
			ListPager<ProjectDocument> documentListPager = new ListPager<ProjectDocument>();
			documentListPager.setList(docDetails.getDocumentList()); 
			documentListPager.setTotalRecords(docDetails.getTotalRecords()); 
			return documentListPager;
		}
		
		@RequestMapping(value={"/add-project-document-type-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String>  addList(@RequestParam String doctypeDesc) throws Exception{
			logger.debug("execute() is executed ");		
			ProjectDocumentTypeService docDetails=new ProjectDocumentTypeService();
			docDetails.setDoctypeDesc(doctypeDesc);
		    docDetails.AddDocument();	
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(docDetails.getNotifyList()));
			return notificationList;
		}
		
		@RequestMapping(value={"/edit-project-document-type-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> editRecreationalList(@RequestParam String docId, String doctypeDesc ) throws Exception{
			logger.debug("execute() is executed ");	
			ProjectDocumentTypeService docDetails=new ProjectDocumentTypeService();
			docDetails.setDocId(docId);
			docDetails.setDoctypeDesc(doctypeDesc);
		    docDetails.EditDocument();	
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();
			notificationList.add(mapper.writeValueAsString(docDetails.getNotifyList()));
			return notificationList;
		}
 
            @RequestMapping(value={"/delete-project-document-type-details"}, method=RequestMethod.GET)
		public @ResponseBody List<String> deleteList(@RequestParam String docId, String doctypeDesc ) throws Exception{
			logger.debug("execute() is executed ");			
			ProjectDocumentTypeService docDetails=new ProjectDocumentTypeService();
			docDetails.setDocId(docId);
			docDetails.setDoctypeDesc(doctypeDesc);
		    docDetails.DeleteDocument();	
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				 
			notificationList.add(mapper.writeValueAsString(docDetails.getNotifyList()));	
			return notificationList;
}

}
