package com.n4systems.api.conversion.users;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.UserView;
import com.n4systems.api.model.UserViewBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.GlobalIdLoader;
import com.n4systems.testutils.DummyTransaction;


public class UserToModelConverterTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_add() throws ConversionException {
		Transaction trans = new DummyTransaction();
		GlobalIdLoader<User> globalIdLoader = createMock(GlobalIdLoader.class);
		OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);
		
		UserView user = new UserViewBuilder().withDefaultValues().withGuid(null).build();
		
		UserToModelConverter converter = new UserToModelConverter(globalIdLoader, orgLoader);

		BaseOrg parentOrg = new PrimaryOrg();
		parentOrg.setName("resultOf<N4>search");
			
		expect(orgLoader.setOrganizationName(user.getOrganization())).andReturn(orgLoader);
		expect(orgLoader.setCustomerName(user.getCustomer())).andReturn(orgLoader);
		expect(orgLoader.setDivision(user.getDivision())).andReturn(orgLoader);
		expect(orgLoader.load()).andReturn(parentOrg);
		
		replay(globalIdLoader);
		replay(orgLoader);
		
		User model = converter.toModel(user, trans);
		
		verifyModel(model, user, parentOrg);
		
		verify(globalIdLoader);
		verify(orgLoader);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_to_model_update() throws ConversionException {
		Transaction trans = new DummyTransaction();
		GlobalIdLoader<User> globalIdLoader = createMock(GlobalIdLoader.class);
		OrgByNameLoader orgLoader = createMock(OrgByNameLoader.class);

		String password = "somePassword";
		// leave GUID on so will try to find & update user 
		UserView user = new UserViewBuilder().withDefaultValues().withAssignPassword("N").withPassword(null).build();
		User userToBeUpdated = UserBuilder.aFullUser().withFirstName("OLD"+user.getFirstName()).withLastName("OLD"+user.getLastName()).withPassword(password).build();

		UserToModelConverter converter = new UserToModelConverter(globalIdLoader, orgLoader);

		BaseOrg parentOrg = new PrimaryOrg();
		parentOrg.setName("resultOf<N4>search");
			
		expect(orgLoader.setOrganizationName(user.getOrganization())).andReturn(orgLoader);
		expect(orgLoader.setCustomerName(user.getCustomer())).andReturn(orgLoader);
		expect(orgLoader.setDivision(user.getDivision())).andReturn(orgLoader);
		expect(orgLoader.load()).andReturn(parentOrg);
		expect(globalIdLoader.setGlobalId(user.getGlobalId())).andReturn(globalIdLoader);
		expect(globalIdLoader.load(trans)).andReturn(userToBeUpdated);
		
		replay(globalIdLoader);
		replay(orgLoader);
		
		User model = converter.toModel(user, trans);

		assertFalse(model.getFirstName().startsWith("OLD"));
		assertFalse(model.getLastName().startsWith("OLD"));
		assertSame(user.getFirstName(), model.getFirstName() );
		assertSame(user.getLastName(), model.getLastName() );
		assertNotNull(model.getHashPassword());  // should still exist from loaded user. 
		
		verify(globalIdLoader);
		verify(orgLoader);
	}	

	private void verifyModel(User model, UserView view, BaseOrg parentOrg) {
		assertNotNull(model);
		assertEquals(model.getPermissions(), view.getPermissions());
		assertEquals(model.getFirstName(), view.getFirstName());
		assertEquals(model.getLastName(), view.getLastName());
		assertEquals(model.getEmailAddress(), view.getEmailAddress());
		assertEquals(model.getUserID(), view.getUserID());
		assertEquals(model.getGlobalId(), view.getGlobalId());
		assertEquals(model.getOwner().getDisplayName(), parentOrg.getDisplayName());
	}
}
