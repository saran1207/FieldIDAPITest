package com.n4systems.fieldid.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.services.SecurityContext;
import com.n4systems.util.persistence.QueryBuilder;

public class FieldIdService {

	@Autowired
    protected SecurityContext securityContext;
	
    protected <T> QueryBuilder<T> createUserSecurityBuilder(Class<T> clazz) {
    	return new QueryBuilder<T>(clazz, securityContext.getUserSecurityFilter());
    }
    
    protected <T> QueryBuilder<T> createTenantSecurityBuilder(Class<T> clazz) {
    	return new QueryBuilder<T>(clazz, securityContext.getTenantSecurityFilter());
    }
}
