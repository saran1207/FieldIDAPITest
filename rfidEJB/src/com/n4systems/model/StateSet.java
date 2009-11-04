package com.n4systems.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "statesets")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class StateSet extends EntityWithTenant implements NamedEntity, Listable<Long>, Saveable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private boolean retired = false;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name="orderIdx")
	private List<State> states = new ArrayList<State>();

	public StateSet() {}
	
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
		name = (name != null) ? name.trim() : null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
	public String getDisplayName() {
		return getName();
	}
	
	public int countOfAvailableStates() {
		return getAvailableStates().size();
	}
	
	public List<State> getAvailableStates() {
		List<State> availableState = new ArrayList<State>();
		for( State state : states ) {
			if( !state.isRetired() ) {
				availableState.add( state );
			}
		}
		return availableState;
	}
	
	
	public boolean equals(Object obj) {
		if (obj == null || this.getId() == null || !(obj instanceof StateSet)) {
			return super.equals(obj);
		} else {
			return equals((StateSet) obj);
		} 
	}
	
	
	public boolean equals(StateSet stateSet) {
		return (getId().equals(stateSet.getId())) ? true : getAvailableStates().equals(stateSet.getAvailableStates());
	}
	
}

