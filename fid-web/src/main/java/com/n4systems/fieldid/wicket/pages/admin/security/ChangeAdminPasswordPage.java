package com.n4systems.fieldid.wicket.pages.admin.security;

import com.n4systems.fieldid.service.admin.AdminUserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.security.PasswordComplexityChecker;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-06-03.
 */
public class ChangeAdminPasswordPage extends FieldIDAdminPage{

    @SpringBean
    private AdminUserService adminUserService;

    public ChangeAdminPasswordPage() {
        add(new FIDFeedbackPanel("feedbackPanel"));

        final PasswordTextField passwordField = new PasswordTextField("password", Model.of(""));
        passwordField.setLabel(Model.of("Password"));

        final PasswordTextField confirmPasswordField = new PasswordTextField("confirmPassword", Model.of(""));
        confirmPasswordField.setLabel(Model.of("Confirm Password"));
        add(confirmPasswordField);

        Form form = new Form("passwordForm"){
            @Override
            protected void onSubmit() {

                PasswordComplexityChecker checker = new PasswordComplexityChecker(8, 1, 1, 1, 1);
                if(!passwordField.getModelObject().equals(confirmPasswordField.getModelObject())) {
                    error(new FIDLabelModel("errors.pwsnotmatch").getObject());
                } else if (!checker.isValid(passwordField.getModelObject().toString())) {
                    error(new FIDLabelModel("error.password_min_requirements").getObject());
                } else {
                    changePassword(passwordField.getModelObject());

                    redirect("/admin/organizations.action");
                }
            }
        };

        add(form);
        form.add(passwordField);
        form.add(confirmPasswordField);
    }

    public void changePassword(String password) {
        AdminUser adminUser = FieldIDSession.get().getAdminUser();
        adminUserService.changePassword(adminUser, password);
    }
}