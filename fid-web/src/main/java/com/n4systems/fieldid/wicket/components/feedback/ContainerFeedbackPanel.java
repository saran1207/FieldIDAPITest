package com.n4systems.fieldid.wicket.components.feedback;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class ContainerFeedbackPanel extends FeedbackPanel {

    public ContainerFeedbackPanel(String id, WebMarkupContainer container) {
        super(id, new ContainerFeedbackFilter(container));
    }

}
