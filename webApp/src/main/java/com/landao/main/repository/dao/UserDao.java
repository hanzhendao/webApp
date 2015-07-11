package com.landao.main.repository.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.landao.framework.orm.Dao;
import com.landao.framework.orm.Page;
import com.landao.main.repository.entity.User;
import com.landao.main.user.viewmodel.UserBean;

@Repository
public class UserDao extends Dao<User, Long>{

	public UserBean getLoginUser(Map<String, Object> condition) {
		return this.getMybatisDao().selectOne("user.getLoginUser", condition);
	}

	public User getUserByUserName(String userName) {
		return this.getMybatisDao().selectOne("user.getUserByUserName", userName);
	}

	public Page<UserBean> getUserListInPage(Page<UserBean> pages, Map<String, Object> condition) {
		return this.getMybatisDao().findPage(pages, condition, "user.getUserListInPageCount", "user.getUserListInPage");
	}
	
}
