package com.n4systems.model;

import com.n4systems.model.parents.EntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.UUID;

import javax.persistence.*;
import java.util.UUID;

@SuppressWarnings("serial")
@Entity
@Table(name = "observations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
	
	@Column(nullable=false)
	private String mobileId;
	
	public Observation(Type type) {
		this( type, null );
	}

	public Observation(Type type, Tenant tenant) {
		super( tenant );
		this.type = type;
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
	
	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		ensureMobileId();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		ensureMobileId();
	}	
	
	@Override
    public String toString() {
	    return getType() + "(" + getId() + "): " + getText() + " = " + getState();
    }
	
	private void ensureMobileId() {
		if (mobileId == null) {
			mobileId = UUID.randomUUID().toString();
		}
	}
}
