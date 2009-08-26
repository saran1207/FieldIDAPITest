package com.n4systems.handlers.creator;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.test.helpers.asserts.Asserts;


public class SignUpHandlerTest {
	
	
	private Transaction mockTransaction;

	@Before
	public void setUp() {
		createMockTransaction();
	}
	
	private void createMockTransaction() {
		mockTransaction = createStrictMock(Transaction.class);
		replay(mockTransaction);
	}
	
	private void successfulTransaction(PersistenceProvider mockPersistenceProvider) {
		expect(mockPersistenceProvider.startTransaction()).andReturn(mockTransaction);
		mockPersistenceProvider.finishTransaction(mockTransaction);
	}
	
	

	@Test(expected=InvalidArgumentException.class)
	public void should_require_a_persistence_provider_sign_account_up() {
		SignUpHandler sut = new SignUpHandlerImpl(null, null, null, null);
		sut.signUp(null);
	}
	
	@Test
	public void should_successfully_sign_account_up() {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		
		successfulTransaction(mockPersistenceProvider);
		successfulTransaction(mockPersistenceProvider);
		
		replay(mockPersistenceProvider);
		
		
		TenantSaver mockTenantSaver = createMock(TenantSaver.class);
		mockTenantSaver.save(same(mockTransaction), isA(Tenant.class));
		replay(mockTenantSaver);
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		PrimaryOrgCreateHandler mockOrgCreateHandler = createMock(PrimaryOrgCreateHandler.class);
		expect(mockOrgCreateHandler.forTenant(isA(Tenant.class))).andReturn(mockOrgCreateHandler);
		expect(mockOrgCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockOrgCreateHandler);
		expect(mockOrgCreateHandler.createWithUndoInformation(mockTransaction)).andReturn(null);
		replay(mockOrgCreateHandler);
		
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try {
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest)).andReturn(new SignUpTenantResponseStub());
		} catch (Exception e) {}
		replay(mockSubscriptionAgent);
		
		BaseSystemStructureCreateHandler mockSystemStructureHandler = createMock(BaseSystemStructureCreateHandler.class);
		expect(mockSystemStructureHandler.forTenant(isA(Tenant.class))).andReturn(mockSystemStructureHandler);
		mockSystemStructureHandler.create(mockTransaction);
		replay(mockSystemStructureHandler);
		
		SignUpHandler sut = new SignUpHandlerImpl(mockSystemStructureHandler, mockOrgCreateHandler, mockSubscriptionAgent, mockTenantSaver);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		
		sut.signUp(signUpRequest);
		
		
		verify(mockPersistenceProvider);
		verify(mockTenantSaver);
		verify(mockOrgCreateHandler);
		verify(mockSubscriptionAgent);
		verify(mockSystemStructureHandler);
	}

	
	
	@Test
	public void should_rollback_and_rethrow_exception_sign_when_tenant_can_not_be_created() {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		rollbackTransaction(mockPersistenceProvider);
		replay(mockPersistenceProvider);
		
		RuntimeException fakeConstraintException = new RuntimeException("constraint violation,  tenant_name already used");

		TenantSaver mockTenantSaver = createMock(TenantSaver.class);
		mockTenantSaver.save(same(mockTransaction), isA(Tenant.class));
		expectLastCall().andThrow(fakeConstraintException);
		replay(mockTenantSaver);
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_name_already_used.");
		
		
		SignUpHandler sut = new SignUpHandlerImpl(null, null, null, mockTenantSaver);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		try {
			sut.signUp(signUpRequest);
		} catch (RuntimeException e) {
			assertSame(fakeConstraintException, e);
		}
		
		verify(mockPersistenceProvider);
		verify(mockTenantSaver);
	}

	private void rollbackTransaction(PersistenceProvider mockPersistenceProvider) {
		expect(mockPersistenceProvider.startTransaction()).andReturn(mockTransaction);
		mockPersistenceProvider.rollbackTransaction(mockTransaction);
	}
	
	
	@Test
	public void should_destory_the_tenant_and_primary_org_on_failure_from_subscription_agent() {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		
		successfulTransaction(mockPersistenceProvider);
		
		successfulTransaction(mockPersistenceProvider);
		
		replay(mockPersistenceProvider);
		
		
		TenantSaver mockTenantSaver = createMock(TenantSaver.class);
		mockTenantSaver.save(same(mockTransaction), isA(Tenant.class));
		mockTenantSaver.remove(same(mockTransaction), isA(Tenant.class));
		replay(mockTenantSaver);
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		PrimaryOrgCreateHandler mockOrgCreateHandler = createMock(PrimaryOrgCreateHandler.class);
		expect(mockOrgCreateHandler.forTenant(isA(Tenant.class))).andReturn(mockOrgCreateHandler);
		expect(mockOrgCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockOrgCreateHandler);
		expect(mockOrgCreateHandler.createWithUndoInformation(mockTransaction)).andReturn(null);
		mockOrgCreateHandler.undo(same(mockTransaction), isA(PrimaryOrg.class));
		replay(mockOrgCreateHandler);
		
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try {
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest)).andThrow(new CommunicationException());
		} catch (Exception e) {}
		
		replay(mockSubscriptionAgent);
		
		
		
		SignUpHandler sut = new SignUpHandlerImpl(null, mockOrgCreateHandler, mockSubscriptionAgent, mockTenantSaver);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		boolean exceptionCaught = false;
		
		try {
			sut.signUp(signUpRequest);
		} catch (ProcessFailureException e) {
			exceptionCaught = true;
			
		}
		
		assertTrue(exceptionCaught);
		verify(mockPersistenceProvider);
		verify(mockTenantSaver);
		verify(mockOrgCreateHandler);
		verify(mockSubscriptionAgent);
	}

	
	
}
