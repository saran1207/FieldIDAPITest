package com.n4systems.fieldid.wicket.pages.setup.loto;

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

public class AddEditDevicePanel extends Panel {

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

            add(new RequiredTextField<>("device", new PropertyModel<String>(model, "device")));
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
