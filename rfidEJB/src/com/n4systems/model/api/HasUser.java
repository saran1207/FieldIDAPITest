package com.n4systems.model.api;

import com.n4systems.model.user.User;

public interface HasUser extends HasTenant {
	public User getUser();
	public void setUser(User user);
}
