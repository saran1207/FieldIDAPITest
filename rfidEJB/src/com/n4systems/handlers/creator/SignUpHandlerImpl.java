package com.n4systems.handlers.creator;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.persistence.PersistenceProvider;
import com.n4systems.persistence.Transaction;
import com.n4systems.subscription.BillingInfoException;
import com.n4systems.subscription.CommunicationException;
import com.n4systems.subscription.SubscriptionAgent;

public class SignUpHandlerImpl implements SignUpHandler {

	private final BaseSystemStructureCreateHandler baseSystemCreator;
	private final PrimaryOrgCreateHandler orgCreateHandler;
	private final SubscriptionAgent subscriptionAgent;
	private final TenantSaver tenantSaver;
	
	private PersistenceProvider persistenceProvider;
	private SignUpRequest signUp;
	
	public SignUpHandlerImpl(BaseSystemStructureCreateHandler baseSystemCreator, PrimaryOrgCreateHandler orgCreateHandler, SubscriptionAgent subscriptionAgent, TenantSaver tenantSaver) {
		super();
		this.baseSystemCreator = baseSystemCreator;
		this.orgCreateHandler = orgCreateHandler;
		this.subscriptionAgent = subscriptionAgent;
		this.tenantSaver = tenantSaver;
	}

	public void signUp(SignUpRequest signUp) {
		if (persistenceProvider == null) {
			throw new InvalidArgumentException("You must give a persistence provider");
		}
		this.signUp = signUp;
		
		Tenant tenant = null;
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			tenant = createAccountPlaceHolder(signUp, transaction);
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw e;
		}
		
		
		try {
			subscriptionAgent.buy(signUp, signUp, signUp);
		} catch (CommunicationException e) {
			undoAccountPlaceHolder(tenant);
			throw new ProcessFailureException("could not complete process", e);
		} catch (BillingInfoException e) {
			undoAccountPlaceHolder(tenant);
			throw new ProcessFailureException("could not complete process", e);
		}
		
		transaction = persistenceProvider.startTransaction();
		try {
			
			baseSystemCreator.forTenant(tenant).create(transaction);
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw e;
		}
	}

	private Tenant createAccountPlaceHolder(SignUpRequest signUp, Transaction transaction) {
		Tenant tenant;
		tenant = createTenant(transaction);
		orgCreateHandler.forTenant(tenant).forAccountInfo(signUp).createWithUndoInformation(transaction);
		return tenant;
	}

	private void undoAccountPlaceHolder(Tenant tenant) {
		Transaction transaction = persistenceProvider.startTransaction();
		try {
			tenantSaver.remove(transaction, tenant);
			orgCreateHandler.undo(transaction, new PrimaryOrg());
			persistenceProvider.finishTransaction(transaction);
		} catch (RuntimeException e) {
			persistenceProvider.rollbackTransaction(transaction);
			throw e;
		}
	}
	
	private Tenant createTenant(Transaction transaction) {
		
		Tenant tenant = new Tenant();
		tenant.setName(signUp.getTenantName());
		tenantSaver.save(transaction, tenant);
		return tenant;
	}
	
	

	public SignUpHandler withPersistenceProvider(PersistenceProvider persistenceProvider) {
		this.persistenceProvider = persistenceProvider;
		return this;
	}
	
	
	
}
