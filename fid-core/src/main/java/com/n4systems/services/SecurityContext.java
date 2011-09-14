package com.n4systems.services;

import org.springframework.context.annotation.Scope;

import com.n4systems.model.security.SecurityFilter;

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

	public void clear() {
		userSecurityFilter = null;
		tenantSecurityFilter = null;
	}
	
}
