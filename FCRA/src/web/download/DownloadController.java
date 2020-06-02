package web.download;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.download.DownloaderService;
import service.services.ShowCauseNoticeService;
import service.services.dashboard.ApplicationStatusService;
import dao.services.downloaders.PropertyDocumentsDownloader;

@Controller
public class DownloadController implements ResourceLoaderAware{
	private ResourceLoader resourceLoader;
	
	@RequestMapping(value = {"/get-lease-deed-property-details" ,"/get-lease-deed-property-tracking"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getLeaseDeed(@RequestParam String propertyId, HttpServletResponse response) {
		String settings = "attachment;filename="+propertyId+"-Deed.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setPropertyId(propertyId);
		downloaderService.setResponse(response);
		downloaderService.getLeaseDeed();
		return null;
	}	

	


	@RequestMapping(value = {"/get-purchase-deed-property-details" , "/get-purchase-deed-property-tracking"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getPurchaseDeed(@RequestParam String propertyId, HttpServletResponse response) {
		String settings = "attachment;filename="+propertyId+"-Deed.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setPropertyId(propertyId);
		downloaderService.setResponse(response);
		downloaderService.getPurchaseDeed();
		return null;
	}	

	@RequestMapping(value = {"/get-area-map-property-details", "/get-area-map-property-tracking"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getAreaMap(@RequestParam String propertyId, HttpServletResponse response) {
		String settings = "attachment;filename="+propertyId+"-Area-Map.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setPropertyId(propertyId);
		downloaderService.setResponse(response);
		downloaderService.getAreaMap();
		return null;
	}	

	@RequestMapping(value = {"/get-survey-site-plan-property-details", "/get-survey-site-plan-property-tracking"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getSurveySitePlan(@RequestParam String propertyId, HttpServletResponse response) {
		String settings = "attachment;filename="+propertyId+"-Survey-Site-Plan.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setPropertyId(propertyId);
		downloaderService.setResponse(response);
		downloaderService.getSurveySitePlan();
		return null;
	}
	
	@RequestMapping(value = "/get-notification-attachment-home", method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getNotificationAttachment(@RequestParam String rowId, HttpServletResponse response) {
		String settings = "attachment;filename=Notification.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setRowId(rowId);
		downloaderService.setResponse(response);
		downloaderService.getNotificationAttachment();
		return null;
	}	

	@RequestMapping(value = "/get-attachment-chat-workspace", method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getChatAttachment(@RequestParam String rowId, HttpServletResponse response) {		
		DownloaderService downloaderService = new DownloaderService();		
		downloaderService.setRowId(rowId);
		downloaderService.setResponse(response);
		String type=downloaderService.getChatAttachmentType();
		if(type.equalsIgnoreCase("pdf")){
			response.setContentType("application/pdf");
		}else if(type.equalsIgnoreCase("doc")){
			response.setContentType("application/msword");
		}
		else if(type.equalsIgnoreCase("docx")){
			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		}
		else if(type.equalsIgnoreCase("jpg")){
			response.setContentType("image/jpeg");
		}
		else if(type.equalsIgnoreCase("png")){
			response.setContentType("image/png");
		}
		String settings = "attachment;filename=FCRA Attachment."+type;
		response.setHeader("Content-Disposition",settings);			
		downloaderService.getChatAttachment();
		return null;
	}
	@RequestMapping(value = "/get-project-doc-chat-workspace", method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getChatProjectDoc(@RequestParam String rowId, HttpServletResponse response) {
		String settings = "attachment;filename=ProjectDoc.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setRowId(rowId);
		downloaderService.setResponse(response);
		downloaderService.getChatProjectDoc();;
		return null;
	}
	
	@RequestMapping(value = "/get-BuildingByelaw-attachment-home", method = RequestMethod.POST, produces="application/pdf")
	public ModelAndView getBuildingByelawsAttachment(@RequestParam String rowId, HttpServletResponse response) {
		String settings = "attachment;filename=Building_byelaws.pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setRowId(rowId);
		downloaderService.setResponse(response);
		downloaderService.getBuildingByelawsAttachment();
		return null;
	}	

	@RequestMapping(value = {"/get-property-image-property-details", "/get-property-image-property-tracking"}, method = RequestMethod.GET, produces="image/gif")
	public ModelAndView getPropertyImage(@RequestParam String propertyId, HttpServletResponse response) {
		String settings = "inline;filename="+propertyId+"-image.jpg";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("image/gif");
		
		DownloaderService downloaderService = new DownloaderService();
		downloaderService.setPropertyId(propertyId);
		downloaderService.setResponse(response);
		downloaderService.getPropertyImage();
		return null;
	}

	@RequestMapping(value = {"/docs/{year}/{month}/{date}/{serviceCode}/{appId}/{docPath}"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getSupportingDocumnets(@PathVariable("year") String year,
			@PathVariable("month") String month,
			@PathVariable("date") String date,
			@PathVariable("serviceCode") String serviceCode,
			@PathVariable("appId") String appId,
			@PathVariable("docPath") String docPath,
			HttpServletResponse response) throws Exception{
		System.out.println("--------download controller--------");
		String settings = "inline;filename="+docPath+".pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		ServletOutputStream servletOutputStream=null;   
		servletOutputStream=response.getOutputStream();
		String filePath1 = "file:/image/FCRAdocs/"+year+"/"+month+"/"+date+"/"+"/"+serviceCode+"/"+appId+"/"+docPath+".pdf";
		String filePath2 = "file:/image/FCRAdocs/"+year+"/"+month+"/"+date+"/"+"/"+serviceCode+"/"+appId+"/"+docPath+".PDF";
		Resource resource = null;
		resource = resourceLoader.getResource(filePath1);
		if(resource.exists() == false)
			resource = resourceLoader.getResource(filePath2);

	    byte[] buf = new byte[20];
	    InputStream inputStream = resource.getInputStream();
		for(int br = inputStream.read(buf);br > -1;br=inputStream.read(buf)){
			servletOutputStream.write(buf, 0 , br);
		}		
        servletOutputStream.flush();
        servletOutputStream.close();
        inputStream.close();
		
		return null;
	}
	@RequestMapping(value = {"/docs/{year}/{month}/{date}/{serviceCode}/{appId}/{appIdEc}/{docPath}"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getSupportingaffidavit(@PathVariable("year") String year,
			@PathVariable("month") String month,
			@PathVariable("date") String date,
			@PathVariable("serviceCode") String serviceCode,
			@PathVariable("appId") String appId,
			@PathVariable("appIdEc") String appIdEc,
			@PathVariable("docPath") String docPath,
			HttpServletResponse response) throws Exception{
		String settings = "inline;filename="+docPath+".pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		ServletOutputStream servletOutputStream=null;   
		servletOutputStream=response.getOutputStream();
		String filePath1 = "file:/image/FCRAdocs/"+year+"/"+month+"/"+date+"/"+"/"+serviceCode+"/"+appId+"/"+appIdEc+"/"+docPath+".pdf";
		String filePath2 = "file:/image/FCRAdocs/"+year+"/"+month+"/"+date+"/"+"/"+serviceCode+"/"+appId+"/"+appIdEc+"/"+docPath+".PDF";
		Resource resource = null;
		resource = resourceLoader.getResource(filePath1);
		if(resource.exists() == false)
			resource = resourceLoader.getResource(filePath2);

	    byte[] buf = new byte[20];
	    InputStream inputStream = resource.getInputStream();
		for(int br = inputStream.read(buf);br > -1;br=inputStream.read(buf)){
			servletOutputStream.write(buf, 0 , br);
		}		
        servletOutputStream.flush();
        servletOutputStream.close();
        inputStream.close();
		
		return null;
	}
	
	@RequestMapping(value = {"/previousdocs/{year}/{month}/{date}/{serviceCode}/{appId}/{docPath}"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView getSupportingPreviousDocumnets(@PathVariable("year") String year,
			@PathVariable("month") String month,
			@PathVariable("date") String date,
			@PathVariable("serviceCode") String serviceCode,
			@PathVariable("appId") String appId,
			@PathVariable("docPath") String docPath,
			HttpServletResponse response) throws Exception{
		String settings = "inline;filename="+docPath+".pdf";
		response.setHeader("Content-Disposition",settings);
		response.setContentType("application/pdf");
		ServletOutputStream servletOutputStream=null;   
		servletOutputStream=response.getOutputStream();
		String filePath1 = "file:/image/FCRAdocs/"+year+"/"+month+"/"+date+"/"+"/"+serviceCode+"/"+appId+"/Deleted/"+docPath+".pdf";
		String filePath2 = "file:/image/FCRAdocs/"+year+"/"+month+"/"+date+"/"+"/"+serviceCode+"/"+appId+"/Deleted/"+docPath+".PDF";
		Resource resource = null;
		resource = resourceLoader.getResource(filePath1);
		if(resource.exists() == false)
			resource = resourceLoader.getResource(filePath2);

	    byte[] buf = new byte[20];
	    InputStream inputStream = resource.getInputStream();
		for(int br = inputStream.read(buf);br > -1;br=inputStream.read(buf)){
			servletOutputStream.write(buf, 0 , br);
		}		
        servletOutputStream.flush();
        servletOutputStream.close();
        inputStream.close();
		
		return null;
	}
	
	@RequestMapping(value={"/download-show-cause-notice-workspace"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView  downloadShowCauseNotice(@RequestParam String appId,@RequestParam String chatId,HttpServletResponse response) throws Exception{		
		//logger.debug("execute() is executed ");		
		ShowCauseNoticeService showCauseNoticeService = new ShowCauseNoticeService();
		
		showCauseNoticeService.setAppId(appId);
		showCauseNoticeService.setChatId(chatId);
		showCauseNoticeService.GetShowCauseNotice(response);		
		return null; 
	}


	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader=resourceLoader;
		
	}	
}
