package com.n4systems.fieldid.service.user;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;
import com.n4systems.exceptions.LoginException;
import com.n4systems.exceptions.LoginFailureInfo;
import com.n4systems.fieldid.service.FieldIdService;


// *NOT* transactional...no DB required.
public class LoginService extends FieldIdService {

	// dangerous architecture if you ever have to cluster!
	// this map is used to keep track of failed logins in memory. they will remain in the map for a limited time. 
	// using DB has its own problems but this technique will falter if multiple JVM's are used
	private ConcurrentMap<String, LoginFailureInfo> failedLogins = new MapMaker()	   
																		   .maximumSize(10000)
																		   .expireAfterWrite(getExpiryTime(), getTimeUnits())
																		   .makeMap();

	
	public LoginException trackLoginFailure(LoginFailureInfo failureInfo) {
		LoginFailureInfo previousFailure = failedLogins.get(failureInfo.getUserId());		
		LoginFailureInfo newFailure = failureInfo.merge(previousFailure);
		failedLogins.put(failureInfo.getUserId(), newFailure);
		return new LoginException(newFailure);
	}

	public void resetFailedLoginAttempts(String userID) {
		failedLogins.remove(userID.toLowerCase());
	}
 
	@Deprecated // only to make unit testing possible via extract/override 
	TimeUnit getTimeUnits() {
		return TimeUnit.MINUTES;
	}
	
	@Deprecated // only to make unit testing possible via extract/override
	long getExpiryTime() {
		return 30;
	}
	
}
