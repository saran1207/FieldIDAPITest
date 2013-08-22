package com.n4systems.persistence.localization2;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;


public class LocalizedText implements Serializable {

    private String text;
    private transient String translatedValue;
    private transient Map<Locale,String> translations;

    public LocalizedText(String value) {
        this.text = value;
    }

    public String getTranslatedValue() {
        return translatedValue;
    }

    public void setTranslatedValue(String translatedValue) {
        this.translatedValue = translatedValue;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return translatedValue!=null ? translatedValue : text;
    }

    public Map<Locale, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<Locale, String> translations) {
        this.translations = translations;
    }
}
