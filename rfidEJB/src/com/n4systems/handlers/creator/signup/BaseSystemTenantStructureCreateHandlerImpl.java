package com.n4systems.handlers.creator.signup;



import rfid.ejb.entity.SerialNumberCounterBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.serialnumbercounter.SerialNumberCounterSaver;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.tenant.SetupDataLastModDatesSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.DateHelper;

public class BaseSystemTenantStructureCreateHandlerImpl implements BaseSystemTenantStructureCreateHandler {
	private static final long DEFAULT_COUNTER_START_VALUE = 1L;
	private static final long YEAR_IN_DAYS = 365L;
	
	private final SetupDataLastModDatesSaver lastModDateSaver;
	private final SerialNumberCounterSaver serialNumberCountSaver;
	
	private Tenant tenant;
	
	public BaseSystemTenantStructureCreateHandlerImpl(SetupDataLastModDatesSaver lastModDateSaver, SerialNumberCounterSaver serialNumberCountSaver) {
		this.lastModDateSaver = lastModDateSaver;
		this.serialNumberCountSaver = serialNumberCountSaver;
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
		SerialNumberCounterBean serialNumberCounter = new SerialNumberCounterBean();
		
		serialNumberCounter.setTenant(tenant);
		serialNumberCounter.setCounter(DEFAULT_COUNTER_START_VALUE);
		serialNumberCounter.setDecimalFormat("000000");
		serialNumberCounter.setDaysToReset(YEAR_IN_DAYS);
		serialNumberCounter.setLastReset(DateHelper.getFirstDayOfThisYear());
		
		serialNumberCountSaver.save(transaction, serialNumberCounter);
	}
	
	
	public BaseSystemTenantStructureCreateHandler forTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
