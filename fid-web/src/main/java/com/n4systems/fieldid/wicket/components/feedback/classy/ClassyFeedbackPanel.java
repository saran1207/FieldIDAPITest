package com.n4systems.fieldid.wicket.components.feedback.classy;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import java.io.Serializable;

public class ClassyFeedbackPanel extends FeedbackPanel {

    public ClassyFeedbackPanel(String id) {
        super(id);
    }

    public ClassyFeedbackPanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    @Override
    protected Component newMessageDisplayComponent(String id, FeedbackMessage message) {
        Serializable containedMsg = message.getMessage();
        if (containedMsg instanceof ClassyFeedbackMessage) {
            ClassyFeedbackMessage<Component> classyMessage = (ClassyFeedbackMessage) containedMsg;
            return classyMessage.createComponent(id);
        } else {
            return super.newMessageDisplayComponent(id, message);
        }
    }
}
