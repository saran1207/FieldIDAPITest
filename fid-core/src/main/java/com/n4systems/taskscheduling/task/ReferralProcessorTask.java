package com.n4systems.taskscheduling.task;

import com.n4systems.model.Tenant;
import com.n4systems.model.signup.SignupReferral;
import com.n4systems.model.signup.SignupReferralSaver;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByReferralCodeLoader;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.persistence.savers.Saver;
import org.apache.log4j.Logger;

import java.util.Date;

public class ReferralProcessorTask implements Runnable {
	private static final Logger logger = Logger.getLogger(ReferralProcessorTask.class);
	
	private final Saver<SignupReferral> referralSaver;
	private final TransactionManager transactionManager;
	private final Tenant referralTenant;
	private final Tenant referredTenant;
	private final String referralCode;
	
	public ReferralProcessorTask(Tenant referralTenant, Tenant referredTenant, String referralCode) {
		this(referralTenant, referredTenant, referralCode, new SignupReferralSaver(), new FieldIdTransactionManager());
	}
	
	public ReferralProcessorTask(Tenant referralTenant, Tenant referredTenant, String referralCode, Saver<SignupReferral> referralSaver, TransactionManager transactionManager) {
		this.referralTenant = referralTenant;
		this.referredTenant = referredTenant;
		this.referralCode = referralCode;
		this.referralSaver = referralSaver;
		this.transactionManager = transactionManager;
	}
	
	@Override
	public void run() {
		Transaction transaction = null;
		try {
			transaction = transactionManager.startTransaction();
			
			User referralUser = loadReferralUser(transaction);
			
			SignupReferral referral = createSignupReferral(referralUser);
			
			referralSaver.save(transaction, referral);
			
			transactionManager.finishTransaction(transaction);
		} catch(Exception e) {
			logger.error(String.format("Failed processing referral for Referral Tenant [%s], Referred Tenant [%s], code [%s]", referralTenant.getName(), referredTenant.getName(), referralCode), e);
			transactionManager.rollbackTransaction(transaction);
		}
	}

	protected User loadReferralUser(Transaction transaction) {
		UserByReferralCodeLoader referralCodeLoader = new UserByReferralCodeLoader();
		referralCodeLoader.setTenant(referralTenant);
		referralCodeLoader.setReferralCode(referralCode);
		return referralCodeLoader.load(transaction);
	}
	
	private SignupReferral createSignupReferral(User referralUser) throws NullReferralUserException {
		if (referralUser == null) {
			throw new NullReferralUserException();
		}
		
		SignupReferral referral = new SignupReferral();
		referral.setSignupDate(new Date());
		referral.setReferralTenant(referralTenant);
		referral.setReferredTenant(referredTenant);
		referral.setReferralUser(referralUser);
		return referral;
	}
	
	public Tenant getReferralTenant() {
		return referralTenant;
	}

	public Tenant getReferredTenant() {
		return referredTenant;
	}

	public String getReferralCode() {
		return referralCode;
	}

	@SuppressWarnings("serial")
	private class NullReferralUserException extends Exception {}
}
