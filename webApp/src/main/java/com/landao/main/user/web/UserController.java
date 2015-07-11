package com.landao.main.user.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.landao.main.user.service.UserService;
import com.landao.main.user.viewmodel.UserBean;

@RequestMapping("/user")
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	private static final Logger logger= LoggerFactory.getLogger(UserService.class);
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(String userName, String password, String isRemember) {
		return userService.userLogin(userName, password, isRemember);
	}
	
	@RequestMapping(value = "/checkRegistUserNameForRepeat", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkRegistUserNameForRepeat(UserBean userBean) {
		return userService.checkRegistUserNameForRepeat(userBean);
	}
	
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> regist(UserBean userBean) {
		return userService.regist(userBean);
	}
	
	@RequestMapping(value = "/getUserListInPage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getUserListInPage(int pageNumber,int pageSize) {
		return userService.getUserListInPage(pageNumber,pageSize);
	}
}
