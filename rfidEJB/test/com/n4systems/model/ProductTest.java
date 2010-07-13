package com.n4systems.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.builders.ProductBuilder;
public class ProductTest {

	Product product;
	@Before
	public void setUp() throws Exception {
		product = new Product();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testArchiveSerialNumber() {
		String serialNumber = "my-good-serial-number";
		product.setSerialNumber( serialNumber );
		
		product.archiveSerialNumber();
		
		assertEquals( serialNumber, product.getArchivedSerialNumber() );
		assertNotSame( serialNumber, product.getSerialNumber() );
		assertNotNull( product.getSerialNumber() );	
	}
	
	@Test
	public void network_id_set_from_linked_product_on_create() {
		product.setLinkedProduct(ProductBuilder.aProduct().build());
		product.onCreate();
		
		assertEquals(product.getLinkedProduct().getNetworkId(), product.getNetworkId());		
	}
	
	@Test
	public void network_id_set_from_linked_product_on_update() {
		product.setLinkedProduct(ProductBuilder.aProduct().build());
		product.onUpdate();
		
		assertEquals(product.getLinkedProduct().getNetworkId(), product.getNetworkId());		
	}
	
	@Test
	public void network_id_set_to_own_id_on_create_when_no_linked_product() {
		product.setId(1L);
		product.onCreate();
		
		assertEquals(product.getId(), product.getNetworkId());		
	}
	
	@Test
	public void network_id_set_to_own_id_on_update_when_no_linked_product() {
		product.setId(1L);
		product.onUpdate();
		
		assertEquals(product.getId(), product.getNetworkId());		
	}
	
	@Test
	public void last_linked_id_synchronizes_on_load() {
		product.setLinkedProduct(ProductBuilder.aProduct().build());
		
		assertTrue(product.linkedProductHasChanged());
		
		product.onLoad();
		
		assertFalse(product.linkedProductHasChanged());
	}
	
	@Test
	public void last_linked_id_synchronizes_on_create() {
		product.setLinkedProduct(ProductBuilder.aProduct().build());
		
		assertTrue(product.linkedProductHasChanged());
		
		product.onCreate();
		
		assertFalse(product.linkedProductHasChanged());
	}
	
	@Test
	public void last_linked_id_synchronizes_on_update() {
		product.setLinkedProduct(ProductBuilder.aProduct().build());
		
		assertTrue(product.linkedProductHasChanged());
		
		product.onUpdate();
		
		assertFalse(product.linkedProductHasChanged());
	}
	
	@Test
	public void linked_id_can_get_nulled() {
		product.setLinkedProduct(ProductBuilder.aProduct().build());
		product.onLoad();
		
		assertFalse(product.linkedProductHasChanged());
		
		product.setLinkedProduct(null);
		
		assertTrue(product.linkedProductHasChanged());
	}
	
	
}
