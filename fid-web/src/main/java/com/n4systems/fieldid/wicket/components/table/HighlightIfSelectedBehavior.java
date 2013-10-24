package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.Model;

public class HighlightIfSelectedBehavior extends AttributeAppender {

    private MultiIdSelection selection;
    private int index;

    public HighlightIfSelectedBehavior(MultiIdSelection selection, int index) {
        super("class", new Model<String>("multiSelected"), " ");
        this.selection = selection;
        this.index = index;
    }

    @Override
    public boolean isEnabled(Component component) {
        return selection.containsIndex(index);
    }

}
