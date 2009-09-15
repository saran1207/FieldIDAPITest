package rfid.ejb.session;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.MailManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.DuplicateRfidException;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.model.UserRequest;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;
import com.n4systems.util.mail.MailMessage;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class UserManager implements User {
	private static Logger logger = Logger.getLogger(UserManager.class);
	private static Logger auditLogger = Logger.getLogger("AuditLog");
	private static final String defaultTablePrefix = "u";
	
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;
	
	@EJB private PersistenceManager persistenceManager;
	@EJB private MailManager mailManager;
	
	public UserBean findUser(String tenantName, String userID, String plainTextPassword) {
		
		if (userID != null) userID = userID.toLowerCase();
		if (tenantName != null) tenantName = tenantName.toLowerCase();
		
		UserBean userBean = null;
		try {
			
			Query query = em.createQuery(generateFromClause() + "where lower(u.tenant.name) = :tenantName and lower(u.userID) = :userID and u.hashPassword = :password and u.deleted = false and u.active = true");
			query.setParameter ("tenantName", tenantName);
			query.setParameter ("userID", userID);
			query.setParameter ("password", UserBean.hashPassword(plainTextPassword));
			
			userBean = (UserBean)query.getSingleResult();
		} catch(NoResultException e) {}
		
		return userBean;
	}
	
	@SuppressWarnings("unchecked")
	public UserBean findUser(String tenantName, String rfidNumber) {
		// do not allow empty rfidNumbers
		if(rfidNumber.trim().length() == 0) {
			return null;
		}
		
		List<UserBean> userBeans = (List<UserBean>)em.createQuery(generateFromClause() + "where u.tenant.name = :tenantName and u.hashSecurityCardNumber = :hashSecurityCardNumber and u.deleted = false and u.active = true")
			.setParameter ("hashSecurityCardNumber", UserBean.hashSecurityCardNumber(rfidNumber))
			.setParameter("tenantName", tenantName).setMaxResults(2)
			.getResultList();
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
	
		String currentUserFilter = "";
		if(currentUserId != null) {
			currentUserFilter = " and u.uniqueID <> :uniqueID";
		}
		
		if (userId != null) userId = userId.toLowerCase();
		
		Query query = em.createQuery("select count(u) from UserBean u where u.tenant.id = :tenantId and lower(u.userID) = :userId" + currentUserFilter);
		
		query.setParameter ("tenantId", tenantId);
		query.setParameter ("userId", userId);
		
		if(currentUserId != null) {
			query.setParameter("uniqueID", currentUserId);
		}
		
		return ((Long)query.getSingleResult() > 0) ? false : true;
	}
	
	public boolean userRfidIsUnique(Long tenantId, String userRfid) {
		return userRfidIsUnique(tenantId, userRfid, null);
	}
	
	public boolean userRfidIsUnique(Long tenantId, String userRfid, Long currentUserId) {
		
		if(userRfid == null || userRfid.length() == 0) {
			// empty rfid's are always allowed
			return true;
		}
		
		String currentUserFilter = "";
		if(currentUserId != null) {
			currentUserFilter = " and u.uniqueID <> :uniqueID";
		}
		
		Query query = em.createQuery("select count(u) from UserBean u where u.tenant.id = :tenantId and u.hashSecurityCardNumber = :userRfid" + currentUserFilter);
		
		query.setParameter ("tenantId", tenantId);
		query.setParameter ("userRfid", UserBean.hashSecurityCardNumber(userRfid));
		
		if(currentUserId != null) {
			query.setParameter("uniqueID", currentUserId);
		}
		
		return ((Long)query.getSingleResult() > 0) ? false : true;
	}
	
	public UserBean findUserBeanByID(String tenantName, String userID) {
		UserBean userBean = null;
		
		try {
			userBean = (UserBean)em.createQuery(generateFromClause() + "where u.tenant.name = :tenantName and u.userID = :userID and u.deleted = false and u.active = true ")
				.setParameter ("tenantName", tenantName)
				.setParameter ("userID", userID)
				.getSingleResult();
			
		} catch(NoResultException e) {}
		
		return userBean;
	}	
	
	public UserBean findUserBeanByIDWithDeleted(String tenantName, String userID) {
		try {
			UserBean user = (UserBean)em.createQuery("from UserBean u where u.tenant.name = :tenantName and u.userID = :userID ")
				.setParameter ("tenantName", tenantName)
				.setParameter ("userID", userID)
				.getSingleResult();
			return user;

		} catch (NoResultException e) {
			return null;
		}
	}
	
	public UserBean findUserBean(Long uniqueID) {
		return em.find(UserBean.class, uniqueID);
	}
	
	public UserBean findUser(Long uniqueID, Long tenantId) {
		Query query = em.createQuery("from UserBean u where u.tenant.id = :tenantId and u.uniqueID = :uniqueID");
		query.setParameter("uniqueID", uniqueID);
		query.setParameter("tenantId", tenantId);
		
		UserBean user = null;
		try {
			user = (UserBean)query.getSingleResult();
		} catch (NoResultException e) {
			// just ignore
		}
		
		return user;
	}
	
	public void updateUser(UserBean user) throws DuplicateUserException {
		if(!userIdIsUnique(user.getTenant().getId(), user.getUserID(), user.getUniqueID())) {
			throw new DuplicateUserException("Account with userId " + user.getUserID() + " already exists for Tenant " + user.getTenant().getName(), user.getUserID());
		}
		
			
		em.merge(user);
	}

	public void updatePassword(Long rUser, String newPlainTextPassword) {
		UserBean obj = em.find(UserBean.class, rUser);
		obj.assignPassword(newPlainTextPassword);
	}
	
	public Long createUser(UserBean userBean) throws DuplicateUserException, DuplicateRfidException {
		long res = 0;
		if (userBean != null) {
			if(!userIdIsUnique(userBean.getTenant().getId(), userBean.getUserID())) {
				throw new DuplicateUserException("Account with userId " + userBean.getUserID() + " already exists for Tenant " + userBean.getTenant().getName(), userBean.getUserID());
			}
			
			if(!userRfidIsUnique(userBean.getTenant().getId(), userBean.getHashSecurityCardNumber())) {
				throw new DuplicateRfidException("Account with hashed rfid " + userBean.getHashSecurityCardNumber() + " already exists for Tenant " + userBean.getTenant().getName(), userBean.getUserID(), userBean.getHashSecurityCardNumber());
			}
			
			em.persist( userBean );
			res = userBean.getUniqueID();
		}
		return res;
	}

	public void removeUser(Long uniqueID) {
		UserBean obj = em.find(UserBean.class, uniqueID);
		em.remove(obj);
	}
	
	public UserBean getUser(Long uniqueID) {		
		return (UserBean) em.find(UserBean.class, uniqueID);		
	}
	
	public UserBean getUserWithSignature(Long uniqueID) {
		Query query = em.createQuery("from UserBean fetch all properties where uniqueID = :uniqueID");
		query.setParameter("uniqueID",uniqueID);
		
		return (UserBean)query.getSingleResult();
	}
	
	public UserBean getUserWithSignature(Long uniqueID, Long tenantId) {
		Query query = em.createQuery("from UserBean fetch all properties where uniqueID = :uniqueID and tenant.id = :tenantId ");
		query.setParameter("uniqueID",uniqueID);
		query.setParameter("tenantId", tenantId);
		
		UserBean user = null;
		try {
			user = (UserBean)query.getSingleResult();
		} catch (NoResultException e) {
			// just ignore 
		}
		
		return user;
	}
	
	public Pager<UserBean> getUsers( SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType ) {
		return getUsers( filter, onlyActive, pageNumber, pageSize, nameFilter, userType, null );
	}

	public Pager<UserBean> getUsers( SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType, CustomerOrg customer ) {
		
		String queryString = "from UserBean ub where  " + filter.produceWhereClause(UserBean.class, "ub") 
			+ " AND ub.active = true AND ub.system = false ";
		
		if(onlyActive) {
			queryString += " AND ub.deleted = false ";
		}
		
		if( nameFilter != null ) {
			queryString += "AND (  LOWER(ub.userID) like :nameFilter OR LOWER( ub.firstName ) like :nameFilter " +
					"OR LOWER(ub.lastName) like :nameFilter )";
		}
		
		if( customer != null ) {
			queryString += "AND ub.owner.customerOrg.id = :customer ";
		} 
		
		if( userType != null ) {
			switch( userType ) {
				case CUSTOMERS:
					queryString += "AND ub.owner.customerOrg IS NOT NULL ";
					break;
				case EMPLOYEES:
					queryString += "AND ub.owner.customerOrg IS NULL ";
					break;
					
			}
		}
		
		String orderBy = " ORDER BY ub.firstName, ub.lastName";
		
		Query query = em.createQuery( queryString + orderBy);
		Query countQuery = em.createQuery( "SELECT count(*) " + queryString);
		
		filter.applyParameters(query, UserBean.class );
		filter.applyParameters(countQuery, UserBean.class);
		if( nameFilter != null ) {
			String filterMatcher = '%' +nameFilter.toLowerCase()  + '%';
			query.setParameter( "nameFilter", filterMatcher );
			countQuery.setParameter( "nameFilter", filterMatcher );
		}
		
		if( customer != null ) {
			query.setParameter( "customer", customer.getId() );
			countQuery.setParameter( "customer", customer.getId() );
		}
		
		return new Page<UserBean>(query, countQuery, pageNumber, pageSize);
	}
	
	
	/**
	 * Returns a collection of users for a particular tenant and end user 
	 * @param tenantid the tenant to return the list of users for
	 * @param endUserID optional argument to filter the list to a particular end user
	 */
	@SuppressWarnings("unchecked")
	public List<ListingPair> getUserList( SecurityFilter filter ) {
		
		
		String queryString = "select new com.n4systems.util.ListingPair( ub.uniqueID, CONCAT(ub.firstName, ' ', ub.lastName ) ) from UserBean ub where ub.system = false and ub.active = true and " + 
			filter.produceWhereClause(UserBean.class, "ub") + " ORDER BY ub.firstName, ub.lastName";
		
		Query query = em.createQuery(queryString);
		filter.applyParameters(query, UserBean.class);
		
		return (List<ListingPair>)query.getResultList();
	}
	
	/**
	 * Returns a collection of users for a particular tenant and end user 
	 * @param tenantid the tenant to return the list of users for
	 * @param endUserID optional argument to filter the list to a particular end user
	 */
	//TODO extract to a loaded it is only used by one call on inspection crud.
	@SuppressWarnings("unchecked")
	public List<ListingPair> getInspectorList( SecurityFilter filter ) {
		SecurityFilter justTenantFilter = new TenantOnlySecurityFilter(filter.getTenantId());
		String queryString = "select DISTINCT ub from UserBean ub where ub.active = true and deleted = false and ub.system = false and ( " + 
					filter.produceWhereClause(UserBean.class, "ub") + " OR ( "+
					justTenantFilter.produceWhereClause(UserBean.class, "ub") + " AND ub.owner.customerOrg IS NULL) )" +
					" ORDER BY ub.firstName, ub.lastName";
		try {
		Query query = em.createQuery(queryString);
		filter.applyParameters(query, UserBean.class);
		justTenantFilter.applyParameters(query, UserBean.class);
		
		// get the userlist and filter out users not having the create/edit inspect
		List<UserBean> users = Permissions.filterHasOneOf((List<UserBean>)query.getResultList(), Permissions.ALLINSPECTION);
		
		return ListHelper.longListableToListingPair(users);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns a collection of users for a particular tenant and end user 
	 * @param tenantid the tenant to return the list of users for
	 * @param endUserID optional argument to filter the list to a particular end user
	 * @deprecated Use UserListableLoader
	 */
	public List<ListingPair> getEmployeeList( SecurityFilter filter ) {
		return getEmployeeList(filter, false);
	}
	
	/**
	 * @deprecated Use UserListableLoader
	 */
	@SuppressWarnings("unchecked")
	public List<ListingPair> getEmployeeList(SecurityFilter filter, boolean withOutDeleted) {
		String queryString = "select DISTINCT ub from UserBean ub where ub.active = true and ub.system = false and ub.r_EndUser IS NULL AND ";
		if (withOutDeleted) {
			queryString += " ub.deleted = false AND ";
		}
		queryString += filter.produceWhereClause(UserBean.class, "ub") + " ORDER BY ub.firstName, ub.lastName";
		
		Query query = em.createQuery(queryString);
		filter.applyParameters(query, UserBean.class);
		
		return ListHelper.longListableToListingPair( (List<UserBean>)query.getResultList() );
	}
	
	public void saveUserRequest( UserRequest userRequest, UserBean userAccount ) throws DuplicateUserException, DuplicateRfidException {
		userAccount.setActive( false );
		createUser( userAccount );
		persistenceManager.save( userRequest );
		auditLogger.info( "user request created for tenant " + userRequest.getTenant().getDisplayName() + " for user " + userRequest.getUserAccount().getUserID() );	
	}
	
	public void acceptRequest( UserRequest userRequest ) {
		UserRequest request = em.find( UserRequest.class, userRequest.getId() );
		userRequest.getUserAccount().setActive(true);
		// send email to the user
		
		em.merge( userRequest.getUserAccount() );
		em.remove(request);
		auditLogger.info( "user request accepted for tenant " + userRequest.getTenant().getDisplayName() + " for user " + userRequest.getUserAccount().getUserID() );
	}
	
	public void denyRequest( UserRequest userRequest ) {
		UserRequest request = em.find( UserRequest.class, userRequest.getId() );
		UserBean user = request.getUserAccount();
		em.remove( request );
		em.remove( user );
		auditLogger.info( "user request denied for tenant " + userRequest.getTenant().getDisplayName() + " for user " + userRequest.getUserAccount().getUserID() );
	}
	
	private String generateFromClause() {
		return "from " + UserBean.class.getName() + " " + defaultTablePrefix + " ";
	}
	
	public void createAndEmailLoginKey(UserBean user, URI baseUri) throws MessagingException {
		
		user.createResetPasswordKey();
		em.merge( user );
		
		logger.info("Created loginkey for User [" + user.getUserID() + "], Tenant [" + user.getTenant().getName() + "]");
		
		String resetPasswordUrl = baseUri.resolve("resetPassword.action").toString() + "?u=" + user.getUserID() + "&k=" + user.getResetPasswordKey();
		String loginUrl =  baseUri.toString();
		String messageBody = "You have requested to reset your Field ID Password. To reset your password please visit the following link: <br/><br/>" +
							"<a href=\"" + resetPasswordUrl + "\">" + resetPasswordUrl + "</a><br/><br/>" +
							"You can then login securely to your account at:<br/><br/>" +
							"<a href=\"" + loginUrl + "\">" + loginUrl + "</a>";
		
		MailMessage message = new MailMessage("Password Reset ", messageBody);
		message.getToAddresses().add(user.getEmailAddress());
		logger.info("Sending loginkey notification to [" + user.getEmailAddress() + "]");
		
		mailManager.sendMessage(message);
	}
	
	public UserBean findUserByResetKey( String tenantName, String userName, String resetPasswordKey ) {
		if( resetPasswordKey != null ) {
			UserBean user = findUserBeanByID( tenantName, userName );
			if( user != null ) {
				if( resetPasswordKey.equals( user.getResetPasswordKey() ) && DateHelper.withinTheLastHour( user.getDateModified() ) ) {
					user.clearResetPasswordKey();
					em.merge( user );
					return user;
				}
				
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
    public List<UserBean> getOuterUserList(Long tenantId, Long ownerId, Long userId, SecurityFilter filter) {
		StringBuilder jpql = new StringBuilder();
		
		jpql.append("from ");
		jpql.append(UserBean.class.getName());
		jpql.append(" WHERE tenant.id = :tenantId AND active = :active AND deleted = :deleted AND system = :system AND uniqueID <> :userId AND");
		
		jpql.append(filter.produceWhereClause(UserBean.class));
		
		if (ownerId != null) {
			jpql.append(" AND (owner.id = :ownerId OR owner.customerOrg IS NULL)");
		}
		
		jpql.append(" ORDER BY owner.name, firstName, lastName");
		
		Query userQuery = em.createQuery(jpql.toString());
		
		filter.applyParameters(userQuery, UserBean.class);
		userQuery.setParameter("tenantId", tenantId);
		userQuery.setParameter("userId", userId);
		userQuery.setParameter("active", true);
		userQuery.setParameter("deleted", false);
		userQuery.setParameter("system", false);
		
		if (ownerId != null) {
			userQuery.setParameter("ownerId", ownerId);
		}
		
		return (List<UserBean>)userQuery.getResultList();
	}
}