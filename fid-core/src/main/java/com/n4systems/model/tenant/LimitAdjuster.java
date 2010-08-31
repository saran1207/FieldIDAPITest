package com.n4systems.model.tenant;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.n4systems.util.DataUnit;

@Embeddable
public class LimitAdjuster {

	@Column(name="diskspace_limit", nullable = false)
	private Long diskSpaceInBytes;
	
	@Column(name="asset_limit", nullable = false)
	private Long assets;
	
	@Column(name="secondary_org_limit", nullable = false)
	private Long secondaryOrgs;

	
	public LimitAdjuster() {
		this(0L, 0L, 0L);
	}

	public LimitAdjuster(Long assets, Long secondaryOrgs, Long diskSpaceInBytes) {
		this(assets, secondaryOrgs, diskSpaceInBytes, DataUnit.BYTES);
		
	}
	
	public LimitAdjuster(Long assets, Long secondaryOrgs, Long diskSpaceInOtherUnit, DataUnit diskSpaceUnit) {
		super();
		this.assets = assets;
		this.secondaryOrgs = secondaryOrgs;
		
		setDiskSpace(diskSpaceInOtherUnit, diskSpaceUnit);
	
	}
	
	public Long getDiskSpaceInBytes() {
		return diskSpaceInBytes;
	}
	
	public boolean isDiskSpaceUnlimited() {
		return diskSpaceInBytes.equals(TenantLimit.UNLIMITED);
	}

	public void setDiskSpaceUnlimited() {
		this.diskSpaceInBytes = TenantLimit.UNLIMITED;
	}
	
	private void setDiskSpace(Long diskSpaceInOtherUnit, DataUnit diskSpaceUnit) {
		if (diskSpaceInOtherUnit.equals(TenantLimit.UNLIMITED)) {
			this.diskSpaceInBytes = diskSpaceInOtherUnit;
		} else {
			this.diskSpaceInBytes = diskSpaceUnit.convertTo(diskSpaceInOtherUnit, DataUnit.BYTES);
		}
	}
	
	public void setDiskSpaceInBytes(Long diskSpaceInBytes) {
		this.diskSpaceInBytes = diskSpaceInBytes;
	}

	public Long getAssets() {
		return assets;
	}

	public boolean isAssetsUnlimited() {
		return assets.equals(TenantLimit.UNLIMITED);
	}
	
	public void setAssetsUnlimited() {
		this.assets = TenantLimit.UNLIMITED;
	}
	
	public void setAssets(Long assets) {
		this.assets = assets;
	}
	
	public TenantLimit adjustLimits(TenantLimit tenantLimit) {
		tenantLimit.addAssets(assets);
		tenantLimit.addDiskSpace(diskSpaceInBytes);
		tenantLimit.addSecondaryOrgs(secondaryOrgs);
		return tenantLimit;
	}

	public Long getSecondaryOrgs() {
		return secondaryOrgs;
	}

	public void setSecondaryOrgs(Long secondaryOrgs) {
		this.secondaryOrgs = secondaryOrgs;
	}
	
	public boolean isSecondaryOrgsUnlimited() {
		return secondaryOrgs.equals(TenantLimit.UNLIMITED);
	}
}
