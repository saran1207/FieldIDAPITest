package com.n4systems.fieldid.wicket.components.eventform.details.oneclick;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.renderer.CriteriaRuleActionChoiceRenderer;
import com.n4systems.model.Button;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class OneClickCriteriaLogicForm extends Panel {

    public OneClickCriteriaLogicForm(String id, IModel<OneClickCriteriaRule> criteriaRuleModel) {
        super(id, criteriaRuleModel);
        Form form;
        add(form = new Form("form"));

        form.add(new ContextImage("buttonImage", "images/eventButtons/" + criteriaRuleModel.getObject().getButton().getButtonName() + ".png"));
        form.add(new Label("buttonLabel", new PropertyModel<String>(criteriaRuleModel, "button.displayText")));

        form.add(new DropDownChoice<CriteriaRule.ActionType>("actionType",
                new PropertyModel<CriteriaRule.ActionType>(criteriaRuleModel, "action"),
                Lists.newArrayList(CriteriaRule.ActionType.values()),
                new CriteriaRuleActionChoiceRenderer())
                .setNullValid(false).add(new UpdateComponentOnChange()));

        form.add(new AjaxLink<Void>("saveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onSaveRule(target, criteriaRuleModel.getObject());
            }
        });

        form.add(new AjaxLink<Void>("removeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onRemoveRule(target, criteriaRuleModel.getObject());
            }
        }.setVisible(!criteriaRuleModel.getObject().isNew()));

        form.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {

            }
        });

    }

    public void onSaveRule(AjaxRequestTarget target, CriteriaRule rule) {}

    public void onRemoveRule(AjaxRequestTarget target, CriteriaRule rule) {}

    public void onCancel(AjaxRequestTarget target) {}
}
