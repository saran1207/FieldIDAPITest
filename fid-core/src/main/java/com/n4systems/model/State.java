package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.api.Retirable;
import com.n4systems.model.parents.EntityWithTenant;

@Entity
@Table(name = "states")
public class State extends EntityWithTenant implements Listable<Long>, Retirable, NamedEntity {
	private static final long serialVersionUID = 1L;
		
	@Column(nullable=false)
	private String displayText;

	@Column(name="event_result", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventResult eventResult;
	
	@Column(nullable=false)
	private String buttonName;

	@Column(nullable=false)
	private boolean retired = false;
	
	public State() {}

	public State(String displayText, EventResult eventResult, String buttonName) {
		this.displayText = displayText;
		this.eventResult = eventResult;
		this.buttonName = buttonName;
	}
	
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
		displayText = (displayText != null) ? displayText.trim() : null;
	}
	
	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public EventResult getEventResult() {
		return eventResult;
	}

	public void setEventResult(EventResult eventResult) {
		this.eventResult = eventResult;
	}
	
	public String getStatusName(){
		return (eventResult != null) ? eventResult.name() : null;
	}
	
	public void setStatusName(String statusName) {
		this.eventResult = EventResult.valueOf(statusName);
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}
	
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}

	public String getDisplayName() {
		return getDisplayText();
	}

	public String getName() {
		return getDisplayText();
	}

	public void setName( String name ) {
		setDisplayText( name );
	}

	@Override
    public String toString() {
	    return getDisplayText() + "/" + getEventResult() + " (" + getId() + ")";
    }
	
	public boolean equals(Object obj) {
		if (obj == null || this.getId() == null || !(obj instanceof State)) {
			return super.equals(obj);
		} else {
			return equals((State) obj);
		} 
	}
	
	public boolean equals(State state) {
		if (getId().equals(state.getId())) {
			return true;
		} 
		
		return (getDisplayText().equalsIgnoreCase(state.getDisplayText()) && 
				getEventResult().equals(state.getEventResult()) &&
				getButtonName().equals(state.getButtonName()));
	}
}
