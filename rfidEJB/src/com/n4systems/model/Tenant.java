package com.n4systems.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.util.HashCode;


@Entity
@Table(name="tenants")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Tenant extends BaseEntity implements Listable<Long>, NamedEntity, Saveable, Comparable<Tenant> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, length=255)
	public static SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("id", null, null, null);
	}
	
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
		return (getId().equals(tenant.getId()) && getName().equals(tenant.getName()));
	}

	@Override
	public int hashCode() {
		return HashCode.newHash().add(getId()).add(getName()).toHash();
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", name, id);
	}

	public int compareTo(Tenant other) {
		return name.compareToIgnoreCase(other.getName());
	}
	
}
