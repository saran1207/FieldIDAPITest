package com.n4systems.fieldid.wicket.model.user;

import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class VisibleUserGroupsModel extends FieldIDSpringModel<List<UserGroup>> {

    @SpringBean
    private UserGroupService userGroupService;

    @Override
    protected List<UserGroup> load() {
        return userGroupService.getVisibleActiveUserGroups();
    }

}
