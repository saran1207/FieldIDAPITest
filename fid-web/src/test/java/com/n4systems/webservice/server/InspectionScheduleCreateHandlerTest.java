package com.n4systems.webservice.server;

import static com.n4systems.model.builders.InspectionScheduleBuilder.*;
import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.EventSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectionschedule.InspectionScheduleSaver;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;


public class InspectionScheduleCreateHandlerTest {
	
	private Asset asset;
	private InspectionType inspectionType;
	private EventSchedule eventSchedule;
	private InspectionScheduleSaver saver;
	private AssetByMobileGuidLoader assetByMobileGuidLoader;
	private FilteredIdLoader<Asset> filteredProductLoader;
	private FilteredIdLoader<InspectionType> filteredInspectionTypeLoader;
	
	
	@Before
	public void setup() {
		asset = anAsset().build();
		inspectionType = anInspectionType().build();
		eventSchedule = aScheduledInspectionSchedule().build();
	}
	
	@Test
	public void check_properly_setting_product_and_inspection_type() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = new InspectionScheduleServiceDTO();
		inspectionScheduleServiceDTO.setProductMobileGuid("productMobileGuid");
		inspectionScheduleServiceDTO.setProductId(asset.getId());
		inspectionScheduleServiceDTO.setInspectionTypeId(inspectionType.getId());
		
		createMocks();

		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(assetByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(eventSchedule, inspectionScheduleServiceDTO);
		
		assertEquals(asset, eventSchedule.getAsset());
		assertEquals(inspectionType, eventSchedule.getInspectionType());
		
	}

	@Test
	public void create_new_inspection_schedule_event_with_product_created_on_mobile() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = createInspectionScheduleServiceDTO(-1L);
		
		createMocks();

		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(assetByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(eventSchedule, inspectionScheduleServiceDTO);
		verify(assetByMobileGuidLoader);
		verify(saver);
		
	}
	
	@Test
	public void create_new_inspection_schedule_event_with_existing_product() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = createInspectionScheduleServiceDTO(asset.getId());
		
		createMocks();
		
		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(assetByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(eventSchedule, inspectionScheduleServiceDTO);
		
		verify(filteredProductLoader);
		verify(saver);
		
	}

	
	@SuppressWarnings("unchecked")
	private void createMocks() {
		saver = createMock(InspectionScheduleSaver.class);
		expect(saver.saveOrUpdate(eventSchedule)).andReturn(eventSchedule);
		replay(saver);

		assetByMobileGuidLoader = createMock(AssetByMobileGuidLoader.class);
		expect(assetByMobileGuidLoader.setMobileGuid("productMobileGuid")).andReturn(assetByMobileGuidLoader);
		expect(assetByMobileGuidLoader.load()).andReturn(asset);
		replay(assetByMobileGuidLoader);

		filteredProductLoader = createMock(FilteredIdLoader.class);
		expect(filteredProductLoader.setId(asset.getId())).andReturn(filteredProductLoader);
		expect(filteredProductLoader.load()).andReturn(asset);
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
