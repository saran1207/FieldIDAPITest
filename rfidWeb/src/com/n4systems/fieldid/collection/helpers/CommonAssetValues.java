package com.n4systems.fieldid.collection.helpers;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Product;
import com.n4systems.model.orgs.BaseOrg;

public class CommonAssetValues {
	public static final String NO_COMMON_LOCATION = null;
	public static final BaseOrg NO_COMMON_OWNER = null;
	public static final ProductStatusBean NO_COMMON_PRODUCT_STATUS = null;
	public static final Assignment NO_COMMON_ASSIGNMENT = null;
	
	
	public static final CommonAssetValues NO_COMMON_VALUES = new CommonAssetValues(NO_COMMON_LOCATION, NO_COMMON_OWNER, NO_COMMON_PRODUCT_STATUS, NO_COMMON_ASSIGNMENT);
	
	public static CommonAssetValues createFrom(Product asset) {
		return new CommonAssetValues(asset.getLocation(), asset.getOwner(), asset.getProductStatus(), new Assignment(asset.getAssignedUser()));

	}
	
	public final String location;
	public final BaseOrg owner;
	public final ProductStatusBean productStatus;
	public final Assignment assignment;
	

	public CommonAssetValues(String location, BaseOrg owner, ProductStatusBean productStatus, Assignment assignment) {
		super();
		this.location = location;
		this.owner = owner;
		this.productStatus = productStatus;
		this.assignment = assignment;
	}

	public CommonAssetValues findCommon(Product asset) {
		CommonAssetValues otherAssetCommonValues = createFrom(asset);
		String commonLocation = commonLocation(otherAssetCommonValues);
		BaseOrg commonOwner = commonOwner(otherAssetCommonValues);
		ProductStatusBean commonProductStatus = commonProductStatus(otherAssetCommonValues);
		Assignment commonAssignment = commonAssignment(otherAssetCommonValues);
		
		return new CommonAssetValues(commonLocation, commonOwner, commonProductStatus, commonAssignment);
	}

	private Assignment commonAssignment(CommonAssetValues otherAssetCommonValues) {
		return assignment != null && assignment.equals(otherAssetCommonValues.assignment) ? assignment : NO_COMMON_ASSIGNMENT;
	}

	private ProductStatusBean commonProductStatus(CommonAssetValues otherAssetCommonValues) {
		return productStatus != null && productStatus.equals(otherAssetCommonValues.productStatus) ? productStatus : NO_COMMON_PRODUCT_STATUS;
	}

	private BaseOrg commonOwner(CommonAssetValues otherAssetCommonValues) {
		return owner != null && owner.equals(otherAssetCommonValues.owner) ? owner : NO_COMMON_OWNER;
	}

	private String commonLocation(CommonAssetValues otherAssetCommonValues) {
		return location != null && location.equals(otherAssetCommonValues.location) ? location : NO_COMMON_LOCATION;
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
}
