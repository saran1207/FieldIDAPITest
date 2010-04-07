package com.n4systems.model.tenant;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.persistence.loaders.AllEntityListLoader;
import com.n4systems.util.ConfigContextOverridableTestDouble;
import com.n4systems.util.ConfigEntry;

public class TenantNameAvailabilityCheckerTest {
	private File reservedTenantNameFile;
	
	@Before
	public void setup_reserved_tenant_names() throws URISyntaxException {
		reservedTenantNameFile = new File(getClass().getResource("reservedTenantNames.properties").toURI());
	}
	
	@Test
	public void is_available_checks_for_unbranded_subdomain() {
		ConfigContextOverridableTestDouble config = new ConfigContextOverridableTestDouble();
		config.markClean();
		config.addConfigurationValue(ConfigEntry.UNBRANDED_SUBDOMAIN, "unbranded");
		
		TenantNameAvailabilityChecker checker = new TenantNameAvailabilityChecker(new EmptyTenantListLoader(), config, reservedTenantNameFile);
		
		assertFalse(checker.isAvailable("unbranded"));
		assertFalse(checker.isAvailable("uNbranDed"));
	}
	
	@Test
	public void is_available_checks_for_reserved_subdomains() {
		TenantNameAvailabilityChecker checker = new TenantNameAvailabilityChecker(new EmptyTenantListLoader(), new ConfigContextOverridableTestDouble(), reservedTenantNameFile);
		
		assertFalse(checker.isAvailable("tenanta"));
		assertFalse(checker.isAvailable("tenantb"));
	}
	
	@Test
	public void is_available_checks_for_existing_tenants() {
		TenantNameAvailabilityChecker checker = new TenantNameAvailabilityChecker(new AllEntityListLoader<Tenant>(Tenant.class) {
			@Override
			public List<Tenant> load() {
				TenantBuilder tenantBuilder = TenantBuilder.aTenant();

				return Arrays.asList(tenantBuilder.named("tenant1").build(), tenantBuilder.named("TENANT2").build());
			}
		}, new ConfigContextOverridableTestDouble(), reservedTenantNameFile);
		
		
		assertFalse(checker.isAvailable("tenant1"));
		assertFalse(checker.isAvailable("tenant2"));
	}
	
	@Test
	public void is_available_returns_true_when_name_available() {
		ConfigContextOverridableTestDouble config = new ConfigContextOverridableTestDouble();
		config.markClean();
		config.addConfigurationValue(ConfigEntry.UNBRANDED_SUBDOMAIN, "unbranded");
		
		TenantNameAvailabilityChecker checker = new TenantNameAvailabilityChecker(new AllEntityListLoader<Tenant>(Tenant.class) {
			@Override
			public List<Tenant> load() {
				TenantBuilder tenantBuilder = TenantBuilder.aTenant();

				return Arrays.asList(tenantBuilder.named("tenant1").build(), tenantBuilder.named("TENANT2").build());
			}
		}, config, reservedTenantNameFile);
		
		assertTrue(checker.isAvailable("this_name_is_free"));
	}
	
	@Test
	public void allows_reserved_names_file_to_not_exist() {
		TenantNameAvailabilityChecker checker = new TenantNameAvailabilityChecker(new EmptyTenantListLoader(), new ConfigContextOverridableTestDouble(), new File("some/random/path"));
		
		assertTrue(checker.isAvailable("tenant1"));
	}
	
	private class EmptyTenantListLoader extends AllEntityListLoader<Tenant> {
		public EmptyTenantListLoader() {
			super(Tenant.class);
		}

		@Override
		public List<Tenant> load() {
			return new ArrayList<Tenant>();
		}
	}
}
