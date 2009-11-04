package com.n4systems.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.parents.EntityWithOwner;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "inspectionbooks")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class InspectionBook extends EntityWithOwner implements NamedEntity, Listable<Long>, Comparable<InspectionBook>, SecurityEnhanced<InspectionBook> {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private boolean open = true;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="book")
	private Set<Inspection> inspections = new TreeSet<Inspection>();
	
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

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public boolean isOpen() {
		return open;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public Set<Inspection> getInspections() {
		return inspections;
	}
	
	public void setInspections(Set<Inspection> inspections) {
		this.inspections = inspections;
	}

	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public String getDisplayName() {
		String ownerName = (getOwner() != null) ? " (" + getOwner().getName() + ")" : "";
		return getName() + ownerName;
	}
	
	@NetworkAccessLevel(SecurityLevel.LOCAL)
	public Long getLegacyId() {
		return legacyId;
	}

	public void setLegacyId(Long legacyId) {
		this.legacyId = legacyId;
	}

	public int compareTo(InspectionBook o) {
		if (o == null || getName() == null) return 0;
		
		return getName().compareToIgnoreCase(o.getName());
	}

	public InspectionBook enhance(SecurityLevel level) {
		return EntitySecurityEnhancer.enhanceEntity(this, level);
	}
	
}
