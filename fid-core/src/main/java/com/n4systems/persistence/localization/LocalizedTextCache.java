package com.n4systems.persistence.localization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.localization.Translation;
import com.n4systems.model.parents.EntityWithTenant;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

public class LocalizedTextCache implements InitializingBean {

    private static Map<Long, Map<TranslationKey, Map<Locale,String>>> cache = Maps.newHashMap();

    private @Autowired PersistenceService persistenceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        initializeCache();
    }

    private void initializeCache() {
        // TODO DD : make this use EHCACHE instead of simple map.
        // that way cache can be configured dynamically (in memory, disk spill over, LRU etc...)
        List<Translation> translations = persistenceService.findAllNonSecure(Translation.class);
        for (Translation translation:translations) {
            add(translation);
        }
        validate();
    }

    private void validate() {
        Set<Class<?>> localizedEntities = new Reflections(BaseEntity.class.getPackage().getName()).getTypesAnnotatedWith(Localized.class);
        Set<String> expected = Sets.newHashSet();
        for (Class<?> clazz:localizedEntities) {
            expected.addAll(getExpectedFor(clazz));
        }

        Set<String> errors = Sets.newHashSet();
        // now test that all of current values are in the expected/defined list of valid translation ognls.
        // i.e. if the DB contains a translation ognl of "assetType.name" but that isn't in expected list (which is say, {at.name, at.desc} or {assetType.displayName, assetType.desc})
        //  then something is screwed up.   either the class/field was unlocalized or it was renamed. (i.e. given a different @Localized value.
        // possible fixes (depending on your assessment could be
        // 1: migrate the value.   assetType.name --> at.name
        // 2: delete the values    assetType.*    DELETE
        // 3: change the @Localized values       @Localize("at")    -->    @Localized("assetType")
        for (Map<TranslationKey, Map<Locale,String>> tenantMap:cache.values()) {
            for (TranslationKey key:tenantMap.keySet()) {
                if (!expected.contains(key.ognl)) {
                    errors.add(key.ognl);
                }
            }
        }
        if (errors.size()>0) {
            throw new IllegalStateException("the DB currently has invalid translation ognl(s) :  " + errors + "\n Their ognl doesn't match current meta-data.  did you recently change the values of any " + Localized.class.getSimpleName() + " values?");
        }
    }

    private Set<String> getExpectedFor(Class<?> clazz) {
        // recall : ognl = class + "." + field      e.g. "assetType.name"
        // class/field values are taken from @Localized annotation values.
        Set<String> expected = Sets.newHashSet();
        String prefix = clazz.getAnnotation(Localized.class).value();
        Set<Field> localizedFields = getAllFields(clazz, withAnnotation(Localized.class));
        for (Field localizedField:localizedFields) {
            String suffix = localizedField.getAnnotation(Localized.class).value();
            String ognl = prefix + "." + suffix;
            expected.add(ognl);
        }
        return expected;
    }

    private void add(Translation translation) {
        Long tenantId = translation.getId().getTenantId();
        Map<TranslationKey, Map<Locale, String>> tenantMap = cache.get(tenantId);
        if (tenantMap==null) {
            tenantMap=Maps.newHashMap();
            cache.put(tenantId, tenantMap);
        }
        add(translation, tenantMap);
    }

    private void add(Translation translation, Map<TranslationKey, Map<Locale, String>> map) {
        TranslationKey translationKey = new TranslationKey(translation);
        Map<Locale, String> keyMap = map.get(translationKey);
        if (keyMap==null) {
            keyMap = Maps.newHashMap();
            map.put(translationKey,keyMap);
        }
        Locale locale = StringUtils.parseLocaleString(translation.getId().getLanguage());
        keyMap.put(locale, translation.getValue());
    }

    public static String getText(EntityWithTenant entity, String ognl, Locale locale) {
        Preconditions.checkArgument(locale != null && entity != null, "must supply non-null args");
        Long tenantId = entity.getTenant().getId();
        Map<TranslationKey, Map<Locale, String>> tenantMap = cache.get(tenantId);
        if (tenantMap!=null) {
            Map<Locale, String> map = tenantMap.get(new TranslationKey(entity, ognl));
            return map==null ? null : map.get(locale);
        }
        return null;
    }

    public static Map<Locale, String> getTranslations(EntityWithTenant entity, String ognl) {
        Long tenantId = entity.getTenant().getId();
        Map<TranslationKey, Map<Locale, String>> tenantMap = cache.get(tenantId);
        if (tenantMap!=null) {
            Map<Locale, String> map = tenantMap.get(new TranslationKey(entity, ognl));
            return map;
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------------

    static class TranslationKey implements Comparable<TranslationKey> {
        String ognl;
        Long entityId;

        TranslationKey(EntityWithTenant entity, String ognl) {
            this.entityId = entity.getId();
            this.ognl = ognl;
        }

        public TranslationKey(Translation translation) {
            this.entityId = translation.getId().getEntityId();
            this.ognl = translation.getId().getOgnl();
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

