package com.n4systems.fieldid.wicket.components.eventform.details.number;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.CriteriaRuleActionChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.DisplayEnumChoiceRenderer;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.NumberFieldCriteriaRule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class NumberCriteriaLogicForm extends Panel {

    private FIDFeedbackPanel feedbackPanel;

    public NumberCriteriaLogicForm(String id, IModel<NumberFieldCriteriaRule> criteriaRuleModel) {
        super(id, criteriaRuleModel);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        Form form;
        RequiredTextField value2;
        add(form = new Form("form"));

        form.add(new RequiredTextField<>("value1", new PropertyModel<>(criteriaRuleModel, "value1")));

        form.add(value2 = new RequiredTextField<Double>("value2", new PropertyModel<>(criteriaRuleModel, "value2")) {
            @Override
            public boolean isVisible() {
                return criteriaRuleModel.getObject().getComparisonType().equals(NumberFieldCriteriaRule.ComparisonType.BT);
            }
        });
        value2.setOutputMarkupPlaceholderTag(true);

        form.add(new DropDownChoice<NumberFieldCriteriaRule.ComparisonType>("comparator",
                new PropertyModel<>(criteriaRuleModel, "comparisonType"),
                Lists.newArrayList(NumberFieldCriteriaRule.ComparisonType.values()),
                new DisplayEnumChoiceRenderer())
                .add(new OnChangeAjaxBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(value2);
                    }
                }));

        form.add(new DropDownChoice<>("actionType",
                new PropertyModel<>(criteriaRuleModel, "action"),
                Lists.newArrayList(CriteriaRule.ActionType.values()),
                new CriteriaRuleActionChoiceRenderer())
                .setNullValid(false)
                .add(new UpdateComponentOnChange()));

        form.add(new AjaxSubmitLink("saveLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onSaveRule(target, criteriaRuleModel.getObject());
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        form.add(new AjaxLink<Void>("removeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onRemoveRule(target, criteriaRuleModel.getObject());
            }

            @Override
            public boolean isVisible() {
                return hasRule();
            }
        });

        form.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancel(target);
            }
        });

    }

    public void onCancel(AjaxRequestTarget target) {

    }

    public boolean hasRule() {
        return false;
    }

    public void onRemoveRule(AjaxRequestTarget target, NumberFieldCriteriaRule object) {

    }

    public void onSaveRule(AjaxRequestTarget target, NumberFieldCriteriaRule object) {

    }
}
