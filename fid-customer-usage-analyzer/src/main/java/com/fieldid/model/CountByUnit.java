package com.fieldid.model;

/**
 * This object holds a count of something and the name of the Tenant that count is for.  This is generically used
 * throughout the app, which is why this description is so ambiguous.
 *
 * Created by Jordan Heath on 2016-09-20.
 */
public class CountByUnit {
    private String name;
    private long count;

    public CountByUnit(String name, long count) {
        this.name = name;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
}
