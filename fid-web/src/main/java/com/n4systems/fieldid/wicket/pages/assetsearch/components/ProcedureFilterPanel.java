package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.service.procedure.LockoutReasonService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.model.procedure.LockoutReason;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ProcedureWorkflowStateCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;


public class ProcedureFilterPanel extends Panel {

    @SpringBean
    private LockoutReasonService lockoutReasonService;

	public ProcedureFilterPanel(final String id, final IModel<ProcedureCriteria> model) {
		super(id,model);

        final PropertyModel<LockoutReason> lockoutReasonStateModel = new PropertyModel<LockoutReason>(model, "lockoutReason");
        FidDropDownChoice<LockoutReason> lockoutReasonSelect = new FidDropDownChoice<LockoutReason>("lockoutReasonSelect", lockoutReasonStateModel, lockoutReasonService.getActiveLockoutReasons(), new ListableLabelChoiceRenderer<LockoutReason>());
        lockoutReasonSelect.setNullValid(true);
        add(lockoutReasonSelect);

        final PropertyModel<ProcedureWorkflowStateCriteria> workflowStateModel = new PropertyModel<ProcedureWorkflowStateCriteria>(model, "workflowState");
        FidDropDownChoice<ProcedureWorkflowStateCriteria> workflowStateSelect = new FidDropDownChoice<ProcedureWorkflowStateCriteria>("workflowStateSelect", workflowStateModel, Arrays.asList(ProcedureWorkflowStateCriteria.values()), new ListableLabelChoiceRenderer<ProcedureWorkflowStateCriteria>());
        workflowStateSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(workflowStateModel.getObject().equals(ProcedureWorkflowStateCriteria.OPEN)) {
                    lockoutReasonSelect.setVisible(false);
                    model.getObject().setLockoutReason(null);
                } else {
                    lockoutReasonSelect.setVisible(true);
                }
                target.add(lockoutReasonSelect);
            }
        });

        workflowStateSelect.setNullValid(false);
        add(workflowStateSelect);

        add( new CollapsiblePanel("dateDetailsCriteriaPanel", new StringResourceModel("label.dates_and_times",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return new ProcedureDateRangeCriteriaPanel(id, model);
            }
        });

        add(new CollapsiblePanel("assetDetailsCriteriaPanel", new StringResourceModel("label.asset_details", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new AssetDetailsCriteriaPanel(id, model, true);
            }
        });


        add( new CollapsiblePanel("identifiersCriteriaPanel", new StringResourceModel("label.identifiers",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return new IdentifiersCriteriaPanel(id, model);
            }
        });

        add( new CollapsiblePanel("peopleDetailsCriteriaPanel", new StringResourceModel("label.people", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new PeopleDetailsCriteriaPanel(id, model, false);
            }
        });
        
	}
}
