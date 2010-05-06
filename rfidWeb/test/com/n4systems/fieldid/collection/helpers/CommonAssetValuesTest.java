package com.n4systems.fieldid.collection.helpers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Product;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.orgs.BaseOrg;


public class CommonAssetValuesTest {
	
	
	@Test
	public void should_create_an_common_values_with_the_location_on_the_asset() throws Exception {
		Product asset = ProductBuilder.aProduct().inLocation("location").build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.location, equalTo("location")); 
	}
	
	
	@Test
	public void should_create_an_common_values_with_the_owner_on_the_asset() throws Exception {
		BaseOrg owner = PrimaryOrgBuilder.aPrimaryOrg().build();
		Product asset = ProductBuilder.aProduct().withOwner(owner).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.owner, sameInstance(owner)); 
	}
	
	@Test
	public void should_create_an_common_values_with_the_product_status_on_the_asset() throws Exception {
		ProductStatusBean productStatus = new ProductStatusBean();
		productStatus.setUniqueID(1L);
		Product asset = ProductBuilder.aProduct().havingStatus(productStatus).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.productStatus, equalTo(productStatus)); 
	}

}
