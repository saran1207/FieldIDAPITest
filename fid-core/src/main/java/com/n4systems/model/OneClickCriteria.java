package com.n4systems.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "oneclick_criteria")
@PrimaryKeyJoinColumn(name="id")
public class OneClickCriteria extends Criteria {

	@ManyToOne(fetch= FetchType.EAGER, cascade={CascadeType.REFRESH}, optional=false)
	private StateSet states;

	public StateSet getStates() {
		return states;
	}

	public void setStates(StateSet states) {
		this.states = states;
	}

    @Override
    public boolean isOneClickCriteria() {
        return true;
    }

    @Override
    public String getTypeDescription() {
        return "One-Click";
    }
}
