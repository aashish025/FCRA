package web.services;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import models.services.ListPager;
import models.services.requests.AbstractRequest;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.services.GrievanceDetailService;
import utilities.lists.List1;
import utilities.lists.List2;
import web.Home;
import web.TokenController;
@Controller
public class GrievanceController extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Home.class);	
	private final String tokenKey = "grievance-entry";
	@RequestMapping(value={"/grievance-entry"}, method=RequestMethod.GET)
	public ModelAndView submit() throws Exception{
		GrievanceDetailService gds = new GrievanceDetailService();
		gds.populateList();
		String tokenGenerated = generateAndSaveToken(tokenKey);
		ModelAndView model = new ModelAndView();	
		model.addObject("stateList", gds.getStateList());
		model.addObject("userList", gds.getUserList());
		ObjectMapper mapper = new ObjectMapper();
		model.addObject("IbList",mapper.writeValueAsString(gds.getIbList()));
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("services/grievance");		
		return model;
	}
	@RequestMapping(value={"/district-grievance-entry"}, method=RequestMethod.GET)
	public @ResponseBody List<List2> getDistrict(@RequestParam String state) throws Exception{
		logger.debug("execute() is executed ");		
		GrievanceDetailService gds = new GrievanceDetailService();
		gds.setState(state);		
		gds.initDistrict();		 
		return gds.getDistrictList();
	}
	@RequestMapping(value={"/advance-list-grievance-entry"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<AbstractRequest> initAdvanceApplicationList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String state
			,@RequestParam String district,@RequestParam String applicationName) throws Exception{
		logger.debug("execute() is executed ");		
		GrievanceDetailService gds = new GrievanceDetailService();
		gds.setPageNum(pageNum);
		gds.setRecordsPerPage(recordsPerPage);
		gds.setSortColumn(sortColumn);
		gds.setSortOrder(sortOrder);		
		gds.setState(state);
		gds.setDistrict(district);
		gds.setAppName(applicationName.toUpperCase());
		gds.initAdvanceApplicationListDetails();
		ListPager<AbstractRequest> ar = new ListPager<AbstractRequest>();
		ar.setList(gds.getApplicationList());
		ar.setTotalRecords(gds.getTotalRecords()); 
		return ar;
	}
	@RequestMapping(value={"/submit-grievance-entry"}, method=RequestMethod.POST)
	public  @ResponseBody List<String> List(HttpServletRequest request,@RequestParam String assoName, @RequestParam String state ,
			@RequestParam String district, @RequestParam String assoAddress,@RequestParam String townCity, @RequestParam String assoPincode ,
			@RequestParam String comName, @RequestParam String comAddress,@RequestParam String comEmail, @RequestParam String comMobile,
			@RequestParam String complaint, @RequestParam String user,@RequestParam String checkedvalue,@RequestParam String requestToken,
			@RequestParam String registrationId,@RequestParam String officevalue ,@RequestParam MultipartFile documentFile ) throws Exception{
		logger.debug("execute() is executed ");		
		GrievanceDetailService grievanceservices=new GrievanceDetailService();
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	    String token=null;
	    if(result){
		grievanceservices.setAssoName(assoName);
		grievanceservices.setState(state);
		grievanceservices.setDistrict(district);
		grievanceservices.setAssoAddress(assoAddress);
		grievanceservices.setTownCity(townCity);
		grievanceservices.setAssoPincode(assoPincode);
		grievanceservices.setComName(comName);
		grievanceservices.setComAddress(comAddress);
		grievanceservices.setComEmail(comEmail);
		grievanceservices.setComMobile(comMobile);
		grievanceservices.setComplaint(complaint);
		grievanceservices.setUser(user);
		grievanceservices.setCheckedvalue(checkedvalue);
		grievanceservices.setRegistrationId(registrationId);
		grievanceservices.setOfficevalue(officevalue);
		grievanceservices.setDocumentFile(documentFile);
		grievanceservices.addDetails(request);	
	    token = getSessionToken(tokenKey);
			}
			else {
				grievanceservices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}

		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();				 
		notificationList.add(mapper.writeValueAsString(grievanceservices.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));
		return notificationList;
	}

}
