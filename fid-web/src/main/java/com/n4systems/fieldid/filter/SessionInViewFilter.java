package com.n4systems.fieldid.filter;

import com.n4systems.persistence.context.ThreadLocalPersistenceContext;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SessionInViewFilter extends OpenEntityManagerInViewFilter {

    @Override
    public EntityManagerFactory lookupEntityManagerFactory() {
        return super.lookupEntityManagerFactory();
    }

    @Override
    public EntityManager createEntityManager(EntityManagerFactory emf) {
        EntityManager entityManager = super.createEntityManager(emf);
        ThreadLocalPersistenceContext.getInstance().setEntityManager(entityManager);
        return entityManager;
    }

}
