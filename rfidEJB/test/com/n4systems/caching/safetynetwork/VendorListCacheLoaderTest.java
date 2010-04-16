package com.n4systems.caching.safetynetwork;

import static com.n4systems.model.builders.OrgBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.VendorOrgConnectionsListLoader;
import com.n4systems.test.helpers.FluentArrayList;

public class VendorListCacheLoaderTest {
	
	private class TestVendorListCacheLoader extends VendorListCacheLoader {
		public VendorOrgConnectionsListLoader mockedConnLoader;
		
		public TestVendorListCacheLoader() {
			mockedConnLoader = EasyMock.createMock(VendorOrgConnectionsListLoader.class);
		}
		
		@Override
		protected VendorOrgConnectionsListLoader createConnectionLoader(VendorListCacheKey key) {
			return mockedConnLoader;
		}
	}
	
	private PrimaryOrg fromOrg;
	private List<PrimaryOrg> vendors;
	private List<OrgConnection> connections;
	
	@Before
	public void setup_connections() {
		fromOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		vendors = new FluentArrayList<PrimaryOrg>(aPrimaryOrg().buildPrimary(), aPrimaryOrg().buildPrimary(), aPrimaryOrg().buildPrimary());
		
		connections = new ArrayList<OrgConnection>();
		connections.add(new OrgConnection(vendors.get(0), fromOrg));
		connections.add(new OrgConnection(vendors.get(1), fromOrg));
		connections.add(new OrgConnection(vendors.get(2), fromOrg));
	}
	
	@Test
	public void test_load() {
		VendorListCacheKey key = new VendorListCacheKey(fromOrg);
		
		TestVendorListCacheLoader cacheLoader = new TestVendorListCacheLoader();
		
		EasyMock.expect(cacheLoader.mockedConnLoader.load()).andReturn(connections);
		EasyMock.replay(cacheLoader.mockedConnLoader);
				
		List<InternalOrg> loadedVendors = cacheLoader.load(key);
		
		assertEquals(vendors, loadedVendors);
		
		EasyMock.verify(cacheLoader.mockedConnLoader);
	}
}
