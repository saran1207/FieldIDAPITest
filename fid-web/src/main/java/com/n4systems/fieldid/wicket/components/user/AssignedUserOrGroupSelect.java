package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.BlankOptionChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.UnassignedIndicator;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AssignedUserOrGroupSelect extends Panel {

    public AssignedUserOrGroupSelect(String id, IModel<Assignable> assigneeModel,
             IModel<List<User>> usersModel, IModel<List<UserGroup>> userGroupsModel,
             IModel<List<Assignable>> assigneesModel) {

        super(id);

        //We should be using new ListableChoiceRenderer<Assignable>() here but there is seems to be a hibernate proxy problem :(
        IChoiceRenderer<Assignable> wrappedRenderer = new IChoiceRenderer<Assignable>() {
            @Override
            public Object getDisplayValue(Assignable object) {
                return object.getDisplayName();
            }

            @Override
            public String getIdValue(Assignable object, int index) {
                if(object.getId() == null) {
                    return ((User)object).getId() + "";
                } else
                return object.getId() + "";
            }
        };

        IChoiceRenderer<Assignable> unassignedOrAssigneeRenderer =
                new BlankOptionChoiceRenderer<Assignable>(new FIDLabelModel("label.unassigned"), wrappedRenderer, UnassignedIndicator.UNASSIGNED);

        if (!userGroupsModel.getObject().isEmpty()) {
            add(new GroupedDropDownChoice<Assignable, Class>("assigneeSelect", assigneeModel, assigneesModel, unassignedOrAssigneeRenderer) {
                {
                    setNullValid(true);
                }

                @Override
                protected void onInitialize() {
                    super.onInitialize();
                    add(new JChosenBehavior());
                }

                @Override
                protected Class getGroup(Assignable choice) {
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
            add(new DropDownChoice<Assignable>("assigneeSelect", assigneeModel, usersModel, unassignedOrAssigneeRenderer)
                    .setNullValid(true)
                    .add(new JChosenBehavior()));
        }
    }

    public Component setRequired(boolean required) {
        FormComponent assigneeSelect = (FormComponent) get("assigneeSelect");
        assigneeSelect.setRequired(required);
        return this;
    }

    public Component setNullVoid(boolean nullvoid) {
        DropDownChoice assigneeSelect = (DropDownChoice) get("assigneeSelect");
        assigneeSelect.setNullValid(nullvoid);
        return this;
    }

    public Component addBehavior(Behavior behavior) {
        DropDownChoice assigneeSelect = (DropDownChoice) get("assigneeSelect");
        assigneeSelect.add(behavior);
        return this;
    }


}
