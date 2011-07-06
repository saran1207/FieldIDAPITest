package com.n4systems.fieldid.wicket.model;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Event;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.SecurityLevel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class NetworkEntityModel<T extends BaseEntity & NetworkEntity<T>> extends FieldIDSpringModel<T> {

    @SpringBean
    private PersistenceManager persistenceManager;

    private Class<T> clazz;
    private Long id;

    public NetworkEntityModel(Class<T> clazz, T object) {
        super(object);
        this.clazz = clazz;
        this.id = object.getId();
    }

    public NetworkEntityModel(Class<T> clazz, Long id) {
        this.clazz = clazz;
        this.id = id;
    }

    @Override
    protected T load() {
        // TODO: Fix this to correctly post fetch what we need...
        return persistenceManager.findAndEnhance(clazz, id, FieldIDSession.get().getSessionUser().getSecurityFilter(), Event.ALL_FIELD_PATHS);
    }

}
