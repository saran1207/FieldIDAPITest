package com.n4systems.fieldid.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.services.SecurityContext;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.TimeZone;

public class FieldIdService {

	@Autowired
    protected SecurityContext securityContext;
	
    public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	protected <T> QueryBuilder<T> createUserSecurityBuilder(Class<T> clazz) {
    	return new QueryBuilder<T>(clazz, securityContext.getUserSecurityFilter());
    }
    
    protected <T> QueryBuilder<T> createTenantSecurityBuilder(Class<T> clazz) {
    	return createTenantSecurityBuilder(clazz, false);
    }
    
    protected <T> QueryBuilder<T> createTenantSecurityBuilder(Class<T> clazz, boolean withArchived) {
    	if (withArchived) {
    		return new QueryBuilder<T>(clazz, securityContext.getTenantSecurityFilterWithArchived());
    	} else {
    		return new QueryBuilder<T>(clazz, securityContext.getTenantSecurityFilter());
    	}
    }

    protected TimeZone getUserTimeZone() {
        return securityContext.getUserSecurityFilter().getTimeZone();
    }


}
