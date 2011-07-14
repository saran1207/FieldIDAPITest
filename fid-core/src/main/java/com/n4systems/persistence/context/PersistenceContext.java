package com.n4systems.persistence.context;

import javax.persistence.EntityManager;

public interface PersistenceContext {

    public EntityManager getEntityManager();
    public void setEntityManager(EntityManager entityManager);

}
