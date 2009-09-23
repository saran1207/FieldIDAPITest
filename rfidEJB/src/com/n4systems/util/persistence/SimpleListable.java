package com.n4systems.util.persistence;

import com.n4systems.model.api.Listable;

/**
 * Provides a basic implementation of the Listable interface.
 * 
 * @param <T>	The id class
 */
public class SimpleListable<T> implements Listable<T> {
	private static final long serialVersionUID = 1L;
	private T id;
	private String displayName;

	public SimpleListable() {}
	
	public SimpleListable(Listable<T> listableToCopy) {
		this(listableToCopy.getId(), listableToCopy.getDisplayName());
	}
	
	public SimpleListable(T id, String displayName) {
	    this.id = id;
	    this.displayName = displayName;
    }

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Listable) {
			return (id.equals(getId())); 
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	
}
