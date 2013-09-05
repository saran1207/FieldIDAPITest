package com.n4systems.services.localization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.parents.legacy.LegacyBaseEntity;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.withAnnotation;

public class LocalizationService extends FieldIdPersistenceService implements InitializingBean {

    private static final Logger logger=Logger.getLogger(LocalizationService.class);

    private static Map<Long, Map<TranslationKey, Map<Locale,Object>>> translationCache = Maps.newHashMap();

    private static final String translationFormat = "%s.%s";
    private static final String collectionTranslationSuffix="@";


    @Override
    public void afterPropertiesSet() throws Exception {
        initializeCache();
    }

    private void initializeCache() {

        try {
        // TODO DD : make this use EHCACHE instead of simple map.
        // that way cache can be configured dynamically (in memory, disk spill over, LRU etc...)
        List<Translation> translations = getAllTranslations();
        for (Translation translation:translations) {
            add(translation);
        }
        validate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
private void validate() {
        // TODO : further validation could do a foreign key check to make sure entity_id column is valid.

        Set<Class> entities = Sets.newHashSet();
        entities.addAll(new Reflections(BaseEntity.class.getPackage().getName()).getSubTypesOf(BaseEntity.class));
        entities.addAll(new Reflections(LegacyBaseEntity.class.getPackage().getName()).getSubTypesOf(LegacyBaseEntity.class));
        entities.addAll(new Reflections("rfid.ejb").getSubTypesOf(LegacyBaseEntity.class));
        Set<String> expected = Sets.newHashSet();
        for (Class<?> clazz:entities) {
            Set<Field> fields = ReflectionUtils.getAllFields(clazz, withAnnotation(Localized.class));
            for (Field field : fields) {
                expected.add(getOgnlFor(field));
            }
        }

        Set<String> errors = Sets.newHashSet();
        // now test that all of current values are in the expected/defined list of valid translation ognl.
        // i.e. if the DB contains a translation ognl of "types.name" but that isn't in expected list which is say, {assettypes.name, assettypes.desc}
        //  then something is screwed up.
        // possible fixes (depending on your assessment) could be
        // 1: migrate the value.   types.name --> assettypes.name
        // 2: delete the translated values  types.*    because they aren't relevant anymore.   (not likely)
        // 3: rename the table so the translation matches.    rename assettypes to types;
        for (Map<TranslationKey, Map<Locale,Object>> tenantMap: translationCache.values()) {
            for (TranslationKey key:tenantMap.keySet()) {
                if (!expected.contains(key.ognl)) {
                    errors.add(key.ognl);
                }
            }
        }
        if (errors.size()>0) {
            logger.error("The DB currently has invalid translation ognl(s) :  " + errors + "\n This doesn't match current meta-data.  did you recently rename/delete any tables or columns?");
        }
    }

    private void add(Translation translation) {
        // TODO DD : remove translation tenant id stuff.   will break everything.  or maybe just save tenant but not required for reading.  more for dealing with security and caching.

        Long tenantId = translation.getId().getTenantId();
        Map<TranslationKey, Map<Locale, Object>> tenantMap = translationCache.get(tenantId);
        if (tenantMap==null) {
            tenantMap=Maps.newHashMap();
            translationCache.put(tenantId, tenantMap);
        }
        add(translation, tenantMap);
    }

    private void add(Translation translation, Map<TranslationKey, Map<Locale, Object>> map) {
        TranslationKey translationKey = new TranslationKey(translation);
        Map<Locale, Object> keyMap = map.get(translationKey);
        if (keyMap==null) {
            keyMap = Maps.newHashMap();
            map.put(translationKey,keyMap);
        }
        Locale locale = StringUtils.parseLocaleString(translation.getId().getLanguage());
        // currently doesn't support sets. must have order.
        if (translationKey.isCollection()) {
            List<String> list = (List<String>) keyMap.get(locale);
            if (list==null) {
                list = Lists.newArrayList();
                keyMap.put(locale, list);
            }
            addToList(translationKey, list, translation.getValue());
        } else {
            keyMap.put(locale, translation.getValue());
        }
    }

    private void addToList(TranslationKey translationKey, List<String> list, String value) {
        while (list.size()<=translationKey.getIndex()) {
            list.add(null);
        }
        list.set(translationKey.getIndex(), value);
    }

    // handle event form saving.  google translate API.

    public Object getText(EntityWithTenant entity, String ognl, Locale locale) {
        Preconditions.checkArgument(locale != null && entity != null, "must supply non-null args");
        Long tenantId = entity.getTenant().getId();
        Map<TranslationKey, Map<Locale, Object>> tenantMap = translationCache.get(tenantId);
        if (tenantMap!=null) {
            Map<Locale, Object> map = tenantMap.get(new TranslationKey(entity, ognl));
            return map==null ? null : map.get(locale);
        }
        return null;
    }

    public Object getTranslation(Saveable entity, String ognl, Locale locale) {
        Map<Locale, Object> translations = getTranslations(entity, ognl);
        return translations==null ? null :
                locale==null ? null : translations.get(locale);
    }

    public Map<Locale, Object> getTranslations(Saveable entity, String ognl) {
        Long tenantId = 0L;
        // what to do if not a tenant to be found??
        Map<TranslationKey, Map<Locale, Object>> tenantMap = translationCache.get(tenantId);
        if (tenantMap!=null) {
            Map<Locale, Object> map = tenantMap.get(new TranslationKey(entity, ognl));
            return map;
        }
        return null;
    }

    private final Session getHibernateSession() {
        return (Session) getEntityManager().getDelegate();
    }

    private final ClassMetadata getClassMetaData(Class clazz) {
        return getHibernateSession().getSessionFactory().getClassMetadata(clazz);
    }

    public List<Translation> getAllTranslations() {
        return persistenceService.findAllNonSecure(Translation.class);
    }

    // TODO DD : cache this.  should only do this once!
    //@Cacheable("localizationOgnl")
    public String getOgnlFor(Field field) {
        // recall : ognl = table + "." + column      e.g. "assettypes.name"
        ClassMetadata metadata = getClassMetaData(field.getDeclaringClass());
        try {
            if (metadata instanceof AbstractEntityPersister) {   // assumes we are using hibernate.
                AbstractEntityPersister persister = (AbstractEntityPersister) metadata;
                if (Collection.class.isAssignableFrom(field.getType())) {
                    return getCollectionOgnlFor(field,persister);
                } else {
                    return getOgnlFor(field,persister);
                }
            } else {
                throw new Exception("can't get ORM meta data...if using something other than hibernate need to refactor this.");
            }
        } catch (Exception e) {
            throw new IllegalStateException("can't create translation ognl for " + field.getName() + " in class " + field.getDeclaringClass().getSimpleName());
        }
    }

    private String getOgnlFor(Field field, AbstractEntityPersister persister) {
        String tableName = persister.getTableName();
        String columnName = persister.getPropertyColumnNames(field.getName())[0];
        return String.format(translationFormat, tableName, columnName);
    }

    private String getCollectionOgnlFor(Field field, AbstractEntityPersister persister) {
        String tableName = persister.getTableName();
        return String.format(translationFormat, tableName, field.getName()) + collectionTranslationSuffix;
    }

    public void save(List<Translation> translations) {
        for (Translation translation : translations) {
            persistenceService.saveOrUpdate(translations);
        }
    }

    public List<Translation> getTranslations(Long entityId) {
        QueryBuilder<Translation> query = createTenantSecurityBuilder(Translation.class);
        query.addSimpleWhere("id.entityId", entityId);
        return persistenceService.findAll(query);
    }



    // -----------------------------------------------------------------------------------------------

    static class TranslationKey implements Comparable<TranslationKey> {
        String ognl;
        Long entityId;
        transient Integer index;

        TranslationKey(Translation translation) {
            this(translation.getId().getEntityId(),translation.getId().getOgnl());
        }

        TranslationKey(Saveable entity, String ognl) {
            this((Long)entity.getEntityId(), ognl);
        }

        public TranslationKey(Long entityId, String ognl) {
            this.entityId = entityId;
            this.ognl = ognl;
            if (isCollection()) {
                normalizeForCollection(); // strip off collection suffix.   recommendations@0 = recommendations@
            }
        }

        public boolean isCollection() {
            return ognl.indexOf(collectionTranslationSuffix)!=-1;
        }

        public Integer getIndex() {
            return index;
        }

        private void normalizeForCollection() {
            int i = ognl.indexOf(collectionTranslationSuffix);
            String suffix = ognl.substring(i + 1);
            if (org.apache.commons.lang.StringUtils.isNotBlank(suffix)) {
                this.index = Integer.parseInt(suffix);
            }
            this.ognl = ognl.substring(0, i + 1);
        }

        @Override
        public int compareTo(TranslationKey key) {
            if (key==null) return -1;
            if (key instanceof TranslationKey) {
                if (entityId==key.entityId) return ognl.compareTo(key.ognl);
                return (int)(entityId-key.entityId);
            }
            return -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TranslationKey)) return false;

            TranslationKey that = (TranslationKey) o;

            if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
            if (ognl != null ? !ognl.equals(that.ognl) : that.ognl != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = ognl != null ? ognl.hashCode() : 0;
            result = 31 * result + (entityId != null ? entityId.hashCode() : 0);
            return result;
        }

    }

}
