package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.HasTenantId;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.services.limiters.LimitType;

@Entity
@Table(name = "alertstatus")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
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
	
	public int getDiskSpace() {
		return diskSpace;
	}

	public void setDiskSpace(int diskSpace) {
		this.diskSpace = diskSpace;
	}
	
	public int getAssets() {
		return assets;
	}

	public void setAssets(int assets) {
		this.assets = assets;
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
		}
	}
}
