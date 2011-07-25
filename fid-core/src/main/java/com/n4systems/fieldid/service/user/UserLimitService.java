package com.n4systems.fieldid.service.user;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Transactional
public class UserLimitService extends FieldIdPersistenceService {
	private Integer employeeUserCount;
	private Integer liteUserCount;
	private Integer readOnlyUserCount;
	private UserLimits userLimits;
	
	@Autowired
	private TenantSettingsService tenantSettingsService;
	
	public UserLimits getUserLimits() {
		if (userLimits == null) {
			userLimits = tenantSettingsService.getTenantSettings().getUserLimits();
		}
		return userLimits;
	}
	
	private Integer countActiveUsers(WhereClause<?> filter) {
		QueryBuilder<User> builder = createTenantSecurityBuilder(User.class);
		builder.addWhere(WhereClauseFactory.create("registered", true));
		builder.addWhere(filter);

		Long userCount = persistenceService.count(builder);
		return userCount.intValue();
	}

	private boolean isUnlimited(int maxUsers) {
		return maxUsers == -1;
	}

	private boolean isEnabled(int maxUsers) {
		return maxUsers != 0;
	}

	public Integer getEmployeeUsersCount() {
		if (employeeUserCount == null) {
			employeeUserCount = countActiveUsers(WhereClauseFactory.create(Comparator.IN, "userType", Arrays.asList(UserType.ADMIN, UserType.FULL)));
		}
		return employeeUserCount;
	}

	public Integer getLiteUsersCount() {
		if (liteUserCount == null) {
			liteUserCount = countActiveUsers(WhereClauseFactory.create("userType", UserType.LITE));
		}
		return liteUserCount;
	}

	public Integer getReadOnlyUserCount() {
		if (readOnlyUserCount == null) {
			readOnlyUserCount = countActiveUsers(WhereClauseFactory.create("userType", UserType.READONLY));
		}
		return readOnlyUserCount;
	}

	public int getMaxEmployeeUsers() {
		int users = getUserLimits().getMaxEmployeeUsers();
		return users;
	}

	public int getMaxLiteUsers() {
		int users = getUserLimits().getMaxLiteUsers();
		return users;
	}

	public int getMaxReadOnlyUsers() {
		int users = getUserLimits().getMaxReadOnlyUsers();
		return users;
	}
	
	public boolean isEmployeeUsersUnlimited() {
		return isUnlimited(getMaxEmployeeUsers());
	}

	public boolean isLiteUsersUnlimited() {
		return isUnlimited(getMaxLiteUsers());
	}

	public boolean isReadOnlyUsersUnlimited() {
		return isUnlimited(getMaxReadOnlyUsers());
	}

	public boolean isLiteUsersEnabled() {
		return isEnabled(getMaxLiteUsers());
	}

	public boolean isReadOnlyUsersEnabled() {
		return isEnabled(getMaxReadOnlyUsers());
	}

	public boolean isEmployeeUsersAtMax() {
		boolean atMax = !isEmployeeUsersUnlimited() && getEmployeeUsersCount() >= getMaxEmployeeUsers();
		return atMax;
	}

	public boolean isLiteUsersAtMax() {
		boolean atMax = !isLiteUsersUnlimited() && getLiteUsersCount() >= getMaxLiteUsers();
		return atMax;
	}

	public boolean isReadOnlyUsersAtMax() {
		boolean atMax = !isReadOnlyUsersUnlimited() && getReadOnlyUserCount() >= getMaxReadOnlyUsers();
		return atMax;
	}
	
	public void updateUserLimits(int maxEmployeeUsers, int maxLiteUsers, int maxReadOnlyUsers) {
		tenantSettingsService.updateUserLimits(new UserLimits(maxEmployeeUsers, maxLiteUsers, maxReadOnlyUsers));
	}
	
}
