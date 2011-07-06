package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.model.BaseEntity;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class HighlightIfSelectedBehavior<T> extends AttributeAppender {

    private IModel<T> rowModel;
    private MultiIdSelection selection;

    public HighlightIfSelectedBehavior(IModel<T> rowModel, MultiIdSelection selection) {
        super("class", true, new Model<String>("multiSelected"), " ");
        this.rowModel = rowModel;
        this.selection = selection;
    }

    @Override
    public boolean isEnabled(Component component) {
        T object = rowModel.getObject();
        if (!(object instanceof BaseEntity)) {
            return false;
        }
        Long id = ((BaseEntity)object).getId();
        return selection.containsId(id);
    }
}
