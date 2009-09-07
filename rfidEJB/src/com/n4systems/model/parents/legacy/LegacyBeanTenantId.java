package com.n4systems.model.parents.legacy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.api.HasTenantId;
import com.n4systems.model.security.SecurityDefiner;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class LegacyBeanTenantId extends LegacyBaseEntity implements HasTenantId {
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenantId", null, null, null);
	}
	
	@Column(name = "tenant_id")
	private Long tenantId;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
