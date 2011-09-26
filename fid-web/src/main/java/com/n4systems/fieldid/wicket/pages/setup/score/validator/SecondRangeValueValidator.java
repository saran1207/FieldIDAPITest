package com.n4systems.fieldid.wicket.pages.setup.score.validator;

import com.n4systems.model.ScoreComparator;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;

public class SecondRangeValueValidator implements INullAcceptingValidator<Double> {

    private IModel<ScoreComparator> comparatorModel;
    private TextField<Double> value1TextField;

    public SecondRangeValueValidator(IModel<ScoreComparator> comparatorModel, TextField<Double> value1TextField) {
        this.comparatorModel = comparatorModel;
        this.value1TextField = value1TextField;
    }

    @Override
    public void validate(IValidatable<Double> value2Field) {
        if (comparatorModel.getObject() == ScoreComparator.BETWEEN) {

            if (value2Field.getValue() == null) {
                ValidationError error = new ValidationError();
                error.addMessageKey("NeedsValue");
                value2Field.error(error);
            }

            IValidatable<Double> value1Field = value1TextField.newValidatable();

            if (value1Field.getValue() != null && value2Field.getValue() != null && value2Field.getValue() <= value1Field.getValue()) {
                ValidationError error = new ValidationError();
                error.addMessageKey("MustBeGreater");
                value2Field.error(error);
            }
        }
    }

}
