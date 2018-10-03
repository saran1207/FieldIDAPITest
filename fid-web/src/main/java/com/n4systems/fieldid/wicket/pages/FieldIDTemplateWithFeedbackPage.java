package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by agrabovskis on 2018-09-17.
 */
public class FieldIDTemplateWithFeedbackPage extends FieldIDTemplatePage {

    private FeedbackPanel feedbackPanel;

    public FieldIDTemplateWithFeedbackPage(final PageParameters parameters) {
        super(parameters);
        addFeedbackPanel();
    }

    private void addFeedbackPanel() {
         /* Existing top feedback panel is in the correct place for our messages but doesn't
            get recognized as a feedback panel for our messages. */
        remove(getTopFeedbackPanel());
        feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px"), " "));
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
    }

    public FeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }
}
