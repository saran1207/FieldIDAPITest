package com.n4systems.model;

import com.n4systems.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "inspectionssub")
@PrimaryKeyJoinColumn(name="inspection_id")
public class SubInspection extends AbstractInspection {
	private static final long serialVersionUID = 1L;
	
	@Column( length = 255 )
	private String name;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public SubInspection() {}

	public SubInspection(TenantOrganization tenant) {
		super(tenant);
	}

	@Override
    public String toString() {
	    return "SubInspection: " + getName() + StringUtils.indent("\n" + super.toString(), 1);
    }

	
}
