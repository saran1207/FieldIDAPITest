package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.eventform.details.oneclick.ButtonGroupDisplayPanel;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.StateSet;
import com.n4systems.model.stateset.StateSetLoader;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class OneClickDetailsPanel extends Panel {

    private ButtonGroupDisplayPanel buttonGroupDisplayPanel;

    public OneClickDetailsPanel(String id, IModel<OneClickCriteria> oneClickCriteria) {
        super(id, oneClickCriteria);
        add(new StateSetForm("stateSetForm"));
        add(buttonGroupDisplayPanel = new ButtonGroupDisplayPanel("buttonGroupDetailsPanel", new PropertyModel<StateSet>(getCriteriaModel(), "states")));
    }

    protected IModel<OneClickCriteria> getCriteriaModel() {
        return (IModel<OneClickCriteria>) getDefaultModel();
    }

    class StateSetForm extends Form {

        public StateSetForm(String id) {
            super(id);

            List<StateSet> stateSetList = getStateSetList();
            DropDownChoice<StateSet> stateChoice;

            add(stateChoice = new DropDownChoice<StateSet>("stateSetSelect", new PropertyModel<StateSet>(getCriteriaModel(), "states"), stateSetList, createChoiceRenderer()));
            stateChoice.setNullValid(false);
            stateChoice.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    onStateSetSelected(getCriteriaModel().getObject().getStates());
                    target.addComponent(buttonGroupDisplayPanel);
                }
                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
            add(new AjaxCheckBox("setsResultCheckbox", new PropertyModel<Boolean>(getCriteriaModel(), "principal")) {
                @Override protected void onUpdate(AjaxRequestTarget target) {}});
        }

        private List<StateSet> getStateSetList() {
            StateSetLoader stateSetLoader = new StateSetLoader(FieldIDSession.get().getSessionUser().getSecurityFilter());
            return stateSetLoader.load();
        }
    }

    private IChoiceRenderer<StateSet> createChoiceRenderer() {
        return new IChoiceRenderer<StateSet>() {
            @Override
            public Object getDisplayValue(StateSet stateSet) {
                return stateSet.getDisplayName();
            }

            @Override
            public String getIdValue(StateSet stateSet, int index) {
                return stateSet.getId()+"";
            }
        };
    }

    protected void onStateSetSelected(StateSet stateSet) { }

}
