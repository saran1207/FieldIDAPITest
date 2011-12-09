package com.n4systems.model.builders;

import static com.n4systems.model.builders.AssetTypeBuilder.*;
import static com.n4systems.model.builders.SubAssetBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

	private String identifier;
    private String rfidNumber;
    private String referenceNumber;
	private Date modified;
	
	private List<SubAsset> subAssets;
	private Location location;
	private AssetStatus assetStatus;
	private User assignedTo;
    private User identifiedBy;
    private boolean published;
    private String purchaseOrder;
	private String nonIntegrationOrderNumber;

    public static AssetBuilder anAsset() {
		return new AssetBuilder(null, null, anAssetType().build(), null, null, new Location(), null, NOT_ASSIGNED, true, null, null, new ArrayList<SubAsset>(), null, null, null);
	}

    public AssetBuilder(){}

	private AssetBuilder(Tenant tenant, BaseOrg owner, AssetType type, String identifier, Date modified, Location location, AssetStatus assetStatus, User assignedTo, boolean published, String purchaseOrder, User identifiedBy, List<SubAsset> subAssets, String rfidNumber, String referenceNumber, String nonIntegrationOrderNumber) {
        super(tenant, owner);
		this.type = type;
		this.identifier = identifier;
		this.modified = modified;
		this.location = location;
		this.assetStatus = assetStatus;
		this.assignedTo = assignedTo;
		this.subAssets = subAssets;
        this.published = published;
        this.purchaseOrder = purchaseOrder;
        this.identifiedBy = identifiedBy;
        this.rfidNumber = rfidNumber;
        this.referenceNumber = referenceNumber;
        this.nonIntegrationOrderNumber = nonIntegrationOrderNumber;
	}

    public AssetBuilder identifiedBy(User identifiedBy) {
        return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
    }

	public AssetBuilder ofType(AssetType type) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder forTenant(Tenant tenant) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder withOwner(BaseOrg owner) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder withIdentifier(String identifier) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder withModifiedDate(Date modified) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder withOneSubAsset() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, Arrays.asList(aSubAsset().build()), rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder withTwoSubAssets() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, Arrays.asList(aSubAsset().build(), aSubAsset().build()), rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder inFreeformLocation(String location) {
		return withAdvancedLocation(Location.onlyFreeformLocation(location));
	}
	
	public AssetBuilder withAdvancedLocation(Location location) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder havingStatus(AssetStatus assetStatus) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder assignedTo(User employee) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, employee, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
	
	public AssetBuilder unassigned() {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, null, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}

    public AssetBuilder published(boolean published) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}

    public AssetBuilder purchaseOrder(String purchaseOrder) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}

    public AssetBuilder rfidNumber(String rfidNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}

	public AssetBuilder referenceNumber(String referenceNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
	}
    
	public AssetBuilder nonIntegrationOrderNumber(String nonIntegrationOrderNumber) {
		return makeBuilder(new AssetBuilder(tenant, owner, type, identifier, modified, location, assetStatus, assignedTo, published, purchaseOrder, identifiedBy, subAssets, rfidNumber, referenceNumber, nonIntegrationOrderNumber));
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
		asset.setIdentifier(identifier);
		asset.setModified(modified);
		asset.setAssetStatus(assetStatus);
		asset.setAssignedUser(assignedTo);
		asset.setAdvancedLocation(location);
        asset.setPublished(published);
        asset.setPurchaseOrder(purchaseOrder);
        asset.setIdentifiedBy(identifiedBy);
        asset.setRfidNumber(rfidNumber);
        asset.setCustomerRefNumber(referenceNumber);
        asset.setNonIntergrationOrderNumber(nonIntegrationOrderNumber);

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
