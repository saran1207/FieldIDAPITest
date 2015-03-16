package com.n4systems.fieldid.wicket.components.setup.loto;

import com.n4systems.fieldid.service.procedure.LockoutReasonService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.LockoutReason;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

public class AddEditLockoutReasonPanel extends Panel {

    private FIDFeedbackPanel feedbackPanel;

    @SpringBean
    private LockoutReasonService lockoutReasonsService;

    public AddEditLockoutReasonPanel(String id, IModel<LockoutReason> model) {
        super(id, model);
        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(new Form<LockoutReason>("form")
                .add(new Label("headerLabel",
                        new FIDLabelModel(model.getObject().isNew() ? "label.add_new_lockout_reason" : "label.edit_lockout_reason")))
                .add(new RequiredTextField<>("name", new PropertyModel<>(model, "name"))
                        .add(new StringValidator.MaximumLengthValidator(255)))
                .add(new AjaxSubmitLink("saveLink") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        LockoutReason lockoutReason = model.getObject();
                        lockoutReason.setTenant(FieldIDSession.get().getTenant());
                        lockoutReasonsService.saveOrUpdate(lockoutReason);
                        onSaveLockoutReason(target);
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {
                        target.add(feedbackPanel);
                    }
                })
                .add(new AjaxLink<Void>("cancelLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        onCancelClicked(target);
                    }
                }));
    }

    protected void onSaveLockoutReason(AjaxRequestTarget target) {}

    protected void onCancelClicked(AjaxRequestTarget target) {}
}
