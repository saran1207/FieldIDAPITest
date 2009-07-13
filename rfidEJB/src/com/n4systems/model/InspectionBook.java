package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "inspectionbooks")
public class InspectionBook extends EntityWithTenant implements NamedEntity, Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private boolean open = true;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="book")
	private Set<Inspection> inspections = new TreeSet<Inspection>();
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Customer customer;
	
	private Long legacyId;

	public InspectionBook() {}

	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}
	

	private void trimName() {
		this.name = (name != null) ? name.trim() : null;
	}
	
	@Override
    public String toString() {
	    return name + " (" + getId() + ")";
    }

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public Set<Inspection> getInspections() {
		return inspections;
	}
	
	public void setInspections(Set<Inspection> inspections) {
		this.inspections = inspections;
	}

	public String getDisplayName() {
		String custName = (customer != null) ? " (" + customer.getName() + ")" : "";
		return getName() + custName;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getLegacyId() {
		return legacyId;
	}

	public void setLegacyId(Long legacyId) {
		this.legacyId = legacyId;
	}
}
