package com.n4systems.webservice.server.handlers;

import static com.n4systems.model.builders.AssetBuilder.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.model.Asset;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.SubAsset;
import com.n4systems.model.asset.AssetSubAssetsLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;

public class RealTimeAssetLookupHandlerTest {
	
	private List<Asset> listOfMultipleAssets;
	private List<Asset> listWithSingleAsset;
	private List<Asset> listWithSingleProductWithOneSubAsset;
	private List<Asset> listOfMultipleProductsWithVariedSubAssets;
	private Date olderDate;
	private Date moreRecentDate;
	private Date mostRecentDate;
	
	@Before
	public void setup() {
		olderDate = DateHelper.createDate(2009, 1, 1);
		moreRecentDate = DateHelper.createDate(2009, 11, 11);
		mostRecentDate = DateHelper.createDate(2009, 11, 12);
		
		listOfMultipleAssets = new FluentArrayList<Asset>(
				anAsset().build(), anAsset().build());
		
		listWithSingleAsset = new FluentArrayList<Asset> (
				anAsset().withModifiedDate(moreRecentDate).build());
		
		
		listWithSingleProductWithOneSubAsset = new FluentArrayList<Asset> (
			anAsset().withModifiedDate(moreRecentDate).withOneSubAsset().build());
		
		listOfMultipleProductsWithVariedSubAssets = new FluentArrayList<Asset>(
				anAsset().withTwoSubAssets().build(), anAsset().withOneSubAsset().build());
	}

	@Test
	public void test_multiple_assets_found_and_no_modified_date_return_all_assets() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsMultipleAssets(), getSubProductLoaderMock(listOfMultipleAssets));
		assertEquals(listOfMultipleAssets, handler.setSearchText("some text").setModified(null).lookup());
	}
	
	@Test
	public void test_multiple_assets_found_and_send_modified_date_return_all_assets() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsMultipleAssets(), getSubProductLoaderMock(listOfMultipleAssets));
		assertEquals(listOfMultipleAssets, handler.setSearchText("some text").setModified(olderDate).lookup());
	}
	
	@Test
	public void test_single_asset_found_modified_date_not_sent_return_asset() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleAsset(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(listWithSingleAsset, handler.setSearchText("some text").setModified(null).lookup());
	}
	
	@Test
	public void test_single_asset_found_modified_date_older_than_assets_return_asset() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleAsset(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(listWithSingleAsset, handler.setSearchText("some text").setModified(olderDate).lookup());
	}
	
	@Test
	public void test_single_asset_found_modified_date_equals_assets_return_empty_list() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleAsset(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(0, handler.setSearchText("some text").setModified(moreRecentDate).lookup().size());						
	}

	@Test
	public void test_single_asset_found_modified_date_more_recent_than_assets_return_empty_list() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleAsset(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(0, handler.setSearchText("some text").setModified(mostRecentDate).lookup().size());								
	}
	
	@Test
	public void test_single_asset_found_with_one_sub_asset_modified_date_not_sent_return_asset_and_sub_asset() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsSingleProductWithOneSubAsset(), getSubProductLoaderMock(listWithSingleProductWithOneSubAsset));
		
		List<Asset> expectedProductsReturned = getExpectedProductsReturned(listWithSingleProductWithOneSubAsset);
		
		List<Asset> actualProductsReturned = handler.setSearchText("some text").setModified(null).lookup();
		
		assertEquals(expectedProductsReturned.size(), actualProductsReturned.size());

		for (Asset asset : expectedProductsReturned) {
			assertTrue(actualProductsReturned.contains(asset));
		}
	}
	
	@Test
	public void test_multiple_assets_found_with_varied_sub_assets_returns_assets_and_all_sub_assets() {
		RealTimeAssetLookupHandler handler = new RealTimeAssetLookupHandler(getSmartSearchLoaderThatReturnsMultipleProductWithVariedSubAssets(), getSubProductLoaderMock(listOfMultipleProductsWithVariedSubAssets));
		
		List<Asset> expectedProductsReturned = getExpectedProductsReturned(listOfMultipleProductsWithVariedSubAssets);
		
		List<Asset> actualProductsReturned = handler.setSearchText("some text").setModified(null).lookup();
		
		assertEquals(expectedProductsReturned.size(), actualProductsReturned.size());

		for (Asset asset : expectedProductsReturned) {
			assertTrue(actualProductsReturned.contains(asset));
		}		
	}
	
	private List<Asset> getExpectedProductsReturned(List<Asset> assets) {
		List<Asset> expectedProductsReturned = new ArrayList<Asset>();
		for (Asset asset : assets) {
			for (SubAsset subAsset : asset.getSubAssets()) {
				expectedProductsReturned.add(subAsset.getAsset());
			}
			
			expectedProductsReturned.add(asset);
		}
		
		return expectedProductsReturned;
	}
	
	@SuppressWarnings("unchecked")
	private AssetSubAssetsLoader getSubProductLoaderMock(List<Asset> assetsToReturn) {
		AssetSubAssetsLoader subAssetsLoaderMock = createMock(AssetSubAssetsLoader.class);
		expect(subAssetsLoaderMock.setAssets((List<Asset>)anyObject())).andReturn(subAssetsLoaderMock);
		expect(subAssetsLoaderMock.load()).andReturn(assetsToReturn);
		replay(subAssetsLoaderMock);
		return subAssetsLoaderMock;
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsMultipleProductWithVariedSubAssets() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listOfMultipleProductsWithVariedSubAssets;
			}
		};
		return loader;				
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsSingleProductWithOneSubAsset() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listWithSingleProductWithOneSubAsset;
			}
		};
		return loader;				
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsMultipleAssets() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listOfMultipleAssets;
			}
		};
		return loader;
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsListWithSingleAsset() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listWithSingleAsset;
			}
		};
		return loader;		
	}
	
}
