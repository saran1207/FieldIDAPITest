package com.n4systems.fieldid.context;

import com.n4systems.model.PlatformType;
import com.n4systems.model.user.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;

public class ThreadLocalInteractionContext implements InteractionContext, Serializable {

    private ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
    private ThreadLocal<Collection<User>> visibleUsersThreadLocal = new ThreadLocal<Collection<User>>();
    private ThreadLocal<String> platformThreadLocal = new ThreadLocal<String>();
    private ThreadLocal<Locale> userThreadLanguage = new ThreadLocal<Locale>();
    private ThreadLocal<PlatformType> platformTypeThreadLocal = new ThreadLocal<PlatformType>();
    private ThreadLocal<Boolean> isForceDefaultLanguage = new ThreadLocal<Boolean>();

    private static final ThreadLocalInteractionContext instance = new ThreadLocalInteractionContext();

    public static InteractionContext getInstance() {
        return instance;
    }

    @Override
    public User getCurrentUser() {
        return userThreadLocal.get();
    }

    @Override
    public Locale getUserThreadLanguage() {
        return userThreadLanguage.get();
    }

    @Override
    public void setUserThreadLanguage(Locale userLang) {
        userThreadLanguage.set(userLang);
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

    public Collection<User> getVisibleUsers() {
        return visibleUsersThreadLocal.get();
    }

    public void setVisibleUsers(Collection<User> visibleUsers) {
        visibleUsersThreadLocal.set(visibleUsers);
    }

    public void clear() {
        platformThreadLocal.remove();
        platformTypeThreadLocal.remove();
        userThreadLocal.remove();
        visibleUsersThreadLocal.remove();
        userThreadLanguage.remove();
        isForceDefaultLanguage.remove();
    }

    @Override
    public void setForceDefaultLanguage(boolean forceDefaultLanguage) {
        this.isForceDefaultLanguage.set(forceDefaultLanguage);
    }

    @Override
    public Boolean isForceDefaultLanguage() {
        Boolean result = isForceDefaultLanguage.get();
        return result==null ? false : result;
    }

    @Override
    public Locale getLanguageToUse() {
        return isForceDefaultLanguage() ? getCurrentUser().getTenant().getSettings().getDefaultLanguage() : getUserThreadLanguage();
    }
}
