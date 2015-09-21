package com.n4systems.model.builders;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.UnitOfMeasureCriteria;

public class UnitOfMeasureCriteriaBuilder extends CriteriaBuilder<UnitOfMeasureCriteria> {

    private final UnitOfMeasure primaryUnit;
    private final UnitOfMeasure secondaryUnit;

    public UnitOfMeasureCriteriaBuilder(String text, boolean retired, boolean required, UnitOfMeasure primaryUnit, UnitOfMeasure secondaryUnit) {
        super(text, retired, required);
        this.primaryUnit = primaryUnit;
        this.secondaryUnit = secondaryUnit;
    }

    public UnitOfMeasureCriteriaBuilder(String text) {
    	this(text, false, false, null, null);
	}

	public static UnitOfMeasureCriteriaBuilder aUnitOfMeasureCriteria() {
        return new UnitOfMeasureCriteriaBuilder(null, false, false, null, null);
    }

    public UnitOfMeasureCriteriaBuilder primaryUnit(UnitOfMeasure primaryUnit) {
        return makeBuilder(new UnitOfMeasureCriteriaBuilder(text, retired, required, primaryUnit, secondaryUnit));
    }

    public UnitOfMeasureCriteriaBuilder secondaryUnit(UnitOfMeasure secondaryUnit) {
        return makeBuilder(new UnitOfMeasureCriteriaBuilder(text, retired, required, primaryUnit, secondaryUnit));
    }

    public UnitOfMeasureCriteriaBuilder withDisplayText(String text) {
        return makeBuilder(new UnitOfMeasureCriteriaBuilder(text, retired, required, primaryUnit, secondaryUnit));
    }

    @Override
    public UnitOfMeasureCriteria createObject() {
        UnitOfMeasureCriteria criteria = super.assignAbstractFields(new UnitOfMeasureCriteria());
        criteria.setPrimaryUnit(primaryUnit);
        criteria.setSecondaryUnit(secondaryUnit);
        return criteria;
    }

}
