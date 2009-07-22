package com.n4systems.model.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TenantLimit implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="diskspace_limit", nullable = false)
	private Long diskSpace = 0L;
	
	@Column(name="user_limit", nullable = false)
	private Long users = 0L;

	public TenantLimit() {}

	public Long getDiskSpace() {
		return diskSpace;
	}

	public void setDiskSpace(Long diskSpace) {
		this.diskSpace = diskSpace;
	}

	public Long getUsers() {
		return users;
	}

	public void setUsers(Long users) {
		this.users = users;
	}
	
}
