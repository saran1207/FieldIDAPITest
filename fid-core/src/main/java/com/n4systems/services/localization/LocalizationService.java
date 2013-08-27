package com.n4systems.services.localization;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.util.persistence.QueryBuilder;

import java.lang.reflect.Field;
import java.util.List;

public class LocalizationService extends FieldIdPersistenceService {

    private String translationFormat = "%s.%s";

    public List<Translation> getTranslations(EntityWithTenant entity) {
        QueryBuilder<Translation> query = createTenantSecurityBuilder(Translation.class);
        query.addSimpleWhere("id.entityId", entity.getId());
        return persistenceService.findAll(query);
    }

    public String getOgnlFor(Class<? extends EntityWithTenant> clazz, Field field) {
        Localized localizedClass = clazz.getAnnotation(Localized.class);
        Localized localizedField = field.getAnnotation(Localized.class);
        return localizedField!=null && localizedField!=null ?
                String.format(translationFormat, localizedClass.value(), localizedField.value()) : null;
    }
}
