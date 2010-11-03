package com.n4systems.model.safetynetwork;

import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

import javax.persistence.EntityManager;

public class SafetyNetworkRegisteredOrAssignedEventLoader extends SecurityFilteredLoader<Event> {

    private SafetyNetworkRegisteredAssetEventLoader registeredLoader;
    private SafetyNetworkAssignedAssetEventLoader assignedLoader;

    public SafetyNetworkRegisteredOrAssignedEventLoader(SecurityFilter filter) {
        super(filter);
        registeredLoader = new SafetyNetworkRegisteredAssetEventLoader(filter);
        assignedLoader = new SafetyNetworkAssignedAssetEventLoader(filter);
    }

    @Override
    protected Event load(EntityManager em, SecurityFilter filter) {
        try {
            return registeredLoader.load();
        } catch (SecurityException e) { }
        return assignedLoader.load();
    }

    public SafetyNetworkRegisteredOrAssignedEventLoader setId(Long id) {
        registeredLoader.setId(id);
        assignedLoader.setId(id);
        return this;
    }

}
