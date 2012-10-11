package com.n4systems.model.dashboard.widget.interfaces;

import com.n4systems.model.user.User;

public interface ConfigurationWithUser {

	User getUser();
    void setUser(User currentUser);
}
