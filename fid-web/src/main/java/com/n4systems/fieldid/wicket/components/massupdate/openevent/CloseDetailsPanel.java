package com.n4systems.fieldid.wicket.components.massupdate.openevent;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.model.Event;
import com.n4systems.model.EventStatus;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class CloseDetailsPanel extends AbstractMassUpdatePanel {

    @SpringBean
    private EventStatusService eventStatusService;

    @SpringBean
    private PersistenceService persistenceService;

    public CloseDetailsPanel(String id, final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel) {
        super(id, criteriaModel);

        this.previousPanel = previousPanel;
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new CloseEventForm("form", new MassUpdateEventModel()));

    }

    class CloseEventForm extends Form<MassUpdateEventModel> {

        private User resolvedBy = getCurrentUser();

        public CloseEventForm(String id, MassUpdateEventModel massUpdateEventModel) {
            super(id, new CompoundPropertyModel<MassUpdateEventModel>(massUpdateEventModel));
            Event event = massUpdateEventModel.getEvent();
            event.setPerformedBy(getCurrentUser());
            event.setEventStatus(getActiveStatuses().get(0));

            List<EventStatus> activeStatuses = getActiveStatuses();
            add( new DropDownChoice<EventStatus>("status",
                    new PropertyModel<EventStatus>(massUpdateEventModel, "event.eventStatus"),
                    activeStatuses,
                    new ChoiceRenderer<EventStatus>("name")).setNullValid(false).setRequired(true).add(new JChosenBehavior()));

            add( new DropDownChoice<User>("resolver",
                    new PropertyModel<User>(massUpdateEventModel, "event.performedBy"),
                    getUsers(),
                    new ChoiceRenderer<User>("displayName")).setNullValid(false).setRequired(true).add(new JChosenBehavior()));

            add(new TextArea<String>("comment", new PropertyModel<String>(massUpdateEventModel, "event.comments")));

            add(new Button("closeButton"));

            add(new Link<Void>("cancelLink") {
                @Override
                public void onClick() {
                    onCancel();
                }
            });
        }

        @Override
        protected void onSubmit() {
            MassUpdateEventModel model = (MassUpdateEventModel) this.getDefaultModelObject();

            String errorMessage = checkRequiredFields(model);
            if(errorMessage.isEmpty()) {
                onNext(model);
            }	else {
                error(errorMessage);
            }
        }

    }

    private String checkRequiredFields(MassUpdateEventModel model) {
		String errorMessage = "";
        Event event = model.getEvent();

        if(event.getEventStatus() == null) {
            return new FIDLabelModel("errors.required",new FIDLabelModel("label.eventstatus").getObject()).getObject();
		}
        if(event.getPerformedBy() == null) {
            return new FIDLabelModel("errors.required",new FIDLabelModel("label.closedby").getObject()).getObject();
        }

		return errorMessage;
	}

    private List<User> getUsers() {
        return userService.getUsers(false, false);
    }

    private List<EventStatus> getActiveStatuses() {
        return eventStatusService.getActiveStatuses();
    }

    public User getCurrentUser() {
        return persistenceService.find(User.class, FieldIDSession.get().getSessionUser().getUniqueID());
    }

    @Override
    public boolean isDetailsPanel() {
        return true;
    }

    protected void onNext(MassUpdateEventModel massUpdateEventModel) {};

}
