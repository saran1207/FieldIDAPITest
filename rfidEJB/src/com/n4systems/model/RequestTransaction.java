package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "requesttransactions")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RequestTransaction extends EntityWithTenant implements NamedEntity {

	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String name;
	
	private String type;

	

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}



}
