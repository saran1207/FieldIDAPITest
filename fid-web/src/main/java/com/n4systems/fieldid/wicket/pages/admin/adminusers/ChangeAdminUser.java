package com.n4systems.fieldid.wicket.pages.admin.adminusers;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ChangeAdminUser extends FieldIDAdminPage {

    private FIDFeedbackPanel feedbackPanel;
    private Long tenantId;
    private User newAdmin;

    @SpringBean
    private UserService userService;

    public ChangeAdminUser(PageParameters parameters) {

        tenantId = parameters.get("id").toLong();

        User currentAdmin = userService.getAdminUser(tenantId);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(new Form<Void>("form") {

            @Override
            protected void onInitialize() {
                super.onInitialize();
                add(new FlatLabel("currentAdmin", new PropertyModel<>(currentAdmin, "displayName")));
                add(new FlatLabel("currentAdminUserId", new PropertyModel<>(currentAdmin, "userID")));

                LoadableDetachableModel<List<User>> userList = new LoadableDetachableModel<List<User>>() {
                    @Override
                    protected List<User> load() {
                        return userService.getAdministrationUsersByTenant(tenantId);
                    }
                };

                DropDownChoice<User> newAdminSelect = new FidDropDownChoice<User>("newAdmin", new PropertyModel<User>(ChangeAdminUser.this, "newAdmin"), userList, new IChoiceRenderer<User>() {
                    @Override
                    public Object getDisplayValue(User object) {
                        return object.getDisplayName() + " (" + object.getUserID() + ")";
                    }

                    @Override
                    public String getIdValue(User object, int index) {
                        return object.getID() + "";
                    }
                });
                newAdminSelect.setNullValid(true);
                add(newAdminSelect);

                add(new SubmitLink("save"));
                add(new NonWicketLink("cancel", "/admin/organizationEdit.action?id=" + tenantId));

            }

            @Override
            protected void onSubmit() {

                currentAdmin.setUserType(UserType.FULL);
                userService.update(currentAdmin);

                newAdmin.setUserType(UserType.ADMIN);
                userService.update(newAdmin);

                redirect("/admin/organizationEdit.action?id=" + tenantId);
            }
        });

    }

    public User getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(User newAdmin) {
        this.newAdmin = newAdmin;
    }
}
