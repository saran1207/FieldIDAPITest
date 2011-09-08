package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "datefield_criteria")
@PrimaryKeyJoinColumn(name="id")
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
