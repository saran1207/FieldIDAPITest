package com.n4systems.commandprocessors;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.n4systems.testutils.NoCommitAndRollBackTransaction;
import com.n4systems.testutils.TestDoubleNonSecuredLoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.NonDataSourceBackedConfigContext;



public class CreateSafetyNetworkConnectionCommandProcessorTest {

	@SuppressWarnings("unchecked")
	@Test public void should_create_new_connection() {
		PrimaryOrg customer = aPrimaryOrg().build();
		PrimaryOrg vendor = aPrimaryOrg().build();
		
		UserBean user = anEmployee().withOwner(customer).build();
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor);
		
		ConfigContext.setCurrentContext(new NonDataSourceBackedConfigContext());
		
		NonSecureIdLoader<BaseOrg> mockLoader = createMock(NonSecureIdLoader.class);
		expect(mockLoader.setId(customer.getId())).andReturn(mockLoader);
		expect(mockLoader.load((Transaction)anyObject())).andReturn(customer);
		expect(mockLoader.setId(vendor.getId())).andReturn(mockLoader);
		expect(mockLoader.load((Transaction)anyObject())).andReturn(vendor);
		replay(mockLoader);
		
		OrgConnectionExistsLoader mockConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, false);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.add(BaseOrg.class, mockLoader);
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(ConfigContext.getCurrentContext());
		
		sut.setActor(user).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(command, new NoCommitAndRollBackTransaction());
		
		verify(mockLoader);
		verify(mockConnectionExistsLoader);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test public void should_not_create_new_connection_if_one_already_exists() {
		PrimaryOrg customer = aPrimaryOrg().build();
		PrimaryOrg vendor = aPrimaryOrg().build();
		
		UserBean user = anEmployee().withOwner(customer).build();
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor);
		
		ConfigContext.setCurrentContext(new NonDataSourceBackedConfigContext());
		
		NonSecureIdLoader<BaseOrg> mockLoader = createMock(NonSecureIdLoader.class);
		replay(mockLoader);
		
		OrgConnectionExistsLoader mockConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, true);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.add(BaseOrg.class, mockLoader);
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(ConfigContext.getCurrentContext());
		
		sut.setActor(user).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(command, new NoCommitAndRollBackTransaction());
		
		verify(mockLoader);
		verify(mockConnectionExistsLoader);
	}

	private CreateSafetyNetworkConnectionMessageCommand createCommand(PrimaryOrg customer, PrimaryOrg vendor) {
		CreateSafetyNetworkConnectionMessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
		command.setCustomerOrgId(customer.getId());
		command.setVendorOrgId(vendor.getId());
		return command;
	}
	
	@Test
	public void should_be_a_valid_command() throws Exception {
		PrimaryOrg customer = aPrimaryOrg().build();
		PrimaryOrg vendor = aPrimaryOrg().build();
		
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor);
		
		OrgConnectionExistsLoader mockOrgConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, false);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockOrgConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(new NonDataSourceBackedConfigContext());
		sut.setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		assertTrue(sut.isCommandStillValid(command));
		
		verify(mockOrgConnectionExistsLoader);
		
	}
	
	
	@Test
	public void should_be_not_a_valid_command() throws Exception {
		PrimaryOrg customer = aPrimaryOrg().build();
		PrimaryOrg vendor = aPrimaryOrg().build();
		
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor);
		
		OrgConnectionExistsLoader mockOrgConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, true);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockOrgConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(new NonDataSourceBackedConfigContext());
		sut.setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		assertFalse(sut.isCommandStillValid(command));
	}

	private OrgConnectionExistsLoader createMockConnectionExistsLoaderWithResponse(CreateSafetyNetworkConnectionMessageCommand command, Boolean response) {
		OrgConnectionExistsLoader mockOrgConnectionExistsLoader = createMock(OrgConnectionExistsLoader.class);
		expect(mockOrgConnectionExistsLoader.setCustomerId(command.getCustomerOrgId())).andReturn(mockOrgConnectionExistsLoader);
		expect(mockOrgConnectionExistsLoader.setVendorId(command.getVendorOrgId())).andReturn(mockOrgConnectionExistsLoader);
		expect(mockOrgConnectionExistsLoader.load((Transaction) anyObject())).andReturn(response);
		replay(mockOrgConnectionExistsLoader);
		return mockOrgConnectionExistsLoader;
	}

}
