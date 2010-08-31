package com.n4systems.fieldid.actions;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.util.ListingPair;


public class SystemSettingsCrudTest {

	
	@Test
	public void should_find_valid_default_vendor_context_when_context_is_null() throws Exception {
		SystemSettingsCrud sut = new SystemSettingsCrudTestDouble(aPrimaryOrg().build());
		sut.setDefaultVendorContext(null);
		assertTrue(sut.isValidVendor());
	}
	
	
	@Test
	public void should_find_invalid_default_vendor_context_when_context_is_set_but_there_are_no_vendors() throws Exception {
		SystemSettingsCrud sut = new SystemSettingsCrudTestDouble(aPrimaryOrg().build());
		sut.setDefaultVendorContext(1L);
		assertTrue(!sut.isValidVendor());
	}
	
	@Test
	public void should_find_invalid_default_vendor_context_when_context_is_set_but_vendor_list_does_not_include_it() throws Exception {
		SystemSettingsCrudTestDouble sut = new SystemSettingsCrudTestDouble(aPrimaryOrg().build());
		sut.vendorList.add(new ListingPair(2L, "some other vendor 1"));
		sut.vendorList.add(new ListingPair(3L, "some other vendor 2"));
		sut.setDefaultVendorContext(1L);
		assertTrue(!sut.isValidVendor());
	}
	
	@Test
	public void should_find_valid_default_vendor_context_when_context_is_set_and_the_vendor_is_in_the_list() throws Exception {
		SystemSettingsCrudTestDouble sut = new SystemSettingsCrudTestDouble(aPrimaryOrg().build());
		sut.vendorList.add(new ListingPair(3L, "some other vendor 2"));
		sut.vendorList.add(new ListingPair(1L, "the vendor being set"));
		sut.setDefaultVendorContext(1L);
		
		assertTrue(sut.isValidVendor());
	}
	
	private class SystemSettingsCrudTestDouble extends SystemSettingsCrud {
		List<ListingPair> vendorList = new ArrayList<ListingPair>();
		PrimaryOrg primaryOrg;
		
		public SystemSettingsCrudTestDouble(PrimaryOrg primaryOrg) {
			super(null);
			this.primaryOrg = primaryOrg;
		}

		
		@Override
		public List<ListingPair> getVendorContextList() {
			return vendorList;
		}


		@Override
		public PrimaryOrg getPrimaryOrg() {
			return primaryOrg;
		}
		
		
	}
}
