package com.n4systems.services.localization;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.util.persistence.QueryBuilder;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.util.List;

public class LocalizationService extends FieldIdPersistenceService {

    private String translationFormat = "%s.%s";

    public List<Translation> getTranslations(Long entityId) {
        QueryBuilder<Translation> query = createTenantSecurityBuilder(Translation.class);
        query.addSimpleWhere("id.entityId", entityId);
        return persistenceService.findAll(query);
    }

    public List<Translation> getTranslations(Object entity) {
        // BLARGH.   this crap is because we don't have a single entity hierarchy.  preferred would be to only act on EntityWithTenant
        if (entity instanceof EntityWithTenant) {
            return getTranslations(((EntityWithTenant)entity).getId());
        } else if (entity instanceof LegacyBaseEntity) {
            return getTranslations(((LegacyBaseEntity)entity).getUniqueID());
        }
        throw new IllegalArgumentException("entity must be of class " + EntityWithTenant.class.getSimpleName() + " or " + LegacyBaseEntity.class.getSimpleName());
    }

    public String getOgnlFor(Class<?> clazz, Field field) {
        Localized localizedClass = clazz.getAnnotation(Localized.class);
        Localized localizedField = field.getAnnotation(Localized.class);
        return localizedField!=null && localizedField!=null ?
                String.format(translationFormat, localizedClass.value(), localizedField.value()) : null;
    }

    private final Session getHibernateSession() {
        return (Session) getEntityManager().getDelegate();
    }

}
