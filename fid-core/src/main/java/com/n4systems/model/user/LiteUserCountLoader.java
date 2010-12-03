/**
 * 
 */
package com.n4systems.model.user;

import javax.persistence.EntityManager;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.services.limiters.LimitLoader;
import com.n4systems.services.limiters.LimitType;

public class LiteUserCountLoader extends Loader<Long> implements LimitLoader {
	private Long tenantId;
	
	public LiteUserCountLoader() {}
	
	@Override
	protected Long load(EntityManager em) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLimit(Transaction transaction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LimitType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTenant(Tenant tenant) {
		// TODO Auto-generated method stub
		
	}

	public LiteUserCountLoader setTenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}

}
