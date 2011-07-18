package com.n4systems.fieldid.utils;

import com.n4systems.model.security.SecurityFilter;
import rfid.web.helper.SessionUser;

public interface WebContextProvider {

    public SessionUser getSessionUser();

    public String getText(String key);

    public SecurityFilter getSecurityFilter();

}
