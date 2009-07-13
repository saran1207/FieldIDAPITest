package com.n4systems.ejb;

import org.junit.Assert;
import org.junit.Test;

public class SafetyNetworkManagerTest {

	
	
	@Test
	public void shouldProduceASNACWithLettersAndNumbers() {
		
		String fidAC = "";
		
		SafetyNetworkManagerImpl safetyNetworkManager = new SafetyNetworkManagerImpl();
		
		for( int i = 0; i < 100; i++ ) {
			fidAC = safetyNetworkManager.generatNewFidAC();
			
			Assert.assertEquals("", fidAC.replaceAll( "[A-Z0-9]+", "") ) ;
			
		}
		Assert.assertTrue(true);
		
	}
}
