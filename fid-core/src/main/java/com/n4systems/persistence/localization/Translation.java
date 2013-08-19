package com.n4systems.persistence.localization;

import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "translations")
public class Translation implements Serializable, UnsecuredEntity {

    private @EmbeddedId CompoundKey id = new CompoundKey();
    private String value;

    public CompoundKey getId() {
        return id;
    }

    public Long getTranslationId() {
        return id.getId();
    }

    public void setId(CompoundKey id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Embeddable
    public static class CompoundKey implements Serializable {
        private Long id;
        private String language;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompoundKey)) return false;

            CompoundKey that = (CompoundKey) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (language != null ? !language.equals(that.language) : that.language != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (language != null ? language.hashCode() : 0);
            return result;
        }

        public Long getId() {
            return id;
        }

        public String getLanguage() {
            return language;
        }
    }

}
