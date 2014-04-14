package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.user.AssignedUserOrGroupSelect;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.user.AssigneesModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.VisibleUserGroupsModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.tenant.SystemSettings;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ProcedureApproverPage extends FieldIDTemplatePage {

    @SpringBean
    private SystemSettingsService systemSettingsService;

    private IModel<SystemSettings> systemSettingsModel;

    public ProcedureApproverPage() {

        systemSettingsModel = Model.of(systemSettingsService.getSystemSettings());

        Form<SystemSettings> form;
        add(form = new Form<SystemSettings>("form") {
            @Override
            protected void onSubmit() {
                systemSettingsService.saveSystemSettings(systemSettingsModel.getObject());
                FieldIDSession.get().info(new FIDLabelModel("message.set_procedure_approver").getObject());
            }
        });

        IModel<List<User>> usersModel = createUsersModel();
        IModel<List<UserGroup>> userGroupsModel = createUserGroupsModel();
        form.add(new AssignedUserOrGroupSelect("approver", new PropertyModel<Assignable>(systemSettingsModel, "procedureApprover"),
                usersModel, userGroupsModel, new AssigneesModel(userGroupsModel, usersModel)));

        form.add(new SubmitLink("save"));
        form.add(new BookmarkablePageLink<SettingsPage>("cancelLink", SettingsPage.class));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedure_approval"));
    }

    private IModel<List<User>> createUsersModel() {
        return new UsersForTenantModel();
    }

    private IModel<List<UserGroup>> createUserGroupsModel() {
        return new VisibleUserGroupsModel();
    }
}
