package com.n4systems.fieldid.permissions;

import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;



import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;



public class SessionSecurityGuardTest extends SecurityGuardTestCase{

	
	@Test
	public void should_return_false_to_each_feature_menthod_with_no_enabled_features() {
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			Tenant tenant = aTenant().build();
			
			TenantCache mockCache = createMock(TenantCache.class);
			expect(mockCache.findPrimaryOrg(tenant.getId())).andReturn(new PrimaryOrg());
			replay(mockCache);
			TenantCache.setInstance(mockCache);
			
			SessionSecurityGuard sut = new SessionSecurityGuard(tenant);
			
			assertFalse("could not find accessor for extended feature " + feature.toString(), testIfFeatueIsEnabled(feature, sut));
		}
	}

	@Test
	public void should_respond_to_each_feature_menthod() {
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			Tenant tenant = aTenant().build();
			PrimaryOrg primaryOrg = aPrimaryOrg().withExtendedFeatures(feature).build();

			TenantCache mockCache = createMock(TenantCache.class);
			expect(mockCache.findPrimaryOrg(tenant.getId())).andReturn(primaryOrg);
			replay(mockCache);
			TenantCache.setInstance(mockCache);
			
			
			SessionSecurityGuard sut = new SessionSecurityGuard(tenant);
			
			assertTrue("could not find accessor for extended feature " + feature.toString(), testIfFeatueIsEnabled(feature, sut));
		}
		 
	}

	private Boolean testIfFeatueIsEnabled(ExtendedFeature feature, SessionSecurityGuard sut) {
		String featureEnabledMethodName = getFeatureMethodName(feature);
		try {
			Method method = sut.getClass().getMethod(featureEnabledMethodName);
			return (Boolean)method.invoke(sut);
		} catch (Exception e) {
			fail("could not find accessor for extended feature " + featureEnabledMethodName + "  " + e.getMessage());
			return null;
		}
	}
	

}
