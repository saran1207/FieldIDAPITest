package com.n4systems.model.builders;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.SubAsset;
import com.n4systems.model.Tenant;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.n4systems.model.builders.AssetTypeBuilder.anAssetType;
import static com.n4systems.model.builders.SubAssetBuilder.aSubAsset;

public class AssetBuilder extends EntityWithOwnerBuilder<Asset> {
	private static final User NOT_ASSIGNED = null;

	private AssetType type;

	private String serialNumber;
    private String rfidNumber;
	private Date modified;
	
	private List<SubAsset> subAssets;
	private Location location;
	private AssetStatus assetStatus;
	private User assignedTo;
    private User identifiedBy;
    private boolean published;
    private String purchaseOrder;

    public static AssetBuilder anAsset() {
		return new AssetBuilder(null, null, anAssetType().build(), null, null, new Location(), null, NOT_ASSIGNED, true, null, null, new ArrayList<SubAsset>(), null);
	}

    public AssetBuilder(){}

	private AssetBuilder(Tenant tenant, BaseOrg owner, AssetType type, String serialNumber, Date modified, Location location, AssetStatus assetStatus, User assignedTo, boolean published, String purchaseOrder, User identifiedBy, List<SubAsset> subAssets, String rfidNumber) {
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
        this.identifiedBy = identifiedBy;
        this.rfidNumber = rfidNumber;
	}

    public AssetBuilder identifiedBy(User identifiedBy) {
        return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
    }

	public AssetBuilder ofType(AssetType type) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder forTenant(Tenant tenant) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder withSerialNumber(String serialNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder withModifiedDate(Date modified) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder withOneSubAsset() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, Arrays.asList(aSubAsset().build()), rfidNumber));
	}
	
	public AssetBuilder withTwoSubAssets() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, Arrays.asList(aSubAsset().build(), aSubAsset().build()), rfidNumber));
	}
	
	public AssetBuilder inFreeformLocation(String location) {
		return withAdvancedLocation(Location.onlyFreeformLocation(location));
	}
	
	public AssetBuilder withAdvancedLocation(Location location) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder havingStatus(AssetStatus assetStatus) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder assignedTo(User employee) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, employee, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}
	
	public AssetBuilder unassigned() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}

    public AssetBuilder published(boolean published) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}

    public AssetBuilder purchaseOrder(String purchaseOrder) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}

    public AssetBuilder rfidNumber(String rfidNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, serialNumber, modified, location, assetStatus, null, published, purchaseOrder, identifiedBy, subAssets, rfidNumber));
	}

	@Override
	public Asset createObject() {
		Asset asset = generate();
		asset.setId(getId());
		populateMasterAssetInSubAssets(asset);
		asset.setSubAssets(subAssets);
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
        asset.setIdentifiedBy(identifiedBy);
        asset.setRfidNumber(rfidNumber);

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
