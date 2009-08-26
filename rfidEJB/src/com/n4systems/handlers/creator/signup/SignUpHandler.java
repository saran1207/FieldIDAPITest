package com.n4systems.handlers.creator.signup;

import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.persistence.PersistenceProvider;

public interface SignUpHandler {

	public void signUp(SignUpRequest signUpRequest);
	
	public SignUpHandler withPersistenceProvider(PersistenceProvider persistenceProvider);

}