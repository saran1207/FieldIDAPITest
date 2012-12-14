package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.components.asset.events.table.EventStateIcon;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.security.Permissions;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;
import java.util.List;

public class ActionsListPage extends FieldIDAuthenticatedPage {

    protected boolean readOnly = false;

    public ActionsListPage(final IModel<CriteriaResult> criteriaResultModel) {
        this(criteriaResultModel, false);
    }

    public ActionsListPage(final IModel<CriteriaResult> criteriaResultModel, boolean readOnly) {
        this.readOnly = readOnly;
        add(new ContextImage("blankSlate", "images/add-action-slate.png") {
            @Override public boolean isVisible() {
                return criteriaResultModel.getObject().getActions().size()==0;
            }
        });

        add(new ListView<Event>("actionsList", new PropertyModel<List<? extends Event>>(criteriaResultModel, "actions")) {
            @Override
            protected void populateItem(final ListItem<Event> item) {
                item.add(new EventStateIcon("eventStateIcon", item.getModel()));
                item.add(new Label("actionType", new PropertyModel<String>(item.getModel(), "type.name")));
                item.add(new Label("assignee", new PropertyModel<String>(item.getModel(), "assignee.fullName")));
                item.add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(item.getModel(), "dueDate"), getCurrentUser().getTimeZone()));
                item.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                        setResponsePage(new ActionDetailsPage(criteriaResultModel, item.getModel()) {
                            @Override protected boolean isEditable() {
                                return !isReadOnly();
                            }
                            @Override protected boolean isStartable(IModel<CriteriaResult> criteriaResultModel) {
                                return super.isStartable(criteriaResultModel) && isReadOnly();
                            }
                            @Override protected void setActionsListResponsePage(IModel<CriteriaResult> criteriaResultModel) {
                                ActionsListPage.this.setActionsListResponsePage(criteriaResultModel);
                            }
                        });
                    }
                });
            }
        });

        WebMarkupContainer issueActionSection = new WebMarkupContainer("issueActionSection");
        issueActionSection.setVisible(!isReadOnly());

        add(issueActionSection);
        issueActionSection.add(new Link<Void>("addActionLink") {
            @Override
            public void onClick() {
                setResponsePage(new AddEditActionPage(criteriaResultModel));
            }
        });
        issueActionSection.add(new AjaxLink("finishedLink") {
            @Override public void onClick(AjaxRequestTarget target) {
                clickFinished(target);
            }
        });
    }

    protected void clickFinished(AjaxRequestTarget target) {
        // simulate click on close button.  uggh. workaround with wicket modal dialog.
        // this code pretty much prevents this page from being used outside a modal window
        target.appendJavaScript("parent.$('.w_close').click();");
    }

    protected void setActionsListResponsePage(IModel<CriteriaResult> criteriaResultModel) {
        setResponsePage(new ActionsListPage(criteriaResultModel));
    }

    protected boolean isReadOnly() {
        return readOnly || !Permissions.hasAllOf(getCurrentUser(), Permissions.CreateEvent);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/event_actions.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

}
