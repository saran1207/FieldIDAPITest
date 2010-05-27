package com.n4systems.model.api;

import com.n4systems.model.user.User;

public interface HasModifiedBy {
	public User getModifiedBy();
	public void setModifiedBy(User modifiedBy);
}
