package com.n4systems.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.mail.search.SearchTerm;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.collection.AbstractPersistentCollection;

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
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;

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

	/**
	 * Constructs a Query from a QueryBuilder, injecting the EntityManager.  Note this method will not
	 * survive serialization (eg RMI).
	 * @see QueryBuilder#createQuery(EntityManager)
	 * @param builder	A QueryBuilder
	 * @return			A prepared Query from this QueryBuilder
	 * @throws InvalidQueryException	Propagates {@link QueryBuilder#createQuery(EntityManager)} exceptions.
	 */
	public Query prepare(QueryBuilder<?> builder) throws InvalidQueryException;

	/**
	 * Returns a count of the number of pages
	 * @param definer	The search definition
	 * @param filter	SecurityFilter to use in search
	 * @return			number of pages
	 */
	public int countPages(BaseSearchDefiner definer, SecurityFilter filter, long pageSize);
	
	/**
	 * Returns a list of entity id's as defined by the {@link BaseSearchDefiner}.  Note that although
	 * the BaseSearchDefiner does not enforce the search class ({@link BaseSearchDefiner#getSearchClass()}) to be
	 * a subclass of {@link BaseEntity}, <code>"id"</code> is still assumed to be the identity field.  This means 
	 * that legacy entities will fail handed to this method.
	 * @param definer	The search definition
	 * @param filter	SecurityFilter to use in search
	 * @return			A list of Long entity ids.
	 */
	public List<Long> idSearch(BaseSearchDefiner definer, SecurityFilter filter);
	
	/**
	 * Performs a search as defined by the {@link SearchDefiner}.  Constructs a query based on the {@link SearchTerm}s and
	 * applies {@link SecurityFilter} rules.  Sets the total number of results back onto the definer ({@link SearchDefiner#setTotalResults(int)}).
	 * Applies {@link SortTerm}s, and runs the query paginated by definer's {@link SearchDefiner#getPage()} and {@link SearchDefiner#getPageSize()}.
	 * Finally the returned entities are passed to the {@link ResultTransformer#transform(List)} of the definder's {@link SearchDefiner#getTransformer()}.
	 * @see SearchDefiner
	 * @see ResultTransformer
	 * @param definer	The search definition
	 * @param filter	SecurityFilter to use in search
	 * @return			The paginated entity data as returned from {@link ResultTransformer#transform(List)}
	 */
	public <K> K search(SearchDefiner<K> definer, SecurityFilter filter);

	
	public <T> List<T> passThroughFindAll(String query, Map<String,Object> parameters) ;
	public <T> T passThroughFind(String queryStr, Map<String,Object> parameters);
	public int executeUpdate(String updateStmt, Map<String,Object> parameters);
}
