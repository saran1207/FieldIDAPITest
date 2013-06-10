package com.n4systems.model;

import com.n4systems.model.api.CrossTenantEntity;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "unitofmeasures")
public class UnitOfMeasure extends AbstractEntity implements NamedEntity, CrossTenantEntity, Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String type;
	private String shortName;
	private boolean selectable;

	@ManyToOne(optional = true)
	@JoinColumn(name = "child_unitofmeasure_id")
	private UnitOfMeasure child;



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * Selectable determines if this unit of measure can be selected from a drop
	 * down or if it ONLY to be used as a child
	 */
	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public UnitOfMeasure getChild() {
		return child;
	}

	public void setChild(UnitOfMeasure child) {
		this.child = child;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

    @Override
    public String getDisplayName() {
        return getName();
    }
}
