package com.n4systems.persistence.loaders;

import com.n4systems.model.signuppackage.SignupPackageBySyncIdLoader;

public class NonSecureLoaderFactory {

	/* 
	 * NOTE: Please do a Source -> Sort Members in Eclipse after adding methods to this factory.
	 */

	
	public static SignupPackageBySyncIdLoader createSignupPackageBySyncIdLoader() {
		return new SignupPackageBySyncIdLoader();
	}
	
}
