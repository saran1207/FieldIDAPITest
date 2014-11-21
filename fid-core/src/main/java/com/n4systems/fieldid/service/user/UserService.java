package com.n4systems.fieldid.service.user;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.ExternalOrgFilter;
import com.n4systems.model.orgs.InternalOrgFilter;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;
import com.n4systems.util.BitField;
import com.n4systems.util.StringUtils;
import com.n4systems.util.UserBelongsToFilter;
import com.n4systems.util.collections.PrioritizedList;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.*;

@Transactional
public class UserService extends CrudService<User> {

    @Autowired
    private OrgService orgService;

	public UserService() {
		super(User.class);
	}

    private static String [] DEFAULT_ORDER = {"firstName", "lastName"};

    public List<User> getUsers(boolean registered, boolean includeSystem) {
        QueryBuilder<User> builder = createUserQueryBuilder(registered, includeSystem);

        if (!getCurrentUser().getGroups().isEmpty() && isUserGroupFilteringEnabled()) {
            return new ArrayList<User>(ThreadLocalInteractionContext.getInstance().getVisibleUsers());
        }

        return persistenceService.findAll(builder);
    }

    public List<User> getUsers(UserListFilterCriteria criteria) {
        QueryBuilder<User> builder = createUserQueryBuilder(criteria);

        if (!getCurrentUser().getGroups().isEmpty() && isUserGroupFilteringEnabled()) {
            return new ArrayList<>(ThreadLocalInteractionContext.getInstance().getVisibleUsers());
        }

        return persistenceService.findAll(builder);
    }

	@Override
	public List<User> findAll(int page, int pageSize) {
		return super.findAll(createUserQueryBuilder(true, false), page, pageSize);
	}

	public List<User> getExpiringPasswordUsersByTenant(Long tenantId, int expiringDays) {

        List<User> finalListOfUsers = new ArrayList<>();
        Date expiringDate = new Date();
        Date today = new Date();
        Date userDate = DateUtils.addDays(today, 10);

        QueryBuilder<User> query = new QueryBuilder<>(User.class, new OpenSecurityFilter());
        query.addSimpleWhere("tenant.id", tenantId);
        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        List<User> userList = persistenceService.findAll(query);

        for(User user:userList) {
            if(user.getPasswordChanged() != null) {
                expiringDate = DateUtils.addDays(user.getPasswordChanged(), expiringDays);
                if (expiringDate.before(userDate) && expiringDate.after(today)) {
                    finalListOfUsers.add(user);
                }
            }
        }

        return finalListOfUsers;
    }

    public Long countUsers(UserListFilterCriteria criteria) {
        QueryBuilder<User> builder = createUserQueryBuilder(criteria);

        if (!getCurrentUser().getGroups().isEmpty() && isUserGroupFilteringEnabled()) {
            return Long.valueOf(new ArrayList<User>(ThreadLocalInteractionContext.getInstance().getVisibleUsers()).size());
        }

        return persistenceService.count(builder);
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

	private QueryBuilder<User> createOrgUserQuery(BaseOrg org, String nameOrUserIdSearch, UserType typeFilter) {
		QueryBuilder<User> builder = createUserQueryBuilder(false, false);

		if (org != null) {
			builder.addSimpleWhere("owner", org);
		}

		if (typeFilter != null) {
			builder.addWhere(WhereClauseFactory.create("userType", typeFilter));
		}

		if (StringUtils.isNotEmpty(nameOrUserIdSearch)) {
			builder.addWhere(WhereClauseFactory.group("nameClauses",
					WhereClauseFactory.create(Comparator.LIKE, "userID", nameOrUserIdSearch, WhereParameter.WILDCARD_RIGHT, WhereClause.ChainOp.OR),
					WhereClauseFactory.create(Comparator.LIKE, "firstName", nameOrUserIdSearch, WhereParameter.WILDCARD_RIGHT, WhereClause.ChainOp.OR),
					WhereClauseFactory.create(Comparator.LIKE, "lastName", nameOrUserIdSearch, WhereParameter.WILDCARD_RIGHT, WhereClause.ChainOp.OR),
					WhereClauseFactory.create(Comparator.LIKE, "emailAddress", nameOrUserIdSearch, WhereParameter.WILDCARD_RIGHT, WhereClause.ChainOp.OR),
					WhereClauseFactory.create(Comparator.LIKE, "owner.name", nameOrUserIdSearch, WhereParameter.WILDCARD_RIGHT, WhereClause.ChainOp.OR)
			));
		}
		return builder;
	}

    public List<User> getOrgUsers(BaseOrg org, String nameOrUserIdSearch, UserType typeFilter, String order, Boolean ascending, int first, int count) {
		QueryBuilder<User> builder = createOrgUserQuery(org, nameOrUserIdSearch, typeFilter);
		if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                builder.addOrder(subOrder, ascending);
            }
        }
        return persistenceService.findAllPaginated(builder, first, count);
    }

    public Long countOrgUsers(BaseOrg org, String nameOrUserIdSearch, UserType typeFilter) {
		QueryBuilder<User> builder = createOrgUserQuery(org, nameOrUserIdSearch, typeFilter);
        return persistenceService.count(builder);
    }

    private QueryBuilder<User> createUserQueryBuilder(boolean registered, boolean includeSystem) {
        UserListFilterCriteria criteria = new UserListFilterCriteria(false);
        if(registered)
            criteria.withRegistered();
        if(includeSystem)
            criteria.withSystemUsers();
        return createUserQueryBuilder(criteria);
    }

    private QueryBuilder<User> createUserQueryBuilder(UserListFilterCriteria criteria) {
        QueryBuilder<User> builder = createUserSecurityBuilder(User.class, criteria.isArchivedOnly());

        if (criteria.isRegistered()) {
            UserQueryHelper.applyRegisteredFilter(builder);
        } else {
            UserQueryHelper.applyFullyActiveFilter(builder);
        }

        if (!criteria.isIncludeSystem()) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.SYSTEM));
        }

        if (criteria.getOwner() != null) {
            builder.addSimpleWhere("owner", criteria.getOwner());
        }

        if (criteria.getCustomer() != null) {
            builder.addSimpleWhere("owner.customerOrg", criteria.getCustomer());
        }

        if(criteria.isFilterOnPrimaryOrg()) {
            builder.addWhere(WhereClauseFactory.createIsNull("owner.secondaryOrg"));
        }

        if(criteria.isFilterOnSecondaryOrg()) {
            builder.addSimpleWhere("owner.secondaryOrg", criteria.getOrgFilter());
        }

        if (criteria.getUserType() != null && criteria.getUserType() != UserType.ALL) {
            builder.addSimpleWhere("userType", criteria.getUserType());
        }

        if (criteria.getUserBelongsToFilter() == UserBelongsToFilter.CUSTOMER) {
            builder.applyFilter(new ExternalOrgFilter("owner"));
        } else if (criteria.getUserBelongsToFilter() == UserBelongsToFilter.EMPLOYEE) {
            builder.applyFilter(new InternalOrgFilter("owner"));
        }

        if (criteria.getGroupFilter() != null) {
            builder.addWhere(new InElementsParameter<UserGroup>("groups", criteria.getGroupFilter()));
        }

        if (criteria.getNameFilter() != null && !criteria.getNameFilter().isEmpty()) {
            WhereParameterGroup whereGroup = new WhereParameterGroup();
            whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "userID", criteria.getNameFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "firstName", criteria.getNameFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            whereGroup.addClause(WhereClauseFactory.create(Comparator.LIKE, "lastName", criteria.getNameFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            builder.addWhere(whereGroup);
        }

        if (criteria.isArchivedOnly()) {
            UserQueryHelper.applyArchivedFilter(builder);
        }

        if(criteria.getOrder() != null && !criteria.getOrder().isEmpty()) {
            if(criteria.getOrder().startsWith("owner")) {
                builder.addJoin(new JoinClause(JoinClause.JoinType.LEFT, criteria.getOrder(), "sort", true));

                OrderClause orderClause1 = new OrderClause("sort", criteria.isAscending());
                orderClause1.setAlwaysDropAlias(true);

                builder.getOrderArguments().add(orderClause1);
                builder.getOrderArguments().add(new OrderClause("id", criteria.isAscending()));
            } else
                for (String subOrder : criteria.getOrder().split(",")) {
                    builder.addOrder(subOrder.trim(), criteria.isAscending());
                }
        } else {
            for (String order : DEFAULT_ORDER) {
                builder.setOrder(order, criteria.isAscending());
            }
        }
        return builder;
    }

    public AdminUser getAdminUser(Long userId) {
        QueryBuilder<AdminUser> builder = createUserSecurityBuilder(AdminUser.class, true);
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "id", userId));
        AdminUser user = persistenceService.find(builder);
        return user;
    }

    public User getUser(Long userId) {
        QueryBuilder<User> builder = createUserSecurityBuilder(User.class, true);
        builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "id", userId));
        User user = persistenceService.find(builder);
        return user;
    }

    public void moveSavedItem(Long userId, int fromIndex, int toIndex) {
        User user = getUser(userId);
        SavedItem savedItem = user.getSavedItems().get(fromIndex);
        user.getSavedItems().remove(fromIndex);
        user.getSavedItems().add(toIndex, savedItem);
    }

    public User authenticateUserByPassword(String tenantName, String userId, String password) {
        QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
        UserQueryHelper.applyFullyActiveFilter(builder);

        builder.addWhere(WhereClauseFactory.createCaseInsensitive("tenant.name", tenantName));
        builder.addWhere(WhereClauseFactory.createCaseInsensitive("userID", userId));
        builder.addWhere(WhereClauseFactory.create("hashPassword", User.hashPassword(password)));
        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "userType", UserType.PERSON));

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
        builder.addWhere(WhereClauseFactory.create(Comparator.NE, "userType", UserType.PERSON));

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
                + filter.produceWhereClause(User.class, "ub") + " OR ( " + justTenantFilter.produceWhereClause(User.class, "ub") + " AND ub.owner.customerOrg IS NULL) )";

        if (!getCurrentUser().getGroups().isEmpty() && isUserGroupFilteringEnabled()) {
            queryString += " AND ub in (:visibleUsers) ";
        }

        queryString += " ORDER BY ub.firstName, ub.lastName";

        Query query = getEntityManager().createQuery(queryString);

        if (!getCurrentUser().getGroups().isEmpty() && isUserGroupFilteringEnabled()) {
            Collection<User> visibleUsers = ThreadLocalInteractionContext.getInstance().getVisibleUsers();
            query.setParameter("visibleUsers", visibleUsers);
        }

        filter.applyParameters(query, User.class);
        justTenantFilter.applyParameters(query, User.class);


        // get the userlist and filter out users not having the create/edit
        return Permissions.filterHasOneOf((List<User>) query.getResultList(), Permissions.ALLEVENT);
    }

    public List<User> search(String term, int threshold) {
        QueryBuilder<User> builder = createUserSecurityBuilder(User.class);

        String[] tokens = term.split("\\s+");   // e.g. if user types in "John Smith" we look for firstname = johhn, lastname = smith.

        if (tokens.length>1) {
            WhereParameterGroup group = new WhereParameterGroup("firstLastNameSearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "firstName", "firstName", tokens[0], WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "lastName", "lastName", tokens[1], WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "userID", "userID", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "identifier", "identifier", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            builder.addWhere(group);
        } else if (org.apache.commons.lang.StringUtils.isNotBlank(term)) {
            WhereParameterGroup group = new WhereParameterGroup("smartsearch");
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "firstName", "firstName", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "lastName", "lastName", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "userID", "userID", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "identifier", "identifier", term, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.OR));
            builder.addWhere(group);
        }

        builder.setLimit(threshold*4);
        List<User> results = persistenceService.findAll(builder);
        return new PrioritizedList<User>(results, threshold);
    }

    public List<User> search(int threshold) {
        return search("",threshold);
    }

    public void create(User user) {
		super.save(user);
    }

    public boolean userIdIsUnique(Long tenantId, String userId, Long currentUserId) {
        if (userId == null) {
            return true;
        }

        QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(User.class, new TenantOnlySecurityFilter(tenantId)).setCountSelect();
        queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userId);

        if (currentUserId != null) {
            queryBuilder.addWhere(Comparator.NE, "id", "id", currentUserId);
        }

        return !persistenceService.exists(queryBuilder);
    }

    public List<User> findUsersByEmailAddress(String emailAddress) {
        QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
        builder.addSimpleWhere("emailAddress", emailAddress);
        List<User> users = new ArrayList<User>();
        users = persistenceService.findAll(builder);
        return users;
    }

    public void archive(User user) {
        user.archiveUser();
        persistenceService.update(user);
    }

    public void unarchive(User user) {
        user.activateEntity();
        persistenceService.update(user);
    }

    private boolean isUserGroupFilteringEnabled() {
        return orgService.getPrimaryOrgForTenant(getCurrentTenant().getId()).hasExtendedFeature(ExtendedFeature.UserGroupFiltering);
    }

	@Override
	public User save(User model) {
		validateUserBeforeSave(model);
		return super.save(model);
	}

	@Override
	public User update(User model) {
		validateUserBeforeSave(model);
		return super.update(model);
	}

	private void validateUserBeforeSave(User user) {
		user.setPermissions(ensurePermissionsMatchUserType(user.getUserType(), user.getPermissions()));

		if (user.getUserType() == UserType.SYSTEM || user.getUserType() == UserType.ADMIN) {
			// system users and admins, must be under the primary org
			if (!user.getOwner().isPrimary()) {
				user.setOwner(getCurrentUser().getOwner().getPrimaryOrg());
			}
		} else if (user.getUserType() == UserType.PERSON) {
			// person users cannot login
			user.setUserID(null);
			user.setHashPassword(null);
		}
	}

	private int ensurePermissionsMatchUserType(UserType type, int permissions) {
		BitField perms = BitField.create(permissions);
		switch (type) {
			case SYSTEM:
			case ADMIN:
				// System and Admin users have full permissions
				perms.set(Permissions.ALL);
				break;
			case FULL:
				// Full users can have any permission
				break;
			case LITE:
			case USAGE_BASED:
				// lite and usage based users can only have create and edit events
				perms.retain(Permissions.CreateEvent, Permissions.EditEvent);
				break;
			case READONLY:
			case PERSON:
				// Readonly and Persons have no permissions
				perms.clearAll();
				break;
			default:
				throw new IllegalArgumentException("Unhandled UserType: " + type.name());
		}
		return perms.getMask();
	}

}
