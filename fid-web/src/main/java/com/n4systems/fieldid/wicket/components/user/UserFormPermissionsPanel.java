package com.n4systems.fieldid.wicket.components.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.List;

public class UserFormPermissionsPanel extends Panel {

    private List<Permission> permissions;

    private IModel<User> userModel;

    public UserFormPermissionsPanel(String id, IModel<User> userModel) {
        super(id, userModel);

        this.userModel = userModel;

        add(new ListView<Permission>("permissions", getPermissionsList(userModel)) {
            @Override
            protected void populateItem(final ListItem<Permission> item) {
                final Permission permission = item.getModelObject();

                item.add(new Label("name", new FIDLabelModel(permission.label)));

                final Model<Boolean> selection = Model.of(permission.enabled? Boolean.TRUE: Boolean.FALSE);

                RadioGroup<Boolean> group = new RadioGroup<Boolean>("group", selection);
                group.add(new AjaxFormChoiceComponentUpdatingBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        Permission updatedPermission = new Permission(permission.id, permission.label, selection.getObject());
                        int index = item.getIndex();
                        permissions.remove(index);
                        permissions.add(index, updatedPermission);
                    }
                });
                group.add(new Radio<Boolean>("on", new Model<Boolean>(Boolean.TRUE)));
                group.add(new Radio<Boolean>("off", new Model<Boolean>(Boolean.FALSE)));
                item.add(group);

            }
        });
    }

    private List<Permission> getPermissionsList(IModel<User> userModel) {
       permissions = Lists.newArrayList();
        int[] permissionList;
        User user = userModel.getObject();
        BitField permField = new BitField(user.isNew() ? 0 : user.getPermissions());

        if (user.isLiteUser() || user.isUsageBasedUser())
            permissionList =  Permissions.getVisibleLiteUserEventPermissions();
        else if (user.isReadOnly())
            permissionList = Permissions.getVisibleReadOnlyEventPermissions();
        else
            permissionList =  Permissions.getVisibleSystemUserEventPermissions();

        for (int permission: permissionList) {
            permissions.add(new Permission(permission, Permissions.getLabel(permission), permField.isSet(permission)));
        }
        return permissions;
    }

    public int getPermissions() {
        BitField perms = new BitField();
        for (Permission permission: permissions) {
            if(permission.enabled) {
                perms.set(permission.id);
            }
        }
        return perms.getMask();
    }

    private class Permission implements Serializable {
        int id;
        String label;
        Boolean enabled;

        public Permission(int id, String label, boolean enabled) {
            this.id = id;
            this.label = label;
            this.enabled = enabled;
        }

    }
}
