package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.utils.Predicate;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

public class AppendToClassIfCondition extends AttributeAppender {

    public AppendToClassIfCondition(final String classToAppend, final Predicate predicate) {
        super("class", new Model<String>() {
            @Override
            public String getObject() {
                if (predicate.evaluate()) {
                    return classToAppend;
                }
                return "";
            }
        }, " ");
    }
}
