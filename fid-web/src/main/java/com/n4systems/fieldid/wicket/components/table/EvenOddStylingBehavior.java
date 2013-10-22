package com.n4systems.fieldid.wicket.components.table;

import org.apache.wicket.behavior.AttributeAppender;

public class EvenOddStylingBehavior extends AttributeAppender {

    public EvenOddStylingBehavior(int index) {
        super("class", index % 2 == 0 ? "row-even" : "row-odd");
    }

}
