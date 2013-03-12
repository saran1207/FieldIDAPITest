package com.n4systems.fieldid.context;

import com.n4systems.model.PlatformType;
import com.n4systems.model.user.User;

import java.util.Collection;
import java.util.List;

public interface InteractionContext {

    public User getCurrentUser();
    public void setCurrentUser(User user);

    public PlatformType getCurrentPlatformType();
    public void setCurrentPlatformType(PlatformType platformType);

    public String getCurrentPlatform();
    public void setCurrentPlatform(String platform);

    public Collection<User> getVisibleUsers();
    public void setVisibleUsers(Collection<User> visibleUsers);

    public void clear();

}
