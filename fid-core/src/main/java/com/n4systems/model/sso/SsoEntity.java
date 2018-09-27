package com.n4systems.model.sso;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * SSO entity ids are to be unique amongst both IDP and SP objects.
 */
@Entity
@Table(name="sso_entity")
public class SsoEntity implements Serializable {

    @Id
    @Column(name="sso_entity_id")
    private String entityId;

    public SsoEntity() {
    }

    public SsoEntity(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}
