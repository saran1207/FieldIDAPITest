/**
 * 
 */
package com.n4systems;

import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

public abstract class Parent {
	@UseThis
	@NetworkAccessLevel(value=SecurityLevel.ALLOWED)
	public Long getId() {
		return 10L;
	}
	
	@UseThis
	public Long getSomeValue() {
		return 40L;
	}
}