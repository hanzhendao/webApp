package com.landao.framework.orm.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.landao.framework.orm.Page;
import com.landao.framework.utils.ObjectUtils;

public class MybatisDao {
	SqlSessionTemplate sqlSessionTemplate;

	public SqlSessionTemplate getSqlSessionTemplate() {
		return this.sqlSessionTemplate;
	}

	@Autowired(required = false)
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public <T> T selectOne(String statement) {
		return this.sqlSessionTemplate.selectOne(statement);
	}

	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionTemplate.selectOne(statement, parameter);
	}

	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionTemplate.selectMap(statement, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String statement, Object parameter,
			String mapKey) {
		return this.sqlSessionTemplate.selectMap(statement, parameter, mapKey);
	}

	public <K, V> Map<K, V> selectMap(String statement, Object parameter,
			String mapKey, RowBounds rowBounds) {
		return this.sqlSessionTemplate.selectMap(statement, parameter, mapKey,
				rowBounds);
	}

	public <E> List<E> selectList(String statement) {
		return this.sqlSessionTemplate.selectList(statement);
	}

	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionTemplate.selectList(statement, parameter);
	}

	public int insert(String statement) {
		return this.sqlSessionTemplate.insert(statement);
	}

	public int insert(String statement, Object parameter) {
		return this.sqlSessionTemplate.insert(statement, parameter);
	}

	public int update(String statement) {
		return this.sqlSessionTemplate.update(statement);
	}

	public int update(String statement, Object parameter) {
		return this.sqlSessionTemplate.update(statement, parameter);
	}

	public int delete(String statement) {
		return this.sqlSessionTemplate.delete(statement);
	}

	public int delete(String statement, Object parameter) {
		return this.sqlSessionTemplate.delete(statement, parameter);
	}

	@SuppressWarnings("unchecked")
	public <T> Page<T> findPage(Page<T> page, Map<String, Object> parameter,
			String countStatement, String pagingStatement) {
		Object o = this.sqlSessionTemplate.selectOne(countStatement, parameter);
		if ((o != null) && (((Integer) o).intValue() != 0)) {
			Integer count = (Integer) o;
			page.setTotalCount(count.longValue());
			parameter.put("_sidx", page.getOrderBy());
			parameter.put("_sord", page.getOrder());
			page.setResult((List<T>) this.sqlSessionTemplate.selectList(
					pagingStatement, parameter, new RowBounds(
							page.getFirst() - 1, page.getPageSize())));
		}
		return page;
	}

	@SuppressWarnings("unchecked")
	public <T> Page<T> findPage(Page<T> page, Object parameter,
			String countStatement, String pagingStatement) {
		Object o = this.sqlSessionTemplate.selectOne(countStatement, parameter);
		if ((o != null) && (((Integer) o).intValue() != 0)) {
			Integer count = (Integer) o;
			page.setTotalCount(count.longValue());
			try {
				Map pars = ObjectUtils.objectToMap(parameter);
				pars.put("_sidx", page.getOrderBy());
				pars.put("_sord", page.getOrder());
				page.setResult((List<T>) this.sqlSessionTemplate.selectList(
						pagingStatement, parameter,
						new RowBounds(page.getFirst() - 1, page.getPageSize())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return page;
	}
	
	public <T> List<T> getPageDatas(int pageSize,int pageNo,
			String querySql,Map<String, Object> parameter) {
		List<T> resultList=new ArrayList<T>();
		int offset=(pageNo-1)*pageSize;
		try {
			resultList = this.sqlSessionTemplate.selectList(
					querySql, parameter,new RowBounds(offset, pageSize));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
