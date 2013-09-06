package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.localization.Translation;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocalizedField implements Serializable {

    private static final Logger logger= Logger.getLogger(LocalizedField.class);

    private List<Translation> translations;
    private String defaultValue;
    private String name;
    private final Object entity;
    private final String ognl;
    private final List<Locale> languages;

    public LocalizedField(Object entity, String name, String defaultValue, String ognl, List<Locale> languages) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.entity = entity;
        this.ognl = ognl;
        this.languages = languages;
        this.translations = new ArrayList<Translation>(Collections.nCopies(languages.size(), (Translation) null));

        Long tenantId = FieldIDSession.get().getTenant().getId();
        Long entityId = (Long) ((Saveable) entity).getEntityId();
        for (Locale language:languages) {
            addTranslation(Translation.makeNew(tenantId, entityId, ognl, language));
        }
    }

    public LocalizedField addTranslation(Translation translation) {
        // all tenantid, entityid, ognl should be same in this list. just the language should be different.
        Locale locale = StringUtils.parseLocaleString(translation.getId().getLanguage());
        int index = languages.indexOf(locale);
        if (index<0) {
            logger.error("language of translation " + translation + " is not supported in list " + languages);
        } else {
            translations.set(index, translation);
        }
        return this;
    }

    public List<Translation> getTranslationsToSave() {
        List<Translation> result = Lists.newArrayList();
        for (Translation translation:translations) {
            if (!translation.isNew() || org.apache.commons.lang.StringUtils.isNotBlank(translation.getValue())) {
                result.add(translation);
            }
        }
        return result;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    @Override
    public String toString() {
        return defaultValue + ':' + translations;
    }

    public String getName() {
        return name;
    }

    public String getOgnl() {
        return ognl;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public Object getEntity() {
        return entity;
    }
}
