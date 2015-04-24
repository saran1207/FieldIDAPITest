package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.PerformEventPage;
import com.n4systems.fieldid.wicket.pages.event.PerformPlaceEventPage;
import com.n4systems.fieldid.wicket.pages.event.ThingEventSummaryPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ActionDetailsPanel extends Panel {

    @SpringBean
    private PersistenceService persistenceService;

    @SpringBean
    private S3Service s3Service;

    private boolean assetSummaryContext;
    private Class<? extends Event> eventClass;

    public ActionDetailsPanel(String id, final IModel<CriteriaResult> criteriaResultModel, final Class<? extends Event> eventClass, final IModel<Event> actionModel, boolean assetSummaryContext) {
        super(id);
        this.eventClass = eventClass;
        this.assetSummaryContext = assetSummaryContext;
        add(new Label("priority", new PropertyModel<String>(actionModel, "priority.name")));
        add(new Label("notes", new PropertyModel<String>(actionModel, "notes")));
        if (actionModel.getObject().getAssignee() != null) {
            add(new Label("assignee", new PropertyModel<String>(actionModel, "assignee.fullName")));
        } else {
            add(new Label("assignee", new PropertyModel<String>(actionModel, "assignedGroup.name")));
        }
        add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(actionModel, "dueDate"), getCurrentUser().getTimeZone()));
        add(new AjaxLink("actionsListLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onBackToList(target);
            }

            @Override
            public boolean isVisible() {
                return !assetSummaryContext;
            }
        });

        Link startOrViewEventLink = new Link("startOrViewEventLink") {
            @Override
            public void onClick() {
                Event triggerEvent = actionModel.getObject().getTriggerEvent();

                if (actionModel.getObject().getWorkflowState() == WorkflowState.OPEN) {
                    Event action = actionModel.getObject();
                    PageParameters actionParams = new PageParameters().add("type", action.getType().getId()).add("assetId", action.getTarget().getId()).add("scheduleId", action.getId());

                    if(triggerEvent.getType().isThingEventType()) {
                        setResponsePage(PerformEventPage.class, actionParams);
                    } else {
                        setResponsePage(PerformPlaceEventPage.class, actionParams);
                    }
                } else {
                    if(triggerEvent.getType().isThingEventType())
                       setResponsePage(ThingEventSummaryPage.class, PageParametersBuilder.id(actionModel.getObject().getId()));
                    else
                        setResponsePage(PlaceSummaryPage.class, PageParametersBuilder.id(actionModel.getObject().getId()));
                }
            }

            @Override
            public boolean isVisible() {
                return actionModel.getObject().getWorkflowState() != WorkflowState.CLOSED &&
                        (assetSummaryContext || ( actionModel.getObject().isActive() && isStartable(criteriaResultModel)));
            }
        };
        add(startOrViewEventLink);
        startOrViewEventLink.setVisible(actionModel.getObject().getWorkflowState() == WorkflowState.COMPLETED || actionModel.getObject().getWorkflowState() == WorkflowState.OPEN);
        IModel<String> startOrViewLabel = actionModel.getObject().getWorkflowState() ==  WorkflowState.OPEN ? new FIDLabelModel("label.start_action") : new FIDLabelModel("label.view_completed_action");
        startOrViewEventLink.add(new Label("startOrViewEventLabel", startOrViewLabel));

        AjaxLink editLink = new AjaxLink("editLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onEditAction(target, actionModel);
            }

            @Override
            public boolean isVisible() {
                return !assetSummaryContext && isEditable();
            }
        };
        add(editLink);

        add(createIssuingEventSection(criteriaResultModel, actionModel));
    }

    public void onBackToList(AjaxRequestTarget target) { }

    public void onEditAction(AjaxRequestTarget target, IModel<Event> eventModel) { }

    protected boolean isStartable(IModel<CriteriaResult> criteriaResultModel) {
        return criteriaResultModel.getObject().getId() != null;
    }

    protected boolean isEditable() {
        return true;
    }

    private WebMarkupContainer createIssuingEventSection(IModel<CriteriaResult> criteriaResultModel, final IModel<Event> actionModel) {
        WebMarkupContainer issuingEventSection = new WebMarkupContainer("issuingEventSection") {
            @Override
            public boolean isVisible() {
                return assetSummaryContext;
            }
        };

        issuingEventSection.add(new DateTimeLabel("issuingEventDate", new UserToUTCDateModel(new PropertyModel<Date>(actionModel, "triggerEvent.date"))));
        issuingEventSection.add(new Label("issuingEventTypeName", new PropertyModel<String>(actionModel, "triggerEvent.type.name")));
        issuingEventSection.add(new Label("criteriaDescription", new PropertyModel<String>(actionModel, "actionDescription")));

        issuingEventSection.add(new ListView<CriteriaResultImage>("criteriaImages", new PropertyModel<List<CriteriaResultImage>>(criteriaResultModel, "criteriaImages")) {
            @Override
            protected void populateItem(ListItem<CriteriaResultImage> item) {
                item.add(new ExternalImage("thumbnail", s3Service.getCriteriaResultImageThumbnailURL(item.getModelObject()).toString()));
            }
        });

        return issuingEventSection;
    }

    private User getCurrentUser() {
        return persistenceService.find(User.class, FieldIDSession.get().getSessionUser().getUniqueID());
    }
}
