package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TenantLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Long UNLIMITED = -1L;
	
	@Column(name="diskspace_limit", nullable = false)
	private Long diskSpace = 0L;
	
	@Column(name="user_limit", nullable = false)
	private Long users = 0L;

	@Column(name="asset_limit", nullable = false)
	private Long assets = 0L;
	
	public TenantLimit() {}

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
	
	
}
