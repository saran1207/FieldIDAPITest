package com.n4systems.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.google.common.collect.Sets;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.builders.InfoFieldBeanBuilder;
import com.n4systems.model.builders.InfoOptionBeanBuilder;
public class AssetTest {

	private String IDENTIFIER = "ID";
	private String RFIDNUMBER = "RFID";
	private String REFERENCENUMBER = "Ref#";
	private String PURCHASEORDER = "PO#";
	private String NONINTEGRATIONORDERNUMBER = "Order#";
	
	private Asset asset;
	
	@Before
	public void setUp() throws Exception {
		asset = new Asset();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testArchiveIdentifier() {
		String identifier = "my-good-identifier";
		asset.setIdentifier(identifier);
		
		asset.archiveIdentifier();
		
		assertEquals( identifier, asset.getArchivedIdentifier() );
		assertNotSame( identifier, asset.getIdentifier() );
		assertNotNull( asset.getIdentifier() );
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
	
	@Test
	public void test_description_null_values() {
		String format = "id:[%1$s] rfid:[%2$s] ref:[%3$s] order:[%4$s] po:[%5$s]";
		String template = String.format(format, "{Identifier}", "{RFID}", "{RefNumber}", "{OrderNumber}", "{PONumber}" );
		Asset asset = AssetBuilder.anAsset().withIdentifier(null).rfidNumber(null).referenceNumber(null).purchaseOrder(null).nonIntegrationOrderNumber(null).build();
		AssetType assetType = AssetTypeBuilder.anAssetType().descriptionTemplate(template).build();
		asset.setType(assetType);

		String description = asset.getDescription();		
		
		// NOTE : nulls should be treated as empty strings, NOT the word "null".
		// e.g. for a template "the id is {Identifier} for X" 
		//  will yield  "the id is for X"   as opposed to "the id is null for X".  
		assertEquals( String.format(format, "", "", "", "", ""), description );		
	}
	
	@Test
	public void test_description() {
		String format = "id:[%1$s] rfid:[%2$s] ref:[%3$s] order:[%4$s] po:[%5$s]";
		String template = String.format(format, "{Identifier}", "{RFID}", "{RefNumber}", "{OrderNumber}", "{PONumber}" );
		Asset asset = AssetBuilder.anAsset().withIdentifier(IDENTIFIER).rfidNumber(RFIDNUMBER).referenceNumber(REFERENCENUMBER).purchaseOrder(PURCHASEORDER).nonIntegrationOrderNumber(NONINTEGRATIONORDERNUMBER).build();
		AssetType assetType = AssetTypeBuilder.anAssetType().descriptionTemplate(template).build();
		asset.setType(assetType);

		String description = asset.getDescription();		
		
		assertEquals( String.format(format, IDENTIFIER, RFIDNUMBER, REFERENCENUMBER, NONINTEGRATIONORDERNUMBER, PURCHASEORDER), description );		
	}
	
	@Test
	public void test_description_duplicate_attribute() {
		String format = "id:[%1$s] rfid:[%2$s] ref:[%3$s] order:[%4$s] po:[%5$s] other:[%6$s]";
		String template = String.format(format, "{Identifier}", "{RFID}", "{RefNumber}", "{OrderNumber}", "{PONumber}", "{uniqueField}" );
		String anotherId = "anotherIdentifier";
		String anotherRfid="anotherRFID";
		String anotherRef = "anotherREF";
		String anotherOrderNum = "anotherOrderNum";
		String anotherPONum = "anotherPONUM";
		String blah = "blahblahblah";

		InfoFieldBean[] fields = {
				// ** attributes with same name as reserved field.
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("Identifier").build(),   
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("RFID").build(),   
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("RefNumber").build(),   
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("OrderNumber").build(),   
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("PONumber").build(),   
				InfoFieldBeanBuilder.aTextField().setRequired(true).named("uniqueField").build(),   
		};
		AssetType assetType = AssetTypeBuilder.anAssetType().descriptionTemplate(template).withFields(fields).build();												
		
		Asset asset = AssetBuilder.anAsset().ofType(assetType).withIdentifier(IDENTIFIER).rfidNumber(RFIDNUMBER).referenceNumber(REFERENCENUMBER).purchaseOrder(PURCHASEORDER).nonIntegrationOrderNumber(NONINTEGRATIONORDERNUMBER).build();

		InfoOptionBean id = InfoOptionBeanBuilder.aDynamicInfoOption().forField(fields[0]).withName(anotherId).build();
		InfoOptionBean rfid = InfoOptionBeanBuilder.aDynamicInfoOption().forField(fields[1]).withName(anotherRfid).build();
		InfoOptionBean ref = InfoOptionBeanBuilder.aDynamicInfoOption().forField(fields[2]).withName(anotherRef).build();
		InfoOptionBean orderNum = InfoOptionBeanBuilder.aDynamicInfoOption().forField(fields[3]).withName(anotherOrderNum).build();
		InfoOptionBean poNum = InfoOptionBeanBuilder.aDynamicInfoOption().forField(fields[4]).withName(anotherPONum).build();
		InfoOptionBean otherField = InfoOptionBeanBuilder.aDynamicInfoOption().forField(fields[5]).withName(blah).build();
		asset.setInfoOptions(Sets.newHashSet(id, rfid, ref, orderNum, poNum, otherField));

		String description = asset.getDescription();
		
		// note how it will use the attribute value instead of the assets IDENTIFIER value.
		// in other words, Identifier and other predefined macros will be overridden. WEB-2539
		assertEquals( String.format(format, anotherId, anotherRfid, anotherRef, anotherOrderNum, anotherPONum, blah ), description );		
	}	
	
}
