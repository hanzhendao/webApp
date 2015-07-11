package com.landao.main.user.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.landao.main.repository.dao.UserDao;
import com.landao.main.repository.entity.User;

public class ProvilegeRealm extends AuthorizingRealm{
	@Autowired
	private UserDao userDao;
	
	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken=(UsernamePasswordToken) token;
		String userName=upToken.getUsername();
		User user=userDao.getUserByUserName(userName);
		if(user==null){
			return null;
		}
		//其在调用getAuthenticationInfo方法获取到AuthenticationInfo信息后，会使用credentialsMatcher来验证凭据是否匹配，
		//如果不匹配将抛出IncorrectCredentialsException异常
		SimpleAuthenticationInfo saInfo=new SimpleAuthenticationInfo(user.getUserName(),user.getPassword(),getName());
		saInfo.setCredentialsSalt(ByteSource.Util.bytes(userName+user.getSalt()));
		return saInfo;
	}

}
