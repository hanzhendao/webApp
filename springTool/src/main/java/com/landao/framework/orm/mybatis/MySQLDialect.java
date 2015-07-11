package com.landao.framework.orm.mybatis;

public class MySQLDialect extends Dialect {
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect
				.append("select * from ( select row_.* from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ ) row__ limit  ").append(offset)
		.append(" , ").append(limit);
		return pagingSelect.toString();
	}
}
