package com.n4systems.persistence;

import com.n4systems.testutils.DummyEntityManagerFactory;

public class PersistenceManagerTestController {

	public static void setDummyEntityManagerFactory() {
		PersistenceManager.overrideEntityManagerFactory(new DummyEntityManagerFactory());
	}
	
	public static void reset() {
		PersistenceManager.overrideEntityManagerFactory(null);
	}
	
}
