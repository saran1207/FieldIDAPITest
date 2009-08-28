package com.n4systems.subscription;

public interface ValidatePromoCodeResponse extends Response {

	public boolean isValid();
	
	/**
	 * @return null if valid code.  Otherwise reason that code is invalid.
	 */
	public InvalidPromoCodeReason getReason();
	
}
