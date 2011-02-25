package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "unitofmeasure_criteria")
public class UnitOfMeasureCriteria extends Criteria {

    @ManyToOne(optional = false)
    @JoinColumn(name = "primary_unit_id")
    private UnitOfMeasure primaryUnit;

    @ManyToOne(optional = true)
    @JoinColumn(name = "secondary_unit_id")
    private UnitOfMeasure secondaryUnit;

    @Override
    public boolean isUnitOfMeasureCriteria() {
        return true;
    }

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
