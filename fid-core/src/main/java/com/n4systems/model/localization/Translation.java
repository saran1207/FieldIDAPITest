package com.n4systems.model.localization;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Locale;

@Entity
@Table(name = "translations")
public class Translation implements Serializable, Saveable {

    private @EmbeddedId CompoundKey id = new CompoundKey();
    private String value;

    private @Transient boolean isNew = false;

    public static SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("id.tenantId", null, null, null);
    }

    public static Translation makeNew(Long tenantId, Long entityId, String ognl, Locale language) {
        return new Translation(tenantId,entityId,ognl,language);
    }

    private Translation(Long tenantId, Long entityId, String ognl, Locale language) {
        id = new CompoundKey(tenantId,entityId, ognl, language);
        isNew = true;
    }

    public Translation() {
    }

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

    public Translation withId(Long tenantId, Long entityId, String ognl, Locale language) {
        setId(new CompoundKey(tenantId, entityId, ognl, language));
        return this;
    }
    
    public Translation withValue(String value) {
        setValue(value);
        return this;
    }

    public boolean isNew() {
        return isNew;
    }

    @Override
    public Object getEntityId() {
        return id.entityId;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "value='" + value + '\'' +
                ", id=" + id +
                '}';
    }

    @Embeddable
    public static class CompoundKey implements Serializable {

        @Column(name="tenant_id") Long tenantId;
        @Column(name="entity_id") Long entityId;
        @Column(name="ognl") String ognl;
        @Column(name="language") String language;

        public CompoundKey() {
        }

        public CompoundKey(Long tenantId, Long entityId, String ognl, Locale language) {
            this.entityId = entityId;
            this.language = language.toString();
            this.ognl = ognl;
            this.tenantId = tenantId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompoundKey)) return false;

            CompoundKey that = (CompoundKey) o;

            if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
            if (language != null ? !language.equals(that.language) : that.language != null) return false;
            if (ognl != null ? !ognl.equals(that.ognl) : that.ognl != null) return false;
            if (tenantId != null ? !tenantId.equals(that.tenantId) : that.tenantId != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = tenantId != null ? tenantId.hashCode() : 0;
            result = 31 * result + (entityId != null ? entityId.hashCode() : 0);
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

        public Long getTenantId() {
            return tenantId;
        }

        public String getFieldOgnl() {
            // assumes format of "classPrefix.fieldSuffix"
            // .:  "widget.name"   -->  "name"
            return ognl.substring(ognl.indexOf('.')+1);
        }

        @Override
        public String toString() {
            return tenantId +
                    "|" + entityId +
                    ":" + ognl +
                    "(" + language + ')';
        }
    }

}
