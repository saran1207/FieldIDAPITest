package com.n4systems.model.eventtype;

import static org.junit.Assert.*;

import com.n4systems.model.builders.EventTypeBuilder;
import org.easymock.EasyMock;
import org.junit.Test;

import com.n4systems.model.EventType;
import com.n4systems.model.api.Cleaner;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class EventTypeCopierTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_copy_process() {
		Cleaner<EventType> typeCleaner = EasyMock.createMock(Cleaner.class);
		FilteredIdLoader<EventType> typeLoader = EasyMock.createMock(FilteredIdLoader.class);
		EventTypeSaver typeSaver = EasyMock.createMock(EventTypeSaver.class);
		EventTypeUniqueNameLoader typeNameLoader = EasyMock.createMock(EventTypeUniqueNameLoader.class);
		
		Long typeId = 42L;
		String newName = "new name";
		EventType fromType = EventTypeBuilder.anEventType().build();
		
		EventTypeCopier copier = new EventTypeCopier(typeCleaner, typeLoader, typeSaver, typeNameLoader);
		
		EasyMock.expect(typeLoader.setId(typeId)).andReturn(typeLoader);
		EasyMock.expect(typeLoader.setPostFetchFields("eventForm.sections", "supportedProofTests", "infoFieldNames")).andReturn(typeLoader);
		EasyMock.expect(typeLoader.load()).andReturn(fromType);
		
		typeCleaner.clean(fromType);
		
		EasyMock.expect(typeNameLoader.setName(fromType.getName())).andReturn(typeNameLoader);
		EasyMock.expect(typeNameLoader.load()).andReturn(newName);
		
		typeSaver.save(fromType);
		
		EasyMock.replay(typeCleaner);
		EasyMock.replay(typeLoader);
		EasyMock.replay(typeSaver);
		EasyMock.replay(typeNameLoader);
		
		EventType copiedType = copier.copy(typeId);
		
		assertEquals(newName, copiedType.getName());
		
		EasyMock.verify(typeCleaner);
		EasyMock.verify(typeLoader);
		EasyMock.verify(typeSaver);
		EasyMock.verify(typeNameLoader);
		
	}
	
}
