package com.n4systems.fieldid.permissions;

import static com.n4systems.model.builders.PrimaryOrgBuilder.aPrimaryOrg;
import static com.n4systems.model.builders.TenantBuilder.aTenant;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantFinder;


public class SerializableSecurityGuardTest {

	
	@Test
	public void should_return_false_to_each_feature_menthod_with_no_enabled_features() {
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			Tenant tenant = aTenant().build();
			
			TenantFinder mockFinder = createMock(TenantFinder.class);
			expect(mockFinder.findPrimaryOrg(tenant.getId())).andReturn(new PrimaryOrg());
			replay(mockFinder);
			TenantFinder.setInstance(mockFinder);
			
			SerializableSecurityGuard sut = new SerializableSecurityGuard(tenant);
			
			assertFalse("could not find accessor for extended feature " + feature.toString(), testIfFeatueIsEnabled(feature, sut));
		}
	}

	@Test
	public void should_respond_to_each_feature_menthod() {
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			Tenant tenant = aTenant().build();
			PrimaryOrg primaryOrg = aPrimaryOrg().withExtendedFeatures(feature).build();

			TenantFinder mockFinder = createMock(TenantFinder.class);
			expect(mockFinder.findPrimaryOrg(tenant.getId())).andReturn(primaryOrg);
			replay(mockFinder);
			TenantFinder.setInstance(mockFinder);
			
			
			SerializableSecurityGuard sut = new SerializableSecurityGuard(tenant);
			
			assertTrue("could not find accessor for extended feature " + feature.toString(), testIfFeatueIsEnabled(feature, sut));
		}
		 
	}

	private Boolean testIfFeatueIsEnabled(ExtendedFeature feature, SerializableSecurityGuard sut) {
		String featureEnabledMethodName = feature.featureEnabledMethodName();
		try {
			Method method = sut.getClass().getMethod(featureEnabledMethodName);
			return (Boolean)method.invoke(sut);
		} catch (Exception e) {
			fail("could not find accessor for extended feature " + featureEnabledMethodName + "  " + e.getMessage());
			return null;
		}
	}

}
