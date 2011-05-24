package com.n4systems.fieldid.wicket.components.feedback;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class ContainerFeedbackFilter implements IFeedbackMessageFilter {

    private WebMarkupContainer container;

    public ContainerFeedbackFilter(WebMarkupContainer container) {
        this.container = container;
    }

    @Override
    public boolean accept(FeedbackMessage message) {
        return containerIsAncestorOf(message.getReporter());
    }

    protected boolean containerIsAncestorOf(Component component) {
        if (component == null) {
            return false;
        }
        return component.equals(container) || containerIsAncestorOf(component.getParent());
    }

}
