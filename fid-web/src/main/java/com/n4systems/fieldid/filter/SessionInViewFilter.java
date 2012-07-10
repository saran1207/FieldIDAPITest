package com.n4systems.fieldid.filter;

import com.n4systems.persistence.context.ThreadLocalPersistenceContext;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;

public class SessionInViewFilter extends OpenEntityManagerInViewFilter {

    @Override
    public EntityManagerFactory lookupEntityManagerFactory() {
        return super.lookupEntityManagerFactory();
    }

    @Override
    public EntityManager createEntityManager(EntityManagerFactory emf) {
        EntityManager entityManager = super.createEntityManager(emf);
        entityManager.setFlushMode(FlushModeType.COMMIT);
        ThreadLocalPersistenceContext.getInstance().setEntityManager(entityManager);
        return entityManager;
    }

}
