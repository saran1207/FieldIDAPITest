package com.n4systems.services.localization;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.localization.Translation;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;

import java.lang.reflect.Field;
import java.util.List;

public class LocalizationService extends FieldIdPersistenceService {


    private static final Logger logger=Logger.getLogger(LocalizationService.class);

    private String translationFormat = "%s.%s";

    public List<Translation> getTranslations(Long entityId) {
        QueryBuilder<Translation> query = createTenantSecurityBuilder(Translation.class);
        query.addSimpleWhere("id.entityId", entityId);
        return persistenceService.findAll(query);
    }

    public <T extends Saveable> List<Translation> getTranslations(T entity) {
        return getTranslations((Long) entity.getEntityId());
    }

//    public String getOgnlFor(Class<?> clazz, Field field) {
//        return localizedField!=null && localizedField!=null ?
//                String.format(translationFormat, localizedClass.value(), localizedField.value()) : null;
//    }

    private final Session getHibernateSession() {
        return (Session) getEntityManager().getDelegate();
    }

    private final ClassMetadata getClassMetaData(String className) {
        return getHibernateSession().getSessionFactory().getClassMetadata(className);
    }

    private final ClassMetadata getClassMetaData(Class clazz) {
        return getHibernateSession().getSessionFactory().getClassMetadata(clazz);
    }

    public List<Translation> getAllTranslations() {
        return persistenceService.findAllNonSecure(Translation.class);
    }

    public String getOgnlFor(Field field) {
        ClassMetadata metadata = getClassMetaData(field.getDeclaringClass());
        try {
            if (metadata instanceof AbstractEntityPersister) {   // assumes we are using hibernate.
                AbstractEntityPersister persister = (AbstractEntityPersister) metadata;
                String tableName = persister.getTableName();
                String columnName = persister.getPropertyColumnNames(field.getName())[0];
                return String.format(translationFormat, tableName, columnName);
            } else {
                throw new Exception("huh?");
            }
        } catch (Exception e) {
            throw new IllegalStateException("can't create translation ognl for " + field.getName() + " in class " + field.getDeclaringClass().getSimpleName());
        }
    }
}
