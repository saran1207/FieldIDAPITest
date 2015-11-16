package com.n4systems.model.criteriarules;

import com.n4systems.model.Button;

import javax.persistence.*;

/**
 * This Entity represents a Criteria Logic Rule for a OneClickCriteria object.  It is mapped to the button from the
 * associated button group for which we want to enforce an action.
 *
 * Created by Jordan Heath on 2015-11-16.
 */
@Entity
@Table(name = "one_click_rules")
@PrimaryKeyJoinColumn(name = "id")
public class OneClickCriteriaRule extends CriteriaRule {

    @ManyToOne
    @JoinColumn(name = "button_id")
    private Button button;

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
