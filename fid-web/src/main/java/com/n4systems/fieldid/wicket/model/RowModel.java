package com.n4systems.fieldid.wicket.model;

import com.n4systems.util.views.RowView;
import org.apache.wicket.model.IModel;

public class RowModel implements IModel<RowView> {

    private RowView rowView;

    public RowModel(RowView rowView) {
        this.rowView = rowView;
    }

    @Override
    public RowView getObject() {
        return rowView;
    }

    @Override
    public void setObject(RowView rowView) {
        this.rowView = rowView;
    }

    @Override
    public void detach() {
        rowView.setEntity(null);
        rowView.setValues(null);
    }

}
