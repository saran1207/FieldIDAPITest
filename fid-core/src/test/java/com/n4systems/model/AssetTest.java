package com.n4systems.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.AssetBuilder;
public class AssetTest {

	Asset asset;
	@Before
	public void setUp() throws Exception {
		asset = new Asset();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testArchiveSerialNumber() {
		String serialNumber = "my-good-serial-number";
		asset.setSerialNumber( serialNumber );
		
		asset.archiveSerialNumber();
		
		assertEquals( serialNumber, asset.getArchivedSerialNumber() );
		assertNotSame( serialNumber, asset.getSerialNumber() );
		assertNotNull( asset.getSerialNumber() );
	}
	
	@Test
	public void network_id_set_from_linked_asset_on_create() {
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		asset.onCreate();
		
		assertEquals(asset.getLinkedAsset().getNetworkId(), asset.getNetworkId());
	}
	
	@Test
	public void network_id_set_from_linked_asset_on_update() {
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		asset.onUpdate();
		
		assertEquals(asset.getLinkedAsset().getNetworkId(), asset.getNetworkId());
	}
	
	@Test
	public void network_id_set_to_own_id_on_create_when_no_linked_asset() {
		asset.setId(1L);
		asset.onCreate();
		
		assertEquals(asset.getId(), asset.getNetworkId());
	}
	
	@Test
	public void network_id_set_to_own_id_on_update_when_no_linked_asset() {
		asset.setId(1L);
		asset.onUpdate();
		
		assertEquals(asset.getId(), asset.getNetworkId());
	}
	
	@Test
	public void last_linked_id_synchronizes_on_load() {
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		
		assertTrue(asset.linkedAssetHasChanged());
		
		asset.onLoad();
		
		assertFalse(asset.linkedAssetHasChanged());
	}
	
	@Test
	public void last_linked_id_synchronizes_on_create() {
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		
		assertTrue(asset.linkedAssetHasChanged());
		
		asset.onCreate();
		
		assertFalse(asset.linkedAssetHasChanged());
	}
	
	@Test
	public void last_linked_id_synchronizes_on_update() {
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		
		assertTrue(asset.linkedAssetHasChanged());
		
		asset.onUpdate();
		
		assertFalse(asset.linkedAssetHasChanged());
	}
	
	@Test
	public void linked_id_can_get_nulled() {
		asset.setLinkedAsset(AssetBuilder.anAsset().build());
		asset.onLoad();
		
		assertFalse(asset.linkedAssetHasChanged());
		
		asset.setLinkedAsset(null);
		
		assertTrue(asset.linkedAssetHasChanged());
	}
	
	
}
