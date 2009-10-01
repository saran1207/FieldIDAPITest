package com.n4systems.model.product;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Product;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.builders.UserBuilder;

public class ProductSaverTest {

	@Test(expected=NotImplementedException.class)
	public void remove_throws_exception() {
		(new ProductSaver()).remove((EntityManager)null, null);
	}
	
	@Test
	public void save_calls_update() {
		class TestProductSaver extends ProductSaver {
			public boolean update_called = false;
			@Override
			public Product update(EntityManager em, Product product) {
				update_called = true;
				return product;
			}
		};
		
		TestProductSaver saver = new TestProductSaver();
		saver.save((EntityManager)null, null);
		
		assertTrue(saver.update_called);
	}
	
	@Test
	public void update_sets_modifiedby_and_resaves_product() {
		Product product = ProductBuilder.aProduct().build();
		UserBean modifiedBy = UserBuilder.anEmployee().build();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(product)).andReturn(product);
		expect(em.merge(product)).andReturn(product);
		replay(em);
		
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		saver.save(em, product);
		
		assertEquals(modifiedBy, product.getModifiedBy());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void update_does_not_resave_product_with_not_null_networkid() {
		class TestProduct extends Product {
			public TestProduct(Long id) {
				super();
				this.id = id;
				onCreate();
			}
		};
		
		Product product = new TestProduct(100L);
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(product)).andReturn(product);
		replay(em);
		
		ProductSaver saver = new ProductSaver();
		saver.save(em, product);
	}
	
}