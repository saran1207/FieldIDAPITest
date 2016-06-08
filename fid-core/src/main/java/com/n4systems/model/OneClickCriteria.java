package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "oneclick_criteria")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OneClickCriteria extends Criteria {

    private boolean principal;

	@ManyToOne(fetch= FetchType.EAGER, cascade={CascadeType.REFRESH}, optional=false)
    @JoinColumn(name="button_group_id")
	private ButtonGroup buttonGroup;

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.ONE_CLICK;
    }


}
