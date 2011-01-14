package com.n4systems.fieldid.actions.customers;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

import rfid.web.helper.SessionUser;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.utils.WebSession;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.tools.Pager;
import com.n4systems.tools.SillyPager;

public class CustomerCrudTest {
	
	private UserManager userManager;
	private PersistenceManager persistenceManager;
	private LoaderFactory loaderFactory;
	private CustomerOrgPaginatedLoader loader;
	private CustomerCrud crud;
	private WebSession session;

	@Before
	public void setUp() {
		userManager = createMock(UserManager.class);

		persistenceManager = createMock(PersistenceManager.class);

		session = new WebSession(new MockHttpSession());
		
		SessionUser sessionUser = new SessionUser(UserBuilder.aUser().build());
		session.setSessionUser(sessionUser);

		loaderFactory = createMock(LoaderFactory.class);
		
		loader = createMock(CustomerOrgPaginatedLoader.class);
		
		crud = new CustomerCrud(userManager, persistenceManager) {

			@Override
			public WebSession getSession() {
				return session;
			}

			@Override
			public LoaderFactory getLoaderFactory() {
				return loaderFactory;
			}
		};

	}

	@Test
	public void testGetPageArchived() throws Exception {
		
		crud.setArchivedOnly(true);
		
		CustomerOrg customerOrgToReturn1 = (CustomerOrg)OrgBuilder.aCustomerOrg().withName("CustomerOne").build();
		CustomerOrg customerOrgToReturn2 = (CustomerOrg)OrgBuilder.aCustomerOrg().withName("CustomerTwo").build();

		setUpLoaderExpectations();
		loader.setArchivedOnly(true);
		expect(loader.load()).andReturn(new SillyPager<CustomerOrg>(Arrays.<CustomerOrg>asList(customerOrgToReturn1, customerOrgToReturn2)));
		
		replay(loaderFactory);
		replay(loader);		
		
		Pager<CustomerOrg> page = crud.getPage();

		assertNotNull(page);
		List<CustomerOrg> results = page.getList();
		assertEquals(2, results.size());
		assertEquals("CustomerOne", results.get(0).getName());
		assertEquals("CustomerTwo", results.get(1).getName());
		
		verify(loaderFactory, loader);
	}

	private void setUpLoaderExpectations() {
		expect(loaderFactory.createCustomerOrgPaginatedLoader()).andReturn(loader);
		expect(loader.setPostFetchFields("modifiedBy", "createdBy")).andReturn(loader);
		expect(loader.setPage(1)).andReturn(loader);
		expect(loader.setPageSize(20)).andReturn(loader);
		expect(loader.setNameFilter(null)).andReturn(loader);
	}

}
