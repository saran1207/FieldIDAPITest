package com.n4systems.model.api;

import com.n4systems.model.security.SecurityLevel;

public interface SecurityEnhanced<T> {
	/**
	 * Enhances the security of this entity to the specified level
	 * @param level	SecurityLevel to enhance to
	 * @return		A proxied, enhanced version of the entity
	 */
	public T enhance(SecurityLevel level);
}
