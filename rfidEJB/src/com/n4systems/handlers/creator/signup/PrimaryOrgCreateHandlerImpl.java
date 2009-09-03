package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.DataUnit;

public class PrimaryOrgCreateHandlerImpl implements PrimaryOrgCreateHandler {
	private static final String DEFAULT_DATE_FORMAT = "MM/dd/yy";

	private static final String DEFAULT_SERIAL_NUMBER_FORMAT = "";

	private final OrganizationSaver orgSaver;
	
	private AccountCreationInformation accountInfo;
	private Tenant tenant;

	
	public PrimaryOrgCreateHandlerImpl(OrganizationSaver orgSaver) {
		super();
		this.orgSaver = orgSaver;
	}

	public void create(Transaction transaction) {
		createWithUndoInformation(transaction);
	}
	
	public PrimaryOrg createWithUndoInformation(Transaction transaction) {
		guards();
		
		PrimaryOrg primaryOrg = createPrimaryOrg();
		
		orgSaver.save(transaction, primaryOrg);
		return primaryOrg;
	}
	


	
	private PrimaryOrg createPrimaryOrg() {
		PrimaryOrg primaryOrg = new PrimaryOrg();
		primaryOrg.setTenant(tenant);
		primaryOrg.setUsingSerialNumber(true);
		primaryOrg.setSerialNumberFormat(DEFAULT_SERIAL_NUMBER_FORMAT);
		primaryOrg.setDateFormat(DEFAULT_DATE_FORMAT);
		primaryOrg.setName(accountInfo.getCompanyName());
		primaryOrg.setCertificateName(accountInfo.getCompanyName());
		primaryOrg.setDefaultTimeZone(accountInfo.getFullTimeZone());
		
		
		return primaryOrg;
	}
	
	


	private void guards() {
		if (accountInfo == null) {
			throw new InvalidArgumentException("you must specify an " + AccountCreationInformation.class.getName());
		}
		
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}
		
	}

	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}
	
	
	public void undo(Transaction transaction, PrimaryOrg primaryOrgToRemove) {
		orgSaver.remove(transaction, primaryOrgToRemove);
	}
	
	public PrimaryOrgCreateHandler forAccountInfo(AccountCreationInformation accountInfo) {
		this.accountInfo = accountInfo;
		return this;
	}


	public PrimaryOrgCreateHandler forTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
