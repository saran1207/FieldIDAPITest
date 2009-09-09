package rfid.ejb.session;

import static com.n4systems.model.builders.PrimaryOrgBuilder.aPrimaryOrg;
import static com.n4systems.model.builders.TenantBuilder.aTenant;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductSerialExtensionValueBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.CustomerOrgBuilder;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.SecondaryOrgBuilder;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.TenantCache;
import com.n4systems.test.helpers.EJBTestCase;
import com.n4systems.webservice.dto.CustomerOrgServiceDTO;
import com.n4systems.webservice.dto.InfoOptionServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;


public class ServiceDTOBeanConverterImplTest extends EJBTestCase {

	ServiceDTOBeanConverterImpl converter ;
	
	@Before
	public void setUp() throws Exception {
		converter = new ServiceDTOBeanConverterImpl();
	}

	@After
	public void tearDown() throws Exception {
		converter = null;
	}

	@Test
	public void test_convert_productServiceDTO_to_new_product_confirm_non_optional_fields() {
		ProductServiceDTO productServiceDTO = new ProductServiceDTO(); 
		Product product = new Product();
		
		populateServiceDTO(productServiceDTO);
		test_product_non_optional_fields(product, productServiceDTO);
		assertValuesForANewProductAreAssigned(productServiceDTO, product);
	}
	
	
	@Test
	public void test_convert_productServiceDTO_to_existing_product_confirm_non_optional_fields() {
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 2, 18);  // 02/18/08
		Date identifiedDate = new PlainDate(cal.getTime());
		
		Product product = new Product();
		
		product.setId(1L);
		product.setLocation( "some value" );
		product.setMobileGUID( "0000-000-00000-0000123" );
		product.setIdentified(identifiedDate);
		product.setPurchaseOrder( "0987654321" );
		product.setRfidNumber( "rfid" );
		product.setSerialNumber( "someserialnumber" );
		
		ProductServiceDTO serviceDTO = new ProductServiceDTO();
		populateServiceDTO(serviceDTO);
		serviceDTO.setMobileGuid("1111-111-11111-1111111");
		serviceDTO.setIdentified("11/06/08 06:30:01 am");
		
		test_product_non_optional_fields(product, serviceDTO);
		assertEquals("0000-000-00000-0000123", product.getMobileGUID());
		assertEquals(identifiedDate, product.getIdentified());
	}
	
	@Test
	public void test_convert_productServiceDTO_to_existing_product_confirm_non_handheld_fields_are_not_overwritten() {
		ProductServiceDTO productServiceDTO = new ProductServiceDTO();
		
		
		Date createDate = new Date( 1000000L );
		Date modifyDate = new Date( 2000000L );
		String uuid = "thisistheuuid";
		String linkedUuid = "thisisthelinkeduuid";
		Date lastInspectionDate = new Date( 300000L );
		productServiceDTO.setSerialNumber("serialTest12311123123");
		
		LineItem shopOrder = new LineItem();
		shopOrder.setId( 1L );
		
		Order customerOrder = new Order();
		customerOrder.setId( 2L );
				
		Set<ProductSerialExtensionValueBean> extensions = new HashSet<ProductSerialExtensionValueBean>();
		extensions.add( new ProductSerialExtensionValueBean() );
		
		Set<InspectionSchedule> schedules = new HashSet<InspectionSchedule>();
		schedules.add( new InspectionSchedule() );
		
				
		UserBean modifiedBy = new UserBean();
		modifiedBy.setUniqueID( 4L );
		
		Product product = new Product();
		product.setId( 1L );
		product.setCreated(createDate);
		product.setModified(modifyDate);
		product.setUuid( uuid );
		product.setLinkedUuid( linkedUuid );
		product.setLastInspectionDate( lastInspectionDate );
		product.setShopOrder( shopOrder );
		product.setCustomerOrder( customerOrder );
		product.setProductSerialExtensionValues( extensions );
		
		product.setModifiedBy( modifiedBy );
		
		
		product.getSubProducts().add(new SubProduct("product1", new Product(), product));
		product.getSubProducts().add(new SubProduct("product2", new Product(), product));

		
		
		test_product_non_optional_fields( product, productServiceDTO );
		
		assertEquals( new Long( 1L ) ,product.getId() );
		assertEquals( createDate ,product.getCreated());
		assertEquals( modifyDate,product.getModified());
		assertEquals( uuid,product.getUuid() );
		assertEquals( linkedUuid,product.getLinkedUuid() );
		assertEquals( lastInspectionDate,product.getLastInspectionDate() );
		assertEquals( shopOrder, product.getShopOrder() );
		assertEquals( customerOrder,product.getCustomerOrder() );
		assertEquals( 1, product.getProductSerialExtensionValues().size() );
		assertEquals( 2, product.getSubProducts().size() );
		assertEquals( modifiedBy, product.getModifiedBy() );
		
		
	}
	
	private void test_product_non_optional_fields( Product product, ProductServiceDTO productServiceDTO  ) {
		ProductType foundProductType = new ProductType();
		foundProductType.setId( 5L );
		
		UserBean foundUser = new UserBean();
		foundUser.setUniqueID(productServiceDTO.getIdentifiedById());
		
		Tenant foundTenant = aTenant().build();
		productServiceDTO.setProductTypeId( foundProductType.getId() );
		
		EntityManager mockEntityManager = EasyMock.createMock( EntityManager.class );
		
		EasyMock.expect( mockEntityManager.find( ProductType.class, foundProductType.getId() ) ).andReturn( foundProductType );
		if (productServiceDTO.identifiedByExists()) {
			EasyMock.expect(mockEntityManager.find(UserBean.class, foundUser.getId())).andReturn(foundUser);
		} 
		EasyMock.replay( mockEntityManager );
		injectEntityManager( converter, mockEntityManager );
		
		
		PrimaryOrg primaryOrg = aPrimaryOrg().onTenant(foundTenant).build();

		TenantCache mockCache = createMock(TenantCache.class);
		expect(mockCache.findTenant(foundTenant.getId())).andReturn(foundTenant);
		expect(mockCache.findPrimaryOrg(foundTenant.getId())).andReturn(primaryOrg);
		replay(mockCache);
		TenantCache.setInstance(mockCache);
		
		product = converter.convert( productServiceDTO, product, foundTenant.getId() );
		
		assertAssignedValuesWereCopiedToProduct( productServiceDTO, product, foundProductType, foundTenant, primaryOrg );
		
		EasyMock.verify( mockEntityManager );
	}

	private void populateServiceDTO(ProductServiceDTO productServiceDTO) {
		productServiceDTO.setComments( "comment" );
		productServiceDTO.setCustomerRefNumber( "1234-54321" );
		
		productServiceDTO.setIdentified( "11/06/08 06:30:01 am" );
		
		
		productServiceDTO.setLocation( "location" );
		productServiceDTO.setMobileGuid( "1234-1234-123222-112221" );
		productServiceDTO.setPurchaseOrder( "0987654321" );
		productServiceDTO.setRfidNumber( "af331fe3058901abae3319933" );
		productServiceDTO.setSerialNumber( "testserial" );
	}

	
	@Test
	public void test_info_options_service_dto_convert_to_new_dynamic_info_option() {
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		
		InfoFieldBean foundInfoField = new InfoFieldBean();
		foundInfoField.setUniqueID( 4L );
		
		EntityManager mockEntityManager = EasyMock.createMock( EntityManager.class );
		EasyMock.expect( mockEntityManager.find( InfoFieldBean.class, foundInfoField.getUniqueID() ) ).andReturn( foundInfoField );
		EasyMock.replay( mockEntityManager );
		injectEntityManager( converter, mockEntityManager );
		
		infoOptionDTO.setInfoFieldId( foundInfoField.getUniqueID() );
		infoOptionDTO.setName( "new dynamic info option" );
		
		InfoOptionBean infoOption = converter.convert( infoOptionDTO );
		assertNotNull( infoOption );
		assertNull( infoOption.getUniqueID() );
		assertEquals( infoOptionDTO.getName(), infoOption.getName() );
		assertEquals( infoOptionDTO.getInfoFieldId(), infoOption.getInfoField().getUniqueID() );
		assertFalse( infoOption.isStaticData() );
		assertEquals( new Long( 0 ), infoOption.getWeight() );
		
		EasyMock.verify( mockEntityManager );
	}
	
	
	@Test
	public void test_info_options_service_dto_convert_to_existing_static_info_option() {
		InfoOptionServiceDTO infoOptionDTO = new InfoOptionServiceDTO();
		
		InfoFieldBean foundInfoField = new InfoFieldBean();
		foundInfoField.setUniqueID( 4L );
		
		InfoOptionBean existingOption = new InfoOptionBean();
		existingOption.setInfoField( foundInfoField );
		existingOption.setUniqueID( 20L );
		existingOption.setWeight( 5L );
		existingOption.setName( "static option" );
		existingOption.setStaticData( true );
		
		EntityManager mockEntityManager = EasyMock.createMock( EntityManager.class );
		EasyMock.expect( mockEntityManager.find( InfoOptionBean.class, existingOption.getUniqueID() ) ).andReturn( existingOption );
		EasyMock.replay( mockEntityManager );
		injectEntityManager( converter, mockEntityManager );
		
		infoOptionDTO.setInfoFieldId( foundInfoField.getUniqueID() - 1L );
		infoOptionDTO.setName( "new dynamic info option" );
		infoOptionDTO.setId( existingOption.getUniqueID() );
		
		InfoOptionBean infoOption = converter.convert( infoOptionDTO );
		assertNotNull( infoOption );
		assertNotSame( infoOptionDTO.getName(), infoOption.getName() );
		assertNotSame( infoOptionDTO.getInfoFieldId(), infoOption.getInfoField().getUniqueID() );
		assertTrue( infoOption.isStaticData() );
		assertEquals( new Long( 5 ), infoOption.getWeight() );
		assertEquals( new Long( 20 ), infoOption.getUniqueID() );
		
		EasyMock.verify( mockEntityManager );
	}
	
	private void assertValuesForANewProductAreAssigned(ProductServiceDTO productServiceDTO, Product product) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a" );
		Date identifiedDate = null;		
		try {
			identifiedDate = new PlainDate(df.parse(productServiceDTO.getIdentified()));
		} catch (ParseException e) {
			fail( "parse Error" );
		}
		assertEquals( identifiedDate, product.getIdentified() );
		assertEquals( productServiceDTO.getMobileGuid(), product.getMobileGUID() );
	}
	private void assertAssignedValuesWereCopiedToProduct( ProductServiceDTO productServiceDTO, Product product, ProductType foundProductType,
			Tenant foundTenant, PrimaryOrg primaryOrg ) {
		assertEquals( foundTenant, product.getTenant() );
		assertEquals( foundProductType, product.getType() );
		assertEquals( productServiceDTO.getComments(), product.getComments() );
		assertEquals( productServiceDTO.getCustomerRefNumber(), product.getCustomerRefNumber() );
		assertEquals( productServiceDTO.getLocation(), product.getLocation() );
		assertEquals( productServiceDTO.getPurchaseOrder(), product.getPurchaseOrder() );
		assertEquals( productServiceDTO.getRfidNumber(), product.getRfidNumber() );
		assertEquals( productServiceDTO.getSerialNumber(), product.getSerialNumber() );
		
		if( productServiceDTO.customerExists() && !productServiceDTO.divisionExists()) {
			assertEquals( new Long( productServiceDTO.getCustomerId() ), product.getOwner().getId() );
		} 
		
		if( productServiceDTO.customerExists() && productServiceDTO.divisionExists() ) {
			assertEquals( new Long( productServiceDTO.getDivisionId() ), product.getOwner().getId() );
		} 
		
		if( productServiceDTO.jobSiteExists() ) {
			assertEquals( new Long( productServiceDTO.getJobSiteId() ), product.getOwner().getId() );
		} 
		
		if (!productServiceDTO.customerExists() && !productServiceDTO.divisionExists() && !productServiceDTO.jobSiteExists()) {
			assertNull(product.getOwner());
		}
		
		if( productServiceDTO.identifiedByExists() ) {
			assertEquals( new Long( productServiceDTO.getIdentifiedById() ), product.getIdentifiedBy().getUniqueID() );
			assertEquals( new Long( product.getIdentifiedBy().getOwner().getId() ), product.getOwner().getId() );
		} else {
			assertNull( product.getIdentifiedBy() );
		}
		
		if (productServiceDTO.getProductStatusId() > 0) {
			assertEquals(new Long(productServiceDTO.getProductStatusId()), product.getProductStatus().getUniqueID());
		} else {
			if (productServiceDTO.getProductStatusId() == ServiceDTOBeanConverterImpl.NULL_ID) {
				assertNull(product.getProductStatus());
			} else {
				// need to test value has not changed
			}
		}
	}
	
	@Test
	public void test_convert_customer_org_from_primary_org() {
		String customerName = "Test Name";
		
		PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().build();
		
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().
									withName(customerName).withParent(primaryOrg).build();
		
		CustomerOrgServiceDTO dto = converter.convert(customerOrg);
		
		assertEquals(dto.getName(), customerName);
		assertNull(dto.getParentId());		
	}
	
	@Test
	public void test_convert_customer_org_from_secondary_org() {
		String customerName = "Test Name";
		
		SecondaryOrg secondaryOrg = SecondaryOrgBuilder.aSecondaryOrg().build();
		
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().
									withName(customerName).withParent(secondaryOrg).build();
		
		CustomerOrgServiceDTO dto = converter.convert(customerOrg);
		
		assertEquals(dto.getName(), customerName);
		assertEquals(dto.getParentId(), secondaryOrg.getId());		
	}
	
	@Test
	public void test_setup_data_last_mod_dates() {
		SetupDataLastModDates model = new SetupDataLastModDates();
		
		model.setAutoAttributes	(new Date(1245703228000L));
		model.setInspectionTypes(new Date(1245603228000L));
		model.setOwners			(new Date(1245503228000L));
		model.setProductTypes	(new Date(1245403228000L));
		
		SetupDataLastModDatesServiceDTO dto = converter.convert(model);
		
		assertEquals(model.getAutoAttributes(), dto.getAutoAttributes());
		assertEquals(model.getInspectionTypes(), dto.getInspectionTypes());
		assertEquals(model.getOwners(), dto.getOwners());
		assertEquals(model.getProductTypes(), dto.getProductTypes());
	}
}
