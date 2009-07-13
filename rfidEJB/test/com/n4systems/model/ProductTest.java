package com.n4systems.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
