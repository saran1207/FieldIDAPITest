package com.n4systems.model.procedure;

import com.n4systems.model.api.DisplayEnum;

public enum AnnotationType implements DisplayEnum {
    CALL_OUT_STYLE("Call Out Style"), ARROW_STYLE("Arrow Style");

    private String label;

    AnnotationType(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}
