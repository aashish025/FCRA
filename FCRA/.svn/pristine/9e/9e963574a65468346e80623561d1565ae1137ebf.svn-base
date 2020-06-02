package web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import service.auth.HomeService;

@Controller
public class Home {
	private final Logger logger = LoggerFactory.getLogger(Home.class);
	//private HomeService homeservice;
	/*@Autowired
	public Login(LoginService loginService){
		this.loginservice=loginService;
	}*/
	@RequestMapping(value={"/home"})
	public ModelAndView login(@RequestParam("proceed")String proceed){
		logger.debug("hello() is executed - $name {}");		 
		ModelAndView model = new ModelAndView();
		HomeService homeservice = new HomeService();
		homeservice.setProceed(proceed);
		homeservice.home();
		model.addObject("proceed", homeservice.getProceed());
		model.addObject("menuString", homeservice.getMenuString());
		model.addObject("menuList", homeservice.getMenuList());
		model.addObject("userId", homeservice.getUserId());
		model.addObject("userName", homeservice.getUserName());
		model.addObject("userDesignation", homeservice.getUserDesignation());
		model.addObject("officeAddress", homeservice.getOfficeAddress());
		model.setViewName("login/home"); /*
		model.addObject("title", helloWorldService.getTitle(name));
		model.addObject("msg", helloWorldService.getDesc());*/ 
		return model;
	}
}
