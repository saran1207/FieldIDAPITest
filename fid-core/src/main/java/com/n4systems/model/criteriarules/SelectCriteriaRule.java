package com.n4systems.model.criteriarules;

import com.n4systems.model.Criteria;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * This Entity represents a Criteria Logic Rule for a SelectCriteria object.  It contains the String value we should
 * expect to see in the Criteria result when this rule is enforced.
 *
 * NOTE: Because there's no simple way to bind these rules to a particular Select value, we just bind it to the String
 *       value that the rule is to be triggered on.  The onus is on the developer to ensure that - if these values are
 *       to change - the rule changes as appropriate, too.  Since the only place you should see this value change is
 *       in the event form editor, this should be fairly easy.
 *
 * Created by Jordan Heath on 2015-11-16.
 */
@Entity
@Table(name="select_rules")
@PrimaryKeyJoinColumn(name = "id")
public class SelectCriteriaRule extends CriteriaRule {

    @Column(name = "select_value")
    private String selectValue;

    public SelectCriteriaRule() {
    }

    public SelectCriteriaRule(Criteria criteria, String selectValue) {
        super(criteria);
        this.selectValue = selectValue;
    }

    public SelectCriteriaRule(Criteria newCriteria, SelectCriteriaRule rule) {
        super(newCriteria, rule);
        this.selectValue = rule.getSelectValue();
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(String selectValue) {
        this.selectValue = selectValue;
    }
}
