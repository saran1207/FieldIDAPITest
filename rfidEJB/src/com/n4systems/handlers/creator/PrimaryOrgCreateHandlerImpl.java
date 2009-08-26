package com.n4systems.handlers.creator;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.OrganizationSaver;
import com.n4systems.model.tenant.TenantLimit;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureFactory;
import com.n4systems.model.tenant.extendedfeatures.ExtendedFeatureSwitch;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DataUnit;

public class PrimaryOrgCreateHandlerImpl implements PrimaryOrgCreateHandler {
	private final OrganizationSaver orgSaver;
	private final UserSaver userSaver;
	
	private AccountCreationInformation accountInfo;
	private Tenant tenant;

	
	public PrimaryOrgCreateHandlerImpl(OrganizationSaver orgSaver, UserSaver userSaver) {
		super();
		this.orgSaver = orgSaver;
		this.userSaver = userSaver;
	}

	public void create(Transaction transaction) {
		createWithUndoInformation(transaction);
	}
	
	public PrimaryOrg createWithUndoInformation(Transaction transaction) {
		guards();
		
		PrimaryOrg primaryOrg = createPrimaryOrg();
		
		orgSaver.save(transaction, primaryOrg);
		userSaver.save(transaction, createSystemUser(primaryOrg));
		userSaver.save(transaction, createAdminUser(primaryOrg));
		return primaryOrg;
		
		
	}
	


	private UserBean createAdminUser(PrimaryOrg primaryOrg) {
		UserBean user = new UserBean();
		
		setCommonUserFields(primaryOrg, user);
		
		user.setAdmin(true);
		user.setUserID(accountInfo.getUsername());
		user.assignPassword(accountInfo.getPassword());
		user.setEmailAddress(accountInfo.getEmail());
		user.setFirstName(accountInfo.getFirstName());
		user.setLastName(accountInfo.getLastName());
		user.setTimeZoneID(accountInfo.getFullTimeZone());
		
		return user;
	}

	private UserBean createSystemUser(PrimaryOrg primaryOrg) {
		UserBean user = new UserBean();
		user.setSystem(true);
		
		setCommonUserFields(primaryOrg, user);
		
		user.setTimeZoneID("Canada:Ontario - Toronto"); 
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");
		return user;
	}

	private void setCommonUserFields(PrimaryOrg primaryOrg, UserBean user) {
		user.setActive(true);
		user.setTenant(tenant);
		user.setOrganization(primaryOrg);
		user.setPermissions(Permissions.SYSTEM);
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
		
		processSignUpPackage(primaryOrg);
		
		return primaryOrg;
	}
	
	
	private void processSignUpPackage(PrimaryOrg primaryOrg) {
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
