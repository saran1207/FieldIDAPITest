package com.n4systems.model.procedure;

import com.n4systems.model.AssetType;
import com.n4systems.model.BaseEntity;
import org.hibernate.annotations.IndexColumn;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="isolation_device_descriptions")
public class IsolationDeviceDescription extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="asset_type_id")
    private AssetType assetType;

    @OneToMany
    @IndexColumn(name="orderIdx")
    @JoinTable(name="isolation_device_descriptions_attribute_values", joinColumns = @JoinColumn(name = "isolation_device_description_id"), inverseJoinColumns = @JoinColumn(name = "attribute_value_id"))
    private List<InfoOptionBean> attributeValues;

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public List<InfoOptionBean> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<InfoOptionBean> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
