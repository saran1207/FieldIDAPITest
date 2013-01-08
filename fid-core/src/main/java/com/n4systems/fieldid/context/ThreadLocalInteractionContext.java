package com.n4systems.fieldid.context;

import com.n4systems.model.PlatformType;
import com.n4systems.model.user.User;

import java.io.Serializable;

public class ThreadLocalInteractionContext implements InteractionContext, Serializable {

    private ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
    private ThreadLocal<String> platformThreadLocal = new ThreadLocal<String>();
    private ThreadLocal<PlatformType> platformTypeThreadLocal = new ThreadLocal<PlatformType>();

    private static final ThreadLocalInteractionContext instance = new ThreadLocalInteractionContext();

    public static InteractionContext getInstance() {
        return instance;
    }

    @Override
    public User getCurrentUser() {
        return userThreadLocal.get();
    }

    @Override
    public void setCurrentUser(User user) {
        userThreadLocal.set(user);
    }

    @Override
    public PlatformType getCurrentPlatformType() {
        return platformTypeThreadLocal.get();
    }

    @Override
    public void setCurrentPlatformType(PlatformType platformType) {
        platformTypeThreadLocal.set(platformType);
    }

    @Override
    public void setCurrentPlatform(String platform) {
        platformThreadLocal.set(platform);
    }

    @Override
    public String getCurrentPlatform() {
        return platformThreadLocal.get();
    }

    public void clear() {
        platformThreadLocal.remove();
        platformTypeThreadLocal.remove();
        userThreadLocal.remove();
    }

}
