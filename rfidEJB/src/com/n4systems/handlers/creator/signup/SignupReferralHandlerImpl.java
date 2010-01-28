package com.n4systems.handlers.creator.signup;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.apache.log4j.Logger;

import com.n4systems.model.Tenant;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ReferralProcessorTask;

public class SignupReferralHandlerImpl implements SignupReferralHandler {
	private static final Logger logger = Logger.getLogger(SignupReferralHandlerImpl.class);
	
	private final Executor taskExecutor; 

	public SignupReferralHandlerImpl() {
		this(TaskExecutor.getInstance());
	}
	
	public SignupReferralHandlerImpl(Executor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	@Override
	public void processReferral(Tenant referralTenant, Tenant referredTenant, String referralCode) {
		if (referralCode == null) {
			logger.info(String.format("Signup Tenant [%s], Referral Tenant [%s] had no referral code", referredTenant.getName(), referralTenant.getName()));
			return;
		}
		
		try {
			taskExecutor.execute(new ReferralProcessorTask(referralTenant, referredTenant, referralCode));
		} catch(RejectedExecutionException e) {
			logger.error(String.format("Failed to execute ReferralProcessorTask.  Signup Tenant [%s], Referral Tenant [%s], Ref Code [%s]", referredTenant.getName(), referralTenant.getName(), referralCode), e);
		}
	}

}
