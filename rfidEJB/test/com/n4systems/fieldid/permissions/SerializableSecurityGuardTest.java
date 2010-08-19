package com.n4systems.fieldid.permissions;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.services.TenantCache;



public class SerializableSecurityGuardTest {

	
	@Test
	public void should_return_false_to_each_feature_menthod_with_no_enabled_features() {
		for (ExtendedFeature feature : ExtendedFeature.values()) {
			Tenant tenant = aTenant().build();
			
			TenantCache mockCache = createMock(TenantCache.class);
			expect(mockCache.findPrimaryOrg(tenant.getId())).andReturn(new PrimaryOrg());
			replay(mockCache);
			TenantCache.setInstance(mockCache);
			
			SerializableSecurityGuard sut = new SerializableSecurityGuard(tenant);
			
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
	
	

	@Test
	public void should_show_that_plans_and_pricing_is_available_when_the_primary_org_does_not_have_the_partner_center() throws Exception {
		PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().withNoExtendedFeatures().build();
		
		SerializableSecurityGuard sut = new SerializableSecurityGuard(primaryOrg.getTenant(), primaryOrg);
		
		assertTrue(sut.isPlansAndPricingAvailable());
	}
	
	@Test
	public void should_show_that_plans_and_pricing_is_available_when_the_primary_org_does_not_have_the_partner_center_and_plans_and_pricing_not_available() throws Exception {
		PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().withNoExtendedFeatures().withPlansAndPricingNotAvailable().build();
		
		SerializableSecurityGuard sut = new SerializableSecurityGuard(primaryOrg.getTenant(), primaryOrg);
		
		assertTrue(sut.isPlansAndPricingAvailable());
	}
	
	@Test
	public void should_show_that_plans_and_pricing_is_available_when_the_primary_org_does_has_the_partner_center_and_plans_and_pricing_available_set_to_true() throws Exception {
		PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().withExtendedFeatures(ExtendedFeature.PartnerCenter).withPlansAndPricingAvailable().build();
		
		SerializableSecurityGuard sut = new SerializableSecurityGuard(primaryOrg.getTenant(), primaryOrg);
		
		assertTrue(sut.isPlansAndPricingAvailable());
	}
	
	
	@Test
	public void should_show_that_plans_and_pricing_is_not_available_when_the_primary_org_does_has_the_partner_center() throws Exception {
		PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().withExtendedFeatures(ExtendedFeature.PartnerCenter).build();
		
		SerializableSecurityGuard sut = new SerializableSecurityGuard(primaryOrg.getTenant(), primaryOrg);
		
		assertTrue(!sut.isPlansAndPricingAvailable());
	}
	
	

}
