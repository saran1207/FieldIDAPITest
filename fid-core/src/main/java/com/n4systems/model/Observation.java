package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.n4systems.model.parents.EntityWithTenant;

@SuppressWarnings("serial")
@Entity
@Table(name = "observations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
abstract public class Observation extends EntityWithTenant {

	public enum Type { 
		RECOMMENDATION("Recommendations"), DEFICIENCY("Deficiencies");
		
		private String displayTitle;
		
		private Type(String displayTitle) {
			this.displayTitle = displayTitle;
		}
		
		public String getDisplayTitle() {
			return displayTitle;
		}
	};
	
	public enum State { 
		OUTSTANDING("Outstanding"), REPAIREDONSITE("Repaired On Site"), REPAIRED("Repaired"), COMMENT("Comment");
		
		private String displayName;
		
		private State(String displayName) {
			this.displayName = displayName;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	};
	
	@Column(insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@Column(nullable=false, length=2048)
	private String text;

	@Column(nullable=false, length=31)
	@Enumerated(EnumType.STRING)
	private State state;
	
	public Observation(Type type) {
		this( type, null );
	}

	public Observation(Type type, Tenant tenant) {
		super( tenant );
		this.type = type;
	}

	@Override
    public String toString() {
	    return getType() + "(" + getId() + "): " + getText() + " = " + getState();
    }

	public Type getType() {
		return type;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setStateString(String state) {
		setState(State.valueOf(state));
	}
	
	public String getStateString() {
		return getState().name();
	}
}
