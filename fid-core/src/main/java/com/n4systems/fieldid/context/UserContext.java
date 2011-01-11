package com.n4systems.fieldid.context;

import com.n4systems.model.user.User;

public interface UserContext {

    public User getCurrentUser();
    public void setCurrentUser(User user);

}
