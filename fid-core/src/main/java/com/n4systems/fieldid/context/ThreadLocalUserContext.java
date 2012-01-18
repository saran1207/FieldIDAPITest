package com.n4systems.fieldid.context;

import com.n4systems.model.user.User;

import java.io.Serializable;

public class ThreadLocalUserContext implements UserContext, Serializable {

    private ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();

    private static final ThreadLocalUserContext instance = new ThreadLocalUserContext();

    public static UserContext getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return userThreadLocal.get();
    }

    @Override
    public void setCurrentUser(User user) {
        userThreadLocal.set(user);
    }

}
