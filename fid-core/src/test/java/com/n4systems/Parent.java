/**
 * 
 */
package com.n4systems;

import com.n4systems.model.security.AllowSafetyNetworkAccess;

public abstract class Parent {
	@UseThis
	@AllowSafetyNetworkAccess
	public Long getId() {
		return 10L;
	}
	
	@UseThis
	public Long getSomeValue() {
		return 40L;
	}
}