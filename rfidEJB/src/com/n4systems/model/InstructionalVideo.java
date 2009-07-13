package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.AbstractEntity;

@Entity
@Table( name="instructionalvideos" )
public class InstructionalVideo extends AbstractEntity implements NamedEntity {

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
