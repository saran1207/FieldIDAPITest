package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.File;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EditUserPage extends UserPage {

    public EditUserPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected User doSave() {
        return update();
    }

    @Override
    protected Component createAccountPanel(String id) {
        return new UserFormAccountPanel(id, userModel);
    }

    @Override
    protected Component createPermissionsPanel(String id) {
        return new UserFormPermissionsPanel(id, userModel);
    }

    @Override
    protected UploadedImage getSignatureImage() {
        File signatureImage = PathHandler.getSignatureImage(userModel.getObject());
        UploadedImage uploadedImage = new UploadedImage();

        if (signatureImage.exists()) {
            uploadedImage.setImage(signatureImage);
            uploadedImage.setUploadDirectory(signatureImage.getPath());
        }

        return uploadedImage;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_users.singular"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(UsersListPage.class).build(),
                aNavItem().label("nav.view_all_archived").page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.view").page("viewUser.action").params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.edit").page(EditUserPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }
}
