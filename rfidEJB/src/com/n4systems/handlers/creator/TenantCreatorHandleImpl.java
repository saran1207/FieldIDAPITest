package com.n4systems.handlers.creator;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Tenant;
import com.n4systems.model.signup.AccountCreationInformation;
import com.n4systems.model.tenant.TenantSaver;
import com.n4systems.persistence.Transaction;

//FIXME decide if this is useful.
public class TenantCreatorHandleImpl implements TenantCreateHandler {

	private final TenantSaver tenantSaver;
	
	
	private AccountCreationInformation accountInfo;
	
	
	public TenantCreatorHandleImpl(TenantSaver tenantSaver) {
		super();
		this.tenantSaver = tenantSaver;
	}

	
	public void create(Transaction transaction) {
		if (accountInfo == null) {
			throw new InvalidArgumentException("you must specify an " + AccountCreationInformation.class.getName());
		}
		
		Tenant tenant = new Tenant();
		tenant.setName(accountInfo.getTenantName());
		tenantSaver.save(transaction, tenant);
	}

	public TenantCreateHandler forAccountInfo(AccountCreationInformation accountInfo) {
		this.accountInfo = accountInfo;
		return this;
	}
	
}
