package com.n4systems.webservice.server.handlers;

import static com.n4systems.model.builders.ProductBuilder.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.product.ProductSubProductsLoader;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;

public class RealTimeProductLookupHandlerTest {
	
	private List<Product> listOfMultipleProducts;
	private List<Product> listWithSingleProduct;
	private List<Product> listWithSingleProductWithOneSubProduct;
	private List<Product> listOfMultipleProductsWithVariedSubProducts;
	private Date olderDate;
	private Date moreRecentDate;
	private Date mostRecentDate;
	
	@Before
	public void setup() {
		olderDate = DateHelper.createDate(2009, 1, 1);
		moreRecentDate = DateHelper.createDate(2009, 11, 11);
		mostRecentDate = DateHelper.createDate(2009, 11, 12);
		
		listOfMultipleProducts = new FluentArrayList<Product>(
				aProduct().build(), aProduct().build());
		
		listWithSingleProduct = new FluentArrayList<Product> (
				aProduct().withModifiedDate(moreRecentDate).build());
		
		
		listWithSingleProductWithOneSubProduct = new FluentArrayList<Product> (
			aProduct().withModifiedDate(moreRecentDate).withOneSubProduct().build());
		
		listOfMultipleProductsWithVariedSubProducts = new FluentArrayList<Product>(
				aProduct().withTwoSubProducts().build(), aProduct().withOneSubProduct().build());
	}

	@Test
	public void test_multiple_products_found_and_no_modified_date_return_all_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProducts(), getSubProductLoaderMock(listOfMultipleProducts));
		assertEquals(listOfMultipleProducts, handler.setSearchText("some text").setModified(null).lookup());
	}
	
	@Test
	public void test_multiple_products_found_and_send_modified_date_return_all_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProducts(), getSubProductLoaderMock(listOfMultipleProducts));
		assertEquals(listOfMultipleProducts, handler.setSearchText("some text").setModified(olderDate).lookup());		
	}
	
	@Test
	public void test_single_product_found_modified_date_not_sent_return_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleProduct));
		assertEquals(listWithSingleProduct, handler.setSearchText("some text").setModified(null).lookup());				
	}
	
	@Test
	public void test_single_product_found_modified_date_older_than_products_return_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleProduct));
		assertEquals(listWithSingleProduct, handler.setSearchText("some text").setModified(olderDate).lookup());				
	}
	
	@Test
	public void test_single_product_found_modified_date_equals_products_return_empty_list() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleProduct));
		assertEquals(0, handler.setSearchText("some text").setModified(moreRecentDate).lookup().size());						
	}

	@Test
	public void test_single_product_found_modified_date_more_recent_than_products_return_empty_list() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct(), getSubProductLoaderMock(listWithSingleProduct));
		assertEquals(0, handler.setSearchText("some text").setModified(mostRecentDate).lookup().size());								
	}
	
	@Test
	public void test_single_product_found_with_one_sub_product_modified_date_not_sent_return_product_and_sub_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsSingleProductWithOneSubProduct(), getSubProductLoaderMock(listWithSingleProductWithOneSubProduct));
		
		List<Product> expectedProductsReturned = getExpectedProductsReturned(listWithSingleProductWithOneSubProduct);
		
		List<Product> actualProductsReturned = handler.setSearchText("some text").setModified(null).lookup();
		
		assertEquals(expectedProductsReturned.size(), actualProductsReturned.size());

		for (Product product : expectedProductsReturned) {
			assertTrue(actualProductsReturned.contains(product));
		}
	}
	
	@Test
	public void test_multiple_products_found_with_varied_sub_products_returns_products_and_all_sub_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProductWithVariedSubProducts(), getSubProductLoaderMock(listOfMultipleProductsWithVariedSubProducts));
		
		List<Product> expectedProductsReturned = getExpectedProductsReturned(listOfMultipleProductsWithVariedSubProducts);
		
		List<Product> actualProductsReturned = handler.setSearchText("some text").setModified(null).lookup();
		
		assertEquals(expectedProductsReturned.size(), actualProductsReturned.size());

		for (Product product : expectedProductsReturned) {
			assertTrue(actualProductsReturned.contains(product));
		}		
	}
	
	private List<Product> getExpectedProductsReturned(List<Product> products) {
		List<Product> expectedProductsReturned = new ArrayList<Product>();
		for (Product product : products) {
			for (SubProduct subProduct : product.getSubProducts()) {
				expectedProductsReturned.add(subProduct.getProduct());
			}
			
			expectedProductsReturned.add(product);
		}
		
		return expectedProductsReturned;
	}
	
	@SuppressWarnings("unchecked")
	private ProductSubProductsLoader getSubProductLoaderMock(List<Product> productsToReturn) {
		ProductSubProductsLoader subProductsLoaderMock = createMock(ProductSubProductsLoader.class);
		expect(subProductsLoaderMock.setProducts((List<Product>)anyObject())).andReturn(subProductsLoaderMock);
		expect(subProductsLoaderMock.load()).andReturn(productsToReturn);
		replay(subProductsLoaderMock);
		return subProductsLoaderMock;
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsMultipleProductWithVariedSubProducts() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Product> load() {
				return listOfMultipleProductsWithVariedSubProducts;
			}
		};
		return loader;				
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsSingleProductWithOneSubProduct() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Product> load() {
				return listWithSingleProductWithOneSubProduct;
			}
		};
		return loader;				
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsMultipleProducts() {		
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Product> load() {
				return listOfMultipleProducts;
			}
		};
		return loader;
	}
	
	private SmartSearchLoader getSmartSearchLoaderThatReturnsListWithSingleProduct() {
		SmartSearchLoader loader = new SmartSearchLoader(new TenantOnlySecurityFilter(1L)) {
			@Override
			public List<Product> load() {
				return listWithSingleProduct;
			}
		};
		return loader;		
	}
	
}
