package com.n4systems.model.security;

public enum SecurityLevel {
	// These values MUST be kept in order of least restricted to most restricted
	ALLOWED,
	LOCAL,
	DIRECT,
	ONE_AWAY,
	MANY_AWAY,
	DENIED;
	
	public boolean isLocal() {
		return (this.equals(ALLOWED) || this.equals(LOCAL));
	}
	
	public boolean isFromNetwork() {
		return !isLocal();
	}
	
	public boolean allows(SecurityLevel other) {
		// ALLOWED on either side is always allowed
		if (this.equals(ALLOWED) || other.equals(ALLOWED)) {
			return true;
		}
		
		// DENIED on either side is never allowed
		if (this.equals(DENIED) || other.equals(DENIED)) {
			return false;
		}
		
		/*
		 * Default to comparing the ordinal.
		 * Ordinal is the position of the enum in the list starting at 0.
		 * eg/ LOCAL will have a lower ordinal then ONE_AWAY and thus will deny access.
		 */
		return (ordinal() >= other.ordinal());
	}	
}
