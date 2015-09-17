package com.n4systems.testutils;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.Map;

public class DummyEntityManagerFactory implements EntityManagerFactory {

    private boolean open = true;

    @Override
    public void close() {
        open = false;
    }

    @Override
    public EntityManager createEntityManager() {
        return new DummyEntityManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityManager createEntityManager(Map arg0) {
        return new DummyEntityManager();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return null;
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return null;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public Cache getCache() {
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return null;
    }

    @Override
    public void addNamedQuery(String s, Query query) {

    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> void addNamedEntityGraph(String s, EntityGraph<T> entityGraph) {

    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

}
