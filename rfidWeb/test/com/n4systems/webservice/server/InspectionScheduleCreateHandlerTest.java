package com.n4systems.webservice.server;

import static com.n4systems.model.builders.InspectionScheduleBuilder.*;
import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.ProductBuilder.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.model.product.ProductByMobileGuidLoader;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;


public class InspectionScheduleCreateHandlerTest {
	
	private Product product;
	private InspectionType inspectionType;
	private InspectionSchedule inspectionSchedule;
	private InspectionScheduleSaver saver;
	private ProductByMobileGuidLoader productByMobileGuidLoader;
	private FilteredIdLoader<Product> filteredProductLoader;
	private FilteredIdLoader<InspectionType> filteredInspectionTypeLoader;
	
	
	@Before
	public void setup() {
		product = aProduct().build();
		inspectionType = anInspectionType().build();
		inspectionSchedule = aScheduledInspectionSchedule().build();
	}
	
	@Test
	public void check_properly_setting_product_and_inspection_type() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = new InspectionScheduleServiceDTO();
		inspectionScheduleServiceDTO.setProductMobileGuid("productMobileGuid");
		inspectionScheduleServiceDTO.setProductId(product.getId());
		inspectionScheduleServiceDTO.setInspectionTypeId(inspectionType.getId());
		
		createMocks();

		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(productByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(inspectionSchedule, inspectionScheduleServiceDTO);
		
		assertEquals(product, inspectionSchedule.getProduct());
		assertEquals(inspectionType, inspectionSchedule.getInspectionType());
		
	}

	@Test
	public void create_new_inspection_schedule_event_with_product_created_on_mobile() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = createInspectionScheduleServiceDTO(-1L);
		
		createMocks();

		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(productByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(inspectionSchedule, inspectionScheduleServiceDTO);
		verify(productByMobileGuidLoader);
		verify(saver);
		
	}
	
	@Test
	public void create_new_inspection_schedule_event_with_existing_product() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = createInspectionScheduleServiceDTO(product.getId());
		
		createMocks();
		
		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(productByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(inspectionSchedule, inspectionScheduleServiceDTO);
		
		verify(filteredProductLoader);
		verify(saver);
		
	}

	
	@SuppressWarnings("unchecked")
	private void createMocks() {
		saver = createMock(InspectionScheduleSaver.class);
		expect(saver.saveOrUpdate(inspectionSchedule)).andReturn(inspectionSchedule);
		replay(saver);

		productByMobileGuidLoader = createMock(ProductByMobileGuidLoader.class);
		expect(productByMobileGuidLoader.setMobileGuid("productMobileGuid")).andReturn(productByMobileGuidLoader);
		expect(productByMobileGuidLoader.load()).andReturn(product);
		replay(productByMobileGuidLoader);

		filteredProductLoader = createMock(FilteredIdLoader.class);
		expect(filteredProductLoader.setId(product.getId())).andReturn(filteredProductLoader);
		expect(filteredProductLoader.load()).andReturn(product);
		replay(filteredProductLoader);

		filteredInspectionTypeLoader =  createMock(FilteredIdLoader.class);
		expect(filteredInspectionTypeLoader.setId(anyLong())).andReturn(filteredInspectionTypeLoader);
		expect(filteredInspectionTypeLoader.load()).andReturn(inspectionType);
		replay(filteredInspectionTypeLoader);
	}

	
	private InspectionScheduleServiceDTO createInspectionScheduleServiceDTO(long productId) {
		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = new InspectionScheduleServiceDTO();
		inspectionScheduleServiceDTO.setProductMobileGuid("productMobileGuid");
		inspectionScheduleServiceDTO.setProductId(productId);
		inspectionScheduleServiceDTO.setInspectionTypeId(inspectionType.getId());
		
		return inspectionScheduleServiceDTO;
	}
	
	
	
}
