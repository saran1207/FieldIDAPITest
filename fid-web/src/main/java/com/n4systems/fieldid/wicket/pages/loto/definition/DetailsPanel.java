package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.loto.Warning;
import com.n4systems.fieldid.wicket.components.text.*;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class DetailsPanel extends Panel {

    private FIDFeedbackPanel feedbackPanel;

    private @SpringBean ProcedureDefinitionService procedureDefinitionService;

    public DetailsPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class", Model.of("details")));
        add(new ProcedureDefinitionDetailsForm("detailsForm", model).setOutputMarkupId(true));
        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
    }

    private class ProcedureDefinitionDetailsForm extends Form {

        public ProcedureDefinitionDetailsForm(String id, IModel<ProcedureDefinition> model) {
            super(id, model);

            setMarkupId("detailsForm");

            add(new LabelledRequiredTextField<String>("procedureCode", "label.procedure_code", new PropertyModel<String>(model, "procedureCode")));

            add(new LabelledTextField<String>("identifier", "label.electronic_id", ProxyModel.of(model, on(ProcedureDefinition.class).getElectronicIdentifier()))
                    .add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.electronic_id"), TipsyBehavior.Gravity.N)));

//            add(new LabelledTextArea<String>("warnings", "label.warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())){
//                @Override
//                public int getMaxLength() {
//                    return 1024;
//                }
//            });

            add(new Warning("warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())).setOutputMarkupId(true));

            //Fields for Application Process and Removal Process of lockouts.
            add(new LabelledTextArea<String>("applicationProcess", "label.lockout_application_process", ProxyModel.of(model, on(ProcedureDefinition.class).getApplicationProcess())){
                @Override
                public int getMaxLength() {
                    return 1024;
                }
            });

            add(new LabelledTextArea<String>("removalProcess", "label.lockout_removal_process", ProxyModel.of(model, on(ProcedureDefinition.class).getRemovalProcess())){
                @Override
                public int getMaxLength() {
                    return 1024;
                }
            });

            add(new LabelledTextArea<String>("testingAndVerification", "label.testing_and_verification_detail_panel", ProxyModel.of(model, on(ProcedureDefinition.class).getTestingAndVerification())){
                @Override
                public int getMaxLength() {
                    return 1024;
                }
            });

            add(new LabelledAutoCompleteUser("user", "label.developed_by", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy()), true));

            add(new LabelledRequiredTextField<String>("equipmentNumber", "label.equipment_number", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentNumber())));

            add(new LabelledRequiredTextField<String>("equipmentLocation", "label.equipment_location", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentLocation())));

            add(new LabelledTextField<String>("building", "label.building", ProxyModel.of(model, on(ProcedureDefinition.class).getBuilding())));

            add(new LabelledRequiredTextField<String>("equipmentDescription", "label.equipment_description", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentDescription())));

            /*
            * This is not the best way to do it, but it's the simplest way to understand the different edge cases for when and how the
            * Procedure Type picker is displayed and with what values.
            */
            boolean hasMainProcedureType = procedureDefinitionService.hasMainProcedureType(model.getObject().getAsset());
            LabelledDropDown<String> procedureTypeLabelledDropDown = null;
            if(model.getObject().getProcedureType() == null && hasMainProcedureType) {
                procedureTypeLabelledDropDown = new LabelledDropDown<String>("procedureType", "label.procedure_type", ProxyModel.of(model, on(ProcedureDefinition.class).getProcedureTypeLabel())) {
                    @Override
                    protected List<String> getChoices() {
                        return Arrays.asList(ProcedureType.SUB.getLabel());
                    }
                };
            } else if (model.getObject().getProcedureType() == null && !hasMainProcedureType) {
                procedureTypeLabelledDropDown = new LabelledDropDown<String>("procedureType", "label.procedure_type", ProxyModel.of(model, on(ProcedureDefinition.class).getProcedureTypeLabel())) {
                    @Override
                    protected List<String> getChoices() {
                        return ProcedureType.MAIN.getAllLabels();
                    }
                };
            } else if (hasMainProcedureType && model.getObject().getProcedureType().equals(ProcedureType.MAIN)) {
                procedureTypeLabelledDropDown = new LabelledDropDown<String>("procedureType", "label.procedure_type", ProxyModel.of(model, on(ProcedureDefinition.class).getProcedureTypeLabel())) {
                    @Override
                    protected List<String> getChoices() {
                        return ProcedureType.MAIN.getAllLabels();
                    }
                };
            } else if (hasMainProcedureType && model.getObject().getProcedureType().equals(ProcedureType.SUB)) {
                procedureTypeLabelledDropDown = new LabelledDropDown<String>("procedureType", "label.procedure_type", ProxyModel.of(model, on(ProcedureDefinition.class).getProcedureTypeLabel())) {
                    @Override
                    protected List<String> getChoices() {
                        return Arrays.asList(ProcedureType.SUB.getLabel());
                    }
                };
            }
            else {
                procedureTypeLabelledDropDown = new LabelledDropDown<String>("procedureType", "label.procedure_type", ProxyModel.of(model, on(ProcedureDefinition.class).getProcedureTypeLabel())) {
                    @Override
                    protected List<String> getChoices() {
                        return ProcedureType.MAIN.getAllLabels();
                    }
                };
            }
            procedureTypeLabelledDropDown.required();
            procedureTypeLabelledDropDown.add(new AttributeAppender("class", "procedure-def-paddingCorrection"));
            add(procedureTypeLabelledDropDown);

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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/procedureDefinition.css");
    }

    protected void doContinue(AjaxRequestTarget target) { }

    protected void doCancel(AjaxRequestTarget target) { }

}
