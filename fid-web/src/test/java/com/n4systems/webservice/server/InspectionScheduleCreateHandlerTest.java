package com.n4systems.webservice.server;

import static com.n4systems.model.builders.EventBuilder.anOpenEvent;
import static com.n4systems.model.builders.EventTypeBuilder.*;
import static com.n4systems.model.builders.AssetBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.asset.AssetByMobileGuidLoader;
import com.n4systems.model.event.SimpleEventSaver;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;


public class InspectionScheduleCreateHandlerTest {
	
	private Asset asset;
	private EventType eventType;
	private Event openEvent;
	private SimpleEventSaver saver;
	private AssetByMobileGuidLoader assetByMobileGuidLoader;
	private FilteredIdLoader<Asset> filteredProductLoader;
	private FilteredIdLoader<EventType> filteredInspectionTypeLoader;
	
	
	@Before
	public void setup() {
		asset = anAsset().build();
		eventType = anEventType().build();
		openEvent = anOpenEvent().build();
	}
	
	@Test
	public void check_properly_setting_product_and_inspection_type() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = new InspectionScheduleServiceDTO();
		inspectionScheduleServiceDTO.setProductMobileGuid("productMobileGuid");
		inspectionScheduleServiceDTO.setProductId(asset.getId());
		inspectionScheduleServiceDTO.setInspectionTypeId(eventType.getId());
		
		createMocks();

		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(assetByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(openEvent, inspectionScheduleServiceDTO);
		
		assertEquals(asset, openEvent.getAsset());
		assertEquals(eventType, openEvent.getEventType());
		
	}

	@Test
	public void create_new_inspection_schedule_event_with_product_created_on_mobile() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = createInspectionScheduleServiceDTO(-1L);
		
		createMocks();

		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(assetByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(openEvent, inspectionScheduleServiceDTO);
		verify(assetByMobileGuidLoader);
		verify(saver);
		
	}
	
	@Test
	public void create_new_inspection_schedule_event_with_existing_product() throws Exception {

		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = createInspectionScheduleServiceDTO(asset.getId());
		
		createMocks();
		
		InspectionScheduleCreateHandler sut = new InspectionScheduleCreateHandler(assetByMobileGuidLoader,
														filteredProductLoader, filteredInspectionTypeLoader, saver);
		
		sut.createNewInspectionSchedule(openEvent, inspectionScheduleServiceDTO);
		
		verify(filteredProductLoader);
		verify(saver);
		
	}

	
	@SuppressWarnings("unchecked")
	private void createMocks() {
		saver = createMock(SimpleEventSaver.class);
		expect(saver.saveOrUpdate(openEvent)).andReturn(openEvent);
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
		expect(filteredInspectionTypeLoader.load()).andReturn(eventType);
		replay(filteredInspectionTypeLoader);
	}

	
	private InspectionScheduleServiceDTO createInspectionScheduleServiceDTO(long productId) {
		InspectionScheduleServiceDTO inspectionScheduleServiceDTO = new InspectionScheduleServiceDTO();
		inspectionScheduleServiceDTO.setProductMobileGuid("productMobileGuid");
		inspectionScheduleServiceDTO.setProductId(productId);
		inspectionScheduleServiceDTO.setInspectionTypeId(eventType.getId());
		
		return inspectionScheduleServiceDTO;
	}
	
	
	
}
