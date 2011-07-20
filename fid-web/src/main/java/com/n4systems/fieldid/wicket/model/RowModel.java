package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.util.views.RowView;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RowModel implements IModel<RowView> {

    @SpringBean
    private PersistenceService persistenceService;

    private RowView rowView;
    private Class entityClass;

    public RowModel(RowView rowView, Class<?> entityClass) {
        // Needed to retrieve the persistence manager
        InjectorHolder.getInjector().inject(this);
        this.rowView = rowView;
        this.entityClass = entityClass;
    }

    @Override
    public RowView getObject() {
        if (rowView.getEntity() == null) {
            rowView.setEntity(persistenceService.findAndEnhance(entityClass, rowView.getId(), FieldIDSession.get().getSessionUser().getSecurityFilter()));
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
