package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by agrabovskis on 2018-09-17.
 */
public class FieldIDTemplateWithFeedbackPage extends FieldIDTemplatePage {

    private FeedbackPanel feedbackPanel;

    public FieldIDTemplateWithFeedbackPage() {
        super();
        addFeedbackPanel();
    }

    public FieldIDTemplateWithFeedbackPage(final PageParameters parameters) {
        super(parameters);
        addFeedbackPanel();
    }

    private void addFeedbackPanel() {
         /* Existing top feedback panel is in the correct place for our messages but doesn't
            get recognized as a feedback panel for our messages. */
        remove(getTopFeedbackPanel());
        feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px;"), " "));
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.get("feedbackul").add(new AttributeAppender("style", new Model("list-style-type: none; padding: 0px 0px;"), " "));
        add(feedbackPanel);
    }

    public FeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSS("li .feedbackPanelINFO {padding: 10px 0px 10px 0px;\n" +
                "text-align: center;\n" +
                "border: 1px solid #5fb336;\n" +
                "background-color: #e3f4db;\n" +
                "font-size: 13px;\n" +
                "display: block;\n" +
                "color: #333333;}", null);
        response.renderCSS("li .feedbackPanelERROR {text-align: center: display:block; color: red;}", null);
    }

}
