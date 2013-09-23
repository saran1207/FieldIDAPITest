package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.user.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Locale;

public class EntityModel<T extends EntityWithTenant> extends FieldIDSpringModel<T> {

    @SpringBean
    private PersistenceService persistenceService;

    private Class<T> clazz;
    private Long id;
    boolean withLocalization = false;

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
        Locale previousLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        try {
            if (withLocalization) {
                Locale language = getCurrentUser().getLanguage();
                ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(language);
            }
            return persistenceService.find(clazz, id);
        } finally {
            if (withLocalization) {
                ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(previousLanguage);
            }
        }
    }

    public EntityModel<T> withLocalization(boolean localization) {
        withLocalization = localization;
        return this;
    }

    public User getCurrentUser() {
        return persistenceService.find(User.class, FieldIDSession.get().getSessionUser().getUniqueID());
    }

}
