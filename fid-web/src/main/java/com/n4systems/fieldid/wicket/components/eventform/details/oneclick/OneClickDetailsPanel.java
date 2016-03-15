package com.n4systems.fieldid.wicket.components.eventform.details.oneclick;

import com.n4systems.fieldid.service.event.ButtonGroupService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Button;
import com.n4systems.model.ButtonGroup;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class OneClickDetailsPanel extends Panel {

    @SpringBean
    private ButtonGroupService buttonGroupService;

    private ButtonGroupDisplayPanel buttonGroupDisplayPanel;

    public OneClickDetailsPanel(String id, IModel<OneClickCriteria> oneClickCriteria) {
        super(id, oneClickCriteria);
        add(new StateSetForm("stateSetForm"));
        add(buttonGroupDisplayPanel = new ButtonGroupDisplayPanel("buttonGroupDetailsPanel", ProxyModel.of(getCriteriaModel(), on(OneClickCriteria.class).getButtonGroup())){
            @Override
            protected void onConfigureCriteriaLogic(AjaxRequestTarget target, Button button) {
                OneClickDetailsPanel.this.onConfigureCriteriaLogic(target, button);
            }

            @Override
            protected Boolean hasRule(Button button) {
                return oneClickCriteria.getObject().getRules().stream().anyMatch(rule -> ((OneClickCriteriaRule) rule).getButton().equals(button));
            }
        });
    }

    protected IModel<OneClickCriteria> getCriteriaModel() {
        return (IModel<OneClickCriteria>) getDefaultModel();
    }

    class StateSetForm extends Form {

        public StateSetForm(String id) {
            super(id);

            List<ButtonGroup> buttonGroupList = getStateSetList();
            DropDownChoice<ButtonGroup> stateChoice;

            add(stateChoice = new DropDownChoice<ButtonGroup>("stateSetSelect", ProxyModel.of(getCriteriaModel(), on(OneClickCriteria.class).getButtonGroup()), buttonGroupList, createChoiceRenderer()));
            stateChoice.setNullValid(false);
            stateChoice.add(new UpdateComponentOnChange() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    onStateSetSelected(getCriteriaModel().getObject().getButtonGroup());
                    target.add(buttonGroupDisplayPanel);
                }
            });
            add(new AjaxCheckBox("setsResultCheckbox", ProxyModel.of(getCriteriaModel(), on(OneClickCriteria.class).isPrincipal())) {
                @Override protected void onUpdate(AjaxRequestTarget target) {
                    boolean setsResult = getCriteriaModel().getObject().isPrincipal();
                    onSetsResultSelected(setsResult);
                }});
        }

        private List<ButtonGroup> getStateSetList() {
            return buttonGroupService.findAllButtonGroups(false);
        }
    }

    private IChoiceRenderer<ButtonGroup> createChoiceRenderer() {
        return new IChoiceRenderer<ButtonGroup>() {
            @Override
            public Object getDisplayValue(ButtonGroup buttonGroup) {
                return buttonGroup.getDisplayName();
            }

            @Override
            public String getIdValue(ButtonGroup buttonGroup, int index) {
                return buttonGroup.getId()+"";
            }
        };
    }

    protected void onStateSetSelected(ButtonGroup buttonGroup) { }

    protected void onSetsResultSelected(boolean setsResult) { }

    protected void onConfigureCriteriaLogic(AjaxRequestTarget target, Button button) {}

}
