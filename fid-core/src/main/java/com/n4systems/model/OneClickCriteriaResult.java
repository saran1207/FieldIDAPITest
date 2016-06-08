package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "oneclick_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OneClickCriteriaResult extends CriteriaResult {

	@ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
    @JoinColumn(name="state_id")
    // Migrating this to button_id would be very difficult due to the size of the data.... consider for later.
	private Button button;

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	@Override
	public EventResult getResult() {
		return ((OneClickCriteria)criteria).isPrincipal() ? button.getEventResult() : null;
	}

	@Override
	public String getResultString() {
		return button != null ? button.getDisplayText() : "";
	}

	@Override
	public String toString() {
		return super.toString() + "[button=" + button + "]";
	}
	
}
