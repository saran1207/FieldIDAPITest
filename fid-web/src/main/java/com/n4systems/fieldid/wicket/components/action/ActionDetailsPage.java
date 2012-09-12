package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ActionDetailsPage extends FieldIDAuthenticatedPage {

    @SpringBean
    private S3Service s3Service;

    public ActionDetailsPage(final IModel<CriteriaResult> criteriaResultModel, final IModel<Event> actionModel) {
        add(new Label("priority", new PropertyModel<String>(actionModel, "priority.name")));
        add(new Label("notes", new PropertyModel<String>(actionModel, "notes")));
        add(new Label("assignee", new PropertyModel<String>(actionModel, "assignee.fullName")));
        add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(actionModel, "nextDate")));
        Link actionsListLink = new Link("actionsListLink") {
            @Override public void onClick() {
                setActionsListResponsePage(criteriaResultModel);
            }
        };
        add(actionsListLink);

        AjaxLink startEventLink = new AjaxLink("startEventLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                // TODO: This is awful, but we're in a modal window, so not sure how else we can navigate
                // setting a response page just loads that page inside the window which is not the expected behavior

                String url = String.format("/fieldid/w/performEvent?type=%d&assetId=%d&scheduleId=%d", actionModel.getObject().getType().getId(), actionModel.getObject().getId(), actionModel.getObject().getId());
                target.appendJavaScript("parent.window.location='"+url+"';");
            }
        };
        add(startEventLink);
        startEventLink.setVisible(isStartable(criteriaResultModel));

        Link editLink = new Link("editLink") {
            @Override public void onClick() {
                setResponsePage(new AddEditActionPage(criteriaResultModel, actionModel));
            }
        };
        add(editLink);
        editLink.setVisible(isEditable());
        add(createIssuingEventSection(criteriaResultModel, actionModel));
    }

    protected void setActionsListResponsePage(IModel<CriteriaResult> criteriaResultModel) {
        setResponsePage(new ActionsListPage(criteriaResultModel));
    }

    protected boolean isStartable(IModel<CriteriaResult> criteriaResultModel) {
        return criteriaResultModel.getObject().getId() != null;
    }

    protected boolean isEditable() {
        return true;
    }

    private WebMarkupContainer createIssuingEventSection(IModel<CriteriaResult> criteriaResultModel, final IModel<Event> actionModel) {
        WebMarkupContainer issuingEventSection = new WebMarkupContainer("issuingEventSection");

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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/event_actions.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

}
