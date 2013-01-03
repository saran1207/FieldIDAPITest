package com.n4systems.fieldid.context;

import com.n4systems.model.user.User;

public interface InteractionContext {

    public User getCurrentUser();
    public void setCurrentUser(User user);

    public String getCurrentPlatform();
    public void setCurrentPlatform(String platform);
}
