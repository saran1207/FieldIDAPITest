package com.n4systems.model.sso;

import com.n4systems.model.Tenant;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Metadata that defines the IDP for a tenant
 */
@Entity
@Table(name="sso_idp_metadata")
public class SsoIdpMetadata implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name="sso_entity_id", unique=true, nullable = false)
    @OneToOne
    private SsoEntity ssoEntity;

   /* @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="idp_tenants",
        joinColumns = {@JoinColumn(name = "entity_id")},
        inverseJoinColumns = { @JoinColumn(name = "id")})
    private List<Tenant> tenants;*/

    @JoinColumn(name="tenant_id", unique=true, nullable = false)
    @OneToOne
    private Tenant tenant;

    @Column(name="metadata", nullable = false)
    private String serializedMetadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SsoEntity getSsoEntity() {
        return ssoEntity;
    }

    public void setSsoEntity(SsoEntity entityName) {
        this.ssoEntity = entityName;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getSerializedMetadata() {
        return serializedMetadata;
    }

    public void setSerializedMetadata(String serializedMetadata) {
        this.serializedMetadata = serializedMetadata;
    }
}
