package com.n4systems.util;

import java.io.Serializable;

import com.n4systems.model.api.Listable;

public class ListingPair implements Serializable, Comparable<ListingPair>, Listable<Long> {

	private static final long serialVersionUID = -7840736279333715725L;

	private Long id;

	private String name;

	public <T extends Listable<Long>> ListingPair(T listable) {
		super();
		this.id = listable.getId();
		this.name = listable.getDisplayName();
	}

	public ListingPair() {
	}

	public ListingPair(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

    @Override
    public String getDisplayName() {
        return name;
    }

    public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (obj instanceof ListingPair) {
			ListingPair pair = (ListingPair) obj;
			return (pair.getId().equals(this.getId()));

		}
		return super.equals(obj);
	}

	public int compareTo(ListingPair o) {
		if (o == null) {
			return 1;
		} else {
			return name.compareToIgnoreCase(o.getName());
		}
	}
	
	public int hashCode() {
		return (id == null) ? super.hashCode() : id.hashCode();
	}

	
}
