package com.n4systems.services.admin;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.admin.AdminUser;
import com.n4systems.model.admin.AdminUserType;
import com.n4systems.services.SecurityService;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public AdminUser authenticateUser(String email, String password) {
		if (email == null || password == null) throw new IllegalArgumentException("Email or password cannot be null");

		QueryBuilder<AdminUser> query = new QueryBuilder<AdminUser>(AdminUser.class);
		query.addWhere(WhereClauseFactory.create("email", email));

		AdminUser user = persistenceService.find(query);
		if (user == null) {
			return null;
		}

		char[] hashPass = securityService.hashSaltedPassword(password, user.getSalt());
		return Arrays.equals(hashPass, user.getPassword()) ? user : null;
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

}
