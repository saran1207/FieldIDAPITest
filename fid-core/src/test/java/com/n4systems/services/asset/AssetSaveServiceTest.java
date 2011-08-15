package com.n4systems.services.asset;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.model.Asset;
import com.n4systems.model.Tenant;

import org.junit.Before;
import org.junit.Test;


import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.user.User;

public class AssetSaveServiceTest {

	private User user;
	
	@Before
	public void setUp() {
		user = anEmployee().build();
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_create_on_no_asset() {
		AssetSaveService sut = new AssetSaveService(null, user);
		sut.create();
	}
	
	@Test(expected=InvalidArgumentException.class)
	public void should_not_allow_update_on_no_asset() {
		AssetSaveService sut = new AssetSaveService(null, user);
		sut.update();
	}
	
	@Test
	public void should_create_asset_with_history_no_files() {
		Tenant tenant = n4();
		Asset asset = anAsset().forTenant(tenant).generate();
		
		LegacyAsset mockAssetManager = createMock(LegacyAsset.class);
		try {
			expect(mockAssetManager.createWithHistory(asset, user)).andReturn(asset);
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockAssetManager);
		AssetSaveService sut = new AssetSaveService(mockAssetManager, user);
		sut.setAsset(asset);
		
		Asset actualAsset = sut.create();
		
		assertEquals(asset, actualAsset);
		verify(mockAssetManager);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_handle_unique_sub_asset_exception_durning_create_asset_with_history_no_files() {
		Asset asset = anAsset().generate();
		Asset expectedAsset = anAsset().build();
		
		LegacyAsset mockAssetManager = createMock(LegacyAsset.class);
		try {
			expect(mockAssetManager.createWithHistory(asset, user)).andThrow(new SubAssetUniquenessException());
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockAssetManager);
		AssetSaveService sut = new AssetSaveService(mockAssetManager, user);
		sut.setAsset(asset);
		
		Asset actualAsset = sut.create();
		
		assertEquals(expectedAsset, actualAsset);
		verify(mockAssetManager);
	}
	
	
	@Test
	public void should_update_asset_no_files() {
		Tenant tenant = n4();
		Asset asset = anAsset().forTenant(tenant).generate();
		
		LegacyAsset mockAssetManager = createMock(LegacyAsset.class);
		try {
			expect(mockAssetManager.update(asset, user)).andReturn(asset);
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockAssetManager);
		AssetSaveService sut = new AssetSaveService(mockAssetManager, user);
		sut.setAsset(asset);
		
		Asset actualAsset = sut.update();
		
		assertEquals(asset, actualAsset);
		verify(mockAssetManager);
	}
	
	@Test(expected=ProcessFailureException.class)
	public void should_handle_unique_sub_asset_exception_durning_update_asset_no_files() {
		Asset asset = anAsset().build();
		
		LegacyAsset mockAssetManager = createMock(LegacyAsset.class);
		try {
			expect(mockAssetManager.update(asset, user)).andThrow(new SubAssetUniquenessException());
		} catch (Exception e) {
			fail("should not have thrown exception.  " + e.getMessage());
		}
		replay(mockAssetManager);
		AssetSaveService sut = new AssetSaveService(mockAssetManager, user);
		sut.setAsset(asset);
		
		Asset actualAsset = sut.update();
		
		assertEquals(asset, actualAsset);
		verify(mockAssetManager);
	}
	
	@Test
	public void save_without_history_does_not_create_history() throws SubAssetUniquenessException {
	
		Tenant tenant = n4();
		Asset asset = anAsset().forTenant(tenant).generate();
		
		LegacyAsset mockAssetManager = createMock(LegacyAsset.class);
		expect(mockAssetManager.create(asset, user)).andReturn(asset);
		
		replay(mockAssetManager);
		
		AssetSaveService sut = new AssetSaveService(mockAssetManager, user);
		sut.setAsset(asset);
		
		sut.createWithoutHistory();
		
		verify(mockAssetManager);
	}
	
	
}
