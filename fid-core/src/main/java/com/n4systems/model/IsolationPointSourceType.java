package com.n4systems.model;

//This enum is similar to ImageAnnotationType we should probably merge them
public enum IsolationPointSourceType {

    C("Chemical"),
    CP("Control Panel"),
    E("Electrical"),
    G("Gas"),
    H("Hydraulic"),
    M("Mechanical"),
    N("Notes"),
    P("Pneumatic"),
    S("Steam"),
    SP("Stored Pressure"),
    V("Valve"),
    W("Water");

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
