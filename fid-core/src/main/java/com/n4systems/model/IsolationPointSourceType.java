package com.n4systems.model;

public enum IsolationPointSourceType {
    W("Water"), V("Valve"), S("Steam"), P("Pneumatic"), G("Gas"), E("Electrical"), CP("Control Panel"), N("Notes");

    private final String identifier;

    IsolationPointSourceType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static IsolationPointSourceType getDefault() {
        return W;
    }

}
