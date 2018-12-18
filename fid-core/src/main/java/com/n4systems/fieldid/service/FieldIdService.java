package com.n4systems.fieldid.service;

import com.n4systems.services.SecurityContext;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TimeZone;

public class FieldIdService {

	@Autowired
    protected SecurityContext securityContext;
	
    public void setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
	}

	protected <T> QueryBuilder<T> createUserSecurityBuilder(Class<T> clazz) {
    	return createUserSecurityBuilder(clazz, false);
    }
	
	protected <T> QueryBuilder<T> createUserSecurityBuilder(Class<T> clazz, boolean withArchived) {
        if(withArchived) {
            return new QueryBuilder<T>(clazz, securityContext.getUserSecurityFilterWithArchived());
        } else {
            return new QueryBuilder<T>(clazz, securityContext.getUserSecurityFilter());
        }
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

    protected <T> QueryBuilder<T> createSecondaryOrgSecurityBuilder(Class<T> clazz) {
        return createSecondaryOrgSecurityBuilder(clazz, false);
    }

    protected <T> QueryBuilder<T> createSecondaryOrgSecurityBuilder(Class<T> clazz, boolean withArchived) {
        if (withArchived) {
            securityContext.setSecondaryOrgSecurityFilter(securityContext.getTenantSecurityFilterWithArchived());
            return new QueryBuilder<T>(clazz, securityContext.getSecondaryOrgSecurityFilterWithArchived());
        } else {
            securityContext.setSecondaryOrgSecurityFilter(securityContext.getTenantSecurityFilter());
            return new QueryBuilder<T>(clazz, securityContext.getSecondaryOrgSecurityFilter());
        }
    }

    protected TimeZone getUserTimeZone() {
        return securityContext.getUserSecurityFilter().getTimeZone();
    }


}
