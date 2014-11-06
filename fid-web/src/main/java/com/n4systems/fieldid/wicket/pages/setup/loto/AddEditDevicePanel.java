package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.procedure.PreconfiguredDevice;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class AddEditDevicePanel extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private FIDFeedbackPanel feedbackPanel;

    public AddEditDevicePanel(String id, IModel<PreconfiguredDevice> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new AddEditDeviceForm("form", model));
    }

    private class AddEditDeviceForm extends Form<PreconfiguredDevice> {

        public AddEditDeviceForm(String id, final IModel<PreconfiguredDevice> model) {
            super(id, model);
            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

            RequiredTextField deviceField;
            add(deviceField = new RequiredTextField<>("device", new PropertyModel<String>(model, "device")));

            deviceField.add(new IValidator<String>() {
                @Override
                public void validate(IValidatable<String> validatable) {
                    if(procedureDefinitionService.isPredefinedDeviceNameExists((String) validatable.getValue(),
                            model.getObject().getIsolationPointSourceType(), model.getObject().getId())) {
                        ValidationError error = new ValidationError();
                        error.addMessageKey("errors.data.device_duplicate");
                        validatable.error(error);
                    }
                }
            });

            add(new TextArea<>("method", new PropertyModel<String>(model, "method")));

            add(new AjaxSubmitLink("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    onEditComplete(target, AddEditDeviceForm.this.getModelObject());
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
        }
    }

    protected void onEditComplete(AjaxRequestTarget target, PreconfiguredDevice device) { }
}
