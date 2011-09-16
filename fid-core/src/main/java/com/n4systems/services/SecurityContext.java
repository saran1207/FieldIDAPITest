package com.n4systems.services;

import org.springframework.context.annotation.Scope;

import com.n4systems.model.security.SecurityFilter;

// NOTE : thread scope is used as opposed to "request" because this bean is also used in background tasks that are not in the middle of a web request.
@Scope("thread")
public class SecurityContext {
	private SecurityFilter userSecurityFilter;
	private SecurityFilter tenantSecurityFilter;

	public SecurityFilter getUserSecurityFilter() {
		if (userSecurityFilter == null) {
			throw new SecurityException("UserSecurityFilter not set in SecurityContext");
		}
		return userSecurityFilter;
	}

	public void setUserSecurityFilter(SecurityFilter userSecurityFilter) {
		this.userSecurityFilter = userSecurityFilter;
	}

	public SecurityFilter getTenantSecurityFilter() {
		if (tenantSecurityFilter == null) {
			throw new SecurityException("TenantSecurityFilter not set in SecurityContext");
		}
		return tenantSecurityFilter;
	}

	public void setTenantSecurityFilter(SecurityFilter tenantSecurityFilter) {
		this.tenantSecurityFilter = tenantSecurityFilter;
	}

	public void set(SecurityContext securityContext) { 
		userSecurityFilter = securityContext.getUserSecurityFilter();
		tenantSecurityFilter = securityContext.getTenantSecurityFilter();
	}
	
	public void reset() {
		userSecurityFilter = null;
		tenantSecurityFilter = null;
	}
	

}
