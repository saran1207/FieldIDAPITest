package com.n4systems.persistence.localization;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.PersistenceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class LocalizedTextCache implements InitializingBean {

    private static Map<Locale, Map<Long,String>> cache = Maps.newHashMap();

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
    }

    private void add(Translation translation) {
        LocaleEditor localeEditor = new LocaleEditor();
        localeEditor.setAsText(translation.getId().getLanguage());
        Locale language = (Locale) localeEditor.getValue();
        Map<Long, String> languageMap = cache.get(language);
        if (languageMap==null) {
            languageMap = new TreeMap<Long,String>();
            cache.put(language,languageMap);
        }
        add(languageMap, translation.getTranslationId(), translation.getValue());
    }

    private void add(Map<Long, String> languageMap, Long id, String value) {
        Preconditions.checkArgument(languageMap.get(id)==null,"an entry for " + id + "/" + value + " already exists = " + languageMap.get(id));
        languageMap.put(id, value);
    }


    // select * from infofieldbean_name_tr =
    //    infofieldbean_id    |      language_code      |     value


    public static String getText(Long id, Locale locale) {
        Preconditions.checkArgument(locale != null && id != null, "must supply non-null args");
        Map<Long, String> map = cache.get(locale);
        return map == null ? null : map.get(id);
    }

    public static Long getCode(String value, Locale locale) {
        // TODO DD: needs optimization.
        Map<Long, String> map = cache.get(locale);

        for (Long code:map.keySet()) {
            String v = map.get(code);
            if (v.equals(value)) {
                return code;
            }
        }
        // TODO DD : implement this!!!

        return 1L;
    }

    private String getTranslationTable(String table, String property) {
        return table + "_" + property;
    }

}

