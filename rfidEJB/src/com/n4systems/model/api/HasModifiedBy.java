package com.n4systems.model.api;

import rfid.ejb.entity.UserBean;

public interface HasModifiedBy {
	public UserBean getModifiedBy();
	public void setModifiedBy(UserBean modifiedBy);
}
