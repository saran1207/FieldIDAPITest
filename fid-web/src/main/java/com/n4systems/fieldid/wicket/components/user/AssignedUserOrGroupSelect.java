package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.BlankOptionChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.CanHaveEventsAssigned;
import com.n4systems.model.user.UnassignedIndicator;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class AssignedUserOrGroupSelect extends Panel {

    public AssignedUserOrGroupSelect(String id, IModel<CanHaveEventsAssigned> assigneeModel,
             IModel<List<User>> usersModel, IModel<List<UserGroup>> userGroupsModel,
             IModel<List<CanHaveEventsAssigned>> assigneesModel) {

        super(id);

        IChoiceRenderer<CanHaveEventsAssigned> unassignedOrAssigneeRenderer =
                new BlankOptionChoiceRenderer<CanHaveEventsAssigned>(new FIDLabelModel("label.unassigned"),
                        new ListableChoiceRenderer<CanHaveEventsAssigned>(), UnassignedIndicator.UNASSIGNED);

        if (!userGroupsModel.getObject().isEmpty()) {
            add(new GroupedDropDownChoice<CanHaveEventsAssigned, Class>("assigneeSelect", assigneeModel, assigneesModel, unassignedOrAssigneeRenderer) {
                {
                    setNullValid(true);
                    add(new JChosenBehavior());
                }

                @Override
                protected Class getGroup(CanHaveEventsAssigned choice) {
                    // Unfortunately some of these items may be security enhanced. Could we figure out a better way to group these?
                    if (User.class.isAssignableFrom(choice.getClass())) {
                        return User.class;
                    } else if (UserGroup.class.isAssignableFrom(choice.getClass())) {
                        return UserGroup.class;
                    }
                    return UnassignedIndicator.class;
                }

                @Override
                protected String getGroupLabel(Class group) {
                    if (group == User.class) {
                        return getString("label.user");
                    } else if (group == UserGroup.class) {
                        return getString("label.user_group");
                    }
                    return "";
                }
            });
        } else {
            add(new DropDownChoice<CanHaveEventsAssigned>("assigneeSelect", assigneeModel, usersModel, unassignedOrAssigneeRenderer)
                    .setNullValid(true)
                    .add(new JChosenBehavior()));
        }
    }

    public Component setRequired(boolean required) {
        FormComponent assigneeSelect = (FormComponent) get("assigneeSelect");
        assigneeSelect.setRequired(required);
        return this;
    }

}
