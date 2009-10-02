package com.n4systems.testutils;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.BaseEntity;
import com.n4systems.persistence.loaders.NonSecureIdLoader;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;

public class TestDoubleNonSecuredLoaderFactory extends NonSecureLoaderFactory {

	@SuppressWarnings("unchecked")
	Map<Class,NonSecureIdLoader> nonSecureIdLoaders = new HashMap<Class, NonSecureIdLoader>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> NonSecureIdLoader<T> createNonSecureIdLoader(Class<T> clazz) {
		return (NonSecureIdLoader<T>)nonSecureIdLoaders.get(clazz);		
	}
	
	public <T extends BaseEntity> void add(Class<T> clazz, NonSecureIdLoader<T> loader) {
		nonSecureIdLoaders.put(clazz, loader);
	}

	
	
}
