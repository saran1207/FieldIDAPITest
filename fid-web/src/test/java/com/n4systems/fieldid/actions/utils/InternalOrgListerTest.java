package com.n4systems.fieldid.actions.utils;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.api.Listable;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.SecondaryOrgBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.persistence.loaders.FilteredListableLoader;
import com.n4systems.test.helpers.FluentArrayList;


public class InternalOrgListerTest {
	
	private PrimaryOrg primaryOrg;
	private SecondaryOrg secondaryOrg;
	private FilteredListableLoader secondaryOrgLoader;


	@Before
	public void setUp() {
		primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().withName("primary").build();
		secondaryOrg = SecondaryOrgBuilder.aSecondaryOrg().withName("secondary").build();
		secondaryOrgLoader = secondaryOrgLoaderToLoadOrgs(secondaryOrg);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_list_of_all_internal_orgs_when_the_ALL_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.ALL, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(primaryOrg, secondaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_list_of_all_internal_orgs_when_the_INTERNAL_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.INTERNAL, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(primaryOrg, secondaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_list_of_all_internal_orgs_when_the_CUSTOMER_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.CUSTOMER, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(primaryOrg, secondaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_list_of_all_internal_orgs_when_the_EXTERNAL_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.EXTERNAL, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(primaryOrg, secondaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	
	@Test
	public void should_list_of_primary_and_secondary_orgs_when_the_NON_PRIMARY_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.NON_PRIMARY, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(primaryOrg).stickOn(secondaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	@Test
	public void should_list_of_only_secondary_orgs_when_the_SECONDARY_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.SECONDARY, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(secondaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	
	@Test
	public void should_list_of_only_primary_org_when_the_PRIMARY_filter_is_applied() throws Exception {
		InternalOrgLister sut = new InternalOrgLister(OrgType.PRIMARY, secondaryOrgLoader, primaryOrg);
		List<Listable<Long>> expectedInternalOrgs = new FluentArrayList<Listable<Long>>().stickOn(primaryOrg);
				
		List<Listable<Long>> actualInternalOrgs = sut.getInternalOrgs();
		
		assertArrayEquals(expectedInternalOrgs.toArray(), actualInternalOrgs.toArray());
	}
	
	
	
	
	/*@SuppressWarnings("unchecked")
	@Test
	public void should_list_of_all_customers_for_the_internal_org_of_selected_org_starting_with_a_blank_when_the_ALL_filter_is_applied() throws Exception {
		BaseOrg currentSelectedOrg = primaryOrg;
		
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().withParent(primaryOrg).withName("customer").build();		
		
		BaseOrgParentFilterListLoader baseOrgParentFilterListLoader = baseOrgListLoaderForCustomer(customerOrg);
		
		loaderFactory.setBaseOrgParentFilterListLoader(baseOrgParentFilterListLoader);
		
		OrgLister sut = new OrgLister(currentSelectedOrg, OrgType.ALL, loaderFactory, primaryOrg);
				
		List<Listable<Long>> customerOrgs = new FluentArrayList<Listable<Long>>().stickOn(new SimpleListable<Long>(-1L, ""), customerOrg);
		
		assertArrayEquals(customerOrgs.toArray(), sut.getCustomerOrgs().toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_list_of_all_divisions_for_the_customer_org_of_selected_org_starting_with_a_blank_when_the_ALL_filter_is_applied() throws Exception {
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().withParent(primaryOrg).withName("customer").build();
		DivisionOrg divisionOrg = DivisionOrgBuilder.aDivisionOrg().withCustomerOrg(customerOrg).withName("division").build();		
		
		BaseOrg currentSelectedOrg = customerOrg;
		
		BaseOrgParentFilterListLoader baseOrgParentFilterListLoader = baseOrgListLoaderForCustomer(customerOrg);
		
		loaderFactory.setBaseOrgParentFilterListLoader(baseOrgParentFilterListLoader);
		
		OrgLister sut = new OrgLister(currentSelectedOrg, OrgType.ALL, loaderFactory, primaryOrg);
				
		List<Listable<Long>> customerOrgs = new FluentArrayList<Listable<Long>>().stickOn(new SimpleListable<Long>(-1L, ""), customerOrg);
		
		assertArrayEquals(customerOrgs.toArray(), sut.getCustomerOrgs().toArray());
	}
	*/
	

	/*@SuppressWarnings("unchecked")
	private BaseOrgParentFilterListLoader baseOrgListLoaderForCustomer(CustomerOrg customerOrg) {
		BaseOrgParentFilterListLoader baseOrgParentFilterListLoader = createMock(BaseOrgParentFilterListLoader.class);
		expect(baseOrgParentFilterListLoader.setClazz(CustomerOrg.class)).andReturn(baseOrgParentFilterListLoader);
		expect(baseOrgParentFilterListLoader.setParent(customerOrg.getInternalOrg())).andReturn(baseOrgParentFilterListLoader);
		expect(baseOrgParentFilterListLoader.load()).andReturn(new FluentArrayList<Listable<Long>>(customerOrg));
		replay(baseOrgParentFilterListLoader);
		return baseOrgParentFilterListLoader;
	}*/
	

	private FilteredListableLoader secondaryOrgLoaderToLoadOrgs(SecondaryOrg ...secondaryOrgs) {
		FilteredListableLoader secondaryOrgLoader = createMock(FilteredListableLoader.class);
		
		expect(secondaryOrgLoader.load()).andReturn(new FluentArrayList<Listable<Long>>(secondaryOrgs));
		replay(secondaryOrgLoader);
		
		return secondaryOrgLoader;
	}
	
	
}
