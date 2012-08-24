package com.n4systems.model.orgs;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.parents.EntityWithTenant;

public enum OrgLocationEnum {
    PRIMARY("label.primary", PrimaryOrg.class),
    SECONDARY("label.secondary", SecondaryOrg.class),
    CUSTOMER("label.customer", CustomerOrg.class),
    DIVISION("label.division", DivisionOrg.class),
    LOCATION("label.location", PredefinedLocation.class),
    UNDEFINED( "label.undefined" ,BaseOrg.class);

    private String label;
    private Class<? extends EntityWithTenant> clazz;

    OrgLocationEnum(String label, Class<? extends EntityWithTenant> clazz) {
        this.label = label;
        this.clazz = clazz;
    }

    public Class<? extends EntityWithTenant> getClazz() {
        return clazz;
    }

    public static OrgLocationEnum fromClass(Class<? extends EntityWithTenant> orgClass) {
        for (OrgLocationEnum value:values()) {
            if (value.getClazz().equals(orgClass)) {
                return value;
            }
        }
        return UNDEFINED;
    }

    public String getLabel() {
        return label;
    }
}
