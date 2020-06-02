package web.reports;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.owasp.esapi.codecs.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.services.dashboard.ProjectDashboardService;
import service.services.dashboard.reports.DashboardReportService;

@Controller
public class DashboardReport {
	@RequestMapping(value={"/init-mail-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> initGenerateReport(@RequestParam String svcCode,@RequestParam String appId,@RequestParam String hosPdfFormat) throws Exception{		
		DashboardReportService drs=new DashboardReportService();
		drs.setAppId(appId);
		drs.setSvcCode(svcCode);	
		drs.setHosPdfFormat(hosPdfFormat);
		String actionStatus=drs.initMailWithOutSign();	
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusDetails=new ArrayList<String>();				 
		statusDetails.add(mapper.writeValueAsString(drs.getNotifyList()));
		statusDetails.add(actionStatus);
		return statusDetails;
	}
	
	@RequestMapping(value={"/init-mail-with-sign-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> initMailWithSign(@RequestParam String svcCode,@RequestParam String appId,@RequestParam String hosPdfFormat,
			@RequestParam String signedData) throws Exception{		
		DashboardReportService drs=new DashboardReportService();
		drs.setAppId(appId);
		drs.setSvcCode(svcCode);	
		drs.setHosPdfFormat(hosPdfFormat);
		drs.setAttachment(Base64.decode(signedData));
		String actionStatus=drs.initMail();	
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusDetails=new ArrayList<String>();				 
		statusDetails.add(mapper.writeValueAsString(drs.getNotifyList()));
		statusDetails.add(actionStatus);
		return statusDetails;
	}
	
	@RequestMapping(value={"/generate-pdf-bytes-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> initGenerateSignedBytes(@RequestParam String svcCode,@RequestParam String appId,@RequestParam String hosPdfFormat) throws Exception{		
		DashboardReportService drs=new DashboardReportService();
		drs.setAppId(appId);
		drs.setSvcCode(svcCode);	
		drs.setHosPdfFormat(hosPdfFormat);
		String actionStatus=drs.generateBytes();	
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusDetails=new ArrayList<String>();				 
		statusDetails.add(mapper.writeValueAsString(drs.getNotifyList()));
		statusDetails.add(Base64.encodeBytes(drs.getAttachment()));
		statusDetails.add(actionStatus);
		return statusDetails;
	}
	
	@RequestMapping(value={"/check-digital-sign-flag-workspace"}, method=RequestMethod.POST)
	public @ResponseBody List<String> checkDigitalSignFlag() throws Exception{		
		DashboardReportService drs=new DashboardReportService();		
		String actionStatus=drs.checkDigitalSignFlagStatus();	
		ObjectMapper mapper = new ObjectMapper();
		List<String> statusDetails=new ArrayList<String>();	
		statusDetails.add(actionStatus);
		statusDetails.add(mapper.writeValueAsString(drs.getNotifyList()));		
		statusDetails.add(drs.getDigitalSignStatus());		
		return statusDetails;
	}
	
	@RequestMapping(value={"/generate-pdf-workspace"}, method = RequestMethod.GET, produces="application/pdf")
	public ModelAndView  initGenerateReportPdf(@RequestParam String appId,@RequestParam String svcCode,@RequestParam String hosPdfFormat) throws Exception{		
		DashboardReportService drs=new DashboardReportService();
		drs.setAppId(appId);
		drs.setSvcCode(svcCode);		
		drs.setHosPdfFormat(hosPdfFormat);
		drs.initGenerateReport();		
		return null; 
	}
	@RequestMapping(value={"/generate-signed-pdf-workspace"}, method = RequestMethod.POST, produces="application/pdf")
	public ModelAndView  initGenerateSignedPdf(@RequestParam String appId,@RequestParam String svcCode,HttpServletResponse response) throws Exception{		
		DashboardReportService drs=new DashboardReportService();
		drs.setAppId(appId);
		drs.setSvcCode(svcCode);		
		drs.GetSignedCertificate(response);		
		return null; 
	}
	
}
