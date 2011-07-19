package com.n4systems.fieldid.service;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Transactional
public class PersistenceService extends FieldIdService {

    @PersistenceContext
    private EntityManager em;

    public <T extends EntityWithTenant> T find(Class<T> entityClass, Long entityId) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, userSecurityFilter);
        queryBuilder.setSimpleSelect();
        queryBuilder.addSimpleWhere("id", entityId);
        queryBuilder.applyFilter(userSecurityFilter);
        return find(queryBuilder);
    }

    public <T extends EntityWithTenant> List<T> findAll(Class<T> entityClass) {
        QueryBuilder<T> queryBuilder = new QueryBuilder<T>(entityClass, userSecurityFilter);
        queryBuilder.applyFilter(userSecurityFilter);
        return findAll(queryBuilder);
    }

    @SuppressWarnings("unchecked")
	public <T> T find(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		T result;

		try {
			result = (T) queryBuilder.createQuery(em).getSingleResult();
		} catch (NoResultException ne) {
			result = null;
		}

        return result;
    }

	public <T> List<T> findAll(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
        Query query = queryBuilder.createQuery(em);
        return (List<T>) query.getResultList();
    }

    public <T extends BaseEntity> Long save(T entity) {
        em.persist(entity);
        return entity.getId();
    }

	public <T extends BaseEntity> T update(T entity) {
		return em.merge(entity);
	}

    public Long count(QueryBuilder<?> searchBuilder) {
        return (Long) searchBuilder.setCountSelect().createQuery(em).getSingleResult();
    }
}
