package com.n4systems.fieldid.wicket.pages.loto;

public enum PrintOptions {
    Compact(45), Normal(75), Spacious(110);

    private final int spacing;

    private PrintOptions(int spacing) {
        this.spacing = spacing;
    }

    public int getSpacing() {
        return spacing;
    }

}
