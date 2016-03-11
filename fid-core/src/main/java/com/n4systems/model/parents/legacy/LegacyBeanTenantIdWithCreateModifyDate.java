package com.n4systems.model.parents.legacy;

import com.n4systems.model.api.HasTenantId;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class LegacyBeanTenantIdWithCreateModifyDate extends LegacyEntityCreateModifyDate implements HasTenantId {
	public static final String columnName = "r_tenant";
	
	@Column(name = columnName)
	private Long tenantId;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
