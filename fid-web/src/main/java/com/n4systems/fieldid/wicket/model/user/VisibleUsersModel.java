package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.User;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.ArrayList;
import java.util.List;

public class VisibleUsersModel extends FieldIDSpringModel<List<User>> {

    @SpringBean
    private UserService userService;

    @Override
    protected List<User> load() {
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();

        List<User> users = userService.getUsers(true, false);

        List<User> filteredUsers = new ArrayList<User>();

        for (User user : users) {
            boolean readOnlyCustomerUserAndExternal = sessionUser.isReadOnlyCustomerUser() && user.getOwner().isExternal();
            if (!readOnlyCustomerUserAndExternal || sessionUser.getOwner().equals(user.getOwner())) {
                filteredUsers.add(user);
            }
        }

        return filteredUsers;
    }

}
