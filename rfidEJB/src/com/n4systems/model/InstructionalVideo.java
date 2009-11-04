package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table( name="instructionalvideos" )
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class InstructionalVideo extends AbstractEntity implements NamedEntity, UnsecuredEntity {

	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false, length=2000)
	private String url;


	public String getName() {
		return name;
	}


	public void setName( String name ) {
		this.name = name;
	}


	public String getDisplayName() {
		return getName();
	}


	public String getUrl() {
		return url;
	}


	public void setUrl( String url ) {
		this.url = url;
	}
	

}
