package com.n4systems.util;


public class NonDataSourceBackedConfigContext extends ConfigContext {

	
	public NonDataSourceBackedConfigContext() {
		super();
		markDirty();
	}
	
	protected void reloadConfigurations() {
		configruations.clear();
	}
}
