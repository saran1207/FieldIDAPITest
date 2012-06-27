package com.n4systems.model;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "assetaudit")
public class AssetAudit extends EntityWithTenant {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="identified", nullable = true)
    private Date identified;
    @Column(name="owner", nullable = true)
    private String owner;
    @Column(name="location", nullable = true)
    private String location;
    @Column(name="purchase_order", nullable = true)
    private String purchaseOrder;
    @Column(name="asset_status", nullable = true)
    private String assetStatus;
    @Column(name="comments", nullable = true)
    private String comments;
    @Column(name = "published", nullable = true)
    private String published;
    @Column(name="user_name")
    private String userName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="assetaudit_asset", joinColumns = @JoinColumn(name = "assetaudit_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "asset_id", referencedColumnName = "id"))
    private Set<Asset> assets;

    public AssetAudit() {
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getIdentified() {
        return identified;
    }

    public void setIdentified(Date identified) {
        this.identified = identified;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

