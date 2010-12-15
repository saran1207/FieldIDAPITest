package com.n4systems.ejb.legacy.impl;

import java.net.URI;
import java.util.List;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.DuplicateRfidException;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.mail.MailManagerFactory;
import com.n4systems.model.UserRequest;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.Permissions;
import com.n4systems.tools.EncryptionUtility;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EntityManagerBackedUserManager implements UserManager {
	private static Logger logger = Logger.getLogger(EntityManagerBackedUserManager.class);
	private static Logger auditLogger = Logger.getLogger("AuditLog");

	protected EntityManager em;

	private PersistenceManager persistenceManager;

	public EntityManagerBackedUserManager() {
	}

	public EntityManagerBackedUserManager(EntityManager em) {
		super();
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
	}

	public User findUser(String tenantName, String userID, String plainTextPassword) {
		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		UserQueryHelper.applyFullyActiveFilter(builder);

		builder.addSimpleWhere("hashPassword", User.hashPassword(plainTextPassword));
		builder.addWhere(Comparator.EQ, "userID", "userID", userID, WhereParameter.IGNORE_CASE);
		builder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE);

		return builder.getSingleResult(em);

	}

	public User findUser(String tenantName, String rfidNumber) {
		// do not allow empty rfidNumbers
		if (rfidNumber.trim().length() == 0) {
			return null;
		}

		QueryBuilder<User> builder = new QueryBuilder<User>(User.class, new OpenSecurityFilter());
		UserQueryHelper.applyFullyActiveFilter(builder);
		builder.addSimpleWhere("hashSecurityCardNumber", User.hashPassword(rfidNumber));
		builder.addWhere(Comparator.EQ, "tenantName", "tenant.name", tenantName, WhereParameter.IGNORE_CASE);

		List<User> userBeans = builder.getResultList(em, 0, 2);

		if (userBeans.size() != 1) {
			return null;
		} else {
			return userBeans.get(0);
		}

	}

	public boolean userIdIsUnique(Long tenantId, String userId) {
		return userIdIsUnique(tenantId, userId, null);
	}

	public boolean userIdIsUnique(Long tenantId, String userId, Long currentUserId) {
		if (userId == null) {
			return true;
		}

		QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(User.class, new TenantOnlySecurityFilter(tenantId)).setCountSelect();
		queryBuilder.addWhere(Comparator.EQ, "userID", "userID", userId, WhereParameter.IGNORE_CASE);

		if (currentUserId != null) {
			queryBuilder.addWhere(Comparator.NE, "id", "id", currentUserId);
		}

		return ((Long) queryBuilder.getSingleResult(em) > 0) ? false : true;
	}

	public boolean userRfidIsUnique(Long tenantId, String userRfid) {
		return userRfidIsUnique(tenantId, userRfid, null);
	}

	public boolean userRfidIsUnique(Long tenantId, String userRfid, Long currentUserId) {

		if (userRfid == null || userRfid.length() == 0) {
			// empty rfid's are always allowed
			return true;
		}

		QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(User.class, new TenantOnlySecurityFilter(tenantId)).setCountSelect();
		queryBuilder.addSimpleWhere("hashSecurityCardNumber", EncryptionUtility.getSHA1HexHash(userRfid.toUpperCase()));

		if (currentUserId != null) {
			queryBuilder.addWhere(Comparator.NE, "id", "id", currentUserId);
		}

		return ((Long) queryBuilder.getSingleResult(em) > 0) ? false : true;
	}

	public User findUserBeanByID(String tenantName, String userID) {

		QueryBuilder<User> query = new QueryBuilder<User>(User.class, new OpenSecurityFilter()).addSimpleWhere("tenant.name", tenantName).addSimpleWhere("userID", userID);
		UserQueryHelper.applyFullyActiveFilter(query);

		return query.getSingleResult(em);
	}

	public void updateUser(User user) throws DuplicateUserException {
		if (!userIdIsUnique(user.getTenant().getId(), user.getUserID(), user.getId())) {
			throw new DuplicateUserException("Account with userId " + user.getUserID() + " already exists for Tenant " + user.getTenant().getName(), user.getUserID());
		}

		em.merge(user);
	}

	public void updatePassword(Long rUser, String newPlainTextPassword) {
		User obj = em.find(User.class, rUser);
		obj.assignPassword(newPlainTextPassword);
	}

	public Long createUser(User userBean) throws DuplicateUserException, DuplicateRfidException {
		long res = 0;
		if (userBean != null) {
			if (!userIdIsUnique(userBean.getTenant().getId(), userBean.getUserID())) {
				throw new DuplicateUserException("Account with userId " + userBean.getUserID() + " already exists for Tenant " + userBean.getTenant().getName(), userBean.getUserID());
			}

			if (!userRfidIsUnique(userBean.getTenant().getId(), userBean.getHashSecurityCardNumber())) {
				throw new DuplicateRfidException("Account with hashed rfid " + userBean.getHashSecurityCardNumber() + " already exists for Tenant " + userBean.getTenant().getName(), userBean
						.getUserID(), userBean.getHashSecurityCardNumber());
			}

			em.persist(userBean);
			res = userBean.getId();
		}
		return res;
	}

	public void removeUser(Long id) {
		User obj = em.find(User.class, id);
		em.remove(obj);
	}

	public Pager<User> getUsers(SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType) {
		return getUsers(filter, onlyActive, pageNumber, pageSize, nameFilter, userType, null);
	}

	public Pager<User> getUsers(SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType, CustomerOrg customer) {
		String queryString = "from " + User.class.getName() + " ub where  " + filter.produceWhereClause(User.class, "ub") + " AND ub.active = true AND ub.userType != " + "'"
				+ UserType.SYSTEM.toString() + "'";

		if (onlyActive) {
			queryString += " AND ub.deleted = false ";
		}

		if (nameFilter != null) {
			queryString += "AND (  LOWER(ub.userID) like :nameFilter OR LOWER( ub.firstName ) like :nameFilter OR LOWER(ub.lastName) like :nameFilter )";
		}

		if (customer != null) {
			queryString += "AND ub.owner.customerOrg.id = :customer ";
		}

		if (userType != null) {
			switch (userType) {
			case READONLY:
				queryString += "AND ub.userType = " + "'" + UserType.READONLY.toString() + "'";
				break;
			case FULL:
				queryString += "AND ub.userType = " + "'" + UserType.FULL.toString() + "'";
				break;
			case LITE:
				queryString += "AND ub.userType= " + "'" + UserType.LITE.toString() + "'";
				break;
			}
		}

		String orderBy = " ORDER BY ub.firstName, ub.lastName";

		Query query = em.createQuery(queryString + orderBy);
		Query countQuery = em.createQuery("SELECT count(*) " + queryString);

		filter.applyParameters(query, User.class);
		filter.applyParameters(countQuery, User.class);
		if (nameFilter != null) {
			String filterMatcher = '%' + nameFilter.toLowerCase() + '%';
			query.setParameter("nameFilter", filterMatcher);
			countQuery.setParameter("nameFilter", filterMatcher);
		}

		if (customer != null) {
			query.setParameter("customer", customer.getId());
			countQuery.setParameter("customer", customer.getId());
		}

		return new Page<User>(query, countQuery, pageNumber, pageSize);
	}

	/**
	 * Returns a collection of users for a particular tenant and end user
	 * 
	 * @param tenantid
	 *            the tenant to return the list of users for
	 * @param endUserID
	 *            optional argument to filter the list to a particular end user
	 */
	@SuppressWarnings("unchecked")
	public List<ListingPair> getUserList(SecurityFilter filter) {

		String queryString = "select new com.n4systems.util.ListingPair( ub.id, CONCAT(ub.firstName, ' ', ub.lastName ) ) from " + User.class.getName() + " ub where ub.userType !='"
				+ UserType.SYSTEM.toString() + "' and ub.active = true and " + filter.produceWhereClause(User.class, "ub") + " ORDER BY ub.firstName, ub.lastName";

		Query query = em.createQuery(queryString);
		filter.applyParameters(query, User.class);

		return (List<ListingPair>) query.getResultList();
	}

	// TODO extract to a loader it is only used by one call on event crud.
	@SuppressWarnings("unchecked")
	public List<ListingPair> getExaminers(SecurityFilter filter) {
		SecurityFilter justTenantFilter = new TenantOnlySecurityFilter(filter.getTenantId());
		String queryString = "select DISTINCT ub from " + User.class.getName() + " ub where ub.active = true and deleted = false and ub.userType != '" + UserType.SYSTEM.toString() + "' and ( "
				+ filter.produceWhereClause(User.class, "ub") + " OR ( " + justTenantFilter.produceWhereClause(User.class, "ub") + " AND ub.owner.customerOrg IS NULL) )"
				+ " ORDER BY ub.firstName, ub.lastName";
		Query query = em.createQuery(queryString);
		filter.applyParameters(query, User.class);
		justTenantFilter.applyParameters(query, User.class);

		// get the userlist and filter out users not having the create/edit
		// inspect
		List<User> users = Permissions.filterHasOneOf((List<User>) query.getResultList(), Permissions.ALLEVENT);

		return ListHelper.longListableToListingPair(users);
	}

	public void saveUserRequest(UserRequest userRequest, User userAccount) throws DuplicateUserException, DuplicateRfidException {
		userAccount.setActive(false);
		createUser(userAccount);
		persistenceManager.save(userRequest);
		auditLogger.info("user request created for tenant " + userRequest.getTenant().getDisplayName() + " for user " + userRequest.getUserAccount().getUserID());
	}

	public void acceptRequest(UserRequest userRequest) {
		UserRequest request = em.find(UserRequest.class, userRequest.getId());
		userRequest.getUserAccount().setActive(true);

		em.merge(userRequest.getUserAccount());
		em.remove(request);
		auditLogger.info("user request accepted for tenant " + userRequest.getTenant().getDisplayName() + " for user " + userRequest.getUserAccount().getUserID());
	}

	public void denyRequest(UserRequest userRequest) {
		UserRequest request = em.find(UserRequest.class, userRequest.getId());
		User user = request.getUserAccount();
		em.remove(request);
		em.remove(user);
		auditLogger.info("user request denied for tenant " + userRequest.getTenant().getDisplayName() + " for user " + userRequest.getUserAccount().getUserID());
	}

	public void createAndEmailLoginKey(User user, URI baseUri) throws MessagingException {

		user.createResetPasswordKey();
		em.merge(user);

		logger.info("Created loginkey for User [" + user.getUserID() + "], Tenant [" + user.getTenant().getName() + "]");

		String resetPasswordUrl = baseUri.resolve("resetPassword.action").toString() + "?u=" + user.getUserID() + "&k=" + user.getResetPasswordKey();
		String loginUrl = baseUri.toString();
		String messageBody = "You have requested to reset your Field ID Password. To reset your password please visit the following link: <br/><br/>" + "<a href=\"" + resetPasswordUrl + "\">"
				+ resetPasswordUrl + "</a><br/><br/>" + "You can then login securely to your account at:<br/><br/>" + "<a href=\"" + loginUrl + "\">" + loginUrl + "</a>";

		MailMessage message = new MailMessage("Password Reset ", messageBody);
		message.getToAddresses().add(user.getEmailAddress());
		logger.info("Sending loginkey notification to [" + user.getEmailAddress() + "]");

		MailManagerFactory.defaultMailManager(ConfigContext.getCurrentContext()).sendMessage(message);
	}

	public User findUserByResetKey(String tenantName, String userName, String resetPasswordKey) {
		if (resetPasswordKey != null) {
			User user = findUserBeanByID(tenantName, userName);
			if (user != null) {
				if (resetPasswordKey.equals(user.getResetPasswordKey()) && DateHelper.withinTheLastHour(user.getModified())) {
					user.clearResetPasswordKey();
					em.merge(user);
					return user;
				}

			}
		}
		return null;
	}

}