package com.n4systems.model.location;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.builders.PredefinedLocationBuilder;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.testutils.DummyEntityManager;

public class PredefinedLocationSaverTest {

	private EntityManager entitityManagerMock;
	private TenantOnlySecurityFilter filter;
	private PredefinedLocationChildLoader childLoader;
	private PredefinedLocation location;
	private PredefinedLocationSaver saver;
	private PredefinedLocationBuilder locationBuilder;
	private static final long TENANT_ID=1L;

	class TestingPredefinedLocationSaver extends PredefinedLocationSaver {
		TenantOnlySecurityFilter filter;
		PredefinedLocationChildLoader loader;

		public TestingPredefinedLocationSaver(TenantOnlySecurityFilter filter, PredefinedLocationChildLoader loader) {
			this.filter = filter;
			this.loader = loader;
		}

		protected TenantOnlySecurityFilter createTenantOnlySecurityFilter(PredefinedLocation location) {
			return filter;
		}

		protected PredefinedLocationChildLoader getChildLoader(PredefinedLocation location, SecurityFilter tenantFilter) {
			return loader;
		}
	};

	@Before
	public void setUp() {
		entitityManagerMock = createMock(EntityManager.class);
		filter = new TenantOnlySecurityFilter(TENANT_ID);
		childLoader = createMock(PredefinedLocationChildLoader.class);
		location = PredefinedLocationBuilder.aPredefinedLocation().build();
		saver = new TestingPredefinedLocationSaver(filter, childLoader);
		locationBuilder = PredefinedLocationBuilder.aPredefinedLocation();
	}

	@Test
	public void should_set_this_nodes_state_to_archived_and_updates() {

		expect(childLoader.setParentId(location.getId())).andReturn(childLoader);
		expect(childLoader.load(entitityManagerMock, filter)).andReturn(new ArrayList<PredefinedLocation>());

		expect(entitityManagerMock.merge(location)).andReturn(location);

		replay(childLoader);
		replay(entitityManagerMock);

		saver.remove(entitityManagerMock, location);

		assertEquals(Archivable.EntityState.ARCHIVED, location.getEntityState());

		verify(childLoader);
		verify(entitityManagerMock);
	}

	@Test
	public void should_set_child_nodes_states_to_archived() {

		PredefinedLocation location = locationBuilder.build();

		List<PredefinedLocation> children = Arrays.asList(locationBuilder.withParent(location).build(), locationBuilder.withParent(location).build());

		entitityManagerMock = new DummyEntityManager();

		expect(childLoader.setParentId(location.getId())).andReturn(childLoader);
		expect(childLoader.load(entitityManagerMock, filter)).andReturn(children);
		
		for (PredefinedLocation child : children) {
			expect(childLoader.setParentId(child.getId())).andReturn(childLoader);
			expect(childLoader.load(entitityManagerMock, filter)).andReturn(new ArrayList<PredefinedLocation>());
		}

		replay(childLoader);

		saver.remove(entitityManagerMock, location);

		for (PredefinedLocation child : children) {
			assertEquals(Archivable.EntityState.ARCHIVED, child.getEntityState());
		}

		verify(childLoader);
	}

	@Test
	public void should_create_security_filter_from_given_tenant() {
		TenantOnlySecurityFilter tenantFilter = new TenantOnlySecurityFilter(TENANT_ID);
		location.setTenant(new Tenant(TENANT_ID, "TENANT"));
		entitityManagerMock = new DummyEntityManager();

		PredefinedLocationSaver saver = new TestingPredefinedLocationSaver(tenantFilter, childLoader);

		expect(childLoader.setParentId(location.getId())).andReturn(childLoader);
		expect(childLoader.load(entitityManagerMock, tenantFilter)).andReturn(new ArrayList<PredefinedLocation>());
		replay(childLoader);
		saver.remove(entitityManagerMock, location);

		assertEquals(location.getTenant().getId(), tenantFilter.getTenantId());
		verify(childLoader);
	}
}
