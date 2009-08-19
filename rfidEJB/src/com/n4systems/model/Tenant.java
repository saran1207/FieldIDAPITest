package com.n4systems.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.util.HashCode;


@Entity
@Table(name="tenants")
public final class Tenant extends BaseEntity implements Listable<Long>, NamedEntity, Saveable, Comparable<Tenant> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String name;
	
	public Tenant() {}
	
	public Tenant(Long id, String name) {
		super(id);
		setName(name);
	}
	
	public String getDisplayName() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = (name != null) ? name.toLowerCase() : null;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Tenant) ? equals((Tenant)obj) : false;
	}

	public boolean equals(Tenant tenant) {
		return (id.equals(tenant.id) && name.equals(tenant.name));
	}

	@Override
	public int hashCode() {
		return HashCode.newHash().add(id).add(name).toHash();
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", name, id);
	}

	public int compareTo(Tenant other) {
		return name.compareToIgnoreCase(other.getName());
	}
}
