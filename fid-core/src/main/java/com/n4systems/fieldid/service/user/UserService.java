package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.util.StringUtils;
import com.n4systems.util.collections.DefaultPrioritizer;
import com.n4systems.util.collections.PrioritizedList;
import com.n4systems.util.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
public class UserService extends FieldIdPersistenceService {

    public List<User> getUsers(boolean registered, boolean includeSystem) {
        QueryBuilder<User> builder = createUserQueryBuilder(registered, includeSystem);

        return persistenceService.findAll(builder);
    }

    public Map<BaseOrg, List<User>> getUsersByOrg() {
        final List<User> users = getUsers(true, false);
        final TreeMap<BaseOrg, List<User>> orgUserMap = createOrgUserMap();
        for (User user : users) {
            if (orgUserMap.get(user.getOwner()) == null) {
                orgUserMap.put(user.getOwner(), new ArrayList<User>());
            }
            orgUserMap.get(user.getOwner()).add(user);
        }

        return orgUserMap;
    }

    private QueryBuilder<User> createUserQueryBuilder(boolean registered, boolean includeSystem) {
        QueryBuilder<User> builder = createUserSecurityBuilder(User.class);

        if (registered) {
            UserQueryHelper.applyRegisteredFilter(builder);
        } else {
            UserQueryHelper.applyFullyActiveFilter(builder);
        }

        if (!includeSystem) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.SYSTEM));
        }
        return builder.addOrder("firstName").addOrder("lastName");
    }

	public User getUser(Long userId) {
		QueryBuilder<User> builder = createUserSecurityBuilder(User.class);
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "id", userId));
        return persistenceService.find(builder);		
	}
	
	public User authenticateUserByPassword(String tenantName, String userId, String password) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		UserQueryHelper.applyFullyActiveFilter(builder);
		
		builder.addWhere(WhereClauseFactory.createCaseInsensitive("tenant.name", tenantName));
		builder.addWhere(WhereClauseFactory.createCaseInsensitive("userID", userId));
		builder.addWhere(WhereClauseFactory.create("hashPassword", User.hashPassword(password)));
		
		User user = persistenceService.find(builder);
		return user;
	}

	public User authenticateUserBySecurityCard(String tenantName, String cardNumber) {
		if (StringUtils.isEmpty(cardNumber)) {
			return null;
		}

		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		UserQueryHelper.applyFullyActiveFilter(builder);
		
		builder.addWhere(WhereClauseFactory.createCaseInsensitive("tenant.name", tenantName));
		builder.addWhere(WhereClauseFactory.create("hashSecurityCardNumber", User.hashSecurityCardNumber(cardNumber)));

		List<User> users = persistenceService.findAll(builder);
		return (users.size() != 1) ? null : users.get(0);
	}

    private TreeMap<BaseOrg, List<User>> createOrgUserMap() {
        return new TreeMap<BaseOrg, List<User>>(new PrimaryOrgFirstComparator());
    }

    @Transactional
    public void shareSavedItemWithUsers(Long savedItemId, List<Long> userIds) {
        final SavedItem savedItem = persistenceService.find(SavedItem.class, savedItemId);
        String sharedByName = getCurrentUser().getFullName();
        try {
            for (Long userId : userIds) {
                final User user = persistenceService.find(User.class, userId);
                final SavedItem clonedItem = (SavedItem) savedItem.clone();
                clonedItem.reset();
                clonedItem.setSendSchedules(new ArrayList<SendSavedItemSchedule>()); // erase any existing schedules for shared report - don't want to inherit them.
                clonedItem.setSharedByName(sharedByName);
                SearchCriteria clonedCriteria = cloneCriteria(savedItem.getSearchCriteria());
                clonedItem.setSearchCriteria(clonedCriteria);
                user.getSavedItems().add(clonedItem);
                persistenceService.save(user);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private SearchCriteria cloneCriteria(SearchCriteria criteria) throws CloneNotSupportedException {
        SearchCriteria clonedCriteria = (SearchCriteria) criteria.clone();
        clonedCriteria.reset();
        List<String> columns = new ArrayList<String>();
        for (String col: criteria.getColumns()) {
            columns.add(col);
        }
        clonedCriteria.setColumns(columns);
        return clonedCriteria;
    }

    public List<User> getExaminers() {
        SecurityFilter filter = securityContext.getUserSecurityFilter();
        SecurityFilter justTenantFilter = securityContext.getTenantSecurityFilter();
        String queryString = "select DISTINCT ub from " + User.class.getName() + " ub where ub.registered = true and state = 'ACTIVE' and ub.userType != '" + UserType.SYSTEM.toString() + "' and ( "
                + filter.produceWhereClause(User.class, "ub") + " OR ( " + justTenantFilter.produceWhereClause(User.class, "ub") + " AND ub.owner.customerOrg IS NULL) )"
                + " ORDER BY ub.firstName, ub.lastName";
        Query query = getEntityManager().createQuery(queryString);
        filter.applyParameters(query, User.class);
        justTenantFilter.applyParameters(query, User.class);

        // get the userlist and filter out users not having the create/edit
        // inspect
        return Permissions.filterHasOneOf((List<User>) query.getResultList(), Permissions.ALLEVENT);
    }

    public List<User> search(String term, int threshold) {
        QueryBuilder<User> builder = createUserSecurityBuilder(User.class);

        if (org.apache.commons.lang.StringUtils.isNotBlank(term)) {
            WhereParameterGroup group = new WhereParameterGroup("smartsearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "firstName", "firstName", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "lastName", "lastName", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "userID", "userID", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            builder.addWhere(group);
        }

        builder.setLimit(threshold*4);
        List<User> results = persistenceService.findAll(builder);
        return new PrioritizedList<User>(results, new DefaultPrioritizer<User>(),threshold);
    }

    public List<User> search(int threshold) {
        return search("",threshold);
    }
}
