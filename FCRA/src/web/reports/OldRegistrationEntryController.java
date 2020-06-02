package web.reports;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.download.DownloaderService;
import service.reports.OldRegistrationEntryService;
import utilities.ValidationException;
import utilities.lists.List1;
import utilities.lists.List2;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import web.TokenController;

	@Controller
	public class OldRegistrationEntryController extends TokenController {
		private final Logger logger = LoggerFactory.getLogger(OldRegistrationEntryController.class);
		private final String tokenKey = "old-registration-entry";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		@RequestMapping(value={"/old-registration-entry"}, method=RequestMethod.GET)
		public ModelAndView submit() throws Exception{
			String tokenGenerated = generateAndSaveToken(tokenKey);
			OldRegistrationEntryService pds = new OldRegistrationEntryService();
			pds.execute();	
			ModelAndView model = new ModelAndView();
			model.addObject("stateList", pds.getStateList());
			model.addObject("assoNatureList",pds.getAssoNatureList());
			ObjectMapper mapper = new ObjectMapper();
			model.addObject("assoNatureList",mapper.writeValueAsString(pds.getAssoNatureList()));
			model.addObject("banknameList",pds.getBanknameList());		
			model.addObject("requestToken", tokenGenerated);
		    model.setViewName("reports/old-registration-entry");		
			return model;
		}
	
		@RequestMapping(value={"/getdistrict-old-registration-entry"}, method=RequestMethod.GET)
			public @ResponseBody List<List2> getDistrictList(@RequestParam String state) throws Exception{
			logger.debug("execute() is executed ");			
			OldRegistrationEntryService pds = new OldRegistrationEntryService();
			pds.setState(state);	
			pds.initDistrictList();
			return  pds.getDistrictList();
	}	
	
		
		@RequestMapping(value={"/get-asso-nature-old-registration-entry"}, method=RequestMethod.GET)
			public @ResponseBody List<List2> getReligionList(@RequestParam String assoNature) throws Exception{
			logger.debug("execute() is executed ");			
			OldRegistrationEntryService pds = new OldRegistrationEntryService();
			pds.setAssoNature(assoNature);	
			pds.initReligionList();
			return  pds.getReligionList();
	}	
		
	
		@RequestMapping(value={"/add-old-registration-entry"}, method=RequestMethod.GET)
		public  @ResponseBody List<String>   oldRegistrationEntry(HttpServletRequest request, @RequestParam String assoName,
				@RequestParam String assoTownCity, @RequestParam String assoAddress,@RequestParam String state, @RequestParam String district,
				@RequestParam String assoPin, @RequestParam String assoNature, @RequestParam String assoReligion,
				@RequestParam String assoAims, @RequestParam String regDate , @RequestParam String validFrom ,
				@RequestParam String validTo,@RequestParam String userId,@RequestParam String accNumber,
				@RequestParam String bankName,@RequestParam String bankAddress,@RequestParam String bankTownCity,
				@RequestParam String bankState,@RequestParam String bankDistrict,@RequestParam String accountNumber,
				@RequestParam String bankZipCode,@RequestParam String oldregRemark,@RequestParam String requestToken ) throws Exception{ /*,@RequestParam  String requestToken*/
			logger.debug("execute() is executed ");		
			//SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			
			OldRegistrationEntryService OldRegistrationEntryServices=new OldRegistrationEntryService();
			String token = null;
	        String res = null;
	        Boolean isTokenValid = TokenController.isTokenValid(tokenKey,requestToken);			
	        if(isTokenValid){
			OldRegistrationEntryServices.setAssoName(assoName);
			OldRegistrationEntryServices.setAssoTownCity(assoTownCity);
			OldRegistrationEntryServices.setAssoAddress(assoAddress);
			OldRegistrationEntryServices.setState(state);
			OldRegistrationEntryServices.setDistrict(district);			
			OldRegistrationEntryServices.setAssoPin(assoPin);
			OldRegistrationEntryServices.setAssoNature(assoNature);
			OldRegistrationEntryServices.setAssoReligion(assoReligion);
			OldRegistrationEntryServices.setAssoAims(assoAims);
			OldRegistrationEntryServices.setRegDate(regDate);
			OldRegistrationEntryServices.setValidFrom(validFrom);
			OldRegistrationEntryServices.setValidTo(validTo);
			OldRegistrationEntryServices.setUserId(userId);
			
			OldRegistrationEntryServices.setBankName(bankName);
			OldRegistrationEntryServices.setAccountNumber(accountNumber);	
			OldRegistrationEntryServices.setBankAddress(bankAddress);
			OldRegistrationEntryServices.setBankTownCity(bankTownCity);
			OldRegistrationEntryServices.setBankState(bankState);
			OldRegistrationEntryServices.setBankDistrict(bankDistrict);
			OldRegistrationEntryServices.setBankZipCode(bankZipCode);		
			OldRegistrationEntryServices.setOldregRemark(oldregRemark);		
	       
	        	res= OldRegistrationEntryServices.initAddOldRegEntry();
	        	token = getSessionToken(tokenKey);	
	        }else {
	        	OldRegistrationEntryServices.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
			}			
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				
			notificationList.add(mapper.writeValueAsString(OldRegistrationEntryServices.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(token)));
			notificationList.add(mapper.writeValueAsString(OldRegistrationEntryServices.getRcn()));
			notificationList.add(mapper.writeValueAsString(new List1(res)));
			return notificationList;
		}	
	
		
		@RequestMapping(value="generatecertificate-old-registration-entry",method=RequestMethod.GET)
		public ModelAndView getReport(@RequestParam("rcn") String rcn){
			OldRegistrationEntryService OldRegistrationEntryServices=new OldRegistrationEntryService();	
			OldRegistrationEntryServices.getOldRegistrationCertification(rcn);
			return null;
		}
		
		
	   @RequestMapping(value="generateReprintcertificate-old-registration-entry",method=RequestMethod.GET)
		public @ResponseBody List<String> getPrintReport(@RequestParam String rcn) throws Exception{
			OldRegistrationEntryService oldRegistrationEntryServices=new OldRegistrationEntryService();	
			String rslt=null;
			oldRegistrationEntryServices.setRcn(rcn);
			rslt=oldRegistrationEntryServices.initPrintDetails();
			ObjectMapper mapper = new ObjectMapper();
			List<String> notificationList=new ArrayList<String>();				
			notificationList.add(mapper.writeValueAsString(oldRegistrationEntryServices.getNotifyList()));
			notificationList.add(mapper.writeValueAsString(new List1(rslt)));
			notificationList.add(mapper.writeValueAsString(oldRegistrationEntryServices.getRcnStatus()));
			return notificationList;
		}
	
}
