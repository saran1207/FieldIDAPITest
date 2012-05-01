package com.n4systems.model.orgs;

public enum OrgEnum {
    PRIMARY("primary", PrimaryOrg.class),
    SECONDARY("secondary", SecondaryOrg.class),
    CUSTOMER("customer", CustomerOrg.class),
    DIVISION("division", DivisionOrg.class),
    UNDEFINED( "undefined" ,BaseOrg.class);

    private String label;
    private Class<? extends BaseOrg> clazz;

    OrgEnum(String label, Class<? extends BaseOrg> clazz) {
        this.label = label;
        this.clazz = clazz;
    }

    public Class<? extends BaseOrg> getClazz() {
        return clazz;
    }

    public static OrgEnum fromClass(Class<? extends BaseOrg> orgClass) {
        for (OrgEnum value:values()) {
            if (value.getClazz().equals(orgClass)) {
                return value;
            }
        }
        return UNDEFINED;
    }
}
