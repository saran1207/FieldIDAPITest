package com.n4systems.fieldid.wicket.components.eventform.details.select;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.renderer.CriteriaRuleActionChoiceRenderer;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.SelectCriteriaRule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This Panel allows the user to create/edit Criteria Logic rules for Select Box Criteria.
 *
 * Created by Jordan Heath on 2015-11-17.
 */
public abstract class SelectCriteriaLogicPanel extends Panel {
    //Pretty sure we don't need this now... model should be stored in the default model for the panel.
    private IModel<SelectCriteriaRule> ruleModel;

    public SelectCriteriaLogicPanel(String id, IModel<SelectCriteriaRule> model) {
        super(id, model);
        this.ruleModel = model;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Form theForm = new Form("selectForm");

        theForm.add(new TextField<>("selectValue", new PropertyModel<>(getDefaultModelObject(), "selectValue")).setEnabled(false));

        theForm.add(new DropDownChoice<>("actionType",
                                         new PropertyModel<>(ruleModel.getObject(), "action"),
                                         Lists.newArrayList(CriteriaRule.ActionType.values()),
                                         new CriteriaRuleActionChoiceRenderer()));

        theForm.add(new AjaxLink("saveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onSaveRule(target, ruleModel.getObject());
            }
        });

        theForm.add(new AjaxLink<Void>("removeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onRemoveRule(target, ruleModel.getObject());
            }
        }.setVisible(hasRule(ruleModel.getObject())));

        theForm.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCancel(target);
            }
        });

        add(theForm);
    }

    protected abstract void onSaveRule(AjaxRequestTarget target, CriteriaRule rule);

    protected abstract void onRemoveRule(AjaxRequestTarget target, CriteriaRule rule);

    protected abstract void onCancel(AjaxRequestTarget target);

    protected abstract boolean hasRule(CriteriaRule rule);
}
