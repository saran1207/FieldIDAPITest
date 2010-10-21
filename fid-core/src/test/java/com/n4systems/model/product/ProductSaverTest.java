package com.n4systems.model.product;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.builders.AssetBuilder;
import org.junit.Test;


import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.builders.UserBuilder;
import com.n4systems.model.user.User;

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
			public Asset update(EntityManager em, Asset product) {
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
		Asset product = AssetBuilder.anAsset().build();
		User modifiedBy = UserBuilder.anEmployee().build();
		
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
		class TestAsset extends Asset {
			public TestAsset(Long id) {
				super();
				this.id = id;
				onCreate();
			}
		};
		
		Asset product = new TestAsset(100L);
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(product)).andReturn(product);
		replay(em);
		
		ProductSaver saver = new ProductSaver();
		saver.save(em, product);
	}
	
	@Test
	public void update_forces_network_recalc_on_linked_product_change() {
		Asset product = AssetBuilder.anAsset().build();
		product.setLinkedAsset(AssetBuilder.anAsset().build());
		
		// we don't really need to test there here, but it may be confusing, if this test 
		// started failing because this was returning false
		assertTrue("This test is fine, it's the asset that's broken", product.linkedAssetHasChanged());
		
		final List<Asset> linkedAssets = Arrays.asList(AssetBuilder.anAsset().build(), AssetBuilder.anAsset().build(), AssetBuilder.anAsset().build());
		
		RecursiveLinkedChildProductLoader loader = new RecursiveLinkedChildProductLoader() {
			@Override
			protected List<Asset> load(EntityManager em) {
				return linkedAssets;
			}
		};
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(product)).andReturn(product);
		expect(em.merge(product)).andReturn(product);
		expect(em.merge(linkedAssets.get(0))).andReturn(linkedAssets.get(0));
		expect(em.merge(linkedAssets.get(1))).andReturn(linkedAssets.get(1));
		expect(em.merge(linkedAssets.get(2))).andReturn(linkedAssets.get(2));
		replay(em);


		ProductSaver saver = new ProductSaver(loader);		
		saver.save(em, product);
		
	}
	
}