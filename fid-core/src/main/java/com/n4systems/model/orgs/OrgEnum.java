package com.n4systems.model.orgs;

public enum OrgEnum {
    PRIMARY("label.primary", PrimaryOrg.class),
    SECONDARY("label.secondary", SecondaryOrg.class),
    CUSTOMER("label.customer", CustomerOrg.class),
    DIVISION("label.division", DivisionOrg.class),
    UNDEFINED( "label.undefined" ,BaseOrg.class);

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

    public String getLabel() {
        return label;
    }
}
