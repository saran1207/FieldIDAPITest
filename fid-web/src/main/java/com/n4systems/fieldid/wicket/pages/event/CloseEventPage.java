package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunLastReportPage;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventStatus;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class CloseEventPage extends FieldIDFrontEndPage {

    private @SpringBean EventStatusService eventStatusService;
    private @SpringBean UserService userService;
    private @SpringBean PersistenceService persistenceService;

    protected IModel<Event> openEventModel;
    private FieldIDFrontEndPage returnPage;

    public CloseEventPage(PageParameters params) {
        super(params);
        Long id = params.get("uniqueID").toLong();
        openEventModel = new EntityModel<Event>(Event.class, id);
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new Label("event", PropertyModel.of(openEventModel, "type.name")));
        add(new ResolveForm("form"));
    }

    public CloseEventPage(PageParameters params, FieldIDFrontEndPage returnPage) {
        this(params);
        this.returnPage = returnPage;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/resolve.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

    @Override
    protected boolean useLegacyCss() {
        return false;
    }

    class ResolveForm extends Form {

        private EventStatus status;
        private String comment;
        private User resolvedBy = getCurrentUser();

        public ResolveForm(String id) {
            super(id);
            resolvedBy = getCurrentUser();
            List<EventStatus> activeStatuses = getActiveStatuses();
            add(new Label("due", new PropertyModel<String>(openEventModel, "nextDate")));
            add(new Label("state", new PropertyModel<String>(openEventModel, "eventState")));
            add( new DropDownChoice<EventStatus>("status",
                    new PropertyModel<EventStatus>(this, "status"),
                    activeStatuses,
                    new ChoiceRenderer<EventStatus>("name")).setNullValid(false).setRequired(true).add(new JChosenBehavior()));

            add( new DropDownChoice<User>("resolver",
                    new PropertyModel<User>(this, "resolvedBy"),
                    getUsers(),
                    new ChoiceRenderer<User>("displayName")).setNullValid(false).setRequired(true).add(new JChosenBehavior()));

            add(new TextArea<String>("comment", new PropertyModel<String>(this, "comment")));

            add(new Button("closeButton"));

            add(new Link<Void>("cancelLink") {
                @Override
                public void onClick() {
                    if (returnPage!=null) {
                        setResponsePage(returnPage);
                    } else {
                        setResponsePage(RunLastReportPage.class);
                    }
                }
            });

            if (activeStatuses.size()>0) {
                status = activeStatuses.get(0);
            }
        }

        @Override
        protected void onSubmit() {
            Event openEvent = openEventModel.getObject();
            Asset asset = openEvent.getAsset();
            openEvent.setEventState(Event.EventState.CLOSED);
            openEvent.setDate(new Date());
            openEvent.setPerformedBy(resolvedBy);
            openEvent.setEventStatus(status);
            openEvent.setComments(comment);
            openEvent.setOwner(asset.getOwner());
            openEvent.setAdvancedLocation(asset.getAdvancedLocation());
            openEvent.setAssignedTo(AssignedToUpdate.assignAssetToUser(asset.getAssignedUser()));
            persistenceService.update(openEvent);
            FieldIDSession.get().info(getString("message.event_closed"));
            if (returnPage!=null) {
                setResponsePage(returnPage);
            } else {
                setResponsePage(ReportPage.class);
            }
        }


    }


    private List<User> getUsers() {
        return userService.getUsers(false, false);
    }

    private List<EventStatus> getActiveStatuses() {
        return eventStatusService.getActiveStatuses();
    }


}
