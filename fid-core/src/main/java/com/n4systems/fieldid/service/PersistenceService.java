package com.n4systems.fieldid.service;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.persistence.QueryBuilder;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class PersistenceService extends FieldIdService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, userSecurityFilter);
        queryBuilder.setSimpleSelect();
        queryBuilder.addSimpleWhere("id", entityId);
        queryBuilder.applyFilter(userSecurityFilter);
        return find(queryBuilder);
    }

    @Transactional(readOnly = true)
    public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, userSecurityFilter);
        queryBuilder.applyFilter(userSecurityFilter);
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
    public Long count(QueryBuilder<?> searchBuilder) {
        return (Long) searchBuilder.setCountSelect().createQuery(em).getSingleResult();
    }

    @Transactional(readOnly = true)
    public <T extends NetworkEntity<T>> T findAndEnhance(Class<T> entityClass, Long entityId, SecurityFilter securityFilter) {
        // TODO: Figure out why readonly=true doesn't properly set underlying hibernate flush mode.
        ((Session)em.getDelegate()).setFlushMode(FlushMode.MANUAL);
        T t = em.find(entityClass, entityId);
        SecurityLevel securityLevel = t.getSecurityLevel(securityFilter.getOwner());
        return t.enhance(securityLevel);
    }

}
