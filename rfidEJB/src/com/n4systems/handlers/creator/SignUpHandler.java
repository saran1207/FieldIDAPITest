package com.n4systems.handlers.creator;

import com.n4systems.persistence.PersistenceProvider;

public interface SignUpHandler {

	public void signUp(SignUpRequest signUpRequest);
	
	public SignUpHandler withPersistenceProvider(PersistenceProvider persistenceProvider);

}