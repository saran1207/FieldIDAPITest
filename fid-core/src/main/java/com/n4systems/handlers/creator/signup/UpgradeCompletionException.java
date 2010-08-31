package com.n4systems.handlers.creator.signup;

import com.n4systems.subscription.UpgradeResponse;

public class UpgradeCompletionException extends Exception {

	private final UpgradeResponse response;
	
	public UpgradeCompletionException(UpgradeResponse response) {
		this.response = response;
	}

	public UpgradeCompletionException(UpgradeResponse response,String message) {
		super(message);
		this.response = response;
	}

	public UpgradeCompletionException(UpgradeResponse response,Throwable cause) {
		super(cause);
		this.response = response;
	}

	public UpgradeCompletionException(UpgradeResponse response, String message, Throwable cause) {
		super(message, cause);
		this.response = response;
	}

	public UpgradeResponse getResponse() {
		return response;
	}

}
