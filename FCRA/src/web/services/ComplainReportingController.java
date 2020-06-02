package web.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import models.services.ListPager;
import models.services.requests.ComplainRegisterBean;
import models.services.requests.ParentMenuName;
import service.services.ComplainReportingService;
import utilities.UserSession;

@Controller
public class ComplainReportingController {
	private final Logger logger = LoggerFactory.getLogger(ComplainReportingController.class);

	@RequestMapping(value = { "/issue-add-reporting" }, method = RequestMethod.GET)
	public String execute(Map<String, Object> model, HttpServletRequest request) throws Exception {
		logger.debug("execute() is executed ");
		ComplainRegisterBean triform = new ComplainRegisterBean();
		model.put("triform", triform);
		ComplainReportingService crs = new ComplainReportingService();
		List<ParentMenuName> menuname = crs.populatemenuname(request);
		model.put("MenuList", menuname);
		return "services/complain-reporting";
	}

	@RequestMapping(value = { "/trisupportform-issue-add-reporting" }, method = RequestMethod.POST)
	public ModelAndView trisupportdata(@Valid @ModelAttribute("triform") ComplainRegisterBean complainRegisterBean,
			BindingResult br, HttpServletRequest request, @RequestParam("attachedFile1") MultipartFile[] attachedFile1,Map<String, Object> modell,HttpSession session)
			throws Exception {
		int cid=0;
		ModelAndView model = new ModelAndView();
		String path=session.getServletContext().getContextPath();  
		System.out.println("------"+path);
		ComplainReportingService crs = new ComplainReportingService();
		List<ParentMenuName> menuname = crs.populatemenuname(request);
		model.addObject("MenuList", menuname);
		if (br.hasErrors()) {
			model.setViewName("services/complain-reporting");
			return model;
		}
		HttpSession http = request.getSession();
		UserSession user = (UserSession) http.getAttribute("user");
	
		for (int i = 0; i < attachedFile1.length; i++) {
			MultipartFile file = attachedFile1[i];
			complainRegisterBean.setComplaintRaisedBy(user.getUserId());
			complainRegisterBean.setSentMacAddress(request.getRemoteAddr());
			complainRegisterBean.setSentBy(user.getUserName());
			complainRegisterBean.setComplaintStatus("PENDING");
			if(file.getSize()!=0) {
			complainRegisterBean.setComplaintAttachmentStatus("1");
			}
			else {
				complainRegisterBean.setComplaintAttachmentStatus("0");
			}
			complainRegisterBean.setRecordStatus("1");
			if(file.getSize()<5242880) {
				if(i==0)
			 cid = crs.populatetriform(complainRegisterBean, request);
			}else {
				model.addObject("cid", "Error : image size not greater than 5 MB");
				model.setViewName("services/complain-reporting");
				return model;
			}
			if(cid!=0) {
			Date date = new Date();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			int year = calendar.get(Calendar.YEAR);
			// Add one to month {0 - 11}
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			new File("\\image\\FCRATIRData\\" + year + "\\" + month + "\\" + day).mkdirs();
			
			try {
				//System.out.println("!!!!----" + file.getSize()+"---"+attachedFile1.length+"---"+cid);
				byte barr[] = file.getBytes();

				BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(
						"\\image\\FCRATIRData\\" + year + "\\" + month + "\\" + day + "\\" + cid + "_" + i + ".jpg"));
				bout.write(barr);
				bout.flush();
				bout.close();

			} catch (Exception e) {
				// return "You failed to upload " + name + " => " + e.getMessage();
			}
			}
			else {
			model.addObject("cid", "Error");
			model.setViewName("services/complain-reporting");
			return model;
		}
			model.addObject("cid", "Tracking Complaint Id: " + cid);
			model.setViewName("services/track-complain");
		}
	
		ComplainRegisterBean trireplyform = new ComplainRegisterBean();
		modell.put("trireplyform", trireplyform);
		return model;
	}

	@RequestMapping(value = { "/issue-track-reporting" }, method = RequestMethod.GET)
	public String trackcomplain(Map<String, Object> modell, HttpServletRequest request) throws Exception {
		logger.debug("execute() is executed ");
		ComplainRegisterBean trireplyform = new ComplainRegisterBean();
		modell.put("trireplyform", trireplyform);
		return "services/track-complain";
	}

	@RequestMapping(value = { "/lisi-issue-track-reporting" }, method = RequestMethod.GET)
	public @ResponseBody ListPager<ComplainRegisterBean> initTrackRequest(
			@RequestParam MultiValueMap<String, String> params, HttpServletRequest req) throws Exception {
		logger.debug("execute() is executed ");
		ComplainReportingService nds = new ComplainReportingService();
		nds.setParameterMap(params);
		nds.pullTrackRequest(req);
		ListPager<ComplainRegisterBean> ReportList = new ListPager<ComplainRegisterBean>();
		ReportList.setList(nds.getTrackRequestList());
		ReportList.setTotalRecords(nds.getTrackRequestCount());
		return ReportList;
	}
	@RequestMapping(value = { "/trireplyform-issue-track-reporting" }, method = RequestMethod.POST)
	public ModelAndView trireplydata(@Valid @ModelAttribute("trireplyform") ComplainRegisterBean complainRegisterBean,
			BindingResult br, HttpServletRequest request)
			throws Exception {
		ModelAndView model = new ModelAndView();
		if (br.hasErrors()) {
			model.setViewName("services/track-complain");
			return model;
		}
		HttpSession http = request.getSession();
		UserSession user = (UserSession) http.getAttribute("user");
		complainRegisterBean.setComplaintRaisedBy(user.getUserId());
		System.out.println("------"+complainRegisterBean.getComplaintSubject());
		System.out.println("------"+complainRegisterBean.getComplaintId());
		System.out.println("------"+complainRegisterBean.getComplaintSentDescription());
		System.out.println("------"+complainRegisterBean.getComplaintRaisedBy());
		ComplainReportingService crs=new ComplainReportingService();
		crs.populatetrireplyform(complainRegisterBean);
		model.setViewName("services/track-complain");
		return model;
}
	
}
