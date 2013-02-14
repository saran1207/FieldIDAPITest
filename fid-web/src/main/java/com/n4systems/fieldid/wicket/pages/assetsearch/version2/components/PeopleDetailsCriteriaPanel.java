package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.ListWithBlankOptionModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUsersModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.user.CanHaveEventsAssigned;
import com.n4systems.model.user.UnassignedIndicator;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class PeopleDetailsCriteriaPanel extends Panel {

    public PeopleDetailsCriteriaPanel(String id, IModel<EventReportCriteria> criteriaModel) {
        super(id, criteriaModel);

        UsersForTenantModel usersForTenantModel = new UsersForTenantModel();

        IModel<CanHaveEventsAssigned> assigneeModel = ProxyModel.of(criteriaModel, on(EventReportCriteria.class).getAssignedUserOrGroup());

        VisibleUserGroupsModel userGroupsModel = new VisibleUserGroupsModel();
        VisibleUsersModel usersModel = new VisibleUsersModel();
        IModel<List<CanHaveEventsAssigned>> assigneesModel = new AssigneesModel(userGroupsModel, usersModel);

        ListWithBlankOptionModel blankOptionUserList = new ListWithBlankOptionModel(assigneesModel, UnassignedIndicator.UNASSIGNED);

        add(new AssignedUserOrGroupSelect("assignee",
                createWrappedModel(criteriaModel, assigneeModel),
                usersModel, userGroupsModel,
                blankOptionUserList).setRenderBodyOnly(true));

        add(new FidDropDownChoice<User>("performedBy", usersForTenantModel, new ListableChoiceRenderer<User>()).setNullValid(true));
    }

    private IModel<CanHaveEventsAssigned> createWrappedModel(final IModel<EventReportCriteria> criteriaModel, final IModel<CanHaveEventsAssigned> assigneeModel) {
        return new IModel<CanHaveEventsAssigned>() {
            @Override
            public CanHaveEventsAssigned getObject() {
                if (criteriaModel.getObject().isUnassignedOnly()) {
                    return UnassignedIndicator.UNASSIGNED;
                }
                return assigneeModel.getObject();
            }

            @Override
            public void setObject(CanHaveEventsAssigned object) {
                assigneeModel.setObject(object);
            }

            @Override
            public void detach() {
                assigneeModel.detach();
            }
        };
    }

}
