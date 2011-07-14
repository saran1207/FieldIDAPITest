package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.security.SecurityLevel;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.impl.SessionImpl;
import org.hibernate.stat.Statistics;

import com.n4systems.ejb.PerformSearchRequiringTransaction;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Retirable;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.model.parents.legacy.LegacyBeanTenantWithCreateModifyDate;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SelectClause;
import com.n4systems.util.persistence.search.BaseSearchDefiner;

public class PersistenceManagerImpl implements PersistenceManager {
	private Logger logger = Logger.getLogger(PersistenceManagerImpl.class);
	private static final String defaultTableAlias = "e";

    @PersistenceContext
	private EntityManager em;

	public PersistenceManagerImpl() {
	}
	
	public PersistenceManagerImpl(EntityManager em) {
		this.em = em;
	}

	private Session getHibernateSession() {
		return (Session) em.getDelegate();
	}
	
	public EntityManager getEntityManager() {
		return em;
	}

	
	public <T extends BaseEntity> T find(Class<T> entityClass, Long entityId) {
		return em.find(entityClass, entityId);
	}

    @Override
    public <T extends NetworkEntity<T>> T findAndEnhance(Class<T> entityClass, Long entityId, SecurityFilter securityFilter, String... postFetchPaths) {
        T t = em.find(entityClass, entityId);
        SecurityLevel securityLevel = t.getSecurityLevel(securityFilter.getOwner());
        return postFetchFields(t.enhance(securityLevel), postFetchPaths);
    }


    public <T extends LegacyBaseEntity> T findLegacy(Class<T> entityClass, Long entityId, SecurityFilter filter) {
		QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, filter);
		queryBuilder.addSimpleWhere("uniqueID", entityId);
		
		return find(queryBuilder);
	}

	
	public <T extends BaseEntity> T find(Class<T> entityClass, Long entityId, String... postFetchFields) {
		return postFetchFields(find(entityClass, entityId), postFetchFields);
	}

	
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Tenant tenant, String... postFetchFields) {
		return postFetchFields(find(entityClass, entityId, tenant), postFetchFields);
	}

	
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Tenant tenant) {
		return find(entityClass, entityId, tenant.getId());
	}

	@SuppressWarnings("unchecked")
	
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Long tenantId) {
		T entity;
		try {
			Query query = em.createQuery(generateFromClause(defaultTableAlias, entityClass) + "where " + defaultTableAlias + ".id = :entityId and " + defaultTableAlias + ".tenant.id = :tenantId");

			query.setParameter("entityId", entityId);
			query.setParameter("tenantId", tenantId);

			entity = (T) query.getSingleResult();
		} catch (NoResultException ne) {
			entity = null;
		}
		return entity;
	}

	
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, SecurityFilter filter, String... postFetchFields) {
		QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, filter);
		queryBuilder.setSimpleSelect();
		queryBuilder.addSimpleWhere("id", entityId);
		queryBuilder.applyFilter(filter);
		queryBuilder.addPostFetchPaths(postFetchFields);
		try {
			return find(queryBuilder);
		} catch (InvalidQueryException iqe) {
			logger.error("invalid page", iqe);
			return null;
		}
	}

	
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Long tenantId, String... postFetchFields) {
		return postFetchFields(find(entityClass, entityId, tenantId), postFetchFields);
	}

	@SuppressWarnings("unchecked")
	
	public <T extends BaseEntity & NamedEntity> T findByName(Class<T> entityClass, String entityName) {
		T entity;

		try {
			Query query = em.createQuery(generateFromClause(defaultTableAlias, entityClass) + "where " + defaultTableAlias + ".name = :entityName");
			query.setParameter("entityName", entityName);

			entity = (T) query.getSingleResult();
		} catch (NoResultException ne) {
			entity = null;
		}

		return entity;
	}

	@SuppressWarnings("unchecked")
	
	public <T extends EntityWithTenant & NamedEntity> T findByName(Class<T> entityClass, Long tenantId, String entityName) {
		T entity;

		try {
			Query query = em.createQuery(generateFromClause(defaultTableAlias, entityClass) + "where " + defaultTableAlias + ".tenant.id = :tenantId and " + defaultTableAlias + ".name = :entityName");
			query.setParameter("entityName", entityName);
			query.setParameter("tenantId", tenantId);

			entity = (T) query.getSingleResult();
		} catch (NoResultException ne) {
			entity = null;
		}

		return entity;
	}

	
	public <T extends EntityWithTenant & NamedEntity> String findName(Class<T> entityClass, Long id, SecurityFilter filter) {
		QueryBuilder<String> builder = new QueryBuilder<String>(entityClass, filter);
		
		return find(builder.setSimpleSelect("name").addSimpleWhere("id", id));
	}

	
	public <T> int countAllPages(Class<T> entityClass, int pageSize, SecurityFilter filter) {
		Long longPageSize = new Long(pageSize);
		Long entityCount;

		int pages = -1;
		try {
			
			entityCount = findCount(new QueryBuilder<T>(entityClass, filter));
			pages = (int)Math.ceil(entityCount.doubleValue() / longPageSize.doubleValue());
			
		} catch (InvalidQueryException e) {
			logger.error("Failed counting [" + entityClass.getSimpleName() + "]", e);
		}

		return pages;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entityClass, String jpqlQuery, Map<String, Object> parameters) {
		Query query = getEntityManager().createQuery(jpqlQuery);
		
		for (String key: parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		
		return (List<T>) query.getResultList();
	}

	
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass) {
		return (List<T>) em.createQuery(generateFromClause(defaultTableAlias, entityClass)).getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, String orderBy) {
		return (List<T>) em.createQuery(generateFromClause(defaultTableAlias, entityClass) + " ORDER BY " + orderBy + " ASC ").getResultList();
	}

	
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Tenant tenant) {
		return findAll(entityClass, tenant.getId());
	}

	@SuppressWarnings("unchecked")
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Set<Long> ids, Tenant tenant, String... postFetchFields) {
		Query query = em.createQuery(generateFromClause(defaultTableAlias, entityClass) + "where " + defaultTableAlias + ".tenant.id = :tenantId and " + defaultTableAlias + ".id in (:idlist)");

		query.setParameter("tenantId", tenant.getId());
		query.setParameter("idlist", ids);

		List<T> results = (List<T>) query.getResultList();
		if (postFetchFields.length > 0) {
			postFetchFields(results, postFetchFields);
		}
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Set<Long> ids, Long tenantId, String... postFetchFields) {
		Query query = em.createQuery(generateFromClause(defaultTableAlias, entityClass) + "where " + defaultTableAlias + ".tenant.id = :tenantId and " + defaultTableAlias + ".id in (:idlist)");

		query.setParameter("tenantId", tenantId);
		query.setParameter("idlist", ids);

		List<T> results = (List<T>) query.getResultList();
		if (postFetchFields.length > 0) {
			postFetchFields(results, postFetchFields);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public <T extends EntityWithTenant> List<T> findAllByDate(Class<T> entityClass, Long tenantId, Date startDate, Date endDate, Long beginningId, Integer limit) {
		Query query = em.createQuery(generateFromClause(defaultTableAlias, entityClass) + "where " + defaultTableAlias + ".tenant.id=:tenantId and "
		        + "modified >= :startDate AND modified <= :endDate AND " + defaultTableAlias + ".id > :beginningId ORDER BY " + defaultTableAlias + ".id");

		query.setParameter("tenantId", tenantId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("beginningId", beginningId);
		if (limit != null) {
			query.setMaxResults(limit);
		}

		return (List<T>) query.getResultList();
	}

	
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Long tenantId) {
		return findAll(entityClass, tenantId, null);
	}

	
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Long tenantId, Map<String, Boolean> orderBy) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("tenant.id", tenantId);
		return findAll(entityClass, whereClause, orderBy);
	}

	
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, Map<String, Object> whereClauses) {
		return findAll(entityClass, whereClauses, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, Map<String, Object> whereClauses, Map<String, Boolean> orderBy) {
		String paramPrefix = "param";
		int position;
		List<Object> paramValues = new ArrayList<Object>();
		String jpql = generateFromClause(defaultTableAlias, entityClass);

		if (whereClauses != null && whereClauses.size() > 0) {
			jpql += "where ";

			position = 0;
			for (Map.Entry<String, Object> whereEntry : whereClauses.entrySet()) {
				if (position > 0) {
					jpql += " AND ";
				}
				jpql += defaultTableAlias + "." + whereEntry.getKey() + " = :" + paramPrefix + position + " ";
				paramValues.add(position, whereEntry.getValue());
				position++;
			}
		}

		if (orderBy != null && orderBy.size() > 0) {
			jpql += "ORDER BY ";

			position = 0;
			for (Map.Entry<String, Boolean> orderByEntry : orderBy.entrySet()) {
				if (position > 0) {
					jpql += " , ";
				}
				jpql += defaultTableAlias + "." + orderByEntry.getKey();
				if (orderByEntry.getValue()) {
					jpql += " ASC ";
				} else {
					jpql += " DESC ";
				}
				position++;
			}
		}

		Query query = em.createQuery(jpql);

		for (position = 0; position < paramValues.size(); position++) {
			query.setParameter(paramPrefix + position, paramValues.get(position));
		}

		return (List<T>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public <T> T find(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		T result;

		try {
			result = (T) queryBuilder.createQuery(em).getSingleResult();
		} catch (NoResultException ne) {
			result = null;
		}

		return postFetchFields(result, queryBuilder.getPostFetchPaths());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		Query createQuery = queryBuilder.createQuery(em);
		List<T> resultList = (List<T>) createQuery.getResultList();
		return postFetchFields(resultList, queryBuilder.getPostFetchPaths());
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
		List<T> resultList = (List<T>) queryBuilder.createQuery(em).setFirstResult(pageSize * page).setMaxResults(pageSize).getResultList();
		return postFetchFields(resultList, queryBuilder.getPostFetchPaths());
	}

	
	public <T> Pager<T> findAllPaged(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
		SelectClause backedUpSelectArgument = queryBuilder.getSelectArgument();
		Pager<T> results = new Page<T>(queryBuilder.createQuery(em), queryBuilder.setCountSelect().createQuery(em), page, pageSize);
		queryBuilder.setSelectArgument(backedUpSelectArgument);
		postFetchFields(results.getList(), queryBuilder.getPostFetchPaths());
		return results;
	}

	
	public Long findCount(QueryBuilder<?> queryBuilder) throws InvalidQueryException {
		return (Long) queryBuilder.setCountSelect().createQuery(em).getSingleResult();
	}

	public <T> void saveAny(T entity) {
		em.persist(entity);
	}
	
	public <T extends BaseEntity> Long save(T entity) {
		em.persist(entity);
		return entity.getId();
	}

	
	public <T> T updateAny(T entity) {
		return em.merge(entity);
	}
	
	public <T extends BaseEntity> T update(T entity) {
		return em.merge(entity);
	}

	
	public <T extends AbstractEntity> Long save(T entity, User user) {
		return save(updateCreatedAndModifiedBy(entity, user));
	}

	public <T extends AbstractEntity> T update(T entity, User user) {
		return update(updateModifiedBy(entity, user));
	}

	public <T extends AbstractEntity> Long save(T entity, Long userId) {
		return save(updateCreatedAndModifiedBy(entity, userId));
	}

	public <T extends AbstractEntity> T update(T entity, Long userId) {
		return update(updateModifiedBy(entity, userId));
	}
	
	public <T extends AbstractEntity> List<T> updateAll(List<T> entities, Long userId) {
		List<T> updatedEntities = new ArrayList<T>();
		
		for (T entity : entities) {
			updatedEntities.add(update(entity, userId));
		}
		return updatedEntities;
	}

	public <T extends BaseEntity> void delete(T entity) {
		em.remove(update(entity));
	}
	
	public <T> void deleteAny(T entity) {
		em.remove(updateAny(entity));
	}
	
	public <T extends BaseEntity> void deleteSafe(T entity) throws EntityStillReferencedException {
		try {
			delete(entity);
			/*
			 * transactions actually commit late. Without the following flush, you wouldn't see the runtime exception until it hits the servlet layer
			 */
			em.flush();
		} catch (RuntimeException e) {
			/*
			 * we need to know if this runtime exception is actually caused by a constraint violation. if it is, then we will wrap it in an EntityStillReferencedException which is NOT a subclass of
			 * RuntimeException (forcing you to handle it).
			 */
			if (causeContains(e, ConstraintViolationException.class)) {
				throw new EntityStillReferencedException(entity.getClass(), entity.getId(), e);
			} else {
				// propagate any other Exceptions
				throw e;
			}
		}
	}
	
	private <T extends AbstractEntity> T updateCreatedAndModifiedBy(T entity, Long userId) {
		return updateCreatedAndModifiedBy(entity, find(User.class, userId));
	}

	private <T extends AbstractEntity> T updateCreatedAndModifiedBy(T entity, User user) {
		entity.setCreatedBy(user);
		return updateModifiedBy(entity, user);
	}
	
	private <T extends AbstractEntity> T updateModifiedBy(T entity, Long userId) {
		return updateModifiedBy(entity, find(User.class, userId));
	}

	private <T extends AbstractEntity> T updateModifiedBy(T entity, User user) {
		entity.setModifiedBy(user);
		return entity;
	}
	
	private String generateFromClause(String tableAlias, Class<?> tableClass) {
		return generateFromClause(tableAlias, tableClass, null);
	}

	
	private String generateFromClause(String tableAlias, Class<?> tableClass, String[] preFetchFields) {
		String clause = "from " + tableClass.getName() + " " + tableAlias + " ";

		if (preFetchFields != null && preFetchFields.length > 0) {
			clause = "select distinct " + tableAlias + " " + clause + generatePreFetchQuery(tableAlias, preFetchFields);
		}

		return clause;
	}

	
	private String generatePreFetchQuery(String tableAlias, String[] preFetchFields) {
		String query = "";

		for (String field : preFetchFields) {
			query += "left join fetch " + tableAlias + "." + field + " ";
		}

		return query;
	}

	
	public <E extends Collection<T>, T> E postFetchFields(E entities, String... postFetchFields) {
		return PostFetcher.postFetchFields(entities, postFetchFields);
	}

	
	public <E extends Collection<T>, T> E postFetchFields(E entities, List<String> postFetchFields) {
		return PostFetcher.postFetchFields(entities, postFetchFields);
	}

	
	public <T> T postFetchFields(T entity, String... postFetchFields) {
		return PostFetcher.postFetchFields(entity, postFetchFields);
	}

	
	public <T> T postFetchFields(T entity, List<String> postFetchFields) {
		return PostFetcher.postFetchFields(entity, postFetchFields);
	}

	
	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, SecurityFilter filter) {
		return findAllLP(entityClass, filter, "displayName");
	}

	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, Long tenantId) {
		// Display Name is the default value for the nameField.

		return findAllLP(entityClass, tenantId, "displayName");
	}

	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, Long tenantId, String nameField) {
		return findAllLP(entityClass, new TenantOnlySecurityFilter(tenantId), nameField);
	}

	@SuppressWarnings("unchecked")
	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, SecurityFilter filter, String nameField) {
		String jpql = "SELECT new com.n4systems.util.ListingPair(id, " + nameField + " ) " + generateFromClause(defaultTableAlias, entityClass);
		jpql += " WHERE " + filter.produceWhereClause(entityClass, defaultTableAlias);

		if (Arrays.asList(entityClass.getInterfaces()).contains(Archivable.class)) {
			jpql += " AND state = :activeState ";
		} else if (Arrays.asList(entityClass.getInterfaces()).contains(Retirable.class)) {
			jpql += " AND retired = false ";
		}

		jpql += " ORDER BY " + nameField;

		Query query = em.createQuery(jpql);

		filter.applyParameters(query, entityClass);
		if (Arrays.asList(entityClass.getInterfaces()).contains(Archivable.class)) {
			query.setParameter("activeState", EntityState.ACTIVE);
		}
		return (List<ListingPair>) query.getResultList();

	}
	
	public List<ListingPair> findAllLP(QueryBuilder<ListingPair> query, String nameField) {
		query.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", nameField));
		query.addOrder(nameField);
		return findAll(query);
	}

	public <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailable(Class<T> entityClass, String name, Long id, Long tenantId) {
		return uniqueNameAvailable(entityClass, name, id, tenantId, null, false);

	}

	public <T extends LegacyBeanTenantWithCreateModifyDate> boolean uniqueAssetStatusNameAvailable(Class<T> entityClass, String name, Long id, Long tenantId){
		String jpql = "SELECT id " + generateFromClause(defaultTableAlias, entityClass) + " WHERE tenant.id = :tenantId AND LOWER(name) = :name ";
		
		if (id != null) {
			jpql += " AND id != :id ";
		}

		Query query = em.createQuery(jpql);
		query.setParameter("tenantId", tenantId);
		query.setParameter("name", name.toLowerCase().trim());

		
		if (id != null) {
			query.setParameter("id", id);
		}

		List<Long> matchingNames = query.getResultList();
		return (matchingNames.size() == 0);
	}
	
	public <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailableWithCustomer(Class<T> entityClass, String name, Long id, Long tenantId, Long customerId) {
		return uniqueNameAvailable(entityClass, name, id, tenantId, customerId, true);
	}

	@SuppressWarnings("unchecked")
	private <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailable(Class<T> entityClass, String name, Long id, Long tenantId, Long customerId, boolean useCustomer) {

		String jpql = "SELECT id " + generateFromClause(defaultTableAlias, entityClass) + " WHERE tenant.id = :tenantId AND LOWER(name) = :name ";
		if (useCustomer) {
			jpql += " AND owner.customerOrg.id ";
			if (customerId != null) {
				jpql += "= :customerId ";
			} else {
				jpql += "IS NULL ";
			}
		}
		if (id != null) {
			jpql += " AND id != :id ";
		}

		Query query = em.createQuery(jpql);
		query.setParameter("tenantId", tenantId);
		query.setParameter("name", name.toLowerCase().trim());

		if (customerId != null) {
			query.setParameter("customerId", customerId);
		}
		if (id != null) {
			query.setParameter("id", id);
		}

		List<Long> matchingNames = query.getResultList();
		return (matchingNames.size() == 0);
	}

	public <T> T reattach(T entity) {
		return reattach(entity, false);
	}

	public <T> T reattach(T entity, boolean refresh) {
		getHibernateSession().lock(entity, LockMode.NONE);
		if (refresh) {
			em.refresh(entity);
		}
		return entity;
	}

	public <T> T reattchAndFetch(T entity, String... fetchFields) {
		return postFetchFields(reattach(entity), fetchFields);
	}

	public <E extends Collection<T>, T> E reattchAndFetch(E entities, String... fetchFields) {
		for (T entity : entities) {
			postFetchFields(reattach(entity), fetchFields);
		}

		return entities;
	}

	public <T extends AbstractPersistentCollection & Iterable<?>> T reattchAndFetch(T persistentCollection) {
		persistentCollection.setCurrentSession((SessionImpl) getHibernateSession());
		persistentCollection.iterator().next().getClass();
		return persistentCollection;
	}


	/**
	 * Checks to see if a Throwable is, or is caused by a Class. This method is recursive, please be careful.
	 * 
	 * @param t A Throwable
	 * @param clazz A class, extending Throwable
	 * @return True if t is or is caused by clazz, false otherwise.
	 */
	private boolean causeContains(Throwable t, Class<? extends Throwable> clazz) {
		// check if this is the class we're searching for
		if (t.getClass().isAssignableFrom(clazz)) {
			return true;
		} else {
			// if not, are we at the end of our cause chain?
			if (t.getCause() == null) {
				// t has no cause, return false
				return false;
			} else {
				// t has a cause, recurse in
				return causeContains(t.getCause(), clazz);
			}
		}
	}
	
	
	public List<Long> idSearch(BaseSearchDefiner definer, SecurityFilter filter) {
		return new PerformSearchRequiringTransaction(em).idSearch(definer, filter);
	}

	
		
	@SuppressWarnings("unchecked")
	public <T> List<T> passThroughFindAll(String queryStr, Map<String,Object> parameters) {
		Query query = createQuery(queryStr, parameters);
		return (List<T>)query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T passThroughFind(String queryStr, Map<String,Object> parameters) {
		Query query = createQuery(queryStr, parameters);
		return (T)query.getSingleResult();
	}

	private Query createQuery(String queryStr, Map<String, Object> parameters) {
		Query query = em.createQuery(queryStr);
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}
	
	public int executeUpdate(String updateStr, Map<String,Object> parameters) {
		Query updateStmt = createQuery(updateStr, parameters);
		return updateStmt.executeUpdate();
	}
	
	public Statistics getHibernateStats() {
		Statistics stats = getHibernateSession().getSessionFactory().getStatistics();
		return stats;
	}
}
