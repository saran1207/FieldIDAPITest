package com.n4systems.model.localization;

import com.n4systems.model.api.UnsecuredEntity;

import javax.persistence.*;
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
        return id.getEntityId();
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

        private @Column(name="entity_id") Long entityId;
        private @Column(name="ognl") String ognl;
        private @Column(name="language") String language;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompoundKey)) return false;

            CompoundKey that = (CompoundKey) o;

            if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
            if (language != null ? !language.equals(that.language) : that.language != null) return false;
            if (ognl != null ? !ognl.equals(that.ognl) : that.ognl != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = entityId != null ? entityId.hashCode() : 0;
            result = 31 * result + (ognl != null ? ognl.hashCode() : 0);
            result = 31 * result + (language != null ? language.hashCode() : 0);
            return result;
        }

        public Long getEntityId() {
            return entityId;
        }

        public String getLanguage() {
            return language;
        }

        public String getOgnl() {
            return ognl;
        }
    }


}
