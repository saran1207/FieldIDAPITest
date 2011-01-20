package com.n4systems.ejb.legacy;

import java.net.URI;
import java.util.List;

import javax.mail.MessagingException;


import com.n4systems.exceptions.DuplicateRfidException;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.model.UserRequest;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;

public interface UserManager {
	
	public User findUser(String tenantName, String userID, String plainTextPassword);
	public User findUser(String tenantName, String rfidNumber);

	public boolean userIdIsUnique(Long tenantId, String userId);
	public boolean userIdIsUnique(Long tenantId, String userId, Long currentUserId);
	public boolean userRfidIsUnique(Long tenantId, String rfidNumber, Long currentUserId);
	
	public User findUserBeanByID(String tenantName, String userID);
	
	
	public Long createUser(User userBean) throws DuplicateUserException, DuplicateRfidException;
	public void updateUser(User dto) throws DuplicateUserException;
	public void updatePassword(Long rUser, String newPassword);	
	public void removeUser(Long uniqueID);
	
	
	
	public List<ListingPair> getUserList( SecurityFilter filter );
	public List<ListingPair> getExaminers( SecurityFilter filter );

	
	public Pager<User> getUsers( SecurityFilter filter, boolean activeOnly, int pageNumber, int pageSize, String nameFilter, UserType userType );
	public Pager<User> getUsers( SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType, CustomerOrg customer );

	public void saveUserRequest( UserRequest userRequest, User userAccount ) throws DuplicateUserException, DuplicateRfidException;
	public void acceptRequest( UserRequest userRequest ) ;
	public void denyRequest( UserRequest userRequest ) ;

	public void createAndEmailLoginKey(User user, URI baseURI) throws MessagingException ;
	public User findAndClearResetKey(String tenantName, String userName, String resetPasswordKey);

    public boolean resetKeyIsValid(String tenantName, String userName, String resetPasswordKey);
	
}
