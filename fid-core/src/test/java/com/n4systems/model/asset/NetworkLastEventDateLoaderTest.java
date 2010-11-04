package com.n4systems.model.asset;

import javax.persistence.EntityManager;

import org.junit.Test;

public class NetworkLastEventDateLoaderTest {

	@Test(expected=SecurityException.class)
	public void load_throws_security_exception_on_null_networkid() {
		LastEventDateLoader loader = new LastEventDateLoader();
		
		loader.load((EntityManager)null);	
	}
	
}
