package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.ListWithBlankOptionModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUsersModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.PeopleCriteria;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.UnassignedIndicator;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class PeopleDetailsCriteriaPanel extends Panel {

    public PeopleDetailsCriteriaPanel(String id, IModel<? extends PeopleCriteria> criteriaModel) {
        this(id, criteriaModel, true);
    }

    public PeopleDetailsCriteriaPanel(String id, IModel<? extends PeopleCriteria> criteriaModel, boolean includeUnassignedOption) {
        super(id, criteriaModel);

        UsersForTenantModel usersForTenantModel = new UsersForTenantModel();

        IModel<Assignable> assigneeModel = ProxyModel.of(criteriaModel, on(EventReportCriteria.class).getAssignedUserOrGroup());

        VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
        VisibleUsersModel usersModel = new VisibleUsersModel();
        IModel<List<Assignable>> assigneesModel = new AssigneesModel(userGroupsModel, usersModel);

        IModel<List<Assignable>> assignablesWithBlankOptionIfNecessary = assigneesModel;
        if (includeUnassignedOption) {
            assignablesWithBlankOptionIfNecessary = new ListWithBlankOptionModel(assigneesModel, UnassignedIndicator.UNASSIGNED);
        }

        add(new AssignedUserOrGroupSelect("assignee",
                createWrappedModel(criteriaModel, assigneeModel),
                usersModel, userGroupsModel,
                assignablesWithBlankOptionIfNecessary).setRenderBodyOnly(true));

        //We should be using new ListableChoiceRenderer<User>() here but there is seems to be a hibernate proxy problem :(
        add(new FidDropDownChoice<User>("performedBy", usersForTenantModel, new IChoiceRenderer<User>() {
            @Override
            public Object getDisplayValue(User object) {
                return object.getDisplayName();
            }

            @Override
            public String getIdValue(User object, int index) {
                return object.getID() + "";
            }
        }).setNullValid(true));
    }

    private IModel<Assignable> createWrappedModel(final IModel<? extends PeopleCriteria> criteriaModel, final IModel<Assignable> assigneeModel) {
        return new IModel<Assignable>() {
            @Override
            public Assignable getObject() {
                if (criteriaModel.getObject().isUnassignedOnly()) {
                    return UnassignedIndicator.UNASSIGNED;
                }
                return assigneeModel.getObject();
            }

            @Override
            public void setObject(Assignable object) {
                assigneeModel.setObject(object);
            }

            @Override
            public void detach() {
                assigneeModel.detach();
            }
        };
    }

}
