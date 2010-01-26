package com.n4systems.ejb;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.util.ProductRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;


public class ProductManagerTest { //extends EJBTestCase {

	
	private static final String MY_GOOD_SERIAL_NUMBER = "my-good-serial-number";
	private ProductManagerImpl productManager;
	private PersistenceManager mockPersitenceManager;
	private UserBean testUser;
	private Product product;
	private Tenant tenant;

	@Before
	public void setUp() throws Exception {
		productManager = new ProductManagerImpl();
		
				
		mockPersitenceManager = createMock(PersistenceManager.class);
		testUser = new UserBean();
		testUser.setUniqueID(1L);
		testUser.setTenant(tenant);

		tenant = new Tenant();
		tenant.setName("test_tenant");
		
		product = new Product();
		product.setId(2L);
		product.getProjects();
		product.setTenant(tenant);
		product.setSerialNumber(MY_GOOD_SERIAL_NUMBER);
	}

	@After
	public void tearDown() throws Exception {
	}


	
	@SuppressWarnings("unchecked")
	@Test public void test_archive_sub_product_used_on_master_inspection() {

		expect(mockPersitenceManager.reattach(product)).andReturn(product);
		expect(mockPersitenceManager.findAll((QueryBuilder)anyObject())).andReturn(new ArrayList<SubProduct>());
		replay(mockPersitenceManager);
		
		ProductRemovalSummary summary = mockSummary(false);
		
		mockFindParentProduct(null, summary);
		productManager.setPersistenceManager(mockPersitenceManager);

		
		boolean expectionThrown = false;
		try {
			productManager.archive(product, testUser);
		} catch (UsedOnMasterInspectionException e) {
			expectionThrown = true;
		}
		assertTrue("this should have had an used on master instpection exception", expectionThrown);
		
	}


	
	private ProductRemovalSummary mockSummary(boolean validToDelete) {
		ProductRemovalSummary summary = null;
		try {
			summary = EasyMock.createMock(ProductRemovalSummary.class, ProductRemovalSummary.class.getDeclaredMethod("validToDelete"));
			EasyMock.expect(summary.validToDelete()).andReturn(validToDelete);
			EasyMock.replay(summary);
		} catch (Exception e) {
			fail("could not create product removal summary " + e.getMessage());
		}
		return summary;
	}

	
	
	
	private void mockFindParentProduct(Product parentProduct, ProductRemovalSummary summary) {
		try {
			productManager = EasyMock.createMock(ProductManagerImpl.class, ProductManagerImpl.class.getDeclaredMethod(
					"parentProduct", Product.class), ProductManagerImpl.class.getDeclaredMethod(
							"testArchive", Product.class));
			EasyMock.expect(productManager.testArchive(product)).andReturn(summary);
			EasyMock.expect(productManager.parentProduct(product)).andReturn(parentProduct);
			EasyMock.replay(productManager);
		} catch (Exception e) {
			fail("can't mock the product manager");
		}
	}

}
