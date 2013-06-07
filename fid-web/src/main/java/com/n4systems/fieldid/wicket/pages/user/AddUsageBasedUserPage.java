package com.n4systems.fieldid.wicket.pages.user;

import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AddUsageBasedUserPage extends UserPage {

    public AddUsageBasedUserPage() {
        user = createUsageBasedUser();
    }

    @Override
    protected User doSave() {
        User usageBasedUser = user.getObject();
        if(getUserFormAccountPanel().isAssignPassword())
            usageBasedUser.assignPassword(getUserFormAccountPanel().getPassword());
        else {
            usageBasedUser.assignPassword(null);
            usageBasedUser.createResetPasswordKey();
        }
        usageBasedUser.assignSecruityCardNumber(getUserFormAccountPanel().getRfidNumber());
        usageBasedUser.setPermissions(getUserFormPermissionsPanel().getPermissions());

        userService.create(usageBasedUser);

        UploadedImage signature = identifiersPanel.getUploadedImage();

        if(signature.isNewImage()) {
            saveSignatureFile(signature);
        }

        if(getUserFormAccountPanel().getWelcomeMessage().isSendEmail())
            sendWelcomeEmail(usageBasedUser, getUserFormAccountPanel().getWelcomeMessage(), getUserFormAccountPanel().isAssignPassword());

        return usageBasedUser;
    }

    private UserFormAccountPanel getUserFormAccountPanel() {
        return (UserFormAccountPanel)accountPanel;
    }

    private UserFormPermissionsPanel getUserFormPermissionsPanel() {
        return (UserFormPermissionsPanel)permissionsPanel;
    }

    @Override
    protected Component createAccountPanel(String id, Form form) {
        return new UserFormAccountPanel(id, user, form);
    }

    @Override
    protected Component createPermissionsPanel(String id) {
        return new UserFormPermissionsPanel(id, user);
    }


    private IModel<User> createUsageBasedUser() {
        User usageBasedUser = new User();
        usageBasedUser.setTenant(getTenant());
        usageBasedUser.setRegistered(true);
        usageBasedUser.setModifiedBy(getCurrentUser());
        usageBasedUser.setCreatedBy(getCurrentUser());
        usageBasedUser.setUserType(UserType.USAGE_BASED);
        usageBasedUser.setTimeZoneID(getSessionUser().getOwner().getInternalOrg().getDefaultTimeZone());
        return Model.of(usageBasedUser);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.addusageBasedUser"));
    }
}
