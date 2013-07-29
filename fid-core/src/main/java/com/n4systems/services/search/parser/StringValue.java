package com.n4systems.services.search.parser;

public class StringValue extends SimpleValue {

    public StringValue(String s) {
        super(s);
        // force to be treated as string only.
        this.date = null;
        this.number = null;
    }
}
