package com.n4systems.util.persistence.search;

public enum SortDirection {
    ASC(true, "asc"), DESC(false, "desc");

    private boolean ascending;
    private String displayName;

    SortDirection(boolean reverse, String externalName) {
        this.ascending = reverse;
        this.displayName = externalName;
    }

    public boolean isAscending() {
        return ascending;
    }

    public String getDisplayName() {
        return displayName;
    }
}
