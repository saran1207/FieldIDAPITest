package com.n4systems.fieldid.context;

import com.n4systems.model.user.User;

import java.io.Serializable;

public class ThreadLocalInteractionContext implements InteractionContext, Serializable {

    private ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
    private ThreadLocal<String> platformThreadLocal = new ThreadLocal<String>();

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
    public void setCurrentPlatform(String platform) {
        platformThreadLocal.set(platform);
    }

    @Override
    public String getCurrentPlatform() {
        return platformThreadLocal.get();
    }

}
