package com.n4systems.fieldid.wicket.components.feedback;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.classy.ClassyFeedbackPanel;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class TopFeedbackPanel extends Panel {

    public TopFeedbackPanel(String id) {
        super(id);
        setOutputMarkupId(true);

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
        feedbackPanelContainer.add(new ClassyFeedbackPanel("feedbackPanel", filter) {
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        //Old CSS file - remove when site is completely moved over to framework styles.
        response.renderCSSReference("style/newCss/layout/feedback_errors.css"); 
    }

}
