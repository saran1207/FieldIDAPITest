package com.n4systems.testutils;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

}
