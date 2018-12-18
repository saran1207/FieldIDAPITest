package com.n4systems.services;

import com.n4systems.model.security.*;
import org.springframework.context.annotation.Scope;

// NOTE : thread scope is used as opposed to "request" because this bean is also used in background tasks that are not in the middle of a web request.
@Scope("thread")
public class SecurityContext {
	private UserSecurityFilter userSecurityFilter;
	private SecurityFilter tenantSecurityFilter;
	private SecurityFilter secondaryOrgSecurityFilter;

	public UserSecurityFilter getUserSecurityFilter() {
		if (!hasUserSecurityFilter()) {
			throw new SecurityException("UserSecurityFilter not set in SecurityContext");
		}
		return userSecurityFilter;
	}

    public UserSecurityFilter getUserSecurityFilterWithArchived() {
        return new UserSecurityFilter(getUserSecurityFilter()).setShowArchived(true);
    }

	public UserSecurityFilter getUserSecurityFilter(boolean withArchived) {
		return withArchived ? getUserSecurityFilterWithArchived() : getUserSecurityFilter();
	}

	public void setUserSecurityFilter(UserSecurityFilter userSecurityFilter) {
		this.userSecurityFilter = userSecurityFilter;
	}

	public boolean hasTenantSecurityFilter() {
		return tenantSecurityFilter != null;
	}

	public boolean hasUserSecurityFilter() {
		return userSecurityFilter != null;
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

	public SecurityFilter getTenantSecurityFilter(boolean withArchived) {
		return withArchived ? getTenantSecurityFilterWithArchived() : getTenantSecurityFilter();
	}

	public void setTenantSecurityFilter(SecurityFilter tenantSecurityFilter) {
		this.tenantSecurityFilter = tenantSecurityFilter;
	}





	public boolean hasSecondaryOrgSecurityFilter() {
		return secondaryOrgSecurityFilter != null;
	}

	public SecurityFilter getSecondaryOrgSecurityFilter() {
		if (!hasSecondaryOrgSecurityFilter()) {
			throw new SecurityException("SecondaryOrgSecurityFilter not set in SecurityContext");
		}
		return secondaryOrgSecurityFilter;
	}

	public SecurityFilter getSecondaryOrgSecurityFilterWithArchived() {
		return getTenantSecurityFilterWithArchived();
	}

	public SecurityFilter getSecondaryOrgSecurityFilter(boolean withArchived) {
		return withArchived ? getTenantSecurityFilterWithArchived() : getSecondaryOrgSecurityFilter();
	}

	public void setSecondaryOrgSecurityFilter(SecurityFilter secondaryOrgSecurityFilter) {
		this.secondaryOrgSecurityFilter = secondaryOrgSecurityFilter;
	}





	public void set(SecurityContext securityContext) { 
		userSecurityFilter = securityContext.getUserSecurityFilter();
		tenantSecurityFilter = securityContext.getTenantSecurityFilter();
		secondaryOrgSecurityFilter = securityContext.getSecondaryOrgSecurityFilter();
	}
	
	public void reset() {
		userSecurityFilter = null;
		tenantSecurityFilter = null;
		secondaryOrgSecurityFilter = null;
	}
	

}
