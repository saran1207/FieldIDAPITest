package com.n4systems.fieldid.context;

import com.n4systems.model.PlatformType;
import com.n4systems.model.user.User;

import java.util.Collection;
import java.util.Locale;

public interface InteractionContext {

    public User getCurrentUser();
    public void setCurrentUser(User user);

    public PlatformType getCurrentPlatformType();
    public void setCurrentPlatformType(PlatformType platformType);

    public String getCurrentPlatform();
    public void setCurrentPlatform(String platform);

    public Collection<User> getVisibleUsers();
    public void setVisibleUsers(Collection<User> visibleUsers);

    public Locale getUserThreadLanguage();
    public void setUserThreadLanguage(Locale userLang);

    public void clear();

    public void setForceDefaultLanguage(boolean forceDefaultLanguage);
    public Boolean isForceDefaultLanguage();

    public Locale getLanguageToUse();

    void disable();
    boolean isEnabled();
}
