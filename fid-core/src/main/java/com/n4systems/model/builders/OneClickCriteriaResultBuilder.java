package com.n4systems.model.builders;

import com.n4systems.model.Button;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.OneClickCriteriaResult;

public class OneClickCriteriaResultBuilder extends BaseBuilder<OneClickCriteriaResult> {

    private final OneClickCriteria criteria;
    private final Button button;

    public static OneClickCriteriaResultBuilder aCriteriaResult() {
        return new OneClickCriteriaResultBuilder(null, null);
    }

    public OneClickCriteriaResultBuilder(OneClickCriteria criteria, Button button) {
        this.criteria = criteria;
        this.button = button;
    }

    public OneClickCriteriaResultBuilder state(Button button) {
        return makeBuilder(new OneClickCriteriaResultBuilder(criteria, button));
    }

    public OneClickCriteriaResultBuilder criteria(OneClickCriteria criteria) {
        return makeBuilder(new OneClickCriteriaResultBuilder(criteria, button));
    }

    @Override
    public OneClickCriteriaResult createObject() {
        OneClickCriteriaResult criteriaResult = new OneClickCriteriaResult();
        criteriaResult.setCriteria(criteria);
        criteriaResult.setButton(button);

        return criteriaResult;
    }

}
