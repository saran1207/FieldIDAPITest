package com.n4systems.handlers.creator.signup;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.junit.Test;

import com.n4systems.model.Tenant;
import com.n4systems.model.builders.TenantBuilder;
import com.n4systems.taskscheduling.task.ReferralProcessorTask;
import com.n4systems.testutils.UsesDummyPersistenceManager;

public class SignupReferralHandlerImplTest extends UsesDummyPersistenceManager {
	
	@Test
	public void process_referral_does_nothing_on_null_referral_code() {
		Executor exec = createMock(Executor.class);
		replay(exec);
		
		SignupReferralHandlerImpl refHandler = new SignupReferralHandlerImpl(exec);
		refHandler.processReferral(new Tenant(), new Tenant(), null);
		verify(exec);
	}
	
	@Test
	public void process_referral_calls_execute() {
		Executor exec = createMock(Executor.class);
		exec.execute((Runnable)anyObject());
		replay(exec);
		
		SignupReferralHandlerImpl refHandler = new SignupReferralHandlerImpl(exec);
		refHandler.processReferral(new Tenant(), new Tenant(), "");
		verify(exec);
	}
	
	@Test
	public void handles_rejected_executions() {
		SignupReferralHandlerImpl refHandler = new SignupReferralHandlerImpl(new Executor() {
			@Override
			public void execute(Runnable command) {
				throw new RejectedExecutionException();
			}
		});
		
		refHandler.processReferral(new Tenant(), new Tenant(), "code");
	}
	
	@Test
	public void executes_referral_processor() {
		final Tenant referralTenant = TenantBuilder.aTenant().build();
		final Tenant referredTenant = TenantBuilder.aTenant().build();
		final String refCode = "code";
		
		SignupReferralHandlerImpl refHandler = new SignupReferralHandlerImpl(new Executor() {
			@Override
			public void execute(Runnable command) {
				assertTrue(command instanceof ReferralProcessorTask);
				
				ReferralProcessorTask refTask = (ReferralProcessorTask)command;
				assertSame(referralTenant, refTask.getReferralTenant());
				assertSame(referredTenant, refTask.getReferredTenant());
				assertSame(refCode, refTask.getReferralCode());
			}
		});
		
		
		refHandler.processReferral(referralTenant, referredTenant, refCode);
	}
}
