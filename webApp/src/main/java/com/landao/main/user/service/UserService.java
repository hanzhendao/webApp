package com.landao.main.user.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.landao.framework.orm.Page;
import com.landao.main.repository.dao.UserDao;
import com.landao.main.repository.entity.User;
import com.landao.main.user.viewmodel.UserBean;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	public ModelAndView userLogin(String userName, String password, String isRemember) {
		ModelAndView modelView=new ModelAndView();
		if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
			modelView.setViewName("login/login");
			modelView.addObject("state", false);
			modelView.addObject("msg", "用户名或密码不能为空");
			return modelView;
		}
		//验证码校验
		UsernamePasswordToken token=new UsernamePasswordToken();
		token.setUsername(userName);
		token.setPassword(password.toCharArray());
		if(StringUtils.isNotBlank(isRemember) && isRemember.equals("1")){	//记住我
			token.setRememberMe(true);
		}else {
			token.setRememberMe(false);
		}
		Subject subject=SecurityUtils.getSubject();
		try {
			subject.login(token);
		} catch (UnknownAccountException ex) {
			modelView.addObject("msg", "账户不存在");
		}catch (IncorrectCredentialsException ex) {
			modelView.addObject("msg", "用户名密码不匹配");
		}catch (ExcessiveAttemptsException e) {
			modelView.addObject("msg", "该用户被锁定,1个小时后再试");
		}catch (AuthenticationException e) {
			modelView.addObject("msg", "认证未通过");
		}catch (Exception e) {
			modelView.addObject("msg", e.getMessage());
		}
		if(subject.isAuthenticated()){
			logger.info("用户--"+subject.getPrincipal()+"--登录系统");
			modelView.setViewName("redirect:../index.jsp");
		}else {
			modelView.setViewName("login/login");
			modelView.addObject("state", false);
			modelView.addObject("userName",userName);
			modelView.addObject("password",password);
			modelView.addObject("isRemember",isRemember);
		}
		return modelView;
	}
	
	public Map<String, Object> checkRegistUserNameForRepeat(UserBean userBean) {
		Map<String, Object> operateMap=new HashMap<String, Object>();
		//检测用户名是否已存在
		User testUser=userDao.findUniqueBy("userName", userBean.getUserName());
		if(testUser!=null){
			operateMap.put("valid", false);
			return operateMap;
		}
		operateMap.put("valid", true);
		return operateMap;
	}
	
	@Transactional
	public Map<String, Object> regist(UserBean userBean) {
		Map<String, Object> operateMap=new HashMap<String, Object>();
		//检测用户名是否已存在
		User testUser=userDao.findUniqueBy("userName", userBean.getUserName());
		if(testUser!=null){
			operateMap.put("state", false);
			return operateMap;
		}
		SecureRandomNumberGenerator randomNumberGenerator=new SecureRandomNumberGenerator();
		String salt = randomNumberGenerator.nextBytes().toHex();
		String password_cipherText= new Md5Hash(userBean.getPassword(),userBean.getUserName()+salt,2).toHex();
		testUser=new User();
		testUser.setUserName(userBean.getUserName());
		testUser.setPassword(password_cipherText);
		testUser.setSalt(salt);
		userDao.save(testUser);
		operateMap.put("state", true);
		System.out.println(1/0);
		return operateMap;
	}

	public Map<String, Object> getUserListInPage(int pageNumber, int pageSize) {
		Map<String, Object> resultMap=new HashMap<String, Object>();
		Page<UserBean> pages=new Page<UserBean>();
		pages.setPageNo(pageNumber);
		pages.setPageSize(pageSize);
		Map<String, Object> condition=new HashMap<String, Object>();
		pages=userDao.getUserListInPage(pages,condition);
		resultMap.put("total", pages.getTotalCount());
		resultMap.put("rows", pages.getResult());
		return resultMap;
	}

	
}
