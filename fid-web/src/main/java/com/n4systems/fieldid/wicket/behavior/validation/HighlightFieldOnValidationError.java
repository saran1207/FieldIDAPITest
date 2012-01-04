package com.n4systems.fieldid.wicket.behavior.validation;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.model.Model;

public class HighlightFieldOnValidationError extends AttributeAppender {

    public HighlightFieldOnValidationError() {
        super("class", new Model<String>("inputError"), " ");
    }

    @Override
    public boolean isEnabled(Component component) {
        return FieldIDSession.get().getFeedbackMessages().hasMessageFor(component, FeedbackMessage.ERROR);
    }

}
