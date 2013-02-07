package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.CanHaveEventsAssigned;

import java.util.ArrayList;
import java.util.List;

public class AssigneesModel extends FieldIDSpringModel<List<CanHaveEventsAssigned>> {

    private UserGroupsModel userGroupsModel;
    private ExaminersModel examinersModel;

    public AssigneesModel(UserGroupsModel userGroupsModel, ExaminersModel examinersModel) {
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
