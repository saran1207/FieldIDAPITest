package com.n4systems.services.limiters;

import com.n4systems.model.Tenant;
import com.n4systems.persistence.Transaction;

public interface LimitLoader {
	public void setTenant(Tenant tenant);
	public long getLimit(Transaction transaction);
	public LimitType getType();
}
