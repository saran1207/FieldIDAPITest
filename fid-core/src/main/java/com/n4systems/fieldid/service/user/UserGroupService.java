package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Transactional
public class UserGroupService extends FieldIdPersistenceService {

    public Collection<User> findUsersVisibleTo(User user) {
        Collection<User> friends = new HashSet<User>();
        for (UserGroup userGroup : user.getGroups()) {
            friends.addAll(getUsersInGroup(userGroup));
        }
        return friends;
    }

    public List<UserGroup> getActiveUserGroups() {
        return findUserGroupsLike(null, Archivable.EntityState.ACTIVE);
    }

    public List<UserGroup> getVisibleActiveUserGroups() {
        if (!getCurrentUser().getGroups().isEmpty()) {
            // We can only see our group if we're in one
            return new ArrayList<UserGroup>(getCurrentUser().getGroups());
        }
        return findUserGroupsLike(null, Archivable.EntityState.ACTIVE);
    }

    public List<UserGroup> findUserGroupsLike(String nameFilter, Archivable.EntityState entityState) {
        QueryBuilder<UserGroup> query = createTenantSecurityBuilder(UserGroup.class, !Archivable.EntityState.ACTIVE.equals(entityState));

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

    public void archiveGroupInto(Long groupToArchiveId, Long newGroupId) {
        UserGroup newGroup = newGroupId == null ? null : persistenceService.find(UserGroup.class, newGroupId);
        UserGroup groupToArchive  = persistenceService.find(UserGroup.class, groupToArchiveId);

        for (User user : groupToArchive.getMembers()) {
            user.getGroups().remove(groupToArchive);
            if (newGroup != null) {
                user.getGroups().add(newGroup);
            }
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

    public List<User> getUsersInGroup(UserGroup userGroup) {
        QueryBuilder<User> query = new QueryBuilder<User>(User.class, new TenantOnlySecurityFilter(userGroup.getTenant().getId()));
        query.addWhere(new InElementsParameter<UserGroup>("groups", userGroup));
        return persistenceService.findAll(query);
    }

    public List<User> getUsersInGroup(Long userGroupId) {
        UserGroup userGroup = persistenceService.findUsingTenantOnlySecurityWithArchived(UserGroup.class, userGroupId);
        return getUsersInGroup(userGroup);
    }

    public UserGroup getUserGroup(Long id) {
        QueryBuilder<UserGroup> query = createUserSecurityBuilder(UserGroup.class);
        query.addSimpleWhere("id", id);
        return persistenceService.find(query);
    }

    public List<UserGroup> getUserGroups(List<Long> ids) {
        QueryBuilder<UserGroup> query = createUserSecurityBuilder(UserGroup.class);
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "id", ids));
        return persistenceService.findAll(query);
    }

}
