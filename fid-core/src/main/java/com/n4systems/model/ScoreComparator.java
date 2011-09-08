package com.n4systems.model;

public enum ScoreComparator {

    GE("comparator.ge", false),
    LE("comparator.le", false),
    BETWEEN("comparator.between", true);

    String label;
    boolean binary;

    ScoreComparator(String label, boolean binary) {
        this.label = label;
        this.binary = binary;
    }

    public String getLabel() {
        return label;
    }

    public boolean isBinary() {
        return binary;
    }

}
