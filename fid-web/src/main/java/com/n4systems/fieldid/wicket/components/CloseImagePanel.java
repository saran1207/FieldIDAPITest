package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class CloseImagePanel extends Panel {

    public CloseImagePanel(String id) {
        super(id);
        add(new ContextImage("closeImage", "images/x.gif"));
        add(new AttributeAppender("class", new Model<String>("closeImage"), " "));
    }

}
