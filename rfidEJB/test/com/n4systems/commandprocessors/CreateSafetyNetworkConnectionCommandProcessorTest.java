package com.n4systems.commandprocessors;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.n4systems.model.messages.CreateSafetyNetworkConnectionMessageCommand;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnectionExistsLoader;
import com.n4systems.model.security.SafetyNetworkSecurityCache;
import com.n4systems.model.user.User;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.NullNotifier;
import com.n4systems.notifiers.TestSingleNotifier;
import com.n4systems.notifiers.notifications.ConnectionInvitationAcceptedNotification;
import com.n4systems.notifiers.notifications.Notification;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.n4systems.testutils.NoCommitAndRollBackTransaction;
import com.n4systems.testutils.TestDoubleNonSecuredLoaderFactory;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.NonDataSourceBackedConfigContext;



public class CreateSafetyNetworkConnectionCommandProcessorTest {

	private PrimaryOrg customer;
	private PrimaryOrg vendor;
	private User userInTheVendorTenant;
	private User userInTheCustomerTenant;
	
	
	@Before
	public void initSafetyNetworkCache() {
		SafetyNetworkSecurityCache.initialize();
	}
	
	@After
	public void cleanUpSafetyNetworkCache() {
		SafetyNetworkSecurityCache.initialize();
	}
	
	@Before
	public void setup() {
		customer = aPrimaryOrg().build();
		vendor = aPrimaryOrg().build();
		userInTheVendorTenant = anEmployee().withOwner(vendor).build();
		userInTheCustomerTenant = anEmployee().withOwner(customer).build();
	}
	
	
	@Test 
	public void should_create_new_connection() {
		
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor, userInTheVendorTenant);
		
		
		NonSecureIdLoader<PrimaryOrg> mockLoader = createSuccessfulIdLoader();
		
		OrgConnectionExistsLoader mockConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, false);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = overrideNonSecuredLoaderFactory(mockLoader, mockConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(getConfigContext(), new NullNotifier());
		
		sut.setActor(userInTheCustomerTenant).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(command, new NoCommitAndRollBackTransaction());
		
		verify(mockLoader);
		verify(mockConnectionExistsLoader);
		
	}

	
	@SuppressWarnings("unchecked")
	@Test 
	public void should_not_create_new_connection_if_one_already_exists() {
		
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor, userInTheVendorTenant);
		
		ConfigContext.setCurrentContext(getConfigContext());
		
		NonSecureIdLoader<PrimaryOrg> mockLoader = createMock(NonSecureIdLoader.class);
		replay(mockLoader);
		
		OrgConnectionExistsLoader mockConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, true);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = overrideNonSecuredLoaderFactory(mockLoader, mockConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(ConfigContext.getCurrentContext(), null);
		
		sut.setActor(userInTheCustomerTenant).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(command, new NoCommitAndRollBackTransaction());
		
		verify(mockLoader);
		verify(mockConnectionExistsLoader);
	}

	private CreateSafetyNetworkConnectionMessageCommand createCommand(PrimaryOrg customer, PrimaryOrg vendor, User creator) {
		CreateSafetyNetworkConnectionMessageCommand command = new CreateSafetyNetworkConnectionMessageCommand();
		command.setCustomerOrgId(customer.getId());
		command.setVendorOrgId(vendor.getId());
		command.setCreatedBy(creator);
		return command;
	}
	
	@Test
	public void should_be_a_valid_command() throws Exception {
		
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor, userInTheVendorTenant);
		
		OrgConnectionExistsLoader mockOrgConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, false);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockOrgConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(getConfigContext(), null);
		sut.setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		assertTrue(sut.isCommandStillValid(command));
		
		verify(mockOrgConnectionExistsLoader);
	}
	
	
	@Test
	public void should_be_not_a_valid_command() throws Exception {
		
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor, userInTheVendorTenant);
		
		OrgConnectionExistsLoader mockOrgConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, true);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockOrgConnectionExistsLoader);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(getConfigContext(), null);
		sut.setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		assertFalse(sut.isCommandStillValid(command));
	}


	private NonDataSourceBackedConfigContext getConfigContext() {
		return new NonDataSourceBackedConfigContext();
	}
	
	
	@Test
	public void should_call_notifier_after_successful_creation_of_the_connection() throws Exception {
		CreateSafetyNetworkConnectionMessageCommand command = createCommand(customer, vendor, userInTheVendorTenant);
		
		NonSecureIdLoader<PrimaryOrg> mockLoader = createSuccessfulIdLoader();
		
		OrgConnectionExistsLoader mockConnectionExistsLoader = createMockConnectionExistsLoaderWithResponse(command, false);
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = overrideNonSecuredLoaderFactory(mockLoader, mockConnectionExistsLoader);
		
		
		Notifier notifier = EasyMock.createMock(Notifier.class);
		expect(notifier.notify((Notification) anyObject())).andReturn(true);
		replay(notifier);
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(getConfigContext(), notifier);
		sut.setActor(userInTheCustomerTenant).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(command, new NoCommitAndRollBackTransaction());
		
		
		verify(notifier);
	}
	
	
	@Test
	public void should_call_notifier_with_a_correct_producer() throws Exception {
		
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = overrideNonSecuredLoaderFactory(createSuccessfulIdLoader(), 
							createMockConnectionExistsLoaderWithResponse(createCommand(customer, vendor, userInTheVendorTenant), false));
		
		TestSingleNotifier notifier = new TestSingleNotifier();
		
		CreateSafetyNetworkConnectionCommandProcessor sut = new CreateSafetyNetworkConnectionCommandProcessor(getConfigContext(), notifier);
		sut.setActor(userInTheCustomerTenant).setNonSecureLoaderFactory(nonSecureLoaderFactory);
		
		sut.process(createCommand(customer, vendor, userInTheVendorTenant), new NoCommitAndRollBackTransaction());
		
		
		ConnectionInvitationAcceptedNotification expectedNotification = new ConnectionInvitationAcceptedNotification();
		expectedNotification.notifiyUser(userInTheVendorTenant);
		expectedNotification.setAcceptingCompanyName(customer.getDisplayName());
		assertEquals(expectedNotification, notifier.notification);
	}

	
	

	private TestDoubleNonSecuredLoaderFactory overrideNonSecuredLoaderFactory(NonSecureIdLoader<PrimaryOrg> mockLoader, OrgConnectionExistsLoader mockConnectionExistsLoader) {
		TestDoubleNonSecuredLoaderFactory nonSecureLoaderFactory = new TestDoubleNonSecuredLoaderFactory();
		nonSecureLoaderFactory.add(PrimaryOrg.class, mockLoader);
		nonSecureLoaderFactory.setOrgConnectionExistsLoader(mockConnectionExistsLoader);
		return nonSecureLoaderFactory;
	}


	@SuppressWarnings("unchecked")
	private NonSecureIdLoader<PrimaryOrg> createSuccessfulIdLoader() {
		NonSecureIdLoader<PrimaryOrg> mockLoader = createMock(NonSecureIdLoader.class);
		expect(mockLoader.setId(customer.getId())).andReturn(mockLoader);
		expect(mockLoader.load((Transaction)anyObject())).andReturn(customer);
		expect(mockLoader.setId(vendor.getId())).andReturn(mockLoader);
		expect(mockLoader.load((Transaction)anyObject())).andReturn(vendor);
		replay(mockLoader);
		return mockLoader;
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
