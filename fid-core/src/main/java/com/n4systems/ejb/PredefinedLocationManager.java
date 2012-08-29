package com.n4systems.ejb;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.security.SecurityFilter;

public interface PredefinedLocationManager {

    void updateChildrenOwner(SecurityFilter securityFilter, PredefinedLocation parentNode);
}
