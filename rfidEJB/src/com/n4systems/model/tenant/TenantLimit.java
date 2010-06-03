package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.n4systems.services.limiters.LimitType;
import com.n4systems.util.HashCode;

@Embeddable
public class TenantLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final Long NONE = 0L;
	public static Long UNLIMITED = -1L;
	
	@Column(name="diskspace_limit", nullable = false)
	private Long diskSpace;
	
	@Column(name="asset_limit", nullable = false)
	private Long assets;
	
	@Column(name="user_limit", nullable = false)
	private Long users;

	@Column(name="org_limit", nullable = false)
	private Long secondaryOrgs;
	
	public TenantLimit() {
		this(0L, 0L, 0L, 0L);
	}
	
	public TenantLimit(long diskSpace, long assets, long users, long secondaryOrgs) {
		this.diskSpace = diskSpace;
		this.assets = assets;
		this.users = users;
		this.secondaryOrgs = secondaryOrgs;
	}

	public Long getLimitForType(LimitType type) {
		Long limit = null;
		switch(type) {
			case ASSETS:
				limit = assets;
				break;
			case DISK_SPACE:
				limit = diskSpace;
				break;
			case EMPLOYEE_USERS:
				limit = users;
				break;
			case SECONDARY_ORGS:
				limit = secondaryOrgs;
				break;
		}
		return limit;
	}
	
	public Long getDiskSpaceInBytes() {
		return diskSpace;
	}
	
	public boolean isDiskSpaceUnlimited() {
		return isUnlimited(getDiskSpaceInBytes());
	}

	public void setDiskSpaceInBytes(Long diskSpace) {
		this.diskSpace = diskSpace ;
	}
	
	public void setDiskSpaceUnlimited() {
		setDiskSpaceInBytes(UNLIMITED);
	}

	public Long getUsers() {
		return users;
	}

	public void setUsers(Long users) {
		this.users = users;
	}
	
	public boolean isUsersUnlimited() {
		return isUnlimited(getUsers());
	}
	
	public void setUsersUnlimited() {
		setUsers(UNLIMITED);
	}

	public Long getAssets() {
		return assets;
	}

	public void setAssets(Long assets) {
		this.assets = assets;
	}
	
	public boolean isAssetsUnlimited() {
		return isUnlimited(getAssets());
	}
	
	public void setAssetsUnlimited() {
		setAssets(UNLIMITED);
	}
	
	public Long getSecondaryOrgs() {
		return secondaryOrgs;
	}

	public void setSecondaryOrgs(Long secondaryOrgs) {
		this.secondaryOrgs = secondaryOrgs;
	}

	public boolean isSecondaryOrgsUnlimited() {
		return isUnlimited(getSecondaryOrgs());
	}
	
	public void setSecondaryOrgsUnlimited() {
		setSecondaryOrgs(UNLIMITED);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TenantLimit) {
			TenantLimit otherLimit = (TenantLimit)obj;
			return (
					otherLimit.getUsers().equals(getUsers()) &&
					otherLimit.getDiskSpaceInBytes().equals(getDiskSpaceInBytes()) &&
					otherLimit.getAssets().equals(getAssets()) &&
					otherLimit.getSecondaryOrgs().equals(getSecondaryOrgs())
			);
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return HashCode.newHash().add(getUsers()).add(getDiskSpaceInBytes()).add(getAssets()).add(getSecondaryOrgs()).toHash();
	}

	public void addUsers(Long additionalUsers) {
		users = addLimits(users, additionalUsers);
	}
	
	public void addDiskSpace(Long additionalDiskSpace) {
		diskSpace = addLimits(diskSpace, additionalDiskSpace);
	}
	
	public void addAssets(Long additionalAssets) {
		assets = addLimits(assets, additionalAssets);
	}
	
	public void addSecondaryOrgs(Long additionalSecondaryOrgs) {
		secondaryOrgs = addLimits(secondaryOrgs, additionalSecondaryOrgs);
	}
	
	private Long addLimits(Long currentLimit, Long limitToAdd) {
		if (currentLimit.equals(UNLIMITED) || limitToAdd.equals(UNLIMITED)) {
			return UNLIMITED;
		}
		return currentLimit + limitToAdd;
	}

	public boolean isAssetLimitGreaterThan(long assetLimit) {
		return isCurrentLimitGreaterThan(assetLimit, assets);
	}

	
	public boolean isDiskLimitInBytesGreaterThan(long diskLimit) {
		return isCurrentLimitGreaterThan(diskLimit, diskSpace);
	}
	
	private boolean isCurrentLimitGreaterThan(long newLimit, long currentLimit) {
		if (currentLimit == TenantLimit.UNLIMITED)
			return true;
		if (newLimit == TenantLimit.UNLIMITED) 
			return false;
		
		return currentLimit >= newLimit;
	}
	
	public boolean isSecondaryOrgsLimitGreaterThan(long secondaryOrgLimit) {
		return isCurrentLimitGreaterThan(secondaryOrgLimit, secondaryOrgs);
	}
	
	
	public static boolean isUnlimited(Long limit) {
		return (limit != null && limit == UNLIMITED);
	}

	@Override
	public String toString() {
		return "TenantLimit [assets=" + assets + ", diskSpace=" + diskSpace + ", secondaryOrgs=" + secondaryOrgs + ", users=" + users + "]";
	}
}
