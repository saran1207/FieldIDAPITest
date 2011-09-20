package com.n4systems.fieldid.wicket.pages.setup.score.validator;

import com.n4systems.model.Score;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

public class MustHaveAValueIfNotNAValidator implements INullAcceptingValidator<Double> {

    private IModel<Score> scoreModel;

    public MustHaveAValueIfNotNAValidator(IModel<Score> scoreModel) {
        this.scoreModel = scoreModel;
    }

    @Override
    public void validate(IValidatable<Double> valueField) {
        if (!scoreModel.getObject().isNa() && valueField.getValue() == null) {
            ValidationError error = new ValidationError();
            error.addMessageKey("NeedsValue");
            valueField.error(error);
        }
    }

}
