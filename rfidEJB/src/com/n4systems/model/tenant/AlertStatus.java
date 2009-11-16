package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.api.HasTenantId;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.services.limiters.LimitType;

@Entity
@Table(name = "alertstatus")
public class AlertStatus implements HasTenantId, Saveable, Serializable {
	private static final long serialVersionUID = 1L;
	public static final int NORMAL_STATUS = 0;
	
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("tenantId", null, null, null);
	}
	
	@Id
	@Column(name="tenant_id")
	private Long tenantId;
	
	@Column(name="diskspace", nullable=false)
	private int diskSpace = NORMAL_STATUS;

	@Column(name="assets", nullable=false)
	private int assets = NORMAL_STATUS;
	
	@Column(nullable=false)
	private int secondaryOrgs = NORMAL_STATUS;
	
	
	public AlertStatus() {}

	public boolean isNew() {
		return (tenantId == null);
	}
	public Object getIdentifier() {
		return getTenantId();
	}
	
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long r_tenant) {
		this.tenantId = r_tenant;
	}
	
	public boolean isLevelAtNormal(LimitType type) {
		return (alertLevel(type) == NORMAL_STATUS);
	}
	
	public boolean isLevelAtThreshold(LimitType type, int threshold) {
		return (alertLevel(type) == threshold);
	}
	
	public int alertLevel(LimitType type) {
		switch (type) {
			case DISK_SPACE:
				return diskSpace;
			case ASSETS:
				return assets;
			case SECONDARY_ORGS: 
				return secondaryOrgs;
			default:
				return NORMAL_STATUS;
		}
	}
	
	public void setLevel(LimitType type, int level) {
		switch (type) {
			case DISK_SPACE:
				diskSpace = level;
				break;
			case ASSETS:
				assets = level;
				break;
			case SECONDARY_ORGS:
				secondaryOrgs = level;
				break;
			default:
				throw new InvalidArgumentException("you have passed an un known type to set limit. [" + type.toString() + "]");
		}
	}
	
	public void clearStatus(LimitType type) {
		switch (type) {
			case DISK_SPACE:
				diskSpace = NORMAL_STATUS;
				break;
			case ASSETS:
				assets = NORMAL_STATUS;
				break;
			case SECONDARY_ORGS:
				secondaryOrgs = NORMAL_STATUS;
				break;
			default:
				throw new InvalidArgumentException("you have passed an un known type to clear status. [" + type.toString() + "]");
		}
	}
}
