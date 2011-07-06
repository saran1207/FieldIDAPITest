package com.n4systems.fieldid.wicket.components.feedback;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class TopFeedbackPanel extends Panel {

    public TopFeedbackPanel(String id) {
        super(id);

        final ErrorLevelFeedbackMessageFilter filter = new ErrorLevelFeedbackMessageFilter(FeedbackMessage.INFO) {
            @Override
            public boolean accept(FeedbackMessage message) {
                return message.getLevel() == FeedbackMessage.INFO;
            }
        };

        WebMarkupContainer feedbackPanelContainer = new WebMarkupContainer("feedbackPanelContainer") {
            @Override
            public boolean isVisible() {
                return FieldIDSession.get().getFeedbackMessages().hasMessage(filter);
            }
        };
        feedbackPanelContainer.add(new FeedbackPanel("feedbackPanel", filter) {
            @Override
            protected String getCSSClass(FeedbackMessage message) {
                if (message.getLevel() == FeedbackMessage.INFO) {
                    return "actionMessage";
                }
                return super.getCSSClass(message);
            }
        });
        add(feedbackPanelContainer);
    }

}
