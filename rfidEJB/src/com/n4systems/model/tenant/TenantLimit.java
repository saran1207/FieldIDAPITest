package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TenantLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Long UNLIMITED = -1L;
	
	@Column(name="diskspace_limit", nullable = false)
	private Long diskSpace;
	
	@Column(name="asset_limit", nullable = false)
	private Long assets;
	
	@Column(name="user_limit", nullable = false)
	private Long users;

	
	
	public TenantLimit() {
		this(0L, 0L, 0L);
	}
	
	public TenantLimit(long diskSpace, long assets, long users) {
		this.diskSpace = diskSpace;
		this.assets = assets;
		this.users = users;
	}

	public Long getDiskSpaceInBytes() {
		return diskSpace;
	}
	
	public boolean isDiskSpaceUnlimited() {
		return (getDiskSpaceInBytes() == UNLIMITED);
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
		return (getUsers() == UNLIMITED);
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
		return (getAssets() == UNLIMITED);
	}
	
	public void setAssetsUnlimited() {
		setAssets(UNLIMITED);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TenantLimit) {
			TenantLimit otherLimit = (TenantLimit)obj;
			return (otherLimit.users.equals(users) 
						&& otherLimit.diskSpace.equals(diskSpace) 
						&& otherLimit.assets.equals(assets));
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		Long sum = (assets + users + diskSpace);
		return sum.hashCode();
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
	
	private Long addLimits(Long currentLimit, Long limitToAdd) {
		if (currentLimit.equals(UNLIMITED) || limitToAdd.equals(UNLIMITED)) {
			return UNLIMITED;
		}
		return currentLimit + limitToAdd;
	}

	@Override
	public String toString() {
		
		return "users = [" + users + "] assets = [" + assets + "] diskspace = [" + diskSpace + "]";
	}
	
	
	
	
}
