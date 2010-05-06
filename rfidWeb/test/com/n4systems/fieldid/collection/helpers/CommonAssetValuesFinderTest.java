package com.n4systems.fieldid.collection.helpers;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.Product;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.test.helpers.FluentArrayList;


public class CommonAssetValuesFinderTest {

	private ProductStatusBean productStatus;

	
	

	public CommonAssetValuesFinderTest() {
		super();
		productStatus = new ProductStatusBean();
		productStatus.setUniqueID(1L);
	}


	@Test
	public void should_find_no_common_values_with_a_list_that_is_empty() throws Exception {
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new ArrayList<Product>());
		assertThat(sut.findCommonValues(), equalTo(CommonAssetValues.NO_COMMON_VALUES));
	}
	
	
	@Test
	public void should_find_all_the_common_values_of_the_asset_when_there_is_only_one_asset() throws Exception {
		Product asset = ProductBuilder.aProduct().withOwner(aPrimaryOrg().build()).inLocation("location").havingStatus(productStatus).build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Product>(asset));
		
		
		CommonAssetValues expectedCommonValues = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}
	
	
	@Test
	public void should_find_all_the_common_values_of_the_asset_when_there_is_multiple_assets_that_have_the_same_field_values() throws Exception {
		
		ProductBuilder builder = ProductBuilder.aProduct().withOwner(aPrimaryOrg().build()).inLocation("location").havingStatus(productStatus);
		Product asset1 = builder.build();
		Product asset2 = builder.build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Product>(asset1, asset2));
		
		
		CommonAssetValues expectedCommonValues = CommonAssetValues.createFrom(asset1);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}

	@Test
	public void should_find_only_the_product_status_field_is_common_value_when_multiple_assets_only_have_the_product_status_in_common() throws Exception {
		
		ProductBuilder builder = ProductBuilder.aProduct().havingStatus(productStatus);
		Product asset1 = builder.withOwner(aPrimaryOrg().build()).inLocation("location1").build();
		Product asset2 = builder.withOwner(aPrimaryOrg().build()).inLocation("location2").build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Product>(asset1, asset2));
		
		
		CommonAssetValues expectedCommonValues = new CommonAssetValues(null, null, productStatus);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}
	
	@Test
	public void should_find_only_the_location_field_is_common_value_when_multiple_assets_only_have_the_location_in_common() throws Exception {
		
		ProductBuilder builder = ProductBuilder.aProduct().inLocation("location1");
		Product asset1 = builder.withOwner(aPrimaryOrg().build()).havingStatus(null).build();
		Product asset2 = builder.withOwner(aPrimaryOrg().build()).havingStatus(productStatus).build();
		Product asset3 = builder.withOwner(aPrimaryOrg().build()).havingStatus(null).build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Product>(asset1, asset2, asset3));
		
		
		CommonAssetValues expectedCommonValues = new CommonAssetValues("location1", null, null);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}
	
}
