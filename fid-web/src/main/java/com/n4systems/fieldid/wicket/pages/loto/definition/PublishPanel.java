package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PublishPanel extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;
    @SpringBean
    private ProcedureService procedureService;

    @SpringBean
    private UserService userService;
    private String rejectionText;
    private IModel<ProcedureDefinition> model;

    public PublishPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);
        this.model = model;
        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class",Model.of("publish")));
        add(new FIDFeedbackPanel("feedbackPanel"));
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
            }.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.save"), TipsyBehavior.Gravity.N)));

            final boolean isWaitingForApproval = model.getObject().getPublishedState() == PublishedState.WAITING_FOR_APPROVAL && procedureDefinitionService.canCurrentUserApprove();

            SubmitLink submitLink = new SubmitLink("publish") {
                @Override
                public void onSubmit() {
                    if (model.getObject().getLockIsolationPoints().isEmpty()) {
                        error(getString("message.isolation_point_required"));
                    } else if (procedureService.hasActiveProcedure(model.getObject().getAsset())) {
                        error(getString("message.cant_publish_with_active_procedure"));
                    } else if (procedureDefinitionService.hasPublishedProcedureCode(model.getObject())) {
                        error(new FIDLabelModel("message.procedure_code_exists", model.getObject().getProcedureCode()).getObject());
                    } else {
                        if (isWaitingForApproval || procedureDefinitionService.canCurrentUserApprove()) {
                            model.getObject().setApprovedBy(getCurrentUser());
                        }
                        doPublish();
                    }
                }

                @Override
                public void onError() {
                    doCancel(null);
                }
            };

            submitLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.publish"), TipsyBehavior.Gravity.N));

            if (procedureDefinitionService.isProcedureApprovalRequiredForCurrentUser()) {
                submitLink.add(new FlatLabel("submitLabel", new FIDLabelModel("label.submit_for_approval")));
            } else {
                submitLink.add(new FlatLabel("submitLabel", new FIDLabelModel("label.publish")));
            }
            add(submitLink);

            final WebMarkupContainer rejectionMessageContainer = new WebMarkupContainer("rejectionMessageContainer");
            rejectionMessageContainer.setOutputMarkupPlaceholderTag(true).setVisible(false);

            AjaxLink openRejectionMessageLink = new AjaxLink("openRejectionPanelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    target.add(rejectionMessageContainer.setVisible(true));
                }
            };
            openRejectionMessageLink.setVisible(isWaitingForApproval);

            add(openRejectionMessageLink);

            SubmitLink rejectLink = new SubmitLink("rejectLink") {
                @Override
                public void onSubmit() {
                    doReject(rejectionText);
                }
            };

            rejectionMessageContainer.add(new TextArea<String>("rejectionMessage", new PropertyModel<String>(PublishPanel.this, "rejectionText")));
            rejectionMessageContainer.add(rejectLink);
            rejectionMessageContainer.add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    target.add(rejectionMessageContainer.setVisible(false));
                }
            });

            add(rejectionMessageContainer);
        }
    }

    private User getCurrentUser() {
        return userService.getUser(FieldIDSession.get().getSessionUser().getUniqueID());
    }

    protected void doPublish() {}

    protected void doSave() {}

    protected void doCancel(AjaxRequestTarget target) {}

    protected void doReject(String message) {}

}
