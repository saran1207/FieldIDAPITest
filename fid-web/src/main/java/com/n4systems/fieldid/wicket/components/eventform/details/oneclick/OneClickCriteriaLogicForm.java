package com.n4systems.fieldid.wicket.components.eventform.details.oneclick;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Button;
import com.n4systems.model.criteriarules.CriteriaRule;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class OneClickCriteriaLogicForm extends Panel {

    private IModel<CriteriaRule.ActionType> criteriaRuleAction;

    public OneClickCriteriaLogicForm(String id, IModel<Button> buttonModel) {
        super(id, buttonModel);
        Form form;
        add(form = new Form("form"));

        form.add(new ContextImage("buttonImage", "images/eventButtons/" + buttonModel.getObject().getButtonName() + ".png"));
        form.add(new Label("buttonLabel", new PropertyModel<String>(buttonModel, "displayText")));

        form.add(new DropDownChoice<CriteriaRule.ActionType>("actionType",
                new PropertyModel<CriteriaRule.ActionType>(this, "criteriaRuleAction"),
                Lists.newArrayList(CriteriaRule.ActionType.values()),
                new IChoiceRenderer<CriteriaRule.ActionType>() {
                    @Override
                    public Object getDisplayValue(CriteriaRule.ActionType object) {
                        return new FIDLabelModel(object.getLabel()).getObject();
                    }

                    @Override
                    public String getIdValue(CriteriaRule.ActionType object, int index) {
                        return object.getName();
                    }
                }));

    }
}
