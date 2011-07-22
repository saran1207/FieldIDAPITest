package com.n4systems.fieldid.wicket.components.table;

import com.n4systems.util.selection.MultiIdSelection;
import com.n4systems.util.views.RowView;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class HighlightIfSelectedBehavior extends AttributeAppender {

    private IModel<?> rowModel;
    private MultiIdSelection selection;

    public HighlightIfSelectedBehavior(IModel<?> rowModel, MultiIdSelection selection) {
        super("class", true, new Model<String>("multiSelected"), " ");
        this.rowModel = rowModel;
        this.selection = selection;
    }

    @Override
    public boolean isEnabled(Component component) {
        Object object = rowModel.getObject();
        if (!(object instanceof RowView)) {
            return false;
        }
        Long id = ((RowView)object).getId();
        return selection.containsId(id);
    }

}
