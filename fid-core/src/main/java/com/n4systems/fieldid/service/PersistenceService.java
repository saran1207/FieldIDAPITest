package com.n4systems.fieldid.service;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;
public class PersistenceService extends FieldIdService {

    @PersistenceContext
    private EntityManager em;

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
    public <T extends BaseEntity & UnsecuredEntity> T findUnsecured(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass);
        queryBuilder.setSimpleSelect();
        queryBuilder.addSimpleWhere("id", entityId);
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
        return (List<T>) query.getResultList();
    }

    @Transactional(readOnly = true)
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
        Query query = queryBuilder.createQuery(em).setFirstResult(page*pageSize).setMaxResults(pageSize);
        return (List<T>) query.getResultList();
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

}
