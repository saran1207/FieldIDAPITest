package com.n4systems.model.api;

import java.io.Serializable;

public interface Listable<T> extends Serializable {
	public T getId();
	public String getDisplayName();
}
