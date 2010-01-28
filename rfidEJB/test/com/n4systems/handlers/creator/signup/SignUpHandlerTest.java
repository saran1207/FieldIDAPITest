package com.n4systems.handlers.creator.signup;

import static com.n4systems.handlers.creator.signup.model.builder.AccountPlaceHolderBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.ejb.MailManager;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.exceptions.BillingValidationException;
import com.n4systems.handlers.creator.signup.exceptions.CommunicationErrorException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpCompletionException;
import com.n4systems.handlers.creator.signup.exceptions.SignUpSoftFailureException;
import com.n4systems.handlers.creator.signup.exceptions.TenantNameUsedException;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;
import com.n4systems.testutils.BaseTest;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.util.mail.MailMessage;

public class SignUpHandlerTest extends BaseTest {

	protected Transaction mockTransaction;
	
	@Override
	public void before() {
		mockTransaction = createMock(FieldIdTransaction.class);
		replay(mockTransaction);
	}
	
	private void successfulTransaction(PersistenceProvider mockPersistenceProvider) {
		expect(mockPersistenceProvider.startTransaction()).andReturn(mockTransaction);
		mockPersistenceProvider.finishTransaction(mockTransaction);
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_require_a_persistence_provider_sign_account_up() throws Exception {
		SignUpHandler sut = new SignUpHandlerImpl(null, null, null, null, null, null);
		sut.signUp(null, null, null, null);
	}
	
	@Test
	public void should_successfully_sign_account_up() throws Exception {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		
		successfulTransaction(mockPersistenceProvider);
		successfulTransaction(mockPersistenceProvider);
		
		replay(mockPersistenceProvider);
		
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = successfulAccountPlaceHolderCreation(signUpRequest, accountPlaceHolder);
		
		
		PrimaryOrg referrerOrg = (PrimaryOrg)OrgBuilder.aPrimaryOrg().build();
		
		SignUpTenantResponseStub subscriptionApproval = new SignUpTenantResponseStub();
		SubscriptionAgent mockSubscriptionAgent = successfulSubscriptionPurchase(signUpRequest, subscriptionApproval);
		
		BaseSystemStructureCreateHandler mockSystemStructureHandler = successfulBaseSystemCreation();
		
		SignUpFinalizationHandler mockSignUpFinalizationHandler = createMock(SignUpFinalizationHandler.class);
		expect(mockSignUpFinalizationHandler.setAccountInformation(signUpRequest)).andReturn(mockSignUpFinalizationHandler);
		expect(mockSignUpFinalizationHandler.setAccountPlaceHolder(accountPlaceHolder)).andReturn(mockSignUpFinalizationHandler);
		expect(mockSignUpFinalizationHandler.setSubscriptionApproval(subscriptionApproval)).andReturn(mockSignUpFinalizationHandler);
		expect(mockSignUpFinalizationHandler.setReferrerOrg(referrerOrg)).andReturn(mockSignUpFinalizationHandler);
		mockSignUpFinalizationHandler.finalizeSignUp(mockTransaction);
		replay(mockSignUpFinalizationHandler);
		
		MailManager mailManager = createMock(MailManager.class);
		mailManager.sendMessage((MailMessage)anyObject());
		replay(mailManager);
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, mockSystemStructureHandler, mockSubscriptionAgent, mockSignUpFinalizationHandler, null, mailManager);
		sut.withPersistenceProvider(mockPersistenceProvider);

		sut.signUp(signUpRequest, referrerOrg, null, null);
		
		verify(mockPersistenceProvider);
		verify(mockAccountPlaceHolderCreateHandler);
		verify(mockSubscriptionAgent);
		verify(mockSystemStructureHandler);
		verify(mockSignUpFinalizationHandler);
		verify(mailManager);
	}

	private BaseSystemStructureCreateHandler successfulBaseSystemCreation() {
		BaseSystemStructureCreateHandler mockSystemStructureHandler = createMock(BaseSystemStructureCreateHandler.class);
		expect(mockSystemStructureHandler.forTenant(isA(Tenant.class))).andReturn(mockSystemStructureHandler);
		mockSystemStructureHandler.create(mockTransaction);
		replay(mockSystemStructureHandler);
		return mockSystemStructureHandler;
	}

	private SubscriptionAgent successfulSubscriptionPurchase(SignUpRequest signUpRequest, SignUpTenantResponseStub subscriptionApproval) {
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try { 
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest))
				.andReturn(subscriptionApproval); 
		} catch (Exception e) {}
		
		replay(mockSubscriptionAgent);
		return mockSubscriptionAgent;
	}

	private AccountPlaceHolderCreateHandler successfulAccountPlaceHolderCreation(SignUpRequest signUpRequest, AccountPlaceHolder accountPlaceHolder) {
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = createMock(AccountPlaceHolderCreateHandler.class);
		expect(mockAccountPlaceHolderCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockAccountPlaceHolderCreateHandler);
		expect(mockAccountPlaceHolderCreateHandler.createWithUndoInformation(mockTransaction)).andReturn(accountPlaceHolder);
		replay(mockAccountPlaceHolderCreateHandler);
		return mockAccountPlaceHolderCreateHandler;
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
		
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, null, null, null, null, null);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		boolean exceptionCaught = false;
		
		try {
			sut.signUp(signUpRequest, null, null, null);
		} catch (SignUpCompletionException e) {
			fail("exception thrown");
		} catch (TenantNameUsedException e) {
			exceptionCaught = true;
		}
		
		assertTrue(exceptionCaught);
		verify(mockPersistenceProvider);
		verify(mockAccountPlaceHolderCreateHandler);
	}

	private void rollbackTransaction(PersistenceProvider mockPersistenceProvider) {
		expect(mockPersistenceProvider.startTransaction()).andReturn(mockTransaction);
		mockPersistenceProvider.rollbackTransaction(mockTransaction);
	}
	
	
	@Test
	public void should_destory_the_tenant_and_primary_org_on_communication_failure_from_subscription_agent() {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		
		successfulTransaction(mockPersistenceProvider);
		
		successfulTransaction(mockPersistenceProvider);
		
		replay(mockPersistenceProvider);
		
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = accountPlaceHolderCreateThenDestroy(signUpRequest);
		
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try {
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest)).andThrow(new CommunicationException());
		} catch (Exception e) {}
		
		replay(mockSubscriptionAgent);
		
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, null, mockSubscriptionAgent, null, null, null);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		boolean exceptionCaught = false;
		
		try {
			sut.signUp(signUpRequest, null, null, null);
		} catch (SignUpCompletionException e) {
			fail("exception thrown");
		} catch (CommunicationErrorException e) {
			exceptionCaught = true;
		}
		
		assertTrue(exceptionCaught);
		verify(mockPersistenceProvider);
		verify(mockSubscriptionAgent);
		verify(mockAccountPlaceHolderCreateHandler);
	}

	private AccountPlaceHolderCreateHandler accountPlaceHolderCreateThenDestroy(SignUpRequest signUpRequest) {
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = createMock(AccountPlaceHolderCreateHandler.class);
		
		expect(mockAccountPlaceHolderCreateHandler.forAccountInfo(signUpRequest)).andReturn(mockAccountPlaceHolderCreateHandler);
		expect(mockAccountPlaceHolderCreateHandler.createWithUndoInformation(mockTransaction)).andReturn(new AccountPlaceHolder(aTenant().build(), aPrimaryOrg().build(), aUser().build(), aUser().build()));
		
		
		mockAccountPlaceHolderCreateHandler.undo(same(mockTransaction), isA(AccountPlaceHolder.class));
		
		replay(mockAccountPlaceHolderCreateHandler);
		
		return mockAccountPlaceHolderCreateHandler;
	}

	
	@Test
	public void should_destory_the_tenant_and_primary_org_on_billing_failure_from_subscription_agent() {
		
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		
		successfulTransaction(mockPersistenceProvider);
		successfulTransaction(mockPersistenceProvider);
		
		replay(mockPersistenceProvider);
		
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = accountPlaceHolderCreateThenDestroy(signUpRequest);
		
		SubscriptionAgent mockSubscriptionAgent = createMock(SubscriptionAgent.class);
		try {
			expect(mockSubscriptionAgent.buy(signUpRequest, signUpRequest, signUpRequest)).andThrow(new BillingInfoException());
		} catch (Exception e) {}
		
		replay(mockSubscriptionAgent);
		
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, null, mockSubscriptionAgent, null, null, null);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		boolean exceptionCaught = false;
		
		try {
			sut.signUp(signUpRequest, null, null, null);
		} catch (SignUpCompletionException e) {
			fail("exception thrown");
		} catch (BillingValidationException e) {
			exceptionCaught = true;
		}
		
		assertTrue(exceptionCaught);
		verify(mockPersistenceProvider);
		verify(mockSubscriptionAgent);
		verify(mockAccountPlaceHolderCreateHandler);
	}
	
	
	
	@Test
	public void should_throw_sign_up_completion_exception_after_any_exception_in_sign_up_completion() {
	
		PersistenceProvider mockPersistenceProvider = createMock(PersistenceProvider.class);
		
		successfulTransaction(mockPersistenceProvider);
		rollbackTransaction(mockPersistenceProvider);
		
		replay(mockPersistenceProvider);
		
		
		SignUpRequest signUpRequest = new SignUpRequest();
		signUpRequest.setTenantName("some_tenant_name");
		
		AccountPlaceHolder accountPlaceHolder = anAccountPlaceHolder().build();
		AccountPlaceHolderCreateHandler mockAccountPlaceHolderCreateHandler = successfulAccountPlaceHolderCreation(signUpRequest, accountPlaceHolder);
		
		SignUpTenantResponseStub subscriptionApproval = new SignUpTenantResponseStub();
		SubscriptionAgent mockSubscriptionAgent = successfulSubscriptionPurchase(signUpRequest, subscriptionApproval);
		
		BaseSystemStructureCreateHandler mockBaseSystemStructureCreateHandler = successfulBaseSystemCreation();
		
		PrimaryOrg referrerOrg = (PrimaryOrg)OrgBuilder.aPrimaryOrg().build();
		
		SignUpFinalizationHandler mockSignUpFinalizationHandler = createMock(SignUpFinalizationHandler.class);
		expect(mockSignUpFinalizationHandler.setAccountInformation(signUpRequest)).andReturn(mockSignUpFinalizationHandler);
		expect(mockSignUpFinalizationHandler.setAccountPlaceHolder(isA(AccountPlaceHolder.class))).andReturn(mockSignUpFinalizationHandler);
		expect(mockSignUpFinalizationHandler.setSubscriptionApproval(subscriptionApproval)).andReturn(mockSignUpFinalizationHandler);
		expect(mockSignUpFinalizationHandler.setReferrerOrg(referrerOrg)).andReturn(mockSignUpFinalizationHandler);
		mockSignUpFinalizationHandler.finalizeSignUp(mockTransaction);
		expectLastCall().andThrow(new RuntimeException("some exception"));
		
		replay(mockSignUpFinalizationHandler);
		
		SignUpHandler sut = new SignUpHandlerImpl(mockAccountPlaceHolderCreateHandler, mockBaseSystemStructureCreateHandler, mockSubscriptionAgent, mockSignUpFinalizationHandler, null, null);
		sut.withPersistenceProvider(mockPersistenceProvider);
		
		boolean exceptionCaught = false;
		
		try {
			sut.signUp(signUpRequest, referrerOrg, null, null);
		} catch (SignUpCompletionException e) {
			exceptionCaught = true;
		}
		
		assertTrue(exceptionCaught);
		verify(mockPersistenceProvider);
		verify(mockSubscriptionAgent);
		verify(mockBaseSystemStructureCreateHandler);
		verify(mockSignUpFinalizationHandler);
		verify(mockAccountPlaceHolderCreateHandler);
	}
	
	@Test
	public void calls_referral_processor_after_signup() throws SignUpSoftFailureException, SignUpCompletionException {
		final String refCode = "refcode";
		final Tenant referredTenant = TenantBuilder.aTenant().build();
		PrimaryOrg refferralOrg = OrgBuilder.aPrimaryOrg().buildPrimary();
		
		SignupReferralHandler referralHandler = createMock(SignupReferralHandler.class);
		SignUpHandler signupHandler = new SignUpHandlerImpl(null, null, null, null, referralHandler, null) {
			@Override
			protected void activateAccount(SignUpRequest signUp, PrimaryOrg referrerOrg, String portalUrl) throws SignUpCompletionException {}
			@Override
			protected void confirmSubscription(SignUpRequest signUp) {}
			@Override
			protected void guard() {}
			@Override
			protected void holdNamesForSignUp(SignUpRequest signUp) {
				placeHolder = new AccountPlaceHolder(referredTenant, null, null, null);
			}
		};

		referralHandler.processReferral(eq(refferralOrg.getTenant()), eq(referredTenant), eq(refCode));
		replay(referralHandler);
		
		signupHandler.withPersistenceProvider(new PersistenceProvider() {
			@Override
			public void finishTransaction(Transaction transaction) {}
			@Override
			public void rollbackTransaction(Transaction transaction) {}
			@Override
			public Transaction startTransaction() {
				return new DummyTransaction();
			}
		});
		
		signupHandler.signUp(new SignUpRequest(), refferralOrg, "", refCode);
		
		verify(referralHandler);
	}
}
