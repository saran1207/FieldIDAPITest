package com.n4systems.services;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import org.springframework.context.annotation.Scope;

// NOTE : thread scope is used as opposed to "request" because this bean is also used in background tasks that are not in the middle of a web request.
@Scope("thread")
public class SecurityContext {
	private UserSecurityFilter userSecurityFilter;
	private SecurityFilter tenantSecurityFilter;

	public UserSecurityFilter getUserSecurityFilter() {
		if (userSecurityFilter == null) {
			throw new SecurityException("UserSecurityFilter not set in SecurityContext");
		}
		return userSecurityFilter;
	}

    public UserSecurityFilter getUserSecurityFilterWithArchived() {
        return new UserSecurityFilter(getUserSecurityFilter()).setShowArchived(true);
    }

	public void setUserSecurityFilter(UserSecurityFilter userSecurityFilter) {
		this.userSecurityFilter = userSecurityFilter;
	}

	public boolean hasTenantSecurityFilter() {
		return tenantSecurityFilter != null;
	}

	public SecurityFilter getTenantSecurityFilter() {
		if (!hasTenantSecurityFilter()) {
			throw new SecurityException("TenantSecurityFilter not set in SecurityContext");
		}
		return tenantSecurityFilter;
	}
	
	public SecurityFilter getTenantSecurityFilterWithArchived() {
		return new TenantOnlySecurityFilter(getTenantSecurityFilter()).setShowArchived(true);
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
