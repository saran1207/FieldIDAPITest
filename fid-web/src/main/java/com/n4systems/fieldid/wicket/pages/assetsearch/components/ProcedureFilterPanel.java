package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ProcedureWorkflowStateCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.Arrays;


public class ProcedureFilterPanel extends Panel {

	public ProcedureFilterPanel(final String id, final IModel<ProcedureCriteria> model) {
		super(id,model);

        final PropertyModel<ProcedureWorkflowStateCriteria> workflowStateModel = new PropertyModel<ProcedureWorkflowStateCriteria>(model, "workflowState");
        FidDropDownChoice<ProcedureWorkflowStateCriteria> workflowStateSelect = new FidDropDownChoice<ProcedureWorkflowStateCriteria>("workflowStateSelect", workflowStateModel, Arrays.asList(ProcedureWorkflowStateCriteria.values()), new ListableLabelChoiceRenderer<ProcedureWorkflowStateCriteria>());
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
