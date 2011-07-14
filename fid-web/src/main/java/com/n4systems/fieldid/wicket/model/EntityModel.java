package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EntityModel<T extends EntityWithTenant> extends FieldIDSpringModel<T> {

    @SpringBean
    private PersistenceService persistenceService;

    private Class<T> clazz;
    private Long id;

    public EntityModel(Class<T> clazz, T object) {
        super(object);
        this.clazz = clazz;
        this.id = object.getId();
    }

    public EntityModel(Class<T> clazz, Long id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    protected T load() {
        return persistenceService.find(clazz, id);
    }

}
