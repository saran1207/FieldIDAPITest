package com.n4systems.fieldid.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class FieldIdService {

	@Autowired
    protected SecurityFilter userSecurityFilter;
	
	@Autowired
    protected SecurityFilter tenantSecurityFilter;

	public void setUserSecurityFilter(SecurityFilter userSecurityFilter) {
		this.userSecurityFilter = userSecurityFilter;
	}
	
	public void setTenantSecurityFilter(SecurityFilter tenantSecurityFilter) {
		this.tenantSecurityFilter = tenantSecurityFilter;
	}
	
    protected <T> QueryBuilder<T> createUserSecurityBuilder(Class<T> clazz) {
    	return new QueryBuilder<T>(clazz, userSecurityFilter);
    }
    
    protected <T> QueryBuilder<T> createTenantSecurityBuilder(Class<T> clazz) {
    	return new QueryBuilder<T>(clazz, tenantSecurityFilter);
    }
}
