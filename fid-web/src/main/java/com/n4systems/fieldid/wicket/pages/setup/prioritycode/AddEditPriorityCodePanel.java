package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.PriorityCodeAutoScheduleType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.Arrays;

import static ch.lambdaj.Lambda.on;

public class AddEditPriorityCodePanel extends Panel {

    AddPriorityCodeForm form;

    public AddEditPriorityCodePanel(String id, IModel<PriorityCode> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(form = new AddPriorityCodeForm("form", model));
    }

    class AddPriorityCodeForm extends Form<PriorityCode> {

        FIDFeedbackPanel feedbackPanel;
        public WebMarkupContainer formContainer;

        public AddPriorityCodeForm(String id, final IModel<PriorityCode> model) {
            super(id, model);
            add(formContainer = new WebMarkupContainer("formContainer"));
            formContainer.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            RequiredTextField<String> nameField;
            formContainer.add(nameField = new RequiredTextField<String>("name", ProxyModel.of(model, on(PriorityCode.class).getName())));
            nameField.add(new PriorityCodeUniqueNameValidator(model.getObject().getId()));

            final TextField<Integer> customDaysField = new TextField<Integer>("autoScheduleCustomDays", ProxyModel.of(model, on(PriorityCode.class).getAutoScheduleCustomDays()));
            final WebMarkupContainer customDaysContainer = new WebMarkupContainer("customDaysContainer") {
                { setOutputMarkupPlaceholderTag(true); }
                @Override
                public boolean isVisible() {
                    return model.getObject().getAutoSchedule() == PriorityCodeAutoScheduleType.CUSTOM;
                }
            };
            customDaysContainer.add(customDaysField);
            formContainer.add(customDaysContainer);

            formContainer.add(new FidDropDownChoice<PriorityCodeAutoScheduleType>("autoSchedule", ProxyModel.of(model, on(PriorityCode.class).getAutoSchedule()), Arrays.asList(PriorityCodeAutoScheduleType.values()), new ListableLabelChoiceRenderer<PriorityCodeAutoScheduleType>()) {
                {
                    setNullValid(true);
                    add(new UpdateComponentOnChange() {
                        @Override
                        protected void onUpdate(AjaxRequestTarget target) {
                            model.getObject().setAutoScheduleCustomDays(null);
                            target.add(customDaysContainer);
                        }
                    });
                }
            });

            formContainer.add(new AjaxSubmitLink("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    PriorityCodeAutoScheduleType autoScheduleType = getModelObject().getAutoSchedule();
                    Integer autoScheduleCustomDays = getModelObject().getAutoScheduleCustomDays();
                    if (autoScheduleType == PriorityCodeAutoScheduleType.CUSTOM && (autoScheduleCustomDays == null || autoScheduleCustomDays < 1 || autoScheduleCustomDays > 1000)) {
                        error(getString("message.invalid_custom_days"));
                        target.add(feedbackPanel);
                        return;
                    }
                    PriorityCode newPriorityCode = AddPriorityCodeForm.this.getModelObject();
                    newPriorityCode.setTenant(FieldIDSession.get().getTenant());
                    onEditComplete(target, newPriorityCode);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
        }
    }

    public void setPriorityCodeModel(IModel<PriorityCode> priorityCode) {
        form.setModel(priorityCode);
    }

    protected void onEditComplete(AjaxRequestTarget target, PriorityCode priorityCode) { }

}
