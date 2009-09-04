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

	
	public LimitAdjuster() {
		this(0L, 0L);
	}

	public LimitAdjuster(Long assets, Long diskSpaceInBytes) {
		this(assets, diskSpaceInBytes, DataUnit.BYTES);
		
	}
	
	public LimitAdjuster(Long assets, Long diskSpaceInOtherUnit, DataUnit diskSpaceUnit) {
		super();
		setDiskSpace(diskSpaceInOtherUnit, diskSpaceUnit);
		this.assets = assets;
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
		return tenantLimit;
	}
}
