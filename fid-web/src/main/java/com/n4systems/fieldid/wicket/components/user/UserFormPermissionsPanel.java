package com.n4systems.fieldid.wicket.components.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
    private List<Permission> lotoPermissions;

    private IModel<User> userModel;

    public UserFormPermissionsPanel(String id, IModel<User> userModel) {
        super(id, userModel);

        this.userModel = userModel;

        add(getListView("permissions", getPermissionsList(userModel)));

        if (FieldIDSession.get().getTenant().getSettings().isLotoEnabled())
            add(getListView("lotoPermissions", getLotoPermissionsList(userModel)));
        else
            add(new WebMarkupContainer("lotoPermissions").setVisible(false));
    }

    private ListView<Permission> getListView(String id, final List<Permission> permissionList) {
        return new ListView<Permission>(id, permissionList) {
            @Override
            protected void populateItem(final ListItem<Permission> item) {
                final Permission permission = item.getModelObject();

                item.add(new Label("name", new FIDLabelModel(permission.label)));

                final Model<Boolean> selection = Model.of(permission.enabled? Boolean.TRUE: Boolean.FALSE);

                RadioGroup<Boolean> group = new RadioGroup<>("group", selection);
                group.add(new AjaxFormChoiceComponentUpdatingBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        Permission updatedPermission = new Permission(permission.id, permission.label, selection.getObject());
                        int index = item.getIndex();
                        permissionList.remove(index);
                        permissionList.add(index, updatedPermission);
                    }
                });
                group.add(new Radio<>("on", new Model<>(Boolean.TRUE)));
                group.add(new Radio<>("off", new Model<>(Boolean.FALSE)));
                item.add(group);

            }
        };
    }

    private List<Permission> getPermissionsList(IModel<User> userModel) {
        this.permissions = Lists.newArrayList();
        List<Integer> permissionList = Lists.newArrayList();
        User user = userModel.getObject();
        BitField permField = new BitField(user.isNew() ? 0 : user.getPermissions());

        if (user.isLiteUser() || user.isUsageBasedUser()) {
            permissionList.addAll(Permissions.getVisibleLiteUserInspectionPermissions());
        } else if (user.isReadOnly()) {
            permissionList.addAll(Permissions.getVisibleReadOnlyInspectionPermissions());
        } else {
            permissionList.addAll(Permissions.getVisibleSystemUserInspectionPermissions());
        }

        permissions = getPermissionsList(permissionList, permField);

        return permissions;
    }

    private List<Permission> getLotoPermissionsList(IModel<User> userModel) {
        lotoPermissions = Lists.newArrayList();
        List<Integer> permissionList = Lists.newArrayList();
        User user = userModel.getObject();
        BitField permField = new BitField(user.isNew() ? 0 : user.getPermissions());

        if (user.isLiteUser() || user.isUsageBasedUser()) {
            permissionList.addAll(Permissions.getVisibleLiteUserLotoPermissions());
        } else if (user.isReadOnly()) {
            permissionList.addAll(Permissions.getVisibleReadOnlyLotoPermissions());
        } else {
            permissionList.addAll(Permissions.getVisibleSystemUserLotoPermissions());
        }

        lotoPermissions = getPermissionsList(permissionList, permField);

        return lotoPermissions;
    }


    private List<Permission> getPermissionsList(List<Integer> permissionList, BitField permField) {
        List<Permission> permissions = Lists.newArrayList();
        for (int permission: permissionList) {
            permissions.add(new Permission(permission, Permissions.getLabel(permission), permField.isSet(permission)));
        }
        return permissions;
    }

    private List<Permission> getAllPermissions() {
        List<Permission> allPermissions = Lists.newArrayList();
        allPermissions.addAll(permissions);
        if (lotoPermissions != null && !lotoPermissions.isEmpty()) {
            allPermissions.addAll(lotoPermissions);
        }
        return allPermissions;
    }

    public int getPermissions() {
        BitField perms = new BitField();
        for (Permission permission: getAllPermissions()) {
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
