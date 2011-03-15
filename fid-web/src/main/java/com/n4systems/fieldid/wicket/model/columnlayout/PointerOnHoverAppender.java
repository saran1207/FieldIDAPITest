package com.n4systems.fieldid.wicket.model.columnlayout;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

public class PointerOnHoverAppender extends AttributeAppender {

    public PointerOnHoverAppender() {
        super("style", true, new Model<String>("cursor:pointer"), " ");
    }

}
