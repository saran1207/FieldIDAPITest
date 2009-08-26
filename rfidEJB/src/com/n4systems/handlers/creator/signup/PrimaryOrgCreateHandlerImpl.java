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
		primaryOrg.setSerialNumberFormat(""); //FIXME THIS NEEDS TO BE A GOOD DEFAULT.
		primaryOrg.setDateFormat("MM/dd/yy"); //FIXME THIS NEEDS TO BE A GOOD DEFAULT.
		primaryOrg.setName(accountInfo.getCompanyName());
		primaryOrg.setCertificateName(accountInfo.getCompanyName());
		primaryOrg.setDefaultTimeZone(accountInfo.getFullTimeZone());
		
		applySignUpPackage(primaryOrg);
		applyPromoCodeAdjustments(primaryOrg);
		
		return primaryOrg;
	}
	
	
	private void applySignUpPackage(PrimaryOrg primaryOrg) {
		for (ExtendedFeature feature : accountInfo.getSignUpPackage().getExtendedFeatures()) {
			ExtendedFeatureSwitch featureSwitch = ExtendedFeatureFactory.getSwitchFor(feature, primaryOrg);
			featureSwitch.enableFeature();
		}
		
		TenantLimit limits = new TenantLimit();
		
		limits.setDiskSpaceInBytes(DataUnit.MEGABYTES.convertTo(accountInfo.getSignUpPackage().getDiskSpaceInMB(), DataUnit.BYTES));
		limits.setAssets(accountInfo.getSignUpPackage().getAssets());
		limits.setUsers(accountInfo.getNumberOfUsers().longValue());
		
		primaryOrg.setLimits(limits);
	}
	
	private void applyPromoCodeAdjustments(PrimaryOrg primaryOrg) {
		//TODO  find what we need to do to apply promo code.
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
