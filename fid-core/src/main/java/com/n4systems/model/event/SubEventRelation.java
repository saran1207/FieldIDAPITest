package com.n4systems.model.event;

import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.*;

@Entity
@Table(name="masterevents_subevents")
public class SubEventRelation {
	
	public static final SecurityDefiner createSecurityDefiner() {
		return new SecurityDefiner("masterEvent.tenant.id", "masterEvent.asset.owner", null, "masterEvent.state");
	}
	
	@SuppressWarnings("unused")
	@Id
	@Column(name="subevents_event_id", nullable=false, insertable=false, updatable=false, unique=true)
	private Long subEventsEventId;
	
	@OneToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="subevents_event_id", nullable=false, insertable=false, updatable=false, unique=true)
	private SubEvent subEvent;

	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="masterevents_event_id", nullable=false, insertable=false, updatable=false)
	private Event masterEvent;
	
	@Column(name="orderidx", nullable=false, insertable=false, updatable=false)
	private Integer orderidx;
	
	public SubEventRelation() {}

	@PrePersist
	@PreUpdate
	@PreRemove
	public void onPersistUpdateRemove() {
		throw new UnsupportedOperationException("This entity is read-only");
	}
	
	public Event getMasterEvent() {
		return masterEvent;
	}

	public void setMasterEvent(Event masterEvent) {
		this.masterEvent = masterEvent;
	}

	public SubEvent getSubEvent() {
		return subEvent;
	}

	public void setSubEvent(SubEvent subEvent) {
		this.subEvent = subEvent;
	}

	public Integer getOrderidx() {
		return orderidx;
	}

	public void setOrderidx(Integer orderidx) {
		this.orderidx = orderidx;
	}
}
