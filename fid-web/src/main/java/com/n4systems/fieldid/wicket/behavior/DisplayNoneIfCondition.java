package com.n4systems.fieldid.wicket.behavior;

import com.n4systems.fieldid.utils.Predicate;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

// When a component is invisible according to wicket (isVisible is false),
// behaviors targeting it do not work.
// This behavior has the same basic effect as setting invisible, except it just uses display: none
// So any behaviors targeting items underneath the hidden container still work
public class DisplayNoneIfCondition extends AttributeAppender {

    private Predicate predicate;

    public DisplayNoneIfCondition(Predicate predicate) {
        super("style", new Model<String>("display:none;"), " ");
        this.predicate = predicate;
    }

    @Override
    public boolean isEnabled(Component component) {
        return predicate.evaluate();
    }

}
