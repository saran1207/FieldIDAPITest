package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.ConfirmNavigationBehavior;
import com.n4systems.fieldid.wicket.behavior.DisableButtonBeforeSubmit;
import com.n4systems.fieldid.wicket.behavior.JavaScriptAlertConfirmBehavior;
import com.n4systems.fieldid.wicket.behavior.validation.DisableNavigationConfirmationBehavior;
import com.n4systems.fieldid.wicket.behavior.validation.RequiredCriteriaValidator;
import com.n4systems.fieldid.wicket.components.Comment;
import com.n4systems.fieldid.wicket.components.event.EventFormEditPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.fileupload.AttachmentsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.event.target.AssetDetailsPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public abstract class SubEventPage extends FieldIDTemplatePage {

    @SpringBean
    EventTypeService eventTypeService;

    @SpringBean
    AssetService assetService;

    protected List<AbstractEvent.SectionResults> sectionResults;
    protected List<FileAttachment> fileAttachments;

    protected IModel<ThingEvent> masterEvent;
    protected IModel<SubEvent> event;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        sectionResults = event.getObject().getSectionResults();
        add(new FIDFeedbackPanel("feedbackPanel"));

        add(new OuterEventForm("outerEventForm"));

        add(new ConfirmNavigationBehavior(new FIDLabelModel("message.confirm_navigation")));
    }

    class OuterEventForm extends Form {

        public OuterEventForm(String id) {
            super(id);

            add(new AssetDetailsPanel("targetDetailsPanel", ProxyModel.of(event, on(SubEvent.class).getAsset())));

            add(new Comment("comments", new PropertyModel<String>(event, "comments")).addMaxLengthValidation(5000));

            EventForm form = event.getObject().getEventForm();
            add(new EventFormEditPanel("eventFormPanel",
                    event,
                    new PropertyModel<List<AbstractEvent.SectionResults>>(SubEventPage.this, "sectionResults"),
                    isActionButtonsVisible()).setVisible(form != null && form.getAvailableSections().size() > 0));
            //TODO set to visible once EventCreationService can save sub event attachments
            add(new AttachmentsPanel("attachmentsPanel", new PropertyModel<List<FileAttachment>>(SubEventPage.this, "fileAttachments")).setVisible(false));

            Button saveButton = new Button("saveButton");
            saveButton.add(new DisableNavigationConfirmationBehavior());
            saveButton.add(new DisableButtonBeforeSubmit());
            add(saveButton);

            add(new Link<Void>("cancelLink") {
                @Override
                public void onClick() {
                    if (masterEvent.getObject().isNew()) {
                        setResponsePage(new PerformMasterEventPage(masterEvent));
                    } else {
                        setResponsePage(new EditMasterEventPage(masterEvent));
                    }
                }
            });
            add(createDeleteLink("deleteLink"));
        }

        @Override
        protected void onValidate() {
            List<AbstractEvent.SectionResults> results =  event.getObject().getSectionResults();

            RequiredCriteriaValidator.validate(results).stream().forEach(message -> error(message));
        }

        @Override
        protected void onSubmit() {
            event.getObject().setSectionResults(sectionResults);
            //TODO refactor EventCreationService to save sub event attachments after sub events are persisted
            //event.getObject().setAttachments(fileAttachments);
            onSave();
        }
    }

    protected abstract void onSave();

    protected Link createDeleteLink(String linkId) {
        return new Link(linkId) {
            {
                add(new JavaScriptAlertConfirmBehavior(new FIDLabelModel("label.confirm_event_delete")));
                if (event.getObject().isNew()) {
                    setVisible(false);
                } else if (masterEvent.getObject().getWorkflowState().equals(WorkflowState.OPEN)) {
                    setVisible(false);
                } else {
                    setVisible(true);
                }

            }

            @Override
            public void onClick() {
                masterEvent.getObject().getSubEvents().remove(event.getObject());
                FieldIDSession.get().info(getString("message.eventdeleted"));
                if (masterEvent.getObject().isNew()) {
                    setResponsePage(new PerformMasterEventPage(masterEvent));
                } else {
                    setResponsePage(new EditMasterEventPage(masterEvent));
                }
            }
        };
    }

    private boolean isActionButtonsVisible() { return true; }

}
