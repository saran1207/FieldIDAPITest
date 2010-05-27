package com.n4systems.taskscheduling.task;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import com.n4systems.model.Tenant;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.signup.SignupReferral;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.testutils.DummySaver;
import com.n4systems.testutils.DummyTransaction;
import com.n4systems.testutils.DummyTransactionManager;

public class ReferralProcessorTaskTest {
	private Tenant referralTenant;
	private Tenant referredTenant;
	private User referralUser;
	private String referralCode;
	
	@Before
	public void setup() {
		referralTenant = TenantBuilder.aTenant().build();
		referredTenant = TenantBuilder.aTenant().build();
		referralUser = UserBuilder.anEmployee().build();
		referralCode = "code";
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void run_saves_within_transaction() {
		DummyTransaction dt = new DummyTransaction();
		TransactionManager tm = createMock(TransactionManager.class);
		Saver<SignupReferral> saver = createMock(Saver.class);
		
		ReferralProcessorTask task = new ReferralProcessorTask(referralTenant, referredTenant, referralCode, saver, tm) {
			@Override
			protected User loadReferralUser(Transaction transaction) {
				return referralUser;
			}
		};
		
		expect(tm.startTransaction()).andReturn(dt);
		
		saver.save(eq(dt), (SignupReferral)anyObject());
		
		tm.finishTransaction(dt);
		
		replay(saver);
		replay(tm);
		
		task.run();
		
		verify(saver);
		verify(tm);
	}
	
	@Test
	public void transaction_rolled_back_when_user_not_found() {
		DummyTransaction dt = new DummyTransaction();
		TransactionManager tm = createMock(TransactionManager.class);
		
		ReferralProcessorTask task = new ReferralProcessorTask(referralTenant, referredTenant, referralCode, new DummySaver<SignupReferral>(), tm) {
			@Override
			protected User loadReferralUser(Transaction transaction) {
				return null;
			}
		};
		
		expect(tm.startTransaction()).andReturn(dt);
		tm.rollbackTransaction(dt);
		
		replay(tm);
		
		task.run();
		
		verify(tm);
	}
	
	@Test
	public void referral_is_built_correctly() {
		Saver<SignupReferral> saver = new Saver<SignupReferral>() {
			@Override
			public void save(Transaction transaction, SignupReferral entity) {
				assertEquals(referralTenant, entity.getReferralTenant());
				assertEquals(referredTenant, entity.getReferredTenant());
				assertEquals(referralUser, entity.getReferralUser());
				assertNotNull(entity.getSignupDate());
			}
		};
		
		ReferralProcessorTask task = new ReferralProcessorTask(referralTenant, referredTenant, referralCode, saver, new DummyTransactionManager()) {
			@Override
			protected User loadReferralUser(Transaction transaction) {
				return referralUser;
			}
		};
		
		task.run();
	}
}
