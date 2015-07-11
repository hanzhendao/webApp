package com.landao.framework.orm;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.landao.framework.orm.hibernate.HibernateDao;
import com.landao.framework.orm.mybatis.MybatisDao;

public class Dao<T, PK extends Serializable> extends HibernateDao<T, PK>{
	private MybatisDao mybatisDao;
	
	public MybatisDao getMybatisDao(){
		return this.mybatisDao;
	}
	
	@Autowired(required=false)
	public void setMybatisDao(MybatisDao mybatisDao) {
		this.mybatisDao = mybatisDao;
	}
}