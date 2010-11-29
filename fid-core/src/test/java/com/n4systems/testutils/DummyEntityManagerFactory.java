package com.n4systems.testutils;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

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
	public Map<String, Object> getProperties() {
		return null;
	}

}
