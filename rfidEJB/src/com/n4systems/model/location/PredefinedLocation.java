package com.n4systems.model.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

@SuppressWarnings("serial")
@Entity(name = "predefinedlocations")
public class PredefinedLocation extends EntityWithTenant implements NamedEntity, TreeNode {

	@Column(nullable = false, length = 255)
	private String name;

	@ManyToOne
	private PredefinedLocation parent;

	public PredefinedLocation() {
		super();
	}

	public PredefinedLocation(Tenant tenant, PredefinedLocation parent) {
		super(tenant);
		this.parent = parent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public PredefinedLocation getParent() {
		return parent;
	}
	
	public void setParent(PredefinedLocation parentNode) {
		parent=parentNode;
	}

	public boolean hasParent() {
		return parent != null;
	}


	public int levelNumber() {
		if (hasParent()) {
			return parent.levelNumber() + 1;
		}
		return 1;
	}
}
