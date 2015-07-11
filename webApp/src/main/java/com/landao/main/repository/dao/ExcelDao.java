package com.landao.main.repository.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.landao.framework.orm.Dao;
import com.landao.main.repository.entity.User;

@Repository
public class ExcelDao extends Dao<User, Long>{

	public int getTotalSize(String countSql, Map<String, Object> condition) {
		return this.getMybatisDao().selectOne(countSql, condition);
	}

	public List<?> getPageDatas(int pageSize, int pageNum, String querySql,
			Map<String, Object> condition) {
		return this.getMybatisDao().getPageDatas(pageSize, pageNum, querySql, condition);
	}

}
