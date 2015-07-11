package com.landao.framework.orm.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;

import com.landao.framework.orm.Page;
import com.landao.framework.utils.ReflectionUtils;

public class HibernateDao<T, PK extends Serializable> {
	HibernateTemplate hibernateTemplate;
	protected Class<T> entityClass;

	@Autowired(required = false)
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public HibernateDao() {
		this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
	}

	public Long save(T entity) {
		Assert.notNull(entity, "entity不能为空");
		return (Long) this.hibernateTemplate.save(entity);
	}

	public void update(T entity) {
		Assert.notNull(entity, "entity不能为空");
		this.hibernateTemplate.update(entity);
	}

	public void saveOrUpdate(T entity) {
		Assert.notNull(entity, "entity不能为空");
		this.hibernateTemplate.saveOrUpdate(entity);
	}

	public void delete(T entity) {
		Assert.notNull(entity, "entity不能为空");
		this.hibernateTemplate.delete(entity);
	}

	public void delete(PK id) {
		Assert.notNull(id, "id不能为空");
		delete(get(id));
	}

	public T get(PK id) {
		Assert.notNull(id, "id不能为空");
		return this.hibernateTemplate.load(this.entityClass, id);
	}

	public List<T> getAll() {
		return this.hibernateTemplate.loadAll(this.entityClass);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(String orderByProperty, boolean isAsc) {
		Assert.hasText(orderByProperty, "propertyName不能为空");
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(this.entityClass);
		detachedCriteria.addOrder(Order.asc(orderByProperty));
		return this.hibernateTemplate.findByCriteria(detachedCriteria);
	}

	@SuppressWarnings("unchecked")
	public List<T> findBy(String propertyName, Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(this.entityClass);
		detachedCriteria.add(Restrictions.eq(propertyName, value));
		return this.hibernateTemplate.findByCriteria(detachedCriteria);
	}

	@SuppressWarnings("unchecked")
	public T findUniqueBy(final String propertyName, final Object value) {
		Assert.hasText(propertyName, "propertyName不能为空");
		return (T) this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public T doInHibernate(Session session)
							throws HibernateException, SQLException {
						Criteria c = session
								.createCriteria(HibernateDao.this.entityClass);
						c.add(Restrictions.eq(propertyName, value));
						return (T) c.uniqueResult();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public T findUnique(final String hql, final Object[] values) {
		return (T) this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public T doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						if (values != null) {
							for (int i = 0; i < values.length; i++) {
								q.setParameter(i, values[i]);
							}
						}
						return (T) q.uniqueResult();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public T findUnique(final String hql, final Map<String, ?> values) {
		return (T) this.hibernateTemplate
				.execute(new HibernateCallback<Object>() {
					public T doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						q.setProperties(values);
						return (T) q.uniqueResult();
					}
				});
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String hql, Object[] values) {
		return this.hibernateTemplate.find(hql, values);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(String hql) {
		return this.hibernateTemplate.find(hql);
	}

	@SuppressWarnings("unchecked")
	public List<T> find(final String hql, final Map<String, ?> values) {
		return (List<T>) this.hibernateTemplate
				.execute(new HibernateCallback<Object>() {
					public T doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query q = session.createQuery(hql);
						q.setProperties(values);
						return (T) q.list();
					}
				});
	}

	public int batchExecute(String hql, Object[] values) {
		return this.hibernateTemplate.bulkUpdate(hql, values);
	}

	public int batchExecute(final String hql, final Map<String, ?> values) {
		return ((Integer) this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public Integer doInHibernate(Session session)
							throws HibernateException {
						Query queryObject = session.createQuery(hql);
						queryObject.setProperties(values);
						return Integer.valueOf(queryObject.executeUpdate());
					}
				})).intValue();
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql,
			final Map<String, ?> values) {
		Assert.notNull(page, "page不能为空");
		return (Page<T>) this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public Page<T> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query count_q = session.createQuery(HibernateDao.this
								.prepareCountHql(hql));
						count_q.setProperties(values);
						Object c = count_q.uniqueResult();
						page.setTotalCount(c != null ? Long.valueOf(
								c.toString()).longValue() : 0L);
						Query q = session.createQuery(hql);

						q.setFirstResult(page.getFirst() - 1);
						q.setMaxResults(page.getPageSize());
						q.setProperties(values);
						page.setResult(q.list());
						return page;
					}
				});
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql,
			final Object[] values) {
		Assert.notNull(page, "page不能为空");
		return (Page<T>) this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public Page<T> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query count_q = session.createQuery(HibernateDao.this
								.prepareCountHql(hql));
						if (values != null) {
							for (int i = 0; i < values.length; i++) {
								count_q.setParameter(i, values[i]);
							}
						}
						Object c = count_q.uniqueResult();
						page.setTotalCount(c != null ? Long.valueOf(
								c.toString()).longValue() : 0L);
						Query q = session.createQuery(hql);

						q.setFirstResult(page.getFirst() - 1);
						q.setMaxResults(page.getPageSize());
						if (values != null) {
							for (int i = 0; i < values.length; i++) {
								q.setParameter(i, values[i]);
							}
						}
						page.setResult(q.list());
						return page;
					}
				});
	}

	@SuppressWarnings("unchecked")
	public Page<T> findPage(final Page<T> page, final String hql, final T entity) {
		return (Page<T>) this.hibernateTemplate
				.executeWithNativeSession(new HibernateCallback<Object>() {
					public Page<T> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query count_q = session.createQuery(HibernateDao.this
								.prepareCountHql(hql));
						count_q.setProperties(entity);
						Object c = count_q.uniqueResult();
						page.setTotalCount(c != null ? Long.valueOf(
								c.toString()).longValue() : 0L);
						Query q = session.createQuery(hql);

						q.setFirstResult(page.getFirst() - 1);
						q.setMaxResults(page.getPageSize());
						q.setProperties(entity);
						page.setResult(q.list());
						return page;
					}
				});
	}

	private String prepareCountHql(String orgHql) {
		String fromHql = orgHql;

		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(*) " + fromHql;
		return countHql;
	}

	protected Query setPageParameterToQuery(Query q, Page<T> page) {
		Assert.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");

		q.setFirstResult(page.getFirst() - 1);
		q.setMaxResults(page.getPageSize());
		return q;
	}

	public String getIdName() {
		ClassMetadata meta = this.hibernateTemplate.getSessionFactory()
				.getClassMetadata(this.entityClass);
		return meta.getIdentifierPropertyName();
	}

	public boolean isPropertyUnique(String propertyName, Object newValue,
			Object oldValue) {
		if ((newValue == null) || (newValue.equals(oldValue))) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return object == null;
	}
}
