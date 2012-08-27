package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.AssignToChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.model.IModel;

import java.util.List;

public class GroupedUserPicker extends GroupedDropDownChoice<User, BaseOrg> {

    public GroupedUserPicker(String id, IModel<User> userModel, IModel<List<User>> usersModel, boolean useJChosen) {
        super(id, userModel, usersModel, new AssignToChoiceRenderer());
        if (useJChosen) {
            add(new JChosenBehavior());
        }

    }
    public GroupedUserPicker(String id, IModel<User> userModel, IModel<List<User>> usersModel) {
        this(id, userModel, usersModel, true);
    }

    @Override
    protected BaseOrg getGroup(User choice) {
        return choice.getOwner();
    }

    @Override
    protected String getGroupLabel(BaseOrg group) {
        return group.getDisplayName();
    }

    @Override
    protected User convertChoiceIdToChoice(String id) {
        return super.convertChoiceIdToChoice(id);
    }
}
