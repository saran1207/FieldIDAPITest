package com.n4systems.services.product;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.model.Asset;
import org.junit.Before;
import org.junit.Test;


import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.user.User;






public class ProductSaveServiceTest {

	private User user;
	
	@Before
	public void setUp() {
		user = anEmployee().build();
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_create_on_no_product() {
		ProductSaveService sut = new ProductSaveService(null, user);
		sut.create();
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_update_on_no_product() {
		ProductSaveService sut = new ProductSaveService(null, user);
		sut.update();
	}
	
	@Test
	public void should_create_product_with_history_no_files() {
		Asset asset = anAsset().generate();
		Asset expectedAsset = anAsset().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.createWithHistory(asset, user)).andReturn(expectedAsset);
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(asset);
		
		Asset actualAsset = sut.create();
		
		assertEquals(expectedAsset, actualAsset);
		verify(mockProductManager);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_handle_unique_sub_product_exception_durning_create_product_with_history_no_files() {
		Asset asset = anAsset().generate();
		Asset expectedAsset = anAsset().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.createWithHistory(asset, user)).andThrow(new SubProductUniquenessException());
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(asset);
		
		Asset actualAsset = sut.create();
		
		assertEquals(expectedAsset, actualAsset);
		verify(mockProductManager);
	}
	
	
	@Test
	public void should_update_product_no_files() {
		Asset asset = anAsset().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.update(asset, user)).andReturn(asset);
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(asset);
		
		Asset actualAsset = sut.update();
		
		assertEquals(asset, actualAsset);
		verify(mockProductManager);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_handle_unique_sub_product_exception_durning_update_product_no_files() {
		Asset asset = anAsset().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.update(asset, user)).andThrow(new SubProductUniquenessException());
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(asset);
		
		Asset actualAsset = sut.update();
		
		assertEquals(asset, actualAsset);
		verify(mockProductManager);
	}
	
	@Test
	public void save_without_history_does_not_create_history() throws SubProductUniquenessException {
		Asset asset = anAsset().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		
		expect(mockProductManager.create(asset, user)).andReturn(asset);
		replay(mockProductManager);
		
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(asset);
		
		sut.createWithoutHistory();
		
		verify(mockProductManager);
	}
	
	
}
