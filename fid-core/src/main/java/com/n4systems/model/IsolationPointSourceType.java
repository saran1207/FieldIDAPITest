package com.n4systems.model;

//This enum is similar to ImageAnnotationType we should probably merge them
public enum IsolationPointSourceType {

    W("Water"),
    V("Valve"),
    S("Steam"),
    P("Pneumatic"),
    G("Gas"),
    E("Electrical"),
    CP("Control Panel"),
    M("Mechanical"),
    H("Hydraulic"),
    C("Chemical"),
    SP("Stored Pressure"),
    N("Notes");

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
