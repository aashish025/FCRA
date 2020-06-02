package web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.auth.LoginService;
import service.auth.LogoutService;
import utilities.notifications.Notification;
import utilities.notifications.Status;
import utilities.notifications.Type;
 

 
@Controller
public class Login extends TokenController{
	private final Logger logger = LoggerFactory.getLogger(Login.class);	
	//private LoginService loginservice;
	//private LogoutService logoutservice;
	private final String resetTokenKey = "reset-password";
	/*
	@Autowired
	public Login(LoginService loginService){
		this.loginservice=loginService;
	}
	*/	
	@RequestMapping(value={"","/","/login"}, method=RequestMethod.GET)
	public ModelAndView login(){
		logger.debug("hello() is executed - $name {}");	
		String tokenGenerated = generateAndSaveToken(resetTokenKey);
		ModelAndView model = new ModelAndView();	
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("login/login");
		return model;
	}
	
	@RequestMapping(value={"/pull-captcha-login"}, method=RequestMethod.GET)
	public @ResponseBody List<String> pullCaptcha() throws Exception{
		logger.debug("hello() is executed - $name {}");		
		LoginService ls=new LoginService();
		ls.pullCaptcha();		
		ls.generateSalt();
		List<String> captchaDetails=new ArrayList<String>();				 
		captchaDetails.add(ls.getCaptchaQuestion());		
		captchaDetails.add(ls.getCaptchaId());
		captchaDetails.add(ls.getSalt());
		return captchaDetails;
	}
	
	@RequestMapping(value={"/generate-otp-reset-password"}, method=RequestMethod.POST)
	public @ResponseBody List<String> generateOtp(@RequestParam String userId,@RequestParam String requestToken) throws Exception{
		logger.debug("hello() is executed - $name {}");	
		Boolean result = TokenController.isTokenValid(resetTokenKey, requestToken);
		String token = null;
		String status=null;
		LoginService ls=new LoginService();
		if(result) {			
			ls.setUserId(userId);
			status=ls.initOTP();		
			token = getSessionToken(resetTokenKey);
		} else {
			ls.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationDetails=new ArrayList<String>();				
		notificationDetails.add(status);
		notificationDetails.add(mapper.writeValueAsString(ls.getNotifyList()));		
		notificationDetails.add(ls.getOtpRefNumber());
		notificationDetails.add(token);
		return notificationDetails;
	}
	
	@RequestMapping(value={"/regenerate-otp-reset-password"}, method=RequestMethod.POST)
	public @ResponseBody List<String> reGenerateOtp(@RequestParam String userId,@RequestParam String requestToken) throws Exception{
		logger.debug("hello() is executed - $name {}");		
		Boolean result = TokenController.isTokenValid(resetTokenKey, requestToken);
		String token = null;
		String status=null;
		LoginService ls=new LoginService();
		if(result) {
			ls.setUserId(userId);
			status=ls.regenerateOTP();
			token = getSessionToken(resetTokenKey);
		}else{
			ls.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
			
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationDetails=new ArrayList<String>();				
		notificationDetails.add(status);
		notificationDetails.add(mapper.writeValueAsString(ls.getNotifyList()));		
		notificationDetails.add(ls.getOtpRefNumber());
		notificationDetails.add(token);
		return notificationDetails;
	}
	
	@RequestMapping(value={"/validate-otp-reset-password"}, method=RequestMethod.POST)
	public @ResponseBody List<String> validateOtp(@RequestParam String otpValue,@RequestParam String otpRefNumber,@RequestParam String requestToken) throws Exception{
		logger.debug("hello() is executed - $name {}");	
		Boolean result = TokenController.isTokenValid(resetTokenKey, requestToken);
		String token = null;
		String status=null;
		LoginService ls=new LoginService();
		if(result) {
			ls.setOtpValue(otpValue);
			ls.setOtpRefNumber(otpRefNumber);
			status=ls.validateOTP();
			token = getSessionToken(resetTokenKey);
		}else{
			ls.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
			
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationDetails=new ArrayList<String>();				
		notificationDetails.add(status);
		notificationDetails.add(mapper.writeValueAsString(ls.getNotifyList()));		
		notificationDetails.add(token);
		return notificationDetails;
	}
	
	@RequestMapping(value={"/submit-reset-password"}, method=RequestMethod.POST)
	public @ResponseBody List<String> submitResetPassword(@RequestParam String newPassword,@RequestParam String confirmNewPassword
			,@RequestParam String otpUser,@RequestParam String requestToken,@RequestParam String otpValue,@RequestParam String otpRefNumber) throws Exception{
		logger.debug("hello() is executed - $name {}");		
		Boolean result = TokenController.isTokenValid(resetTokenKey, requestToken);
		String token = null;
		String status=null;
		LoginService ls=new LoginService();
		if(result) {
			ls.setNewPassword(newPassword);
			ls.setConfirmNewPassword(confirmNewPassword);
			ls.setUserId(otpUser);
			ls.setOtpValue(otpValue);
			ls.setOtpRefNumber(otpRefNumber);
			status=ls.submitResetPassword();	
			token = getSessionToken(resetTokenKey);
		}else{
			ls.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		}
		
		ObjectMapper mapper = new ObjectMapper();
		List<String> notificationDetails=new ArrayList<String>();				
		notificationDetails.add(status);
		notificationDetails.add(mapper.writeValueAsString(ls.getNotifyList()));		
		notificationDetails.add(token);
		return notificationDetails;
	}
	
	@RequestMapping(value={"/logout"}, method=RequestMethod.GET)
	public ModelAndView logout(){
		logger.debug("hello() is executed - $name {}");	
		LogoutService logoutservice=new LogoutService();
		logoutservice.initLogout();
		String tokenGenerated = generateAndSaveToken(resetTokenKey);			
		ModelAndView model = new ModelAndView();		
		model.addObject("requestToken", tokenGenerated);
		model.setViewName("login/login");
		return model;
	}
	
	@RequestMapping(value={"","/","/login"}, method=RequestMethod.POST)
	public ModelAndView login(@RequestParam("userId")String userId, @RequestParam("password")String password,@RequestParam String clientCaptchaAnswer
			,@RequestParam String clientCaptchaId,@RequestParam String requestToken) throws Exception{
		logger.debug("Login is executed with username and password - "+userId+" - "+password);
		Boolean resultToken = TokenController.isTokenValid(resetTokenKey, requestToken);
		String token = null;
		LoginService loginservice = new LoginService();
		ModelAndView model = new ModelAndView();
		if(resultToken){
			loginservice.setUserId(userId);
			loginservice.setPassword(password);
			loginservice.setClientCaptchaAnswer(clientCaptchaAnswer);
			loginservice.setClientCaptchaId(clientCaptchaId);
			String result = loginservice.login();			
			model.addObject("notifyList", loginservice.getNotifyList());
			if(result.equals("success") == true)
				model.setViewName("redirect:home?proceed=");
			else if(result.equals("loginFail") == true){
				token = getSessionToken(resetTokenKey);
				model.addObject("requestToken", token);
				model.setViewName("login/login");
			}			
			else if(result.equals("changePassword") == true)
				/*model.setViewName("redirect:home?proceed=changePassword");*/
				model.setViewName("redirect:home?proceed=change-password");			
		}else{
			loginservice.getNotifyList().add(new Notification("Info!!", "Request you have sent is invalid. It is possible that it might be a duplicate request or you have opened the same page in a different tab.", Status.WARNING, Type.STICKY));
		} /*
		model.addObject("title", helloWorldService.getTitle(name));
		model.addObject("msg", helloWorldService.getDesc());*/ 
		return model;
	}	
}
