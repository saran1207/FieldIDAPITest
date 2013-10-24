package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

public class EvenOddStylingBehavior extends AttributeAppender {

    public EvenOddStylingBehavior(int index) {
        super("class", index % 2 == 0 ? new Model<String>("row-even") : new Model<String>("row-odd"), " ");
    }

}
