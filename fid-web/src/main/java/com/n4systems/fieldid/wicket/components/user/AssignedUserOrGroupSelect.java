package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.ExaminersModel;
import com.n4systems.fieldid.wicket.model.user.UserGroupsModel;
import com.n4systems.model.user.CanHaveEventsAssigned;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AssignedUserOrGroupSelect extends Panel {

    public AssignedUserOrGroupSelect(String id, IModel<CanHaveEventsAssigned> assigneeModel, boolean allowGroupSelection) {
        super(id);

        ExaminersModel examinersModel = new ExaminersModel();
        UserGroupsModel userGroupsModel = new UserGroupsModel();

        AssigneesModel assigneesModel = new AssigneesModel(userGroupsModel, examinersModel);

        if (allowGroupSelection && !userGroupsModel.getObject().isEmpty()) {
            add(new GroupedDropDownChoice<CanHaveEventsAssigned, Class>("assigneeSelect", assigneeModel, assigneesModel, new ListableChoiceRenderer<CanHaveEventsAssigned>()) {
                {
                    setNullValid(true);
                    add(new JChosenBehavior());
                }
                @Override
                protected Class getGroup(CanHaveEventsAssigned choice) {
                    return choice.getClass();
                }

                @Override
                protected String getGroupLabel(Class group) {
                    if (group.equals(User.class)) {
                        return getString("label.user");
                    }
                    return getString("label.user_group");
                }
            });
        } else {
            add(new DropDownChoice<CanHaveEventsAssigned>("assigneeSelect", new PropertyModel<CanHaveEventsAssigned>(this, "assignee"), examinersModel, new ListableChoiceRenderer<CanHaveEventsAssigned>())
                    .setNullValid(true)
                    .add(new JChosenBehavior()));
        }
    }

}
