package com.n4systems.webservice.server.handlers;

import static com.n4systems.model.builders.ProductBuilder.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.Product;
import com.n4systems.model.product.SmartSearchLoader;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.test.helpers.DateHelper;
import com.n4systems.test.helpers.FluentArrayList;

public class RealTimeProductLookupHandlerTest {
	
	private List<Product> listOfMultipleProducts;
	private List<Product> listWithSingleProduct;
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
		
	}

	@Test
	public void test_multiple_products_found_and_no_modified_date_return_all_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProducts());
		assertEquals(listOfMultipleProducts, handler.setSearchText("some text").setModified(null).lookup());
	}
	
	@Test
	public void test_multiple_products_found_and_send_modified_date_return_all_products() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsMultipleProducts());
		assertEquals(listOfMultipleProducts, handler.setSearchText("some text").setModified(olderDate).lookup());		
	}
	
	@Test
	public void test_single_product_found_modified_date_not_sent_return_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct());
		assertEquals(listWithSingleProduct, handler.setSearchText("some text").setModified(null).lookup());				
	}
	
	@Test
	public void test_single_product_found_modified_date_older_than_products_return_product() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct());
		assertEquals(listWithSingleProduct, handler.setSearchText("some text").setModified(olderDate).lookup());				
	}
	
	@Test
	public void test_single_product_found_modified_date_equals_products_return_empty_list() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct());
		assertEquals(0, handler.setSearchText("some text").setModified(moreRecentDate).lookup().size());						
	}

	@Test
	public void test_single_product_found_modified_date_more_recent_than_products_return_empty_list() {
		RealTimeProductLookupHandler handler = new RealTimeProductLookupHandler(getSmartSearchLoaderThatReturnsListWithSingleProduct());
		assertEquals(0, handler.setSearchText("some text").setModified(mostRecentDate).lookup().size());								
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
