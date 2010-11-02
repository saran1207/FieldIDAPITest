package com.n4systems.model.safetynetwork;

import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

import javax.persistence.EntityManager;

public class SafetyNetworkRegisteredOrAssignedInspectionLoader extends SecurityFilteredLoader<Event> {

    private SafetyNetworkRegisteredAssetInspectionLoader registeredLoader;
    private SafetyNetworkAssignedAssetInspectionLoader assignedLoader;

    public SafetyNetworkRegisteredOrAssignedInspectionLoader(SecurityFilter filter) {
        super(filter);
        registeredLoader = new SafetyNetworkRegisteredAssetInspectionLoader(filter);
        assignedLoader = new SafetyNetworkAssignedAssetInspectionLoader(filter);
    }

    @Override
    protected Event load(EntityManager em, SecurityFilter filter) {
        try {
            return registeredLoader.load();
        } catch (SecurityException e) { }
        return assignedLoader.load();
    }

    public SafetyNetworkRegisteredOrAssignedInspectionLoader setId(Long id) {
        registeredLoader.setId(id);
        assignedLoader.setId(id);
        return this;
    }

}
