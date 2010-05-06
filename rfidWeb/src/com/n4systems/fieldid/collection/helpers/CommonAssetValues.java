package com.n4systems.fieldid.collection.helpers;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.BaseOrg;

public class CommonAssetValues {
	public static final CommonAssetValues NO_COMMON_VALUES = new CommonAssetValues(null, null, null);
	
	public static CommonAssetValues createFrom(Product asset) {
		return new CommonAssetValues(asset.getLocation(), asset.getOwner(), asset.getProductStatus());

	}
	
	public final String location;
	public final BaseOrg owner;
	public final ProductStatusBean productStatus;
	

	public CommonAssetValues(String location, BaseOrg owner, ProductStatusBean productStatus) {
		super();
		this.location = location;
		this.owner = owner;
		this.productStatus = productStatus;
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

	public CommonAssetValues findCommon(Product asset) {
		CommonAssetValues otherAssetCommonValues = createFrom(asset);
		String commonLocation = commonLocation(otherAssetCommonValues);
		BaseOrg commonOwner = commonOwner(otherAssetCommonValues);
		ProductStatusBean commonProductStatus = commonProductStatus(otherAssetCommonValues);
		
		return new CommonAssetValues(commonLocation, commonOwner, commonProductStatus);
	}

	private ProductStatusBean commonProductStatus(CommonAssetValues otherAssetCommonValues) {
		return productStatus != null && productStatus.equals(otherAssetCommonValues.productStatus) ? this.productStatus : null;
	}

	private BaseOrg commonOwner(CommonAssetValues otherAssetCommonValues) {
		return owner != null && owner.equals(otherAssetCommonValues.owner) ? this.owner : null;
	}

	private String commonLocation(CommonAssetValues otherAssetCommonValues) {
		return location != null && location.equals(otherAssetCommonValues.location) ? this.location : null;
	}
	
}
