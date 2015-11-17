package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.criteriarules.CriteriaRule;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class CriteriaRuleActionChoiceRenderer implements IChoiceRenderer<CriteriaRule.ActionType> {
    @Override
    public Object getDisplayValue(CriteriaRule.ActionType object) {
        return new FIDLabelModel(object.getLabel()).getObject();
    }

    @Override
    public String getIdValue(CriteriaRule.ActionType object, int index) {
        return object.getName();
    }
}
