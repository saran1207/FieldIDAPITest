package com.n4systems.model.asset;

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

public class AssetSaverTest {

	@Test(expected=NotImplementedException.class)
	public void remove_throws_exception() {
		(new AssetSaver()).remove((EntityManager)null, null);
	}
	
	@Test
	public void save_calls_update() {
		class TestAssetSaver extends AssetSaver {
			public boolean update_called = false;
			@Override
			public Asset update(EntityManager em, Asset asset) {
				update_called = true;
				return asset;
			}
		};
		
		TestAssetSaver saver = new TestAssetSaver();
		saver.save((EntityManager)null, null);
		
		assertTrue(saver.update_called);
	}
	
	@Test
	public void update_sets_modifiedby_and_resaves_asset() {
		Asset asset = AssetBuilder.anAsset().build();
		User modifiedBy = UserBuilder.anEmployee().build();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(asset)).andReturn(asset);
		expect(em.merge(asset)).andReturn(asset);
		replay(em);
		
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		saver.save(em, asset);
		
		assertEquals(modifiedBy, asset.getModifiedBy());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void update_does_not_resave_asset_with_not_null_networkid() {
		class TestAsset extends Asset {
			public TestAsset(Long id) {
				super();
				this.id = id;
				onCreate();
			}
		};
		
		Asset asset = new TestAsset(100L);
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(asset)).andReturn(asset);
		replay(em);
		
		AssetSaver saver = new AssetSaver();
		saver.save(em, asset);
	}
	
	@Test
	public void update_forces_network_recalc_on_linked_asset_change() {
		Asset asset = AssetBuilder.anAsset().build();
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		
		// we don't really need to test there here, but it may be confusing, if this test 
		// started failing because this was returning false
		assertTrue("This test is fine, it's the asset that's broken", asset.linkedAssetHasChanged());
		
		final List<Asset> linkedAssets = Arrays.asList(AssetBuilder.anAsset().build(), AssetBuilder.anAsset().build(), AssetBuilder.anAsset().build());
		
		RecursiveLinkedChildAssetLoader loader = new RecursiveLinkedChildAssetLoader() {
			@Override
			protected List<Asset> load(EntityManager em) {
				return linkedAssets;
			}
		};
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(asset)).andReturn(asset);
		expect(em.merge(asset)).andReturn(asset);
		expect(em.merge(linkedAssets.get(0))).andReturn(linkedAssets.get(0));
		expect(em.merge(linkedAssets.get(1))).andReturn(linkedAssets.get(1));
		expect(em.merge(linkedAssets.get(2))).andReturn(linkedAssets.get(2));
		replay(em);


		AssetSaver saver = new AssetSaver(loader);
		saver.save(em, asset);
		
	}
	
}