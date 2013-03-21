package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class AssigneesModel extends FieldIDSpringModel<List<Assignable>> {

    private IModel<List<UserGroup>> userGroupsModel;
    private IModel<List<User>> examinersModel;

    public AssigneesModel(IModel<List<UserGroup>> userGroupsModel, IModel<List<User>> examinersModel) {
        this.userGroupsModel = userGroupsModel;
        this.examinersModel = examinersModel;
    }

    @Override
    protected List<Assignable> load() {
        List<Assignable> assignees = new ArrayList<Assignable>();
        assignees.addAll(userGroupsModel.getObject());
        assignees.addAll(examinersModel.getObject());
        return assignees;
    }

}
