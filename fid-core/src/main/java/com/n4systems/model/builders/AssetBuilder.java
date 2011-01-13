package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetTypeBuilder.*;
import static com.n4systems.model.builders.SubAssetBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.SubAsset;

import com.n4systems.model.Tenant;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class AssetBuilder extends EntityWithOwnerBuilder<Asset> {
	private static final User NOT_ASSIGNED = null;

	private AssetType type;

	private String serialNumber;
	private Date modified;
	
	private SubAsset[] subAssets;
	private Location location;
	private AssetStatus assetStatus;
	private User assignedTo;
    private boolean published;
    private String purchaseOrder;

    public static AssetBuilder anAsset() {
		return new AssetBuilder(null, null, anAssetType().build(), null, null, new Location(), null, NOT_ASSIGNED, true, null);
	}

    public AssetBuilder(){}

	private AssetBuilder(Tenant tenant, BaseOrg owner, AssetType type, String serialNumber, Date modified, Location location, AssetStatus assetStatus, User assignedTo, boolean published, String purchaseOrder, SubAsset... subAssets) {
        super(tenant, owner);
		this.type = type;
		this.serialNumber = serialNumber;
		this.modified = modified;
		this.location = location;
		this.assetStatus = assetStatus;
		this.assignedTo = assignedTo;
		this.subAssets = subAssets;
        this.published = published;
        this.purchaseOrder = purchaseOrder;
	}

	public AssetBuilder ofType(AssetType type) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder forTenant(Tenant tenant) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder withSerialNumber(String serialNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder withModifiedDate(Date modified) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder withOneSubAsset() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, aSubAsset().build()));
	}
	
	public AssetBuilder withTwoSubAssets() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, aSubAsset().build(), aSubAsset().build()));
	}
	
	public AssetBuilder inFreeformLocation(String location) {
		return withAdvancedLocation(Location.onlyFreeformLocation(location));
	}
	
	public AssetBuilder withAdvancedLocation(Location location) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder havingStatus(AssetStatus assetStatus) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder assignedTo(User employee) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, employee, published, purchaseOrder, subAssets));
	}
	
	public AssetBuilder unassigned() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, subAssets));
	}

    public AssetBuilder published(boolean published) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, subAssets));
	}

    public AssetBuilder purchaseOrder(String purchaseOrder) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, subAssets));
	}
	
	@Override
	public Asset createObject() {
		Asset asset = generate();
		asset.setId(getId());
		populateMasterAssetInSubAssets(asset);
		asset.setSubAssets(new ArrayList<SubAsset>(Arrays.asList(subAssets)));
		return asset;
	}
	
	public Asset generate() {
		Asset asset = super.assignAbstractFields(new Asset());

		asset.setType(type);
		asset.setSerialNumber(serialNumber);
		asset.setModified(modified);
		asset.setAssetStatus(assetStatus);
		asset.setAssignedUser(assignedTo);
		asset.setAdvancedLocation(location);
        asset.setPublished(published);
        asset.setPurchaseOrder(purchaseOrder);

		return asset;
	}
	
	private void populateMasterAssetInSubAssets(Asset asset) {
		if (subAssets != null) {
			for (SubAsset subAsset : subAssets) {
				subAsset.setMasterAsset(asset);
			}
		}
	}
	
}
