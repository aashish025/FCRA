package web.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.services.ApplicationTrackingService;
import utilities.lists.List1;
@Controller
public class FetchInadvertentlyClarificationRequest {
	private final Logger logger = LoggerFactory.getLogger(ApplicationTracking.class);

	@RequestMapping(value={"/Fetch-Inadvertently-Clarification-Request"}, method=RequestMethod.GET)
	public ModelAndView submit(){		
		System.out.println("details------11111");
		ModelAndView model = new ModelAndView();		
		model.setViewName("services/Fetch-Inadvertently-Clarification-Request"); 
		return model;
	}
	
	@RequestMapping(value={"/pull-basic-details-Fetch-Inadvertently-Clarification-Request"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullApplicationDetails(@RequestParam String applicationId) throws Exception{
		
		ApplicationTrackingService tracker = new ApplicationTrackingService();
		tracker.setApplicationId(applicationId.toUpperCase());
		String result = tracker.pullApplicationDetails();		
		String applicationDetails = tracker.getApplicationDetails();
		List<String> details=new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();
		//details.add(mapper.writeValueAsString(new List1(result)));
		//details.add(mapper.writeValueAsString(tracker.getNotifyList()));
		details.add(applicationDetails);
		//details.add(mapper.writeValueAsString(tracker.getSmsList()));
		//details.add(mapper.writeValueAsString(tracker.getMailList()));
		System.out.println("details11------"+details);
		return details;
	}
}
