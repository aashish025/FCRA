package web.reports;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import models.services.ListPager;
import models.services.RedFlagAssociations;
import models.services.RedFlagDonors;
import models.services.requests.AbstractRequest;

import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.reports.RedFlagAssociationsServices;
import service.reports.RedFlagDonorsServices;
import utilities.ValidationException;
//import service.reports.RedFlagAssociationsServices;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;


@Controller
public class RedFlagDonorsController extends TokenController {
private final Logger logger = LoggerFactory.getLogger(RedFlagDonorsController.class);
private final String tokenKey = "red-flag-donors";
DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
@RequestMapping(value={"/red-flag-donors"}, method=RequestMethod.GET)
public ModelAndView submit() throws Exception{				
	String tokenGenerated = TokenController.generateAndSaveToken(tokenKey);
	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();          
	ServletContext context = attr.getRequest().getSession().getServletContext();
	String version = context.getInitParameter("version");
	if(ESAPI.validator().isValidInput("version", version, "version", 6, false) == false){
		throw new ValidationException("version- Invalid entry. Only  numbers and . allowed (6 characters max)");
	}
	ModelAndView model = new ModelAndView();
	RedFlagDonorsServices redFlagDonorsServices =new RedFlagDonorsServices();
	redFlagDonorsServices.execute();
	model.addObject("categoryList",redFlagDonorsServices.getCategoryList());
	model.addObject("countryList",redFlagDonorsServices.getCountryList());
	model.addObject("categoryListadd",redFlagDonorsServices.getCategoryList());
	model.addObject("countryListadd",redFlagDonorsServices.getCountryList());
	model.addObject("requestToken", tokenGenerated);
	model.addObject("currentDate", dateFormat.format(new Date()));
	int roleIdAddGrant=0;int roleIdDeleteGrant=0;
	int temp_roleId=redFlagDonorsServices.getRoleId();
	if(temp_roleId-16==17 || temp_roleId-16==0){
		roleIdAddGrant=16;
	} 
	if(temp_roleId-16==17 || temp_roleId-16==1){
			roleIdDeleteGrant=17;
		}
	model.addObject("roleIdAddGrant",roleIdAddGrant);
	model.addObject("roleIdDeleteGrant",roleIdDeleteGrant);
	int roleIdAddGrantyellow=0;int roleIdDeleteGrantyellow=0;
	int temp_roleIdyellow=redFlagDonorsServices.getYelloFlagRemove();
	if(temp_roleIdyellow-18==15 || temp_roleIdyellow-15==0){
		roleIdAddGrantyellow=15;
	} 
	if(temp_roleIdyellow-15==18 || temp_roleIdyellow-18==0){
			roleIdDeleteGrantyellow=18;
		}
	
	model.addObject("roleIdAddGrantyellow",roleIdAddGrantyellow);
	model.addObject("roleIdDeleteGrantyellow",roleIdDeleteGrantyellow);
	model.addObject("version", version);
	model.setViewName("reports/red-flag-donor");		
	return model;
}

@RequestMapping(value={"/get-list-red-flag-donors"}, method=RequestMethod.GET)
public @ResponseBody ListPager<RedFlagDonors> pullAcquisitionTypeList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagDonorsServices redFlagDonorsServices=new RedFlagDonorsServices();
	redFlagDonorsServices.setPageNum(pageNum);
	redFlagDonorsServices.setRecordsPerPage(recordsPerPage);
	redFlagDonorsServices.setSortColumn(sortColumn);
	redFlagDonorsServices.setSortOrder(sortOrder);
	redFlagDonorsServices.initRedFlagDonors();
	ListPager<RedFlagDonors> officeListPager = new ListPager<RedFlagDonors>();
	officeListPager.setList(redFlagDonorsServices.getRedFlagDonorList()); 
	officeListPager.setTotalRecords(redFlagDonorsServices.getTotalRecords()); 
	return officeListPager;
}

@RequestMapping(value={"/add-red-flag-donors"}, method=RequestMethod.GET)
public  @ResponseBody List<String>   redFlagAssociation(HttpServletRequest request, @RequestParam String donorName ,
		@RequestParam String rbiCircularIssueDate, @RequestParam String donorCountry,@RequestParam String originatorOffice, @RequestParam String originatorOrderNo ,
		@RequestParam String originatorOrderDate, @RequestParam String categoryDesc,@RequestParam String remarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam  String flagValue) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagDonorsServices redFlagDonorsServices=new RedFlagDonorsServices();
	redFlagDonorsServices.setDonorName(donorName);
	redFlagDonorsServices.setRbiCircularIssueDate(rbiCircularIssueDate);
	redFlagDonorsServices.setDonorCountry(donorCountry);
	redFlagDonorsServices.setOriginatorOffice(originatorOffice);
	redFlagDonorsServices.setOriginatorOrderDate(originatorOrderDate);
	redFlagDonorsServices.setOriginatorOrderNo(originatorOrderNo);
	redFlagDonorsServices.setCategoryDesc(categoryDesc);
	redFlagDonorsServices.setRemarks(remarkOriginatorOffice);
	redFlagDonorsServices.setFlagValue(flagValue);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
		redFlagDonorsServices.initAddRedFlagAssociation();
	token = getSessionToken(tokenKey);
	}
	else {
		redFlagDonorsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagDonorsServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}

@RequestMapping(value={"/add-red-remove-yellow-red-flag-donors"}, method=RequestMethod.GET)
public  @ResponseBody List<String>   RemoveYellowaddredFlagAssociation(HttpServletRequest request,@RequestParam String donorId, 
		@RequestParam String rbiCircularIssueDate,@RequestParam String originatorOffice, @RequestParam String originatorOrderNo ,
		@RequestParam String originatorOrderDate, @RequestParam String categoryDesc,@RequestParam String remarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam  String flagaddred) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagDonorsServices redFlagDonorsServices=new RedFlagDonorsServices();
	redFlagDonorsServices.setDonorId(donorId);
	redFlagDonorsServices.setRbiCircularIssueDate(rbiCircularIssueDate);
	redFlagDonorsServices.setOriginatorOffice(originatorOffice);
	redFlagDonorsServices.setOriginatorOrderDate(originatorOrderDate);
	redFlagDonorsServices.setOriginatorOrderNo(originatorOrderNo);
	redFlagDonorsServices.setCategoryDesc(categoryDesc);
	redFlagDonorsServices.setRemarks(remarkOriginatorOffice);
	redFlagDonorsServices.setFlagValue(flagaddred);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
		redFlagDonorsServices.initAddRedRemoveYellowFlagAssociation();
	token = getSessionToken(tokenKey);
	}
	else {
		redFlagDonorsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagDonorsServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}

@RequestMapping(value={"/delete-red-flag-donors"}, method=RequestMethod.GET)
public @ResponseBody List<String> deleteAcquisitionList(HttpServletRequest request,@RequestParam String donorId,@RequestParam String deloriginatorOffice, @RequestParam String deloriginatorOrderNo ,
		@RequestParam String deloriginatorOrderDate,@RequestParam String delremarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam String flagdelete) throws Exception{
	logger.debug("execute() is executed ");			
	RedFlagDonorsServices redFlagDonorsServices=new RedFlagDonorsServices();
	redFlagDonorsServices.setDonorId(donorId);
	redFlagDonorsServices.setFlagdelete(flagdelete);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
		redFlagDonorsServices.initDeleteRedFlagDonors(donorId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice);
		token = getSessionToken(tokenKey);
	}
	else {
		redFlagDonorsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING,Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagDonorsServices.getNotifyList()));	
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}
@RequestMapping(value={"/get-search-list-red-flag-donors"}, method=RequestMethod.GET)
public @ResponseBody ListPager<RedFlagDonors> initApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
		,@RequestParam String applicationName) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagDonorsServices redflag =new RedFlagDonorsServices();
	redflag.setPageNum(pageNum);
	redflag.setRecordsPerPage(recordsPerPage);
	redflag.setSortColumn(sortColumn);
	redflag.setSortOrder(sortOrder);	
	//redflag.setAppId(applicationId.toUpperCase());
	redflag.setAppName(applicationName);
	redflag.initApplicationListDetails();
	ListPager<RedFlagDonors> officeListPager = new ListPager<RedFlagDonors>();
	officeListPager.setList(redflag.getApplicationList()); 
	officeListPager.setTotalRecords(redflag.getTotalRecords()); 
	return officeListPager;
}
	
}
