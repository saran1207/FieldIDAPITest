package com.n4systems.model;

/**
 * This is the enum for the type of LOTO Printout, either Short or Long.
 *
 * Created by jheath on 2014-10-22.
 */
public enum LotoPrintoutType {
    SHORT("Short"),
    LONG("Long");

    private String label;

    LotoPrintoutType (String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}
