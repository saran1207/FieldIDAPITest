package com.n4systems.model.localization;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Locale;

@Entity
@Table(name = "languages")
public class Language extends BaseEntity implements UnsecuredEntity, Comparable{

    //Language codes http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
    private Locale locale;

    public Language() {
    }

    public Language(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDisplayLanguage() {
        return locale.getDisplayLanguage();
    }

    @Override
    public int compareTo(Object otherLanguage) {
        return this.getDisplayLanguage().compareTo(((Language)otherLanguage).getDisplayLanguage());
    }
}
