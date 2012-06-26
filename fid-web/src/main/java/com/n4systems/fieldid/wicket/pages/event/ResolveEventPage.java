package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventStatus;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ResolveEventPage extends FieldIDFrontEndPage {

    private @SpringBean EventService eventService;
    private @SpringBean EventStatusService eventStatusService;
    private @SpringBean UserService userService;

    private EventSchedule schedule;



    public ResolveEventPage(EventSchedule eventSchedule) {
        initializeEvent(eventSchedule);
    }

    private void initializeEvent(EventSchedule event) {
        this.schedule = event;
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new Label("event", Model.of("someEventName")));
        add(new ResolveForm("form"));
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
        private User resolvedBy;

        public ResolveForm(String id) {
            super(id);
            add(new Label("due", new PropertyModel<String>(schedule, "nextDate")));
            add(new Label("state", new PropertyModel<String>(schedule, "status")));
            add( new DropDownChoice<EventStatus>("status",
                    new PropertyModel<EventStatus>(this, "status"),
                    getEventStatus(),
                    new ChoiceRenderer<EventStatus>("name")).add(new JChosenBehavior()));

            add( new DropDownChoice<User>("resolver",
                    new PropertyModel<User>(this, "resolvedBy"),
                    getUsers(),
                    new ChoiceRenderer<User>("displayName")).add(new JChosenBehavior()));

            add(new TextArea<String>("comment", new PropertyModel<String>(this, "comment")));
        }
    }

    private List<User> getUsers() {
        return userService.getUsers(false,false);
    }

    private List<EventStatus> getEventStatus() {
        return eventStatusService.getActiveStatuses();
    }


}
