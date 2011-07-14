package com.n4systems.persistence.context;


import javax.persistence.EntityManager;

public class ThreadLocalPersistenceContext implements PersistenceContext {

    private ThreadLocal<EntityManager> userThreadLocal = new ThreadLocal<EntityManager>();

    private static ThreadLocalPersistenceContext instance = new ThreadLocalPersistenceContext();

    public static PersistenceContext getInstance() {
        return instance;
    }

    @Override
    public EntityManager getEntityManager() {
        return userThreadLocal.get();
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        userThreadLocal.set(entityManager);
    }

}
