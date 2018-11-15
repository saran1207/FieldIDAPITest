package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

/**
 * Created by tracyshi on 2014-08-13.
 */
public class ManageUserMobilePasscodePage extends FieldIDTemplatePage {

    @SpringBean
    private UserService userService;

    private Long uniqueId;
    private IModel<User> userModel;
    private Form buttonForm;
    private Button turnOffPasscodeButton;
    private Button changePasscodeButton;

    public ManageUserMobilePasscodePage(PageParameters params) {
        super(params);
        uniqueId = params.get("uniqueID").toLong();
        userModel = Model.of(userService.getUser(uniqueId));

        buttonForm = new Form("buttonForm");

        turnOffPasscodeButton = new Button("turnOffPasscodeButton") {
          @Override
          public void onSubmit() {
              User user = userModel.getObject();
              user.assignSecruityCardNumber(null);
              userService.update(user);
              FieldIDSession.get().info(new FIDLabelModel("message.passcoderemoved").getObject());
              setResponsePage(EditUserMobilePasscodePage.class, PageParametersBuilder.uniqueId(uniqueId));
          }
        };

        changePasscodeButton = new Button("changePasscodeButton") {
          @Override
        public void onSubmit() {
              setResponsePage(EditUserMobilePasscodePage.class, PageParametersBuilder.uniqueId(uniqueId));
          }
        };

        buttonForm.add(turnOffPasscodeButton);
        buttonForm.add(changePasscodeButton);
        add(buttonForm);
        add(new FIDFeedbackPanel("feedbackPanel"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        UserListFilterCriteria criteria = new UserListFilterCriteria(false);
        Long activeUserCount = userService.countUsers(criteria.withArchivedOnly(false));
        Long archivedUserCount = userService.countUsers(criteria.withArchivedOnly());

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeUserCount)).page(UsersListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedUserCount)).page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.view").page(ViewUserPage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.edit").page(EditUserPage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.change_password").page(ChangeUserPasswordPage.class).params(uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_passcode").page(ManageUserMobilePasscodePage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build(),
                aNavItem().label("nav.mobile_profile").page(UserOfflineProfilePage.class).params(uniqueId(userModel.getObject().getId())).build(),
                aNavItem().label("nav.add").page(AddUserPage.class).onRight().build(),
                aNavItem().label(new FIDLabelModel("nav.import_export")).page(UserImportPage.class).onRight().build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.user_change_rfid_number"));
    }

}
