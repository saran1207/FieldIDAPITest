package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.components.asset.events.table.EventStateIcon;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ActionsListPanel extends Panel {

    @SpringBean
    private PersistenceService persistenceService;

    protected Class<? extends Event> eventClass;
    protected boolean readOnly = false;

    public ActionsListPanel(String id, final IModel<CriteriaResult> criteriaResultModel, Class<? extends Event> eventClass) {
        this(id, criteriaResultModel, eventClass, false);
    }

    public ActionsListPanel(String id, IModel<CriteriaResult> criteriaResultModel, Class<? extends Event> eventClass, boolean readOnly) {
        super(id, criteriaResultModel);
        this.eventClass = eventClass;
        this.readOnly = readOnly;

        add(new ContextImage("blankSlate", "images/add-action-slate.png") {
            @Override
            public boolean isVisible() {
                return criteriaResultModel.getObject().getActions().size()==0;
            }
        });

        add(new ListView<Event>("actionsList", new PropertyModel<List<Event>>(criteriaResultModel, "actions")) {
            @Override
            protected void populateItem(final ListItem<Event> item) {
                item.add(new EventStateIcon("eventStateIcon", item.getModel()));
                item.add(new Label("actionType", new PropertyModel<String>(item.getModel(), "type.name")));
                if (item.getModelObject().getAssignee() != null) {
                    item.add(new Label("assignee", new PropertyModel<String>(item.getModel(), "assignee.fullName")));
                } else {
                    item.add(new Label("assignee", new PropertyModel<String>(item.getModel(), "assignedGroup.name")));
                }
                item.add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(item.getModel(), "dueDate"), getCurrentUser().getTimeZone()));
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        onShowDetailsPanel(target, item.getModel());
                    }
                });
            }
        });

        WebMarkupContainer issueActionSection = new WebMarkupContainer("issueActionSection");
        issueActionSection.setVisible(!isReadOnly());

        add(issueActionSection);
        issueActionSection.add(new AjaxLink<Void>("addActionLink") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                onAddAction(ajaxRequestTarget);
            }
        });

        issueActionSection.add(new AjaxLink("finishedLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                clickFinished(target);
            }
        });
    }

    protected boolean isReadOnly() {
        return readOnly || !Permissions.hasAllOf(getCurrentUser(), Permissions.CreateEvent);
    }

    protected void clickFinished(AjaxRequestTarget target) {
        target.appendJavaScript("parent.$('.w_close').click();");
    }

    private User getCurrentUser() {
        return persistenceService.find(User.class, FieldIDSession.get().getSessionUser().getUniqueID());
    }

    public void onAddAction(AjaxRequestTarget ajaxRequestTarget) {}

    public void onShowDetailsPanel(AjaxRequestTarget target, IModel<Event> eventModel) {}
}
