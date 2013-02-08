package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.CanHaveEventsAssigned;
import com.n4systems.model.user.UnassignedIndicator;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class AssigneesModel extends FieldIDSpringModel<List<CanHaveEventsAssigned>> {

    private IModel<List<UserGroup>> userGroupsModel;
    private IModel<List<User>> examinersModel;

    public AssigneesModel(IModel<List<UserGroup>> userGroupsModel, IModel<List<User>> examinersModel) {
        this.userGroupsModel = userGroupsModel;
        this.examinersModel = examinersModel;
    }

    @Override
    protected List<CanHaveEventsAssigned> load() {
        List<CanHaveEventsAssigned> assignees = new ArrayList<CanHaveEventsAssigned>();
        assignees.addAll(userGroupsModel.getObject());
        assignees.addAll(examinersModel.getObject());
        return assignees;
    }

}
