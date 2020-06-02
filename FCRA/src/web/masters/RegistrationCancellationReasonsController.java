/*package web.masters;

public class RegistrationCancellationReasonsController {

}
*/
package web.masters;
import java.util.ArrayList;
import java.util.List;
import models.services.ListPager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import service.masters.UserStatusService;
import utilities.lists.List1;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import web.Home;
import web.TokenController;
import service.masters.RegistrationCancellationReasonsService;


@Controller
public class RegistrationCancellationReasonsController extends TokenController {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	private final String tokenKey = "registration-cancellation-reasons";
		
	@RequestMapping(value={"/registration-cancellation-reasons"}, method=RequestMethod.GET)
	public ModelAndView submit(){
		logger.debug("execute() is executed ");	
		String tokenGenerated = generateAndSaveToken(tokenKey);
		RegistrationCancellationReasonsService cancelReasons=new RegistrationCancellationReasonsService();
		ModelAndView model = new ModelAndView();
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("masters/Regn-cancellation-reasons"); 
		return model;
	}
	
	@RequestMapping(value={"/get-registration-cancellation-reasons"}, method=RequestMethod.GET)
	public @ResponseBody ListPager<models.master.RegistrationCancellationReasons> pullUserList(@RequestParam String pageNum, 
			@RequestParam String recordsPerPage, @RequestParam String sortColumn, @RequestParam String sortOrder) throws Exception{
		logger.debug("execute() is executed ");		
		RegistrationCancellationReasonsService cancelReasons = new RegistrationCancellationReasonsService();
		cancelReasons.setPageNum(pageNum);
		cancelReasons.setRecordsPerPage(recordsPerPage);
		cancelReasons.setSortColumn(sortColumn);
		cancelReasons.setSortOrder(sortOrder);
		cancelReasons.initializeRegistrationCancellationReasonsList();
		ListPager<models.master.RegistrationCancellationReasons> userListPager = new ListPager<models.master.RegistrationCancellationReasons>();
		userListPager.setList(cancelReasons.getCancelReasonList());
		userListPager.setTotalRecords(cancelReasons.getTotalRecords()); 
		return userListPager;
	}
	
	@RequestMapping(value={"/add-registration-cancellation-reasons"}, method=RequestMethod.POST)
	public @ResponseBody List<String> addUserList(@RequestParam String reasonDesc,@RequestParam String reasonId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		RegistrationCancellationReasonsService cancelReasons = new RegistrationCancellationReasonsService();
		if(result) {
			cancelReasons.setReasonDesc(reasonDesc);	
			cancelReasons.setReasonId(reasonId);	
			cancelReasons.initAddRegistrationCancellationReasons();
		token = getSessionToken(tokenKey);
		}
		else {
			cancelReasons.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(cancelReasons.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
	
	@RequestMapping(value={"/delete-registration-cancellation-reasons"}, method=RequestMethod.GET)
	public @ResponseBody List<String> deleteuserList(@RequestParam String reasonId,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		RegistrationCancellationReasonsService cancelReasons = new RegistrationCancellationReasonsService();
		if(result) {
		cancelReasons.setReasonId(reasonId);				
		cancelReasons.initDeleteRegistrationCancellationReasons();
		token = getSessionToken(tokenKey);
		}
		else {
			cancelReasons.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(cancelReasons.getNotifyList()));	
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	}
@RequestMapping(value={"/edit-registration-cancellation-reasons"}, method=RequestMethod.GET)
	public @ResponseBody List<String> editUserList(@RequestParam String reasonId,@RequestParam String reasonDesc,@RequestParam String requestToken) throws Exception{
		logger.debug("execute() is executed ");	
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		RegistrationCancellationReasonsService cancelReasons = new RegistrationCancellationReasonsService();
		if(result) {
			cancelReasons.setReasonId(reasonId);
		cancelReasons.setReasonDesc(reasonDesc);
		cancelReasons.initEditRegistrationCancellationReasons();
		token = getSessionToken(tokenKey);
		}
		else {
			cancelReasons.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		ObjectMapper mapper=new ObjectMapper();
		List<String> notificationList=new ArrayList<String>();
		notificationList.add(mapper.writeValueAsString(cancelReasons.getNotifyList()));
		notificationList.add(mapper.writeValueAsString(new List1(token)));	
		return notificationList;
	
	}
}