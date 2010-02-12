package com.n4systems.model.safetynetwork;

import com.n4systems.persistence.loaders.Loader;


public interface IdLoader<T extends Loader<?>> {
	public T setId(Long id);
}