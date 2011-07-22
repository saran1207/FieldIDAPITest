package com.n4systems.ejb.legacy.wrapper;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;

import com.google.common.collect.MapMaker;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.ejb.legacy.impl.EntityManagerBackedUserManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.exceptions.DuplicateRfidException;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.exceptions.LoginException;
import com.n4systems.model.UserRequest;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.UserType;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListingPair;

public class UserEJBContainer extends EJBTransactionEmulator<UserManager> implements UserManager {

	// TODO DD : convert this map into spring bean and inject.
	
	// dangerous architecture if you ever have to cluster...this is used to keep track of failed logins in memory.
	// using DB has its own problems but this technique will falter if multiple JVM's are used
	private static ConcurrentMap<String, LoginException> failedLogins = new MapMaker()	   
																		   .maximumSize(10000)
																		   .expireAfterWrite(30, TimeUnit.MINUTES)
																		   .makeMap();
		
	@Override
	protected UserManager createManager(EntityManager em) {
		return new EntityManagerBackedUserManager(em);
	}

	@Override
	public void acceptRequest(UserRequest userRequest) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).acceptRequest(userRequest);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public void createAndEmailLoginKey(User user, URI baseURI) throws MessagingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).createAndEmailLoginKey(user, baseURI);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public Long createUser(User userBean) throws DuplicateUserException, DuplicateRfidException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createUser(userBean);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public void denyRequest(UserRequest userRequest) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).denyRequest(userRequest);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	@Override
	public User findUser(String tenantName, String userID, String plainTextPassword) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findUser(tenantName, userID, plainTextPassword);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public void lockUser(final String tenantName, final String userID, final Integer duration, final Integer failedLoginAttempts) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).lockUser(tenantName, userID, duration, failedLoginAttempts);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}		
	}
	
	@Override
	public User findUserByPw(final String tenantName, final String userID, final String plainTextPassword) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findUserByPw(tenantName, userID, plainTextPassword);		  
		} catch (RuntimeException e) {
			if (e instanceof LoginException) { 
				trackFailedLoginAttempts((LoginException)e);
			}
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}		
	}

	private void trackFailedLoginAttempts(LoginException e) {
		LoginException previousException = failedLogins.get(e.getUserId());		
		failedLogins.put(e.getUserId(), previousException==null ? e : e.merge(previousException));		
	}

	@Override
	public User findUser(String tenantName, String rfidNumber) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findUser(tenantName, rfidNumber);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}



	@Override
	public User findUserBeanByID(String tenantName, String userID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findUserBeanByID(tenantName, userID);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	@Override
	public User findAndClearResetKey(String tenantName, String userName, String resetPasswordKey) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAndClearResetKey(tenantName, userName, resetPasswordKey);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

    @Override
	public boolean resetKeyIsValid(String tenantName, String userName, String resetPasswordKey) {
        TransactionManager transactionManager = new FieldIdTransactionManager();
        Transaction transaction = transactionManager.startTransaction();
        try {
            return createManager(transaction.getEntityManager()).resetKeyIsValid(tenantName, userName, resetPasswordKey);

        } catch (RuntimeException e) {
            transactionManager.rollbackTransaction(transaction);

            throw e;
        } finally {
            transactionManager.finishTransaction(transaction);
        }
    }

    @Override
	public List<ListingPair> getExaminers(SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getExaminers(filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	
	@Override
	public List<ListingPair> getUserList(SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getUserList(filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public Pager<User> getUsers(SecurityFilter filter, boolean onlyActive, int pageNumber, int pageSize, String nameFilter, UserType userType, CustomerOrg customer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getUsers(filter, onlyActive, pageNumber, pageSize, nameFilter, userType, customer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public Pager<User> getUsers(SecurityFilter filter, boolean activeOnly, int pageNumber, int pageSize, String nameFilter, UserType userType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getUsers(filter, activeOnly, pageNumber, pageSize, nameFilter, userType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}



	@Override
	public void removeUser(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removeUser(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public void saveUserRequest(UserRequest userRequest, User userAccount) throws DuplicateUserException, DuplicateRfidException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).saveUserRequest(userRequest, userAccount);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public void updatePassword(Long rUser, String newPassword) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).updatePassword(rUser, newPassword);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public void updateUser(User dto) throws DuplicateUserException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).updateUser(dto);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public boolean userIdIsUnique(Long tenantId, String userId, Long currentUserId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).userIdIsUnique(tenantId, userId, currentUserId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public boolean userIdIsUnique(Long tenantId, String userId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).userIdIsUnique(tenantId, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public boolean userRfidIsUnique(Long tenantId, String rfidNumber, Long currentUserId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).userRfidIsUnique(tenantId, rfidNumber, currentUserId);
		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
	
	
}
