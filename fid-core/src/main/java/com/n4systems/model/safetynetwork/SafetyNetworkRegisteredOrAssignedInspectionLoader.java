package com.n4systems.model.safetynetwork;

import com.n4systems.model.Inspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;

import javax.persistence.EntityManager;

public class SafetyNetworkRegisteredOrAssignedInspectionLoader extends SecurityFilteredLoader<Inspection> {

    private SafetyNetworkRegisteredProductInspectionLoader registeredLoader;
    private SafetyNetworkAssignedProductInspectionLoader assignedLoader;

    public SafetyNetworkRegisteredOrAssignedInspectionLoader(SecurityFilter filter) {
        super(filter);
        registeredLoader = new SafetyNetworkRegisteredProductInspectionLoader(filter);
        assignedLoader = new SafetyNetworkAssignedProductInspectionLoader(filter);
    }

    @Override
    protected Inspection load(EntityManager em, SecurityFilter filter) {
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
