package com.n4systems.fieldid.collection.helpers;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;

public class CommonAssetValues {
	public static final Location NO_COMMON_LOCATION = null;
	public static final BaseOrg NO_COMMON_OWNER = null;
	public static final AssetStatus NO_COMMON_ASSET_STATUS = null;
	public static final Assignment NO_COMMON_ASSIGNMENT = null;
	
	
	public static final CommonAssetValues NO_COMMON_VALUES = new CommonAssetValues(NO_COMMON_LOCATION, NO_COMMON_OWNER, NO_COMMON_ASSET_STATUS, NO_COMMON_ASSIGNMENT);
	
	public static CommonAssetValues createFrom(Asset asset) {
		return new CommonAssetValues(asset.getAdvancedLocation(), asset.getOwner(), asset.getAssetStatus(), new Assignment(asset.getAssignedUser()));

	}
	
	public final Location location;
	public final BaseOrg owner;
	public final AssetStatus assetStatus;
	public final Assignment assignment;
	

	public CommonAssetValues(Location location, BaseOrg owner, AssetStatus assetStatus, Assignment assignment) {
		super();
		this.location = location;
		this.owner = owner;
		this.assetStatus = assetStatus;
		this.assignment = assignment;
	}

	public CommonAssetValues findCommon(Asset asset) {
		CommonAssetValues otherAssetCommonValues = createFrom(asset);
		Location commonLocation = commonLocation(otherAssetCommonValues);
		BaseOrg commonOwner = commonOwner(otherAssetCommonValues);
		AssetStatus commonAssetStatus = commonAssetStatus(otherAssetCommonValues);
		Assignment commonAssignment = commonAssignment(otherAssetCommonValues);
		
		return new CommonAssetValues(commonLocation, commonOwner, commonAssetStatus, commonAssignment);
	}

	private Assignment commonAssignment(CommonAssetValues otherAssetCommonValues) {
		return hasCommonAssignment() && assignment.equals(otherAssetCommonValues.assignment) ? assignment : NO_COMMON_ASSIGNMENT;
	}

	private AssetStatus commonAssetStatus(CommonAssetValues otherAssetCommonValues) {
		return assetStatus != null && assetStatus.equals(otherAssetCommonValues.assetStatus) ? assetStatus : NO_COMMON_ASSET_STATUS;
	}

	private BaseOrg commonOwner(CommonAssetValues otherAssetCommonValues) {
		return owner != null && owner.equals(otherAssetCommonValues.owner) ? owner : NO_COMMON_OWNER;
	}

	private Location commonLocation(CommonAssetValues otherAssetCommonValues) {
		return hasCommonLocation() && location.equals(otherAssetCommonValues.location) ? location : NO_COMMON_LOCATION;
	}
	
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public boolean hasCommonAssignment() {
		return assignment != NO_COMMON_ASSIGNMENT;
	}

	public boolean hasCommonLocation() {
		return location != NO_COMMON_LOCATION;
	}
}
