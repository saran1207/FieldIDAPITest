package com.n4systems.fieldid.collection.helpers;

import static com.n4systems.fieldid.collection.helpers.CommonAssetValues.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import com.n4systems.model.Asset;
import com.n4systems.model.builders.AssetBuilder;
import org.junit.Test;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.model.location.Location;
import com.n4systems.test.helpers.FluentArrayList;


public class CommonAssetValuesFinderTest {

	private AssetStatus assetStatus;

	
	

	public CommonAssetValuesFinderTest() {
		super();
		assetStatus = new AssetStatus();
		assetStatus.setUniqueID(1L);
	}


	@Test
	public void should_find_no_common_values_with_a_list_that_is_empty() throws Exception {
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new ArrayList<Asset>());
		assertThat(sut.findCommonValues(), equalTo(CommonAssetValues.NO_COMMON_VALUES));
	}
	
	
	@Test
	public void should_find_all_the_common_values_of_the_asset_when_there_is_only_one_asset() throws Exception {
		Asset asset = AssetBuilder.anAsset().withOwner(aPrimaryOrg().build()).inFreeformLocation("location").havingStatus(assetStatus).build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Asset>(asset));
		
		
		CommonAssetValues expectedCommonValues = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}
	
	
	@Test
	public void should_find_all_the_common_values_of_the_asset_when_there_is_multiple_assets_that_have_the_same_field_values() throws Exception {
		
		AssetBuilder builder = AssetBuilder.anAsset().withOwner(aPrimaryOrg().build()).inFreeformLocation("location").havingStatus(assetStatus);
		Asset asset1 = builder.build();
		Asset asset2 = builder.build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Asset>(asset1, asset2));
		
		
		CommonAssetValues expectedCommonValues = CommonAssetValues.createFrom(asset1);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}

	@Test
	public void should_find_only_the_product_status_field_is_common_value_when_multiple_assets_only_have_the_product_status_in_common() throws Exception {
		
		AssetBuilder builder = AssetBuilder.anAsset().havingStatus(assetStatus);
		Asset asset1 = builder.withOwner(aPrimaryOrg().build()).inFreeformLocation("location1").build();
		Asset asset2 = builder.withOwner(aPrimaryOrg().build()).inFreeformLocation("location2").assignedTo(anEmployee().build()).build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Asset>(asset1, asset2));
		
		
		CommonAssetValues expectedCommonValues = new CommonAssetValues(NO_COMMON_LOCATION, NO_COMMON_OWNER, assetStatus, NO_COMMON_ASSIGNMENT);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}
	
	@Test
	public void should_find_only_the_location_field_is_common_value_when_multiple_assets_only_have_the_location_in_common() throws Exception {
		
		AssetBuilder builder = AssetBuilder.anAsset().withAdvancedLocation(new Location(null, "location1")).assignedTo(null);
		Asset asset1 = builder.withOwner(aPrimaryOrg().build()).havingStatus(null).build();
		Asset asset2 = builder.withOwner(aPrimaryOrg().build()).havingStatus(assetStatus).build();
		Asset asset3 = builder.withOwner(aPrimaryOrg().build()).havingStatus(null).assignedTo(anEmployee().build()).build();
		
		CommonAssetValuesFinder sut = new CommonAssetValuesFinder(new FluentArrayList<Asset>(asset1, asset2, asset3));
		
		
		CommonAssetValues expectedCommonValues = new CommonAssetValues(Location.onlyFreeformLocation("location1"), NO_COMMON_OWNER, NO_COMMON_ASSET_STATUS, NO_COMMON_ASSIGNMENT);
		
		assertThat(sut.findCommonValues(), equalTo(expectedCommonValues));
	}
	
	
	
	
	
	
}
