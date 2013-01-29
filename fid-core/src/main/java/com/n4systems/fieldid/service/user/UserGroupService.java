package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserGroupService extends FieldIdPersistenceService {

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

    public void archiveGroup(UserGroup userGroup) {
        userGroup.setState(Archivable.EntityState.ARCHIVED);
        persistenceService.update(userGroup);
    }

    public void unArchiveGroup(UserGroup userGroup) {
        userGroup.setState(Archivable.EntityState.ACTIVE);
        persistenceService.update(userGroup);
    }

}
