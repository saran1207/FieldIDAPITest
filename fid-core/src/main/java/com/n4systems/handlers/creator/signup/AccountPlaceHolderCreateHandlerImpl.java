package com.n4systems.handlers.creator.signup;


import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.handlers.creator.signup.model.AccountPlaceHolder;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;
import com.n4systems.security.Permissions;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.UserType;

public class AccountPlaceHolderCreateHandlerImpl implements AccountPlaceHolderCreateHandler {

	
	private final TenantSaver tenantSaver;
	private final PrimaryOrgCreateHandler primaryOrgCreator;
	private final UserSaver userSaver;
	
	private AccountCreationInformation accountInfo;
	
	
	public AccountPlaceHolderCreateHandlerImpl(TenantSaver tenantSaver, PrimaryOrgCreateHandler primaryOrgCreator, UserSaver userSaver) {
		super();
		this.tenantSaver = tenantSaver;
		this.primaryOrgCreator = primaryOrgCreator;
		this.userSaver = userSaver;
	}

	
	public void create(Transaction transaction) {
		createWithUndoInformation(transaction);
	}

	public AccountPlaceHolder createWithUndoInformation(Transaction transaction) {
		if (accountInfo == null) {
			throw new InvalidArgumentException("you must give an account creation info to create.");
		}
		
		Tenant tenant = createDeactivatedTenant(transaction);
		
		PrimaryOrg primaryOrg = createPrimaryOrg(transaction, tenant);
		
		User systemUser = createSystemUser(transaction, primaryOrg);
		User adminUser = createAdminUser(transaction, primaryOrg);
		
		
		return new AccountPlaceHolder(tenant, primaryOrg, systemUser, adminUser);
	}


	private PrimaryOrg createPrimaryOrg(Transaction transaction, Tenant tenant) {
		return primaryOrgCreator.forAccountInfo(accountInfo).forTenant(tenant).createWithUndoInformation(transaction);
	}


	private Tenant createDeactivatedTenant(Transaction transaction) {
		Tenant tenant = new Tenant();
		tenant.setName(accountInfo.getTenantName());
		tenantSaver.save(transaction, tenant);
		return tenant;
	}
	
	private User createAdminUser(Transaction transaction, PrimaryOrg primaryOrg) {
		User user = new User();
		
		setCommonUserFields(primaryOrg, user);
		
		user.setUserType(UserType.ADMIN);
		user.setUserID(accountInfo.getUsername());
		user.setEmailAddress(accountInfo.getEmail());
		user.setFirstName(accountInfo.getFirstName());
		user.setLastName(accountInfo.getLastName());
		user.setTimeZoneID(accountInfo.getFullTimeZone());
		
		userSaver.save(transaction, user);
		
		return user;
	}

	private User createSystemUser(Transaction transaction, PrimaryOrg primaryOrg) {
		User user = new User();
		user.setUserType(UserType.SYSTEM);
		
		setCommonUserFields(primaryOrg, user);
		
		
		user.setTimeZoneID("Canada:Ontario - Toronto"); 
		user.setUserID(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_USERNAME));
		user.setHashPassword(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_PASSWORD));
		user.setEmailAddress(ConfigContext.getCurrentContext().getString(ConfigEntry.SYSTEM_USER_ADDRESS));
		user.setFirstName("N4");
		user.setLastName("Admin");
		
		userSaver.save(transaction, user);
		return user;
	}

	private void setCommonUserFields(PrimaryOrg primaryOrg, User user) {
		user.setRegistered(true);
		user.setTenant(primaryOrg.getTenant());
		user.setOwner(primaryOrg);
		user.setPermissions(Permissions.SYSTEM);
	}

	
	
	public void undo(Transaction transaction, AccountPlaceHolder creationResult) {
		userSaver.remove(transaction, creationResult.getSystemUser());
		userSaver.remove(transaction, creationResult.getAdminUser());
		primaryOrgCreator.undo(transaction, creationResult.getPrimaryOrg());
		tenantSaver.remove(transaction, creationResult.getTenant());
	}
	
	public AccountPlaceHolderCreateHandler forAccountInfo(AccountCreationInformation accountInfo) {
		this.accountInfo = accountInfo;
		return this;
	}

	

}
