package com.n4systems.fieldid.service.admin;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.SecurityService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.admin.AdminUserType;
import com.n4systems.model.admin.SudoPermission;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Transactional
public class AdminUserService extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(AdminUserService.class);

	@Autowired private SecurityService securityService;
	@Autowired private MailService mailService;

	private void sendWelcomeEmail(String email, String password) throws MessagingException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("password", password);
		mailService.sendNotification("adminUserWelcome", email, "Field ID Admin Account", params);
	}

	public void createAndNotifyAdminUserWithRandomPass(String email, AdminUserType type) {
		String password = securityService.generatePassword(12);
		createAdminUser(email, password, type);

		try {
			sendWelcomeEmail(email, password);
		} catch (MessagingException e) {
			logger.error("Failed sending admin user welcome message", e);
		}
	}

	public AdminUser createAdminUser(String email, String password, AdminUserType type) {
		AdminUser user = new AdminUser();
		user.setEnabled(true);
		user.setEmail(email);
		user.setType(type);
		user.setSalt(securityService.generateSalt(64));
		user.setPassword(securityService.hashSaltedPassword(password, user.getSalt()));
		persistenceService.save(user);
		return user;
	}

	public AdminUser changePassword(AdminUser adminUser, String newPassword) {
		adminUser.setSalt(securityService.generateSalt(64));
		adminUser.setPassword(securityService.hashSaltedPassword(newPassword, adminUser.getSalt()));
		return persistenceService.update(adminUser);
	}

	private boolean passwordMatches(AdminUser user, String password) {
		char[] hashPass = securityService.hashSaltedPassword(password, user.getSalt());
		return Arrays.equals(hashPass, user.getPassword());
	}

	public AdminUser authenticateUser(String email, String password) {
		if (email == null || password == null) throw new IllegalArgumentException("Email or password cannot be null");

		QueryBuilder<AdminUser> query = new QueryBuilder<AdminUser>(AdminUser.class);
		query.addWhere(WhereClauseFactory.create("email", email));
		query.addWhere(WhereClauseFactory.create("enabled", true));

		AdminUser user = persistenceService.find(query);
		if (user == null) {
			return null;
		}

		return passwordMatches(user, password) ? user : null;
	}

	public List<AdminUser> findAllAdminUsers() {
		QueryBuilder<AdminUser> query = new QueryBuilder<AdminUser>(AdminUser.class);
		query.addOrder("type");
		query.addOrder("email");
		List<AdminUser> users = persistenceService.findAll(query);
		return users;
	}

	public boolean adminUserExists(String email) {
		QueryBuilder<AdminUser> query = new QueryBuilder<AdminUser>(AdminUser.class);
		query.addWhere(WhereClauseFactory.create("email", email));

		boolean exists = persistenceService.exists(query);
		return exists;
	}

	public void removeAdminUser(AdminUser adminUser) {
		persistenceService.remove(adminUser);
	}

	public void resetUserPasswordAndNotify(AdminUser adminUser) {
		String newPassword = securityService.generatePassword(12);
		changePassword(adminUser, newPassword);

		try {
			sendWelcomeEmail(adminUser.getEmail(), newPassword);
		} catch (MessagingException e) {
			logger.error("Failed sending admin user reset password message", e);
		}
	}

	public AdminUser enableUser(AdminUser adminUser, boolean enabled) {
		adminUser.setEnabled(enabled);
		return persistenceService.update(adminUser);
	}

	public boolean sudoPermissionExists(AdminUser adminUser, User user) {
		QueryBuilder<SudoPermission> query = new QueryBuilder<SudoPermission>(SudoPermission.class);
		query.addWhere(WhereClauseFactory.create("adminUser", adminUser));
		query.addWhere(WhereClauseFactory.create("user", user));
		boolean exists = persistenceService.exists(query);
		return exists;
	}

	public void createSudoPermission(AdminUser adminUser, User user) {
		QueryBuilder<SudoPermission> query = new QueryBuilder<SudoPermission>(SudoPermission.class);
		query.addWhere(WhereClauseFactory.create("adminUser", adminUser));
		query.addWhere(WhereClauseFactory.create("user", user));
		SudoPermission permission = persistenceService.find(query);

		if (permission != null) {
			permission.setCreated(new Date());
			persistenceService.update(permission);
		} else {
			permission = new SudoPermission();
			permission.setAdminUser(adminUser);
			permission.setUser(user);
			persistenceService.save(permission);
		}
	}

	public User attemptSudoAuthentication(String tenantName, String userId, String password) {
		QueryBuilder<SudoPermission> query = new QueryBuilder<SudoPermission>(SudoPermission.class);
		query.addWhere(WhereClauseFactory.create("adminUser.enabled", true));
		query.addWhere(WhereClauseFactory.create("user.tenant.name", tenantName));
		query.addWhere(WhereClauseFactory.create("user.userID", userId));
		List<SudoPermission> permissions = persistenceService.findAll(query);

		SudoPermission allowedPermission = null;
		for (SudoPermission permission: permissions) {
			if (passwordMatches(permission.getAdminUser(), password)) {
				allowedPermission = permission;
				break;
			}
		}

		if (allowedPermission != null) {
			logger.info("SudoLogin: [" + allowedPermission.getAdminUser().getEmail() + "] --> [" + allowedPermission.getUser() + "]");
			return allowedPermission.getUser();
		} else {
			return null;
		}
	}

	@Scheduled(fixedRate = 300000) // 5 min
	public void expireSudoPermissions() {
		QueryBuilder<SudoPermission> query = new QueryBuilder<SudoPermission>(SudoPermission.class);

		Date expireDate = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2));
		query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LT, "created", expireDate));

		for (SudoPermission permission: persistenceService.findAll(query)) {
			persistenceService.remove(permission);
		}
	}
}
