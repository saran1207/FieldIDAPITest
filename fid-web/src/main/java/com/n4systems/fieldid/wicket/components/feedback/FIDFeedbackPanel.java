package com.n4systems.fieldid.wicket.components.feedback;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class FIDFeedbackPanel extends Panel {

    public FIDFeedbackPanel(String id) {
        super(id);

        WebMarkupContainer feedbackPanelContainer = new WebMarkupContainer("feedbackPanelContainer") {
            @Override
            public boolean isVisible() {
                return FieldIDSession.get().getFeedbackMessages().hasMessage(new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR));
            }
        };
        feedbackPanelContainer.add(new FeedbackPanel("feedbackPanel") {
            @Override
            protected String getCSSClass(FeedbackMessage message) {
                if (message.getLevel() == FeedbackMessage.ERROR) {
                    return "errorMessage";
                }
                return super.getCSSClass(message);
            }
        });
        add(feedbackPanelContainer);
    }

}
