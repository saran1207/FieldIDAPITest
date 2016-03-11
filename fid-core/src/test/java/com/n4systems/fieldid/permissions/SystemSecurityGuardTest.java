package com.n4systems.fieldid.permissions;

import com.n4systems.model.ExtendedFeature;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;



public class SystemSecurityGuardTest {

	/**
	 * all extended feature should have accessor method in the form "is[FeatureName]Enabled()"
	 */
	@Test
	public void should_have_a_method_for_each_extended_feature() {
		Class<SystemSecurityGuard> interfaceDefinition = SystemSecurityGuard.class;
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			String featureEnabledMethodName = feature.featureEnabledMethodName();
			try {
				assertNotNull(interfaceDefinition.getMethod(featureEnabledMethodName));
			} catch (NoSuchMethodException e) {
				
				fail("could not find accessor for extended feature " + featureEnabledMethodName);
			}
		}
	}
	
	
}
