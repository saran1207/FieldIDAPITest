package com.n4systems.fieldid.wicket.pages.user;

import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.File;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EditUsageBasedUserPage extends UserPage {

    public EditUsageBasedUserPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected User doSave() {
        User usageBasedUser = user.getObject();

        usageBasedUser.assignSecruityCardNumber(getUserFormAccountPanel().getRfidNumber());
        usageBasedUser.setPermissions(getUserFormPermissionsPanel().getPermissions());

        UploadedImage signature = identifiersPanel.getUploadedImage();

        if(signature.isNewImage() || signature.isRemoveImage()) {
            saveSignatureFile(signature);
        }

        userService.update(usageBasedUser);

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

    @Override
    protected UploadedImage getSignatureImage() {
        File signatureImage = PathHandler.getSignatureImage(user.getObject());
        UploadedImage uploadedImage = new UploadedImage();

        if (signatureImage.exists()) {
            uploadedImage.setImage(signatureImage);
            uploadedImage.setUploadDirectory(signatureImage.getPath());
        }

        return uploadedImage;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_users.singular"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page("userList.action").build(),
                aNavItem().label("nav.view_all_archived").page("archivedUserList.action").params(param("currentPage", 1)).build(),
                aNavItem().label("nav.view").page("viewUser.action").params(uniqueId(user.getObject().getId())).build(),
                aNavItem().label("nav.edit").page(EditUsageBasedUserPage.class).params(uniqueId(user.getObject().getId())).build(),
                aNavItem().label("nav.add").page("addUser.action").onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }
}
