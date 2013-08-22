package com.n4systems.persistence.localization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.BaseEntity;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.reflections.ReflectionUtils.*;

public class LocalizedTextCache implements InitializingBean {

    private static Map<Locale, Map<TranslationKey,String>> cache = Maps.newHashMap();
    private static Set<String> ognl = Sets.newHashSet();

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
        // i.e. if the DB contains a translation ognl of "assetType.name" and it isn't in the class meta-data/annotations then somethings screwed up.
        for (Map<TranslationKey,String> languageMap:cache.values()) {
            for (TranslationKey key:languageMap.keySet()) {
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
        Locale language = StringUtils.parseLocaleString(translation.getId().getLanguage());
        Map<TranslationKey, String> languageMap = cache.get(language);
        if (languageMap==null) {
            languageMap = new TreeMap<TranslationKey,String>();
            cache.put(language,languageMap);
        }
        languageMap.put(new TranslationKey(translation), translation.getValue());
    }

    public static String getText(BaseEntity entity, String ognl, Locale locale) {
        Preconditions.checkArgument(locale != null && entity != null, "must supply non-null args");
        Map<TranslationKey, String> map = cache.get(locale);
        TranslationKey key = new TranslationKey(entity, ognl);
        return map == null ? null : map.get(key);
    }

    // -----------------------------------------------------------------------------------------------

    static class TranslationKey implements Comparable<TranslationKey> {
        String ognl;
        Long entityId;

        TranslationKey(BaseEntity entity, String ognl) {
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
    }

}

