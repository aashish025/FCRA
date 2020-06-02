package web.upload;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import service.masters.NotificationDetailsService;
import service.upload.UploaderService;
import web.Home;

@Controller
public class UploadController {
	//UploaderService us=null;	
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	
	@RequestMapping(value={"/upload-attachment-details-home"}, method=RequestMethod.POST)
	public @ResponseBody String uploadNotificationAttachment(MultipartHttpServletRequest request,@RequestParam String uploadId,
			@RequestParam String fileName) throws Exception{
		logger.debug("execute() is executed ");		
		UploaderService us = new UploaderService();
		Iterator<String> itr =  request.getFileNames();				
		
	    MultipartFile mpf = request.getFile(itr.next());
		us.setAttachment(mpf);
		us.setUploadId(uploadId);
		us.setFileName(fileName);
		us.initUploadAttachment();
		System.out.println("rrrrrrrrrrrr"+fileName);
		return "success";
	}
	
	@RequestMapping(value={"/delete-attachment-details-home"}, method=RequestMethod.POST)
	public @ResponseBody String deleteNotificationAttachment(@RequestParam String uploadId) throws Exception{
		logger.debug("execute() is executed ");		
		UploaderService us = new UploaderService();		
		us.setUploadId(uploadId);
		us.initDeleteAttachment();				 
		return "success";
	}
}
