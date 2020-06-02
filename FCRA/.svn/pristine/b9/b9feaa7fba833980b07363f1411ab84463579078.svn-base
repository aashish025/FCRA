package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import service.services.PasswordService;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;

@Controller
public class ChangePassword extends TokenController {	
	private final Logger logger = LoggerFactory.getLogger(ChangePassword.class);
	//private PasswordService passwordService;
	private final String tokenKey = "change-password";

	@RequestMapping(value = "/change-password", method = RequestMethod.GET)
	public ModelAndView tester() throws Exception {
		logger.debug("tester() is executed ");
		String tokenGenerated = generateAndSaveToken(tokenKey);
		PasswordService passwordService = new PasswordService();
		passwordService.checkPasswordStatus();
		ModelAndView model = new ModelAndView();
		model.addObject("notifyList", passwordService.getNotifyList());
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("services/password");
		return model;
	}	
	
	@RequestMapping(value = "/change-password-submit-change-password", method = RequestMethod.POST)
	public ModelAndView changePassword(
			@RequestParam("currentPasswordWithHash") String currentPasswordWithHash,
			@RequestParam("newPasswordWithHash") String newPasswordWithHash,
			@RequestParam("newPasswordConfirmedWithHash") String newPasswordConfirmedWithHash, 
			@RequestParam ("requestToken") String requestToken) throws Exception {
		logger.debug("changePassword() is executed ");
		PasswordService passwordService = new PasswordService();
		ModelAndView model = new ModelAndView();
		//COMPARE TOKEN
		Boolean result = TokenController.isTokenValid(tokenKey, requestToken);
		String token = null;
		String status = null;
		if(result) {
			status = passwordService.execute(currentPasswordWithHash, newPasswordWithHash,
				newPasswordConfirmedWithHash);
			if (status.equals("passwordForcefullyChanged")) {
				model.addObject("forcedChange", "true");
			} else {
				model.addObject("forcedChange", "false");
			}
			token = getSessionToken(tokenKey);
		} else {
			passwordService.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		};
		//ModelAndView model = new ModelAndView();
		model.addObject("notifyList", passwordService.getNotifyList());
		model.addObject("requestToken", token);
		/*if (status.equals("passwordForcefullyChanged")) {
			model.addObject("forcedChange", "true");
		} else {
			model.addObject("forcedChange", "false");
		}*/
		model.setViewName("services/password");
		return model;
	}
}
