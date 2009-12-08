package com.n4systems.model.inspectiontype;

import static org.junit.Assert.*;

import org.easymock.classextension.EasyMock;
import org.junit.Test;

import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.builders.InspectionTypeBuilder;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class InspectionTypeCopierTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test_copy_process() {
		Cleaner<InspectionType> typeCleaner = EasyMock.createMock(Cleaner.class);
		FilteredIdLoader<InspectionType> typeLoader = EasyMock.createMock(FilteredIdLoader.class);
		InspectionTypeSaver typeSaver = EasyMock.createMock(InspectionTypeSaver.class);
		InspectionTypeUniqueNameLoader typeNameLoader = EasyMock.createMock(InspectionTypeUniqueNameLoader.class);
		
		Long typeId = 42L;
		String newName = "new name";
		InspectionType fromType = InspectionTypeBuilder.anInspectionType().build();
		
		InspectionTypeCopier copier = new InspectionTypeCopier(typeCleaner, typeLoader, typeSaver, typeNameLoader);
		
		EasyMock.expect(typeLoader.setId(typeId)).andReturn(typeLoader);
		EasyMock.expect(typeLoader.setPostFetchFields("sections", "supportedProofTests", "infoFieldNames")).andReturn(typeLoader);
		EasyMock.expect(typeLoader.load()).andReturn(fromType);
		
		typeCleaner.clean(fromType);
		
		EasyMock.expect(typeNameLoader.setName(fromType.getName())).andReturn(typeNameLoader);
		EasyMock.expect(typeNameLoader.load()).andReturn(newName);
		
		typeSaver.save(fromType);
		
		EasyMock.replay(typeCleaner);
		EasyMock.replay(typeLoader);
		EasyMock.replay(typeSaver);
		EasyMock.replay(typeNameLoader);
		
		InspectionType copiedType = copier.copy(typeId);
		
		assertEquals(newName, copiedType.getName());
		
		EasyMock.verify(typeCleaner);
		EasyMock.verify(typeLoader);
		EasyMock.verify(typeSaver);
		EasyMock.verify(typeNameLoader);
		
	}
	
}
