package com.landao.main.repository.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.landao.framework.orm.Dao;
import com.landao.main.member.viewmodel.MemberBean;
import com.landao.main.repository.entity.User;

@Repository
public class MemberDao extends Dao<User,Long>{

	public int getTotalSize(String totalSql) {
		return this.getMybatisDao().selectOne(totalSql);
	}

	public List<MemberBean> getDataList(String string) {
		return this.getMybatisDao().selectList(string);
	}

	public List getPageData(int pageSize, int pageNo) {
		Map<String, Object> condition=new HashMap<String, Object>();
		return this.getMybatisDao().getPageDatas(pageSize, pageNo, "member.getMembersList", condition);
	}

}
