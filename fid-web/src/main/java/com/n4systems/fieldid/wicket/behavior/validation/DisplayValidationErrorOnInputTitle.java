package com.n4systems.fieldid.wicket.behavior.validation;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.ValidationErrorFeedback;
import org.apache.wicket.model.IModel;

public class DisplayValidationErrorOnInputTitle extends AttributeModifier {

    public DisplayValidationErrorOnInputTitle(Component component) {
        super("title", true, createValidationErrorMessageModel(component));
    }

    @Override
    public boolean isEnabled(Component component) {
        return FieldIDSession.get().getFeedbackMessages().hasMessageFor(component, FeedbackMessage.ERROR);
    }

    static IModel<String> createValidationErrorMessageModel(final Component component) {
        return new IModel<String>() {
            @Override
            public String getObject() {
                FeedbackMessage feedbackMessage = FieldIDSession.get().getFeedbackMessages().messageForComponent(component);
                if (feedbackMessage.getMessage() instanceof ValidationErrorFeedback) {
                    return ((ValidationErrorFeedback)feedbackMessage.getMessage()).getMessage();
                }
                return "";
            }

            @Override
            public void setObject(String object) {
            }

            @Override
            public void detach() {
            }
        };
    }

}
