package com.n4systems.fieldid.wicket.components.massupdate.openevent;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.model.Event;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class AssignDetailsPanel extends AbstractMassUpdatePanel {
    
    public AssignDetailsPanel(String id, final IModel<EventReportCriteria> criteriaModel, AbstractMassUpdatePanel previousPanel) {
        super(id, criteriaModel);

        this.previousPanel = previousPanel;
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AssignEventForm("form", new MassUpdateEventModel()));
    }
    
    class AssignEventForm extends Form<MassUpdateEventModel> {

        public AssignEventForm(String id, MassUpdateEventModel massUpdateEventModel) {
            super(id, new CompoundPropertyModel<MassUpdateEventModel>(massUpdateEventModel));

            add( new DropDownChoice<User>("assignee",
                    new PropertyModel<User>(massUpdateEventModel, "event.assignee"),
                    getUsers(),
                    new ChoiceRenderer<User>("assignToDisplayName")).setNullValid(false).setRequired(true).add(new JChosenBehavior()));

            add(new Button("assignButton"));

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
                model.getSelect().put("assignee", true);
                onNext(model);
            }	else {
                error(errorMessage);
            }
        }
    }

    private String checkRequiredFields(MassUpdateEventModel model) {
        String errorMessage = "";
        Event event = model.getEvent();

        if(event.getAssignee() == null) {
            return new FIDLabelModel("errors.required",new FIDLabelModel("title.assignee").getObject()).getObject();
        }

        return errorMessage;
    }

    private List<User> getUsers() {
        return userService.getUsers(false, false);
    }

    protected void onNext(MassUpdateEventModel massUpdateEventModel) {};

    @Override
    public boolean isDetailsPanel() {
        return true;
    }
}