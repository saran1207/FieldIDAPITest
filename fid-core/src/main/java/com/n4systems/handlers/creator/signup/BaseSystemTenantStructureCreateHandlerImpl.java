package com.n4systems.handlers.creator.signup;



import com.n4systems.model.serialnumbercounter.IdentifierCounterSaver;
import rfid.ejb.entity.IdentifierCounterBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.DateHelper;

public class BaseSystemTenantStructureCreateHandlerImpl implements BaseSystemTenantStructureCreateHandler {
	private static final long DEFAULT_COUNTER_START_VALUE = 1L;
	private static final long YEAR_IN_DAYS = 365L;
	
	private final SetupDataLastModDatesSaver lastModDateSaver;
	private final IdentifierCounterSaver identifierCountSaver;
	
	private Tenant tenant;
	
	public BaseSystemTenantStructureCreateHandlerImpl(SetupDataLastModDatesSaver lastModDateSaver, IdentifierCounterSaver identifierCountSaver) {
		this.lastModDateSaver = lastModDateSaver;
		this.identifierCountSaver = identifierCountSaver;
	}
	

	public void create(Transaction transaction) {
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}
		
		createLastModDates(transaction);
		createDefaultSerialNumberCounter(transaction);
	}
	
	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}
	
	private void createLastModDates(Transaction transaction) {
		SetupDataLastModDates setupModDates = new SetupDataLastModDates(tenant);
		lastModDateSaver.save(transaction, setupModDates);
	}
	
	private void createDefaultSerialNumberCounter(Transaction transaction) {
		IdentifierCounterBean identifierCounter = new IdentifierCounterBean();
		
		identifierCounter.setTenant(tenant);
		identifierCounter.setCounter(DEFAULT_COUNTER_START_VALUE);
		identifierCounter.setDecimalFormat("000000");
		identifierCounter.setDaysToReset(YEAR_IN_DAYS);
		identifierCounter.setLastReset(DateHelper.getFirstDayOfThisYear());
		
		identifierCountSaver.save(transaction, identifierCounter);
	}
	
	
	public BaseSystemTenantStructureCreateHandler forTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
