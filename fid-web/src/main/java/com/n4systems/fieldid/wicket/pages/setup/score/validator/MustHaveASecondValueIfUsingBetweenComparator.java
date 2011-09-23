package com.n4systems.fieldid.wicket.pages.setup.score.validator;

import com.n4systems.model.ScoreComparator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

public class MustHaveASecondValueIfUsingBetweenComparator implements INullAcceptingValidator<Double> {

    private IModel<ScoreComparator> comparatorModel;

    public MustHaveASecondValueIfUsingBetweenComparator(IModel<ScoreComparator> comparatorModel) {
        this.comparatorModel = comparatorModel;
    }

    @Override
    public void validate(IValidatable<Double> valueField) {
        if (comparatorModel.getObject() == ScoreComparator.BETWEEN && valueField.getValue() == null) {
            ValidationError error = new ValidationError();
            error.addMessageKey("NeedsValue");
            valueField.error(error);
        }
    }

}
