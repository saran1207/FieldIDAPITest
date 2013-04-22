package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PublishPanel extends Panel {

    @SpringBean private ProcedureDefinitionService procedureDefinitionService;

    public PublishPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class",Model.of("publish")));
        add(new PublishForm("publishForm"));

    }

    private class PublishForm extends Form {

        public PublishForm(String id) {
            super(id);
            add(new Label("message", Model.of("are you sure you want to publish this?")));
            add(new AjaxLink("cancel") {
                @Override public void onClick(AjaxRequestTarget target) {
                    doCancel(target);
                }
            });

            add(new SubmitLink("save") {
                @Override
                public void onSubmit() {
                    doSave();
                }

                @Override
                public void onError() {
                    doCancel(null);
                }
            });

            SubmitLink submitLink = new SubmitLink("publish") {
                @Override
                public void onSubmit() {
                    doPublish();
                }

                @Override
                public void onError() {
                    doCancel(null);
                }
            };

            if (procedureDefinitionService.isProcedureApprovalRequiredForCurrentUser()) {
                submitLink.add(new Label("submitLabel", new FIDLabelModel("label.submit_for_approval")));
            } else {
                submitLink.add(new Label("submitLabel", new FIDLabelModel("label.publish")));
            }
            add(submitLink);
        }
    }

    protected void doPublish() {}

    protected void doSave() {}

    protected void doCancel(AjaxRequestTarget target) {}

}
