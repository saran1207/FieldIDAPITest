package com.n4systems.model.orgs;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.security.SecurityDefiner;

import javax.persistence.*;

@Entity
@Table(name = "primary_org_children")
public class PrimaryOrgChildren implements HasOwner {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "parent.id", null, null, false);
    }


    enum ChildType {SECONDARY,CUSTOMER};

    @Id
    protected Long id;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable=false)
    private ChildType type;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private BaseOrg parent;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="org_id")
    // this will be either a SecondaryOrg or a CustomerOrg.
    private BaseOrg org;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    public ChildType getType() {
        return type;
    }

    public void setType(ChildType type) {
        this.type = type;
    }

    public BaseOrg getParent() {
        return parent;
    }

    public void setParent(BaseOrg parent) {
        this.parent = parent;
    }

    @Override
    public BaseOrg getOwner() {
        return parent;
    }

    @Override
    public void setOwner(BaseOrg owner) {
        this.parent = owner;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BaseOrg getOrg() {
        return org;
    }

    public void setOrg(BaseOrg org) {
        this.org = org;
    }
}
