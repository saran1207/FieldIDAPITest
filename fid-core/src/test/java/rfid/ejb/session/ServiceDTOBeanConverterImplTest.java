package rfid.ejb.session;

import static com.n4systems.model.builders.PrimaryOrgBuilder.*;
import static com.n4systems.model.builders.TenantBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.SubAsset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.legacy.impl.ServiceDTOBeanConverterImpl;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.builders.CustomerOrgBuilder;
import com.n4systems.model.builders.DivisionOrgBuilder;
import com.n4systems.model.builders.PrimaryOrgBuilder;
import com.n4systems.model.builders.SecondaryOrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.services.TenantCache;
import com.n4systems.webservice.dto.CustomerOrgServiceDTO;
import com.n4systems.webservice.dto.DivisionOrgServiceDTO;
import com.n4systems.webservice.dto.InfoOptionServiceDTO;
import com.n4systems.webservice.dto.InternalOrgServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;


public class ServiceDTOBeanConverterImplTest {

	ServiceDTOBeanConverterTestExtension converter ;
	
	@Before
	public void setUp() throws Exception {
		converter = new ServiceDTOBeanConverterTestExtension();
	}

	@After
	public void tearDown() throws Exception {
		converter = null;
	}

	@Test
	public void test_convert_productServiceDTO_to_new_product_confirm_non_optional_fields() {
		ProductServiceDTO productServiceDTO = new ProductServiceDTO(); 
		Asset asset = new Asset();
		
		populateServiceDTO(productServiceDTO);
		test_product_non_optional_fields(asset, productServiceDTO);
		assertValuesForANewProductAreAssigned(productServiceDTO, asset);
	}
	
	
	@Test
	public void test_convert_productServiceDTO_to_existing_product_confirm_non_optional_fields() {
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		//cal.set(2008, 2, 18);  // 02/18/08
		Date identifiedDate = new PlainDate(cal.getTime());
		
		Asset asset = new Asset();
		
		asset.setId(1L);
		asset.setMobileGUID( "0000-000-00000-0000123" );
		asset.setIdentified(identifiedDate);
		asset.setPurchaseOrder( "0987654321" );
		asset.setRfidNumber( "rfid" );
		asset.setSerialNumber( "someserialnumber" );
		
		ProductServiceDTO serviceDTO = new ProductServiceDTO();
		populateServiceDTO(serviceDTO);
		serviceDTO.setMobileGuid("1111-111-11111-1111111");
		serviceDTO.setIdentified(dateFormat.format(identifiedDate));
		
		test_product_non_optional_fields(asset, serviceDTO);
		assertEquals("0000-000-00000-0000123", asset.getMobileGUID());
		assertEquals(identifiedDate, asset.getIdentified());
	}
	
	@Test
	public void test_convert_productServiceDTO_to_existing_product_confirm_non_handheld_fields_are_not_overwritten() {
		ProductServiceDTO productServiceDTO = new ProductServiceDTO();
		
		
		Date createDate = new Date( 1000000L );
		Date modifyDate = new Date( 2000000L );
		Date lastEventDate = new Date( 300000L );
		productServiceDTO.setSerialNumber("serialTest12311123123");
		
		LineItem shopOrder = new LineItem();
		shopOrder.setId( 1L );
		
		Order customerOrder = new Order();
		customerOrder.setId( 2L );
				
		Set<EventSchedule> schedules = new HashSet<EventSchedule>();
		schedules.add( new EventSchedule() );
		
				
		User modifiedBy = new User();
		modifiedBy.setId(4L);
		
		Asset asset = new Asset();
		asset.setId( 1L );
		asset.setCreated(createDate);
		asset.setModified(modifyDate);
		asset.setLastEventDate( lastEventDate );
		asset.setShopOrder( shopOrder );
		asset.setCustomerOrder( customerOrder );
		
		asset.setModifiedBy( modifiedBy );
		
		
		asset.getSubAssets().add(new SubAsset("product1", new Asset(), asset));
		asset.getSubAssets().add(new SubAsset("product2", new Asset(), asset));

		
		
		test_product_non_optional_fields(asset, productServiceDTO );
		
		assertEquals( new Long( 1L ) , asset.getId() );
		assertEquals( createDate , asset.getCreated());
		assertEquals( modifyDate, asset.getModified());
		assertEquals( lastEventDate, asset.getLastEventDate() );
		assertEquals( shopOrder, asset.getShopOrder() );
		assertEquals( customerOrder, asset.getCustomerOrder() );
		assertEquals( 2, asset.getSubAssets().size() );
		assertEquals( modifiedBy, asset.getModifiedBy() );
		
		
	}
	
	@SuppressWarnings("deprecation")
	private void test_product_non_optional_fields( Asset asset, ProductServiceDTO productServiceDTO  ) {
		AssetType foundAssetType = new AssetType();
		foundAssetType.setId( 5L );
		
		User foundUser = new User();
		foundUser.setId(productServiceDTO.getIdentifiedById());
		
		Tenant foundTenant = aTenant().build();
		productServiceDTO.setProductTypeId( foundAssetType.getId() );
		
		EntityManager mockEntityManager = createMock( EntityManager.class );
		
		expect( mockEntityManager.find( AssetType.class, foundAssetType.getId() ) ).andReturn(foundAssetType);
		if (productServiceDTO.identifiedByExists()) {
			expect(mockEntityManager.find(User.class, foundUser.getId())).andReturn(foundUser);
		} 
		
		PrimaryOrg primaryOrg = aPrimaryOrg().onTenant(foundTenant).build();
		expect(mockEntityManager.find(BaseOrg.class, productServiceDTO.getOwnerId())).andReturn(primaryOrg);
		
		
		replay( mockEntityManager );
		converter.setEntityManager(mockEntityManager);

		TenantCache mockCache = createMock(TenantCache.class);
		expect(mockCache.findTenant(foundTenant.getId())).andReturn(foundTenant);
		expect(mockCache.findPrimaryOrg(foundTenant.getId())).andReturn(primaryOrg);
		replay(mockCache);
		TenantCache.setInstance(mockCache);
		
		asset = converter.convert( productServiceDTO, asset, foundTenant.getId() );
		
		assertAssignedValuesWereCopiedToProduct( productServiceDTO, asset, foundAssetType, foundTenant, primaryOrg );
		
		verify( mockEntityManager );
	}

	private void populateServiceDTO(ProductServiceDTO productServiceDTO) {
		productServiceDTO.setComments( "comment" );
		productServiceDTO.setCustomerRefNumber( "1234-54321" );
		
		productServiceDTO.setIdentified( "11/06/08 06:30:01 am" );
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
		
		EntityManager mockEntityManager = createMock( EntityManager.class );
		expect( mockEntityManager.find( InfoFieldBean.class, foundInfoField.getUniqueID() ) ).andReturn( foundInfoField );
		replay( mockEntityManager );
		converter.setEntityManager(mockEntityManager);
		
		infoOptionDTO.setInfoFieldId( foundInfoField.getUniqueID() );
		infoOptionDTO.setName( "new dynamic info option" );
		
		InfoOptionBean infoOption = converter.convert( infoOptionDTO );
		assertNotNull( infoOption );
		assertNull( infoOption.getUniqueID() );
		assertEquals( infoOptionDTO.getName(), infoOption.getName() );
		assertEquals( infoOptionDTO.getInfoFieldId(), infoOption.getInfoField().getUniqueID() );
		assertFalse( infoOption.isStaticData() );
		assertEquals( new Long( 0 ), infoOption.getWeight() );
		
		verify( mockEntityManager );
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
		
		EntityManager mockEntityManager = createMock( EntityManager.class );
		expect( mockEntityManager.find( InfoOptionBean.class, existingOption.getUniqueID() ) ).andReturn( existingOption );
		replay( mockEntityManager );
		converter.setEntityManager(mockEntityManager);
		
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
		
		verify( mockEntityManager );
	}
	
	private void assertValuesForANewProductAreAssigned(ProductServiceDTO productServiceDTO, Asset asset) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a" );
		Date identifiedDate = null;		
		try {
			identifiedDate = new PlainDate(df.parse(productServiceDTO.getIdentified()));
		} catch (ParseException e) {
			fail( "parse Error" );
		}
		assertEquals( identifiedDate, asset.getIdentified() );
		assertEquals( productServiceDTO.getMobileGuid(), asset.getMobileGUID() );
	}
	
	private void assertAssignedValuesWereCopiedToProduct( ProductServiceDTO productServiceDTO, Asset asset, AssetType foundAssetType,
			Tenant foundTenant, PrimaryOrg primaryOrg ) {
		assertEquals( foundTenant, asset.getTenant() );
		assertEquals(foundAssetType, asset.getType() );
		assertEquals( productServiceDTO.getComments(), asset.getComments() );
		assertEquals( productServiceDTO.getCustomerRefNumber(), asset.getCustomerRefNumber() );
		assertEquals( productServiceDTO.getPurchaseOrder(), asset.getPurchaseOrder() );
		assertEquals( productServiceDTO.getRfidNumber(), asset.getRfidNumber() );
		assertEquals( productServiceDTO.getSerialNumber(), asset.getSerialNumber() );
		
		if( productServiceDTO.customerExists() && !productServiceDTO.divisionExists()) {
			assertEquals( new Long( productServiceDTO.getCustomerId() ), asset.getOwner().getId() );
		} 
		
		if( productServiceDTO.customerExists() && productServiceDTO.divisionExists() ) {
			assertEquals( new Long( productServiceDTO.getDivisionId() ), asset.getOwner().getId() );
		} 
		
		if( productServiceDTO.jobSiteExists() ) {
			assertEquals( new Long( productServiceDTO.getJobSiteId() ), asset.getOwner().getId() );
		} 
		
		if (!productServiceDTO.customerExists() && !productServiceDTO.divisionExists() && !productServiceDTO.jobSiteExists()) {
			assertEquals(asset.getOwner(), primaryOrg);
		}
		
		if( productServiceDTO.identifiedByExists() ) {
			assertEquals( new Long( productServiceDTO.getIdentifiedById() ), asset.getIdentifiedBy().getId() );
			assertEquals( new Long( asset.getIdentifiedBy().getOwner().getId() ), asset.getOwner().getId() );
		} else {
			assertNull( asset.getIdentifiedBy() );
		}
		
		if (productServiceDTO.getProductStatusId() > 0) {
			assertEquals(new Long(productServiceDTO.getProductStatusId()), asset.getAssetStatus().getUniqueID());
		} else {
			if (productServiceDTO.getProductStatusId() == ServiceDTOBeanConverterImpl.NULL_ID) {
				assertNull(asset.getAssetStatus());
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
		
		assertEquals(dto.getId(), customerOrg.getId());
		assertEquals(dto.getName(), customerName);
		assertEquals(dto.getParentId(), customerOrg.getParent().getId());		
	}
	
	@Test
	public void test_convert_customer_org_from_secondary_org() {
		String customerName = "Test Name";
		
		SecondaryOrg secondaryOrg = SecondaryOrgBuilder.aSecondaryOrg().build();
		
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().
									withName(customerName).withParent(secondaryOrg).build();
		
		CustomerOrgServiceDTO dto = converter.convert(customerOrg);
		
		assertEquals(dto.getId(), customerOrg.getId());
		assertEquals(dto.getName(), customerName);
		assertEquals(dto.getParentId(), secondaryOrg.getId());		
	}
	
	@Test
	public void test_convert_division_org() {
		PrimaryOrg primaryOrg = PrimaryOrgBuilder.aPrimaryOrg().build();
		CustomerOrg customerOrg = CustomerOrgBuilder.aCustomerOrg().withParent(primaryOrg).build();
		DivisionOrg divisionOrg = DivisionOrgBuilder.aDivisionOrg().withCustomerOrg(customerOrg).build();
		
		DivisionOrgServiceDTO dto = converter.convert(divisionOrg);
		
		assertEquals(dto.getId(), divisionOrg.getId());
		assertEquals(dto.getName(), divisionOrg.getName());
		assertEquals(dto.getParentId(), customerOrg.getId());
	}
	
	@Test
	public void test_convert_secondary_org() {
		SecondaryOrg secondaryOrg = SecondaryOrgBuilder.aSecondaryOrg().build();
		
		InternalOrgServiceDTO dto = converter.convert(secondaryOrg);
		
		assertEquals(dto.getId(), secondaryOrg.getId());
		assertEquals(dto.getName(), secondaryOrg.getName());
	}
	
	@Test
	public void test_setup_data_last_mod_dates() {
		SetupDataLastModDates model = new SetupDataLastModDates();
		
		model.setAutoAttributes	(new Date(1245703228000L));
		model.setInspectionTypes(new Date(1245603228000L));
		model.setOwners			(new Date(1245503228000L));
		model.setAssetTypes(new Date(1245403228000L));
		model.setLocations		(new Date(1245403228000L));
		
		SetupDataLastModDatesServiceDTO dto = converter.convert(model);
		
		assertEquals(model.getAutoAttributes(), dto.getAutoAttributes());
		assertEquals(model.getInspectionTypes(), dto.getInspectionTypes());
		assertEquals(model.getOwners(), dto.getOwners());
		assertEquals(model.getAssetTypes(), dto.getProductTypes());
		assertEquals(model.getLocations(), dto.getLocations());
	}
}
