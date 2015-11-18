package com.n4systems.model.criteriarules;

import com.n4systems.model.Criteria;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

/**
 * This is the base entity for Criteria Logic Rules.  On top of this abstract class, we have several fully built
 * classes representing the different types of Criteria Logic Rules which can be created.  All of the common fields
 * between these types live here.
 *
 * We join to the Criteria from this class, so you should probably make sure you cast to the correct type of Criteria
 * when reading the values... I don't think it'll case on its own.
 *
 * Created by Jordan Heath on 2015-11-16.
 */
@Entity
@Table(name="criteria_rules")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CriteriaRule extends EntityWithTenant {

    @Column(name="action")
    @Enumerated(EnumType.STRING)
    private ActionType action;

    @ManyToOne
    @JoinColumn(name = "criteria_id")
    private Criteria criteria;

    public CriteriaRule() {
    }

    public CriteriaRule(Criteria criteria) {
        super(criteria.getTenant());
        this.criteria = criteria;
    }

    public CriteriaRule(Criteria newCriteria, CriteriaRule rule) {
        super(rule.getTenant());
        this.criteria = newCriteria;
        this.action = rule.getAction();
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public enum ActionType {
        IMAGE("label.take_photo"), ACTION("label.assign_action");

        private String label;

        ActionType(String label) {
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
