package com.n4systems.fieldid.wicket.pages.useraccount.changepassword;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.user.UsersListPage;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ChangeUserAccountPasswordPage extends AccountSetupPage {

    @SpringBean protected UserService userService;

    protected UserType userType;
    private Long uniqueId;
    protected IModel<User> userModel;

    public ChangeUserAccountPasswordPage(UserType userType) {
        this.userType = userType;
    }

    public ChangeUserAccountPasswordPage(IModel<User> userModel) {
        super();
        this.uniqueId = userModel.getObject().getId();
        this.userModel = userModel;
    }

    public ChangeUserAccountPasswordPage(PageParameters parameters) {
        super(parameters);
        uniqueId = parameters.get("uniqueID").toLong();
        userModel = Model.of(loadExistingUser());
    }

    public ChangeUserAccountPasswordPage() {
        super();
        uniqueId = getSessionUser().getId();
        userModel = Model.of(loadExistingUser());
    }

    protected void doSave() {
        update();
        setResponsePage(ChangeUserAccountPasswordPage.class);
    }

    protected Component createAccountPanel(String id, Form form) {
        return new UserAccountFormPanel(id, userModel,form);
    }

    protected Component accountPanel;

    protected User loadExistingUser() {
        return userService.getUser(uniqueId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddUserForm("addUserForm"));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.change_password"));
    }

    protected UserAccountFormPanel getUserAccountFormPanel() {
        return (UserAccountFormPanel)accountPanel;
    }

    protected User update() {
        User user = userModel.getObject();
        user.updatePassword(getUserAccountFormPanel().getNewPassword());

        userService.update(user);

        return user;
    }

    protected void addConfirmBehavior(SubmitLink submitLink) {
    }

    protected void onOwnerPicked(AjaxRequestTarget target) {}


    class AddUserForm extends Form {

        SubmitLink submitLink;

        public AddUserForm(String id) {
            super(id);

            add(accountPanel = createAccountPanel("accountPanel",this));

            add(submitLink = new SubmitLink("save"));

            addConfirmBehavior(submitLink);

            add(new BookmarkablePageLink<UsersListPage>("cancel", ChangeUserAccountPasswordPage.class));
        }

        @Override
        protected void onSubmit() {
            doSave();
            FieldIDSession.get().info(new FIDLabelModel("message.users_password_updated").getObject());
        }
    }

}
