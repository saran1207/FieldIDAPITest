package com.n4systems.model.procedure;

import com.n4systems.model.api.DisplayEnum;

import java.util.ArrayList;
import java.util.List;

public enum AnnotationType implements DisplayEnum {
    CALL_OUT_STYLE("Call Out"), ARROW_STYLE("Arrow");

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

    public List<String> getAllLabels() {
        List<String> labels = new ArrayList<String>();

        labels.add(this.CALL_OUT_STYLE.getLabel());
        labels.add(this.ARROW_STYLE.getLabel());

        return labels;
    }
}
