package com.n4systems.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "unitofmeasure_criteria")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnitOfMeasureCriteria extends Criteria {

    @ManyToOne(optional = false)
    @JoinColumn(name = "primary_unit_id")
    private UnitOfMeasure primaryUnit;

    @ManyToOne(optional = true)
    @JoinColumn(name = "secondary_unit_id")
    private UnitOfMeasure secondaryUnit;

    public UnitOfMeasure getPrimaryUnit() {
        return primaryUnit;
    }

    public void setPrimaryUnit(UnitOfMeasure primaryUnit) {
        this.primaryUnit = primaryUnit;
    }

    public UnitOfMeasure getSecondaryUnit() {
        return secondaryUnit;
    }

    public void setSecondaryUnit(UnitOfMeasure secondaryUnit) {
        this.secondaryUnit = secondaryUnit;
    }

    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.UNIT_OF_MEASURE;
    }
}
