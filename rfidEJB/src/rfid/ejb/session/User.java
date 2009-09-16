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
	
	
}
