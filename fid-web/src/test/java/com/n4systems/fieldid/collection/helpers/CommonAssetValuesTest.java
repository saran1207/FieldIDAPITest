package com.n4systems.fieldid.collection.helpers;

import static com.n4systems.model.builders.PredefinedLocationBuilder.*;
import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;


import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;


public class CommonAssetValuesTest {
	
	
	@Test
	public void should_create_a_common_values_with_the_location_on_the_asset() throws Exception {
		Asset asset = anAsset().withAdvancedLocation(Location.onlyFreeformLocation("location")).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.location, equalTo(Location.onlyFreeformLocation("location"))); 
	}
	
	
	
	
	
	
	@Test
	public void should_create_a_common_values_with_the_owner_on_the_asset() throws Exception {
		BaseOrg owner = aPrimaryOrg().build();
		Asset asset = anAsset().withOwner(owner).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.owner, sameInstance(owner)); 
	}
	
	@Test
	public void should_create_a_common_values_with_the_asset_status_on_the_asset() throws Exception {
		AssetStatus assetStatus = new AssetStatus();
		assetStatus.setId(1L);
		Asset asset = anAsset().havingStatus(assetStatus).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.assetStatus, equalTo(assetStatus));
	}
	
	@Test
	public void should_create_a_common_values_with_the_assignment_as_a_person() throws Exception {
		User assignedUser = anEmployee().build();
		Asset asset = anAsset().assignedTo(assignedUser).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		assertThat(sut.assignment, equalTo(new Assignment(assignedUser)));
	}
	
	
	
	
	
	
	@Test
	public void should_find_that_2_assets_with_the_same_predefined_location_but_different_free_form_location_have_no_location_in_common() throws Exception {
		PredefinedLocation predefinedLocation = aPredefinedLocation().build();
		
		Asset asset = anAsset().withAdvancedLocation(new Location(predefinedLocation, "location")).build();
		Asset asset2 = anAsset().withAdvancedLocation(new Location(predefinedLocation, "different location")).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		CommonAssetValues resultingCommonValues = sut.findCommon(asset2);
		
		assertThat(resultingCommonValues, hasNoCommonLocation());
	
		
	}

	
	@Test
	public void should_find_that_2_assets_with_the_same_predefined_location_free_form_location_have_the_location_in_common() throws Exception {
		PredefinedLocation predefinedLocation = aPredefinedLocation().build();
		
		Asset asset = anAsset().withAdvancedLocation(new Location(predefinedLocation, "location")).build();
		Asset asset2 = anAsset().withAdvancedLocation(new Location(predefinedLocation, "location")).build();
		
		CommonAssetValues sut = CommonAssetValues.createFrom(asset);
		
		CommonAssetValues resultingCommonValues = sut.findCommon(asset2);
		
		assertThat(resultingCommonValues, hasCommonLocation(new Location(predefinedLocation, "location")));
		
	}



	private Matcher<CommonAssetValues> hasCommonLocation(final Location location) {
		return new TypeSafeMatcher<CommonAssetValues>() {

			@Override
			public boolean matchesSafely(CommonAssetValues item) {
				return location.equals(item.location);
			}

			public void describeTo(Description arg0) {
				arg0.appendText("a common location of ").appendValue(location);
				
			}
			
		};
	}






	private Matcher<CommonAssetValues> hasNoCommonLocation() {
		return new TypeSafeMatcher<CommonAssetValues>() {

			@Override
			public boolean matchesSafely(CommonAssetValues item) {
				return !item.hasCommonLocation();
			}

			public void describeTo(Description arg0) {
				arg0.appendText("a common asset values containing no common location");
				
			}
			
		};
	}
	
	
	


}
