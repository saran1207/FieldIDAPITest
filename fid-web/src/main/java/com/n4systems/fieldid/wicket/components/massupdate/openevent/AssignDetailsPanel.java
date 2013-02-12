package com.n4systems.fieldid.wicket.components.massupdate.openevent;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.massupdate.AbstractMassUpdatePanel;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.MassUpdateEventModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.UserGroupsModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Event;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.List;

import static ch.lambdaj.Lambda.on;

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

            ExaminersModel usersModel = new ExaminersModel();
            UserGroupsModel userGroupsModel = new UserGroupsModel();
            add(new AssignedUserOrGroupSelect("assignee",
                    ProxyModel.of(massUpdateEventModel, on(MassUpdateEventModel.class).getEvent().getAssignedUserOrGroup()),
                    usersModel, userGroupsModel,
                    new AssigneesModel(userGroupsModel, usersModel)).setRequired(true));

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

        if (event.getAssignedUserOrGroup() == null) {
            return new FIDLabelModel("errors.required",new FIDLabelModel("label.assignee").getObject()).getObject();
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