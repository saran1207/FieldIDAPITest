package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.EditUserPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UsersListPage;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ArchivedUsersListActionCell extends Panel {

    @SpringBean
    private UserLimitService userLimitService;

    @SpringBean
    private UserService userService;

    public ArchivedUsersListActionCell(String componentId, IModel<User> rowModel) {
        super(componentId, rowModel);

        final User user = rowModel.getObject();


        add(new AjaxLink<Void>("unarchive") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                boolean limitReached = false;

                if(user.isEmployee()) {
                    if (userLimitService.isEmployeeUsersAtMax()) {
                        onError(target, new FIDLabelModel("label.unarchive_employee_user_limit", userLimitService.getMaxEmployeeUsers()).getObject());
                        limitReached = true;
                    }
                } else if (user.isLiteUser()) {
                    if (userLimitService.isLiteUsersAtMax()) {
                        onError(target, new FIDLabelModel("label.unarchive_lite_user_limit", userLimitService.getMaxLiteUsers()).getObject());
                        limitReached = true;
                    }
                } else if (user.isReadOnly()) {
                    if (userLimitService.isReadOnlyUsersAtMax()) {
                        onError(target, new FIDLabelModel("label.unarchive_readonly_user_limit", userLimitService.getMaxReadOnlyUsers()).getObject());
                        limitReached = true;
                    }
                } else if (user.isPerson()) {
                    userService.unarchive(user);
                    FieldIDSession.get().info(new FIDLabelModel("message.user_unarchived").getObject());
                    setResponsePage(UsersListPage.class);
                }

                if(!limitReached & !user.isPerson()) {
                    setResponsePage(EditUserPage.class, PageParametersBuilder.uniqueId(user.getId()));
                }
            }
        });

    }

    protected void onError(AjaxRequestTarget target, String message) {}
}
