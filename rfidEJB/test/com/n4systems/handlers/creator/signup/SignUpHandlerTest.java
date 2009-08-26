package com.n4systems.handlers.creator.signup;

import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.creator.signup.AccountPlaceHolderCreateHandler;
import com.n4systems.handlers.creator.signup.BaseSystemStructureCreateHandler;
import com.n4systems.handlers.creator.signup.SignUpHandler;
import com.n4systems.handlers.creator.signup.SignUpHandlerImpl;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.Subscription;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.test.helpers.asserts.Asserts;
import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.*;

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
		
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();

		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = createMock(AccountPlaceHolderCreateHandler.class);
		expect(mockAccountPlaceHolderCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockAccountPlaceHolderCreateHandler);
		expect(mockAccountPlaceHolderCreateHandler.createWithUndoInformation(mockTransaction)).andReturn(accountPlaceHolder);
		replay(mockAccountPlaceHolderCreateHandler);
		
		SignUpTenantResponseStub subscriptionApproval = new SignUpTenantResponseStub();
		
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try { 
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest))
				.andReturn(subscriptionApproval); 
		} catch (Exception e) {}
		
		replay(mockSubscriptionAgent);
		
		BaseSystemStructureCreateHandler mockSystemStructureHandler = createMock(BaseSystemStructureCreateHandler.class);
		expect(mockSystemStructureHandler.forTenant(isA(Tenant.class))).andReturn(mockSystemStructureHandler);
		mockSystemStructureHandler.create(mockTransaction);
		replay(mockSystemStructureHandler);
		
		SubscriptionApprovalHandler mockSubscriptionApprovalHandler = createMock(SubscriptionApprovalHandler.class);
		expect(mockSubscriptionApprovalHandler.forAccountPlaceHolder(accountPlaceHolder)).andReturn(mockSubscriptionApprovalHandler);
		expect(mockSubscriptionApprovalHandler.forSubscriptionApproval(subscriptionApproval)).andReturn(mockSubscriptionApprovalHandler);
		mockSubscriptionApprovalHandler.applyApproval(mockTransaction);
		replay(mockSubscriptionApprovalHandler);
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, mockSystemStructureHandler, mockSubscriptionAgent, mockSubscriptionApprovalHandler);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		
		sut.signUp(signUpRequest);
		
		
		verify(mockPersistenceProvider);
		verify(mockAccountPlaceHolderCreateHandler);
		verify(mockSubscriptionAgent);
		verify(mockSystemStructureHandler);
		verify(mockSubscriptionApprovalHandler);
	}

	
	
	@Test
	public void should_rollback_and_rethrow_exception_sign_when_tenant_can_not_be_created() {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		rollbackTransaction(mockPersistenceProvider);
		replay(mockPersistenceProvider);
		
		RuntimeException fakeConstraintException = new RuntimeException("constraint violation,  tenant_name already used");

		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_name_already_used.");
		
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = createMock(AccountPlaceHolderCreateHandler.class);
		expect(mockAccountPlaceHolderCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockAccountPlaceHolderCreateHandler);
		expect(mockAccountPlaceHolderCreateHandler.createWithUndoInformation(mockTransaction)).andThrow(fakeConstraintException);
		replay(mockAccountPlaceHolderCreateHandler);
		
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, null, null, null);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		try {
			sut.signUp(signUpRequest);
		} catch (RuntimeException e) {
			assertSame(fakeConstraintException, e);
		}
		
		verify(mockPersistenceProvider);
		verify(mockAccountPlaceHolderCreateHandler);
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
		
		
		
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = createMock(AccountPlaceHolderCreateHandler.class);
		expect(mockAccountPlaceHolderCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockAccountPlaceHolderCreateHandler);
		expect(mockAccountPlaceHolderCreateHandler.createWithUndoInformation(mockTransaction)).andReturn(new AccountPlaceHolder(null,null,null,null));
		mockAccountPlaceHolderCreateHandler.undo(same(mockTransaction), isA(AccountPlaceHolder.class));
		replay(mockAccountPlaceHolderCreateHandler);
		
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try {
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest)).andThrow(new CommunicationException());
		} catch (Exception e) {}
		
		replay(mockSubscriptionAgent);
		
		
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, null, mockSubscriptionAgent, null);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		boolean exceptionCaught = false;
		
		try {
			sut.signUp(signUpRequest);
		} catch (ProcessFailureException e) {
			exceptionCaught = true;
			
		}
		
		assertTrue(exceptionCaught);
		verify(mockPersistenceProvider);
		verify(mockSubscriptionAgent);
		verify(mockAccountPlaceHolderCreateHandler);
	}

	
	
}
