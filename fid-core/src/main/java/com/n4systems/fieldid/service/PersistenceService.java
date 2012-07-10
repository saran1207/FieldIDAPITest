package com.n4systems.fieldid.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

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
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
public class PersistenceService extends FieldIdService {

    @PersistenceContext
    private EntityManager em;

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

    private Session getHibernateSession() {
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

    @Transactional
    public <T extends AbstractEntity> void delete(T entity) {
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
        Query query = queryBuilder.createQuery(em).setFirstResult(page*pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }
    
    @Transactional(readOnly = true)
	public boolean exists(QueryBuilder<?> queryBuilder) throws InvalidQueryException {
        boolean exists = queryBuilder.entityExists(em);
        return exists;
    }

    @Transactional
    public void save(Saveable saveable) {
    	em.persist(saveable);
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
	public <T extends BaseEntity> void update(List<T> entities) {
        for (T entity : entities) {
            update(entity);
        }
	}

    @Transactional
    public void remove(Object entity) {
        em.remove(entity);
    }

    @Transactional
    public Long count(QueryBuilder<?> searchBuilder) {
        return (Long) searchBuilder.setCountSelect().createQuery(em).getSingleResult();
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

}

