package com.n4systems.fieldid.wicket.model;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.views.RowView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RowModel implements IModel<RowView> {

    @SpringBean
    private PersistenceManager pm;

    private RowView rowView;
    private Class entityClass;

    public RowModel(RowView rowView) {
        this.rowView = rowView;
        this.entityClass = rowView.getEntity().getClass();
    }

    @Override
    public RowView getObject() {
        if (rowView.getEntity() == null) {
            rowView.setEntity(pm.findAndEnhance(entityClass, rowView.getId(), FieldIDSession.get().getSessionUser().getSecurityFilter()));
        }
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
