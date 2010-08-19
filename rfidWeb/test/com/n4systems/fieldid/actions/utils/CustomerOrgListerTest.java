package com.n4systems.fieldid.actions.utils;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.api.Listable;
import com.n4systems.model.builders.CustomerOrgBuilder;
import com.n4systems.model.builders.DivisionOrgBuilder;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.util.persistence.SimpleListable;



public class CustomerOrgListerTest {
	
	private PrimaryOrg primaryOrg;
	
	@Before
	public void setUp() {
		primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().build();
	}
	
	@Test
	public void should_include_list_of_customers_with_filters_ALL() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.ALL, null);
		assertTrue(sut.includeCustomerList());
	}
	
	@Test
	public void should_not_include_list_of_customers_when_filter_type_INTERNAL() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.INTERNAL, null);
		
		assertFalse(sut.includeCustomerList());
	}
	
	@Test
	public void should_not_include_list_of_customers_when_filter_type_PRIMARY() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.PRIMARY, null);
		assertFalse(sut.includeCustomerList());
	}
	
	@Test
	public void should_include_list_of_customers_when_filter_type_NON_PRIMARY() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.NON_PRIMARY, null);
		assertTrue(sut.includeCustomerList());
	}
	
	@Test
	public void should_not_include_list_of_customers_when_filter_type_SECONDARY() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.SECONDARY, null);
		assertFalse(sut.includeCustomerList());
	}
	
	@Test
	public void should_include_list_of_customers_with_filters_CUSTOMER() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.CUSTOMER, null);
		assertTrue(sut.includeCustomerList());
	}
	
	@Test
	public void should_include_list_of_customers_with_filters_EXTERNAL() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.EXTERNAL, null);
		assertTrue(sut.includeCustomerList());
	}
	
	
	@Test
	public void should_include_blank_customer_with_filters_ALL() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.ALL, null);
		
		assertTrue(sut.includeBlankInList(primaryOrg));
	}
	
	@Test
	public void should_include_not_blank_customer_with_filters_NON_PRIMARY_and_selected_owner_is_under_the_primary() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.NON_PRIMARY, null);
		DivisionOrg divisionOwner = DivisionOrgBuilder.aPrimaryDivisionOrg().build();
		
		assertFalse(sut.includeBlankInList(primaryOrg));
		
		assertFalse(sut.includeBlankInList(divisionOwner));
	}
	@Test
	public void should_include_blank_customer_with_filters_NON_PRIMARY_and_selected_owner_is_not_under_the_primary() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.NON_PRIMARY, null);
		CustomerOrg customerOwner = CustomerOrgBuilder.aSecondaryCustomerOrg().build();
		assertTrue(sut.includeBlankInList(customerOwner));
	}
	
	@Test
	public void should_not_include_blank_customer_when_filter_type_INTERNAL() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.INTERNAL, null);
		
		assertFalse(sut.includeBlankInList(primaryOrg));
	}
	
	@Test
	public void should_not_include_blank_customer_when_filter_type_PRIMARY() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.PRIMARY, null);
		assertFalse(sut.includeBlankInList(primaryOrg));
	}
	
	@Test
	public void should_not_include_blank_customer_when_filter_type_SECONDARY() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.SECONDARY, null);
		assertFalse(sut.includeBlankInList(primaryOrg));
	}
	
	@Test
	public void should_not_include_blank_customer_with_filters_CUSTOMER() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.CUSTOMER, null);
		assertFalse(sut.includeBlankInList(primaryOrg));
	}
	
	@Test
	public void should_not_include_blank_customer_with_filters_EXTERNAL() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.EXTERNAL, null);
		assertFalse(sut.includeBlankInList(primaryOrg));
	}
	
	@Test
	public void should_not_include_blank_customer_with_filters_NON_PRIMARY_and_the() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.EXTERNAL, null);
		assertFalse(sut.includeBlankInList(primaryOrg));
	}
	
	
	@Test
	public void should_create_a_empty_list() throws Exception {
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.PRIMARY, null);
		List<Listable<Long>> expectedList = new ArrayList<Listable<Long>>();
		
		List<Listable<Long>> actualList = sut.getCustomerList(null);
		
		assertArrayEquals(expectedList.toArray(), actualList.toArray());
	}
	
	@Test
	public void should_create_a_customer_list_with_a_blank_at_the_top() throws Exception {
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().withName("customer").build();
		
		BaseOrgParentFilterListLoader baseOrgParentFilterListLoader = customerLoader(customerOrg);
		
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.ALL, baseOrgParentFilterListLoader);
		
		List<Listable<Long>> expectedList = new FluentArrayList<Listable<Long>>()
				.stickOn(new SimpleListable<Long>(-1L, ""))
				.stickOn(customerOrg);
		
		List<Listable<Long>> actualList = sut.getCustomerList(customerOrg);
		
		assertArrayEquals(expectedList.toArray(), actualList.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_create_a_customer_list_with_out_a_blank_at_the_top() throws Exception {
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().withName("customer").build();
		
		BaseOrgParentFilterListLoader baseOrgParentFilterListLoader = customerLoader(customerOrg);
		
		CustomerOrgLister sut = new CustomerOrgLister(OrgType.CUSTOMER, baseOrgParentFilterListLoader);
		
		List<Listable<Long>> expectedList = new FluentArrayList<Listable<Long>>(customerOrg);
		
		List<Listable<Long>> actualList = sut.getCustomerList(customerOrg);
		
		assertArrayEquals(expectedList.toArray(), actualList.toArray());
	}
	

	@SuppressWarnings("unchecked")
	private BaseOrgParentFilterListLoader customerLoader(CustomerOrg customerOrg) {
		BaseOrgParentFilterListLoader baseOrgParentFilterListLoader = createMock(BaseOrgParentFilterListLoader.class);
		expect(baseOrgParentFilterListLoader.setClazz(CustomerOrg.class)).andReturn(baseOrgParentFilterListLoader);
		expect(baseOrgParentFilterListLoader.setParent(customerOrg.getInternalOrg())).andReturn(baseOrgParentFilterListLoader);
		expect(baseOrgParentFilterListLoader.load()).andReturn(new FluentArrayList<Listable<Long>>(customerOrg));
		replay(baseOrgParentFilterListLoader);
		return baseOrgParentFilterListLoader;
	}
	
	
}
