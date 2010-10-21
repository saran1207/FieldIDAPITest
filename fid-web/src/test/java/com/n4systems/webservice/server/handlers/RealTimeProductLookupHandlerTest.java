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

import com.n4systems.model.SubProduct;
import com.n4systems.model.product.ProductSubProductsLoader;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;

public class RealTimeProductLookupHandlerTest {
	
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
			anAsset().withModifiedDate(moreRecentDate).withOneSubProduct().build());
		
		listOfMultipleProductsWithVariedSubAssets = new FluentArrayList<Asset>(
				anAsset().withTwoSubProducts().build(), anAsset().withOneSubProduct().build());
	}

	@Test
	public void test_multiple_products_found_and_no_modified_date_return_all_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProducts(), getSubProductLoaderMock(listOfMultipleAssets));
		assertEquals(listOfMultipleAssets, handler.setSearchText("some text").setModified(null).lookup());
	}
	
	@Test
	public void test_multiple_products_found_and_send_modified_date_return_all_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProducts(), getSubProductLoaderMock(listOfMultipleAssets));
		assertEquals(listOfMultipleAssets, handler.setSearchText("some text").setModified(olderDate).lookup());
	}
	
	@Test
	public void test_single_product_found_modified_date_not_sent_return_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(listWithSingleAsset, handler.setSearchText("some text").setModified(null).lookup());
	}
	
	@Test
	public void test_single_product_found_modified_date_older_than_products_return_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(listWithSingleAsset, handler.setSearchText("some text").setModified(olderDate).lookup());
	}
	
	@Test
	public void test_single_product_found_modified_date_equals_products_return_empty_list() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(0, handler.setSearchText("some text").setModified(moreRecentDate).lookup().size());						
	}

	@Test
	public void test_single_product_found_modified_date_more_recent_than_products_return_empty_list() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleAsset));
		assertEquals(0, handler.setSearchText("some text").setModified(mostRecentDate).lookup().size());								
	}
	
	@Test
	public void test_single_product_found_with_one_sub_product_modified_date_not_sent_return_product_and_sub_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsSingleProductWithOneSubProduct(), getSubProductLoaderMock(listWithSingleProductWithOneSubAsset));
		
		List<Asset> expectedProductsReturned = getExpectedProductsReturned(listWithSingleProductWithOneSubAsset);
		
		List<Asset> actualProductsReturned = handler.setSearchText("some text").setModified(null).lookup();
		
		assertEquals(expectedProductsReturned.size(), actualProductsReturned.size());

		for (Asset asset : expectedProductsReturned) {
			assertTrue(actualProductsReturned.contains(asset));
		}
	}
	
	@Test
	public void test_multiple_products_found_with_varied_sub_products_returns_products_and_all_sub_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProductWithVariedSubProducts(), getSubProductLoaderMock(listOfMultipleProductsWithVariedSubAssets));
		
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
			for (SubProduct subProduct : asset.getSubProducts()) {
				expectedProductsReturned.add(subProduct.getAsset());
			}
			
			expectedProductsReturned.add(asset);
		}
		
		return expectedProductsReturned;
	}
	
	@SuppressWarnings("unchecked")
	private ProductSubProductsLoader getSubProductLoaderMock(List<Asset> productsToReturn) {
		ProductSubProductsLoader subProductsLoaderMock = createMock(ProductSubProductsLoader.class);
		expect(subProductsLoaderMock.setProducts((List<Asset>)anyObject())).andReturn(subProductsLoaderMock);
		expect(subProductsLoaderMock.load()).andReturn(productsToReturn);
		replay(subProductsLoaderMock);
		return subProductsLoaderMock;
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsMultipleProductWithVariedSubProducts() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listOfMultipleProductsWithVariedSubAssets;
			}
		};
		return loader;				
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsSingleProductWithOneSubProduct() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listWithSingleProductWithOneSubAsset;
			}
		};
		return loader;				
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsMultipleProducts() {		
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listOfMultipleAssets;
			}
		};
		return loader;
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsListWithSingleProduct() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Asset> load() {
				return listWithSingleAsset;
			}
		};
		return loader;		
	}
	
}
