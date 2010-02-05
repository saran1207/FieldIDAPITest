package com.n4systems.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.stat.Statistics;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.AbstractStringIdEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

@Local
public interface PersistenceManager {
	public EntityManager getEntityManager();

	public <T extends BaseEntity> T find(Class<T> entityClass, Long entityId);
	
	public <T extends AbstractStringIdEntity> T find(Class<T> entityClass, String id);

	public <T extends BaseEntity> T find(Class<T> entityClass, Long entityId, String... postFetchFields);

	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Tenant tenant, String... postFetchFields);

	public <T extends LegacyBaseEntity> T findLegacy(Class<T> entityClass, Long entityId);

	public <T extends LegacyBaseEntity> T findLegacy(Class<T> entityClass, Long entityId, SecurityFilter filter);
	
	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Tenant tenant);

	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Long tenantId);

	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, Long tenantId, String... postFetchFields);

	public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId, SecurityFilter filter, String... postFetchFields);

	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass);
	
	public <T> List<T> findAll(Class<T> entityClass, String jpqlQuery, Map<String, Object> parameters);

	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, String orderBy);

	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Set<Long> ids, Tenant tenant, String... postFetchFields);

	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Tenant tenant);

	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Long tenantId);

	public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass, Long tenantId, Map<String, Boolean> orderBy);

	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, Map<String, Object> whereClauses);

	public <T extends BaseEntity> List<T> findAll(Class<T> entityClass, Map<String, Object> whereClauses, Map<String, Boolean> orderBy);

	public <T extends EntityWithTenant> List<T> findAllByDate(Class<T> entityClass, Long tenantId, Date startDate, Date endDate, Long beginningId, Integer limit);

	public <T extends BaseEntity & NamedEntity> T findByName(Class<T> entityClass, String entityName);

	public <T extends EntityWithTenant & NamedEntity> T findByName(Class<T> entityClass, Long tenantId, String entityName);
	
	public <T extends EntityWithTenant & NamedEntity> String findName(Class<T> entityClass, Long id, SecurityFilter filter);

	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, SecurityFilter filter);

	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, Long tenantId);

	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, SecurityFilter filter, String nameField);

	public <T extends EntityWithTenant> List<ListingPair> findAllLP(Class<T> entityClass, Long tenantId, String nameField);
	
	public List<ListingPair> findAllLP(QueryBuilder<ListingPair> query, String nameField);

	public <T> T find(QueryBuilder<T> queryBuilder) throws InvalidQueryException;

	public <T> List<T> findAll(QueryBuilder<T> queryBuilder) throws InvalidQueryException;

	public <T> List<T> findAll(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException;

	public <T> Pager<T> findAllPaged(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException;

	public <T> int countAllPages(Class<T> entityClass, int pageSize, SecurityFilter filter);

	public Long findCount(QueryBuilder<?> queryBuilder) throws InvalidQueryException;

	public <T> void saveAny(T entity);
	
	public <T extends BaseEntity> Long save(T entity);
	
	public <T extends AbstractStringIdEntity> void save(T entity);

	public <T> T updateAny(T entity);
	
	public <T extends BaseEntity> T update(T entity);

	public <T extends AbstractStringIdEntity> T update(T entity);
	
	public <T extends AbstractEntity> Long save(T entity, UserBean user);

	public <T extends AbstractEntity> T update(T entity, UserBean user);

	public <T extends AbstractEntity> Long save(T entity, Long userId);

	public <T extends AbstractEntity> T update(T entity, Long userId);
	
	public <T extends AbstractEntity> List<T> updateAll(List<T> entity, Long userId);
	
	public <T extends BaseEntity> void delete(T entity);
	public <T> void deleteAny(T entity);
	
	/**
	 * Attempts to delete an Entity and watches for ConstraintViolationExceptions
	 * 
	 * @param entity The entity to remove
	 * @throws If a ConstraintViolationException is thrown in the process of deleting.
	 */
	public <T extends BaseEntity> void deleteSafe(T entity) throws EntityStillReferencedException;

	public <E extends Collection<T>, T> E postFetchFields(E entities, String... postFetchFields);

	public <E extends Collection<T>, T> E postFetchFields(E entities, List<String> postFetchFields);

	public <T> T postFetchFields(T entity, List<String> postFetchFields);

	public <T> T postFetchFields(T entity, String... postFetchFields);

	public <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailable(Class<T> entityClass, String name, Long id, Long tenantId);

	public <T extends EntityWithTenant & NamedEntity> boolean uniqueNameAvailableWithCustomer(Class<T> entityClass, String name, Long id, Long tenantId, Long customerId);

	public <T> T reattach(T entity);

	public <T> T reattach(T entity, boolean refresh);

	public <T> T reattchAndFetch(T entity, String... fetchFields);

	public <T extends AbstractPersistentCollection & Iterable<?>> T reattchAndFetch(T persistentCollection);

	public <E extends Collection<T>, T> E reattchAndFetch(E entities, String... fetchFields);

	public <T> List<T> passThroughFindAll(String query, Map<String,Object> parameters) ;
	public <T> T passThroughFind(String queryStr, Map<String,Object> parameters);
	public int executeUpdate(String updateStmt, Map<String,Object> parameters);
	
	public Statistics getHibernateStats();
}
