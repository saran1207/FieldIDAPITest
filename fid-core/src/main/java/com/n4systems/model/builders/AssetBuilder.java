package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetTypeBuilder.*;
import static com.n4systems.model.builders.SubProductBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class AssetBuilder extends BaseBuilder<Asset> {
	private static final User NOT_ASSIGNED = null;

	private Tenant tenant;
	private BaseOrg owner;
	private AssetType type;

	private String serialNumber;
	private Date modified;
	
	private SubProduct[] subProducts;
	private Location location;
	private AssetStatus assetStatus;
	private User assignedTo;
    private boolean published;
	
	public static AssetBuilder anAsset() {
		return new AssetBuilder(TenantBuilder.n4(), OrgBuilder.aPrimaryOrg().build(), anAssetType().build(), null, null, new Location(), null, NOT_ASSIGNED, true);
	}

    public AssetBuilder(){}

	private AssetBuilder(Tenant tenant, BaseOrg owner, AssetType type, String serialNumber, Date modified, Location location, AssetStatus assetStatus, User assignedTo, boolean published, SubProduct... subProducts) {
		this.tenant = tenant;
		this.owner = owner;
		this.type = type;
		this.serialNumber = serialNumber;
		this.modified = modified;
		this.location = location;
		this.assetStatus = assetStatus;
		this.assignedTo = assignedTo;
		this.subProducts = subProducts;
        this.published = published;
	}

	public AssetBuilder ofType(AssetType type) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder forTenant(Tenant tenant) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder withSerialNumber(String serialNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder withModifiedDate(Date modified) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder withOneSubProduct() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, aSubProduct().build()));
	}
	
	public AssetBuilder withTwoSubProducts() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, aSubProduct().build(), aSubProduct().build()));
	}
	
	public AssetBuilder inFreeformLocation(String location) {
		return withAdvancedLocation(Location.onlyFreeformLocation(location));
	}
	
	public AssetBuilder withAdvancedLocation(Location location) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder havingStatus(AssetStatus assetStatus) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, subProducts));
	}
	
	public AssetBuilder assignedTo(User employee) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, employee, published, subProducts));
	}
	
	public AssetBuilder unassigned() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, subProducts));
	}

    public AssetBuilder published(boolean published) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, subProducts));
	}
	
	@Override
	public Asset createObject() {
		Asset asset = generate();
		asset.setId(id);
		populateMasterProductInSubProducts(asset);
		asset.setSubProducts(new ArrayList<SubProduct>(Arrays.asList(subProducts)));
		return asset;
	}
	
	public Asset generate() {
		Asset asset = new Asset();
		
		asset.setTenant(tenant);
		asset.setOwner(owner);
		asset.setType(type);
		asset.setSerialNumber(serialNumber);
		asset.setModified(modified);
		asset.setAssetStatus(assetStatus);
		asset.setAssignedUser(assignedTo);
		asset.setAdvancedLocation(location);
        asset.setPublished(published);
		return asset;
	}
	
	private void populateMasterProductInSubProducts(Asset asset) {
		if (subProducts != null) {
			for (SubProduct subProduct : subProducts) {
				subProduct.setMasterProduct(asset);
			}
		}
	}
	
}
