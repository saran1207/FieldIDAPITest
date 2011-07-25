package com.n4systems.fieldid.service.user;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;
import com.n4systems.exceptions.LoginException;
import com.n4systems.fieldid.service.FieldIdService;


// *NOT* transactional...no DB required.
public class LoginService extends FieldIdService {

	// TODO DD : eventually move all of legacy login related code to here.  (findUser etc...)
	
	// dangerous architecture if you ever have to cluster!
	// this map is used to keep track of failed logins in memory.
	// using DB has its own problems but this technique will falter if multiple JVM's are used
	private ConcurrentMap<String, LoginException> failedLogins = new MapMaker()	   
																		   .maximumSize(10000)
																		   .expireAfterWrite(30, TimeUnit.MINUTES)
																		   .makeMap();
			
	public LoginException trackFailedLoginAttempts(LoginException e) {
		LoginException previousException = failedLogins.get(e.getUserId());		
		LoginException mergedException = previousException==null ? e : e.merge(previousException);
		failedLogins.put(e.getUserId(), mergedException);
		return mergedException;
	}
 
}
