package com.n4systems.model.criteriarules;

public enum CriteriaRuleAction {

    PHOTO("label.take_photo"),
    ACTION("label.assign_action");

    private String label;

    private CriteriaRuleAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }
}
