package com.n4systems.model.orgs;

import static com.n4systems.model.builders.CustomerOrgBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;


public class FindOrCreateCustomerOrgHandlerTest {

	private static Saver<? super CustomerOrg> SAVER_THAT_WILL_FAIL_IF_CALLED = null;
	
	@Test
	public void should_not_find_the_cusomter() throws Exception {
		ListLoader<CustomerOrg> customerLoader = createCustomerLoaderForList(new ArrayList<CustomerOrg>());
		
		FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> sut = createFindOrCreateCustomerHandlerThatWillNotFindACustomer(customerLoader, SAVER_THAT_WILL_FAIL_IF_CALLED);
		
		assertNull(sut.find(aPrimaryOrg().build(), "SOME NAME", "SOME CODE"));
		assertFalse(sut.orgWasCreated());
		verify(customerLoader);
	}
	
	
	@Test
	public void should_find_the_customer() throws Exception {
		ListLoader<CustomerOrg> customerLoader = createCustomerLoaderForList(new ArrayList<CustomerOrg>());
		
		FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> sut = createFindOrCreateCustomerHandlerThatWillFindACustomer(customerLoader, SAVER_THAT_WILL_FAIL_IF_CALLED);
		
		assertNotNull(sut.find(aPrimaryOrg().build(), "SOME NAME", "SOME CODE"));
		assertFalse(sut.orgWasCreated());
		verify(customerLoader);
	}


	
	
	@Test
	public void should_find_the_customer_and_not_try_to_create() throws Exception {
		ListLoader<CustomerOrg> customerLoader = createCustomerLoaderForList(new ArrayList<CustomerOrg>());
		
		FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> sut = createFindOrCreateCustomerHandlerThatWillFindACustomer(customerLoader, SAVER_THAT_WILL_FAIL_IF_CALLED);
		
		assertNotNull(sut.findOrCreate(aPrimaryOrg().build(), "SOME NAME", "SOME CODE"));
		assertFalse(sut.orgWasCreated());
		verify(customerLoader);
	}


	
	@Test
	public void should_not_find_customer_and_create_a_new_one() throws Exception {
		PrimaryOrg primaryOrg = aPrimaryOrg().build();

		ListLoader<CustomerOrg> customerLoader = createCustomerLoaderForList(new ArrayList<CustomerOrg>());
		
		OrgSaver saver = createMock(OrgSaver.class);
		saver.save((CustomerOrg)anyObject());
		replay(saver);
		
		FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> sut = createFindOrCreateCustomerHandlerThatWillNotFindACustomer(customerLoader, saver);
		
		CustomerOrg actualCustomer = sut.findOrCreate(primaryOrg, "SOME NAME", "SOME CODE");
		
		assertTrue(sut.orgWasCreated());
		assertCustomerCreateCorrectly(primaryOrg, actualCustomer, "SOME NAME", "SOME CODE");
		
		verify(customerLoader);
		verify(saver);
	}


	private void assertCustomerCreateCorrectly(PrimaryOrg primaryOrg, CustomerOrg createdCustomer, String name, String code) {
		assertEquals(createdCustomer.getName(), name);
		assertEquals(createdCustomer.getCode(), code);
		assertEquals(createdCustomer.getParent(), primaryOrg);
		assertEquals(createdCustomer.getTenant(), primaryOrg.getTenant());
	}
	

	@SuppressWarnings("unchecked")
	private ListLoader<CustomerOrg> createCustomerLoaderForList(ArrayList<CustomerOrg> listOfCustomers) {
		ListLoader<CustomerOrg> customerLoader = createMock(ListLoader.class);
		expect(customerLoader.load()).andReturn(listOfCustomers);
		expectLastCall().atLeastOnce();
		replay(customerLoader);
		return customerLoader;
	}
	
	private FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> createFindOrCreateCustomerHandlerThatWillFindACustomer(ListLoader<CustomerOrg> customerLoader, Saver<? super CustomerOrg> saver) {
		return new FindOrCreateCustomerOrgHandler(customerLoader, saver) {
			@Override
			protected ExternalOrgFuzzyFinder<CustomerOrg> getFuzzyMatcher() {
				return new ExternalOrgFuzzyMatcherSpy<CustomerOrg>(aPrimaryCustomerOrg().build());
			}
			
		};
	}
	
	private FindOrCreateExternalOrgHandler<CustomerOrg, PrimaryOrg> createFindOrCreateCustomerHandlerThatWillNotFindACustomer(ListLoader<CustomerOrg> customerLoader, Saver<? super CustomerOrg> saver) {
		return new FindOrCreateCustomerOrgHandler(customerLoader, saver) {
			@Override
			protected ExternalOrgFuzzyFinder<CustomerOrg> getFuzzyMatcher() {
				return new ExternalOrgFuzzyMatcherSpy<CustomerOrg>(null);
			}
		};
	}
	
	private class ExternalOrgFuzzyMatcherSpy<T extends ExternalOrg> extends ExternalOrgFuzzyFinder<T> {
		private T extenalOrg;
		private String code;
		
		private String name;
		
		public ExternalOrgFuzzyMatcherSpy(T extenalOrg) {
			this.extenalOrg = extenalOrg;
		}
		
		@Override
		public T find(String code, String name, List<T> orgs) {
			this.code = code;
			this.name = name;
			return extenalOrg;
		}
		
		public String getCode() {
			return code;
		}


		public String getName() {
			return name;
		}
	}

}
