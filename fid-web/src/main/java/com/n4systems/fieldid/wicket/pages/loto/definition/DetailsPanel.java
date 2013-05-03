package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.text.LabelledAutoCompleteUser;
import com.n4systems.fieldid.wicket.components.text.LabelledRequiredTextField;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import static ch.lambdaj.Lambda.on;

public class DetailsPanel extends Panel {

    private FIDFeedbackPanel feedbackPanel;

    public DetailsPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class", Model.of("details")));
        add(new ProcedureDefinitionDetailsForm("detailsForm", model));
        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
    }

    private class ProcedureDefinitionDetailsForm extends Form {

        public ProcedureDefinitionDetailsForm(String id, IModel<ProcedureDefinition> model) {
            super(id, model);

            setMarkupId("detailsForm");

            add(new LabelledRequiredTextField<String>("procedureCode", "label.procedure_code", new PropertyModel<String>(model, "procedureCode")));

            add(new LabelledTextField<String>("identifier", "label.electronic_id", ProxyModel.of(model, on(ProcedureDefinition.class).getElectronicIdentifier()))
                    .add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.electronic_id").getObject(), TipsyBehavior.Gravity.E)));

            add(new LabelledTextArea<String>("warnings", "label.warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())));

            add(new LabelledAutoCompleteUser("user", "label.developed_by", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy()), true));

            add(new LabelledRequiredTextField<String>("equipmentNumber", "label.equipment_number", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentNumber())));

            add(new LabelledRequiredTextField<String>("equipmentLocation", "label.equipment_location", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentLocation())));

            add(new LabelledTextField<String>("building", "label.building", ProxyModel.of(model, on(ProcedureDefinition.class).getBuilding())));

            add(new LabelledRequiredTextField<String>("equipmentDescription", "label.equipment_description", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentDescription())));

            add(new AjaxLink("cancel") {
                @Override public void onClick(AjaxRequestTarget target) {
                    doCancel(target);
                }
            });
            AjaxSubmitLink submitLink;
            add(submitLink = new AjaxSubmitLink("continue") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    doContinue(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            submitLink.setMarkupId("detailsContinueLink");
        }
    }

    protected void doContinue(AjaxRequestTarget target) {

    }

    protected void doCancel(AjaxRequestTarget target) {

    }

}
