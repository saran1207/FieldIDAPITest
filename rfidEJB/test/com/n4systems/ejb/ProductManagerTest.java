package com.n4systems.ejb;

import static org.easymock.EasyMock.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.test.helpers.EJBTestCase;
import com.n4systems.util.ProductRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;


public class ProductManagerTest extends EJBTestCase {

	
	private static final String MY_GOOD_SERIAL_NUMBER = "my-good-serial-number";
	private ProductManagerImpl productManager;
	private Query mockQuery;
	private EntityManager mockEntityManager;
	private PersistenceManager mockPersitenceManager;
	private UserBean testUser;
	private Product product;
	private Tenant tenant;

	@Before
	public void setUp() throws Exception {
		productManager = new ProductManagerImpl();
		
		mockEntityManager = createMock(EntityManager.class);
				
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
	@Test public void test_archive_regular_product() {
		expect(mockPersitenceManager.reattach(product)).andReturn(product);
		expect(mockPersitenceManager.findAll((QueryBuilder)anyObject())).andReturn(new ArrayList<SubProduct>());
		expect(mockPersitenceManager.update(product, testUser)).andReturn(product);
		
		ProductRemovalSummary summary = mockSummary(true);
		
		mockFindParentProduct(null, summary);
		setUpArchiveInspection();
		prepareSchedules();
		replay(mockEntityManager);
		replay(mockPersitenceManager);
		productManager.setPersistenceManager(mockPersitenceManager);

		Product savedProduct = null;
		try {
			savedProduct = productManager.archive(product, testUser);
		} catch (UsedOnMasterInspectionException e) {
			fail("this should not have had an used on master instpection exception");
		}

		assertTrue("product not archived", savedProduct.isArchived());
		assertNotNull(savedProduct.getArchivedSerialNumber());

		verify(mockPersitenceManager);
		EasyMock.verify(summary);
		verifyArchiveInspections();
	}
	
	@SuppressWarnings("unchecked")
	@Test public void test_archive_master_product() {
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		subProducts.add(new SubProduct("subProduct", new Product(), product));
		
		expect(mockPersitenceManager.reattach(product)).andReturn(product);
		expect(mockPersitenceManager.findAll((QueryBuilder)anyObject())).andReturn(subProducts);
		mockPersitenceManager.delete((SubProduct)anyObject());
		expect(mockPersitenceManager.update(product, testUser)).andReturn(product);
		
		ProductRemovalSummary summary = mockSummary(true);
		
		mockFindParentProduct(null, summary);
		setUpArchiveInspection();
		prepareSchedules();
		replay(mockEntityManager);
		replay(mockPersitenceManager);
		productManager.setPersistenceManager(mockPersitenceManager);

		

		Product savedProduct = null;
		try {
			savedProduct = productManager.archive(product, testUser);
		} catch (UsedOnMasterInspectionException e) {
			fail("this should not have had an used on master instpection exception");
		}

		assertTrue("product not archived", savedProduct.isArchived());
		assertEquals(MY_GOOD_SERIAL_NUMBER, savedProduct.getArchivedSerialNumber());
		assertEquals(0, savedProduct.getSubProducts().size());

		verify(mockPersitenceManager);
		EasyMock.verify(summary);
		verifyArchiveInspections();
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test public void test_archive_sub_product() {
		
		Product master = new Product();
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		subProducts.add(new SubProduct("subProduct", product, master));
		master.setSubProducts(subProducts);
		master.setId(100L);
		
		
		expect(mockPersitenceManager.reattach(product)).andReturn(product);
		expect(mockPersitenceManager.findAll((QueryBuilder)anyObject())).andReturn(new ArrayList<SubProduct>());
		expect(mockPersitenceManager.update(master, testUser)).andReturn(master);
		expect(mockPersitenceManager.update(product, testUser)).andReturn(product);
		mockPersitenceManager.delete(new SubProduct(product, master));
		
		ProductRemovalSummary summary = mockSummary(true);
		
		mockFindParentProduct(master, summary);
		setUpArchiveInspection();
		prepareSchedules();
		replay(mockEntityManager);
		replay(mockPersitenceManager);
		productManager.setPersistenceManager(mockPersitenceManager);

		Product savedProduct = null;
		try {
			savedProduct = productManager.archive(product, testUser);
		} catch (UsedOnMasterInspectionException e) {
			fail("this should not have had an used on master instpection exception");
		}

		assertTrue("product not archived", savedProduct.isArchived());
		assertEquals(MY_GOOD_SERIAL_NUMBER, savedProduct.getArchivedSerialNumber());
		assertEquals(0, savedProduct.getSubProducts().size());

		verify(mockPersitenceManager);
		EasyMock.verify(summary);
		verifyArchiveInspections();
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

	
	private void prepareSchedules() {
		Query mockUpdateScheduleQuery = createMock(Query.class);
		expect(mockUpdateScheduleQuery.executeUpdate()).andReturn(0);
		expect(mockUpdateScheduleQuery.setParameter((String)anyObject(), (String)anyObject())).andReturn(mockUpdateScheduleQuery);
		expectLastCall().atLeastOnce();
		replay(mockUpdateScheduleQuery);
		
		expect(mockEntityManager.createQuery("UPDATE " + InspectionSchedule.class.getName() 
				+ " SET state = :archiveState,  modifiedBy = :archivingUser , modified = :now "
				+ " WHERE product = :product AND state = :activeState ")).andReturn(mockUpdateScheduleQuery);
		
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


	public void test_process_schedules_nothing_changed() {

	}

	public void test_process_schedules_emptyed() {

	}

	public void test_process_schedules_added_and_removed() {

	}

	@Test public void test_archive_inspections() {
		setUpArchiveInspection();
		replay(mockEntityManager);
		productManager.setPersistenceManager(mockPersitenceManager);
		try {
			Method archiveInspections = productManager.getClass().getDeclaredMethod("archiveInspections",
					Product.class, UserBean.class);
			archiveInspections.setAccessible(true);
			archiveInspections.invoke(productManager, product, testUser);
		} catch (Exception e) {
			fail("failed to call the method." + e.toString());
		}

		verifyArchiveInspections();
	}

	private void verifyArchiveInspections() {
		//verify(mockEntityManager);
		verify(mockQuery);
	}

	private void setUpArchiveInspection() {
		mockQuery = createMock(Query.class);

		expect(mockQuery.setParameter((String) anyObject(), anyObject())).andReturn(mockQuery);
		expectLastCall().atLeastOnce();
		
		expect(mockQuery.executeUpdate()).andReturn(4);
		replay(mockQuery);

		Query mockIndexingQuery = createMock(Query.class);
		
		expect(mockIndexingQuery.setParameter((String) anyObject(), anyObject())).andReturn(mockIndexingQuery);
		expectLastCall().atLeastOnce();
		expect(mockIndexingQuery.getResultList()).andReturn(new ArrayList<Long>());
		replay(mockIndexingQuery);
		
		
		expect(mockEntityManager.createQuery("select id from Inspection WHERE product = :product AND state = :archiveState")).andReturn(mockIndexingQuery);
		expect(mockEntityManager.createQuery((String) anyObject())).andReturn(mockQuery);
		

		productManager.setEntityManager(mockEntityManager);
		
	}

}
