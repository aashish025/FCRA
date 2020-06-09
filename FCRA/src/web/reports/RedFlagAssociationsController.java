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
import service.reports.RedFlaggedRcnsService;
import utilities.ValidationException;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;


@Controller
public class RedFlagAssociationsController extends TokenController {
private final Logger logger = LoggerFactory.getLogger(RedFlagAssociationsController.class);
private final String tokenKey = "red-flag-associations";
DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
@RequestMapping(value={"/red-flag-associations"}, method=RequestMethod.GET)
public ModelAndView submit() throws Exception{				
	String tokenGenerated = TokenController.generateAndSaveToken(tokenKey);
	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();          
	ServletContext context = attr.getRequest().getSession().getServletContext();
	String version = context.getInitParameter("version");
	if(ESAPI.validator().isValidInput("version", version, "version", 6, false) == false){
		throw new ValidationException("version- Invalid entry. Only  numbers and . allowed (6 characters max)");
	}
	ModelAndView model = new ModelAndView();
	RedFlagAssociationsServices redAssociationsServices =new RedFlagAssociationsServices();
	redAssociationsServices.execute();
	
	model.addObject("categoryList",redAssociationsServices.getCategoryList());
	model.addObject("categoryListadd", redAssociationsServices.getCategoryList());
	model.addObject("stateList",redAssociationsServices.getStateList());
	model.addObject("requestToken", tokenGenerated);
	model.addObject("currentDate", dateFormat.format(new Date()));
	int roleIdAddGrant=0;int roleIdDeleteGrant=0;
	int temp_roleId=redAssociationsServices.getRoleId();
	if(temp_roleId-16==17 || temp_roleId-16==0){
		roleIdAddGrant=16;
	} 
	if(temp_roleId-16==17 || temp_roleId-16==1){
			roleIdDeleteGrant=17;
		}
	model.addObject("roleIdAddGrant",roleIdAddGrant);
	model.addObject("roleIdDeleteGrant",roleIdDeleteGrant);
	int roleIdAddGrantyellow=0;int roleIdDeleteGrantyellow=0;
	int temp_roleIdyellow=redAssociationsServices.getYelloFlagRemove();
	if(temp_roleIdyellow-18==15 || temp_roleIdyellow-15==0){
		roleIdAddGrantyellow=15;
	} 
	if(temp_roleIdyellow-15==18 || temp_roleIdyellow-18==0){
			roleIdDeleteGrantyellow=18;
		}
	
	model.addObject("roleIdAddGrantyellow",roleIdAddGrantyellow);
	model.addObject("roleIdDeleteGrantyellow",roleIdDeleteGrantyellow);
	model.addObject("version", version);
	model.setViewName("reports/red-flag-associations");		
	return model;
}

@RequestMapping(value={"/get-list-red-flag-associations"}, method=RequestMethod.GET)
public @ResponseBody ListPager<RedFlagAssociations> pullAcquisitionTypeList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagAssociationsServices redFlagAssociationsServices=new RedFlagAssociationsServices();
	redFlagAssociationsServices.setPageNum(pageNum);
	redFlagAssociationsServices.setRecordsPerPage(recordsPerPage);
	redFlagAssociationsServices.setSortColumn(sortColumn);
	redFlagAssociationsServices.setSortOrder(sortOrder);
	redFlagAssociationsServices.initRedFlagAssociations();
	ListPager<RedFlagAssociations> officeListPager = new ListPager<RedFlagAssociations>();
	officeListPager.setList(redFlagAssociationsServices.getRedFlagAssociationsList()); 
	officeListPager.setTotalRecords(redFlagAssociationsServices.getTotalRecords()); 
	return officeListPager;
}

@RequestMapping(value={"/add-red-flag-associations"}, method=RequestMethod.GET)
public  @ResponseBody List<String>   redFlagAssociation(HttpServletRequest request, @RequestParam String assoName ,
		@RequestParam String assoAddress, @RequestParam String assoState,@RequestParam String originatorOffice, @RequestParam String originatorOrderNo ,
		@RequestParam String originatorOrderDate, @RequestParam String categoryDesc,@RequestParam String remarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam  String flagValue) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagAssociationsServices redFlagAssociationsServices=new RedFlagAssociationsServices();
	redFlagAssociationsServices.setAssoName(assoName);
	redFlagAssociationsServices.setAssoAddress(assoAddress);
	redFlagAssociationsServices.setAssoState(assoState);
	redFlagAssociationsServices.setOriginatorOffice(originatorOffice);
	redFlagAssociationsServices.setOriginatorOrderDate(originatorOrderDate);
	redFlagAssociationsServices.setOriginatorOrderNo(originatorOrderNo);
	redFlagAssociationsServices.setCategoryDesc(categoryDesc);
	redFlagAssociationsServices.setRemarks(remarkOriginatorOffice);
	redFlagAssociationsServices.setFlagValue(flagValue);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
	redFlagAssociationsServices.initAddRedFlagAssociation();
	token = getSessionToken(tokenKey);
	}
	else {
		redFlagAssociationsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagAssociationsServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}
@RequestMapping(value={"/add-yellow-red-flag-associations"}, method=RequestMethod.GET)
public  @ResponseBody List<String> YellowFlagAssociation(HttpServletRequest request, @RequestParam String assoName ,
		@RequestParam String assoAddress, @RequestParam String assoState,@RequestParam String originatorOffice, @RequestParam String originatorOrderNo ,
		@RequestParam String originatorOrderDate, @RequestParam String categoryDesc,@RequestParam String remarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam  String flagValue) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagAssociationsServices redFlagAssociationsServices=new RedFlagAssociationsServices();
	redFlagAssociationsServices.setAssoName(assoName);
	redFlagAssociationsServices.setAssoAddress(assoAddress);
	redFlagAssociationsServices.setAssoState(assoState);
	redFlagAssociationsServices.setOriginatorOffice(originatorOffice);
	redFlagAssociationsServices.setOriginatorOrderDate(originatorOrderDate);
	redFlagAssociationsServices.setOriginatorOrderNo(originatorOrderNo);
	redFlagAssociationsServices.setCategoryDesc(categoryDesc);
	redFlagAssociationsServices.setRemarks(remarkOriginatorOffice);
	redFlagAssociationsServices.setFlagValue(flagValue);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
	redFlagAssociationsServices.initAddYellowFlagAssociation();
	token = getSessionToken(tokenKey);
	}
	else {
		redFlagAssociationsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagAssociationsServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}

@RequestMapping(value={"/delete-red-flag-associations"}, method=RequestMethod.GET)
public @ResponseBody List<String> deleteAcquisitionList(HttpServletRequest request,@RequestParam String assoId,@RequestParam String deloriginatorOffice, @RequestParam String deloriginatorOrderNo ,
		@RequestParam String deloriginatorOrderDate,@RequestParam String delremarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam String flagdelete) throws Exception{
	logger.debug("execute() is executed ");			
	RedFlagAssociationsServices redFlagAssociationsServices=new RedFlagAssociationsServices();
	redFlagAssociationsServices.setAssoId(assoId);
	redFlagAssociationsServices.setDeleteflagValue(flagdelete);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
	redFlagAssociationsServices.initDeleteRedFlagAssociations(assoId,deloriginatorOffice,deloriginatorOrderNo,deloriginatorOrderDate,delremarkOriginatorOffice);
	token = getSessionToken(tokenKey);
	}
	else {
		redFlagAssociationsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING,Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagAssociationsServices.getNotifyList()));	
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}
@RequestMapping(value={"/add-red-remove-yellow-red-flag-associations"}, method=RequestMethod.GET)
public  @ResponseBody List<String>   RemoveYellowaddredFlagAssociation(HttpServletRequest request,@RequestParam String assoId, 
		@RequestParam String originatorOffice, @RequestParam String originatorOrderNo ,
		@RequestParam String originatorOrderDate, @RequestParam String categoryDesc,@RequestParam String remarkOriginatorOffice,@RequestParam  String requestToken,@RequestParam  String flagaddred) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagAssociationsServices redFlagAssociationsServices=new RedFlagAssociationsServices();
	redFlagAssociationsServices.setAssoId(assoId);
	redFlagAssociationsServices.setOriginatorOffice(originatorOffice);
	redFlagAssociationsServices.setOriginatorOrderDate(originatorOrderDate);
	redFlagAssociationsServices.setOriginatorOrderNo(originatorOrderNo);
	redFlagAssociationsServices.setCategoryDesc(categoryDesc);
	redFlagAssociationsServices.setRemarks(remarkOriginatorOffice);
	redFlagAssociationsServices.setFlagValue(flagaddred);
	Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
	String token = null;
	if(result){
		redFlagAssociationsServices.initAddRedRemoveYellowFlagAssociation();
	token = getSessionToken(tokenKey);
	}
	else {
		redFlagAssociationsServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
	}
	ObjectMapper mapper = new ObjectMapper();
	List<String> notificationList=new ArrayList<String>();				 
	notificationList.add(mapper.writeValueAsString(redFlagAssociationsServices.getNotifyList()));
	notificationList.add(mapper.writeValueAsString(new List1(token)));
	return notificationList;
}

@RequestMapping(value={"/get-search-list-red-flag-associations"}, method=RequestMethod.GET)
public @ResponseBody ListPager<RedFlagAssociations> initApplicationList(@RequestParam String pageNum, 
		@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder,@RequestParam String applicationId
		,@RequestParam String applicationName) throws Exception{
	logger.debug("execute() is executed ");		
	RedFlagAssociationsServices redflag =new RedFlagAssociationsServices();
	redflag.setPageNum(pageNum);
	redflag.setRecordsPerPage(recordsPerPage);
	redflag.setSortColumn(sortColumn);
	redflag.setSortOrder(sortOrder);	
	redflag.setAppName(applicationName);
	redflag.initApplicationListDetails();
	ListPager<RedFlagAssociations> officeListPager = new ListPager<RedFlagAssociations>();
	officeListPager.setList(redflag.getApplicationList());
	officeListPager.setTotalRecords(redflag.getTotalRecords()); 
	return officeListPager;
}
	
}
