package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "unitofmeasure_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "EventCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnitOfMeasureCriteriaResult extends CriteriaResult {

    @Column(name="primary_value", length = 255)
    private String primaryValue;

    @Column(name="secondary_value", length = 255)
    private String secondaryValue;

    public String getPrimaryValue() {
        return primaryValue;
    }

    public void setPrimaryValue(String primaryValue) {
        this.primaryValue = primaryValue;
    }

    public String getSecondaryValue() {
        return secondaryValue;
    }

    public void setSecondaryValue(String secondaryValue) {
        this.secondaryValue = secondaryValue;
    }

	@Override
	public String getResultString() {
		StringBuffer buff = new StringBuffer();
		if (primaryValue!=null) {
			buff.append(primaryValue);
		}
		if (secondaryValue!=null) {
			buff.append(" : ");
			buff.append(secondaryValue);
		}
		return buff.toString();
	}
}
