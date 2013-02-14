package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Transactional
public class UserGroupService extends FieldIdPersistenceService {

    public List<UserGroup> getActiveUserGroups() {
        return findUserGroupsLike(null, Archivable.EntityState.ACTIVE);
    }

    public List<UserGroup> getVisibleActiveUserGroups() {
        if (getCurrentUser().getGroup() != null) {
            // We can only see our group if we're in one
            return Arrays.asList(getCurrentUser().getGroup());
        }
        return findUserGroupsLike(null, Archivable.EntityState.ACTIVE);
    }

    public List<UserGroup> findUserGroupsLike(String nameFilter, Archivable.EntityState entityState) {
        QueryBuilder<UserGroup> query = createTenantSecurityBuilder(UserGroup.class, !entityState.equals(Archivable.EntityState.ACTIVE));

        if (!entityState.equals(Archivable.EntityState.ACTIVE)) {
            query.addSimpleWhere("state", entityState);
        }

        if (nameFilter != null) {
            WhereParameterGroup nameGroup = new WhereParameterGroup("nameGroup");

            nameGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "name", nameFilter, WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            nameGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "groupId", nameFilter, WhereParameter.TRIM | WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));

            query.addWhere(nameGroup);
        }
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public void archiveGroupInto(UserGroup groupToArchive, UserGroup newGroup) {
        for (User user : groupToArchive.getMembers()) {
            user.setGroup(newGroup);
            persistenceService.update(user);
        }
        groupToArchive.setState(Archivable.EntityState.ARCHIVED);
        persistenceService.update(groupToArchive);
    }

    public void archiveGroup(UserGroup userGroup) {
        userGroup.setState(Archivable.EntityState.ARCHIVED);
        persistenceService.update(userGroup);
    }

    public void unArchiveGroup(UserGroup userGroup) {
        userGroup.setState(Archivable.EntityState.ACTIVE);
        persistenceService.update(userGroup);
    }

    public boolean exists(String name, Long id) {
        QueryBuilder<UserGroup> query = createUserSecurityBuilder(UserGroup.class);
        query.addSimpleWhere("name", name);
        List<UserGroup> userGroups = persistenceService.findAll(query);
        if (userGroups.size() == 0) {
            return false;
        }
        return !userGroups.get(0).getId().equals(id);
    }
}
