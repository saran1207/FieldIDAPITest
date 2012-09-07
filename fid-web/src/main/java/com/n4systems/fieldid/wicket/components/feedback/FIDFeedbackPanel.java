package com.n4systems.fieldid.wicket.components.feedback;

import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.n4systems.fieldid.wicket.FieldIDSession;

public class FIDFeedbackPanel extends Panel {

    public FIDFeedbackPanel(String id) {
        super(id);
        setOutputMarkupId(true);
        final IFeedbackMessageFilter filter = new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR);
        WebMarkupContainer feedbackPanelContainer = new WebMarkupContainer("feedbackPanelContainer") {
            @Override
            public boolean isVisible() {
                return FieldIDSession.get().getFeedbackMessages().hasMessage(filter);
            }
        };
        feedbackPanelContainer.add(new FeedbackPanel("feedbackPanel", filter) {
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/layout/feedback_errors.css");
    }
}
