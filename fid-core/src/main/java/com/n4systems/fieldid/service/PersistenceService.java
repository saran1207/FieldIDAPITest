package com.n4systems.fieldid.service;

import com.google.common.base.Preconditions;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SelectClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
public class PersistenceService extends FieldIdService {

    @PersistenceContext
    private EntityManager em;

    public PersistenceService() {
        super();
    }

    /**
     * This constructor allows us to override the injected entity manager
     * @param em
     */
    public PersistenceService(EntityManager em) {
        super();
        this.em = em;
    }

    // Once all web calls are successfully ported to open session in view (used now in wicket)
    // there should be no need to reattach anything.
    @Deprecated
    public <T> T reattach(T entity) {
        return reattach(entity, false);
    }

    @Deprecated
    public <T> void evict(T entity) {
        getHibernateSession().evict(entity);
    }

    @Deprecated
    public <T> T reattach(T entity, boolean refresh) {
        getHibernateSession().lock(entity, LockMode.NONE);
        if (refresh) {
            em.refresh(entity);
        }
        return entity;
    }

    public Session getHibernateSession() {
        return (Session) em.getDelegate();
    }

    @Transactional(readOnly = true)
    public <T> T findNonSecure(Class<T> entityClass, Object id) {
    	T entity = em.find(entityClass, id);
        return entity;
    }

    @Transactional(readOnly = true)
    public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = createUserSecurityBuilder(entityClass).addSimpleWhere("id", entityId);
        return find(queryBuilder);
    }

    @Transactional(readOnly = true)
    public <T extends AbstractEntity> T findById(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = createUserSecurityBuilder(entityClass).addSimpleWhere("id", entityId);
        return find(queryBuilder);
    }

	@Transactional(readOnly = true)
	public <T extends AbstractEntity> List<T> findAllById(Class<T> entityClass, Collection<Long> entityIds) {
		QueryBuilder<T> queryBuilder = createUserSecurityBuilder(entityClass).addWhere(WhereClauseFactory.create(Comparator.IN, "id", entityIds));
		return findAll(queryBuilder);
	}

    @Transactional
    public <T extends AbstractEntity> void delete(T entity) {
        em.remove(entity);
    }

	@Transactional
	public <T extends Serializable> void deleteAny(T entity) {
		em.remove(entity);
	}

    @Transactional(readOnly = true)
    public <T extends BaseEntity & UnsecuredEntity> T findUnsecured(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass);
        queryBuilder.setSimpleSelect();
        queryBuilder.addSimpleWhere("id", entityId);
        return find(queryBuilder);
    }

    @Transactional(readOnly = true)
    public <T extends BaseEntity & UnsecuredEntity> T findAllUnsecured(Class<T> entityClass) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass);
        queryBuilder.setSimpleSelect();
        return find(queryBuilder);
    }

    @Transactional(readOnly = true)
    public <T extends EntityWithTenant> T findUsingTenantOnlySecurityWithArchived(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = createTenantSecurityBuilder(entityClass, true).addSimpleWhere("id", entityId);
        return find(queryBuilder);
    }

    @Transactional(readOnly = true)
    public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass) {
        QueryBuilder<T> queryBuilder = createUserSecurityBuilder(entityClass);
        return findAll(queryBuilder);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
	public <T> T find(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		T result;

		try {
			result = (T) queryBuilder.createQuery(em).getSingleResult();
		} catch (NoResultException ne) {
			result = null;
		}

        return result;
    }

    @Transactional(readOnly = true)
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
        Query query = queryBuilder.createQuery(em);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
        return findAllPaginated(queryBuilder, page*pageSize, pageSize);
    }

    @Transactional(readOnly = true)
    public <T> List<T> findAllTransformed(QueryBuilder<T> queryBuilder, int page, int pageSize, ResultTransformer transformer) throws InvalidQueryException {
        return findAllPaginatedTransformed(queryBuilder, page*pageSize, pageSize, transformer);
    }

	@Transactional(readOnly = true)
	public <T> List<T> findAllPaginated(QueryBuilder<T> queryBuilder, int first, int count) throws InvalidQueryException {
		Query query = queryBuilder.createQuery(em).setFirstResult(first).setMaxResults(count);
		return query.getResultList();
	}

    @Transactional(readOnly = true)
    public <T> List<T> findAllPaginatedTransformed(QueryBuilder<T> queryBuilder, int first, int count, ResultTransformer transformer) throws InvalidQueryException {
        Query query = queryBuilder.createQuery(em).setFirstResult(first).setMaxResults(count);
        /* Result Transformer can only be set on the Hibernate Query class */
        query.unwrap(org.hibernate.Query.class).setResultTransformer(transformer);
        return query.getResultList();
    }

	@Transactional
	public <T> List<T> findAllNonSecure(Class<T> clazz) throws InvalidQueryException {
		Query query = em.createQuery("SELECT t FROM " + clazz.getName() + " t", clazz);
		return query.getResultList();
	}
    
    @Transactional(readOnly = true)
	public boolean exists(QueryBuilder<?> queryBuilder) throws InvalidQueryException {
        boolean exists = queryBuilder.entityExists(em);
        return exists;
    }

	@Transactional
	public void saveAny(Object entity) {
		em.persist(entity);
	}

    @Transactional
    public void save(Saveable saveable) {
    	em.persist(saveable);
    }

    @Transactional
    public <T extends BaseEntity> T saveOrUpdate(T entity) {
        if (entity.isNew()) {
            save(entity);
            return entity;
        } else {
            return update(entity);
        }
    }

    @Transactional
    public <T extends AbstractEntity> List<T> updateAll(List<T> entities, Long userId) {
        List<T> updatedEntities = new ArrayList<T>();

        for (T entity : entities) {
            updatedEntities.add(update(entity, userId));
        }
        return updatedEntities;
    }

    @Transactional
    public <T extends BaseEntity> Long save(T entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Transactional
    public <T extends BaseEntity> T update(T entity) {
        return em.merge(entity);
    }

    @Transactional
    public <T extends AbstractEntity> T update(T entity, Long userId) {
        return update(updateModifiedBy(entity, userId));
    }

    @Transactional
    public  Saveable update(Saveable entity) {
        return em.merge(entity);
    }

    private <T extends AbstractEntity> T updateModifiedBy(T entity, Long userId) {
        return updateModifiedBy(entity, find(User.class, userId));
    }

    private <T extends AbstractEntity> T updateModifiedBy(T entity, User user) {
        entity.setModifiedBy(user);
        return entity;
    }

    @Transactional
    public <T extends LegacyBaseEntity> T update(T entity) {
        return em.merge(entity);
    }

    @Transactional
    public <T extends BaseEntity> void update(List<T> entities) {
        for (T entity : entities) {
            update(entity);
        }
    }

    @Transactional
    public <T extends Saveable> void saveOrUpdate(List<T> entities) {
        for (T entity : entities) {
            if (entity.isNew()) {
                save(entity);
            } else {
                update(entity);
            }
        }
    }

    @Transactional
    public void remove(Object entity) {
        em.remove(entity);
    }

    @Transactional
    public Long count(QueryBuilder<?> searchBuilder) {
		SelectClause originalSelect = searchBuilder.getSelectArgument();
		Long count = null;
		try {
			count = (Long) searchBuilder.setCountSelect().createQuery(em).getSingleResult();
		} finally {
			searchBuilder.setSelectArgument(originalSelect);
		}
        return count;
    }

    @Deprecated
    @Transactional
    public <T extends Saveable> void save(Saver<T> saver, T entity) {
    	saver.save(em, entity);
    }

    @Deprecated
    @Transactional
    public <T extends Saveable> T update(Saver<T> saver, T entity) {
    	return saver.update(em, entity);
    }

    @Deprecated
    @Transactional
    public <T extends Saveable> void remove(Saver<T> saver, T entity) {
    	saver.remove(em, entity);
    }
    
    @Deprecated
    @Transactional
    public <T> T load(Loader<T> loader) {
    	return loader.load(em);
    }

    @Transactional
    public void archive(ArchivableEntityWithTenant archivableEntity) {
        archivableEntity.setState(Archivable.EntityState.ARCHIVED);
        update(archivableEntity);
    }

    @Transactional
    public List<?> runQuery(String queryString, Map<String,Object> params) {     	
    	Query query = em.createQuery(queryString);
    	for (Entry<String,Object> entry:params.entrySet()) {
    		query.setParameter(entry.getKey(), entry.getValue());
    	}    	    	
    	return query.getResultList();    	
    }

    @Transactional
    public Query createSQLQuery (String queryString, Class resultClass) {
        return em.createNativeQuery(queryString, resultClass);
    }

    @Transactional
    public Query createQuery (String queryString) {
        return createQuery(queryString, new HashMap<String, Object>());
    }
    
    @Transactional
    public Query createQuery (String queryString, Map<String,Object> params) {     	
    	Query query = em.createQuery(queryString);
    	for (Entry<String,Object> entry:params.entrySet()) {
    		query.setParameter(entry.getKey(), entry.getValue());
    	}    	    	
    	return query;
    }

    public <T extends BaseEntity & NamedEntity> T findByName(Class<T> entityClass, String entityName) {
        T entity;

        try {
            QueryBuilder<T> query = createTenantSecurityBuilder(entityClass);
            query.addSimpleWhere("name", entityName);
            entity = find(query);
        } catch (NoResultException ne) {
            entity = null;
        }

        return entity;
    }

	public  <T extends BaseEntity & NamedEntity> boolean isUniqueName(Class<T> clazz, String name, Long id) {
		Preconditions.checkArgument(NamedEntity.class.isAssignableFrom(clazz), "entity must implement "+NamedEntity.class.getSimpleName()+" in order to update its name.");
        QueryBuilder<T> queryBuilder = createUserSecurityBuilder(clazz);
        queryBuilder.addSimpleWhere("name", name);
        if(id != null) {
        	queryBuilder.addWhere(WhereClauseFactory.create(Comparator.NE, "id", id));
        }
        return find(queryBuilder)==null;
	}

    @Transactional
    public void clearSession() {
        getHibernateSession().clear();
    }

    @Transactional
    public boolean contains(Object entity) {
        return em.contains(entity);
    }

    @Transactional
    public Object merge(Object entity) {
        return em.merge(entity);
    }
}
