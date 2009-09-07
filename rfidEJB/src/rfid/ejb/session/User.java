package rfid.ejb.session;

import java.net.URI;
import java.util.List;

import javax.ejb.Local;
import javax.mail.MessagingException;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.DuplicateRfidException;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.model.UserRequest;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;

@Local
public interface User {
	public UserBean findUserBean(Long uniqueID);
	public UserBean findUser(Long uniqueID, Long tenantId);
	public UserBean findUser(String tenantName, String userID, String plainTextPassword);
	public UserBean findUser(String tenantName, String rfidNumber);

	public boolean userIdIsUnique(Long tenantId, String userId);
	public boolean userIdIsUnique(Long tenantId, String userId, Long currentUserId);
	public boolean userRfidIsUnique(Long tenantId, String rfidNumber);
	public boolean userRfidIsUnique(Long tenantId, String rfidNumber, Long currentUserId);
	
	public UserBean findUserBeanByID(String tenantName, String userID);
	
	public UserBean findUserBeanByIDWithDeleted(String tenantName, String userID) ;
	
	public Long createUser(UserBean userBean) throws DuplicateUserException, DuplicateRfidException;
	public void updateUser(UserBean dto) throws DuplicateUserException;
	public void updatePassword(Long rUser, String newPassword);	
	public void removeUser(Long uniqueID);
	public UserBean getUser(Long uniqueID);
	public UserBean getUserWithSignature(Long uniqueID);
	public UserBean getUserWithSignature(Long uniqueID, Long tenantId);
	
	public List<ListingPair> getUserList( SecurityFilter filter );
	public List<ListingPair> getInspectorList( SecurityFilter filter );
	/**
	 * @deprecated Use UserListableLoader
	 */
	public List<ListingPair> getEmployeeList( SecurityFilter filter );
	/**
	 * @deprecated Use UserListableLoader
	 */
	public List<ListingPair> getEmployeeList( SecurityFilter filter, boolean withOutDeleted );
	
	public Pager<UserBean> getUsers( SecurityFilter filter, boolean activeOnly, int pageNumber, int pageSize, String nameFilter, UserType userType );
	public Pager<UserBean> getUsers( SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType, CustomerOrg customer );

	public void saveUserRequest( UserRequest userRequest, UserBean userAccount ) throws DuplicateUserException, DuplicateRfidException;
	public void acceptRequest( UserRequest userRequest ) ;
	public void denyRequest( UserRequest userRequest ) ;

	public void createAndEmailLoginKey(UserBean user, URI baseURI) throws MessagingException ;
	public UserBean findUserByResetKey( String tenantName, String userName, String resetPasswordKey );
	
	/**
	 * Constructs an 'Outer' list of active users.  Given an non-null, tenant, customer and division, the set of users returned 
	 * will be:
	 * <ol>
	 *	<li>Employee users for this tenant</li>
	 *	<li>Customer users for this tenant and Customer (no division)</li>
	 *	<li>Division users for this tenant, Customer & Division</li>
	 * </ol>
	 * This method relies on the security filter to ensure that users are not returned outside of ones permissions.
	 * Users are returned sorted by owner, first name and last name (in that order).
	 * @param tenantId		Id of a Tenant
	 * @param customerId	Id of a BaseOrg
	 * @param userId		Id of a User to NOT include in the final list
	 * @param filter		A SecurityFitler
	 * @return				The set of User objects.
	 */
	public List<UserBean> getOuterUserList(Long tenantId, Long ownerId, Long userId, SecurityFilter filter);
}
