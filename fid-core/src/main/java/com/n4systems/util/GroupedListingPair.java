package com.n4systems.util;

public class GroupedListingPair<T> extends ListingPair {

    private Object group;

    public GroupedListingPair() { }

    public GroupedListingPair(Long id, String name, Object group) {
        super(id, name);
        this.group = group;
    }

    public Object getGroup() {
        return group;
    }

    public void setGroup(Object group) {
        this.group = group;
    }

}
