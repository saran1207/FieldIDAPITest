package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "datefield_criteria")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DateFieldCriteria extends Criteria {
	
	@Column(nullable=false)
	private boolean includeTime = false;

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.DATE_FIELD;
    }

	public boolean isIncludeTime() {
		return includeTime;
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}

}
