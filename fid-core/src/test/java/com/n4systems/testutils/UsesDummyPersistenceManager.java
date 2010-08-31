package com.n4systems.testutils;

import org.junit.After;
import org.junit.Before;

import com.n4systems.persistence.PersistenceManagerTestController;

public abstract class UsesDummyPersistenceManager {

	@Before
	public void preTest() {
		PersistenceManagerTestController.setDummyEntityManagerFactory();
		before();
	}
	
	@After
	public void postTest() {
		PersistenceManagerTestController.reset();
		after();
	}

	protected void before() {}
	
	protected void after() {}
}
