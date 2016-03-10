package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import org.springframework.stereotype.Component;

/**
 * Criteria Rule for Number Field Criteria.
 *
 * Created by Jordan Heath on 2016-03-10.
 */
@Component
public class ApiNumberFieldCriteriaRule extends ApiCriteriaRule {
    private Double value1;
    private Double value2;
    private String comparisonType;

    public Double getValue1() {
        return value1;
    }

    public ApiNumberFieldCriteriaRule setValue1(Double value1) {
        this.value1 = value1;
        return this;
    }

    public Double getValue2() {
        return value2;
    }

    public ApiNumberFieldCriteriaRule setValue2(Double value2) {
        this.value2 = value2;
        return this;
    }

    public String getComparisonType() {
        return comparisonType;
    }

    /**
     * LE, GE, BT or EQ only.
     *
     * @param comparisonType A String value matching one of those mentioned above.
     * @return this
     */
    public ApiNumberFieldCriteriaRule setComparisonType(String comparisonType) {
        this.comparisonType = comparisonType;
        return this;
    }
}
