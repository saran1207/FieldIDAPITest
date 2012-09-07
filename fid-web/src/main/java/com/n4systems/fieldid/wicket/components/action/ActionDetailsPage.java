package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class ActionDetailsPage extends FieldIDAuthenticatedPage {

    public ActionDetailsPage(final IModel<CriteriaResult> criteriaResultModel, final IModel<Event> actionModel) {
        add(new Label("priority", new PropertyModel<String>(actionModel, "priority.name")));
        add(new Label("notes", new PropertyModel<String>(actionModel, "notes")));
        add(new Label("assignee", new PropertyModel<String>(actionModel, "assignee.fullName")));
        add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(actionModel, "nextDate")));
        add(new Link("actionsListLink") {
            @Override
            public void onClick() {
                setResponsePage(new ActionsListPage(criteriaResultModel));
            }
        });

        AjaxLink startEventLink = new AjaxLink("startEventLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                // TODO: This is awful, but we're in a modal window, so not sure how else we can navigate
                // setting a response page just loads that page inside the window which is not the expected behavior
                AbstractEvent event = criteriaResultModel.getObject().getEvent();
                String url = String.format("/fieldid/w/performEvent?type={0}&assetId={1}&scheduleId={2}", event.getType().getId(), event.getAsset().getId(), event.getId());
                target.appendJavaScript("parent.window.location='"+url+"';");
            }
        };
        add(startEventLink);
        startEventLink.setVisible(criteriaResultModel.getObject().getId() != null);

        add(new Link("editLink") {
            @Override
            public void onClick() {
                setResponsePage(new AddEditActionPage(criteriaResultModel, actionModel));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/event_actions.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

}
