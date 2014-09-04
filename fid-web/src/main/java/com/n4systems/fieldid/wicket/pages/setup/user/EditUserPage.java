package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormPermissionsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EditUserPage extends UserPage {

    @SpringBean
    private S3Service s3Service;

    public EditUserPage(IModel<User> userModel) {
        super(userModel);
    }

    public EditUserPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void doSave() {
        update();
        setResponsePage(ViewUserPage.class, PageParametersBuilder.uniqueId(userModel.getObject().getId()));
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
        } else if(s3Service.userSignatureExists(userModel.getObject())){
            signatureImage = s3Service.downloadUserSignature(userModel.getObject());
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
        UserListFilterCriteria criteria = new UserListFilterCriteria(false);
        Long activeUserCount = userService.countUsers(criteria.withArchivedOnly(false));
        Long archivedUserCount = userService.countUsers(criteria.withArchivedOnly());

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeUserCount)).page(UsersListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedUserCount)).page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.view").page(ViewUserPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.edit").page(EditUserPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.change_password").page(ChangeUserPasswordPage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.mobile_passcode")
                        .page((userModel.getObject().getHashSecurityCardNumber() == null) ? EditUserMobilePasscodePage.class : ManageUserMobilePasscodePage.class)
                        .params(PageParametersBuilder.uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.mobile_profile").page(UserOfflineProfilePage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }
}
