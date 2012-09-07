package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.security.Permissions;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

public class ActionsListPage extends FieldIDAuthenticatedPage {

    public ActionsListPage(final IModel<CriteriaResult> criteriaResultModel) {
        add(new ListView<Event>("actionsList", new PropertyModel<List<? extends Event>>(criteriaResultModel, "actions")) {
            @Override
            protected void populateItem(final ListItem<Event> item) {
                item.add(new Label("actionType", new PropertyModel<String>(item.getModel(), "type.name")));
                item.add(new Label("assignee", new PropertyModel<String>(item.getModel(), "assignee.fullName")));
                item.add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(item.getModel(), "nextDate")));
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        setResponsePage(new ActionDetailsPage(criteriaResultModel, item.getModel()));
                    }
                });
            }
        });

        WebMarkupContainer issueActionSection = new WebMarkupContainer("issueActionSection");
        issueActionSection.setVisible(Permissions.hasAllOf(getCurrentUser(), Permissions.CreateEvent));

        add(issueActionSection);
        issueActionSection.add(new Link<Void>("addActionLink") {
            @Override
            public void onClick() {
                setResponsePage(new AddEditActionPage(criteriaResultModel));
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
