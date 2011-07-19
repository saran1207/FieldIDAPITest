package com.n4systems.fieldid.service;

import com.n4systems.model.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class FieldIdService {

//    @Autowired
    protected SecurityFilter userSecurityFilter;

    public SecurityFilter getUserSecurityFilter() {
        return userSecurityFilter;
    }

    public void setUserSecurityFilter(SecurityFilter userSecurityFilter) {
        this.userSecurityFilter = userSecurityFilter;
    }

}
