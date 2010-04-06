package com.n4systems.services.product;

import static com.n4systems.model.builders.ProductBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.model.Product;






public class ProductSaveServiceTest {

	private UserBean user;
	
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
		Product product = aProduct().generate();
		Product expectedProduct = aProduct().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.createWithHistory(product, user)).andReturn(expectedProduct);
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(product);
		
		Product actualProduct = sut.create();
		
		assertEquals(expectedProduct, actualProduct);
		verify(mockProductManager);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_handle_unique_sub_product_exception_durning_create_product_with_history_no_files() {
		Product product = aProduct().generate();
		Product expectedProduct = aProduct().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.createWithHistory(product, user)).andThrow(new SubProductUniquenessException());
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(product);
		
		Product actualProduct = sut.create();
		
		assertEquals(expectedProduct, actualProduct);
		verify(mockProductManager);
	}
	
	
	@Test
	public void should_update_product_no_files() {
		Product product = aProduct().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.update(product, user)).andReturn(product);
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(product);
		
		Product actualProduct = sut.update();
		
		assertEquals(product, actualProduct);
		verify(mockProductManager);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_handle_unique_sub_product_exception_durning_update_product_no_files() {
		Product product = aProduct().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		try {
			expect(mockProductManager.update(product, user)).andThrow(new SubProductUniquenessException());
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockProductManager);
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(product);
		
		Product actualProduct = sut.update();
		
		assertEquals(product, actualProduct);
		verify(mockProductManager);
	}
	
	@Test
	public void save_without_history_does_not_create_history() throws SubProductUniquenessException {
		Product product = aProduct().build();
		
		LegacyProductSerial mockProductManager = createMock(LegacyProductSerial.class);
		
		expect(mockProductManager.create(product, user)).andReturn(product);
		replay(mockProductManager);
		
		ProductSaveService sut = new ProductSaveService(mockProductManager, user);
		sut.setProduct(product);
		
		sut.createWithoutHistory();
		
		verify(mockProductManager);
	}
	
	
}
