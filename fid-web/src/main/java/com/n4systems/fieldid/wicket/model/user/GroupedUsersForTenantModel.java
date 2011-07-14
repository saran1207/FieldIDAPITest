package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.viewhelpers.NaturalOrderSort;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GroupedUsersForTenantModel extends FieldIDSpringModel<List<User>> {

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

        Collections.sort(filteredUsers, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                Comparator<String> naturalComparator = NaturalOrderSort.getNaturalComparator();
                if (user1.getOwner().equals(user2.getOwner())) {
                    return naturalComparator.compare(user1.getDisplayName(), user2.getDisplayName());
                }
                return compareOwners(user1.getOwner(), user2.getOwner(), naturalComparator);
            }
        });


        return filteredUsers;
    }

    private int compareOwners(BaseOrg owner1, BaseOrg owner2, Comparator<String> naturalComparator) {
        if (owner1.isPrimary() && !owner2.isPrimary()) {
            return -1;
        } else if (owner2.isPrimary() && !owner1.isPrimary()) {
            return 1;
        }
        return naturalComparator.compare(owner1.getDisplayName(), owner2.getDisplayName());
    }

}

