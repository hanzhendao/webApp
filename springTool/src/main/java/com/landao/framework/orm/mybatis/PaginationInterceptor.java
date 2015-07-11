package com.landao.framework.orm.mybatis;

import java.util.Map;
import java.util.Properties;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "prepare", args = { java.sql.Connection.class }) })
public class PaginationInterceptor implements Interceptor {
	protected static Logger log = LoggerFactory
			.getLogger(PaginationInterceptor.class);

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		MetaObject metaStatementHandler = MetaObject
				.forObject(statementHandler);

		RowBounds rowBounds = (RowBounds) metaStatementHandler
				.getValue("delegate.rowBounds");
		if ((rowBounds == null) || (rowBounds == RowBounds.DEFAULT)) {
			return invocation.proceed();
		}

		DefaultParameterHandler defaultParameterHandler = (DefaultParameterHandler) metaStatementHandler
				.getValue("delegate.parameterHandler");
		Map parameterMap = (Map) defaultParameterHandler.getParameterObject();
		Object sidx = parameterMap.get("_sidx");
		Object sord = parameterMap.get("_sord");

		String originalSql = (String) metaStatementHandler
				.getValue("delegate.boundSql.sql");

		if ((sidx != null) && (sord != null)) {
			originalSql = originalSql + " order by " + sidx + " " + sord;
		}

		Configuration configuration = (Configuration) metaStatementHandler
				.getValue("delegate.configuration");

		Dialect.Type databaseType = null;
		try {
			databaseType = Dialect.Type.valueOf(configuration.getVariables()
					.getProperty("dialect").toUpperCase());
		} catch (Exception e) {
			log.error("dialect:", e);
		}
		if (databaseType == null) {
			throw new RuntimeException(
					"the value of the dialect property in configuration.xml is not defined : "
							+ configuration.getVariables().getProperty(
									"dialect"));
		}
		Dialect dialect = null;
		switch (databaseType) {
		case ORACLE:
			dialect = new OracleDialect();
			break;
		case MYSQL:
			dialect = new MySQLDialect();
		}

		metaStatementHandler.setValue("delegate.boundSql.sql", dialect
				.getLimitString(originalSql, rowBounds.getOffset(),
						rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset",
				Integer.valueOf(0));
		metaStatementHandler.setValue("delegate.rowBounds.limit",
				Integer.valueOf(2147483647));
		if (log.isDebugEnabled()) {
			BoundSql boundSql = statementHandler.getBoundSql();
			log.debug("生成分页SQL : " + boundSql.getSql());
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties arg0) {
	}
}
