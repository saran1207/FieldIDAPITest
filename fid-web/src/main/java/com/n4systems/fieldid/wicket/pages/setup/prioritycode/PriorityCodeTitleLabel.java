package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;


public class PriorityCodeTitleLabel extends Panel {
    public PriorityCodeTitleLabel(String id) {
        super(id);
        ContextImage tooltip;

        add(tooltip = new ContextImage("tooltip", "images/tooltip-icon.png"));
        tooltip.add(new AttributeAppender("title", new FIDLabelModel("msg.priority_codes")));
    }
}
