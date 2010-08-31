package com.n4systems.model.security;

public enum SecurityLevel {
	ALLOWED			(0),
	LOCAL			(1),	// from a network distance perspective LOCAL and LOCAL_ENDUSER are the same thing
	LOCAL_ENDUSER	(1),	
	DIRECT			(2),
	ONE_AWAY		(3),
	MANY_AWAY		(4),
	DENIED			(5);

	private int level;
	
	SecurityLevel(int level) {
		this.level = level;
	}
	
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
		
		return (level >= other.level);
	}	
}
