package com.n4systems.model.criteriarules;

import com.n4systems.model.Criteria;
import com.n4systems.model.api.DisplayEnum;

import javax.persistence.*;

/**
 * This Entity represents a Criteria Logic Rule for a NumberFieldCriteria object.  It contains two numbers, only one of
 * which is guaranteed to contain a value, and a field indicating the kind of comparison (GE, LE, EQ, etc.)
 *
 * Created by Jordan Heath on 2015-11-16.
 */
@Entity
@Table(name = "number_field_rules")
@PrimaryKeyJoinColumn(name = "id")
public class NumberFieldCriteriaRule extends CriteriaRule {

    @Column(name = "number_1")
    private Double value1;

    @Column(name = "number_2")
    private Double value2;

    @Column(name = "comparison_type")
    @Enumerated(EnumType.STRING)
    private ComparisonType comparisonType = ComparisonType.LE;

    public NumberFieldCriteriaRule() {
    }

    public NumberFieldCriteriaRule(Criteria criteria) {
        super(criteria);
    }

    public NumberFieldCriteriaRule(Criteria newCriteria, NumberFieldCriteriaRule rule) {
        super(newCriteria, rule);
        this.value1 = rule.getValue1();
        this.value2 = rule.getValue2();
        this.comparisonType = rule.getComparisonType();
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }

    public ComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    //NOTE: This may be wrong and we may need to remove the label value entirely... I think it's fine, though.
    public enum ComparisonType implements DisplayEnum {
        LE("<="), GE(">="), EQ("=="), BT("Between");

        private String label;

        ComparisonType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }
}
