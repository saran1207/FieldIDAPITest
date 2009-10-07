package com.n4systems.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.impl.SessionImpl;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.api.Retirable;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.AbstractStringIdEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.SelectClause;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

@Interceptors( { TimingInterceptor.class })
@Stateless
public class PersistenceManagerImpl implements PersistenceManager {
	private Logger logger = Logger.getLogger(PersistenceManagerImpl.class);
	private static final String unitName = "rfidEM";
	private static final String defaultTableAlias = "e";

	@PersistenceContext(unitName = unitName)
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private Session getHibernateSession() {
		return (Session) em.getDelegate();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public EntityManager getEntityManager() {
		return em;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends BaseEntity> T find(Class<T> entityClass, Long entityId) {
		return em.find(entityClass, entityId);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends AbstractStringIdEntity> T find(Class<T> entityClass, String id) {
		return em.find(entityClass, id);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends LegacyBaseEntity> T findLegacy(Class<T> entityClass, Long entityId) {
		return em.find(entityClass, entityId);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends LegacyBaseEntity> T findLegacy(Class<T> entityClass, Long entityId, SecurityFilter filter) {
		QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, filter);
		queryBuilder.addSimpleWhere("uniqueID", entityId);
		
		return find(queryBuilder);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends BaseEntity> T find(Class<T> entityClass, Long entityId, String... postFetchFields) {
		return postFetchFields(find(entityClass, entityId), postFetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Tenant tenant, String... postFetchFields) {
		return postFetchFields(find(entityClass, entityId, tenant), postFetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Tenant tenant) {
		return find(entityClass, entityId, tenant.getId());
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Long tenantId, String... postFetchFields) {
		return postFetchFields(find(entityClass, entityId, tenantId), postFetchFields);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant & NamedEntity> String findName(Class<T> entityClass, Long id, SecurityFilter filter) {
		QueryBuilder<String> builder = new QueryBuilder<String>(entityClass, filter);
		
		return find(builder.setSimpleSelect("name").addSimpleWhere("id", id));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> List<T> findAll(Class<T> entityClass, String jpqlQuery, Map<String, Object> parameters) {
		Query query = getEntityManager().createQuery(jpqlQuery);
		
		for (String key: parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		
		return (List<T>) query.getResultList();
	}

	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass) {
		return (List<T>) em.createQuery(generateFromClause(defaultTableAlias, entityClass)).getResultList();
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, String orderBy) {
		return (List<T>) em.createQuery(generateFromClause(defaultTableAlias, entityClass) + " ORDER BY " + orderBy + " ASC ").getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Tenant tenant) {
		return findAll(entityClass, tenant.getId());
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Long tenantId) {
		return findAll(entityClass, tenantId, null);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Long tenantId, Map<String, Boolean> orderBy) {
		Map<String, Object> whereClause = new HashMap<String, Object>();
		whereClause.put("tenant.id", tenantId);
		return findAll(entityClass, whereClause, orderBy);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, Map<String, Object> whereClauses) {
		return findAll(entityClass, whereClauses, null);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		Query createQuery = queryBuilder.createQuery(em);
		List<T> resultList = (List<T>) createQuery.getResultList();
		return postFetchFields(resultList, queryBuilder.getPostFetchPaths());
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
		List<T> resultList = (List<T>) queryBuilder.createQuery(em).setFirstResult(pageSize * page).setMaxResults(pageSize).getResultList();
		return postFetchFields(resultList, queryBuilder.getPostFetchPaths());
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> Pager<T> findAllPaged(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
		SelectClause backedUpSelectArgument = queryBuilder.getSelectArgument();
		Pager<T> results = new Page<T>(queryBuilder.createQuery(em), queryBuilder.setCountSelect().createQuery(em), page, pageSize);
		queryBuilder.setSelectArgument(backedUpSelectArgument);
		postFetchFields(results.getList(), queryBuilder.getPostFetchPaths());
		return results;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	public <T extends AbstractStringIdEntity> void save(T entity) {
		em.persist(entity);
	}
	
	public <T> T updateAny(T entity) {
		return em.merge(entity);
	}
	
	public <T extends BaseEntity> T update(T entity) {
		return em.merge(entity);
	}

	public <T extends AbstractStringIdEntity> T update(T entity) {
		return em.merge(entity);
	}
	
	public <T extends AbstractEntity> Long save(T entity, UserBean user) {
		return save(updateModifiedBy(entity, user));
	}

	public <T extends AbstractEntity> T update(T entity, UserBean user) {
		return update(updateModifiedBy(entity, user));
	}

	public <T extends AbstractEntity> Long save(T entity, Long userId) {
		return save(updateModifiedBy(entity, userId));
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

	private <T extends AbstractEntity> T updateModifiedBy(T entity, Long userId) {
		return updateModifiedBy(entity, findLegacy(UserBean.class, userId));
	}

	private <T extends AbstractEntity> T updateModifiedBy(T entity, UserBean user) {
		entity.setModifiedBy(user);
		return entity;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private String generateFromClause(String tableAlias, Class<?> tableClass) {
		return generateFromClause(tableAlias, tableClass, null);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private String generateFromClause(String tableAlias, Class<?> tableClass, String[] preFetchFields) {
		String clause = "from " + tableClass.getName() + " " + tableAlias + " ";

		if (preFetchFields != null && preFetchFields.length > 0) {
			clause = "select distinct " + tableAlias + " " + clause + generatePreFetchQuery(tableAlias, preFetchFields);
		}

		return clause;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private String generatePreFetchQuery(String tableAlias, String[] preFetchFields) {
		String query = "";

		for (String field : preFetchFields) {
			query += "left join fetch " + tableAlias + "." + field + " ";
		}

		return query;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <E extends Collection<T>, T> E postFetchFields(E entities, String... postFetchFields) {
		return PostFetcher.postFetchFields(entities, postFetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <E extends Collection<T>, T> E postFetchFields(E entities, List<String> postFetchFields) {
		return PostFetcher.postFetchFields(entities, postFetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> T postFetchFields(T entity, String... postFetchFields) {
		return PostFetcher.postFetchFields(entity, postFetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> T postFetchFields(T entity, List<String> postFetchFields) {
		return PostFetcher.postFetchFields(entity, postFetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, SecurityFilter filter) {
		return findAllLP(entityClass, filter, "displayName");
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, Long tenantId) {
		// Display Name is the default value for the nameField.

		return findAllLP(entityClass, tenantId, "displayName");
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, Long tenantId, String nameField) {
		return findAllLP(entityClass, new TenantOnlySecurityFilter(tenantId), nameField);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ListingPair> findAllLP(QueryBuilder<ListingPair> query, String nameField) {
		query.setSelectArgument(new NewObjectSelect(ListingPair.class, "id", nameField));
		query.addOrder(nameField);
		return findAll(query);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailable(Class<T> entityClass, String name, Long id, Long tenantId) {
		return uniqueNameAvailable(entityClass, name, id, tenantId, null, false);

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailableWithCustomer(Class<T> entityClass, String name, Long id, Long tenantId, Long customerId) {
		return uniqueNameAvailable(entityClass, name, id, tenantId, customerId, true);
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> T reattach(T entity) {
		return reattach(entity, false);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> T reattach(T entity, boolean refresh) {
		getHibernateSession().lock(entity, LockMode.NONE);
		if (refresh) {
			em.refresh(entity);
		}
		return entity;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T> T reattchAndFetch(T entity, String... fetchFields) {
		return postFetchFields(reattach(entity), fetchFields);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <E extends Collection<T>, T> E reattchAndFetch(E entities, String... fetchFields) {
		for (T entity : entities) {
			postFetchFields(reattach(entity), fetchFields);
		}

		return entities;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <T extends AbstractPersistentCollection & Iterable<?>> T reattchAndFetch(T persistentCollection) {
		persistentCollection.setCurrentSession((SessionImpl) getHibernateSession());
		persistentCollection.iterator().next().getClass();
		return persistentCollection;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Query prepare(QueryBuilder<?> builder) throws InvalidQueryException {
		return builder.createQuery(em);
	}

	/**
	 * Checks to see if a Throwable is, or is caused by a Class. This method is recursive, please be careful.
	 * 
	 * @param t A Throwable
	 * @param clazz A class, extending Throwable
	 * @return True if t is or is caused by clazz, false otherwise.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
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

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public int countPages(BaseSearchDefiner definer, SecurityFilter filter, long pageSize) {
		QueryBuilder<Long> countBuilder = createBaseSearchQueryBuilder(definer, filter);

		Long count = find(countBuilder.setCountSelect());
		
		return (int)Math.ceil(count.doubleValue() / (double)pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Long> idSearch(BaseSearchDefiner definer, SecurityFilter filter) {
		// construct a search QueryBuilder with Long as the select class since we will force simple select to be "id" later
		QueryBuilder<Long> idBuilder = createBaseSearchQueryBuilder(definer, filter);
		
		addSortTermsToBuilder(idBuilder, definer.getSortTerms());
		// note that this will fail for entities not implementing BaseEntity (unless you get lucky)
		List<Long> ids = findAll(idBuilder.setSimpleSelect("id"));
		
		return ids;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <K> K search(SearchDefiner<K> definer, SecurityFilter filter) {
		
		// create our base query builder (no sort terms yet)
		QueryBuilder<?> searchBuilder = createBaseSearchQueryBuilder(definer, filter);
		
		// get/set the total result count now before the sort terms get added
		definer.setTotalResults(findCount(searchBuilder).intValue());
		
		// now we can add in our sort terms
		addSortTermsToBuilder(searchBuilder, definer.getSortTerms());
		
		// get the paged result list of entities, also set the select now since findCount() would have set it to a count select
		// note the generics have been left off NetworkEntity since they'll just get in the way
		List<NetworkEntity> entities = (List<NetworkEntity>) findAll(searchBuilder.setSimpleSelect(), definer.getPage(), definer.getPageSize());

		// now we need to make sure the entities get security enhanced
		List<NetworkEntity> enhancedEntities = EntitySecurityEnhancer.enhanceList(entities, filter);
		
		// transform the results to their final form
		return definer.getTransformer().transform(enhancedEntities);
	}

	/**
	 * Adds a list of SortTerms to a QueryBuilder.  Note the list of sort terms will be cleared prior to adding the new terms.
	 * @param builder		A search query builder
	 * @param sortTerms		List of SortTerms
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void addSortTermsToBuilder(QueryBuilder<?> builder, List<SortTerm> sortTerms) {
		builder.getOrderArguments().clear();
		
		if (sortTerms != null) {
			for (SortTerm sortTerm : sortTerms) {
				builder.getOrderArguments().add(sortTerm.toSortField());
			}
		}
	}
	
	/**
	 * Creates a basic QueryBuilder to be used in search methods.  Only the table and where parameters (including those from the SecurityFilter) will be defined so
	 * that it can be reused for returning entities, entity id's and result counts.  The select class is also specified so that the return type works properly for
	 * id queries where a Long return type is expected.
	 * @param definer		The search definition 
	 * @return				A QueryBuilder constructed from the Definer.
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private <T> QueryBuilder<T> createBaseSearchQueryBuilder(BaseSearchDefiner definer, SecurityFilter securityFilter) {
		// create our QueryBuilder, note the type will be the same as our selectClass
		QueryBuilder<T> searchBuilder = new QueryBuilder<T>(definer.getSearchClass(), securityFilter);

		// add all the left join columns
		if (definer.getJoinColumns() != null) {
			for (String column: definer.getJoinColumns()) {
				searchBuilder.addLeftJoin(column, null);
			}
		}
		
		// convert all our search terms to where parameters
		if (definer.getSearchTerms() != null && !definer.getSearchTerms().isEmpty()) {
			for (SearchTermDefiner term : definer.getSearchTerms()) {
				for (WhereClause<?> param: term.getWhereParameters()) {
					searchBuilder.addWhere(param);
				}
			}
		}
		
		if (definer.getSearchFilters() != null && !definer.getSearchFilters().isEmpty()) {
			for (QueryFilter filter : definer.getSearchFilters()) {
				filter.applyFilter(searchBuilder);
			}
		}
		return searchBuilder;
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
}
